package com.bn.d2.bill;
public class Constant {
	public static ScreenScaleResult ssr;//��Ļ�Ľ��
	public static float wRatio;//��Ӧȫ�������ű���
	public static float hRatio;
	public static int SCREEN_WIDTH;//��Ļ�Ŀ��
	public static int SCREEN_HEIGHT;//��Ļ�ĸ߶�
	//������̨�ĳ���
	public static float BOTTOM_WIDTH=85*2+168*2+95;//������̨����
	public static float BOTTOM_HEIGHT=85*2+228;//������̨��߶�
	public static float EDGE_BIG=45;//���Ե�ߴ�
	public static float EDGE_SMALL=40;//С��Ե�ߴ�
	public static float MIDDLE=95;//�м䶴�ĳߴ�
	public static float DIS_CORNER=20;//ͼƬ���϶���ͽ����ĸ����ľ���
	public static float DIS_MIDDLE=30;//ͼƬ���϶�����м䶴�ľ���
	
	public static float TABLE_X=20;//�������Ͻ�����
	public static float TABLE_Y=20;
	public static float HOLE_CENTER_REVISE=20;//��������������ֵ
	public static float CORNER_HOLE_R=23;//�ĽǶ��İ뾶
	public static float MIDDLE_HOLE_R=32f;//�м䶴�İ뾶
	//��Ϸ�������Ͻǵ�����
	public static float X_OFFSET;
	public static float Y_OFFSET;
	//��������ĳ���
	public static float BALL_SIZE=24;//��ֱ��
	public static float X_OFFESET_BALL1=350;//һ�������������Ͻǳ�ʼλ�ã�yֵ�̶������ɵ���
	public static float GAP_BETWEEN_BALLS=3;//������֮��ĳ�ʼ��϶
	public static float DIS_WITH_MAIN_BALL=238;//һ�����ĸ���ľ���
	public static float V_MAX=150;//�������ٶȣ��涨���������ٶȲ����Գ���100��
	public static float K=1.3f/V_MAX;//��֡�ٶ�ϵ��
	
	public static float TIME_SPAN=0.05f;//���˶���ģ��ʱ�������涨: timeSpan������>=Ball.d��
	public static float V_ATTENUATION=0.996f;//�ٶ�˥������
	public static float V_MIN=1.5f;//�ٶ���Сֵ�����ٶ�С�ڴ�ֵʱ��ֹͣ�˶�
	//������˵ĳ���
	public static float DIS_WITH_BALL=10;//�����ĸ���Ե�ľ���
	//�����������ĳ���
	public static float BAR_X=686;//������λ��
	public static float BAR_Y=90;
	public static float RAINBOW_WIDTH=37;//�ʺ������
	public static float RAINBOW_HEIGHT=9.22f;//�ʺ����߶�
	public static float RAINBOW_GAP=1f;//�ʺ�����϶
	public static float RAINBOW_X=BAR_X+7.5f;//��һ���ʺ�λ�õ���ֵ
	public static float RAINBOW_Y=BAR_Y-17;
	//���ڰ�ť�ĳ���
	public static float GO_BTN_X=674;//go��ťλ��
	public static float GO_BTN_Y=360;
	public static float LEFT_BTN_X=390;//��ťλ��
	public static float LEFT_BTN_Y=420;
	public static float RIGHT_BTN_X=LEFT_BTN_X-200;//�Ұ�ťλ��
	public static float RIGHT_BTN_Y=LEFT_BTN_Y;
	public static float AIM_BTN_X=556;//Ŀ�갴ťλ��
	public static float AIM_BTN_Y=424;
	public static float CHOICE_BTN_Y0=180;//ѡ�ť
	public static float CHOICE_BTN_Y1=CHOICE_BTN_Y0+90;//ѡ�ť
	public static float CHOICE_BTN_Y2=CHOICE_BTN_Y1+90;//ѡ�ť
	public static float SOUND_BTN_Y1=180;
	public static float SOUND_BTN_Y2=320;
	//���ڻ������˵��ĳ���
	static int screenWidthTest=800;//���Ի���Ļ���
	static int screenHeightTest=480;//���Ի���Ļ�߶�
	static float bigWidth=200;//ѡ�в˵���Ŀ��
	static float smallWidth=130;//δѡ�в˵���Ŀ��
	static float bigHeight=161;//ѡ�в˵���ĸ߶�
	static float smallHeight=(smallWidth/bigWidth)*bigHeight;//δѡ�в˵���ĸ߶�
    
	static float selectX=screenWidthTest/2-bigWidth/2;//ѡ�в˵����������Ļ�ϵ�Xλ��
	static float selectY=screenHeightTest/2-60;//ѡ�в˵����ϲ�����Ļ�ϵ�Yλ��
	static float span=10;//�˵���֮��ļ��
	static float slideSpan=30;//������ֵ
	 
	static float totalSteps=10;//�������ܲ���
	static float percentStep=1.0f/totalSteps;//ÿһ���Ķ����ٷֱ�
	static int timeSpan=20;//ÿһ�������ļ��ʱ�䣨ms��
	//�������ֺ����ֵĳ���
	static float TIMER_END_X=765;
	static float TIMER_END_Y=30;
	static float RI_QI_X=230;
	static float DE_FEN_X=500;
	static float DE_FEN_Y=170;
	//����С�����ĳ���
	static float BMP_Y=150;
	static float HELP_Y=150;
	//��ʼ�������ķ���
	public static void initConst(int screenWidth,int screenHeight)
	{
		SCREEN_WIDTH=screenWidth;//��Ļ�ĳߴ�
		SCREEN_HEIGHT=screenHeight;
		//��Ӧȫ�������ű���
		wRatio=screenWidth/(float)screenWidthTest;
		hRatio=screenHeight/(float)screenHeightTest;
		//������Ļ�Ľ��
		ssr=ScreenScaleUtil.calScale(screenWidth, screenHeight);		
		X_OFFSET=ssr.lucX;
		Y_OFFSET=ssr.lucY;
		//������̨�ĳ���
		BOTTOM_WIDTH*=ssr.ratio;//������̨����
		BOTTOM_HEIGHT*=ssr.ratio;//������̨��߶�
		EDGE_BIG*=ssr.ratio;//���Ե�ߴ�
		EDGE_SMALL*=ssr.ratio;//С��Ե�ߴ�
		MIDDLE*=ssr.ratio;//�м䶴�ĳߴ�
		DIS_CORNER*=ssr.ratio;//ͼƬ���϶���ͽ����ĸ����ľ���
		DIS_MIDDLE*=ssr.ratio;//ͼƬ���϶�����м䶴�ľ���;
		BALL_SIZE*=ssr.ratio;//��ֱ��
		X_OFFESET_BALL1*=ssr.ratio;//һ�������������Ͻǳ�ʼλ�ã�yֵ�̶������ɵ���
		GAP_BETWEEN_BALLS*=ssr.ratio;//������֮��ĳ�ʼ��϶
		DIS_WITH_MAIN_BALL*=ssr.ratio;//һ�����ĸ���ľ���
		V_MAX*=ssr.ratio;//�������ٶȣ��涨���������ٶȲ����Գ���100��
		//��������ĳ���
		TABLE_X*=ssr.ratio;//�������Ͻ�����
		TABLE_Y*=ssr.ratio;
		HOLE_CENTER_REVISE*=ssr.ratio;//��������������ֵ
		CORNER_HOLE_R*=ssr.ratio;//�ĽǶ��İ뾶
		MIDDLE_HOLE_R*=ssr.ratio;//�м䶴�İ뾶
		//������˵ĳ���
		DIS_WITH_BALL*=ssr.ratio;//�����ĸ���Ե�ľ���
		//�����������ĳ���
		BAR_X*=ssr.ratio;//������λ��
		BAR_Y*=ssr.ratio;
		RAINBOW_WIDTH*=ssr.ratio;//�ʺ������
		RAINBOW_HEIGHT*=ssr.ratio;//�ʺ����߶�
		RAINBOW_GAP*=ssr.ratio;//�ʺ�����϶
		RAINBOW_X*=ssr.ratio;//��һ���ʺ�λ�õ���ֵ
		RAINBOW_Y*=ssr.ratio;
		//���ڰ�ť�ĳ���
		GO_BTN_X*=ssr.ratio;//go��ťλ��
		GO_BTN_Y*=ssr.ratio;
		LEFT_BTN_X*=ssr.ratio;//��ťλ��
		LEFT_BTN_Y*=ssr.ratio;
		RIGHT_BTN_X*=ssr.ratio;//�Ұ�ťλ��
		RIGHT_BTN_Y*=ssr.ratio;
		AIM_BTN_X*=ssr.ratio;//Ŀ�갴ťλ��
		AIM_BTN_Y*=ssr.ratio;
		CHOICE_BTN_Y0*=ssr.ratio;//ѡ�ť
		CHOICE_BTN_Y1*=ssr.ratio;
		CHOICE_BTN_Y2*=ssr.ratio;
		SOUND_BTN_Y1*=ssr.ratio;//���ְ�ťλ��
		SOUND_BTN_Y2*=ssr.ratio;
		//���ڻ������˵��ĳ���		
		bigWidth*=ssr.ratio;//ѡ�в˵���Ŀ��
		smallWidth*=ssr.ratio;//δѡ�в˵���Ŀ��
		bigHeight*=ssr.ratio;//ѡ�в˵���ĸ߶�
		smallHeight*=ssr.ratio;//δѡ�в˵���ĸ߶�	    
		selectX*=ssr.ratio;//ѡ�в˵����������Ļ�ϵ�Xλ��
		selectY*=ssr.ratio;//ѡ�в˵����ϲ�����Ļ�ϵ�Yλ��
		span*=ssr.ratio;//�˵���֮��ļ��
		slideSpan*=ssr.ratio;//������ֵ
		//�������ֵĳ���
		TIMER_END_X*=ssr.ratio;
		TIMER_END_Y*=ssr.ratio;
		RI_QI_X*=ssr.ratio;
		DE_FEN_X*=ssr.ratio;
		DE_FEN_Y*=ssr.ratio;
		//����С�����ĳ���
		BMP_Y*=ssr.ratio;
		HELP_Y*=ssr.ratio;
	}
	public static boolean IsTwoRectCross//һ�����ε��ĸ�����֮һ�Ƿ�����һ��������
	(
			float xLeftTop1,float yLeftTop1,float length1,float width1,//���ϵ�x,y���꣬������
			float xLeftTop2,float yLeftTop2,float length2,float width2
	)
	{
		if
		(
				isPointInRect(xLeftTop1,yLeftTop1,xLeftTop2,yLeftTop2,length2,width2)||	//���϶���
				isPointInRect(xLeftTop1+length1,yLeftTop1,xLeftTop2,yLeftTop2,length2,width2)||	//���϶���
				isPointInRect(xLeftTop1,yLeftTop1+width1,xLeftTop2,yLeftTop2,length2,width2)||	//���¶���
				isPointInRect(xLeftTop1+length1,yLeftTop1+width1,xLeftTop2,yLeftTop2,length2,width2)||	//���¶���
				
				isPointInRect(xLeftTop2,yLeftTop2,xLeftTop1,yLeftTop1,length1,width1)||	//���϶���
				isPointInRect(xLeftTop2+length2,yLeftTop2,xLeftTop1,yLeftTop1,length1,width1)||	//���϶���
				isPointInRect(xLeftTop2,yLeftTop2+width2,xLeftTop1,yLeftTop1,length1,width1)||	//���¶���
				isPointInRect(xLeftTop2+length2,yLeftTop2+width2,xLeftTop1,yLeftTop1,length1,width1)	//���¶���
		)
		{
			return true;
		}
		return false;
	}
	public static boolean isPointInRect//һ�����Ƿ��ھ����ڣ������߽磩
	(
			float pointx,float pointy,
			float xLeftTop,float yLeftTop,float length,float width
	)
	{
		if(
				pointx>=xLeftTop&&pointx<=xLeftTop+length&&
				pointy>=yLeftTop&&pointy<=yLeftTop+width
		  )
		  {
			  return true;
		  }
		return false;
	}
}
