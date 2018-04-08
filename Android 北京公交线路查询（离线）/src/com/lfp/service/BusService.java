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
		db.execSQL("INSERT INTO `bus_line` (`id`, `line`, `station`) VALUES (NULL, '2路', '宽街路口南*沙滩路口南*北京妇产医院*东华门*天安门东*天安门广场西*大栅栏*天桥*永定门内*沙子口*木樨园桥北*海户屯')");
	}
	//select
	public Bus find(String lines){
		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = db.rawQuery("select id,line,station from bus_line where line like '%"+lines+"%'", null);
		if(cursor.moveToNext()){//迭代记录集
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
