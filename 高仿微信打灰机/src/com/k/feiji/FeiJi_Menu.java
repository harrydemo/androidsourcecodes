package com.k.feiji;

import com.baidu.mobstat.StatService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FeiJi_Menu extends FeiJi_BaseAc {

	private Button _FeiJi_Button_New, _FeiJi_Button_Score,
			_FeiJi_Button_Exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feiji_menu);
		Init();
	}

	private void Init() {
		// TODO Auto-generated method stub
		_FeiJi_Button_New = (Button) findViewById(R.id.feiji_bu_new);
		_FeiJi_Button_Score = (Button) findViewById(R.id.feiji_bu_score);
		_FeiJi_Button_Exit = (Button) findViewById(R.id.feiji_bu_exit);

		_FeiJi_Button_New.setOnClickListener(new OnClick());
		_FeiJi_Button_Score.setOnClickListener(new OnClick());
		_FeiJi_Button_Exit.setOnClickListener(new OnClick());
	}

	private class OnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.feiji_bu_new:
				Intent intent = new Intent(FeiJi_Menu.this, FeiJi_Main.class);
				startActivity(intent);
				finish();
				break;

			case R.id.feiji_bu_score:
				Intent i = new Intent(FeiJi_Menu.this, FeiJi_Score.class);
				startActivity(i);
				break;

			case R.id.feiji_bu_exit:
				finish();
				break;
			}
		}

	}
	
	public void onResume() {
		super.onResume();

		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();

		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onPause(this);
	}
}
