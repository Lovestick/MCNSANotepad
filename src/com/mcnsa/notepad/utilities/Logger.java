package com.mcnsa.notepad.utilities;

public class Logger {
	// get the minecraft logger
	static java.util.logging.Logger log = java.util.logging.Logger.getLogger("Minecraft");

	public static java.util.logging.Logger log() {
		return log;
	}

	// for simpler logging
	public static void log(String format, Object... args) {
		log(String.format(format, args));
	}
	public static void log(String info) {
		ColourHandler.consoleMessage("&f[&6MCNSANotepad&f] " + info);
	}

	// for error reporting
	public static void warning(String format, Object... args) {
		warning(String.format(format, args));
	}
	public static void warning(String info) {
		ColourHandler.consoleMessage("&f[&6MCNSANotepad&f] &e<WARNING> " + info);
	}

	// for error reporting
	public static void error(String format, Object... args) {
		error(String.format(format, args));
	}
	public static void error(String info) {
		ColourHandler.consoleMessage("&f[&6MCNSANotepad&f] &c<ERROR> " + info);
	}

	// for debugging
	// (disable for final release)
	public static void debug(String format, Object... args) {
		debug(String.format(format, args));
	}
	public static void debug(String info) {
		ColourHandler.consoleMessage("&f[&6MCNSANotepad&f] &9<DEBUG> " + info);
	}
}
