package wyf.wpf;
/*
 * ����ΪAI�ĺ�̨�̣߳�ʵ�ֵĹ�������AI�����˶�Ա����������˶��Ĳ���
 * ��ȷ���Լ����˶������������ң��������˹������㷨�Ƚϸ��ӣ�����Ҳ����
 * ר�����������㷨ԭ��ģ����Բ�����һ���Ƚϼ򵥵��㷨�����������ķ���ƫ��
 * �������������ϡ����¡������ȣ���7�����򣩣� ��ôAI���˶�����������󣬷�֮
 * �����ҡ�
 */
public class AIThread extends Thread{
	GameView father;			//��ͼ������
	boolean flag;				//ѭ�����Ʊ���
	int sleepSpan = 30;			//˯��ʱ��
	//������,��ʼ����Ա����
	public AIThread(GameView father){	
		this.father = father;
		flag = true;	//�����̱߳�־λ
	}
	//�߳��������ִ�з���
	public void run(){
		while(flag){ 
			int d = father.ball.direction;		//��ȡ�����˶�����
			if(d >0 && d<8){					//���������ƫ��
				father.aiDirection = 4;			//AI�˶������Ϊ����   
			}
			else if(d>8 && d<15){				//���������ƫ��
				father.aiDirection = 12;		//AI�˶������Ϊ����
			}			
			try{
				Thread.sleep(sleepSpan);		//����һ��ʱ��
			}
			catch(Exception e){
				e.printStackTrace();			//��ӡ�������쳣
			}
		}
	}
}