package com.giftest.fneg;

import android.app.Activity;
import android.os.Bundle;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;

public class GifTestActivity extends Activity {
	private GifView gifView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        gifView=(GifView)findViewById(R.id.gifView);
        gifView.setGifImageType(GifImageType.WAIT_FINISH);  
        gifView.setGifImage(R.drawable.loading4);
       // GifAction action=new Gif
        //gifView.setShowDimension(300, 300);  
        // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示  
       
    }
}