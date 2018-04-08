package wyf.wpf;		//

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class WelcomeDrawThread extends Thread{
	WelcomeView father;//WelcomeView的引用
	SurfaceHolder surfaceHolder;
	boolean flag;//循环控制变量
	boolean isDrawOn;//内层循环变量
	int sleepSpan = 80;//睡眠时间
	
	public WelcomeDrawThread(WelcomeView father,SurfaceHolder surfaceHolder){
		super.setName("==WelcomeDrawThread");
		this.father = father;
		this.surfaceHolder = surfaceHolder;
		flag = true;
		isDrawOn = true;
	}
	//线程执行方法
	public void run(){
		while(flag){
			Canvas canvas = null;
			while(isDrawOn){
				try{
					canvas = surfaceHolder.lockCanvas(null);
					synchronized(surfaceHolder){
						father.doDraw(canvas);
					}				
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{//释放画布
					if(canvas != null){
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
			try{//外层循环睡眠
				Thread.sleep(2000);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}