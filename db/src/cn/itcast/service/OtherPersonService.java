package cn.itcast.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.itcast.domain.Person;

public class OtherPersonService {
	private DBOpenHelper dbOpenHelper;
	
	public OtherPersonService(Context context){
		dbOpenHelper = new DBOpenHelper(context);
	}
	
	public void save(Person person){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", person.getName());
		values.put("phone", person.getPhone());
		values.put("amount", person.getAmount());
		db.insert("person", null, values); // insert into person(personid) values(NULL)
		//db.close();
	}
	
	public void update(Person person){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", person.getName());
		values.put("phone", person.getPhone());
		values.put("amount", person.getAmount());
		db.update("person", values, "personid=?", new String[]{person.getId().toString()});
	}
	
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("person", "personid=?", new String[]{id.toString()});
	}
	
	public Person find(Integer id){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("person", new String[]{"personid","name","phone"}, 
				"personid=?", new String[]{id.toString()}, null, null, null);
		if(cursor.moveToFirst()){
			int personid = cursor.getInt(cursor.getColumnIndex("personid"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			int amount = cursor.getInt(cursor.getColumnIndex("amount"));
			cursor.close();
			return new Person(personid, name, phone, amount);
		}
		return null;
	}
	
	public List<Person> getScrollData(int offset, int maxResult){
		List<Person> persons = new ArrayList<Person>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("person", null, null, null, null, null, null, offset+ ","+ maxResult);
		while(cursor.moveToNext()){
			int personid = cursor.getInt(cursor.getColumnIndex("personid"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			int amount = cursor.getInt(cursor.getColumnIndex("amount"));
			persons.add(new Person(personid, name, phone, amount));
		}
		return persons;
	}
	
	public long getCount(){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("person", new String[]{"count(*)"} , null, null, null, null, null);
		cursor.moveToFirst();
		return cursor.getLong(0);
	}
}
