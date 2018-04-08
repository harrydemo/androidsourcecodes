package com.hanye.MovieTicket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MovieTicketActivity extends Activity {
	/** Called when the activity is first created. */
	private GridView gv;
	private SimpleAdapter adapter;
	private static final int ROW = 10;// ��������

	private ArrayList<Map<String, Object>> list;
	private List<String> dataList;// ����ѡ����λ

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ����ʾ����ı�����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ʾϵͳ�ı�����
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		gv = (GridView) findViewById(R.id.gridView1);
		dataList = new ArrayList<String>();

		adapter = new SimpleAdapter(// ����������
				this,// Context
				generateDataList(), // ����List
				R.layout.t,// �ж�Ӧlayout �� id
				new String[] { "Image", "Name" }, // �����б�
				new int[] { R.id.imageView1, R.id.textView1 });// �ж�Ӧ�ռ�id�б�

		gv.setAdapter(adapter);// ΪGridView��������������
		gv.setNumColumns(ROW);
		gv.setVerticalSpacing(20);

		gv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (dataList.contains(arg2 + 1 + "")) {
					arg1.setBackgroundColor(Color.BLACK);
					dataList.remove(arg2 + 1 + "");
				} else {
					arg1.setBackgroundColor(Color.BLUE);
					dataList.add(arg2 + 1 + "");

				}

			}

		});

	}

	/**
	 * ��������List
	 * 
	 * @return
	 */
	private List<Map<String, Object>> generateDataList() {

		if (list == null) {
			list = new ArrayList<Map<String, Object>>();
		} else {
			list.clear();
		}
		for (int i = 1; i <= 50; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("Image", R.drawable.yizi);
			map.put("Name", i);
			list.add(map);
		}
		return list;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 1, 1, "�ύ");

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 1:
			Toast.makeText(this, dataList.toString(), Toast.LENGTH_SHORT)
					.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}