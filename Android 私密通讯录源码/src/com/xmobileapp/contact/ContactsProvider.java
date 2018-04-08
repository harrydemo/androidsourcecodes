/*
 * [程序名称] Android 通讯录
 * [作者] xmobileapp团队
 * [参考资料] Google Android Samples 
 * [开源协议] Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
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

package com.xmobileapp.contact;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ContactsProvider extends ContentProvider {
	private static final String TAG= "ContactsProvider"; 

	private DBHelper dbHelper;
	private SQLiteDatabase contactsDB;
	
	public static final String AUTHORITY = "com.xmobileapp.provider.contact";
	public static final String CONTACTS_TABLE = "contacts";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/contacts");
	
	public static final int CONTACTS = 1;
	public static final int CONTACT_ID = 2;
	private static final UriMatcher uriMatcher;	
	
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY,"contacts",CONTACTS);
		//单独列
		uriMatcher.addURI(AUTHORITY,"contacts/#",CONTACT_ID);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(getContext());
		contactsDB = dbHelper.getWritableDatabase();
		return (contactsDB == null)? false : true;
	}
	// 删除指定数据列
	@Override
	public int delete(Uri uri, String where, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count;
		switch (uriMatcher.match(uri)) {
			case CONTACTS:
				count = contactsDB.delete(CONTACTS_TABLE, where, selectionArgs);
				break;
			case CONTACT_ID:
				String contactID = uri.getPathSegments().get(1);
				count = contactsDB.delete(CONTACTS_TABLE, 
						ContactColumn._ID+ "="+contactID
						+ (!TextUtils.isEmpty(where) ? " AND ("+ where + ")" : ""),
						selectionArgs);
				break;
			default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	// URI类型转换
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
			case CONTACTS:
			return "vnd.android.cursor.dir/vnd.xmobileapp.contact";
			case CONTACT_ID:
			return "vnd.android.cursor.item/vnd.xmobileapp.contact";
			default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
	// 插入数据
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		
		if (uriMatcher.match(uri) != CONTACTS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
			Log.e(TAG+"insert","initialValues is not null");
		} else {
			values = new ContentValues();
		}
		Long now = Long.valueOf(System.currentTimeMillis());
		// 设置默认值
		if (values.containsKey(ContactColumn.CREATED) == false) {
			values.put(ContactColumn.CREATED, now);
		}
		if (values.containsKey(ContactColumn.MODIFIED) == false) {
			values.put(ContactColumn.MODIFIED, now);
		}
		if (values.containsKey(ContactColumn.NAME) == false) {
			values.put(ContactColumn.NAME, "");
			Log.e(TAG+"insert","NAME is null");
		}
		if (values.containsKey(ContactColumn.MOBILE) == false) {
			values.put(ContactColumn.MOBILE, "");
		}
		if (values.containsKey(ContactColumn.EMAIL) == false) {
			values.put(ContactColumn.EMAIL, "");
		}
		Log.e(TAG+"insert",values.toString());
		long rowId = contactsDB.insert(CONTACTS_TABLE, null, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(CONTENT_URI,rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			Log.e(TAG+"insert",noteUri.toString());
			return noteUri;
		}

		throw new SQLException("Failed to insert row into " + uri);

	}
	// 查询数据
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.e(TAG+":query"," in Query");
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(CONTACTS_TABLE);
		
		switch (uriMatcher.match(uri)) {
			case CONTACT_ID:
				qb.appendWhere(ContactColumn._ID + "=" + uri.getPathSegments().get(1));
				break;
			default: break;
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = ContactColumn._ID;
		} else {
			orderBy = sortOrder;
		}
		Cursor c = qb.query(contactsDB,projection,
							selection, selectionArgs,
							null, null,orderBy);

		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
		
	}
	// 更新数据库
	@Override
	public int update(Uri uri, ContentValues values, String where, String[] selectionArgs) {

		int count;
		Log.e(TAG+"update",values.toString());
		Log.e(TAG+"update",uri.toString());
		Log.e(TAG+"update :match",""+uriMatcher.match(uri));
		switch (uriMatcher.match(uri)) {
			case CONTACTS:
				Log.e(TAG+"update",CONTACTS+"");
				count = contactsDB.update(CONTACTS_TABLE, values, where, selectionArgs);
				break;
			case CONTACT_ID:
				String contactID = uri.getPathSegments().get(1);
				Log.e(TAG+"update",contactID+"");
				count = contactsDB.update(CONTACTS_TABLE,values,
						ContactColumn._ID + "=" + contactID
						+ (!TextUtils.isEmpty(where) ? " AND ("+ where + ")" : ""), 
						selectionArgs);
				break;
			default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
