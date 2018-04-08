package com.android.andtriplog;


import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class AndTripLog extends Activity  implements LocationListener {

	static final String DATABASE_NAME = "andtriplog.db";
    static final int DATABASE_VERSION = 7;
    public DatabaseHelper mAndTripLogDB;

    private TextView gpsstateTextView;
	protected LocationManager myLocationManager = null;
	private int current_trip = 0;
	private int current_point = 0;
	private ListView listview_trace;	
	private SimpleCursorAdapter sc_adapter;
	private Location last_loc;
	private float total_distance;
	private long first_timestamp = 0;
	private long last_timestamp = 0;
	private ExportThread mExportThread;
	
	private static final int MENU_COPY = 1;
    private static final int MENU_START = 2;
    private static final int MENU_STOP = 3;
    private static final int MENU_DELETE_ALL = 4;
    private static final int MENU_EXPORT_ALL = 5;
    private static final int MENU_EXPORT_SEND_ALL = 6;

	private MenuItem start_menu;
	private MenuItem stop_menu;
	private MenuItem delete_all_menu;
	private MenuItem export_all_menu;
	private MenuItem export_send_all_menu;
	
    private ProgressDialog mProgressDialog;
    private AlertDialog mDeleteDialog;
    
	protected PowerManager.WakeLock wlatp;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.main);
        Log.w("AndTripLog","1");
        listview_trace = (ListView)findViewById(R.id.traceView);
        registerForContextMenu(listview_trace);
        gpsstateTextView = (TextView)findViewById(R.id.gpsstate);
        gpsstateTextView.setText(R.string.tips_not_running); 
        mAndTripLogDB = new DatabaseHelper(this);
        mAndTripLogDB.startDatabase();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wlatp = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AndTripLog tag");
        fillList();
    }
    
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	menu.setHeaderTitle(R.string.c_menu_title);
    	menu.setHeaderIcon(R.drawable.satellite);
        menu.add(Menu.NONE,0,0,R.string.c_menu_send);
        menu.add(Menu.NONE,1,1,R.string.c_menu_save);
        menu.add(Menu.NONE,2,2,R.string.c_menu_delete);
    }
    
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        long id = sc_adapter.getItemId(info.position);
        switch(item.getItemId()) {
        case 0: //Send
            exportTrip(id,true);
        	break;
        case 1: //Save
            exportTrip(id,false);
        	break;
        case 2: //Delete
//            exportTrip(id,false);
            deleteTrip(id);
            fillList();
        	break;
        }
        return true;
    }


    @Override
    protected void onPause() {
    	super.onPause();
        Log.w("AndTripLog","onpause");
    }

    @Override
    protected void onResume() {
    	super.onResume();
        Log.w("AndTripLog","onresume");
    }

    public boolean  isFinishing() {
    	return false;
    }

    @Override
    protected void onStop() {
    	super.onStop();
        Log.w("AndTripLog","onstop");
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
        Log.w("AndTripLog","onstart");
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if (wlatp.isHeld()) {
    		wlatp.release();
    	}
        myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        myLocationManager.removeUpdates(this);
        mAndTripLogDB.end();
        Log.w("AndTripLog","ondestroy");
    }

    @Override
    protected void onRestart() {
    	super.onRestart();
        Log.w("AndTripLog","onrestart");
    }

    /**
     * 
     *
     */
    private void startAnewTrip() {
    	current_trip = mAndTripLogDB.startAnewTrip();
    	current_point = 0;
    	total_distance = 0;
    	last_loc = null;
    	first_timestamp = 0;
    	last_timestamp = 0;
    }
    
    private void newPointDB(double longitude,double latitude,double altitude,long gps_time) {
    	mAndTripLogDB.newPointDB(longitude, latitude, altitude, gps_time, current_trip);
    	current_point++;
    }
    
    private void stopTrip(){
        myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        myLocationManager.removeUpdates(this);
    	mAndTripLogDB.stopTrip(last_timestamp, first_timestamp, total_distance, current_trip);
    	current_trip = 0;
    	current_point = 0;
    }

    private void fillList(){
        gpsstateTextView.setText(R.string.tips_not_running); 
        Cursor lst = mAndTripLogDB.getListing();
        startManagingCursor(lst);
        Log.w("AndTripLog","4");
        	listview_trace.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        	
        	sc_adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, lst,new String[] {"cmt","descr"},
        			new int[] {android.R.id.text1, android.R.id.text2}); 
        	listview_trace.setAdapter(sc_adapter);
    }
    
    private void setupForGPSAutoRefreshing() {
            final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 0; // in Meters
            final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
            // Get the first provider available
            myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Log.w("AndTripLog","40 gps");
        	myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, this);
            Log.w("AndTripLog","50 gps");
    }    

  	public void onProviderDisabled(String provider) {
  		Log.w("AndTripLog","OnProviderDisabled " + provider);
  		gpsstateTextView.setText(R.string.gps_inactive);
  	}
      	  	
  	public void  	onProviderEnabled(String provider) {
  		Log.w("AndTripLog","OnProviderEnabled " + provider);
//  		gpsstateTextView.setText("OnProviderEnabled " + provider);
  	}
      	  	  	  	  	
  	public void  	onStatusChanged(String provider, int status, Bundle extras){
  		Log.w("AndTripLog","onStatusChanged "+provider+":"+status);
  		//  		gpsstateTextView.setText("onStatusChanged "+provider+":"+status);
  	}
			
  	private String formatMilliSeconds(long s) {
  		String ret = new String();
  		if (s < 0) {
  			ret += s+"ms";
  		} else if (s < 60000) {
  			ret += (s/1000)+"s";
  		} else if (s < 1000 * 3600) {
  			long min = s/(1000*60);
  			long sec = (s- (min*60*1000))/ 1000;
  			ret += min+"m,";
  			ret += sec+"s";
  		} else {
  			long min = s/(1000*60);
  			long hour = min/60;
  			min -= hour*60;
  			long sec = (s- (min*60*1000))/ 1000;
  			ret += hour+"h,";
  			ret += min+"m,";
  			ret += sec+"s";
  		}
  		return ret;
  	}
  	
  	@Override
  	public void onLocationChanged(Location location) {
  			Log.w("AndTripLog","136 write");
  			Date d = new Date(location.getTime());
  			if (last_loc != null) {
   				total_distance += location.distanceTo(last_loc);
  			} else {
  				first_timestamp = location.getTime();
  			}
  			last_timestamp = location.getTime();
  			last_loc = new Location(location);
  			gpsstateTextView.setText("Altitude : " + location.getAltitude() 
  										+ "\nLongitude : " + location.getLongitude() 
  										+ "\nLatitude : " +location.getLatitude() 
  										+ "\nSpeed : "+ (location.hasSpeed() ? location.getSpeed() + "m/s" : "?")
  										+ "\nTime : "+ d.toLocaleString() + " GMT"
  										+ "\nTotal distance : " + total_distance
  										+ "\nAverage Speed: "+ ((last_timestamp - first_timestamp) > 0 ? (total_distance*1000/(last_timestamp-first_timestamp))+ "m/s":"")
  										+ "\nDuration : " + formatMilliSeconds(last_timestamp - first_timestamp));
  			newPointDB(location.getLongitude(),location.getLatitude(),location.getAltitude(),location.getTime());
  		Log.w("AndTripLog","145 gps");
  	}
    


    private void startAction() {
    	if (!wlatp.isHeld()) {
    		wlatp.acquire();
    	}
    	gpsstateTextView.setText(R.string.starting);
    	startAnewTrip();
    	setupForGPSAutoRefreshing();
	}

    private void stopAction() {
    	stopTrip();
    	fillList();
    	if (wlatp.isHeld()) {
    		wlatp.release();
    	}
    }
    

    private void exportTrip(long id,boolean email) 
    {
    	showDialog(DIALOG_EXPORT_PROGRESS);
    	mExportThread = new ExportThread(this,id,mProgressDialog,email);
    	mExportThread.start();
    }
    
    private static final int DIALOG_DELETE_VALID = 1;
    private static final int DIALOG_COPYRIGHT = 2;
    private static final int DIALOG_DELETE_ALL_VALID = 3;
    private static final int DIALOG_EXPORT_PROGRESS = 4;
    protected long delete_id;
    
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_DELETE_VALID:
        	mDeleteDialog = new AlertDialog.Builder(AndTripLog.this)
            .setIcon(R.drawable.satellite)
            .setTitle("Delete trip "+delete_id)
            .setMessage(R.string.sure_delete)
            .setPositiveButton(R.string.yes_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	mAndTripLogDB.deleteTrip(delete_id);
                	Toast.makeText(AndTripLog.this, "Delete "+ delete_id, Toast.LENGTH_LONG).show();
                	fillList();
                }
            })
            .setNegativeButton(R.string.dont_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	Toast.makeText(AndTripLog.this, "Don't Delete "+delete_id, Toast.LENGTH_LONG).show();
                }
            })
            .create();
        	return mDeleteDialog;
        case DIALOG_EXPORT_PROGRESS:
            mProgressDialog = new ProgressDialog(AndTripLog.this);
            mProgressDialog.setIcon(R.drawable.satellite);
            mProgressDialog.setTitle("Exporting...");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMax(100);
            mProgressDialog.setButton(getText(android.R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	mExportThread.interrupt();
                }
            });
            mProgressDialog.setProgress(0);
           return mProgressDialog;
        case DIALOG_DELETE_ALL_VALID:
        	return new AlertDialog.Builder(AndTripLog.this)
            .setIcon(R.drawable.satellite)
            .setTitle("Delete all trip")
            .setMessage(R.string.sure_delete)
            .setPositiveButton(R.string.yes_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	mAndTripLogDB.deleteAllTrip();
                	Toast.makeText(AndTripLog.this, "Delete all trips", Toast.LENGTH_LONG).show();
                	fillList();
                }
            })
            .setNegativeButton(R.string.dont_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	Toast.makeText(AndTripLog.this, "Don't Delete trips", Toast.LENGTH_LONG).show();
                }
            })
            .create();
        case DIALOG_COPYRIGHT:
        	return new AlertDialog.Builder(AndTripLog.this)
            .setIcon(R.drawable.satellite)
            .setTitle("Application Copyright")
            .setMessage("Author: Jean-Samuel Reynaud\nLicence: GPL")
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
            .create();
        }
        return null;
    }
    
    private void deleteTrip(long id) {
    	delete_id = id;
    	if (mDeleteDialog != null) {
    		mDeleteDialog.setTitle("Delete trip "+delete_id);
    	}
        showDialog(DIALOG_DELETE_VALID);
        Log.w("AndTripLog","del 1 " + id);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, MENU_COPY, 0, R.string.menu_copy).setIcon(android.R.drawable.ic_menu_info_details);
        start_menu = menu.add(Menu.NONE, MENU_START, 0, R.string.menu_start).setIcon(android.R.drawable.ic_media_play).setEnabled(true);
        stop_menu = menu.add(Menu.NONE, MENU_STOP, 0, R.string.menu_stop).setIcon(android.R.drawable.ic_media_pause).setEnabled(false);
        delete_all_menu = menu.add(Menu.NONE, MENU_DELETE_ALL, 0, R.string.menu_delete_all).setIcon(android.R.drawable.ic_menu_delete).setEnabled(true);
        export_all_menu = menu.add(Menu.NONE, MENU_EXPORT_ALL, 0, R.string.menu_export_all).setIcon(android.R.drawable.ic_menu_save).setEnabled(true);
        export_send_all_menu = menu.add(Menu.NONE, MENU_EXPORT_SEND_ALL, 0, R.string.menu_export_send_all).setIcon(android.R.drawable.ic_menu_send).setEnabled(true);
        return true;
    }
    
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
           case MENU_COPY:
               showDialog(DIALOG_COPYRIGHT);
               return true;
           case MENU_START:
        	   stop_menu.setEnabled(true);
        	   start_menu.setEnabled(false);
        	   delete_all_menu.setEnabled(false);
        	   export_all_menu.setEnabled(false);
        	   export_send_all_menu.setEnabled(false);
        	   listview_trace.setEnabled(false);
        	   gpsstateTextView.setLines(15);
               unregisterForContextMenu(listview_trace);
        	   startAction();
        	   return true;
           case MENU_STOP:
        	   stopAction();
               registerForContextMenu(listview_trace);
        	   start_menu.setEnabled(true);
        	   stop_menu.setEnabled(false);
        	   listview_trace.setEnabled(true);
        	   delete_all_menu.setEnabled(true);
        	   export_all_menu.setEnabled(true);
        	   export_send_all_menu.setEnabled(true);
        	   gpsstateTextView.setLines(1);
        	   return true;
           case MENU_DELETE_ALL:
               showDialog(DIALOG_DELETE_ALL_VALID);
               Log.w("AndTripLog","del all");
        	   return true;
           case MENU_EXPORT_ALL:
        	   exportTrip(-1, false);
        	   return true;
           case MENU_EXPORT_SEND_ALL:
        	   exportTrip(-1, true);
        	   return true;
       }
       return false;
   }
}