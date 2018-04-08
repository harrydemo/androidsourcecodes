package com.szy;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener {
	private Button btn;
	private ListView listView;
	private static final int REQUEST_CODE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btn = (Button) this.findViewById(R.id.btn);
		listView = (ListView) this.findViewById(R.id.listview);
		/**
		 * 判断当前手机是否支持语音识别功能
		 */
		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (list.size() != 0) {
			btn.setOnClickListener(this);
		} else {
			btn.setEnabled(false);
			btn.setText("当前语音识别设备不可用...");
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn) {
			/**
			 * 启动手机内置的语言识别功能
			 */
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // 设置为当前手机的语言类型
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "请说话，我识别");// 出现语言识别界面上面需要显示的提示
			startActivityForResult(intent, REQUEST_CODE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		/**
		 * 回调获取从谷歌得到的数据
		 */
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			List<String> list = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			listView.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, list)); // 把数据显示在listview中
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}