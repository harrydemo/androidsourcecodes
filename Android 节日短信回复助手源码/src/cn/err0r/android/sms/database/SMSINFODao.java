package cn.err0r.android.sms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SMSINFODao extends DBHelper {

	public SMSINFODao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public long insert(SMSINFOModel smsinfo){
		SQLiteDatabase db = this.getWritableDatabase();
		deleteByPn(smsinfo.get_pn());
		ContentValues cv = new ContentValues();
		cv.put("PN", smsinfo.get_pn());
		if(smsinfo.get_who().equals(""))
			cv.put("Who", smsinfo.get_pn());
		else
			cv.put("Who", smsinfo.get_who());
		cv.put("Body", smsinfo.get_body());
		cv.put("GetDate", smsinfo.get_getdate());
		long sum = db.insert("smsinfo", null, cv);
		db.close();
		return sum;
	}
	
	public void deleteByPn(String pn){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from smsinfo where PN='"+pn+"'");
	}
	
	public Cursor select(){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("select PN,Who,Body from smsinfo order by sid desc", null);
	}
	
	public Cursor select(String strSql){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("select PN,Who,Body from smsinfo where 1=1 "+strSql+" order by sid desc", null);
	}
	
	
}
