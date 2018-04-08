/**
 * 
 */
package com.himi.frameAnimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 *@author Himi
 *@AlphaAnimation 渐变透明度动画效果
 *@ScaleAnimation 渐变尺寸伸缩动画效果
 *@TranslateAnimation 画面转换位置移动动画效果
 *@RotateAnimation 画面转移旋转动画效果
 */
public class MyViewAnimation extends View {
	private Paint paint;
	private Bitmap bmp;
	private int x = 50;
	private Animation mAlphaAnimation;
	private Animation mScaleAnimation;
	private Animation mTranslateAnimation;
	private Animation mRotateAnimation;

	public MyViewAnimation(Context context) {
		super(context);
		paint = new Paint();
		paint.setAntiAlias(true);
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
		this.setFocusable(true);//只有当该View获得焦点时才会调用onKeyDown方法 
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);
		paint.setColor(Color.WHITE);
		canvas.drawText("Himi", x, 50, paint);//备注1
		canvas.drawText("方向键↑ 渐变透明度动画效果", 80, this.getHeight() - 80, paint);
		canvas.drawText("方向键↓ 渐变尺寸伸缩动画效果", 80, this.getHeight() - 60, paint);
		canvas.drawText("方向键← 画面转换位置移动动画效果", 80, this.getHeight() - 40, paint);
		canvas.drawText("方向键→ 画面转移旋转动画效果", 80, this.getHeight() - 20, paint);
		canvas.drawBitmap(bmp, this.getWidth() / 2 - bmp.getWidth() / 2, 
				this.getHeight() / 2 - bmp.getHeight() / 2, paint);
		x += 1;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {//渐变透明度动画效果
			mAlphaAnimation = new AlphaAnimation(0.1f, 1.0f);
			//第一个参数fromAlpha 为动画开始时候透明度
			//第二个参数toAlpha 为动画结束时候透明度
			//注意：取值范围[0-1];[完全透明-完全不透明]
			mAlphaAnimation.setDuration(3000);
			////设置时间持续时间为3000 毫秒=3秒
			this.startAnimation(mAlphaAnimation);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {//渐变尺寸伸缩动画效果
			mScaleAnimation = new ScaleAnimation(0.0f, 2.0f, 1.5f, 1.5f, Animation
					.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.0f);
			//第一个参数fromX为动画起始时X坐标上的伸缩尺寸
			//第二个参数toX为动画结束时X坐标上的伸缩尺寸
			//第三个参数fromY为动画起始时Y坐标上的伸缩尺寸
			//第四个参数toY 为动画结束时Y 坐标上的伸缩尺寸
			//注意：
			//0.0表示收缩到没有
			//1.0表示正常无伸缩
			//值小于1.0表示收缩
			//值大于1.0表示放大
			//-----我这里1-4参数表明是起始图像大小不变，动画终止的时候图像被放大1.5倍
			//第五个参数pivotXType 为动画在X 轴相对于物件位置类型
			//第六个参数pivotXValue 为动画相对于物件的X 坐标的开始位置
			//第七个参数pivotXType 为动画在Y 轴相对于物件位置类型
			//第八个参数pivotYValue 为动画相对于物件的Y 坐标的开始位置
			//提示：位置类型有三种,每种效果大家自己尝试哈~这里偷下懒~
			//毕竟亲眼看到效果的区别才记忆深刻~
			//Animation.ABSOLUTE 、Animation.RELATIVE_TO_SELF、Animation.RELATIVE_TO_PARENT
			mScaleAnimation.setDuration(2000);
			this.startAnimation(mScaleAnimation);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {//画面转换位置移动动画效果
			mTranslateAnimation = new TranslateAnimation(0, 100, 0, 100);
			//第一个参数fromXDelta为动画起始时X坐标上的移动位置
			//第二个参数toXDelta为动画结束时X坐标上的移动位置
			//第三个参数fromYDelta为动画起始时Y坐标上的移动位置
			//第四个参数toYDelta 为动画结束时Y 坐标上的移动位置
			mTranslateAnimation.setDuration(2000);
			this.startAnimation(mTranslateAnimation);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {//画面转移旋转动画效果
			mRotateAnimation = new RotateAnimation(0.0f, 360.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			//第一个参数fromDegrees为动画起始时的旋转角度
			//第二个参数toDegrees 为动画旋转到的角度
			//第三个参数pivotXType 为动画在X 轴相对于物件位置类型
			//第四个参数pivotXValue 为动画相对于物件的X 坐标的开始位置
			//第五个参数pivotXType 为动画在Y 轴相对于物件位置类型
			//第六个参数pivotYValue 为动画相对于物件的Y 坐标的开始位置
			mRotateAnimation.setDuration(3000);
			this.startAnimation(mRotateAnimation);
		}
		return super.onKeyDown(keyCode, event);
	}
}
