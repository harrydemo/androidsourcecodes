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

public class PeiduiActivity extends Activity
{		Spinner nanspinner,nvspinner;
		EditText resultEdit;
		Button OKButton;
		String nanxingzuo,nvxingzuo;
		UserService userService;
		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.peidui);
			userService=new UserService(this);
			resultEdit=(EditText)this.findViewById(R.id.result);
			OKButton=(Button)this.findViewById(R.id.peiduiOK);
			OKButton.setOnClickListener(new buttonOnClickListener());
			nanspinner = (Spinner) findViewById(R.id.nanxingzuospinner);
			nvspinner = (Spinner) findViewById(R.id.nvxingzuospinner);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.xingzuoarray, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			nanspinner.setAdapter(adapter);
			nvspinner.setAdapter(adapter);
			nanspinner.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					nanxingzuo=arg0.getItemAtPosition(arg2).toString();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0)
				{
					
				}
		});
			nvspinner.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					nvxingzuo=arg0.getItemAtPosition(arg2).toString();
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
				case R.id.peiduiOK:
					//userService.insertPeidui();
				    String result=userService.findTedianBytwoxingzuo(nanxingzuo, nvxingzuo);
				    resultEdit.setText(result);
					break;

				default:
					break;
				}
			}
			
			
		}
}
