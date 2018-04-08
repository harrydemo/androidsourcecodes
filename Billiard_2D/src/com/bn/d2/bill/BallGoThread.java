package com.bn.d2.bill;
 
import java.util.ArrayList;
//�����˶����߳�
public class BallGoThread extends Thread 
{
	GameView gameView;
	//�߳��Ƿ���������ı�־λ
	private boolean flag=true;		
	//���ڼ�¼ɾ���������
	ArrayList<Ball> ballsToDelete=new ArrayList<Ball>();//Ҫɾ��������б�
	private int sleepSpan=7;//�߳�˯��ʱ��
	public BallGoThread(GameView gameView)
	{
		this.gameView=gameView;		
	}
	public void run()
	{
		while(flag)
		{
			ballsToDelete.clear();//���Ҫɾ�����б�
			//����������
			for(Ball b:gameView.alBalls){
				b.go();
				//ÿ��һ���ж��Ƿ����
				if(b.isInHole()){					
					if(b==gameView.alBalls.get(0)){//����ǰ������
						b.hide();//����������������������ɾ��
					}
					else{//�����ͨ�����
						ballsToDelete.add(b);//�������������ɾ���б�
					}					
				}
			}
			gameView.alBalls.removeAll(ballsToDelete);//���б���ɾ����������
			//�ж��Ƿ������ʾ��˲�����
			boolean allBallsStoppedFlag=true;//�������Ƿ�ͣ�˵ı�־
			for(Ball b:gameView.alBalls){
				if(!b.isStoped()){//ֻҪ��һ����û��ͣ����־λ��Ϊfalse
					allBallsStoppedFlag=false;
					break;
				}
			}
			if(allBallsStoppedFlag){//���������ͣ��
				if(gameView.alBalls.get(0).isHided()){//��������������������
					gameView.alBalls.get(0).reset();//���������¸�λ
				}
				gameView.cue.setShowCueFlag(true);//��ʾ���
				//�����ȫ����������Ϸ����
				if(gameView.alBalls.size()<=1){
					gameView.overGame();
				}
			}
			//�߳�����һ����ʱ��
			try {
				Thread.sleep(sleepSpan);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}