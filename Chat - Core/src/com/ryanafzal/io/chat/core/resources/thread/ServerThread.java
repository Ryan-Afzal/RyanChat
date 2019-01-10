package com.ryanafzal.io.chat.core.resources.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.ryanafzal.io.chat.core.server.Server;

/**
 * Thread which manages the {@code serverSocket} of its parent server. 
 * It accepts clients and adds their connections to the server.
 * @author s-afzalr
 *
 */
public class ServerThread implements Runnable {
	
	private Server server;
	private ServerSocket serverSocket;
	
	public ServerThread(Server server, ServerSocket serverSocket) {
		this.server = server;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		while (this.server.isRunning()) {
			try {
				//Accept a connection
				Socket socket = this.serverSocket.accept();
				
				//Add the connection to the server.
				this.server.addConnection(socket);
				
				//Output a status message
				this.server.outputCommandMessage("ACCEPTED CLIENT AT: " + socket.getRemoteSocketAddress());
			} catch (IOException ex) {
				ex.printStackTrace();
				this.server.outputErrorMessage("ACCEPT FAILED ON PORT: " + this.server.PORT);
			}
		}
	}

}
