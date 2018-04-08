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

import org.jsharkey.sky.webservice.Forecast.ParseException;

import java.util.List;

public interface ForecastSource {
    
    /**
     * Query the given location and parse any returned data into a set of
     * {@link Forecast} objects. This is a blocking call while waiting for the
     * webservice to return.
     */
    public List<Forecast> getForecasts(double lat, double lon, int days) throws ParseException;

}
