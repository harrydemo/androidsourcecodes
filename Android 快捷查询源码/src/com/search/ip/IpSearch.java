package com.search.ip;

import com.search.R;
import com.search.common.ActivityUtils;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

/**
 * Ip地址查询
 * @author Administrator
 *
 */
public class IpSearch extends Activity {
	
	private EditText ipText;
	
	private Search search;
	
	private Resources res;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ip);	
        
        ipText = (EditText)this.findViewById(R.id.ip_ip);
        
        search = new Search();
        
        res = this.getResources();
    }
    
    public void onClick(View v){
    	
    	if(v.getId() == R.id.ip_search){
    		String ip = ipText.getText().toString().trim();
    		if(ActivityUtils.validateNull(ip)){
    			IpListener listener = new IpListener(this);
    			search.asyncRequest(ip, listener);
    		}else{
    			ActivityUtils.showDialog(this, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.ip_null));
    		}
    	}
    }
}
