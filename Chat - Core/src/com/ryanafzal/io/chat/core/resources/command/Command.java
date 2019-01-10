package com.ryanafzal.io.chat.core.resources.command;

import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public abstract class Command {
	
	public static final char COMMAND_CHARACTER = '\\';

	protected CommandRegistry registry;
	public final String[] aliases;
	public final String description;
	public final Level level;
	
	public Command(CommandRegistry registry, String[] aliases, String description, Level level) {
		registry.addCommand(this);
		this.registry = registry;
		this.aliases = aliases;
		this.description = description;
		this.level = level;
	}
	
	public abstract void run(Iterable<?> args);

}
