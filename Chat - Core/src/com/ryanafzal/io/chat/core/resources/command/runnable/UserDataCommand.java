package com.ryanafzal.io.chat.core.resources.command.runnable;

import com.ryanafzal.io.chat.core.client.Client;
import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.resources.user.User;

public class UserDataCommand implements RunnableCommand {
	
	private final User USER;
	
	public UserDataCommand(User user) {
		this.USER = user;
	}

	@Override
	public void run(Object input) {
		Client client = (Client) input;
		
		client.setUser(USER);
		client.setReady(true);
		
		client.printWelcomeMessage();
	}

	@Override
	public Type getType() {
		return Type.CLIENT;
	}

}
