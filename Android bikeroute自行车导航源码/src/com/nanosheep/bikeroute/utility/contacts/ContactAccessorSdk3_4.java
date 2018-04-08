/**
 * 
 */
package com.nanosheep.bikeroute.utility.contacts;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.Contacts;
import android.provider.Contacts.ContactMethodsColumns;
import android.provider.Contacts.People;
import android.provider.Contacts.People.Phones;
import android.provider.Contacts.PhonesColumns;

/**
 * A contactaccessor to older android devices (pre 2.0), modified from
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
@SuppressWarnings("deprecation")
public class ContactAccessorSdk3_4 extends AbstractContactAccessor {

	/* (non-Javadoc)
	 * @see com.nanosheep.bikeroute.ContactAccessor#getPickContactIntent()
	 */
	@Override
	public Intent getPickContactIntent() {
		return new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
	}

	/* (non-Javadoc)
	 * @see com.nanosheep.bikeroute.ContactAccessor#loadContact(android.content.ContentResolver, android.net.Uri)
	 */
	@Override
	public String loadAddress(final ContentResolver contentResolver,
			final Uri contactUri) {
		String addr = "";

        final Uri phoneUri = Uri.withAppendedPath(contactUri, Phones.CONTENT_DIRECTORY);
        final Cursor cursor = contentResolver.query(phoneUri,
                new String[]{PhonesColumns.NUMBER}, null, null, PhonesColumns.ISPRIMARY + " DESC");

        try {
            if (cursor.moveToFirst()) {
            	final String personId = cursor.getString(cursor.getColumnIndex(BaseColumns._ID));
            	final String addrWhere = Contacts.ContactMethods.PERSON_ID 
                + " = ? AND " + ContactMethodsColumns.KIND + " = ?"; 
                final String[] addrWhereParams = new String[]{personId, 
                		Contacts.ContactMethods.CONTENT_POSTAL_ITEM_TYPE}; 		
                final Cursor addrCur = contentResolver.query(Contacts.ContactMethods.CONTENT_URI, 
                null, addrWhere, addrWhereParams, null); 
                if(addrCur.moveToFirst()) {
                	addr = addrCur.getString(
                   addrCur.getColumnIndex(Contacts.ContactMethodsColumns.DATA));
                } 
                addrCur.close();
            }
        } finally {
            cursor.close();
        }

        return addr;
	}

}
