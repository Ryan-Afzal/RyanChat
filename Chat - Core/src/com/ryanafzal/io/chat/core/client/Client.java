package com.ryanafzal.io.chat.core.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ryanafzal.io.chat.core.resources.application.ApplicationWindow;
import com.ryanafzal.io.chat.core.resources.command.Command;
import com.ryanafzal.io.chat.core.resources.command.runnable.DisconnectCommand;
import com.ryanafzal.io.chat.core.resources.command.runnable.LoginCommand;
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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Client extends ApplicationWindow {
	
	private int PORT = 440;
	private InetAddress IP;
	
	private User user;
	
	private Socket socket;
	private ToServerThread toServer;
	private FromServerThread fromServer;
	
	private boolean register;
	private int currentGroupID;
	
	//Login GUI
	private Label usernameLabel;
	private Label passwordLabel;
	private TextField usernameField;
	private PasswordField passwordField;
	private Button loginButton;
	private ToggleButton registerButton;
	private ToggleGroup registerGroup;
	
	private Separator separator;
	
	//User Info
	private Label userDataUsernameLabel;
	private Label userDataPermissionLevelLabel;
	
	public Client() {
		super();
		
		this.register = false;
		
		GridPane loginPane = new GridPane();
	    loginPane.setHgap(10);
	    loginPane.setVgap(10);
	    loginPane.setPadding(new Insets(0, 10, 0, 10));
		
		this.usernameLabel = new Label("Username: ");
		this.passwordLabel = new Label("Password: ");
		this.usernameField = new TextField();
		this.passwordField = new PasswordField();
		this.loginButton = new Button("Login");
		this.loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				login(usernameField.getText(), passwordField.getText());
				usernameField.setText("");
				passwordField.setText("");
			}
		});
		this.registerGroup = new ToggleGroup();
		this.registerGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		            Toggle toggle, Toggle new_toggle) {
		                if (new_toggle == null) {
		                	register = false;
		                } else {
		                	register = (Boolean) new_toggle.getUserData();
		                }
		             }
		    });
		
		this.registerButton = new ToggleButton("Register");
		this.registerButton.setUserData(true);
		this.registerButton.setStyle("-fx-base: salmon;");
		this.registerButton.setToggleGroup(this.registerGroup);
		loginPane.add(this.usernameLabel, 0, 0);
		loginPane.add(this.passwordLabel, 0, 1);
		loginPane.add(this.usernameField, 1, 0);
		loginPane.add(this.passwordField, 1, 1);
		loginPane.add(this.loginButton, 0, 2);
		loginPane.add(this.registerButton, 1, 2);
		
		this.separator = new Separator();
		
		GridPane userDataPane = new GridPane();
		userDataPane.setHgap(10);
		userDataPane.setVgap(10);
		userDataPane.setPadding(new Insets(0, 10, 0, 10));
		
		this.userDataUsernameLabel = new Label("<Username Unavailable>");
		this.userDataPermissionLevelLabel = new Label("<Rank Unavailable>");
		userDataPane.add(this.userDataUsernameLabel, 0, 0);
		userDataPane.add(this.userDataPermissionLevelLabel, 0, 1);
		
		VBox right = new VBox();
		right.getChildren().add(loginPane);
		right.getChildren().add(this.separator);
		right.getChildren().add(userDataPane);
		
		this.root.setRight(right);
	}
	
	@Override
	public String getTitle() {
		return "MorphineChat";
	}
	
	@Override
	public void process(String input) {
		if (input.charAt(0) == Command.COMMAND_CHARACTER) {
			List<?> args = new ArrayList<Object>(Arrays.asList(input.split(" ")));
			args.remove(0);
			this.registry.runCommand(input.substring(1), args, this.getPermissionRank());
			
		} else {//TODO Change the PacketData.AddressType.GLOBAL to the complex address system.
			PacketData data = new PacketData(this.user.getID(), PacketData.AddressType.GROUP, Server.GLOBAL_GROUP_ID, this.getPermissionRank());
			Packet packet = new Packet(new PacketMessage(this.user.getName(), input), data);
			this.toServer.addPacket(packet);
		}
	}
	
	private void login(String username, String password) {
		final Client c = this;
		
		Task<Void> task = new Task<Void>() {
			@Override 
		    protected Void call() throws Exception {
		    	try {
					IP = InetAddress.getByName("51S500036590");
					socket = new Socket(IP, PORT);
					
		            toServer = new ToServerThread(socket, c);
					fromServer = new FromServerThread(socket, c);
		            
		            Thread serverInThread = new Thread(toServer);
		            Thread serverOutThread = new Thread(fromServer);
		            serverInThread.start();
		            serverOutThread.start();
					
		            connect(username, password);
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
	
	private void connect(String username, String password) {
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
	
	public static void main(String[] args) {
		Client.launch(args);
	}

	@Override
	public void onClose() {
		this.disconnect();
		this.isRunning = false;
		
		this.toServer.cancel();
		this.fromServer.cancel();
		
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@Override
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
		this.outputCommandMessage(""
				+ "Successfully Signed In as: " + this.user.getName()
				);
	}
	
	public void refreshUserDataPane() {
		boolean b = true;//TODO
		if (b) return;
		
		Task<Void> task = new Task<Void>() {
			@Override 
		    protected Void call() throws Exception {
				if (user == null) {
					userDataUsernameLabel.setText("<Username Unavailable>");
					userDataPermissionLevelLabel.setText("<Rank Unavailable>");
				} else {
					userDataUsernameLabel.setText(user.getName());
					userDataPermissionLevelLabel.setText(getPermissionRank().getName());
				}
				
				return null;
		    }
		};
		
		task.run();
	}

}
