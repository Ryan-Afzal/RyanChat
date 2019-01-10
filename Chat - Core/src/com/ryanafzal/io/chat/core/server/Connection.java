package com.ryanafzal.io.chat.core.server;

import java.io.IOException;
import java.net.Socket;

import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.thread.FromClientThread;
import com.ryanafzal.io.chat.core.resources.thread.ToClientThread;

/**
 * Represents a connection from the server to a client.
 * @author s-afzalr
 *
 */
public final class Connection {
	
	public final Server server;
	
	protected final Socket socket;
	protected ToClientThread toClient;
	protected FromClientThread fromClient;
	protected Thread toClientThread;
	protected Thread fromClientThread;
	
	public Connection(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		this.toClient = new ToClientThread(socket, server);
		this.fromClient = new FromClientThread(socket, server, this);
		this.toClientThread = new Thread(this.toClient);
		this.fromClientThread = new Thread(this.fromClient);
		this.toClientThread.start();
		this.fromClientThread.start();
	}
	
	public void queuePacket(Packet packet) {
		this.toClient.addPacket(packet);
	}
	
	/**
	 * Terminates the connection with the client.
	 * This will suspend all threads and close the socket.
	 * @throws IOException If the socket cannot be closed.
	 */
	public void destroy() throws IOException {
		this.server.destroyConnection(this);
		
		this.toClientThread.interrupt();
		this.fromClientThread.interrupt();
		this.toClient = null;
		this.fromClient = null;
		this.toClientThread = null;
		this.fromClientThread = null;
		this.socket.close();
	}

}