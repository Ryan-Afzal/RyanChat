package com.ryanafzal.io.chat.core.resources.thread;

import com.ryanafzal.io.chat.core.client.Client;

import javafx.concurrent.Task;

public class PingThread extends Task<Void> {

	public static final double THRESHHOLD = 5000;
	
	private Client client;
	
	
	
	/**
	 * How long since the last ping, in milliseconds
	 */
	private double lastPing;

	public PingThread(Client client) {
		this.client = client;
	}

	@Override
	public Void call() {
		while (this.client.isClientRunning() && !this.isCancelled()) {
			if (this.lastPing > PingThread.THRESHHOLD) {
				this.client.disconnected();
			}
		}
		
		return null;
	}
	
	public synchronized void updatePing() {
		this.lastPing = 0.0;
	}

}
