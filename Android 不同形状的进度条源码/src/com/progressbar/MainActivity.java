package com.progressbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
//ʹ��Runnable�ӿ�
public class MainActivity extends Activity implements Runnable {
	private Thread th ;			//����һ���߳�
	private ProgressBar pb ; 	//����һ������������
	private boolean stateChage; //��ʶ����ֵ�����С��״̬
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		//ʵ������������
		pb = (ProgressBar)findViewById(R.id.porb); 
		th = new Thread(this);//ʵ���̶߳���
		th.start();//�����߳�
	}

	@Override
	public void run() {//ʵ��Runnable�ӿڳ�����
		while(true){ 
			int current = pb.getProgress();//�õ���ǰ����ֵ
			int currentMax = pb.getMax();//�õ���������������ֵ
			int secCurrent = pb.getSecondaryProgress();//�õ��ײ㵱ǰ����ֵ
			//���´���ʵ�ֽ���ֵԽ��Խ��Խ��ԽС��һ����̬Ч��
			if(stateChage==false){ 
				if(current>=currentMax){ 
					stateChage=true; 
				}else{
					//���ý���ֵ
					pb.setProgress(current+1);
					//���õײ����ֵ
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