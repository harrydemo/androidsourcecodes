package wyf.wpf;			//声明包语句
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
/*
 * Ball类是一个继承自Thread类的线程类。其主要的功能首先是封装足球有关的信息，如坐标点、方向、移动速度等。
 * 其次，该类也负责移动足球的位置，进行碰撞检测，这些功能主要是通过run方法来实现的，run方法中主要有两个方法
 * move和checkCollision，前者负责根据足球的方向（16种之一）来的移动足球的位置。后者用于进行碰撞检测，查看
 * 是否足球碰到AI或玩家的运动员，是否遇到边界，是否遇到一些Bonus如冰冻小球等等。该类还有一个drawSelf方法，
 * 用于在游戏View的myDraw方法中调用以绘制自己。
 */
public class Ball extends Thread{
	int x;								//足球中心的x坐标
	int y;								//足球中心的y坐标
	int direction=-1;					//足球的运动方向，从0到15顺时针代表从向上开始的16个方向，写书的时候画个图贴上去
	int velocity=20;					//足球的运动速率
	int maxVelocity = 20;				//最大运动速率
	int minVelocity = 5;				//最小运动速率
	int ballSize = 10;					//足球大小
	Matrix matrix;						//Matrix对象，用来实现足球图片的翻转效果
	Bitmap bmpBall;						//足球的图片
	GameView father;					//FieldView对象引用
	float acceleration=-0.10f;			//足球在无人撞击时速度会逐渐衰减
	boolean isStarted;					//比赛是否开始
	boolean isPlaying;					//比赛是否正在进行
	float sin675=0.92f;					//特定角度正弦值，用于计算移动的像素个数
	float sin225=0.38f;					//特定角度正弦值，用于计算移动的像素个数
	float sin45=0.7f;					//特定角度正弦值，用于计算移动的像素个数
	int sleepSpan = 50;					//休眠时间
	float changeOdd = 0.6f;				//变向的几率
	int lastKicker;						//最近的这一脚是谁踢的，0代表自己，8代表AI
	
	public Ball(GameView father){
		super.setName("##-Ball");			//设置线程名字，用于调试用
		this.father = father;
		
		Resources r = father.getContext().getResources();	//获取Resources对象
		bmpBall = BitmapFactory.decodeResource(r, R.drawable.ball);//设置图片
		matrix = new Matrix();		
		isStarted = true;		//设置循环变量
		isPlaying = true;		//
	}
	//线程的任务方法
	public void run(){
		while(isStarted){
			while(isPlaying){
				//移动足球
				move();
				//碰撞检测
				checkCollision();
				//休眠一下
				try{
					Thread.sleep(sleepSpan);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			try{
				Thread.sleep(500);
			}
			catch(Exception e){		e.printStackTrace();
			}
		}
	}
	//绘制足球图片
	public void drawSelf(Canvas canvas){
		Bitmap bmp=null;
		if(isPlaying){
			matrix.postRotate(5);		//将足球图片旋转一定角度，生成新的Bitmap对象
			 bmp = Bitmap.createBitmap(bmpBall, 0, 0, ballSize, ballSize, matrix, true);
		}		
		else{
			bmp = bmpBall;				//如果没有在游戏中，则不作旋转变换
		}
		canvas.drawBitmap(bmp, x-ballSize/2, y-ballSize/2, null);	
	}
	//移动足球
	public void move(){
		switch(direction){
		case 0:			//方向向上
			y -= velocity;									//移动
			decreamentVelocity();								//衰减速度
			break;
		case 1:			//上偏右22.5度
			x += (int)(velocity*sin225);						//移动
			y -= (int)(velocity*sin675);							
			decreamentVelocity();								//衰减速度
			break;
		case 2:			//上偏右45度
			x += (int)(velocity*sin45);							//移动	
			y -= (int)(velocity*sin45);						
			decreamentVelocity();								//衰减速度
			break;
		case 3:			//上偏右67.5度
			x += (int)(velocity*sin225);							//移动	
			y -= (int)(velocity*sin675);						
			decreamentVelocity();								//衰减速度
			break;
		case 4:			//方向向右
			x += velocity;									//移动				
			decreamentVelocity();								//衰减速度
			break;
		case 5:			//右偏下22.5度
			x += (int)(velocity*sin675);							//移动	
			y += (int)(velocity*sin225);						
			decreamentVelocity();								//衰减速度
			break;
		case 6:			//右偏下45度
			x += (int)(velocity*sin45);						//移动	
			y += (int)(velocity*sin45);						
			decreamentVelocity();								//衰减速度
			break;
		case 7:			//右偏下67.5度
			x += (int)(velocity*sin225);						//移动	
			y += (int)(velocity*sin675);						
			decreamentVelocity();								//衰减速度
			break;
		case 8:			//方向向下
			y += velocity;									//移动										
			decreamentVelocity();								//衰减速度
			break;
		case 9:			//下偏左22.5度
			x -= (int)(velocity*sin225);						//移动	
			y += (int)(velocity*sin675);						
			decreamentVelocity();								//衰减速度
			break;
		case 10:		//下偏左45度
			x -= (int)(velocity*sin45);						//移动	
			y += (int)(velocity*sin45);						
			decreamentVelocity();								//衰减速度
			break;
		case 11:		//下偏左67.5度
			x -= (int)(velocity*sin675);						//移动	
			y += (int)(velocity*sin225);						
			decreamentVelocity();								//衰减速度
			break;
		case 12:		//方向向左
			x -= velocity;									//移动					
			decreamentVelocity();								//衰减速度
			break;
		case 13:		//左偏上22.5度
			x -= (int)(velocity*sin675);						//移动	
			y -= (int)(velocity*sin225);						
			decreamentVelocity();								//衰减速度
			break;
		case 14:		//左偏上45度
			x -= (int)(velocity*sin45);						//移动	
			y -= (int)(velocity*sin45);						
			decreamentVelocity();								//衰减速度
			break;
		case 15:		//左偏上67.5度
			x -= (int)(velocity*sin225);						//移动	
			y -= (int)(velocity*sin675);						
			decreamentVelocity();								//衰减速度
			break;
		default:
			break;
		}
	}
	
	public void decreamentVelocity(){
		velocity = (int)(velocity*(1+acceleration));		//衰减速度
		if(velocity<minVelocity){		//衰减到足球的最小速度则停止衰减
			velocity = minVelocity;
		}
	}	
	//总的碰撞检测方法，其中包含多个检测子项，分别为其设立一个方法
	public void checkCollision(){
		checkForBorders();		//检查是否出边界
		checkForAIPlayers();	//检查是否碰到AI
		checkForUserPlayers();	//检查是否碰到玩家
		checkIfScoreAGoal();	//检查是否进球啦
		checkForBonus();		//检查是否碰到Bonus
	}
	/*
	 * 此方法检测足球是否碰到了边界，如果碰到了上下左右中的某一边界
	 * 则处理方法为：一定几率沿正确的路线（类似反射定律）改变方向，
	 * 一定概率随机变向，如以方向1碰到上边界，会以某个较大的概率
	 * 将小球的方向改为7，而会有相对较小的概率将方向改为5、6、7三个
	 * 方向中的某一个。
	 */
	public void checkForBorders(){
		int d = direction;
		//左右是不是出边界了
		if(x <= father.fieldLeft){
			//撞了左边界
			if(d>8 && d<16 && d!=12){	//如果不是正撞到左边界
				if(Math.random() < changeOdd){	//一定概率概率沿正确反射路线变向
					direction = 16 - direction;
				}
				else{							//一定概率随机变向
					direction = (direction>12?1:5) + (int)(Math.random()*100)%3;
				}
			}	
			else if(d == 12){			//如果是正撞到左边界
				if(Math.random() < 0.4){		//注意这个概率要小，因为正撞上去希望随机变向的概率大一些	
					direction = 4;
				}
				else{
					direction = (Math.random() > 0.5?3:5);
				}
			}
		}
		else if(x > father.fieldRight){
			//撞到右边界
			if(d >0 && d<8 && d!=4){
				if(Math.random() < changeOdd){		//按正常反射路线变向
					direction = 16-direction;
				}
				else{								//一定几率随机变向
					direction = (direction>4?9:13) + (int)(Math.random()*100)%3;
				}
			}
			else if(d == 4){			//如果是正撞到右边界
				if(Math.random() < 0.4){
					direction = 12;
				}
				else{
					direction = (Math.random()>0.5?11:13);
				}
			}
		}
		d = direction;
		//判断是否撞到上边界		
		if(y < father.fieldUp){
			//不是正撞
			if(d>0 && d<4 || d>12&&d<16){
				if(Math.random() < changeOdd){			//一定几率沿正确反射路线变向
					direction = (d>12?24:8) - d;
				}
				else{									//一定几率随机变向
					direction = (d>12?9:5) + (int)(Math.random()*100)%3;
				}
			}
			else if(d == 0){			//正撞到上边界
				if(Math.random() < 0.4){				//一定几率沿正确反射路线返回
					direction = 8;
				}
				else{
					direction = (Math.random() < 0.5?7:9);		//一定几率随机变向
				}
			}
		}
		//判断是否撞到下边界
		else if(y > father.fieldDown){
			//不是正撞
			if(d >4 && d<12 && d!=8){
				if(Math.random() < changeOdd){	//按正常反射路线变向
					direction = (d>8?24:8) - d;
				}
				else{							//随机变向
					direction = (d>8?13:1) +(int)(Math.random()*100)%3;
				}
			}
			else if(d == 8){		//正撞到下边界
				if(Math.random() < 0.4){		//正常变向
					direction = 0;
				}
				else{							//随机变向
					direction = (Math.random()>0.5?1:15);
				}
			}
		}
	}
	/*
	 * 此方法检测是否碰到AI运动员，如果碰到，则调用handleCollision方法处理碰撞，
	 * 同时播放声音设置足球新速率和设置lastKicker
	 */
	public void checkForAIPlayers(){
		int r = (this.ballSize + father.playerSize)/2;
		for(Player p:father.alAIPlayer){
			if((p.x - this.x)*(p.x - this.x) + (p.y - this.y)*(p.y - this.y) <= r*r){		//发生碰撞
				handleCollision(this,p);			//处理碰撞
				if(father.father.wantSound && father.father.mpKick!=null){		//播放声音
					try {							//用try/catch语句包装
						father.father.mpKick.start();
					} catch (Exception e) {}
				}				
				velocity = p.power;
				lastKicker = 8;				//记录最后一脚是谁踢的
			}
		}		
	}
	/*
	 * 此方法检测是否碰到了玩家 的足球运动员，
	 */
	public void checkForUserPlayers(){
		int r = (this.ballSize + father.playerSize)/2;
		for(Player p:father.alMyPlayer){
			if((p.x - this.x)*(p.x - this.x) + (p.y - this.y)*(p.y - this.y) <= r*r){		//发生碰撞
				handleCollision(this,p);			//处理碰撞
				if(father.father.wantSound && father.father.mpKick!=null){		//播放声音
					try {
						father.father.mpKick.start();
					} catch (Exception e) {}	
				}				
				velocity = p.power;			//被 赋予新速度
				lastKicker = 0;				//记录最后一脚谁踢的
			}
		}
	}
	/*
	 * 此方法处理足球和运动员之间的碰撞,AI和玩家的处理方式是一样的，
	 * 首先查看Player对象的movingDirection，再综合Player对象的
	 * attackDirection，确定方向范围，类似直角坐标系中的4个象限，
	 * 然后在方向范围中随机产生一个，这样产生的方向有惯性在里面，看着
	 * 比较真实。需要注意的是如果足球和运动员碰撞时运动员静止不动，
	 * 那么可选的方向就是1或15（进攻方向朝上）、7或9（进攻方行朝下）
	 */
	public void handleCollision(Ball ball,Player p){
		switch(p.movingDirection){
		case 12:				//移动方向向左
			if(p.attackDirection == 0){		//攻击方向向上
				ball.direction = 13 + (int)(Math.random()*100)%3;		//取13，14，15中一个
			}
			else{							//攻击方向向下
				ball.direction = 9 + (int)(Math.random()*100)%3;		//取9，10，11中一个
			}
			break;
		case 4:					//移动方向向右
			if(p.attackDirection == 0){		//攻击方向向上
				ball.direction = 1 + (int)(Math.random()*100)%3;		//取1，2，3中一个
			}
			else{							//攻击方向向下
				ball.direction = 5 + (int)(Math.random()*100)%3;		//取5,6,7中一个
			}
			break;
		default:				//没有移动
			if(p.attackDirection == 0){		//攻击方向向上
				ball.direction = 15 + (int)(Math.random()*100)%3;		//取1，2，3中一个
				if(ball.direction > 15){
					ball.direction = ball.direction % 16;
				}
			}
			else{							//攻击方向向下
				ball.direction = 7 + (int)(Math.random()*100)%3;		//取7，8，9中一个
			}
			break;			
		}
	}
	/*
	 * 此方法用于检测是否进球，如是，则相应球队得分加1，然后判断游戏是否结束（游戏规则是谁先进够8个谁就赢）
	 */
	public void checkIfScoreAGoal(){
		if(this.y <= father.fieldUp && this.x > father.AIGoalLeft && this.x < father.AIGoalRight){
			//上方球门进球,即玩家
			isPlaying = false;
			father.scores[0]++;
			father.checkIfLevelUp();
		}
		else if(this.y >= father.fieldDown && this.x > father.myGoalLeft && this.x < father.myGoalRight){
			//AI进球
			isPlaying = false;
			father.scores[1]++;
			father.checkIfLevelUp();
		}
	}
	//升级方法
	public void levelUp(){
		this.minVelocity +=3;
		if(minVelocity > 20){
			minVelocity = 20;
		}
	}
	/*
	 * 该方法判断是否碰到了Bonus，如果碰到，对相应的Bonus进行操作
	 * 改变其状态，调用其方法修改游戏参数等等，并播放声音
	 */
	public void checkForBonus(){
		if(father.balLive.size() != 0){
			for(Bonus b:father.balLive){
				if((b.x - x)*(b.x - x) + (b.y-y)*(b.y-y) <= (b.bonusSize/2+ballSize/2)*(b.bonusSize/2+ballSize/2)
						&& b.status == Bonus.LIVE){
					b.status = Bonus.EFFECTIVE;
					father.balLive.remove(b);
					b.setTarget(this.lastKicker);
					b.doJob();
					b.setTimeout(Bonus.EFFECT_SPAN);	
					if(father.father.wantSound){
						if(b instanceof IceBonus){		//是冰冻小球
							try {
								father.father.mpIce.start();
							} catch (Exception e) {}
						}							
						else if( b instanceof LargerGoalBonus){		//是扩大球门的
							try {
								father.father.mpLargerGoal.start();
							} catch (Exception e) {}
						}
					}
					break;
				}					
			}
		}
	}
	
	
}