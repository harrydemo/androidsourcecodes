package com.bn.d2.bill;
/**
 * 监听触控按键的线程类
 */
public class KeyThread extends Thread {
	private boolean flag=true;	
	GameView gameView;
	private int sleepSpan=40;
	private float changeSpeedTime=80f;//改变速度的时间点
	public KeyThread(GameView gameView)
	{
		this.gameView=gameView;
	}
	@Override
	public void run()
	{
		while(flag)
		{
			if(!((gameView.keyState&0x20)==0))//第6位是1，可以改变按下按钮时间
			{
				gameView.btnPressTime+=3.5f;
			}
			if(!((gameView.keyState&0x1)==0))//和00001按位或，判断第1位是否为1，标志向左移
			{
				if(gameView.btnPressTime<changeSpeedTime)//如果按键时间不足
				{
					gameView.cue.rotateLeftSlowly();//将球杆慢速左转
				}
				else//如果按键超过规定时间
				{
					gameView.cue.rotateLeftFast();//将大炮快速左转
				}		
			}
			else if(!((gameView.keyState&0x2)==0))//和00010按位或，判断第2位是否为1，标志向右移
			{
				if(gameView.btnPressTime<changeSpeedTime)//如果按键时间不足
				{
					gameView.cue.rotateRightSlowly();//将球杆慢速右转
				}
				else//如果按键超过规定时间
				{
					gameView.cue.rotateRightFast();//将大炮快速右转
				}
			}
			try{
            	Thread.sleep(sleepSpan);//睡眠指定毫秒数
            }
            catch(Exception e){
            	e.printStackTrace();//打印堆栈信息
            }
		}
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
