package wyf.wpf;				//���������
/*
 * ������������˶�Ա��������Ҫ��װ����Ա��һЩ��Ϣ
 * ���⻹����һ����Ա����levelUp���������Ӯ��һ��
 * ������ʤ��֮���������������
 */
public class Player{
	int x;						//��Ա���ĵ�x����
	int y;						//��Ա���ĵ�y����
	int movingDirection=-1;		//�˶�Ա���˶�����12��4��
	int movingSpan = 1;			//�ƶ�����
	int attackDirection;		//��������0��8��
	int power=10;				//����ʱ������ٶȴ�С
	
	public void levelUp(){		//���������
		movingSpan+=1;			//ÿ���ƶ��Ĳ�������
		if(movingSpan > 5){
			movingSpan = 5;
		}
	}
}