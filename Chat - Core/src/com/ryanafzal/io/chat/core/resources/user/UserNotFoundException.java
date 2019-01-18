package com.ryanafzal.io.chat.core.resources.user;

public class UserNotFoundException extends Exception {
	
	private static final long serialVersionUID = -4227174839137438254L;
	
	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String arg0) {
		super(arg0);
	}

	public UserNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public UserNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UserNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
