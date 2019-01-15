package com.ryanafzal.io.chat.core.resources.command.runnable;

import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class ChangeUserPermissionLevelCommand implements RunnableCommand {
	
	private static final long serialVersionUID = -8701721967048667160L;
	
	public final long USERID;
	public final Level NEWLEVEL;
	
	public ChangeUserPermissionLevelCommand(long userID, Level newLevel) {
		this.USERID = userID;
		this.NEWLEVEL = newLevel;
	}

	@Override
	public void run(Object input) {
		// TODO Auto-generated method stub

	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
