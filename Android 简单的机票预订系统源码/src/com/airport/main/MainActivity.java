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

	// 为自定义主界面而配的适配器
	public class ImageAdapter extends BaseAdapter {

		public ImageAdapter(Context c) {

		}

		// 返回图片的长度
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
			image.setPadding(8, 8, 8, 8);// 图片之间的距离
			image.setAdjustViewBounds(false);// 边界是否对齐
			image.setImageResource(images[position]);
			TextView music_title = (TextView) view.findViewById(R.id.main_text);
			music_title.setText(texts[position]);
			return view;
		}

		// 为图片定义一个数组
		private Integer[] images = { R.drawable.plane, R.drawable.wine,
				R.drawable.ticketmanager, R.drawable.dingdanmanager,
				R.drawable.settings, R.drawable.about };
	}

	// 为文字定一个数组
	private String[] texts = { "机票查询", "酒店查询", "订单管理", "订单审批", "系统设置", "退出" };
}