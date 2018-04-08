package com.msi.manning.weather.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.msi.manning.weather.Constants;
import com.msi.manning.weather.R;
import com.msi.manning.weather.data.DBHelper;
import com.msi.manning.weather.data.WeatherRecord;
import com.msi.manning.weather.data.YWeatherFetcher;
import com.msi.manning.weather.data.DBHelper.Location;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Background service to check for severe weather for specific locations and alert user.
 * 
 * Note that this is started at BOOT (in which case onCreate and onStart are called), and is bound
 * from within ReportDetail Activity in WeatherReporter application. This Service is started in the
 * background for alert processing (standalone), bound in Activities to call methods on Binder to
 * register alert locations.
 * 
 * @author charliecollins
 * 
 */
public class WeatherAlertService extends Service {

    private static final String CLASSTAG = WeatherAlertService.class.getSimpleName();
    private static final String LOC = "LOC";
    private static final String ZIP = "ZIP";
    private static final long ALERT_QUIET_PERIOD = 10000;
    private static final long ALERT_POLL_INTERVAL = 15000;

    // convenience for Activity classes in the same process to get current device location
    // (so they don't have to repeat all the LocationManager and provider stuff locally)
    // (this would NOT work across applications, only for things in the same PROCESS)
    public static String deviceLocationZIP = "94102";

    private Timer timer;
    private DBHelper dbHelper;
    private NotificationManager nm;

    private TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // poll user specified locations
            List<Location> locations = dbHelper.getAllAlertEnabled();
            for (Location loc : locations) {
                WeatherRecord record = loadRecord(loc.zip);
                if (record.isSevere()) {
                    if ((loc.lastalert + WeatherAlertService.ALERT_QUIET_PERIOD) < System.currentTimeMillis()) {
                        loc.lastalert = System.currentTimeMillis();
                        dbHelper.update(loc);
                        sendNotification(loc.zip, record);
                    }
                }
            }

            // poll device location
            Location deviceAlertEnabledLoc = dbHelper.get(DBHelper.DEVICE_ALERT_ENABLED_ZIP);
            if (deviceAlertEnabledLoc != null) {
                WeatherRecord record = loadRecord(WeatherAlertService.deviceLocationZIP);
                if (record.isSevere()) {
                    if ((deviceAlertEnabledLoc.lastalert + WeatherAlertService.ALERT_QUIET_PERIOD) < System
                        .currentTimeMillis()) {
                        deviceAlertEnabledLoc.lastalert = System.currentTimeMillis();
                        dbHelper.update(deviceAlertEnabledLoc);
                        sendNotification(WeatherAlertService.deviceLocationZIP, record);
                    }
                }
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            notifyFromHandler((String) msg.getData().get(WeatherAlertService.LOC), (String) msg.getData().get(
                WeatherAlertService.ZIP));
        }
    };

    @Override
    public void onCreate() {
        this.dbHelper = new DBHelper(this);
        this.timer = new Timer();
        this.timer.schedule(this.task, 5000, WeatherAlertService.ALERT_POLL_INTERVAL);
        this.nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Log.v(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG + "   onStart");

        LocationManager locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final Geocoder geocoder = new Geocoder(this);

        LocationListener locListener = new LocationListener() {

            public void onLocationChanged(android.location.Location loc) {
                Log
                    .v(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG
                        + "   locationProvider LOCATION CHANGED lat/long - " + loc.getLatitude() + " "
                        + loc.getLongitude());
                double lati = loc.getLatitude();
                double longi = loc.getLongitude();
                try {
                    List<Address> addresses = geocoder.getFromLocation(lati, longi, 5);
                    if (addresses != null) {
                        for (Address a : addresses) {
                            Log.w(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG
                                + "   parsing address for geocode ZIP - country:" + a.getCountryCode() + " locality:"
                                + a.getLocality() + " postalCode:" + a.getPostalCode());
                            if (a.getPostalCode() != null) {
                                WeatherAlertService.deviceLocationZIP = addresses.get(0).getPostalCode();
                                Log.v(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG
                                    + "   updating deviceLocationZIP to " + WeatherAlertService.deviceLocationZIP);
                                break;
                            }
                        }
                        Log.v(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG
                            + "   after parsing all geocode addresses deviceLocationZIP = "
                            + WeatherAlertService.deviceLocationZIP);
                    } else {
                        Log.v(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG
                            + "   NOT updating deviceLocationZIP, geocode addresses NULL");
                    }
                } catch (IOException e) {
                    Log.e(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG, e);
                }
            }

            public void onProviderDisabled(String s) {
                Log.v(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG + "   locationProvider DISABLED - " + s);
            }
            public void onProviderEnabled(String s) {
                Log.v(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG + "   locationProvider ENABLED - " + s);
            }
            public void onStatusChanged(String s, int i, Bundle b) {
                Log
                    .v(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG + "   locationProvider STATUS CHANGE - "
                        + s);
            }
        };

        // we set minTime and minDistance to 0 here to get updated ALL THE TIME ALWAYS
        // in real life you DO NOT want to do this, it will consume too many resources
        // see LocationMangaer in JavaDoc for guidelines (time less than 60000 is not recommended)
        String locProvider = locMgr.getBestProvider(LocationHelper.PROVIDER_CRITERIA, true);
        Log.v(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG + "   locationProvider - " + locProvider);
        if (locProvider != null) {
            locMgr.requestLocationUpdates(locProvider, 0, 0, locListener);
        } else {
            Log.e(Constants.LOGTAG, " " + WeatherAlertService.CLASSTAG + "  NO LOCATION PROVIDER AVAILABLE");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.dbHelper.cleanup();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private WeatherRecord loadRecord(String zip) {
        final YWeatherFetcher ywh = new YWeatherFetcher(zip, true);
        return ywh.getWeather();
    }

    private void sendNotification(String zip, WeatherRecord record) {
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString(WeatherAlertService.ZIP, zip);
        bundle.putString(WeatherAlertService.LOC, record.getCity() + ", " + record.getRegion());
        message.setData(bundle);
        this.handler.sendMessage(message);
    }

    private void notifyFromHandler(String location, String zip) {
        Uri uri = Uri.parse("weather://com.msi.manning/loc?zip=" + zip);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Intent.FLAG_ACTIVITY_NEW_TASK, intent,
            PendingIntent.FLAG_ONE_SHOT);
        final Notification n = new Notification(R.drawable.severe_weather_24, "Severe Weather Alert!", System
            .currentTimeMillis());
        n.setLatestEventInfo(this, "Severe Weather Alert!", location, pendingIntent);
        this.nm.notify(Integer.parseInt(zip), n);
    }
}
