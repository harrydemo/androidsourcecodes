/*
 * This file is part of NetworkMonitor copyright Dave Edwards (eddyspace.com)
 * 
 * Firstly thanks to all and sundry for the tutorials and forum threads I've used
 * in putting this together.
 *
 * Where I've copied whole chunks of stuff I've tried to attribute it properly.
 * Otherwise, I've at least typed it in and munged it to suit which makes it 
 * mine I guess.
 *
 * As far as it goes: Network Monitor is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation, either version 3 of the License, 
 * or (at your option) any later version.
 * 
 * Network Monitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * Please see  <a href='http://www.gnu.org/licenses/'>www.gnu.org/licenses</a> 
 * for a copy of the GNU General Public License.
 */

package com.eddyspace.networkmonitor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Returns an object that can be used to manage a log file.
 * 
 * The log file is assumed to be csv separated text file and a header row is
 * inserted when the file is created.
 * 
 * @author davo
 *
 */
public class LogAdapter {
    private static final String LOG_TAG = "NetworkMonitor.LogAdapter";
	private final static boolean localLOGV = true;

	public static final String PREFS_NAME = "com.eddyspace.networkmonitor_preferences";
	private static final String LOG_DIR = "/sdcard";
    private static String LOG_NAME = "";
    private SharedPreferences prefs = null;
    private boolean logExists = false;
    private File logFile = null;
    private String logFileSize = null;
    private Context context;        
	private String[] logLables;

    /**
     * Constructor for Log Adaptor
     * 
     * @param ctx a context for the application used to get shared preferences
     */
    public LogAdapter(Context ctx ) {
    	if(localLOGV) Log.v(LOG_TAG, "Log adaptor constructor");
    	context = ctx;
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE );
        LOG_NAME = LOG_DIR + "/" + prefs.getString("preference_log_name", context.getString(R.string.preference_log_name_default));
        if(localLOGV) Log.v(LOG_TAG, "Log file name is: " + LOG_NAME);
        
        // Make sure the file exists and is writable
        logFile = new File(LOG_NAME);
        logLables = new LableMap(context).getLableList();        
        if( getWriteableFile() ) {
        	logExists = true;
        }
        long size = logFile.length();
        logFileSize = Tools.insertCommas(String.valueOf(size / 1024));        
    }
    
    /**
     * Clears the log file completely and creates a new one
     * 
     * @return boolean success or failure
     */
    public boolean clearLog() {
    	if(localLOGV) Log.v(LOG_TAG, "Clearing log file");    	
    	if( logExists ) {    		
        	try {
        		if( logFile.delete() ) {
        			if(localLOGV) Log.v(LOG_TAG, "Line file deleted");
        			return getWriteableFile();			
        		}
        	} catch (SecurityException e) {
        		Log.w(LOG_TAG, "Security Exception on delete file: " + e.getMessage());
        	}        	
    	}
    	if(localLOGV) Log.v(LOG_TAG, "Unable to delete log file");
    	return false;    	
    }    

    /**
     * Append a line to the log file but only if the third field is different
     * than the third field of the last line in the file.
     * <p>
     * This method was written to reduce extraneous log entries caused by the
     * phone sleeping and waking.
     *  
     * @param  line    the comma separated log line to append.
     * @return boolean success or failure
     */
    public boolean appendUniqueLine(String line) {    	
    	if(localLOGV) Log.v(LOG_TAG, "Append Unique Log line");
    	boolean appended = false;
    	String lastLine = Tools.lastLine(LOG_NAME);
    	String[] lineList = line.split(",");
    	String[] lastLineList = lastLine.split(",");
    	
    	if( ! lineList[2].equals(lastLineList[2]) ) {
    		try{        	
    			BufferedWriter out = new BufferedWriter(new FileWriter(LOG_NAME,true));
            	try {
            		out.write(line + System.getProperty("line.separator"));
                	appended = true;
                	if(localLOGV) Log.v(LOG_TAG, "Line appended to log");
            	} catch (IOException e){	//Catch exception if any
            		Log.w(LOG_TAG,"File Write Error: " + e.getMessage());    
            	} finally {
            		out.close();
            	}    			
    		}catch (IOException e){	//Catch exception if any
    			Log.w(LOG_TAG,"File Open Error: " + e.getMessage());    
    		}
    	} else {
    		if(localLOGV) Log.v(LOG_TAG, "Line not unique: Old=" + lastLineList[2] + " new=" + lineList[2]);
    	}
    	return appended;
	}    
    
    /**
     * Append a line to the log file unconditionally
     * <p>
     * The method I used when first constructing this helper class
     *  
     * @param  line    the comma separated log line to append.
     * @return boolean success or failure
     */
    public boolean appendLine(String line) {
    	boolean appended = false;
        try{        	
        	BufferedWriter out = new BufferedWriter(new FileWriter(LOG_NAME,true));
        	try {
        		out.write(line + System.getProperty("line.separator"));
            	appended = true;
            	if(localLOGV) Log.v(LOG_TAG, "Line appended to log");
        	} catch (IOException e){	//Catch exception if any
        		Log.w(LOG_TAG,"File Write Error: " + e.getMessage());    
        	} finally {
        		out.close();
        	}
    	}catch (IOException e){	//Catch exception if any
    		Log.w(LOG_TAG,"File Open Error: " + e.getMessage());    
    	}
    	return appended;
	}    
    
    /**
     * Return a list of dates contained in the file.
     * <p>
     * The purpose of this is to read the whole file and only return
     * unique dates.  This enables some selectivity in the readlog routines
     * which can list the dates and then display the entries on that date.
     * <p>
     * The idea is to reduce the overhead when reading large log files.
     *
     * @return ArrayList of string dates contained in the file
     */    
    public ArrayList<String> getDates() {
    	ArrayList<String> list = new ArrayList<String>();    	
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(LOG_NAME)));
            String strLine;            
            while ((strLine = br.readLine()) != null)   {
            	String[] s = strLine.split(",");
           		if( ! list.contains(s[0]) ) {
           			list.add(s[0]);
           		}
            }            
            br.close();            
        } catch (Exception e){	//Catch exception if any
              Log.w(LOG_TAG, "Error: " + e.getMessage());
        }        
		return list;    	
    }
    
    /**
     * Return lines from the log matching a specific date in the first field 
     * 
     * @param  date date to match in the first field of each line of the log
     * @return ArrayList of string lines from the log file matching the date
     */
    public ArrayList<String> getLinesByDate( String date ) {
    	ArrayList<String> list = new ArrayList<String>();    	 
        try {             
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(LOG_NAME)));
            String strLine;            
            while ((strLine = br.readLine()) != null)   {
            	String[] s = strLine.split(",");
            	if( date.equals(s[0]) ) {
            		list.add(strLine);
            	}
            }            
            br.close();
        } catch (Exception e){	//Catch exception if any
              Log.w(LOG_TAG, "Error: " + e.getMessage());
        }
		return list;		    	
    }
    
    /**
     * To retrieve the whole file as an ArrayList of strings
     * <p>
     * This is not very scalable and if you use it, there would be some
     * reasonable limits set
     * 
     * @return an ArrayList of all the lines from the log
     */
    public ArrayList<String> getAllLines() {
    	ArrayList<String> list = new ArrayList<String>();    	
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(LOG_NAME)));
            String strLine;            
            while ((strLine = br.readLine()) != null)   {
        		list.add(strLine);
            }            
            br.close();            
        } catch (Exception e){	//Catch exception if any
              Log.w(LOG_TAG, "Error: " + e.getMessage());
        }        
		return list;    	
    }
    
    /**
     * Return the approximate size of the log file in Kb rounded to the nearest Kb.
     * 
     * @return String representation of the size (eg "3Kb")
     */
    public String getLogSize() {
    	return logFileSize + " Kb";		    	
    }

    /**
     * Return the path to the log file
     *  
     * @return String full path to the log file
     */
    public String getLogPath() {
    	if(localLOGV) Log.v(LOG_TAG, "getLogPath");
    	return(LOG_NAME);
    }
    
    /**
     * Return a list of the headings for the fields in the csv log
     * 
     * @return String array of the field headers
     */
    public String[] getLogLables() {
    	return logLables;
    }
    
    /**
     * Create the log file and insert the header line
     * 
     * @return boolean success or failure
     */
    public boolean getWriteableFile() {
    	if(localLOGV) Log.v(LOG_TAG, "getWriteableFile");
    	try {    		
    		if( logFile.createNewFile() ) {
    			if(localLOGV) Log.v(LOG_TAG, "Created new file");
    			appendLine(Tools.join(",", logLables));    			
    		}
    	} catch (IOException e) {    	
    		Log.e(LOG_TAG, "IO Exception: " + e.getMessage());
    		throw new RuntimeException(e.getMessage());
    	} catch (SecurityException e) {
    		Log.e(LOG_TAG, "Security Exception: " + e.getMessage());
    		throw new RuntimeException(e.getMessage());
    	}
    	return logFile.exists() && logFile.canWrite();    	
    }   
}

