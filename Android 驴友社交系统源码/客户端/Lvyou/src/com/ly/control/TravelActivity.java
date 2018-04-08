package com.ly.control;



import com.ly.control.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TravelActivity extends Activity {
	private Button bt;
	private String id,title,time,content;
	private TextView tv2,tv4,tv6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.travel);
		tv2=(TextView) findViewById(R.id.TextView02);
		tv4=(TextView) findViewById(R.id.TextView04);
		tv6=(TextView) findViewById(R.id.TextView06);
		
		id=getIntent().getStringExtra("id");
		title=getIntent().getStringExtra("title");
		time=getIntent().getStringExtra("time");
		content=getIntent().getStringExtra("content");
		//bt=(Button) findViewById(R.id.Button01);
		tv2.setText(title);
		tv4.setText(time);
		tv6.setText(content);
		
		
	}
//	private OnClickListener l=new OnClickListener(){
//
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			if(v==bt){
//				Intent i=new Intent(TravelActivity.this,ActorActivity.class);
//				startActivity(i);
//			}
//		}
//		
//	};
}
