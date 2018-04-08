package com.gw.android.boom;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.gw.android.boom.game.MainGame;
/**
 * 炸弹人
 * @author 原作者：来自eoe的377139882
 * @mender 修改者：potter
 *
 */
public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainGame mainGame = new MainGame(this);
		setScreenOrientation(true);
		setContentView(mainGame);
	}
	
	/**
	 * 设置全屏
	 * @param b
	 */
	private void setScreenOrientation(boolean b) {
		if (b) {
			this
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 自动横屏
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}
}