package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class IamgeTranslatActivity extends Activity {
	/** Called when the activity is first created. */

	public ImageView imageView;
	public ImageView imageView2;

	public Animation animation1;
	public Animation animation2;
	
	public TextView text;

	public boolean juage = true;

	public int images[] = new int[] { R.drawable.icon, R.drawable.expriment,
			R.drawable.changer, R.drawable.dataline, R.drawable.preffitication };

	public int count = 0;

	public Handler handler = new Handler();

	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			AnimationSet animationSet1 = new AnimationSet(true);
			AnimationSet animationSet2 = new AnimationSet(true);
			imageView2.setVisibility(0);
			TranslateAnimation ta = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					-1f, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 0f);
			ta.setDuration(2000);
			animationSet1.addAnimation(ta);
			animationSet1.setFillAfter(true);
			ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					0f, Animation.RELATIVE_TO_SELF, 0f);
			ta.setDuration(2000);
			animationSet2.addAnimation(ta);
			animationSet2.setFillAfter(true);
			//iamgeView 出去  imageView2 进来
			imageView.startAnimation(animationSet1);
			imageView2.startAnimation(animationSet2);
			imageView.setBackgroundResource(images[count % 5]);
			count++;
			imageView2.setBackgroundResource(images[count % 5]);
			
			text.setText(String.valueOf(count));
			if (juage)
				handler.postDelayed(runnable, 6000);
			Log.i("handler", "handler");
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		imageView = (ImageView) findViewById(R.id.imageView);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		text=(TextView)findViewById(R.id.text);
		text.setText(String.valueOf(count));
		//将iamgeView先隐藏，然后显示
		imageView2.setVisibility(4);
		handler.postDelayed(runnable, 2000);
	}

	public void onPause() {
		juage = false;
		super.onPause();
	}
}