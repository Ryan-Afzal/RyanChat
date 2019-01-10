package com.ryanafzal.io.chat.core.resources.sendable;

public class PacketMessage extends PacketContents {
	
	private static final long serialVersionUID = 1L;
	
	public final String MESSAGE;
	
	public PacketMessage(String message) {
		this.MESSAGE = message;
	}

}
