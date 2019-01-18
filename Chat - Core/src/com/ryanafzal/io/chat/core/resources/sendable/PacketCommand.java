package com.ryanafzal.io.chat.core.resources.sendable;

import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;

public class PacketCommand extends PacketContents {
	
	private static final long serialVersionUID = -516565125788760887L;
	
	public final RunnableCommand COMMAND;
	
	public PacketCommand(RunnableCommand command) {
		this.COMMAND = command;
	}

}
