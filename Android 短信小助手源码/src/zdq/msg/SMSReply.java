package zdq.msg;

import zdq.data.ReplyData;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SMSReply extends Activity{
	private Button bt_set;
	private Button bt_no;
	private EditText et_msg;
	private String msg;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.reply);
		
		getview();
		this.setTitle("快速回复内容");
		et_msg.setText(ReplyData.reply_msg);
		bt_no.setOnClickListener(bt_no_onclick);
		bt_set.setOnClickListener(bt_set_onclick);
	}
	
	
	private void getview() {
		bt_no=(Button)findViewById(R.id.bt_no);
		bt_set=(Button)findViewById(R.id.bt_set);
		et_msg=(EditText)findViewById(R.id.et_msg);
	}
	
	private OnClickListener bt_no_onclick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	private OnClickListener bt_set_onclick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			msg=et_msg.getText().toString();
			
			if(!"".equals(msg)){
				ReplyData.reply_msg=msg;
				finish();
			}
		}
	};
}
