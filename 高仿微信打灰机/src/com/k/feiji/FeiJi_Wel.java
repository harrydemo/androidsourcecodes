package com.k.feiji;

import com.baidu.mobstat.StatService;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class FeiJi_Wel extends FeiJi_BaseAc {

	private ImageView _FeiJi_Wel_Animal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feiji_wel);

		_FeiJi_Wel_Animal = (ImageView) findViewById(R.id.feiji_wel_animal);

		_FeiJi_Wel_Animal.setBackgroundResource(R.drawable.feiji_back_1);

		Handle_Delay(0, 1000);
		Handle_Delay(1, 2000);
		Handle_Delay(2, 2500);

	}

	private void Handle_Delay(int chage, int time) {

		final int _Chage = chage;
		Handler _Animal_Handler = new Handler();
		_Animal_Handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (_Chage == 0) {
					_FeiJi_Wel_Animal
							.setBackgroundResource(R.drawable.feiji_back_2);
				} else if (_Chage == 1) {
					_FeiJi_Wel_Animal
							.setBackgroundResource(R.drawable.feiji_back_3);
				} else {
					Intent intent = new Intent(FeiJi_Wel.this, FeiJi_Menu.class);
					startActivity(intent);
					finish();
				}
			}
		}, time);
	}
	public void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
}
