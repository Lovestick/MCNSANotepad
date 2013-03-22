package com.mcnsa.notepad.managers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mcnsa.notepad.annotations.CustomString;
import com.mcnsa.notepad.commands.EditNote;
import com.mcnsa.notepad.commands.Information;
import com.mcnsa.notepad.commands.NoteDate;
import com.mcnsa.notepad.commands.ViewNotes;
import com.mcnsa.notepad.commands.WriteNote;
import com.mcnsa.notepad.exceptions.CommandException;
import com.mcnsa.notepad.exceptions.CommandUsageException;
import com.mcnsa.notepad.exceptions.DatabaseException;
import com.mcnsa.notepad.exceptions.PermissionException;
import com.mcnsa.notepad.utilities.ColourHandler;
import com.mcnsa.notepad.utilities.CustomStringContext;

public class CommandManager implements CommandExecutor {
	@CustomString(node = "database-error") public static String databaseError = "&cDatabase error (%ex.message%)";
	@CustomString(node = "proper-usage") public static String usage = "&6Usage: %usage%";
	@CustomString(node = "no-permission-command") public static String noPermissionString = "&cYou don't have permission to do that!";

	private WriteNote writeNoteCommand = new WriteNote();
	private EditNote editNoteCommand = new EditNote();
	private NoteDate noteDateCommand = new NoteDate();
	private ViewNotes viewNotesCommand = new ViewNotes();
	private Information informationCommand = new Information();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			if(command.getName().equalsIgnoreCase("note")) {
				if(!PermissionsManager.playerHasPermission(sender, "writenotes")) {
					throw new PermissionException("writenotes");
				}
				writeNoteCommand.onExecute(sender, args);
			}
			else if(command.getName().equalsIgnoreCase("editnote")) {
				if(!PermissionsManager.playerHasPermission(sender, "editnotes")) {
					throw new PermissionException("editnotes");
				}
				editNoteCommand.onExecute(sender, args);
			}
			else if(command.getName().equalsIgnoreCase("notedate")) {
				if(!PermissionsManager.playerHasPermission(sender, "editnotes")) {
					throw new PermissionException("editnotes");
				}
				noteDateCommand.onExecute(sender, args);
			}
			else if(command.getName().equalsIgnoreCase("editnote")) {
				if(!PermissionsManager.playerHasPermission(sender, "editnotes")) {
					throw new PermissionException("editnotes");
				}
				editNoteCommand.onExecute(sender, args);
			}
			else if(command.getName().equalsIgnoreCase("notes")) {
				if(!PermissionsManager.playerHasPermission(sender, "viewnotes")) {
					throw new PermissionException("viewnotes");
				}
				viewNotesCommand.onExecute(sender, args);
			}
			else if(command.getName().equalsIgnoreCase("notepad")) {
				if(!PermissionsManager.playerHasPermission(sender, "information")) {
					throw new PermissionException("information");
				}
				informationCommand.onExecute(sender, args);
			}
		}
		catch(PermissionException e) {
			ColourHandler.sendMessage(sender, (new CustomStringContext(noPermissionString)).toString());
			return false;
		}
		catch(CommandException e) {
			ColourHandler.sendMessage(sender, e.getContext().setCommand(command.getName()).setCommandSender(sender).toString());
			return false;
		} catch (CommandUsageException e) {
			ColourHandler.sendMessage(sender, e.getContext().setCommand(command.getName()).setCommandSender(sender).toString());
			ColourHandler.sendMessage(sender, (new CustomStringContext(usage)).setCommand(command.getName()).setCommandSender(sender).setUsage(e.getUsage().toString()).toString());
			return false;
		} catch (DatabaseException e) {
			ColourHandler.sendMessage(sender, ((new CustomStringContext(databaseError)).setCommand(command.getName()).setCommandSender(sender).setException(e)).toString());
			return false;
		}
		return true;
	}
}
