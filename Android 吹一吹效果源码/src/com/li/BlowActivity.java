package com.li;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BlowActivity extends Activity {
	static TextView text;
	Button btn;
	Button btn1;
	static int i=0;
	RecordThread tt=null;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text=(TextView)findViewById(R.id.text);
        btn=(Button)findViewById(R.id.btn);
		btn.setOnClickListener(listener);

    }
    
    class MyHandler extends Handler{
    	
    }
    
    OnClickListener listener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				tt=new RecordThread(r);           //�����ť�������߳�
				tt.start();

			  

		}
    	
    };
    
    MyHandler r=new MyHandler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);          //���յ�message�����UI����ͨ��isblowֹͣ�߳�
			text.setText("�㴵��һ����Ļ"+i);
			Parameter.isblow=false;
	
		}
    };
}