package com.ryanafzal.io.chat.core.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ryanafzal.io.chat.core.resources.command.Command;
import com.ryanafzal.io.chat.core.resources.command.CommandInfo;
import com.ryanafzal.io.chat.core.resources.command.runnable.DisconnectCommand;
import com.ryanafzal.io.chat.core.resources.command.runnable.LoginCommand;
import com.ryanafzal.io.chat.core.resources.misc.Speed;
import com.ryanafzal.io.chat.core.resources.sendable.Packet;
import com.ryanafzal.io.chat.core.resources.sendable.PacketCommand;
import com.ryanafzal.io.chat.core.resources.sendable.PacketContents;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData;
import com.ryanafzal.io.chat.core.resources.sendable.PacketMessage;
import com.ryanafzal.io.chat.core.resources.thread.FromServerThread;
import com.ryanafzal.io.chat.core.resources.thread.ToServerThread;
import com.ryanafzal.io.chat.core.resources.user.User;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;
import com.ryanafzal.io.chat.core.server.Server;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.control.Toggle;

/**
 * Holds client interaction code
 * @author s-afzalr
 *
 */
public class Client {
	
	private ClientGUI parent;
	
	/**
	 * THIS IS THE IP ADDRESS FOR THE SERVER!
	 */
	private static final String SERVER_IP_ADDRESS = "10.0.0.14.24.16.159.137";
	
	private int PORT = 4444;
	private InetAddress IP;
	
	private User user;
	
	private Socket socket;
	private ToServerThread toServer;
	private FromServerThread fromServer;
	
	private boolean register;
	private long currentGroupID;
	private Level currentVisibilityLevel;
	
	public Client(ClientGUI parent) {
		super();
		this.parent = parent;
		this.register = false;
		this.currentVisibilityLevel = Level.USER;
		
		this.connect();
	}
	
	public void process(String input) {
		if (input.charAt(0) == Command.COMMAND_CHARACTER) {
			List<String> args = new ArrayList<String>(Arrays.asList(input.split("-")));
			String name = args.remove(0).trim().substring(1);
			for (int i = 0; i < args.size(); i++) {
				args.set(i, args.get(i).trim());
			}
			
			CommandInfo info = new CommandInfo(this.user.getID(), this.getPermissionRank(), this.currentGroupID);
			
			this.parent.registry.runCommand(name, info, args, this.getPermissionRank());
			
		} else {
			PacketData data = new PacketData(
					this.user.getID(), 
					PacketData.AddressType.GROUP, 
					Server.GLOBAL_GROUP_ID, //TODO
					this.currentVisibilityLevel);
			Packet packet = new Packet(new PacketMessage(this.user.getName(), input, this.getPermissionRank()), data);
			this.toServer.addPacket(packet);
		}
	}
	
	protected void connect() {
		final Client c = this;
		
		Task<Void> task = new Task<Void>() {
			@Override 
		    protected Void call() throws Exception {
		    	try {
					//IP = InetAddress.getByName("51S500036590.it.bsd405.org");
					IP = InetAddress.getByName(SERVER_IP_ADDRESS);
		    		socket = new Socket(IP, PORT);
					
		            toServer = new ToServerThread(socket, c);
					fromServer = new FromServerThread(socket, c);
		            
		            Thread serverInThread = new Thread(toServer);
		            Thread serverOutThread = new Thread(fromServer);
		            serverInThread.start();
		            serverOutThread.start();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	
				return null;
		    }
		};
		
		new Thread(task).start();
	}
	
	public void login(String username, String password) {
		PacketData data = new PacketData(0, PacketData.AddressType.SERVER, User.SERVER.getID(), Level.SERVER);
		PacketContents contents = new PacketCommand(new LoginCommand(username, password.hashCode(), this.register));
		Packet packet = new Packet(contents, data);
		this.toServer.addPacket(packet);
	}
	
	private void disconnect() {
		PacketData data = new PacketData(this.user.getID(),PacketData.AddressType.SERVER, User.SERVER.getID(), Level.SERVER);
		PacketContents contents = new PacketCommand(new DisconnectCommand());
		Packet packet = new Packet(contents, data);
		this.toServer.addPacket(packet);
	}

	public void onClose() {
		this.disconnect();
		
		this.toServer.cancel();
		this.fromServer.cancel();
		
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Level getPermissionRank() {
		return this.user.getPermissionLevel(this.currentGroupID);
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void printWelcomeMessage() {
		this.parent.outputCommandMessage(""
				+ "Successfully Signed In as: " + this.user.getName()
				);
	}
	
	public void refreshUserDataPane() {
		this.parent.refreshUserDataPane();
	}
	
	/**
	 * Called automatically by ClientGUI
	 * @param ov
	 * @param toggle
	 * @param new_toggle
	 */
	protected void changedRegisterToggle(ObservableValue<? extends Toggle> ov,
            Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null) {
                	register = false;
                } else {
                	register = (Boolean) new_toggle.getUserData();
                }
    }
	
	protected void changedLevelToggle(ObservableValue<? extends Toggle> ov,
            Toggle toggle, Toggle new_toggle) {
                currentVisibilityLevel = (Level) new_toggle.getUserData();
	}
	
	@Speed("1")
	public boolean isRunning() {
		return this.parent.isRunning();
	}
	
	@Speed("1")
	public ClientGUI getParent() {
		return this.parent;
	}
	
	public long getCurrentGroupID() {
		return this.currentGroupID;
	}
	
	public void queuePacket(Packet packet) {
		this.toServer.addPacket(packet);
	}

}
