package com.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationActivity extends Activity {

	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(new AnimationView(this));
	    }
	    
	    private static class AnimationView extends View {
	    	
	        private AnimateDrawable mDrawable;

	        public AnimationView(Context context) {
	            super(context);

	            Drawable pic = context.getResources().getDrawable
	                           (R.drawable.mountain);
	            
	            pic.setBounds(0, 0, pic.getIntrinsicWidth(), 
	  		                        pic.getIntrinsicHeight());
	            
	            Animation move = new TranslateAnimation(0, 150, 0, 300);
	            move.setDuration(5000);
	            move.setRepeatCount(-1);
	            move.initialize(0, 0, 0, 0);
	            
	            mDrawable = new AnimateDrawable(pic, move);
	            move.startNow();
	        }
	        
	        @Override protected void onDraw(Canvas canvas) {
	            canvas.drawColor(Color.GREEN);
	            mDrawable.draw(canvas);
	            invalidate();
	        }
	    }
}