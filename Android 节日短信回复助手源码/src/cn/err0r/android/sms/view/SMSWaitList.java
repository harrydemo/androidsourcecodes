package cn.err0r.android.sms.view;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import cn.err0r.android.sms.ListAdapter;
import cn.err0r.android.sms.ListAdapter.ViewHolder;
import cn.err0r.android.sms.R;
import cn.err0r.android.sms.database.SMSINFODao;

public class SMSWaitList extends BaseActivty {
	String TAG = "SMSWaitList";
	ListView list;
	SMSINFODao smsinfodao;
	
	
	@Override
	public void onResume(){

		Log.i("wait list","onResume");
		ListAdapter adapter=new ListAdapter(this);    
        list.setAdapter(adapter);
        super.onResume();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smswaitlist);
		
		list=(ListView)findViewById(R.id.lv);    
            
        list.setItemsCanFocus(false);    
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);    
    
        list.setOnItemClickListener(new OnItemClickListener(){    
            public void onItemClick(AdapterView<?> parent, View view,    
                    int position, long id) {    
                ViewHolder vHollder = (ViewHolder) view.getTag();    
                //在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。    
                vHollder.cBox.toggle();    
                ListAdapter.isSelected.put(position, vHollder.cBox.isChecked());    
            }    
        }); 
        
        Button btnReplySMS = (Button)findViewById(R.id.btnReplySMS);
        btnReplySMS.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<String> pns = new ArrayList<String>();
				for(int i=0;i<list.getCount();i++){    
	                if(ListAdapter.isSelected.get(i)){    
	                    ViewHolder vHollder = (ViewHolder) list.getChildAt(i).getTag(); 
	                    pns.add((String) vHollder.pn.getText());
	                    Log.i(TAG, "--onClick --"+vHollder.pn.getText());    
	                }    
	            }
				Intent intent = new Intent(SMSWaitList.this, SmsSend.class); 
				Bundle mBundle = new Bundle();  
		        mBundle.putSerializable("PNS",pns);  
		        intent.putExtras(mBundle); 
				startActivity(intent);
				finish();
			}
		});
	
        Button btnDelete = (Button)findViewById(R.id.deallist_btn2);
        btnDelete.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(smsinfodao == null)
					smsinfodao =  new SMSINFODao(SMSWaitList.this);
				for(int i=0;i<list.getCount();i++){    
	                if(ListAdapter.isSelected.get(i)){    
	                    ViewHolder vHollder = (ViewHolder) list.getChildAt(i).getTag(); 
	                    smsinfodao.deleteByPn((String) vHollder.pn.getText());
	                    Log.i(TAG, "--onClick --"+vHollder.pn.getText());    
	                }    
	            }
				smsinfodao.close();
				onResume();
			}
		});
        
        Button btnViewSMS =  (Button)findViewById(R.id.btnViewSMS);
        btnViewSMS.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(smsinfodao == null)
					smsinfodao =  new SMSINFODao(SMSWaitList.this);
				for(int i=0;i<list.getCount();i++){    
	                if(ListAdapter.isSelected.get(i)){ 
	                	ViewHolder vHollder = (ViewHolder) list.getChildAt(i).getTag(); 
	                	Cursor info = smsinfodao.select(" and pn ='"+ (String) vHollder.pn.getText() +"'");
	                	info.moveToFirst();	                	
	                	new AlertDialog.Builder(SMSWaitList.this)
	    		        .setTitle(info.getString(1))
	    		        .setMessage(info.getString(2))
	    		        .setIcon(android.R.drawable.ic_dialog_email)
	    		        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int whichButton) {
					        	dialog.cancel();
					        }
					        })
	    		        .show();    
	                    break;
	                }    
	            }
				smsinfodao.close();
			}
		});
	}
}
