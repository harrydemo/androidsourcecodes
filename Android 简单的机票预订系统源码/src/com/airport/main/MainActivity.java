package com.airport.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				switch (position) {
				case 0:
					Intent intent = new Intent(MainActivity.this,
							CheckAirportTabActivity.class);
					startActivity(intent);
					finish();
					break;

				case 1:
					break;
				case 2:
					break;
				case 3:
					Intent intent1 = new Intent(MainActivity.this,
							FillInformation.class);
					startActivity(intent1);
					finish();
					break;
				case 4:
					break;
				case 5:
				System.exit(1);
					break;
				}

			}
		});
	}

	// Ϊ�Զ�������������������
	public class ImageAdapter extends BaseAdapter {

		public ImageAdapter(Context c) {

		}

		// ����ͼƬ�ĳ���
		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			LayoutInflater li = getLayoutInflater();
			view = li.inflate(R.layout.main_textview, null);
			ImageView image = (ImageView) view.findViewById(R.id.main_images);
			image.setPadding(8, 8, 8, 8);// ͼƬ֮��ľ���
			image.setAdjustViewBounds(false);// �߽��Ƿ����
			image.setImageResource(images[position]);
			TextView music_title = (TextView) view.findViewById(R.id.main_text);
			music_title.setText(texts[position]);
			return view;
		}

		// ΪͼƬ����һ������
		private Integer[] images = { R.drawable.plane, R.drawable.wine,
				R.drawable.ticketmanager, R.drawable.dingdanmanager,
				R.drawable.settings, R.drawable.about };
	}

	// Ϊ���ֶ�һ������
	private String[] texts = { "��Ʊ��ѯ", "�Ƶ��ѯ", "��������", "��������", "ϵͳ����", "�˳�" };
}