package com.ryanafzal.io.chat.core.resources.command;

import java.util.Arrays;

import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class ListCommand extends Command {

	public ListCommand(CommandRegistry registry) {
		super(
				registry, 
				new String[] {"list", "help", "l"}, 
				"Lists all commands.", 
				Level.USER);
	}

	@Override
	public void run(CommandInfo info, Iterable<?> args) {
		this.registry.app.getPermissionRank();
		this.registry.app.outputCommandMessage("Commands available at rank: " + level);
		for (Command c : this.registry.getCommandsAtRank(level)) {
			this.registry.app.outputCommandMessage(""
					+ Arrays.toString(c.aliases)
					+ ": "
					+ c.description);
		}
	}

}
