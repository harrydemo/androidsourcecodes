package wyf.wpf;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import static wyf.wpf.ConstantUtil.*;

public class DrawThread extends Thread{
	GameView father;
	SurfaceHolder surfaceHolder;
	boolean flag;//�߳��Ƿ�ִ�б�־λ
	boolean isViewOn;//GameView�Ƿ���ʾ��־λ
	
	public DrawThread(GameView father,SurfaceHolder surfaceHolder){
		this.father = father;
		this.surfaceHolder = surfaceHolder;
		flag = true;
	}
	//�̵߳�ִ�з���
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
					Thread.sleep(DRAW_THREAD_SLEEP_SPAN);//����
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			try{
				Thread.sleep(1500);//��ת����ʱ��
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}