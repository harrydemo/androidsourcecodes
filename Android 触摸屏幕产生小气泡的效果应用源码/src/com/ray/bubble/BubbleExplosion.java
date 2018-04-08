package com.ray.bubble;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.Contacts.ContactMethods;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class BubbleExplosion extends Activity {
	private FrameLayout fl;
	private ExplosionView exv1;
	private AnimationDrawable exa1;
	private ContactMethods contact;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                      WindowManager.LayoutParams. FLAG_FULLSCREEN);
        fl = new FrameLayout(this);
        fl.setBackgroundResource(R.drawable.bg);
        
        exv1 = new ExplosionView(this);
		exv1.setVisibility(View.INVISIBLE);
	    exv1.setBackgroundResource(R.anim.explosion);
	    exa1 = (AnimationDrawable)exv1.getBackground();
		fl.addView(exv1);
		fl.setOnTouchListener(new LayoutListener());
        setContentView(fl);
    }
    
    class ExplosionView extends ImageView{

		public ExplosionView(Context context) {
			super(context);
		}
		//handle the location of the explosion
		public void setLocation(int top,int left){
			this.setFrame(left, top, left+40, top+40);
		}	
    }
    
    class LayoutListener implements OnTouchListener{

		public boolean onTouch(View v, MotionEvent event) {
			//first u have to stop the animation,or if the animation
			//is starting ,u can start it again!
			exv1.setVisibility(View.INVISIBLE);
			exa1.stop();
			float x = event.getX();
			float y = event.getY();
			exv1.setLocation((int)y-20, (int)x-20);
			exv1.setVisibility(View.VISIBLE);
			exa1.start();
			return false;
		}
    	
    }
}