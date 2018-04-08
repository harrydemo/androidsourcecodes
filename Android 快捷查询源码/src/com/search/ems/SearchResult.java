package com.search.ems;

import com.search.R;
import com.search.common.ActivityUtils;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class SearchResult extends Activity {
	
	private TextView emsCompany,emsOrder,emsTime,emsContext;
	
	private Resources res;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ems_info);
		
		this.emsCompany = (TextView)this.findViewById(R.id.ems_company);
		this.emsOrder = (TextView)this.findViewById(R.id.ems_order);
		this.emsTime = (TextView)this.findViewById(R.id.ems_time);
		this.emsContext = (TextView)this.findViewById(R.id.ems_context);
		
		
		res = this.getResources();
		
		this.showResult();
	}

	private void showResult() {
		
		Bundle bundle = this.getIntent().getExtras();
		
		Ems ems = (Ems) bundle.getSerializable("ems");
		
		if(ems != null){
			if(ems.getStatus().equals("1")){
				this.emsCompany.setText(ems.getCompany());
				this.emsOrder.setText(ems.getOrder());
				this.emsTime.setText(ems.getTime());
				this.emsContext.setText(ems.getContext());
				//this.emsMessage.setText(ems.getMessage());	
				
			}else{
				ActivityUtils.showDialog(this, res.getString(R.string.ok), res.getString(R.string.tip), ems.getMessage());
			}

		}else{
			ActivityUtils.showDialog(this, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.ems_fail));
		}
		
	}

}
