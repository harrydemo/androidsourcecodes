package com.ebook;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeActivity extends Activity implements AnimationListener {   
    private ImageView  imageView = null;   
    private Animation alphaAnimation = null;   
       
    @Override  
    protected void onCreate(Bundle savedInstanceState) {   
           
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.welcome);   
        imageView = (ImageView)findViewById(R.id.welcome_image_view);   
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);   
        alphaAnimation.setFillEnabled(true); //����Fill����   
        alphaAnimation.setFillAfter(true);  //���ö��������һ֡�Ǳ�����View����   
        imageView.setAnimation(alphaAnimation);   
        alphaAnimation.setAnimationListener(this);  //Ϊ�������ü���   
    }   
       
    @Override  
    public void onAnimationStart(Animation animation) {   
           
    }   
       
    @Override  
    public void onAnimationEnd(Animation animation) {   
        //��������ʱ������ӭ���沢ת�������������   
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);   
        startActivity(intent);
        this.finish();   
    }   
       
    @Override  
    public void onAnimationRepeat(Animation animation) {   
           
    }   
       
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {   
        //�ڻ�ӭ��������BACK��   
        if(keyCode==KeyEvent.KEYCODE_BACK) {   
            return false;   
        }   
        return false;   
    }   
}

        
        