package com.lqf.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 此类是子定义滑动屏幕的类 必须继承ViewGroup
 */
public class MyScrollLayout extends ViewGroup {
	// 定义滑动控制器
	Scroller scroller;
	// 定义速度控制器
	VelocityTracker velocity;
	// 定义当前屏幕位置
	int screenplace;
	// 定义最近的一个点X坐标
	float mLastx;
	// 定义滚动速率
	public static final int SNAP_VELOCITY = 600;

	// 重写构造函数
	public MyScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 初始化变量
		init(context);
	}

	public MyScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 初始化变量
		init(context);
	}

	public MyScrollLayout(Context context) {
		super(context);
		// 初始化变量
		init(context);
	}

	// 创建滑动控制器的上下文对象
	public void init(Context context) {
		// 创建滑动控制的对象
		scroller = new Scroller(context);
		// 获取当前屏幕位置为0
		screenplace = 0;
	}

	// 对内容的大小进行定义(protected受保护的)
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 获取内容的大小
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 得到子组件的数量
		int childCount = getChildCount();
		int width = MeasureSpec.getSize(widthMeasureSpec);
		// 使用for循环判断
		for (int i = 0; i < childCount; i++) {
			// 获取子组件
			View view = getChildAt(i);
			// 调用measure为子组件大小赋值
			view.measure(widthMeasureSpec, heightMeasureSpec);
		}
		// 初始化滑动位置，使其滑动到第一个界面
		scrollTo(screenplace * width, 0);
	}

	// 重写构造函数,对内容的布局进行定义
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// 使用if进行判断
		if (changed) {
			// 得到子组件的数量
			int childCount = getChildCount();
			// 定义子组件的宽度为0
			int childLeft = 0;
			// 使用for循环判断
			for (int i = 0; i < childCount; i++) {
				// 获取子组件
				View view = getChildAt(i);
				// 得到子组件的宽度
				int width = view.getMeasuredWidth();
				// 调用布局方法滚动布局的位置
				view.layout(childLeft, 0, childLeft + width,
						view.getMeasuredHeight());
				// 返回值
				childLeft += width;
			}
		}
	}

	// 定义子组件在父组件中滑动的方法
	public void computeScroll() {
		// 使用if判断动画是否停止
		if (scroller.computeScrollOffset()) {
			// 如果没有停止,将一直更新子控件的值
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			// 返回值
			postInvalidate();
		}
	}

	// 定义子控件被触摸时的操作方法
	public boolean onTouchEvent(MotionEvent event) {
		// 定义一个X变量
		float curX = event.getX();
		// 定义一个动作
		int action = event.getAction();
		// 使用switch循环判断操作时的动作
		switch (action) {
		// 按下操作时
		case MotionEvent.ACTION_DOWN:
			// 使用if判断速度是否为空
			if (velocity == null) {
				// 初始化速度控制
				velocity = VelocityTracker.obtain();
				// 将触屏事件交给速度控制
				velocity.addMovement(event);
				// 还没完成滑动动画 但是已碰到屏幕
			}
			if (!scroller.isFinished()) {
				// 停止动画
				scroller.abortAnimation();
			}
			// X坐标与最近X坐标相等
			mLastx = curX;
			break;
		// 移动操作时
		case MotionEvent.ACTION_MOVE:
			// 定义移动的X坐标位置变量
			int distance_x = (int) (mLastx - curX);
			// 使用if判断是否能移动
			if (IsCanMove(distance_x)) {
				// 使用if判断速度是否为空
				if (velocity != null) {
					// 初始化速度控制
					velocity = VelocityTracker.obtain();
					// 将触屏事件交给速度控制
					velocity.addMovement(event);
				}
				// X坐标与最近X坐标相等
				mLastx = curX;
				scrollBy(distance_x, 0);
			}
			break;
		// 抬起操作时
		case MotionEvent.ACTION_UP:
			// 定义速度滚动的X坐标为0
			int velocityX = 0;
			// 使用if判断速度是否为空
			if (velocity != null) {
				// 将触屏事件交给速度控制
				velocity.addMovement(event);
				// 计算当前的速度
				velocity.computeCurrentVelocity(1000);
				// 重新获取速率
				velocityX = (int) velocity.getXVelocity();
			}
			// 判断速率 并且 判断 当前屏幕位置 左右滚动
			if (velocityX > SNAP_VELOCITY && screenplace > 0) {
				//调用方法
				snapToScreen(screenplace - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& screenplace < getChildCount() - 1) {
				//调用方法
				snapToScreen(screenplace + 1);
			} else {
				//调用方法
				snapToDestination();
			}
			// 使用if判断速度是否为空
			if (velocity != null) {
				// 是循环反复利用
				velocity.recycle();
				velocity = null;
			}
			break;
		}
		// 返回值
		return true;
	}

	//抬起操作时屏幕的滚动方法
	public void snapToDestination() {
		//定义最终的横坐标变量
		final int screenWidth = getWidth();
		//计算方法
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		//获取值
		snapToScreen(destScreen);
	}

	//抬起操作时屏幕固定的位置方法
	public void snapToScreen(int whichScreen) {
		//得到哪个屏幕的X坐标
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		//判断
		if (getScrollX() != (whichScreen * getWidth())) {
			//
			final int delta = whichScreen * getWidth() - getScrollX();
			//
			scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
			//相等
			screenplace = whichScreen;
			//
			invalidate();

		}
	}

	// 定义移动操作时的方法
	private boolean IsCanMove(int distance_x) {
		// 滑动向右滑、如果distance_x小于0 并且 偏移量小于0了 则不能滑动了
		if (distance_x < 0 && getScrollX() < 0) {
			return false;
		}
		// 滑动向右滑、如果滑动大于组件0 并且 偏移量大于了所有组件的宽度和、 那么则返回false
		if (getScrollX() > (getChildCount() - 1) * getWidth() && distance_x > 0) {
			return false;
		}
		// 返回值
		return true;
	}
}
