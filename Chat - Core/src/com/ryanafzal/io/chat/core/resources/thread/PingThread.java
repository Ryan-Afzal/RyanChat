package com.ryanafzal.io.chat.core.resources.thread;

import java.time.Duration;
import java.time.Instant;

import com.ryanafzal.io.chat.core.client.Client;

import javafx.concurrent.Task;

public class PingThread extends Task<Void> {

	/**
	 * The number of seconds without a ping signal until the thread notifies disconnection.
	 */
	public static final double THRESHHOLD = 30;
	
	private Client client;
	
	private Instant lastPingTime;

	public PingThread(Client client) {
		this.client = client;
	}

	@Override
	public Void call() {
		while (this.client.isClientRunning() && !this.isCancelled()) {
			if (Duration.between(this.lastPingTime, Instant.now()).getSeconds() > PingThread.THRESHHOLD) {
				this.client.disconnected();
			}
		}
		
		return null;
	}
	
	public synchronized void updatePing() {
		this.lastPingTime = Instant.now();
	}

}
