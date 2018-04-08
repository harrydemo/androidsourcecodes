package xujun.util;

import android.app.AlertDialog;
import android.content.Context;

public class MessageUtil {
	public static void alert(Context context,String title,String message)
	{
		new AlertDialog.Builder(context).setTitle(title).setMessage(message).create().show();
	}
}
