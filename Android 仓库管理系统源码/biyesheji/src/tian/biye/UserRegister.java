package tian.biye;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * �û�ע��ҳ��
 * 
 * @author ��־Զ
 * 
 */
public class UserRegister extends Activity {
	private EditText name;
	private EditText pass;
	private EditText passsure;
	private EditText workid;
	SqlHelpdemo db;
	SQLiteDatabase sDatabase = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userregister);
		name = (EditText) findViewById(R.id.usename);
		pass = (EditText) findViewById(R.id.password);
		passsure = (EditText) findViewById(R.id.passwordsure);
		workid = (EditText) findViewById(R.id.useide);
		db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
		sDatabase = db.getWritableDatabase();

	}

	public void sure(View v) {
		db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
		sDatabase = db.getWritableDatabase();
		if (name.getText().toString().equals("")
				|| pass.getText().toString().equals("")
				|| passsure.getText().toString().equals("")
				|| workid.getText().toString().equals("")) {

			DialogDemo.builder(UserRegister.this, "������Ϣ", "����д������Ϣ��");

		} else if (!pass.getText().toString()
				.equals(passsure.getText().toString())) {
			DialogDemo.builder(UserRegister.this, "������Ϣ", "�����������벻һ�£�");
		} else {
			String ename = name.getText().toString();
			String epass = pass.getText().toString();
			String eid = workid.getText().toString();
			// ��ѯ���
			String selectStr = "select username from user_info";
			Cursor select_cursor = sDatabase.rawQuery(selectStr, null);
			select_cursor.moveToFirst();
			String string = null;
			do {
				try {
					string = select_cursor.getString(0);
				} catch (Exception e) {
					// TODO: handle exception
					string = "";
				}
				if (string.equals(ename)) {
					DialogDemo.builder(UserRegister.this, "������Ϣ",
							"�û����Ѵ��ڣ��������û���");
					select_cursor.close();
					break;

				}
			} while (select_cursor.moveToNext());
			// û������ע�Ὺʼ
			if (!string.equals(ename)) {
				// ����ID
				int id = 0;
				String select = "select max(_id) from user_info";
				Cursor seCursor = sDatabase.rawQuery(select, null);
				try {
					seCursor.moveToFirst();
					id = Integer.parseInt(seCursor.getString(0));
					id += 1;
				} catch (Exception e) {
					// TODO: handle exception
					id = 0;
				}
				sDatabase.execSQL("insert into user_info values('" + id + "','"
						+ ename + "','" + epass + "','" 
						+ eid + "')");
				DialogDemo.builder(UserRegister.this, "��ʾ", "ע��ɹ����뷵�ص�¼�����¼");
				
				seCursor.close();
			
				
			}
		}

	}

}
