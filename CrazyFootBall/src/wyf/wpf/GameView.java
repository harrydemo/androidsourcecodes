package wyf.wpf;			//声明包语句
import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/*
 * 该类是游戏的主视图类，负责游戏画面的绘制，菜单的绘制绘制等等
 * 成员方法多为绘制方法，同时也有修改游戏数据的方法，如关卡数据等。为实现
 * SurfaceHolder.Callback接口重写了一些方法
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	Ball ball;			//小球对象
	int fieldLeft=27;		//球场的左边界在屏幕中的位置	
	int fieldRight=293;		//球场的右边界在屏幕中的位置
	int fieldUp = 68;		//球场的上边界在屏幕中的位置
	int fieldDown = 470;	//球场的下边界在屏幕中的位置
	int barDistance = 50;		//两个摇杆的间距
	int playerSize = 18;		//运动员图片的大小
	int maxLeftPosition = 115;		//守门员最左能走到的地方=
	int maxRightPosition = 205;		//守门员最右能走到的地方=
	int aiDirection = -1;			//记录AI的运动方向，4为左，12为右=
	int level=1;				//游戏的难度
	int MaxLevel = 3;		//最高等级
	int [] scores = {0,0};	//比分，前我后敌
	int scoreLeft = 80;		//绘制左边比分时的参考X坐标
	int scoreRight = 240;	//绘制右边比分时的参考X坐标
	int timeCounter=0;		//计时器，用于显示
	int winPoint = 8;			//获胜点，谁的进球数先达到该值获胜
	int AIGoalLeft;				//AI球门左边=
	int AIGoalRight;			//AI球门右边=
	int myGoalLeft;				//玩家的球门左边=
	int myGoalRight;			//玩家的球门右边=
	
	boolean isScoredAGoal;	//若进了球则暂时会置为true
	boolean isGameOver;		//一局比赛结束
	boolean isGameWin;		//是否赢了
	boolean isShowDialog;	//是否显示对话框
	boolean isGamePassAll;	//是否打赢了所有等级的比赛

	ArrayList<Bonus> balLive = new ArrayList<Bonus>();//这个用于碰撞检测，里面全是活动态的Bonus=
	ArrayList<Bonus> balForDraw = new ArrayList<Bonus>();//这个用于绘制，里面既有活动态，也有生效态的Bonus=
	ArrayList<Bonus> balAdd = new ArrayList<Bonus>();			//存放待添加Bonus对象的临时集合=
	ArrayList<Bonus> balDelete = new ArrayList<Bonus>();		//存放待删除Bonus对象的临时集合=
	BonusManager bm;											//BonusManager对象引用=
	
	FootballActivity father;		//FootballActivity对象引用
	DrawThread dt;				//后台刷屏线程
	AIThread ait;				//控制AI移动的线程	
	ArrayList<Player> alMyPlayer = new ArrayList<Player>(11);	//玩家的运动员集合
	ArrayList<Player> alAIPlayer = new ArrayList<Player>(11);	//AI 的运动员集合

	Bitmap bmpMyPlayer;		//玩家的球员图片	
	Bitmap bmpAIPlayer;		//AI的球员图片
	Bitmap bmpGoalBanner;	//进球后显示的图片
	Bitmap [] bmpScores;	//表示比分的数字图片
	Bitmap bmpWinBanner;	//游戏胜利的图片
	Bitmap bmpWinText;		//游戏胜利的文字
	Bitmap bmpLoseBanner;	//游戏失败的图片
	Bitmap bmpLoseText;		//游戏失败的文字
	Bitmap bmpPassAll;		//打赢所有比赛提示的图片
	Bitmap bmpMyClub;		//记录玩家所选的俱乐部
	Bitmap bmpAIClub;		//记录AI的俱乐部
	Bitmap bmpMenu;			//菜单图片
	Bitmap bmpDialog;		//对话框图片
	Bitmap bmpBackField;	//球场背景图片
	//Rect对象，用来与点击事件发生的位置进行匹配
	Rect rectMenu = new Rect(134,0,186,60);		//代表菜单的矩形框，
	Rect rectYesToDialog = new Rect(75,271,100,292);	//代表返回主菜单对话框中的是按钮
	Rect rectNoToDialog = new Rect(223,271,248,292);	//代表返回主菜单对话框中的否按钮
	int clubID;//存放俱乐部logo的图片ID
		
	//构造器，初始化成员变量
	public GameView(FootballActivity father,int clubID) {
		super(father);		//调用父类构造器
		getHolder().addCallback(this);//添加Callback接口
		this.father = father;
		this.clubID = clubID;//获得俱乐部logo		
		ball = new Ball(this);		//创建足球线程	
		initPlayerInstance();//初始化双方队员		
		initGame();//初始化游戏		
		initBitmap(father);	//初始化图片资源			
		ait = new AIThread(this);//创建AI分析线程		
		bm = new BonusManager(this);//初始化BonusManager		
		dt = new DrawThread(this,getHolder());//创建后台刷屏线程
	}
	//启动线程
	public void startGame(){
		ait.start();			//启动AI分析线程
		bm.start();				//启动BonusManager线程
		ball.start();			//启动足球线程
	}
	//初始化位图
	public void initBitmap(Context context){
		Resources r = context.getResources();		//获得Resources对象
		bmpMyPlayer = BitmapFactory.decodeResource(r,R.drawable.player18);		//玩家球员图片初始化
		bmpAIPlayer = BitmapFactory.decodeResource(r, R.drawable.ai_player);	//AI球员图片初始化
		bmpScores = new Bitmap[10];										 		//数字图片初始化
		bmpScores[0] = BitmapFactory.decodeResource(r, R.drawable.digit_0);		//数字图片0
		bmpScores[1] = BitmapFactory.decodeResource(r, R.drawable.digit_1);		//数字图片1
		bmpScores[2] = BitmapFactory.decodeResource(r, R.drawable.digit_2);		//数字图片2
		bmpScores[3] = BitmapFactory.decodeResource(r, R.drawable.digit_3);		//数字图片3
		bmpScores[4] = BitmapFactory.decodeResource(r, R.drawable.digit_4);		//数字图片4
		bmpScores[5] = BitmapFactory.decodeResource(r, R.drawable.digit_5);		//数字图片5
		bmpScores[6] = BitmapFactory.decodeResource(r, R.drawable.digit_6);		//数字图片6
		bmpScores[7] = BitmapFactory.decodeResource(r, R.drawable.digit_7);		//数字图片7 
		bmpScores[8] = BitmapFactory.decodeResource(r, R.drawable.digit_8);		//数字图片8  
		bmpScores[9] = BitmapFactory.decodeResource(r, R.drawable.digit_9);		//数字图片9
		bmpGoalBanner = BitmapFactory.decodeResource(r, R.drawable.game_goal);	//进球后的标语		
		bmpWinText = BitmapFactory.decodeResource(r, R.drawable.game_win);	//比赛胜利时的提示文字
		bmpLoseBanner = BitmapFactory.decodeResource(r, R.drawable.game_over);	//比赛失败时的提示图片
		bmpLoseText = BitmapFactory.decodeResource(r, R.drawable.game_lose);	//比赛失败时的提示文字
		Matrix m = new Matrix();			//创建Matrix对象，用于裁剪图片以适合游戏界面中俱乐部图片的大小
		Bitmap bmpTemp = BitmapFactory.decodeResource(r,(int) clubID);	//拿到指定的俱乐部图片
		int width = bmpTemp.getWidth();				//获得俱乐部图片宽度
		int height = bmpTemp.getHeight();			//获取俱乐部图片高度
		m.postScale(60.0f/width,60.0f/height);		//设置Matrix
		bmpMyClub = Bitmap.createBitmap(bmpTemp, 0, 0, width,height , m, true);		//玩家俱乐部logo
		bmpAIClub = BitmapFactory.decodeResource(r, R.drawable.ai_club);		//AI的俱乐部logo		
		bmpMenu = BitmapFactory.decodeResource(r, R.drawable.menu);//菜单按钮图片
		bmpDialog = BitmapFactory.decodeResource(r, R.drawable.dialog);	//菜单对话框	 
		bmpBackField = BitmapFactory.decodeResource(r, R.drawable.field);//足球场背景
		bmpPassAll = BitmapFactory.decodeResource(r, R.drawable.game_pass);	//打赢所有比赛的提示图片
	}	
	//初始化游戏参数,通关后会调用这个方法,包含initRound方法
	public void initGame(){
		scores[0]=0;		//玩家比分归零
		scores[1]=0;		//AI比分归零
		AIGoalLeft = maxLeftPosition;		//初始化AI的球门左边坐标
		AIGoalRight = maxRightPosition;		//初始化AI的球门右边坐标
		myGoalLeft = maxLeftPosition;		//初始化玩家的球门左边坐标
		myGoalRight = maxRightPosition;		//初始化玩家的球门左边坐标
		initRound();
	}
	//屏幕渲染方法
	protected void doDraw(Canvas canvas) {
		canvas.drawBitmap(bmpBackField, 0, 0, null);		//画球场背景
		//画出小球
		ball.drawSelf(canvas);
		//画出双方玩家
		drawBothPlayers(canvas);
		//画出比分
		drawScores(canvas);
		//检查并绘制活的Bonus
		checkAndDrawBonus(canvas);
		//画出双方的logo
		drawLogo(canvas);
		//画出菜单
		canvas.drawBitmap(bmpMenu, 134, 0, null);
		if(isScoredAGoal){		//如果进了一球，绘制进球图片
			canvas.drawBitmap(bmpGoalBanner, (fieldLeft+fieldRight)/2-bmpGoalBanner.getWidth()/2, (fieldUp+fieldDown)/2-bmpGoalBanner.getHeight()/2, null);
			timeCounter++;		//每画一次，计时器增加
			if(timeCounter > 50){	//如果计时器增加到50
				isScoredAGoal = false;		//设置进球标志位为false
				initRound();				//初始化游戏回合
				timeCounter = 0;			//清空计时器
				ball.isPlaying = true;		//小球运动标志位设为true
			}
		}
		if(isGameOver){//一场比赛结束，根据胜负，绘制相应图片	
			if(isGamePassAll){	//是否打赢了最强的AI
				canvas.drawBitmap(bmpPassAll,(fieldLeft+fieldRight)/2-bmpPassAll.getWidth()/2, (fieldUp+fieldDown)/2-bmpPassAll.getHeight()/2, null);
				timeCounter++;//每画一次，计时器增加
				if(timeCounter >50){//如果计时器增加到50
					isGameOver = false;					//设置游戏结束标志位为false
					isShowDialog = true;				//设置显示对话框为true				
				}
			}
			else if(isGameWin){			//获得胜利				
				canvas.drawBitmap(bmpLoseBanner, (fieldLeft+fieldRight)/2-bmpWinBanner.getWidth()/2, (fieldUp+fieldDown)/2-bmpWinBanner.getHeight()/2-20, null);
				canvas.drawBitmap(bmpWinText, (fieldLeft+fieldRight)/2-bmpWinText.getWidth()/2, (fieldUp+fieldDown)/2-bmpWinText.getHeight()/2, null);
				timeCounter++;//每画一次，计时器增加
				if(timeCounter >50){//如果计时器增加到50
					isGameOver = false;//设置游戏结束标志位为false
					initGame();			//初始化游戏
					levelUp();			//升级
					timeCounter = 0;	//计时器清零
					ball.isPlaying = true;	//足球运动标志位为true
				}
			}
			else{
				canvas.drawBitmap(bmpLoseBanner, (fieldLeft+fieldRight)/2-bmpWinBanner.getWidth()/2, (fieldUp+fieldDown)/2-bmpWinBanner.getHeight()/2-20, null);
				canvas.drawBitmap(bmpLoseText, (fieldLeft+fieldRight)/2-bmpWinText.getWidth()/2, (fieldUp+fieldDown)/2-bmpWinText.getHeight()/2, null);
				timeCounter++;//每画一次，计时器增加
				if(timeCounter >50){//如果计时器增加到50
					isGameOver = false;					//设置游戏结束标志位为false
					isShowDialog = true;				//设置显示对话框为true				
				}
			}
		}
		if(isShowDialog){
			canvas.drawBitmap(bmpDialog, 60, 200, null);	//画出返回主菜单对话框
		}
	}	
	//把横杆画出来
	public void drawBars(Canvas canvas){
		Paint paint = new Paint();
		paint.setStrokeWidth(2.5f);
		for(int i=0;i<8;i++){
			canvas.drawLine(fieldLeft, fieldUp+barDistance/2+i*barDistance, fieldRight, fieldUp+barDistance/2+i*barDistance, paint);
		}
	}
	//方法：初始化球员对象
	public void initPlayerInstance(){
		for(int i=0;i<11;i++){
			Player p1 = new Player();	//创建一个Player对象
			p1.attackDirection = 0;		//设置球员的进攻方向
			alMyPlayer.add(p1);			//把Player对象添加到玩家的球员集合中
			Player p2 = new Player();	//创建一个Player对象
			p2.attackDirection = 8;		//设置球员的进攻方向
			alAIPlayer.add(p2);			//把Player对象添加到AI的球员集合中
		}
	}
	//方法：根据用户的选择排兵布阵,direction为进攻方向，0为向上，1为向下
	public void initPlayerPositions(ArrayList<Player> al,int [] pos,int direction){
		int index = 0;
		for(int i=0;i<3;i++){			//一共有3条进攻线
			int playerNumber = pos[i];	//取出每个进攻线上有多少队员
			int segmentNumber = playerNumber + 1;		//根据进攻线上的奇偶性分段
			int segmentSpan = (fieldRight - fieldLeft)/segmentNumber;		//求出每段段长
			for(int j=1;j<=playerNumber;j++){
				Player p = al.get(index++);
				p.x = fieldLeft + j*segmentSpan;
				if(direction == 0){		//进攻方向向上
					p.y = fieldUp + barDistance/2 + barDistance*2*(i+1);
				}
				else{					//进攻方向向上
					p.y = fieldDown - barDistance/2 - barDistance*2*(i+1);
				}
			}
		}
		//确定守门员的位置
		Player p = al.get(index);		//守门员为球员列表中的最后一个
		p.x = (fieldLeft + fieldRight)/2;		//守门员的X坐标为球场的
		if(direction == 0){			//如果进攻方向向上
			p.y =  fieldUp + barDistance/2 + 7*barDistance;
		}
		else{		//如果进攻方向向下
			p.y = fieldUp + barDistance/2;
		}
	}
	//方法：初始化游戏回合，每次进球后调用该方法一次
	public void initRound(){
		//复位小球位置，随机产生方向
		ball.x = (fieldLeft + fieldRight)/2;//指定小球X坐标
		ball.y = (fieldUp + fieldDown)/2;//指定小球Y坐标
		ball.direction = (int)(Math.random()*100)%16;	//随机指定小球的方向
		//复位双方运动员		
		int [] layoutArray = father.layoutArray;		//玩家球员的占位数组
		initPlayerPositions(alMyPlayer,layoutArray,0);		//为己方队员站位
		int [] tempArray2 = {3,3,4};					//AI球员的占位数组		
		initPlayerPositions(alAIPlayer,tempArray2,1);		//为AI方队员站位
	}
	//方法：画足球运动员
	public void drawBothPlayers(Canvas canvas){
		for(Player p:alMyPlayer){	//遍历玩家球员列表
			canvas.drawBitmap(bmpMyPlayer, p.x-playerSize/2, p.y-playerSize/2, null);//绘制球员
		}
		for(Player p:alAIPlayer){		//遍历AI球员列表
			canvas.drawBitmap(bmpAIPlayer, p.x-playerSize/2, p.y-playerSize/2, null);//绘制球员
		}
	}
	//方法：画双方比分
	public void drawScores(Canvas canvas){
		//画左边比分从左往右画
		String score = scores[0]+"";	
		int l = score.length();	//获得比分字符串长度
		for(int j=0;j<l;j++){	//根据字符串内容绘制数字图片
			canvas.drawBitmap(bmpScores[score.charAt(j)-'0'], scoreLeft+j*32, 2, null);
		}
		//画右边比分，从右往左画
		String scoreR = scores[1]+"";
		int l2 = scoreR.length();//获得比分字符串长度
		for(int i=l2-1;i>=0;i--){//根据字符串内容绘制数字图片
			canvas.drawBitmap(bmpScores[scoreR.charAt(i)-'0'], scoreRight-(l2-i)*32, 2, null);
		}
	}
	//方法：检查并绘制Bonus
	public void checkAndDrawBonus(Canvas canvas){
		if(balForDraw.size() != 0){		//检查是否有可画的Bonus
				for(Bonus b:balForDraw){
					if(b.status == Bonus.LIVE){		//判断Bonus的状态是否为LIVE
						b.drawSelf(canvas);	//画Bonus
					}
					else if(b.status == Bonus.EFFECTIVE){//判断Bonus的状态是否为EFFECTIVE
						b.drawEffect(canvas);		//画Bonus的作用效果
					}					
				}				
		}
		//检查balAdd有没有需要添加的Bonus
		if(balAdd.size() != 0){	//
			balForDraw.addAll(balAdd);		//将balAdd中的Bonus添加到balForDraw中
			balAdd.clear();					//清空balAdd
		}
		//检查balDelete有没有需要删除的Bonus
		if(balDelete.size() != 0){
			balForDraw.removeAll(balDelete);	//从balForDraw删除balDelete中的Bonus
			balDelete.clear();					//清空balDelete
		}
	}
	//方法：画双方俱乐部logo
	public void drawLogo(Canvas canvas){
		canvas.drawBitmap(bmpMyClub, 0, 0, null);		//画玩家俱乐部的图片
		canvas.drawBitmap(bmpAIClub, 240, 0, null);		//画AI俱乐部的图片
	}
	//方法：移动运动员，传入的参数可以为12(左),4(右)，如果传入其他则将球员方向设为-1
	public void movePlayers(ArrayList<Player> al,int direction){
		switch(direction){					//对传入的方向参数进行判断
		case 12:			//12为左
			Player pl1 = al.get(al.size()-1);
			if(pl1.x - pl1.movingSpan  >= maxLeftPosition){	//判断是否还能向左走
				for(Player p: al){
					p.x -= p.movingSpan;		//向左移动指定距离
					p.movingDirection = 12;		//记录移动方向
				}				
			}			
			break;
		case 4:			//4为右
			Player pl2 = al.get(al.size()-1);
			if(pl2.x + pl2.movingSpan  <= maxRightPosition){//判断是否还能向右走
				for(Player p: al){
					p.x += p.movingSpan;		//向右移动指定的距离
					p.movingDirection = 4;		//记录移动方向
				}				
			}	
			break;
		default:		//如果传入的参数既不是4也不是12
			for(Player p: al){
				p.movingDirection = -1;			//将球员的移动方向设为-1
			}
			break;
		}
	}
	//方法：检查是否比赛结束,原则是谁先得到指定分谁就赢
	public void checkIfLevelUp(){
		if(Math.max(scores[0], scores[1]) == winPoint){	//如果两队中有一队的比分达到了胜利点
			isGameOver = true;							//将游戏结束标志位置为true
			if(scores[0] == winPoint){					//判断是否是玩家赢得比赛
				if(this.level >= MaxLevel){		//达到最高等级
					isGamePassAll = true;			//设置游戏打通所有等级标志位
				}
				else{
					isGameWin = true;				//设置比赛胜利标志位
				}				
				if(father.wantSound && father.mpCheerForWin!= null){					//如需要，播放相应声音
					father.mpCheerForWin.start();
				}				
			}
			else{										//是AI赢得比赛
				isGameWin = false;
				if(father.wantSound && father.mpCheerForLose!=null){					//如需要，播放相应声音
					father.mpCheerForLose.start();
				}				
			}
		}
		else{											//如果只是单纯的进了一个球
			isScoredAGoal = true;						//将进球标志位设为true
			if(father.wantSound && father.mpCheerForGoal!=null){						//如需要，播放相应声音
				father.mpCheerForGoal.start();
			}			
		}
	}
	//方法：游戏升级，改变参数增加难度
	public void levelUp(){
		ball.levelUp();	//调用小球升级方法
		for(Player p:alMyPlayer){		//遍历玩家球员列表
			p.levelUp();				//调用玩家的升级方法
		}
		this.level++;
	}
	@Override
	protected void finalize() throws Throwable {
		System.out.println("############ FieldView  is dead##########");
		super.finalize();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		//重写surfaceChanged方法
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {//重写surfaceCreated方法
		if(!dt.isAlive()){
			dt.start();
		}			 
        father.pmt = new PlayerMoveThread(father);//初始化并启动球员的移动处理线程
        father.pmt.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {//重写surfaceDestroyed方法
		dt.isGameOn = false;	//停止刷屏线程的执行
		father.pmt.flag = false;
	}
}