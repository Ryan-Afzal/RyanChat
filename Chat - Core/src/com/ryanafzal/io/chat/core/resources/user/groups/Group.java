package com.ryanafzal.io.chat.core.resources.user.groups;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class Group extends BaseGroup {
	
	private PriorityQueue<User> chainOfCommand;
	private User owner;
	
	public Group(User owner, String name, long groupID) {
		super(name, groupID);
		
		this.chainOfCommand = new PriorityQueue<User>(2, new Comparator<User> () {
			@Override
			public int compare(User arg0, User arg1) {
				return arg0.getPermissionLevel(GROUPID).compareTo(arg1.getPermissionLevel(groupID));
			}
		});
		
		this.owner = owner;
		this.addUser(owner, Level.ADMIN);
	}
	
	@Override
	public void addUser(User user, Level level) {
		super.addUser(user, level);
		this.chainOfCommand.add(user);
	}
	
	@Override
	public void removeUser(long ID) {
		super.removeUser(ID);
		
		if (ID == this.owner.getID()) {
			this.owner = this.chainOfCommand.poll();
			
			if (this.owner == null) {
				this.destroy();
			} else {
				this.owner.setPermissionLevel(this.GROUPID, Level.ADMIN);
			}
		}
	}
	
	@Override
	public void changePermission(long ID, Level rank) {
		super.changePermission(ID, rank);
		//TODO
		
	}
	
	@Override
	protected void destroy() {
		super.destroy();
		this.chainOfCommand = null;
	}
	
}
