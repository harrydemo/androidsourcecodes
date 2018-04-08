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
 * ��Ļ����
 * �����û���ҳ���������绬��������������Ҽ��ȡ�
 * ��ʱ���ݾ�����û�����������Ҫ��ʾ�ļ�ҳ��֪ͨBaseViewerActivityȥ��ʾ��Щҳ
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

	static final int NONE = 0;  //����
	static final int DRAG = 1;  //�϶�
	static final int ZOOM = 2;  //����
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
		final int width = decodeService.getEffectivePagesWidth();  //��ȡ��ͼ�Ŀ�
		final int height = decodeService.getEffectivePagesHeight();  //�� 
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
	 * ����ͼ����
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
	 * ���Ź��ܵĴ���
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
	 * �������¼�
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
			velocityTracker = VelocityTracker.obtain();  //��obtain()������������ʵ������ʼ�����ٶ�
		}
		velocityTracker.addMovement(ev);  //��������ǽ�motion event ��ӵ�velocityTracker
		switch (ev.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mode = DRAG;
			/*
			 * ˫���Ŵ�
			 * �õ�һ�ε���͵ڶ��ε����ʱ������ж��Ƿ�Ϊ˫��
			 */
			if ((ev.getEventTime() - lastDownEventTime < DOUBLE_TAP_TIME) && dblclick) {
				setCurrentValue(getmagnifyCurrentValues() - (ev.getX() - lastX));
				dblclick=false;
        		mode = DRAG;
        		/*
        		 * ˫����С
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
			 * velocityTracker   ����׷�ٴ����¼���flinging�¼������������¼���������
			 * fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) 
			 * �������пؼ�����ʱ����λ��������ֵ�ļ���
			 */
		case MotionEvent.ACTION_UP:
			velocityTracker.computeCurrentVelocity(1000); // ��ʼ������
            scroller.fling(getScrollX(), getScrollY(), (int) -velocityTracker.getXVelocity(), 
            		(int) -velocityTracker.getYVelocity(), getLeftLimit(), getRightLimit(), getTopLimit(), getBottomLimit());
            velocityTracker.recycle();  //����
            velocityTracker = null;	
            mode = DRAG;
			break;
		/**
		 * APIԭ���� A non-primary pointer has gone down. ����������ǣ��ǵ�һ���㰴��
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
						 * ��ʾ�µľ����������ָ�մ����ľ���󡷡����Ŵ� ( +10�����������ӳ�һ�·Ŵ�
						 */
						if (newDist > oldDist + 10) {
							oldDist = spacing(ev);			
							setCurrentValue(getCurrentValue() - (ev.getX() - lastX));
							lastX = ev.getX();	
						}
						/**
						 * ��ʾ�µľ����������ָ�մ����ľ���С:>>>>��С
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

	// ˫���Ŵ�
	public float getmagnifyCurrentValues() {
		float mv = (zoomModel.getZoom() - 0.2f) * 2200;
		return mv;
	}
	//��㴥���Ŵ�
	public float getCurrentValue() {
		return (zoomModel.getZoom() - 0.5f) * 350f;
	}

	// ˫���sС
	public float getReduceCurrentValues() {
		return (zoomModel.getZoom() - 1.0f) *0.1f;
	}
	
	//��㴥����С
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
	 * ���
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}
	
	/*
	 * �õ��е�
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/*
	 * ��ȡ������X��Y����ֵ
	 */
	private void setLastPosition(MotionEvent ev) {
		lastX = ev.getX();
		lastY = ev.getY();
	}

	/**
	 * �����¼����������㰴�������Ҽ���ҳ�������ǿ������������ƶ���
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
	 *  scrollTo�����ƶ����������ʱ��ʾ������ͼ
	 * @see android.view.View#scrollTo(int, int)
	 */	 
	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(Math.min(Math.max(x, getLeftLimit()), getRightLimit()),
				Math.min(Math.max(y, getTopLimit()), getBottomLimit()));    //����������ҵ�����
		viewRect = null;
	}

	/*
	 * RectF ��������һ�����ε��ĸ������ȸ������ꡣ����ͨ����������4���ߵ���������ʾһ������
	 * RectF(int,a,int b,int c,int d);ͨ���ĸ����꣬����һ�����Ρ�
	 */
	RectF getViewRect() {
		if (viewRect == null) {
			viewRect = new RectF(getScrollX(), getScrollY(), getScrollX()
					+ getWidth(), getScrollY() + getHeight());
		}
		return viewRect;
	}

	/*
	 * �������ֵ��
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
	 * ����ÿһҳ�Ĵ�С
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

	//ֹͣ���� Scroller����������x��yλ��ʱ��ֹ������
	private void stopScroller() {
		if (!scroller.isFinished()) {
			scroller.abortAnimation(); //��ֹ����
		}
	}

}
