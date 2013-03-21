package com.mcnsa.notepad.exceptions;

public class DatabaseException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8041555914035604892L;

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(String format, Object... args) {
		super(String.format(format, args));
	}
}
