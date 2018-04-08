package com.org.opensky;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Select extends Activity implements View.OnClickListener {
	ImageView i;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_1);

		i = (ImageView) findViewById(R.id.image);
		i.setOnClickListener(this);
	}

	public void onClick(View v) {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		findViewById(R.id.image).startAnimation(shake);
	}

}