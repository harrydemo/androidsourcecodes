package wyf.wpf;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import static wyf.wpf.ConstantUtil.*;

public class DrawThread extends Thread{
	GameView father;
	SurfaceHolder surfaceHolder;
	boolean flag;//线程是否执行标志位
	boolean isViewOn;//GameView是否被显示标志位
	
	public DrawThread(GameView father,SurfaceHolder surfaceHolder){
		this.father = father;
		this.surfaceHolder = surfaceHolder;
		flag = true;
	}
	//线程的执行方法
	public void run(){
		while(flag){
			while(isViewOn){
				Canvas canvas = null;
				try{
					canvas = surfaceHolder.lockCanvas();
					synchronized(surfaceHolder){
						father.doDraw(canvas);
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					if(canvas != null){
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
				try{
					Thread.sleep(DRAW_THREAD_SLEEP_SPAN);//休眠
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			try{
				Thread.sleep(1500);//空转休眠时间
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}