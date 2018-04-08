package yzy.Tank;


public class BackGroundThread extends Thread {
	GameView gameView;// GameView������
	private float span = 2f;// ͼƬ�ƶ�����
	private boolean flag = true;// ѭ����־λ
	double touchTime = 0;// ��ǰ������ʱ��

	public BackGroundThread(GameView gameView) {// ������
		this.gameView = gameView;
	}

	public void setFlag(boolean flag) {// ���ñ��λ
		this.flag = flag;
	}

	public void run() {
		while (flag) {
			if (gameView.status == 1) {// ��Ϸ��ʱ
				gameView.backGroundIX -= span;
				if (gameView.backGroundIX < -ConstantUtil.pictureWidth) {
					gameView.i = (gameView.i + 1) % ConstantUtil.pictureCount;
					gameView.backGroundIX += ConstantUtil.pictureWidth;
				}
				touchTime=touchTime+0.5;

				try {
					for (EnemyTank ep : gameView.enemyTanks) {// ��ʱ����ֵ�̹
						if (ep.touchPoint == touchTime) {
							ep.status = true;
						}
					}
					for(Life l : gameView.lifes){//��ʱ�����Ѫ��
						if(l.touchPoint == touchTime){
							l.status = true;
						}
					}
				} catch (Exception e) {}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
