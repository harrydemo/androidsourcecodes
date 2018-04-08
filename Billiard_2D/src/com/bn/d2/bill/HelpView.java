package com.bn.d2.bill;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 *  ��������
 *
 */
public class HelpView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity������
	Paint paint;//��������
	//����ͼƬ
	Bitmap bgBitmap;
	Bitmap helpBmp;
	public HelpView(GameActivity activity) {
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
		canvas.drawColor(Color.GRAY);
		//���Ʊ���
		canvas.drawBitmap(bgBitmap, 0, 0, paint);
		float x=(Constant.SCREEN_WIDTH-helpBmp.getWidth())/2;
		canvas.drawBitmap(helpBmp, x, Constant.HELP_Y, paint);
	}
	//���»��Ƶķ���
    public void repaint()
	{
		Canvas canvas=this.getHolder().lockCanvas();
		try
		{
			synchronized(canvas)
			{
				onDraw(canvas);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas!=null)
			{
				this.getHolder().unlockCanvasAndPost(canvas);
			}
		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//��������
		paint.setAntiAlias(true);//�򿪿����
		initBitmap();//��ʼ��λͼ��Դ
		repaint();//���ƽ���
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	//����ͼƬ�ķ���
	public void initBitmap(){
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
		helpBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.help0);
		//��Ӧ��
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);		
		helpBmp=PicLoadUtil.scaleToFitFullScreen(helpBmp, Constant.wRatio, Constant.hRatio);
	}	
}
