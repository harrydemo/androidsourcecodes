package cn.com.mythos.android;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import cn.com.mythos.android.Contents.Contents;
import cn.com.mythos.android.Contents.Utils;
import cn.com.mythos.touhoucartoonreader.R;

public class BaseActivity extends Activity {
	protected static Map<String, String> imagePosition;
	protected static LinkedList<String> imageList;
	public static ArrayList<Activity> allActivity = new ArrayList<Activity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		allActivity.add(this);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK == keyCode && event.getRepeatCount() == 0) {
			promptExit(this);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//退出
	public static void exitApp(Context context) {
		for(Activity ac : allActivity) {
			if(ac != null) {
				ac.finish();
			}
		}
		saveReaderState();
//		context.startActivity(LogoutActivity);
		System.exit(0);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveReaderState();
	}
	
	//保存当前阅读的漫画信息
	public static void saveReaderState() {
		try{
			String picPath = Utils.getImagePath(imagePosition, imageList);
			if(picPath != null) {
				Utils.saveFile(Contents.SHOWHISTORY, picPath, false);
			}
		}catch (Exception e) {
			
		}
	}
	
	//退出
	public static void promptExit(final Context context) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context); 
		dialog.setTitle(R.string.logout_title);
		dialog.setMessage(R.string.logout_body);
		dialog.setPositiveButton(R.string.logout_submit, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				exitApp(context);
			}
		});
		dialog.setNegativeButton(R.string.logout_cancel, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	
}
