package com.hrw.android.player.utils;

import java.io.IOException;

import android.media.MediaPlayer;

public class MediaPlayerUtil extends MediaPlayer {
	private static MediaPlayerUtil INSTANCE = null;

	private boolean isPause = false;

	private String path;

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	private MediaPlayerUtil() {

	}

	public String getPlayingMediaPath() {
		return this.path;
	}

	public static MediaPlayerUtil getInstance(
			OnCompletionListener onCompeletionListener) {
		if (null == INSTANCE) {
			INSTANCE = new MediaPlayerUtil();
		}
		INSTANCE.setOnCompletionListener(onCompeletionListener);
		return INSTANCE;
	}

	@Override
	public void reset() {
		isPause = false;
		super.reset();
	}

	public void play(String path) throws IllegalStateException, IOException {
		this.path = path;
		this.setDataSource(this.path);
		if (!isPause) {
			super.prepare();
		}
		super.start();
	}

	@Override
	public void stop() throws IllegalStateException {
		isPause = false;
		super.stop();
	}

	@Override
	public void pause() throws IllegalStateException {
		isPause = true;
		super.pause();
	}

	public void previousOrNext(String path) throws IllegalStateException,
			IOException {
		isPause = false;
		play(path);

	}
}
