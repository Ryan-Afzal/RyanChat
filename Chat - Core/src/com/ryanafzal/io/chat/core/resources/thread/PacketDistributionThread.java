package com.ryanafzal.io.chat.core.resources.thread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData;
import com.ryanafzal.io.chat.core.resources.user.groups.BaseGroup;
import com.ryanafzal.io.chat.core.server.Connection;
import com.ryanafzal.io.chat.core.server.Server;

import javafx.concurrent.Task;

public class PacketDistributionThread extends Task<Void> {
	
	private Server server;
	
	private LinkedList<Packet> queue;
	
	public PacketDistributionThread(Server server) {
		this.server = server;
	}
	
	@Override
	public Void call() throws Exception {
		while (this.server.isServerRunning() && !this.isCancelled()) {
			if (!this.queue.isEmpty()) {
				this.distributePacket(this.queue.removeLast());
			}
		}
		
		return null;
	}
	
	public synchronized void queuePacket(Packet packet) {
		this.queue.addFirst(packet);
	}
	
	private void distributePacket(Packet packet) {
		long address = packet.getPacketData().ADDRESS;
		if (packet.getPacketData().ADDRESSTYPE == PacketData.AddressType.GROUP) {
			BaseGroup g = this.server.getGroupByID(address);
			HashSet<Connection> connections = new HashSet<Connection>();
			
			for (Long l : g.getUsersAtRank(packet.getPacketData().LEVEL)) {
				connections.add(this.server.getConnectionByUserID(l));
			}
			
			this.distributePacket(packet, connections);
		} else if (packet.getPacketData().ADDRESSTYPE == PacketData.AddressType.INDIVIDUAL) {
			ArrayList<Connection> connections = new ArrayList<Connection>();
			connections.add(this.server.getConnectionByUserID(address));
			this.distributePacket(packet, connections);
		}
	}
	
	private void distributePacket(Packet packet, Iterable<Connection> recipients) {
		for (Connection c : recipients) {
			c.queuePacket(packet);
		}
	}

}
