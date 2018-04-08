package com.chenqi.activity;

import com.chenqi.service.UserService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ShengxiaoActivity extends Activity
{
		EditText yearText;
		EditText tedianText;
		TextView resultView;
		Button OKBtn;
		Button cancelBtn;
		UserService userService;
		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.shengxiao);	
			yearText=(EditText)this.findViewById(R.id.birthdayYear);
			tedianText=(EditText)this.findViewById(R.id.shengxiaotedian);
			resultView=(TextView)this.findViewById(R.id.shengxiaoresult);
			OKBtn=(Button)this.findViewById(R.id.shengxiaoOK);
			cancelBtn=(Button)this.findViewById(R.id.shengxiaoCancel);
			OKBtn.setOnClickListener(new buttonOnClickListener());
			cancelBtn.setOnClickListener(new buttonOnClickListener());
			userService=new UserService(this);
		}
		private class buttonOnClickListener implements View.OnClickListener{

			@Override
			public void onClick(View arg0)
			{
				switch (((Button)arg0).getId())
				{
				case R.id.shengxiaoOK:
					String year=yearText.getText().toString();
					String result=getShengxiaoByYear(year);
					resultView.setText(result);
					//userService.insertShengxiao();
					String tedian=userService.findTedianByShengxiaoName(result);
					tedianText.setText(tedian);
					break;

				case R.id.shengxiaoCancel:
					finish();
					break;

				}
			}
		}
		private String getShengxiaoByYear(String year){
			
	            if (Integer.valueOf(year)< 15)
	            {
	               
	            	Toast.makeText(this, R.string.shengxiaowarning, Toast.LENGTH_LONG).show();
	                return null;
	            }
	            String shengxiao="";
	            switch ((Integer.valueOf(year.trim()) - 3) % 12)
	            {
	                case 1:
	                   shengxiao="Êó";
	                    break;
	                case 2:
	                    shengxiao = "Å£";
	                    break;
	                case 3:
	                    shengxiao = "»¢";
	                    break;
	                case 4:
	                    shengxiao = "ÍÃ";
	                    break;
	                case 5:
	                    shengxiao = "Áú";
	                    break;
	                case 6:
	                    shengxiao = "Éß";
	                    break;
	                case 7:
	                    shengxiao = "Âí";
	                    break;
	                case 8:
	                    shengxiao = "Ñò";
	                    break;
	                case 9:
	                    shengxiao = "ºï";
	                    break;
	                case 10:
	                    shengxiao = "¼¦";
	                    break;
	                case 11:
	                    shengxiao = "¹·";
	                    break;
	                case 0:
	                    shengxiao = "Öí";
	                    break;
	            }
	            return shengxiao;
		}
}
