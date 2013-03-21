package com.mcnsa.notepad.utilities;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mcnsa.notepad.MCNSANotepad;
import com.mcnsa.notepad.annotations.CustomString;
import com.mcnsa.notepad.exceptions.CommandException;

public class PlayerMatcher implements Listener {
	@CustomString(node = "player-not-found") public static String playerNotFound = "&cError: Player '%targetPlayer%' not found!";
	public static HashMap<String, OfflinePlayer> archivedPlayers = new HashMap<String, OfflinePlayer>();
	
	public PlayerMatcher(MCNSANotepad plugin) {
		// register our event handler
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		
		// get all our offline players
		OfflinePlayer[] offlinePlayers = Bukkit.getServer().getOfflinePlayers();
		
		// build our hashmap
		archivedPlayers.clear();
		for(OfflinePlayer player: offlinePlayers) {
			archivedPlayers.put(player.getName(), player);
		}
	}
	
	// utility function
	private static boolean everLoggedOn(String name) {
		return archivedPlayers.containsKey(name);
	}
	
	// event handler for joining players
	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		// track new players
		if(!event.getPlayer().hasPlayedBefore()) {
			archivedPlayers.put(event.getPlayer().getName(), event.getPlayer());
		}
	}
	
	public static OfflinePlayer matchPlayer(String name) throws CommandException {
		// try to match an online player
		Player matched = Bukkit.getServer().getPlayer(name);
		
		if(matched != null) {
			// found an online player
			return matched;
		}
		
		// see if they have ever logged on before
		if(everLoggedOn(name)) {
			// yup!
			return archivedPlayers.get(name);
		}
		
		throw new CommandException(new CustomStringContext(playerNotFound).setTargetPlayer(name));
	}
}
