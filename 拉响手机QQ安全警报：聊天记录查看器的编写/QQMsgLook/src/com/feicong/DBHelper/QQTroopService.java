package com.feicong.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.feicong.qqmsglook.QQTroup;

public class QQTroopService implements IQQService {
	private DBHelper db;
	public QQTroopService(DBHelper db) {
		this.db = db;
		Log.i("QQTroopService", "QQTroopService Create...");
	}
	
	public List<Object> queryList(int startIndex, int endIndex)
	{
		List<Object> troops = new ArrayList<Object>();
		try
		{
			SQLiteDatabase data = db.getReadableDatabase();
			StringBuilder sb = new StringBuilder("select troopname, troopuin, troopmemo from TroopInfo");
			sb.append(" limit ");
			sb.append(String.valueOf(startIndex));
			sb.append(',');
			sb.append(String.valueOf(endIndex));
			Log.i("SQL", sb.toString());
			Cursor cursor = data.rawQuery(sb.toString(), null);
			while (cursor.moveToNext())
			{
				Log.i("SQL", cursor.getString(0));
				troops.add(new QQTroup(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return troops;
	}
	/**
	 *查询群数目
	 */
	public long queryCount()
	{
		try
		{
			SQLiteDatabase data = db.getReadableDatabase();
			Cursor cursor = data.rawQuery("select count(*) from TroopInfo",
					null);
			if (cursor.moveToNext())
			{
				return cursor.getLong(0);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
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
				QQTroup troop = (QQTroup) obj;
				map.put("troopName", troop.getName());
				map.put("troopUin", troop.getUin());
				map.put("troopMemo", troop.getMemo());
				list.add(map);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

}
