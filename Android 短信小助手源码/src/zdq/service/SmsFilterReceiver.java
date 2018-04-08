package zdq.service;

import zdq.data.SqliteDatebase;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.SmsMessage;

public class SmsFilterReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] pdus = (Object[])intent.getExtras().get("pdus");//获取短信内容 
		
		SqliteDatebase sd=new SqliteDatebase(context);
		sd.getpn();

        for(Object pdu : pdus){
            byte[] data = (byte[]) pdu;//获取单条短信内容，短信内容以pdu格式存在 
            SmsMessage message = SmsMessage.createFromPdu(data);//使用pdu格式的短信数据生成短信对象 
            
            String sender = message.getOriginatingAddress();//获取短信的发送者 
            
            Cursor cursor=sd.getpn();
        	cursor.moveToFirst();
            while(!cursor.isAfterLast() && (cursor.getString(1) != null)){   
            	if(sender.equals(cursor.getString(1))){ 
                    	abortBroadcast();            //短信拦截
                	}
            		cursor.moveToNext();
            }
        }
        
	}
}