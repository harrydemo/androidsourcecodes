package wyf.ytl;


public class WelcomeViewThread extends Thread{
	private boolean flag = true;//循环标志位
	WelcomeView welcomeView;//WelcomeView的引用
	public WelcomeViewThread(WelcomeView welcomeView){//构造器
		this.welcomeView = welcomeView;//得到WelcomeView的引用
	}
	public void setFlag(boolean flag){//设置循环标志位
		this.flag = flag;
	}
	public void run(){//重写的run方法
    	try{
    		Thread.sleep(300);//睡眠三百毫秒，保证界面已经显示
    	}
    	catch(Exception e){//捕获异常
    		e.printStackTrace();//打印异常信息
    	}
		while(flag){
			welcomeView.logoX += 10;//移动欢迎界面的logo
			if(welcomeView.logoX>0){//到位后停止移动
				welcomeView.logoX = 0;
			}
			welcomeView.boyX += 20;//移动小男孩图片
			if(welcomeView.boyX>70){//到位置后停止移动
				welcomeView.boyX = 70;
			}
			welcomeView.oldboyX += 15;//移动小老头
			if(welcomeView.oldboyX>0){//到位后停止移动
				welcomeView.oldboyX = 0;
			}
			welcomeView.bordbackgroundY += 50;//移动文字背景
			if(welcomeView.bordbackgroundY>240){
				welcomeView.bordbackgroundY = 240;
			}
			welcomeView.logo2X -= 30;//更改图片的坐标
			if(welcomeView.logo2X<150){
				welcomeView.logo2X = 150; //停止移动
			}
			if(welcomeView.logo2X == 150){//当logo2到位后按钮才移动出现
				welcomeView.menuY -= 30;
				if(welcomeView.menuY<355){
					welcomeView.menuY = 355;
				}
			} 
        	try{ 
        		Thread.sleep(100);//睡眠指定毫秒数 
        	}catch(Exception e){//捕获异常
        		e.printStackTrace();//打印异常信息
        	}
		}
	}
}