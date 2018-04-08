package wyf.wpf;				//���������
import java.util.List;			//���������
import java.util.Timer;//���������
import java.util.TimerTask;//���������
import android.graphics.Bitmap;//���������
import android.graphics.Canvas;//���������
/*
 * ����Ϊһ��Bonus�ĸ��࣬��Ҫ�ṩһЩ�����ĳ�Ա���Ƿ�����һ��Bonus����Ҫ�İ���
 * �Լ��Ļ��Ʋ��֡�������Ļ��ơ��Լ��������ڼ�ʱ���������������ڼ�ʱ���������̨
 * ���ݵ��޸ġ������������ڽ������̨���ݵĻָ���
 */
public abstract class Bonus{
	public static final int PREPARE = 0;	//׼��̬�����Ի����������ǲ���������
	public static final int LIVE = 1;		//�̬�����Ա����������Ա�����
	public static final int DEAD = 2;	//����̬�������Ա������������Ա�����
	public static final int EFFECTIVE = 3;//��Ч̬�������Ա������������Ա����������ǿ��Ի�����������ã��������
	public static final int LIFE_SPAN = 5000;
	public static final int EFFECT_SPAN = 5000;
	public static final int PREPARE_SPAN = 2000;
	int status = -1;			//0�����ڣ�1��������2����Ч
	int x,y;					//Bonus���ĵ������
	int bonusSize;				//Bonus�Ĵ�С
	int selfIndex=0;			//�Լ�֡����
	int effectIndex = 0;		//��Ч�������
	int selfFrameNumber;		//�Լ�����֡����
	int effectFrameNumber;		//��Ч����֡����
	int target;					//��˭������0Ϊ�Լ���8ΪAI�������ǵĽ�����������
	
	GameView father;			//FieldView��������
		
	Bitmap [] bmpSelf;			//���ڻ����Լ���Bitmap����
	Bitmap [] bmpEffect;		//���ڻ���Ч����Bitmap����
	
	Timer timer = new Timer();		//������ʱ������
	List<Bonus> owner;				//��¼�Լ�����ӵ��ĸ�������
	//����һ��ʱ���ſ��Դ�PREPARE��LIVE̬
	public void setPrepareTimeout(int timeout){
		timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				Bonus.this.status = Bonus.LIVE;
				setTimeout(Bonus.LIFE_SPAN);
			}			
		}, 
		timeout);
	}
	//���ö�ʱ�ķ���
	public void setTimeout(int timeout){
		timer = new Timer();	
		timer.schedule(new TimerTask(){		//���ó�Ա����schedule������һ����ʱ��
			@Override
			public void run() {
				Bonus.this.status = Bonus.DEAD;			//ɱ��Bonus
				Bonus.this.undoJob();					//����undoJob����
				Bonus.this.owner.remove(Bonus.this);	//���������ļ������Ƴ��Լ�
				father.balDelete.add(Bonus.this);		//���Լ���ӵ���ɾ��������
			}			
		}, 
		timeout);		
	}
	//���Լ��ķ���
	public void drawSelf(Canvas canvas){
		canvas.drawBitmap(bmpSelf[(selfIndex++)%selfFrameNumber], x-bonusSize/2, y-bonusSize/2, null);
	}
	//���󷽷�������Ч������Ҫ������д
	public abstract void drawEffect(Canvas canvas);
	//���󷽷����޸ĺ�̨���ݣ���Ҫ������д
	public abstract void doJob();
	//���󷽷����ָ���̨���ݣ���Ҫ������д
	public abstract void undoJob();
	//���󷽷�������Ŀ�꣬��Ҫ������д
	public abstract void setTarget(int lastKicker);
}