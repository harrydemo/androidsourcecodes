package com.animation.test;

import zhuojin.lession15.animation.frame.R;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	ImageView img1=null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        img1=(ImageView)findViewById(R.id.img1);
	}
    AnimationDrawable   anim=new AnimationDrawable();
	
	public  void startBtn(View v)
	{
      for(int i=0;i<8;i++)
      {
    	  anim.addFrame(this.getResources().getDrawable(R.drawable.walk1+i), 100);
      }
		anim.setOneShot(false);
		img1.setBackgroundDrawable(anim);
		if(anim.isRunning())
		{
			anim.stop();
		}else
		{
			anim.stop();
			anim.start();
		}
	}
}



