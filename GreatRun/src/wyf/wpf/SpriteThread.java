package wyf.wpf;						//声明包语句
//类SpriteThread负责定时修改当前动画段的当前动画帧
public class SpriteThread extends Thread{
	Sprite father;			//Sprite对象引用
	boolean flag = false;	//循环标志位
	int sleepSpan = 200;		//线程休眠时间
    boolean isGameOn;
	//构造器
	public SpriteThread(Sprite father){
		super.setName("==SpriteThread");
		this.father = father;
		flag = true;
		isGameOn = true;
	}
	//线程执行方法
	public void run(){
		while(flag){
			while(isGameOn){
				father.nextFrame();		//调用换帧方法
				try{				//线程休眠
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