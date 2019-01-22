package com.ryanafzal.io.chat.core.resources.command.runnable;

import com.ryanafzal.io.chat.core.client.Client;
import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;

public class AlertCommand extends RunnableCommand {
	
	private static final long serialVersionUID = -2824804324473133917L;
	
	private final String USERNAME;
	private final String MESSAGE;
	
	public AlertCommand(String username, String message) {
		this.USERNAME = username;
		this.MESSAGE = message;
	}
	
	@Override
	public void run(Object input) {
		Client client = (Client) input;
		String message = "["
				+ USERNAME
				+ "]: "
				+ MESSAGE;
		
		throw new UnsupportedOperationException();//TODO
	}

	@Override
	public Type getType() {
		return Type.CLIENT;
	}

}
