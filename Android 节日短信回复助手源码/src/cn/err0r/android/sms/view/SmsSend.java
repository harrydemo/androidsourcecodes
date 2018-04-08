package cn.err0r.android.sms.view;


import java.util.ArrayList;
import java.util.HashMap;

import cn.err0r.android.sms.R;
import cn.err0r.android.sms.database.SMSINFODao;
import cn.err0r.android.sms.database.SMSINFOModel;
import cn.err0r.android.sms.database.SMSSampleDao;
import cn.err0r.android.sms.database.SMSSampleModel;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SmsSend extends Activity{
	CheckBox samplelist_cBox;
    TextView lblPn;
    TextView lblName;
    ListView list;
    EditText txtContent;
    Button msgsend,sample_delete;
    int listpos;
    
	SMSSampleModel smssample;
    SMSSampleDao smssampledao;
    SMSINFODao smsinfodao;
    
    ArrayList<HashMap<String, String>> mylist;
    ArrayList<String> pns;
    
    
    void getView(){
    	lblPn = (TextView)findViewById(R.id.msgsend_tv3);
    	list = (ListView) findViewById(R.id.msgsend_lv);  //绑定Layout里面的ListView  
    	lblName = (TextView)findViewById(R.id.msgsend_tv2);
    	txtContent = (EditText)findViewById(R.id.msgsend_et);
    	msgsend = (Button)findViewById(R.id.msgsend_btn);
    }
    
    
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msgsend);
		getView();
	    //生成动态数组，并且转载数据  
	    mylist = new ArrayList<HashMap<String, String>>(); 
	    smssample = new SMSSampleModel();
        smssampledao = new SMSSampleDao(this);
        Cursor cursor = smssampledao.select();
        
        while(cursor.moveToNext()){
        	HashMap<String, String> map = new HashMap<String, String>();  
        	map.put("Body", cursor.getString(cursor.getColumnIndex("Body")));
        	mylist.add(map);  
        }
        
        cursor.close();
        smssampledao.close();
        //生成适配器的Item和动态数组对应的元素  
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,mylist,//数据源   
        		R.layout.samplesms,//ListItem的XML实现  
        		//动态数组对应的子项          
        		new String[] {"Body"},   
		        //ImageItem的XML文件里面的TextView ID  
		        new int[] {R.id.samplesms_tv1} 
      );  
       
      //添加并且显示  
      list.setAdapter(listItemAdapter);  
      
      list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				txtContent.setText(mylist.get(arg2).get("Body").toString());
			}
        });

	    
	    Intent i = getIntent();
	    
	    if(i != null){
	    	if(i.getSerializableExtra("SMSINFO") != null){
	    		lblPn.setText(((SMSINFOModel)i.getSerializableExtra("SMSINFO")).get_pn());
	    		lblName.setText(((SMSINFOModel)i.getSerializableExtra("SMSINFO")).get_who());
	    	}
	    	if(i.getSerializableExtra("PNS") != null){
	    		pns = (ArrayList<String>)i.getSerializableExtra("PNS");
	    		lblPn.setText(getResources().getString(R.string.smssend_willsend)+pns.size()+getResources().getString(R.string.smssend_tonfriend));
	    	}
	    }
	    
	    msgsend.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(pns == null)
					SendSms(SmsSend.this,lblPn.getText().toString().trim(), txtContent.getText().toString().trim());
				else{
					for (String iterable_element : pns) {
						SendSms(SmsSend.this,iterable_element, txtContent.getText().toString().trim());
						smsinfodao = new SMSINFODao(SmsSend.this);
						smsinfodao.deleteByPn(iterable_element);
						smsinfodao.close();
					}
				}
				finish();	
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
	}

    public void sendSMS(String pn ,String body){
    		ContentValues values = new ContentValues(); 
    		values.put("address", pn); 
    		values.put("body", body); 
    		getContentResolver().insert(Uri.parse("content://sms/sent"), values); 
    		smsinfodao = new SMSINFODao(SmsSend.this);
    		smsinfodao.deleteByPn(pn);
    		smsinfodao.close();
    		finish();
    }
    
    public Boolean SendSms(Context context,String addre, String mess)
    {
            try
            {
                PendingIntent mPI = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
                SmsManager.getDefault().sendTextMessage(addre, null, mess, mPI,null);
                sendSMS(addre,mess);
                Toast.makeText(SmsSend.this, getResources().getString(R.string.smssend_succeed), Toast.LENGTH_SHORT).show();
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
    }
}
