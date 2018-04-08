/**
 * 
 */
package com.nanosheep.bikeroute.utility.contacts;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
 * Use reflection to return an instance of a contactaccessor specific
 * to the current environment. Code here is modified slightly from
 * http://developer.android.com/resources/samples/BusinessCard/index.html
 * 
 *
 * Copyright (C) 2009 The Android Open Source Project
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
 *
 * 
 * @author jono@nanosheep.net
 * @version Jul 28, 2010
 */
public abstract class AbstractContactAccessor {
	
	private static AbstractContactAccessor instance;
	
	public static AbstractContactAccessor getInstance() {
        if (instance == null) {
            final String className;

            @SuppressWarnings("deprecation")
            int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
            if (sdkVersion < 5) {
                className = "com.nanosheep.bikeroute.utility.contacts.ContactAccessorSdk3_4";
            } else {
                className = "com.nanosheep.bikeroute.utility.contacts.ContactAccessorSdk5";
            }

            /*
             * Find the required class by name and instantiate it.
             */
            try {
                final Class<? extends AbstractContactAccessor> clazz =
                        Class.forName(className).asSubclass(AbstractContactAccessor.class);
                instance = clazz.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        return instance;
    }

    /**
     * Returns the {@link Intent#ACTION_PICK} intent configured for the right authority: legacy
     * or current.
     */
    public abstract Intent getPickContactIntent();

    /**
     * Retrieves an address string for the given contact.
     */
    public abstract String loadAddress(ContentResolver contentResolver, Uri contactUri);

}
