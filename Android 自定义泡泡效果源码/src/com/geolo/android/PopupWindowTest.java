package com.geolo.android;

import com.geolo.android.list.myListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ViewFlipper;

public class PopupWindowTest extends Activity {
	private PopupWindow pop;
	private LayoutInflater inflater ;
	private Button mButton,mButton2,mButton3;
	private ViewFlipper mViewFlipper,mViewFlipper2;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mButton = (Button)findViewById(R.id.button);
		mButton2 = (Button)findViewById(R.id.button02);
		mButton3 = (Button)findViewById(R.id.button03);
		inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.hello,null);
		pop = new PopupWindow(layout,480,350);
		mViewFlipper = (ViewFlipper)layout.findViewById(R.id.flipper);
		mViewFlipper2 = (ViewFlipper)layout.findViewById(R.id.flipper02);
		mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_left_in));
		mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_left_out));
		
		mViewFlipper2.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_left_in));
		mViewFlipper2.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_left_out));

		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.showAsDropDown(v); 
				mViewFlipper.startFlipping();
				mViewFlipper2.startFlipping();
			}
		});

		mButton2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 20, 20); 
				mViewFlipper.startFlipping();
				mViewFlipper2.startFlipping();
			}
		});

		mButton3.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PopupWindowTest.this, myListView.class);
				startActivity(intent);
			}
		});

		layout.findViewById(R.id.helloButton).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
			}

		});
	}
}