package org.music.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelperTools {
	private MusicDownloadDB dbhelper;

	public DBHelperTools(Context context) {
		dbhelper = new MusicDownloadDB(context);
	}

	/**
	 * 查看数据库中是否有数据
	 */
	public boolean isHasInfors(String urlstr) {
		SQLiteDatabase database = dbhelper.getReadableDatabase();
		String sql = "select count(*)  from download_info where url=?";
		Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		return count == 0;
	}

	/**
	 * 保存 下载的具体信息
	 */
	public void saveInfos(List<DownloadMusicInfo> infos) {
		SQLiteDatabase database = dbhelper.getWritableDatabase();
		for (DownloadMusicInfo info : infos) {
			String sql = "insert into download_info(thread_id,start_pos, end_pos,compelete_size,url) values (?,?,?,?,?)";
			Object[] bindArgs = { info.getThreadId(), info.getStartPos(),
					info.getEndPos(), info.getCompeleteSize(), info.getUrl() };
			database.execSQL(sql, bindArgs);
		}
	}

	/**
	 * 得到下载具体信息
	 */
	public List<DownloadMusicInfo> getInfos(String urlstr) {
		List<DownloadMusicInfo> list = new ArrayList<DownloadMusicInfo>();
		SQLiteDatabase database = dbhelper.getReadableDatabase();
		String sql = "select thread_id, start_pos, end_pos,compelete_size,url from download_info where url=?";
		Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
		while (cursor.moveToNext()) {
			DownloadMusicInfo info = new DownloadMusicInfo(cursor.getInt(0),
					cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
					cursor.getString(4));
			list.add(info);
		}
		cursor.close();
		return list;
	}

	/**
	 * 更新数据库中的下载信息
	 */
	public void updataInfos(int threadId, int compeleteSize, String urlstr) {
		SQLiteDatabase database = dbhelper.getReadableDatabase();
		String sql = "update download_info set compelete_size=? where thread_id=? and url=?";
		Object[] bindArgs = { compeleteSize, threadId, urlstr };
		database.execSQL(sql, bindArgs);
	}

	/**
	 * 关闭数据库
	 */
	public void closeDb() {
		dbhelper.close();
	}

	/**
	 * 下载完成后删除数据库中的数据
	 */
	public void delete(String url) {
		SQLiteDatabase database = dbhelper.getReadableDatabase();
		database.delete("download_info", "url=?", new String[] { url });
		database.close();
	}
}
