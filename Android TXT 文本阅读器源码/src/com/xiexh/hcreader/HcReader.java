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
	 * ��������
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
     * �����Ի���
     * @param id
     * @return
     */
    protected Dialog onCreateDialog( int id ){
    	switch(id){
    		case DIALOG_INFO:
    			return new AlertDialog.Builder(this)
    					.setIcon(android.R.drawable.ic_dialog_info)
    					.setTitle("Hello xxh")
    					.setMessage("�汾��HC�Ķ���1.0 \n֧�ָ�ʽ��TXT")
    					.setPositiveButton("OK", null)
    					.create();
    		default:
    			return null;
    	}
    }
    /**
     * ����
     */
    public void onDestroy(){
    	super.onDestroy();
    	Log.i(TAG, "onDestroy");
    }
}