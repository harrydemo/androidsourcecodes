package tian.biye;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;


/**
 * �û�����ҳ��
 * 
 * @author ��־Զ
 * 
 */

public class Yonghu extends Activity {
	private ListView listview;
	// private TextView xuhao;
	// private TextView yonghum;
	// private TextView mima;
	// private te
	String id[];
	String name[];
	String pass[];
	String num[];
	SqlHelpdemo db;
	int i = 0;
	SQLiteDatabase sDatabase = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yonghu);
		setTitle("�û�����");
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
		sDatabase = db.getWritableDatabase();
		listview = (ListView) findViewById(R.id.yonghulist);
		List<Map<String, Object>> slist = new ArrayList<Map<String, Object>>();
		String selectStr = "select _id,username,password,num  from user_info";
		Cursor cursor = sDatabase.rawQuery(selectStr, null);

		cursor.moveToFirst();

		int count = cursor.getCount();
		pass = new String[count];
		num = new String[count];
		id = new String[count];
		name = new String[count];
		do {
			try {
				id[i] = cursor.getString(0);
				name[i] = cursor.getString(1);
				pass[i] = cursor.getString(2);
				num[i] = cursor.getString(3);
				i++;

			} catch (Exception e) {
				// TODO: handle exception

			}

		} while (cursor.moveToNext());

		for (int i = 0; i < id.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("did", id[i]);
			map.put("dname", name[i]);
			map.put("dpass", pass[i]);
			map.put("dnum", num[i]);
			slist.add(map);
		}
		SimpleAdapter simple = new SimpleAdapter(this, slist,
				R.layout.yonghuadapter, new String[] { "did", "dname", "dpass",
						"dnum" }, new int[] { R.id.t1, R.id.t2, R.id.t3,
						R.id.t4, });
		listview.setAdapter(simple);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				for(int i=0;i<name.length;i++){
					if(arg2==i){
						
						builder.setTitle("ȷ����Ϣ");
						builder.setMessage("ȷ��Ҫɾ�����û���");
						final int j=i;
						builder.setPositiveButton("ȷ��", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
					sDatabase.execSQL("delete from user_info where username='"+name[j]+"'");
								Intent intent=new Intent();
								intent.setClass(Yonghu.this, Yonghu.class);
								startActivity(intent);
								finish();
							}
						});
						builder.setNegativeButton("ȡ��", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
								
							}
						});
						builder.create().show();
						
					}
					
				}
				
			}
		});
	}

}
