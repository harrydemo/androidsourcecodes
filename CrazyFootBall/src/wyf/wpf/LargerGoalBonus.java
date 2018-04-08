package wyf.wpf;			//���������
import android.content.res.Resources;		//���������
import android.graphics.Bitmap;				//���������
import android.graphics.BitmapFactory;		//���������
import android.graphics.Canvas;				//���������
import android.graphics.Matrix;				//���������
/*
 * �����Ǽ̳���Bonus��ģ�ʵ�ֵ�Ч����������������˸�Bonus���Ὣ�Է�������һ��ʱ��������
 * ������д�˸����drawEfect��doJob��undoJob�ȷ�����
 */
public class LargerGoalBonus extends Bonus{
	GameView father;			//FileView��������
	int effectWidth;			//
	int effectHeight;
	//����������ʼ����Ҫ��Ա����
	public LargerGoalBonus(GameView father,int x,int y){
		this.father = father;
		Resources r = father.getContext().getResources();//��ȡ��Դ����
		bmpSelf = new Bitmap[2];		//��ʼ����ʾ�Լ���ͼƬ����
		bmpSelf[0] = BitmapFactory.decodeResource(r, R.drawable.bonus_g1);
		bmpSelf[1] = BitmapFactory.decodeResource(r, R.drawable.bonus_g2);
		this.selfFrameNumber = 2;
		bmpEffect = new Bitmap[2];		//��ʼ�����ڻ���Ч����ͼƬ���� 
		bmpEffect[0] = BitmapFactory.decodeResource(r, R.drawable.goal1);
		bmpEffect[1] = BitmapFactory.decodeResource(r, R.drawable.goal2);
		this.effectFrameNumber =2;		
		this.x = x;						//��ʼ��X����
		this.y = y;						//��ʼ��Y����
		this.effectWidth = 126;			//�������ŵ�ͼƬ��ȣ����ڻ��Ƴ������ź����ڸı����ŵĴ�С
		this.effectHeight = 8;			//�������ŵ�ͼƬ�߶ȣ����ڻ��Ƴ�������
		this.bonusSize = 30;			//����Ĵ�С
	}
	@Override
	public void doJob() {
		if(target == 0){			//���ö�������ң���ҵ����ű��
			father.myGoalLeft = (father.maxLeftPosition + father.maxRightPosition)/2 - effectWidth/2;
			father.myGoalRight = (father.maxLeftPosition + father.maxRightPosition)/2 + effectWidth/2;
		}
		else if(target == 8){		//���ö�����AI��AI�����ű��
			father.AIGoalLeft = (father.maxLeftPosition + father.maxRightPosition)/2 - effectWidth/2;
			father.AIGoalRight = (father.maxLeftPosition + father.maxRightPosition)/2 + effectWidth/2;
		}
	}
	@Override
	public void drawEffect(Canvas canvas) {//���������Ƴ�������Ч��
		int x = (father.maxLeftPosition + father.maxRightPosition)/2 - effectWidth/2;//���㳬������ͼƬ��X����
		if(target == 8){		//�������Ŀ����AI
			int y = father.fieldUp-4;		//����AI�������ŵ�Y����
			canvas.drawBitmap(bmpEffect[(effectIndex++)%effectFrameNumber], x, y, null);//���Ƴ�������
		}
		else if(target == 0){//�������Ŀ�������
			int y = father.fieldDown+2;		//������ҳ������ŵ�Y����
			Matrix m = new Matrix();
			m.postRotate(180);
			//���������ŵ�ת180�ȣ�Ĭ�ϳ������ŵ�ͼƬ����AI�Ǳߵ���������
			Bitmap bmp = Bitmap.createBitmap(bmpEffect[(effectIndex++)%effectFrameNumber], 0, 0, this.effectWidth, effectHeight,m, true);
			canvas.drawBitmap(bmp, x, y, null);//���Ƴ�������
		}		
	}
	@Override
	public void undoJob() {
		if(target == 0){			//���ö��������
			father.myGoalLeft = father.maxLeftPosition;		//�ָ���ҵ����Ŵ�С
			father.myGoalRight = father.maxRightPosition;
		}
		else if(target == 8){		//���ö�����AI
			father.AIGoalLeft = father.maxLeftPosition;		//�ָ�AI�����Ŵ�С
			father.AIGoalRight = father.maxRightPosition;
		}
	}
	@Override
	public void setTarget(int lastKicker) {		//�趨����Ŀ�����
		this.target = (lastKicker == 0?8:0);
	}
	
}