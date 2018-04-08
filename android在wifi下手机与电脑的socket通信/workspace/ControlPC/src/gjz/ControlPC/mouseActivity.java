package gjz.ControlPC;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.os.Bundle;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class mouseActivity extends Activity implements OnTouchListener, OnGestureListener  ,OnDoubleTapListener 
{
	private GestureDetector mGestureDetector;
	
	final int MOUSEEVENTF_CANCEL      =  0x0001; /* mouse move */
	final int MOUSEEVENTF_MOVE      =  0x0001; /* mouse move */
	
	final int MOUSEEVENTF_LEFTDOWN  =  0x0002; /* left button down */
	final int MOUSEEVENTF_LEFTUP    =  0x0003; /* left button up */
	final int MOUSEEVENTF_RIGHTDOWN =  0x0004; /* right button down */
	final int MOUSEEVENTF_RIGHTUP   =  0x0005; /* right button up */
	
	final int MOUSEEVENTF_TAP       =  0x0006; /* 单击 */
	final int MOUSEEVENTF_DOUBLETAP =  0x0007; /* 双击 */
	
	final int MOUSEEVENTF_ROLLUP    =  0x0008; /* 向上拖动滚动*/
	final int MOUSEEVENTF_ROLLDOWN  =  0x0009; /* 向下拖动滚动*/
	
	private TextView touchInof;
	private Button mouseRightButton, mouseLeftButton, rollUpButton, rollDownButton;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mouse_layout);
	        
	        touchInof = (TextView)findViewById(R.id.touchInfo);    
	        
	        mouseRightButton= (Button)findViewById(R.id.rightButton);
	        mouseRightButton.setOnClickListener(buttonClickListener);
	        mouseLeftButton= (Button)findViewById(R.id.leftButton);
	        mouseLeftButton.setOnClickListener(buttonClickListener);
	        
	        rollUpButton= (Button)findViewById(R.id.rollerButtonUp);
	        rollUpButton.setOnClickListener(buttonClickListener); 	        
	        rollUpButton.setOnLongClickListener(buttonlongClickListener);
	        rollUpButton.setLongClickable(true);
	        
	        rollDownButton= (Button)findViewById(R.id.rollerButtonDown);
	        rollDownButton.setOnClickListener(buttonClickListener);
	        rollDownButton.setLongClickable(true);
	        rollDownButton.setOnLongClickListener(buttonlongClickListener);
	        
	        mGestureDetector = new GestureDetector((OnGestureListener) this);     
	        RelativeLayout viewSnsLayout = (RelativeLayout)findViewById(R.id.touchLayout);     
	        viewSnsLayout.setOnTouchListener(this);     
	        viewSnsLayout.setLongClickable(true);
	    }
	 private OnClickListener buttonClickListener = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch(arg0.getId())
				{
				case R.id.rightButton://右击
					sendMessage(MOUSEEVENTF_RIGHTUP+"");
					break;
				case R.id.leftButton://单击
					sendMessage(MOUSEEVENTF_TAP+"");
					break;
					
				case R.id.rollerButtonUp://向上拖动滚动
					if(clickButton != 0)
					{
						clickButton = 0;
						mThread.interrupt();
						sendMessage("long click up");
					}
					else
						sendMessage(MOUSEEVENTF_ROLLUP+"");
					break;
					
				case R.id.rollerButtonDown://向下拖动滚动
					if(clickButton != 0)
					{
						clickButton = 0;
						mThread.interrupt();
						sendMessage("long click dwon");
					}
					else
						sendMessage(MOUSEEVENTF_ROLLDOWN+"");
					break;
				}
			}
	 };
	 private Thread mThread = null;
	 private int clickButton = 0;
	 private OnLongClickListener buttonlongClickListener =  new OnLongClickListener()
	 {
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			clickButton = v.getId();
			mThread = new Thread(mLongClickRunnable);
			mThread.start();	
			return false;
		}		 
	 };
	 
	private Runnable	mLongClickRunnable	= new Runnable() 
	{
			public void run()
			{
				while(clickButton!=0)
				{
					if(clickButton==R.id.rollerButtonDown)
						sendMessage(MOUSEEVENTF_ROLLDOWN+"");
					else if(clickButton==R.id.rollerButtonUp)
						sendMessage(MOUSEEVENTF_ROLLUP+"");
				}
			}
	};
		
	private void sendMessage(String msgText)
	{
			if ( ControlPCActivity.mPrintWriterClient!=null ) 
			{
					try 
					{				    	
						ControlPCActivity.mPrintWriterClient.print(msgText);//发送给服务器
						ControlPCActivity.mPrintWriterClient.flush();
					}
					catch (Exception e) 
					{
						// TODO: handle exception
						Toast.makeText(this, "发送异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
					}
			}
			else if ( ControlPCActivity.mPrintWriterServer!=null ) 
			{
				try 
				{				    	
					ControlPCActivity.mPrintWriterServer.print(msgText);//发送给服务器
					ControlPCActivity.mPrintWriterServer.flush();
				}
				catch (Exception e) 
				{
					// TODO: handle exception
					Toast.makeText(this, "发送异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
		}
	}

		private boolean isLongPress  = false;
		private float touchMoveX=0, touchMoveY=0;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			//return false;
			
			if(isLongPress)
			{
				if(event.getAction()==MotionEvent.ACTION_MOVE)
				{					
					if(touchMoveX==0)
					{
						touchMoveX = event.getX();
						touchMoveY = event.getY();
					}
					else
					{
						sendMessage(MOUSEEVENTF_MOVE+":" + (event.getX()-touchMoveX) + ";" + (event.getY()-touchMoveY));//左键弹起
						
						touchInof.setText("onTouch move: " + (event.getX()-touchMoveX) + " : " + (event.getY()-touchMoveY));
						touchMoveX = event.getX();
						touchMoveY = event.getY();
					}
				}
				else if(event.getAction()==MotionEvent.ACTION_UP)
				{
					touchInof.setText("onTouch up: " + event.getX() + " : " + event.getY());		
					isLongPress = false;
					sendMessage(MOUSEEVENTF_LEFTUP+"");//左键弹起
				}
				else
				{
					touchMoveX = 0;
					isLongPress = false;
					sendMessage(MOUSEEVENTF_CANCEL+"");//左键弹起					
				}
				return false;
			}			
			return mGestureDetector.onTouchEvent(event); 
		}

		@Override
		public boolean onDown(MotionEvent event) {
			// TODO Auto-generated method stub
			touchInof.setText("onDown: "+ event.getX() + " : " + event.getY());						
			return false;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			// 用户按下触摸屏、快速移动后松开,这个时候，你的手指运动是有加速度的。  
	        // 由1个MotionEvent ACTION_DOWN,    
	        // 多个ACTION_MOVE, 1个ACTION_UP触发    
	        // e1：第1个ACTION_DOWN MotionEvent    
	        // e2：最后一个ACTION_MOVE MotionEvent    
	        // velocityX：X轴上的移动速度，像素/秒    
	        // velocityY：Y轴上的移动速度，像素/秒 
			touchInof.setText("onFling:"
					+ "\n向量的起点:"+event1.getX() + " : " + event1.getY()
					+ "\n向量的终点:"+event2.getX() + " : " + event2.getY()
					+ "\n水平方向的速度:"+ velocityX
					+ "\n垂直方向的速度:"+ velocityY);
			
			sendMessage(MOUSEEVENTF_MOVE+":" + velocityX + ";" + velocityY);//左键弹起
			
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			touchInof.setText("onLongPress: \n"+e.getX() + " : " + e.getY());			
		}

		// 滑动时触发，e1为down时的MotionEvent，e2为move时的MotionEvent  
		@Override
		public boolean onScroll(MotionEvent event1, MotionEvent event2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			
			touchInof.setText("onScroll:"
					+ "\n向量的起点:"+event1.getX() + " : " + event1.getY()
					+ "\n向量的终点:"+event2.getX() + " : " + event2.getY()
					+ "\n水平方向的距离:"+ distanceX
					+ "\n垂直方向的距离:"+ distanceY);
			
			sendMessage(MOUSEEVENTF_MOVE+":" + distanceX + ";" + distanceY);//左键弹起
			
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			touchInof.setText("onShowPress: \n"+e.getX() + " : " + e.getY());
			isLongPress = true;
			sendMessage(MOUSEEVENTF_LEFTDOWN+"");//左键按下
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			touchInof.setText("onSingleTapUp: \n"+e.getX() + " : " + e.getY());
			return false;
		}

		// 第二次单击down时触发，e为第一次down时的MotionEvent  
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO Auto-generated method stub
			touchInof.setText("onDoubleTap: "+ e.getX() + " : " + e.getY());
			
			sendMessage(MOUSEEVENTF_DOUBLETAP+"");
			return false;
		}

		// 第二次单击down,move和up时都触发，e为不同时机下的MotionEvent  
		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			// TODO Auto-generated method stub
			touchInof.setText("onDoubleTapEvent: "+ e.getX() + " : " + e.getY());
			return false;
		}

		// 完成一次单击，并确定没有二击事件后触发（300ms），e为down时的MotionEvent  
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			touchInof.setText("onSingleTapConfirmed: "+ e.getX() + " : " + e.getY());
			
			sendMessage(MOUSEEVENTF_TAP+"");
			return false;
		}

}
