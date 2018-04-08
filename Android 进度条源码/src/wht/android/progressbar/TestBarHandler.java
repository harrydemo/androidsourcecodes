package wht.android.progressbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class TestBarHandler extends Activity {
	private ProgressBar firstBar = null;// 进度条
	private Button myButton = null;// 按钮

	// private int i =0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		firstBar = (ProgressBar) findViewById(R.id.firstbar);
		myButton = (Button) findViewById(R.id.myButton);
		myButton.setOnClickListener(new ButtonListener());
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			firstBar.setVisibility(View.VISIBLE);// 设置进度条可见
			handler.post(progressBar);
		}

	}
	//使用匿名内部类来覆写handleMessage方法
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			firstBar.setProgress(msg.arg1);
			handler.post(progressBar);
		}

	};
	
	//定义一个线程
	Runnable progressBar = new Runnable() {
		int i = 0;
		@Override
		public void run() {
			i = i + 5;
			Message msg = handler.obtainMessage();//得到一个消息对象，Message类由Android操作系统提供
			
			msg.arg1 = i;//将msg对象的arg1参数的值设为i,用arg1和arg2两个成员变量传递信息，优点是系统性能消耗很少
			try {
			
				Thread.sleep(1000);	//设置当前线程睡眠1秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			handler.sendMessage(msg);//将msg消息加入消息队列中
			if (i == 100) {
				handler.removeCallbacks(progressBar);//移除此线程对象
			}
		}
	};

}