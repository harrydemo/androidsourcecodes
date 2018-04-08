package com.crackedcarrot.UI;

import android.os.SystemClock;

import com.crackedcarrot.Coords;
import com.crackedcarrot.Scaler;
import com.crackedcarrot.Sprite;
import com.crackedcarrot.menu.R;

public class RangeIndicator extends Sprite {
	private long startTime;
	private long currentTime;
	private long lastUpdateTime;

	private Show showRunner;
	private Hide hideRunner;
	private BlinkRangeRed blinkR;

	public RangeIndicator(Scaler s) {
		// The grid only has one subtype, and one frame. Magical constants for
		// the win.
		super(R.drawable.range_indicator, OVERLAY, 1);
		this.x = 0;
		this.y = 0;
		this.z = 0;
		Coords co = s.scale(128, 128);
		this.setWidth(co.getX());
		this.setHeight(co.getY());
		this.draw = false;

		this.r = 1.0f;
		this.g = 1.0f;
		this.b = 1.0f;
		this.opacity = 0.0f;

		showRunner = new Show();
		hideRunner = new Hide();
		blinkR = new BlinkRangeRed();
	}

	public Show getShowRunner() {
		return showRunner;
	}

	public Hide getHideRunner() {
		return hideRunner;
	}

	public BlinkRangeRed getBlinkRedRunner() {
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
				if ((currentTime - lastUpdateTime) > 50) {
					opacity += 0.05f;
					lastUpdateTime = currentTime;
				}
				SystemClock.sleep(10);
				currentTime = SystemClock.uptimeMillis();
			}
			// Show the range for a little while.
			// SystemClock.sleep(100);

		}
	};

	private class Hide implements Runnable {
		// @Override
		public void run() {
			if (draw == false)
				return;

			/*
			 * opacity = 0.5f; startTime = SystemClock.uptimeMillis();
			 * currentTime = SystemClock.uptimeMillis(); lastUpdateTime =
			 * currentTime;
			 */
			// Fade out should take equal time as fade in. Update the timers.
			startTime = SystemClock.uptimeMillis();
			currentTime = SystemClock.uptimeMillis();
			lastUpdateTime = currentTime;
			opacity = 0.5f;
			while ((currentTime - startTime) < 500) {
				if ((currentTime - lastUpdateTime) > 50) {
					opacity -= 0.05f;
					lastUpdateTime = currentTime;
				}
				SystemClock.sleep(10);
				currentTime = SystemClock.uptimeMillis();
			}
			opacity = 0.0f;
			draw = false;
		}
	}

	private class BlinkRangeRed implements Runnable {
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
