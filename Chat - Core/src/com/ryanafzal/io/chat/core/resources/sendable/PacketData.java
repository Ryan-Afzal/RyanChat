package com.ryanafzal.io.chat.core.resources.sendable;

import java.io.Serializable;
import java.time.*;
import java.util.UUID;

import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class PacketData implements Serializable {
	
	private static final long serialVersionUID = -4874063942022551267L;
	
	public final String ID;
	public final LocalDateTime TIMESTAMP;
	public final long SENDERID;
	public final AddressType ADDRESSTYPE;
	public final long ADDRESS;
	public final Level LEVEL;
	
	/**
	 * 
	 * @param senderid The User ID of the user who sent the packet
	 * @param address The ID of the recipient. This can be either a User ID, the Server ID, or a Group ID.
	 * @param level The required permission level in order to recieve the packet.
	 */
	public PacketData(long senderid, AddressType addresstype, long address, Level level) {
		this.ID = UUID.randomUUID().toString();
		this.TIMESTAMP = LocalDateTime.now();
		this.SENDERID = senderid;
		this.ADDRESSTYPE = addresstype;
		this.ADDRESS = address;
		this.LEVEL = level;
	}
	
	public enum AddressType {
		SERVER, GROUP, INDIVIDUAL
	}
	
}
