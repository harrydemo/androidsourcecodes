package com.bn.d2.bill;
/**
 * 
 *���𵹼�ʱ���߳�
 *
 */
public class TimeRunningThread extends Thread {
	GameView gameView;
	private boolean flag=true;
	private int sleepSpan=1000;		
	public TimeRunningThread(GameView gameView)
	{
		this.gameView=gameView;
	}
	@Override
	public void run()
	{
		while(flag)
		{			
			 try{
	            	Thread.sleep(sleepSpan);//˯��ָ��������
	            }
	            catch(Exception e){
	            	e.printStackTrace();//��ӡ��ջ��Ϣ
	            }
	            gameView.timer.subtractTime(1);
		}
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
