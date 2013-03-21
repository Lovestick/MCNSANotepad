package com.mcnsa.notepad.exceptions;

import com.mcnsa.notepad.utilities.CustomStringContext;

public class CommandException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2794584664600435475L;
	
	private CustomStringContext context = null;

	public CommandException(CustomStringContext context) {
		this.context = context;
	}
	
	public CustomStringContext getContext() {
		return context;
	}
}
