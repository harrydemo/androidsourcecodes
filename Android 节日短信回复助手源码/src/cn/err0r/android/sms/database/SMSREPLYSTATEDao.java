package cn.err0r.android.sms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SMSREPLYSTATEDao extends DBHelper {

	public SMSREPLYSTATEDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public long insert(SMSREPLYSTATEModel smsreplystate){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Rid", smsreplystate.get_rid());
		cv.put("State", smsreplystate.get_state());
		cv.put("Body", smsreplystate.get_body());
		cv.put("Date", smsreplystate.get_date());
		long sum = db.insert("smsreplystate", null, cv);
		db.close();
		return sum;
		
	}
}
