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
    
	 // ����ImageView����
    ImageView imageView;
    // ����TextView
    ImageView textView;
    // ImageView��alphaֵ
    int image_alpha = 240;
    // Handler����������UI_Thread��MessageQueue������Ϣ
    Handler mHandler;
    // �߳��Ƿ������жϱ���
    boolean isrung = false;
    private Thread mSplashThread;    

    @Override

    public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.first);
            final FirstSplash sPlashScreen = this;  
            isrung = true;
            // ���ImageView�Ķ���

            imageView = (ImageView) this.findViewById(R.id.thefirst);
            textView = (ImageView) this.findViewById(R.id.information);

            // ����imageView��Alphaֵ
            imageView.setAlpha(image_alpha); 
   
            textView.setAlpha(image_alpha);

         // ��������������߳�
            mSplashThread =  new Thread(){
                @Override
                public void run(){
                    try {
                        synchronized(this){
                            // ������ͣ��ʱ��
                            wait(6000);
                        }
                    }
                    catch(InterruptedException ex){                    
                    }

                    finish();
                    
                    //������һ��Activity
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
            // ����һ���߳�����Alphaֵ�ݼ�
            new Thread(new Runnable() {
                    public void run() {
                            while (isrung) {
                                    try {
                                            Thread.sleep(180);
                                            // ����Alphaֵ
                                            updateAlpha();

                                    } catch (InterruptedException e) {

                                            e.printStackTrace();
                                    }
                            }
                    }
               }).start();

           // ������Ϣ֮�����imageview��ͼ

            mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                            super.handleMessage(msg);

                            imageView.setAlpha(image_alpha);
                            textView.setAlpha(image_alpha);

                            // ˢ����ͼ

                            imageView.invalidate();
                            textView.invalidate();
                    }
                };
           }
    // ����Alpha
    public void updateAlpha() {

            if (image_alpha - 8 >= 0) {
            	
                   image_alpha -= 8;

            } else {

                    image_alpha = 0;
                    isrung = false;
            }
            // ������Ҫ����imageview��ͼ����Ϣ-->�����Ƿ������߳�
            mHandler.sendMessage(mHandler.obtainMessage());
    }

    /**
     *������Ļ�����¼�
     *�����û�����һ��APP��ͼ��ʱ���㻽�������߳�
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
