package com.ryanafzal.io.chat.core.resources.command;

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
	public void run(Iterable<?> args) {
		// TODO Auto-generated method stub
	}

}
