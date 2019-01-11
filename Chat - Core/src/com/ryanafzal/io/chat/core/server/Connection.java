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
	
	public Connection(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		this.toClient = new ToClientThread(socket, server);
		this.fromClient = new FromClientThread(socket, server, this);
        Thread clientInThread = new Thread(toClient);
        Thread clientOutThread = new Thread(fromClient);
        clientInThread.start();
        clientOutThread.start();
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
		this.toClient.cancel();
		this.fromClient.cancel();
		this.toClient = null;
		this.fromClient = null;
		this.socket.close();
		this.server.destroyConnection(this);
	}

}