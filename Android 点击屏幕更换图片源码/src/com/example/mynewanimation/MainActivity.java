package com.example.mynewanimation;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	
	LinearLayout layout;
	MyViewGroup myViewGroup;
	BackgroundView backgroundView;
	AnimationView animationView;
	Animation animation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        layout = (LinearLayout) this.findViewById(R.id.root);
        myViewGroup = MyViewGroup.getInstance(this);
        layout.addView(myViewGroup);
        
        backgroundView = (BackgroundView) myViewGroup.getChildAt(0);
        animationView = (AnimationView) myViewGroup.getChildAt(1);
    	animationView.setVisibility(View.INVISIBLE);
        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.myanim);
        animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				myViewGroup.switchBack();
			}
		});
        
        layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				animationView.setVisibility(View.VISIBLE);
				animationView.startAnimation(animation);
				animationView.setVisibility(View.INVISIBLE);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
