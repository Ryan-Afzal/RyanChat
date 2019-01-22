package com.ryanafzal.io.chat.core.resources.command;

import java.io.Serializable;

public abstract class RunnableCommand implements Serializable {
	
	/**
	 * Run the command with the specified application
	 * @param input
	 */
	public abstract void run(Object input);
	
	/**
	 * Gets the type of the command, i.e. where it runs.
	 * @return Returns the {@code Type}
	 */
	public abstract Type getType();
	
	public enum Type {
		CLIENT, SERVER, CONNECTION
	}
	
}
