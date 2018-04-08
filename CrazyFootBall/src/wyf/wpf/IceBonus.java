package wyf.wpf;				//���������
import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
/*
 * �����Ǽ̳���Bonus��ģ�ʵ�ֵ�Ч����������������˸�Bonus���Ὣ�Է���Ա��������һ��ʱ�����޷��ƶ���
 * ������д�˸����drawEfect��doJob��undoJob�ȷ�����
 */
public class IceBonus extends Bonus{
	//�����������ݴ���Ĳ�����ʼ����Ҫ��Ա����
	public IceBonus(GameView father,int x,int y){
		this.father = father;
		Resources r = father.getContext().getResources();	//�����Դ����
		this.bmpSelf = new Bitmap[2];		//��ʼ��������ʾ�Լ���ͼƬ����
		this.bmpSelf[0] = BitmapFactory.decodeResource(r, R.drawable.bonus_i1);
		this.bmpSelf[1] = BitmapFactory.decodeResource(r, R.drawable.bonus_i2);
		this.selfFrameNumber = 2;			//�趨֡����
		this.bonusSize = 30;				//��ʼ��Bonus�Ĵ�С
		this.x = x;							//��ʼ��X����
		this.y = y;							//��ʼ��Y����
		this.bmpEffect = new Bitmap[1];		//��ʼ��Ч��ͼƬ
		bmpEffect[0] = BitmapFactory.decodeResource(r, R.drawable.ice);
	}
	
	@Override
	public void drawEffect(Canvas canvas){//��дdrawEffect����������target��ͬ����ס��Ӧ����Ա
		ArrayList<Player> tempTarget = null;
		if(target == 8){		//���Ŀ����AI
			tempTarget = father.alAIPlayer;	//��ʱĿ���б�ָ��AI����Ա�б�
		}
		else if(target == 0){	//���Ŀ�������
			tempTarget = father.alMyPlayer;//��ʱĿ���б�ָ����ҵ���Ա�б�
		}
		for(Player p:tempTarget){	//������ҵ���Ա�б�
			int x = p.x;			//��ȡ��Ա��X����
			int y = p.y;			//��ȡ��Ա��X����
			canvas.drawBitmap(bmpEffect[0], x-bonusSize/2+3, y-bonusSize/2, null);//���Ʊ��飬3������ֵ
		}
	}
	@Override
	public void doJob() {
		if(father.ball.lastKicker == 0){				//���һ��������ߵã���סAI
			father.father.pmt.aiMoving = false;			//����AI�����ƶ�
		}
		else{											//���һ����AI�ߵã���ס���
			father.father.pmt.myMoving = false;			//������Ҳ����ƶ�
		}
	}
	@Override
	public void undoJob() {
		if(target == 8){				//Ŀ����AI,��ס����AI����Ա
			father.father.pmt.aiMoving = true;		//�ⶳAI
		}
		else if(target == 0){			//Ŀ�������,��ס������ҵ���Ա
			father.father.pmt.myMoving = true;		//�ⶳ���
		}
	}
	@Override
	public void setTarget(int lastKicker) {//�趨���õ�Ŀ��
		this.target = (lastKicker == 0?8:0);//���������ߵģ�Ŀ�����AI��������Ŀ��������
	}
}