package com.lfp.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.lfp.busactivity.Database;

import com.lfp.domain.Bus;



public class BusService {
	private Database database;
	private Context context;
	
	public BusService(Context context){
		this.context = context;
		database = new Database(context);
	}
	

	//save data
	public void save(Bus bus){
		SQLiteDatabase db = database.getWritableDatabase();
		db.execSQL("INSERT INTO `bus_line` (`id`, `line`, `station`) VALUES (NULL, '2·', '���·����*ɳ̲·����*��������ҽԺ*������*�찲�Ŷ�*�찲�Ź㳡��*��դ��*����*��������*ɳ�ӿ�*ľ��԰�ű�*������')");
	}
	//select
	public Bus find(String lines){
		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = db.rawQuery("select id,line,station from bus_line where line like '%"+lines+"%'", null);
		if(cursor.moveToNext()){//������¼��
			Bus bus = new Bus();
			bus.setId(cursor.getInt(cursor.getColumnIndex("id")));
			bus.setLine(cursor.getString(1));
			bus.setStation(cursor.getString(2));
			return bus;
		}
		cursor.close();
		return null;
	}
	

}
