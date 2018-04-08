package com.hrw.android.player.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.hrw.android.player.db.constants.UriConstant;

public class DataBaseProvider extends ContentProvider {
	// public static public static final Uri PLAYLIST_CONTENT_URI = Uri
	// .parse("content://" + AUTHORITY + "/" + "hrw_playlist");
	// public static final Uri AUDIO_CONTENT_URI = Uri.parse("content://"
	// + AUTHORITY + "/" + "hrw_playlist_audio");
	private DataBaseHelper dataBaseHelper;
	// 通过UriMatcher匹配外部请求
	private static UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	// 匹配状态常量

	private static final int PLAYLIST = 1;

	private static final int AUDIO_LIST = 2;
	// 添加Uri

	static {
		URI_MATCHER.addURI(UriConstant.AUTHORITY, UriConstant.PLAYLIST_PATH,

		PLAYLIST);

		URI_MATCHER.addURI(UriConstant.AUTHORITY, UriConstant.AUDIO_LIST_PATH,

		AUDIO_LIST);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
		switch (URI_MATCHER.match(uri)) {
		case PLAYLIST:
			count = db.delete(DataBaseHelper.PLAYLIST_TABLE_NAME, selection,
					selectionArgs);
			//db.delete(DataBaseHelper.AUDIO_LIST_TABLE_NAME, "playlist_id = ?", selectionArgs);
			break;
		case AUDIO_LIST:
			count = db.delete(DataBaseHelper.AUDIO_LIST_TABLE_NAME, selection,
					selectionArgs);
			break;
		default:
			break;
		}
		//db.close();
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
		long count = 0;
		switch (URI_MATCHER.match(uri)) {
		case PLAYLIST: {
			try {
				count = db.insert(DataBaseHelper.PLAYLIST_TABLE_NAME, null,
						values);
				break;
			} catch (Exception ex) {
				Log.e("error", "insert table palylist:" + ex.getMessage());
			}
		}
		case AUDIO_LIST: {
			try {
				count = db.insert(DataBaseHelper.AUDIO_LIST_TABLE_NAME, null,
						values);
				break;
			} catch (Exception ex) {
				Log.e("error", "insert table audio list:" + ex.getMessage());
			}
		}
		}
		//db.close();
		if (count > 0)
			return uri;
		else
			return null;
	}

	@Override
	public boolean onCreate() {
		dataBaseHelper = new DataBaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
		Cursor cur = null;
		// 依次参数为：表明，查询字段，where语句,占位符替换，group by(分组)，having(分组条件),order by(排序)
		switch (URI_MATCHER.match(uri)) {
		case PLAYLIST:
			cur = db.query(DataBaseHelper.PLAYLIST_TABLE_NAME, projection,
					selection, selectionArgs, null, null, sortOrder);
			break;
		case AUDIO_LIST:
			cur = db.query(DataBaseHelper.AUDIO_LIST_TABLE_NAME, projection,
					selection, selectionArgs, null, null, sortOrder);
			break;
		default:
			break;
		}
		//db.close();
		return cur;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
