package com.crackedcarrot.UI;

import android.os.Handler;
import android.os.Looper;
import com.crackedcarrot.Scaler;
import com.crackedcarrot.Sprite;

public class UIHandler extends Thread {

	private Grid g;
	private RangeIndicator range;

	private Handler mHandler;

	public UIHandler(Scaler s) {
		g = new Grid(s);
		range = new RangeIndicator(s);
	}

	public void run() {
		Looper.prepare();

		mHandler = new Handler();

		Looper.loop();

	}

	public void showGrid() {
		// Log.d("HUD","Showing grid.");
		this.mHandler.post(g.getShowRunner());
	}

	public void hideGrid() {
		// Log.d("HUD","Showing grid.");
		this.mHandler.post(g.getHideRunner());

	}

	public void blinkRedGrid() {
		this.mHandler.removeCallbacks(g.getBlinRedRunner());
		this.mHandler.post(g.getBlinRedRunner());
	}

	public void blinkRedRange() {
		this.mHandler.removeCallbacks(range.getBlinkRedRunner());
		this.mHandler.post(range.getBlinkRedRunner());
	}

	public void showRangeIndicator(int towerX, int towerY, int towerRange,
			int width, int height) {
		this.mHandler.removeCallbacks(range.getShowRunner());
		range.scaleSprite(towerX, towerY, towerRange, (int) (width * 1.04),
				(int) (height * 1.04));
		this.mHandler.post(range.getShowRunner());
	}

	public void hideRangeIndicator() {
		this.mHandler.removeCallbacks(range.getHideRunner());
		this.mHandler.post(range.getHideRunner());
	}

	public Sprite[] getOverlayObjectsToRender() {
		Sprite[] rArray = new Sprite[2];
		rArray[0] = g;
		rArray[1] = range;
		return rArray;
	}
}
