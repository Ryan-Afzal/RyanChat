package com.ryanafzal.io.chat.core.resources.user.groups;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class Group {
	
	private HashMap<Long, Level> permissionMap;
	private User owner;
	private PriorityQueue<User> chainOfCommand;
	
	//Info
	private String name;
	public final int GROUPID;
	
	private boolean inactive;
	
	public Group(User owner, String name, int groupID) {
		this.name = name;
		this.GROUPID = groupID;
		
		this.permissionMap = new HashMap<Long, Level>();
		this.chainOfCommand = new PriorityQueue<User>();
		
		this.owner = owner;
		this.permissionMap.put(owner.getID(), Level.ADMIN);
		
		this.inactive = false;
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addUser(User user) {
		this.permissionMap.put(user.getID(), user.getPermissionLevel());
	}
	
	public void removeUser(long ID) {
		this.permissionMap.remove(ID);
		
		if (ID == this.owner.getID()) {
			this.owner = this.chainOfCommand.poll();
			
			if (this.owner == null) {
				this.destroy();
			}
		}
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
	
	private void destroy() {
		this.permissionMap = null;
		this.chainOfCommand = null;
		this.inactive = true;
	}
	
}
