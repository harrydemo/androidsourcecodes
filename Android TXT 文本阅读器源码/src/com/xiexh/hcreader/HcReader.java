package com.xiexh.hcreader;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public class HcReader extends Activity {
	/**
	 * 变量定义
	 */
	private final int DIALOG_INFO = 0;
	private final String TAG = "[HcReader]";
   /** 
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i(TAG, "onCreate");
        showDialog(DIALOG_INFO);
        
        OnClickListener listener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch( v.getId() ){
					case R.id.txtBtn :
						Intent i = new Intent( HcReader.this,TxtList.class );
						startActivity(i);
				}
				
			}
        };
        findViewById(R.id.txtBtn).setOnClickListener(listener);
    }
   
    /**
     * 创建对话框
     * @param id
     * @return
     */
    protected Dialog onCreateDialog( int id ){
    	switch(id){
    		case DIALOG_INFO:
    			return new AlertDialog.Builder(this)
    					.setIcon(android.R.drawable.ic_dialog_info)
    					.setTitle("Hello xxh")
    					.setMessage("版本：HC阅读器1.0 \n支持格式：TXT")
    					.setPositiveButton("OK", null)
    					.create();
    		default:
    			return null;
    	}
    }
    /**
     * 结束
     */
    public void onDestroy(){
    	super.onDestroy();
    	Log.i(TAG, "onDestroy");
    }
}