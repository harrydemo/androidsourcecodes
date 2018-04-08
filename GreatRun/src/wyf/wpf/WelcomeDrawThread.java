package wyf.wpf;		//

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class WelcomeDrawThread extends Thread{
	WelcomeView father;//WelcomeView������
	SurfaceHolder surfaceHolder;
	boolean flag;//ѭ�����Ʊ���
	boolean isDrawOn;//�ڲ�ѭ������
	int sleepSpan = 80;//˯��ʱ��
	
	public WelcomeDrawThread(WelcomeView father,SurfaceHolder surfaceHolder){
		super.setName("==WelcomeDrawThread");
		this.father = father;
		this.surfaceHolder = surfaceHolder;
		flag = true;
		isDrawOn = true;
	}
	//�߳�ִ�з���
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
				finally{//�ͷŻ���
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
			try{//���ѭ��˯��
				Thread.sleep(2000);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}