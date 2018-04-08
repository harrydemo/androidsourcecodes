package zdq.msg;

import zdq.service.SmsService_filter;
import zdq.service.Smsservice;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class SmsReceiverActivity extends Activity {
	
	private Button bt_reply,bt_filter,bt_filter_set,bt_reply_set;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        getview();
        
        bt_reply.setOnClickListener(bt_reply_onclick);
        bt_filter.setOnClickListener(bt_filter_onclick);
        bt_filter_set.setOnClickListener(bt_filter_set_onclick);
        bt_reply_set.setOnClickListener(bt_reply_set_onclick);
    }
    
    private void getview(){
    	bt_reply=(Button)findViewById(R.id.bt_reply);
    	bt_filter=(Button)findViewById(R.id.bt_filter);
    	bt_filter_set=(Button)findViewById(R.id.bt_filter_set);
    	bt_reply_set=(Button)findViewById(R.id.bt_reply_set);
    }
    
    private OnClickListener bt_reply_onclick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			if(bt_reply.getText().equals(getResources().getString(R.string.bt01))){
				Intent intent = new Intent();
				intent.setClass(SmsReceiverActivity.this, Smsservice.class);
				startService(intent);
				bt_reply.setText(R.string.bt02);
				Toast.makeText(SmsReceiverActivity.this, "打开监听", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent();
				intent.setClass(SmsReceiverActivity.this, Smsservice.class);
				stopService(intent);
				bt_reply.setText(R.string.bt01);
				Toast.makeText(SmsReceiverActivity.this, "关闭监听", Toast.LENGTH_SHORT).show();
			}
		}
    };
    
    private OnClickListener bt_filter_onclick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			if(bt_filter.getText().equals(getResources().getString(R.string.bt01))){
				Intent intent = new Intent();
				intent.setClass(SmsReceiverActivity.this, SmsService_filter.class);
				startService(intent);
				bt_filter.setText(R.string.bt02);
				Toast.makeText(SmsReceiverActivity.this, R.string.bt01, Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent();
				intent.setClass(SmsReceiverActivity.this, SmsService_filter.class);
				stopService(intent);
				bt_filter.setText(R.string.bt01);
				Toast.makeText(SmsReceiverActivity.this, R.string.bt02, Toast.LENGTH_SHORT).show();
			}
		}
    };
    
    private OnClickListener bt_filter_set_onclick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent it=new Intent();
			it.setClass(SmsReceiverActivity.this, SQLFilter.class);
			startActivity(it);
		}
    	
    };
    
    private OnClickListener bt_reply_set_onclick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent it=new Intent();
			it.setClass(SmsReceiverActivity.this, SMSReply.class);
			startActivity(it);
		}
    };
    
    
    
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(0,0,0,R.string.about);
    	menu.add(0,1,1,R.string.exit);
    	
		return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	switch(item.getItemId()){
    	case 0:
    		openDialog();
    		break;
    	case 1:
    		finish();
    		break;
    	}
    	return true;
    }
    
    private void openDialog(){
    	new AlertDialog.Builder(this)
    	.setTitle(R.string.about)
    	.setMessage(R.string.message)
    	.setPositiveButton(R.string.ok, 
    			new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}
    			).show();
    }
}