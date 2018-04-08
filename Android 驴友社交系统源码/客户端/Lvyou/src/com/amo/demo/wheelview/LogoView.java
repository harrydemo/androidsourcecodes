/**
 *  Copyright 2011 ChinaSoft International Ltd. All rights reserved.
 */

package com.amo.demo.wheelview;




import com.ly.control.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>
 * Title: LogoView
 * </p>
 * <p>
 * Description: ������ʾ����Logo���Զ�����ͼ
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: ChinaSoft International Ltd.
 * </p>
 * 
 * @author etc
 * @version 1.0
 */
public class LogoView extends View {
	/**
	 * ��ǰͼƬ͸���ȣ�0Ϊ��͸����255Ϊ��͸��
	 * */
	private int alpha = 0;

	/**
	 * ���췽��
	 * */
	public LogoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ���췽��
	 * */
	public LogoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Ӧ�������LOGOͼ��
	 * */
	private Bitmap logo;

	/**
	 * �ػ���ͼ�ķ���
	 * 
	 * @param alpha
	 *            �����ػ�ʱ��LOGOͼƬ��͸����ȡֵ
	 * */
	public void repaint(int alpha) {
		this.alpha = alpha;
		//System.out.println("111111111111");
		// �׳��ػ��¼�
		invalidate();
		
	}

	/**
	 * ���췽��
	 * */
	public LogoView(Context context) {
		super(context);
		// ����Ӧ�����LOGOͼƬ
		logo = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.welcome2);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
        //System.out.println("22222222");
		// ����
		canvas.drawColor(Color.WHITE);
		// �������ʶ���
		Paint p = new Paint();
		// ���û��ʶ����͸����Ϊ��ǰ͸����ȡֵ
		p.setAlpha(alpha);
		// ����ͼ�����û��ʻ���Logoͼ��
		canvas.drawBitmap(logo, 0.0f, 0.0f, p);
	}

}
