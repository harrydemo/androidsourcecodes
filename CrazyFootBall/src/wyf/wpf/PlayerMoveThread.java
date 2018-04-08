package wyf.wpf;			//声明包语句
/*
 * 该类继承自Thread，主要功能是，根据双方运动员的方向，修改其后台位置数据
 * 实现方法是每个一段时间，读取FootballActivity的direction属性，判断
 * 标志位并执行修改，通过调用FielView类的movePlayers方法来修改
 */
public class PlayerMoveThread extends Thread{
	FootballActivity father;	//Activity的引用
	boolean outerFlag;			//线程执行标志位
	boolean flag;				//是否需要移动球员的位置标志位
	int sleepSpan = 20;			//线程休眠时间
	boolean myMoving;		//为true表示玩家可移动，为false表示玩家不可动
	boolean aiMoving;		//为true表示AI可移动，为false表示AI不可动
	//构造器，初始化主要成员变量
	public PlayerMoveThread(FootballActivity father){
		super.setName("##-PlayerMoveThread");	//为线程设置名称，调试使用
		this.father = father;
		outerFlag = true;
		flag = true;
		myMoving = true;			//初始状态玩家的球员是可移动的
		aiMoving = true;			//初始状态电脑AI的球员是可移动的
	}
	//方法：线程的执行方法
	public void run(){
		while(outerFlag){
			while(flag){
				//修改玩家和AI运动员的位置				
				if(father.current == father.gv){	//如果FieldView是当前屏幕
					if(myMoving){					//如果玩家的球员是可移动的
						int key = father.keyState;	//读取键盘监听状态
						if((key & 1) == 1){			//键盘状态为向右
							father.gv.movePlayers(father.gv.alMyPlayer, 4);//调用方法向右移动球员
						}
						else if((key & 2) == 2){	//键盘状态为向左
							father.gv.movePlayers(father.gv.alMyPlayer, 12);	//调用方法向左移动球员
						}
						else{						//将direction设置为静止-1
							father.gv.movePlayers(father.gv.alMyPlayer, -1);
						}
					}			
					if(aiMoving){					//判断AI是否可以移动
						int d = father.gv.aiDirection;						//读取AI球员的运动方向
						father.gv.movePlayers(father.gv.alAIPlayer, d);	//修改AI运动员的位置
					}	
				}
				try{
					Thread.sleep(sleepSpan);		//线程休眠一段时间
				}
				catch(Exception e){
					e.printStackTrace();			//打印并捕获异常
				}			
			}
			try{
				Thread.sleep(300);			//当不需要移动玩家时，线程空转后睡眠一段时间
			}
			catch(Exception e){
				e.printStackTrace();		//捕获并打印异常
			}	
		}
	}
}