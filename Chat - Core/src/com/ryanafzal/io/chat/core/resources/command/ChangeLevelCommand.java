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

public class ChangeLevelCommand extends Command {

	public ChangeLevelCommand(CommandRegistry registry) {
		super(registry, new String[] {"changerank", "changelevel"}, "Promote the specified user to the specified level", Level.MODERATOR);
	}

	@Override
	public void run(CommandInfo info, Iterable<?> args) {
		Iterator<?> it = args.iterator();
		String target = (String) it.next();
		Level level = Level.getLevelFromString((String) it.next());
		
		if (info.CALLERLEVEL.compareTo(level) > 0) {
			PacketData data = new PacketData(info.CALLERID, AddressType.SERVER, User.SERVER.getID(), Level.SERVER);
			PacketCommand contents = new PacketCommand(new ChangeUserPermissionLevelCommand(target, info.CURRENTGROUP, level));
			Packet packet = new Packet(contents, data);
			((ClientGUI) this.registry.app).client.queuePacket(packet);
		} else {
			//TODO
		}
	}

}
