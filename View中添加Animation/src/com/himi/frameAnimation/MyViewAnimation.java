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
 *@AlphaAnimation ����͸���ȶ���Ч��
 *@ScaleAnimation ����ߴ���������Ч��
 *@TranslateAnimation ����ת��λ���ƶ�����Ч��
 *@RotateAnimation ����ת����ת����Ч��
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
		this.setFocusable(true);//ֻ�е���View��ý���ʱ�Ż����onKeyDown���� 
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);
		paint.setColor(Color.WHITE);
		canvas.drawText("Himi", x, 50, paint);//��ע1
		canvas.drawText("������� ����͸���ȶ���Ч��", 80, this.getHeight() - 80, paint);
		canvas.drawText("������� ����ߴ���������Ч��", 80, this.getHeight() - 60, paint);
		canvas.drawText("������� ����ת��λ���ƶ�����Ч��", 80, this.getHeight() - 40, paint);
		canvas.drawText("������� ����ת����ת����Ч��", 80, this.getHeight() - 20, paint);
		canvas.drawBitmap(bmp, this.getWidth() / 2 - bmp.getWidth() / 2, 
				this.getHeight() / 2 - bmp.getHeight() / 2, paint);
		x += 1;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {//����͸���ȶ���Ч��
			mAlphaAnimation = new AlphaAnimation(0.1f, 1.0f);
			//��һ������fromAlpha Ϊ������ʼʱ��͸����
			//�ڶ�������toAlpha Ϊ��������ʱ��͸����
			//ע�⣺ȡֵ��Χ[0-1];[��ȫ͸��-��ȫ��͸��]
			mAlphaAnimation.setDuration(3000);
			////����ʱ�����ʱ��Ϊ3000 ����=3��
			this.startAnimation(mAlphaAnimation);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {//����ߴ���������Ч��
			mScaleAnimation = new ScaleAnimation(0.0f, 2.0f, 1.5f, 1.5f, Animation
					.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.0f);
			//��һ������fromXΪ������ʼʱX�����ϵ������ߴ�
			//�ڶ�������toXΪ��������ʱX�����ϵ������ߴ�
			//����������fromYΪ������ʼʱY�����ϵ������ߴ�
			//���ĸ�����toY Ϊ��������ʱY �����ϵ������ߴ�
			//ע�⣺
			//0.0��ʾ������û��
			//1.0��ʾ����������
			//ֵС��1.0��ʾ����
			//ֵ����1.0��ʾ�Ŵ�
			//-----������1-4������������ʼͼ���С���䣬������ֹ��ʱ��ͼ�񱻷Ŵ�1.5��
			//���������pivotXType Ϊ������X ����������λ������
			//����������pivotXValue Ϊ��������������X ����Ŀ�ʼλ��
			//���߸�����pivotXType Ϊ������Y ����������λ������
			//�ڰ˸�����pivotYValue Ϊ��������������Y ����Ŀ�ʼλ��
			//��ʾ��λ������������,ÿ��Ч������Լ����Թ�~����͵����~
			//�Ͼ����ۿ���Ч��������ż������~
			//Animation.ABSOLUTE ��Animation.RELATIVE_TO_SELF��Animation.RELATIVE_TO_PARENT
			mScaleAnimation.setDuration(2000);
			this.startAnimation(mScaleAnimation);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {//����ת��λ���ƶ�����Ч��
			mTranslateAnimation = new TranslateAnimation(0, 100, 0, 100);
			//��һ������fromXDeltaΪ������ʼʱX�����ϵ��ƶ�λ��
			//�ڶ�������toXDeltaΪ��������ʱX�����ϵ��ƶ�λ��
			//����������fromYDeltaΪ������ʼʱY�����ϵ��ƶ�λ��
			//���ĸ�����toYDelta Ϊ��������ʱY �����ϵ��ƶ�λ��
			mTranslateAnimation.setDuration(2000);
			this.startAnimation(mTranslateAnimation);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {//����ת����ת����Ч��
			mRotateAnimation = new RotateAnimation(0.0f, 360.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			//��һ������fromDegreesΪ������ʼʱ����ת�Ƕ�
			//�ڶ�������toDegrees Ϊ������ת���ĽǶ�
			//����������pivotXType Ϊ������X ����������λ������
			//���ĸ�����pivotXValue Ϊ��������������X ����Ŀ�ʼλ��
			//���������pivotXType Ϊ������Y ����������λ������
			//����������pivotYValue Ϊ��������������Y ����Ŀ�ʼλ��
			mRotateAnimation.setDuration(3000);
			this.startAnimation(mRotateAnimation);
		}
		return super.onKeyDown(keyCode, event);
	}
}
