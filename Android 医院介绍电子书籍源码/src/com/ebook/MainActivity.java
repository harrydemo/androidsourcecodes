package com.ebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;


import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;


import android.widget.Button;


public class MainActivity extends Activity {
	private Button Button01;//封面跳转按钮
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	getWindow().setBackgroundDrawable(null);
    	setContentView(R.layout.main);
    	


    	//Button01 监听
    	Button01 = (Button)findViewById(R.id.Button_wel);
        Button01.setOnClickListener(new Button.OnClickListener(){
        	
        	@Override       	
        	public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, Contents.class);
				startActivityForResult(intent, 11);  
				finish();        
				//添加界面切换效果
				
				int version = Integer.valueOf(android.os.Build.VERSION.SDK);
				
				if(version  >= 5) { 
					
					//动画效果
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);   
				   //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);     
				    overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);   
				    }
				}
			}
        );
        }
    //监听键盘  返回键
    protected void dialog() {
    	AlertDialog.Builder builder = new Builder(MainActivity.this);
    	builder.setMessage("确定要退出吗?");
    	builder.setTitle("提示");
    	builder.setPositiveButton("确认",new android.content.DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			MainActivity.this.finish();
    			}
    		}
    	);
    	builder.setNegativeButton("取消",new android.content.DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			}
    		}
    	);
    	builder.create().show();
    	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
    		dialog();
    		
    		return false;
    		
    	}
    	
    	return false;
    	
    }
    
    public void setButton01(Button button01) {
    	
		Button01 = button01;
		
    }
    
    public Button getButton01() {
    	
    	return Button01;
    	
    }
	
}