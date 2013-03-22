package com.mcnsa.notepad.commands;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mcnsa.notepad.MCNSANotepad;
import com.mcnsa.notepad.annotations.CustomString;
import com.mcnsa.notepad.annotations.Setting;
import com.mcnsa.notepad.exceptions.CommandException;
import com.mcnsa.notepad.exceptions.CommandUsageException;
import com.mcnsa.notepad.exceptions.DatabaseException;
import com.mcnsa.notepad.interfaces.CommandHandler;
import com.mcnsa.notepad.managers.NoteManager.Note;
import com.mcnsa.notepad.utilities.ColourHandler;
import com.mcnsa.notepad.utilities.CustomStringContext;
import com.mcnsa.notepad.utilities.PlayerMatcher;

public class ViewNotes implements CommandHandler {
	@Setting(node = "notes.notes-per-page") public static int notesPerPage = 5;
	@CustomString(node = "notes.no-player-arg") public static String noPlayerArg = "&cError (%command%): You must provide a player to write a note about!";
	@CustomString(node = "notes.no-notes") public static String noNotes = "&6%targetPlayer% doesn't have any notes!";
	@CustomString(node = "notes.invalid-page-number") public static String invalidPageNumber = "&cInvalid page number (there are %numPages% pages)!";
	@CustomString(node = "notes.note-header") public static String noteHeader = "&e--- &6Notes for &f%targetPlayer% &6(Page &f%page%&6/&f%numPages%&6) &e---";
	@CustomString(node = "notes.noteFormat") public static String noteFormat = "&7[%date%] &6%noteTaker%: &f%note% &7(%noteID%)";
	@CustomString(node = "notes.usage") public static String usage = "/notes <player> [page]";
	@CustomString(node = "notes.description") public static String description = "Shows you the notes for a given player";

	public void onExecute(CommandSender sender, String[] args)
			throws CommandException, CommandUsageException, DatabaseException {
		// make sure they have a player target
		if(args.length == 0) {
			throw new CommandUsageException(new CustomStringContext(noPlayerArg), new CustomStringContext(usage));
		}
		
		// get our target player
		String targetPlayer = args[0];
		try {
			OfflinePlayer offlinePlayer = PlayerMatcher.matchPlayer(targetPlayer);
			targetPlayer = offlinePlayer.getName();
		}
		catch(Exception e) {
			
		}
		
		// get a resultset of all our notes
		ArrayList<Note> notes = MCNSANotepad.getNoteManager().getPlayerNotes(targetPlayer);
		if(notes.size() == 0) {
			throw new CommandException((new CustomStringContext(noNotes)).setTargetPlayer(targetPlayer));
		}
		
		// calculate the number of pages
		int totalPages = notes.size() / notesPerPage;
		if(notes.size() % 5 != 0) totalPages++;
		
		// parse the page
		int page = 1;
		if(args.length > 1) {
			// get the page
			try {
				page = Integer.parseInt(args[1]);
			}
			catch(NumberFormatException e) {
				throw new CommandUsageException((new CustomStringContext(invalidPageNumber).setNumPages(totalPages)), new CustomStringContext(usage));
			}
		}
		
		// make sure we have an appropriate page
		page -= 1;
		if(page < 0 || page >= totalPages) {
			throw new CommandException((new CustomStringContext(invalidPageNumber)).setNumPages(totalPages));
		}
		
		// calculate the start and end warp indices
		int start = page * notesPerPage;
		int end = start + notesPerPage;
		if(end > notes.size()) {
			end = notes.size();
		}
		
		// ok, we have our start and end
		// send it!
		ColourHandler.sendMessage(sender, ((new CustomStringContext(noteHeader)).setTargetPlayer(targetPlayer).setPage(page + 1).setNumPages(totalPages)).toString());
		for(int i = start; i < end; i++) {
			CustomStringContext noteContext = new CustomStringContext(noteFormat);
			noteContext.setTargetPlayer(notes.get(i).notee);
			noteContext.setNoteTaker(notes.get(i).noteTaker);
			noteContext.setTimestamp(notes.get(i).date);
			noteContext.setNoteID(notes.get(i).noteID);
			noteContext.setNote(notes.get(i).note);
			ColourHandler.sendMessage(sender, noteContext.toString());
		}
	}

	public void onChatComplete(Player player, String enteredText,
			Object... args) throws CommandException, DatabaseException {
		// do nothing
	}
}
