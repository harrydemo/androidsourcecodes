package com.bn.d2.bill;
import android.view.SurfaceHolder;

public class GameViewDrawThread extends Thread{
	private boolean flag = true;	
	private int sleepSpan = 10;
	GameView gameView;
	SurfaceHolder surfaceHolder;
	public GameViewDrawThread(GameView gameView){
		this.gameView = gameView;
		this.surfaceHolder = gameView.getHolder();
	}
	public void run(){	
        while (this.flag) {
            gameView.repaint();
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
