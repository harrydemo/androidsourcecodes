package com.stickycoding.rokon;

import android.util.Log;

/**
 * Debug.java
 * Functions and helpers to aid debugging and engine reports.
 * DebugMode can be toggled to avoid using the print command.
 * 
 * @author Richard
 */
public class Debug {
	
	private static String tag = "Rokon";
	protected static boolean debugMode = true;
	
	public static String getDebugTag() {
		return tag;
	}
	
	/**
	 * Sets the tag to be used in LogCat debug messages
	 * If unset, it will default to "Rokon"
	 * 
	 * @param tag any valid String for LogCat tags
	 */
	public static void setDebugTag(String tag) {
		Debug.tag = tag;
	}
	
	/**
	 * Prints a warning to LogCat with information about the engine warning
	 * 
	 * @param source The source of the warning, such as function name
	 * @param message The message to be passed on
	 */
	public static void warning(String source, String message) {
		if(!debugMode) return;
		Log.w(tag, source + " - " + message);
		Exception e = new Exception(source + " - " + message);
		e.printStackTrace();
	}
	
	/**
	 * Prints a warning to LogCat with information about the engine warning
	 * 
	 * @param message The message to be passed on
	 */
	public static void warning(String message) {
		if(!debugMode) return;
		Log.w(tag, message);
	}
	
	/**
	 * Prints to the verbose stream of LogCat with information from the engine
	 * 
	 * @param message The message to be passed on
	 */
	public static void print(String message) {
		if(!debugMode) return;
		Log.v(tag, message);
	}
	
	/**
	 * Prints to the error stream of LogCat with information from the engine
	 * 
	 * @param message The message to be passed on
	 */
	public static void error(String message) {
		Log.e(tag, message);
		Exception e = new Exception(message);
		e.printStackTrace();
	}
	
	/**
	 * Prints to the verbose stream of LogCat, with information that comes in when in Rokon.verboseMode
	 * 
	 * @param method
	 * @param message
	 */
	public static void verbose(String method, String message) {
		if(!debugMode) return;
		Log.v(tag, method + " - " + message);
		if(!debugMode) return;
	}
	
	/**
	 * Prints to the verbose stream of LogCat, with information that comes in when in Rokon.verboseMode
	 * 
	 * @param message
	 */
	public static void verbose(String message) {
		if(!debugMode) return;
		Log.v(tag, message);
	}
	
	/**
	 * Forces the application to exit, this is messy, unsure if it should be used. For debugging purposes while testing, it will be.
	 */
	public static void forceExit() {
		System.exit(0);
	}

}
