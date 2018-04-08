package com.worldchip.apk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


 public class SplashScreen extends Activity {
    
    /**
     *������Ļ�����¼���
     */
    private Thread mSplashThread;    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ��������
        setContentView(R.layout.splash);
        
        final SplashScreen sPlashScreen = this;   
        
        // ��������������߳�
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        // ������ͣ��ʱ��
                        wait(2500);
                    }
                }
                catch(InterruptedException ex){                    
                }

                finish();
                
                //������һ��Activity
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, StoreLogin.class);
                startActivity(intent);
                
                try{
                	stop();   
                }catch(Exception e){
                	System.out.println(e);
                }
            }
        };
        
        mSplashThread.start();
        
    }

    
} 
