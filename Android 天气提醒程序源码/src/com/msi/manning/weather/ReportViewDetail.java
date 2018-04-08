package com.msi.manning.weather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.msi.manning.weather.data.DBHelper;
import com.msi.manning.weather.data.WeatherForecast;
import com.msi.manning.weather.data.WeatherRecord;
import com.msi.manning.weather.data.YWeatherFetcher;
import com.msi.manning.weather.data.DBHelper.Location;
import com.msi.manning.weather.service.WeatherAlertService;

/**
 * Show Review detail for review item user selected, allow user to enable/disable alerts, and
 * show/react menu for other actions.
 * 
 * @author charliecollins
 * 
 */
public class ReportViewDetail extends Activity {

    private static final String CLASSTAG = ReportViewDetail.class.getSimpleName();
    private static final int MENU_VIEW_SAVED_LOCATIONS = Menu.FIRST;
    private static final int MENU_REMOVE_SAVED_LOCATION = Menu.FIRST + 1;
    private static final int MENU_SAVE_LOCATION = Menu.FIRST + 2;
    private static final int MENU_SPECIFY_LOCATION = Menu.FIRST + 3;
    private static final int MENU_VIEW_CURRENT_LOCATION = Menu.FIRST + 4;

    private TextView location;
    private TextView date;
    private TextView condition;
    private TextView forecast;
    private ImageView conditionImage;
    private CheckBox currentCheck;

    private ProgressDialog progressDialog;
    private WeatherRecord report;
    private String reportZip;
    private String deviceZip;
    private boolean useDeviceLocation;

    private Location savedLocation;
    private Location deviceAlertEnabledLocation;

    private DBHelper dbHelper;

    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
            progressDialog.dismiss();
            if ((report == null) || (report.getCondition() == null)) {
                Toast.makeText(ReportViewDetail.this, R.string.message_report_unavailable, Toast.LENGTH_SHORT).show();
            } else {
                Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + "   HANDLER report - " + report);
                location.setText(report.getCity() + ", " + report.getRegion() + " " + report.getCountry());
                date.setText(report.getDate());

                StringBuffer cond = new StringBuffer();
                cond.append(report.getCondition().getDisplay() + "\n");
                cond.append("Temperature: " + report.getTemp() + " F " + " (wind chill " + report.getWindChill()
                    + " F)\n");
                cond.append("Barometer: " + report.getPressure() + " and " + report.getPressureState() + "\n");
                cond.append("Humidity: " + report.getHumidity() + "% - Wind: " + report.getWindDirection() + " "
                    + report.getWindSpeed() + "mph\n");
                cond.append("Sunrise: " + report.getSunrise() + " - Sunset:  " + report.getSunset());
                condition.setText(cond.toString());

                StringBuilder fore = new StringBuilder();
                for (int i = 0; (report.getForecasts() != null) && (i < report.getForecasts().length); i++) {
                    WeatherForecast fc = report.getForecasts()[i];
                    fore.append(fc.getDay() + ":\n");
                    fore.append(fc.getCondition().getDisplay() + " High:" + fc.getHigh() + " F - Low:" + fc.getLow()
                        + " F");
                    if (i == 0) {
                        fore.append("\n\n");
                    }
                }
                
                forecast.setText(fore.toString());
                String resPath = "com.msi.manning.weather:drawable/" + "cond" + report.getCondition().getId();
                int resId = getResources().getIdentifier(resPath, null, null);
                conditionImage.setImageDrawable(getResources().getDrawable(resId));
            }
        }
    };

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + " onCreate");

        this.setContentView(R.layout.report_view_detail);

        this.location = (TextView) findViewById(R.id.view_location);
        this.date = (TextView) findViewById(R.id.view_date);
        this.condition = (TextView) findViewById(R.id.view_condition);
        this.forecast = (TextView) findViewById(R.id.view_forecast);
        this.conditionImage = (ImageView) findViewById(R.id.condition_image);
        this.currentCheck = (CheckBox) findViewById(R.id.view_configure_alerts);

        // currentCheck listener, enable/disable alerts
        this.currentCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(final CompoundButton button, final boolean isChecked) {
                Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + " onCheckedChanged - isChecked - "
                    + isChecked);
                updateAlertStatus(isChecked);
            }
        });

        // start the service - though it may already have been started on boot
        // multiple starts don't hurt it, and if app is installed and device NOT
        // booted needs this
        startService(new Intent(this, WeatherAlertService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + " onPause");
        this.dbHelper.cleanup();
        this.deviceZip = WeatherAlertService.deviceLocationZIP;
        if (this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + " onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + " onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + " onStart");
        this.dbHelper = new DBHelper(this);

        // determine current location zip using convenience deviceLocationZIP member of
        // WeatherAlertService
        // (it is established there via LocationManager)
        this.deviceZip = WeatherAlertService.deviceLocationZIP;

        // determine reportZip from Uri (or default to deviceZip)
        if ((getIntent().getData() != null) && (getIntent().getData().getEncodedQuery() != null)
            && (getIntent().getData().getEncodedQuery().length() > 8)) {
            Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + " Intent data and query present, parse for zip");
            String queryString = getIntent().getData().getEncodedQuery();
            Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + " queryString - " + queryString);
            this.reportZip = queryString.substring(4, 9);
            this.useDeviceLocation = false;
        } else {
            Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + " Intent data not present, use current location");
            this.reportZip = this.deviceZip;
            this.useDeviceLocation = true;
        }

        // get saved state from db records
        this.savedLocation = this.dbHelper.get(this.reportZip);
        this.deviceAlertEnabledLocation = this.dbHelper.get(DBHelper.DEVICE_ALERT_ENABLED_ZIP);

        if (this.useDeviceLocation) {
            this.currentCheck.setText(R.string.view_checkbox_current);
            if (this.deviceAlertEnabledLocation != null) {
                this.currentCheck.setChecked(true);
            } else {
                this.currentCheck.setChecked(false);
            }
        } else {
            this.currentCheck.setText(R.string.view_checkbox_specific);
            if (this.savedLocation != null) {
                if (this.savedLocation.alertenabled == 1) {
                    this.currentCheck.setChecked(true);
                } else {
                    this.currentCheck.setChecked(false);
                }
            }
        }
        loadReport(this.reportZip);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (this.useDeviceLocation) {
            menu.add(0, ReportViewDetail.MENU_SPECIFY_LOCATION, 0,
                getResources().getText(R.string.menu_specify_location)).setIcon(android.R.drawable.ic_menu_edit);
        } else {
            menu.add(0, ReportViewDetail.MENU_VIEW_CURRENT_LOCATION, 2,
                getResources().getText(R.string.menu_device_location)).setIcon(android.R.drawable.ic_menu_mylocation);
            menu.add(0, ReportViewDetail.MENU_SPECIFY_LOCATION, 3,
                getResources().getText(R.string.menu_specify_location)).setIcon(android.R.drawable.ic_menu_edit);
            if (this.savedLocation != null) {
                menu.add(0, ReportViewDetail.MENU_REMOVE_SAVED_LOCATION, 4,
                    getResources().getText(R.string.menu_remove_location)).setIcon(android.R.drawable.ic_menu_delete);
            } else {
                menu
                    .add(0, ReportViewDetail.MENU_SAVE_LOCATION, 5, getResources().getText(R.string.menu_save_location))
                    .setIcon(android.R.drawable.ic_menu_add);
            }
        }
        menu.add(0, ReportViewDetail.MENU_VIEW_SAVED_LOCATIONS, 1, getResources().getText(R.string.menu_goto_saved))
            .setIcon(android.R.drawable.ic_menu_myplaces);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        Intent intent = null;
        Uri uri = null;
        switch (item.getItemId()) {
            case MENU_VIEW_CURRENT_LOCATION:
                uri = Uri.parse("weather://com.msi.manning/loc?zip=" + this.deviceZip);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case MENU_SPECIFY_LOCATION:
                startActivity(new Intent(ReportViewDetail.this, ReportSpecifyLocation.class));
                break;
            case MENU_VIEW_SAVED_LOCATIONS:
                intent = new Intent(ReportViewDetail.this, ReportViewSavedLocations.class);
                intent.putExtra("deviceZip", this.deviceZip);
                startActivity(intent);
                break;
            case MENU_SAVE_LOCATION:
                Location loc = new Location();
                loc.alertenabled = 0;
                loc.lastalert = 0;
                loc.zip = this.reportZip;
                loc.city = this.report.getCity();
                loc.region = this.report.getRegion();
                this.dbHelper.insert(loc);
                uri = Uri.parse("weather://com.msi.manning/loc?zip=" + this.reportZip);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case MENU_REMOVE_SAVED_LOCATION:
                if (this.savedLocation != null) {
                    this.dbHelper.delete(this.reportZip);
                }
                uri = Uri.parse("weather://com.msi.manning/loc?zip=" + this.reportZip);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void updateAlertStatus(final boolean isChecked) {
        Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + " updateAlertStatus - " + isChecked);
        // NON DEVICE
        if (!this.useDeviceLocation) {
            if (isChecked) {
                // no loc at all, create it as saved, and set alertenabled 1
                if (this.savedLocation == null) {
                    Location loc = new Location();
                    loc.alertenabled = 1;
                    loc.lastalert = 0;
                    loc.zip = this.reportZip;
                    loc.city = this.report.getCity();
                    loc.region = this.report.getRegion();
                    this.dbHelper.insert(loc);
                    // if loc already is saved, just update alertenabled
                } else {
                    this.savedLocation.alertenabled = 1;
                    this.dbHelper.update(this.savedLocation);
                }
            } else {
                if (this.savedLocation != null) {
                    this.savedLocation.alertenabled = 0;
                    this.dbHelper.update(this.savedLocation);
                }
            }
            // DEVICE
        } else {
            // store whether or not user wants current device location
            // alerts in special Location with DEVICE_ALERT_ENABLED_ZIP value
            if (isChecked) {
                if (this.deviceAlertEnabledLocation == null) {
                    Location currentLoc = new Location();
                    currentLoc.alertenabled = 1;
                    currentLoc.lastalert = 0;
                    currentLoc.zip = DBHelper.DEVICE_ALERT_ENABLED_ZIP;
                    this.dbHelper.insert(currentLoc);
                }
            } else {
                if (this.deviceAlertEnabledLocation != null) {
                    this.dbHelper.delete(DBHelper.DEVICE_ALERT_ENABLED_ZIP);
                }
            }
        }
    }

    private void loadReport(final String zip) {
        Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + " loadReport");
        Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + "    zip - " + zip);

        this.progressDialog = ProgressDialog.show(this, getResources().getText(R.string.view_working), getResources()
            .getText(R.string.view_get_report), true, false);

        final YWeatherFetcher ywh = new YWeatherFetcher(zip);

        // get report in a separate thread for ProgressDialog/Handler
        // when complete send "empty" msg to handler indicating thread is done
        new Thread() {

            @Override
            public void run() {
                report = ywh.getWeather();
                Log.v(Constants.LOGTAG, " " + ReportViewDetail.CLASSTAG + "    report - " + report);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
