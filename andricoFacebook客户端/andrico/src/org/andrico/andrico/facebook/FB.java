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
/**
 * Copyright 2007 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.andrico.andrico.facebook;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class FB extends FBBase {
    final static String LOG = "Facebook";

    /**
     * @param api
     * @param secret
     */
    public FB(String api, String secret) {
        super(api, secret);
    }

    /**
     * Create a method that requests an authorization token to pass to the
     * facebook login page.
     * 
     * @link http://developers.facebook.com/documentation.php?v=1.0&method=auth.
     *       createToken
     * @link http://developers.facebook.com/documentation.php?doc=login_desktop
     *       Authentication Guide
     * @return
     */
    public FBMethod auth_createToken() {
        Log.d(LOG, "facebook.auth.createToken");

        // HashMap parameters =
        // getRequestParameters("facebook.auth.createToken");
        return getMethodFactory().FBMethod("facebook.auth.createToken");
    }

    /**
     * Create a FacebookMethod that expires a session object.
     * 
     * @link http://developers.facebook.com/documentation.php?v=1.0&method=auth.
     *       getSession
     * @link http://developers.facebook.com/documentation.php?doc=login_desktop
     *       Authentication Guide
     * @param authToken
     * @return
     */
    public FBMethod auth_expireSession() {
        Log.d(LOG, "facebook.auth.expireSession");

        return getMethodFactory().create("facebook.auth.expireSession", null);
    }

    /**
     * Create a FBMethod that requests a session object. This should be
     * called after the user has received an auth token and logged into
     * facebook.
     * 
     * @link http://developers.facebook.com/documentation.php?v=1.0&method=auth.
     *       getSession
     * @link http://developers.facebook.com/documentation.php?doc=login_desktop
     *       Authentication Guide
     * @param authToken
     * @return
     */
    public FBMethod auth_getSession(String authToken) {
        Log.d(LOG, "facebook.auth.getSession");
        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("auth_token", authToken);

        return getMethodFactory().create("facebook.auth.getSession", parameters);
    }

    /**
     * Create a method to get events of a particular user.
     * 
     * @link 
     *       http://developers.facebook.com/documentation.php?v=1.0&method=events
     *       .get
     * @param uid The uid to gather events for.
     * @return
     */
    public FBMethod events_get(String uid) {
        Log.d(LOG, "Creating facebook.events.get method.");

        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("uid", uid);

        return getMethodFactory().create("facebook.events.get", parameters, true);
    }

    /**
     * Post a user action.
     * 
     * @link http://developers.facebook.com/documentation.php?v=1.0&method=feed.
     *       publishActionOfUser
     * @param title The title of the action in the mini-feed
     * @param body The body text of the action in the mini-feed
     * @param images ArrayList.ArrayList A list at most four elements in size,
     *            each element a list having the image's url and optional link.
     * @return
     */
    public FBMethod feed_publishActionOfUser(String title, String body,
            ArrayList<ArrayList<String>> images) {
        Log.e(LOG, "Posting Action");

        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("title", title);
        parameters.put("body", body);

        images = (images == null) ? new ArrayList<ArrayList<String>>() : images;
        int imagesSize = images.size();
        for (int i = 0; i < imagesSize; i++) {
            ArrayList<String> image = images.get(i);
            String param = "image_" + Integer.toString(i);
            parameters.put(param, image.get(0));
            if (image.size() > 1) {
                parameters.put(param + "_link", image.get(1));
            }
        }
        return getMethodFactory().create("facebook.feed.publishActionOfUser", parameters, true);
    }

    /**
     * Create a method to query with the "Facebook Query Language"
     * 
     * @link 
     *       http://developers.facebook.com/documentation.php?v=1.0&method=fql.query
     * @param query A FBL query to send.
     * @return
     */
    public FBMethod fql_query(String query) {
        TreeMap<String, String> parameters = new TreeMap<String, String> ();
        parameters.put("query", query);

        return getMethodFactory().create("facebook.fql.query", parameters, true);
    }


    /**
     * Create a method to get status,first_name,last_name from the session's
     * user.
     * 
     * @link 
     *       http://developers.facebook.com/documentation.php?v=1.0&method=users.
     *       getInfo
     * @return
     */
 
    // MY METHOD!!!!!
    public FBMethod users_getMyInfo() {
        return users_getInfo(getSession().getUid(), "status,first_name,last_name,phone,adress");
    }
    
    
    public FBMethod users_getInfo() {
        return users_getInfo(getSession().getUid(), "status,first_name,last_name");
    }

    /**
     * Create a method to get information about a user or users.
     * 
     * @link http://developers.facebook.com/documentation.php?v=1.0&method=get.
     *       userInfo
     * @param uids String The users ids, comma-seperated.
     * @param fields String The fields to get, comma-seperated.
     * @return
     */
    public FBMethod users_getInfo(String uids, String fields) {
        Log.d(LOG, "Creating facebook.users.getInfo method.");

        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("uids", uids);
        parameters.put("fields", fields);

        return getMethodFactory().create("facebook.users.getInfo", parameters, true);
    }

    /**
     * @param extPerm Must be one of email, offline_access, status_update,
     *            photo_upload, create_listing, create_event, rsvp_event, sms
     * @return
     */
    public FBMethod users_hasAppPermission(String extPerm) {
        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("ext_perm", extPerm);
        return getMethodFactory().create("facebook.users.hasAppPermission", parameters, true);
    }

    /**
     * Create a method to set the session's uid's status.
     * 
     * @link 
     *       http://developers.facebook.com/documentation.php?v=1.0&method=users.
     *       setStatus
     * @param status The status to set.
     * @return
     */
    public FBMethod users_setStatus(String status, Boolean includesVerb) {
        Log.e(LOG, "Setting Facebook Status: " + status);
        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("status", status);
        parameters.put("status_includes_verb", includesVerb.toString());
        return getMethodFactory().create("facebook.users.setStatus", parameters, true);
    }

}
