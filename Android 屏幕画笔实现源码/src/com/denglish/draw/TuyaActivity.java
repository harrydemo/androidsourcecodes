package com.denglish.draw;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

/**
 * 
 * @category: Viewʵ��Ϳѻ�������Լ���������
 * @author лΰ
 * @date: 2012-11-9
 * 
 */
public class TuyaActivity extends Activity {

	private TuyaView tuyaView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		tuyaView = new TuyaView(this, dm.widthPixels, dm.heightPixels);
		setContentView(tuyaView);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// ���ؼ�
			tuyaView.undo();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}