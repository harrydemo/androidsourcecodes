package com.androidmediaplayer.utils;

import com.androidmediaplayer.R;

import android.content.Context;
import android.content.res.Resources;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseOpenHelper extends SQLiteOpenHelper {

	private static final String DBNAME = "QTMP01.db";
	private static final int version = 1;
	private Context context = null;

	public DataBaseOpenHelper(Context context) {
		super(context, DBNAME, null, version);
		this.context = context; 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    Resources rs = context.getResources();
	    String str = rs.getString(R.string.recentplay);
		try {
			db.beginTransaction();
			db.execSQL("CREATE TABLE PLAYLIST (PLAYLIST_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,PLAYLIST_NAME TEXT)");
			db.execSQL("CREATE TABLE PLAYLISTMAP (MAP_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, MP3_ID INTEGER NOT NULL, PLAYLIST_ID INTEGER NOT NULL, FOREIGN KEY (PLAYLIST_ID) REFERENCES PLAYLIST (PLAYLIST_ID) ON DELETE CASCADE)");
			db.execSQL("INSERT INTO PLAYLIST(PLAYLIST_ID,PLAYLIST_NAME) VALUES(?,?)",new Object[]{0,str});
			db.execSQL("CREATE TRIGGER MAP_DELETE BEFORE DELETE ON PLAYLIST FOR EACH ROW BEGIN DELETE FROM PLAYLISTMAP WHERE PLAYLIST_ID=OLD.PLAYLIST_ID; END;");
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
//			Date date = new Date();
//			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//			String dateStr = df.format(date);
			db.beginTransaction();
			
			db.execSQL("ALTER TABLE PLAYLISTMAP RENAME TO PLAYLISTMAP_old_" + version);
			db.execSQL("ALTER TABLE PLAYLIST RENAME TO PLAYLIST_old_" + version);
			
			db.execSQL("CREATE TABLE PLAYLIST (PLAYLIST_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,PLAYLIST_NAME TEXT)");
			db.execSQL("CREATE TABLE PLAYLISTMAP (MAP_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, MP3_ID INTEGER NOT NULL, PLAYLIST_ID INTEGER NOT NULL, FOREIGN KEY (PLAYLIST_ID) REFERENCES PLAYLIST (PLAYLIST_ID) ON DELETE CASCADE)");
			
			db.execSQL("INSERT INTO PLAYLIST (PLAYLIST_ID, PLAYLIST_NAME) SELECT PLAYLIST_ID, PLAYLIST_NAME FROM PLAYLIST_old_" + version);
			db.execSQL("INSERT INTO PLAYLISTMAP (MAP_ID, MP3_ID, PLAYLIST_ID) SELECT MAP_ID, MP3_ID, PLAYLIST_ID FROM PLAYLISTMAP_old_" + version);
			
			db.execSQL("DROP TABLE IF EXISTS PLAYLISTMAP_old_" + version);
			db.execSQL("DROP TABLE IF EXISTS PLAYLIST_old_" + version);
			
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

}
