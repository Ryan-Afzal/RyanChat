package com.ryanafzal.io.chat.core.resources.command;

import java.util.Iterator;

import com.ryanafzal.io.chat.core.client.ClientGUI;
import com.ryanafzal.io.chat.core.resources.command.runnable.ChangeUserPermissionLevelCommand;
import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.sendable.PacketCommand;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData.AddressType;
import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class PingCommand extends Command {

	public PingCommand(CommandRegistry registry) {
		super(registry, new String[] {"alert", "ping"}, "Send an alert to the specified user", Level.USER);
	}

	@Override
	public void run(CommandInfo info, Iterable<?> args) {
		/*Iterator<?> i = args.iterator();
		String username = (String) i.next();
		String message = (String) i.next();
		
		PacketData data = new PacketData(info.CALLERID, AddressType.SERVER, User.SERVER.getID(), Level.SERVER);
		PacketCommand contents = new PacketCommand(null);
		Packet packet = new Packet(contents, data);
		((ClientGUI) this.registry.app).client.queuePacket(packet);*/
		throw new UnsupportedOperationException();//TODO
	}

}
