package com.hrw.android.player.db;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hrw.android.player.builder.ContentValuesBuilder;
import com.hrw.android.player.domain.Playlist;

public final class DataBaseHelper extends SQLiteOpenHelper {
	public static final String PLAYLIST_TABLE_NAME = "hrw_playlist";
	public static final String AUDIO_LIST_TABLE_NAME = "hrw_playlist_audio";
	public static final String DATABASE_NAME = "AndroidPlayer.db";

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 12);
	}

	public final void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		paramSQLiteDatabase
				.execSQL("CREATE TABLE IF NOT EXISTS hrw_playlist (id INTEGER PRIMARY KEY,name TEXT CHECK( name != '' ),add_date INTEGER,modified_date INTEGER);");
		Log.i("Android Player:create database table:", "hrw_playlist");
		paramSQLiteDatabase
				.execSQL("CREATE TABLE IF NOT EXISTS hrw_playlist_audio (id INTEGER PRIMARY KEY,playlist_id INTEGER NOT NULL,audio_name TEXT,audio_path,add_date INTEGER,modified_date INTEGER);");
		Log.i("Android Player:create database table:", "hrw_playlist_audio");
		Playlist pl = new Playlist();
		Date date = new Date();
		pl.setAddDate(date);
		pl.setUpdateDate(date);
		pl.setName("我的最爱");
		try {
			ContentValues cv = ContentValuesBuilder.getInstance().bulid(pl);
			paramSQLiteDatabase.insert("hrw_playlist", null, cv);
		} catch (IllegalArgumentException e) {
			Log.e("DataBaseHelper", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("DataBaseHelper", e.getMessage());
		}

	}

	public final void onUpgrade(SQLiteDatabase paramSQLiteDatabase,
			int paramInt1, int paramInt2) {
		paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS hrw_playlist");
		Log.i("Android Player:drop database table:", "hrw_playlist");
		paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS hrw_playlist_audio");
		Log.i("Android Player:drop database table:", "hrw_playlist_audio");
		onCreate(paramSQLiteDatabase);
	}
}
