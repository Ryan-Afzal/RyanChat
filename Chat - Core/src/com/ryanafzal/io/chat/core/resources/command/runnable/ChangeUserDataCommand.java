package com.ryanafzal.io.chat.core.resources.command.runnable;

import java.util.HashMap;

import com.ryanafzal.io.chat.core.resources.command.RunnableCommand;
import com.ryanafzal.io.chat.core.resources.user.User;

/**
 * Updates data stored on the server. This can be permission levels, usernames, and more.
 * @author s-afzalr
 *
 */
public class ChangeUserDataCommand implements RunnableCommand {
	
	public final long USERID;
	public final long TARGETGROUPID;
	public final HashMap<String, ?> PROPERTIES;
	
	public ChangeUserDataCommand(long userid, long targetGroupID, HashMap<String, ?> properties) {
		this.USERID = userid;
		this.TARGETGROUPID = targetGroupID;
		this.PROPERTIES = properties;
	}
	
	public ChangeUserDataCommand(long userid, HashMap<String, ?> properties) {
		this(userid, -1, properties);
	}

	@Override
	public void run(Object input) {
		// TODO Auto-generated method stub

	}

	@Override
	public Type getType() {
		return Type.SERVER;
	}

}
