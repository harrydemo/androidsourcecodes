package it.telecomitalia.jchat;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 
 * Customized IntentReceiver used by {@link SendSMSActivity} for collecting
 * results after sending SMS (success and errors)
 * 定制IntentReceiver}{@链接SendSMSActivity用于收集发送短信后的结果（成功和错误）
 */
//消息接受
public class SMSIntentReceiver extends BroadcastReceiver
{

	/**
	 * Handler used for posting delayed UI updates, coming from
	 * {@link SendSMSActivity}
	 * 用于张贴延迟UI更新处理程序，来自{@的链接SendSMSActivity}
	 */
	private Handler recvHandler;
	/**
	 * Instance of progress dialog
	 * 实例化进程对话框
	 */
	private ProgressDialog progDlg;
	/**
	 * Current context
	 */
	private Context ctx;

	/**
	 * Instance of {@link SendSMSActivity} 's ImageButton
	 */
	private ImageButton sendBtn;

	private StringBuffer messageBuf;

	/**
	 * Creates a new instance of SMSIntentReceiver
	 * 创建一个新实例的SMSntentReceiver
	 * @param hndl
	 *            handler for posting delayed GUI events
	 */
	public SMSIntentReceiver(Handler hndl)
	{
		recvHandler = hndl;

	}

	/**
	 * Overrides IntentReceiver.onReceiveIntent() to show the necessary UI
	 * notification for handling SMS results (success or errors) and stopping
	 * the progress bar once that all SMSs have been sent
	 * 覆盖IntentReceiver.onReceiveIntent（）
	 * 显示处理短信的结果（成功或错误），
	 * 并停止进度条后，所有的SMSS已被送往必要的UI通知
	 */
	public void onReceive(Context context, Intent intent)
	{

		String action = intent.getAction();
		SendSMSActivity activity = (SendSMSActivity) context;
		sendBtn = (ImageButton) activity.findViewById(R.id.sendsmsBtn);
		progDlg = activity.getProgressDialog();
		ctx = context;

		if (action.equals(SendSMSActivity.SMS_SENT_ACTION))
		{
			messageBuf = new StringBuffer();
			messageBuf.append("Message was delivered successfully");

			recvHandler.postDelayed(new Runnable()
			{

				public void run()
				{
					progDlg.dismiss();
					Toast.makeText(ctx, messageBuf.toString(), 2000).show();
					sendBtn.setEnabled(true);
				}

			}, 3000);

		}

	}
}
