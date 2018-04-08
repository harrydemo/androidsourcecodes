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
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Class to hold supporting routines for Network Monitor
 * 
 * @author davo
 *
 */
public class Tools {
	private static final String LOG_TAG = "NetworkMonitor.Tools";
	private final static boolean localLOGV = false;
	private final static String PREFS_NAME = "com.eddyspace.networkmonitor_preferences";	
		
	/**
	 * Implements "system" calls using reflection
	 * <p>
	 * Many thanks to gimite.net for this routine.  Only used because
	 * android.os.Exec is not included anymore.
	 * <p>
	 * I couldn't find any other way to do a lot of this work but I'd
	 * be more than happy for hints as to other methods that don't include
	 * system calls..
	 * 
	 * @param cmd 	the command to run in the system call
	 * @param arg1	first argument for the command
	 * @param arg2	second argument for the command
	 * @return		String result of running the command
	 */
    public static String mySystem(String cmd, String arg1, String arg2) {
    	try {

    	    Class<?> execClass = Class.forName("android.os.Exec");
    	    Method createSubprocess = execClass.getMethod("createSubprocess",
    	            String.class, String.class, String.class, int[].class);
    	    Method waitFor = execClass.getMethod("waitFor", int.class);
    	    
    	    // Executes the command.
    	    // NOTE: createSubprocess() is asynchronous.
    	    int[] pid = new int[1];    	     
    	    FileDescriptor fd = (FileDescriptor)createSubprocess.invoke(
    	            null, cmd, arg1, arg2, pid);
    	    
    	    // Reads stdout.
    	    // NOTE: You can write to stdin of the command using new FileOutputStream(fd).
    	    FileInputStream in = new FileInputStream(fd);
    	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    	    String output = "";
    	    try {
    	        String line;
    	        while ((line = reader.readLine()) != null) {
    	            output += line + System.getProperty("line.separator");
    	        }
    	    } catch (IOException e) {
    	        // It seems IOException is thrown when it reaches EOF.
    	    }
    	    
    	    // Waits for the command to finish.
    	    waitFor.invoke(null, pid[0]);    	    
    	    return output;    	    
    	} catch (ClassNotFoundException e) {
    	    throw new RuntimeException(e.getMessage());
    	} catch (SecurityException e) {
    	    throw new RuntimeException(e.getMessage());
    	} catch (NoSuchMethodException e) {
    	    throw new RuntimeException(e.getMessage());
    	} catch (IllegalArgumentException e) {
    	    throw new RuntimeException(e.getMessage());
    	} catch (IllegalAccessException e) {
    	    throw new RuntimeException(e.getMessage());
    	} catch (InvocationTargetException e) {
    	    throw new RuntimeException(e.getMessage());
    	}	
    }

    /**
     * Return the name of an interface
     * 
     * @param intf	the interface to use
     * @return		String name of the interface
     */
    public static String getInterfaceName(NetworkInterface intf) {
    	return intf.getName().toString();
    }

    /**
     * return the ip address of an interface
     * 
     * @param intf	the interface to use
     * @return		String dotted decimal IP address
     */
    public static String getIPAddress( NetworkInterface intf) {
    	String result = "";
    	for( Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
    		InetAddress inetAddress = enumIpAddr.nextElement();
    		result = inetAddress.getHostAddress();
    	}
    	return result;
    }
    
    /**
     * Return a the first interface on the system that is not localhost
     * 
     * @return	NetworkInterface object
     */
    public static NetworkInterface getInternetInterface() {
    	try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				if( ! intf.equals(NetworkInterface.getByName("lo"))) {
					return intf;	
				}				
			}
		} catch (SocketException ex) {			
			Log.w(LOG_TAG,  ex.toString());
		}
        return null;
    } 
    
    /**
     * An implementation of the "join" construct (the opposite of split())
     * 
     * @param token		the text string to join the list together with
     * @param strings	list of strings to join together
     * @return			String result from the joining
     */
    public static String join( String token, String[] strings ) {
        StringBuffer sb = new StringBuffer();
       
        for( int x = 0; x < ( strings.length - 1 ); x++ ) {
            sb.append( strings[x] );
            sb.append( token );
        }
        sb.append( strings[ strings.length - 1 ] );       
        return( sb.toString() );
    }
    
    /**
     * An implementation of "commify" used to put commas in a numeric string
     * <p>
     * A neat re-entrant implementation from a forum somewhere.  Thanks, whoever 
     * you are!
     * 
     * @param str	the numeric string to commify
     * @return		String with commas inserted in the right places
     */
    public static String insertCommas(String str) {
    	if(str.length() < 4) {
    		return str;
    	}
    	return insertCommas(str.substring(0, str.length() - 3)) + "," + str.substring(str.length() - 3, str.length());
    }
    
    /**
     * Return the current date
     * 
     * @return String representation of todays date in dd/MM/yyyy (strftime()) format
     */
    public static String getTodaysDate() {
    	SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
    	Calendar cal = Calendar.getInstance();
    	return dfDate.format(cal.getTime());    	
    }
    
    /**
     * Return the current time in HH:mm:ss (strftime()) format
     * 
     * @return
     */
    public static String getTodaysTime() {
    	SimpleDateFormat dfDate = new SimpleDateFormat("HH:mm:ss");
    	Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    	return dfDate.format(cal.getTime());    	
    }
    
    /**
     * Simple routine to return a buffered reader for a web page.
     * <p>
     * Only used to get the external (natted) ip address from whatismyip.com
     * 
     * @param url
     * @return
     * @throws Exception
     */
    public static BufferedReader readFromWeb(String url) throws Exception {
		return new BufferedReader(
			new InputStreamReader(
				new URL(url).openStream()));
	}
    
    /**
     * Return the last line in a file
     * <p>
     * I wrote this myself because even though it seems like a common problem, there were
     * no simple answers.  This routine works in my case but it might not work in
     * others.  I posted this solution on forums.java.com as an alternative 
     * implementation
     * <p>
     * It opens the file for random access, seeks to the maxlinelength bytes from 
     * the end of the file and then reads lines till the end of the file 
     * returning the last line read.
     * 
     * @param fileName 	file to read
     * @return			String last line in the file
     */
    public static String lastLine (String fileName) {
    	String last = null;
    	long maxLineLength = 200;
    	RandomAccessFile raf = null;    	
    	try {
    		raf = new RandomAccessFile(fileName, "r");
    		long len;
			try {
				len = raf.length();
				if( len > maxLineLength ) {
					raf.seek(len - maxLineLength);
				}
				String s = "";
				while( (s = raf.readLine()) != null) {
					last = s;
				}
			} catch (IOException e) { 
				Log.w(LOG_TAG, e.toString());
			}    		    		
    	} catch (FileNotFoundException e) {
    		Log.w(LOG_TAG, e.toString());
    	} finally {
    		try {
				raf.close();
			} catch (IOException e) { 
				Log.w(LOG_TAG, e.toString());				
			}
    	}
    	return last;
    }
    
    public static void MakeTableLayout(Context context, Map<String,String> infoMap, TableLayout tl, String type) {       
        if(localLOGV) Log.v(LOG_TAG, "onCreate NetmonInfo");
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE );        
        
	    LableMap lm = new LableMap(context);
	    String[] lableList = lm.getLableList();    	
	    // Padding row to start
	    tl.addView(getPaddingRow(context), makeParams("row"));	    
	    for( String s : lableList ) {
	    	if(localLOGV) Log.v(LOG_TAG, "Adding row for " + s + " Item: " + lm.getLable(s) + " value: " + infoMap.get(s));
            tl.addView(getRow(lm.getLable(s), infoMap.get(s), context), makeParams("row"));
            tl.addView(getPaddingRow(context), makeParams("row"));
	    }
	    
        // Leave a gap before showing log file info
	    // Don't show for log entries..
	    if( type.equals("net_info") ) {
	    	LogAdapter la = new LogAdapter(context);
	    	tl.addView(getPaddingRow(context), makeParams("row"));         
	    	tl.addView(getPaddingRow(context), makeParams("row"));
	    	tl.addView(getRow(context.getString(R.string.key_log_path_name), la.getLogPath(), context), makeParams("row"));
	    	tl.addView(getPaddingRow(context), makeParams("row"));
	    	String logPref = context.getString(R.string.log_turned_off);
	    	if(prefs.getBoolean("preference_log", true)) {    	
	    		logPref = la.getLogSize();
	    	}
	    	tl.addView(getRow(context.getString(R.string.key_log_size_name), logPref, context), makeParams("row"));
	    	tl.addView(getPaddingRow(context), makeParams("row"));
    	
	    	String monPref = context.getString(R.string.monitoring_turned_off);
	    	if( prefs.getBoolean("preference_monitor",true) ) {
	    		monPref = prefs.getString("preference_monitor_type", context.getString(R.string.preference_monitor_type_default));
	    	}            
	    	tl.addView(getPaddingRow(context), makeParams("row"));
	    	tl.addView(getRow(context.getString(R.string.monitoring_using), monPref, context), makeParams("row"));
	    }
    }
    
    /**
     * The table layout for NetworkInfo
     * 
     * @param what
     * @return
     */
    private static TableLayout.LayoutParams makeParams(String what) {
    	TableLayout.LayoutParams lp = null;
    	
    	if(what.equals("cell")) {
    		lp = new TableLayout.LayoutParams(
    				LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
    	} else if( what.equals("row")) {
    		lp = new TableLayout.LayoutParams(
    				LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
    	} else if( what.equals("header_row")) {
    		lp = new TableLayout.LayoutParams(
    				LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
    	}
		return lp;
	}
    
    /**
     * Return a formatted padding row to display
     * 
     * @return Padding TableRow  
     */
    private static TableRow getPaddingRow(Context context) {        
        TableRow tr = new TableRow(context);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        
        TextView tv = new TextView(context);
        tv.setHeight(5);                
        tr.addView(tv);
        return tr;
    }
    
    /**
     * Return a Table row of data to display
     * 
     * @param item 	first column data
     * @param value	second column data
     * @return		TableRow formatted to display
     */
    private static TableRow getRow(String item, String value, Context context) {
		// The left hand cell is the lable for the item
        TableRow tr = new TableRow(context);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        
        TextView labelTV = new TextView(context);
        labelTV.setBackgroundColor(Color.BLUE);
        labelTV.setTextColor(Color.WHITE);
        labelTV.setPadding(5,1,1,1);
        labelTV.setText(item);                
        tr.addView(labelTV);

        // And the value
        TextView valueTV = new TextView(context);
        valueTV.setText(value);
        valueTV.setPadding(5,1,1,1);
        tr.addView(valueTV);
        return tr;
    }
}
