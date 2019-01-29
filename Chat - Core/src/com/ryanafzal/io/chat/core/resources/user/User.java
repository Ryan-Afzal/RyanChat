package com.ryanafzal.io.chat.core.resources.user;

import java.io.Serializable;
import java.util.HashMap;

import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class User implements Serializable {
	
	private static final long serialVersionUID = 2123732792688009294L;
	
	public static final User GUEST = new User("GUEST", 1, 1);
	public static final User SERVER = new User("SERVER", 0, 0);
	
	private String name;
	private int password;
	private final long ID;
	private HashMap<Long, Level> groupPermissionLevels;
	
	public User(String name, int password, long ID) {
		this.name = name;
		this.password = password;
		this.ID = ID;
		this.groupPermissionLevels = new HashMap<Long, Level>();
	}
	
	public User(String name, String password, long ID) {
		this(name, password.hashCode(), ID);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPassword() {
		return this.password;
	}
	
	public void setPassword(int password) {
		this.password = password;
	}
	
	public long getID() {
		return this.ID;
	}

	public Level getPermissionLevel(long groupID) {
		return this.groupPermissionLevels.get(groupID);
	}
	
	public void setPermissionLevel(long groupID, Level level) {
		this.groupPermissionLevels.put(groupID, level);
	}

}
