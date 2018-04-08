package cn.err0r.android.sms.view;

import cn.err0r.android.sms.R;
import cn.err0r.android.sms.SampleListAdapter;
import cn.err0r.android.sms.SampleListAdapter.ViewHolder;
import cn.err0r.android.sms.database.SMSSampleDao;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DealList extends Activity {
	ListView lv;
	Button deallist_add,samplesmslist_delete;
    SMSSampleDao smssampledao;
    SampleListAdapter adapter;
    
    void getView(){
    	lv = (ListView)findViewById(R.id.deallist_lv);
    	deallist_add=(Button)findViewById(R.id.deallist_btn1);
    	samplesmslist_delete=(Button)findViewById(R.id.samplesms_btn1);
    }
    
	@Override
	public void onResume(){
		Log.i("deallist","onResume");
		adapter=new SampleListAdapter(this); 
		lv.setAdapter(adapter); 
		super.onResume();
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deallist);
		
		getView();
		
        lv.setItemsCanFocus(false);  
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); 
        
        lv.setOnItemClickListener(new OnItemClickListener(){    
            public void onItemClick(AdapterView<?> parent, View view,    
                    int position, long id) {    
            	ViewHolder vHollder = (ViewHolder) view.getTag();   
            	Intent it= new Intent(DealList.this, SMSAdd.class);
            	Bundle bundle= new Bundle();
            	bundle.putString("Context", vHollder.sample_body.getTag().toString());
            	bundle.putBoolean("State", vHollder.sample_cBox.isChecked());
            	bundle.putInt("Sid", Integer.valueOf(vHollder.sample_cBox.getTag().toString()));
            	it.putExtras(bundle);
            	startActivity(it);
            }    
        }); 

       deallist_add.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent add= new Intent(DealList.this, SMSAdd.class);
			startActivity(add);
			}
       });
      
	}

}

