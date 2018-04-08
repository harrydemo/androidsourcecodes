package ninjarush.relatedclass;



import ninjarush.mainactivity.R;
import ninjarush.mainactivity.UserAchieve;
import ninjarush.mainsurfaceview.NinjaRushSurfaceView;
import ninjarush.music.GameMusic;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class Gameing_Bg {
	//����1�ľ���
	private Rect rectBg1;
	private Bitmap bmpbg1,bmpbg2,bmpbg3;
	//����
	private Bitmap bmpcloud_0,bmpcloud_1,bmpcloud_2,bmpcloud_3;
	//��������ʼ����
	private int bg1X,bg1Y,bg2X,bg22X,bg2Y,bg3X,bg33X,bg3Y;
	//���Ƶ���ʼ����
	private int bc0X,bc0Y,bc1X,bc1Y,bc2X,bc2Y,bc3X,bc3Y;
	//��������
	private int textX,textY;
	//�����Լ����Ƶ��˶��ٶ�
	private int bgspeed,bcspeed;
	private int meter;
	//�Ƿ���������
	private int x,y,x1,y1;
	public static boolean islogic;
	//
	private int metertime;
	private int count;
	//��Ļ����Y�ٶ�
	private int yy;
	//��Ļ���ƾ���
	private int yy_d;
	//�����Ļ�ƶ���״̬
	private boolean isChange;
	public Gameing_Bg(Bitmap bmpbg1,Bitmap bmpbg2,Bitmap bmpbg3,Bitmap bmpcloud_0,
				Bitmap bmpcloud_1,Bitmap bmpcloud_2,Bitmap bmpcloud_3){
		this.bmpbg1=bmpbg1;
		this.bmpbg2=bmpbg2;
		this.bmpbg3=bmpbg3;
		this.bmpcloud_0=bmpcloud_0;
		this.bmpcloud_1=bmpcloud_1;
		this.bmpcloud_2=bmpcloud_2;
		this.bmpcloud_3=bmpcloud_3;
		isChange=false;
		//��ʼ����������
		bg1X=0;
		bg1Y=NinjaRushSurfaceView.screenH-bmpbg1.getHeight();
		bg2X=0;
		bg22X=bg2X+bmpbg2.getWidth();
		bg2Y=NinjaRushSurfaceView.screenH-bmpbg2.getHeight();
		bg3X=0;
		bg33X=bg3X+bmpbg3.getWidth();
		bg3Y=NinjaRushSurfaceView.screenH-bmpbg3.getHeight();
		//��ʼ����������
		bc0X=NinjaRushSurfaceView.screenW+bmpcloud_0.getWidth();
		bc0Y=(int)(Math.random()*NinjaRushSurfaceView.screenH/3);
		bc1X=bc0X+NinjaRushSurfaceView.screenW-bmpcloud_1.getWidth();
		bc1Y=(int)(Math.random()*NinjaRushSurfaceView.screenH/3);
		bc2X=bc0X+NinjaRushSurfaceView.screenW-bmpcloud_2.getWidth()+40;
		bc2Y=(int)(Math.random()*NinjaRushSurfaceView.screenH/3);
		bc3X=bc0X+NinjaRushSurfaceView.screenW-bmpcloud_3.getWidth()+20;
		bc3Y=(int)(Math.random()*NinjaRushSurfaceView.screenH/3);
		//��������
		textX=NinjaRushSurfaceView.screenW/100;
		textY=NinjaRushSurfaceView.screenH/6;
		
		//��ʼ�������ٶ�
		bgspeed=2;//����
		bcspeed=1;//����
		islogic=true;
		metertime=2;
		meter=1;
		//ʵ��������1�ľ���
		rectBg1=new Rect(bg1X, bg1Y, NinjaRushSurfaceView.screenW, NinjaRushSurfaceView.screenH);
		
	}
	
	public void draw(Canvas canvas,Paint paint) {
		//����1
//		canvas.scale(bmpbg1.getWidth()/bg1X, bmpbg1.getHeight()/bg1Y, bmpbg1.getWidth()/2, bmpbg1.getHeight()/2);
		canvas.drawBitmap(bmpbg1, null,rectBg1, paint);
		//����2
		canvas.drawBitmap(bmpbg2, bg2X, bg2Y, paint);
		canvas.drawBitmap(bmpbg2, bg22X, bg2Y, paint);
		//����3
		canvas.drawBitmap(bmpbg3, bg3X, bg3Y, paint);
		canvas.drawBitmap(bmpbg3, bg33X, bg3Y, paint);
		//������
		canvas.drawBitmap(bmpcloud_0, bc0X, bc0Y, paint);
		canvas.drawBitmap(bmpcloud_1, bc1X, bc1Y, paint);
		canvas.drawBitmap(bmpcloud_2, bc2X, bc2Y, paint);
		canvas.drawBitmap(bmpcloud_3, bc3X, bc3Y, paint);
		//��ʾ����
		paint.setColor(Color.WHITE);
		paint.setTextSize(25);
		
		canvas.drawText(meter+"��", textX, textY, paint);
		
	}
	
	public void logic(){
		if(islogic){
		count++;//ÿˢ������������һ����������ʱ��ÿˢһ��������������һ
		if(count%metertime==0){
			meter++;
			if(meter==500)
				UserAchieve.userAchieve[0]=1;
			if(meter==1000)
				UserAchieve.userAchieve[1]=1;
			if(meter==2000)
				UserAchieve.userAchieve[2]=1;
			if(meter==4000)
				UserAchieve.userAchieve[3]=1;
		}
		//����2���ٶ�Ϊ��׼�ٶ�
		bg2X-=bgspeed;
		bg22X-=bgspeed;
		//����3Ϊ���ǵ�һϵ����Ҫ���ٶ�--��׼�ٶ�*3
		bg3X-=3*bgspeed;
		bg33X-=3*bgspeed;
		
		bc0X-=bcspeed;
		bc1X-=bcspeed;
		bc2X-=bcspeed;
		bc3X-=bcspeed;
		//����2���߼�
		if(bg2X<=-bmpbg2.getWidth()-10){
			bg2X=bg22X+bmpbg2.getWidth();
		}
		if(bg22X<=-bmpbg2.getWidth()-10){
			bg22X=bg2X+bmpbg2.getWidth();
		}
		///////////��������������������������������������/////////////////////////
		//������������ƶ����ж�
		bc0Y+=yy;
		bc1Y+=yy;
		bc2Y+=yy;
		bc3Y+=yy;
		bg1Y+=yy;
		bg2Y+=yy;
		bg3Y+=yy;
		if(bg1Y>=NinjaRushSurfaceView.screenH-bmpbg1.getHeight()+NinjaRushSurfaceView.screenH/8){
			yy=-NinjaRushSurfaceView.screenH/40;
		}else if(	bg1Y<=NinjaRushSurfaceView.screenH-bmpbg1.getHeight()){
			yy=0;
			isChange=false;
		}
		///////////��������������������������������������/////////////////////////
		//����3���߼�
		if(bg3X<=-bmpbg3.getWidth()-10){
			bg3X=bg33X+bmpbg3.getWidth();
		}else if(bg33X<=-bmpbg3.getWidth()-10){
			bg33X=bg3X+bmpbg3.getWidth();
		}
		
		//���Ƶ��߼�
		if(bc0X<=-bmpcloud_0.getWidth()-10){
			bc0X=NinjaRushSurfaceView.screenW+bmpcloud_0.getWidth();
			bc0Y=(int)(Math.random()*110);
		}else if(bc1X<=-bmpcloud_1.getWidth()-10){
			bc1X=bc0X+NinjaRushSurfaceView.screenW-bmpcloud_1.getWidth();
			bc1Y=(int)(Math.random()*110);
		}else if(bc2X<=-bmpcloud_2.getWidth()-10){
			bc2X=bc0X+NinjaRushSurfaceView.screenW-bmpcloud_2.getWidth()+(int)(Math.random()*60)+10;
			bc2Y=(int)(Math.random()*110);
		}else if(bc3X<=-bmpcloud_3.getWidth()-10){
			bc3X=bc0X+NinjaRushSurfaceView.screenW-bmpcloud_3.getWidth()+(int)(Math.random()*30)+10;
			bc3Y=(int)(Math.random()*110);
		}
		
	}
}

				public int getMeter() {
					return meter;
				}
			
				public void setMeter(int meter) {
					this.meter = meter;
				}
	
				public void onTouchEvent(MotionEvent event,int playercurrentY){
					
						if(event.getAction()==MotionEvent.ACTION_DOWN){
							 x=(int)event.getX();
							 y=(int)event.getY();
						}else if(event.getAction()==MotionEvent.ACTION_UP){
							x1=(int)event.getX();
							 y1=(int)event.getY();
							 
								///////////��������������������������������������/////////////////////////
								//��������ʱ���ж�4��9��4��   ������
							 if(x1==x&&y1==y&&playercurrentY<=Tools.IS_BG_DOWN){
									yy=NinjaRushSurfaceView.screenH/40;
									isChange=true;
								}
						///////////��������������������������������������/////////////////////////
							 //��������
							if(x1-NinjaRushSurfaceView.screenW/20>x){
//								Tools.sound_wind = GameMusic.playSound(R.raw.wind, -1);
								bgspeed=Tools.PREBGSPEED;
								bcspeed=2;
								x=y=0;
								x1=y1=0;
								metertime=1;
							}
							else if(x1<x-NinjaRushSurfaceView.screenW/20){
//								GameMusic.stopSound(Tools.sound_wind);
								bgspeed=Tools.LATERBGSPEED;
								bcspeed=1;
								x=y=0;
								x1=y1=0;
								metertime=2;
							}
						}else if(event.getAction()==MotionEvent.ACTION_MOVE){
							 
						}
						
				
						
				}
	public Gameing_Bg(){
		
	}

	public int getBgspeed() {
		return bgspeed;
	}

	public void setBgspeed(int bgspeed) {
		this.bgspeed = bgspeed;
	}

	public int getBcspeed() {
		return bcspeed;
	}
	
	public boolean isChange() {
		return isChange;
	}

	public void setBcspeed(int bcspeed) {
		this.bcspeed = bcspeed;
	}

	public int getYy() {
		return bg1Y-(NinjaRushSurfaceView.screenH-bmpbg1.getHeight());
	}



	
}
