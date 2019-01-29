package com.ryanafzal.io.chat.core.resources.thread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;

import com.ryanafzal.io.chat.core.client.Client;
import com.ryanafzal.io.chat.core.resources.sendable.Packet;

import javafx.concurrent.Task;

public class ToServerThread extends Task<Void> {
	
	private Socket socket;
	private Client client;
	private LinkedList<Packet> packetsToSend;
	
	public ToServerThread(Socket socket, Client client) {
		this.socket = socket;
		this.client = client;
		this.packetsToSend = new LinkedList<Packet>();
	}

	@Override
	public Void call() {
		try (ObjectOutputStream toServerStream = new ObjectOutputStream(this.socket.getOutputStream())) {
			toServerStream.flush();
			
			while (!this.socket.isClosed() && this.client.isClientRunning() && !this.isCancelled()) {
				if (!this.packetsToSend.isEmpty()) {
					Packet nextSend;
					
                    synchronized (this.packetsToSend) {
                        nextSend = this.packetsToSend.removeLast();
                    }
                    
                    toServerStream.writeObject(nextSend);
                    toServerStream.flush();
				}
			}
			
			toServerStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public synchronized void addPacket(Packet packet) {
		this.packetsToSend.push(packet);
	}

}
