package com.song;

import com.song.ui.BucketListActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 第一个界面
 * @author admin
 *
 */
public class ImageShowActivity extends Activity {
    
	private Button button1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if (!android.os.Environment.getExternalStorageState().equals(
        		android.os.Environment.MEDIA_MOUNTED)){
        	Dialog noSDDialog = new Dialog(this);
        	noSDDialog.setTitle("SD Card Error!");
        	noSDDialog.setContentView(R.layout.no_sd_alert_layout);
        	noSDDialog.show();
        	return;
        }
        ImageManager.bucketList = ImageManager.loadAllBucketList(this);		
        initUiData();
        initListener();
        
    }
    
    public void initUiData(){    	
    	button1 = (Button)findViewById(R.id.button1);
    	
    }
    
    public void initListener(){
    	
    	button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				Intent intent = new Intent();
				intent.setClass(ImageShowActivity.this, BucketListActivity.class);
				startActivity(intent);
			}
		});
    	
    	
    	
    }
}