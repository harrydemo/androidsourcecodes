package com.renzh.earthtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class EarthTestAty extends Activity{
	private ImageView ivEarth=null;
    private SeekBar skBar=null;
    private MyGLSurfaceView glView=null;
  private void pringMsg(String str){
	  System.out.println("EarthAty---->"+str);
  }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		glView=(MyGLSurfaceView) ((LinearLayout)findViewById(R.id.gl_lay)).getChildAt(0);
		pringMsg("glView="+glView);
		ivEarth=(ImageView) findViewById(R.id.iv_earth);
		ivEarth.setImageBitmap(BmpOper.decorateBmp(BmpOper.getBmpFromRaw(this, R.raw.earth)));
		
		skBar=(SeekBar) findViewById(R.id.skBar);
		skBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				pringMsg(""+(progress));
				
			}

			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				 
				glView.toRoatY(seekBar.getProgress(), 1.5f) ;
				glView.toRoatX(seekBar.getProgress(), 1.5f) ;
				pringMsg("stop ,start roatX"+seekBar.getProgress());
			}
			
		});
	}
}