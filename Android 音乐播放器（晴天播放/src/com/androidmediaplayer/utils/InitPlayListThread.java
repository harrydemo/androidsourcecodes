package com.androidmediaplayer.utils;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.androidmediaplayer.model.Mp3Info;

public class InitPlayListThread implements Runnable{
	
	private Context context = null;
	
	public InitPlayListThread(Context context){
		this.context = context;
	}

	@Override
	public void run() {
	    if(LinkedListPlayList.playListId != -1){
	        if(!LinkedListPlayList.getPlayListMp3Infos().isEmpty()){
	            LinkedListPlayList.clear();
	        }
	        PlayListMapUtil playListUtilMap = new PlayListMapUtil(context);
	        ArrayList<Integer> mp3Ids = playListUtilMap.findMp3IDsByPlayListId(LinkedListPlayList.playListId);
	        if(mp3Ids.size() > 0){
	            StringBuilder sb = new StringBuilder();
	            int size = mp3Ids.size();
	            for(int i=0; i<size; i++){
	                sb.append(mp3Ids.get(i)).append(',');
	            }
	            sb.deleteCharAt(sb.length()-1);
	            ArrayList<Mp3Info> mp3Infos = getMp3Infos("_id in(" + sb.toString() + ")", null, null);
	            if(null != mp3Infos && mp3Infos.size() > 0){
	                LinkedListPlayList.addAll(mp3Infos);
	            }
	        }
	    }
	}
	
	/**
	 * 查询播放列表音乐
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return
	 */
	private ArrayList<Mp3Info> getMp3Infos(String selection, String[] selectionArgs,
			String sortOrder) {
		ArrayList<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		Cursor c = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DATA,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media._ID}, selection,
				selectionArgs, sortOrder);
		int length = 0;
		length = c.getCount();
		if (null != c && 0 != length) {
			c.moveToFirst();
			Mp3Info mp3info = null;
			for (int i = 0; i < length; i++) {
				mp3info = new Mp3Info();
				mp3info.setMp3Name(c.getString(0));
				mp3info.setPath(c.getString(1));
				mp3info.setArtist(c.getString(2));
				mp3info.setDuration(c.getString(3));
				mp3info.setAudio_id(String.valueOf(c.getInt(4)));
				mp3Infos.add(mp3info);
				c.moveToNext();
			}
			return mp3Infos;
		}else{
			return null;
		}
	}

}
