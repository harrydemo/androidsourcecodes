package yzy.Tank;

import java.util.ArrayList;

public class MoveThread extends Thread {
	private boolean flag = true;// ѭ����־λ
	GameView gameView;// GameView������
	ArrayList<Bullet> deleteBollet = new ArrayList<Bullet>();//ɾ���ӵ�����
	ArrayList<EnemyTank> deleteEnemy = new ArrayList<EnemyTank>();
	ArrayList<Life> deleteLife = new ArrayList<Life>();
	private int countEnemyFire = 0;//�л����ӵ�������
	private int countEnemyFireN = 20;//ÿ���ٴ�ѭ����һ���ӵ� 
	private int countEnemyBolletMove = 0;//�з��ӵ��ƶ��ļ�����
	private int countEnemyBolletN = 3;//ÿ���ٴ�ѭ���ƶ�һ�� 
	public MoveThread(GameView gameView) {// ������
		this.gameView = gameView;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void run() {
		while (flag) {
			try {//�ҷ��ӵ��ƶ�
				for (Bullet b : gameView.goodBollets) {
					b.move();
					if(b.x<0 || b.x>ConstantUtil.screenWidth|| b.y<0 || b.y>ConstantUtil.screenHeight){//��������Ļ��Χ��
						deleteBollet.add(b);//���ӵ���ӵ�ɾ���ӵ�����
					}else{//����Ļ��Χ��
						for(EnemyTank ep : gameView.enemyTanks){
							if(ep.status == true){
								if(ep.contain(b,gameView)){//���е���
									if(ep.life==0){
										Explode e= null;
										if(ep.type==5){
											 e = new Explode(ep.x+61, ep.y+90, gameView,1);//����BOSS��ը����
										}else{
											 e = new Explode(ep.x-15, ep.y-15, gameView,1);//������ͨ��̹��ը����
										}
										if(e!=null)
											gameView.explodeList.add(e);//��ӱ�ը����
									}
										deleteBollet.add(b);//ɾ���ҷ��ӵ�
									if(gameView.activity.isSound){
										gameView.playSound(3,0);
									}
									
								}
							}
						}
					}
					gameView.goodBollets.removeAll(deleteBollet);//ɾ���ӵ�
					deleteBollet.clear();//���
				}
			} catch (Exception e) {}
			
			try{//��̹���ƶ�
				for(EnemyTank ep : gameView.enemyTanks){
					if(ep.status == true){
						ep.move();
						if(ep.getX()<-ep.bitmap.getWidth() || ep.getX()>ConstantUtil.screenWidth
								|| ep.getY()<-ep.bitmap.getHeight() || ep.getY()>ConstantUtil.screenHeight){
							deleteEnemy.add(ep);
						}
						else{
							if(gameView.tank.contain(ep)){//�ط�̹�������ҷ�̹��
								Explode e=null;
								if(ep.type==5)
									e = new Explode(ep.x+61, ep.y+90, gameView,1);//����BOSS��ը����
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
			try{//�ƶ�Ѫ��
				for(Life l : gameView.lifes){
					if(l.status == true){
						l.move();
						if(l.x<-l.bitmap.getWidth() || l.x>ConstantUtil.screenWidth
								|| l.y<-l.bitmap.getHeight() || l.y>ConstantUtil.screenHeight){
							deleteLife.add(l);
						}
						else{
							if(gameView.tank.contain(l)){//��ײ��Ѫ��
								deleteLife.add(l);
								gameView.playSound(5,0);//��Ѫ��Ч
							}
						}
					}
				}
				gameView.lifes.removeAll(deleteLife);
				deleteLife.clear();
			}catch(Exception e){}
			if(countEnemyFire == 0){//��̹���ӵ�
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
			if(countEnemyBolletMove == 0){//�з��ӵ��ƶ�
				try{
					for(Bullet b : gameView.badBollets){
						b.move();
						if(b.x<0 || b.x>ConstantUtil.screenWidth|| b.y<0 || b.y>ConstantUtil.screenHeight){
							deleteBollet.add(b);
						}else if(b.type==3&b.y>=383){//��������������� ��ը
							Explode e = new Explode(b.x+12, b.y+12, gameView,2);
							gameView.explodeList.add(e);
							deleteBollet.add(b);
							gameView.playSound(3,0);//������ը��Ч
						}
						else{
							if(gameView.tank.contain(b)){//��ײ����Ƿ�����ҷ�̹��
								/*Explode e = new Explode(b.x, b.y, gameView,1);
								gameView.explodeList.add(e);*/
								Explode e = new Explode(b.x+12, b.y+12, gameView,2);
								gameView.explodeList.add(e);
								gameView.playSound(3, 0);
								deleteBollet.add(b);//���к�ɾ���ӵ�
							}
						}
					}
					gameView.badBollets.removeAll(deleteBollet);
					deleteBollet.clear();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			countEnemyFire = (countEnemyFire+1)%countEnemyFireN;//ѭ�����Լ�
			countEnemyBolletMove = (countEnemyBolletMove+1)%countEnemyBolletN;//ѭ�����Լ�
			
			try {// ˯��
				Thread.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
