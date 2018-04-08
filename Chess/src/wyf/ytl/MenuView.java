package wyf.ytl;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 * 该类是菜单界面
 *
 */
public class MenuView extends SurfaceView implements SurfaceHolder.Callback {
	ChessActivity activity;//总Activity的引用
	private TutorialThread thread;//刷帧的线程
	Bitmap startGame;//开始游戏图片
	Bitmap openSound;//打开声音图片
	Bitmap closeSound;//关闭声音的图片
	Bitmap help;//帮助的图片
	Bitmap exit;//退出游戏的图片 
	public MenuView(Context context,ChessActivity activity) {//构造器 
		super(context);
		this.activity = activity;//得到activity引用
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);//启动刷帧线程
        initBitmap();//初始化图片资源
	}
	public void initBitmap(){//初始化图片资源图片
		startGame = BitmapFactory.decodeResource(getResources(), R.drawable.startgame);//开始游戏按钮
		openSound = BitmapFactory.decodeResource(getResources(), R.drawable.opensound);//开始声音按钮
		closeSound = BitmapFactory.decodeResource(getResources(), R.drawable.closesound);//关闭声音按钮
		help = BitmapFactory.decodeResource(getResources(), R.drawable.help);//帮助按钮
		exit = BitmapFactory.decodeResource(getResources(), R.drawable.exit);//退出按钮
	}
	public void onDraw(Canvas canvas){//自己写的绘制方法
		canvas.drawColor(Color.BLACK);//清屏
		canvas.drawBitmap(startGame, 50, 50, null);//绘制图片
		if(activity.isSound){//放声音时，绘制关闭声音图片
			canvas.drawBitmap(closeSound, 50, 150, null);//绘制关闭声音
		}else{//没有放声音时绘制打开声音图片
			canvas.drawBitmap(openSound, 50, 150, null);//绘制开始声音
		}
		canvas.drawBitmap(help, 50, 250, null);//绘制帮助按钮
		canvas.drawBitmap(exit, 50, 350, null);//绘制退出按钮
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}
	public void surfaceCreated(SurfaceHolder holder) {//创建时启动刷帧
        this.thread.setFlag(true);//设置循环标志位
        this.thread.start();//启动线程
	}
	public void surfaceDestroyed(SurfaceHolder holder) {//摧毁时释放刷帧线程
        boolean retry = true;//循环标志位
        thread.setFlag(false);//设置循环标志位
        while (retry) {//循环
            try {
                thread.join();//等待线程结束
                retry = false;//停止循环
            }catch (InterruptedException e){}//不断地循环，直到刷帧线程结束
        }
	}
	public boolean onTouchEvent(MotionEvent event) {//屏幕监听
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(event.getX()>105 && event.getX()<220
					&&event.getY()>60 && event.getY()<95){//点击的是开始游戏
				activity.myHandler.sendEmptyMessage(2);
			}else if(event.getX()>105 && event.getX()<220
					&&event.getY()>160 && event.getY()<195){//点击的是声音按钮
				activity.isSound = !activity.isSound;//将声音开关取反
				if(!activity.isSound){//当没有放声音时
					if(activity.gamesound != null){//检查当前是否已经有声音正在播放
						if(activity.gamesound.isPlaying()){//当游戏声音正在播放时，
							activity.gamesound.pause();//停止声音的播放
						}	
					}
				}else{//当需要播放声音时
					if(activity.gamesound != null){//当gamesound不为空时
						if(!activity.gamesound.isPlaying()){//且当前声音没有在播放
							activity.gamesound.start();//则播放声音
						}	
					}
				}
			}else if(event.getX()>105 && event.getX()<220
					&&event.getY()>260 && event.getY()<295){//点击的是帮助按钮
				activity.myHandler.sendEmptyMessage(3);//向activity发送Hander消息通知切换View
			}else if(event.getX()>105 && event.getX()<220
					&&event.getY()>360 && event.getY()<395){//点击的是退出游戏
				System.exit(0);//直接退出游戏
			}
		}
		return super.onTouchEvent(event);
	}
	class TutorialThread extends Thread{//刷帧线程
		private int span = 500;//睡眠的毫秒数 
		private SurfaceHolder surfaceHolder;//SurfaceHolder的引用
		private MenuView menuView;//MenuView的引用
		private boolean flag = false;//循环标记位
        public TutorialThread(SurfaceHolder surfaceHolder, MenuView menuView) {//构造器
            this.surfaceHolder = surfaceHolder;//得到surfaceHolder引用
            this.menuView = menuView;//得到menuView引用
        }
        public void setFlag(boolean flag) {//设置循环标记位
        	this.flag = flag;
        }
		public void run() {//重写的run方法
			Canvas c;//画布
            while (this.flag) {//循环
                c = null;
                try {
                	// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {//同步锁
                    	menuView.onDraw(c);//调用绘制方法
                    }
                } finally {//使用finally保证下面代码一定被执行
                    if (c != null) {
                    	//更新屏幕显示内容
                        this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try{
                	Thread.sleep(span);//睡眠指定毫秒数
                }catch(Exception e){//捕获异常
                	e.printStackTrace();//有异常时打印异常堆栈信息
                }
            }
		}
	}
}