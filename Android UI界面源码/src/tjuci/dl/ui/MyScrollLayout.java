package tjuci.dl.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 此类主要是横向滑动屏幕的类
 * 
 * @author dl
 * 
 */
public class MyScrollLayout extends ViewGroup {
	Scroller scroller;// 滑动控制
	VelocityTracker velocity;// 速度控制
	int mCurScreen;// 当前屏幕位置
	float mLastX;//最近的一点X坐标
	public static final int SNAP_VELOCITY = 600;
	
	public MyScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);// 初始化变量
	}

	public MyScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);// 初始化变量
	}

	public MyScrollLayout(Context context) {
		super(context);
		init(context);// 初始化变量
	}

	public void init(Context context) {
		scroller = new Scroller(context);// 创建滑动控制的对象
		mCurScreen = 0;// 当前屏幕为0
	}

	/**
	 * 对子内容大小进行定义
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int childCount = getChildCount();// 得到子组件的数量
		int width = MeasureSpec.getSize(widthMeasureSpec);
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);// 得到子组件
			childView.measure(widthMeasureSpec, heightMeasureSpec);// 调用measure为子组件大小赋值
		}

		
		scrollTo(mCurScreen * width, 0);// 初始化滑动位置，使其滑动到第一个界面
	}

	/**
	 * 对内容的布局进行定义
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed){
			int childCount = getChildCount();//得到子组件的数量
			int childLeft = 0;
			for(int i = 0; i < childCount; i ++){
				View childView = getChildAt(i);//得到子组件
				int width = childView.getMeasuredWidth();//得到子组件的宽度
				childView.layout(childLeft, 0, childLeft + width, childView.getMeasuredHeight());
				
				childLeft += width;
			}
		}
	}
	
	@Override
	public void computeScroll() {//当父组件要求子组件滑动式调用此方法
		if(scroller.computeScrollOffset()){//当动画没有停止时
			scrollTo(scroller.getCurrX(), scroller.getCurrY());//如果动画没有停止  那么一直更新子View的值
			postInvalidate();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float curX = event.getX();
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN://按下操作时
			if(velocity == null ){
				velocity = VelocityTracker.obtain();//初始化速度控制
				velocity.addMovement(event);//将触屏事件交给速度控制
			}
			if(!scroller.isFinished()){//还没完成滑动动画  但是已碰到屏幕
				scroller.abortAnimation();//停止动画
			}
			mLastX = curX;
			break;

		case MotionEvent.ACTION_MOVE://移动操作时
			int distance_x = (int)(mLastX - curX);
			if(IsCanMove(distance_x)){//判断是否能移动
				if(velocity != null ){
					velocity.addMovement(event);//将触屏事件交给速度控制
				}
				mLastX = curX;
				scrollBy(distance_x, 0);
			}
			break;
		case MotionEvent.ACTION_UP://抬起操作时
			int velocityX = 0;
            if (velocity != null)
            {
            	velocity.addMovement(event); 
            	velocity.computeCurrentVelocity(1000);  
            	velocityX = (int) velocity.getXVelocity();
            }
                    
                
            if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {       
                // Fling enough to move left       
                snapToScreen(mCurScreen - 1);       
            } else if (velocityX < -SNAP_VELOCITY       
                    && mCurScreen < getChildCount() - 1) {       
                // Fling enough to move right       
                snapToScreen(mCurScreen + 1);       
            } else {       
                snapToDestination();       
            }      
            
           
            
            if (velocity != null) {       
            	velocity.recycle();       
            	velocity = null;       
            }       
            
            break;   
		}
		return true;//范围true  则说明   已完成操作  不用再扩张操作了
	}

	 public void snapToDestination() {    
	        final int screenWidth = getWidth();    

	        final int destScreen = (getScrollX()+ screenWidth/2)/screenWidth;    
	        snapToScreen(destScreen);    
	 }  
	
	 public void snapToScreen(int whichScreen) {    
	
	        // get the valid layout page    
	        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));    
	        if (getScrollX() != (whichScreen*getWidth())) {    
	                
	            final int delta = whichScreen*getWidth()-getScrollX();    
	        
	            scroller.startScroll(getScrollX(), 0,     
	                    delta, 0, Math.abs(delta)*2);

	            
	            mCurScreen = whichScreen;    
	            invalidate();       // Redraw the layout    
	            
	        }    
	    }    

	/**
	 * @param distance_x 移动距离
	 * @return 是否能够移动
	 */
	public boolean IsCanMove(int distance_x){
		//滑动向右滑、如果distance_x小于0 并且 偏移量小于0了 则不能滑动了
		if(distance_x < 0 && getScrollX() < 0){
			return false;
		}
		//滑动向右滑、如果滑动大于组件0 并且 偏移量大于了所有组件的宽度和、 那么则返回false
		if(getScrollX() > (getChildCount() - 1) * getWidth() && distance_x > 0){
			return false;
		}
		return true;
	}
}
