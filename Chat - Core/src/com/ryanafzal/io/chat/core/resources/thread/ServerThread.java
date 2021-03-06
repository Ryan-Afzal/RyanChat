package com.ryanafzal.io.chat.core.resources.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.ryanafzal.io.chat.core.server.Server;

import javafx.concurrent.Task;

/**
 * Thread which manages the {@code serverSocket} of its parent server. 
 * It accepts clients and adds their connections to the server.
 * @author s-afzalr
 *
 */
public class ServerThread extends Task<Void> {
	
	private Server server;
	private ServerSocket serverSocket;
	
	public ServerThread(Server server, ServerSocket serverSocket) {
		this.server = server;
		this.serverSocket = serverSocket;
	}

	@Override
	public Void call() {
		while (this.server.isServerRunning() && !this.isCancelled()) {
			try {
				Socket socket = this.serverSocket.accept();
				this.server.addConnection(socket);
				this.server.log("ACCEPTED CLIENT AT: " + socket.getRemoteSocketAddress());
			} catch (IOException ex) {
				ex.printStackTrace();
				this.server.logError("ACCEPT FAILED ON PORT: " + this.server.PORT);
			}
		}
		
		return null;
	}

}
