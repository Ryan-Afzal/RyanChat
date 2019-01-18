package com.ryanafzal.io.chat.core.resources.command;

import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class PingCommand extends Command {

	public PingCommand(CommandRegistry registry) {
		super(registry, new String[] {"alert", "ping"}, "Send an alert to the specified user", Level.USER);
	}

	@Override
	public void run(CommandInfo info, Iterable<?> args) {
		//TODO
	}

}
