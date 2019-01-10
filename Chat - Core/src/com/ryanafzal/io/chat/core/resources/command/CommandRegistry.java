package com.ryanafzal.io.chat.core.resources.command;

import java.util.Arrays;
import java.util.HashSet;

import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class CommandRegistry {
	
	private HashSet<Command> commands;
	
	public CommandRegistry() {
		this.commands = new HashSet<Command>();
		new ListCommand(this);
	}
	
	protected void addCommand(Command command) {
		this.commands.add(command);
	}
	
	public void runCommand(String name, Iterable<?> args, Level level) {
		Command c = this.commands
				.stream()
				.filter(command -> command.level.compareTo(level) <= 0)
				.filter(command -> Arrays.asList(command.aliases).contains(name))
				.findFirst()
				.orElse(null);
		if (c == null) {
			throw new IllegalArgumentException("Command " + name + " does not exist or is inaccessible.");
		} else {
			c.run(args);
		}
	}

}
