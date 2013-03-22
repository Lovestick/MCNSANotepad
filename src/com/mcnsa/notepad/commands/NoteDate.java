package com.mcnsa.notepad.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mcnsa.notepad.MCNSANotepad;
import com.mcnsa.notepad.annotations.CustomString;
import com.mcnsa.notepad.exceptions.CommandException;
import com.mcnsa.notepad.exceptions.CommandUsageException;
import com.mcnsa.notepad.exceptions.DatabaseException;
import com.mcnsa.notepad.interfaces.CommandHandler;
import com.mcnsa.notepad.managers.DatabaseManager;
import com.mcnsa.notepad.utilities.ColourHandler;
import com.mcnsa.notepad.utilities.CustomStringContext;
import com.mcnsa.notepad.utilities.Logger;

public class NoteDate implements CommandHandler {
	@CustomString(node = "notedate.invalid-args") public static String invalidArgs = "&cInvalid arguments!";
	@CustomString(node = "notedate.usage") public static String usage = "/notedate <noteID> <new date> [new time]";
	@CustomString(node = "notedate.invalid-date") public static String invalidDate = "&cInvalid date format! Use: YYYY-MM-DD";
	@CustomString(node = "notedate.invalid-time") public static String invalidTime = "&cInvalid time format! Use: HH:MM:SS";
	@CustomString(node = "notedate.failed-edit") public static String failedEdit = "&cError: failed to edit the note's date! (Database error)";

	public void onExecute(CommandSender sender, String[] args)
			throws CommandException, CommandUsageException, DatabaseException {
		// make sure we have appropriate args
		if(args.length != 2 && args.length != 3) {
			throw new CommandUsageException((new CustomStringContext(invalidArgs)), new CustomStringContext(usage));
		}
		
		// get our note ID
		Integer noteID = null;
		try {
			noteID = Integer.parseInt(args[0]);
		}
		catch(NumberFormatException e) {
			throw new CommandUsageException((new CustomStringContext(EditNote.invalidID)), new CustomStringContext(usage));
		}
		
		ArrayList<HashMap<String, Object>> results = DatabaseManager.accessQuery(
				"select id from notes where id=?;",
				noteID);
		if(results.size() == 0) {
			throw new CommandException((new CustomStringContext(EditNote.invalidID)).setNoteID(noteID));
		}
		
		// attempt to parse the date string
		String dateString = args[1];
		Integer year = null;
		Integer month = null;
		Integer day = null;
		if(dateString.matches("^[0-9]+-[0-9]+-[0-9]+$")) {
			String[] parts = dateString.split("-");
			
			try {
				year = Integer.parseInt(parts[0]);
				month = Integer.parseInt(parts[1]) - 1;
				day = Integer.parseInt(parts[2]);
			}
			catch(Exception e) {
				throw new CommandException((new CustomStringContext(invalidDate)));
			}
		}
		else {
			throw new CommandException((new CustomStringContext(invalidDate)));
		}
		
		// try to parse the time
		Integer hour = 0;
		Integer minute = 0;
		Integer second = 0;
		if(args.length == 3) {
			String timeString = args[2];
			// parse the time
			if(timeString.matches("^[0-9]+:[0-9]+:[0-9]+$")) {
				String[] parts = timeString.split(":");
				
				try {
					hour = Integer.parseInt(parts[0]);
					minute = Integer.parseInt(parts[1]);
					second = Integer.parseInt(parts[2]);
				}
				catch(Exception e) {
					throw new CommandException((new CustomStringContext(invalidTime)));
				}
			}
			else {
				throw new CommandException((new CustomStringContext(invalidTime)));
			}
		}
		
		// build a new timestamp out of our time object
		Calendar cal = new GregorianCalendar(year, month, day, hour, minute, second);
		Timestamp newTimestamp = new Timestamp(cal.getTimeInMillis());
		
		// and update the query
		int numResults = MCNSANotepad.getNoteManager().editDate(noteID, newTimestamp);
		
		// make sure it worked!
		if(numResults == 0) {
			throw new CommandException((new CustomStringContext(failedEdit)));
		}
		
		// alert them
		ColourHandler.sendMessage(sender, ((new CustomStringContext(EditNote.successful)).setCommandSender(sender).setNoteID(noteID)).toString());
		
		// and log it
		if(WriteNote.logNotes) {
			Logger.log("%s updated note #%d", sender.getName(), noteID);
		}
	}

	public void onChatComplete(Player player, String enteredText,
			Object... args) throws CommandException, DatabaseException {
		// do nothing
	}

}
