package com.mcnsa.notepad.utilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import com.mcnsa.notepad.MCNSANotepad;
import com.mcnsa.notepad.annotations.CustomString;
import com.mcnsa.notepad.exceptions.CommandException;
import com.mcnsa.notepad.exceptions.DatabaseException;
import com.mcnsa.notepad.interfaces.CommandHandler;
import com.mcnsa.notepad.managers.CommandManager;

public class MultilineChatEntry implements Listener {
	@CustomString(node = "multiline-chat.cancelled") public static String chatCancelled = "&aText entry cancelled";
	@CustomString(node = "multiline-chat.added-text") public static String addedText = "&9Added text: &f%addedText%";
	@CustomString(node = "multiline-chat.already-entering") public static String alreadyEntering = "&cYou are already entering multiline text!";
	@CustomString(node = "multiline-chat.begin-entering") public static String beginEntering = "&aYou are now entering multiline text. Continue entering text, line-by-line, until you're done. When you're done, send 'done' by itself on its own line (or 'cancel' to stop).";
	
	public MultilineChatEntry() {
		// register our events
		Bukkit.getServer().getPluginManager().registerEvents(this, MCNSANotepad.getInstance());
	}
	
	// utility function to determine if someone has godmode or not
	private static boolean isEnteringMultilineChat(Player player) {
		// if any of our metadata values come back as true,
		// we have god mode on
		for(MetadataValue val: player.getMetadata("mlnEnabled")) {
			if(val.asBoolean()) {
				return true;
			}
		}
		
		// guess not!
		return false;
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		// see if they player is entering chat
		Player player = event.getPlayer();
		if(isEnteringMultilineChat(player)) {
			// yup!
			// capture it
			String line = event.getMessage();
			
			// cancel it!
			event.setCancelled(true);
			
			if(line.equals("done")) {
				onDone(player);
			}
			else if(line.equals("cancel")){
				onCancel(player);
			}
			else {
				String mlText = "";
				for(MetadataValue val: player.getMetadata("mlnText")) {
					mlText = val.asString();
					break;
				}
				
				mlText += " " + line;
				
				// remove the old mlText
				player.removeMetadata("mlnText", MCNSANotepad.getInstance());
				// add the new mlText
				player.setMetadata("mlnText", new FixedMetadataValue(MCNSANotepad.getInstance(), mlText));
				
				ColourHandler.sendMessage(player, (new CustomStringContext(addedText)).setCommandSender(player).setAddedText(line).toString());
			}
		}
	}
	
	public static void onDone(Player player) {
		// get the various meta datas
		String mlText = "";
		for(MetadataValue val: player.getMetadata("mlnText")) {
			mlText = val.asString();
			break;
		}
		CommandHandler mlOnDone = null;
		for(MetadataValue val: player.getMetadata("mlnOnDone")) {
			// see if it's ours
			Class<?>[] interfaces = val.value().getClass().getInterfaces();
			boolean isOurs = false;
			for(Class<?> face: interfaces) {
				//Logger.debug("onDone interface: " + face.getName());
				if(face.equals(CommandHandler.class)) {
					isOurs = true;
					break;
				}
			}
			if(!isOurs) {
				// nope
				return;
			}
			
			mlOnDone = (CommandHandler)val.value();
			break;
		}
		Object[] mlArgs = null;
		for(MetadataValue val: player.getMetadata("mlnArgs")) {
			mlArgs = (Object[])val.value();
			break;
		}
		
		// and call it
		try {
			mlOnDone.onChatComplete(player, mlText.trim(), mlArgs);
		}
		catch(CommandException e) {
			ColourHandler.sendMessage(player, e.getContext().setCommandSender(player).toString());
		} catch (DatabaseException e) {
			ColourHandler.sendMessage(player, ((new CustomStringContext(CommandManager.databaseError)).setCommandSender(player).setException(e)).toString());
			e.printStackTrace();
		}
		finally {
			// destroy all the meta datas
			player.removeMetadata("mlnEnabled", MCNSANotepad.getInstance());
			player.removeMetadata("mlnOnDone", MCNSANotepad.getInstance());
			player.removeMetadata("mlnText", MCNSANotepad.getInstance());
			player.removeMetadata("mlnArgs", MCNSANotepad.getInstance());
		}
	}
	
	public static void onCancel(Player player) {
		// destroy all the meta datas
		player.removeMetadata("mlnEnabled", MCNSANotepad.getInstance());
		player.removeMetadata("mlnOnDone", MCNSANotepad.getInstance());
		player.removeMetadata("mlnText", MCNSANotepad.getInstance());
		player.removeMetadata("mlnArgs", MCNSANotepad.getInstance());
		
		// tell them
		ColourHandler.sendMessage(player, (new CustomStringContext(chatCancelled)).setCommandSender(player).toString());
	}

	public static void scheduleMultilineTextEntry(Player player, CommandHandler onDone, Object... args) throws CommandException {
		// make sure they're not already doing entering text
		if(isEnteringMultilineChat(player)) {
			throw new CommandException((new CustomStringContext(alreadyEntering)).setCommandSender(player));
		}

		// set the player's metadata
		player.setMetadata("mlnEnabled", new FixedMetadataValue(MCNSANotepad.getInstance(), true));
		player.setMetadata("mlnOnDone", new FixedMetadataValue(MCNSANotepad.getInstance(), onDone));
		player.setMetadata("mlnText", new FixedMetadataValue(MCNSANotepad.getInstance(), new String("")));
		player.setMetadata("mlnArgs", new FixedMetadataValue(MCNSANotepad.getInstance(), args));
		
		// and tell them
		ColourHandler.sendMessage(player, (new CustomStringContext(beginEntering)).setCommandSender(player).toString());
	}
}
