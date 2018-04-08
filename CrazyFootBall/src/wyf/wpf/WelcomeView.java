package wyf.wpf;										//���������
import android.content.Context;							//���������
import android.content.res.Resources;					//���������
import android.graphics.Bitmap;							//���������
import android.graphics.BitmapFactory;					//���������
import android.graphics.Canvas;							//���������
import android.graphics.Color;							//���������
import android.graphics.Matrix;							//���������
import android.graphics.Paint;							//���������
import android.view.SurfaceHolder;						//���������
import android.view.SurfaceView;						//���������
/*
 * ����̳���View��ʵ�ֻ�ӭ�����Ĳ��ţ��Լ����˵�����ʾ
 */
public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback{
	WelcomeThread wt;				//��̨�޸������߳�
	WelcomeDrawThread wdt;			//��̨�ػ��߳�
	FootballActivity father;		//Activity������
	int index = 0;					//����3������֡������
	int status = -1;				//0�������򶯻���1������ת������2����ȫ�����ԣ�3�������
	int alpha = 255;				//͸���ȣ���ʼΪ255������͸��
	int [] layout = {3,3,4};		//�����Ա��վλ���飬3��ֵ�ֱ����ǰ�����г�����
	CustomGallery cg;				//�Զ����Gallery�࣬����ѡ����ֲ�logo	
	Bitmap [] bmpLayout;			//����ǰ�����г�����3�����ߵ�ͼƬ����
	Bitmap bmpPlus;					//�Ӻ�ͼƬ
	Bitmap bmpMinus;  				//����ͼƬ	
	Bitmap bmpPlayer;				//���ͼƬ
	Bitmap [] bmpSound;				//��������ͼƬ����
	Bitmap bmpStart;				//��ʼ��ťͼƬ
	Bitmap bmpQuit;					//�˳���ťͼƬ
	Bitmap [] bmpGallery;			//�洢Gallery����Ҫ��ʾ������
	Bitmap [] bmpAnimaition;		//�洢��ӭ����֡������
	Bitmap bmpBack;					//����ͼƬ
	Matrix matrix;					//Matrix����������ת����ͼ
	//����������ʼ����Ա����
	public WelcomeView(FootballActivity father) {
		super(father);
		this.father = father;
		getHolder().addCallback(this);	
		initBitmap(father);							//��ʼ��ͼƬ
		matrix = new Matrix();						//����Matrix����
		cg = new CustomGallery(10,10,100,100);		//����CustomGallery����
		cg.setContent(bmpGallery);					//ΪCustomGallery����������ʾ����
		cg.setCurrent(2);							//����CustomGallery��ǰ��ʾ��ͼƬ
		wt = new WelcomeThread(this);				//����WelcomeThread����
		wdt = new WelcomeDrawThread(this,getHolder());			//����WelcomeDrawThread����
		status = 0;												//���ó�ʼ״ֵ̬Ϊ0
	}		
	public void initBitmap(Context context){//��ʼ��ͼƬ
		Resources r = context.getResources();			//��ȡResources����
		bmpBack = BitmapFactory.decodeResource(r, R.drawable.welcome);	//��������ͼƬ
		bmpLayout = new Bitmap[3];//������ʾǰ�����г����󳡵�ͼƬ����
		bmpLayout[0] = BitmapFactory.decodeResource(r, R.drawable.fwd_field);
		bmpLayout[1] = BitmapFactory.decodeResource(r, R.drawable.mid_field);
		bmpLayout[2] = BitmapFactory.decodeResource(r, R.drawable.bck_field);
		bmpPlus = BitmapFactory.decodeResource(r, R.drawable.plus);		//�����Ӻ�ͼƬ
		bmpMinus = BitmapFactory.decodeResource(r, R.drawable.minus);	//��������ͼƬ
		bmpPlayer = BitmapFactory.decodeResource(r, R.drawable.player20);	//������ԱͼƬ
		bmpSound = new Bitmap[2];											//������������ͼƬ����
		bmpSound[0] = BitmapFactory.decodeResource(r, R.drawable.sound1);	
		bmpSound[1] = BitmapFactory.decodeResource(r, R.drawable.sound2);
		bmpStart = BitmapFactory.decodeResource(r, R.drawable.start);		//������ʼͼƬ��ť
		bmpQuit = BitmapFactory.decodeResource(r, R.drawable.quit);			//������ʼͼƬ��ť
		bmpAnimaition = new Bitmap[3];										//������������
		bmpAnimaition[0] = BitmapFactory.decodeResource(r, R.drawable.p1);
		bmpAnimaition[1] = BitmapFactory.decodeResource(r, R.drawable.p2);
		bmpAnimaition[2] = BitmapFactory.decodeResource(r, R.drawable.p3);
		//��ʼ��Gallery��ͼƬ��Դ   
		bmpGallery = new Bitmap[8];											//�����Զ���GalleryҪ��ʾ������ͼƬ����
		for(int i=0;i<bmpGallery.length;i++){
			bmpGallery[i] = BitmapFactory.decodeResource(r, father.imageIDs[i]);
		}		 
	}	
	public void doDraw(Canvas canvas) {//���������ڸ��ݲ�ͬ״̬������Ļ
		Paint paint = new Paint();		//��������
		switch(status){
		case 0://��ʾ3������֡
			canvas.drawBitmap(bmpAnimaition[index], 0, 0, null);
			break;
		case 1://����ͼƬ��ת����
			canvas.drawColor(Color.BLACK);		//����Ļ
			Bitmap bmpTemp = Bitmap.createBitmap(bmpBack, 0, 0,
					bmpBack.getWidth(), bmpBack.getHeight(), matrix, true);//��ת����ͼ
			canvas.drawBitmap(bmpTemp, 0, 0, null);			//���Ʊ���ͼ
			break;
		case 2://ȫ��͸��
		case 3://ȫ������--------------����������һ����ֻ��͸���Ȳ�ͬ
			canvas.drawColor(Color.BLACK);			//����Ļ
			paint.setAlpha(alpha);					//����͸����
			canvas.drawBitmap(bmpBack, 0, 0, paint);//������
			cg.drawGallery(canvas,paint);		//���Զ����Gallery
			for(int i=0;i<layout.length;i++){	//�������ϸ��������ϵ���Ϣ���л���
				canvas.drawBitmap(bmpLayout[i], 0, 200+40*i, paint);//�����������Ƽ�ǰ�����г�����
				for(int j=0;j<layout[i];j++){
					canvas.drawBitmap(bmpPlayer, 65+j*18, 205+40*i, paint);	//��������������������Ա
				}
				canvas.drawBitmap(bmpPlus, 244, 200+40*i, paint);			//���ƼӺŰ�ť
				canvas.drawBitmap(bmpMinus, 280, 200+40*i, paint);			//���Ƽ��Ű�ť
			}
			canvas.drawBitmap(bmpSound[father.wantSound?0:1], 135, 370, paint);	//������������
			canvas.drawBitmap(bmpStart, 205, 425, paint);						//���ƿ�ʼ��ť
			canvas.drawBitmap(bmpQuit, 25, 425, paint);							//�����˳���ť
			break;
		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {										//��дsurfaceChanged����
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {			//��дsurfaceCreated����
		if(!wt.isAlive()){			//������̨�޸������߳�
			wt.start();
		}
		if(!wdt.isAlive()){			//������̨�����߳�
			wdt.start();
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {		//��дsurfaceDestroyed����
		if(wt.isAlive()){				//ֹͣ��̨�޸������߳�
			wt.isWelcoming = false;
		}
		if(wdt.isAlive()){				//ֹͣ��̨�����߳�
			wdt.flag = false;
		}		
	}	
}