package wyf.wpf;				//���������
import android.graphics.Canvas;		//���������
import android.view.SurfaceHolder;	//���������	

public class LoadingDrawThread extends Thread{
	LoadingView father;				//LoadingView��������	
	SurfaceHolder surfaceHolder;	//LoadingView��SurfaceHolder��������
	boolean flag;					//�߳�ִ�б�־λ
	int sleepSpan = 120;			//�߳�����ʱ��
	//������
	public LoadingDrawThread(LoadingView father,SurfaceHolder surfaceHolder){
		super.setName("##-LoadingThread");		//Ϊ�߳��������ƣ�����ʹ��
		this.father = father;
		this.surfaceHolder = surfaceHolder;
		flag = true;
	}
	//�������̵߳�ִ�з���
	public void run(){
		Canvas canvas=null;
		while(flag){
			try{
				canvas = surfaceHolder.lockCanvas(null);//Ϊ��������
				synchronized(surfaceHolder){
					father.doDraw(canvas);	//���»���
				}
			}
			catch(Exception e){
				e.printStackTrace();		//���񲢴�ӡ�쳣
			}
			finally{
				if(canvas != null){
					surfaceHolder.unlockCanvasAndPost(canvas);//�ͷŻ����������ػ���
				}
			}
			try{
				Thread.sleep(sleepSpan);		//����һ��ʱ��
			}
			catch(Exception e){
				e.printStackTrace();			//���񲢴�ӡ�쳣
			}				
		}
	}
}