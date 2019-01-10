package com.ryanafzal.io.chat.core.resources.command;

import java.io.Serializable;

public interface RunnableCommand extends Serializable {
	
	/**
	 * Run the command with the specified application
	 * @param input
	 */
	public void run(Object input);
	
	/**
	 * Gets the type of the command, i.e. where it runs.
	 * @return Returns the {@code Type}
	 */
	public Type getType();
	
	public enum Type {
		CLIENT, SERVER, CONNECTION
	}
	
}
