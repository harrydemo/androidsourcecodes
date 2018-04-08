package com.ly.control;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import com.ly.control.R;
import com.ly.db.AddPlaceHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddPlaceActivity extends Activity {
	private Spinner sp;
	private ProgressDialog pd;
	private Button bt1,bt2;
	private TextView tv1,tv2,tv3;
	private EditText et1,et2;
	private String title,content,type,id;
	private String [] leibie ={"酒店","旅游景点","火车站" };
	ArrayList<String> list=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addplace);
		bt1=(Button)findViewById(R.id.Button01);
		bt2=(Button)findViewById(R.id.Button02);
		bt1.setOnClickListener(l);
		bt2.setOnClickListener(l);
		
		tv1=(TextView)findViewById(R.id.TextView01);
		tv2=(TextView)findViewById(R.id.TextView02);
		tv3=(TextView)findViewById(R.id.TextView03);
		
		et1=(EditText)findViewById(R.id.EditText01);
		et2=(EditText)findViewById(R.id.EditText02);
		
		 pd=new ProgressDialog(this);
		
		sp= (Spinner)findViewById(R.id.sp1);
		
		final   ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,leibie);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(aa);
	}
	private OnClickListener l=new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==bt1){
				Intent i=new Intent(AddPlaceActivity.this,PhotoActivity.class);
				startActivity(i);
			}
			if(v==bt2){
				title = et1.getText().toString();
				type = sp.getSelectedItem().toString();
				content = et2.getText().toString();
				id = getIntent().getStringExtra("result");
				AddPlaceHelper aph=new AddPlaceHelper(AddPlaceActivity.this);
				SQLiteDatabase sd=aph.getWritableDatabase();
				sd.execSQL("insert into "+AddPlaceHelper.table_name+" values(null,?,?,?,?)",new String[]{title,type,content,id});
				sd.close();
				
				Toast.makeText(AddPlaceActivity.this, "添加成功" ,Toast.LENGTH_LONG).show();
				
				Intent i=new Intent(AddPlaceActivity.this,LYAddMemoryActivity.class);
				i.putExtra("result", id);
				startActivity(i);
			}
		}};
}	

