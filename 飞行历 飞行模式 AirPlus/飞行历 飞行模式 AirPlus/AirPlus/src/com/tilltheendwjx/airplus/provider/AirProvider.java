/**
 * 
 */
package com.tilltheendwjx.airplus.provider;

import com.tilltheendwjx.airplus.dbhelper.AirDatabaseHelper;
import com.tilltheendwjx.airplus.utils.Log;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * @author wjx
 * 
 */
public class AirProvider extends ContentProvider {

	private AirDatabaseHelper mOpenHelper;

	private static final int AIRS = 1;
	private static final int AIRS_ID = 2;
	private static final UriMatcher sURLMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		sURLMatcher.addURI("com.tilltheendwjx.airplus", "air", AIRS);
		sURLMatcher.addURI("com.tilltheendwjx.airplus", "air/#", AIRS_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri url, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		long rowId = 0;
		switch (sURLMatcher.match(url)) {
		case AIRS:
			count = db.delete("airs", where, whereArgs);
			break;
		case AIRS_ID:
			String segment = url.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			if (TextUtils.isEmpty(where)) {
				where = "_id=" + segment;
			} else {
				where = "_id=" + segment + " AND (" + where + ")";
			}
			count = db.delete("airs", where, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Cannot delete from URL: " + url);
		}

		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri url) {
		int match = sURLMatcher.match(url);
		switch (match) {
		case AIRS:
			return "vnd.android.cursor.dir/airs";
		case AIRS_ID:
			return "vnd.android.cursor.item/airs";
		default:
			throw new IllegalArgumentException("Unknown URL");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri url, ContentValues initialValues) {
		if (sURLMatcher.match(url) != AIRS) {
			throw new IllegalArgumentException("Cannot insert into URL: " + url);
		}

		Uri newUrl = mOpenHelper.commonInsert(initialValues);
		getContext().getContentResolver().notifyChange(newUrl, null);
		return newUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		mOpenHelper = new AirDatabaseHelper(getContext());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 * java.lang.String[], java.lang.String, java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public Cursor query(Uri url, String[] projectionIn, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		// Generate the body of the query
		int match = sURLMatcher.match(url);
		switch (match) {
		case AIRS:
			qb.setTables("airs");
			break;
		case AIRS_ID:
			qb.setTables("airs");
			qb.appendWhere("_id=");
			qb.appendWhere(url.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor ret = qb.query(db, projectionIn, selection, selectionArgs, null,
				null, sortOrder);

		if (ret == null) {
			if (Log.LOGV)
				Log.v("Alarms.query: failed");
		} else {
			ret.setNotificationUri(getContext().getContentResolver(), url);
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri url, ContentValues values, String where,
			String[] whereArgs) {
		int count;
		long rowId = 0;
		int match = sURLMatcher.match(url);
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		switch (match) {
		case AIRS_ID: {
			String segment = url.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			count = db.update("airs", values, "_id=" + rowId, null);
			break;
		}
		default: {
			throw new UnsupportedOperationException("Cannot update URL: " + url);
		}
		}
		if (Log.LOGV)
			Log.v("*** notifyChange() rowId: " + rowId + " url " + url);
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

}
