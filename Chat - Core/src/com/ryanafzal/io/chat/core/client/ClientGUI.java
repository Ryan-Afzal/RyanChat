package com.ryanafzal.io.chat.core.client;

import com.ryanafzal.io.chat.core.resources.application.ApplicationWindow;
import com.ryanafzal.io.chat.core.resources.command.ChangeLevelCommand;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class ClientGUI extends ApplicationWindow {
	
	public final Client client;
	
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
		new ChangeLevelCommand(this.registry);
		
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
				client.login(usernameField.getText(), passwordField.getText());
				usernameField.setText("");
				passwordField.setText("");
			}
		});
		this.registerGroup = new ToggleGroup();
		this.registerGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		            Toggle toggle, Toggle new_toggle) {
		                client.changed(ov, toggle, new_toggle);
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
		return "SpencerChat";
	}
	
	@Override
	public void process(String input) {
		this.client.process(input);
	}
	
	@Override
	public void onClose() {
		this.client.onClose();
	}
	
	@Override
	public Level getPermissionRank() {
		return this.client.getPermissionRank();
	}
	
	public static void main(String[] args) {
		ClientGUI.launch(args);
	}

}
