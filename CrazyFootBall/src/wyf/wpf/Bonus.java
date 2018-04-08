package wyf.wpf;				//声明包语句
import java.util.List;			//引入相关类
import java.util.Timer;//引入相关类
import java.util.TimerTask;//引入相关类
import android.graphics.Bitmap;//引入相关类
import android.graphics.Canvas;//引入相关类
/*
 * 该类为一切Bonus的父类，主要提供一些公共的成员或是方法。一个Bonus类主要的包括
 * 自己的绘制部分、触发后的绘制、自己生命周期计时、触发后生命周期计时、触发后后台
 * 数据的修改、出发生命周期结束后后台数据的恢复。
 */
public abstract class Bonus{
	public static final int PREPARE = 0;	//准备态，可以画出来，但是不可以碰到
	public static final int LIVE = 1;		//活动态，可以被画出，可以被碰到
	public static final int DEAD = 2;	//死亡态，不可以被画出，不可以被碰到
	public static final int EFFECTIVE = 3;//生效态，不可以被画出，不可以被碰到，但是可以画其产生的作用，如冰冻等
	public static final int LIFE_SPAN = 5000;
	public static final int EFFECT_SPAN = 5000;
	public static final int PREPARE_SPAN = 2000;
	int status = -1;			//0：存在，1：死亡，2：生效
	int x,y;					//Bonus中心点的坐标
	int bonusSize;				//Bonus的大小
	int selfIndex=0;			//自己帧索引
	int effectIndex = 0;		//生效后的索引
	int selfFrameNumber;		//自己动画帧总数
	int effectFrameNumber;		//生效动画帧总数
	int target;					//对谁起作用0为自己，8为AI，以他们的进攻方向区分
	
	GameView father;			//FieldView对象引用
		
	Bitmap [] bmpSelf;			//用于绘制自己的Bitmap数组
	Bitmap [] bmpEffect;		//用于绘制效果的Bitmap数组
	
	Timer timer = new Timer();		//创建定时器对象
	List<Bonus> owner;				//记录自己被添加到哪个集合中
	//设置一段时间后才可以从PREPARE到LIVE态
	public void setPrepareTimeout(int timeout){
		timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				Bonus.this.status = Bonus.LIVE;
				setTimeout(Bonus.LIFE_SPAN);
			}			
		}, 
		timeout);
	}
	//设置定时的方法
	public void setTimeout(int timeout){
		timer = new Timer();	
		timer.schedule(new TimerTask(){		//调用成员方法schedule来启动一个定时器
			@Override
			public void run() {
				Bonus.this.status = Bonus.DEAD;			//杀死Bonus
				Bonus.this.undoJob();					//调用undoJob方法
				Bonus.this.owner.remove(Bonus.this);	//从其所属的集合中移除自己
				father.balDelete.add(Bonus.this);		//将自己添加到待删除集合中
			}			
		}, 
		timeout);		
	}
	//画自己的方法
	public void drawSelf(Canvas canvas){
		canvas.drawBitmap(bmpSelf[(selfIndex++)%selfFrameNumber], x-bonusSize/2, y-bonusSize/2, null);
	}
	//抽象方法：绘制效果，需要子类重写
	public abstract void drawEffect(Canvas canvas);
	//抽象方法：修改后台数据，需要子类重写
	public abstract void doJob();
	//抽象方法：恢复后台数据，需要子类重写
	public abstract void undoJob();
	//抽象方法：设置目标，需要子类重写
	public abstract void setTarget(int lastKicker);
}