package com.testindex;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private int count = 5;
	private int[] imgIDs = {R.id.widget29,R.id.widget30,R.id.widget31,R.id.widget32,R.id.widget33};
	private int INDEX_SELECTED = 0;
	private final int EDIT_TYPE_SELECTED = 1;     //选中的   
	private final int EDIT_TYPE_NO_SELECTED = 2;  //未选中的
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final IndexThread thread = new IndexThread();
        for(int id : imgIDs)
        ((ImageView)findViewById(id)).setBackgroundResource(R.drawable.progress_bg_small);
        thread.start();
        Button btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				thread.flag = false;
				
			}
		});
    }
    
    public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i("Test","---"+ msg.arg1);
			switch(msg.what)
			{
			case EDIT_TYPE_SELECTED:
				((ImageView)findViewById(msg.arg1)).setBackgroundResource(R.drawable.progress_go_small);
				break;
			case EDIT_TYPE_NO_SELECTED:
				((ImageView)findViewById(msg.arg1)).setBackgroundResource(R.drawable.progress_bg_small);
				break;
			}
		}
    };
    
    class IndexThread extends Thread
    {
    	boolean flag = true;
    	@Override
	     public void run()
	     {
    		Message msg;
    		while(flag)
    		{
    			for(int i= 0 ; i < count ; i++)
    			{
    				Log.i("Test","---"+ count);
    				msg = new Message();
    				msg.what = EDIT_TYPE_SELECTED;
    				msg.arg1 = imgIDs[i];
    				myHandler.sendMessage(msg);
    				//findViewById(imgIDs[i]).setBackgroundResource(R.drawable.progress_go_small);
    				msg = new Message();
    				if(i==0)
    				{
    					msg.what = EDIT_TYPE_NO_SELECTED;
    					msg.arg1 = imgIDs[count-1];
    					myHandler.sendMessage(msg);
    					//findViewById(imgIDs[count-1]).setBackgroundResource(R.drawable.progress_bg_small);
    				}
    				else
    				{
    					msg.what = EDIT_TYPE_NO_SELECTED;
    					msg.arg1 = imgIDs[i-1];
    					myHandler.sendMessage(msg);
    					//findViewById(imgIDs[i-1]).setBackgroundResource(R.drawable.progress_bg_small);
    				}
    				SystemClock.sleep(500);
    			}
    		}
    		
	     }
    }
}