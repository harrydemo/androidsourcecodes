package wyf.wpf;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ProgressDrawThread extends Thread{
	ProgressView father;//ProcessView对象的引用
	SurfaceHolder surfaceHolder;
	int sleepSpan = 100;
	boolean flag;//循环控制变量
	//构造器
	public ProgressDrawThread(ProgressView father,SurfaceHolder surfaceHolder){
		super.setName("==ProcessDrawThread");
		this.father = father;
		this.surfaceHolder = surfaceHolder;
	}
	//线程执行方法
	public void run(){
		Canvas canvas=null;
		while(flag){
			try{
				canvas = surfaceHolder.lockCanvas(null);
				synchronized(surfaceHolder){
					father.doDraw(canvas);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(canvas != null){//释放Canvas的锁
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			try{
				Thread.sleep(sleepSpan);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}