package com.genius.demo;

import com.genius.demo.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TrafficStatActivity extends Activity{

	private TextView mTextViewBaseSendTextView;
	private TextView mTextViewBaseRecvTextView;
	
	private TextView mTextViewTotalSendTextView;
	private TextView mTextViewTotalRecvTextView;
	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
        setContentView(R.layout.act_set_trafficstat);
        
        init();
    }
	
	private  void init()
	{
		
		
		View layout1 = findViewById(R.id.trafic_today_tbl);
		mTextViewBaseSendTextView = (TextView) layout1.findViewById(R.id.base_send_trafic);
		mTextViewBaseRecvTextView = (TextView) layout1.findViewById(R.id.base_recv_trafic);
		
		mTextViewTotalSendTextView = (TextView) layout1.findViewById(R.id.total_send_trafic);
		mTextViewTotalRecvTextView = (TextView) layout1.findViewById(R.id.total_recv_trafic);
		
		
		mTextViewBaseSendTextView.setText("14 KB");
		mTextViewBaseRecvTextView.setText("3 KB");
		
		mTextViewTotalSendTextView.setText("14 KB");
		mTextViewTotalRecvTextView.setText("3 KB");
		
	}
}
