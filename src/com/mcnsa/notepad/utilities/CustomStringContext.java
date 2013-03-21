package com.mcnsa.notepad.utilities;

import java.sql.Timestamp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class CustomStringContext {
	public String message = null;
	public CommandSender commandSender = null;
	public String command = null;
	public String usage = null;
	public String addedText = null;
	public String version = null;
	public String description = null;
	public String targetPlayer = null;
	public String noteTaker = null;
	public String note = null;
	public Location location = null;
	public World world = null;
	public Exception e = null;
	public Integer page = null;
	public Integer numPages = null;
	public Integer noteID = null;
	public Timestamp timestamp = null;
	
	// various constructors
	public CustomStringContext(String message) {
		this.message = message;
	}
	public CustomStringContext(String message, CommandSender commandSender) {
		this.message = message;
		this.commandSender = commandSender;
	}
	
	// setters for all our variables
	public CustomStringContext setCommandSender(CommandSender commandSender) {
		this.commandSender = commandSender;
		return this;
	}
	public CustomStringContext setCommand(String command) {
		this.command = command;
		return this;
	}
	public CustomStringContext setUsage(String usage) {
		this.usage = usage;
		return this;
	}
	public CustomStringContext setAddedText(String addedText) {
		this.addedText = addedText;
		return this;
	}
	public CustomStringContext setVersion(String version) {
		this.version = version;
		return this;
	}
	public CustomStringContext setDescription(String description) {
		this.description = description;
		return this;
	}
	public CustomStringContext setTargetPlayer(String targetPlayer) {
		this.targetPlayer = targetPlayer;
		return this;
	}
	public CustomStringContext setNoteTaker(String noteTaker) {
		this.noteTaker = noteTaker;
		return this;
	}
	public CustomStringContext setNote(String note) {
		this.note = note;
		return this;
	}
	public CustomStringContext setLocation(Location location) {
		this.location = location;
		return this;
	}
	public CustomStringContext setWorld(World world) {
		this.world = world;
		return this;
	}
	public CustomStringContext setException(Exception e) {
		this.e = e;
		return this;
	}
	public CustomStringContext setPage(int page) {
		this.page = page;
		return this;
	}
	public CustomStringContext setNumPages(int numPages) {
		this.numPages = numPages;
		return this;
	}
	public CustomStringContext setNoteID(int noteID) {
		this.noteID = noteID;
		return this;
	}
	public CustomStringContext setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
		return this;
	}
	
	// and get a formatted string out
	@SuppressWarnings("deprecation")
	public String toString() {
		String ret = new String(message);
		
		// our various replacements
		if(commandSender != null) {
			ret = ret.replaceAll("%commandSender%", commandSender.getName());
		}
		if(command != null) {
			ret = ret.replaceAll("%command%", command);
		}
		if(usage != null) {
			ret = ret.replaceAll("%usage%", usage);
		}
		if(addedText != null) {
			ret = ret.replaceAll("%addedText%", addedText);
		}
		if(version != null) {
			ret = ret.replaceAll("%version%", version);
		}
		if(description != null) {
			ret = ret.replaceAll("%description%", description);
		}
		if(targetPlayer != null) {
			ret = ret.replaceAll("%targetPlayer%", targetPlayer);
		}
		if(noteTaker != null) {
			ret = ret.replaceAll("%noteTaker%", noteTaker);
		}
		if(note != null) {
			ret = ret.replaceAll("%note%", note);
		}
		if(location != null) {
			ret = ret.replaceAll("%loc%", location.toString());
			ret = ret.replaceAll("%loc.world%", location.getWorld().getName());
			ret = ret.replaceAll("%loc.x%", Integer.toString(location.getBlockX()));
			ret = ret.replaceAll("%loc.y%", Integer.toString(location.getBlockY()));
			ret = ret.replaceAll("%loc.z%", Integer.toString(location.getBlockZ()));
		}
		if(world != null) {
			ret = ret.replaceAll("%world%", world.getName());
		}
		if(e != null) {
			ret = ret.replaceAll("%ex%", e.toString());
			ret = ret.replaceAll("%ex.type%", e.getClass().toString());
			ret = ret.replaceAll("%ex.message%", e.getMessage().toString());
		}
		if(page != null) {
			ret = ret.replaceAll("%page%", Integer.toString(page));
		}
		if(numPages != null) {
			ret = ret.replaceAll("%numPages%", Integer.toString(numPages));
		}
		if(noteID != null) {
			ret = ret.replaceAll("%noteID%", Integer.toString(noteID));
		}
		if(timestamp != null) {
			ret = ret.replaceAll("%timestamp%", timestamp.toString());
			ret = ret.replaceAll("%time%", String.format("%02d:%02d:%02d", timestamp.getHours(), timestamp.getMinutes(), timestamp.getSeconds()));
			ret = ret.replaceAll("%date%", String.format("%04d-%02d-%02d", (timestamp.getYear() + 1900), (timestamp.getMonth() + 1), timestamp.getDate()));
		}
		
		return ret;
	}
}
