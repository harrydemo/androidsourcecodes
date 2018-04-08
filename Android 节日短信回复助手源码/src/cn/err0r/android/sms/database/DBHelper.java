package cn.err0r.android.sms.database;

import cn.err0r.android.sms.R;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "smsex.db";
	private final static int DataBase_Version = 1;
	Context ct;
	
	public DBHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context,DATABASE_NAME,null,DataBase_Version);
		ct=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String SmsInfosqlStr = "Create table smsinfo ("+
									"SID INTEGER PRIMARY KEY AUTOINCREMENT,"+
									"PN text,"+
									"Who text,"+
									"Body text,"+
									"GetDate text"+
									");";
		db.execSQL(SmsInfosqlStr);
		String SmsReplyStatesqlStr = "Create table smsreplystate ("+
									"RID INTEGER PRIMARY KEY AUTOINCREMENT,"+
									"SID interger,"+
									"State interger,"+
									"Body text,"+
									"GetDate text"+
									");";
		db.execSQL(SmsReplyStatesqlStr);
		String Smssample = "Create table smssample ("+
						"SID INTEGER PRIMARY KEY AUTOINCREMENT,"+
						"Class  text,"+
						"Body text,"+
						"State interger"+
						");";
		db.execSQL(Smssample);
		
		String[] sample=ct.getResources().getStringArray(R.array.sample);
		for(int n=0;n<sample.length;n++)
			db.execSQL("Insert into smssample (Class, Body, State) values("+
						"'Spring Festval','"+
						sample[n]+
						"','0')");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}