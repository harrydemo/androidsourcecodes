package wyf.wpf;				//声明包语句
import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
/*
 * 该类是继承自Bonus类的，实现的效果是如果足球碰到了该Bonus，会将对方球员冰冻，在一定时间内无法移动。
 * 该类重写了父类的drawEfect、doJob、undoJob等方法。
 */
public class IceBonus extends Bonus{
	//构造器，根据传入的参数初始化主要成员变量
	public IceBonus(GameView father,int x,int y){
		this.father = father;
		Resources r = father.getContext().getResources();	//获得资源对象
		this.bmpSelf = new Bitmap[2];		//初始化用于显示自己的图片数组
		this.bmpSelf[0] = BitmapFactory.decodeResource(r, R.drawable.bonus_i1);
		this.bmpSelf[1] = BitmapFactory.decodeResource(r, R.drawable.bonus_i2);
		this.selfFrameNumber = 2;			//设定帧总数
		this.bonusSize = 30;				//初始化Bonus的大小
		this.x = x;							//初始化X坐标
		this.y = y;							//初始化Y坐标
		this.bmpEffect = new Bitmap[1];		//初始化效果图片
		bmpEffect[0] = BitmapFactory.decodeResource(r, R.drawable.ice);
	}
	
	@Override
	public void drawEffect(Canvas canvas){//重写drawEffect方法，根据target不同，冻住相应的球员
		ArrayList<Player> tempTarget = null;
		if(target == 8){		//如果目标是AI
			tempTarget = father.alAIPlayer;	//临时目标列表指向AI的球员列表
		}
		else if(target == 0){	//如果目标是玩家
			tempTarget = father.alMyPlayer;//临时目标列表指向玩家的球员列表
		}
		for(Player p:tempTarget){	//遍历玩家的球员列表
			int x = p.x;			//获取球员的X坐标
			int y = p.y;			//获取球员的X坐标
			canvas.drawBitmap(bmpEffect[0], x-bonusSize/2+3, y-bonusSize/2, null);//绘制冰块，3是修正值
		}
	}
	@Override
	public void doJob() {
		if(father.ball.lastKicker == 0){				//最后一脚是玩家踢得，冻住AI
			father.father.pmt.aiMoving = false;			//设置AI不能移动
		}
		else{											//最后一脚是AI踢得，冻住玩家
			father.father.pmt.myMoving = false;			//设置玩家不能移动
		}
	}
	@Override
	public void undoJob() {
		if(target == 8){				//目标是AI,冻住的是AI的球员
			father.father.pmt.aiMoving = true;		//解冻AI
		}
		else if(target == 0){			//目标是玩家,冻住的是玩家的球员
			father.father.pmt.myMoving = true;		//解冻玩家
		}
	}
	@Override
	public void setTarget(int lastKicker) {//设定作用的目标
		this.target = (lastKicker == 0?8:0);//如果是玩家踢的，目标就是AI；反过来目标就是玩家
	}
}