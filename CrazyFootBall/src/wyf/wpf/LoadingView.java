package wyf.wpf;							//���������
import android.content.Context;				//���������
import android.content.res.Resources;		//���������
import android.graphics.Bitmap;				//���������
import android.graphics.BitmapFactory;		//���������
import android.graphics.Canvas;				//���������
import android.graphics.Color;				//���������
import android.graphics.Paint;				//���������
import android.view.SurfaceHolder;			//���������
import android.view.SurfaceView;			//���������
/*
 * ����̳���SurfaceView����Ҫ�Ĺ������ں�̨���ء���������ʱ��ǰ̨ ��ʾ����
 */
public class LoadingView extends SurfaceView implements SurfaceHolder.Callback{
	FootballActivity father;		//Activity������	
	Bitmap bmpProgress;				//��ʾ����ʱͼƬ
	Bitmap [] bmpProgSign;			//�������ϵı�־��
	Bitmap bmpLoad;					//������ͼƬ����
	int progress=0;					//���ȣ�0��100
	int progY = 330;				//��������Y����
	LoadingDrawThread lt;			//LoadingView��ˢ���߳�	
	public LoadingView(FootballActivity father) {//����������ʼ����Ҫ��Ա����
		super(father);			//���ø��๹����
		this.father = father;
		initBitmap(father);		//��ʼ��ͼƬ
		getHolder().addCallback(this);	//���Callback�ӿ�
		lt = new LoadingDrawThread(this,getHolder());//����ˢ���߳�
	}	
	public void doDraw(Canvas canvas) {//������������Ļ
		canvas.drawColor(Color.BLACK);					//����Ļ		
		canvas.drawBitmap(bmpLoad, 10, 100, null);		//������ʱͼƬ		
		canvas.drawBitmap(bmpProgress, 5, progY, null);		//��������ͼƬ		
		//���ڸ���
		Paint p = new Paint();							//�������ʶ���		
		p.setColor(Color.BLACK);						//���û�����ɫ
		int temp = (int)((progress/100.0)*320);			//������ֵ�������Ļ�ϵĳ���
		canvas.drawRect(temp, progY, 315, progY+20, p);			//���ڸ��ﵲס������ͼƬ
		//����������־��
		for(int i=0;i<3;i++){
			canvas.drawBitmap(bmpProgSign[i], 140*i, progY-10, null);
		}
		if(progress == 100){				//���ƽ�������������ʾ����
			p.setTextSize(13.5f);
			p.setColor(Color.GREEN);
			canvas.drawText("������Ļ��ʼ��Ϸ...", 100, progY+50, p);
		}else{								//���ƽ�����δ������ʾ����
			p.setTextSize(13.5f);
			p.setColor(Color.RED);
			canvas.drawText("������,���Ժ�....", 120, progY+50, p);	
		}
	}	
	public void initBitmap(Context context){//��������ʼ��ͼƬ
		Resources r = context.getResources();		//��ȡ��Դ����	
		bmpProgress = BitmapFactory.decodeResource(r, R.drawable.progress);	//��ʼ��������ͼƬ
		bmpProgSign = new Bitmap[3];										//��ʼ����������־��
		bmpProgSign[0] = BitmapFactory.decodeResource(r, R.drawable.prog1);
		bmpProgSign[1] = BitmapFactory.decodeResource(r, R.drawable.prog2);
		bmpProgSign[2] = BitmapFactory.decodeResource(r, R.drawable.prog3);
		bmpLoad = BitmapFactory.decodeResource(r, R.drawable.load);			//��ʼ������ͼƬ
	}
	@Override
	protected void finalize() throws Throwable {
		System.out.println("############ LoadingView  is dead##########");
		super.finalize();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {//��дsurfaceChanged����
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {//��дsurfaceCreated����
		if(!lt.isAlive()){			//�����̨ˢ���̻߳�δ�������������߳�ˢ��
			lt.start();
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {//��дsurfaceDestroyed����
		lt.flag = false;			//ֹͣˢ���߳�
	}	
}