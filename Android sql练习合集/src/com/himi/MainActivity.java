package com.himi;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// ------------�����ֱ��淽ʽ--------��SQLite��---------

/**
 * @author Himi
 * @���淽ʽ��SQLite ���������ݿ⡢
 * @�ŵ㣺 ���Խ��Լ������ݴ洢���ļ�ϵͳ�������ݿ⵱�У� Ҳ���Խ��Լ������ݴ�
 *         ����SQLite���ݿ⵱�У������Դ浽SD����
 * @ע��1�����ݿ����һ����Ϸ(һ��Ӧ��)��˵��˽�еģ�������һ����Ϸ���У� 
 *         ���ݿ������Ҳ��Ψһ�ġ�
 * @ע��2 apk�д��������ݿ��ⲿ�Ľ�����û��Ȩ��ȥ��/д��, 
 *         ������Ҫ�����ݿ��ļ�������sdcard�Ͽ��Խ����������.
 * @ע��3 ����ɾ��id��ǰ�����ݻ���ȫ��ɾ�����ݵ�ʱ��SQLite�����Զ�����
 *        Ҳ����˵��������ݵ�ʱ���㲻ָ��id��ôSQLiteĬ�ϻ�����ԭ��id������һ��������
 * @ע��4 android �� ��SQLite �﷨���ִ�Сд��!!!!!���Ҫע�⣡
 *   String UPDATA_DATA = "UPDATE himi SET text='ͨ��SQL������޸�����'  WHERE id=1"; 
 *                  ǧ�� ���ܿ���д��
 *   String UPDATA_DATA = "updata himi set text='ͨ��SQL������޸�����'  where id=1";
 */
public class MainActivity extends Activity implements OnClickListener {
	private Button btn_addOne, btn_deleteone, btn_check, btn_deleteTable,
			btn_edit, btn_newTable;
	private TextView tv;
	private MySQLiteOpenHelper myOpenHelper;// ����һ���̳�SQLiteOpenHelper��ʵ��
	private SQLiteDatabase mysql ; 
//---------------����������Ա�����������SD���д洢���ݿ��ļ�ʹ��
//	private File path = new File("/sdcard/himi");// ����Ŀ¼
//	private File f = new File("/sdcard/himi/himi.db");// �����ļ�
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		tv = (TextView) findViewById(R.id.tv_title);
		btn_addOne = (Button) findViewById(R.id.sql_addOne);
		btn_check = (Button) findViewById(R.id.sql_check);
		btn_deleteone = (Button) findViewById(R.id.sql_deleteOne);
		btn_deleteTable = (Button) findViewById(R.id.sql_deleteTable);
		btn_newTable = (Button) findViewById(R.id.sql_newTable);
		btn_edit = (Button) findViewById(R.id.sql_edit);
		btn_edit.setOnClickListener(this);
		btn_addOne.setOnClickListener(this);
		btn_check.setOnClickListener(this);
		btn_deleteone.setOnClickListener(this);
		btn_deleteTable.setOnClickListener(this);
		btn_newTable.setOnClickListener(this);
		myOpenHelper = new MySQLiteOpenHelper(this);// ʵ��һ�����ݿ⸨����
//��ע1  ----�����ʹ�õ��ǽ����ݿ���ļ�������SD���У���ô�������ݿ�mysql���²�����
//		if (!path.exists()) {// Ŀ¼���ڷ���false
//			path.mkdirs();// ����һ��Ŀ¼
//		}
//		if (!f.exists()) {// �ļ����ڷ���false
//			try {
//				f.createNewFile();//�����ļ� 
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} 
	}

	@Override
	public void onClick(View v) {  
		try { 
//��ע2----�����ʹ�õ��ǽ����ݿ���ļ�������SD���У���ô�������ݿ�mysql���²�����
//			    mysql = SQLiteDatabase.openOrCreateDatabase(f, null); 
//��ע3--- ���������ݿ��ļ�Ĭ�Ϸ���ϵͳ��,��ô�������ݿ�mysql���²�����
				mysql = myOpenHelper.getWritableDatabase(); // ʵ�����ݿ�
			if (v == btn_addOne) {// �������
				// ---------------------- ��д���������---------
				// ContentValues ��ʵ����һ����ϣ��HashMap�� keyֵ���ֶ����ƣ�
				//Valueֵ���ֶε�ֵ��Ȼ�� ͨ�� ContentValues �� put �����Ϳ���
				//�����ݷŵ�ContentValues�У�Ȼ����뵽����ȥ!
				ContentValues cv = new ContentValues();
				cv.put(MySQLiteOpenHelper.TEXT, "Himi��������");
				mysql.insert(MySQLiteOpenHelper.TABLE_NAME, null, cv);
				// inser() ��һ������ ��ʶ��Ҫ��������ı���
				// �ڶ������� :Ĭ�ϴ�null����
				// �������ǲ��������
				// ---------------------- SQL������--------------
				// String INSERT_DATA =
				// "INSERT INTO himi (id,text) values (1, 'ͨ��SQL������')";
				// db.execSQL(INSERT_DATA);
				tv.setText("������ݳɹ�������鿴���ݿ��ѯ");
			} else if (v == btn_deleteone) {// ɾ������
				// ---------------------- ��д�����ɾ��
				mysql.delete("himi", MySQLiteOpenHelper.ID + "=1", null);
				// ��һ������ ��Ҫ�����ı���
				// �ڶ�������Ϊ id+�������±� ����������Ǵ���null����ʾȫ��ɾ��
				// ����������Ĭ�ϴ�null����
				// ----------------------- SQL�����ɾ��
				// String DELETE_DATA = "DELETE FROM himi WHERE id=1";
				// db.execSQL(DELETE_DATA);
				tv.setText("ɾ�����ݳɹ�������鿴���ݿ��ѯ");
			} else if (v == btn_check) {// ��������
//��ע4------
				Cursor cur = mysql.rawQuery("SELECT * FROM "
						+ MySQLiteOpenHelper.TABLE_NAME, null);
				if (cur != null) {
					String temp = "";
					int i = 0;
					while (cur.moveToNext()) {//ֱ������false˵�����е�������ĩβ
						temp += cur.getString(0); 
						// ����0 ָ�����е��±�,�����0ָ����id��
						temp += cur.getString(1);
						// �����0����ڵ�ǰӦ�������ǵ�text����
						i++;
						temp += "  "; // ��������������ʾ��ʽ ,�Ǻ�~
						if (i % 3 == 0) // ��������������ʾ��ʽ ,�Ǻ�~
							temp += "\n";// ��������������ʾ��ʽ ,�Ǻ�~
					}
					tv.setText(temp);
				}
			} else if (v == btn_edit) {// �޸�����
				// ------------------------�����ʽ���޸� -------------
				ContentValues cv = new ContentValues();
				cv.put(MySQLiteOpenHelper.TEXT, "�޸ĺ������");
				mysql.update("himi", cv, "id " + "=" + Integer.toString(3), null);
				// ------------------------SQL������޸� -------------
				// String UPDATA_DATA =
				// "UPDATE himi SET text='ͨ��SQL������޸�����'  WHERE id=1";
				// db.execSQL(UPDATA_DATA);
				tv.setText("�޸����ݳɹ�������鿴���ݿ��ѯ");
			} else if (v == btn_deleteTable) {// ɾ����
				mysql.execSQL("DROP TABLE himi");
				tv.setText("ɾ����ɹ�������鿴���ݿ��ѯ");
			} else if (v == btn_newTable) {// �½���
				String TABLE_NAME = "himi";
				String ID = "id";
				String TEXT = "text";
				String str_sql2 = "CREATE TABLE " + TABLE_NAME + "(" + ID
						+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TEXT
						+ " text );";
				mysql.execSQL(str_sql2);
				tv.setText("�½���ɹ�������鿴���ݿ��ѯ");
			}
			// ɾ�����ݿ�:
			// this.deleteDatabase("himi.db");
		} catch (Exception e) {
			tv.setText("����ʧ�ܣ�");
		} finally {// ���try���쳣��ҲҪ�����ݿ���йر�
			mysql.close();
		}
	}
}

//
// ��ע1:
// ��Android�в�ѯ������ͨ��Cursor����ʵ�ֵģ�������ʹ��SQLiteDatabase.query()����ʱ��
// ��õ�һ��Cursor����Cursorָ��ľ���ÿһ�����ݡ����ṩ�˺ܶ��йز�ѯ�ķ��������巽�����£�
//
// ���� ˵��
// move �Ե�ǰ��λ��Ϊ�ο�����Cursor�ƶ���ָ����λ�ã��ɹ�����true, ʧ�ܷ���false
//
// moveToPosition ��Cursor�ƶ���ָ����λ�ã��ɹ�����true,ʧ�ܷ���false
//
// moveToNext ��Cursor��ǰ�ƶ�һ��λ�ã��ɹ�����true,ʧ�ܷ���false
//
// moveToLast ��Cursor����ƶ�һ��λ�ã��ɹ�����true,ʧ�ܷ��� false��
//
// movetoFirst ��Cursor�ƶ�����һ�У��ɹ�����true,ʧ�ܷ���false
//
// isBeforeFirst ����Cursor�Ƿ�ָ���һ������֮ǰ
//
// isAfterLast ����Cursor�Ƿ�ָ�����һ������֮��
//   
// isClosed ����Cursor�Ƿ�ر�
//
// isFirst ����Cursor�Ƿ�ָ���һ������
//
// isLast ����Cursor�Ƿ�ָ�����һ������
//
// isNull ����ָ��λ�õ�ֵ�Ƿ�Ϊnull
//
// getCount �����ܵ���������
//
// getInt ���ص�ǰ����ָ������������
