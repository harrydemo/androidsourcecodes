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
	private ProgressBar firstBar = null;// ������
	private Button myButton = null;// ��ť

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
			firstBar.setVisibility(View.VISIBLE);// ���ý������ɼ�
			handler.post(progressBar);
		}

	}
	//ʹ�������ڲ�������дhandleMessage����
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			firstBar.setProgress(msg.arg1);
			handler.post(progressBar);
		}

	};
	
	//����һ���߳�
	Runnable progressBar = new Runnable() {
		int i = 0;
		@Override
		public void run() {
			i = i + 5;
			Message msg = handler.obtainMessage();//�õ�һ����Ϣ����Message����Android����ϵͳ�ṩ
			
			msg.arg1 = i;//��msg�����arg1������ֵ��Ϊi,��arg1��arg2������Ա����������Ϣ���ŵ���ϵͳ�������ĺ���
			try {
			
				Thread.sleep(1000);	//���õ�ǰ�߳�˯��1��
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			handler.sendMessage(msg);//��msg��Ϣ������Ϣ������
			if (i == 100) {
				handler.removeCallbacks(progressBar);//�Ƴ����̶߳���
			}
		}
	};

}