package com.ryanafzal.io.chat.core.resources.command;

import java.util.Iterator;

import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class ChangeLevelCommand extends Command {

	public ChangeLevelCommand(CommandRegistry registry) {
		super(registry, new String[] {"changerank", "changelevel"}, "Promote the specified user to the specified level", Level.MODERATOR);
	}

	@Override
	public void run(CommandInfo info, Iterable<?> args) {
		Iterator<?> it = args.iterator();
		User target = (User) it.next();
		Level level = (Level) it.next();
		
		//TODO
	}

}
