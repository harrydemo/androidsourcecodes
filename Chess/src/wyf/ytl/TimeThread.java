package wyf.ytl;
/**
 * 
 * ����Ϊ����˼��ʱ����߳���
 * ˭����˼��������㽫˭����˼��ʱ���һ
 *
 */
public class TimeThread extends Thread{
	private boolean flag = true;//ѭ����־
	GameView gameView;
	public TimeThread(GameView gameView){//������
		this.gameView = gameView;//�õ�GameView����
	}
	public void setFlag(boolean flag){//����ѭ�����λ
		this.flag = flag;
	}
	@Override
	public void run(){//��д��run����
		while(flag){//ѭ��
			if(gameView.caiPan == false){//��ǰΪ�ڷ����塢˼��
				gameView.heiTime++;//�ڷ�ʱ���Լ�
			}
			else if(gameView.caiPan == true){//��ǰΪ�췽���塢˼��
				gameView.hongTime++;//�췽ʱ���Լ�
			}
			try{
				Thread.sleep(1000);//˯��һ����
			}
			catch(Exception e){//�����쳣
				e.printStackTrace();//��ӡ�쳣��Ϣ
			}
		}
	}
}