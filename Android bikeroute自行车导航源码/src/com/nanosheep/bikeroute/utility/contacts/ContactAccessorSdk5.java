/**
 * 
 */
package com.nanosheep.bikeroute.utility.contacts;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

/**
 * A contactaccessor which retrieves addresses in string form, for newer
 *  android devices (post 2.0), modified from 
 *  http://developer.android.com/resources/samples/BusinessCard/index.html
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
public class ContactAccessorSdk5 extends AbstractContactAccessor {

	/* (non-Javadoc)
	 * @see com.nanosheep.bikeroute.ContactAccessor#getPickContactIntent()
	 */
	@Override
	public Intent getPickContactIntent() {
		return new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
	}

	/* (non-Javadoc)
	 * @see com.nanosheep.bikeroute.ContactAccessor#loadAddress(android.content.ContentResolver, android.net.Uri)
	 */
	@Override
	public String loadAddress(final ContentResolver contentResolver, final Uri contactUri) {
		Long contactId = (long) -1;
		String addr = "";

        // Load the id for the specified person
        final Cursor cursor = contentResolver.query(contactUri,
                new String[]{BaseColumns._ID, Contacts.DISPLAY_NAME}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                contactId = cursor.getLong(0);
                final String where = ContactsContract.Data.CONTACT_ID + " = ? AND "
                + ContactsContract.Data.MIMETYPE + " = ?"; 
         		final String[] whereParameters = new String[]{contactId.toString(), 
         				ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE}; 
         		
         		final Cursor addrCur = contentResolver.query(ContactsContract.Data.CONTENT_URI, null,
         				where, whereParameters, null); 
         		if(addrCur.moveToFirst()) {
         			addr = addrCur.getString(addrCur.getColumnIndex(
         					ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
         			
         		} 
         		addrCur.close();
            }
        } finally {
            cursor.close();
        }

        return addr;
	}

}
