package com.feicong.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.feicong.qqmsglook.QQFriend;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 此类查询QQ相关信息
 * @author Administrator
 *
 */
public class QQFriendService implements IQQService {
	private DBHelper db;
	public QQFriendService(DBHelper db) {
		this.db = db;
		Log.i("QQFriendService", "QQFriendService Create...");
	}
	/**
	 * 查找好友
	 * @param qqUin
	 * @return
	 */
	public QQFriend find(String qqUin) {
		try
		{
			SQLiteDatabase data = db.getReadableDatabase();
			StringBuilder sb = new StringBuilder(
					"select group_name,name from Friends,Groups where Friends.groupid=Groups.group_id and uin=");
			sb.append(qqUin);
			Log.i("SQL", sb.toString());
			Cursor cursor = data.rawQuery(sb.toString(), null);
			if (cursor.moveToNext())
			{
				return new QQFriend(cursor.getString(0), cursor.getString(1),
						qqUin);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
		
	public List<Object> queryList(int startIndex, int endIndex)
	{
		List<Object> friends = new ArrayList<Object>();
		try
		{
			SQLiteDatabase data = db.getReadableDatabase();
			StringBuilder sb = new StringBuilder(
					"select group_name, name, uin from Friends,Groups where Friends.groupid=Groups.group_id order by group_id");
			sb.append(" limit ");
			sb.append(String.valueOf(startIndex));
			sb.append(',');
			sb.append(String.valueOf(endIndex));
			Log.i("SQL", sb.toString());
			Cursor cursor = data.rawQuery(sb.toString(), null);
			while (cursor.moveToNext())
			{
				Log.i("SQL", cursor.getString(1));
				friends.add(new QQFriend(cursor.getString(0), cursor
						.getString(1), cursor.getString(2)));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return friends;
	}
	/**
	 * 查询好友数目
	 */
	public long queryCount()
	{
		SQLiteDatabase data = db.getReadableDatabase();
		Cursor cursor = data.rawQuery("select count(*) from Friends", null);
		if (cursor.moveToNext()) {
			return cursor.getLong(0);
		}
		return 0;
	}
	public ArrayList<HashMap<String, String>> setAdapterListData(int startIndex, int endIndex)
	{
		List<Object> objs;    	
    	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
		{
			objs = queryList(startIndex, endIndex);
			for (Object obj : objs)
			{
				HashMap<String, String> map = new HashMap<String, String>();
				QQFriend friend = (QQFriend) obj;
				map.put("memberLevel", friend.getMemberLevel());
				map.put("name", friend.getName());
				map.put("qquin", friend.getqqUin());
				list.add(map);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
}
