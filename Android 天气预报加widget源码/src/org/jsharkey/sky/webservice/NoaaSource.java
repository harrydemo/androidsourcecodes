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
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsharkey.sky.ForecastProvider;
import org.jsharkey.sky.webservice.Forecast.ParseException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.TimeFormatException;

/**
 * Helper class to handle querying a webservice for forecast details and parsing
 * results into {@link ForecastProvider}.
 */
public class NoaaSource implements ForecastSource {
    private static final String TAG = "NoaaHelper";

    static final String WEBSERVICE_URL = "http://www.weather.gov/forecasts/xml/sample_products/browser_interface/ndfdBrowserClientByDay.php?&lat=%f&lon=%f&format=24+hourly&numDays=%d";

    /**
     * Various XML tags present in the response.
     */
    private static final String TAG_TEMPERATURE = "temperature";
    private static final String TAG_WEATHER = "weather";
    private static final String TAG_POP = "probability-of-precipitation";
    private static final String TAG_HAZARDS = "hazards";
    private static final String TAG_WEATHER_CONDITIONS = "weather-conditions";
    private static final String TAG_HAZARD = "hazard";
    private static final String TAG_LAYOUT_KEY = "layout-key";
    private static final String TAG_START_VALID_TIME = "start-valid-time";
    private static final String TAG_VALUE = "value";
    private static final String TAG_HAZARDTEXTURL = "hazardTextURL";
    private static final String TAG_MOREWEATHERINFORMATION = "moreWeatherInformation";

    /**
     * Various XML attributes present in the response.
     */
    private static final String ATTR_TIME_LAYOUT = "time-layout";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_WEATHER_SUMMARY = "weather-summary";
    private static final String ATTR_PHENOMENA = "phenomena";
    private static final String ATTR_SIGNIFICANCE = "significance";

    private static final String TYPE_MAXIMUM = "maximum";
    private static final String TYPE_MINIMUM = "minimum";

    /**
     * Recycled string builder used by {@link #parseDate(String)}.
     */
    private static Editable sEditable = new SpannableStringBuilder();

    /**
     * Recycled timestamp used by {@link #parseDate(String)}.
     */
    private static Time sTime = new Time();

    private static XmlPullParserFactory sFactory = null;

    /**
     * Parse a NWS date string into a Unix timestamp. Assumes incoming values
     * are in the format "2009-03-23T18:00:00-07:00", which we adjust slightly
     * to correctly follow RFC 3339 before parsing.
     */
    private static long parseDate(String raw) throws TimeFormatException {
        // Inject milliseconds so that NWS dates follow RFC
        sEditable.clear();
        sEditable.append(raw);
        sEditable.insert(19, ".000");

        String rfcFormat = sEditable.toString();
        sTime.parse3339(rfcFormat);
        return sTime.toMillis(false);
    }

    /**
     * Retrieve a specific {@link Forecast} object from the given {@link Map}
     * structure. If the {@link Forecast} doesn't exist, it's created and
     * returned.
     */
    private static Forecast getForecast(Map<String, List<Forecast>> forecasts,
            String layout, int index) {
        if (!forecasts.containsKey(layout)) {
            forecasts.put(layout, new ArrayList<Forecast>());
        }
        List<Forecast> layoutSpecific = forecasts.get(layout);

        while (index >= layoutSpecific.size()) {
            layoutSpecific.add(new Forecast());
        }
        return layoutSpecific.get(index);
    }

    /**
     * Flatten a set of {@link Forecast} objects that are separated into
     * <code>time-layout</code> sections in the given {@link Map}. This discards
     * any forecasts that have empty {@link Forecast#conditions}.
     * <p>
     * Sorts the resulting list by time, with any alerts forced to the top.
     */
    private static List<Forecast> flattenForecasts(Map<String, List<Forecast>> forecasts) {
        List<Forecast> flat = new ArrayList<Forecast>();

        // Collect together all forecasts that have valid conditions
        for (String layout : forecasts.keySet()) {
            for (Forecast forecast : forecasts.get(layout)) {
                if (!TextUtils.isEmpty(forecast.conditions)) {
                    flat.add(forecast);
                }
            }
        }

        // Sort by time, but always bump alerts to top
        Collections.sort(flat, new Comparator<Forecast>() {
            public int compare(Forecast left, Forecast right) {
                if (left.alert) {
                    return -1;
                } else {
                    return (int)(left.validStart - right.validStart);
                }
            }
        });

        return flat;
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Forecast> getForecasts(double lat, double lon, int days) throws ParseException {
        if (Double.isNaN(lat) || Double.isNaN(lon)) {
            throw new ParseException("Requested forecast for invalid location");
        } else {
            Log.d(TAG, String.format("queryLocation() with lat=%f, lon=%f, days=%d", lat, lon, days));
        }

        // Make API call to find forecasts
        String url = String.format(WEBSERVICE_URL, lat, lon, days);
        Reader reader = WebserviceHelper.queryApi(url);
        
        // Parse incoming forecast data
        List<Forecast> forecasts = parseResponse(reader);
        return forecasts;
    }

    /**
     * Parse a webservice XML response into {@link Forecast} objects.
     */
    private static List<Forecast> parseResponse(Reader response) throws ParseException {
        // Keep a temporary mapping between time series tags and forecasts
        Map<String, List<Forecast>> forecasts = new HashMap<String, List<Forecast>>();
        String detailsUrl = null;

        try {
            if (sFactory == null) {
                sFactory = XmlPullParserFactory.newInstance();
            }
            XmlPullParser xpp = sFactory.newPullParser();

            int index = 0;
            String thisTag = null;
            String thisLayout = null;
            String thisType = null;

            xpp.setInput(response);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    thisTag = xpp.getName();

                    if (TAG_TEMPERATURE.equals(thisTag) || TAG_WEATHER.equals(thisTag)
                            || TAG_POP.equals(thisTag) || TAG_HAZARDS.equals(thisTag)) {
                        thisLayout = xpp.getAttributeValue(null, ATTR_TIME_LAYOUT);
                        thisType = xpp.getAttributeValue(null, ATTR_TYPE);
                        index = -1;

                    } else if (TAG_WEATHER_CONDITIONS.equals(thisTag)) {
                        Forecast forecast = getForecast(forecasts, thisLayout, ++index);
                        forecast.conditions = xpp.getAttributeValue(null, ATTR_WEATHER_SUMMARY);

                    } else if (TAG_HAZARD.equals(thisTag)) {
                        Forecast forecast = getForecast(forecasts, thisLayout, ++index);
                        forecast.alert = true;
                        forecast.conditions = xpp.getAttributeValue(null, ATTR_PHENOMENA) + " "
                                + xpp.getAttributeValue(null, ATTR_SIGNIFICANCE);
                    }

                } else if (eventType == XmlPullParser.END_TAG) {
                    thisTag = null;

                } else if (eventType == XmlPullParser.TEXT) {
                    if (TAG_LAYOUT_KEY.equals(thisTag)) {
                        thisLayout = xpp.getText();
                        index = -1;

                    } else if (TAG_START_VALID_TIME.equals(thisTag)) {
                        Forecast forecast = getForecast(forecasts, thisLayout, ++index);
                        forecast.validStart = parseDate(xpp.getText());

                    } else if (TAG_VALUE.equals(thisTag) && TYPE_MAXIMUM.equals(thisType)) {
                        Forecast forecast = getForecast(forecasts, thisLayout, ++index);
                        forecast.tempHigh = Integer.parseInt(xpp.getText());
                        forecast.url = detailsUrl;

                    } else if (TAG_VALUE.equals(thisTag) && TYPE_MINIMUM.equals(thisType)) {
                        Forecast forecast = getForecast(forecasts, thisLayout, ++index);
                        forecast.tempLow = Integer.parseInt(xpp.getText());

                    } else if (TAG_HAZARDTEXTURL.equals(thisTag)) {
                        Forecast forecast = getForecast(forecasts, thisLayout, index);
                        forecast.url = xpp.getText();

                    } else if (TAG_MOREWEATHERINFORMATION.equals(thisTag)) {
                        detailsUrl = xpp.getText();

                    }
                }
                eventType = xpp.next();
            }
        } catch (IOException e) {
            throw new ParseException("Problem parsing XML forecast", e);
        } catch (XmlPullParserException e) {
            throw new ParseException("Problem parsing XML forecast", e);
        } catch (TimeFormatException e) {
            throw new ParseException("Problem parsing XML forecast", e);
        }

        // Flatten non-empty forecasts into single list
        return flattenForecasts(forecasts);
    }
}
