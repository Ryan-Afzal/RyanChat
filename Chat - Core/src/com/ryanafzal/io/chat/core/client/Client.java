package com.ryanafzal.io.chat.core.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Client extends ApplicationWindow {
	
	private int PORT = 440;
	private InetAddress IP;
	
	private User user;
	
	private Socket socket;
	private ToServerThread toServer;
	private FromServerThread fromServer;
	
	//Login GUI
	private Label usernameLabel;
	private Label passwordLabel;
	private TextField usernameField;
	private PasswordField passwordField;
	private Button loginButton;
	
	private Separator separator;
	
	//User Info
	
	
	public Client() {
		super();
		
		GridPane grid = new GridPane();
	    grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(0, 10, 0, 10));
		
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
		grid.add(this.usernameLabel, 0, 0);
		grid.add(this.passwordLabel, 0, 1);
		grid.add(this.usernameField, 1, 0);
		grid.add(this.passwordField, 1, 1);
		grid.add(loginButton, 0, 2);
		
		this.separator = new Separator();
		
		VBox right = new VBox();
		right.getChildren().add(grid);
		right.getChildren().add(separator);
		
		this.root.setRight(right);
	}

	@Override
	public String getTitle() {
		return "RyanChat";
	}

	@Override
	public void process(String input) {
		if (input.charAt(0) == Command.COMMAND_CHARACTER) {
			//TODO Process Commands.
		} else {//TODO Change the User.SERVER.getID() and Level.USER to the complex system.
			PacketData data = new PacketData(this.user.getID(), User.SERVER.getID(), Level.USER);
			Packet packet = new Packet(new PacketMessage(input), data);
			this.toServer.addPacket(packet);
		}
	}
	
	private void login(String username, String password) {
		try {
			this.IP = InetAddress.getByName("51S500036590");
			this.socket = new Socket(IP, PORT);
            Thread.sleep(1000);
			
            this.toServer = new ToServerThread(this.socket, this);
			this.fromServer = new FromServerThread(this.socket, this);
            Thread serverInThread = new Thread(this.toServer);
            Thread serverOutThread = new Thread(this.fromServer);
            serverInThread.start();
            serverOutThread.start();
            
            this.connect(username, password);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void connect(String username, String password) {
		PacketData data = new PacketData(0, User.SERVER.getID(), Level.SERVER);
		PacketContents contents = new PacketCommand(new LoginCommand(username, password.hashCode(), false));//TODO
		Packet packet = new Packet(contents, data);
		this.toServer.addPacket(packet);
	}
	
	private void disconnect() {
		PacketData data = new PacketData(this.user.getID(), User.SERVER.getID(), Level.SERVER);
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
		
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

}
