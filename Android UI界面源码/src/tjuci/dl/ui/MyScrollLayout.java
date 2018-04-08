package tjuci.dl.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * ������Ҫ�Ǻ��򻬶���Ļ����
 * 
 * @author dl
 * 
 */
public class MyScrollLayout extends ViewGroup {
	Scroller scroller;// ��������
	VelocityTracker velocity;// �ٶȿ���
	int mCurScreen;// ��ǰ��Ļλ��
	float mLastX;//�����һ��X����
	public static final int SNAP_VELOCITY = 600;
	
	public MyScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);// ��ʼ������
	}

	public MyScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);// ��ʼ������
	}

	public MyScrollLayout(Context context) {
		super(context);
		init(context);// ��ʼ������
	}

	public void init(Context context) {
		scroller = new Scroller(context);// �����������ƵĶ���
		mCurScreen = 0;// ��ǰ��ĻΪ0
	}

	/**
	 * �������ݴ�С���ж���
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int childCount = getChildCount();// �õ������������
		int width = MeasureSpec.getSize(widthMeasureSpec);
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);// �õ������
			childView.measure(widthMeasureSpec, heightMeasureSpec);// ����measureΪ�������С��ֵ
		}

		
		scrollTo(mCurScreen * width, 0);// ��ʼ������λ�ã�ʹ�们������һ������
	}

	/**
	 * �����ݵĲ��ֽ��ж���
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed){
			int childCount = getChildCount();//�õ������������
			int childLeft = 0;
			for(int i = 0; i < childCount; i ++){
				View childView = getChildAt(i);//�õ������
				int width = childView.getMeasuredWidth();//�õ�������Ŀ��
				childView.layout(childLeft, 0, childLeft + width, childView.getMeasuredHeight());
				
				childLeft += width;
			}
		}
	}
	
	@Override
	public void computeScroll() {//�������Ҫ�����������ʽ���ô˷���
		if(scroller.computeScrollOffset()){//������û��ֹͣʱ
			scrollTo(scroller.getCurrX(), scroller.getCurrY());//�������û��ֹͣ  ��ôһֱ������View��ֵ
			postInvalidate();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float curX = event.getX();
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN://���²���ʱ
			if(velocity == null ){
				velocity = VelocityTracker.obtain();//��ʼ���ٶȿ���
				velocity.addMovement(event);//�������¼������ٶȿ���
			}
			if(!scroller.isFinished()){//��û��ɻ�������  ������������Ļ
				scroller.abortAnimation();//ֹͣ����
			}
			mLastX = curX;
			break;

		case MotionEvent.ACTION_MOVE://�ƶ�����ʱ
			int distance_x = (int)(mLastX - curX);
			if(IsCanMove(distance_x)){//�ж��Ƿ����ƶ�
				if(velocity != null ){
					velocity.addMovement(event);//�������¼������ٶȿ���
				}
				mLastX = curX;
				scrollBy(distance_x, 0);
			}
			break;
		case MotionEvent.ACTION_UP://̧�����ʱ
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
		return true;//��Χtrue  ��˵��   ����ɲ���  ���������Ų�����
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
	 * @param distance_x �ƶ�����
	 * @return �Ƿ��ܹ��ƶ�
	 */
	public boolean IsCanMove(int distance_x){
		//�������һ������distance_xС��0 ���� ƫ����С��0�� ���ܻ�����
		if(distance_x < 0 && getScrollX() < 0){
			return false;
		}
		//�������һ�����������������0 ���� ƫ������������������Ŀ�Ⱥ͡� ��ô�򷵻�false
		if(getScrollX() > (getChildCount() - 1) * getWidth() && distance_x > 0){
			return false;
		}
		return true;
	}
}
