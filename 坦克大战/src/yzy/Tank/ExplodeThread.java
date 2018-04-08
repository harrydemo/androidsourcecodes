package yzy.Tank;

import java.util.ArrayList;
/**
 * 
 * 该类为爆炸的换帧线程
 * 没隔一段时间对GameView中explodeList中的爆炸换一帧
 *
 */

public class ExplodeThread extends Thread{
	private boolean flag = true;//循环标记位 
	private int span = 100;//睡眠的毫秒数
	GameView gameView;//GameView的引用
	
	ArrayList<Explode> deleteExplodes = new ArrayList<Explode>();//用于暂时存放需要删除的爆炸
	
	public ExplodeThread(GameView gameView){//构造器
		this.gameView = gameView;
	}
	
	public void setFlag(boolean flag){//设置循环标记位
		this.flag = flag;
	}
	
	public void run(){
		while(flag){
			try{//防止并发访问的异常
				for(Explode e : gameView.explodeList){
					if(e.nextFrame()){
					}
					else{//当没有下一帧时删除该爆炸
						deleteExplodes.add(e);
					}
				}
				gameView.explodeList.removeAll(deleteExplodes);
				deleteExplodes.clear();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			try{
				Thread.sleep(span);//睡眠休息
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}