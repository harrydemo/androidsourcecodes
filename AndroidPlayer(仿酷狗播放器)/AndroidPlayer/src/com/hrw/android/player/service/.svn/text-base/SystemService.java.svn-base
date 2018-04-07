package com.hrw.android.player.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hrw.android.player.BelmotPlayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

public class SystemService {

	private Context context;
	private Cursor cursor;

	public SystemService(Context context) {
		this.context = context;
	}

	public Cursor getAllSongs() {
		if (cursor != null)
			return cursor;
		ContentResolver resolver = context.getContentResolver();
		cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		return cursor;

	}

	public String getRealPathByAudioName(String name) {
		Log.i(BelmotPlayer.TAG + "SystemService",
				"getRealPathByAudioName name=" + name);
		ContentResolver resolver = context.getContentResolver();
		String[] proj = { MediaStore.Images.Media.DATA };
		String selection = MediaStore.Audio.Media.DISPLAY_NAME + " = ?";

		String[] selectionArgs = new String[] { name };

		Cursor cursor = resolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, selection,
				selectionArgs, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public List<String> getAudioPathList() {
		List<String> list = new ArrayList<String>();
		ContentResolver resolver = context.getContentResolver();
		String[] proj = { MediaStore.Images.Media.DATA };

		Cursor cursor = resolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null,
				null);

		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				/*
				 * java中\\表示一个\，而regex中\\也表示\，所以当\\\\解析成regex的时候为\\
				 */
				list.add(cursor.getString(column_index));
			}

		}
		return list;

	}

	public Set<String> getFolderContainMedia() {
		Set<String> f = new HashSet<String>();
		ContentResolver resolver = context.getContentResolver();
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = resolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null,
				null);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				/*
				 * java中\\表示一个\，而regex中\\也表示\，所以当\\\\解析成regex的时候为\\
				 */
				f.add(cursor.getString(column_index).substring(0,
						cursor.getString(column_index).lastIndexOf("/") + 1));
			}
		}
		return f;
	}

	public Set<String> getMediasByFolder(String path) {
		Set<String> f = new HashSet<String>();
		ContentResolver resolver = context.getContentResolver();
		String[] proj = { MediaStore.Images.Media.DATA };
		// String
		// selection=MediaStore.Audio.Media.DATA+" like '/mnt/sdcard/Recording/%'";
		String selection = MediaStore.Audio.Media.DATA + " like ?";
		String[] selectionArgs = { path + "%" };
		// String selection = MediaStore.Audio.Media.DATA + " like " +"'"
		// +path+"%'";
		Cursor cursor = resolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, selection,
				selectionArgs, null);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				/*
				 * java中\\表示一个\，而regex中\\也表示\，所以当\\\\解析成regex的时候为\\
				 */
				f.add(cursor.getString(column_index));
			}
		}
		return f;
	}
}
