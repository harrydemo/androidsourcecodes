package wyf.wpf;		//声明包语句
/*
 * 该类继承自Thread，主要实现欢迎界面的后台数据
 * 的修改以实现动画效果
 */
public class WelcomeThread extends Thread{
	WelcomeView father;				//WelcomeView对象的引用
	boolean isWelcoming = false;	//线程执行标志位
	float rotateAngle = 60;			//旋转角度
	int rotateCounter = 0;			//旋转计数器
	int animationCounter=0;			//换帧计数器
    int sleepSpan = 150;			//休眠时间
	//构造器：初始化主要成员变量	
	public WelcomeThread(WelcomeView father){
		this.father = father;
		isWelcoming = true;
	}	
	public void run(){//线程的执行方法
		while(isWelcoming){
			switch(father.status){//获取现在的状态
			case 0:			//该状态为3个图片轮流显示		
				animationCounter++;				//换帧计数器自加
				if(animationCounter == 4){		//计数器达到4时换帧
					father.index ++;
					if(father.index == 3){		//判断是否播放完毕所有帧
						father.status = 1;		//转入下一状态
					}	
					animationCounter = 0;		//清空计数器
				}
				break;
			case 1://该状态为背景图片旋转着进来
				father.matrix.postRotate(rotateAngle);		//旋转角度
				rotateCounter++;							//计数器自加
				if(rotateCounter == 6){//旋转计数器到了
					father.status = 2;//设置状态
					father.alpha = 0;	//设置alpha值，用于菜单渐显
				}
				break;
			case 2://该状态为菜单渐显菜单渐显
				father.alpha +=51;			//alpha值增加
				if(father.alpha >= 255){
					father.status = 3;//进入待命状态，此状态玩家可以选择菜单选项
				}
				break;
			case 3:	//如果遇到了待命状态，就自己把自己关闭
				this.isWelcoming = false;
				break;
			}
			try{
				Thread.sleep(sleepSpan );		//休眠一段时间
			}
			catch(Exception e){
				e.printStackTrace();			//捕获并打印异常
			}
		}
	}
}