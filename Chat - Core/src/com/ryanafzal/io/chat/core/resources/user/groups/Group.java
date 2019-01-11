package com.ryanafzal.io.chat.core.resources.user.groups;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class Group {
	
	private HashMap<Long, Level> permissionMap;
	private PriorityQueue<User> chainOfCommand;
	private User owner;
	
	//Info
	private String name;
	public final long GROUPID;
	
	private boolean inactive;
	
	public Group(User owner, String name, long groupID) {
		this.name = name;
		this.GROUPID = groupID;
		
		this.permissionMap = new HashMap<Long, Level>();
		this.chainOfCommand = new PriorityQueue<User>(2, new Comparator<User> () {
			@Override
			public int compare(User arg0, User arg1) {
				return arg0.getPermissionLevel(GROUPID).compareTo(arg1.getPermissionLevel(groupID));
			}
		});
		
		this.owner = owner;
		this.permissionMap.put(owner.getID(), Level.ADMIN);
		
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
		
		if (ID == this.owner.getID()) {
			this.owner = this.chainOfCommand.poll();
			
			if (this.owner == null) {
				this.destroy();
			} else {
				this.owner.setPermissionLevel(GROUPID, Level.ADMIN);
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
	
	public boolean inactive() {
		return this.inactive;
	}
	
}
