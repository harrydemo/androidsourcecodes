package com.androidmediaplayer.utils;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PlayListMapUtil {

	private DataBaseOpenHelper dataBaseOpenHelper;

	public PlayListMapUtil(Context context) {
		dataBaseOpenHelper = new DataBaseOpenHelper(context);
	}
	
	public void save(Integer mp3_id, Integer playList_id){
		SQLiteDatabase dataBase = dataBaseOpenHelper.getWritableDatabase();
		dataBase.execSQL("INSERT INTO PLAYLISTMAP (MP3_ID,PLAYLIST_ID) VALUES(?,?)",new Object[]{mp3_id, playList_id});
		dataBase.close();
	}
	
	public ArrayList<Integer> findMp3IDsByPlayListId(Integer playList_id){
		SQLiteDatabase dataBase = dataBaseOpenHelper.getReadableDatabase();
		Cursor cursor = dataBase.rawQuery("SELECT MP3_ID FROM PLAYLISTMAP WHERE PLAYLIST_ID=?", new String[]{String.valueOf(playList_id)});
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		while(cursor.moveToNext()){
			arrayList.add(cursor.getInt(0));
		}
		cursor.close();
		dataBase.close();
		return arrayList;
	}
	
	public void deleteByAudioId(Integer audioId){
		SQLiteDatabase dataBase = dataBaseOpenHelper.getReadableDatabase();
		dataBase.execSQL("DELETE FROM PLAYLISTMAP WHERE MP3_ID=?",new Object[]{audioId});
		dataBase.close();
	}
	
	public void delete(Integer mp3_id, Integer playList_id) {
		SQLiteDatabase dataBase = dataBaseOpenHelper.getReadableDatabase();
		dataBase.execSQL("DELETE FROM PLAYLISTMAP WHERE MP3_ID=? AND PLAYLIST_ID=?",new Object[]{mp3_id, playList_id});
		dataBase.close();
	}
	
}
