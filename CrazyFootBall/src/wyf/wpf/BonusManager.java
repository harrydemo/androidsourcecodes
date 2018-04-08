package wyf.wpf;		//声明包语句
/*
 * 此类继承自Thread，主要实现对Bonus对象的管理，通过定期检测屏幕上Bonus的个数，
 * 根据概率随机生成Bonus。
 */
public class BonusManager extends Thread{
	boolean flag = false;		//设置线程执行标志位
	GameView father;			//FieldView对象引用
	int sleepSpan = 3000;		//休眠时间
	int maxBonus = 2;		//设置最大的Bonus个数
	//构造器，初始化主要成员变量
	public BonusManager(GameView father){
		this.father = father;
		this.flag = true;
	}
	//方法：线程的run方法
	public void run(){
		while(flag){
			//只在游戏正常状态下才产生Bonus
			if((!father.isGameOver) && (!father.isScoredAGoal) &&(!father.isShowDialog)){
				generateBonus();		//调用generateBonus方法产生Bonus
			}
			try{
				Thread.sleep(sleepSpan);	//休眠一段时间
			}
			catch(Exception e){
				e.printStackTrace();		//打印捕获异常
			}
		}
	}
	//随机生成Bonus
	public void generateBonus(){		
		int currentBonusNumber = father.balLive.size();		//获取活着的Bonus的个数
		float generateOdd = 1 - currentBonusNumber*0.33f;			//计算生成概率
		if(Math.random() < generateOdd){		//产生一个随机数，如果小于生成概率
			int x = (int)(Math.random() * (father.fieldRight-father.fieldLeft))+ father.fieldLeft;
			int y = (int)(Math.random() * (father.fieldDown - father.fieldUp)) + father.fieldUp;
			Bonus b;
			if(Math.random() > 0.5){			//产生随机数，如果值小于0.5，则创建IceBonus
				b = new IceBonus(father,x,y);
			}
			else{								//如果随机数小于0.5，则创建LargerGoalBonus
				b = new LargerGoalBonus(father,x,y);
			}	
			b.status = Bonus.PREPARE;	//设置状态为准备态	
			father.balLive.add(b);				//将Bonus添加到用于碰撞检测的集合中
			b.owner = father.balLive;		//设置其所属者			
			father.balAdd.add(b);			//将Bonus添加到待添加集合中			
			b.setPrepareTimeout(Bonus.PREPARE_SPAN);//为刚生成的Bonus设置准备超时
		}
	}	
}