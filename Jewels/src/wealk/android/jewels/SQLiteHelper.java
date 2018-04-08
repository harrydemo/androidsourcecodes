package wealk.android.jewels;	
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库操作
 * @author Longxia
 * @time 2010-10-26
 * @modify Qingfeng 2010-10-26
 */
public class SQLiteHelper extends SQLiteOpenHelper{
	
	// ===========================================================
	// Constants
	// ===========================================================	
	private static final String TAG = "SQLiteHelper";
	
	private static final String DATABASE_NAME = "jewels.db";//数据库名
	private static final String TABLE_NAME = "score";//表名
    private static final int DATABASE_VERSION = 1;//版本号
	
	// ===========================================================
	// Fields
	// ===========================================================   
	private SQLiteDatabase mDB;
	
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	public SQLiteHelper(Context context) {			
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + TABLE_NAME + " ("
				 + "_id integer primary key autoincrement,"
				 + "_name varchar(50),"
				 + "_score integer,"
				 + "_model integer,"//0--表示普通模式和无限模式，1--表示记时模式
				 + "_rank integer)");
		
		Log.i(TAG, "onCreate"); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
		db.execSQL("drop table if exists " + TABLE_NAME);
		onCreate(db);
		Log.i(TAG, "onUpgrade");
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	/**
	 * 添加数据
	 */
	public void insertData(String name,int score,int model,int rank){
		mDB = getWritableDatabase();
		mDB.beginTransaction();//开始事务
		ContentValues values = new ContentValues();
		values.put("_name", name);
		values.put("_score", score);
		values.put("_model", model);
		values.put("_rank", rank);
		try{			
			mDB.insert(TABLE_NAME, "_id", values);	
			mDB.setTransactionSuccessful(); //执行到endTransaction()提交当前事务,如不调用此方法会回滚事务 .	
		}catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}finally{
			mDB.endTransaction();//由事务的标志决定是提交事务，还是回滚事务.
			mDB.close();
		}
	}
	
	/**
	 * 判断是否已经存在
	 * @param nameString
	 * @return
	 * @author Qingfeng
	 */
	public boolean isNameExist(String nameString){
		boolean flag = false;
		mDB = getReadableDatabase();	
		Cursor cursor = mDB.query(TABLE_NAME, null, "_name='"+nameString+"'", null, null, null, "_score desc","3");	//查询表中数据
		int nameIndex = cursor.getColumnIndex("_name");
		for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
			if(cursor.getString(nameIndex).equals("") ||cursor.getString(nameIndex) == null){
				flag = false;//不存在
			}else {
				flag = true;//已经存在
			}
		}
		cursor.close();	
		mDB.close();			
		return flag;
	}
	
	/**
	 * 查询数据
	 * @return
	 */
	public String queryData(){
		int i = 0;
		String result = "";
		mDB = getReadableDatabase();
		Cursor cursor = mDB.query(TABLE_NAME, null, null, null, null, null, "_score desc","3");
		int nameIndex = cursor.getColumnIndex("_name");	
		int scoreIndex = cursor.getColumnIndex("_score");
		for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
			i += 1;
			result = result + i + "          ";
			result = result + cursor.getString(nameIndex) + "         ";
			result = result + cursor.getInt(scoreIndex) + "           \n";	
		}
		cursor.close();	
		mDB.close();
		return result;
	}
	
	/**
	 * 查询插入数据的排行并且更新该排名之后的排名
	 * @param score
	 * @return
	 * @author Houzi
	 */
	public String queryrank(String score,int model){
		String rank = null;
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(TABLE_NAME, null, " _score >= "+score+" and _model="+model+"" , null, null, null, null, null);
		rank = String.valueOf(c.getCount());
		
		Cursor cc = db.query(TABLE_NAME, null, " _score < "+score+" and _model="+model+"" , null, null, null, null, null);
		if(cc.getCount() > 0){
			int mName = cc.getColumnIndex("_name");
			int mScore = cc.getColumnIndex("_score");
			int mModel = cc.getColumnIndex("_model");
			int mRank = cc.getColumnIndex("_rank");
			String aa ="";
			String bb ="";
			String ff = "";
			String ee ="";
			for(cc.moveToFirst();!(cc.isAfterLast());cc.moveToNext()){
				if(cc.getString(mName)!=null){
				aa=aa+","+ cc.getString(mName);
				bb =bb+ ","+String.valueOf(cc.getInt(mScore));
				ff = ff + ","+ String.valueOf(cc.getInt(mModel));
				ee =ee+","+String.valueOf(cc.getInt(mRank));
				}
			}
			String[] aaa=aa.split(",");
			String[] bbb=bb.split(",");
			String[] modelArray = ff.split(",");
			String[] ddd=ee.split(",");
			for(int i=0;i<aaa.length;i++){
				if(aaa[i]!=null && aaa[i]!="" && aaa[i].length()>0){
					ContentValues values=new ContentValues();
					values.put("_name", aaa[i]);
					values.put("_score", Integer.parseInt(bbb[i]));
					values.put("_model", Integer.parseInt(modelArray[i]));
					values.put("_rank", Integer.parseInt(ddd[i])+1);
					db.update(TABLE_NAME, values, "_score = '"+bbb[i]+"'", null);
				}
			}			
		}
		c.close();	
		db.close();
		return rank;
	}

	/**
	 * 根据不同的model 查询数据
	 * @param model 查询的类型
	 * @return 返回的一个Cursor集，按照分数的降序排列
	 */
	public Cursor getListViewCursorByModel(int model) {
		Cursor cursor = null;
		try {
			mDB = getWritableDatabase();
			cursor = mDB.query(TABLE_NAME, null, "_model="+model+"", null, null, null, "_score desc",null);
			for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
			}
		} catch (Exception e) {
		}finally{
			mDB.close();
		}		
		return cursor;
	}


}