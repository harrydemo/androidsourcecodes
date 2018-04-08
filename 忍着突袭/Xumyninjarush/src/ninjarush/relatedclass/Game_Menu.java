package ninjarush.relatedclass;



import ninjarush.mainactivity.DeveloperActivity;
import ninjarush.mainactivity.R;
import ninjarush.mainactivity.UserAchieve;
import ninjarush.mainsurfaceview.NinjaRushSurfaceView;
import ninjarush.music.GameMusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Game_Menu {
	private Context context;
	private Bitmap bmpinitbg,bmpmore1,bmpmore2,bmpplay_up,bmpplay_down,bmpachi_up,
					bmpachi_down,bmpopen_up,bmpopen_down;
	
	private boolean isPlay_down,isAchi_down,isOpen_down,isMore_down;

	private int initbgW,initbgH;//GameMenu ����ͼƬ�Ŀ�͸�
	
	private float scaleWith,scaleHeight;//GameMenu�� ͼƬ ��͸ߵ����ű���
	
	private Bitmap play,achi,open,more; //��ʼ���ɾͣ�GameBox �������ࡱ��ť��ͼƬ
	
	//	private int bgX,bgY,bm
	public Game_Menu(Bitmap bmpinitbg,Bitmap bmpmore1,Bitmap bmpmore2,
			Bitmap bmpplay_up,Bitmap bmpplay_down,Bitmap bmpachi_up,
			Bitmap bmpachi_down,Bitmap bmpopen_up,Bitmap bmpopen_down,Context context){
		this.context=context;
		this.bmpinitbg = bmpinitbg;//GameMenu ����ͼƬ
		this.bmpmore1 = bmpmore1;//GameMenu �����ࡱδ����ͼƬ
		this.bmpmore2 = bmpmore2;//GameMenu �����ࡱ���±���ͼƬ
		this.bmpplay_up = bmpplay_up;//GameMenu ��ʼδ����ͼƬ
		this.bmpplay_down = bmpplay_down;//GameMenu ��ʼ����ͼƬ
		this.bmpachi_up = bmpachi_up;//GameMenu �ɾ�δ����ͼƬ
		this.bmpachi_down = bmpachi_down;//GameMenu �ɾͰ���ͼƬ
		this.bmpopen_up = bmpopen_up;//GameMenu GameBoxδ����ͼƬ
		this.bmpopen_down = bmpopen_down;//GameMenu GameBox���±���ͼƬ
		//��ʼ�� ͼƬ�Ƿ���
		this.isPlay_down = false;
		this.isAchi_down = false;
		this.isOpen_down = false;
		this.isMore_down = false;
		//��ʼ�� ��ť��ͼƬ
		play = bmpplay_up;
		achi = bmpachi_up;
		open = bmpopen_up;
		more = bmpmore1;
		
		
		
		this.initbgW = bmpinitbg.getWidth();//��ȡ����ͼƬ�Ŀ�
		this.initbgH = bmpinitbg.getHeight();//��ȡ����ͼƬ�ĸ�
		
		scaleWith = (float)NinjaRushSurfaceView.screenW/initbgW;//��ȡ��ı���
		scaleHeight = (float)NinjaRushSurfaceView.screenH/initbgH;//��ȡ�ߵı���
		
	}
	
	//��ͼ����
		public void draw(Canvas canvas,Paint paint){
			//���Ʊ���ͼƬ
			canvas.drawBitmap(bmpinitbg, null, new Rect(0, 0, NinjaRushSurfaceView.screenW,NinjaRushSurfaceView.screenH), paint);
			//���� "����"ͼƬ
			canvas.drawBitmap(more, null, new Rect(0, 0,NinjaRushSurfaceView.screenW/4,NinjaRushSurfaceView.screenH/3), paint);
			//���ƿ�ʼͼƬ
			canvas.drawBitmap(play, null, new Rect(NinjaRushSurfaceView.screenW*60/100, NinjaRushSurfaceView.screenH*38/100, NinjaRushSurfaceView.screenW*90/100, NinjaRushSurfaceView.screenH*58/100), paint);
			//���� �ɾ� ͼƬ
			canvas.drawBitmap(achi, null, new Rect(NinjaRushSurfaceView.screenW*60/100, NinjaRushSurfaceView.screenH*55/100, NinjaRushSurfaceView.screenW*90/100, NinjaRushSurfaceView.screenH*75/100), paint);
			//���� GAME BOx ͼƬ
			canvas.drawBitmap(open, null, new Rect(NinjaRushSurfaceView.screenW*60/100, NinjaRushSurfaceView.screenH*75/100, NinjaRushSurfaceView.screenW*90/100, NinjaRushSurfaceView.screenH*95/100), paint);
	
		}
		
		//�߼�����
		public void logic(){
			if(isPlay_down){
				play = bmpplay_down;
			}else{
				play = bmpplay_up;
			}
			
			if(isAchi_down){
				achi = bmpachi_down;
				}else{
				achi = bmpachi_up;
				}
			if(isOpen_down){
				open = bmpopen_down;
			}else{
				open = bmpopen_up;
			}
			
			if(isMore_down){
				more = bmpmore2;
			}else{
				more = bmpmore1;
			}
			
		}
		
		//�¼�����
		public void onTouchEvent(MotionEvent event){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				if(new Rect(NinjaRushSurfaceView.screenW*60/100, NinjaRushSurfaceView.screenH*38/100, NinjaRushSurfaceView.screenW*90/100, NinjaRushSurfaceView.screenH*58/100).contains((int)event.getX(), (int)event.getY())){
					isPlay_down = true;
				}
				else if(new Rect(NinjaRushSurfaceView.screenW*60/100, NinjaRushSurfaceView.screenH*55/100, NinjaRushSurfaceView.screenW*90/100, NinjaRushSurfaceView.screenH*75/100).contains((int)event.getX(), (int)event.getY())){
					isAchi_down = true;
				}
				else if(new Rect(NinjaRushSurfaceView.screenW*60/100, NinjaRushSurfaceView.screenH*75/100, NinjaRushSurfaceView.screenW*90/100, NinjaRushSurfaceView.screenH*95/100).contains((int)event.getX(),(int) event.getY())){
					isOpen_down = true;
				}
				else if( new Rect(0, 0,NinjaRushSurfaceView.screenW/4,NinjaRushSurfaceView.screenH/3).contains((int)event.getX(),(int) event.getY())){
					isMore_down = true;
				}
			}
			
			if(event.getAction() == MotionEvent.ACTION_UP){
				//��ʼ�ļ���
				if(new Rect(NinjaRushSurfaceView.screenW*60/100, NinjaRushSurfaceView.screenH*38/100, NinjaRushSurfaceView.screenW*90/100, NinjaRushSurfaceView.screenH*58/100).contains((int)event.getX(), (int)event.getY())){
					NinjaRushSurfaceView.status=Tools.GAME_LOADING;
					
				}
				//�ɾ͵ļ���
				else if(new Rect(NinjaRushSurfaceView.screenW*60/100, NinjaRushSurfaceView.screenH*55/100, NinjaRushSurfaceView.screenW*90/100, NinjaRushSurfaceView.screenH*75/100).contains((int)event.getX(), (int)event.getY())){
					Intent intent=new Intent(context, UserAchieve.class);
					context.startActivity(intent);
				}
				//�˳���ť�ļ���
				else if(new Rect(NinjaRushSurfaceView.screenW*60/100, NinjaRushSurfaceView.screenH*75/100, NinjaRushSurfaceView.screenW*90/100, NinjaRushSurfaceView.screenH*95/100).contains((int)event.getX(),(int) event.getY())){
					NinjaRushSurfaceView.flag=false;
					GameMusic.releaseMusic();//�ͷű���������Դ
					GameMusic.releaseSound();//�ͷ� ��Ч��Դ
					System.exit(0);
				}
				//���ఴť�ļ���
				else if( new Rect(0, 0,NinjaRushSurfaceView.screenW/4,NinjaRushSurfaceView.screenH/3).contains((int)event.getX(),(int) event.getY())){
					Intent intent=new Intent(context, DeveloperActivity.class);
					context.startActivity(intent);
				}
				isPlay_down = false;
				isAchi_down = false;
				isOpen_down = false;
				isMore_down = false;
			}
			
		}
		
		
			
			
}
