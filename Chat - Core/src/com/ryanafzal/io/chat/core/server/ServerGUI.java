package com.ryanafzal.io.chat.core.server;

import java.net.UnknownHostException;

import com.ryanafzal.io.chat.core.resources.application.ApplicationWindow;
import com.ryanafzal.io.chat.core.resources.misc.Speed;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

import javafx.stage.Stage;

public class ServerGUI extends ApplicationWindow {
	
	private Server server;
	
	public ServerGUI() throws UnknownHostException {
		super("SpencerChat Server");
		/*try {
			this.server = new Server(this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			this.outputErrorMessage("[FATAL ERROR]: Unable to connect to LocalHost.");
		}*/
	}
	
	@Speed("1")
	@Override
	public void process(String input) {
		//TODO
	}
	
	@Speed("n")
	@Override
	public void onClose() {
		this.server.close();
	}
	
	@Speed("1")
	@Override
	public Level getPermissionRank() {
		return Level.SERVER;
	}
	
	public static void main(String[] args) {
		ServerGUI.launch(args);
	}

	@Override
	protected void initGUI(Stage stage) {
		try {
			this.server = new Server(this);
			new Thread(this.server).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			this.outputErrorMessage("[FATAL ERROR]: Unable to connect to LocalHost.");
		}
	}

}
