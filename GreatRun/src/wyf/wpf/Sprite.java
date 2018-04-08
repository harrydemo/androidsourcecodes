package wyf.wpf;			//���������
import static wyf.wpf.ConstantUtil.*;		//���������
import java.util.ArrayList;					//���������
import android.graphics.Canvas;				//���������
/*
 * ������������Ϸ����ƽ�̲��ͼ���ƶ��ľ��顣��ҿ��Ƶ�Ӣ�ۺ�׷����ҵĹ�����̳��Դ���
 * ��������Ҫ��������Ķ������б����б��а����������еĶ���Ƭ�ε�ͼƬ������󣬲��ҿ���
 * ͨ����Ա��������ӻ�ɾ����ͬʱ���г�Ա������¼��ǰ�������Լ���ǰ�����εĶ���֡����Ա
 * ����nextFrame�����޸ĵ�ǰ�����εĵ�ǰ����֡ʵ�ֶ���Ч�����ڲ��߳����ڶ�ʱ����nextFrame������
 */
public class Sprite{
	ArrayList<int[]> animationSegmentList = new ArrayList<int[]>();	//�������б��������ϣ����µȶ�����
	//��ǰ�Ķ��������б��е���������ֹ��0��,1��2�ң�3�ϣ��ƶ���4�£�5��6�ң�7��
	int currentSegment;							//��ǰ�����ε�����						
	int currentFrame;								//��ǰ�����εĶ���֡����
	int x;		//�����ڴ��ͼ������
	int y;		//�����ڴ��ͼ������
	int col;	//�����ڵ�ͼ�ϵ�������ͨ������31��31������������ڵĵط����
	int row;	//�����ڵ�ͼ�ϵ�������ͨ������31��31������������ڵĵط����
	SpriteThread st;
	//������
	public Sprite(int col,int row){
		this.col = col;
		this.row = row;
		this.x = col * TILE_SIZE;								//x�����Ӧ��ͼƬ���Ͻ�
		this.y = row * TILE_SIZE+TILE_SIZE-SPRITE_HEIGHT;		//y�����Ӧ��ͼƬ���Ͻ�
		st = new SpriteThread(this);
	}
	//�޸ĵ�ǰ�����εĵ�ǰ����֡
	public void nextFrame(){
			int [] currSeg = animationSegmentList.get(currentSegment);
			currentFrame = (currentFrame + 1)%currSeg.length;		
	}
	//�����������Լ��Ķ������б�
	public void makeAnimation(int [][] imgId){
		for(int [] imgIDs:imgId){
			addAnimationSegment(imgIDs);
		}
	}
	//�������򶯻����б�����Ӷ�����
	public void addAnimationSegment(int [] ani){
		this.animationSegmentList.add(ani);		//�򶯻����б�����Ӷ�����
	}
	//���������ö�����
	public void setAnimationSegment(int segment){
		this.currentSegment = segment;		//���ö�����
		this.currentFrame = 0;				//��ͷ��ʼѭ��֡
	}
	//���������Լ�����Ļ�ϻ���
	public void drawSelf(Canvas canvas,int screenX,int screenY){
		int imgId = animationSegmentList.get(currentSegment)[currentFrame];	//��ȡҪ���Ƶ��Ǹ�֡
		BitmapManager.drawGamePublic(imgId, canvas, screenX, screenY);		//����BitmapManager�ľ�̬��������ͼƬ
	}
	//��������������
	public void startAnimation(){
		st.isGameOn = true;
		if(!st.isAlive()){
			st.start();
		}
	}
	//��������ͣ����
	public void pauseAnimation(){
		st.isGameOn = false;
	}
	//���������ٶ����߳�
	public void stopAnimation(){
		st.flag = false;
		st.isGameOn = false;
	}
}