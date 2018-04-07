package com.hrw.android.player.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.hrw.android.player.dao.AudioDao;
import com.hrw.android.player.db.constants.UriConstant;
import com.hrw.android.player.domain.Audio;

public class AudioDaoImpl extends ContextWrapper implements AudioDao {
	private Uri uri = Uri.parse(UriConstant.AUDIO_LIST_URI);
	private ContentResolver cr;

	public AudioDaoImpl(Context base) {
		super(base);
	}

	@Override
	public String getMusicPathByName(String name) {
		String path = null;
		ContentResolver cr = getContentResolver();
		Uri uri = Uri.parse(UriConstant.AUDIO_LIST_URI);
		String[] projection = { "audio_path" };
		String selection = "audio_name = ?";
		String[] selectionArgs = { name };
		Cursor c = cr.query(uri, projection, selection, selectionArgs, null);
		if (c.moveToFirst()) {
			path = c.getString(0);
		}
		return path;
	}

	@Override
	public List<String> getMusicPathListByName(String name) {
		List<String> musicList = new ArrayList<String>();
		ContentResolver cr = getContentResolver();
		Uri uri = Uri.parse(UriConstant.AUDIO_LIST_URI);
		String[] projection = { "audio_path" };
		String selection = "audio_name like ?";
		String[] selectionArgs = { "%" + name + "%" };
		Cursor c = cr.query(uri, projection, selection, selectionArgs, null);
		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);
				musicList.add(c.getString(0));
			}
		}
		return musicList;
	}

	@Override
	public List<Audio> getLocalAudioListByName(String name) {
		List<Audio> musicList = new ArrayList<Audio>();
		ContentResolver resolver = getContentResolver();
		String[] projection = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DATA };
		String selection = MediaStore.Audio.Media.DATA + " like ?";
		String[] selectionArgs = { "%" + name + "%" };
		Cursor cursor = resolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
				selection, selectionArgs,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				Audio audio = new Audio();
				audio.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
				audio.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
				audio.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
				musicList.add(audio);
			}
		}
		cursor.close();
		return musicList;
	}

	@Override
	public List<String> getMusicListByPId(String id) {
		List<String> musicList = new ArrayList<String>();
		ContentResolver cr = getContentResolver();
		Uri uri = Uri.parse(UriConstant.AUDIO_LIST_URI);
		String[] projection = { "audio_name" };
		String selection = "playlist_id = ?";
		String[] selectionArgs = { id };
		Cursor c = cr.query(uri, projection, selection, selectionArgs, null);
		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);
				musicList.add(c.getString(0));
			}
		}
		c.close();
		return musicList;

	}

	@Override
	public void addMediaToPlaylist(ContentValues values) {
		Uri uri = Uri.parse(UriConstant.AUDIO_LIST_URI);
		ContentResolver cr = getContentResolver();
		cr.insert(uri, values);
	}

	@Override
	public void removeAudioFromPlaylist(String audioId, String playlistId) {
		ContentResolver cr = getContentResolver();
		cr.delete(uri, "id = ? and playlist_id = ?", new String[] {
				audioId, playlistId });
	}

	@Override
	public List<String> getLocalAudioPathList() {
		List<String> musicList = new ArrayList<String>();
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				musicList.add(cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
			}
		}
		cursor.close();
		return musicList;
	}

	@Override
	public List<Audio> getAudioListByPlaylistId(String playlistId) {
		cr = getContentResolver();
		List<Audio> audioList = new ArrayList<Audio>();
		String[] projection = { "id", "audio_path","audio_name","playlist_id" };
		String selection = "playlist_id = ?";
		Cursor c = cr.query(uri, projection, selection,
				new String[] { playlistId }, null);
		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				Audio audio = new Audio();
				c.moveToPosition(i);
				audio.setId(c.getLong(0));
				audio.setPath(c.getString(1));
				audio.setName(c.getString(2));
				audio.setPlaylistId(c.getString(3));
				audioList.add(audio);
			}
		}
		c.close();
		return audioList;
	}

	@Override
	public List<Audio> getLocalAudioList() {
		List<Audio> musicList = new ArrayList<Audio>();
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				Audio audio = new Audio();
				audio.setId(cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
				audio
						.setName(cursor
								.getString(cursor
										.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
				audio.setPath(cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
				musicList.add(audio);
			}
		}
		cursor.close();
		return musicList;
	}
}
