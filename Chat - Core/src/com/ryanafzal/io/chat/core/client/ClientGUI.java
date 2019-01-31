package com.ryanafzal.io.chat.core.client;

import com.ryanafzal.io.chat.core.resources.application.ApplicationWindow;
import com.ryanafzal.io.chat.core.resources.command.ChangeLevelCommand;
import com.ryanafzal.io.chat.core.resources.misc.RadioToggleButton;
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
import javafx.stage.Stage;

public class ClientGUI extends ApplicationWindow {
	
	public Client client;
	
	//Login GUI
	private Label usernameLabel;
	private Label passwordLabel;
	private TextField usernameField;
	private PasswordField passwordField;
	private Button loginButton;
	private ToggleButton registerButton;
	private ToggleGroup registerGroup;
	
	//User Info
	private Label userDataUsernameLabel;
	private Label userDataPermissionLevelLabel;
	
	//Message Info
	private RadioToggleButton[] levelToggleButtons;
	private ToggleGroup levelToggleGroup;
	
	public ClientGUI() {
		super("SpencerChat");
		
		new ChangeLevelCommand(this.registry);
	}
	
	@Override
	protected void initGUI(Stage stage) {
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
		                client.changedRegisterToggle(ov, toggle, new_toggle);
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
		
		GridPane userDataPane = new GridPane();
		userDataPane.setHgap(10);
		userDataPane.setVgap(10);
		userDataPane.setPadding(new Insets(0, 10, 0, 10));
		
		this.userDataUsernameLabel = new Label("<Username Unavailable>");
		this.userDataPermissionLevelLabel = new Label("<Rank Unavailable>");
		userDataPane.add(this.userDataUsernameLabel, 0, 0);
		userDataPane.add(this.userDataPermissionLevelLabel, 0, 1);
		
		GridPane messageDataPane = new GridPane();
		messageDataPane.setHgap(10);
		messageDataPane.setVgap(10);
		messageDataPane.setPadding(new Insets(0, 10, 0, 10));

		this.levelToggleGroup = new ToggleGroup();
		this.levelToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		            Toggle toggle, Toggle new_toggle) {
		                client.changedLevelToggle(ov, toggle, new_toggle);
		             }
		    });
		
		VBox levelToggles = new VBox();
		this.levelToggleButtons = new RadioToggleButton[Level.values().length];
		for (int i = 0; i < this.levelToggleButtons.length; i++) {
			this.levelToggleButtons[i] = new RadioToggleButton(Level.values()[i].getName());
			this.levelToggleButtons[i].setUserData(Level.values()[i]);
			this.levelToggleButtons[i].setStyle("-fx-base: #" + Level.values()[i].getColor().toString().substring(2) + "");
			this.levelToggleButtons[i].setToggleGroup(this.levelToggleGroup);
			levelToggles.getChildren().add(this.levelToggleButtons[i]);
		}
		messageDataPane.add(levelToggles, 0, 0);
		
		
		
		VBox right = new VBox();
		right.getChildren().add(loginPane);
		right.getChildren().add(new Separator());
		right.getChildren().add(userDataPane);
		right.getChildren().add(new Separator());
		right.getChildren().add(messageDataPane);
		
		this.root.setRight(right);
		
		this.client = new Client(this);
		new Thread(this.client).start();
		
		this.levelToggleButtons[0].fire();
	}
	
	@Override
	public void process(String input) {
		this.client.process(input);
	}
	
	@Override
	public void onClose() {
		this.client.close();
	}
	
	@Override
	public Level getPermissionRank() {
		return this.client.getPermissionRank();
	}
	
	public static void main(String[] args) {
		ClientGUI.launch(args);
	}

	//TODO IllegalStateException not on JavaFX Application Thread
	protected void refreshUserDataPane() {
		//this.userDataUsernameLabel.setText(this.client.getUser().getName());
		//this.userDataPermissionLevelLabel.setText(this.client.getUser().getPermissionLevel(this.client.getCurrentGroupID()).getName().toUpperCase());
	}

}
