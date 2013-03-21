package com.mcnsa.notepad.exceptions;

public class SettingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1766968400337040817L;

	public SettingException(String message) {
		super(message);
	}

	public SettingException(String format, Object... args) {
		super(String.format(format, args));
	}
}
