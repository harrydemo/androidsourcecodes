package com.crackedcarrot.UI;

import android.os.SystemClock;

import com.crackedcarrot.Scaler;
import com.crackedcarrot.Sprite;
import com.crackedcarrot.menu.R;

public class Grid extends Sprite {

	private long startTime;
	private long currentTime;
	private long lastUpdateTime;

	private Show showRunner;
	private Hide hideRunner;
	private BlinkRed blinkR;

	public Grid(Scaler s) {
		// The grid only has one subtype, and one frame. Magical constants for
		// the win.
		super(R.drawable.grid, OVERLAY, 0);
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.setWidth(s.getScreenResolutionX());
		this.setHeight(s.getScreenResolutionY());
		this.draw = false;
		this.opacity = 0.0f;

		showRunner = new Show();
		hideRunner = new Hide();
		blinkR = new BlinkRed();
	}

	public Show getShowRunner() {
		return showRunner;
	}

	public Hide getHideRunner() {
		return hideRunner;
	}

	public BlinkRed getBlinRedRunner() {
		return blinkR;
	}

	private class Show implements Runnable {
		// @Override
		public void run() {
			if (draw == true)
				return;

			opacity = 0.0f;
			draw = true;
			startTime = SystemClock.uptimeMillis();
			currentTime = SystemClock.uptimeMillis();
			lastUpdateTime = currentTime;
			while ((currentTime - startTime) < 500) {
				if ((currentTime - lastUpdateTime) > 100) {
					opacity += 0.1f;
					lastUpdateTime = currentTime;
				}
				SystemClock.sleep(10);
				currentTime = SystemClock.uptimeMillis();
			}
			opacity = 0.5f;
		}
	};

	private class Hide implements Runnable {
		// @Override
		public void run() {
			if (draw == false)
				return;

			opacity = 0.5f;
			startTime = SystemClock.uptimeMillis();
			currentTime = SystemClock.uptimeMillis();
			lastUpdateTime = currentTime;
			while ((currentTime - startTime) < 500) {
				if ((currentTime - lastUpdateTime) > 100) {
					opacity -= 0.1f;
					lastUpdateTime = currentTime;
				}
				SystemClock.sleep(10);
				currentTime = SystemClock.uptimeMillis();
			}
			opacity = 0.0f;
			draw = false;
		}
	};

	private class BlinkRed implements Runnable {
		// @Override
		public void run() {
			if (draw == false)
				return;
			g = 1.0f;
			b = 1.0f;

			currentTime = SystemClock.uptimeMillis();
			lastUpdateTime = 400;

			while (lastUpdateTime > 0) {
				startTime = SystemClock.uptimeMillis();
				lastUpdateTime -= startTime - currentTime;
				currentTime = SystemClock.uptimeMillis();

				if (lastUpdateTime > 200) {
					g = (((lastUpdateTime / 2) - 100) / 100);
					b = (((lastUpdateTime / 2) - 100) / 100);
				} else {
					g = 1 - (((lastUpdateTime / 2) - 100) / 100);
					b = 1 - (((lastUpdateTime / 2) - 100) / 100);
				}
				SystemClock.sleep(10);
			}

			g = 1.0f;
			b = 1.0f;
		}
	};

}
