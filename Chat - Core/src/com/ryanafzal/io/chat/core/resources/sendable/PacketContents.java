package com.ryanafzal.io.chat.core.resources.sendable;

import java.io.Serializable;
/**
 * Superclass of all 'Packet Contents'
 * @author s-afzalr
 * 
 * PacketMessage    : Message
 * PacketLogin      : Sent to server when the client connects
 * PacketUserData   : Sent to client after PacketLogin is recieved
 * PacketDisconnect : Sent to server when the client disconnects
 * 
 */
@SuppressWarnings("serial")
public abstract class PacketContents implements Serializable {

	public PacketContents() {
		//TODO
	}

}
