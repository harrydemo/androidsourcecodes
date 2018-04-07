package com.chenqi.activity;

import com.chenqi.service.UserService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class XuexingActivity extends Activity
{		Spinner spinner;
		EditText tedianText;
		Button xuexingButton;
		String xuexingName;
		UserService userService;
		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.xuexing);
			userService=new UserService(this);
			tedianText=(EditText)this.findViewById(R.id.xuexingtedian);
			xuexingButton=(Button)this.findViewById(R.id.xuexingOK);
			xuexingButton.setOnClickListener(new buttonOnClickListener());
			 spinner = (Spinner) findViewById(R.id.xuexingspinner);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.xuexingarray, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					 xuexingName= arg0.getItemAtPosition(arg2).toString();
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0)
				{
					
				}
				
			});
		}
		private class buttonOnClickListener implements OnClickListener{

			@Override
			public void onClick(View arg0)
			{
				switch (((Button)arg0).getId())
				{
				case R.id.xuexingOK:
					//userService.insertXuexing();
				    String result=userService.findTedianByxuexingName(xuexingName);
				    tedianText.setText(result);
					break;

				default:
					break;
				}
			}
			
			
		}
}
