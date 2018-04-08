/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

public class CallAction  implements VoiceActionListener{
	private String mPerson;
    private String number;
    MainActivity mActivity;
  
    Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				mPerson=(String)msg.obj;
				makeCall();
	         break;			
			}
			super.handleMessage(msg);
		}
	};
	
	public CallAction(String person,MainActivity activity)
	  {
	    mPerson = person;
	    mActivity=activity;
	  }
	public void makeCall()
	  {
	    if ((mPerson == null) || (mPerson.equals("")))
	    {
	      
	    }else{
	    	 mPerson=mPerson.trim();
	    	 number=null; 
	    	 if(isPhoneNumber(mPerson)){
	    		 number=mPerson;
	    	 }
	    	 else
	    		 number=getNumberByName(mPerson,mActivity);
	    	 if(number == null)
	         {
	           mPerson = null;
	           VoiceFunction fun=new VoiceFunction(mActivity,null);
	           fun.getVoiceResponse("��ѯ����  ��Ҫ���˭��","contact", handler);
	         }else{	    		    		 
	    		 new VoiceDialog(mActivity,"����Ҫ����"+mPerson+"\n����Ϊ"+number,rightFn,wrongFn,null);	    		 
	        }
	    }
	  }
	
	private boolean isPhoneNumber(String num)
	{
		//return num.matches("\\d+$");
		return num.matches("^\\d+\\D?$");
	}

	@Override
	public void onVoiceResult(String result) {
		// TODO Auto-generated method stub
		
	}

	
	FunctionPointer rightFn=new FunctionPointer(){

		@Override
		public void callback() {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + number));        	         
	        mActivity.startActivity(intent);
		}
		
	};
	
	FunctionPointer wrongFn=new FunctionPointer(){
		@Override
		public void callback() {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	 private  String getNumberByName(String name, Context context)
	  {
		 Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, name);
		  ContentResolver  resolver  = context.getContentResolver();
		  Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Contacts._ID}, null, null, null);  
		  if((cursor!=null)&&(cursor.moveToFirst())){
			  int idCoulmn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			  long id = cursor.getLong(idCoulmn);
		      cursor.close();
		      cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  new String[]{"data1"}, "contact_id = ?",  new String[]{Long.toString(id)}, null);
		      if ((cursor != null) && (cursor.moveToFirst()))
		      {
		        int m = cursor.getColumnIndex("data1");
		        String num = cursor.getString(m);
		        cursor.close();
		       return num;
		      }	      
		  }
		  return null;
	  }
}
