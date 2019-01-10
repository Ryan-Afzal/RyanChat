package com.ryanafzal.io.chat.core.resources.user.permission;

import java.io.Serializable;

import javafx.scene.paint.Color;

public enum Level implements Serializable {
	/**
	 * Accessible to all users of the system.
	 */
	USER(0, Color.BLACK), 
	/**
	 * Accessible to moderators and above.
	 */
	MODERATOR(1, Color.MAGENTA), 
	/**
	 * Accessible to officers and above.
	 */
	OFFICER(2, Color.AQUAMARINE), 
	/**
	 * Accessible to administrators and above.
	 */
	ADMIN(3, Color.ORANGE), 
	/**
	 * Accessible only to the server itself. This should be used only for serverside commands.
	 */
	SERVER(4, Color.GREEN);
	
	private final int num;
	private final Color color;
	
	private Level(int num, Color color) {
		this.num = num;
		this.color = color;
	}
	
	public int getNumericValue() {
		return this.num;
	}
	
	public Color getColor() {
		return this.color;
	}
}
