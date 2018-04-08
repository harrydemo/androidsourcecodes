package com.lqf.shezhi;

import com.lqf.gerenriji.R;
import com.lqf.gerenriji.ZhuJieMian;
import com.lqf.riji.DuanXin;
import com.lqf.riji.XiaoHua;
import com.lqf.rili.RiLi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SheZhi extends Activity {
	//定义所需要的控件
	private Button guanyu, fankui, mimasuo, gongju, zhangben, xiaohua, duanxin;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shezhi);
		//设置字体样式
		Typeface typeface = Typeface.createFromAsset(getAssets(),"xingkai.ttf");
		//获取控件并绑定点击事件
		guanyu = (Button) findViewById(R.id.guanyu);
		guanyu.setOnClickListener(new MyButton());
		guanyu.getBackground().setAlpha(80);
		guanyu.setTypeface(typeface);//设置字体样式
		
		fankui = (Button) findViewById(R.id.fankui);
		fankui.setOnClickListener(new MyButton());
		fankui.getBackground().setAlpha(80);
		fankui.setTypeface(typeface);//设置字体样式
		
		mimasuo = (Button) findViewById(R.id.mimasuo);
		mimasuo.setOnClickListener(new MyButton());
		mimasuo.getBackground().setAlpha(80);
		mimasuo.setTypeface(typeface);//设置字体样式
		
		gongju = (Button) findViewById(R.id.gongju);
		gongju.setOnClickListener(new MyButton());
		gongju.getBackground().setAlpha(80);
		gongju.setTypeface(typeface);//设置字体样式
		
		zhangben = (Button) findViewById(R.id.zhangben);
		zhangben.setOnClickListener(new MyButton());
		zhangben.getBackground().setAlpha(80);
		zhangben.setTypeface(typeface);//设置字体样式
		
		xiaohua = (Button) findViewById(R.id.xiaohua);
		xiaohua.setOnClickListener(new MyButton());
		xiaohua.getBackground().setAlpha(80);
		xiaohua.setTypeface(typeface);//设置字体样式
		
		duanxin = (Button) findViewById(R.id.duanxin);
		duanxin.setOnClickListener(new MyButton());
		duanxin.getBackground().setAlpha(80);
		duanxin.setTypeface(typeface);//设置字体样式
	}

	// 点击所有Button按钮所触发的事件
	class MyButton implements OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.guanyu:
				Intent intent1 = new Intent(SheZhi.this,GuanYu.class);
				startActivity(intent1);
				break;
			case R.id.fankui:
				Intent intent2 = new Intent(SheZhi.this,FanKui.class);
				startActivity(intent2);
				break;
			case R.id.mimasuo:
				Intent intent3 = new Intent(SheZhi.this,MiMa.class);
				startActivity(intent3);
				break;
			case R.id.gongju:
				Intent intent4 = new Intent(SheZhi.this,GongJuXiang.class);
				startActivity(intent4);
				break;
			case R.id.zhangben:
				break;
			case R.id.xiaohua:
				Intent intent6 = new Intent(SheZhi.this,XiaoHua.class);
				startActivity(intent6);
				break;
			case R.id.duanxin:
				Intent intent7 = new Intent(SheZhi.this,DuanXin.class);
				startActivity(intent7);
				break;
			}
		}
	}
	//返回键动画效果
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				Intent intent = new Intent(SheZhi.this, ZhuJieMian.class);
				setResult(RESULT_OK, intent);
				finish();
				overridePendingTransition(R.anim.my_up, R.anim.my_down);
				return false;
			}
			return false;
		}
}
