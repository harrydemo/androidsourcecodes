/**
 * Part of one of Andsen's open source project (a41cv / aSQLiteManager) 
 *
 * @author andsen
 *
 */
package dk.andsen.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Utils {
	private static boolean logging = true;
	
	private static String app = "aSQLMan";
	/**
	 * Write a debug message to the log
	 * @param msg Message
	 */
	public static void logD(String msg) {
		if (logging)
			Log.d(app, msg);
	}
	
	/**
	 * Write an error message to the log
	 * @param msg Message
	 */
	public static void logE(String msg) {
		if (logging)
			Log.e(app, msg);
	}
	
	/**
	 * Show a dialog with an error message
	 * @param e the message
	 * @param cont the programmes content
	 */
	public static void showException(String e, Context cont) {
		AlertDialog alertDialog = new AlertDialog.Builder(cont).create();
		alertDialog.setTitle("Error");
		alertDialog.setMessage(e);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		alertDialog.show();
	}

	public static void showMessage(String title, String msg, Context cont) {
		AlertDialog alertDialog = new AlertDialog.Builder(cont).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		alertDialog.show();
	}

	
	/**
	 * Display the message as a short toast message
	 * @param context
	 * @param msg the message to display
	 */
	public static void toastMsg(Context context, String msg) {
      Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT) ;
      toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
      toast.show();
	}

	/**
	 * Test if a SDCard is available
	 * @return true if a external SDCard is available
	 */
	public static boolean isSDAvailable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

}
