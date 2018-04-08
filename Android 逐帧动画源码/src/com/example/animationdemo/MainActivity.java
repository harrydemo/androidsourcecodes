package com.example.animationdemo;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView animationIV;
	private AnimationDrawable animationDrawable;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		animationIV = (ImageView) findViewById(R.id.animationIV);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonA:
			start_animation(R.anim.animation1);
			break;
		case R.id.buttonB:
			stop_animation();
			break;
		case R.id.buttonC:
			start_animation(R.anim.animation2);
			break;
		default:
			break;
		}
	}
	private void start_animation(int id){
		animationIV.setImageResource(id);
		animationDrawable = (AnimationDrawable) animationIV
				.getDrawable();
		animationDrawable.start();
	}
	private void stop_animation(){
		animationDrawable.stop();
	}
}
