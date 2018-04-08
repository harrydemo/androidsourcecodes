package yzy.Tank;

public class KeyThread extends Thread {
	GameView gameView;//Activity������
	private boolean flag = true;//ѭ����־
	int action;//����״̬��
	private boolean KEY_UP = false;//��
	private boolean KEY_DOWN = false;//��
	private boolean KEY_LEFT = false;//��
	private boolean KEY_RIGHT = false;//��
	private boolean KEY_A = false;//����
	
	int countMove = 0;//�ɻ��ƶ��ļ�����
	int countFine = 0;//�ɻ����ӵ��ļ�����
	int moveN = 2;//ÿ2��ѭ���ƶ�һ��
	int fineN = 5;//ÿ3��ѭ����һ���ӵ�
	public KeyThread(GameView gameView){//������
		this.gameView = gameView;
	}
	public void setFlag(boolean flag){//���ñ�־λ
		this.flag = flag;
	}
	@Override
	public void run() {
		while(flag){
			action = gameView.action;//�õ���ǰ���̵�״̬��
			if(action==1){
				KEY_UP = true;
			}else{
				KEY_UP = false;
			}
			if(action==2){
				KEY_DOWN = true;
			}else{
				KEY_DOWN = false;
			}
			if(action==3){
				KEY_LEFT = true;
			}else{
				KEY_LEFT = false;
			}
			if(action==4){
				KEY_RIGHT = true;
			}else{
				KEY_RIGHT = false;
			}
			if(action==6){
				KEY_A = true;
			}else{
				KEY_A = false;
			}
			/*if(action==5){
				KEY_T = true;
			}else{
				KEY_T = false;
			}*/
			if(countMove == 0){//ÿmoveN��ѭ���ƶ�һ��
				if (KEY_UP) {// ��
					if(gameView.tank.getgunX()>-36){
						gameView.tank.setgunX(gameView.tank.getgunX()-gameView.tank.getgunXSpan());
					}
				}
				if (KEY_DOWN) {// ��
					if(gameView.tank.getgunX()<0){
						gameView.tank.setgunX(gameView.tank.getgunX()+gameView.tank.getgunXSpan());
					}
				}
				if (KEY_LEFT) {// ��
					if(gameView.tank.getX()>-5){
						gameView.tank.setX(gameView.tank.getX()-gameView.tank.getSpan());
					}
				}
				if (KEY_RIGHT) {// ��
					if(gameView.tank.getX()<850){
						gameView.tank.setX(gameView.tank.getX()+gameView.tank.getSpan());
					}
				}
				if(countFine == 0){//ÿѭ��countFine�η���һ���ӵ�
					if (KEY_A) {// ����
						gameView.tank.fire();
					}
				}
			}
			countMove = (countMove+1)%moveN;
			countFine = (countFine+1)%fineN;
			try{
				Thread.sleep(18);//˯��ָ��������
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}