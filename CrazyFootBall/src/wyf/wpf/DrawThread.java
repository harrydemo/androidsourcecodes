package wyf.wpf;			//声明包语句

import android.graphics.Canvas;			//引入相关类
import android.view.SurfaceHolder;		//引入相关类
/*
 * 此类继承自Thread类，主要功能是定时重绘游戏屏幕GameView，通过SurfaceHolder的lockCanvas方法
 * 为 画布加锁然后重绘
 */
public class DrawThread extends Thread{
	GameView father;				//FieldView对象引用
	SurfaceHolder surfaceHolder;	//FieldView对象的SurfaceHolder
	boolean isGameOn = true;		//线程执行的标志位
	int sleepSpan = 20;				//线程的休眠时间
	//构造器，初始化主要成员变量
	public DrawThread(GameView father,SurfaceHolder surfaceHolder){
		super.setName("##-DrawThread");			//为线程设置名称，做调试用
		this.father = father;				
		this.surfaceHolder = surfaceHolder;
		isGameOn = true;
	}
	//方法：线程的执行方法
	public void run(){
		Canvas canvas = null;
		while(isGameOn){
			try{
				canvas = surfaceHolder.lockCanvas(null);	//为画布加锁
				synchronized(surfaceHolder){
					father.doDraw(canvas);					//重绘画布
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				if(canvas != null){		//将锁释放并传回画布
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			try{						
				Thread.sleep(sleepSpan);		//休眠一段时间
			}
			catch(Exception e){
				e.printStackTrace();			//捕获并打印异常
			}
		}
	}
}