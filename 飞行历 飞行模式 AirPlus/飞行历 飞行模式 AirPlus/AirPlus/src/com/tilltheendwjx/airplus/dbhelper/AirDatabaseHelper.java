/**
 * 
 */
package com.tilltheendwjx.airplus.dbhelper;

import com.tilltheendwjx.airplus.Air;
import com.tilltheendwjx.airplus.utils.Log;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;

/**
 * @author wjx
 * 
 */
public class AirDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "airs.db";
	private static final int DATABASE_VERSION = 3;

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public AirDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	public AirDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE airs (" + "_id INTEGER PRIMARY KEY,"
				+ "start_hour INTEGER, " + "start_minutes INTEGER, "
				+ "end_hour INTEGER, " + "end_minutes INTEGER, "
				+ "daysofweek INTEGER, " + "start_Airtime INTEGER, "
				+ "end_Airtime INTEGER, " + "enabled INTEGER, "
				+ "vibrate INTEGER, " + "message TEXT,"
				+ "airModeRadios TEXT);");

		// insert default Airs
		String insertMe = "INSERT INTO airs "
				+ "(start_hour, start_minutes, end_hour, end_minutes, daysofweek, start_Airtime, end_Airtime, enabled, vibrate, "
				+ " message, airModeRadios) VALUES ";
		db.execSQL(insertMe + "(8, 30, 9, 30, 31, 0, 0, 0 ,1, '','');");
		db.execSQL(insertMe + "(10, 00, 11, 00, 31, 0, 0, 0 ,1 , '','');");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
		if (Log.LOGV)
			Log.v("Upgrading airs database from version " + oldVersion + " to "
					+ currentVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS Airs");
		onCreate(db);
	}

	public Uri commonInsert(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		long rowId = db.insert("airs", Air.Columns.MESSAGE, values);
		if (rowId < 0) {
			throw new SQLException("Failed to insert row");
		}
		if (Log.LOGV)
			Log.v("Added air rowId = " + rowId);

		return ContentUris.withAppendedId(Air.Columns.CONTENT_URI, rowId);
	}

}
