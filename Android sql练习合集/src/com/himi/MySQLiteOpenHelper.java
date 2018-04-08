
package com.himi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author Himi
 * @解释 此类我们只需要传建一个构造函数 以及重写两个方法就OK啦、
 * 
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public final static int VERSION = 1;// 版本号
	public final static String TABLE_NAME = "himi";// 表名
	public final static String ID = "id";// 后面ContentProvider使用
	public final static String TEXT = "text";
	public static final String DATABASE_NAME = "Himi.db";

	public MySQLiteOpenHelper(Context context) {
		// 在Android 中创建和打开一个数据库都可以使用openOrCreateDatabase 方法来实现，
		// 因为它会自动去检测是否存在这个数据库，如果存在则打开，不过不存在则创建一个数据库；
		// 创建成功则返回一个 SQLiteDatabase对象，否则抛出异常FileNotFoundException。
		// 下面是来创建一个名为"DATABASE_NAME"的数据库，并返回一个SQLiteDatabase对象 
		
		super(context, DATABASE_NAME, null, VERSION); 
	} 
	@Override
	// 在数据库第一次生成的时候会调用这个方法，一般我们在这个方法里边生成数据库表;
	public void onCreate(SQLiteDatabase db) { 
		String str_sql = "CREATE TABLE " + TABLE_NAME + "(" + ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TEXT + " text );";
		// CREATE TABLE 创建一张表 然后后面是我们的表名
		// 然后表的列，第一个是id 方便操作数据,int类型
		// PRIMARY KEY 是指主键 这是一个int型,用于唯一的标识一行;
		// AUTOINCREMENT 表示数据库会为每条记录的key加一，确保记录的唯一性;
		// 最后我加入一列文本 String类型
		// ----------注意：这里str_sql是sql语句，类似dos命令，要注意空格！
		db.execSQL(str_sql);
		// execSQL()方法是执行一句sql语句
		// 虽然此句我们生成了一张数据库表和包含该表的sql.himi文件,
		// 但是要注意 不是方法是创建，是传入的一句str_sql这句sql语句表示创建！！
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 一般默认情况下，当我们插入 数据库就立即更新
		// 当数据库需要升级的时候，Android 系统会主动的调用这个方法。
		// 一般我们在这个方法里边删除数据表，并建立新的数据表，
		// 当然是否还需要做其他的操作，完全取决于游戏需求。
		Log.v("Himi", "onUpgrade");
	} 
}  