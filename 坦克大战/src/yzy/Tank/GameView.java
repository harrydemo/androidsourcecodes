package yzy.Tank;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * ��Ϸ������
 */
public class GameView extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder sh;
	private Canvas canvas;
	
	MainActivity activity;//����activity
	
	private Bitmap gdbj;//����
	private Bitmap[] gdbjs = new Bitmap[ConstantUtil.pictureCount];//װ�ָ��Ժ��ͼƬ
	float backGroundIX = 0;//����ͼ��x����
	int i = 0;//����ͼ������
	BackGroundThread backGroundThread;//���������߳� 
	
	private Bitmap control;//���ư�ť
	SoundPool soundPool;//����
	HashMap<Integer, Integer> soundPoolMap; 
	KeyThread keyThread;//���̼����߳�
	int action = 0;//���̵�״̬
	
	MoveThread moveThread;//�ƶ�������߳� 
	ExplodeThread explodeThread;//��ը��֡���߳� 
	int status = 1;//��Ϸ��״̬1��ʾ��Ϸ��,2��ʾ��Ϸʧ�ܼ��ҷ��ɻ�û��������
	private boolean isdraw = true;//�Ƿ�ֹͣ����
	Tank tank = new Tank(140, 389,ConstantUtil.life, this);
	ArrayList<Bullet> goodBollets = new ArrayList<Bullet>();//�ҷ�̹�˷������ӵ�
	ArrayList<Bullet> badBollets = new ArrayList<Bullet>();//�з�̹�˷������ӵ�
	ArrayList<EnemyTank> enemyTanks;//�з���̹�� 
	Bitmap enemyTank1;//��̹��1
	Bitmap enemyTank2;//��̹��2
	Bitmap enemyTank3;//��̹��3
	Bitmap enemyTank4;//�зɵ�
	Bitmap enemyTank5;
	ArrayList<Life> lifes;//���Ѫ��
	Bitmap life;//Ѫ���ͼƬ
	ArrayList<Explode> explodeList = new ArrayList<Explode>();//��ըЧ��1
	int[] explodesID = new int[]{//��ը������֡
			R.drawable.baozha1,R.drawable.baozha2,R.drawable.baozha3,
			R.drawable.baozha4,R.drawable.baozha5,R.drawable.baozha6,
			R.drawable.baozha7,R.drawable.baozha8,R.drawable.baozha9,
		};
	Bitmap[] explodes = new Bitmap[explodesID.length];//��ը1������
	
	ArrayList<Explode> explodeList2 = new ArrayList<Explode>();//��ըЧ��2
	int[] explodesID2 = new int[]{//��ը2������֡
			R.drawable.bz1,R.drawable.bz2,
			R.drawable.bz3,R.drawable.bz4,R.drawable.bz5,
	};
	Bitmap[] explodes2 = new Bitmap[explodesID2.length];//��ը2������
	
	public GameView(MainActivity activity) {
		super(activity);
		this.activity = activity;//activity������
		gdbj = BitmapFactory.decodeResource(this.getResources(), R.drawable.gdbj);//����ͼƬ
		for(int i=0; i<gdbjs.length; i++){//�г�СͼƬ
			gdbjs[i] = Bitmap.createBitmap(gdbj, ConstantUtil.pictureWidth*i, 0, ConstantUtil.pictureWidth, 480);
		}
		gdbj=null;//�ͷŵ��󱳾�ͼ
		control= BitmapFactory.decodeResource(this.getResources(), R.drawable.control);//���Ƽ�ͼƬ
		initSounds();//��ʼ������
		
		this.backGroundThread = new BackGroundThread(this);//��ʼ�����������߳�
		this.keyThread = new KeyThread(this);//��ʼ�����̼���
		this.moveThread = new MoveThread(this);//��ʼ�������ƶ��߳�
		this.explodeThread = new ExplodeThread(this);//��ʼ����ը�߳�
		enemyTanks = Maps.getFirst();//ȡ��һ�صĵ�̹��
		lifes = Maps.getFirstLife();//ȡ��һ�صĵ�Ѫ��
		enemyTank1 = BitmapFactory.decodeResource(getResources(), R.drawable.tk1);//��̹1��ͼƬ
		enemyTank2 = BitmapFactory.decodeResource(getResources(), R.drawable.tk2);//��̹2��ͼƬ
		enemyTank3 = BitmapFactory.decodeResource(getResources(), R.drawable.tk3);//��̹3��ͼƬ
		enemyTank4 = BitmapFactory.decodeResource(getResources(), R.drawable.feidie);//�зɵ�ͼƬ
		enemyTank5 = BitmapFactory.decodeResource(getResources(), R.drawable.boss);//boss
		
		for(int i=0; i<explodes.length; i++){//��ʼ����ը1ͼƬ
			explodes[i] = BitmapFactory.decodeResource(getResources(), explodesID[i]);
		}
		for(int i=0; i<explodes2.length; i++){//��ʼ����ը2ͼƬ
			explodes2[i] = BitmapFactory.decodeResource(getResources(), explodesID2[i]);
		}
		for(EnemyTank ep : enemyTanks){//Ϊ��̹��ʼ��ͼƬ
			if(ep.type == 1){ 
				ep.bitmap = enemyTank1;
			}
			else if(ep.type == 2){
				ep.bitmap = enemyTank2;
			}
			else if(ep.type == 3){
				ep.bitmap = enemyTank3;
			}
			else if(ep.type == 4){
				ep.bitmap = enemyTank4;
			}
			else if(ep.type == 5){
				ep.bitmap = enemyTank5;
			}
		}
		life = BitmapFactory.decodeResource(getResources(), R.drawable.xuekuai);//Ѫ��ͼƬ
		for(Life l : lifes){//ΪѪ���ʼ��ͼƬ
			l.bitmap = life;
		}
		
		this.setFocusable(true);// ���õ�ǰView��ӵ�п��ƽ���
		sh = this.getHolder();// View�൱�ڵ��� SurfaceHolder����ң����
		sh.addCallback(this);
	} 
	public void initSounds(){//��ʼ������
		 soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	     soundPoolMap = new HashMap<Integer, Integer>();   	
	     soundPoolMap.put(1, soundPool.load(getContext(), R.raw.tk3, 1));
	     soundPoolMap.put(2, soundPool.load(getContext(), R.raw.zidanfashe, 1));
	     soundPoolMap.put(3, soundPool.load(getContext(), R.raw.baozha, 1));
	     soundPoolMap.put(4, soundPool.load(getContext(), R.raw.daodanyinxiao, 1));
	     soundPoolMap.put(5, soundPool.load(getContext(), R.raw.chixue, 1));
	}
	public void playSound(int sound, int loop) {
		AudioManager mgr = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   
	    
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}
	public void drawView() {
		//����������x��ģ��󻭵ĻḲ��ǰ�滭��
		float backGroundIX=this.backGroundIX;
		int i=this.i;
		try {
			if (sh != null) {
				canvas = sh.lockCanvas();// �õ���� �������
				canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));//�����
				
				//��i��౳��
				if(backGroundIX>0){
					float n=(backGroundIX/ConstantUtil.pictureWidth)+((backGroundIX%ConstantUtil.pictureWidth==0)?0:1);//����i�����м���ͼ
					for(int j=1;j<=n;j++){
						canvas.drawBitmap(gdbjs[(i-j+ConstantUtil.pictureCount)%ConstantUtil.pictureCount], backGroundIX-ConstantUtil.pictureWidth*j, 0, null);
					}
				}
				//��i������
				canvas.drawBitmap(gdbjs[i], backGroundIX, 0, null);
				//��i�Ҳ౳��
				if(backGroundIX<854-ConstantUtil.pictureWidth){
					float k=854-(backGroundIX+ConstantUtil.pictureWidth);
					float n=(k/ConstantUtil.pictureWidth)+((k%ConstantUtil.pictureWidth==0)?0:1);//����i�����м���ͼ
					for(int j=1;j<=n;j++){
						canvas.drawBitmap(gdbjs[(i+j)%ConstantUtil.pictureCount], backGroundIX+ConstantUtil.pictureWidth*j, 0, null
						);
					}
				}	
				canvas.drawBitmap(control, 42, 138, null);//�����Ƽ�
				
				tank.drawView(canvas);//����̹��
				try{//���Ƶ�̹��
					for(EnemyTank ep:enemyTanks){
						if(ep.status == true){
							ep.draw(canvas);
						}
					}
				}catch(Exception e){  }
				
				try{//�����ҷ��ӵ�
					for(Bullet b:goodBollets){
						b.draw(canvas);
					}			
				}catch(Exception e){  }
				
				try{//���Ƶз��ӵ�  
					for(Bullet b:badBollets){
						b.draw(canvas);
					}
				}
				catch(Exception e){
				}	
				
				try{//���Ʊ�ը
					for(Explode e : explodeList){
						e.draw(canvas);
					}
				}
				catch(Exception e){}
				
				try{//����Ѫ��
					for(Life l : lifes){
						if(l.status == true){
							l.draw(canvas);
						}
					}
				}
				catch(Exception e){}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				sh.unlockCanvasAndPost(canvas);// �Ӵ����� ���͸�ң����
			}
		}
	}
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(this).start();//����GameView�߳�
		
		this.backGroundThread.setFlag(true);
		this.backGroundThread.start();//�������������߳�
		
		this.keyThread.setFlag(true);
		keyThread.start();//�������̼����߳�
		
		this.moveThread.setFlag(true);
        moveThread.start();//�����ӵ��Ѿ������ƶ�����ƶ��߳�
        
        this.explodeThread.setFlag(true);
        explodeThread.start();//������ը�߳�
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		isdraw = false;
		this.backGroundThread.setFlag(false);//ֹͣ���������߳�
		this.keyThread.setFlag(false);//ֹͣ���̼���
		this.moveThread.setFlag(false);//ֹͣ�ӵ��Ѿ������ƶ�����ƶ��߳�
		this.explodeThread.setFlag(false);//ֹͣ��ը�߳�
		
		while (retry) {
            try {
            	keyThread.join();
                backGroundThread.join();
                moveThread.join();
                explodeThread.join();
                retry = false;
            } 
            catch (InterruptedException e) {//���ϵ�ѭ����ֱ��ˢ֡�߳̽���
            }
        }
	}

	@Override
	public void run() {
		while (isdraw) {
			drawView();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if ((event.getX() >= 100 & event.getX() <= 170)& (event.getY() >= 125 & event.getY() <= 201)) {// ��
				action = 1;
			}else if ((event.getX() >= 100 & event.getX() <= 170)& (event.getY() >= 275 & event.getY() <= 349)) {// ��
				action = 2;
			}else if ((event.getX() >= 0 & event.getX() <= 100)& (event.getY() >= 201 & event.getY() <= 275)) {// ��
				action = 3;
			}else if ((event.getX() >= 171 & event.getX() <= 241)& (event.getY() >= 201 & event.getY() <= 275)) {// ��
				action = 4;
			}else{
				action = 6;//����ڿ��������� ������Ļ  ��������ӵ�
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
				action = 5;
		}
		return true;
	}
}
