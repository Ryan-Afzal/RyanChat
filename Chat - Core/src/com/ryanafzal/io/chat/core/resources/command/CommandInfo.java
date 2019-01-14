package com.ryanafzal.io.chat.core.resources.command;

import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public final class CommandInfo {
	
	public final long CALLERID;
	public final Level CALLERLEVEL;
	public final long CURRENTGROUP;
	
	public CommandInfo(long callerID, Level callerLevel, long currentGroup) {
		this.CALLERID = callerID;
		this.CALLERLEVEL = callerLevel;
		this.CURRENTGROUP = currentGroup;
	}

}
