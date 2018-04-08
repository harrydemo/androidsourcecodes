package com.hll.ViewFlipperTest;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ViewFlipperTest extends Activity implements OnTouchListener,
		OnGestureListener, OnDoubleTapListener {
	private ViewFlipper mFlipper;
	GestureDetector mGestureDetector;
	private int mCurrentLayoutState;
	private static final int FLING_MIN_DISTANCE = 100;
	private static final int FLING_MIN_VELOCITY = 200;

	TextView counttv;
	Button buttonNext1 = null, 
		   buttonNext2 = null;
	
	ListView lv1 = null,
			 lv2 = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		findView();
		setListener();

	}

	public void findView() {
		mFlipper = (ViewFlipper) findViewById(R.id.details);
		// ע��һ����������ʶ�����
		mGestureDetector = new GestureDetector(this);
		mCurrentLayoutState = 0;
		// ������סViewFlipper,��������ʶ���϶�������
		mFlipper.setLongClickable(true);

		counttv = (TextView) findViewById(R.id.counttv);
		buttonNext1 = (Button) findViewById(R.id.Button_next1);
		buttonNext2 = (Button) findViewById(R.id.Button_next2);
		lv1 = (ListView) findViewById(R.id.list1);
//		lv2 = (ListView) findViewById(R.id.list2);
		
		lv1.setAdapter(new HgroupAdapter(this,null,0));
	}

	public void setListener() {
		// ��mFlipper����һ��listener
		mFlipper.setOnTouchListener(this);
		lv1.setOnTouchListener(this);
		counttv.setText("9");
		buttonNext1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mFlipper.showNext();
				counttv.setText("7");
			}
		});
		buttonNext2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mFlipper.showNext();
				counttv.setText("8");
			}

		});
	}

	/**
	 * �˷����ڱ�����δ�õ�������ָ����ת��ĳ��ҳ��
	 */
	public void switchLayoutStateTo(int switchTo) {
		while (mCurrentLayoutState != switchTo) {
			if (mCurrentLayoutState > switchTo) {
				mCurrentLayoutState--;
				mFlipper.setInAnimation(inFromLeftAnimation());
				mFlipper.setOutAnimation(outToRightAnimation());
				mFlipper.showPrevious();
			} else {
				mCurrentLayoutState++;
				mFlipper.setInAnimation(inFromRightAnimation());
				mFlipper.setOutAnimation(outToLeftAnimation());
				mFlipper.showNext();
			}
		}
		;
	}

	/** * ������Ҳ����Ķ���Ч�� * @return */
	protected Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(500);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	/** * ���������˳��Ķ���Ч�� * @return */
	protected Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	/** * �����������Ķ���Ч�� * @return */
	protected Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	/** * ������Ҳ��˳�ʱ�Ķ���Ч�� * @return */
	protected Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(500);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * * �û����´������������ƶ����ɿ�����������¼� * e1����1��ACTION_DOWN MotionEvent *
	 * e2�����һ��ACTION_MOVE MotionEvent * velocityX��X���ϵ��ƶ��ٶȣ�����/�� *
	 * velocityY��Y���ϵ��ƶ��ٶȣ�����/�� * �������� �� *
	 * X�������λ�ƴ���FLING_MIN_DISTANCE�����ƶ��ٶȴ���FLING_MIN_VELOCITY������/��
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			// ������໬����ʱ�� //����View������Ļʱ��ʹ�õĶ���
			mFlipper.setInAnimation(inFromRightAnimation());
			// ����View�˳���Ļʱ��ʹ�õĶ���
			mFlipper.setOutAnimation(outToLeftAnimation());
			mFlipper.showNext();
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {

			// �����Ҳ໬����ʱ��
			mFlipper.setInAnimation(inFromLeftAnimation());
			mFlipper.setOutAnimation(outToRightAnimation());
			mFlipper.showPrevious();
		}
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onTouch(View v, MotionEvent event) {
		// һ��Ҫ�������¼���������ʶ����ȥ�����Լ��������鷳�ģ�
		return mGestureDetector.onTouchEvent(event);
	}

	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}