package com.worldchip.apk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


 public class SplashScreen extends Activity {
    
    /**
     *闪动屏幕触碰事件类
     */
    private Thread mSplashThread;    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 闪屏布局
        setContentView(R.layout.splash);
        
        final SplashScreen sPlashScreen = this;   
        
        // 启动闪屏界面的线程
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        // 闪屏的停留时间
                        wait(2500);
                    }
                }
                catch(InterruptedException ex){                    
                }

                finish();
                
                //启动下一个Activity
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
