package com.ryanafzal.io.chat.core.resources.command.runnable;

import com.ryanafzal.io.chat.core.client.Client;
import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.resources.user.User;

public class UserDataCommand extends RunnableCommand {
	
	private static final long serialVersionUID = 6304972741634413731L;
	
	private final User USER;
	
	public UserDataCommand(User user) {
		this.USER = user;
	}
	
	@Override
	public void run(Object input) {
		Client client = (Client) input;
		
		if (client.getUser() == null) {
			client.connected();
		}
	}

	@Override
	public Type getType() {
		return Type.CLIENT;
	}

}
