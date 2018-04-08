package wyf.wpf;
/*
 * 该类为AI的后台线程，实现的功能是让AI足球运动员根据足球的运动的参数
 * 来确定自己的运动方向（向左还是右）。由于人工智能算法比较复杂，本书也不是
 * 专门用来讲解算法原理的，所以采用了一个比较简单的算法，即如果足球的方向偏左
 * （即可以是左上、左下、正坐等，共7个方向）， 那么AI的运动方向就是向左，反之
 * 则向右。
 */
public class AIThread extends Thread{
	GameView father;			//视图类引用
	boolean flag;				//循环控制变量
	int sleepSpan = 30;			//睡眠时间
	//构造器,初始化成员变量
	public AIThread(GameView father){	
		this.father = father;
		flag = true;	//设置线程标志位
	}
	//线程启动后的执行方法
	public void run(){
		while(flag){ 
			int d = father.ball.direction;		//获取足球运动方向
			if(d >0 && d<8){					//如果足球方向偏左
				father.aiDirection = 4;			//AI运动方向改为向左   
			}
			else if(d>8 && d<15){				//如果足球方向偏右
				father.aiDirection = 12;		//AI运动方向改为向右
			}			
			try{
				Thread.sleep(sleepSpan);		//休眠一段时间
			}
			catch(Exception e){
				e.printStackTrace();			//打印并捕获异常
			}
		}
	}
}