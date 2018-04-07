package gl.test.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ToneView {
	  /**
	   * 亮度
	   * */
	
	private TextView mLight;
	private SeekBar mLightBar;
	
	private float mDensity;
	
	private static final int TEXT_WIDTH = 50;
	private ColorMatrix mAllMatrix;
	private ColorMatrix mLightMatix;
	
	private LinearLayout mParent;
	
	private float mLightnewssValue= 0F;
	
	private final int MIDDLE_VALUE = 127;
	
	
	/**
	 * 处理后的图片
	 * **/
	private Bitmap mBitmap;

	public ToneView(Context context) {
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public void setTag(int tag){
		mLightBar.setTag(tag);
	}


	private void init(Context context) {
		// TODO Auto-generated method stub
		
		mDensity = context.getResources().getDisplayMetrics().density;
		
		
		mLight = new TextView(context);
		mLight.setText("亮度");
		mLightBar = new SeekBar(context);
		mLightBar.setMax(255);
		mLightBar.setProgress(127);
		//mLightBar.setTag(1);
		
		
		LinearLayout light = new LinearLayout(context);
		light.setOrientation(LinearLayout.HORIZONTAL);
		light.setLayoutParams(new LinearLayout.LayoutParams(
				
				LinearLayout.LayoutParams.MATCH_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT));
		
		LinearLayout.LayoutParams textLayoutparams = new LinearLayout.LayoutParams(
				(int) (TEXT_WIDTH * mDensity),
				LinearLayout.LayoutParams.MATCH_PARENT);
		mLight.setGravity(Gravity.CENTER);
		light.addView(mLight, textLayoutparams);
		
		LinearLayout.LayoutParams seekLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		light.addView(mLightBar, seekLayoutParams);
		
		mParent =  new LinearLayout(context);
		mParent.setOrientation(LinearLayout.VERTICAL);
		mParent.setLayoutParams(new LinearLayout.LayoutParams(
				
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		mParent.addView(light);
		
		
		
		
		
	}

	public void setSaturationBarListener(OnSeekBarChangeListener l) {
		// TODO Auto-generated method stub
		mLightBar.setOnSeekBarChangeListener(l);
	}

	public View getParentView() {
		// TODO Auto-generated method stub
		return mParent;
	}

	
	
	/**
	 * 返回处理后的位图
	 * */
	 public Bitmap getBitmap (){
		return mBitmap;
		 
	 }
	 
	 
	 /**
	  * 对位图进行处理
	  * */
	 
	 public Bitmap handleImage(Bitmap bm,int flag){
		 
		 
		 Bitmap bmp =Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm.getConfig().ARGB_8888);
		 Canvas canvas = new Canvas(bmp);
		 Paint paint = new Paint();
		 paint.setAntiAlias(true);
		 if(null == mAllMatrix){
			 mAllMatrix = new ColorMatrix();
		 }
		 
		 if(null == mLightMatix){
			 mLightMatix = new ColorMatrix();
			 }
		 switch(flag){
		 case 1:
			 mLightMatix.reset();
			mLightMatix.setScale(mLightnewssValue, mLightnewssValue, mLightnewssValue, 1);
			
			 Log.d("may", "改变亮度");
			 break;
		 }
		 
		 mAllMatrix.reset();
		 mAllMatrix.postConcat(mLightMatix);
		 
		 
		 paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
		 canvas.drawBitmap(bm, 0, 0,paint);
		 mBitmap = bmp;
		return bmp;
		 
	 }


	public void setLight(int hue) {
		// TODO Auto-generated method stub
		mLightnewssValue = (float) (hue * 1.0D/MIDDLE_VALUE);
		
	}
	
	
}
