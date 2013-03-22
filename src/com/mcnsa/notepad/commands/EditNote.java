package com.mcnsa.notepad.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mcnsa.notepad.annotations.CustomString;
import com.mcnsa.notepad.exceptions.CommandException;
import com.mcnsa.notepad.exceptions.CommandUsageException;
import com.mcnsa.notepad.exceptions.DatabaseException;
import com.mcnsa.notepad.interfaces.CommandHandler;
import com.mcnsa.notepad.managers.DatabaseManager;
import com.mcnsa.notepad.utilities.ColourHandler;
import com.mcnsa.notepad.utilities.CustomStringContext;
import com.mcnsa.notepad.utilities.Logger;
import com.mcnsa.notepad.utilities.MultilineChatEntry;

public class EditNote implements CommandHandler {
	@CustomString(node = "editnote.console-usage") public static String consoleUsage = "/editnote <noteID> <notes>";
	@CustomString(node = "editnote.player-usage") public static String playerUsage = "/editnote <noteID> [notes]";
	@CustomString(node = "editnote.invalid-id") public static String invalidID = "&cInvalid note ID %noteID%!";
	@CustomString(node = "editnote.successful") public static String successful = "&aNote %noteID% has been saved!";

	public void updateNote(CommandSender sender, Integer noteID, String note) throws DatabaseException, CommandException {
		// add the note
		int results = DatabaseManager.updateQuery(
				"update notes set note=? where id=?;",
				note,
				noteID);
		
		// make sure it worked!
		if(results == 0) {
			throw new CommandException((new CustomStringContext(WriteNote.failedToRecordNote)));
		}
		
		// alert them
		ColourHandler.sendMessage(sender, ((new CustomStringContext(successful)).setCommandSender(sender).setNoteID(noteID)).toString());
		
		// and log it
		if(WriteNote.logNotes) {
			Logger.log("%s updated note #%d", sender.getName(), noteID);
		}
	}
	
	@Override
	public void onExecute(CommandSender sender, String[] args)
			throws CommandException, CommandUsageException, DatabaseException {
		// deal with arguments
		boolean multiline = false;
		if(sender instanceof Player && args.length == 1) {
			multiline = true;
		}
		else if(!(sender instanceof Player) && args.length < 2) {
			throw new CommandUsageException(new CustomStringContext(WriteNote.consoleNoNoteArgs), new CustomStringContext(consoleUsage));
		}
		else if(args.length == 0) {
			throw new CommandUsageException(new CustomStringContext(WriteNote.noPlayerArg), new CustomStringContext(playerUsage));
		}
		
		// get our note ID
		Integer noteID = null;
		try {
			noteID = Integer.parseInt(args[0]);
		}
		catch(NumberFormatException e) {
			if(sender instanceof Player) {
				throw new CommandUsageException((new CustomStringContext(invalidID)), new CustomStringContext(playerUsage));	
			}
			else {
				throw new CommandUsageException((new CustomStringContext(invalidID)), new CustomStringContext(consoleUsage));
			}		
		}
		
		ArrayList<HashMap<String, Object>> results = DatabaseManager.accessQuery(
				"select id from notes where id=?;",
				noteID);
		if(results.size() == 0) {
			throw new CommandException((new CustomStringContext(invalidID)).setNoteID(noteID));
		}
		
		if(multiline) {
			// schedule multiline text entry for this player
			MultilineChatEntry.scheduleMultilineTextEntry((Player)sender, this, noteID);
		}
		else {
			// join our arguments
			String note = "";
			boolean first = true;
			for(String arg: args) {
				if(first) {
					first = false;
					continue;
				}
				note += arg + " ";
			}
			note = note.trim();
			
			// write it
			updateNote(sender, noteID, note);
		}
	}

	@Override
	public void onChatComplete(Player player, String note,
			Object... args) throws CommandException, DatabaseException {
		// make sure we check our args
		if(args.length != 1 || args[0].getClass().equals(Integer.class)) {
			throw new IllegalArgumentException("Invalid args!");
		}
		
		// get our player
		Integer noteID = (Integer)args[0];
		
		// and write the note!
		updateNote(player, noteID, note);
	}

}
