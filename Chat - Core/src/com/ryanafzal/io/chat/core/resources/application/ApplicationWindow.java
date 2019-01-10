package com.ryanafzal.io.chat.core.resources.application;

import java.time.format.DateTimeFormatter;

import com.ryanafzal.io.chat.core.resources.command.CommandRegistry;
import com.ryanafzal.io.chat.core.resources.sendable.PacketData;
import com.ryanafzal.io.chat.core.resources.sendable.PacketMessage;
import com.ryanafzal.io.chat.core.resources.user.permission.Level;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class ApplicationWindow extends Application {
	
	public static final int WIDTH = 750;
	public static final int HEIGHT = 600;
	
	public static final String COMMAND_CARAT = ">>";
	public static final Color COMMAND_COLOR = Color.BLUE;
	public static final Color ERROR_COLOR = Color.RED;
	
	private Stage primaryStage;
	protected BorderPane root;
	
	//Console
	private OutputArea outputArea;
	private TextField inputField;
	
	protected boolean isRunning;
	private boolean isReady = false;
	
	protected final CommandRegistry registry;
	
	public ApplicationWindow() {
		this.isRunning = true;
		this.registry = new CommandRegistry();
		this.root = new BorderPane();
		
		//Center
		VBox centerPane = new VBox();
		
		this.outputArea = new OutputArea();
		centerPane.getChildren().add(this.outputArea);
		
		this.inputField = new TextField();
		this.inputField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (isReady) {
					String text = inputField.getText();
					if (!text.equals("")) {
						process(text);
						inputField.setText("");
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		centerPane.getChildren().add(this.inputField);
		
		this.root.setCenter(centerPane);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(this.getTitle());
		
		this.primaryStage.setScene(new Scene(this.root, ApplicationWindow.WIDTH, ApplicationWindow.HEIGHT, Color.BLACK));
		this.primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void stop() {
		this.onClose();
	}
	
	/**
	 * Outputs a message.
	 * @param message The message to output.
	 */
	public synchronized void outputMessage(String message) {
		this.outputArea.addLine(message);
	}
	
	/**
	 * Outputs a user-sent message.
	 * @param message The message to output.
	 * @param level The permission level to output in.
	 */
	public void outputMessage(String message, Level level) {
		this.outputArea.addLine(message, level.getColor());
	}
	
	/**
	 * Outputs an error message.
	 * @param message The message to output.
	 */
	public void outputErrorMessage(String message) {
		this.outputArea.addLine(message, ERROR_COLOR);
	}
	
	/**
	 * Outputs a 'command formatted' message.
	 * @param message The message to output.
	 */
	public void outputCommandMessage(String message) {
		this.outputArea.addLine(COMMAND_CARAT + " " + message, COMMAND_COLOR);
	}
	
	public void outputPacketMessage(PacketMessage message, PacketData data) {
		String header = "["
				+ data.TIMESTAMP.format(DateTimeFormatter.ISO_LOCAL_TIME)
				+ "]"
				+ message.USERNAME
				+ ": ";
		
		this.outputMessage(header + message.MESSAGE, data.LEVEL);
	}
	
	public boolean isReady() {
		return this.isReady;
	}
	
	public void setReady(boolean ready) {
		this.isReady = ready;
	}
	
	public boolean isRunning() {
		return this.isRunning;
	}
	
	/**
	 * Gets the title of the window.
	 * @return Returns the title of the window.
	 */
	public abstract String getTitle();
	
	/**
	 * Processes a line of input from the window's console.
	 * This method is called automatically when a user enters a line into the console.
	 * @param input A line of input from the window's console.
	 */
	public abstract void process(String input);
	
	/**
	 * Performs an action when the window is closed.
	 * This method is called automatically when the window is closed.
	 */
	public abstract void onClose();
	
}
