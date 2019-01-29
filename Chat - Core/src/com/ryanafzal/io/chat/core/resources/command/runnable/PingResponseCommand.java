package com.ryanafzal.io.chat.core.resources.command.runnable;

import com.ryanafzal.io.chat.core.client.Client;
import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;

public class PingResponseCommand extends RunnableCommand {
	
	private static final long serialVersionUID = -3842360561257133869L;

	public PingResponseCommand() {}

	@Override
	public void run(Object input) {
		Client client = (Client) input;
		client.updatePing();
	}

	@Override
	public Type getType() {
		return Type.CLIENT;
	}

}
