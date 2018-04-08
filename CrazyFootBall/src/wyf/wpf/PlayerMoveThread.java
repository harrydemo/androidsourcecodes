package wyf.wpf;			//���������
/*
 * ����̳���Thread����Ҫ�����ǣ�����˫���˶�Ա�ķ����޸����̨λ������
 * ʵ�ַ�����ÿ��һ��ʱ�䣬��ȡFootballActivity��direction���ԣ��ж�
 * ��־λ��ִ���޸ģ�ͨ������FielView���movePlayers�������޸�
 */
public class PlayerMoveThread extends Thread{
	FootballActivity father;	//Activity������
	boolean outerFlag;			//�߳�ִ�б�־λ
	boolean flag;				//�Ƿ���Ҫ�ƶ���Ա��λ�ñ�־λ
	int sleepSpan = 20;			//�߳�����ʱ��
	boolean myMoving;		//Ϊtrue��ʾ��ҿ��ƶ���Ϊfalse��ʾ��Ҳ��ɶ�
	boolean aiMoving;		//Ϊtrue��ʾAI���ƶ���Ϊfalse��ʾAI���ɶ�
	//����������ʼ����Ҫ��Ա����
	public PlayerMoveThread(FootballActivity father){
		super.setName("##-PlayerMoveThread");	//Ϊ�߳��������ƣ�����ʹ��
		this.father = father;
		outerFlag = true;
		flag = true;
		myMoving = true;			//��ʼ״̬��ҵ���Ա�ǿ��ƶ���
		aiMoving = true;			//��ʼ״̬����AI����Ա�ǿ��ƶ���
	}
	//�������̵߳�ִ�з���
	public void run(){
		while(outerFlag){
			while(flag){
				//�޸���Һ�AI�˶�Ա��λ��				
				if(father.current == father.gv){	//���FieldView�ǵ�ǰ��Ļ
					if(myMoving){					//�����ҵ���Ա�ǿ��ƶ���
						int key = father.keyState;	//��ȡ���̼���״̬
						if((key & 1) == 1){			//����״̬Ϊ����
							father.gv.movePlayers(father.gv.alMyPlayer, 4);//���÷��������ƶ���Ա
						}
						else if((key & 2) == 2){	//����״̬Ϊ����
							father.gv.movePlayers(father.gv.alMyPlayer, 12);	//���÷��������ƶ���Ա
						}
						else{						//��direction����Ϊ��ֹ-1
							father.gv.movePlayers(father.gv.alMyPlayer, -1);
						}
					}			
					if(aiMoving){					//�ж�AI�Ƿ�����ƶ�
						int d = father.gv.aiDirection;						//��ȡAI��Ա���˶�����
						father.gv.movePlayers(father.gv.alAIPlayer, d);	//�޸�AI�˶�Ա��λ��
					}	
				}
				try{
					Thread.sleep(sleepSpan);		//�߳�����һ��ʱ��
				}
				catch(Exception e){
					e.printStackTrace();			//��ӡ�������쳣
				}			
			}
			try{
				Thread.sleep(300);			//������Ҫ�ƶ����ʱ���߳̿�ת��˯��һ��ʱ��
			}
			catch(Exception e){
				e.printStackTrace();		//���񲢴�ӡ�쳣
			}	
		}
	}
}