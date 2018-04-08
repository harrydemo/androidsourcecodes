package com.progressbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
//使用Runnable接口
public class MainActivity extends Activity implements Runnable {
	private Thread th ;			//声明一条线程
	private ProgressBar pb ; 	//声明一个进度条对象
	private boolean stateChage; //标识进度值最大最小的状态
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		//实例进度条对象
		pb = (ProgressBar)findViewById(R.id.porb); 
		th = new Thread(this);//实例线程对象
		th.start();//启动线程
	}

	@Override
	public void run() {//实现Runnable接口抽象函数
		while(true){ 
			int current = pb.getProgress();//得到当前进度值
			int currentMax = pb.getMax();//得到进度条的最大进度值
			int secCurrent = pb.getSecondaryProgress();//得到底层当前进度值
			//以下代码实现进度值越来越大，越来越小的一个动态效果
			if(stateChage==false){ 
				if(current>=currentMax){ 
					stateChage=true; 
				}else{
					//设置进度值
					pb.setProgress(current+1);
					//设置底层进度值
					pb.setSecondaryProgress(current+1);
				}
			}else{
				if(current<=0){ 
					stateChage=false; 
				}else{
					pb.setProgress(current-1);
					pb.setSecondaryProgress(current-1);
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}