package cn.com.karl.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Eric
 *
 */
public class SQLHelper extends SQLiteOpenHelper {
	
	public static final String TB_WORD="tb_word";
	public static final String ID="_id";
	public static final String NAME="name";
	public static final String AUDIO="audio";
	public static final String PRON="pron";	
	public static final String DEF="def";	
	public static final String XML="xml";

	
	public SQLHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuffer sbSQL=new StringBuffer();
		sbSQL.append("create table if not exists ");
		sbSQL.append(TB_WORD);
		sbSQL.append("(");
		sbSQL.append(ID+" integer primary key,");
		sbSQL.append(NAME+" varchar,");
		sbSQL.append(XML+" varchar");
		sbSQL.append(")");
		db.execSQL(sbSQL.toString());		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TB_WORD);
		onCreate(db);
	}

}
