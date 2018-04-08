package wyf.wpf;	//声明包语句		



import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback{
	RunActivity father;					//RunActivity的引用
	WelcomeThread wt;//后台修改数据线程
	WelcomeDrawThread wdt;//后台重绘线程
	
	int status=-1;		//记录当前状态,0：背景渐显，1：卷轴渐出，2：字体渐出，3：显示菜单，4：待命，5：有按钮点下
	int alpha = 0;//记录背景图片的背景色
	int selectedIndex=-1;//选择的菜单选项，如0为开始/1为帮助/2退出
	Rect rectMenuStart = new Rect(40,380,104,444);//开始按钮矩形框
	Rect rectMenuHelp = new Rect(128,380,194,444);//帮助按钮矩形框
	Rect rectMenuQuit = new Rect(218,380,282,444);//退出按钮矩形框
	Rect rectSoundMenu = new Rect(94,445,226,475);//声音菜单
	//构造器，初始化主要成员变量
	public WelcomeView(RunActivity father) {
		super(father);
		this.father = father;
		getHolder().addCallback(this);
		wt = new WelcomeThread(this);
		wdt = new WelcomeDrawThread(this,getHolder());
		status = 0;
	}
	//方法：绘制屏幕
	public void doDraw(Canvas canvas){
		Paint paint = new Paint();	//创建画笔
		paint.setAlpha(alpha);		
		BitmapManager.drawWelcomePublic(0, canvas, 0, 0,paint);//画背景
	}
	
	
	
//方法：处理用户点击屏幕事件
	public boolean myTouchEvent(int x,int y){
		if(rectMenuStart.contains(x, y)){//点下开始按钮
			selectedIndex = 0;//设置按下的索引
			status = 5;//设置状态为5，即按下列按钮
		}
		else if(rectMenuHelp.contains(x, y)){//点下帮助按钮
			selectedIndex = 1;//设置按下的索引
			status = 5;//设置状态为5，即按下列按钮
		}
		else if(rectMenuQuit.contains(x, y)){//点下退出按钮
			selectedIndex = 2;//设置按下的索引
			status = 5;//设置状态为5，即按下列按钮
		}
		
		return true;
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {//重写接口方法
	}
	
	public void surfaceCreated(SurfaceHolder holder) {//重写surfaceCreated方法
		wdt.isDrawOn = true;
		if(!wdt.isAlive()){
			wdt.start();
		}
		wt.flag = true;
		if(!wt.isAlive()){
			wt.start();
		}		
		
	}

	
	public void surfaceDestroyed(SurfaceHolder holder) {//重写surfaceDestroyed方法
		wdt.isDrawOn = false;
	}
	
}