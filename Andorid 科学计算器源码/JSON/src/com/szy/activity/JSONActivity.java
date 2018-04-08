package com.szy.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class JSONActivity extends Activity {
	private Button button1;
	private Button button2;
	private Button button3;
	private String URL_JSONARRAY = "http://10.0.2.2:8080/Miusc/aa.json";
	private String URL_JSONOBJECT = "http://10.0.2.2:8080/Miusc/bb.json";
	private String URL_JSON = "http://10.0.2.2:8080/Miusc/cc.json";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViews();
		setListeners();
	}

	private void findViews() {
		button1 = (Button) this.findViewById(R.id.button1);
		button2 = (Button) this.findViewById(R.id.button2);
		button3 = (Button) this.findViewById(R.id.button3);
	}

	private void setListeners() {
		button1.setOnClickListener(onClickListener);
		button2.setOnClickListener(onClickListener);
		button3.setOnClickListener(onClickListener);
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.button1:
				try {
					showResult(JSONutil.getJSONArray(URL_JSONARRAY));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.button2:
				try {
					showResult(JSONutil.getJSONObject(URL_JSONOBJECT));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.button3:
				try {
					showResult(JSONutil.getJSON(URL_JSON));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	private void showResult(List<Map<String, String>> list) {
		StringBuffer sb=new StringBuffer();
		try {
			for (Map<String, String> map : list) {
				String id = map.get("id");
				String name = map.get("name");
				sb.append(id).append("--").append(name);
			}
			new AlertDialog.Builder(this).setTitle("json").setMessage(sb.toString()).setPositiveButton("ok", null).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}