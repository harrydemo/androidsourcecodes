package tian.biye;

import android.app.Activity;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Tianjias extends Activity {
	private EditText name;
	private EditText guige;
	private EditText danwei;
	SqlHelpdemo db;
	SQLiteDatabase sDatabase = null;
	String names;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tianjias);
		setTitle("�����Ʒ��Ϣ");
		Intent inte = getIntent();
		Bundle namee = inte.getExtras();
		 names = inte.getStringExtra("username");
		db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
		sDatabase = db.getWritableDatabase();
		name=(EditText) findViewById(R.id.tianjiashangme);
		guige=(EditText) findViewById(R.id.tianjiashangge);
		danwei=(EditText) findViewById(R.id.tianjiashangje);
		
	}
	public void save(View v){
		
		if(name.getText().toString().equals("")){
			DialogDemo.builder(Tianjias.this, "��ʾ","��������Ʒ��" );
		}
		else {
			// ��ѯ���
			String ename=name.getText().toString();
			String eguige=guige.getText().toString();
			String edanwei=danwei.getText().toString();
			String selectStr = "select pname,pguige,pdanwei from products";
			System.out.println("11111111111111");
			Cursor cursor = sDatabase.rawQuery(selectStr, null);
			System.out.println("22222222222222");
			cursor.moveToFirst();
			String nam = null;
			String guig = null;
			String danwe = null;
			do {
				try {
					nam = cursor.getString(0);
					guig = cursor.getString(1);
					danwe= cursor.getString(2);
					System.out.println("3333333333333333333333");
				} catch (Exception e) {
					// TODO: handle exception
					nam = "";
					guig = "";
					danwe = "";
				}
				if (nam.equals(ename)&&guig.equals(eguige)&&danwe.equals(edanwei)) {
					DialogDemo.builder(Tianjias.this, "������Ϣ","����Ʒ��Ϣ�Ѵ���");
					cursor.close();
					break;

				}
			} while (cursor.moveToNext());
		
			if (!(nam.equals(ename)&&guig.equals(eguige)&&danwe.equals(edanwei))) {
				// ����ID
				int id = 0;
				String select = "select max(_id) from products";
				Cursor seCursor = sDatabase.rawQuery(select, null);
				try {
					seCursor.moveToFirst();
					id = Integer.parseInt(seCursor.getString(0));
					id += 1;
				} catch (Exception e) {
					// TODO: handle exception
					id = 0;
				}
				sDatabase.execSQL("insert into products values('" + id + "','"
						+ ename + "','" + eguige + "','" 
						+ edanwei + "')");
				Toast.makeText(Tianjias.this, "��ӳɹ�", Toast.LENGTH_LONG).show();
				
				seCursor.close();
			
				
			}
		}
		
	}
	public void back(View v){
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(Tianjias.this, MenuDemo.class);
		startActivity(intent);
	}

}
