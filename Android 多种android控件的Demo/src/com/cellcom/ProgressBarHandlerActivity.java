package com.cellcom;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import android.widget.TextView;

/**
 * 
 * @author nwang
 * 
 * ������ProgressBar���߳�ʹ�á�������ʼ��ť��������ˮƽ����ǰ����
 *
 */
public class ProgressBarHandlerActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Button startButton;
	private Button stopButton;
	private TextView result;
	private ProgressBar firstProgressBar;
	private final static int defaultValue=10;
	private int i=0;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);
        setTitle("ProgressBarʹ�ã�");
        startButton=(Button)findViewById(R.id.startButton);
        stopButton=(Button)findViewById(R.id.stopButton);
        result=(TextView)findViewById(R.id.result);
        firstProgressBar=(ProgressBar)findViewById(R.id.firstProgressBar);
        
        firstProgressBar.setVisibility(0);//�տ�ʼ���ý������ɼ�
        
        startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*i+=defaultValue;
				firstProgressBar.setProgress(i);
				firstProgressBar.setSecondaryProgress(i+defaultValue);*/
				result.setText(((double)i/firstProgressBar.getMax()*100)+"%");
				handler.post(progressBarThread);
			}
		});
        
        stopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handler.removeCallbacks(progressBarThread);			
			}
		});
    }
    
    //����Handler����
    Handler handler=new Handler();
    
    //�����̶߳���
    MyRunnable progressBarThread=new MyRunnable();
    class MyRunnable implements Runnable{
		@Override
		public void run() {
			i+=defaultValue;
			handler.postDelayed(progressBarThread, 1000);
			firstProgressBar.setProgress(i);
			firstProgressBar.setSecondaryProgress(i+defaultValue);
			result.setText(((double)i/firstProgressBar.getMax()*100)+"%");
			if(i>=firstProgressBar.getMax()) i=0;
			
		}
    	
    }
    
}