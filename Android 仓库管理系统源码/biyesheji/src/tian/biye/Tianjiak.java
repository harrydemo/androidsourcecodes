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
 *  ��ӿͻ���Ϣҳ��
 * @author��־Զ
 * 
 */
public class Tianjiak extends Activity {
	private EditText gsmc;
	private EditText lxr;
	private EditText lxdz;
	private EditText csmc;
	private EditText dqmc;
	private EditText yzbm;
	private EditText lxdh;
	private EditText czhm;
	private EditText gszy;
	SqlHelpdemo db;
	SQLiteDatabase sDatabase = null;
	String names;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tianjiak);
		setTitle("��ӿͻ���Ϣ");
		Intent inte = getIntent();
		Bundle name = inte.getExtras();
		 names = inte.getStringExtra("username");
		db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
		sDatabase = db.getWritableDatabase();
		gsmc=(EditText) findViewById(R.id.gsmce);
		lxr=(EditText) findViewById(R.id.lxre);
		csmc=(EditText) findViewById(R.id.csmce);
		lxdz=(EditText) findViewById(R.id.lxdze);
		dqmc=(EditText) findViewById(R.id.dqmce);
		yzbm=(EditText) findViewById(R.id.yzbme);
		lxdh=(EditText) findViewById(R.id.lxdhe);
		czhm=(EditText) findViewById(R.id.czhme);
		gszy=(EditText) findViewById(R.id.gszye);
	}
    /**
     * ���水ť����
     * @param v
     */
	public void save1(View v) {
		if(gsmc.getText().toString().equals("")){
			DialogDemo.builder(Tianjiak.this, "��ʾ", "�����빫˾����");
		}
		else{
			// ��ѯ���
						String egsmc=gsmc.getText().toString();
						String elxr=lxr.getText().toString();
						String elxdz=lxdz.getText().toString();
						String ecsmc=csmc.getText().toString();
						String edqmc=dqmc.getText().toString();
						String eyzbm=yzbm.getText().toString();
						String elxdh=lxdh.getText().toString();
						String eczhm=czhm.getText().toString();
						String egszy=gszy.getText().toString();
						String selectStr = "select comname  from guke";
						System.out.println("11111111111111");
						Cursor cursor = sDatabase.rawQuery(selectStr, null);
						System.out.println("22222222222222");
						cursor.moveToFirst();
						String nameg = null;
						
						do {
							try {
								nameg = cursor.getString(0);
								
								System.out.println("3333333333333333333333");
							} catch (Exception e) {
								// TODO: handle exception
								nameg = "";
								
							}
							if (nameg.equals(egsmc)) {
								DialogDemo.builder(Tianjiak.this, "������Ϣ","�ù�˾��Ϣ�Ѵ���");
								cursor.close();
								break;

							}
						} while (cursor.moveToNext());
					
						if (!nameg.equals(egsmc)) {
							// ����ID
							int id = 0;
							String select = "select max(_id) from guke";
							Cursor seCursor = sDatabase.rawQuery(select, null);
							try {
								seCursor.moveToFirst();
								id = Integer.parseInt(seCursor.getString(0));
								id += 1;
							} catch (Exception e) {
								// TODO: handle exception
								id = 0;
							}
							sDatabase.execSQL("insert into guke values('" + id + "','"
									+ egsmc + "','" + elxr + "','" +elxdz+ "','"+ecsmc+ "','"
									+ edqmc+ "','"+eyzbm+ "','"+ elxdh+ "','"+eczhm+ "','"+egszy+ "')");
							Toast.makeText(Tianjiak.this, "��ӳɹ�", Toast.LENGTH_LONG).show();
							
							seCursor.close();
						
							
						}
		}
	}

	public void back1(View v) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(Tianjiak.this, MenuDemo.class);
		startActivity(intent);

	}

}
