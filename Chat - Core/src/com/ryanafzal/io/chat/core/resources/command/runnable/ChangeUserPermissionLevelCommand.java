package com.ryanafzal.io.chat.core.resources.command.runnable;

import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.sendable.PacketCommand;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData.AddressType;
import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;
import com.ryanafzal.io.chat.core.server.Server;

public class ChangeUserPermissionLevelCommand extends RunnableCommand {
	
	private static final long serialVersionUID = -8701721967048667160L;
	
	public final String USERNAME;
	public final long TARGETGROUP;
	public final Level NEWLEVEL;
	
	public ChangeUserPermissionLevelCommand(String userName, long targetGroupID, Level newLevel) {
		this.USERNAME = userName;
		this.TARGETGROUP = targetGroupID;
		this.NEWLEVEL = newLevel;
	}

	@Override
	public void run(Object input) {
		Server server = (Server) input;
		User target = server.getUserByName(this.USERNAME);
		
		server.changeGroupPermissions(this.TARGETGROUP, target.getID(), this.NEWLEVEL);
		
		PacketData data = new PacketData(User.SERVER.getID(), AddressType.INDIVIDUAL, target.getID(), Level.USER);
		PacketCommand contents = new PacketCommand(new UpdateCachedGroupPermissionsCommand(this.TARGETGROUP, this.NEWLEVEL));
		Packet packet = new Packet(contents, data);
		
		server.getConnectionByUserID(target.getID()).queuePacket(packet);
	}

	@Override
	public Type getType() {
		return Type.SERVER;
	}

}
