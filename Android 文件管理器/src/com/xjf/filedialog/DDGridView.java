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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.xjf.filedialog.DDListView.DragListener;
import com.xjf.filedialog.DDListView.DropListener;
import com.xjf.filedialog.DDListView.DropOutListener;
import com.xjf.filedialog.DDListView.StartDragListener;
/**
 * GridView 扩展,可拖拉,带弹性, DDListView的注释比较详细.
 * */
public class DDGridView extends GridView{

	public final static String tag = "FileDialog";
	
	private Context context;
	private FileManager fileManager;
	private android.view.WindowManager.LayoutParams mWindowParams;
	private TextView dragView;
	private Bitmap dragBitmap = null; // 要Drag的项的Bitmap
	private WindowManager mWindowManager;

	private View currentPosView = null;

	private DragListener dragListener = null;
	private DropListener dropListener = null;
	private DropOutListener dropOutListener = null;
	private StartDragListener startDragListener = null;

	private int dragItemFrom = -1; // 拉动的目标项的位置
	private int dragCurPos = -1; // 拖动时当前的项.

	public int dragMinX = 0;
	public int dragMaxX = 0;
	
	private boolean waitMoveDrag = false;
	private boolean dragging = false;
	private final int sdkLevel = Build.VERSION.SDK_INT;
	private final boolean SDK8 = Build.VERSION.SDK_INT >= 8;
	private Method method = null;	//smoothScrollToPosition 要求SDK LEVEL 8
	
	//private View parentView;

	private boolean downing = false;
	//private int dragY = 0;
	private boolean doTask = false;
	private boolean timerRun = false;
	//private int ROLL_HEIGHT;
	
	public DDGridView(Context context){
		super(context);
		init(context);
	}
	
	
	public DDGridView(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}
	
	public DDGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context){
		this.context = context;
		fileManager = (FileManager) context;
		initDragWindowParams();
		this.setSmoothScrollbarEnabled(true);
		if (SDK8) {
			try {
				Class c = this.getClass();
				method = c.getMethod("smoothScrollToPosition", int.class);
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
		//ROLL_HEIGHT = ROLL_HEIGHT_DP * (int)fileManager.getDensity();
	}
	
	public void setDragable(boolean b){ 
		synchronized (this) {
			waitMoveDrag = b;
		}
	}
	public boolean dragable() { 
		synchronized (this) {
			return waitMoveDrag;
		}
	}
	public boolean isDragging() { return dragging;}

	public void clearDragBG() {
		if (currentPosView != null) {
			currentPosView.setBackgroundDrawable(null);
		}
	}
	
	
	
	
	public void drag(int position) {
		dragItemFrom = dragCurPos = position;
		View item = (View) getChildAt(position - getFirstVisiblePosition());
		if (item == null) {
			
		}
		dragging = true;
		//itemHeight = item.getHeight();
		// item.setBackgroundColor(Color.BLUE);
		item.setDrawingCacheEnabled(true);
		//DDGridView.this.get
		//startDrag(bm, (int) event.getRawX(), (int) event.getRawY());
		startDragListener.startDrag(position);
	}
	/**/
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		/**/
		if (!waitMoveDrag || 
				(dragListener == null && dropListener == null)) {
			return super.onInterceptTouchEvent(event);
		}
		
		fileManager.clearClickTime();
		int act = event.getAction();
		waitMoveDrag = false;
		switch (act) {
		case MotionEvent.ACTION_DOWN:
			int x = (int) event.getX();
			int y = (int) event.getY();
			int itemNum = pointToPosition(x, y);
			if (itemNum == INVALID_POSITION)
				break;
			dragItemFrom = dragCurPos = itemNum;
			View item = (View) getChildAt(itemNum - getFirstVisiblePosition());
			if (item == null) {
				break;
			}
			dragging = true;
			//itemHeight = item.getHeight();
			// item.setBackgroundColor(Color.BLUE);
			item.setDrawingCacheEnabled(true);
			Bitmap bm = Bitmap.createBitmap(item.getDrawingCache());
			startDrag(bm, (int) event.getRawX(), (int) event.getRawY());
			startDragListener.startDrag(itemNum);
			return false;
		}
		/***/
		return super.onInterceptTouchEvent(event);
	}
	/***/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (dragging) {
			//doTask = false;
			int action = event.getAction();
			//downHeight = getHeight() - itemHeight;
			switch (action) {
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				
				dragging = false;
				if (currentPosView != null)
					currentPosView.setBackgroundDrawable(null);
					//currentPosView.setBackgroundColor(0x00ffffff);
				stopDrag();
				//Log.d(tag, "stop " + dragCurPos);
				if (dropOutListener != null && 
						(event.getY() < 0
						|| (event.getY() + getTop()) > getBottom())
						|| dragCurPos == DDGridView.INVALID_POSITION) {
					// 在没有文件的位置或超出listView的地方放手.
					//Log.d(tag, "out");
					dropOutListener.dropOut(dragItemFrom,
							(int) event.getX(), (int) event.getY());
				} else if (dropListener != null && dragCurPos < getCount()
						&& dragCurPos >= 0) {
					dropListener.drop(dragItemFrom, dragCurPos);
				}
				break;
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				int x = (int) event.getX();
				int y = (int) event.getY();
				
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
				 * 在边缘时可以上下滚动
				 * */
				//if (!doTask) {
					//dragY = y;
					int viewHeight = getHeight() / 6;
					if (y >= viewHeight * 5) {
						if (itemnum == getCount() - 1) {
							doTask = false;
							break;
						}
						downing = true;
						doTask = true;
						sendMessage();
						
					} else if (y <= viewHeight) {
						if (itemnum == 0) {
							//setSelectionFromTop(itemnum, 0);
							doTask = false;
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
				/**/
				break;
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	// 下面是实现上下滚动
	Timer timer = new Timer();;
	class XTimerTask extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				timerRun = true;
				while (doTask) {
					ha.sendEmptyMessage(0);
					Thread.sleep(450);
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
		timer.schedule(new XTimerTask(), 50);
	}
	Handler ha = new Handler() {
		public void handleMessage(Message msg) {
			if (!doTask)
				return;
			/**
			currentPosView = getChildAt(dragCurPos - getFirstVisiblePosition());
			if (currentPosView != null)
				currentPosView.setBackgroundDrawable(null);
				/**/
			int position;
			if (!downing) {
				position = getFirstVisiblePosition();
				if (position == 0) {
					doTask = false;
				} else {
					position -= 5;
				}
			} else {
				position = getLastVisiblePosition();
				if (position == getCount() - 1){
					doTask = false;
				} else {
					position += 5;
				}
			}
			if (SDK8 && method != null)
				try {
					method.invoke(DDGridView.this, position);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					FileManager.error("IllegalArgumentException: " + e.getLocalizedMessage()
							+ "\n" + e.getMessage());
					method = null;
					setSelection(position);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					FileManager.error("IllegalAccessException: " + e.getLocalizedMessage()
							+ "\n" + e.getMessage());
					method = null;
					setSelection(position);
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					FileManager.error("InvocationTargetException: " + e.getLocalizedMessage()
							+ "\n" + e.getMessage());
					method = null;
					setSelection(position);
				}
			else
				setSelection(position);
			if (!doTask)
				ha.removeMessages(0);
		}
	};
	
	
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

		mWindowManager = (WindowManager) context.getSystemService("window");
		mWindowManager.addView(v, mWindowParams);
		
		dragView = v;
		if (fileManager.multFile) {
			dragView.setTextSize(35);
			dragView.setTextColor(Color.RED);
			dragView.setGravity(Gravity.CENTER);
			dragView.setText("" + fileManager.getCurrentSelectedCount());
		} //else
			//dragView.setBackgroundResource(R.drawable.list_drag_background);
		
	}

	private void stopDrag() {
		//doTask = false;
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
	
	
	
	/**
	 * 
					int itemCount = getCount();
					Log.d(tag, "count: " + itemCount + " last: " + lastPos);
					if (lastPos != (itemCount - 1))
						return false;
					int la = lastPos - firstPos;
					Log.d(tag, "laLL: " + la);
					View lastView = getChildAt(la);

					Log.d(tag, "Last: " + "h: " + lastView.getHeight() + "  " + 
							"top: " + lastView.getTop() + " bottom: " + lastView.getBottom());
					Log.d(tag, "MeasuredHeight: " + lastView.getMeasuredHeight());
					Log.d(tag, "Grid Hieght: " + getHeight());
					Rect outRect = new Rect();
					if (outRect != null)
						Log.d(tag, "jj: " + (outRect.top - outRect.bottom));
					lastView.getHitRect(outRect);
	 * */

}
