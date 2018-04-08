/**
 * 
 */
package com.nanosheep.bikeroute;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.nanosheep.bikeroute.utility.RouteDatabase;

/**
 * Activity for browsing/selecting favourite routes.
 * 
 * This file is part of BikeRoute.
 * 
 * Copyright (C) 2011  Jonathan Gray
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * @author jono@nanosheep.net
 * @version Jan 25, 2011
 */
public class SavedRoutes extends ListActivity {

    private RouteDatabase db;
    
    private Cursor data;

    private static final String fields[] = {RouteDatabase.FRIENDLY_NAME, RouteDatabase.ROUTER };
    
    private BikeRouteApp app;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (BikeRouteApp) getApplicationContext();
	}
    
    @Override 
    public void onStart() {
    	db = app.getRouteDB();
        data = db.getRoutes();
        
        setListAdapter(new SimpleCursorAdapter(this, R.layout.row, data, fields, new int[] {R.id.routename, R.id.routeby }));
        
        //Context menu for deleting saved routes
        registerForContextMenu(getListView());
    	super.onStart();
    }
    
    @Override
    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
    	showDialog(R.id.load);
    	Thread t = new Thread() {
    		public void run() {
    	    	app.setRoute(db.getRoute(id));
    	    	final Intent map = new Intent(SavedRoutes.this, LiveRouteMap.class);
    			map.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    			dismissDialog(R.id.load);
    			startActivityForResult(map, R.id.trace);	
    		}
    	};
    	t.start();
    }
    
    @Override
    public Dialog onCreateDialog(final int id) {
    	ProgressDialog pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage(getText(R.string.load_msg));
		pDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(final DialogInterface arg0) {
				removeDialog(R.id.load);
			}
			});
		return pDialog;
    }
    
    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenuInfo info) {
    	menu.add("Delete");
    }
    
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
    	AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
    	db.delete(menuInfo.id);
    	data.requery();
    	return true;
    }
}
