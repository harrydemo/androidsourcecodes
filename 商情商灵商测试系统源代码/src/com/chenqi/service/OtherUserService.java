package com.chenqi.service;

import java.util.ArrayList;
import java.util.List;

import com.chenqi.domain.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class OtherUserService {
	private DBOpenHelper dbOpenHelper;
	
	public OtherUserService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	public void save(User user){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", user.getUsername());
		values.put("pwd", user.getPwd());
		values.put("sex", user.getSex());
		values.put("age", user.getAge());
		values.put("telephone", user.getTelephone());
		db.insert("userinfo", null, values);
	}
	
	public void update(User user){
		
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", user.getUsername());
		values.put("pwd", user.getPwd());
		values.put("sex", user.getSex());
		values.put("age", user.getAge());
		values.put("telephone", user.getTelephone());
		db.update("userinfo", values, "userid=?", new String[]{user.getUserid().toString()});
	}
	
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("userinfo", "userid=?", new String[]{id.toString()});
	}
	
	public User find(Integer id){
		//如果只对数据进行读取，建议使用此方法
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("userinfo", new String[]{"userid", "username","pwd","sex","age","telephone"},
				"userid=?", new String[]{id.toString()}, null, null, null);
		
		if(cursor.moveToFirst()){
			String username = cursor.getString(cursor.getColumnIndex("username"));
			String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
			String sex = cursor.getString(cursor.getColumnIndex("sex"));
			int age = cursor.getInt(cursor.getColumnIndex("age"));
			String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
			return new User(username,pwd,sex,age,telephone);
		}
		return null;
	}
	
	public Boolean checkUser(String username ,String pwd){
		//如果只对数据进行读取，建议使用此方法
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("userinfo", new String[]{"userid"},
				"username=? and pwd=?", new String[]{username.trim(),pwd.trim()}, null, null, null);
		
		if(cursor.moveToFirst()){
			
			return true;
		}
		return false;
	}
	
	public List<User> getScrollData(Integer offset, Integer maxResult){
		List<User> users = new ArrayList<User>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("userinfo", null, null, null, null, null, null, offset+","+ maxResult);
		while(cursor.moveToNext()){
			String username = cursor.getString(cursor.getColumnIndex("username"));
			String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
			String sex = cursor.getString(cursor.getColumnIndex("sex"));
			int age = cursor.getInt(cursor.getColumnIndex("age"));
			String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
			User user = new User(username,pwd,sex,age,telephone);
			users.add(user);
		}
		cursor.close();
		return users;
	}

	public long getCount() {// select count(*) from user
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("userinfo", new String[]{"count(*)"}, null, null, null, null, null);
		cursor.moveToFirst();
		return cursor.getLong(0);
	}
}
