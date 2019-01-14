package com.ryanafzal.io.chat.core.resources.command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.ryanafzal.io.chat.core.resources.application.ApplicationWindow;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class CommandRegistry {
	
	protected ApplicationWindow app;
	private HashSet<Command> commands;
	
	public CommandRegistry(ApplicationWindow app) {
		this.app = app;
		this.commands = new HashSet<Command>();
		new ListCommand(this);
	}
	
	protected void addCommand(Command command) {
		this.commands.add(command);
	}
	
	public List<Command> getCommandsAtRank(Level level) {
		return this.commands
				.stream()
				.filter(command -> command.level.compareTo(level) <= 0)
				.collect(Collectors.toList());
	}
	
	public void runCommand(String name, CommandInfo info, Iterable<?> args, Level level) {
		Command c = this.getCommandsAtRank(level)
				.stream()
				.filter(command -> Arrays.asList(command.aliases).contains(name))
				.findFirst()
				.orElse(null);
		if (c == null) {
			throw new IllegalArgumentException("Command " + name + " does not exist or is inaccessible.");
		} else {
			c.run(info, args);
		}
	}

}
