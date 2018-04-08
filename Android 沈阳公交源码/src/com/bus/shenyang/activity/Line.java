package com.bus.shenyang.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.bus.shenyang.BusActivity;
import com.bus.shenyang.R;
import com.bus.shenyang.common.Bus;
import com.bus.shenyang.database.BusDb;

public class Line extends Activity {
	BusDb dbfile;
	private SQLiteDatabase database;

	public void onCreate(Bundle savedInstanceState) {
		dbfile = new BusDb(this);
		dbfile.openDatabase();
		dbfile.closeDatabase();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line);
		final EditText key = (EditText) this.findViewById(R.id.editText1);
		Button seach = (Button) this.findViewById(R.id.button1);
		Button opposite = (Button) this.findViewById(R.id.button2);
		final TextView result = (TextView) this.findViewById(R.id.textView2);

		database = SQLiteDatabase.openOrCreateDatabase(BusDb.DB_PATH + "/"
				+ BusDb.DB_NAME, null);

		seach.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				result.setText(" ");
				String busline = key.getText().toString();

				Bus bus = new Bus();
				StringBuffer lines = new StringBuffer();
				Cursor cursor = null;

				if (busline != null && !busline.equals("")) {
					cursor = database.rawQuery(
							"select id,line,time,station,opposite from bus_line where line like '%"
									+ busline + "%'", null);
					if (cursor.moveToNext()) {// 迭代记录集
						bus.setId(cursor.getInt(cursor.getColumnIndex("id")));
						lines.append(cursor.getString(1));
						lines.append("→");
						bus.setStation(cursor.getString(3));
						bus.setLine(lines.toString());// 所有线路连接在一起
					} else {
						bus = null;
					}

				} else {
					cursor = null;
					bus = null;
				}

				if (bus == null) {
					new AlertDialog.Builder(Line.this)
							.setTitle("提示")
							.setMessage("亲。。。。您输入的线路有误")
							.setPositiveButton("请输入",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}

									})

							.show();
				} else {
					result.setText(bus.getStation());
				}

			}
		});
		opposite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				result.setText(" ");
				String busline = key.getText().toString();

				Bus bus = new Bus();
				StringBuffer lines = new StringBuffer();
				Cursor cursor = null;

				if (busline != null && !busline.equals("")) {
					cursor = database.rawQuery(
							"select id,line,time,station,opposite from bus_line where line like '%"
									+ busline + "%'", null);
					if (cursor.moveToNext()) {// 迭代记录集
						bus.setId(cursor.getInt(cursor.getColumnIndex("id")));
						lines.append(cursor.getString(1));
						lines.append("→");
						bus.setStation(cursor.getString(4));
						bus.setLine(lines.toString());// 所有线路连接在一起
					} else {
						bus = null;
					}

				} else {
					cursor = null;
					bus = null;
				}

				if (bus == null) {
					new AlertDialog.Builder(Line.this)
							.setTitle("提示")
							.setMessage("亲。。。。您输入的线路有误")
							.setPositiveButton("请输入",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}

									})

							.show();
				} else {
					result.setText(bus.getStation());
				}

			}
		});
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(Line.this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("沈阳离线公交")
					.setMessage("你确定退出了哦?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface arg0,
										int arg1) {
									android.os.Process.killProcess(android.os.Process.myPid());
									Line.this.finish();
								}

							}).setNegativeButton("取消", null).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
}
