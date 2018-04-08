package com.search.telephone;

import com.search.R;
import com.search.RequestListener;
import com.search.common.ActivityUtils;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class TelephoneSearch extends Activity {
	
	private EditText telephoneNum;
	
	private Search search;
	
	private String telephone;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.telephone);
        this.telephoneNum = (EditText)this.findViewById(R.id.telephone_num);
        search = new Search();
    }
    
    public void onClick(View v){
    	
    	if(v.getId() == R.id.telephone_search){
    		
    		if(this.validate()){
        		RequestListener listener = new TelephoneListener(this);
        		search.asyncRequest(telephone, listener);
    		}else{
    			Resources res = this.getResources();
    			
    			ActivityUtils.showDialog(this, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.tel_invalid));
    		}

    	}
    }
    
    /**
     * У��
     * @return
     */
    public boolean validate(){
    	telephone = telephoneNum.getText().toString();
    	if(telephone.equals("")){
    		return false;
    	}else{
    		for(char c : telephone.toCharArray()){
    			if(c < '0' || c > '9'){
    				return false;
    			}
    		}
    	}
    	
    	return true;
    }
}
