package wyf.wpf;			//���������

import android.graphics.Canvas;			//���������
import android.view.SurfaceHolder;		//���������
/*
 * ����̳���Thread�࣬��Ҫ�����Ƕ�ʱ�ػ���Ϸ��ĻGameView��ͨ��SurfaceHolder��lockCanvas����
 * Ϊ ��������Ȼ���ػ�
 */
public class DrawThread extends Thread{
	GameView father;				//FieldView��������
	SurfaceHolder surfaceHolder;	//FieldView�����SurfaceHolder
	boolean isGameOn = true;		//�߳�ִ�еı�־λ
	int sleepSpan = 20;				//�̵߳�����ʱ��
	//����������ʼ����Ҫ��Ա����
	public DrawThread(GameView father,SurfaceHolder surfaceHolder){
		super.setName("##-DrawThread");			//Ϊ�߳��������ƣ���������
		this.father = father;				
		this.surfaceHolder = surfaceHolder;
		isGameOn = true;
	}
	//�������̵߳�ִ�з���
	public void run(){
		Canvas canvas = null;
		while(isGameOn){
			try{
				canvas = surfaceHolder.lockCanvas(null);	//Ϊ��������
				synchronized(surfaceHolder){
					father.doDraw(canvas);					//�ػ滭��
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				if(canvas != null){		//�����ͷŲ����ػ���
					surfaceHolder.unlockCanvasAndPost(canvas);
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