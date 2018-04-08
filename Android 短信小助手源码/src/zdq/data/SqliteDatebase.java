package zdq.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqliteDatebase {
	public static final String DB_NAME = "blackpn.db";
	public static final String TB_NAME = "blackpns";
	private SQLiteDatabase db;
	
	public SqliteDatebase(Context context){
		try {
			db = context.openOrCreateDatabase(DB_NAME,
					Context.MODE_PRIVATE, null);
			db.execSQL("CREATE TABLE IF NOT EXISTS " +
					TB_NAME + "(" +
					PnBean.ID + " integer primary key," +
					PnBean.PN + " varchar" + 
					")");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("打开或者创建DB异常:" + ex.getMessage());
		}
	}
	
	public void OpenOrCreateTable() {
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				TB_NAME + "(" +
				PnBean.ID + " integer primary key," +
				PnBean.PN + " varchar" + 
				")");
	}


	/**
	 * 变更列名
	 * @param db
	 * @param oldColumn
	 * @param newColumn
	 * @param typeColumn
	 */
	public void updateColumn(String oldColumn, 
			String newColumn, String typeColumn){
		try{
			db.execSQL("ALTER TABLE " +
					TB_NAME + " CHANGE " +
					oldColumn + " "+ newColumn +
					" " + typeColumn
			);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询黑名单
	 * @return
	 */
	public Cursor getpn(){
		String sql = null;
		Cursor cursor=null;
		
		cursor = db.query(true, TB_NAME, 
				new String[]{PnBean.ID, PnBean.PN}, 
				sql, 
				null, null, null, null, null);
		return cursor;
	}
	
	/**
	 * 查询黑名单
	 * @return
	 */
	public Cursor getpn(String pn){
		String sql=PnBean.PN + " like '%" + pn;
		Cursor cursor=null;
		
		cursor = db.query(true, TB_NAME, 
				new String[]{PnBean.ID, PnBean.PN}, 
				sql, 
				null, null, null, null, null);
		return cursor;
	}
	
	
	
	
	
	
	
	/**
	 * 删除指定黑名单
	 * 
	 */
	public boolean delpn(int id){
		try{
			db.delete(TB_NAME, PnBean.ID + "=" + id, null);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 插入指定黑名单
	 * 
	 */
	public Long insertpn(String pn){
		ContentValues values = new ContentValues();
		values.put(PnBean.PN, pn);
		return db.insertOrThrow(TB_NAME, PnBean.ID, values);
		
	}
}