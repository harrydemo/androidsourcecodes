package com.search.ip;

import com.search.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class SearchResult extends Activity {
	
	private TextView ipView,attrView;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ip_info);	
        
        ipView = (TextView)this.findViewById(R.id.ip_ip2);
        attrView = (TextView)this.findViewById(R.id.ip_addr);
        
        this.showResult();
    }

	private void showResult() {
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		Ip ip = (Ip)bundle.getSerializable("ip");
		
		ipView.setText(ip.getIp());
		attrView.setText(ip.getLocation());
		
	}
}
