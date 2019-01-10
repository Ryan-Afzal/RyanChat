package com.ryanafzal.io.chat.core.resources.sendable;

import java.io.Serializable;

/**
 * A data packet. Contains packet data and contents.
 * @author s-afzalr
 * 
 */
public class Packet implements Serializable {

	private static final long serialVersionUID = -4560219672036842158L;
	private final PacketContents CONTENTS;
	private final PacketData DATA;
	
	public Packet(PacketContents contents, PacketData data) {
		this.CONTENTS = contents;
		this.DATA = data;
	}
	
	public PacketContents getPacketContents() {
		return this.CONTENTS;
	}
	
	public PacketData getPacketData() {
		return this.DATA;
	}

}
