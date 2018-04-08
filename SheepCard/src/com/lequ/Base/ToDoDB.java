package com.lequ.Base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoDB extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "blackjack_db";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "blackjack_table";
	private final static String MENU_TABLE_NAME ="blackjack_menu_table";
	public final static String FIELD_ID = "_id";
	public final static String FIELD_MAX_SCORE = "max_score";
	public final static String FIELD_MONEY = "my_money";
	public final static String FIELD_WIN_COUNT = "win_count";
	public final static String FIELD_LOSE_COUNT = "lose_count";
	public final static String MENU_FIELD_NAME = "menu_name";
	public final static String MENU_FIELD_COUNT = "menu_count";


	public ToDoDB(Context context){
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE "+TABLE_NAME+" ("+FIELD_ID
				+" integer primary key autoincrement, "
				+" "+FIELD_MAX_SCORE+" text not null,"
				+" "+FIELD_MONEY+" text not null,"
				+" "+FIELD_WIN_COUNT+" text not null,"
				+" "+FIELD_LOSE_COUNT+" text not null);";
		db.execSQL(sql);

		sql = "CREATE TABLE "+MENU_TABLE_NAME+" ("+FIELD_ID
				+" integer primary key autoincrement, "
				+" "+MENU_FIELD_NAME+" integer not null,"
				+" "+MENU_FIELD_COUNT+" integer not null);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS "+TABLE_NAME;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS "+MENU_TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	public Cursor select(){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.query(TABLE_NAME,null,null,null,null,null,null);
	}

	public Cursor selectMenu(){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.query(MENU_TABLE_NAME, null, " "+MENU_FIELD_COUNT +" > ?",
				new String[]{"0"}, null, null, null);
	}

	public Cursor selectMenuByName(int name){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.query(MENU_TABLE_NAME, null, " "+MENU_FIELD_NAME+" = ?", 
				new String[]{Integer.toString(name)}, null, null, null);
	}

	public long insert(long maxScore,long money,long winCount,long loseCount){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_MAX_SCORE, Long.toString(maxScore));
		values.put(FIELD_MONEY, Long.toString(money));
		values.put(FIELD_WIN_COUNT, Long.toString(winCount));
		values.put(FIELD_LOSE_COUNT, Long.toString(loseCount));
		return db.insert(TABLE_NAME, null, values);
	}

	public long insertMenu(int name,int count){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(MENU_FIELD_NAME, name);
		values.put(MENU_FIELD_COUNT, count);
		return db.insert(MENU_TABLE_NAME, null, values);
	}

	public void delete(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_ID+" = ?";
		String[] whereValue = {Integer.toString(id)};
		db.delete(TABLE_NAME, where, whereValue);
	}

	public void deleteMenu(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_ID+" = ?";
		String[] whereValue = {Integer.toString(id)};
		db.delete(MENU_TABLE_NAME, where, whereValue);
	}

	public void update(int id,long maxScore,long money,long winCount,long loseCount){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_ID+" = ?";
		String[] whereValue = {Integer.toString(id)};
		ContentValues values = new ContentValues();
		values.put(FIELD_MAX_SCORE, Long.toString(maxScore));
		values.put(FIELD_MONEY,Long.toString(money));
		values.put(FIELD_WIN_COUNT,Long.toString(winCount));		
		values.put(FIELD_LOSE_COUNT,Long.toString(loseCount));
		if(values.size()!=0)
			db.update(TABLE_NAME, values, where, whereValue);
	}

	public void updateMenu(int id,int count){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = MENU_FIELD_NAME+" = ?";
		String[] whereVlaue = {Integer.toString(id)};
		ContentValues values = new ContentValues();
		values.put(MENU_FIELD_COUNT, count);
		db.update(MENU_TABLE_NAME, values, where, whereVlaue);
	}
}
