package com.mcnsa.notepad.exceptions;

public class PermissionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4919158090907694257L;

	public PermissionException(String message) {
		super(message);
	}

	public PermissionException(String format, Object... args) {
		super(String.format(format, args));
	}
}
