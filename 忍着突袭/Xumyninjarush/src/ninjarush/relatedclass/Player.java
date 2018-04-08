package ninjarush.relatedclass;

import ninjarush.mainactivity.R;
import ninjarush.mainactivity.UserAchieve;
import ninjarush.mainsurfaceview.NinjaRushSurfaceView;
import ninjarush.music.GameMusic;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;

public class Player {
	private Bitmap blood,changeToDart,dartMan,hurtBlood,changeff,dartShow,rope,ropef,protectDart,leaf,ropeMan,bulletMan;
	//���ǳԵ���ʳ������
	private int totalFood;
	public static int dartManWeight;
	//������������̧���ʱ���Ƿ���Ҫ������Ծ�ȵĶ���
	private float x=0,y=0;
	//���ǵ�xy����
	private int tx,ty;
	//�����Ƿ�����
	private boolean isPlayerDead;
	//�����Ƿ���ľ׮֮��
	private boolean isUpOfPale;
	//�����Ƿ��Ѫ
	private boolean isHurt;
	//�����Ƿ��޵�
	private boolean undead;
	//���ǳԵ�ʳ���Ժ������״̬
	private boolean isStar;
	//���ǵ�Ѫ��
	private int playerBlood;
	//���ǵ��˶�״̬
	private int playerStatus;
	//���ǵĳԵ����ڵĸ���
	private int foodMount;
	private Rect rect;
	//���ǻ�ͼ���Ƶļ�����
	private int currentPlayer;
	//���ǵ�Ѫ���Ƶļ�����
	private int currentHurtBlood;
	//y�����ϵ���Ծ����
	private int distense;
	//y������ǰ���ľ���
	private int currentDistense;
	//��Ծ�ļ����������Ϊ����
	private int jumpTime;
	//����������������Ծ��״̬
	private boolean jumpUp,jumpDown;
	//���ǽ���dart״̬��Ķ����л�������
	private int currentDart;
	//����תȦ����ʱ��
	private int cycleTime;
	//����תȦ�ĵ�ǰʱ��
	private int currentCycle;
	//���ǳ�������ʳ����������������ʼʱ��
	private int jump;
	//���ǳԵ�ʳ���Ժ��������˸Ч�������л�������
	private int currentStar;
	//����״̬��������ʾ�ٶ�Ч���ı���,��ʼ��Ϊ0��֮��ѭ���ۼ�
	private int unDone;
	//������ʱ��paint͸���ȵĸı�
	private int alpha;
	//�ж�ͷ���Ƿ���ľ׮
	private boolean isPaleOnHead;
	//�Ƿ���Ҫ��������״̬
	private boolean isDrawRope;
	//�������ڵ�ʱ��
	private int ropeTime;
	//��ǰ�������ڵ�ʱ��
	private int currentRope;
	//����������Ҫ��pale���·������꣬�Լ���Ļ���ٶ�
	private int paleBottom,speedScreen;
	//����ͷ����tx�ľ���
	int distenceRopef;
	//����������
	int ropeMount;
	//������ֵ��жϲ���ֵ
	private boolean isProtectDart;
	//������ֵ���ʱ��
	private int protectTime;
	//������ֵĵ�ǰʱ��
	private int currentProtect;
	//�������л��ļ�����
	private int currentPAction;
	//�޵�תȦʱ�Ƿ���Ҫ��Ӱ�Ĳ�������ֵ
	private boolean isNeedShadow;
	//��Ҷ�Ķ����л�������
	private int currentLeaf;
	//Ҷ�ӵ�xyֵ
	private int leafX1,leafY1,leafX2,leafY2,leafX3,leafY3;
	//��Ծ������1�䵽2ʱ����һ�����ݵ��¶׶���
	private boolean isChangeToTow;
	//���ƶ��ݶ����ļ�����������Ϊ0��ʱ��������״̬
	private int totalChangeTime;
	//�������޵�תȦ֮����Ҫ�ͷ�һ���ӵ��Ŀ��Ʋ���ֵ
	private boolean isShootMore;
	//�����Ƿų�����ʱ�Ķ�������
	private boolean isBullet;
	//�Ƿ���Ծ������
	public  boolean isJumpTwice;
	//���������Դ���������Ļ������һ
	public boolean isScreenDown;
	public Player(Bitmap blood,Bitmap changetodart,Bitmap dartman,Bitmap hurtblood,Bitmap changeff,Bitmap dartShow,Bitmap rope,Bitmap ropef,Bitmap protectDart,Bitmap leaf,Bitmap ropeMan,Bitmap bulletMan){
		this.blood=blood;
		this.changeToDart=changetodart;
		this.dartMan=dartman;
		this.hurtBlood=hurtblood;
		this.changeff=changeff;
		this.dartShow=dartShow;
		this.rope=rope;
		this.ropef=ropef;
		this.protectDart=protectDart;
		this.leaf=leaf;
		this.ropeMan=ropeMan;
		this.bulletMan=bulletMan;
		dartManWeight=dartMan.getWidth()/10;
		isUpOfPale=false;
		isHurt=false;
		undead=false;
		jumpUp=false;
		jumpDown=true;
		isPlayerDead=false;
		isPaleOnHead=false;
		isDrawRope=false;
		isStar=false;
		isProtectDart=false;
		isNeedShadow=false;
		isChangeToTow=false;
		isShootMore=false;
		isBullet=false;
		isJumpTwice=false;
		//���ǵ�Ѫ��
		playerBlood=5;//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//�Ե�ʳ�������
		foodMount=0;
		//���ֶ����������ĳ�ʼ��
		currentPlayer=0;
		distense=0;
		currentDistense=0;
		currentHurtBlood=0;
		currentStar=0;
		unDone=0;
		currentPAction=0;
		currentLeaf=0;
		totalFood=0;
		//����תȦ�����Ķ���������
		currentDart=0;
		//���ǵ�ǰתȦ��ʱ��
		currentCycle=0;
		//����תȦ����ʱ��
		cycleTime=80;
		//�������ڵ����ʱ��
		ropeTime=6;
		//��ǰ������ʱ�������
		currentRope=0;
		//�������ʱ��
		protectTime=50;
		//����ĵ�ǰʱ��
		currentProtect=0;
		totalChangeTime=1;
		alpha=255;
		jump=1;
		playerStatus=Tools.PLAYERWALK;
		tx=Tools.NINJA_X;
		ty=100;
		rect=new Rect(tx, ty, tx+dartMan.getWidth()/10, ty+dartMan.getHeight());
	}
	
	public void draw(Canvas canvas,Paint paint){
		
		//������Ծʱ�Ļ�ͼ
		if(jumpUp&&!isDrawRope&&!isChangeToTow&&playerStatus!=Tools.PLAYERDART){
			//�����ܵ�ʱ�򣬲����Ķ���Ч��
			if(playerStatus==Tools.PLAYERRUN){
				for(int i=0;i<4;i++){
					if(i!=unDone){
						paint.setAlpha(alpha-60*(4-i));
						canvas.save();
						canvas.clipRect(tx-dartMan.getWidth()*(4-i)*2/50+4, ty+6*(4-i), tx-dartMan.getWidth()*(4-i)*2/50+4+dartMan.getWidth()/10, ty+6*(4-i)+dartMan.getHeight());
						canvas.drawBitmap(dartMan, tx-dartMan.getWidth()*(4-i)*2/50+4-5*dartMan.getWidth()/10, ty+6*(4-i), paint);
						canvas.restore();
					}
				}
				unDone++;
				if(unDone==4)
					unDone=0;
				paint.setAlpha(255);
			}
			if(isBullet){
				isBullet=false;
				canvas.drawBitmap(bulletMan, tx, ty, paint);
			}
			else{
				canvas.save();
				canvas.clipRect(rect);
				canvas.drawBitmap(dartMan	, tx-dartMan.getWidth()*7/10, ty, paint);
				canvas.restore();
			}
		}
		else{
			//������Ծʱ�Ļ�ͼ
			if(jumpDown&&!isDrawRope&&!isChangeToTow&&playerStatus!=Tools.PLAYERDART){
				//�����ܵ�ʱ�򣬲����Ķ���Ч��
				if(playerStatus==Tools.PLAYERRUN){
					for(int i=0;i<4;i++){
						if(i!=unDone){
							paint.setAlpha(alpha-60*(4-i));
							canvas.save();
							canvas.clipRect(tx-dartMan.getWidth()*(4-i)*2/50+4, ty-6*(4-i), tx-dartMan.getWidth()*(4-i)*2/50+4+dartMan.getWidth()/10, ty-6*(4-i)+dartMan.getHeight());
							canvas.drawBitmap(dartMan, tx-dartMan.getWidth()*(4-i)*2/50+4-5*dartMan.getWidth()/10, ty-6*(4-i), paint);
							canvas.restore();
						}
					}
					unDone++;
					if(unDone==4)
						unDone=0;
					paint.setAlpha(255);
				}
				if(isBullet){
					isBullet=false;
					canvas.drawBitmap(bulletMan, tx, ty, paint);
				}
				else{
					canvas.save();
					canvas.clipRect(rect);
					canvas.drawBitmap(dartMan	, tx-dartMan.getWidth()*6/10, ty, paint);
					canvas.restore();
				}
			}else{
				//������ʱ��ͼ
				if(isDrawRope&&!isChangeToTow&&playerStatus!=Tools.PLAYERDART){
					if(currentRope<=ropeTime){
						canvas.drawBitmap(ropef, distenceRopef, paleBottom, paint);
						for(int i=0;i<ropeMount;i++){
							canvas.drawBitmap(rope,distenceRopef+ropef.getWidth()/2+ (tx+ropeMan.getWidth()-distenceRopef-ropef.getWidth()/2)*i/ropeMount, paleBottom+ropef.getHeight()+i*rope.getHeight(), paint);
						}
						currentRope++;
						distenceRopef-=speedScreen;
					}else{
						currentRope=0;
						isDrawRope=false;
						isUpOfPale=false;
						jumpDown=false;
						//�������֮��Ķ�����Ծ����
						jumpUp=true;
						distense=4;
						currentDistense=0;
					}
					//�����ܵ�ʱ�򣬲����Ķ���Ч��
					if(playerStatus==Tools.PLAYERRUN){
						for(int i=0;i<4;i++){
							if(i!=unDone){
								paint.setAlpha(alpha-63*(4-i));
								canvas.save();
								canvas.clipRect(tx-dartMan.getWidth()*(4-i)*2/50+4, ty-6*(4-i), tx-dartMan.getWidth()*(4-i)*2/50+4+dartMan.getWidth()/10, ty-6*(4-i)+dartMan.getHeight());
								canvas.drawBitmap(dartMan, tx-dartMan.getWidth()*(4-i)*2/50+4-5*dartMan.getWidth()/10, ty-6*(4-i), paint);
								canvas.restore();
							}
						}
						unDone++;
						if(unDone==4)
							unDone=0;
						paint.setAlpha(255);
					}
					//������ʱ����Ļ���
					if(isBullet){
						isBullet=false;
						canvas.drawBitmap(bulletMan, tx, ty, paint);
					}
					else{
						canvas.drawBitmap(ropeMan, tx, ty, paint);
					}
				}else {
					//�����������ʱ������Ծ�Ĺ��̣��������Ӧ�Ļ���
					if(!isChangeToTow){
						//������״̬����Ļ���
						switch(playerStatus){
						//���ߵ�״̬
						case Tools.PLAYERWALK:
							//�Խ���Ҷ�ӵĻ���--Ҷ������Ƭ
							canvas.save();
							canvas.clipRect(leafX1, leafY1, leafX1+leaf.getWidth()/4, leafY1+leaf.getHeight());
							canvas.drawBitmap(leaf, leafX1-currentLeaf*leaf.getWidth()/4, leafY1, paint);
							canvas.restore();
							canvas.save();
							canvas.clipRect(leafX2, leafY2, leafX2+leaf.getWidth()/4, leafY2+leaf.getHeight());
							canvas.drawBitmap(leaf, leafX2-leaf.getWidth()/4, leafY2, paint);
							canvas.restore();
							canvas.save();
							canvas.clipRect(leafX3, leafY3, leafX3+leaf.getWidth()/4, leafY3+leaf.getHeight());
							canvas.drawBitmap(leaf, leafX3-leaf.getWidth()/4, leafY3, paint);
							canvas.restore();
							//������Ļ���
							if(isBullet){
								isBullet=false;
								canvas.drawBitmap(bulletMan, tx, ty, paint);
							}
							else{
								canvas.save();
								canvas.clipRect(rect);
								canvas.drawBitmap(dartMan, tx-currentPlayer*dartMan.getWidth()/10, ty, paint);
								canvas.restore();
								}
							break;
							//���ܵ�״̬
						case Tools.PLAYERRUN:
							for(int i=0;i<4;i++){
								if(i!=unDone){
									paint.setAlpha(alpha-60*(4-i));
									canvas.save();
									canvas.clipRect(tx-dartMan.getWidth()*(4-i)*2/50+4, ty, tx-dartMan.getWidth()*(4-i)*2/50+4+dartMan.getWidth()/10, ty+dartMan.getHeight());
									canvas.drawBitmap(dartMan, tx-dartMan.getWidth()*(4-i)*2/50+4-5*dartMan.getWidth()/10, ty, paint);
									canvas.restore();
								}
							}
							unDone++;
							if(unDone==4)
								unDone=0;
							paint.setAlpha(255);
							if(isBullet){
								isBullet=false;
								canvas.drawBitmap(bulletMan, tx, ty, paint);
							}
							else{
								canvas.save();
								canvas.clipRect(tx, ty, tx+dartMan.getWidth()/10, ty+dartMan.getHeight());
								canvas.drawBitmap(dartMan,tx-currentPlayer*dartMan.getWidth()/10, ty, paint);
								canvas.restore();
							}
							break;
							//�޵�תȦ��״̬
						case Tools.PLAYERDART:
							if(undead){
								if(isNeedShadow)
									for(int i=0;i<4;i++){
										if(i!=unDone){
											paint .setAlpha(alpha-60*(i+1));
											canvas.save();
											canvas.clipRect(tx-10*(i+1), ty, tx-5*(i+1)+changeToDart.getWidth()/11, ty+changeToDart.getHeight());
											canvas.drawBitmap(changeToDart, tx-5*(i+1)-7*changeToDart.getWidth()/11, ty, paint);
											canvas.restore();
										}
									}
								unDone++;
								if(unDone==4)
									unDone=0;
								paint.setAlpha(255);
								canvas.save();
								canvas.clipRect(tx,ty,tx+changeToDart.getWidth()/11,ty+changeToDart.getHeight());
								canvas.drawBitmap(changeToDart, tx-currentDart*changeToDart.getWidth()/11, ty, paint);
								canvas.restore();
								
							}
							break;
						}
					}else{
						//���������ж�����ͣ�Ļ�Ӱ����
						if(playerStatus==Tools.PLAYERRUN){
							for(int i=0;i<4;i++){
								if(i!=unDone){
									paint.setAlpha(alpha-60*(4-i));
									canvas.save();
									canvas.clipRect(tx-dartMan.getWidth()*(4-i)*2/50+4, ty, tx-dartMan.getWidth()*(4-i)*2/50+4+dartMan.getWidth()/10, ty+dartMan.getHeight());
									canvas.drawBitmap(dartMan, tx-dartMan.getWidth()*(4-i)*2/50+4-6*dartMan.getWidth()/10, ty, paint);
									canvas.restore();
								}
							}
							unDone++;
							if(unDone==4)
								unDone=0;
							paint.setAlpha(255);
						}
						//������ͣʱ�Ļ���
						if(isBullet)
							canvas.drawBitmap(bulletMan, tx, ty, paint);
						else{
							canvas.save();
							canvas.clipRect(rect);
							canvas.drawBitmap(dartMan	, tx-dartMan.getWidth()*6/10, ty, paint);
							canvas.restore();
						}
					}
				}
			}
		}
		//������Ѫ���Ļ���
		for(int i=0;i<playerBlood;i++){
			canvas.drawBitmap(blood, i*(blood.getWidth()+4), 0, paint);
		}
//		switch(playerBlood){
//		case 3:
//			canvas.drawBitmap(blood, 0, 0, paint);
//			canvas.drawBitmap(blood, blood.getWidth()+4, 0, paint);
//			canvas.drawBitmap(blood,2*blood.getWidth()+8, 0, paint);
//			break;
//		case 2:
//			canvas.drawBitmap(blood, 0, 0, paint);
//			canvas.drawBitmap(blood, blood.getWidth()+4, 0, paint);
//			break;
//		case 1:
//			canvas.drawBitmap(blood, 0, 0, paint);
//			break;
//		}
		
		//�Ե�ʳ���ʳ�������Ļ���
		switch(foodMount){
		case 1:
			canvas.drawBitmap(dartShow, 0, NinjaRushSurfaceView.screenH/3, paint);
			break;
		case 2:
			canvas.drawBitmap(dartShow, 0, NinjaRushSurfaceView.screenH/3, paint);
			canvas.drawBitmap(dartShow, dartShow.getWidth()+5, NinjaRushSurfaceView.screenH/3, paint);
			break;
		}
		//����ʱ��Ѫ�Ļ���
		if(isHurt){
			canvas.save();
			canvas.clipRect(tx,ty,tx+hurtBlood.getWidth()/5,ty+hurtBlood.getHeight());
			canvas.drawBitmap(hurtBlood	, tx-currentHurtBlood*hurtBlood.getWidth()/5, ty, paint);
			canvas.restore();
		}
		//�Ե�ʳ��ʱ��������˸Ч��
		if(isStar){
			canvas.save();
			canvas.clipRect(tx-8, ty, tx+changeff.getWidth()/9, ty+changeff.getHeight());
			canvas.drawBitmap(changeff, tx-8-currentStar*changeff.getWidth()/9, ty, paint);
			canvas.restore();
		}
		//�����Ϲֺ��޵д�Ȧ�Ļ���
		if(undead&&isProtectDart){
			currentProtect++;
			if(currentProtect<=protectTime){
				canvas.save();
				canvas.clipRect(tx-protectDart.getWidth()/10+dartMan.getWidth()/20, ty-protectDart.getHeight()/2+dartMan.getHeight()/2, tx-protectDart.getWidth()/10+dartMan.getWidth()/20+protectDart.getWidth()/5,  ty-protectDart.getHeight()/2+dartMan.getHeight()/2+protectDart.getHeight());
				canvas.drawBitmap(protectDart, tx-protectDart.getWidth()/10+dartMan.getWidth()/20-currentPAction*protectDart.getWidth()/5,  ty-protectDart.getHeight()/2+dartMan.getHeight()/2, paint);
				canvas.restore();
			}else{
				undead=false;
				isProtectDart=false;
				currentProtect=0;
				currentPAction=0;
			}
		}
		
	}
	//�߼�������
	public void logic(){
		
		//���������ϻ�������Ծ��״̬ʱ�������Ƕ������л�
		if(!jumpDown&&!jumpUp&&!isChangeToTow&&!isBullet){
			currentPlayer++;
		if(currentPlayer==9)
			currentPlayer=0;
		}
		//������ʱѪ�Ķ������л�
		if(isHurt){
			currentHurtBlood++;
			if(currentHurtBlood==4){
				currentHurtBlood=0;
				isHurt=false;
			}
		}
		//���Ե�ʳ���Ժ����˸Ч�������л�
		if(isStar){
			currentStar++;
			if(currentStar>8){
				currentStar=0;
				isStar=false;
			}
		}
		//���ڳ����������ں��޵е�ʱ������е���Ӧ�Ķ������л�
		if(undead&&!isChangeToTow&&playerStatus==Tools.PLAYERDART){
			//������ʼ�׶ε���������jumpΪ�䶯����ʱ����
			jump++;
			if(jump%1==0&&currentDart<5){
				//�޵�תȦ��ʱ���ˣ�ֹͣ��·���ϵ��ܲ�����
				GameMusic.pauseRun();
				
				GameMusic.pauseWind();//��ͣ����
				//ʱ����Ҫ�ı�ͼƬ�����ﶯ���л��Ŀ�����currentDart
				currentDart++;
				//ÿ��Ծһ�ξͻ�������һ���ľ���
				ty-=Tools.NINJA_SPEED*4/3;
			}
			//תȦʱ�䵽�ˣ����������Ӧ�Ĳ���
			if(currentDart>=5&&currentCycle<=cycleTime){
				isNeedShadow=true;
				//currentCyΪתȦʱ//��������ʱ���߼��������������������޵�תȦ��ʱ��ֹͣ������Ŀ�����
				currentCycle++;
				currentDart++;
				if(Tools.countUndead++ == 0){
					
					Tools.sound_Undead = GameMusic.playSound(R.raw.changed, 0);//����޵е���Ч
				}
				
				
				
				//��תȦʱ���ڣ�currentDһֱ��5-9֮���л�
				if(currentDart>=9)
					currentDart=5;
			}else if(currentCycle>cycleTime&&currentDart<11&&currentDart>=5){
				currentDart++;//��תȦʱ�����������ָ�뻹��5-9�ķ�Χʱ����������
				if(currentDart==10)
					isShootMore=true;
				isNeedShadow=false;
			}
			
			if(currentDart==11){
				
				Tools.countUndead = 0;
				
				
				
				currentCycle=0;
				currentDart=0;
				jump=1;
				undead=false;
				setStatus(Tools.PLAYERWALK);
				isUpOfPale=false;
				jumpUp=false;
				jumpDown=true;
			}
		}
		//��������ʱ���߼��������������������޵�תȦ��ʱ��ֹͣ����
		if(jumpUp&&!isDrawRope&&playerStatus!=Tools.PLAYERDART&&!isChangeToTow){
			currentDistense++;
			if(currentDistense>=distense){
				currentDistense=0;
				jumpUp=false;
				jumpDown=true;
				return;
			}
				ty-=Tools.NINJA_SPEED;
		}
		//�����ʱ��������������ģ�������ľ׮�����ӳ��������ǽ����޵�תȦ��ʱ��ֹͣ������һֱ����ֱ������
		if(jumpDown&&!isUpOfPale&&!isDrawRope&&playerStatus!=Tools.PLAYERDART&&!isChangeToTow){
			ty+=Tools.NINJA_SPEED;
			if(ty>=NinjaRushSurfaceView.screenH)
				isPlayerDead=true;
		}
		//������ʱ������ͣЧ����ʱ�����
		if(isChangeToTow){
			totalChangeTime--;
			if(totalChangeTime<0){
				totalChangeTime=1;
				isChangeToTow=false;
			}
		}
		//�Թ���Ķ����л�����
		if(undead&&isProtectDart){
			currentPAction++;
			if(currentPAction==5)
				currentPAction=0;
		}
		//��Ҷ�ӵ��߼�����
		if(playerStatus==Tools.PLAYERWALK&&!jumpDown&&!jumpUp){
			currentLeaf++;
			leafX1-=5;
			leafY1-=1;
			leafX3-=3;
			leafY3-=2;
			if(currentLeaf==4){
				currentLeaf=0;
				leafX1=tx-leaf.getWidth()/16;
				leafY1=ty+dartMan.getHeight()*4/5;
				leafX3=tx-leaf.getWidth()/12;
				leafY3=ty+dartMan.getHeight()*5/6;
			}
		}else{
			currentLeaf=0;
			leafX1=tx-leaf.getWidth()/16;
			leafY1=ty+dartMan.getHeight()*4/5;
			leafX3=tx-leaf.getWidth()/12;
			leafY3=ty+dartMan.getHeight()*5/6;
		}
		//Ϊrect����ֵ
		rect.set(tx, ty, tx+dartMan.getWidth()/10, ty+dartMan.getHeight());
	}
	//ontouch����
	public void ontouch(MotionEvent event,int[] position,int[] position2) {
		//����ڷ����ڵ������ڣ��򲻲���
		if((event.getX()>=position[0]&&event.getX()<=position[0]+position[2])&&(event.getY()>=position[1]&&event.getY()<=position[1]+position[3])){
			isBullet=true;
			return;
		}
		//�������ͣ�������ڣ��򲻲���
		if((event.getX()>=position2[0]&&event.getX()<=position2[0]+position2[2])&&(event.getY()>=position2[1]&&event.getY()<=position2[1]+position2[3])){
			return;
		}
		if(playerStatus==Tools.PLAYERDART)
			return;
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			x= event.getX();
			y= event.getY();
		}
		if(event.getAction()==MotionEvent.ACTION_UP&&playerStatus!=Tools.PLAYERDART){
			//��ͷ����ľͷ��ʱ����Ծ��������ִ�У�ֻ��ִ���������Ķ�������ʱִֻ����Ծ�Ķ���
			if((event.getX()>=x-NinjaRushSurfaceView.screenW/16&&event.getX()<=x+NinjaRushSurfaceView.screenW/16)&&(event.getY()>=y-NinjaRushSurfaceView.screenH/32&&event.getY()<=y+NinjaRushSurfaceView.screenH/32)&&!isPaleOnHead){
				jumpTime++;
				if(Tools.isJump){
					GameMusic.playSound(R.raw.jump, 0);//jump ��Ч
					Tools.isJump=false;
					GameMusic.pauseRun();//����run ��Ч
				}
				if(jumpTime>=2){
					isJumpTwice=true;
					return;
					}
				jumpUp=true;
				jumpDown=false;
				isUpOfPale=false;
				if(jumpTime==1){
					
					distense=Tools.FIRSTDISTENSE;
				}
				////������ ����  ����ж�������ֻ�еڶ�������ʱ��ִ�еڶ���������������Ĳ�������
				else if(jumpTime==2){
					isChangeToTow=true;
					distense=Tools.SECONDDISTENSE;
					currentDistense=0;
				}
			}
			//ִ���������Ķ�������
			if((event.getX()>=x-NinjaRushSurfaceView.screenW/16&&event.getX()<=x+NinjaRushSurfaceView.screenW/16)&&(event.getY()>=y-NinjaRushSurfaceView.screenH/32&&event.getY()<=y+NinjaRushSurfaceView.screenH/32)&&isPaleOnHead){
				if(!isDrawRope){
					isDrawRope=true;
					distenceRopef=tx;
					ropeMount=(ty-paleBottom-ropef.getHeight())/rope.getHeight();
//					jumpTime=1;
					}
				}
			//���һ�����Ļ�����Ǳ���--������ľ׮��ʱ�Żᱼ��
			if(event.getX()-x>=NinjaRushSurfaceView.screenW/20&&isUpOfPale){
				setStatus(Tools.PLAYERRUN);
				if(Tools.countWind++ == 0){
					GameMusic.startWind();
				}
				else{
					GameMusic.nextWind(R.raw.wind);
				}
			}
			//���󻬶���Ļ�����ǿ�ʼ�߶�--������ľ׮��ʱ�ſ��Ըı�״̬
			if(x-event.getX()>=NinjaRushSurfaceView.screenW/20&&isUpOfPale){
				setStatus(Tools.PLAYERWALK);
				GameMusic.pauseWind();
			}
		}
		
	}
	//���س�Ա��λ�ú���
	public int[] getPosition(){
		switch(playerStatus){
		case Tools.PLAYERWALK:
			int[] p={tx,ty,dartMan.getWidth()/10,dartMan.getHeight()};
			return p;
		case Tools.PLAYERRUN:
			int[] p1={tx,ty,dartMan.getWidth()/10,dartMan.getHeight()};
			return p1;
		case Tools.PLAYERDART:
			int[] p2={tx,ty,changeToDart.getWidth()/11,changeToDart.getHeight()};
			return p2;
		}
		return null;
	}
	//��ȡ�����Ƿ�����״̬
	public boolean getIsPlayerDead() {
		return isPlayerDead;
	}
	//��ȡ�Ƿ�ͷ����ľ׮��״̬
	public boolean getIsUpOfPale() {
		return isUpOfPale;
	}
	//���м����û��Ƿ��䵽ľ׮֮��
	public void setIsUpOfPale(boolean UpOfPale) {
		if(!isUpOfPale&&UpOfPale){
			currentLeaf=0;
			leafX1=tx-leaf.getWidth()/16;
			leafY1=ty+dartMan.getHeight()*4/5;
			leafX3=tx-leaf.getWidth()/12;
			leafY3=ty+dartMan.getHeight()*5/6;
			///4yue18ri gai  
			isScreenDown=false;
		}
		if(UpOfPale){
			this.isUpOfPale = UpOfPale;
			isJumpTwice=false;
			jumpDown=false;
			jumpUp=false;
			jumpTime=0;
			Tools.isJump=true;	
			if(Tools.countRun++==0){
				GameMusic.startRun();//���� run��Ч
			}
			else if(!GameMusic.mprun.isPlaying()){
				GameMusic.nextrun(R.raw.run);
			}
		
		}		
			if(!UpOfPale&&!jumpUp){
				this.isUpOfPale=UpOfPale;
				jumpDown=true;
				jumpUp=false;
			
				
				
			}
		
		
	}
	public int getPlayerStatus() {
		return playerStatus;
	}

	//����������˲�����ײ��ʱ�����Ĳ���

	public void setIsHurt() {
		//��������޵�״̬���Ѫ
		if(!undead){
			isHurt=true;
			playerBlood--;
			GameMusic.playSound(R.raw.hurt, 0);
			if(playerBlood<0)
				isPlayerDead=true;
		}
	}
	//��������Ƿ��޵е�״̬
	public boolean getIsUndead() {
		return undead;
	}
	public void setUndead(boolean undead) {
		this.undead = undead;
	}

	//������������״̬���л�
	public void setStatus(int status){
		this.playerStatus=status;
		switch(playerStatus){
		case Tools.PLAYERWALK:
			rect.set(tx, ty, tx+dartMan.getWidth()/10, ty+dartMan.getHeight());
			currentCycle=0;
			currentDart=0;
			isNeedShadow=false;
			break;
		case Tools.PLAYERRUN:
			break;
		case Tools.PLAYERDART:
			ty=ty+dartMan.getHeight()-changeToDart.getHeight();
			break;
		}
	}
	
	public boolean getIsShootMore() {
		return isShootMore;
	}

	public void setIsShootMore(boolean isShootMore) {
		this.isShootMore = isShootMore;
	}

	//�����ǳԵ�ʳ��ʱ�Ĳ���
	public void eatFood(int kind){
		//���ǳԵ�10��ʳ���ʱ�������Ӧ�ĳɾ�
		totalFood++;
		GameMusic.playSound(R.raw.eat, 0);//�Զ��� �� ��Ч
		if(totalFood==10)
			UserAchieve.userAchieve[4]=1;
		
		if(kind==Tools.FOODBULLET){
			this.foodMount++;
			isStar=true;
			if(foodMount>=3){
				setStatus(Tools.PLAYERDART);
				foodMount=0;
				undead=true;
				isProtectDart=false;
			}
		}
		if(kind==Tools.FOODHEART){
				playerBlood++;
		}
	}
	//�����Ƿ�ľ׮���Դ���
	public void setIsPaleOnHead(boolean paleOnHead,int paleY,int speedScreen){
			if(paleOnHead){
				this.isPaleOnHead=paleOnHead;
				this.paleBottom=paleY;
				this.speedScreen=speedScreen;
				isScreenDown=true;
			}else{
				this.isPaleOnHead=paleOnHead;
				this.speedScreen=speedScreen;
			}
		
	}
	//�����޵д�Ȧ״̬
	//�������ǽ����޵д���Ļ���
	public void setIsProtectDart(){
		this.isProtectDart=true;
		undead=true;
	}
	//����������ʱ��ʵʱ��ȡ��Ļ���ƶ��ٶ�
	public void getScreenSpeed(int y){
		if(isDrawRope&&!isChangeToTow&&playerStatus!=Tools.PLAYERDART)
			paleBottom=y;
	}

	public boolean getIsDrawRope() {
		return isDrawRope;
	}
	public boolean getIsJumpTwice(){
		return this.isJumpTwice;
	}

	public boolean getIsScreenDown() {
		return isScreenDown;
	}

	public void setScreenDown(boolean isScreenDown) {
		this.isScreenDown = isScreenDown;
	}
	
}
