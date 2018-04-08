package com.bn.d2.bill;
 
import java.util.ArrayList;
//桌球运动的线程
public class BallGoThread extends Thread 
{
	GameView gameView;
	//线程是否继续工作的标志位
	private boolean flag=true;		
	//用于记录删除球的引用
	ArrayList<Ball> ballsToDelete=new ArrayList<Ball>();//要删除的球的列表
	private int sleepSpan=7;//线程睡眠时间
	public BallGoThread(GameView gameView)
	{
		this.gameView=gameView;		
	}
	public void run()
	{
		while(flag)
		{
			ballsToDelete.clear();//清除要删除的列表
			//让所有球走
			for(Ball b:gameView.alBalls){
				b.go();
				//每走一步判断是否进洞
				if(b.isInHole()){					
					if(b==gameView.alBalls.get(0)){//如果是白球进洞
						b.hide();//将白球隐藏起来，但并不删除
					}
					else{//如果普通球进洞
						ballsToDelete.add(b);//将进洞的球加入删除列表
					}					
				}
			}
			gameView.alBalls.removeAll(ballsToDelete);//从列表中删除进洞的球
			//判断是否可以显示球杆并击球
			boolean allBallsStoppedFlag=true;//所有球是否都停了的标志
			for(Ball b:gameView.alBalls){
				if(!b.isStoped()){//只要有一个球还没有停，标志位置为false
					allBallsStoppedFlag=false;
					break;
				}
			}
			if(allBallsStoppedFlag){//如果所有球都停了
				if(gameView.alBalls.get(0).isHided()){//如果白球因进洞而被隐藏
					gameView.alBalls.get(0).reset();//将白球重新复位
				}
				gameView.cue.setShowCueFlag(true);//显示球杆
				//如果球全部进洞，游戏结束
				if(gameView.alBalls.size()<=1){
					gameView.overGame();
				}
			}
			//线程休眠一定的时间
			try {
				Thread.sleep(sleepSpan);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}