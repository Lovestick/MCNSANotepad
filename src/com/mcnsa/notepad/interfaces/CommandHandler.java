package com.mcnsa.notepad.interfaces;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mcnsa.notepad.exceptions.CommandException;
import com.mcnsa.notepad.exceptions.CommandUsageException;
import com.mcnsa.notepad.exceptions.DatabaseException;

public interface CommandHandler {
	public void onExecute(CommandSender sender, String args[]) throws CommandException, CommandUsageException, DatabaseException;
	public void onChatComplete(Player player, String enteredText, Object... args) throws CommandException, DatabaseException;
}
