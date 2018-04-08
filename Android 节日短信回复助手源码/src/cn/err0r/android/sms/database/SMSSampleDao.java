package cn.err0r.android.sms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SMSSampleDao  extends DBHelper {

	public SMSSampleDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public long insert(SMSSampleModel smssample){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Class", smssample.get_class());
		cv.put("Body", smssample.get_body());
		cv.put("State", smssample.get_state());
		long sum = db.insert("smssample", null, cv);
		db.close();
		return sum;
	}
	
	public long updatafastreplyState(Integer id, boolean state, String body){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		if(state)
			cv.put("State", 1);
		else
			cv.put("State", 0);
		cv.put("Body", body);
//		Log.i("database",state+"");
		long sum =db.update("smssample", cv, "sid=?", new String[]{id.toString()});
		db.close();
		return sum;
		
	}
	
	public long updatafastreplyState(Integer id, boolean state,SMSSampleModel smssample){
		delete(id);
		return insert(smssample);
	}
	
	public long updatafastreplyState(Integer id){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor=db.rawQuery("select State from smssample where Sid="+id, null);
		cursor.moveToNext();
		ContentValues cv = new ContentValues();
		if(cursor.getInt(0)==1)
			cv.put("State",0 );
			else cv.put("State", 1);
		Log.i("database",cv.get("State").toString());
		long sum =db.update("smssample", cv, "sid=?", new String[]{id.toString()});
		db.close();
		cursor.close();
		return sum;
	}
	
	public long delete(Integer id){
		SQLiteDatabase db = this.getWritableDatabase();
		long sum = db.delete("smssample", "sid=?",new String[]{id.toString()});
		db.close();
		return sum;
	}
	
	public Cursor select(){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("select * from smssample order by sid desc", null);
	}
	
	public Cursor select(String where){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("select * from smssample where 1=1 and "+where+"", null);
	}
	
	public Cursor selectSpring(){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("select * from smssample where Class='Spring' ", null);
	}
	
	public Cursor selectBirthday(){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("select * from smssample where Class='Birthday' ", null);
	}
	
	public Cursor selectUser(){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("select * from smssample where Class='User' ", null);
	}
	
	public Cursor selectfastreplyState(){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("select * from smssample where State=1 ", null);
	}
	

}
