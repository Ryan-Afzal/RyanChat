package com.ryanafzal.io.chat.core.resources.user.permission;

import java.io.Serializable;

import javafx.scene.paint.Color;

public enum Level implements Serializable {
	
	/**
	 * Accessible to all users of the system.
	 */
	USER("User", 0, Color.BLACK), 
	/**
	 * Accessible to moderators and above.
	 */
	MODERATOR("Moderator", 1, Color.MAGENTA), 
	/**
	 * Accessible to officers and above.
	 */
	OFFICER("Officer", 2, Color.AQUAMARINE), 
	/**
	 * Accessible to administrators and above.
	 */
	ADMIN("Admin", 3, Color.ORANGE), 
	/**
	 * Accessible only to the server itself. This should be used only for serverside commands.
	 */
	SERVER("Server", 4, Color.GREEN);
	
	private final String name;
	private final int num;
	private final Color color;
	
	private Level(String name, int num, Color color) {
		this.name = name;
		this.num = num;
		this.color = color;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getNumericValue() {
		return this.num;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public static Level getLevelFromString(String string) {
		for (Level l : Level.values()) {
			if (l.getName().toLowerCase().equals(string.toLowerCase())) {
				return l;
			}
		}
		
		throw new IllegalArgumentException("Level " + string + " does not exist");
	}
}
