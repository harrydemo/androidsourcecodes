package com.myc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ImageActivity extends Activity {
	/** Called when the activity is first created. */
	ListView mListView;
	private String[] mListStr = { "ͼƬ����", "ͼƬԲ��", "ͼƬ��Ӱ", "��תͼƬ", "ͼƬ��ת", "ͼƬɫ�����Ͷȡ�ɫ�ࡢ���ȴ���", "Ϳѻ��ˮӡ", "ͼƬ��д����", "����Ч��",
			"ģ��Ч��" ,"�ữЧ��(��˹ģ��)", "����Ч��", "��Ч��", "��ƬЧ��", "����Ч��", "ͼƬ�ü�", "����"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findView();

		// ImageIO.write(r, "jpg", new File("d:\\u.jpg"));// ����ͼƬ�ļ�
	}

	private void findView() {
		mListView = (ListView) findViewById(R.id.listview);
		// SimpleAdapter = new SimpleAdapter(context, data, resource, from, to)
		mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListStr));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				if (position == 5) {
					Intent intent = new Intent(ImageActivity.this, ImageToneActivity.class);
					startActivity(intent);
					return;
				}
				
				if (position == 15) {
					Intent intent = new Intent(ImageActivity.this, ChooseImage.class);
					startActivity(intent);
					return;
				}
				
				if (position == 16) {
					Intent intent = new Intent(ImageActivity.this, SketchActivity.class);
					startActivity(intent);
					return;
				}


				Intent intent = new Intent(ImageActivity.this, Image.class);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
	}
}