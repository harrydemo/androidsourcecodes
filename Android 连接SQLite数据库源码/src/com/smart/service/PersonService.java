package com.smart.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smart.domain.Person;

public class PersonService {
	/**
	 * �����Ǵ���ҵ��ķ�������JAVA����ʵ����
	 * ����������Ĵ����ݿ⣬һ��Ҫ�ر����ݿ�
	 * */
	private DataBaseOpenHelper dbOpenHelper;

	public PersonService(Context context) {

		dbOpenHelper = new DataBaseOpenHelper(context);
	}

	public void save(Person person) {
		SQLiteDatabase database=dbOpenHelper.getWritableDatabase();
		database.execSQL("insert into person(name,age) values(?,?)",new Object[]{person.getName(),person.getAge()});
	
//	database.close();
	}

	public void update(Person person) {
		SQLiteDatabase database=dbOpenHelper.getWritableDatabase();
		database.execSQL("update person set name=?,age=? where personid=?", 
				new Object[]{person.getName(),person.getAge(),person.getPersonid()});
	
	
	
	
	
	
	
	
	
	
	}
	
	
	public Person find(Integer id){
		SQLiteDatabase database=dbOpenHelper.getWritableDatabase();
		Cursor cursor=	database.rawQuery("select * from person where personid=?", new String[]{String.valueOf(id)});
		if(cursor.moveToNext()){
		return new Person(cursor.getInt(0),cursor.getString(1),cursor.getShort(2));
			
			
			
		}
	
	
		return null;
	}

	public void delete(Integer... ids) {
		
		if(ids.length>0){
			StringBuilder sb=new StringBuilder();
			for(Integer id:ids){
				sb.append("?").append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			SQLiteDatabase database=dbOpenHelper.getWritableDatabase();
			database.execSQL("delete from person where personid in("+sb+")", (Object[])ids);
			
		}
		
	
			
	
	
	}
//��ҳ����
	public List<Person> getScrollData(int startResult, int maxResult) {
		 List<Person> persons=new ArrayList<Person>();
		SQLiteDatabase database=dbOpenHelper.getWritableDatabase();
		Cursor cursor=	database.rawQuery("select * from person limit ?,?",
				new String[]{String.valueOf(startResult),String.valueOf(maxResult)});
		while(cursor.moveToNext()){
			persons.add( new Person(cursor.getInt(0),cursor.getString(1),
				cursor.getShort(2)));
		}
			
		
		return persons;

	}
//ȡ�����е�����
	public long getCount() {
		SQLiteDatabase database=dbOpenHelper.getWritableDatabase();
		Cursor cursor=	database.rawQuery("select * from person ", null);
		if(cursor.moveToNext()){
		return cursor.getLong(0);
		}
		return 0;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
