package com.mcnsa.notepad;

import org.bukkit.plugin.java.JavaPlugin;

//import com.mcnsa.notepad.managers.DatabaseManager;
import com.mcnsa.notepad.managers.CommandManager;
import com.mcnsa.notepad.managers.ConfigurationManager;
import com.mcnsa.notepad.managers.DatabaseManager;
import com.mcnsa.notepad.managers.PermissionsManager;
import com.mcnsa.notepad.utilities.DatabaseTableInfo;
import com.mcnsa.notepad.utilities.Logger;
import com.mcnsa.notepad.utilities.MultilineChatEntry;

public class MCNSANotepad extends JavaPlugin {	
	// keep track of ourself
	static MCNSANotepad instance = null;
	
	// our managers
	PermissionsManager permissionsManager = null;
	DatabaseManager databaseManager = null;
	ConfigurationManager configurationManager = null;
	
	// our command handler
	CommandManager commandManager = null;
	
	// our chat handler
	MultilineChatEntry multilineChatEntry = null;
	
	public MCNSANotepad() {
		MCNSANotepad.instance = this;
	}
	
	public void onEnable() {
		// initialize our permissions manager
		permissionsManager = new PermissionsManager();

		// build our notes table
		DatabaseTableInfo notesTable = new DatabaseTableInfo();
		notesTable.name = "notes";
		notesTable.fields = new String[]{
				"date TIMESTAMP",
				"noteTaker TINYTEXT",
				"notee TINYTEXT",
				"note TEXT"
		};
		DatabaseManager.addTableConstruct(notesTable);
		
		// initialize our database manager
		databaseManager = new DatabaseManager();
		
		// set our command executors
		commandManager = new CommandManager();
		this.getCommand("note").setExecutor(commandManager);
		this.getCommand("notes").setExecutor(commandManager);
		this.getCommand("notepad").setExecutor(commandManager);
		
		// load our multiline chat handler
		multilineChatEntry = new MultilineChatEntry();
		
		// load our configuration
		//this.saveDefaultConfig();
		configurationManager = new ConfigurationManager(this.getConfig());
		this.saveConfig();
		
		// let them now!
		Logger.log("&aPlugin enabled!");
	}
	
	public void onDisable() {
		// shutdown
		try {
			databaseManager.disable();
		}
		catch(Exception e) {
			Logger.error("Failed to disable database manager (%s)!", e.getMessage());
		}
		Logger.log("plugin disabled");
	}
	
	public static MCNSANotepad getInstance() {
		return instance;
	}
}
