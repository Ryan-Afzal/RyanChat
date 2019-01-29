package com.ryanafzal.io.chat.core.resources.sendable;

import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public abstract class PacketDataFactory {

	public static PacketData getToServerPacketData(long senderID) {
		return new PacketData(senderID, PacketData.AddressType.SERVER, User.SERVER.getID(), Level.SERVER);
	}

}
