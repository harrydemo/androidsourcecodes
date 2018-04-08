package wyf.wpf;		//���������
/*
 * ����̳���Thread����Ҫʵ�ֶ�Bonus����Ĺ���ͨ�����ڼ����Ļ��Bonus�ĸ�����
 * ���ݸ����������Bonus��
 */
public class BonusManager extends Thread{
	boolean flag = false;		//�����߳�ִ�б�־λ
	GameView father;			//FieldView��������
	int sleepSpan = 3000;		//����ʱ��
	int maxBonus = 2;		//��������Bonus����
	//����������ʼ����Ҫ��Ա����
	public BonusManager(GameView father){
		this.father = father;
		this.flag = true;
	}
	//�������̵߳�run����
	public void run(){
		while(flag){
			//ֻ����Ϸ����״̬�²Ų���Bonus
			if((!father.isGameOver) && (!father.isScoredAGoal) &&(!father.isShowDialog)){
				generateBonus();		//����generateBonus��������Bonus
			}
			try{
				Thread.sleep(sleepSpan);	//����һ��ʱ��
			}
			catch(Exception e){
				e.printStackTrace();		//��ӡ�����쳣
			}
		}
	}
	//�������Bonus
	public void generateBonus(){		
		int currentBonusNumber = father.balLive.size();		//��ȡ���ŵ�Bonus�ĸ���
		float generateOdd = 1 - currentBonusNumber*0.33f;			//�������ɸ���
		if(Math.random() < generateOdd){		//����һ������������С�����ɸ���
			int x = (int)(Math.random() * (father.fieldRight-father.fieldLeft))+ father.fieldLeft;
			int y = (int)(Math.random() * (father.fieldDown - father.fieldUp)) + father.fieldUp;
			Bonus b;
			if(Math.random() > 0.5){			//��������������ֵС��0.5���򴴽�IceBonus
				b = new IceBonus(father,x,y);
			}
			else{								//��������С��0.5���򴴽�LargerGoalBonus
				b = new LargerGoalBonus(father,x,y);
			}	
			b.status = Bonus.PREPARE;	//����״̬Ϊ׼��̬	
			father.balLive.add(b);				//��Bonus��ӵ�������ײ���ļ�����
			b.owner = father.balLive;		//������������			
			father.balAdd.add(b);			//��Bonus��ӵ�����Ӽ�����			
			b.setPrepareTimeout(Bonus.PREPARE_SPAN);//Ϊ�����ɵ�Bonus����׼����ʱ
		}
	}	
}