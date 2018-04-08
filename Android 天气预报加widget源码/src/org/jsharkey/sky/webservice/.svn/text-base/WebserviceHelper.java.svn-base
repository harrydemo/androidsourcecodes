/*
 * Copyright (C) 2009 Jeff Sharkey, http://jsharkey.org/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jsharkey.sky.webservice;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsharkey.sky.ForecastProvider;
import org.jsharkey.sky.ForecastUtils;
import org.jsharkey.sky.R;
import org.jsharkey.sky.ForecastProvider.AppWidgets;
import org.jsharkey.sky.ForecastProvider.AppWidgetsColumns;
import org.jsharkey.sky.ForecastProvider.Forecasts;
import org.jsharkey.sky.ForecastProvider.ForecastsColumns;
import org.jsharkey.sky.webservice.Forecast.ParseException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;

/**
 * Helper class to handle querying a webservice for forecast details and parsing
 * results into {@link ForecastProvider}.
 */
public class WebserviceHelper {
    private static final String TAG = "ForcastHelper";

    private static final String[] PROJECTION_APPWIDGET = {
        AppWidgetsColumns.LAT,
        AppWidgetsColumns.LON,
        AppWidgetsColumns.COUNTRY_CODE
    };

    private static final int COL_LAT = 0;
    private static final int COL_LON = 1;
    private static final int COL_COUNTRY_CODE = 2;
    
    public static final String COUNTRY_US = "US";

    /**
     * Timeout to wait for webservice to respond. Because we're in the
     * background, we don't mind waiting for good data.
     */
    static final long WEBSERVICE_TIMEOUT = 30 * DateUtils.SECOND_IN_MILLIS;

    /**
     * User-agent string to use when making requests. Should be filled using
     * {@link #prepareUserAgent(Context)} before making any other calls.
     */
    private static String sUserAgent = null;
    
    private static HttpClient sClient = new DefaultHttpClient();

    /**
     * Prepare the internal User-Agent string for use. This requires a
     * {@link Context} to pull the package name and version number for this
     * application.
     */
    public static void prepareUserAgent(Context context) {
        try {
            // Read package name and version number from manifest
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            sUserAgent = String.format(context.getString(R.string.template_user_agent),
                    info.packageName, info.versionName);
            
        } catch(NameNotFoundException e) {
            Log.e(TAG, "Couldn't find package information in PackageManager", e);
        }
    }
    
    /**
     * Open a request to the given URL, returning a {@link Reader} across the
     * response from that API.
     */
    public static Reader queryApi(String url) throws ParseException {
        if (sUserAgent == null) {
            throw new ParseException("Must prepare user agent string");
        }
        
        Reader reader = null;
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", sUserAgent);

        try {
            HttpResponse response = sClient.execute(request);

            StatusLine status = response.getStatusLine();
            Log.d(TAG, "Request returned status " + status);

            HttpEntity entity = response.getEntity();
            reader = new InputStreamReader(entity.getContent());

        } catch (IOException e) {
            throw new ParseException("Problem calling forecast API", e);
        }
        
        return reader;
    }

    /**
     * Perform a webservice query to retrieve and store the forecast for the
     * given widget. This call blocks until request is finished and
     * {@link Forecasts#CONTENT_URI} has been updated.
     */
    public static void updateForecasts(Context context, Uri appWidgetUri, int days)
            throws ParseException {
        
        if (sUserAgent == null) {
            prepareUserAgent(context);
        }

        Uri appWidgetForecasts = Uri.withAppendedPath(appWidgetUri, AppWidgets.TWIG_FORECASTS);

        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = null;
        double lat = Double.NaN;
        double lon = Double.NaN;
        String countryCode = null;

        // Pull exact forecast location from database
        try {
            cursor = resolver.query(appWidgetUri, PROJECTION_APPWIDGET, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                lat = cursor.getDouble(COL_LAT);
                lon = cursor.getDouble(COL_LON);
                countryCode = cursor.getString(COL_COUNTRY_CODE);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, "using country code=" + countryCode);

        // Query webservice for this location
        List<Forecast> forecasts = null;
        if (COUNTRY_US.equals(countryCode)) {
            forecasts = new NoaaSource().getForecasts(lat, lon, days);
        } else {
            forecasts = new MetarSource().getForecasts(lat, lon, days);
        }

        if (forecasts == null || forecasts.size() == 0) {
            throw new ParseException("No forecasts found from webservice query");
        }

        // Purge existing forecasts covered by incoming data, and anything
        // before today
        long lastMidnight = ForecastUtils.getLastMidnight();
        long earliest = Long.MAX_VALUE;
        for (Forecast forecast : forecasts) {
            earliest = Math.min(earliest, forecast.validStart);
        }

        resolver.delete(appWidgetForecasts,
            ForecastsColumns.VALID_START + " >= " + earliest + " OR " +
            ForecastsColumns.VALID_START + " <= " + lastMidnight, null);

        // Insert any new forecasts found
        ContentValues values = new ContentValues();
        for (Forecast forecast : forecasts) {
            Log.d(TAG, "inserting forecast with validStart=" + forecast.validStart);
            values.clear();
            values.put(ForecastsColumns.VALID_START, forecast.validStart);
            values.put(ForecastsColumns.TEMP_HIGH, forecast.tempHigh);
            values.put(ForecastsColumns.TEMP_LOW, forecast.tempLow);
            values.put(ForecastsColumns.CONDITIONS, forecast.conditions);
            values.put(ForecastsColumns.URL, forecast.url);
            if (forecast.alert) {
                values.put(ForecastsColumns.ALERT, ForecastsColumns.ALERT_TRUE);
            }
            resolver.insert(appWidgetForecasts, values);
        }

        // Mark widget cache as being updated
        values.clear();
        values.put(AppWidgetsColumns.LAST_UPDATED, System.currentTimeMillis());
        resolver.update(appWidgetUri, values, null, null);
    }

}
