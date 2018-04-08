package com.devchina.bitmap;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.widget.ImageView;

public class DemoActivity extends FinalActivity {
	
	@ViewInject(id=R.id.imageView) ImageView imageView; //����findViewById
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //ʹ��FinalBitmap��� ������ͼƬ��ʾ��imageView��
        new FinalBitmap(this).init().display(imageView, "http://www.devchina.com/static/image/k/logo.png");
    }
}