package cn.com.ldci.plants;

import java.util.ArrayList;


public class MoveThread extends Thread {
	private int sleepSpan = 300;//˯�ߵĺ�����
	private boolean flag = true;//ѭ����־λ 
	GameView gameView;//GameView������
	ArrayList<Bullets> deleteBollet = new ArrayList<Bullets>();
	int count = 0;
	public MoveThread(GameView gameView) {
		this.gameView=gameView;
	}
	public void setFlag(boolean b) {
		// TODO Auto-generated method stub
		
	}
	public void run(){
		
		while(flag){
			try{
				for(BestZomebie b : gameView.zombies){
					if(b.currentX<=0){
						gameView.status=2;
						gameView.activity.myHandler.sendEmptyMessage(1);
						
					}
					b.move();
				}
			}catch(Exception e){
				
			}
			try{
				if(count == 0){
				for(Plants ps : gameView.plants){
					ps.fire(gameView);
				}	
				}
				count = (count+1)%5;
			}catch(Exception e){
				
			}
			try{
				gameView.sun.move();
			}catch(Exception e){
				
			}
			try{//˯����Ϣ
				Thread.sleep(sleepSpan);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
