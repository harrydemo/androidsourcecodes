package cn.err0r.android.sms.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.err0r.android.sms.R;
import cn.err0r.android.sms.ReceiverType;
import cn.err0r.android.sms.SMSEX;
import cn.err0r.android.sms.database.SMSINFODao;

public class Main extends BaseActivty {
	Button btnStartMonitor;
	Button btnStartTrust;
	Button btnStartReply;
	TextView waitView,deallist;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnStartMonitor=(Button)findViewById(R.id.btnStartMonitor);
        btnStartMonitor.setOnClickListener(new OnClickListener() {
 		
 		public void onClick(View v) {
 			// TODO Auto-generated method stub
 			Log.i("smstoast", "set smstoast");
 			if(btnStartMonitor.getText().equals(getResources().getString(R.string.startmoritor))){
	 			stopService(new Intent("cn.err0r.android.sms.MSG_SERVICE"));
	 			Intent service = new Intent("cn.err0r.android.sms.MSG_SERVICE");
	 			Bundle mBundle = new Bundle();  
		        mBundle.putSerializable("TYPE",ReceiverType.Standard);
		        service.putExtras(mBundle);
	 			startService(service);
	 			btnStartMonitor.setText(R.string.endmoritor);
 			}else{
 				stopService(new Intent("cn.err0r.android.sms.MSG_SERVICE"));
 				btnStartMonitor.setText(R.string.startmoritor);
 				btnStartTrust.setText(R.string.starttrust);
	 			btnStartReply.setText(R.string.startreply);
 			}
 		}
        });
        
        btnStartTrust=(Button)findViewById(R.id.btnStartTrust);
        btnStartTrust.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btnStartTrust.getText().equals(getResources().getString(R.string.starttrust))){
					Log.i("smstoast", "set smstoast");
		 			stopService(new Intent("cn.err0r.android.sms.MSG_SERVICE"));
		 			Intent service = new Intent("cn.err0r.android.sms.MSG_SERVICE");
		 			Bundle mBundle = new Bundle();  
			        mBundle.putSerializable("TYPE",ReceiverType.Quiet);
			        service.putExtras(mBundle);
		 			startService(service);
		 			btnStartMonitor.setText(R.string.endmoritor);
		 			btnStartTrust.setText(R.string.endtrust);
		 			btnStartReply.setText(R.string.startreply);
				}else{
					stopService(new Intent("cn.err0r.android.sms.MSG_SERVICE"));
	 				btnStartMonitor.setText(R.string.endmoritor);
	 				btnStartTrust.setText(R.string.starttrust);
	 				Intent service = new Intent("cn.err0r.android.sms.MSG_SERVICE");
		 			Bundle mBundle = new Bundle();  
			        mBundle.putSerializable("TYPE",ReceiverType.Standard);
			        service.putExtras(mBundle);
		 			startService(service);
				}
			}
		});
        
        btnStartReply = (Button)findViewById(R.id.btnStartReply);
        btnStartReply.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btnStartReply.getText().equals(getResources().getString(R.string.startreply))){
					Log.i("smstoast", "set smstoast");
		 			stopService(new Intent("cn.err0r.android.sms.MSG_SERVICE"));
		 			Intent service = new Intent("cn.err0r.android.sms.MSG_SERVICE");
		 			Bundle mBundle = new Bundle();  
			        mBundle.putSerializable("TYPE",ReceiverType.Simple);
			        service.putExtras(mBundle);
		 			startService(service);
		 			btnStartMonitor.setText(R.string.endmoritor);
		 			btnStartTrust.setText(R.string.starttrust);
		 			btnStartReply.setText(R.string.endreply);
				}else{
					stopService(new Intent("cn.err0r.android.sms.MSG_SERVICE"));
	 				btnStartMonitor.setText(R.string.endmoritor);
	 				btnStartReply.setText(R.string.startreply);
	 				Intent service = new Intent("cn.err0r.android.sms.MSG_SERVICE");
		 			Bundle mBundle = new Bundle();  
			        mBundle.putSerializable("TYPE",ReceiverType.Standard);
			        service.putExtras(mBundle);
		 			startService(service);
				}
			}
		});
        
        deallist = (TextView)findViewById(R.id.deallist);
        deallist.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent deallist= new Intent(Main.this, DealList.class);
				startActivity(deallist);
			}
		});
        waitView = (TextView)findViewById(R.id.waitView);
        waitView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Main.this,SMSWaitList.class));
			}
		});
    }

	@Override
	public void onPause(){
		super.onPause();
		SMSEX ex = (SMSEX)this.getApplication();
		ex.setM(btnStartMonitor.getText().toString());
		ex.setT(btnStartTrust.getText().toString());
		ex.setR(btnStartReply.getText().toString());
	}
	
	@Override
	public void onResume(){
		super.onResume();
		waitView.setText(getResources().getString(R.string.waiting_list) + getWaitCount());
		SMSEX ex = (SMSEX)this.getApplication();
		if(ex.getM() != null){
			btnStartMonitor.setText(ex.getM());
			btnStartTrust.setText(ex.getT());
			btnStartReply.setText(ex.getR());
		}
	}
	
	SMSINFODao smsinfodao;
	String getWaitCount(){
		
		if(smsinfodao == null)
			smsinfodao = new SMSINFODao(Main.this);
		Cursor getcount = smsinfodao.select();
		int count = getcount.getCount();
		getcount.close();
		smsinfodao.close();
		if(count != 0)
			return "("+count+")";
		return "";
	}

}
