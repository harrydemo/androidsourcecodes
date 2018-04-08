package wyf.wpf;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ProgressDrawThread extends Thread{
	ProgressView father;//ProcessView���������
	SurfaceHolder surfaceHolder;
	int sleepSpan = 100;
	boolean flag;//ѭ�����Ʊ���
	//������
	public ProgressDrawThread(ProgressView father,SurfaceHolder surfaceHolder){
		super.setName("==ProcessDrawThread");
		this.father = father;
		this.surfaceHolder = surfaceHolder;
	}
	//�߳�ִ�з���
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
				if(canvas != null){//�ͷ�Canvas����
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