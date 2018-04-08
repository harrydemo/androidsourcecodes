package com.pro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ProgressDia extends Activity implements OnClickListener
{
    
    Button b1;
    
    ProgressDialog1 mProgressDialog;
    
    boolean flag = false;
    
    int index = 0;
    
    int max = 100;
    
    Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    if (index > 100)
                    {
                        mProgressDialog.dismiss();
                        flag = false;
                        index = 0;
                        mProgressDialog = null;
                        break;
                    }
                    index++;
                    if (null != mProgressDialog)
                    {
                    	mProgressDialog.setMessage("�Ѿ�����");
                        if ((index >= 30)&&(index <= 69))
                        {
                        	
                            mProgressDialog.setTitle("�ѹ�����֮һ");
                            mProgressDialog.setDynamicStyle(ProgressDialog1.STYLE_SPINNER,"�����ϴ�1������");
                        }
                        if (index == 70)
                        {
                        	
                            mProgressDialog.setTitle("��������֮һ");
                            mProgressDialog.setDynamicStyle(ProgressDialog1.STYLE_HORIZONTAL,"�����ϴ�2������");
                        }
                        
                        mProgressDialog.setProgress(index);
                    }
                    
                    break;
                
                default:
                    break;
            }
        };
        
    };
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        b1 = (Button)findViewById(R.id.Button01);
        b1.setOnClickListener(ProgressDia.this);
    }
    
    public void onClick(View v)
    {
        if (v.equals(b1))
        {
            flag = true;
            mProgressDialog = new ProgressDialog1(ProgressDia.this);
            
            mProgressDialog.setTitle("��ʼ");
            mProgressDialog.setProgressStyle(ProgressDialog1.STYLE_HORIZONTAL);
            mProgressDialog.setMax(100);
            mProgressDialog.setButton2("ȡ��", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    flag = false;
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    index = 0;
                }
            });
            mProgressDialog.show();
            new Thread()
            {
                public void run()
                {
                    while (flag)
                    {
                        try
                        {
                            Thread.sleep(50);
                            handler.obtainMessage(0).sendToTarget();
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
            }.start();
        }
    }
}