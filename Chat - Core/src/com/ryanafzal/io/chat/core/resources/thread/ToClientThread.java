package com.ryanafzal.io.chat.core.resources.thread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.server.Server;

public class ToClientThread implements Runnable {

	private Socket socket;
	private Server server;
	private LinkedList<Packet> packetsToSend;
	
	public ToClientThread(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		this.packetsToSend = new LinkedList<Packet>();
	}

	@Override
	public void run() {
		try (ObjectOutputStream toClientStream = new ObjectOutputStream(this.socket.getOutputStream())) {
			toClientStream.flush();
			
			while (!this.socket.isClosed() && this.server.isRunning()) {
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
	}
	
	public synchronized void addPacket(Packet packet) {
		this.packetsToSend.push(packet);
	}

}
