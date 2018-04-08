package com.bn.d2.bill;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 *  ��Ч���ý���
 *
 */
public class SoundControlView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity������
	Paint paint;//��������
	//�߳�����
	DrawThread drawThread;//�����߳�����	
	//���ⰴťͼƬ
	Bitmap onBitmap;
	Bitmap offBitmap;
	//���ֵ�ͼƬ	
	private Bitmap yinyueOnBitmap;
	private Bitmap yinyueOffBitmap;
	private Bitmap yinxiaoOnBitmap;
	private Bitmap yinxiaoOffBitmap;
	//���˵������ⰴť��������
	SoundSwitchButton yinyueBtn;
	SoundSwitchButton yinxiaoBtn;	
	//����ͼƬ
	Bitmap bgBitmap;
	public SoundControlView(GameActivity activity) {
		super(activity);
		this.activity=activity;
		//��ý��㲢����Ϊ�ɴ���
		this.requestFocus();
        this.setFocusableInTouchMode(true);
		getHolder().addCallback(this);//ע��ص��ӿ�		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);  	
		//���Ʊ���
		canvas.drawColor(Color.GRAY);
		canvas.drawBitmap(bgBitmap, 0, 0, paint);
		//�������ⰴť
		yinyueBtn.drawSelf(canvas, paint);
		yinxiaoBtn.drawSelf(canvas, paint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();		
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:
    		//��������ְ�ť��
    		if(yinyueBtn.isActionOnButtonOn(x, y))
    		{
    			yinyueBtn.switchOn();
    			activity.setBackGroundMusicOn(true);
    		}
    		else if(yinyueBtn.isActionOnButtonOff(x, y))
    		{
    			yinyueBtn.switchOff();
    			activity.setBackGroundMusicOn(false);
    		}
    		//�������Ч��ť��
    		else if(yinxiaoBtn.isActionOnButtonOn(x, y))
    		{
    			yinxiaoBtn.switchOn();
    			activity.setSoundOn(true);
    		}
    		else if(yinxiaoBtn.isActionOnButtonOff(x, y))
    		{
    			yinxiaoBtn.switchOff();
    			activity.setSoundOn(false);
    		}    		
    		break;	
    	}
		return true;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//��������
		paint.setAntiAlias(true);//�򿪿����	
		createAllThreads();//���������߳�
		initBitmap();//��ʼ��λͼ��Դ	
		//�������ⰴť����
		int btnX=(Constant.SCREEN_WIDTH-onBitmap.getWidth()-yinyueOnBitmap.getWidth())/2;
		yinyueBtn=new SoundSwitchButton(yinyueOnBitmap,yinyueOffBitmap,onBitmap,offBitmap,btnX,Constant.SOUND_BTN_Y1,activity.isBackGroundMusicOn());
		yinxiaoBtn=new SoundSwitchButton(yinxiaoOnBitmap,yinxiaoOffBitmap,onBitmap,offBitmap,btnX,Constant.SOUND_BTN_Y2,activity.isSoundOn());
		startAllThreads();//���������߳�
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		  boolean retry = true;
	        stopAllThreads();
	        while (retry) {
	            try {
	            	drawThread.join();
	                retry = false;
	            } 
	            catch (InterruptedException e) {e.printStackTrace();}//���ϵ�ѭ����ֱ��ˢ֡�߳̽���
	        }
	}
	//����ͼƬ�ķ���
	public void initBitmap(){
		onBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.on);
		offBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.off);
		yinyueOnBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyuekai);
		yinyueOffBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyueguan);
		yinxiaoOnBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.yinxiaokai);
		yinxiaoOffBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.yinxiaoguan);	
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
		
		onBitmap=PicLoadUtil.scaleToFit(onBitmap, Constant.ssr.ratio);
		offBitmap=PicLoadUtil.scaleToFit(offBitmap, Constant.ssr.ratio);
		yinyueOnBitmap=PicLoadUtil.scaleToFit(yinyueOnBitmap, Constant.ssr.ratio);		
		yinyueOffBitmap=PicLoadUtil.scaleToFit(yinyueOffBitmap, Constant.ssr.ratio);
		yinxiaoOnBitmap=PicLoadUtil.scaleToFit(yinxiaoOnBitmap, Constant.ssr.ratio);		
		yinxiaoOffBitmap=PicLoadUtil.scaleToFit(yinxiaoOffBitmap, Constant.ssr.ratio);
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);
	}
	void createAllThreads()
	{
		drawThread=new DrawThread(this);//���������߳�		
	}
	void startAllThreads()
	{
		drawThread.setFlag(true);     
		drawThread.start();
	}
	void stopAllThreads()
	{
		drawThread.setFlag(false);       
	}
	private class DrawThread extends Thread{
		private boolean flag = true;	
		private int sleepSpan = 100;
		SoundControlView fatherView;
		SurfaceHolder surfaceHolder;
		public DrawThread(SoundControlView fatherView){
			this.fatherView = fatherView;
			this.surfaceHolder = fatherView.getHolder();
		}
		public void run(){
			Canvas c;
	        while (this.flag) {
	            c = null;
	            try {
	            	// �����������������ڴ�Ҫ��Ƚϸߵ�����£����������ҪΪnull
	                c = this.surfaceHolder.lockCanvas(null);
	                synchronized (this.surfaceHolder) {
	                	fatherView.onDraw(c);//����
	                }
	            } finally {
	                if (c != null) {
	                	//���ͷ���
	                    this.surfaceHolder.unlockCanvasAndPost(c);
	                }
	            }
	            try{
	            	Thread.sleep(sleepSpan);//˯��ָ��������
	            }
	            catch(Exception e){
	            	e.printStackTrace();//��ӡ��ջ��Ϣ
	            }
	        }
		}
		public void setFlag(boolean flag) {
			this.flag = flag;
		}
	}
}
