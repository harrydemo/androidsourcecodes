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
 * �㲥�࣬��������
 * 
 * @author emmet7life@yahoo.cn
 * */
public class BroadCastTool extends BroadcastReceiver {

	String SMScontent_01;
	String SMScontent_02;
	static HashMap<String, PhoneInfo> mPhoneMap = new HashMap<String, PhoneInfo>();
	static HashMap<String, SmsInfo> mSMSMap = new HashMap<String, SmsInfo>();

	public static final String SMS_SYSTEM_ACTION = "android.provider.Telephony.SMS_RECEIVED";// ���ն��ŵ�ACTION��ʶ
	public static final String SYSTEM_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
	public static final String SMS_RECEIVED_ACTION = "com.ldci.t56.mobile.safe.SMS_RECEIVED_ACTION";// ���յ���������ʱ�����㲥��ACTION��ʶ
	public static final String CALL_RECEIVED_ACTION = "com.ldci.t56.mobile.safe.CALL_RECEIVED_ACTION";// ���յ���������ʱ�����㲥��ACTION��ʶ
	public static final String AUTO_START_SERVICE = "com.ldci.t56.mobile.safe.AUTO_START_SERVICE";//����ϵͳ�����Ĺ㲥
	public static String SMS_PHONENUMBER;//���ն��ź���
	public static String SMS_CONTENT;//���ն�������
	private DbAdapter mDbAdapter;//�������ݿ�
	private ITelephony iTelephony;//�Ҷϵ绰��һ������
	private TelephonyManager telephonyMgr;//�绰������
	public SharedPreferences mSharedPreferences;//�洢����������Ĺ�����
	private boolean isReceiveCall;//�Ƿ���յ绰
	private boolean isAutoStartWithPhone;//�Ƿ���ϵͳ����
	private boolean isReceiveSMS;//�Ƿ���ն���
	private String mUndisturbedMode;//ҹ��ģʽ��Ϣ

	public BroadCastTool() {
		SMS_PHONENUMBER = new String();
		SMS_CONTENT = new String();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// ��ȡ�����ļ���Ϣ��ʵʱ��ȡ
		SharedPreferences settings = context.getSharedPreferences("DEMO",Context.MODE_PRIVATE);
		SMScontent_01 = settings.getString("smartE01", "");
		SMScontent_02 = settings.getString("smartE02", "");
		mSharedPreferences = context.getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE);
		isAutoStartWithPhone = mSharedPreferences.getBoolean("isAutoStartWithPhone", false);
		isReceiveCall = mSharedPreferences.getBoolean("isReceiveCall", false);
		isReceiveSMS = mSharedPreferences.getBoolean("isReceiveSMS", false);
		mUndisturbedMode = mSharedPreferences.getString("UndisturbedMode", "�ر�");

		// ���������㲥��ʵ�ֿ����Զ��������
		if (intent.getAction().equals(SYSTEM_BOOT_COMPLETED)) {
			if (isAutoStartWithPhone) {
				Intent mIntent = new Intent(AUTO_START_SERVICE);
				context.startService(mIntent);// ��������
			}
		}
		// �������Ź㲥��ʵ��������������
		if (intent.getAction().equals(SMS_SYSTEM_ACTION)) {
			// 1.���ն��ŵ����ȼ���ߣ�����ǰ���ж�
			if (isReceiveSMS) {
				Toast.makeText(context, "������Ϣ֮���ն��ţ���ѡ", Toast.LENGTH_LONG).show();
				abortBroadcast();// ��ֹ���Ź㲥�������ն����ǹ�ѡ״̬ʱ������һ�ж���
			} else {
				// 2.���ն���δ��ѡ״̬ʱ����Ҫ�ж�ҹ��ģʽ�Ƿ��������ѡ�������ض��Ż����ض��ź͵绰ʱ����Ҫ�ж�ʱ��Σ���������ص�ʱ���������ֹ�㲥
				if (("���ض���".equals(mUndisturbedMode) || "���ض��ź͵绰".equals(mUndisturbedMode)) && isIncludedTime(context)) {
					abortBroadcast();// ��ֹ���Ź㲥�������ն����ǹ�ѡ״̬ʱ������һ�ж���
				} else {
					// 2.1.�����Խ��ն��ŵ�ʱ�����Ƚ������ź�������ݣ������жϺ����Ƿ�
					// Ϊ���ź������еĺ��룬�������ֱ�����Σ����Ѷ��ŷŵ�������������
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

							mMessagePhone.append(currentMessage.getDisplayOriginatingAddress());// ��ȡ�绰����
							mMessageContent.append(currentMessage.getDisplayMessageBody());// ��ȡ��������
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
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��  HH:mm:ss");
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
											info.getAddress() + "��"
													+ info.getDate()
													+ "���ͣ��������£�"
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
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��  HH:mm:ss");
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
									smsmanager.sendTextMessage(SMS_PHONENUMBER, null, info.getPhoneNum() + "��"+ info.getDate()+ "��绰����!", null,null);
									mPhoneMap.remove(keyStr[i].toString());
									info.setState("no");
									mPhoneMap.put(keyStr[i].toString(), info);
								}
							}
						}

						SMS_PHONENUMBER = mMessagePhone.toString();
						SMS_CONTENT = mMessageContent.toString();
						Toast.makeText(context,"<----ԭʼ����---->" + SMS_PHONENUMBER + "\n"
															 + "<----����֮��---->"+ trimSmsNumber("+86", SMS_PHONENUMBER),Toast.LENGTH_LONG).show();

						mDbAdapter = new DbAdapter(context);
						mDbAdapter.open();// ---------------------------------------------------------------------------------------------------------------------���ݿ⣺��
						boolean isContainSensitive = false;
						// 2.2�жϸú����Ƿ��ڶ��ź������У�������������ظö��ţ�������������ݵ���Ϣ�������������ݿ���
						Cursor mCursor = mDbAdapter.getPhone(trimSmsNumber("+86", SMS_PHONENUMBER), 2);
						if (mCursor.moveToFirst()) {
							abortBroadCastAndSaveData(context, 1);// ----------------------------------------------------------��Ϊ�ú����ں������У���������
						} else {// 2.3������ں������У���������Ĺ��������ж϶���������
							mSharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);// ��ȡ�����ļ��е����д���Ϣ
							String xmlInfo = mSharedPreferences.getString("sensitive", "");
							if (xmlInfo.length() != 0) {// �����д����ݲ�Ϊ�յ�ʱ���ж�
								String[] mArray = xmlInfo.substring(0,xmlInfo.length()).split(",");// ò�ƿ��Բ���ȥ���һ������ֱ�Ӳ��
								for (int i = 0; i != mArray.length; i++) {
									if (SMS_CONTENT.contains(mArray[i])) {
										isContainSensitive = true;
										abortBroadCastAndSaveData(context, 2);// ----------------------------------------------��Ϊ�ö������ݺ����дʣ���������
										break;
									}
								}
							}
							if (isContainSensitive == false) {//�ж��Ƿ��Ǹ����龰ģʽ�Ķ������ݣ����������Ӧ�ĸ���
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
						mDbAdapter.close();// ---------------------------------------------------------------------------------------------------------------------���ݿ⣺�ر�
					}
				}
			}
		}
		
		// ��������
		if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
			Log.d("call", "get action");
			telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			MyPhoneStateListener mMPSL = new MyPhoneStateListener(context);
			telephonyMgr.listen(mMPSL, MyPhoneStateListener.LISTEN_CALL_STATE);
			// ���÷����ȡ���ص�endcall����
			try {
				Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);
				getITelephonyMethod.setAccessible(true);
				iTelephony = (ITelephony) getITelephonyMethod.invoke(telephonyMgr, (Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	// ����
	protected void ring(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_OFF);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,AudioManager.VIBRATE_SETTING_OFF);
	}

	// ����
	protected void silent(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_OFF);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,AudioManager.VIBRATE_SETTING_OFF);
	}

	// ��
	protected void vibrate(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_ON);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,AudioManager.VIBRATE_SETTING_ON);
	}
	//�绰״̬������
	class MyPhoneStateListener extends PhoneStateListener {
		
		int i = 0;
		Context mContext;
		AudioManager audioManager;
		TelephonyManager mTM;
		
		public MyPhoneStateListener(Context context) {
			mContext = context;
			mTM = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		}

		/** ��������Ϊ�������Ҷϵ绰 */
		private void audioSilentEndCall() {
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);// ����Ϊ����ģʽ
			try {
				iTelephony.endCall();// �Ҷϵ绰
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// ����״̬
				if (audioManager != null) {
					audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);// ����Ϊ��ͨģʽ
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:// ����״̬
//				ServiceTool.serviceInt++;
				// ��������״̬��ʱ����Ҫ�жϡ������еġ����ص绰����������Ϣ������ǹ�ѡ�ģ���ֱ�Ӿܽӵ����е绰
				if (isReceiveCall == true) {
					audioSilentEndCall();
					Toast.makeText(mContext, "����֮�ܽӵ绰����ѡ", Toast.LENGTH_LONG).show();
				} else {
					if (("���ص绰".equals(mUndisturbedMode) || "���ض��ź͵绰".equals(mUndisturbedMode))&& isIncludedTime(mContext)) {
						audioSilentEndCall();
					} else {
						// �жϸú����Ƿ��ں������У��������Ҷϣ����洢������Ϣ�����ݿ���
						mDbAdapter = new DbAdapter(mContext);
						mDbAdapter.open();// ---------------------------------------------------------------------------------------------------------------------���ݿ⣺��

						if (mDbAdapter.getPhone(trimSmsNumber("+86", incomingNumber), 4).moveToFirst()) {
							audioSilentEndCall();
							// ��������
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
								// ɾ���ֻ��绰��¼	
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
						mDbAdapter.close();// ---------------------------------------------------------------------------------------------------------------------���ݿ⣺�ر�
					}
				}
				break;
			}
		}

	}

	/** �жϵ�ǰʱ���Ƿ���ҹ������ģʽ��ʱ����� */
	private boolean isIncludedTime(Context context) {
		long mStartTime = mSharedPreferences.getLong("UndisturbedStartTime", 0L);
		long mEndTime = mSharedPreferences.getLong("UndisturbedEndTime", 0L);
		long mCurrentTime = System.currentTimeMillis();
		if (mCurrentTime >= mStartTime && mCurrentTime <= mEndTime) {
			return true;
		}
		return false;
	}

	/** ȥ�����Ҵ��ŵķ��� */
	public final static String trimSmsNumber(String prefix, String number) {
		String s = number;
		if (prefix.length() > 0 && number.startsWith(prefix)) {
			s = number.substring(prefix.length());
		}
		return s;
	}

	/** ��ֹ�㲥��������ݵ����������ݿ��� */
	private void abortBroadCastAndSaveData(Context context, int i) {
		BroadCastTool.this.abortBroadcast();// ��ֹ���Ź㲥�����յ���������֮�󣬴��������Ϣ�����ݿ��У�Ȼ����ֹ�㲥
		// ���ݿ����������������������ݵ����ݿ���
		Message_Rubbish_Info mRMI = new Message_Rubbish_Info();
		mRMI.setMessage_rubbish_phone(SMS_PHONENUMBER);// ---------------------------���ź���
		mRMI.setMessage_rubbish_content(SMS_CONTENT);// -----------------------------------��������
		mRMI.setMessage_rubbish_time(GetCurrentTime.getFormateDate());// -----------------�ռ�ʱ��
		mDbAdapter.getAdd(mRMI);// ----------------------------------------------------------------------��������
		// ���ص��������Ż��ߺ���������֮���͹㲥��ˢ�¶���Ϣ�����ؼ�¼ҳ��
		Intent mIntent = new Intent();
		mIntent.setAction(SMS_RECEIVED_ACTION);
		context.sendBroadcast(mIntent);

		if (i == 1) {
			Toast.makeText(context,"�ú����ں������У���������\n\n" + SMS_PHONENUMBER + "\n\n"+ SMS_CONTENT + "\n\n"+ GetCurrentTime.getFormateDate(),Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context,"�ö��ź����дʣ�������\n\n" + SMS_PHONENUMBER + "\n\n" + SMS_CONTENT+ "\n\n" + GetCurrentTime.getFormateDate(),Toast.LENGTH_LONG).show();
		}

	}

}