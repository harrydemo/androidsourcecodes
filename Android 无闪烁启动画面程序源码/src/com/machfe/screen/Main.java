package com.machfe.screen; 


 
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
 
/* 
 *  ����: machfe
 *  ���䣺mni2005@163.com
 *  QQ :  173436291
 * 
 */

public class Main extends Activity {
	 
	 
	private LayoutInflater  m_flater = null; 
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash);	 
		m_flater = getLayoutInflater(); 
		
		LoadMainTask task= new LoadMainTask(this);
		task.execute("");
		
	}

	 
	  public View LoadMainView(  LayoutInflater flater){
	    	
	    	View view = flater.inflate(R.layout.main, null);  
	    	
		    Button	btnOk =(Button)view.findViewById(R.id.BtnOk);  
	    	btnOk.setOnClickListener(new OnClickListener(){  
				public void onClick(View v) { 
					  finish();
				}	    		
	    	});   
	    	
			 return view;

	    } 
  
	  private class LoadMainTask extends AsyncTask<Object, Object, View> {

			public LoadMainTask(Context context) {

			}

			protected View doInBackground(Object... params) {
				View view = null;
				view = LoadMainView(m_flater);
			
				//Ϊ�˲��Լ�����ʱ����ҿ�������һ�������Դ�����ݵ�
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				 
					e.printStackTrace();
				}
				return view;
			}

		    	// ִ�����
			protected void onPostExecute(View view) {
				setContentView(view);

			}

		}
		
	  
  //���ΰ���
  public boolean onKeyDown(int keyCode, KeyEvent event){
		
  	if(keyCode == KeyEvent.KEYCODE_BACK){
  		return  true;
		} else if(keyCode == KeyEvent.KEYCODE_HOME){
  		return  true;
		} 
	return  false;
 }

    
}