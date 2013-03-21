package com.mcnsa.notepad.exceptions;

import com.mcnsa.notepad.utilities.CustomStringContext;

public class CommandUsageException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -643386560394276196L;
	
	private CustomStringContext context = null;
	private CustomStringContext usage = null;

	public CommandUsageException(CustomStringContext context, CustomStringContext usage) {
		this.context = context;
		this.usage = usage;
	}
	
	public CustomStringContext getContext() {
		return context;
	}
	
	public CustomStringContext getUsage() {
		return usage;
	}
}
