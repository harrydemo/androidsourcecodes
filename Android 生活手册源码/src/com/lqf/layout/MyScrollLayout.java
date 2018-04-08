package com.lqf.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * �������Ӷ��廬����Ļ���� ����̳�ViewGroup
 */
public class MyScrollLayout extends ViewGroup {
	// ���廬��������
	Scroller scroller;
	// �����ٶȿ�����
	VelocityTracker velocity;
	// ���嵱ǰ��Ļλ��
	int screenplace;
	// ���������һ����X����
	float mLastx;
	// �����������
	public static final int SNAP_VELOCITY = 600;

	// ��д���캯��
	public MyScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// ��ʼ������
		init(context);
	}

	public MyScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// ��ʼ������
		init(context);
	}

	public MyScrollLayout(Context context) {
		super(context);
		// ��ʼ������
		init(context);
	}

	// ���������������������Ķ���
	public void init(Context context) {
		// �����������ƵĶ���
		scroller = new Scroller(context);
		// ��ȡ��ǰ��Ļλ��Ϊ0
		screenplace = 0;
	}

	// �����ݵĴ�С���ж���(protected�ܱ�����)
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// ��ȡ���ݵĴ�С
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// �õ������������
		int childCount = getChildCount();
		int width = MeasureSpec.getSize(widthMeasureSpec);
		// ʹ��forѭ���ж�
		for (int i = 0; i < childCount; i++) {
			// ��ȡ�����
			View view = getChildAt(i);
			// ����measureΪ�������С��ֵ
			view.measure(widthMeasureSpec, heightMeasureSpec);
		}
		// ��ʼ������λ�ã�ʹ�们������һ������
		scrollTo(screenplace * width, 0);
	}

	// ��д���캯��,�����ݵĲ��ֽ��ж���
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// ʹ��if�����ж�
		if (changed) {
			// �õ������������
			int childCount = getChildCount();
			// ����������Ŀ��Ϊ0
			int childLeft = 0;
			// ʹ��forѭ���ж�
			for (int i = 0; i < childCount; i++) {
				// ��ȡ�����
				View view = getChildAt(i);
				// �õ�������Ŀ��
				int width = view.getMeasuredWidth();
				// ���ò��ַ����������ֵ�λ��
				view.layout(childLeft, 0, childLeft + width,
						view.getMeasuredHeight());
				// ����ֵ
				childLeft += width;
			}
		}
	}

	// ����������ڸ�����л����ķ���
	public void computeScroll() {
		// ʹ��if�ж϶����Ƿ�ֹͣ
		if (scroller.computeScrollOffset()) {
			// ���û��ֹͣ,��һֱ�����ӿؼ���ֵ
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			// ����ֵ
			postInvalidate();
		}
	}

	// �����ӿؼ�������ʱ�Ĳ�������
	public boolean onTouchEvent(MotionEvent event) {
		// ����һ��X����
		float curX = event.getX();
		// ����һ������
		int action = event.getAction();
		// ʹ��switchѭ���жϲ���ʱ�Ķ���
		switch (action) {
		// ���²���ʱ
		case MotionEvent.ACTION_DOWN:
			// ʹ��if�ж��ٶ��Ƿ�Ϊ��
			if (velocity == null) {
				// ��ʼ���ٶȿ���
				velocity = VelocityTracker.obtain();
				// �������¼������ٶȿ���
				velocity.addMovement(event);
				// ��û��ɻ������� ������������Ļ
			}
			if (!scroller.isFinished()) {
				// ֹͣ����
				scroller.abortAnimation();
			}
			// X���������X�������
			mLastx = curX;
			break;
		// �ƶ�����ʱ
		case MotionEvent.ACTION_MOVE:
			// �����ƶ���X����λ�ñ���
			int distance_x = (int) (mLastx - curX);
			// ʹ��if�ж��Ƿ����ƶ�
			if (IsCanMove(distance_x)) {
				// ʹ��if�ж��ٶ��Ƿ�Ϊ��
				if (velocity != null) {
					// ��ʼ���ٶȿ���
					velocity = VelocityTracker.obtain();
					// �������¼������ٶȿ���
					velocity.addMovement(event);
				}
				// X���������X�������
				mLastx = curX;
				scrollBy(distance_x, 0);
			}
			break;
		// ̧�����ʱ
		case MotionEvent.ACTION_UP:
			// �����ٶȹ�����X����Ϊ0
			int velocityX = 0;
			// ʹ��if�ж��ٶ��Ƿ�Ϊ��
			if (velocity != null) {
				// �������¼������ٶȿ���
				velocity.addMovement(event);
				// ���㵱ǰ���ٶ�
				velocity.computeCurrentVelocity(1000);
				// ���»�ȡ����
				velocityX = (int) velocity.getXVelocity();
			}
			// �ж����� ���� �ж� ��ǰ��Ļλ�� ���ҹ���
			if (velocityX > SNAP_VELOCITY && screenplace > 0) {
				//���÷���
				snapToScreen(screenplace - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& screenplace < getChildCount() - 1) {
				//���÷���
				snapToScreen(screenplace + 1);
			} else {
				//���÷���
				snapToDestination();
			}
			// ʹ��if�ж��ٶ��Ƿ�Ϊ��
			if (velocity != null) {
				// ��ѭ����������
				velocity.recycle();
				velocity = null;
			}
			break;
		}
		// ����ֵ
		return true;
	}

	//̧�����ʱ��Ļ�Ĺ�������
	public void snapToDestination() {
		//�������յĺ��������
		final int screenWidth = getWidth();
		//���㷽��
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		//��ȡֵ
		snapToScreen(destScreen);
	}

	//̧�����ʱ��Ļ�̶���λ�÷���
	public void snapToScreen(int whichScreen) {
		//�õ��ĸ���Ļ��X����
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		//�ж�
		if (getScrollX() != (whichScreen * getWidth())) {
			//
			final int delta = whichScreen * getWidth() - getScrollX();
			//
			scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
			//���
			screenplace = whichScreen;
			//
			invalidate();

		}
	}

	// �����ƶ�����ʱ�ķ���
	private boolean IsCanMove(int distance_x) {
		// �������һ������distance_xС��0 ���� ƫ����С��0�� ���ܻ�����
		if (distance_x < 0 && getScrollX() < 0) {
			return false;
		}
		// �������һ�����������������0 ���� ƫ������������������Ŀ�Ⱥ͡� ��ô�򷵻�false
		if (getScrollX() > (getChildCount() - 1) * getWidth() && distance_x > 0) {
			return false;
		}
		// ����ֵ
		return true;
	}
}
