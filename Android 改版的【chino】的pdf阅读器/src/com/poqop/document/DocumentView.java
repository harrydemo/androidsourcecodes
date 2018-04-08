package com.poqop.document;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import com.poqop.document.events.ZoomListener;
import com.poqop.document.models.CurrentPageModel;
import com.poqop.document.models.DecodingProgressModel;
import com.poqop.document.models.ZoomModel;
import com.poqop.document.multitouch.MultiTouchZoom;

/**
 * 屏幕滚动
 * 处理用户翻页操作，比如滑动、点击上下左右键等。
 * 此时根据具体的用户操作计算需要显示哪几页，通知BaseViewerActivity去显示这些页
 * @author Administrator
 * 
 */
public class DocumentView extends View implements ZoomListener {
	final ZoomModel zoomModel;
	private final CurrentPageModel currentPageModel;
	DecodeService decodeService;
	private final HashMap<Integer, Page> pages = new HashMap<Integer, Page>();
	private boolean isInitialized = false;
	private int pageToGoTo;
	private float lastX;
	private float lastY;
	private VelocityTracker velocityTracker;
	private final Scroller scroller;
	DecodingProgressModel progressModel;
	private RectF viewRect;
	private boolean inZoom;
	private long lastDownEventTime;
	private static final int DOUBLE_TAP_TIME = 500;
	private MultiTouchZoom multiTouchZoom;

	private static final int MAX_VALUE = 3800;
	private static final float MULTIPLIER = 400.0f;

	static final int NONE = 0;  //空闲
	static final int DRAG = 1;  //拖动
	static final int ZOOM = 2;  //缩放
	int mode = NONE;

	private float oldDist;
	private PointF midPoint = new PointF();
	private boolean isZoom = false;

	
	private boolean dblclick = true;
	public DocumentView(Context context, final ZoomModel zoomModel,
			DecodingProgressModel progressModel,
			CurrentPageModel currentPageModel) {
		super(context);
		this.zoomModel = zoomModel;
		this.progressModel = progressModel;
		this.currentPageModel = currentPageModel;
		setKeepScreenOn(true);
		scroller = new Scroller(getContext());
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	public void setDecodeService(DecodeService decodeService) {
		this.decodeService = decodeService;
	}

	private void init() {
		if (isInitialized) {
			return;
		}
		final int width = decodeService.getEffectivePagesWidth();  //获取试图的宽
		final int height = decodeService.getEffectivePagesHeight();  //高 
		for (int i = 0; i < decodeService.getPageCount(); i++) {
			pages.put(i, new Page(this, i));
			pages.get(i).setAspectRatio(width, height);
		}
		isInitialized = true;
		invalidatePageSizes();
		goToPageImpl(pageToGoTo);
	}

	private void goToPageImpl(final int toPage) {
		scrollTo(0, pages.get(toPage).getTop());
	}

	/*
	 * 让视图更新
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		// bounds could be not updated
		post(new Runnable() {
			public void run() {
				currentPageModel.setCurrentPageIndex(getCurrentPage());
			}
		});
		if (inZoom) {
			return;
		}
		// on scrollChanged can be called from scrollTo just after new layout
		// applied so we should wait for relayout
		post(new Runnable() {
			public void run() {
				updatePageVisibility();
			}
		});
	}

	private void updatePageVisibility() {
		for (Page page : pages.values()) {
			page.updateVisibility();
		}
	}

	public void commitZoom() {
		for (Page page : pages.values()) {
			page.invalidate();
		}
		inZoom = false;
	}

	public void showDocument() {
		// use post to ensure that document view has width and height before
		// decoding begin
		post(new Runnable() {
			public void run() {
				init();
				updatePageVisibility();
			}
		});
	}

	public void goToPage(int toPage) {
		if (isInitialized) {
			goToPageImpl(toPage);
		} else {
			pageToGoTo = toPage;
		}
	}

	public int getCurrentPage() {
		for (Map.Entry<Integer, Page> entry : pages.entrySet()) {
			if (entry.getValue().isVisible()) {
				return entry.getKey();
			}
		}
		return 0;
	}

	/**
	 * 缩放功能的处理
	 */
	public void zoomChanged(float newZoom, float oldZoom) {
		inZoom = true;
		stopScroller();
		final float ratio = newZoom / oldZoom;
		invalidatePageSizes();
		scrollTo(
				(int) ((getScrollX() + getWidth() / 2) * ratio - getWidth() / 2),
				(int) ((getScrollY() + getHeight() / 2) * ratio - getHeight() / 2));
		postInvalidate();
	}

	/**
	 * 触摸屏事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);

		if (multiTouchZoom != null) {
			if (multiTouchZoom.onTouchEvent(ev)) {
				return true;
			}

			if (multiTouchZoom.isResetLastPointAfterZoom()) {
				setLastPosition(ev);
				multiTouchZoom.setResetLastPointAfterZoom(false);
			}
		}

		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();  //用obtain()函数来获得类的实例，开始计算速度
		}
		velocityTracker.addMovement(ev);  //这个方法是将motion event 添加到velocityTracker
		switch (ev.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mode = DRAG;
			/*
			 * 双击放大
			 * 用第一次点击和第二次点击的时间差来判断是否为双击
			 */
			if ((ev.getEventTime() - lastDownEventTime < DOUBLE_TAP_TIME) && dblclick) {
				setCurrentValue(getmagnifyCurrentValues() - (ev.getX() - lastX));
				dblclick=false;
        		mode = DRAG;
        		/*
        		 * 双击缩小
        		 */
				}else if((ev.getEventTime() - lastDownEventTime < DOUBLE_TAP_TIME) && !dblclick) {
					setCurrentValue(getReduceCurrentValues() - (ev.getX() - lastX));
	        		dblclick=true;  
            }else{
    			if (!scroller.isFinished()) {
    				scroller.abortAnimation();
    				zoomModel.commit();
    			}
    			else {
                    lastDownEventTime = ev.getEventTime();
                }            	
            }
			stopScroller();
			setLastPosition(ev);
			
			/**
			 * velocityTracker   用来追踪触摸事件（flinging事件和其他手势事件）的速率
			 * fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) 
			 * 方法进行控件滚动时各种位置坐标数值的计算
			 */
		case MotionEvent.ACTION_UP:
			velocityTracker.computeCurrentVelocity(1000); // 初始化速率
            scroller.fling(getScrollX(), getScrollY(), (int) -velocityTracker.getXVelocity(), 
            		(int) -velocityTracker.getYVelocity(), getLeftLimit(), getRightLimit(), getTopLimit(), getBottomLimit());
            velocityTracker.recycle();  //回收
            velocityTracker = null;	
            mode = DRAG;
			break;
		/**
		 * API原文是 A non-primary pointer has gone down. 翻译过来就是：非第一个点按下
		 */
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(ev);
			midPoint(midPoint, ev);
			isZoom = true;
			mode = ZOOM;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode=DRAG;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				scrollBy((int) (lastX - ev.getX()), (int) (lastY - ev.getY()));
				setLastPosition(ev);
				break;
			} else if (mode==ZOOM) {				
					float newDist = spacing(ev);
						/**
						 * 表示新的距离比两个手指刚触碰的距离大》》》放大 ( +10个像素用来延迟一下放大
						 */
						if (newDist > oldDist + 10) {
							oldDist = spacing(ev);			
							setCurrentValue(getCurrentValue() - (ev.getX() - lastX));
							lastX = ev.getX();	
						}
						/**
						 * 表示新的距离比两个手指刚触碰的距离小:>>>>缩小
						 */
						if (newDist + 20 < oldDist) {
							oldDist = spacing(ev);			
							setCurrentValue(getRedCurrentValues()
									- (ev.getX() - lastX));
							lastX = ev.getX();
						}
						break;
					}
				}	
		return true;
	}

	// 双击放大
	public float getmagnifyCurrentValues() {
		float mv = (zoomModel.getZoom() - 0.2f) * 2200;
		return mv;
	}
	//多点触摸放大
	public float getCurrentValue() {
		return (zoomModel.getZoom() - 0.5f) * 350f;
	}

	// 双击縮小
	public float getReduceCurrentValues() {
		return (zoomModel.getZoom() - 1.0f) *0.1f;
	}
	
	//多点触摸缩小
	public float getRedCurrentValues() {
		return (zoomModel.getZoom() - 1.0f) *350f;
	}
	
	public void setCurrentValue(float currentValue) {
		if (currentValue < 0.0)
			currentValue = 0.0f;
		if (currentValue > MAX_VALUE)
			currentValue = MAX_VALUE;
		final float zoom = 1.0f + currentValue / MULTIPLIER;
		zoomModel.setZoom(zoom);
	}

	/*
	 * 间隔
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}
	
	/*
	 * 得到中点
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/*
	 * 获取触摸的X，Y并赋值
	 */
	private void setLastPosition(MotionEvent ev) {
		lastX = ev.getX();
		lastY = ev.getY();
	}

	/**
	 * 按键事件处理，这里你按上下左右键，页面内容是可以上下左右移动的
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				lineByLineMoveTo(1);
				return true;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				lineByLineMoveTo(-1);
				return true;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				verticalDpadScroll(1);
				return true;
			case KeyEvent.KEYCODE_DPAD_UP:
				verticalDpadScroll(-1);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	private void verticalDpadScroll(int direction) {
		scroller.startScroll(getScrollX(), getScrollY(), 0, direction
				* getHeight() / 2);
		invalidate();
	}

	private void lineByLineMoveTo(int direction) {
		if (direction == 1 ? getScrollX() == getRightLimit()
				: getScrollX() == getLeftLimit()) {
			scroller.startScroll(getScrollX(), getScrollY(), direction
					* (getLeftLimit() - getRightLimit()), (int) (direction
					* pages.get(getCurrentPage()).bounds.height() / 50));
		} else {
			scroller.startScroll(getScrollX(), getScrollY(), direction
					* getWidth() / 2, 0);
		}
		invalidate();
	}

	private int getTopLimit() {
		return 0;
	}

	private int getLeftLimit() {
		return 0;
	}

	private int getBottomLimit() {
		return (int) pages.get(pages.size() - 1).bounds.bottom - getHeight();
	}

	private int getRightLimit() {
		return (int) (getWidth() * zoomModel.getZoom()) - getWidth();
	}

	/*
	 *  scrollTo是在移动到这个坐标时显示出的视图
	 * @see android.view.View#scrollTo(int, int)
	 */	 
	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(Math.min(Math.max(x, getLeftLimit()), getRightLimit()),
				Math.min(Math.max(y, getTopLimit()), getBottomLimit()));    //获得上下左右的坐标
		viewRect = null;
	}

	/*
	 * RectF 这个类包含一个矩形的四个单精度浮点坐标。矩形通过上下左右4个边的坐标来表示一个矩形
	 * RectF(int,a,int b,int c,int d);通过四个坐标，构造一个矩形。
	 */
	RectF getViewRect() {
		if (viewRect == null) {
			viewRect = new RectF(getScrollX(), getScrollY(), getScrollX()
					+ getWidth(), getScrollY() + getHeight());
		}
		return viewRect;
	}

	/*
	 * 计算滚动值。
	 * @see android.view.View#computeScroll()
	 */
	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Page page : pages.values()) {
			page.draw(canvas);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		float scrollScaleRatio = getScrollScaleRatio();
		invalidatePageSizes();
		invalidateScroll(scrollScaleRatio);
		commitZoom();
	}

	/**
	 * 定义每一页的大小
	 */
	void invalidatePageSizes() {
		if (!isInitialized) {
			return;
		}
		float heightAccum = 0;
		int width = getWidth();
		float zoom = zoomModel.getZoom();
		for (int i = 0; i < pages.size(); i++) {
			Page page = pages.get(i);
			float pageHeight = page.getPageHeight(width, zoom);
			page.setBounds(new RectF(0, heightAccum, width * zoom, heightAccum
					+ pageHeight));
			heightAccum += pageHeight;
		}
	}

	private void invalidateScroll(float ratio) {
		if (!isInitialized) {
			return;
		}
		stopScroller();
		final Page page = pages.get(0);
		if (page == null || page.bounds == null) {
			return;
		}
		scrollTo((int) (getScrollX() * ratio), (int) (getScrollY() * ratio));
	}

	private float getScrollScaleRatio() {
		final Page page = pages.get(0);
		if (page == null || page.bounds == null) {
			return 0;
		}
		final float v = zoomModel.getZoom();
		return getWidth() * v / page.bounds.width();
	}

	//停止动画 Scroller滚动到最终x与y位置时中止动画。
	private void stopScroller() {
		if (!scroller.isFinished()) {
			scroller.abortAnimation(); //中止动画
		}
	}

}
