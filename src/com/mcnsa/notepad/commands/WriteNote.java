package com.mcnsa.notepad.commands;

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
import com.mcnsa.notepad.utilities.ColourHandler;
import com.mcnsa.notepad.utilities.CustomStringContext;
import com.mcnsa.notepad.utilities.Logger;
import com.mcnsa.notepad.utilities.MultilineChatEntry;
import com.mcnsa.notepad.utilities.PlayerMatcher;

public class WriteNote implements CommandHandler {
	@Setting(node = "note.log-notes-to-console") public static boolean logNotes = true;
	@CustomString(node = "note.description") public static String description = "Writes a note about a given player";
	@CustomString(node = "note.console-no-note-args") public static String consoleNoNoteArgs = "&cError (%command%): You must provide a note!";
	@CustomString(node = "note.no-player-arg") public static String noPlayerArg = "&cError (%command%): You must provide a player to write a note about!";
	@CustomString(node = "note.console-usage") public static String consoleUsage = "/note <player> <notes>";
	@CustomString(node = "note.player-usage") public static String playerUsage = "/note <player> [notes]";
	@CustomString(node = "note.failed-to-record-note") public static String failedToRecordNote = "&cError: failed to record the note! (Database error)";
	@CustomString(node = "note.successful-note") public static String successfulNote = "&aYour note about %targetPlayer% has been successfully recorded";
	
	public void writeNote(CommandSender sender, OfflinePlayer targetPlayer, String note) throws DatabaseException, CommandException {
		int results = MCNSANotepad.getNoteManager().writeNote(sender, targetPlayer.getName(), note);
		
		// make sure it worked!
		if(results == 0) {
			throw new CommandException((new CustomStringContext(failedToRecordNote)));
		}
		
		// alert them
		ColourHandler.sendMessage(sender, ((new CustomStringContext(successfulNote)).setCommandSender(sender).setTargetPlayer(targetPlayer.getName())).toString());
		
		// and log it
		if(logNotes) {
			Logger.log("%s recorded a note about %s: %s", sender.getName(), targetPlayer.getName(), note);
		}
	}
	
	public void onExecute(CommandSender sender, String args[]) throws CommandException, CommandUsageException, DatabaseException {
		// deal with arguments
		boolean multiline = false;
		if(sender instanceof Player && args.length == 1) {
			multiline = true;
		}
		else if(!(sender instanceof Player) && args.length < 2) {
			throw new CommandUsageException(new CustomStringContext(consoleNoNoteArgs), new CustomStringContext(consoleUsage));
		}
		else if(args.length == 0) {
			throw new CommandUsageException(new CustomStringContext(noPlayerArg), new CustomStringContext(playerUsage));
		}
		
		// get our target player
		// try to match an online player
		OfflinePlayer targetPlayer = PlayerMatcher.matchPlayer(args[0]);
		
		if(multiline) {
			// schedule multiline text entry for this player
			MultilineChatEntry.scheduleMultilineTextEntry((Player)sender, this, targetPlayer);
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
			writeNote(sender, targetPlayer, note);
		}
	}

	public void onChatComplete(Player player, String note, Object... args) throws CommandException, DatabaseException {
		// make sure we check our args
		if(args.length != 1 || args[0].getClass().equals(OfflinePlayer.class)) {
			throw new IllegalArgumentException("Invalid args!");
		}
		
		// get our player
		OfflinePlayer targetPlayer = (OfflinePlayer)args[0];
		
		// and write the note!
		writeNote(player, targetPlayer, note);
	}
}
