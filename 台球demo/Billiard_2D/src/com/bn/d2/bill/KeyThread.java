package com.bn.d2.bill;
/**
 * �������ذ������߳���
 */
public class KeyThread extends Thread {
	private boolean flag=true;	
	GameView gameView;
	private int sleepSpan=40;
	private float changeSpeedTime=80f;//�ı��ٶȵ�ʱ���
	public KeyThread(GameView gameView)
	{
		this.gameView=gameView;
	}
	@Override
	public void run()
	{
		while(flag)
		{
			if(!((gameView.keyState&0x20)==0))//��6λ��1�����Ըı䰴�°�ťʱ��
			{
				gameView.btnPressTime+=3.5f;
			}
			if(!((gameView.keyState&0x1)==0))//��00001��λ���жϵ�1λ�Ƿ�Ϊ1����־������
			{
				if(gameView.btnPressTime<changeSpeedTime)//�������ʱ�䲻��
				{
					gameView.cue.rotateLeftSlowly();//�����������ת
				}
				else//������������涨ʱ��
				{
					gameView.cue.rotateLeftFast();//�����ڿ�����ת
				}		
			}
			else if(!((gameView.keyState&0x2)==0))//��00010��λ���жϵ�2λ�Ƿ�Ϊ1����־������
			{
				if(gameView.btnPressTime<changeSpeedTime)//�������ʱ�䲻��
				{
					gameView.cue.rotateRightSlowly();//�����������ת
				}
				else//������������涨ʱ��
				{
					gameView.cue.rotateRightFast();//�����ڿ�����ת
				}
			}
			try{
            	Thread.sleep(sleepSpan);//˯��ָ��������
            }
            catch(Exception e){
            	e.printStackTrace();//��ӡ��ջ��Ϣ
            }
		}
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
