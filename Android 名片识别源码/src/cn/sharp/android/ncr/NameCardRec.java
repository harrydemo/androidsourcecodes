package cn.sharp.android.ncr;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * 主运行类
 */


public class NameCardRec extends Activity implements OnClickListener {
	private final static String TAG = "NameCardRec";

	private final static int MENU_PREF_SETTINGS = 0;
	private ImageButton btnReadFromSdcard;
	private ImageButton btnRecFromCamera;
	private ImageButton btnSetting;
	private ImageButton btnContact;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//没有标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		//根据横竖屏不同状态    设置不同的显示界面
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.e(TAG, "landscape");
			//横屏
			setContentView(R.layout.main_l);
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.e(TAG, "portrait");
			//竖屏
			setContentView(R.layout.main);
		}

		//初始化
		btnReadFromSdcard = (ImageButton) findViewById(R.id.btn_read_from_sdcard);
		btnRecFromCamera = (ImageButton) findViewById(R.id.btn_rec_from_camera);
		btnSetting = (ImageButton) findViewById(R.id.btn_setting);
		btnContact = (ImageButton) findViewById(R.id.btn_contact);
		btnSetting.setOnClickListener(this);
		btnReadFromSdcard.setOnClickListener(this);
		btnRecFromCamera.setOnClickListener(this);
		btnContact.setOnClickListener(this);
		
		Log.e(TAG, "leaving onCreate, tid:" + Thread.currentThread().getId());
	}

	//菜单栏
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_PREF_SETTINGS:
			Intent prefIntent = new Intent(this, PrefSettings.class);
			startActivity(prefIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
   //监听
	public void onClick(View v) {
		Log.e("________________", v.getContext().toString());
		if (v == btnReadFromSdcard) {
			//从sdcard识别
			Intent intent = new Intent(this, NamecardImageGallery.class);
			startActivity(intent);
		} else if (v == btnRecFromCamera) {
			//拍照识别
			Intent intent = new Intent(this, StaticRecFromCamera.class);
			Log.e(TAG, "before starting static rec activity");
			startActivity(intent);
			Log.e(TAG, "static rec activity started");
		} else if (v == btnSetting) {
			//设置
			Intent prefIntent = new Intent(this, PrefSettings.class);
			startActivity(prefIntent);
		} else if (v == btnContact) {
			Intent intent = new Intent(Intent.ACTION_VIEW, People.CONTENT_URI);
			startActivity(intent);
		}

	}
}