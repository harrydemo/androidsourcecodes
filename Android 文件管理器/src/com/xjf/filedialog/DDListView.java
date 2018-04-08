package com.xjf.filedialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
/**
 * ListView ��չ, ������, ������
 * */
public class DDListView extends ListView  {
	//private final static String tag = "FileDialog";

	private Context context;
	private FileManager fileManager;
	private android.view.WindowManager.LayoutParams mWindowParams;
	private TextView dragView;
	private Bitmap dragBitmap = null; // ҪDrag�����Bitmap
	private WindowManager mWindowManager;

	private View currentPosView = null;

	private DragListener dragListener = null;
	private DropListener dropListener = null;
	private DropOutListener dropOutListener = null;
	private StartDragListener startDragListener = null;

	private int dragItemFrom = -1; // ������Ŀ�����λ��
	private int dragCurPos = -1; // �϶�ʱ��ǰ����.
	private int itemHeight;
	private int downHeight;

	public int dragMinX = 0;
	public int dragMaxX = 0;
	
	private int touchX = 0;
	private int touchY = 0;
	//private int touchRawY = 0;
	
	private boolean waitMoveDrag = false;
	private boolean dragging = false;
	private final int sdkLevel = Build.VERSION.SDK_INT;
	private final boolean SDK8 = Build.VERSION.SDK_INT >= 8;
	private Method method = null;

	public DDListView(Context context) {
		super(context);
		this.context = context;
		this.fileManager = (FileManager) context;
		// TODO Auto-generated constructor stub
		init();
	}

	public DDListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.fileManager = (FileManager) context;
		// TODO Auto-generated constructor stub
		init();
	}

	public DDListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		this.fileManager = (FileManager) context;
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		initDragWindowParams();
		if (SDK8) {
			try {
				Class c = this.getClass();
				method = c.getMethod("smoothScrollToPosition", int.class);
				Class<?>[] cs = method.getParameterTypes();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				FileManager.error("SecurityException:\n" + e.getLocalizedMessage());
				method = null;
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				FileManager.error("NoSuchMethodException:\n" + e.getLocalizedMessage());
				method = null;
			}
		}
		//this.setDividerHeight(4);
	}
	public boolean isDragging() { return dragging;}

	private boolean downing = false;
	private int dragY = 0;
	private boolean doTask = false;
	private boolean timerRun = false;

	// ������ʵ�����¹���
	Timer timer = new Timer();

	class XTimerTask extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				timerRun = true;
				while (doTask) {
					ha.sendEmptyMessage(0);
					Thread.sleep(300);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				doTask = false;
				timerRun = false;
			}
		}

	};

	private void sendMessage() {
		if (timerRun)
			return;
			if (doTask == false)
				return;
			timer.cancel();
			timer = new Timer();
			timer.schedule(new XTimerTask(), 30);
	}

	Handler ha = new Handler() {
		public void handleMessage(Message msg) {
			if (!doTask)
				return;
			//int smoll = 0;
			currentPosView = getChildAt(dragCurPos - getFirstVisiblePosition());
			if (currentPosView != null)
				currentPosView.setBackgroundDrawable(null);
			if (downing) {
				if (dragCurPos < getCount()){
					dragCurPos++;
				} else {
					dragY = downHeight;
					doTask = false;
				}
			} else {
				if (dragCurPos > 0) {
					dragCurPos--;
				} else {
					dragY = 0;
					doTask = false;
				}
			}
			currentPosView = getChildAt(dragCurPos - getFirstVisiblePosition());
			if (currentPosView != null)
				currentPosView.setBackgroundResource(
						R.drawable.list_drag_background);
			//smoll = itemHeight * dragCurPos + dragY;
			//FileManager.dbg("smoll: " + smoll);
			//smoothScrollBy(smoll, 100);

			if (SDK8 && method != null)
				try {
					method.invoke(DDListView.this, dragCurPos);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					FileManager.error("IllegalArgumentException: " + e.getLocalizedMessage());
					method = null;
					setSelectionFromTop(dragCurPos, dragY);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					FileManager.error("IllegalAccessException: " + e.getLocalizedMessage());
					method = null;
					setSelectionFromTop(dragCurPos, dragY);
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					FileManager.error("InvocationTargetException: " + e.getLocalizedMessage());
					method = null;
					setSelectionFromTop(dragCurPos, dragY);
				}
			else
				setSelectionFromTop(dragCurPos, dragY);
			if (!doTask) {
				ha.removeMessages(0);
			}
		}
	};

	
	

	/**/
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		/**/
		if ((dragListener == null && dropListener == null))
			return super.onInterceptTouchEvent(event);
		waitMoveDrag = false;
		int act = event.getAction();

		switch (act) {
		case MotionEvent.ACTION_DOWN:
			// �ж��Ƿ������ҷ��λ��, ������listitem��ͼ��
			int x = (int) event.getX();
			int y = (int) event.getY();
			if (dragMinX < x && x < dragMaxX) {
				int itemNum = pointToPosition(x, y);
				if (itemNum == INVALID_POSITION)
					break;
				dragItemFrom = dragCurPos = itemNum;
				View item = (View) getChildAt(itemNum
						- getFirstVisiblePosition());
				if (item == null) {
					break;
				}
				dragging = true;
				itemHeight = item.getHeight();
				// item.setBackgroundColor(Color.BLUE);
				item.setDrawingCacheEnabled(true);
				Bitmap bm = Bitmap.createBitmap(item.getDrawingCache());
				startDrag(bm, (int) event.getRawX(), (int) event.getRawY());
				startDragListener.startDrag(itemNum);
				return false;
			}
			dragView = null;
			break;
		}
		/**
		if (dragListener == null && dropListener == null)
			return super.onInterceptTouchEvent(event);

		if (dragging){
			int itemNum = pointToPosition(0, touchY);
			if (itemNum == INVALID_POSITION) {
				dragging = false;
				return false;
			}
			dragItemFrom = dragCurPos = itemNum;
			return false;
		}
		touchX = (int) event.getRawX();
		touchRawY = (int) event.getRawY();
		touchY = (int) event.getY();
		/**
		int act = event.getAction();

		switch (act) {
		case MotionEvent.ACTION_DOWN:
			if (dragMinX < touchX && touchX < dragMaxX) {
				super.onInterceptTouchEvent(event);
				return false;
			}
		}
		/***/
		return super.onInterceptTouchEvent(event);
	}
	/***/
	
	public void startToDrag(int top){
		if (dragListener == null && dropListener == null)
			return;
		waitMoveDrag = true;
		dragging = true;
		onInterceptTouchEvent( MotionEvent.obtain(18583914, 18583914, 
				MotionEvent.ACTION_DOWN, touchX, touchY, 0));
	}
	
	public void clearDragBG() {
		if (currentPosView != null)
			currentPosView.setBackgroundDrawable(null);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (dragging) {
			doTask = false;
			int action = event.getAction();
			downHeight = getHeight() - itemHeight;
			switch (action) {
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				
				dragging = false;
				if (currentPosView != null)
					currentPosView.setBackgroundDrawable(null);
					//currentPosView.setBackgroundColor(0x00ffffff);
				
				stopDrag();
				if (dropOutListener != null && (
						event.getY() > DDListView.this.getHeight()
						|| dragCurPos == DDListView.INVALID_POSITION)){

					// ��û���ļ���λ�û򳬳�listView�ĵط�����.
					dropOutListener.dropOut(dragItemFrom, 
							(int)event.getX(), 
							(int)event.getY());
				} else if (dropListener != null && dragCurPos < getCount()
						&& dragCurPos >= 0) {
					dropListener.drop(dragItemFrom, dragCurPos);
				} 
				break;
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				int x = (int) event.getX();
				int y = (int) event.getY();
				/**
				if (waitMoveDrag) {

					waitMoveDrag = false;
					View item = (View) getChildAt(dragItemFrom
							- getFirstVisiblePosition());
					if (item == null) {
						break;
					}
					itemHeight = item.getHeight();
					// item.setBackgroundColor(Color.BLUE);
					item.setDrawingCacheEnabled(true);
					Bitmap bm = Bitmap.createBitmap(item.getDrawingCache());
					startDrag(bm, x, y);
					currentPosView = getChildAt(dragItemFrom - getFirstVisiblePosition());
					if (currentPosView != null)
						currentPosView.setBackgroundResource(
								R.drawable.list_drag_background);
					if (dragListener != null)
						dragListener.drag(dragCurPos, dragItemFrom, true);
				}
				/**/
				
				dragView(x, y);
				int itemnum = pointToPosition(x, y);
				if (itemnum == INVALID_POSITION) {
					dragCurPos = itemnum;
					break;
				}
				if (itemnum != dragCurPos && currentPosView != null) {
					currentPosView.setBackgroundDrawable(null);
					//currentPosView.setBackgroundColor(0x00ffffff);
				}
				currentPosView = getChildAt(itemnum - getFirstVisiblePosition());
				if (currentPosView != null)
					currentPosView.setBackgroundResource(
							R.drawable.list_drag_background);
				if ((action == MotionEvent.ACTION_DOWN || itemnum != dragCurPos)) {
					if (dragListener != null) {
						dragListener.drag(dragCurPos, itemnum,
								action == MotionEvent.ACTION_DOWN);
					}
					dragCurPos = itemnum;
				}

				/**
				 * �ڱ�Եʱ�������¹���
				 * */
				dragY = y;
				//if (!doTask) {
					if (y >= downHeight
							&& (!downing || !doTask)) {
						if (itemnum == getCount() - 1) {
							setSelectionFromTop(itemnum, downHeight);
							break;
						}
						downing = true;
						doTask = true;
						sendMessage();
					} else if (y <= itemHeight
									&& (downing || !doTask)) {
						if (itemnum == 0) {
							setSelectionFromTop(itemnum, 0);
							break;
						}
						downing = false;
						doTask = true;
						sendMessage();
					} else {
						doTask = false;
						ha.removeMessages(0);
					}
				//}
				break;
			}
			return true;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * ��ҷʱ,����ҷ���ļ�����ʾ����
	 * */
	private void initDragWindowParams() {
		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
		mWindowParams.alpha = 1.0f;
		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.windowAnimations = 0;
	}

	private void startDrag(Bitmap bm, int x, int y) {
		stopDrag();
		mWindowParams.x = x;
		mWindowParams.y = y;
		TextView v = new TextView(context);
		v.setBackgroundDrawable(new BitmapDrawable(bm));
		dragBitmap = bm;
		// �ڵ�ǰ�������View, ��������ҷ��ͼ��
		mWindowManager = (WindowManager) context.getSystemService("window");
		mWindowManager.addView(v, mWindowParams);

		dragView = v;

		// ��ҷ����ļ�ʱ, ��View�������ʾ�ļ���Ŀ
		if (fileManager.multFile) {
			dragView.setTextSize(35);
			dragView.setTextColor(Color.RED);
			dragView.setGravity(Gravity.FILL_HORIZONTAL);
			dragView.setText("" + fileManager.getCurrentSelectedCount());
		}
		
		//dragView.setBackgroundResource(R.drawable.list_drag_background);
	}

	private void stopDrag() {
		ha.removeMessages(0);
		doTask = false;
		if (dragView != null) {
			mWindowManager.removeView(dragView);
			dragView.setBackgroundDrawable(null);
			dragView = null;
		}
		if (dragBitmap != null) {
			if (!dragBitmap.isRecycled())
				dragBitmap.recycle();
			dragBitmap = null;
		}
	}

	private void dragView(int x, int y) {
		// Log.d(tag, "dragView: (" + x + "," + y + ")");
		mWindowParams.x = x;
		mWindowParams.y = y + (dragView.getHeight());
		mWindowManager.updateViewLayout(dragView, mWindowParams);
	}

	public void setDragListener(DragListener l) {
		dragListener = l;
	}

	public void setDropListener(DropListener l) {
		dropListener = l;
	}
	
	public void setDropOutListener(DropOutListener l){
		dropOutListener = l;
	}
	
	public void setStartDragListener(StartDragListener l){
		startDragListener = l;
	}

	public interface DragListener {
		/**
		 * Drag from {@code from} item to {@code to} item,
		 * 
		 * @param down
		 *            down action touch or not(move)
		 * */
		void drag(int from, int to, boolean down);
	}

	public interface DropListener {
		/**
		 * Drop from {@code from} item to {@code to} item,
		 * */
		void drop(int from, int to);
	}
	
	public interface DropOutListener {
		void dropOut(int from, int x, int y);
	}

	public interface StartDragListener {
		void startDrag(int from);
	}
}