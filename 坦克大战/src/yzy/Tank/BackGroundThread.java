package yzy.Tank;


public class BackGroundThread extends Thread {
	GameView gameView;// GameView的引用
	private float span = 2f;// 图片移动长度
	private boolean flag = true;// 循环标志位
	double touchTime = 0;// 当前所到的时间

	public BackGroundThread(GameView gameView) {// 构造器
		this.gameView = gameView;
	}

	public void setFlag(boolean flag) {// 设置标记位
		this.flag = flag;
	}

	public void run() {
		while (flag) {
			if (gameView.status == 1) {// 游戏中时
				gameView.backGroundIX -= span;
				if (gameView.backGroundIX < -ConstantUtil.pictureWidth) {
					gameView.i = (gameView.i + 1) % ConstantUtil.pictureCount;
					gameView.backGroundIX += ConstantUtil.pictureWidth;
				}
				touchTime=touchTime+0.5;

				try {
					for (EnemyTank ep : gameView.enemyTanks) {// 到时间出现敌坦
						if (ep.touchPoint == touchTime) {
							ep.status = true;
						}
					}
					for(Life l : gameView.lifes){//到时间出现血块
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
