package yzy.Tank;

public class KeyThread extends Thread {
	GameView gameView;//Activity的引用
	private boolean flag = true;//循环标志
	int action;//键盘状态码
	private boolean KEY_UP = false;//上
	private boolean KEY_DOWN = false;//下
	private boolean KEY_LEFT = false;//左
	private boolean KEY_RIGHT = false;//右
	private boolean KEY_A = false;//发射
	
	int countMove = 0;//飞机移动的计数器
	int countFine = 0;//飞机发子弹的计数器
	int moveN = 2;//每2次循环移动一下
	int fineN = 5;//每3次循环发一次子弹
	public KeyThread(GameView gameView){//构造器
		this.gameView = gameView;
	}
	public void setFlag(boolean flag){//设置标志位
		this.flag = flag;
	}
	@Override
	public void run() {
		while(flag){
			action = gameView.action;//得到当前键盘的状态码
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
			if(countMove == 0){//每moveN次循环移动一次
				if (KEY_UP) {// 上
					if(gameView.tank.getgunX()>-36){
						gameView.tank.setgunX(gameView.tank.getgunX()-gameView.tank.getgunXSpan());
					}
				}
				if (KEY_DOWN) {// 下
					if(gameView.tank.getgunX()<0){
						gameView.tank.setgunX(gameView.tank.getgunX()+gameView.tank.getgunXSpan());
					}
				}
				if (KEY_LEFT) {// 左
					if(gameView.tank.getX()>-5){
						gameView.tank.setX(gameView.tank.getX()-gameView.tank.getSpan());
					}
				}
				if (KEY_RIGHT) {// 右
					if(gameView.tank.getX()<850){
						gameView.tank.setX(gameView.tank.getX()+gameView.tank.getSpan());
					}
				}
				if(countFine == 0){//每循环countFine次发射一次子弹
					if (KEY_A) {// 发射
						gameView.tank.fire();
					}
				}
			}
			countMove = (countMove+1)%moveN;
			countFine = (countFine+1)%fineN;
			try{
				Thread.sleep(18);//睡觉指定毫秒数
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}