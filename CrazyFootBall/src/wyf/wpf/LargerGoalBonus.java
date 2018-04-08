package wyf.wpf;			//声明包语句
import android.content.res.Resources;		//引入相关类
import android.graphics.Bitmap;				//引入相关类
import android.graphics.BitmapFactory;		//引入相关类
import android.graphics.Canvas;				//引入相关类
import android.graphics.Matrix;				//引入相关类
/*
 * 该类是继承自Bonus类的，实现的效果是如果足球碰到了该Bonus，会将对方球门在一定时间内增大。
 * 该类重写了父类的drawEfect、doJob、undoJob等方法。
 */
public class LargerGoalBonus extends Bonus{
	GameView father;			//FileView对象引用
	int effectWidth;			//
	int effectHeight;
	//构造器，初始化主要成员变量
	public LargerGoalBonus(GameView father,int x,int y){
		this.father = father;
		Resources r = father.getContext().getResources();//获取资源对象
		bmpSelf = new Bitmap[2];		//初始化显示自己的图片数组
		bmpSelf[0] = BitmapFactory.decodeResource(r, R.drawable.bonus_g1);
		bmpSelf[1] = BitmapFactory.decodeResource(r, R.drawable.bonus_g2);
		this.selfFrameNumber = 2;
		bmpEffect = new Bitmap[2];		//初始化用于绘制效果的图片数组 
		bmpEffect[0] = BitmapFactory.decodeResource(r, R.drawable.goal1);
		bmpEffect[1] = BitmapFactory.decodeResource(r, R.drawable.goal2);
		this.effectFrameNumber =2;		
		this.x = x;						//初始化X坐标
		this.y = y;						//初始化Y坐标
		this.effectWidth = 126;			//超长球门的图片宽度，用于绘制超长球门和用于改变球门的大小
		this.effectHeight = 8;			//超长球门的图片高度，用于绘制超长球门
		this.bonusSize = 30;			//自身的大小
	}
	@Override
	public void doJob() {
		if(target == 0){			//作用对象是玩家，玩家的球门变宽
			father.myGoalLeft = (father.maxLeftPosition + father.maxRightPosition)/2 - effectWidth/2;
			father.myGoalRight = (father.maxLeftPosition + father.maxRightPosition)/2 + effectWidth/2;
		}
		else if(target == 8){		//作用对象是AI，AI的球门变宽
			father.AIGoalLeft = (father.maxLeftPosition + father.maxRightPosition)/2 - effectWidth/2;
			father.AIGoalRight = (father.maxLeftPosition + father.maxRightPosition)/2 + effectWidth/2;
		}
	}
	@Override
	public void drawEffect(Canvas canvas) {//方法：绘制超长球门效果
		int x = (father.maxLeftPosition + father.maxRightPosition)/2 - effectWidth/2;//计算超长球门图片的X坐标
		if(target == 8){		//如果作用目标是AI
			int y = father.fieldUp-4;		//计算AI超长球门的Y坐标
			canvas.drawBitmap(bmpEffect[(effectIndex++)%effectFrameNumber], x, y, null);//绘制超长球门
		}
		else if(target == 0){//如果作用目标是玩家
			int y = father.fieldDown+2;		//计算玩家超长球门的Y坐标
			Matrix m = new Matrix();
			m.postRotate(180);
			//将超长球门掉转180度，默认超长球门的图片用在AI那边的球门正好
			Bitmap bmp = Bitmap.createBitmap(bmpEffect[(effectIndex++)%effectFrameNumber], 0, 0, this.effectWidth, effectHeight,m, true);
			canvas.drawBitmap(bmp, x, y, null);//绘制超长球门
		}		
	}
	@Override
	public void undoJob() {
		if(target == 0){			//作用对象是玩家
			father.myGoalLeft = father.maxLeftPosition;		//恢复玩家的球门大小
			father.myGoalRight = father.maxRightPosition;
		}
		else if(target == 8){		//作用对象是AI
			father.AIGoalLeft = father.maxLeftPosition;		//恢复AI的球门大小
			father.AIGoalRight = father.maxRightPosition;
		}
	}
	@Override
	public void setTarget(int lastKicker) {		//设定作用目标对象
		this.target = (lastKicker == 0?8:0);
	}
	
}