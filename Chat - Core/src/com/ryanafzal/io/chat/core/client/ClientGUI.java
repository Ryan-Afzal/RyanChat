package com.ryanafzal.io.chat.core.client;

import com.ryanafzal.io.chat.core.resources.application.ApplicationWindow;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class ClientGUI extends ApplicationWindow {
	
	private Client client;
	
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
	
	public ClientGUI() {
		this.client = new Client(this);
	}

}
