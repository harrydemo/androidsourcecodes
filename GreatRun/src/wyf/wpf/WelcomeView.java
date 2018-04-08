package wyf.wpf;	//���������		



import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback{
	RunActivity father;					//RunActivity������
	WelcomeThread wt;//��̨�޸������߳�
	WelcomeDrawThread wdt;//��̨�ػ��߳�
	
	int status=-1;		//��¼��ǰ״̬,0���������ԣ�1�����ὥ����2�����彥����3����ʾ�˵���4��������5���а�ť����
	int alpha = 0;//��¼����ͼƬ�ı���ɫ
	int selectedIndex=-1;//ѡ��Ĳ˵�ѡ���0Ϊ��ʼ/1Ϊ����/2�˳�
	Rect rectMenuStart = new Rect(40,380,104,444);//��ʼ��ť���ο�
	Rect rectMenuHelp = new Rect(128,380,194,444);//������ť���ο�
	Rect rectMenuQuit = new Rect(218,380,282,444);//�˳���ť���ο�
	Rect rectSoundMenu = new Rect(94,445,226,475);//�����˵�
	//����������ʼ����Ҫ��Ա����
	public WelcomeView(RunActivity father) {
		super(father);
		this.father = father;
		getHolder().addCallback(this);
		wt = new WelcomeThread(this);
		wdt = new WelcomeDrawThread(this,getHolder());
		status = 0;
	}
	//������������Ļ
	public void doDraw(Canvas canvas){
		Paint paint = new Paint();	//��������
		paint.setAlpha(alpha);		
		BitmapManager.drawWelcomePublic(0, canvas, 0, 0,paint);//������
	}
	
	
	
//�����������û������Ļ�¼�
	public boolean myTouchEvent(int x,int y){
		if(rectMenuStart.contains(x, y)){//���¿�ʼ��ť
			selectedIndex = 0;//���ð��µ�����
			status = 5;//����״̬Ϊ5���������а�ť
		}
		else if(rectMenuHelp.contains(x, y)){//���°�����ť
			selectedIndex = 1;//���ð��µ�����
			status = 5;//����״̬Ϊ5���������а�ť
		}
		else if(rectMenuQuit.contains(x, y)){//�����˳���ť
			selectedIndex = 2;//���ð��µ�����
			status = 5;//����״̬Ϊ5���������а�ť
		}
		
		return true;
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {//��д�ӿڷ���
	}
	
	public void surfaceCreated(SurfaceHolder holder) {//��дsurfaceCreated����
		wdt.isDrawOn = true;
		if(!wdt.isAlive()){
			wdt.start();
		}
		wt.flag = true;
		if(!wt.isAlive()){
			wt.start();
		}		
		
	}

	
	public void surfaceDestroyed(SurfaceHolder holder) {//��дsurfaceDestroyed����
		wdt.isDrawOn = false;
	}
	
}