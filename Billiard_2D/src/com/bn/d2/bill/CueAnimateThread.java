package com.bn.d2.bill;

public class CueAnimateThread extends Thread{
	GameView gameView;
	private boolean flag=true;
	private int sleepSpan=40;
	public CueAnimateThread(GameView gameView){
		this.gameView=gameView;
	}
	@Override
	public void run(){
		gameView.cue.setShowingAnimFlag(true);//标记正在播放击球动画
		while(flag)
		{
			//改变和母球的距离，直到击中母球
			if(gameView.cue.changeDisWithBall() <= 0){
				gameView.cue.resetAnimValues();//恢复和母球的初始距离
				break;
			}
			try{
            	Thread.sleep(sleepSpan);//睡眠指定毫秒数
            }
            catch(Exception e){
            	e.printStackTrace();//打印堆栈信息
            }
		}
		//打母球
		float v=Ball.vMax*(gameView.strengthBar.getCurrHeight()/gameView.strengthBar.getHeight());//将力度折算成母球的速度
		float angle=gameView.cue.getAngle();//获得球杆转的角度
		gameView.alBalls.get(0).changeVxy(v, angle);//改变母球的速度
		gameView.cue.setShowingAnimFlag(false);///打完球后，标记击球动画播放完毕
		gameView.cue.setShowCueFlag(false);//打完球后，隐藏球杆
		if(gameView.activity.isSoundOn()){//播放击球声音
			gameView.playSound(GameView.SHOOT_SOUND, 0);
		}		
	}
}
