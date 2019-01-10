package com.ryanafzal.io.chat.core.resources.user;

import java.io.Serializable;

import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class User implements Serializable, Comparable<User> {
	
	private static final long serialVersionUID = 2123732792688009294L;
	
	public static final User SERVER = new User("SERVER", 0, 0, Level.SERVER);
	
	private String name;
	private int password;
	private final long ID;
	private Level permissionLevel;
	
	public User(String name, int password, long ID, Level permissionLevel) {
		this.name = name;
		this.password = password;
		this.ID = ID;
		this.setPermissionLevel(permissionLevel);
	}
	
	public User(String name, String password, long ID, Level permissionLevel) {
		this(name, password.hashCode(), ID, permissionLevel);
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

	public Level getPermissionLevel() {
		return permissionLevel;
	}

	public void setPermissionLevel(Level permissionLevel) {
		this.permissionLevel = permissionLevel;
	}

	@Override
	public int compareTo(User arg0) {
		return this.permissionLevel.compareTo(arg0.permissionLevel);
	}

}
