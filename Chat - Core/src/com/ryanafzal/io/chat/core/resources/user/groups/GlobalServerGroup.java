package com.ryanafzal.io.chat.core.resources.user.groups;

import com.ryanafzal.io.chat.core.server.Server;

public class GlobalServerGroup extends BaseGroup {

	public GlobalServerGroup() {
		super("Global Chat", Server.GLOBAL_GROUP_ID);
	}
	
	@Override
	protected void destroy() {
		throw new UnsupportedOperationException("Cannot destroy global server group.");
	}
	
	@Override
	public boolean inactive() {
		return false;
	}

}
