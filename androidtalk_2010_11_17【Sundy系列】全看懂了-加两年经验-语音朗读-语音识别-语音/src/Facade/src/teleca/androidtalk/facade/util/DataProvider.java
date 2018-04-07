package teleca.androidtalk.facade.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * @author Jacky Zhang
 * @version 0.1
 * It's a provider which operate database
 */
public class DataProvider extends ContentProvider {
	/*
	 * database name
	 */
	private static final String DATABASE_NAME = "AndroidTalk.db";
	/*
	 * database version
	 */
	private static final int DATABASE_VERSION = 2;
	/*
	 * table name
	 */
	private static final String TABLE_NAME = "Command";
	private DatabaseHelper mOpenHelper;
//	private static final String CMD_NAME = "cmd_name";
//	private static final String CMD_CATEGORY = "cmd_category";
//	private static final String RELATION = "relation";
	private static class DatabaseHelper extends SQLiteOpenHelper{
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "+TABLE_NAME+"('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'cmd_name' TEXT NOT NULL, 'cmd_category' TEXT NOT NULL, 'relation' TEXT NOT NULL);");
/*			String sql_1 = "insert into "+TABLE_NAME+"(cmd_name, cmd_category, relation)values('start','call','myapp');";
			String sql_2 = "insert into "+TABLE_NAME+"(cmd_name, cmd_category, relation)values('visit','app','google');";
			String sql_3 = "insert into "+TABLE_NAME+"(cmd_name, cmd_category, relation)values('open','web','baidu.com');";
			try{
				db.execSQL(sql_1);
				db.execSQL(sql_2);
				db.execSQL(sql_3);
			}
			catch(SQLException e){
				Log.e("ERROR", e.toString());
			}*/
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
		
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int result = db.delete(TABLE_NAME, selection, selectionArgs);
		return result;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
/*		String field_1 = values.get("cmd_name").toString();
		String field_2 = values.get("cmd_category").toString();
		String field_3 = values.get("relation").toString();
		String sql_1 = "INSERT INTO " + TABLE_NAME + " (cmd_name, cmd_category, relation) values('" + field_1 + "', '" + field_2 + "', '" + field_3 +"');";
		try {
			db.execSQL(sql_1);
		}
		catch (SQLException e) {
			Log.e("ERROR", e.toString());
			return null;
		}*/
		db.insert(TABLE_NAME, null, values);
		return uri;
	}
	
	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
//		Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME, selectionArgs);
		Cursor c = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
/*		String field_1 = values.get("cmd_name").toString();
		String field_2 = values.get("cmd_category").toString();
		String field_3 = values.get("relation").toString();
		String sql_1 = "UPDATE " + TABLE_NAME + " SET cmd_name = '" + field_1 + "', cmd_category='" + field_2 + "', relation='" + field_3 +"' WHERE "+selection+";";
		try {
			db.execSQL(sql_1);
		}
		catch (SQLException e) {
			Log.e("ERROR", e.toString());
			e.printStackTrace();
			return -1;
		}*/
		int result = db.update(TABLE_NAME, values, selection, selectionArgs);
		return result;
	}

}
