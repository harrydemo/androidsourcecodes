package cn.com.karl.anim;
import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class PathButtonActivity extends Activity 
{
	private Button buttonCamera, buttonDelete, buttonWith, buttonPlace, buttonMusic, buttonThought, buttonSleep;
	private Animation animationTranslate, animationRotate, animationScale;
	private static int width, height;
	private LayoutParams params = new LayoutParams(0, 0);
	private static Boolean isClick = false;
	private TextView refresh;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.path_button);
        
        refresh=(TextView) this.findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(PathButtonActivity.this,MainActivity.class);
				startActivity(intent);
				//第一个参数为启动时动画效果，第二个参数为退出时动画效果
				overridePendingTransition(R.anim.fade, R.anim.hold);
			}
		});
        initialButton();
  
        
    }
	private void initialButton() 
	{
		// TODO Auto-generated method stub
		Display display = getWindowManager().getDefaultDisplay(); 
		height = display.getHeight();  
		width = display.getWidth();
		Log.v("width  & height is:", String.valueOf(width) + ", " + String.valueOf(height));
		
		params.height = 50;
		params.width = 50;
		//设置边距  (int left, int top, int right, int bottom)
		params.setMargins(10, height - 98, 0, 0);
		
		buttonSleep = (Button) findViewById(R.id.button_composer_sleep);	
		buttonSleep.setLayoutParams(params);
		
		buttonThought = (Button) findViewById(R.id.button_composer_thought);
		buttonThought.setLayoutParams(params);
		
		buttonMusic = (Button) findViewById(R.id.button_composer_music);
		buttonMusic.setLayoutParams(params);
		
		buttonPlace = (Button) findViewById(R.id.button_composer_place);
		buttonPlace.setLayoutParams(params);
		
		buttonWith = (Button) findViewById(R.id.button_composer_with);
		buttonWith.setLayoutParams(params);

		buttonCamera = (Button) findViewById(R.id.button_composer_camera);
		buttonCamera.setLayoutParams(params);
		
		buttonDelete = (Button) findViewById(R.id.button_friends_delete);		
		buttonDelete.setLayoutParams(params);
		
		buttonDelete.setOnClickListener(new OnClickListener() 
		{
				
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub					
				if(isClick == false)
				{
					isClick = true;
					buttonDelete.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));					
					buttonCamera.startAnimation(animTranslate(0.0f, -180.0f, 10, height - 240, buttonCamera, 80));
					buttonWith.startAnimation(animTranslate(30.0f, -150.0f, 60, height - 230, buttonWith, 100));
					buttonPlace.startAnimation(animTranslate(70.0f, -120.0f, 110, height - 210, buttonPlace, 120));
					buttonMusic.startAnimation(animTranslate(80.0f, -110.0f, 150, height - 180, buttonMusic, 140));
					buttonThought.startAnimation(animTranslate(90.0f, -60.0f, 175, height - 135, buttonThought, 160));
					buttonSleep.startAnimation(animTranslate(170.0f, -30.0f, 190, height - 90, buttonSleep, 180));
				
				}
				else
				{					
					isClick = false;
					buttonDelete.startAnimation(animRotate(90.0f, 0.5f, 0.45f));
					buttonCamera.startAnimation(animTranslate(0.0f, 140.0f, 10, height - 98, buttonCamera, 180));
					buttonWith.startAnimation(animTranslate(-50.0f, 130.0f, 10, height - 98, buttonWith, 160));
					buttonPlace.startAnimation(animTranslate(-100.0f, 110.0f, 10, height - 98, buttonPlace, 140));
					buttonMusic.startAnimation(animTranslate(-140.0f, 80.0f, 10, height - 98, buttonMusic, 120));
					buttonThought.startAnimation(animTranslate(-160.0f, 40.0f, 10, height - 98, buttonThought, 80));
					buttonSleep.startAnimation(animTranslate(-170.0f, 0.0f, 10, height - 98, buttonSleep, 50));
					
				}
					
			}
		});
		buttonCamera.setOnClickListener(new OnClickListener() 
		{
				
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub	
				buttonCamera.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));	
				buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonWith.setOnClickListener(new OnClickListener() 
		{
				
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub					
				buttonWith.startAnimation(setAnimScale(2.5f, 2.5f));	
				buttonCamera.startAnimation(setAnimScale(0.0f, 0.0f));	
				buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonPlace.setOnClickListener(new OnClickListener() 
		{
				
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub					
				buttonPlace.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));	
				buttonCamera.startAnimation(setAnimScale(0.0f, 0.0f));	
				buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonMusic.setOnClickListener(new OnClickListener() 
		{
				
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub					
				buttonMusic.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));	
				buttonCamera.startAnimation(setAnimScale(0.0f, 0.0f));	
				buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonThought.setOnClickListener(new OnClickListener() 
		{
				
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub					
				buttonThought.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));	
				buttonCamera.startAnimation(setAnimScale(0.0f, 0.0f));	
				buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonSleep.setOnClickListener(new OnClickListener() 
		{
				
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub					
				buttonSleep.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));	
				buttonCamera.startAnimation(setAnimScale(0.0f, 0.0f));	
				buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		
	}
	
	protected Animation setAnimScale(float toX, float toY) 
	{
		// TODO Auto-generated method stub
		animationScale = new ScaleAnimation(1f, toX, 1f, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.45f);
		animationScale.setInterpolator(PathButtonActivity.this, anim.accelerate_decelerate_interpolator);
		animationScale.setDuration(500);
		animationScale.setFillAfter(false);
		return animationScale;
		
	}
	
	protected Animation animRotate(float toDegrees, float pivotXValue, float pivotYValue) 
	{
		// TODO Auto-generated method stub
		animationRotate = new RotateAnimation(0, toDegrees, Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue);
		animationRotate.setAnimationListener(new AnimationListener() 
		{
			
			@Override
			public void onAnimationStart(Animation animation) 
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) 
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) 
			{
				// TODO Auto-generated method stub
				animationRotate.setFillAfter(true);
			}
		});
		return animationRotate;
	}
	//移动的动画效果        
	/* 
	 * TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) 
	 * 
	 * float fromXDelta:这个参数表示动画开始的点离当前View X坐标上的差值；
     *
　　       * float toXDelta, 这个参数表示动画结束的点离当前View X坐标上的差值；
     *
　　       * float fromYDelta, 这个参数表示动画开始的点离当前View Y坐标上的差值；
     *
　　       * float toYDelta)这个参数表示动画开始的点离当前View Y坐标上的差值；
	 */
	protected Animation animTranslate(float toX, float toY, final int lastX, final int lastY,
			final Button button, long durationMillis) 
	{
		// TODO Auto-generated method stub
		animationTranslate = new TranslateAnimation(0, toX, 0, toY);				
		animationTranslate.setAnimationListener(new AnimationListener()
		{
						
			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub
								
			}
						
			@Override
			public void onAnimationRepeat(Animation animation) 
			{
				// TODO Auto-generated method stub
							
			}
						
			@Override
			public void onAnimationEnd(Animation animation)
			{
				// TODO Auto-generated method stub
				params = new LayoutParams(0, 0);
				params.height = 50;
				params.width = 50;											
				params.setMargins(lastX, lastY, 0, 0);
				button.setLayoutParams(params);
				button.clearAnimation();
						
			}
		});																								
		animationTranslate.setDuration(durationMillis);
		return animationTranslate;
	}
	
  @Override
protected void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	finish();
}
		
}