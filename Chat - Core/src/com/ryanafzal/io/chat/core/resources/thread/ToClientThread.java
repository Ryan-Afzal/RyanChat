package com.ryanafzal.io.chat.core.resources.thread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.server.Server;

import javafx.concurrent.Task;

public class ToClientThread extends Task<Void> {

	private Socket socket;
	private Server server;
	private LinkedList<Packet> packetsToSend;
	
	public ToClientThread(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		this.packetsToSend = new LinkedList<Packet>();
	}

	@Override
	public Void call() {
		try (ObjectOutputStream toClientStream = new ObjectOutputStream(this.socket.getOutputStream())) {
			toClientStream.flush();
			
			while (!this.socket.isClosed() && this.server.isServerRunning() && !this.isCancelled()) {
				if (!this.packetsToSend.isEmpty()) {
					Packet nextSend;
					
                    synchronized (this.packetsToSend) {
                        nextSend = this.packetsToSend.removeLast();
                    }
                    
                    toClientStream.writeObject(nextSend);
                    toClientStream.flush();
				}
			}
			
			toClientStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public synchronized void addPacket(Packet packet) {
		this.packetsToSend.push(packet);
	}

}
