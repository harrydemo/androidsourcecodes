package com.bus.shenyang.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bus.shenyang.R;
import com.bus.shenyang.common.Bus;
import com.bus.shenyang.database.BusDb;

public class Transfer extends Activity {
	String[] sumb;
	BusDb dbfile;
	private SQLiteDatabase database;
	boolean state=true;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbfile = new BusDb(this);
		dbfile.openDatabase();
		dbfile.closeDatabase();
		setContentView(R.layout.transfer);
		final EditText key1 = (EditText) this.findViewById(R.id.editText1);
		final EditText key2 = (EditText) this.findViewById(R.id.editText2);
		Button query = (Button) this.findViewById(R.id.button1);
		final TextView result = (TextView) this.findViewById(R.id.textView3);

		database = SQLiteDatabase.openOrCreateDatabase(BusDb.DB_PATH + "/"
				+ BusDb.DB_NAME, null);

		query.setOnClickListener(new View.OnClickListener() {

			private int[] countx;
			private int[] county;
			int busstation1x, busstation1y;
			String[] LineStation1;// 经过这个站的所有站点
			String[] LineStation2;
			String[] linkStation1;
			String[] linkStation2;// 一条线路的所有站点
			String[] s1;
			String[] s2;
			private String strlineStation;
			private String busstation1;
			private String       busstation2;
			private String[] LineStation3;
			private String[] LineStation4;
			private String[] LineStation33;
			private String[] LineStation44;
			@Override
			public void onClick(View v) {
				result.setText(" ");
				
				  busstation1 = key1.getText().toString();
			      busstation2 = key2.getText().toString();
				System.out.println("1111111111111");
				Bus bus1 = chaxun(busstation1);
				Bus bus2 = chaxun(busstation2);
				System.out.println("bus1=" + bus1);
				System.out.println("bus2=" + bus2);
				if (bus1 == null || bus2 == null) {
					new AlertDialog.Builder(Transfer.this)
							.setTitle("提示")
							.setMessage("亲。。。。您输入的站点有误")
							.setPositiveButton("请输入",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}

									})

							.show();
				} else {
					
						StringBuffer station1 = new StringBuffer();
						String str33,str44;
						str33 = bus1.getLine();
						str44 = bus2.getLine();
						LineStation33 = str33.split("→");
						LineStation44 = str44.split("→");
						int LineAmount11 = LineStation33.length;
						int LineAmount22 = LineStation44.length;
						for (int index1 = 0; index1 < LineAmount11; index1++) {
							for (int in1 = 0; in1 < LineAmount22; in1++) {					
								if(LineStation33[index1].equals(LineStation44[in1]))
								{
								 station1.append(LineStation33[index1]);
								 station1.append("或→");
								 state=false;
								 System.out.println("二站点在同一线路上换乘的");
								}
							}
						}

						result.setText("您输入的二站在同一条线路"+station1.toString());
						
					if(state)	
						
					 {
						System.out.println("二站点不在同一线路上换乘的");
						String str1, str2,str3,str4;
						StringBuffer station = new StringBuffer();
						str1 = bus1.getStation();
						str2 = bus2.getStation();
						str3 = bus1.getLine();
						str4 = bus2.getLine();
						
						
						LineStation1 = str1.split("→");
						LineStation2 = str2.split("→");
						LineStation3 = str3.split("→");
						LineStation4 = str4.split("→");
						
						int LineAmount1 = LineStation1.length;
						int LineAmount2 = LineStation2.length;
						for (int index = 0; index < LineAmount1; index++) {
							linkStation1 = LineStation1[index].split("、");
							for (int in = 0; in < LineAmount2; in++) {
								linkStation2 = LineStation2[in].split("、");
								 strlineStation=Focus(linkStation1 ,linkStation2,LineStation3[index],LineStation4[in]);
								 station.append(strlineStation);
								 station.append("→");
							}
						}

						
						result.setText(station.toString());
				
					}
				}
				
				
			}

			private String Focus(String[] s1,String[] s2,String s3,String s4) {
				String stri =" ";
				String str3 = "";
				String message="";
				countx = new int[10];
				county = new int[10];
				int m, n, m1, n1, i, j, arry = 0;
				System.out.println(s1.length+"!!!!!!!!!!!!"+s2.length);
				for (i = 0; i < s1.length; i++) {
					for (j = 0; j < s2.length; j++) {
						if (s1[i].equals(s2[j])) {
							str3 = str3 + s1[i];
							message="乘坐"+s3+"在"+s1[i]+"换乘"+s4;
							System.out.println("乘坐"+s3+"在"+s1[i]+"换乘"+s4);
							System.out.println("i=" + i + "--j=" + j);

							countx[arry] = i;
							county[arry] = j;
							arry++;

						}
					}
				}
				
			
				return message;
			}
		});
	}

	protected Bus chaxun(String busstation) {
		Bus bus = new Bus();
		StringBuffer lines = new StringBuffer();
		StringBuffer station = new StringBuffer();
		Cursor cursor = null;
		if (busstation != null && !busstation.equals("")
				&& busstation.length() > 1) {
			cursor = database.rawQuery(
					"select id,line,time,station,opposite from bus_line where station like '%"
							+ busstation + "%'", null);
			if (cursor.moveToNext()) {
				if (cursor.getColumnIndex("id") == 0
						|| cursor.getColumnIndex("id") == 1) {
					bus.setId(cursor.getInt(cursor.getColumnIndex("id")));
					lines.append(cursor.getString(1));
					lines.append("→");
					station.append(cursor.getString(3));
					station.append("→");

				}
				while (cursor.moveToNext()) {// 迭代记录集
					// cursor.moveToFirst();
					bus.setId(cursor.getInt(cursor.getColumnIndex("id")));
					System.out.println("id="
							+ cursor.getInt(cursor.getColumnIndex("id")));
					lines.append(cursor.getString(1));
					lines.append("→");
					station.append(cursor.getString(3));
					station.append("→");
				}
				bus.setLine(lines.toString());
				bus.setStation(station.toString());
				System.out.println("bus.getLine()=" + bus.getLine());
				System.out.println("bus.getStation()=" + bus.getStation());
			} else {
				bus = null;
			}
		} else {
			cursor = null;
			bus = null;
		}

		return bus;

	}
	
	  @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				new AlertDialog.Builder(Transfer.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("沈阳离线公交")
						.setMessage("你确定退出了哦?")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										android.os.Process.killProcess(android.os.Process.myPid());
									}

								}).setNegativeButton("取消", null).show();
				return true;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
		
	
	
}
