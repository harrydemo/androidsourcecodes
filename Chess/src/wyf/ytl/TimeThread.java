package wyf.ytl;
/**
 * 
 * 该类为计算思考时间的线程类
 * 谁正在思考，该类便将谁的总思考时间加一
 *
 */
public class TimeThread extends Thread{
	private boolean flag = true;//循环标志
	GameView gameView;
	public TimeThread(GameView gameView){//构造器
		this.gameView = gameView;//得到GameView引用
	}
	public void setFlag(boolean flag){//设置循环标记位
		this.flag = flag;
	}
	@Override
	public void run(){//重写的run方法
		while(flag){//循环
			if(gameView.caiPan == false){//当前为黑方走棋、思考
				gameView.heiTime++;//黑方时间自加
			}
			else if(gameView.caiPan == true){//当前为红方走棋、思考
				gameView.hongTime++;//红方时间自加
			}
			try{
				Thread.sleep(1000);//睡眠一秒种
			}
			catch(Exception e){//捕获异常
				e.printStackTrace();//打印异常信息
			}
		}
	}
}