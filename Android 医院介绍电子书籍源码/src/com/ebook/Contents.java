package com.ebook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

import android.content.DialogInterface;
import android.content.Intent;
//import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import android.widget.ListView;
import android.widget.SimpleAdapter;


public class Contents extends Activity {
	
	
	         //目录标题 
	                                   //js1          js2                                      js3             
             
	private String data[] = { "济南银屑病医院简介 ", "SD-P多维银屑病治疗康复体系有什么优势？" , "德国UVB光疗疗法",
			                  "绿色净肤疗法",   "纯中药汽浴疗法" ,    "高氧舱氧吧红外疗法" ,    "SD-P免疫细胞再生疗法"
		
		       
		                   ,"SD-P自体血净化疗法","SD-P康复治疗体系的独特功效","SD-P康复治疗体系对比其他疗法的优势"
		                   ,"联系地址"
		                   //,"传值8","传值9"
		                   //,"传值10"
		                 }; 
             
	private ListView datalist = null;            
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();        
	private SimpleAdapter simpleAdapter = null;

	@Override        
	public void onCreate(Bundle savedInstanceState) {
                   
            	 
		super.onCreate(savedInstanceState);                          	 
		super.setContentView(R.layout.maintwo);
		
		
//		
//		 t1= (TextView) findViewById(R.id.paoView);
// 	    t1.setText("089289599298929682968");
//
// 	    t1.setTextSize(30);
// 	    t1.setHorizontallyScrolling(true);
// 	    t1.setFocusable(true);
        this.datalist = (ListView) super.findViewById(R.id.datalist);
   	 
		for (int x = 0; x < this.data.length; x++) {
		 
			Map<String, String> map = new HashMap<String, String>(); 		 
			map.put("name", this.data[x]); 		 
			this.list.add(map); 	 
		}
  	
		this.simpleAdapter = new SimpleAdapter(this, this.list,R.layout.data_list, new String[] { "name" } , new int[] { R.id.name });  
		 
		this.datalist.setAdapter(this.simpleAdapter); 	
		this.datalist.setOnItemClickListener(new OnItemClickListenerImpl());      
	}

	
	private class OnItemClickListenerImpl implements OnItemClickListener {
  	 

		public void onItemClick(
            			  			
				AdapterView<?> parent, View view, int position,long id) 	 
		{
		     

			
			Intent intent = new Intent();
			if(position==10){
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
			    intent.setClass(Contents.this,Readaddress.class);		 
				Contents.this.startActivity(intent);
					
				
				Bundle bundle = new Bundle();		
				bundle.putString("name",".txt" );	
				bundle.putBoolean("Ismale", true);	
				intent.putExtras(bundle);	
			
				finish();
	 
		}
		
		else {
				
				   

				
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		        
				Bundle bundle = new Bundle();
				bundle.putInt("name",position);
				
				bundle.putBoolean("Ismale", true);	
				intent.putExtras(bundle);
				intent.setClass(Contents.this,ReadAsset.class);	
            	Contents.this.startActivity(intent);
				//startActivityForResult(intent, 0);	
				finish();	
					
			}
		
		}
		 
	}
	
	//菜单键
	  public boolean onCreateOptionsMenu(Menu menu) {
	         MenuInflater inflater = getMenuInflater();
	         //设置Menu界面为res/menu/menu.xml
	         inflater.inflate(R.menu.speel_c, menu);
	         return true;
	     }
	
	     @Override
	     public boolean onOptionsItemSelected(MenuItem item) {
	        
	         int item_id = item.getItemId();
	      
           final Intent intent = new Intent();
           
	         switch (item_id) {
	         
	         case R.id.address3:
	        	 intent.setClass(Contents.this,Readaddress.class);
	        	 startActivity(intent);
	        	 finish();

	        	 break;
	       
	         case R.id.guanyu2:
	        	 new AlertDialog.Builder(this)    
	        	  
	        	 .setTitle("致谢") 
	        	   
	        	 .setMessage(" 济南银屑病医院  ........."
	        			 
	        			 
	        	 	) 
	        	   
	        	 .setPositiveButton("确定", null) 
	        	   
	        	 .show();
	        	 break;
	        	 
	         case R.id.end2:
	        	 
	        	 finish();
	        	 break;
	         }
	     
	         
	         
	         return true;
	     }
	  
	

//返回键监听

//
 
	protected void dialog() {
	
	
		AlertDialog.Builder builder = new Builder(Contents.this);	
		builder.setMessage("是否退出应用程序?");	
		builder.setTitle("提示");		
		builder.setPositiveButton("是",new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				finish();
			}
		}
		);

		builder.setNegativeButton("否",
	       
				new android.content.DialogInterface.OnClickListener() {

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

}












