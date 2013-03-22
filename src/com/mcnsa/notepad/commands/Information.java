package com.mcnsa.notepad.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import com.mcnsa.notepad.annotations.CustomString;
import com.mcnsa.notepad.exceptions.CommandException;
import com.mcnsa.notepad.exceptions.CommandUsageException;
import com.mcnsa.notepad.exceptions.DatabaseException;
import com.mcnsa.notepad.interfaces.CommandHandler;
import com.mcnsa.notepad.utilities.ColourHandler;
import com.mcnsa.notepad.utilities.CustomStringContext;

// /notepad version
// /notepad [help]
public class Information implements CommandHandler {
	@CustomString(node = "information.version-format") public static String versionStringFormat = "&6MCNSANotepad &eVersion &f%version%";
	@CustomString(node = "information.help-header") public static String helpHeader = "&e--- &6MCNSANotepad &fHelp &e---";
	@CustomString(node = "information.command-listing") public static String commandListing = "%usage%\n\t%description%";
	
	@CustomString(node = "information.usage") public static String usage = "/notepad [version]";
	@CustomString(node = "information.description") public static String description = "Gives help about the MCNSANotepad commands";
	
	public static String version = "";
	
	public Information() {
		// automatically load our version from our plugin.yml
		Plugin plugin = Bukkit.getPluginManager().getPlugin("MCNSANotepad");
		PluginDescriptionFile desc = plugin.getDescription();
		version = desc.getVersion();
	}
	
	public static void sendVersion(CommandSender sender) {
		ColourHandler.sendMessage(sender, ((new CustomStringContext(versionStringFormat)).setCommandSender(sender).setVersion(version)).toString());
	}

	public void onExecute(CommandSender sender, String[] args)
			throws CommandException, CommandUsageException, DatabaseException {
		// deal with the version stuff off the bat
		if(args.length == 1 && args[0].equalsIgnoreCase("version")) {
			sendVersion(sender);
			return;
		}
		
		// anything else, lets just send the help
		ColourHandler.sendMessage(sender, ((new CustomStringContext(helpHeader)).setCommandSender(sender).setVersion(version)).toString());
		
		// our /note command
		CustomStringContext noteContext = new CustomStringContext(commandListing);
		noteContext.setCommandSender(sender);
		noteContext.setVersion(version);
		if(sender instanceof Player) {
			noteContext.setUsage(WriteNote.playerUsage);	
		}
		else {
			noteContext.setUsage(WriteNote.consoleUsage);			
		}
		noteContext.setDescription(WriteNote.description);
		ColourHandler.sendMessage(sender, noteContext.toString());
		
		// our /notes command
		CustomStringContext notesContext = new CustomStringContext(commandListing);
		notesContext.setCommandSender(sender);
		notesContext.setVersion(version);
		notesContext.setUsage(ViewNotes.usage);
		notesContext.setDescription(ViewNotes.description);
		ColourHandler.sendMessage(sender, notesContext.toString());
		
		// our /notepad command
		CustomStringContext notepadContext = new CustomStringContext(commandListing);
		notepadContext.setCommandSender(sender);
		notepadContext.setVersion(version);
		notepadContext.setUsage(usage);
		notepadContext.setDescription(description);
		ColourHandler.sendMessage(sender, notepadContext.toString());
	}

	public void onChatComplete(Player player, String enteredText,
			Object... args) throws CommandException, DatabaseException {
		// nothing to do here
	}

}
