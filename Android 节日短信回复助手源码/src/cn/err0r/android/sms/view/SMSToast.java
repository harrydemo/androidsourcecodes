package cn.err0r.android.sms.view;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.err0r.android.sms.R;
import cn.err0r.android.sms.database.SMSINFODao;
import cn.err0r.android.sms.database.SMSINFOModel;
import cn.err0r.android.sms.database.SMSSampleDao;
import cn.err0r.android.sms.database.SMSSampleModel;

public class SMSToast extends BaseActivty {
	SMSINFOModel smsinfo;
	TextView lvlpn;
	TextView lvlcontext;
	Button btnReply;
	Button btnWait;
	Button btnClose;
	Button btnSave;
	Button btnAutoReply;
	Button btnSend;
	Button btnChange;
	EditText etContext;
	EditText etChange;
	
	SMSSampleModel smssample;
    SMSSampleDao smssampledao;
    
    SMSINFODao smsinfodao;
    
	ArrayList<String> pns = new ArrayList<String>();
	
	void getView(){
		lvlpn = (TextView)findViewById(R.id.tvPn);
		lvlcontext = (TextView)findViewById(R.id.tvContext);
		btnReply = (Button)findViewById(R.id.btnReply);
		btnWait = (Button)findViewById(R.id.btnWait);
		btnClose = (Button)findViewById(R.id.btnClose);
		btnSave = (Button)findViewById(R.id.button3);
		btnAutoReply = (Button)findViewById(R.id.btnAutoReply);
		btnSend = (Button)findViewById(R.id.btnSend);
		btnChange = (Button)findViewById(R.id.btnChange);
		etChange = (EditText)findViewById(R.id.etPn);
		etContext = (EditText)findViewById(R.id.editText1);
	}
	
	void ChangeView(int type){
		switch (type) {
		case 1:
			btnAutoReply.setVisibility(View.GONE);
			btnReply.setVisibility(View.VISIBLE);
			btnWait.setVisibility(View.VISIBLE);
			btnSave.setVisibility(View.VISIBLE);
			break;
		case 2:
			btnAutoReply.setVisibility(View.VISIBLE);
			btnReply.setVisibility(View.GONE);
			btnWait.setVisibility(View.VISIBLE);
			btnSave.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.smstoast);
        getView();        
        Intent intent = this.getIntent(); 
        ChangeView(intent.getIntExtra("Type", 1));
        smsinfo = (SMSINFOModel)intent.getSerializableExtra("SMSINFO");
        pns.add(smsinfo.get_pn());
        String showpn =  getResources().getString(R.string.from)+smsinfo.get_who() + (smsinfo.get_who().equals("")? smsinfo.get_pn() : "-" + smsinfo.get_pn());
        lvlpn.setText(showpn);
        lvlcontext.setText(smsinfo.get_body());
        
        btnReply.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SMSToast.this, SmsSend.class); 
				Bundle mBundle = new Bundle();  
		        mBundle.putSerializable("SMSINFO",smsinfo);  
		        intent.putExtras(mBundle); 
				startActivity(intent);
				finish();
			}
		});
        
        btnWait.setOnClickListener(waitListener);
        btnClose.setOnClickListener(closeListener);
        btnSave.setOnClickListener(saveSamle);
        btnAutoReply.setOnClickListener(autoSendSMS);
        btnSend.setOnClickListener(sendSMS);
        btnChange.setOnClickListener(changeUserName);
	}
	
	OnClickListener changeUserName = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(etChange.getVisibility() == View.GONE){
				etChange.setVisibility(View.VISIBLE);
				lvlpn.setVisibility(View.GONE);
			}else{
				etChange.setVisibility(View.GONE);
				lvlpn.setVisibility(View.VISIBLE);
			}
		}
	};
	
	OnClickListener sendSMS = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SendSms(SMSToast.this,smsinfo.get_pn(),etContext.getText().toString());
			finish();
		}
	};
	
	OnClickListener autoSendSMS = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			smssampledao = new SMSSampleDao(SMSToast.this);
			Cursor sms = smssampledao.select("State = '1'");
			int max = sms.getCount();
			Log.i("smsCount",String.valueOf(max));
			int r = getIndex(max-1);
			Log.i("R",String.valueOf(r));
			if(sms.moveToPosition(r)){
				SendSms(SMSToast.this,smsinfo.get_pn(),sms.getString(2));
				finish();
			}
			else{
				Toast.makeText(SMSToast.this, getResources().getString(R.string.smstoast_randomsendfailed), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	OnClickListener saveSamle = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SMSINFODao smsdao = new SMSINFODao(SMSToast.this);
			if(smsdao != null){
				smsdao.insert(smsinfo);					
			}
			smsdao.close();
			
			Intent i = new Intent(SMSToast.this,SMSAdd.class);
			i.putExtra("Context", smsinfo.get_body());
			SMSToast.this.startActivity(i);
			
			finish();
		}
	};
	
    OnClickListener closeListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			  //点击关闭把信息存入待回消息			
//			SMSINFODao smsdao = new SMSINFODao(SMSToast.this);
//			if(smsdao != null){
//				smsdao.insert(smsinfo);					
//			}
//			smsdao.close();
			finish();
		}
	}; 
	
	OnClickListener waitListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SMSINFODao smsdao = new SMSINFODao(SMSToast.this);
			if(smsdao != null){
				if(etChange.getVisibility()==View.VISIBLE)
					smsinfo.set_who(etChange.getText().toString());
				smsdao.insert(smsinfo);					
			}
			smsdao.close();
			finish();
		}
	};
    
	public int getIndex(int max){
		return (int)(Math.random()*max) ;
        
	}
	
	public void sendSMS(String pn ,String body){
		ContentValues values = new ContentValues(); 
		values.put("address", pn); 
		values.put("body", body); 
		//把短信插入发件箱
		getContentResolver().insert(Uri.parse("content://sms/sent"), values); 
		smsinfodao = new SMSINFODao(SMSToast.this);
		smsinfodao.deleteByPn(pn);
		smsinfodao.close();
		finish();
	}
	
	public Boolean SendSms(Context context,String addre, String mess)
	{
	        try
	        {
	            PendingIntent mPI = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
	            SmsManager.getDefault().sendTextMessage(addre, null, mess, mPI,null);
	            sendSMS(addre,mess);
	            Toast.makeText(SMSToast.this, getResources().getString(R.string.smssend_succeed), Toast.LENGTH_SHORT).show();
	            return true;
	        }
	        catch (Exception e)
	        {
	            return false;
	        }
	}
}
