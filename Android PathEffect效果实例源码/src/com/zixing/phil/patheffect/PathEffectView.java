package com.zixing.phil.patheffect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathDashPathEffect.Style;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.util.AttributeSet;
import android.view.View;

public class PathEffectView extends View{

	private Paint mPaint;
	private Paint starPaint;
	private Path mPath;
	private Path star;

	private float phase = 0;
	
	public PathEffectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PathEffectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public PathEffectView(Context context) {
		super(context);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		for(int i=0;i<9;i++){
			mPath.moveTo(0, 0+(i*50));
			mPath.lineTo(50, 30+(i*50));
			mPath.lineTo(100,0+(i*50));
			mPath.lineTo(150, 30+(i*50));
			mPath.lineTo(200, 0+(i*50));
			PathEffect pe = null;
			switch (i) {
			case 0:
				pe = new CornerPathEffect(10);
				break;
			case 1:
				pe = new DashPathEffect(new float[]{10f,3f}, phase);
				break;
			case 2:
				pe = new DiscretePathEffect(5f, 10f);
				break;
			case 3:
				pe = new PathDashPathEffect(star, 20, phase, Style.ROTATE);
				break;
			case 4:
				pe = new PathDashPathEffect(star, 20, phase, Style.MORPH);
				break;
			case 5:
				pe = new PathDashPathEffect(star, 20, phase, Style.TRANSLATE);
				break;
			case 6:
				PathEffect cpeOuter = new PathDashPathEffect(star, 20, phase, Style.MORPH);
				PathEffect cpeInner = new DiscretePathEffect(5f, 10f);
				pe = new ComposePathEffect(cpeOuter, cpeInner);
				break;
			case 7:
				PathEffect cpe1 = new PathDashPathEffect(star, 20, phase, Style.MORPH);
				PathEffect cpe2 = new DiscretePathEffect(5f, 10f);
				pe = new SumPathEffect(cpe1, cpe2);
				break;
			default:
				break;
			}
			mPaint.setPathEffect(pe);
			canvas.drawPath(mPath, mPaint);
			mPath.reset();
		}
		phase+=2;
		invalidate();
	}
	
	private void init(){
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2);
		mPaint.setColor(Color.RED);
		mPath = new Path();	
		starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		starPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		starPaint.setColor(Color.YELLOW);
		star(10);
	}
	
	private void star(float length){
		star = new Path();
		float dis1 = (float)((length/2)/Math.tan((54f/180)*Math.PI));
		float dis2 = (float)(length*Math.sin((72f/180)*Math.PI));
		float dis3 = (float)(length*Math.cos((72f/180)*Math.PI));
		star.moveTo(length/2, 0);
		star.lineTo(length/2-dis3, dis2);
		star.lineTo(length, dis1);
		star.lineTo(0, dis1);
		star.lineTo(length/2+dis3, dis2);
		star.lineTo(length/2, 0);
	}
	
}
