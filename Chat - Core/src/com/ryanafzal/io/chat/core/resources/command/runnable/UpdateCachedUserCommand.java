package com.ryanafzal.io.chat.core.resources.command.runnable;

import com.ryanafzal.io.chat.core.client.Client;
import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.resources.user.User;

public class UpdateCachedUserCommand implements RunnableCommand {
	
	private static final long serialVersionUID = -3654202227708673478L;
	
	public final User USER;
	
	public UpdateCachedUserCommand(User user) {
		this.USER = user;
	}

	@Override
	public void run(Object input) {
		Client client = (Client) input;
		client.setUser(this.USER);
		client.refreshUserDataPane();
	}

	@Override
	public Type getType() {
		return Type.CLIENT;
	}

}
