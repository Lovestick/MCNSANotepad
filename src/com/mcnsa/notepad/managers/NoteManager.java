package com.mcnsa.notepad.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.command.CommandSender;

import com.mcnsa.notepad.exceptions.DatabaseException;

public class NoteManager {
	public class Note {
		public Integer noteID;
		public String noteTaker;
		public String notee;
		public Timestamp date;
		public String note;
	}
	
	public int writeNote(CommandSender sender, String targetPlayer, String note) throws DatabaseException {
		// add the note
		return DatabaseManager.updateQuery(
				"insert into notes (id, date, noteTaker, notee, note) values (NULL, ?, ?, ?, ?);",
				new Timestamp(System.currentTimeMillis()),
				sender.getName(),
				targetPlayer,
				note);
	}
	
	public int editNote(Integer noteID, String note) throws DatabaseException {
		return DatabaseManager.updateQuery(
				"update notes set note=? where id=?;",
				note,
				noteID);
	}
	
	public int editDate(Integer noteID, Timestamp date) throws DatabaseException {
		return DatabaseManager.updateQuery(
				"update notes set date=? where id=?;",
				date,
				noteID);
	}
	
	public ArrayList<Note> getPlayerNotes(String targetPlayer) throws DatabaseException {
		// run our query
		ArrayList<HashMap<String, Object>> results = DatabaseManager.accessQuery(
				"select * from notes where notee=? order by date desc;",
				targetPlayer);
		
		ArrayList<Note> notes = new ArrayList<Note>();
		for(HashMap<String, Object> result: results) {
			// format the results into a note class
			Note note = new Note();
			note.noteID = (Integer)result.get("id");
			note.noteTaker = (String)result.get("noteTaker");
			note.notee = (String)result.get("notee");
			note.date = (Timestamp)result.get("date");
			note.note = (String)result.get("note");
			
			// add our note to the list
			notes.add(note);
		}
		
		return notes;
	}
}
