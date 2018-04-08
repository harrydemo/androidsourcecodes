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
 * ���ְ����
 *
 */
public class HighScoreView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity������
	Paint paint;//��������
	DrawThread drawThread;//�����߳�����		
	Bitmap bgBitmap;//����ͼƬ	
	Bitmap bmp;//���ֵ�ͼƬ
	Bitmap defenBitmap;
	Bitmap riqiBitmap;
	Bitmap[] numberBitmaps;//����ͼƬ	
	Bitmap gangBitmap;//����"-"��Ӧ��ͼƬ
	int bmpx;//����λ��	
	String queryResultStr;//��ѯ���ݿ�Ľ��
	String[] splitResultStrs;//����ѯ����зֺ������
	private int numberWidth;//����ͼƬ�Ŀ��
	private int posFrom=-1;//��ѯ�Ŀ�ʼλ��
	private int length=5;//��ѯ������¼����
	int downY=0;//���º�̧���y����
	int upY=0;
	public HighScoreView(GameActivity activity) {
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
		//��������+Constant.X_OFFSET,  +Constant.Y_OFFSET
		canvas.drawBitmap(bmp, bmpx+Constant.X_OFFSET, Constant.BMP_Y+Constant.Y_OFFSET, paint);
		canvas.drawBitmap(defenBitmap, Constant.DE_FEN_X+Constant.X_OFFSET, Constant.DE_FEN_Y+Constant.Y_OFFSET, paint);
		canvas.drawBitmap(riqiBitmap, Constant.RI_QI_X+Constant.X_OFFSET, Constant.DE_FEN_Y+Constant.Y_OFFSET, paint);
		//���Ƶ÷ֺ�ʱ��
		float x;
		float y;
		for(int i=0;i<splitResultStrs.length;i++)
		{
			if(i%2==0)//����÷ֵ�λ��
			{
				x=Constant.SCREEN_WIDTH*3/4;				
			}
			else//����ʱ���λ��
			{
				x=Constant.SCREEN_WIDTH/2;
			}
			y=Constant.BMP_Y+defenBitmap.getHeight()+(numberBitmaps[0].getHeight()+10)*(i/2+1);
			//�����ַ���
			drawDateBitmap(splitResultStrs[i],x+Constant.X_OFFSET,y+Constant.Y_OFFSET,canvas,paint);
		}
	}
	//������ͼƬ�ķ���
	public void drawDateBitmap(String numberStr,float endX,float endY,Canvas canvas,Paint paint)
	{
		for(int i=0;i<numberStr.length();i++)
		{
			char c=numberStr.charAt(i);
			if(c=='-')
			{
				canvas.drawBitmap(gangBitmap,endX-numberWidth*(numberStr.length()-i), endY, paint);
			}
			else
			{
				canvas.drawBitmap
				(
						numberBitmaps[c-'0'], 
						endX-numberWidth*(numberStr.length()-i), 
						endY, 
						paint
				);
			}			
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int y = (int) event.getY();		
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:
    		downY=y;
    		break;
    	case MotionEvent.ACTION_UP:
    		upY=y;    		
        	if(Math.abs(downY-upY)<20)//����ֵ��Χ�ڣ�����ҳ
        	{
        		return true;
        	}
        	else if(downY<upY)//����Ĩ
        	{	
        		//���Ĩ����ǰҳ��������Ĩ
        		if(this.posFrom-this.length>=-1)
        		{
        			this.posFrom-=this.length;        			
        		}
        	}
        	else//����Ĩ
        	{	
        		//���Ĩ�����ҳ��������Ĩ
        		if(this.posFrom+this.length<activity.getRowCount()-1)
        		{
        			this.posFrom+=this.length;        			
        		}
        	}
        	queryResultStr=activity.query(posFrom,length);//�õ����ݿ��е�����
			splitResultStrs=queryResultStr.split("/", 0);//��"/"�з֣�������մ�
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
		numberWidth=numberBitmaps[0].getWidth()+3;//�õ�����ͼƬ�Ŀ��
		//��ʼ��ͼƬ��λ��
		bmpx=(Constant.SCREEN_WIDTH-bmp.getWidth())/2;
		posFrom=-1;//��ѯ�Ŀ�ʼλ����-1			
		queryResultStr=activity.query(posFrom,length);//�õ����ݿ��е�����
		splitResultStrs=queryResultStr.split("/", 0);//��"/"�з֣�������մ�		
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
	//��ͼƬ����
	public void initBitmap(){
		bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bmp);	
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
		numberBitmaps=new Bitmap[]{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number0),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number1),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number2),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number3),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number4),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number5),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number6),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number7),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number8),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number9),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number0),
		};
		gangBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.gang);
		defenBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.defen);
		riqiBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.riqi);
		//��Ӧ��
		bmp=PicLoadUtil.scaleToFit(bmp, Constant.ssr.ratio);
		gangBitmap=PicLoadUtil.scaleToFit(gangBitmap, Constant.ssr.ratio);
		riqiBitmap=PicLoadUtil.scaleToFit(riqiBitmap, Constant.ssr.ratio);
		defenBitmap=PicLoadUtil.scaleToFit(defenBitmap, Constant.ssr.ratio);				
		for(int i=0;i<numberBitmaps.length;i++){
			numberBitmaps[i]=PicLoadUtil.scaleToFit(numberBitmaps[i], Constant.ssr.ratio);
		}
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
		HighScoreView fatherView;
		SurfaceHolder surfaceHolder;
		public DrawThread(HighScoreView fatherView){
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
