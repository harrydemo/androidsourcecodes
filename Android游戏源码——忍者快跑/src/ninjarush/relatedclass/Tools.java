package ninjarush.relatedclass;

import android.media.MediaPlayer;
import ninjarush.mainactivity.MyninjarushActivity;
import ninjarush.mainsurfaceview.NinjaRushSurfaceView;

public class Tools {
	//Sound ��Ч��Դ �� sound�е� id
	public static int sound_run;//run
	public static int sound_wind;//����
	public static int sound_Undead;//�޵�
	public static boolean isJump = false;
	public static int countRun = 0;
	public static int countWind = 0;
	public static int countUndead = 0;
	

	
	/////��Ϸ����
	public static final int GAME_LOADING=-1;//��ϷLoading����
	public static final int GAME_MENU=0;//��Ϸ �˵����泣��
	public static final int	GAME_PLAYING =1;//��Ϸ�н��� ����
	public static final int GAME_OVER=2;//��Ϸ�������泣��
	public static final int GAME_PAUSE = 3;//��Ϸ��ͣ���泣��
	public static final int GAME_MORE = 4;//������泣��
	public static final int GAME_BOX = 5;//GameBox ���泣��
	public static final int GAME_ACHI = 6;//�ɾͽ��泣��
	public static final int PREBGSPEED=5;
	public static final int LATERBGSPEED=2;
	//��Ϸ����Ļ���ƴ�����Y���곣��
	public static final int IS_BG_DOWN=NinjaRushSurfaceView.screenH/2;
	//�ŵ�����
	public static final int STYLE_LAND=0X111;
	public static final int STYLE_SKY=0X112;
	public static final int STYLE_START=0X113;	
	//���ߵ���ز���-----------------------------------------------------------------------------------------------------
	//��һ����Ծ�ľ���
		public static final int FIRSTDISTENSE=12;
		//�ڶ�����Ծ�ľ���
		public static final int SECONDDISTENSE=6;
		//�����߶����ܶ����޵�תȦ������״̬
		public static final int PLAYERWALK=0,PLAYERRUN=1,PLAYERDART=2;
		//ʳ�������
		public static final int FOODBULLET=100,FOODHEART=101;
		//���߳��ֵ�x����
		public static final int NINJA_X=NinjaRushSurfaceView.screenW/8;
		//���������ƶ����ٶ�
		public static final int NINJA_SPEED=NinjaRushSurfaceView.screenH/35;
	//���ߵ���ز���-----------------------------------------------------------------------------------------------------
		
		//�ӵ�����
		public static final int BULLETPLAYER=0x101;
		public static final int BULLETBOSS=0x102;
		public static final int BULLETPLAYERWUDIAFTER=0x103;
		public static final int BULLET_SPEED_BOSS=20;
		public static final int BULLET_SPEED_PLAYER=15;
		//������������
		public static final int DEAD_CROW=0x301;
		public static final int DEAD_ANT=0x302;
		public static final int DEAD_BOSS=0x303;
		//����
		public static final int ENEMY_CROW=0X324;
		public static final int ENEMY_ANT=0X322;
		public static final int ENEMY_DAO=0X323;
		public static boolean isCollision(int[] position1,int[] position2){
		if(position2[0]-position1[0]>=position1[2])
			return false;
		if(position1[0]-position2[0]>=position2[2])
			return false;
		if(position2[1]-position1[1]>=position1[3])
			return false;
		if(position1[1]-position2[1]>=position2[3])
			return false;
		return true;
	}
	
}
