/**
 * OnionCoffee - Anonymous Communication through TOR Network
 * Copyright (C) 2005-2007 RWTH Aachen University, Informatik IV
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package TorJava;


// USAGE: Tor.log.logXXX(Logger.YY,"");

/**
 * central logging class for events in TorJava
 * 
 * CG - ported to Android platform
 * 
 * @author Lexi Pimenidis
 * @author Connell Gauld
 */
public class Logger {

	private static final String TagGeneral = "Tor";
	private static final String TagDirectory = "TorDirectory";
	private static final String TagTLS = "TorTLS";
	private static final String TagCircuit = "TorCircuit";
	private static final String TagStream = "TorStream";
	private static final String TagCell = "TorCell";
	private static final String TagCrypto = "TorCrypto";
//	private static final String TagHiddenService = "TorHiddenService";

	static final int GENERAL = 0;
	static final int DIRECTORY = 1;
	static final int TLS = 2;
	static final int CIRCUIT = 3;
	static final int STREAM = 4;
	static final int CELL = 5;
	static final int CRYPTO = 6;
	static final int HIDDENSERVICE = 7;

	public static final int NOLOG = 0; // log nothing
	public static final int ERROR = 1; // only for REAL errors
	public static final int WARNING = 2; // for errors that are non-critical
	public static final int INFO = 3; // for important messages
	public static final int VERBOSE = 4; // for detailed program flow
	public static final int RAW_DATA = 5; // for really detailed debugging
											// information

	static final String[] warningLevelToString = { "NoLog", "Error", "Warning",
			"Info", "Verbose", "Data" };

	static int LOG_GENERAL = INFO;
	static int LOG_DIRECTORY = WARNING;
	static int LOG_TLS = ERROR;
	static int LOG_CIRCUIT = WARNING;
	static int LOG_STREAM = WARNING;
	static int LOG_CELL = WARNING;
	static int LOG_CRYPTO = WARNING;
	static int LOG_HIDDENSERVICE = VERBOSE;

	static int LOG_FILE_GENERAL = INFO;
	static int LOG_FILE_DIRECTORY = VERBOSE;
	static int LOG_FILE_TLS = ERROR;
	static int LOG_FILE_CIRCUIT = INFO;
	static int LOG_FILE_STREAM = WARNING;
	static int LOG_FILE_CELL = WARNING;
	static int LOG_FILE_CRYPTO = WARNING;
	static int LOG_FILE_HIDDENSERVICE = VERBOSE;

	Logger() {

	}

	Logger(boolean noLocalFileSystemAccess) {
		// Here to prevent things breaking
	}

	void close() {
	}

//	private static void addToBugReport(String type, String message) {
		// TODO Implement bug reporting
		/*
		 * // init vairables, if neccessary if (bugReportInfo==null)
		 * bugReportInfo = new HashMap<String,Vector<String>>(); Vector<String>
		 * queue = null; // get queue (LIFO) for messages if
		 * (!bugReportInfo.containsKey(type)) { queue = new Vector<String>();
		 * bugReportInfo.put(type,queue); } else { queue =
		 * bugReportInfo.get(type); } // append message to end bugreport
		 * queue.add(message); // delete oldest messages,if report is growing
		 * too large if (queue.size()>maxLengthBugReport)
		 * queue.removeElementAt(0);
		 */
//	}

	public static String getBugReport() {
		/*
		 * if (bugReportInfo==null) return "empty";
		 * 
		 * StringBuffer sb = new StringBuffer(); Iterator i =
		 * bugReportInfo.keySet().iterator(); while(i.hasNext()) { String type =
		 * (String)i.next(); Vector queue = (Vector)(bugReportInfo.get(type));
		 * sb.append("\r\n--------------------"+type+"\r\n"); for(int
		 * j=0;j<queue.size();++j) { sb.append( (String)(queue.elementAt(j)) );
		 * sb.append( "\r\n" ); } sb.append( "\r\n" ); } return sb.toString();
		 */
		// TODO Implement bug reporting
		return "Bug reporting not implemented";
	}

	public static void logGeneral(int severity, String message) {
		/*
		 * if (severity <= severityBugReport) addToBugReport("General",message);
		 * if (severity <= LOG_GENERAL) //logString("General",severity,
		 * message); if (noOutputToFilesystem) return; if (severity <=
		 * LOG_FILE_GENERAL) logStringFile("General",severity, message);
		 */
		logString(TagGeneral, severity, message);
	}

	public static void logDirectory(int severity, String message) {
    	/*
				if (severity <= severityBugReport)
						addToBugReport("Dir",message);
        if (severity <= LOG_DIRECTORY)
            logString("Dir",severity, message);
        if (noOutputToFilesystem) return;
        if (severity <= LOG_FILE_DIRECTORY)
            logStringFile("Dir",severity, message);*/
    	logString(TagDirectory, severity, message);
    }

	public static void logTLS(int severity, String message) {
		/*if (severity <= severityBugReport)
			addToBugReport("TLS", message);
		if (severity <= LOG_TLS)
			logString("TLS", severity, message);
		if (noOutputToFilesystem)
			return;
		if (severity <= LOG_FILE_TLS)
			logStringFile("TLS", severity, message);*/
		logString(TagTLS, severity, message);
	}

	public static void logCircuit(int severity, String message) {
		/*if (severity <= severityBugReport)
			addToBugReport("Circuit", message);
		if (severity <= LOG_CIRCUIT)
			logString("Circuit", severity, message);
		if (noOutputToFilesystem)
			return;
		if (severity <= LOG_FILE_CIRCUIT)
			logStringFile("Circuit", severity, message);*/
		logString(TagCircuit, severity, message);
	}

	public static void logStream(int severity, String message) {
		/*if (severity <= severityBugReport)
			addToBugReport("Stream", message);
		if (severity <= LOG_STREAM)
			logString("Stream", severity, message);
		if (noOutputToFilesystem)
			return;
		if (severity <= LOG_FILE_STREAM)
			logStringFile("Stream", severity, message);*/
		logString(TagStream, severity, message);
	}

	public static void logCell(int severity, String message) {
		/*if (severity <= severityBugReport)
			addToBugReport("Cell", message);
		if (severity <= LOG_CELL)
			logString("Cell", severity, message);
		if (noOutputToFilesystem)
			return;
		if (severity <= LOG_FILE_CELL)
			logStringFile("Cell", severity, message);*/
		logString(TagCell, severity, message);
	}

	public static void logCrypto(int severity, String message) {
		/*if (severity <= severityBugReport)
			addToBugReport("Crypto", message);
		if (severity <= LOG_CRYPTO)
			logString("Crypto", severity, message);
		if (noOutputToFilesystem)
			return;
		if (severity <= LOG_FILE_CRYPTO)
			logStringFile("Crypto", severity, message);*/
		logString(TagCrypto, severity, message);
	}

	public static void logHiddenService(int severity, String message) {
		/*if (severity <= severityBugReport)
			addToBugReport("HiddenService", message);
		if (severity <= LOG_HIDDENSERVICE)
			logString("HiddenService", severity, message);
		if (noOutputToFilesystem)
			return;
		if (severity <= LOG_FILE_HIDDENSERVICE)
			logStringFile("HiddenService", severity, message);*/
		logString(TagCrypto, severity, message);
	}

	// **************************************************

	/*static private String finalizeMessage(String type, int severity,
			String message) {
		String time = new Date().toString();
		return time + " [" + type + "," + warningLevelToString[severity] + "] "
				+ message;
	}*/

	/*
	 * static synchronized private void logString(String type,int severity,
	 * String message) { if (redirectOutput instanceof javax.swing.JList) {
	 * JList list = (JList)redirectOutput; int index =
	 * list.getSelectedIndex()+1;
	 * ((DefaultListModel)list.getModel()).insertElementAt
	 * (finalizeMessage(type,severity, message),index);
	 * list.setSelectedIndex(index); list.ensureIndexIsVisible(index); } else if
	 * (redirectOutput instanceof javax.swing.JTextArea) {
	 * ((JTextArea)redirectOutput).append(finalizeMessage(type,severity,
	 * message)+"\n");
	 * ((JTextArea)redirectOutput).setCaretPosition(((JTextArea)redirectOutput
	 * ).getText().length()); } else if (redirectOutput instanceof
	 * java.awt.TextArea) {
	 * ((TextArea)redirectOutput).append(finalizeMessage(type,severity,
	 * message)+"\n"); } else {
	 * System.out.println(finalizeMessage(type,severity, message)); } }
	 */
	/*
	 * static synchronized private void logStringFile(String type,int severity,
	 * String message) { if (noOutputToFilesystem) return; if
	 * (TorConfig.getLogFilename() != null) { try { FileOutputStream logFile =
	 * new FileOutputStream(TorConfig.getLogFilename(),true); message =
	 * finalizeMessage(type,severity, message) + "\r\n";
	 * logFile.write(message.getBytes()); logFile.close(); } catch(IOException
	 * e) { TorConfig.setLogFilename(null);
	 * logGeneral(ERROR,"Error accessing Logfile "
	 * +TorConfig.getLogFilename()+":"+e.getMessage()); } }; }
	 */

	static synchronized private void logString(String tag, int severity,
			String msg) {
		/*switch (severity) {
		case ERROR:
			Log.e(tag, msg);
			break;
		case WARNING:
			Log.w(tag, msg);
			break;
		case INFO:
			Log.i(tag, msg);
			break;
		case VERBOSE:
			Log.v(tag, msg);
		default:
			break;
		}*/
	}
}
