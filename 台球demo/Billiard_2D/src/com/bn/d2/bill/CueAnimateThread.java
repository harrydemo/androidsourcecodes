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
		gameView.cue.setShowingAnimFlag(true);//������ڲ��Ż��򶯻�
		while(flag)
		{
			//�ı��ĸ��ľ��룬ֱ������ĸ��
			if(gameView.cue.changeDisWithBall() <= 0){
				gameView.cue.resetAnimValues();//�ָ���ĸ��ĳ�ʼ����
				break;
			}
			try{
            	Thread.sleep(sleepSpan);//˯��ָ��������
            }
            catch(Exception e){
            	e.printStackTrace();//��ӡ��ջ��Ϣ
            }
		}
		//��ĸ��
		float v=Ball.vMax*(gameView.strengthBar.getCurrHeight()/gameView.strengthBar.getHeight());//�����������ĸ����ٶ�
		float angle=gameView.cue.getAngle();//������ת�ĽǶ�
		gameView.alBalls.get(0).changeVxy(v, angle);//�ı�ĸ����ٶ�
		gameView.cue.setShowingAnimFlag(false);///������󣬱�ǻ��򶯻��������
		gameView.cue.setShowCueFlag(false);//��������������
		if(gameView.activity.isSoundOn()){//���Ż�������
			gameView.playSound(GameView.SHOOT_SOUND, 0);
		}		
	}
}
