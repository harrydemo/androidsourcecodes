package com.msi.manning.weather;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.msi.manning.weather.data.DBHelper;
import com.msi.manning.weather.data.DBHelper.Location;

import java.util.List;

/**
 * Allow user to choose previously saved locations.
 * 
 * @author charliecollins
 * 
 */
public class ReportViewSavedLocations extends ListActivity {

    private static final String CLASSTAG = ReportViewSavedLocations.class.getSimpleName();
    private static final int MENU_SPECIFY_LOCATION = Menu.FIRST;
    private static final int MENU_VIEW_CURRENT_LOCATION = Menu.FIRST + 1;

    private DBHelper dbHelper;
    private ProgressDialog progressDialog;
    private TextView empty;
    private List<Location> locations;
    private ListAdapter adapter;

    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
            Log.v(Constants.LOGTAG, " " + ReportViewSavedLocations.CLASSTAG + " worker thread done, setup list");
            progressDialog.dismiss();
            if ((locations == null) || (locations.size() == 0)) {
                empty.setText("No Data");
            } else {
                adapter = new ArrayAdapter<Location>(ReportViewSavedLocations.this, R.layout.list_item_1, locations);
                setListAdapter(adapter);
            }
        }
    };

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        this.setContentView(R.layout.report_view_saved_locations);
        getListView().setEmptyView(findViewById(R.id.view_saved_locations_empty));
        this.empty = (TextView) findViewById(R.id.view_saved_locations_empty);
        this.dbHelper = new DBHelper(this);
        loadLocations();
    }

    @Override
    protected void onPause() {
        super.onResume();
        this.dbHelper.cleanup();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, ReportViewSavedLocations.MENU_SPECIFY_LOCATION, 0,
            getResources().getText(R.string.menu_specify_location)).setIcon(android.R.drawable.ic_menu_edit);
        menu.add(0, ReportViewSavedLocations.MENU_VIEW_CURRENT_LOCATION, 1,
            getResources().getText(R.string.menu_device_location)).setIcon(android.R.drawable.ic_menu_mylocation);

        return true;
    }

    @Override
    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        Log.v(Constants.LOGTAG, " " + ReportViewSavedLocations.CLASSTAG + " selected list item");
        Location loc = this.locations.get(position);
        Uri uri = Uri.parse("weather://com.msi.manning/loc?zip=" + loc.zip);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        switch (item.getItemId()) {
            case MENU_VIEW_CURRENT_LOCATION:
                String deviceZip = getIntent().getStringExtra("deviceZip");
                Uri uri = Uri.parse("weather://com.msi.manning/loc?zip=" + deviceZip);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case MENU_SPECIFY_LOCATION:
                startActivity(new Intent(ReportViewSavedLocations.this, ReportSpecifyLocation.class));
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void loadLocations() {
        Log.v(Constants.LOGTAG, " " + ReportViewSavedLocations.CLASSTAG + " loadLocations");
        this.progressDialog = ProgressDialog.show(this, " Working...", " Retrieving saved locations", true, false);
        new Thread() {

            @Override
            public void run() {
                locations = dbHelper.getAll();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
