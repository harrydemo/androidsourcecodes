package com.androidmediaplayer.utils;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidmediaplayer.model.PlayList;

public class PlayListUtil {

	private DataBaseOpenHelper dataBaseOpenHelper;
	
	public PlayListUtil(Context context){
		dataBaseOpenHelper = new DataBaseOpenHelper(context);
	}
	
	public void save(PlayList playList){
		SQLiteDatabase dataBase = dataBaseOpenHelper.getWritableDatabase();
		dataBase.execSQL("INSERT INTO PLAYLIST(PLAYLIST_NAME) VALUES(?)",new Object[]{playList.getPlayList_name()});
		dataBase.close();
	}
	
	public void update(PlayList playList){
		SQLiteDatabase dataBase = dataBaseOpenHelper.getReadableDatabase();
		dataBase.execSQL("UPDATE PLAYLIST SET PLAYLIST_NAME=? WHERE PLAYLIST_ID=?",new Object[]{playList.getPlayList_name(),playList.getPlayList_id()});
		dataBase.close();
	}
	
	public ArrayList<PlayList> findAll(){
		SQLiteDatabase dataBase = dataBaseOpenHelper.getReadableDatabase();
		Cursor cursor = dataBase.rawQuery("SELECT * FROM PLAYLIST", null);
		if(null != cursor && 0 != cursor.getCount()){
			ArrayList<PlayList> arrayList = new ArrayList<PlayList>();
			PlayList playList = null;
			while(cursor.moveToNext()){
				playList = new PlayList();
				playList.setPlayList_id(cursor.getInt(0));
				playList.setPlayList_name(cursor.getString(1));
				arrayList.add(playList);
			}
			cursor.close();
			dataBase.close();
			return arrayList;
		}else{
			if(null != cursor){
				cursor.close();
			}
			dataBase.close();
			return null;
		}
		
	}
	
	public PlayList find(Integer playList_id){
		SQLiteDatabase dataBase = dataBaseOpenHelper.getReadableDatabase();
		Cursor cursor = dataBase.rawQuery("SELECT * FROM PLAYLIST WHERE PLAYLIST_ID=?", new String[]{String.valueOf(playList_id)});
		if(cursor.moveToFirst()){
			PlayList playList = new PlayList();
			playList.setPlayList_id(cursor.getInt(0));
			playList.setPlayList_name(cursor.getString(1));
			cursor.close();
			dataBase.close();
			return playList;
		}
		if(null != cursor){
			cursor.close();
		}
		dataBase.close();
		return null;
	}
	
	public void delete(Integer... ids){
		if(ids != null && ids.length > 0){
			StringBuilder sb = new StringBuilder();
			int length = ids.length;
			for(int i=0; i<length; i++){
				sb.append('?').append(',');
			}
			sb.deleteCharAt(sb.length()-1);
			SQLiteDatabase dataBase = dataBaseOpenHelper.getReadableDatabase();
			dataBase.execSQL("DELETE FROM PLAYLIST WHERE PLAYLIST_ID IN(" + sb + ")", ids);
			dataBase.close();
		}
	}
	
}
