package com.ebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;





import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.IOException;   
import java.io.InputStream;
 

//显示assets文件加下的资源文件
public class ReadAsset extends Activity {
	
	private ScrollView scrollView;
	private String s;
	private int y;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState); 
		//getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.read); 
       
        Bundle bundle = this.getIntent().getExtras();
        int name = bundle.getInt("name");//int 
       
//        int name1 = bundle.getInt("namey");//k
//        String name2 = bundle.getString("namek");//k
        
    
        	 s = name+".txt";
        	
		
        
       
        @SuppressWarnings("unused")
		boolean ismale = bundle.getBoolean("Ismale");
        
        //读取 Assets  文件下的 .txt文件
        try {
        	InputStream is = getAssets().open(s);
        	int size = is.available();   						            
        	byte[] buffer = new byte[size];   				            
        	is.read(buffer);   				            
        	is.close();   
	            
        	String text = new String(buffer, "GB2312"); 
        
        	TextView tv = (TextView) findViewById(R.id.text);  
        	Handler handler = new Handler();  
        	handler.postDelayed(mScrollView, 200); 
        	tv.setText(text);
        	
        } catch (IOException e) {   
        	throw new RuntimeException(e);   
       
        }          
scrollView = (ScrollView)findViewById(R.id.scrollView);   
        
        scrollView.setOnTouchListener(new OnTouchListener() {   
        	@Override  
            public boolean onTouch(View v, MotionEvent event) {   
				
                if (event.getAction() == MotionEvent.ACTION_UP) {   
                    
                	  

                        scrollView.getScrollY();         
                }   
                return false;   
            }   
        });  
       
        
//章节显示  标题栏  
        
	}
	
	//菜单键
	  public boolean onCreateOptionsMenu(Menu menu) {
	         MenuInflater inflater = getMenuInflater();
	         //设置Menu界面为res/menu/menu.xml
	         inflater.inflate(R.menu.speel_r, menu);
	         return true;
	     }
	
	     @Override
	     public boolean onOptionsItemSelected(MenuItem item) {
	        
	         int item_id = item.getItemId();
	      
             final Intent intent = new Intent();
             
	         switch (item_id) {
	         
	         case R.id.address2:
	        	 intent.setClass(ReadAsset.this,Readaddress.class);
	        	 startActivity(intent);
	        	 finish();
   
	        	
	             break;
	         case R.id.Contents:
	        	 intent.setClass(ReadAsset.this,Contents.class);
	        	 startActivity(intent);
	        	 finish();
	        	 
	        	 break;
	         case R.id.guanyu:
	        	 new AlertDialog.Builder(this)    
	        	  
	        	 .setTitle("致谢") 
	        	   
	        	 .setMessage("感谢 你对 济南银屑病医院的支持 与关注官方质询网站：www.pf0531.com 银屑病 电子书  版本：0.0.1"
	        			 
	        			 
	        	 	) 
	        	   
	        	 .setPositiveButton("确定", null) 
	        	   
	        	 .show();
	        	 break;
	        	 
	         case R.id.end:
	        	 
	        	 finish();
	        	 break;
	         }
	     
	         
	         
	         return true;
	     }
	  
	 

		private Runnable mScrollView = new Runnable() {  
	            
	         

				@Override  
	            public void run() {  
	            
	                  scrollView.scrollTo(0, y);//改变滚动条的位置  

	            }  };
	
//监听键盘  返回 键
   
	public void onBackPressed() {
		super.onBackPressed();
		
    	Intent intent = new Intent(ReadAsset.this,Contents.class);
    	startActivity(intent);
        finish();
    	}
	public void setmScrollView(Runnable mScrollView) {
		this.mScrollView = mScrollView;
	}
	public Runnable getmScrollView() {
		return mScrollView;
	}
	
	     }
