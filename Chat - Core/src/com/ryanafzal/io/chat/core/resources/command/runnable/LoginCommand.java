package com.ryanafzal.io.chat.core.resources.command.runnable;

import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.sendable.PacketCommand;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData;
import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.UserNotFoundException;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;
import com.ryanafzal.io.chat.core.server.Connection;

public class LoginCommand implements RunnableCommand {
	
	private static final long serialVersionUID = 7838053858700842032L;
	
	private final String USERNAME;
	private final int PASSWORD;
	private final boolean REGISTER;
	
	public LoginCommand(String username, int password, boolean register) {
		this.USERNAME = username;
		this.PASSWORD = password;
		this.REGISTER = register;
	}
	
	@Override
	public void run(Object input) {
		Connection connection = (Connection) input;
		
		PacketData data = new PacketData(User.SERVER.getID(), PacketData.AddressType.SERVER, 0, Level.USER);
		
		PacketCommand contents;
		User u;
		try {
			u = connection.server.login(this.USERNAME, this.PASSWORD, this.REGISTER);
			contents = new PacketCommand(new UserDataCommand(u));
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		connection.server.inductConnection(u.getID(), connection);
		
		Packet packet = new Packet(contents, data);
		connection.queuePacket(packet);
	}

	@Override
	public Type getType() {
		return Type.CONNECTION;
	}

}
