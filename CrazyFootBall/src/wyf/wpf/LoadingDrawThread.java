package wyf.wpf;				//声明包语句
import android.graphics.Canvas;		//引入相关类
import android.view.SurfaceHolder;	//引入相关类	

public class LoadingDrawThread extends Thread{
	LoadingView father;				//LoadingView对象引用	
	SurfaceHolder surfaceHolder;	//LoadingView的SurfaceHolder对象引用
	boolean flag;					//线程执行标志位
	int sleepSpan = 120;			//线程休眠时间
	//构造器
	public LoadingDrawThread(LoadingView father,SurfaceHolder surfaceHolder){
		super.setName("##-LoadingThread");		//为线程设置名称，调试使用
		this.father = father;
		this.surfaceHolder = surfaceHolder;
		flag = true;
	}
	//方法：线程的执行方法
	public void run(){
		Canvas canvas=null;
		while(flag){
			try{
				canvas = surfaceHolder.lockCanvas(null);//为画布加锁
				synchronized(surfaceHolder){
					father.doDraw(canvas);	//重新绘制
				}
			}
			catch(Exception e){
				e.printStackTrace();		//捕获并打印异常
			}
			finally{
				if(canvas != null){
					surfaceHolder.unlockCanvasAndPost(canvas);//释放画布锁并传回画布
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