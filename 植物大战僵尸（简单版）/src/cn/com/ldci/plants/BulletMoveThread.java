package cn.com.ldci.plants;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class BulletMoveThread extends Thread{
	private int sleepSpan = 80;//睡眠的毫秒数
	private boolean flag = true;//循环标志位 
	GameView gameView;//GameView的引用
	ArrayList<Bullets> deleteBollet = new ArrayList<Bullets>();
	ArrayList<Zomebie> deleteZomebie = new ArrayList<Zomebie>();
	int count = 0;
	public BulletMoveThread(GameView gameView) {
		this.gameView=gameView;
	}
	public void setFlag(boolean b) {
		// TODO Auto-generated method stub
		
	}
public void run(){
		
		while(flag){
			
			try{
				for(Bullets bs : gameView.goodBollets1){
					bs.move();
					if(bs.x>480){
						deleteBollet.add(bs);
					}else{
						for(Zomebie zm :gameView.zombies){
							if(zm.avlive==true){
								if(zm.checkHitThing(bs)){
									deleteBollet.add(bs);
								}
								if(zm.lifes <= 0){
									deleteZomebie.add(zm);
								}
							}
						}
					}
				}
				gameView.goodBollets1.removeAll(deleteBollet);
				deleteBollet.clear();
				for(Bullets bs : gameView.goodBollets2){
					bs.move();
					if(bs.x>480){
						deleteBollet.add(bs);
					}else{
						for(Zomebie zm :gameView.zombies){
							if(zm.avlive==true){
								if(zm.checkHitThing(bs)){
									deleteBollet.add(bs);
								}
								if(zm.lifes <= 0){
									deleteZomebie.add(zm);
								}
							}
						}
					}
				}
				gameView.goodBollets2.removeAll(deleteBollet);
				deleteBollet.clear();
				for(Bullets bs : gameView.goodBollets3){
					bs.move();
					if(bs.x>480){
						deleteBollet.add(bs);
					}else{
						for(Zomebie zm :gameView.zombies){
							if(zm.avlive==true){
								if(zm.checkHitThing(bs)){
									deleteBollet.add(bs);
								}
								if(zm.lifes <= 0){
									deleteZomebie.add(zm);
								}
							}
						}
					}
				}
				gameView.goodBollets3.removeAll(deleteBollet);
				gameView.zombies.removeAll(deleteZomebie);
				deleteBollet.clear();
				deleteZomebie.clear();
			}catch(Exception e){
				
			}
			try{//睡眠休息
				Thread.sleep(sleepSpan);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}


