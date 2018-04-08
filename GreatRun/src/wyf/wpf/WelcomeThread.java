package wyf.wpf;				//声明包语句
public class WelcomeThread extends Thread{
	WelcomeView father;
	boolean flag;
	int sleepSpan = 100;//休眠时间
	
	public WelcomeThread(WelcomeView father){
		this.father = father;
		flag = true;
	}
	//线程的执行方法
	public void run(){
		while(flag){
			switch(father.status){
			case 0://正在渐显背景图片
				father.alpha += 5;
				if(father.alpha == 255){
					father.status = 4;
				}
				break;
			case 5://有按钮点下
				int index = father.selectedIndex;
				father.father.myHandler.sendEmptyMessage(index);
				father.status = 4;//回到待命状态
				break;
			default:
				break;
			}
			try{
				Thread.sleep(sleepSpan);
			}										//线程休眠
			catch(Exception e){
				e.printStackTrace();
			}										//捕获并打印异常
		}
	}
}