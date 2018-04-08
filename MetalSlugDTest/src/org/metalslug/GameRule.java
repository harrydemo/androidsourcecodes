package org.metalslug;

//Ϊ�˼򻯾�ʹ����public����

public class GameRule {
	
	public int score;			//�÷�
	
	public int comb;            //������
	
	public int rate=1;
	
	public int totalBullet;     //�ӵ������
	
	public int hitBullet;		//��������
	
	public int deadCount;		//������
	
	public int knifeUseCount;   //������
	
	public long costTime;		//����ʱ��(ms)
	
	public boolean wiatComb;
	
	public void clearState(){
		score=0;comb=0;
		rate=1;
	}
	
	//��missionģʽ���ӵ����з���Ϊ100�����ӻ��з���Ϊ500.
	//�õ������۵�Ҫ��1����������Ϊ0 
	//2�������ʴ���60%	3����������ռ�ܹ���������40%����ʱ�ӷ�
	//4������ʱ���ٵ÷ָ�		5������������
	//������Ч�����������������ĵ��˻�δ�����Ĺ������㣩���5s���ٴγ�����Ч����
	//��������+1������������
	//3-10������2��
	//10-15: ����3��
	//15-20: ����4��
	//20-30: ����8��
	//30-50: ����16��
	//��������ʱ����������
	
	//�������ر�ĵ��˻��н���������ͬ����Ч��
	//0���� +10000
	//������ +10000
	//���� +20000
	//���Ÿ߷�Ŭ���ɣ�shaonian��
	
	public void addScore(int score){
		this.score+=score*rate;
	}
	
	public void addComb(){
		this.comb++;
		if(comb>2&&comb<=5) {rate=2;return;}
		if(comb>5&&comb<=10) {rate=4;return;}
		if(comb>10&&comb<=15){rate=8;return;}
		if(comb>15&&comb<=30) {rate=16;return;}
		if(comb>30) rate=32;
	}
}
