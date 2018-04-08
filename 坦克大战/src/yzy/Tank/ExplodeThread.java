package yzy.Tank;

import java.util.ArrayList;
/**
 * 
 * ����Ϊ��ը�Ļ�֡�߳�
 * û��һ��ʱ���GameView��explodeList�еı�ը��һ֡
 *
 */

public class ExplodeThread extends Thread{
	private boolean flag = true;//ѭ�����λ 
	private int span = 100;//˯�ߵĺ�����
	GameView gameView;//GameView������
	
	ArrayList<Explode> deleteExplodes = new ArrayList<Explode>();//������ʱ�����Ҫɾ���ı�ը
	
	public ExplodeThread(GameView gameView){//������
		this.gameView = gameView;
	}
	
	public void setFlag(boolean flag){//����ѭ�����λ
		this.flag = flag;
	}
	
	public void run(){
		while(flag){
			try{//��ֹ�������ʵ��쳣
				for(Explode e : gameView.explodeList){
					if(e.nextFrame()){
					}
					else{//��û����һ֡ʱɾ���ñ�ը
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
				Thread.sleep(span);//˯����Ϣ
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}