package com.ryanafzal.io.chat.core.resources.user.groups;

import java.util.HashMap;
import java.util.stream.Collectors;

import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public abstract class BaseGroup {
	
	private HashMap<Long, Level> permissionMap;
	
	private boolean inactive;
	
	private String name;
	public final long GROUPID;
	
	public BaseGroup(String name, long groupID) {
		this.name = name;
		this.GROUPID = groupID;
		
		this.permissionMap = new HashMap<Long, Level>();
		this.inactive = false;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addUser(User user) {
		this.addUser(user, Level.USER);
	}
	
	public void addUser(User user, Level level) {
		user.setPermissionLevel(this.GROUPID, level);
		this.permissionMap.put(user.getID(), level);
	}
	
	public void removeUser(long ID) {
		this.permissionMap.remove(ID);
	}
	
	public void changePermission(long ID, Level rank) {
		if (this.permissionMap.containsKey(ID)) {
			this.permissionMap.put(ID, rank);
		}
	}
	
	public Iterable<Long> getUsersAtRank(Level rank) {
		return this.permissionMap.keySet()
				.stream()
				.filter(id -> permissionMap.get(id).compareTo(rank) >= 0)
				.collect(Collectors.toList());
	}
	
	protected void destroy() {
		this.inactive = true;
	}
	
	public boolean inactive() {
		return this.inactive;
	}

}
