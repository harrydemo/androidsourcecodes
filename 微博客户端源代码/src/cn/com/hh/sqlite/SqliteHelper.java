package cn.com.hh.sqlite;

import cn.com.hh.domian.UserInfo;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * �������ݿ�  ���������û���¼blog����Ҫ����Ϣ
 * @author nxh
 *
 */
public class SqliteHelper extends SQLiteOpenHelper {

	public static final String TB_NAME = "users";

	public SqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	//������
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL("CREATE TABLE IF NOT EXISTS "+
	                TB_NAME+"("+
	                UserInfo.ID+" integer primary key,"+
	                UserInfo.USERID+" varchar,"+
	                UserInfo.TOKEN+" varchar,"+
	                UserInfo.TOKENSECRET+" varchar,"+
	                UserInfo.USERNAME+" varchar,"+
	                UserInfo.USERICON+" blob"+
	                ")"
	                );
	        Log.e("Database","onCreate");
	}
	//���±�
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
        Log.e("Database","onUpgrade");
	}
	//������
	public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
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

}
