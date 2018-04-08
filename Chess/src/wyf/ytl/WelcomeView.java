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
 * 欢迎界面
 * 通过WelcomeViewThread类改变坐标实现动画效果
 *
 */
public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback {
	ChessActivity activity;
	private TutorialThread thread;//刷帧的线程
	private WelcomeViewThread moveThread;//物件移动的线程
	Bitmap welcomebackage;//大背景
	Bitmap logo;
	Bitmap boy;//小孩的图片
	Bitmap oldboy;//老头的图片
	Bitmap bordbackground;//文字背景
	Bitmap logo2;
	Bitmap menu;//菜单按钮
	
	int logoX = -120;//初始化需要移动的图片的相应坐标
	int boyX = -100;
	int oldboyX = -120;
	int logo2X = 320;
	
	int bordbackgroundY = -100;//背景框的y坐标
	int menuY = 520;//菜单的y坐标
	public WelcomeView(Context context,ChessActivity activity) {//构造器 
		super(context);
		this.activity = activity;//得到activity引用
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);//初始化刷帧线程
        this.moveThread = new WelcomeViewThread(this);//初始化图片移动线程
        initBitmap();//初始化所以图片
	}
	public void initBitmap(){//初始化所以图片
		welcomebackage = BitmapFactory.decodeResource(getResources(), R.drawable.welcomebackage);
		logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		boy = BitmapFactory.decodeResource(getResources(), R.drawable.boy);
		oldboy = BitmapFactory.decodeResource(getResources(), R.drawable.oldboy);
		bordbackground = BitmapFactory.decodeResource(getResources(), R.drawable.bordbackground);
		logo2 = BitmapFactory.decodeResource(getResources(), R.drawable.logo2);
		menu = BitmapFactory.decodeResource(getResources(), R.drawable.menu);
	}
	public void onDraw(Canvas canvas){//自己写的绘制方法,并非重写的
		//画的内容是z轴的，后画的会覆盖前面画的
		canvas.drawColor(Color.BLACK);//清屏
		canvas.drawBitmap(welcomebackage, 0, 100, null);//绘制welcomebackage
		canvas.drawBitmap(logo, logoX, 110, null);//绘制logo
		canvas.drawBitmap(boy, boyX, 210, null);//绘制boy
		canvas.drawBitmap(oldboy, oldboyX, 270, null);//绘制oldboy
		canvas.drawBitmap(bordbackground, 150, bordbackgroundY, null);//绘制bordbackground
		canvas.drawBitmap(logo2, logo2X, 100, null);//绘制logo2
		canvas.drawBitmap(menu, 200, menuY, null);//绘制menu
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}
	public void surfaceCreated(SurfaceHolder holder) {//创建时启动相应进程
        this.thread.setFlag(true);//设置循环标志位
        this.thread.start();//启动线程
        
        this.moveThread.setFlag(true);//设置循环标志位
        this.moveThread.start();//启动线程
	}
	public void surfaceDestroyed(SurfaceHolder holder) {//摧毁时释放相应进程
        boolean retry = true;
        thread.setFlag(false);//设置循环标志位
        moveThread.setFlag(false);
        while (retry) {//循环
            try {
                thread.join();//等待线程结束
                moveThread.join();
                retry = false;//停止循环
            } 
            catch (InterruptedException e) {//不断地循环，直到刷帧线程结束
            }
        }
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {//屏幕监听
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(event.getX()>200 && event.getX()<200+menu.getWidth()
					&& event.getY()>355 && event.getY()<355+menu.getHeight()){//点击菜单按钮
				activity.myHandler.sendEmptyMessage(1);
			}
		}
		return super.onTouchEvent(event);
	}
	class TutorialThread extends Thread{//刷帧线程
		private int span = 100;//睡眠的毫秒数 
		private SurfaceHolder surfaceHolder;//SurfaceHolder引用
		private WelcomeView welcomeView;//WelcomeView引用
		private boolean flag = false;
        public TutorialThread(SurfaceHolder surfaceHolder, WelcomeView welcomeView) {//构造器
            this.surfaceHolder = surfaceHolder;//得到SurfaceHolder引用
            this.welcomeView = welcomeView;//得到WelcomeView引用
        }
        public void setFlag(boolean flag) {//设置循环标记位
        	this.flag = flag;
        }
		@Override
		public void run() {//重写的run方法
			Canvas c;//画布
            while (this.flag) {//循环
                c = null;
                try {
                	// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {//同步
                    	welcomeView.onDraw(c);//绘制
                    }
                } finally {//使用finally语句保证下面的代码一定会被执行
                    if (c != null) {
                    	//更新屏幕显示内容
                        this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try{
                	Thread.sleep(span);//睡眠指定毫秒数
                }
                catch(Exception e){//捕获异常
                	e.printStackTrace();//打印堆栈信息
                }
            }
		}
	}
}