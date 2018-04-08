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

package org.jsharkey.sky;

import java.util.regex.Pattern;

import org.jsharkey.sky.ForecastProvider.AppWidgetsColumns;
import org.jsharkey.sky.ForecastProvider.ForecastsColumns;

import android.content.res.Resources;
import android.text.format.Time;

/**
 * Various forecast utilities.
 */
public class ForecastUtils {
    /**
     * Time when we consider daytime to begin. We keep this early to make sure
     * that our 6AM widget update will change icons correctly.
     */
    private static final int DAYTIME_BEGIN_HOUR = 5;

    /**
     * Time when we consider daytime to end. We keep this early to make sure
     * that our 6PM widget update will change icons correctly.
     */
    private static final int DAYTIME_END_HOUR = 16;
    
    private static final String[] sIconAlert = new String[] { "alert", "advisory", "warning", "watch" };
    private static final String[] sIconStorm = new String[] { "thunder", "tstms" };
    private static final String[] sIconSnow = new String[] { "snow", "ice", "frost", "flurr", "wintry" };
    private static final String[] sIconShower = new String[] { "rain" };
    private static final String[] sIconScatter = new String[] { "shower", "drizzle" };
    private static final String[] sIconClear = new String[] { "sunny", "breezy", "clear" };
    private static final String[] sIconClouds = new String[] { "cloud", "fog" };

    /**
     * Select an icon to describe the given {@link ForecastsColumns#CONDITIONS}
     * string. Uses a descending importance scale that matches keywords against
     * the described conditions.
     * 
     * @param daytime If true, return daylight-specific icons when available,
     *            otherwise assume night icons.
     */
    public static int getIconForForecast(String conditions, boolean daytime) {
        int icon = 0;
        conditions = conditions.toLowerCase();
        
        if (stringContains(conditions, sIconAlert)) {
            icon = R.drawable.weather_severe_alert;
        } else if (stringContains(conditions, sIconStorm)) {
            icon = R.drawable.weather_storm;
        } else if (stringContains(conditions, sIconSnow)) {
            icon = R.drawable.weather_snow;
        } else if (stringContains(conditions, sIconShower)) {
            icon = R.drawable.weather_showers;
        } else if (stringContains(conditions, sIconScatter)) {
            icon = R.drawable.weather_showers_scattered;
        } else if (stringContains(conditions, sIconClear)) {
            icon = daytime ? R.drawable.weather_clear : R.drawable.weather_clear_night;
        } else if (stringContains(conditions, sIconClouds)) {
            icon = daytime ? R.drawable.weather_few_clouds : R.drawable.weather_few_clouds_night;
        }
        return icon;
    }

    /**
     * Search the subject string for the given words using a case-sensitive
     * search. This is usually faster than a {@link Pattern} regular expression
     * because we don't have JIT.
     */
    private static boolean stringContains(String subject, String[] searchWords) {
        for (String word : searchWords) {
            if (subject.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Correctly format the given temperature for user display.
     * 
     * @param temp Temperature to format, in degrees Fahrenheit.
     * @param units Target units to convert to before displaying, usually
     *            something like {@link AppWidgetsColumns#UNITS_FAHRENHEIT}.
     */
    public static String formatTemp(Resources res, int temp, int units) {
        if (units == AppWidgetsColumns.UNITS_FAHRENHEIT) {
            return res.getString(R.string.temperature_f, temp);
        } else if (units == AppWidgetsColumns.UNITS_CELSIUS) {
            // Convert to Celsius before display
            temp = ((temp - 32) * 5) / 9;
            return res.getString(R.string.temperature_c, temp);
        }
        return null;
    }

    /**
     * Get the timestamp of the last midnight, in a base similar to
     * {@link System#currentTimeMillis()}.
     */
    public static long getLastMidnight() {
        Time time = new Time();
        time.setToNow();
        time.hour = 0;
        time.minute = 0;
        time.second = 0;
        return time.toMillis(false);
    }

    /**
     * Calcuate if it's currently "daytime" by our internal definition. Used to
     * decide which icons to show when updating widgets.
     */
    public static boolean isDaytime() {
        Time time = new Time();
        time.setToNow();
        return (time.hour >= DAYTIME_BEGIN_HOUR && time.hour <= DAYTIME_END_HOUR);
    }
}
