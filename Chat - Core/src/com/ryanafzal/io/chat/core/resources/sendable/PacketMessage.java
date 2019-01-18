package com.ryanafzal.io.chat.core.resources.sendable;

import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class PacketMessage extends PacketContents {
	
	private static final long serialVersionUID = 1L;
	
	public final String USERNAME;
	public final String MESSAGE;
	public final Level LEVEL;
	
	public PacketMessage(String username, String message, Level level) {
		this.USERNAME = username;
		this.MESSAGE = message;
		this.LEVEL = level;
	}

}
