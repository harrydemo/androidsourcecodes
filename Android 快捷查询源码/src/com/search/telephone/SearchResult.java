package com.search.telephone;

import com.search.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class SearchResult extends Activity {
	
	private TextView mobile,province,city,areacode,postcode,card;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.telephone_info);
        
        mobile = (TextView)this.findViewById(R.id.telephone_mobile);
        province = (TextView)this.findViewById(R.id.telephone_province);
        city = (TextView)this.findViewById(R.id.telephone_city);
        areacode = (TextView)this.findViewById(R.id.telephone_areacode);
        postcode = (TextView)this.findViewById(R.id.telephone_postcode);
        card = (TextView)this.findViewById(R.id.telephone_card);
        
        this.showResult();

    }
    
    public void showResult(){
    	Intent intent = this.getIntent();
    	Bundle bundle = intent.getExtras();
    	
    	Telephone tel = (Telephone)bundle.getSerializable("telephone");
    	if(tel != null){
        	mobile.setText(tel.getMobile());
        	province.setText(tel.getProvince());
        	city.setText(tel.getCity());
        	areacode.setText(tel.getAreaCode());
        	postcode.setText(tel.getPostCode());
        	card.setText(tel.getCard());
    	}

    }

}
