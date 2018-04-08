package com.bus.shenyang.net;

import java.util.Calendar;

import com.bus.shenyang.R;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Detailed extends Activity  implements  UpdatePointsNotifier{
	private int flag;
	private boolean state=false;
	private int amount = 50;
	private String ststop;
	private TextView textview;
	private Button button1;
	private int m = 0;
	private int year;
	private int mouth;
	private int day;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detailed);
		AppConnect.getInstance("243988d2b20a7c04aa412689fe4c6a37", "WAPS", this);
		AppConnect.getInstance(this).getPoints(this);
		Bundle bundle = this.getIntent().getExtras();
		ststop = bundle.getString("connet");
		textview=(TextView)findViewById(R.id.textView1);
		textview.setText(ststop);
		button1=(Button)findViewById(R.id.button1);
		button1.setOnClickListener(new ButtonOnClickListener());
	
	}

	public class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			if (check(m)) {
				boolean bool;
				bool = time();
				System.out.println("bool=" + bool);
				{
					if (!bool) {
						int a=flag;
						System.out.println("flag1="+flag);
						if(a>=amount)
						{						
							AppConnect.getInstance(Detailed.this).spendPoints(amount, Detailed.this);
							System.out.println("flag2="+flag);
							state=true;
						}else{
							System.out.println("1111111111111111");
							AppConnect.getInstance(Detailed.this).showOffers(Detailed.this);
							String str = "您的积分余额不足50分不能使用相关功能,余额为"+flag+"请获取积分";
							Toast.makeText(Detailed.this
									, str, Toast.LENGTH_LONG).show();
							
						}
						
						
						if (state) {
							m = 1;
							SharedPreferences sp = Detailed.this
									.getSharedPreferences("sunfengqi",
											Context.MODE_PRIVATE);
							Editor edit = sp.edit();
							edit.putInt("age", m);

							edit.commit();
							System.out.println("SharedPreferences设置成功");
							Intent i = new Intent();
							i.setAction(Intent.ACTION_SEND);
							i.putExtra(Intent.EXTRA_TEXT, ststop);
							i.setType("text/*");
							startActivity(i);
					
						}
					} else {
						Intent i = new Intent();
						i.setAction(Intent.ACTION_SEND);
						i.putExtra(Intent.EXTRA_TEXT, ststop);
						i.setType("text/*");
						startActivity(i);
					}

				}

			} else {
				Intent i = new Intent();
				i.setAction(Intent.ACTION_SEND);
				i.putExtra(Intent.EXTRA_TEXT, ststop);
				i.setType("text/*");
				startActivity(i);
			}

			
			
		}

			private boolean time() {
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		mouth = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);
		System.out.println("year=" + year);
		System.out.println("mouth=" + mouth);
		System.out.println("day=" + day);
		if (year == 2012 && mouth == 3&& (day==21||day==20)) {
			return true;
		} else {
			return false;
		}

	}

		private boolean check(int pamtar) {
		SharedPreferences sp = Detailed.this.getSharedPreferences("sunfengqi",
				Context.MODE_PRIVATE); 
		int id = sp.getInt("age", pamtar);
		System.out.println("id" + id);
		if (id == 0) {
			return true;
		} else {
			return false;
		}
	}

	}

	@Override
	public void getUpdatePoints(String currencyName, int pointTotal) {
		flag=pointTotal;
		
	}

	@Override
	public void getUpdatePointsFailed(String error) {
		Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
		
	}

}
