package com.ryanafzal.io.chat.core.resources.command.runnable;

import java.io.IOException;

import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.server.Connection;

public class DisconnectCommand implements RunnableCommand {
	
	@Override
	public void run(Object input) {
		Connection connection = (Connection) input;
		
		try {
			connection.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Type getType() {
		return Type.CONNECTION;
	}

}
