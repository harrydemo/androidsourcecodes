 /*************************************************************************** 
 *              Copyright (C) 2009 Andrico Team                             * 
 *              http://code.google.com/p/andrico/                           *
 *                             												*
 * Licensed under the Apache License, Version 2.0 (the "License");			*
 * you may not use this file except in compliance with the License.			*
 * 																			*	
 * You may obtain a copy of the License at 									*
 * http://www.apache.org/licenses/LICENSE-2.0								*
 *																			*
 * Unless required by applicable law or agreed to in writing, software		*
 * distributed under the License is distributed on an "AS IS" BASIS,		*
 *																			*
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.	*
 * See the License for the specific language governing permissions and		*
 * limitations under the License.											*
 ****************************************************************************/

package org.andrico.andrico;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonParser {
    
    /**
     * Check the result for an error code.
     * 
     * @param result
     * @return -1 if no error, otherwise the integer error code, null if a
     *         problem occurred.
     */
    public static Integer parseForErrorCode(String result) {
        JSONObject error;
        try {
            error = new JSONObject(result);
            return error.getInt("error_code");
        } catch (JSONException e) {
            // This likely means that the result is not an error.
            return -1;
        } catch (NullPointerException e) {
            return null;
        }
    }
}
