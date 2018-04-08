package wyf.wpf;			//���������

import static wyf.wpf.ConstantUtil.*;		//���������
import android.graphics.Canvas;				//���������

/*
 * ÿһ��MyDrawable��������һ�����ƶ���ÿ��MyDrawable�����а���һ��ͼƬ
 * ��ͼƬ�ǵȿ�ͼƬ������Ա����drawSelf���մ����λ��
 * ����ͼ�е��С������������Դ˼�������Ͻǵ����꣬������ơ�
 */
public class MyDrawable{
	public int height;		//ͼƬ�߶� 
	int imgId;			//ͼƬID
	//������
	public MyDrawable(int imgId,int height){
		this.height = height;
		this.imgId = imgId;
	}
	//���ղ�����������
	public void drawSelf(Canvas canvas,int row,int col,int offsetX,int offsetY){
		int topLeftCornerX = col*TILE_SIZE;						//���Ͻ�x����������½�x����
		int topLeftCornerY = (row+1)*TILE_SIZE - height;		//���Ͻ�y���������½�y�����ȥͼƬ�߶�
		BitmapManager.drawCurrentStage(imgId, canvas, topLeftCornerX-offsetX, topLeftCornerY-offsetY);
	}
}