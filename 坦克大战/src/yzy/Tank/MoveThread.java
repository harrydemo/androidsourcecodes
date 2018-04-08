package yzy.Tank;

import java.util.ArrayList;

public class MoveThread extends Thread {
	private boolean flag = true;// 循环标志位
	GameView gameView;// GameView的引用
	ArrayList<Bullet> deleteBollet = new ArrayList<Bullet>();//删除子弹集合
	ArrayList<EnemyTank> deleteEnemy = new ArrayList<EnemyTank>();
	ArrayList<Life> deleteLife = new ArrayList<Life>();
	private int countEnemyFire = 0;//敌机发子弹计数器
	private int countEnemyFireN = 20;//每多少次循环大一发子弹 
	private int countEnemyBolletMove = 0;//敌方子弹移动的计数器
	private int countEnemyBolletN = 3;//每多少次循环移动一下 
	public MoveThread(GameView gameView) {// 构造器
		this.gameView = gameView;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void run() {
		while (flag) {
			try {//我放子弹移动
				for (Bullet b : gameView.goodBollets) {
					b.move();
					if(b.x<0 || b.x>ConstantUtil.screenWidth|| b.y<0 || b.y>ConstantUtil.screenHeight){//当出了屏幕范围后
						deleteBollet.add(b);//把子弹添加到删除子弹集合
					}else{//在屏幕范围内
						for(EnemyTank ep : gameView.enemyTanks){
							if(ep.status == true){
								if(ep.contain(b,gameView)){//打中敌人
									if(ep.life==0){
										Explode e= null;
										if(ep.type==5){
											 e = new Explode(ep.x+61, ep.y+90, gameView,1);//创建BOSS爆炸对象
										}else{
											 e = new Explode(ep.x-15, ep.y-15, gameView,1);//创建普通敌坦爆炸对象
										}
										if(e!=null)
											gameView.explodeList.add(e);//添加爆炸对象
									}
										deleteBollet.add(b);//删除我方子弹
									if(gameView.activity.isSound){
										gameView.playSound(3,0);
									}
									
								}
							}
						}
					}
					gameView.goodBollets.removeAll(deleteBollet);//删除子弹
					deleteBollet.clear();//清空
				}
			} catch (Exception e) {}
			
			try{//敌坦克移动
				for(EnemyTank ep : gameView.enemyTanks){
					if(ep.status == true){
						ep.move();
						if(ep.getX()<-ep.bitmap.getWidth() || ep.getX()>ConstantUtil.screenWidth
								|| ep.getY()<-ep.bitmap.getHeight() || ep.getY()>ConstantUtil.screenHeight){
							deleteEnemy.add(ep);
						}
						else{
							if(gameView.tank.contain(ep)){//地方坦克碰到我放坦克
								Explode e=null;
								if(ep.type==5)
									e = new Explode(ep.x+61, ep.y+90, gameView,1);//创建BOSS爆炸对象
								else
									e = new Explode(ep.x-15, ep.y-15, gameView,1);
								gameView.explodeList.add(e);
								ep.life--;
								if(ep.life <=0){
									deleteEnemy.add(ep);
									if(gameView.activity.isSound){
										gameView.playSound(3,0);
									}
								}
							}
						}
					}
				}
				gameView.enemyTanks.removeAll(deleteEnemy);
				deleteEnemy.clear();
			}catch(Exception e){}
			try{//移动血块
				for(Life l : gameView.lifes){
					if(l.status == true){
						l.move();
						if(l.x<-l.bitmap.getWidth() || l.x>ConstantUtil.screenWidth
								|| l.y<-l.bitmap.getHeight() || l.y>ConstantUtil.screenHeight){
							deleteLife.add(l);
						}
						else{
							if(gameView.tank.contain(l)){//碰撞到血块
								deleteLife.add(l);
								gameView.playSound(5,0);//吃血音效
							}
						}
					}
				}
				gameView.lifes.removeAll(deleteLife);
				deleteLife.clear();
			}catch(Exception e){}
			if(countEnemyFire == 0){//敌坦打子弹
				try{
					for(EnemyTank ep : gameView.enemyTanks){
						if(ep.status == true){
							ep.fire(gameView);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(countEnemyBolletMove == 0){//敌方子弹移动
				try{
					for(Bullet b : gameView.badBollets){
						b.move();
						if(b.x<0 || b.x>ConstantUtil.screenWidth|| b.y<0 || b.y>ConstantUtil.screenHeight){
							deleteBollet.add(b);
						}else if(b.type==3&b.y>=383){//如果导弹碰到地面 爆炸
							Explode e = new Explode(b.x+12, b.y+12, gameView,2);
							gameView.explodeList.add(e);
							deleteBollet.add(b);
							gameView.playSound(3,0);//导弹爆炸音效
						}
						else{
							if(gameView.tank.contain(b)){//碰撞检测是否打中我方坦克
								/*Explode e = new Explode(b.x, b.y, gameView,1);
								gameView.explodeList.add(e);*/
								Explode e = new Explode(b.x+12, b.y+12, gameView,2);
								gameView.explodeList.add(e);
								gameView.playSound(3, 0);
								deleteBollet.add(b);//打中后删除子弹
							}
						}
					}
					gameView.badBollets.removeAll(deleteBollet);
					deleteBollet.clear();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			countEnemyFire = (countEnemyFire+1)%countEnemyFireN;//循环的自加
			countEnemyBolletMove = (countEnemyBolletMove+1)%countEnemyBolletN;//循环的自加
			
			try {// 睡眠
				Thread.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
