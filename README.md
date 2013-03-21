MCNSANotepad
================

A notepad for moderators and admins. Note down stuff about players.

## Commands

<table>
	<tr>
		<th>Command</th>
		<th>Permissions</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>/note &lt;player&gt; [note]</td>
		<td>mcnsanotepad.writenotes</td>
		<td>Adds a note about the given player. If command issuer is a player and [note] is blank / not provided, user will enter multiline text mode. In this mode, every chat message the send will add to the note string, until they enter the line 'done' by itself. They can also enter the line 'cancel' to cancel the command and text entry. Console <b>must</b> use the [note] parameter</td>
	</tr>
	<tr>
		<td>/notes &lt;player&gt; [page]</td>
		<td>mcnsanotepad.viewnotes</td>
		<td>Reads notes about a given player. Will pageinate results for easier reading.</td>
	</tr>
	<tr>
		<td>/notepad [version]</td>
		<td>mcnsanotepad.information</td>
		<td>Displays a help menu on the usage of the notepad commands or optionally, a version string.</td>
	</tr>
</table>

## Configuration
A default configuration will be generated upon first run of the plugin. Just about all aspects of the plugin are configurable in this file, including all the messages that ever get relayed to players. Configuration nodes should generally be self-explanatory. Any configuration nodes that end id "-string" are strings that will be sent to the player, after context-specific replacements have been done. Colour codes can be used according to the following table:

<table>
	<tr>
		<th>Code</th>
		<th>Colour</th>
	</tr>
	<tr>
		<td>&0</td>
		<td>Black</td>
	</tr>
	<tr>
		<td>&1</td>
		<td>Dark blue</td>
	</tr>
	<tr>
		<td>&2</td>
		<td>Dark green</td>
	</tr>
	<tr>
		<td>&3</td>
		<td>Dark aqua</td>
	</tr>
	<tr>
		<td>&4</td>
		<td>Dark red</td>
	</tr>
	<tr>
		<td>&5</td>
		<td>Purple</td>
	</tr>
	<tr>
		<td>&6</td>
		<td>Gold</td>
	</tr>
	<tr>
		<td>&7</td>
		<td>Grey</td>
	</tr>
	<tr>
		<td>&8</td>
		<td>Dark grey</td>
	</tr>
	<tr>
		<td>&9</td>
		<td>Blue</td>
	</tr>
	<tr>
		<td>&a</td>
		<td>Green</td>
	</tr>
	<tr>
		<td>&b</td>
		<td>Aqua</td>
	</tr>
	<tr>
		<td>&c</td>
		<td>Red</td>
	</tr>
	<tr>
		<td>&d</td>
		<td>Pink</td>
	</tr>
	<tr>
		<td>&e</td>
		<td>Yellow</td>
	</tr>
	<tr>
		<td>&f</td>
		<td>White</td>
	</tr>
	<tr>
		<td>&k</td>
		<td>Obfuscated</td>
	</tr>
	<tr>
		<td>&l</td>
		<td>Bold</td>
	</tr>
	<tr>
		<td>&m</td>
		<td>Strikethrough</td>
	</tr>
	<tr>
		<td>&n</td>
		<td>Underline</td>
	</tr>
	<tr>
		<td>&o</td>
		<td>Italic</td>
	</tr>
	<tr>
		<td>&r</td>
		<td>Reset</td>
	</tr>
</table>

Further, the following replacements can be used (note that not all replacements will be active all the time - they are context-specific):

<table>
	<tr>
		<th>Code</th>
		<th>Replacement</th>
	</tr>
	<tr>
		<td>%commandSender%</td>
		<td>The name of the person who issued the command</td>
	</tr>
	<tr>
		<td>%command%</td>
		<td>The command issued</td>
	</tr>
	<tr>
		<td>%usage%</td>
		<td>A usage string for the command</td>
	</tr>
	<tr>
		<td>%addedText%</td>
		<td>The recently typed line by the player in multiline text mode</td>
	</tr>
	<tr>
		<td>%version%</td>
		<td>The plugin version</td>
	</tr>
	<tr>
		<td>%description%</td>
		<td>A command description</td>
	</tr>
	<tr>
		<td>%targetPlayer%</td>
		<td>The name of a player targeted by a command</td>
	</tr>
	<tr>
		<td>%noteTaker%</td>
		<td>The name of whoever originally wrote the note</td>
	</tr>
	<tr>
		<td>%note%</td>
		<td>A note about a player</td>
	</tr>
	<tr>
		<td>%ex%</td>
		<td>An exception</td>
	</tr>
	<tr>
		<td>%ex.type%</td>
		<td>The type of exception that occurred</td>
	</tr>
	<tr>
		<td>%ex.message%</td>
		<td>The exception's message</td>
	</tr>
	<tr>
		<td>%page%</td>
		<td>A page number</td>
	</tr>
	<tr>
		<td>%numPages%</td>
		<td>The total number of pages</td>
	</tr>
	<tr>
		<td>%noteID%</td>
		<td>A note's ID #</td>
	</tr>
	<tr>
		<td>%timestamp%</td>
		<td>A timestamp with the date and time</td>
	</tr>
	<tr>
		<td>%date%</td>
		<td>A date formatted as YYYY-MM-DD</td>
	</tr>
	<tr>
		<td>%time%</td>
		<td>A time formatted as HH:MM:SS</td>
	</tr>
</table>