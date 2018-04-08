package com.worldchip.apk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class FirstSplash extends Activity {
    
	 // 声明ImageView对象
    ImageView imageView;
    // 声明TextView
    ImageView textView;
    // ImageView的alpha值
    int image_alpha = 240;
    // Handler对象用来给UI_Thread的MessageQueue发送消息
    Handler mHandler;
    // 线程是否运行判断变量
    boolean isrung = false;
    private Thread mSplashThread;    

    @Override

    public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.first);
            final FirstSplash sPlashScreen = this;  
            isrung = true;
            // 获得ImageView的对象

            imageView = (ImageView) this.findViewById(R.id.thefirst);
            textView = (ImageView) this.findViewById(R.id.information);

            // 设置imageView的Alpha值
            imageView.setAlpha(image_alpha); 
   
            textView.setAlpha(image_alpha);

         // 启动闪屏界面的线程
            mSplashThread =  new Thread(){
                @Override
                public void run(){
                    try {
                        synchronized(this){
                            // 闪屏的停留时间
                            wait(6000);
                        }
                    }
                    catch(InterruptedException ex){                    
                    }

                    finish();
                    
                    //启动下一个Activity
                    Intent intent = new Intent();
                    intent.setClass(sPlashScreen, SplashScreen.class);
                    startActivity(intent);
                    
                    try{
                    	stop();   
                    }catch(Exception e){
                    	System.out.println(e);
                    }
                }
            };
            
            mSplashThread.start();
            // 开启一个线程来让Alpha值递减
            new Thread(new Runnable() {
                    public void run() {
                            while (isrung) {
                                    try {
                                            Thread.sleep(180);
                                            // 更新Alpha值
                                            updateAlpha();

                                    } catch (InterruptedException e) {

                                            e.printStackTrace();
                                    }
                            }
                    }
               }).start();

           // 接受消息之后更新imageview视图

            mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                            super.handleMessage(msg);

                            imageView.setAlpha(image_alpha);
                            textView.setAlpha(image_alpha);

                            // 刷新视图

                            imageView.invalidate();
                            textView.invalidate();
                    }
                };
           }
    // 更新Alpha
    public void updateAlpha() {

            if (image_alpha - 8 >= 0) {
            	
                   image_alpha -= 8;

            } else {

                    image_alpha = 0;
                    isrung = false;
            }
            // 发送需要更新imageview视图的消息-->这里是发给主线程
            mHandler.sendMessage(mHandler.obtainMessage());
    }

    /**
     *闪动屏幕触碰事件
     *即当用户触碰一个APP的图标时，便唤起闪屏线程
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread){
                mSplashThread.notifyAll();
            }
        }
        return true;
    }
}
