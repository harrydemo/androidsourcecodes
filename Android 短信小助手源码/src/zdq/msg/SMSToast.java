package zdq.msg;

import zdq.data.ReplyData;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SMSToast extends Activity {
	private Button bt_send;
	private Button bt_no;
	private EditText et_addr;
	private EditText et_msg;
	private String addr;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.toast);
		
        this.setTitle("");
		getview();
		
		Intent intent = this.getIntent(); 
		addr = (String) intent.getSerializableExtra("addr");
		et_addr.setText(addr);
		et_msg.setText(ReplyData.reply_msg);
		
		getclick();
	}
	
	private void getview(){
		bt_send=(Button)findViewById(R.id.bt_send);
		bt_no=(Button)findViewById(R.id.bt_no);
		et_addr=(EditText)findViewById(R.id.et_addr);
		et_msg=(EditText)findViewById(R.id.et_msg);
	}
	
	private void getclick(){
		bt_send.setOnClickListener(bt_send_onclick);
		bt_no.setOnClickListener(bt_no_onclick);
	}
	
	private OnClickListener bt_send_onclick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			SendSms(SMSToast.this,et_addr.getText().toString(),et_msg.getText().toString());
			finish();
		}
	};
	
	private OnClickListener bt_no_onclick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	/**
	 * 发送短信
	 * @param context
	 * @param addre
	 * @param mess
	 */
	public void SendSms(Context context,String addre, String mess)
	{
	       SmsManager manager = SmsManager.getDefault();
	       manager.sendTextMessage(addre, null, mess, null, null);
	}
}