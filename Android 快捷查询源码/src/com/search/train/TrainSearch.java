package com.search.train;
import java.net.URLEncoder;

import com.search.R;
import com.search.RequestListener;
import com.search.common.ActivityUtils;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class TrainSearch extends Activity {
	
	private EditText trainFrom, trainTo;
	
	private Search search;
	
	private Resources res;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.train);
        
        search = new Search();
        
        trainFrom = (EditText)this.findViewById(R.id.train_from);
        trainTo = (EditText)this.findViewById(R.id.train_to);
        
        res = this.getResources();
    }
    
    public void onClick(View v){
  
    	String from = trainFrom.getText().toString().trim();
    	String to = trainTo.getText().toString().trim();
    	
    	if(ActivityUtils.validateNull(from) && ActivityUtils.validateNull(to)){
        	String queryFrom = URLEncoder.encode(from);
        	String queryTo = URLEncoder.encode(to);
        	//Log.v("Encode：", queryFrom + queryTo);
        	RequestListener listener = new TrainListener(this);
        	search.asyncRequest(queryFrom, queryTo, listener); 

    	}else{
    		ActivityUtils.showDialog(this, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.train_null));
    	}

    }
}
