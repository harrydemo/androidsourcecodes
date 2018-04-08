/**
 * 
 */
package com.ty.weather;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.ty.weather.util.ForecastEntity;
import com.ty.weather.util.WidgetEntity;

/**
 * @author TANGYONG
 * @version 1.0 date : 2010-05-21
 */
public class ForecastProvider extends ContentProvider {
	private static final String TAG = "ForecastProvider";
	// Content URI
	public static final String AUTHORITY = "com.ty.weather";
	// table weather name
	public static final String TABLE_WIDGET = "weather_widget";
	// table weather detail name;
	private static final String TABLE_FORECAST = "weather_forecast";

	private DatabaseHelper dbHelper;

	public static class WeatherWidgets implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/widgets");
		
		public static final String FORECAST_END = "forecasts";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/awidget";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/widget";

	}

	public static class WeatherDetails implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/forecasts");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/forecasts";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/forecasts";

	}

	/**
	 * Helper to manage upgrading between versions of the forecast database.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "forecasts.db";

		private static final int DATABASE_VERSION = 2;

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_WIDGET + " (" 
					+ BaseColumns._ID + " INTEGER PRIMARY KEY," 
					+ WidgetEntity.CITY + " TEXT,"
					+ WidgetEntity.UPDATE_MILIS + " INTEGER,"
					+ WidgetEntity.IS_CONFIGURED + " INTEGER,"
					+ WidgetEntity.POSTALCODE + " TEXT,"
					+ WidgetEntity.FORECASTDATE + " INTEGER,"
					+ WidgetEntity.CONDITION + " TEXT,"
					+ WidgetEntity.TEMPF + " INTEGER," 
					+ WidgetEntity.TEMPC + " INTEGER," 
					+ WidgetEntity.HUMIDITY + " TEXT,"
					+ WidgetEntity.ICON + " TEXT,"
					+ WidgetEntity.WINDCONDITION + " TEXT,"
					+ WidgetEntity.LAST_UPDATE_TIME + " INTEGER);");

			db.execSQL("CREATE TABLE " + TABLE_FORECAST + " (" 
					+ BaseColumns._ID + " INTEGER PRIMARY KEY," 
					+ ForecastEntity.WIDGET_ID + " INTEGER," 
					+ ForecastEntity.DAYOFWEEK + " TEXT,"
					+ ForecastEntity.LOW + " INTEGER,"
					+ ForecastEntity.HIGHT + " INTEGER,"
					+ ForecastEntity.ICON + " TEXT,"
					+ ForecastEntity.CONDITION + " TEXT);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			int version = oldVersion;

			if (version != DATABASE_VERSION) {
				Log.w(TAG, "Destroying old data during upgrade.");
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIDGET);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORECAST);
				onCreate(db);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		Log.d(TAG, "delete() with uri=" + uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		int count = 0;

		switch (uriMatcher.match(uri)) {
		case WIDGETS: {
            count = db.delete(TABLE_WIDGET, selection, selectionArgs);
            break;
        }
        case WIDGETS_ID: {
            // Delete a specific widget and all its forecasts
            long appWidgetId = Long.parseLong(uri.getPathSegments().get(1));
            count = db.delete(TABLE_WIDGET, BaseColumns._ID + "=" + appWidgetId, null);
            count += db.delete(TABLE_FORECAST, ForecastEntity.WIDGET_ID + "="
                    + appWidgetId, null);
            break;
        }
        case WIDGETS_FORECASTS: {
            // Delete all the forecasts for a specific widget
            long appWidgetId = Long.parseLong(uri.getPathSegments().get(1));
            if (selection == null) {
                selection = "";
            } else {
                selection = "(" + selection + ") AND ";
            }
            selection += ForecastEntity.WIDGET_ID + "=" + appWidgetId;
            count = db.delete(TABLE_FORECAST, selection, selectionArgs);
            break;
        }
		case FORECASTS: {
			count = db.delete(TABLE_FORECAST, selection, selectionArgs);
			break;
		}
		case FORECASTS_ID: {
			long forecaseId = Long.parseLong(uri.getPathSegments().get(1));
			count = db.delete(TABLE_FORECAST, BaseColumns._ID + "=" + forecaseId,
					null);
			break;
		}
		default:
			throw new UnsupportedOperationException();
		}

		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
        case WIDGETS:
            return WeatherWidgets.CONTENT_TYPE;
        case WIDGETS_ID:
            return WeatherWidgets.CONTENT_ITEM_TYPE;
        case WIDGETS_FORECASTS:
            return WeatherDetails.CONTENT_TYPE;
		case FORECASTS:
			return WeatherDetails.CONTENT_TYPE;
		case FORECASTS_ID:
			return WeatherDetails.CONTENT_ITEM_TYPE;
		}
		throw new IllegalStateException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Log.d(TAG, "insert() with uri=" + uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Uri resultUri = null;

		switch (uriMatcher.match(uri)) {
		case WIDGETS: {
            long rowId = db.insert(TABLE_WIDGET, WidgetEntity.POSTALCODE, values);
            if (rowId != -1) {
                resultUri = ContentUris.withAppendedId(WeatherWidgets.CONTENT_URI, rowId);
            }
            break;
        }
        case WIDGETS_FORECASTS: {
            // Insert a forecast into a specific widget
            long appWidgetId = Long.parseLong(uri.getPathSegments().get(1));
            values.put(ForecastEntity.WIDGET_ID, appWidgetId);
            long rowId = db.insert(TABLE_FORECAST, ForecastEntity.CONDITION, values);
            if (rowId != -1) {
                resultUri = ContentUris.withAppendedId(WeatherDetails.CONTENT_URI, rowId);
            }
            break;
        }
        case FORECASTS: {
			long rowId = db.insert(TABLE_FORECAST,
					ForecastEntity.CONDITION, values);
			if (rowId != -1) {
				resultUri = ContentUris.withAppendedId(WeatherDetails.CONTENT_URI,
						rowId);
			}
			break;
		}
		default:
			throw new UnsupportedOperationException();
		}

		return resultUri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG, "Forecast Provider onCreate!");

		dbHelper = new DatabaseHelper(getContext());
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
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Log.d(TAG, "query() with uri=" + uri);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String limit = null;

		switch (uriMatcher.match(uri)) {
		case WIDGETS: {
            qb.setTables(TABLE_WIDGET);
            break;
        }
        case WIDGETS_ID: {
            String appWidgetId = uri.getPathSegments().get(1);
            qb.setTables(TABLE_WIDGET);
            qb.appendWhere(BaseColumns._ID + "=" + appWidgetId);
            break;
        }
        case WIDGETS_FORECASTS: {
            // Pick all the forecasts for given widget, sorted by date and
            // importance
            String appWidgetId = uri.getPathSegments().get(1);
            qb.setTables(TABLE_FORECAST);
            qb.appendWhere(ForecastEntity.WIDGET_ID + "=" + appWidgetId);
            sortOrder = BaseColumns._ID + " ASC";
            break;
        }case FORECASTS: {
			qb.setTables(TABLE_FORECAST);
			break;
		}
		case FORECASTS_ID: {
			String forecastId = uri.getPathSegments().get(1);
			qb.setTables(TABLE_FORECAST);
			qb.appendWhere(BaseColumns._ID + "=" + forecastId);
			break;
		}
		}

		return qb.query(db, projection, selection, selectionArgs, null, null,
				sortOrder, limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		Log.d(TAG, "update() with uri=" + uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		switch (uriMatcher.match(uri)) {
		case WIDGETS: {
            return db.update(TABLE_WIDGET, values, selection, selectionArgs);
        }
        case WIDGETS_ID: {
            long appWidgetId = Long.parseLong(uri.getPathSegments().get(1));
            return db.update(TABLE_WIDGET, values, BaseColumns._ID + "=" + appWidgetId,
                    null);
        }
        case FORECASTS: {
			return db.update(TABLE_FORECAST, values, selection, selectionArgs);
		}
		}

		throw new UnsupportedOperationException();
	}

	/**
	 * Matcher used to filter an incoming {@link Uri}.
	 */
	private static final UriMatcher uriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	private static final int WIDGETS = 101;
	private static final int WIDGETS_ID = 102;
	private static final int WIDGETS_FORECASTS = 103;

	private static final int FORECASTS = 201;
	private static final int FORECASTS_ID = 202;

	static {
		uriMatcher.addURI(AUTHORITY, "widgets", WIDGETS);
		uriMatcher.addURI(AUTHORITY, "widgets/#", WIDGETS_ID);
		uriMatcher.addURI(AUTHORITY, "widgets/#/forecasts", WIDGETS_FORECASTS);

		uriMatcher.addURI(AUTHORITY, "forecasts", FORECASTS);
		uriMatcher.addURI(AUTHORITY, "forecasts/#", FORECASTS_ID);
	}

}
