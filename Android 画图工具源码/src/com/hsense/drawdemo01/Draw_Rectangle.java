package com.hsense.drawdemo01;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

/*
 * ����
 * 
 * ˼·��
 * 1��������
 * 2�����������ϽǵĶ���ʼ������Ϊpoint1��
 * �������ϽǵĶ���ʼ������Ϊpoint2��
 * 3���ƶ��������ִ�в���2
 */
public class Draw_Rectangle extends MyDraw {

	// ���ε�4����������ĵ㣨�����ƶ���
	private Point point1, point2, point3, point4, cenPoint;
	private Rect point1Rect, point2Rect, point3Rect, point4Rect;
	Rect rect;

	
	
	
	
	public Draw_Rectangle(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		point1 = new Point();
		point2 = new Point();
		point3 = new Point();
		point4 = new Point();
		cenPoint = new Point();
		rect = new Rect();
		point1Rect = null;
		point2Rect = null;
		point3Rect = null;
		point4Rect = null;
	}
	
	
	

	// �����¼�
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			// ��ȡ�������
			downPoint.set((int) event.getX(), (int) event.getY());

			/*
			 * �ж��Ծ��ζ���point2Ϊ���ĵ�С�����Ƿ�Ϊ�գ�
			 * ΪʲôҪ�ж�point2Rect������point1Rect��
			 * ����û��ڵ�ǰҳ��ֻ���һ�£�Ҳ�����point1Rect���������point2Rect��
			 * �������ֻ��point1Rect��û��point2Rect�ж���û�������
			 * �����point2Rect != null����ǰҳ���Ѿ��л��õľ�����
			 */
			if (point2Rect != null) {
				//�ж��û�������ĵ��Ƿ��ھ��ζ���point1Ϊ���ĵľ���point1Rect�ڣ�
				//�������������ڣ���������Ϊ�û�����˸õ�
				if (point1Rect.contains(downPoint.x, downPoint.y)) {
					downState = 1;
					Log.i("onTouchDown", "downState = 1");
				} else if (point2Rect.contains(downPoint.x, downPoint.y)) {
					downState = 2;
					Log.i("onTouchDown", "downState = 2");
				} else if (point3Rect.contains(downPoint.x, downPoint.y)) {
					downState = 3;
					Log.i("onTouchDown", "downState = 3");
				} else if (point4Rect.contains(downPoint.x, downPoint.y)) {
					downState = 4;
					Log.i("onTouchDown", "downState = 4");
				} else if (rect.contains(downPoint.x, downPoint.y)) {
					downState = 5;
					Log.i("onTouchDown", "downState = 5");
				} else {
					downState = 0;
					Log.i("onTouchDown", "downState = 0");
				}
			}
			break;

			
			
		// �϶�
		case MotionEvent.ACTION_MOVE:
			// ��ȡ��ǰ�϶�������
			movePoint.set((int) event.getX(), (int) event.getY());

			// �����û������������㻭��Ӧ�ľ���
			switch (downState) {
			case 1:
				// �����point1����point2���䣻����point1��point2�������õ�point3,point4
				point1.set(movePoint.x, movePoint.y);
				point3.set(point1.x, point2.y);
				point4.set(point2.x, point1.y);
				break;
			case 2:
				// �����point2����point1���䣻����point1��point2�������õ�point3,point4
				point2.set(movePoint.x, movePoint.y);
				point3.set(point1.x, point2.y);
				point4.set(point2.x, point1.y);
				break;
			case 3:
				// �����point3�����������þ��ε�point3��1��2
				point3.set(movePoint.x, movePoint.y);
				point1.set(point3.x, point4.y);
				point2.set(point4.x, point3.y);
				break;
			case 4:
				// �����point4�����������þ��ε�point4��1��2
				point4.set(movePoint.x, movePoint.y);
				point1.set(point3.x, point4.y);
				point2.set(point4.x, point3.y);
				break;
			case 5:
				// ��������м��
				cenPoint.x = (point1.x + point2.x) / 2;
				cenPoint.y = (point1.y + point2.y) / 2;
				// �ƶ�����
				int movedX = movePoint.x - cenPoint.x;
				int movedY = movePoint.y - cenPoint.y;
				//�������þ���4�����������
				point1.x = point1.x + movedX;
				point1.y = point1.y + movedY;
				point2.x = point2.x + movedX;
				point2.y = point2.y + movedY;
				point3.set(point1.x, point2.y);
				point4.set(point2.x, point1.y);
				break;
			default:
				getStartPoint();
				break;
			}
			// ÿ���϶���֮����Ҫ�����趨4������С���Ρ�
			setRect();
			bitmap.eraseColor(Color.TRANSPARENT);
			//������
			canvas.drawRect(rect, paint);
			invalidate();
			break;
		}

		return true;
	}

	
	
	
	
	// �ж��û���ʼ�����ε�
	public void getStartPoint() {

		if (downPoint.x < movePoint.x && downPoint.y < movePoint.y) {
			point1.set(downPoint.x, downPoint.y);
			point2.set(movePoint.x, movePoint.y);
			point3.set(point1.x, point2.y);
			point4.set(point2.x, point1.y);
		} else if (downPoint.x < movePoint.x && downPoint.y > movePoint.y) {
			point3.set(downPoint.x, downPoint.y);
			point4.set(movePoint.x, movePoint.y);
			point1.set(point3.x, point4.y);
			point2.set(point4.x, point3.y);
		} else if (downPoint.x > movePoint.x && downPoint.y > movePoint.y) {
			point2.set(downPoint.x, downPoint.y);
			point1.set(movePoint.x, movePoint.y);
			point3.set(point1.x, point2.y);
			point4.set(point2.x, point1.y);
		} else if (downPoint.x > movePoint.x && downPoint.y < movePoint.y) {
			point4.set(downPoint.x, downPoint.y);
			point3.set(movePoint.x, movePoint.y);
			point1.set(point3.x, point4.y);
			point2.set(point4.x, point3.y);
		}

		setRect();

	}

	public void setRect() {
		// �����Ծ��ε�4������Ϊ���ĵ�С����
		point1Rect = new Rect(point1.x - 30, point1.y - 30, point1.x + 30,
				point1.y + 30);
		point2Rect = new Rect(point2.x - 30, point2.y - 30, point2.x + 30,
				point2.y + 30);
		point3Rect = new Rect(point3.x - 30, point3.y - 30, point3.x + 30,
				point3.y + 30);
		point4Rect = new Rect(point4.x - 30, point4.y - 30, point4.x + 30,
				point4.y + 30);

		rect.set(point1.x, point1.y, point2.x, point2.y);

	}

}
