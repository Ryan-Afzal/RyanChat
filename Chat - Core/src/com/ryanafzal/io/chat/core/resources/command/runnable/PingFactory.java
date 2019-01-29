package com.ryanafzal.io.chat.core.resources.command.runnable;

import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.sendable.PacketCommand;
import com.ryanafzal.io.chat.core.resources.sendable.PacketDataFactory;

public abstract class PingFactory {

	public static Packet createPing(long id) {
		return new Packet(
				new PacketCommand(new PingCommand()), 
				PacketDataFactory.getToServerPacketData(id));
	}

}
