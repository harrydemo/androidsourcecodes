package cn.android.browser;


import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.ViewFlipper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;

public class HelpTabAct extends TabActivity 
{
	
	private ViewFlipper flipper;
	private Context context;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        TabHost tabHost = getTabHost();
        
        LayoutInflater.from(this).inflate(R.layout.help, tabHost.getTabContentView(), true);

        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("帮助")
                .setContent(R.id.view1));
        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("设置")
                .setContent(new Intent(this, WebPreference.class)));
        
        context = this;
        flipper = (ViewFlipper)findViewById(R.id.vf_help);
        flipper.setLongClickable(true);
        flipper.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return detector.onTouchEvent(event);
			}
		});
    }
    
    private GestureDetector detector = new GestureDetector(new OnGestureListener() 
	{
		//手势识别函数，到此函数被系统回调时说明系统认为发生了手势事件，
        //我们可以做进一步判定。
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,	float velocityY) 
		{
			Intent intent = new Intent();
			intent.setClass(context, Main.class);
			if (e1.getX() - e2.getX() > 120) {
				flipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_out));
				startActivity(intent);
				return true;
			} 
			else if (e1.getX() - e2.getX() < -120) {
				flipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.push_right_out));
				startActivity(intent);
				return true;
			} 
			return false;
		}
	    
		@Override
		public boolean onSingleTapUp(MotionEvent e) {				
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent e) {
			
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}
	});   
}

