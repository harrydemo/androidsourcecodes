/*
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
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.widget.ArrayAdapter; 
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class NetworkTools extends ListActivity {
	private final static String LOG_TAG = "NetworkMonitor.NetworkTools";
	private final static boolean localLOGV = true;
	private final static String DNS_HOST_PING_PORT = "53";
	private final static String OTHER_HOST_PING_PORT = "80";
	private final static int PING_DEFAULT_PORT = 80;
	private final static int PING_TIMEOUT = 4000;
	private final static int PING_COUNT = 4;
	private final static int BUSY_DIALOG = 1;
	private final static int PINGING_DIALOG = 2;
	
	public final static String PREFS_NAME = "com.eddyspace.networkmonitor_preferences";
	
	private NetInfoAdapter ni;
	private static ProgressDialog pd_busy = null;
	private static ProgressDialog pd_ping = null;

	private Context context;
	private SharedPreferences prefs = null;		
	
	final static String ls = "<br />"; // System.getProperty("line.separator");
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(localLOGV) Log.v(LOG_TAG, "Starting NetworkTools");
        context = getBaseContext();
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE );        
        
        setContentView(R.layout.netmon_tools);        
        TextView textView = (TextView) findViewById(R.id.nettools_text);
        
        ni = new NetInfoAdapter(this);                
        if( Boolean.parseBoolean(getString(R.string.pro_version)) ){
        	if( ni.isConnected() ) {        		
        		// textView.setText(getString(R.string.nettools_connected) + " " + ni.getInfo("type"));
        		String[] items = new String[] {
        				getString(R.string.nat_ip_list_string),
        				getString(R.string.ping_google_list_string),
        				getString(R.string.netcfg_list_string),
        				// getString(R.string.netstat_list_string),
        				// getString(R.string.ping_gateway_list_string),
        				// getString(R.string.ping_dns_list_string),
        				// getString(R.string.services_list_string),
        				// getString(R.string.disk_free_list_string),
        				// getString(R.string.getprop_list_string)        
        		};
        		textView.setText(Html.fromHtml(getString(R.string.connected) + " " + ni.getInfo("type")) + " " + ni.getInfo("netID"));
        		setListAdapter(new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, items));
                getListView().setTextFilterEnabled(true);
        	} else {
        		textView.setText(Html.fromHtml(getString(R.string.no_data_net)));
        	}
        } else {
    		textView.setText(Html.fromHtml(getString(R.string.not_pro_version_string)));
        }
    }
    
    protected void onListItemClick(ListView lv, View v, int position, long id) {
    	Object o = this.getListAdapter().getItem(position);
    	String which = o.toString();
    	if(localLOGV) Log.v(LOG_TAG, "onListItemClick got position: " + position + ", Relates to: " + which + ", id: " + id );
                
    	switch (position) {
    	case 0:
    		new DoGetNatIP().execute();
    		break;
    	case 1:
    		String host = prefs.getString("preference_ping_host", "google.com");
    		String port = prefs.getString("preference_ping_host_port", "80");    		
    		new DoJavaPing().execute(host, port);
    		// new DoSystemTask().execute("/system/bin/ping", "-c2", "google.com");
	    	break;
    	case 2:
    		showInterfaces();
    		//new DoSystemTask().execute("/system/bin/netcfg", null, null);
		    break;
    	case 3:
    		new DoSystemTask().execute("/system/bin/netstat", null, null);
	    	break;
    	case 4:
    		String gateway = ni.getInfo("gateway");
    		new DoJavaPing().execute(gateway, OTHER_HOST_PING_PORT);
    		// new DoSystemTask().execute("/system/bin/ping", "-c2", gateway);
    		break;
    	case 5:
	    	String dns = ni.getInfo("dns");
			new DoJavaPing().execute(dns, DNS_HOST_PING_PORT);
    		// new DoSystemTask().execute("/system/bin/ping", "-c2", dns);
		    break;
    	case 6:
    		new DoSystemTask().execute("/system/bin/service", "list", null);
		    break;
    	case 7:
    		new DoSystemTask().execute("/system/bin/df", null, null);
		    break;
    	case 8:
    		new DoSystemTask().execute("/system/bin/getprop", null, null);
		    break;
    	default:	    	
			break;
    	}
    }
	
    @Override
    protected Dialog onCreateDialog(int id) {
    	if( id == BUSY_DIALOG ) {    		
    		if(localLOGV) Log.v(LOG_TAG, "onCreateDialog: Create BUSY_DIALOG");
    		pd_busy = new ProgressDialog(NetworkTools.this);
    		pd_busy.setMessage(getString(R.string.tools_other_progress_dialog_text));
    		pd_busy.setCancelable(true);
    		return pd_busy;
    		// The dialog gets shown in onPreExecute    		
    	}
    	if( id == PINGING_DIALOG ) {
    		if(localLOGV) Log.v(LOG_TAG, "onCreateDialog: Create PINGING_DIALOG");    		
    		pd_ping = new ProgressDialog(NetworkTools.this);
    		pd_ping.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		pd_ping.setMessage(getString(R.string.tools_ping_progress_dialog_text));
    		pd_ping.setCancelable(true);
    		pd_ping.setMax(PING_COUNT);
    		return pd_ping;
    		// The dialog gets shown in onPreExecute
    	}    	
    	return null;
    }
    
    private class DoSystemTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args ) {
    		if(localLOGV) Log.v(LOG_TAG, "DoSystemTask: returning message");
    		// return Tools.mySystem(args[0],args[1],args[2]);
    		return "n/a";
        }
        
        @Override
        protected void onPreExecute() {
    		if(localLOGV) Log.v(LOG_TAG, "onPreExecute: show dialog BUSY_DIALOG");
        	showDialog(BUSY_DIALOG);
        }
        
        @Override
        protected void onPostExecute(String result) {
        	if(localLOGV) Log.v(LOG_TAG, "onPostExecute: dismiss dialog BUSY_DIALOG and show result");
        	pd_busy.dismiss();
    		Intent showResult = new Intent(getBaseContext(), ShowResults.class);
    		showResult.putExtra("message", result);
    		startActivity(showResult);
        }
    }

    private class DoJavaPing extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args ) {        	
        	return JavaPing(args[0], args[1]);        	        	
        }
        
        @Override
        protected void onPreExecute() {
        	showDialog(PINGING_DIALOG);
        }
        
        @Override
        protected void onPostExecute(String result) {
        	pd_ping.setProgress(0);
        	pd_ping.dismiss();
    		Intent showResult = new Intent(getBaseContext(), ShowResults.class);
    		showResult.putExtra("message", result);
    		startActivity(showResult);
        }
    }
    
    public static String JavaPing(String host, String portString) {
        final int timeout = PING_TIMEOUT;
        final int count = PING_COUNT;
        

        int port = PING_DEFAULT_PORT;
        String mResult = "Pinging " + host + " using TCP on port " + port + ls;
        try {
        	int p = Integer.parseInt(portString);
        	port = p;
        } catch (NumberFormatException e) {
        	Log.v(LOG_TAG, "Number format exception - using default port " + PING_DEFAULT_PORT);
        	mResult += portString + " is not a number, using default port " + PING_DEFAULT_PORT + ls;
        }

        InetAddress address = null;
        try {
        	address = InetAddress.getByName(host);
        } catch (UnknownHostException e) {        	
        	mResult += "Unknown host " + host + ls;
        	return mResult;
        }
        
        long[] rtt = new long[count];        
        for( int i = 0; i < count; i++ ) {        	
            
            Socket s = new Socket(); // Could maybe re-use this
        	        	
            try {
    			s.bind(null);
    		} catch (IOException e) {		
    			mResult += "Unknown host " + host + ls;    			
    			return mResult;
    		}            
        	
        	String state = "n/a";
        	String reachable = "unreachable";
        	long t = 0;
        	long start = System.currentTimeMillis();
        	try {
                s.connect(new InetSocketAddress(address, port), timeout);
                t = System.currentTimeMillis() - start;
                s.close();
                reachable = "reachable";
                state = "open";                
            } catch (SocketTimeoutException e ) {            	        			
            	Log.v(LOG_TAG, "SocketTimeoutException " + e.toString());
            } catch (IOException e) {
            	if( e.toString().matches(".*Connection refused.*") ) {
            		t = System.currentTimeMillis() - start;
            		Log.v(LOG_TAG, "IOException " + e.toString() + " is a successfull ping");
            		state = "closed";
            		reachable = "reachable";
            	}
            }
            rtt[i] = t;
            mResult += (i + 1) + ": " + reachable + " on port " + port + "(" + state + ")" + " in " + t + "ms" + ls;
        	try {        		
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.e(LOG_TAG, "InterruptedException (unable to sleep)" + e.toString());
			}
			pd_ping.incrementProgressBy(1);
        }
        
        long max = 0;
        long min = -1;
        long total = 0;
        int returned = 0;
        for( int i = 0; i < count; i++ ) {
        	if( rtt[i] > 0 ) {
        		returned++;
       			if( min > rtt[i] || min == -1 ) {
       				min = rtt[i];
       			}
       			if( max < rtt[i] ) {
       				max = rtt[i];        		
       			}
       			total += rtt[i];
       		}
        }
        long ave = returned > 0? total/returned: 0;
        min = min < 0? 0: min; 
        mResult += "Min: " + Tools.insertCommas(String.valueOf(min))  + ", Max: " + Tools.insertCommas(String.valueOf(max)) + ", Ave: " + Tools.insertCommas(String.valueOf(ave)) + ls; 
        return mResult;
    }
    
    private class DoGetNatIP extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args ) {        	
        	return context.getString(R.string.key_nat_ip_name) + ": " + getNatIP();        	        	
        }
        
        @Override
        protected void onPreExecute() {
        	showDialog(BUSY_DIALOG);
        }

        protected void onPostExecute(String result) {
        	pd_busy.dismiss();
    		Intent showResult = new Intent(getBaseContext(), ShowResults.class);
    		showResult.putExtra("message", result);
    		startActivity(showResult);
        }
    }
    
    private String getNatIP() {        
    	if(localLOGV) Log.v(LOG_TAG, "getNatIP");
        BufferedReader reader;
		String result = "";
		try {
			reader = Tools.readFromWeb("http://whatismyip.com/automation/n09230945NL.asp");
			String line = reader.readLine();
			while (line != null) {
				if(localLOGV) Log.v(LOG_TAG, " result " + line);
				result = result + line;
				try {						
					line = reader.readLine();
				} catch (IOException e) {						
					Log.w(LOG_TAG, e.getMessage());
				} finally {
					reader.close();
				}
			}
		} catch (IOException e) {			
			Log.w(LOG_TAG, "IO Exception: " + e.getMessage());
		} catch (Exception e) {
			Log.w(LOG_TAG, "Exception: " + e.getMessage());
			e.printStackTrace();
		}			
		return result;        
    }
    
    public void showInterfaces() {
        Enumeration<NetworkInterface> nets;
        String mResult = "";
		try {
			nets = NetworkInterface.getNetworkInterfaces();
	        for (NetworkInterface netint : Collections.list(nets)) {
	            mResult += displayInterfaceInformation(netint);
	        }
		} catch (SocketException e) {
			Log.e(LOG_TAG, "SocketException " + e.toString());
			mResult += "SocketException: " + e.toString();
		}
		Intent showResult = new Intent(getBaseContext(), ShowResults.class);
		showResult.putExtra("message", mResult);
		startActivity(showResult);
    }
	
    static String displayInterfaceInformation(NetworkInterface netint) {
    	String result = "Name: " + netint.getDisplayName() + "(" + netint.getName() + ")" +ls;    	
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            result += "InetAddress: " + inetAddress + ls;
        }
        return result;        
    }
}  
