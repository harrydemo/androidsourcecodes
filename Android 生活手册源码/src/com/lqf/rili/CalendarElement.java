package com.lqf.rili;

import android.graphics.Canvas;



/*��
 * Ҫ���Ƶ������ֳ����ɿ�,��ÿһ�鶼��Ҫ��ͬ���Ľӿ�,�Ա�ͳһ��������.
 * ���,��Щ�鶼Ҫʵ��һ��CalendarElement�ӿڡ�
 * ��CalendarElement �ӿ�����һ��draw����
 * public void draw(Canvas canvas);
 */
public interface CalendarElement {
	//�����Ļ滭����
	public void draw(Canvas canvas);
}
