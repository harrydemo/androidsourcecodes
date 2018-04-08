/*
 * 系统名称：
 * 模块名称：
 * 描述：
 * 作者：徐骏
 * version 1.0
 * time  2010-11-12 上午10:55:19
 * copyright Anymusic Ltd.
 */
package xujun.keepaccount.sql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import xujun.keepaccount.entity.Account;
import xujun.keepaccount.entity.AccountEnum;
import xujun.util.CalendarUtil;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * 使用方式:</br>
 * dbHelper = new DBHelper(this);</br>
 * dbHelper.open();</br>
 * 处理数据</br>
 * dbHelper.close()</br>
 * @author 徐骏
 * @data   2010-11-12
 */

public class DBHelper
{
	//sqlite支持NULL Integer real text blob类型,注意该类的所有数据库操作的异常都没有处理
	private Context context;
	private SQLiteDatabase dbInstance;
	private DBCreator dbCreator;
	private static final String DB_NAME = "db_keepaccount";
	private static final int DB_VERSION = 1;
	
	public static final String TABLE_NAME="myAccount";
	public static final String COLUMN_ID = "account_id";
	public static final String COLUMN_TYPE="account_type";
	public static final String COLUMN_MONEY="account_money";
	public static final String COLUMN_DATE = "account_date";
	
	private static final String CREATE_TABLE= new StringBuffer().append("Create table ").append(TABLE_NAME)
								.append(" (")
								.append(COLUMN_ID).append(" integer primary key,")
								.append(COLUMN_TYPE).append(" integer not null,")
								.append(COLUMN_MONEY).append(" real not null,")
								.append(COLUMN_DATE).append(" text not null)")
								.toString();
	public DBHelper(Context context)
	{
		this.context = context;
	}
	public void open()
	{
		//第一次，android系统发现数据库没有创建，会调用onCreate方法，以后就只是返回数据库的一个引用
		dbCreator = new DBCreator(context,DB_NAME,null,DB_VERSION,CREATE_TABLE,TABLE_NAME);
		dbInstance = dbCreator.getWritableDatabase();
	}
	public void close()
	{
		dbCreator.close();
	}
	public void insert(ContentValues values)
	{
		dbInstance.insert(TABLE_NAME, null, values);
	}
	public void update(ContentValues values,String whereClause,String[] whereArgs)
	{
		dbInstance.update(TABLE_NAME, values, whereClause, whereArgs);
	}
	public void delete(String whereClause, String[] whereArgs)
	{
		dbInstance.delete(TABLE_NAME, whereClause, whereArgs);
	}
	/**
	 * 查询
	 * @param sql SQL语句，参数用?占位
	 * @param selectionArgs 参数?的值数组
	 * @return Cursor游标，注意，和JDBC的ResultSet一样，是在线数据集，所以处理完之前，不能调用close()
	 */
	public Cursor query(String sql, String[] selectionArgs)
	{
		return dbInstance.rawQuery(sql, selectionArgs);
	}
	public ArrayList<Account> getQueryAccountList(String sql,String[] args)
	{
		ArrayList<Account> accoutList = new ArrayList<Account>();
		open();
		Cursor cursor = query(sql, args);
		while (cursor.moveToNext()) {
			Account account = new Account();
			account.setAccountId(cursor.getInt(0));
			account.setType(AccountEnum.getAccountEnum(cursor.getInt(1)));
			account.setMoney(cursor.getFloat(2));
			account.setDate(cursor.getString(3));
			accoutList.add(account);
		}
		//注意游标必须要关闭，否则多查询几次就会出现异常
		if(!cursor.isClosed())
		{
			cursor.close();
		}
		close();
		return accoutList;
	}
	private class DBCreator extends SQLiteOpenHelper
	{
		private Context context;
		private String createTableSql;
		private String tableName;
		

		public DBCreator(Context context, String dbname, CursorFactory factory,
				int version,String createTableSql,String tableName)
		{
			super(context, dbname, factory, version);
			this.context = context;
			this.createTableSql = createTableSql;
			this.tableName = tableName;
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(createTableSql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("drop table if exists "+tableName);
			onCreate(db);
		}
	}
}
