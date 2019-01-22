package com.ryanafzal.io.chat.core.resources.command.runnable;

import java.io.IOException;

import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.sendable.PacketContents;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData.AddressType;
import com.ryanafzal.io.chat.core.resources.sendable.PacketMessage;
import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;
import com.ryanafzal.io.chat.core.server.Connection;
import com.ryanafzal.io.chat.core.server.Server;

public class DisconnectCommand extends RunnableCommand {
	
	private static final long serialVersionUID = -4000097812926981337L;

	@Override
	public void run(Object input) {
		Connection connection = (Connection) input;
		Server server = connection.server;
		
		try {
			connection.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PacketData data = new PacketData(User.SERVER.getID(), AddressType.GROUP, Server.GLOBAL_GROUP_ID, Level.USER);
		PacketContents contents = new PacketMessage(User.SERVER.getName(), connection.getUser().getName() + " has disconnected.", Level.SERVER);
		Packet packet = new Packet(contents, data);
		server.enqueuePacket(packet);
	}

	@Override
	public Type getType() {
		return Type.CONNECTION;
	}

}
