package com.ryanafzal.io.chat.core.resources.command.runnable;

import com.ryanafzal.io.chat.core.client.Client;
import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class UpdateCachedGroupPermissionsCommand implements RunnableCommand {
	
	private static final long serialVersionUID = 8047156003099194491L;
	
	public final long GROUPID;
	public final Level NEWLEVEL;
	
	public UpdateCachedGroupPermissionsCommand(long groupID, Level newLevel) {
		this.GROUPID = groupID;
		this.NEWLEVEL = newLevel;
	}
	
	@Override
	public void run(Object input) {
		Client client = (Client) input;
		client.getUser().setPermissionLevel(this.GROUPID, this.NEWLEVEL);
		client.refreshUserDataPane();
	}

	@Override
	public Type getType() {
		return Type.CLIENT;
	}

}
