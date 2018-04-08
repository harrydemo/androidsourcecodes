package cn.err0r.android.sms.recevier;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.err0r.android.sms.ReceiverType;
import cn.err0r.android.sms.database.SMSINFODao;
import cn.err0r.android.sms.database.SMSINFOModel;
import cn.err0r.android.sms.view.SMSToast;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;


public class SmsReceiver extends BroadcastReceiver{
	SMSINFODao smsdao;
	SMSINFOModel smsinfo;
	ReceiverType type;
	
	public SmsReceiver(){
//		Log.i("TAG","SmsRecevier is Create!");
	}
	
	public SmsReceiver(ReceiverType type){
		this.type = type;
	}
	
	public void setReceiverType(ReceiverType type){
		this.type = type;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		// TODO Auto-generated method stub
//		Log.i("TAG","SmsRecevier is onReceive!");
		
		String smsbody;
		String smspn;
		String smsdate;		
		
		smsinfo = new SMSINFOModel();	
		if(smsdao == null)
			smsdao = new SMSINFODao(context);	
		smsbody=GetSmsBody(intent);
		if(smsbody!=null){
			smspn=GetSmsPn(intent);
			smsdate=GetSmsDate(intent);
			
			smsinfo.set_body(smsbody);
	    	smsinfo.set_pn(smspn);
	    	smsinfo.set_who(GetContact(smspn, context));
	    	smsinfo.set_getdate(smsdate);
	    	Intent mBootIntent = new Intent(context, SMSToast.class);
	    	Bundle mBundle;
	    	switch (this.type) {
			case Simple:
//				Log.i("TAG", "快速回复模式");
				mBootIntent = new Intent(context, SMSToast.class); 
		        mBootIntent.setAction("android.intent.action.MAIN"); 
		        mBootIntent.addCategory("android.intent.category.LAUNCHER");  
		        mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		        mBootIntent.putExtra("Type", 2);
		        mBundle = new Bundle();  
		        mBundle.putSerializable("SMSINFO",smsinfo);  
		        mBootIntent.putExtras(mBundle); 	        
		        context.startActivity(mBootIntent); 
				break;
			case Quiet:
//				Log.i("TAG", "后台托管模式");
				smsdao.insert(smsinfo);
				smsdao.close();
				break;
			
			default:
//				Log.i("TAG", "标准模式");
				mBootIntent = new Intent(context, SMSToast.class); 
		        mBootIntent.setAction("android.intent.action.MAIN"); 
		        mBootIntent.addCategory("android.intent.category.LAUNCHER");  
		        mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		        mBootIntent.putExtra("Type", 1);
		        mBundle = new Bundle();  
		        mBundle.putSerializable("SMSINFO",smsinfo);  
		        mBootIntent.putExtras(mBundle); 	        
		        context.startActivity(mBootIntent); 
				break;
			}
	    	
	    	
//	    	smsdao.insert(smsinfo);
	    	
//	    	Cursor cursor=smsdao.select();
//	    	cursor.moveToNext();
//	    	Log.i("inserted:succec" , cursor.getString(cursor.getColumnIndex("SID")) );
//	    	cursor.close();
//	    	smsdao.close();
//	    	
//	        //阻止广播继续传递，如果该receiver比系统的级别高，
//	        //那么系统就不会收到短信通知了
//	        abortBroadcast();
		}
	}
	
    /**
     * 获得短信时间
     * */
	public String GetSmsDate(Intent intent){
		Bundle bundle = intent.getExtras();
        Object pdus[] = (Object[]) bundle.get("pdus");
        SmsMessage msg = SmsMessage.createFromPdu((byte[])pdus[0]);
		Date date = new Date(msg.getTimestampMillis());
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String smsdate =format.format(date).toString();
		return smsdate;
	}
	
    /**
     * 获得短信内容
     * */
    public static String GetSmsBody(Intent intent){
    	String tempString = "";
        Bundle bundle = intent.getExtras();
        Object pdus[] = (Object[]) bundle.get("pdus");
		if(pdus != null && pdus.length > 0){
			SmsMessage[] messages = new SmsMessage[pdus.length];
			for (int i = 0; i < messages.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				tempString += messages[i].getDisplayMessageBody();
				}
			if(CheckSms(tempString)){
				Log.i("smsBody", tempString);
				return  tempString;	
			}
		}
		return null;
    }

	
	//联系人信息
    private static final String[] PHONES_PROJECTION = new String[] {
    	Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.NUMBER};
    
    /**
     * 获得短信联系人
     * */
    public static String GetContact(String phoneNum,Context context){
    	String contactName = "";
    	Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
                PHONES_PROJECTION, //返回字段
                null, // 
                null, // WHERE clause value substitution
                null); 
    	while(cursor.moveToNext()){
    		Log.i(cursor.getString(0),cursor.getString(1));
    		if(phoneNum.contains(cursor.getString(1).replace("-", ""))){
    			contactName = cursor.getString(0);
    			break;
    		}
    	}
        return contactName;
    }

    /**
     * 获得短信号码
     * */
    public String GetSmsPn(Intent intent){
        Bundle bundle = intent.getExtras();
        Object pdus[] = (Object[]) bundle.get("pdus");
        return SmsMessage.createFromPdu((byte[]) pdus[0]).getDisplayOriginatingAddress();
    }

	public static boolean CheckSms(String sms){
		return true;
	}
	
    /**
     * 发送短信
     **/
    public static Boolean SendSms(Context context,String addre, String mess)
    {
            try
            {
                PendingIntent mPI = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
                SmsManager.getDefault().sendTextMessage(addre, null, mess, mPI,null);
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
    }
	
}
