package wyf.wpf;		//声明包语句
import android.graphics.Canvas;		//引入相关类
import android.graphics.Color;		//引入相关类
import android.graphics.Paint;//引入相关类
import android.view.SurfaceHolder;//引入相关类
import android.view.SurfaceView;//引入相关类

public class ProgressView extends SurfaceView implements SurfaceHolder.Callback{
	RunActivity father;
	ProgressDrawThread pdt;
	int progress; //进度值
	int target = -1;//进度条满后切屏的目标，0为GameView，3为WelcomeView
	
	public ProgressView(RunActivity father,int target) {
		super(father);
		this.father = father;
		this.target = target;
		getHolder().addCallback(this);
		pdt = new ProgressDrawThread(this,getHolder());
	}
	//屏幕的绘制方法
	public void doDraw(Canvas canvas){
		//画进度条
		BitmapManager.drawSystemPublic(0, canvas, 0, 0, null);
		//画遮挡物
		Paint paint = new Paint();
		paint.setColor(Color.DKGRAY);
		int p = this.progress;
		if(p == 100){//判断是否进度条读完
			father.myHandler.sendEmptyMessage(target);		
		}
	}


	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	
	public void surfaceCreated(SurfaceHolder holder) {
		pdt.flag = true;//设置循环控制变量
		if(!pdt.isAlive()){//是否需要启动线程
			pdt.start();
		}
	}

	
	public void surfaceDestroyed(SurfaceHolder holder) {
		pdt.flag = false;//停掉后台绘制线程
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	
}