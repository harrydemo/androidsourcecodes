package com.ldci.t56.mobile.tool;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.ldci.t56.mobile.db.DbAdapter;
import com.ldci.t56.mobile.info.Call_Record_Info;
import com.ldci.t56.mobile.info.Message_Rubbish_Info;
import com.ldci.t56.mobile.info.PhoneInfo;
import com.ldci.t56.mobile.info.SmsInfo;

/**
 * 广播类，监听短信
 * 
 * @author emmet7life@yahoo.cn
 * */
public class BroadCastTool extends BroadcastReceiver {

	String SMScontent_01;
	String SMScontent_02;
	static HashMap<String, PhoneInfo> mPhoneMap = new HashMap<String, PhoneInfo>();
	static HashMap<String, SmsInfo> mSMSMap = new HashMap<String, SmsInfo>();

	public static final String SMS_SYSTEM_ACTION = "android.provider.Telephony.SMS_RECEIVED";// 接收短信的ACTION标识
	public static final String SYSTEM_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
	public static final String SMS_RECEIVED_ACTION = "com.ldci.t56.mobile.safe.SMS_RECEIVED_ACTION";// 当收到垃圾短信时发出广播的ACTION标识
	public static final String CALL_RECEIVED_ACTION = "com.ldci.t56.mobile.safe.CALL_RECEIVED_ACTION";// 当收到垃圾短信时发出广播的ACTION标识
	public static final String AUTO_START_SERVICE = "com.ldci.t56.mobile.safe.AUTO_START_SERVICE";//接收系统启动的广播
	public static String SMS_PHONENUMBER;//接收短信号码
	public static String SMS_CONTENT;//接收短信内容
	private DbAdapter mDbAdapter;//操作数据库
	private ITelephony iTelephony;//挂断电话的一个对象
	private TelephonyManager telephonyMgr;//电话管理类
	public SharedPreferences mSharedPreferences;//存储基本数据类的共享类
	private boolean isReceiveCall;//是否接收电话
	private boolean isAutoStartWithPhone;//是否随系统启动
	private boolean isReceiveSMS;//是否接收短信
	private String mUndisturbedMode;//夜间模式信息

	public BroadCastTool() {
		SMS_PHONENUMBER = new String();
		SMS_CONTENT = new String();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// 读取配置文件信息：实时读取
		SharedPreferences settings = context.getSharedPreferences("DEMO",Context.MODE_PRIVATE);
		SMScontent_01 = settings.getString("smartE01", "");
		SMScontent_02 = settings.getString("smartE02", "");
		mSharedPreferences = context.getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE);
		isAutoStartWithPhone = mSharedPreferences.getBoolean("isAutoStartWithPhone", false);
		isReceiveCall = mSharedPreferences.getBoolean("isReceiveCall", false);
		isReceiveSMS = mSharedPreferences.getBoolean("isReceiveSMS", false);
		mUndisturbedMode = mSharedPreferences.getString("UndisturbedMode", "关闭");

		// 监听开机广播，实现开机自动启动软件
		if (intent.getAction().equals(SYSTEM_BOOT_COMPLETED)) {
			if (isAutoStartWithPhone) {
				Intent mIntent = new Intent(AUTO_START_SERVICE);
				context.startService(mIntent);// 启动服务
			}
		}
		// 监听短信广播，实现拦截垃圾短信
		if (intent.getAction().equals(SMS_SYSTEM_ACTION)) {
			// 1.拒收短信的优先级最高，在最前面判断
			if (isReceiveSMS) {
				Toast.makeText(context, "设置信息之拒收短信：勾选", Toast.LENGTH_LONG).show();
				abortBroadcast();// 中止短信广播：当拒收短信是勾选状态时，拒收一切短信
			} else {
				// 2.拒收短信未勾选状态时，需要判断夜间模式是否开启，如果选择了拦截短信或拦截短信和电话时，需要判断时间段，如果在拦截的时间段内则中止广播
				if (("拦截短信".equals(mUndisturbedMode) || "拦截短信和电话".equals(mUndisturbedMode)) && isIncludedTime(context)) {
					abortBroadcast();// 中止短信广播：当拒收短信是勾选状态时，拒收一切短信
				} else {
					// 2.1.当可以接收短信的时候，首先解析短信号码和内容，接着判断号码是否
					// 为短信黑名单中的号码，如果是则直接屏蔽，并把短信放到短信垃圾箱中
					StringBuilder mMessagePhone = new StringBuilder();
					StringBuilder mMessageContent = new StringBuilder();
					Bundle mBundle = intent.getExtras();
					if (null != mBundle) {
						Object[] mObject = (Object[]) mBundle.get("pdus");
						SmsMessage[] mMessage = new SmsMessage[mObject.length];
						for (int i = 0; i < mObject.length; i++) {
							mMessage[i] = SmsMessage.createFromPdu((byte[]) mObject[i]);
						}
						for (SmsMessage currentMessage : mMessage) {

							mMessagePhone.append(currentMessage.getDisplayOriginatingAddress());// 读取电话号码
							mMessageContent.append(currentMessage.getDisplayMessageBody());// 读取短信内容
						}
						SMS_PHONENUMBER = mMessagePhone.toString();
						SMS_CONTENT = mMessageContent.toString();
						SmsManager smsmanager = SmsManager.getDefault();
						if (SMS_CONTENT.equals(SMScontent_01)) {

							Uri uriSMS = Uri.parse("content://sms/inbox");
							Cursor cursor = context.getContentResolver().query(uriSMS, null, "read=0", null, null);

							while (cursor.moveToNext()) {
								SmsInfo info = new SmsInfo();
								String smsno = cursor.getString(cursor.getColumnIndex("_id"));
								String address = cursor.getString(cursor.getColumnIndex("address"));
								info.setAddress(address);
								Calendar cal = Calendar.getInstance();
								long date = cursor.getLong(cursor.getColumnIndex("date"));
								cal.setTimeInMillis(date);
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM年dd日  HH:mm:ss");
								String dateStr = sdf.format(cal.getTime());
								info.setDate(dateStr);
								String body = cursor.getString(cursor.getColumnIndex("body"));
								info.setBody(body);
								if ((!mSMSMap.containsKey(smsno))&& (!body.equals(SMScontent_01))&& (!body.equals(SMScontent_02))) {
									info.setState("yes");
									mSMSMap.put(smsno, info);
								}
							}
							Set<String> key_1 = mSMSMap.keySet();
							Object[] keyStr_1 = key_1.toArray();
							for (int i = 0; i < keyStr_1.length; i++) {
								SmsInfo info = mSMSMap.get(keyStr_1[i].toString());
								if (info.getState().equals("yes")) {

									smsmanager.sendTextMessage(
											SMS_PHONENUMBER,
											null,
											info.getAddress() + "于"
													+ info.getDate()
													+ "发送，内容如下："
													+ info.getBody(), null,
											null);
									info.setState("no");
									mSMSMap.remove(keyStr_1[i].toString());
									mSMSMap.put(keyStr_1[i].toString(), info);
								}
							}
						}

						if (SMS_CONTENT.equals(SMScontent_02)) {
							StringBuilder builder = new StringBuilder();
							builder.append("type = 3 and new = 1");

							Cursor csor = context.getContentResolver().query(Calls.CONTENT_URI, null,builder.toString(), null, null);
							while (csor.moveToNext()) {
								PhoneInfo info = new PhoneInfo();
								String smsno = csor.getString(csor.getColumnIndex("_id"));
								String number = csor.getString(csor.getColumnIndex("number"));
								info.setPhoneNum(number);
								Calendar cal = Calendar.getInstance();
								long date = csor.getLong(csor.getColumnIndex("date"));
								cal.setTimeInMillis(date);
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM年dd日  HH:mm:ss");
								String dateStr = sdf.format(cal.getTime());
								info.setDate(dateStr);

								if (!mPhoneMap.containsKey(smsno)) {
									info.setState("yes");
									mPhoneMap.put(smsno, info);
								}
							}
							Set<String> key = mPhoneMap.keySet();
							Object[] keyStr = key.toArray();
							for (int i = 0; i < keyStr.length; i++) {

								PhoneInfo info = mPhoneMap.get(keyStr[i].toString());
								if (info.getState().equals("yes")) {
									smsmanager.sendTextMessage(SMS_PHONENUMBER, null, info.getPhoneNum() + "于"+ info.getDate()+ "打电话给您!", null,null);
									mPhoneMap.remove(keyStr[i].toString());
									info.setState("no");
									mPhoneMap.put(keyStr[i].toString(), info);
								}
							}
						}

						SMS_PHONENUMBER = mMessagePhone.toString();
						SMS_CONTENT = mMessageContent.toString();
						Toast.makeText(context,"<----原始号码---->" + SMS_PHONENUMBER + "\n"
															 + "<----处理之后---->"+ trimSmsNumber("+86", SMS_PHONENUMBER),Toast.LENGTH_LONG).show();

						mDbAdapter = new DbAdapter(context);
						mDbAdapter.open();// ---------------------------------------------------------------------------------------------------------------------数据库：打开
						boolean isContainSensitive = false;
						// 2.2判断该号码是否在短信黑名单中，如果存在则拦截该短信，并保存短信内容等信息到垃圾短信数据库中
						Cursor mCursor = mDbAdapter.getPhone(trimSmsNumber("+86", SMS_PHONENUMBER), 2);
						if (mCursor.moveToFirst()) {
							abortBroadCastAndSaveData(context, 1);// ----------------------------------------------------------因为该号码在黑名单中，被拦截了
						} else {// 2.3如果不在黑名单中，则接下来的工作就是判断短信内容了
							mSharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);// 读取配置文件中的敏感词信息
							String xmlInfo = mSharedPreferences.getString("sensitive", "");
							if (xmlInfo.length() != 0) {// 当敏感词数据不为空的时候判断
								String[] mArray = xmlInfo.substring(0,xmlInfo.length()).split(",");// 貌似可以不用去最后一个逗号直接拆分
								for (int i = 0; i != mArray.length; i++) {
									if (SMS_CONTENT.contains(mArray[i])) {
										isContainSensitive = true;
										abortBroadCastAndSaveData(context, 2);// ----------------------------------------------因为该短信内容含敏感词，被拦截了
										break;
									}
								}
							}
							if (isContainSensitive == false) {//判断是否是更改情景模式的短信内容，如果是做相应的更改
								mSharedPreferences = context.getSharedPreferences("QJMO",Context.MODE_PRIVATE);
								String mQJ_MS = mSharedPreferences.getString("mQJ_MS", "");
								String mQJ_JY = mSharedPreferences.getString("mQJ_JY", "");
								String mQJ_ZD = mSharedPreferences.getString("mQJ_ZD", "");
								String mQJ_XL = mSharedPreferences.getString("mQJ_XL", "");
								AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
								if (SMS_CONTENT.equals(mQJ_MS + "+" + mQJ_JY)) {
									silent(audio);
								} else if (SMS_CONTENT.equals(mQJ_MS + "+"+ mQJ_ZD)) {
									vibrate(audio);
								} else if (SMS_CONTENT.equals(mQJ_MS + "+"+ mQJ_XL)) {
									ring(audio);
								}
							}

						}
						mDbAdapter.close();// ---------------------------------------------------------------------------------------------------------------------数据库：关闭
					}
				}
			}
		}
		
		// 监听来电
		if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
			Log.d("call", "get action");
			telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			MyPhoneStateListener mMPSL = new MyPhoneStateListener(context);
			telephonyMgr.listen(mMPSL, MyPhoneStateListener.LISTEN_CALL_STATE);
			// 利用反射获取隐藏的endcall方法
			try {
				Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);
				getITelephonyMethod.setAccessible(true);
				iTelephony = (ITelephony) getITelephonyMethod.invoke(telephonyMgr, (Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	// 铃声
	protected void ring(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_OFF);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,AudioManager.VIBRATE_SETTING_OFF);
	}

	// 静音
	protected void silent(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_OFF);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,AudioManager.VIBRATE_SETTING_OFF);
	}

	// 震动
	protected void vibrate(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_ON);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,AudioManager.VIBRATE_SETTING_ON);
	}
	//电话状态监听类
	class MyPhoneStateListener extends PhoneStateListener {
		
		int i = 0;
		Context mContext;
		AudioManager audioManager;
		TelephonyManager mTM;
		
		public MyPhoneStateListener(Context context) {
			mContext = context;
			mTM = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		}

		/** 设置铃声为静音并挂断电话 */
		private void audioSilentEndCall() {
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);// 设置为静音模式
			try {
				iTelephony.endCall();// 挂断电话
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 待机状态
				if (audioManager != null) {
					audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);// 设置为普通模式
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:// 来电状态
//				ServiceTool.serviceInt++;
				// 当是来电状态的时候需要判断“设置中的”拦截电话“的配置信息，如果是勾选的，则直接拒接掉所有电话
				if (isReceiveCall == true) {
					audioSilentEndCall();
					Toast.makeText(mContext, "设置之拒接电话：勾选", Toast.LENGTH_LONG).show();
				} else {
					if (("拦截电话".equals(mUndisturbedMode) || "拦截短信和电话".equals(mUndisturbedMode))&& isIncludedTime(mContext)) {
						audioSilentEndCall();
					} else {
						// 判断该号码是否在黑名单中，如果是则挂断，并存储来电信息到数据库中
						mDbAdapter = new DbAdapter(mContext);
						mDbAdapter.open();// ---------------------------------------------------------------------------------------------------------------------数据库：打开

						if (mDbAdapter.getPhone(trimSmsNumber("+86", incomingNumber), 4).moveToFirst()) {
							audioSilentEndCall();
							// 保存数据
							if (!mDbAdapter.getTime(DbAdapter.CALL_RECORD_TABLE_NAME,DbAdapter.CALL_RECORD_TIME,GetCurrentTime.getFormateDate()).moveToFirst()) {
								Call_Record_Info mCRI = new Call_Record_Info();
								mCRI.setCall_record_time(GetCurrentTime.getFormateDate());
								mCRI.setCall_record_phone(trimSmsNumber("+86",incomingNumber));
								mDbAdapter.getAdd(mCRI);
								Intent mIntent = new Intent();
								mIntent.setAction(CALL_RECEIVED_ACTION);
								mContext.sendBroadcast(mIntent);
							}
//							if(ServiceTool.serviceInt == 0){
//								Call_Record_Info mCRI = new Call_Record_Info();
//								mCRI.setCall_record_time(GetCurrentTime.getFormateDate());
//								mCRI.setCall_record_phone(trimSmsNumber("+86",incomingNumber));
//								mDbAdapter.getAdd(mCRI);
//								Intent mIntent = new Intent();
//								mIntent.setAction(CALL_RECEIVED_ACTION);
//								mContext.sendBroadcast(mIntent);
//								Log.d("call", "into databases++++++++++++++++++++++++++++++++++++Start");
								// 删除手机电话记录	
								int mCount1 = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER).getCount();		
								boolean mDeleteCallLog = true;								
								while(mDeleteCallLog){
									int mCount2 = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER).getCount();		
									if(mCount2 != mCount1){ 
										mDeleteCallLog = false;												
										int mId = 0;
										Cursor mCursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, "number"+"=?", new String[]{incomingNumber}, CallLog.Calls.DEFAULT_SORT_ORDER);											
										if(mCursor.moveToNext()){										
											mId = mCursor.getInt(mCursor.getColumnIndex("_id"));														
										}								
										mId = mCursor.getInt(mCursor.getColumnIndex("_id"));
										mContext.getContentResolver().delete(CallLog.Calls.CONTENT_URI, "_id"+"=?", new String[]{String.valueOf(mId)});
										mCursor.close();
									}
								}
//								Log.d("call", "into databases++++++++++++++++++++++++++++++++++++End");
//							}
						}
						mDbAdapter.close();// ---------------------------------------------------------------------------------------------------------------------数据库：关闭
					}
				}
				break;
			}
		}

	}

	/** 判断当前时间是否在夜间免扰模式的时间段内 */
	private boolean isIncludedTime(Context context) {
		long mStartTime = mSharedPreferences.getLong("UndisturbedStartTime", 0L);
		long mEndTime = mSharedPreferences.getLong("UndisturbedEndTime", 0L);
		long mCurrentTime = System.currentTimeMillis();
		if (mCurrentTime >= mStartTime && mCurrentTime <= mEndTime) {
			return true;
		}
		return false;
	}

	/** 去掉国家代号的方法 */
	public final static String trimSmsNumber(String prefix, String number) {
		String s = number;
		if (prefix.length() > 0 && number.startsWith(prefix)) {
			s = number.substring(prefix.length());
		}
		return s;
	}

	/** 中止广播并存放数据到垃圾箱数据库中 */
	private void abortBroadCastAndSaveData(Context context, int i) {
		BroadCastTool.this.abortBroadcast();// 中止短信广播：当收到垃圾短信之后，存放垃圾信息到数据库中，然后中止广播
		// 数据库操作：插入该垃圾短信数据到数据库中
		Message_Rubbish_Info mRMI = new Message_Rubbish_Info();
		mRMI.setMessage_rubbish_phone(SMS_PHONENUMBER);// ---------------------------短信号码
		mRMI.setMessage_rubbish_content(SMS_CONTENT);// -----------------------------------短信内容
		mRMI.setMessage_rubbish_time(GetCurrentTime.getFormateDate());// -----------------收件时间
		mDbAdapter.getAdd(mRMI);// ----------------------------------------------------------------------插入数据
		// 拦截到垃圾短信或者黑名单短信之后发送广播，刷新短信息的拦截记录页面
		Intent mIntent = new Intent();
		mIntent.setAction(SMS_RECEIVED_ACTION);
		context.sendBroadcast(mIntent);

		if (i == 1) {
			Toast.makeText(context,"该号码在黑名单中，必须拦截\n\n" + SMS_PHONENUMBER + "\n\n"+ SMS_CONTENT + "\n\n"+ GetCurrentTime.getFormateDate(),Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context,"该短信含敏感词，杯具了\n\n" + SMS_PHONENUMBER + "\n\n" + SMS_CONTENT+ "\n\n" + GetCurrentTime.getFormateDate(),Toast.LENGTH_LONG).show();
		}

	}

}