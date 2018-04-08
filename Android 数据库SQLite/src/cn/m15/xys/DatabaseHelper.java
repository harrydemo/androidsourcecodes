package cn.m15.xys;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mInstance = null;

    /** ���ݿ����� **/
    public static final String DATABASE_NAME = "xys.db";

    /** ���ݿ�汾�� **/
    private static final int DATABASE_VERSION = 1;

    /**���ݿ�SQL��� ���һ����**/
    private static final String NAME_TABLE_CREATE = "create table test("
	    + "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT,"+"hp INTEGER DEFAULT 100,"+ "mp INTEGER DEFAULT 100,"
	    + "number INTEGER);";

    DatabaseHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
     /**����ģʽ**/
    static synchronized DatabaseHelper getInstance(Context context) {
	if (mInstance == null) {
	    mInstance = new DatabaseHelper(context);
	}
	return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	/**����������ӱ�**/
	db.execSQL(NAME_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	/**�����õ���ǰ���ݿ�İ汾��Ϣ ��֮ǰ���ݿ�İ汾��Ϣ   �����������ݿ�**/ 
    }

    
    /**
     * ɾ�����ݿ�
     * @param context
     * @return
     */
    public boolean deleteDatabase(Context context) {
	return context.deleteDatabase(DATABASE_NAME);
    }
}
