package com.pocketdigi.animation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class main extends Activity {
    /** Called when the activity is first created. */
	TextView tv,tv2,tv3,tv4;
	Button bt3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button bt=(Button)findViewById(R.id.bt);
        tv=(TextView)findViewById(R.id.tv);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
        
        
        bt3=(Button)findViewById(R.id.bt3);
        bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(main.this,activity2.class);
				startActivity(intent);
				overridePendingTransition(R.anim.a2,R.anim.a1);
				//淡出淡入动画效果
			}
        	
        });
        bt3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Animation translate=AnimationUtils.loadAnimation(main.this, R.anim.translate);
		        Animation scale=AnimationUtils.loadAnimation(main.this, R.anim.scale);
		        Animation rotate=AnimationUtils.loadAnimation(main.this, R.anim.rotate);
		        Animation alpha=AnimationUtils.loadAnimation(main.this, R.anim.a1);
		        //载入XML文件成Animation对象
		        tv.startAnimation(translate);
		        tv2.startAnimation(scale);
		        tv3.startAnimation(alpha);
		        tv4.startAnimation(rotate);
		        //应用动画
		        
			}});
    }
}