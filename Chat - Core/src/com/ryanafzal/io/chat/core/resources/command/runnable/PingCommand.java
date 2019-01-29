package com.ryanafzal.io.chat.core.resources.command.runnable;

import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.server.Connection;

public class PingCommand extends RunnableCommand {
	
	private static final long serialVersionUID = 6851474418828715275L;

	protected PingCommand() {}

	@Override
	public void run(Object input) {
		Connection connection = (Connection) input;
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Type getType() {
		return Type.CONNECTION;
	}

}
