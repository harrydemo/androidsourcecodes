package com.example.android.bitmapfun.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class ImageGridActivity extends FragmentActivity {

	 private static final String TAG = "ImageGridFragment";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fragmentmanager的方法，通过tag找到fragment，tag是在fragmentmanager添加addfragment时添加的
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //添加fragment，并添加tag
            //add（int arg0，Fragment f，String arg1）
            //arg0为容器布局文件，可以创建一个xml布局用来显示添加的fragment，
            //f为要添加的fragment
            //arg1，可以不添加，添加则为添加的fragment设置TAG，后续可以通过TAG找到该fragment
            ft.add(android.R.id.content, new ImageGridFragment(), TAG);
            ft.commit();
        }
    }
}
