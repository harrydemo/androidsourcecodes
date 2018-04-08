package cn.err0r.android.sms.view;

import cn.err0r.android.sms.R;
import cn.err0r.android.sms.database.SMSSampleDao;
import cn.err0r.android.sms.database.SMSSampleModel;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SMSAdd extends Activity {
	
	EditText et ; 
	CheckBox cb,samplesmslist_cb ;
	Button btnSave;
	SMSSampleModel sample;
	SMSSampleDao sampledao;
	int sid;
	
	void findView(){
		et = (EditText)findViewById(R.id.add_et1);
		cb = (CheckBox)findViewById(R.id.add_cb1);
		btnSave = (Button)findViewById(R.id.add_btn1);
	}
	
	//每次打开时调用
	@Override
	protected void onStart() {
		
		// TODO Auto-generated method stub
		super.onStart();
		sid=-1;
		//判断是否是通过intent调用
		Intent i = getIntent();
		if(i.getStringExtra("Context") != null){
			et.setText(i.getStringExtra("Context"));
			et.setSelection(et.length());
			cb.setChecked(i.getBooleanExtra("State", false));
			sid=i.getIntExtra("Sid", -1);
			Log.i("intent", et.getText().toString()+" "+cb.isChecked()+" "+sid);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		findView();
		
		btnSave.setOnClickListener(saveSample);
	}
	
	
	OnClickListener saveSample = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			sample = new SMSSampleModel();
			sample.set_class("User");
			sample.set_body(et.getText().toString());
			sample.set_state(cb.isChecked() == true ? "1" : "0");
			if(sampledao == null)
				sampledao = new SMSSampleDao(SMSAdd.this);
			switch (sid){
			case -1:sampledao.insert(sample);
			Toast.makeText(SMSAdd.this, getResources().getString(R.string.add_succeed), Toast.LENGTH_SHORT).show();
				break;
			default:sampledao.updatafastreplyState(sid, cb.isChecked(),sample.get_body());
			Toast.makeText(SMSAdd.this, getResources().getString(R.string.edit_succeed), Toast.LENGTH_SHORT).show();
			}
			finish();
		}
	};
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
	}
}
