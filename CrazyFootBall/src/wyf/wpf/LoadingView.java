package wyf.wpf;							//声明包语句
import android.content.Context;				//引入相关类
import android.content.res.Resources;		//引入相关类
import android.graphics.Bitmap;				//引入相关类
import android.graphics.BitmapFactory;		//引入相关类
import android.graphics.Canvas;				//引入相关类
import android.graphics.Color;				//引入相关类
import android.graphics.Paint;				//引入相关类
import android.view.SurfaceHolder;			//引入相关类
import android.view.SurfaceView;			//引入相关类
/*
 * 该类继承自SurfaceView，主要的功能是在后台加载、创建对象时在前台 显示进度
 */
public class LoadingView extends SurfaceView implements SurfaceHolder.Callback{
	FootballActivity father;		//Activity的引用	
	Bitmap bmpProgress;				//显示进度时图片
	Bitmap [] bmpProgSign;			//进度条上的标志物
	Bitmap bmpLoad;					//进度条图片对象
	int progress=0;					//进度，0到100
	int progY = 330;				//进度条的Y坐标
	LoadingDrawThread lt;			//LoadingView的刷屏线程	
	public LoadingView(FootballActivity father) {//构造器，初始化主要成员变量
		super(father);			//调用父类构造器
		this.father = father;
		initBitmap(father);		//初始化图片
		getHolder().addCallback(this);	//添加Callback接口
		lt = new LoadingDrawThread(this,getHolder());//创建刷屏线程
	}	
	public void doDraw(Canvas canvas) {//方法：绘制屏幕
		canvas.drawColor(Color.BLACK);					//清屏幕		
		canvas.drawBitmap(bmpLoad, 10, 100, null);		//画加载时图片		
		canvas.drawBitmap(bmpProgress, 5, progY, null);		//画进度条图片		
		//画遮盖物
		Paint p = new Paint();							//创建画笔对象		
		p.setColor(Color.BLACK);						//设置画笔颜色
		int temp = (int)((progress/100.0)*320);			//将进度值换算成屏幕上的长度
		canvas.drawRect(temp, progY, 315, progY+20, p);			//画遮盖物挡住进度条图片
		//画进度条标志物
		for(int i=0;i<3;i++){
			canvas.drawBitmap(bmpProgSign[i], 140*i, progY-10, null);
		}
		if(progress == 100){				//绘制进度条已满的提示文字
			p.setTextSize(13.5f);
			p.setColor(Color.GREEN);
			canvas.drawText("单击屏幕开始游戏...", 100, progY+50, p);
		}else{								//绘制进度条未满的提示文字
			p.setTextSize(13.5f);
			p.setColor(Color.RED);
			canvas.drawText("加载中,请稍后....", 120, progY+50, p);	
		}
	}	
	public void initBitmap(Context context){//方法：初始化图片
		Resources r = context.getResources();		//获取资源对象	
		bmpProgress = BitmapFactory.decodeResource(r, R.drawable.progress);	//初始化进度条图片
		bmpProgSign = new Bitmap[3];										//初始化进度条标志物
		bmpProgSign[0] = BitmapFactory.decodeResource(r, R.drawable.prog1);
		bmpProgSign[1] = BitmapFactory.decodeResource(r, R.drawable.prog2);
		bmpProgSign[2] = BitmapFactory.decodeResource(r, R.drawable.prog3);
		bmpLoad = BitmapFactory.decodeResource(r, R.drawable.load);			//初始化加载图片
	}
	@Override
	protected void finalize() throws Throwable {
		System.out.println("############ LoadingView  is dead##########");
		super.finalize();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {//重写surfaceChanged方法
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {//重写surfaceCreated方法
		if(!lt.isAlive()){			//如果后台刷屏线程还未启动，就启动线程刷屏
			lt.start();
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {//重写surfaceDestroyed方法
		lt.flag = false;			//停止刷屏线程
	}	
}