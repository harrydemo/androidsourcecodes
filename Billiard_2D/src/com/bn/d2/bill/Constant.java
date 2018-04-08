package com.bn.d2.bill;
public class Constant {
	public static ScreenScaleResult ssr;//屏幕的结果
	public static float wRatio;//适应全屏的缩放比例
	public static float hRatio;
	public static int SCREEN_WIDTH;//屏幕的宽度
	public static int SCREEN_HEIGHT;//屏幕的高度
	//关于球台的常量
	public static float BOTTOM_WIDTH=85*2+168*2+95;//整个球台面宽度
	public static float BOTTOM_HEIGHT=85*2+228;//整个球台面高度
	public static float EDGE_BIG=45;//大边缘尺寸
	public static float EDGE_SMALL=40;//小边缘尺寸
	public static float MIDDLE=95;//中间洞的尺寸
	public static float DIS_CORNER=20;//图片左上顶点和角上四个洞的距离
	public static float DIS_MIDDLE=30;//图片左上顶点和中间洞的距离
	
	public static float TABLE_X=20;//桌球左上角坐标
	public static float TABLE_Y=20;
	public static float HOLE_CENTER_REVISE=20;//球洞中心坐标修正值
	public static float CORNER_HOLE_R=23;//四角洞的半径
	public static float MIDDLE_HOLE_R=32f;//中间洞的半径
	//游戏界面左上角的坐标
	public static float X_OFFSET;
	public static float Y_OFFSET;
	//关于桌球的常量
	public static float BALL_SIZE=24;//球直径
	public static float X_OFFESET_BALL1=350;//一号球离桌面左上角初始位置（y值固定，不可调）
	public static float GAP_BETWEEN_BALLS=3;//球与球之间的初始间隙
	public static float DIS_WITH_MAIN_BALL=238;//一号球和母球间的距离
	public static float V_MAX=150;//球的最大速度（规定：球的最大速度不可以超过100）
	public static float K=1.3f/V_MAX;//换帧速度系数
	
	public static float TIME_SPAN=0.05f;//球运动的模拟时间间隔（规定: timeSpan不可以>=Ball.d）
	public static float V_ATTENUATION=0.996f;//速度衰减比例
	public static float V_MIN=1.5f;//速度最小值，当速度小于此值时球停止运动
	//关于球杆的常量
	public static float DIS_WITH_BALL=10;//球杆与母球边缘的距离
	//关于力度条的常量
	public static float BAR_X=686;//力度条位置
	public static float BAR_Y=90;
	public static float RAINBOW_WIDTH=37;//彩虹条宽度
	public static float RAINBOW_HEIGHT=9.22f;//彩虹条高度
	public static float RAINBOW_GAP=1f;//彩虹条间隙
	public static float RAINBOW_X=BAR_X+7.5f;//第一个彩虹位置调整值
	public static float RAINBOW_Y=BAR_Y-17;
	//关于按钮的常量
	public static float GO_BTN_X=674;//go按钮位置
	public static float GO_BTN_Y=360;
	public static float LEFT_BTN_X=390;//左按钮位置
	public static float LEFT_BTN_Y=420;
	public static float RIGHT_BTN_X=LEFT_BTN_X-200;//右按钮位置
	public static float RIGHT_BTN_Y=LEFT_BTN_Y;
	public static float AIM_BTN_X=556;//目标按钮位置
	public static float AIM_BTN_Y=424;
	public static float CHOICE_BTN_Y0=180;//选项按钮
	public static float CHOICE_BTN_Y1=CHOICE_BTN_Y0+90;//选项按钮
	public static float CHOICE_BTN_Y2=CHOICE_BTN_Y1+90;//选项按钮
	public static float SOUND_BTN_Y1=180;
	public static float SOUND_BTN_Y2=320;
	//关于滑动主菜单的常量
	static int screenWidthTest=800;//测试机屏幕宽度
	static int screenHeightTest=480;//测试机屏幕高度
	static float bigWidth=200;//选中菜单项的宽度
	static float smallWidth=130;//未选中菜单项的宽度
	static float bigHeight=161;//选中菜单项的高度
	static float smallHeight=(smallWidth/bigWidth)*bigHeight;//未选中菜单项的高度
    
	static float selectX=screenWidthTest/2-bigWidth/2;//选中菜单项左侧在屏幕上的X位置
	static float selectY=screenHeightTest/2-60;//选中菜单项上侧在屏幕上的Y位置
	static float span=10;//菜单项之间的间距
	static float slideSpan=30;//滑动阈值
	 
	static float totalSteps=10;//动画的总步数
	static float percentStep=1.0f/totalSteps;//每一步的动画百分比
	static int timeSpan=20;//每一步动画的间隔时间（ms）
	//关于数字和文字的常量
	static float TIMER_END_X=765;
	static float TIMER_END_Y=30;
	static float RI_QI_X=230;
	static float DE_FEN_X=500;
	static float DE_FEN_Y=170;
	//关于小背景的常量
	static float BMP_Y=150;
	static float HELP_Y=150;
	//初始化常量的方法
	public static void initConst(int screenWidth,int screenHeight)
	{
		SCREEN_WIDTH=screenWidth;//屏幕的尺寸
		SCREEN_HEIGHT=screenHeight;
		//适应全屏的缩放比例
		wRatio=screenWidth/(float)screenWidthTest;
		hRatio=screenHeight/(float)screenHeightTest;
		//计算屏幕的结果
		ssr=ScreenScaleUtil.calScale(screenWidth, screenHeight);		
		X_OFFSET=ssr.lucX;
		Y_OFFSET=ssr.lucY;
		//关于球台的常量
		BOTTOM_WIDTH*=ssr.ratio;//整个球台面宽度
		BOTTOM_HEIGHT*=ssr.ratio;//整个球台面高度
		EDGE_BIG*=ssr.ratio;//大边缘尺寸
		EDGE_SMALL*=ssr.ratio;//小边缘尺寸
		MIDDLE*=ssr.ratio;//中间洞的尺寸
		DIS_CORNER*=ssr.ratio;//图片左上顶点和角上四个洞的距离
		DIS_MIDDLE*=ssr.ratio;//图片左上顶点和中间洞的距离;
		BALL_SIZE*=ssr.ratio;//球直径
		X_OFFESET_BALL1*=ssr.ratio;//一号球离桌面左上角初始位置（y值固定，不可调）
		GAP_BETWEEN_BALLS*=ssr.ratio;//球与球之间的初始间隙
		DIS_WITH_MAIN_BALL*=ssr.ratio;//一号球和母球间的距离
		V_MAX*=ssr.ratio;//球的最大速度（规定：球的最大速度不可以超过100）
		//关于桌球的常量
		TABLE_X*=ssr.ratio;//桌球左上角坐标
		TABLE_Y*=ssr.ratio;
		HOLE_CENTER_REVISE*=ssr.ratio;//球洞中心坐标修正值
		CORNER_HOLE_R*=ssr.ratio;//四角洞的半径
		MIDDLE_HOLE_R*=ssr.ratio;//中间洞的半径
		//关于球杆的常量
		DIS_WITH_BALL*=ssr.ratio;//球杆与母球边缘的距离
		//关于力度条的常量
		BAR_X*=ssr.ratio;//力度条位置
		BAR_Y*=ssr.ratio;
		RAINBOW_WIDTH*=ssr.ratio;//彩虹条宽度
		RAINBOW_HEIGHT*=ssr.ratio;//彩虹条高度
		RAINBOW_GAP*=ssr.ratio;//彩虹条间隙
		RAINBOW_X*=ssr.ratio;//第一个彩虹位置调整值
		RAINBOW_Y*=ssr.ratio;
		//关于按钮的常量
		GO_BTN_X*=ssr.ratio;//go按钮位置
		GO_BTN_Y*=ssr.ratio;
		LEFT_BTN_X*=ssr.ratio;//左按钮位置
		LEFT_BTN_Y*=ssr.ratio;
		RIGHT_BTN_X*=ssr.ratio;//右按钮位置
		RIGHT_BTN_Y*=ssr.ratio;
		AIM_BTN_X*=ssr.ratio;//目标按钮位置
		AIM_BTN_Y*=ssr.ratio;
		CHOICE_BTN_Y0*=ssr.ratio;//选项按钮
		CHOICE_BTN_Y1*=ssr.ratio;
		CHOICE_BTN_Y2*=ssr.ratio;
		SOUND_BTN_Y1*=ssr.ratio;//音乐按钮位置
		SOUND_BTN_Y2*=ssr.ratio;
		//关于滑动主菜单的常量		
		bigWidth*=ssr.ratio;//选中菜单项的宽度
		smallWidth*=ssr.ratio;//未选中菜单项的宽度
		bigHeight*=ssr.ratio;//选中菜单项的高度
		smallHeight*=ssr.ratio;//未选中菜单项的高度	    
		selectX*=ssr.ratio;//选中菜单项左侧在屏幕上的X位置
		selectY*=ssr.ratio;//选中菜单项上侧在屏幕上的Y位置
		span*=ssr.ratio;//菜单项之间的间距
		slideSpan*=ssr.ratio;//滑动阈值
		//关于数字的常量
		TIMER_END_X*=ssr.ratio;
		TIMER_END_Y*=ssr.ratio;
		RI_QI_X*=ssr.ratio;
		DE_FEN_X*=ssr.ratio;
		DE_FEN_Y*=ssr.ratio;
		//关于小背景的常量
		BMP_Y*=ssr.ratio;
		HELP_Y*=ssr.ratio;
	}
	public static boolean IsTwoRectCross//一个矩形的四个顶点之一是否在另一个矩形内
	(
			float xLeftTop1,float yLeftTop1,float length1,float width1,//左上点x,y坐标，长，宽
			float xLeftTop2,float yLeftTop2,float length2,float width2
	)
	{
		if
		(
				isPointInRect(xLeftTop1,yLeftTop1,xLeftTop2,yLeftTop2,length2,width2)||	//左上顶点
				isPointInRect(xLeftTop1+length1,yLeftTop1,xLeftTop2,yLeftTop2,length2,width2)||	//右上顶点
				isPointInRect(xLeftTop1,yLeftTop1+width1,xLeftTop2,yLeftTop2,length2,width2)||	//左下顶点
				isPointInRect(xLeftTop1+length1,yLeftTop1+width1,xLeftTop2,yLeftTop2,length2,width2)||	//右下顶点
				
				isPointInRect(xLeftTop2,yLeftTop2,xLeftTop1,yLeftTop1,length1,width1)||	//左上顶点
				isPointInRect(xLeftTop2+length2,yLeftTop2,xLeftTop1,yLeftTop1,length1,width1)||	//右上顶点
				isPointInRect(xLeftTop2,yLeftTop2+width2,xLeftTop1,yLeftTop1,length1,width1)||	//左下顶点
				isPointInRect(xLeftTop2+length2,yLeftTop2+width2,xLeftTop1,yLeftTop1,length1,width1)	//右下顶点
		)
		{
			return true;
		}
		return false;
	}
	public static boolean isPointInRect//一个点是否在矩形内（包括边界）
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
