package wyf.wpf;						//���������
//��SpriteThread����ʱ�޸ĵ�ǰ�����εĵ�ǰ����֡
public class SpriteThread extends Thread{
	Sprite father;			//Sprite��������
	boolean flag = false;	//ѭ����־λ
	int sleepSpan = 200;		//�߳�����ʱ��
    boolean isGameOn;
	//������
	public SpriteThread(Sprite father){
		super.setName("==SpriteThread");
		this.father = father;
		flag = true;
		isGameOn = true;
	}
	//�߳�ִ�з���
	public void run(){
		while(flag){
			while(isGameOn){
				father.nextFrame();		//���û�֡����
				try{				//�߳�����
					Thread.sleep(sleepSpan);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}	
			try{
				Thread.sleep(1500);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}