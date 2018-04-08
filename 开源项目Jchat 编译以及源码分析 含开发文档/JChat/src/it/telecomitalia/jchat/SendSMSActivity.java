package it.telecomitalia.jchat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Activity that allows a user to write an SMS message text and to send it to a
 * contact. SMS functionalities are provided by SMSManager class, and a custom
 * IntentReceiver is used to receive all messages.
 * <p>
 * Please note that the emulator for now always report success after sending an
 * SMS
 * 活动允许用户写SMS消息文本，将它发送到一个接触。短信功能提供SmsManager的类，自定义，IntentReceiver用来接收的所有邮件。
 * 请注意，现在的模拟器总是报成功后发送短信
 */

//发送消息界面
public class SendSMSActivity extends Activity
{

	/**
	 * Text body of SMS
	 */
	private EditText smsBody;
	/**
	 * Action that shall be used for SMS correctly sent (It shall be catched by
	 * {@link SMSIntentReceiver})
	 * 正确的行动，应使用短信发送（应}的{@ link SMSIntentReceiver擦肩而过）
	 */
	public static final String SMS_SENT_ACTION = "com.tilab.msn.SMS_SENT";
	/**
	 * Action that shall be used for error in sending SMS (It shall be catched
	 * by {@link SMSIntentReceiver})
	 * 行动，应在发送短信的错误使用（应擦肩而过{@的链接SMSIntentReceiver}）
	 */
	public static final String SMS_ERROR_ACTION = "com.tilab.msn.SMS_ERROR";
	/**
	 * Name of SMS address parameter
	 */
	public static final String SMS_ADDRESS_PARAM = "SMS_ADDRESS_PARAM";

	/**
	 * Name of SMS delivery message parameter
	 */
	public static final String SMS_DELIVERY_MSG_PARAM = "SMS_DELIVERY_MSG_PARAM";
	/**
	 * Name of SMS address list
	 */
	public static final String SMS_ADDRESS_LIST = "SMS_ADDRESS_LIST";
	/**
	 * List of SMS receivers' addresses
	 */
	private List<String> addresses;

	/**
	 * Instance of progress dialog shown after sending SMS
	 */
	private ProgressDialog dlg;

	/**
	 * Handler used to post delayed Toast (for simulating SMS sending)
	 * 用于发布延迟显示处理（模拟短信发送）
	 */
	private Handler handler;
	/**
	 * Customized intent receiver for receiving SMS
	 * 定制的意图接收接收短讯
	 */
	private BroadcastReceiver smsReceiver;

	/**
	 * Overrides Activity.onCreate() for the current activity, It basically
	 * prepares the gui and registers the customized intent receiver providing
	 * our own IntentFilter.
	 * 覆盖Activity.onCreate（当前活动），它基本上准备GUI和注册自定义的意图接收器，提供我们自己的IntentFilter。
	 */
	protected void onCreate(Bundle icicle)
	{
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.sms_dialog);
		handler = new Handler();

		smsReceiver = new SMSIntentReceiver(handler);
		IntentFilter myFilter = new IntentFilter();
		myFilter.addAction(SMS_SENT_ACTION);
		myFilter.addAction(SMS_ERROR_ACTION);

		registerReceiver(smsReceiver, myFilter);

		Intent dataIntent = getIntent();
		addresses = (List<String>) dataIntent.getSerializableExtra(SMS_ADDRESS_LIST);

		EditText title = (EditText) super.findViewById(R.id.addressEdt);
		title.setText(formatAdresses(addresses));

		smsBody = (EditText) super.findViewById(R.id.smsbodyEdt);
		ImageButton sendBtn = (ImageButton) super.findViewById(R.id.sendsmsBtn);

		sendBtn.setOnClickListener(new View.OnClickListener()
		{

			/**
			 * Handles sending of SMS, dividing the message in sub messages,
			 * sending them and showing a progress dialog
			 * 处理发送短信，划分子邮件的消息，将它们发送和显示一个进度对话框
			 */
			public void onClick(View arg0)
			{
				SmsManager smsMgr = SmsManager.getDefault();
				String smsText = smsBody.getText().toString();

				if (smsText.length() > 0)
				{

					ImageButton btn = (ImageButton) arg0;
					btn.setEnabled(false);
					ArrayList<String> messages = smsMgr.divideMessage(smsText);
					StringBuffer buffer = new StringBuffer();

					if (messages.size() > 1)
					{
						buffer.append("SMS is too long! A total of ");
						buffer.append(messages.size());
						buffer.append(" messages will be sent!");
					}

					if (buffer.length() > 0)
					{
						Toast.makeText(SendSMSActivity.this, buffer.toString(), 1000).show();
					}

					dlg = new ProgressDialog(SendSMSActivity.this);
					dlg.setIndeterminate(true);
					dlg.setCancelable(false);
					dlg.setMessage("Sending  " + messages.size() + " messages to " + SendSMSActivity.this.addresses.size() + " contacts...");

					SendSMSActivity.this.handler.postDelayed(new Runnable()
					{

						public void run()
						{
							// TODO Auto-generated method stub
							dlg.show();
						}

					}, 2000);

					for (String address : SendSMSActivity.this.addresses)
					{

						ArrayList<PendingIntent> listOfIntents = new ArrayList<PendingIntent>();

						for (int i = 0; i < messages.size(); i++)
						{
							Intent sentIntent = new Intent(SendSMSActivity.SMS_SENT_ACTION);
							sentIntent.putExtra(SMS_ADDRESS_PARAM, address);
							sentIntent.putExtra(SMS_DELIVERY_MSG_PARAM, (messages.size() > 1) ? "Part " + i + " of SMS " : "SMS ");
							PendingIntent pi = PendingIntent.getBroadcast(SendSMSActivity.this, 0, sentIntent, PendingIntent.FLAG_CANCEL_CURRENT);
							listOfIntents.add(pi);
						}

						smsMgr.sendMultipartTextMessage(address, null, messages, listOfIntents, null);
					}
				} 
				else
				{
					Toast.makeText(SendSMSActivity.this, SendSMSActivity.this.getText(R.string.error_sms_empty), 3000).show();
				}

			}

		});
	}

	/**
	 * Unregisters the customized SMS Intent receiver
	 * 注销定制的短信意向书接收
	 */
	protected void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(smsReceiver);
	}

	/**
	 * Prepares a string chaining together all the phone numbers (SMS addresses)
	 * using ";" as a separator
	 * 准备一个字符串串联起来所有的电话号码（SMS地址）使用“;”作为分隔符
	 * @param addresses
	 *            for all contacts
	 * @return the string containing all phone numbers chained together
	 * 返回的字符串，其中包含链接所有电话号码
	 */
	private String formatAdresses(List<String> addresses)
	{
		StringBuffer buffer = new StringBuffer();

		int numOfAddr = addresses.size();

		for (int i = 0; i < numOfAddr - 1; i++)
		{
			buffer.append(addresses.get(i));
			buffer.append("; ");
		}

		buffer.append(addresses.get(numOfAddr - 1));

		return buffer.toString();
	}

	/**
	 * Returns the progress dialog instance
	 * 
	 * @return the progress dialog
	 */
	public ProgressDialog getProgressDialog()
	{
		return dlg;
	}

}
