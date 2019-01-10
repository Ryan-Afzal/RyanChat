package com.ryanafzal.io.chat.core.resources.sendable;

import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;

public class PacketCommand extends PacketContents {
	
	public final RunnableCommand COMMAND;
	
	public PacketCommand(RunnableCommand command) {
		this.COMMAND = command;
	}

}
