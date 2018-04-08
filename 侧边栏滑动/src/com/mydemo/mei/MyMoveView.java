package com.mydemo.mei;


import com.mydemo.mei.util.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MyMoveView extends ViewGroup {

	private final static int TOUCH_STATE_REST = 0;

	private final static int TOUCH_STATE_MOVING = 1;

	private final static int MOVE_TO_LEFT = 1;

	private final static int MOVE_TO_RIGHT = 2;

	private final static int MOVE_TO_REST = 0;

	public final static int MAIN = 0;
	public final static int LEFT = 1;
	public final static int RIGHT = 2;

	private int touch_state = TOUCH_STATE_REST;

	private int move_state = MOVE_TO_REST;

	private int now_state = LEFT;

	private final float WIDTH_RATE = 0.83f;
	private BaseView main_show_view;
	private LeftMenuView left_show_view;
	private View firstView;
	private ImageView image_01;
	private ImageView image_02;
	private Context context;
	private int currentTab;//当前的activity

	private int min_distance = 200;
	private final int MOVE_TIME=10;//移动时间
	private int move_main= 90;//移动距离
	private int move_left = 40;//移动的最后距离

	private int screen_w;
	private int screen_h;

	private int move_x_v;
	
	private boolean isFaceRuturn = true;//是否界面返回
	private boolean isStarted = false;//是否刚启动,false:一进来显示左边,true,显示中间

	private boolean isAimationMoving = false;
	private boolean isMoveEnable = true;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			synchronized (MyMoveView.this) {
				isAimationMoving = true;
				int move_change = (int) (screen_w * WIDTH_RATE / 5);
				int left = main_show_view.getView().getLeft();
				if (msg.what == 1) {
					move(move_change + left);
				}
				if (msg.what == 11) {
					isAimationMoving = false;
					moveToLeft(false);
				}
				if (msg.what == 2) {
					move(-1 * move_change + left);
				}

				if (msg.what == 0) {
					if (now_state == LEFT) {
						move(-1 * move_x_v);
					} else {
						move(move_x_v);
					}
				}
				if (msg.what == 10) {
					isAimationMoving = false;
					moveToMain(false,0);
				}
			}
		}
	};

	public MyMoveView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public MyMoveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public MyMoveView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}
	
	/**
	 * 初始化移动布局
	 * @param main_show_view
	 * @param currentTab
	 */
	public void initView(){
		Display display =  ((Activity)context).getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int Height = display.getHeight();
		this.screen_w = width;
		this.screen_h = Height;
		this.move_main =Util.getWindowWidth(context)/5;
		this.move_left = Util.getWindowWidth(context)/10;
		this.setKeepScreenOn(true);
		this.min_distance = (int) (screen_w / 5.0);
	}
	/**
	 * 
	 * @param main_show_view
	 * @param currentTab
	 */
	public void setMainView(BaseView mainView,LeftMenuView leftView,int curTab){
		this.currentTab = curTab;
		if(this.left_show_view == null){
			this.left_show_view = leftView;
			this.addView(left_show_view.getView());
		}
		
		if(this.main_show_view == null){
			this.addView(mainView.view);
		}else{
			this.removeView(this.main_show_view.view);
			this.addView(mainView.view);
		}
		this.main_show_view = mainView;
		if(isStarted){
			moveToMain(isFaceRuturn,0);
		}else{
			isStarted=true;
		}
		
		if(!isFaceRuturn){
			isFaceRuturn = true;
		}
	}

	public void move(int start) {
		int left = main_show_view.getView().getLeft();
		if (now_state == MAIN) {
			if (left > 0) {
				if (move_state != MOVE_TO_LEFT) {
					move_state = MOVE_TO_LEFT;
				}
				left_show_view.getView().setVisibility(View.VISIBLE);
			} else if (left < 0) {
				if (move_state != MOVE_TO_RIGHT) {
					move_state = MOVE_TO_RIGHT;
				}
				left_show_view.getView().setVisibility(View.GONE);
			} else {
				move_state = MOVE_TO_REST;
			}
			main_show_view.getView().layout(start, 0, start + screen_w,
					screen_h);
		} else {
			left = (int) (screen_w * WIDTH_RATE);
			if (now_state == RIGHT) {
				left = -1 * left;
			}
			left = left + start;
			main_show_view.getView().layout(left, 0, left + screen_w, screen_h);
		}
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		if (move_state == MOVE_TO_REST) {
			if (now_state == MAIN) {
				int w = (int) (screen_w * WIDTH_RATE);
				main_show_view.getView().layout(0, 0, screen_w, screen_h);
				left_show_view.getView().layout(0, 0, w, screen_h);

			} else if (now_state == LEFT) {
				moveToLeft(false);
			} 
		}
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// ULog.d("msg", "" + widthMeasureSpec);

		main_show_view.view.measure(widthMeasureSpec, heightMeasureSpec);
		left_show_view.getView().measure(MeasureSpec.UNSPECIFIED,
				heightMeasureSpec);
		left_show_view.setWidth((int) (screen_w * WIDTH_RATE));
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	private int start_x;
	private int start_y;
	private boolean isMoved;

	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(!isMoveEnable){
			return false;
		}
		
		try{
			int action = ev.getAction();
			float x = ev.getX();
			float y = ev.getY();
			switch (action) {
			case MotionEvent.ACTION_DOWN:// 按下去
				
				
				super.dispatchTouchEvent(ev);
				start_y = (int) y;
				move_x_v = 0;
				if (this.touch_state == TOUCH_STATE_REST) {
					this.touch_state = TOUCH_STATE_MOVING;
					start_x = (int) x;
	
					isMoved = false;
					move_state = MOVE_TO_REST;
				}
				break;
			case MotionEvent.ACTION_MOVE:// 拖动时

				int last_y = (int) y;
				int last_x = (int) x;
				super.dispatchTouchEvent(ev);
				if (!isMoved) {
					if (Math.abs(last_y - start_y) > Math.abs(last_x - start_x)) {
						return super.onTouchEvent(ev);
						//return true;
					} else {
						/*if (Math.abs(last_x - start_x) > 10) {
							isMoved = true;
						}*/
						if(last_x - start_x > 10 || ( Math.abs(last_x - start_x) > 10 && now_state == LEFT ) ){
							isMoved=true;
						}
					}
				}
				
				if (isMoved) {
					
					if (this.touch_state == TOUCH_STATE_MOVING) {
						int move_x = last_x - start_x;
						if(move_x<0 && now_state==MAIN){
							return false;
						}
						if (Math.abs(last_x - start_x) > 10) {
							int left =main_show_view.view.getLeft();
							
							Log.d("msg", "left:" + left);
							Log.d("msg", "x:" + last_x);
							isMoved = true;
							
							Log.d("msg", "move_x:"+move_x+",now_state:"+now_state);
							if (move_x > 0 && now_state == LEFT) {
								isMoved = false;
								break;
							}
							main_show_view.setScroll(false);
							move(move_x);
						}
					}
					return false;
				}
				
				break;
			case MotionEvent.ACTION_UP:// 放开时
				main_show_view.setScroll(true);
				if (this.touch_state == TOUCH_STATE_MOVING) {
					if (isMoved) {
						last_x = (int) x;
						Log.d("msg", "UP:"+Math.abs(last_x - start_x)+",now:"+now_state);
						Log.d("msg", "now_state:"+now_state+",move_state:"+move_state);
						if (Math.abs(last_x - start_x) >= 50) {
							if (now_state == MAIN) {
								//if (move_state == MOVE_TO_LEFT) {
									Log.d("msg", "moveToLeft(true)1");
									this.moveToLeft(true);
								//}
							
							} else {
								int moveWidth = screen_w-Math.abs(main_show_view.view.getLeft());
								Log.d("msg", "moveToMain(true,"+moveWidth+")1");
								this.moveToMain(true,screen_w-Math.abs(main_show_view.view.getLeft()));
							}
						} else {
							if (now_state == MAIN) {
								this.moveToMain(false,0);
							}
							if (now_state == LEFT) {
								Log.d("msg", "moveToLeft(true)2");
								this.moveToLeft(true);
							}
						}
						/*if(now_state == LEFT){
							if (Math.abs(last_x - start_x) >= 10) {
								if (now_state == MAIN) {
									if (move_state == MOVE_TO_LEFT) {
										this.moveToLeft(true);
									}
								
								} else {
									this.moveToMain(true,screen_w-Math.abs(main_show_view.getLeft()));
								}
							} else {
								if (now_state == MAIN) {
									this.moveToMain(true,screen_w-Math.abs(main_show_view.getLeft()));
								}
								if (now_state == LEFT) {
									this.moveToLeft(true);
								}
							}
						}else{
							if (Math.abs(last_x - start_x) >= min_distance) {
								if (now_state == MAIN) {
									if (move_state == MOVE_TO_LEFT) {
										this.moveToLeft(true);
									}
								
								} else {
									this.moveToMain(true,Math.abs(main_show_view.getLeft()));
								}
							} else {
								if (now_state == MAIN) {
									this.moveToMain(true,Math.abs(main_show_view.getLeft()));
								}
								if (now_state == LEFT) {
									this.moveToLeft(true);
								}
							}
						}*/
						
						move_state = MOVE_TO_REST;
					} else {
						super.dispatchTouchEvent(ev);
						this.touch_state = TOUCH_STATE_REST;
						return false;// 为了响应listView的点击事件
					}
				}
				super.onTouchEvent(ev);
				this.touch_state = TOUCH_STATE_REST;
				break;
			}
			return true;
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			return super.onTouchEvent(ev);
		}catch(java.lang.IllegalArgumentException e){
			return super.onTouchEvent(ev);
		}catch(Exception e){
			return super.onTouchEvent(ev);
		}
	}

	public boolean getIsMoved() {
		return isMoved;
	}

	public void moveToLeft(boolean b) {
		
		if (!b) {
			int move_x = (int) (screen_w * WIDTH_RATE);
			left_show_view.getView().layout(0, 0, screen_w, screen_h);
			main_show_view.getView().layout(move_x, 0, move_x + screen_w,
					screen_h);
			
			now_state = LEFT;

		} else {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					int left = (int) (screen_w * WIDTH_RATE -main_show_view.view.getLeft());
					Log.d("msg", "moveToLeft-left:"+left);
					Message msg = new Message();
					if (left > move_left  ) {
						msg.what = 1;
						mHandler.sendMessage(msg);
						mHandler.postDelayed(this, MOVE_TIME);
					} else {
						msg.what = 11;
						mHandler.sendMessage(msg);
					}
				}
			}, 0);
		}
	}

	public void moveToMain(boolean b,int move) {
		if (!b) {
			left_show_view.getView().setVisibility(View.VISIBLE);
			int w = (int) (screen_w * WIDTH_RATE);
			main_show_view.getView().layout(0, 0, screen_w, screen_h);
			left_show_view.getView().layout(0, 0, w, screen_h);
			
			now_state = MAIN;
			
			
			
		} else {
			move_x_v = move;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					int left = Math.abs(main_show_view.view.getLeft()) - 0;
					Log.d("msg", "moveToMain-left:"+left+",move_x_v:"+move_x_v);
					Message msg = new Message();
					if (left > move_main) {
						msg.what = 0;
						move_x_v = move_x_v+move_main;
						now_state = LEFT;
						mHandler.sendMessage(msg);
						mHandler.postDelayed(this, MOVE_TIME);
					} else {
						msg.what = 10;
						mHandler.sendMessage(msg);
					}
				}
			}, 0);
		}
	}

	public int getNowState() {
		return this.now_state;
	}
	
	public void setNowState(int state){
		this.now_state = state;
	}

	public LeftMenuView getLeft_show_view() {
		return left_show_view;
	}
	
	public BaseView getMain_show_view() {
		return main_show_view;
	}

	/**
	 * 显示隐藏左边菜单
	 */
	public void showHideLeftMenu(){
		if (now_state == MyMoveView.MAIN) {
			moveToLeft(true);
		} else {
			moveToMain(true,0);
		}
	}

	public boolean isMoveEnable() {
		return isMoveEnable;
	}

	public void setMoveEnable(boolean isMoveEnable) {
		this.isMoveEnable = isMoveEnable;
	}

	public void setFaceRuturn(boolean isFaceRuturn) {
		this.isFaceRuturn = isFaceRuturn;
	}
	
}
