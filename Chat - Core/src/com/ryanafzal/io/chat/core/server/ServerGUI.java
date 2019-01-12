package com.ryanafzal.io.chat.core.server;

import com.ryanafzal.io.chat.core.resources.application.ApplicationWindow;
import com.ryanafzal.io.chat.core.resources.misc.Speed;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

public class ServerGUI extends ApplicationWindow {
	
	private Server server;
	
	public ServerGUI() {
		this.server = new Server(this);
	}

	@Speed("1")
	@Override
	public String getTitle() {
		return "MorphineChat Server";
	}
	
	@Speed("1")
	@Override
	public void process(String input) {
		// TODO Auto-generated method stub
	}
	
	@Speed("n")
	@Override
	public void onClose() {
		this.server.onClose();
	}
	
	@Speed("1")
	@Override
	public Level getPermissionRank() {
		return Level.SERVER;
	}
	
	public static void main(String[] args) {
		ServerGUI.launch(args);
	}

}
