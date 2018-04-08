package com.feicong.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.feicong.qqmsglook.QQMsg;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class QQMessageService implements IQQService{
	private DBHelper db;
	private String uin;
	private Boolean bQQ;
	
	public QQMessageService(DBHelper db, String uin, Boolean bQQ){
		this.db = db;
		this.uin = uin;
		this.bQQ = bQQ;
		Log.i("QQMessageService", "QQMessageService Create...");
	}
	
	public ArrayList<HashMap<String, String>> setAdapterListData(int startIndex, int endIndex){
    	List<Object> msgs;    	
    	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        msgs = queryList(startIndex, endIndex);
        for (Object obj : msgs){
        	HashMap<String, String> map = new HashMap<String, String>();
        	QQMsg msg = (QQMsg)obj;
        	map.put("uin", msg.getUin());
        	map.put("msg", msg.getMessage());
        	list.add(map);
        }
        return list;
    }
	
	/**
	 * 查询好友或群消息
	 */
	public List<Object> queryList(int startIndex, int endIndex)
	{
		List<Object> messages = new ArrayList<Object>();
		SQLiteDatabase data = db.getReadableDatabase();
		StringBuilder sb = new StringBuilder("select senderuin,msg from mr_");
		if(bQQ){
			sb.append("friend_");
		}
		else{
			sb.append("troop_");
		}
		sb.append(uin);
		sb.append(" limit ");
		sb.append(String.valueOf(startIndex));
		sb.append(',');
		sb.append(String.valueOf(endIndex));
		Log.i("SQL", sb.toString());
		try
		{
			Cursor cursor = data.rawQuery(sb.toString(), null);
			while (cursor.moveToNext())
			{
				messages.add(new QQMsg(cursor.getString(0), cursor.getString(1)));
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return messages;
	}
	/**
	 * 查询消息数目
	 */
	public long queryCount()
	{
		SQLiteDatabase data = db.getReadableDatabase();
		StringBuilder sb = new StringBuilder("select count(*) from mr_");
		if(bQQ){
			sb.append("friend_");
		}
		else{
			sb.append("troop_");
		}
		sb.append(uin);
		Cursor cursor = data.rawQuery(sb.toString(), null);
		if (cursor.moveToNext()) {
			return cursor.getLong(0);
		}
		return 0;
	}

}
