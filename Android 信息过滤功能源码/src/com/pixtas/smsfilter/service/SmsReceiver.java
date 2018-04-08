package com.pixtas.smsfilter.service;

import java.util.ArrayList;
import java.util.List;

import com.pixtas.helper.DatabaseAdapterHelper;
import com.pixtas.smsfilter.Config;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Contacts;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.SlidingDrawer;


public class SmsReceiver extends BroadcastReceiver{
	private static final String TAG = "SmsReceiver";
	
//	private List<String> nameContactsInPhone = new ArrayList<String>();
	private List<String> numberContactsInPhone = new ArrayList<String>();
	
	private Context mContext = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		this.mContext = context;
		String incomingNumber = null;
		if(intent.getAction().equals(Config.SMS_RECEIVE_ACTION)) {
			getContactsInfoListFromPhone(context);
			if(isReceive(context)){
				Bundle bundle = intent.getExtras();
				Object[] myOBJdups = (Object[]) bundle.get("pdus");
				SmsMessage[] smsMessages = new SmsMessage[myOBJdups.length];
				for(int i= 0;i < myOBJdups.length; i ++){
					smsMessages[i] = SmsMessage.createFromPdu((byte[]) myOBJdups[i]);
					incomingNumber = smsMessages[i].getDisplayOriginatingAddress();
				}
				if(Config.debug){
					Log.v(TAG,"sms---->incomingNumber:" + incomingNumber);
				}
				boolean isExist = isExist(incomingNumber);
				if(!isExist){
					if(Config.debug){
						Log.v(TAG,incomingNumber + "-sms------>is not in contacts,change silent");
					} 
				    this.changeSilent();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(2000);
								if(Config.debug){
									Log.v(TAG,"----sms---->change normal");
								}
								changeNormal();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
				}else{
					if(Config.debug){
						Log.v(TAG,incomingNumber + "-sms------>change nomal");
					}
					this.changeNormal();
				}
			}
		}else if(intent.getAction().equals(Config.PHONE_STATE)){
			getContactsInfoListFromPhone(context);
			if(isReceive(context)){
				Bundle bundle = intent.getExtras();
				incomingNumber = bundle.getString("incoming_number"); 
				if(Config.debug){
					Log.v(TAG,"ring--->incomingNumber:" + incomingNumber);
				}
				mPhoneCallListener phoneListener = new mPhoneCallListener(); 
				TelephonyManager telMgr = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
				telMgr.listen(phoneListener, mPhoneCallListener.LISTEN_CALL_STATE); 
			}
		}
	}
	/*来电监听器*/
	private class mPhoneCallListener extends PhoneStateListener 
	{ 
		@Override 
		public void onCallStateChanged(int state, String incomingNumber) 
		{ 
			// TODO Auto-generated method stub 
			switch(state)  
			{  
			/*无任何状态*/ 
			case TelephonyManager.CALL_STATE_IDLE: 
				if(Config.debug){
					Log.v(TAG,"CALL_STATE_IDLE change nomal");
				}
				changeNormal();
				break;

				/*接起电话的状态*/  
			case TelephonyManager.CALL_STATE_OFFHOOK: 
				break;

				/*来电状态*/   
			case TelephonyManager.CALL_STATE_RINGING: 

				/*当来电号码不在联系人*/ 
				boolean isExist = isExist(incomingNumber);
				if(!isExist && isReceive(mContext))
				{    
					if(Config.debug){
						Log.v(TAG,incomingNumber + "-->is not in contacts,change silent");
					}
					changeSilent();    
				}
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}
	
	private boolean isExist(String incomingNumber){
		for(int i = 0; i < numberContactsInPhone.size(); i ++){
			String numberContact = numberContactsInPhone.get(i).replace("-", "");
			if(Config.debug){
				Log.v(TAG, "numberContact-->" + numberContact);  
			}
			int NCLen = numberContact.length();
			int INLen = incomingNumber.length();
			if(NCLen <= INLen){
				String temp = incomingNumber.substring(incomingNumber.length() - numberContact.length(), incomingNumber.length());
				if(numberContact.equals(temp)){
					return true;
				}
			}
		}
		return false;
	}
	private boolean isReceive(Context context){
		DatabaseAdapterHelper databaseAdapterHelper = new DatabaseAdapterHelper(context);
		databaseAdapterHelper.open_sms();
		Cursor cursor = databaseAdapterHelper.fetchAllData();
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			int flag = cursor.getInt(cursor.getColumnIndex("data"));
			if(flag == 1){
				return true;
			}
		}
		cursor.close();
		databaseAdapterHelper.close_sms();
		return false;
	}
	
	private void getContactsInfoListFromPhone(Context context) {  
        /* 取得ContentResolver */  
        ContentResolver content = context.getContentResolver();  
        /* 取得通讯录的Phones表的cursor */  
        Cursor contactcursor = content.query(Contacts.Phones.CONTENT_URI, null,  
                null, null, Contacts.People.DEFAULT_SORT_ORDER);  
        /* 在LogCat里打印所有关于的列名 */  
//        for (int i = 0; i < contactcursor.getColumnCount(); i++) {  
//            String columnName = contactcursor.getColumnName(i);  
//            Log.d(TAG, "column name:" + columnName);  
//        }  
        /* 逐条读取记录信息 */  
        int Num = contactcursor.getCount();  
        String number;  
        for (int i = 0; i < Num; i++) {  
            contactcursor.moveToPosition(i);  
//            String type = contactcursor.getString(contactcursor.getColumnIndexOrThrow(Contacts.Phones.TYPE));  
//            Log.v(TAG, "type=" + type);  
//            String person_id = contactcursor.getString(contactcursor  
//                    .getColumnIndexOrThrow(Contacts.Phones.PERSON_ID));  
//            Log.v(TAG, "person_id=" + person_id);  
//            name = contactcursor.getString(contactcursor  
//                    .getColumnIndexOrThrow(Contacts.Phones.NAME));  
            number = contactcursor.getString(contactcursor  
                    .getColumnIndexOrThrow(Contacts.Phones.NUMBER));  
//          number = number == null ? "无输入电话" : number;// 当找不到电话时显示"无输入电话"  
            
//            nameContactsInPhone.add(name);  
//            Log.v(TAG, "name=" + name);  
            numberContactsInPhone.add(number);
            if(Config.debug){
//            	Log.v(TAG, "*****number=" + number);  
            }
        }  
    } 
	
	/*手机静音*/
	private void changeSilent(){
		try
		{             
			AudioManager audioManager = (AudioManager)
			mContext.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager != null)
			{
				/*设置静音*/ 
				audioManager.setRingerMode(AudioManager.
						RINGER_MODE_SILENT); 
				audioManager.getStreamVolume(
						AudioManager.STREAM_RING);
			}
		}catch(Exception e)
		{ 
			e.printStackTrace();   
		}  
	}
	/*手机铃声正常*/
	private void changeNormal(){
		try
		{ 
			AudioManager audioManager = (AudioManager)
			mContext.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager != null) 
			{ 
				/*设置铃声正常*/ 
				audioManager.setRingerMode(AudioManager.
						RINGER_MODE_NORMAL);               
				audioManager.getStreamVolume(
						AudioManager.STREAM_RING);
			} 
		}         
		catch(Exception e)
		{ 
			e.printStackTrace(); 
		}
	}

}
