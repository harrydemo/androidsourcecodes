package com.hrw.android.player.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import com.hrw.android.player.BelmotPlayer;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class PlayerEngineImpl implements IPlayerEngine {

	public PlayerEngineImpl() {
		if (null == mediaPlayerEngine) {
			Log.i(BelmotPlayer.TAG + "---PlayerEngineImpl---",
					"Line 19 New MediaPlayerEngine();");
			mediaPlayerEngine = new MediaPlayerEngine();
			// mediaPlayerEngine
			// .setOnCompletionListener(new OnCompletionListener() {
			//
			// @Override
			// public void onCompletion(MediaPlayer mp) {
			// switch (playbackMode) {
			// case SHUFFLE: {
			// Collections.shuffle(playbackOrder);
			// break;
			// }
			// }
			// next();
			//
			// }
			// });

		}
	}

	public enum PlaybackMode {
		NORMAL, SHUFFLE, REPEAT, SHUFFLE_AND_REPEAT
	}

	private PlaybackMode playbackMode = PlaybackMode.NORMAL;

	private List<Integer> playbackOrder = new ArrayList<Integer>();

	private String path = "";

	private int selectedOrderIndex = 0;

	private List<String> mediaList = new ArrayList<String>();

	private MediaPlayerEngine mediaPlayerEngine = null;

	@Override
	public void setOnCompletionListener(
			OnCompletionListener onCompletionListener) {
		Log.i(BelmotPlayer.TAG + "---PlayerEngineImpl---",
				"Line 56 setOnCompletionListener;");
		mediaPlayerEngine.setOnCompletionListener(onCompletionListener);
	}

	private boolean isEmpty() {
		return mediaList.size() == 0;
	}

	private int getListSize() {
		return mediaList.size();
	}

	public PlaybackMode getPlaybackMode() {
		return playbackMode;
	}

	@Override
	public void setPlaybackMode(PlaybackMode playbackMode) {
		this.playbackMode = playbackMode;
		calculateOrder(true);
	}

	@Override
	public void forward(int time) {
		mediaPlayerEngine.seekTo(time);

	}

	@Override
	public boolean isPlaying() {
		return mediaPlayerEngine.isPlaying();
	}

	@Override
	public boolean isPause() {
		return mediaPlayerEngine.isPause();
	}

	private int getSelectedOrderIndexByPath(String path) {
		int selectedIndex = mediaList.indexOf(path);
		return playbackOrder.indexOf(selectedIndex);

	}

	private String getPathByPlaybackOrderIndex(int index) {
		return mediaList.get(playbackOrder.get(index));
	}

	@Override
	public void previous() {
		if (!isEmpty()) {
			selectedOrderIndex = getSelectedOrderIndexByPath(path);
			selectedOrderIndex--;
			if (selectedOrderIndex < 0) {
				selectedOrderIndex = mediaList.size() - 1;
			}
			this.path = getPathByPlaybackOrderIndex(selectedOrderIndex);
			mediaPlayerEngine.previousOrNext();
		}

	}

	@Override
	public void next() {
		if (!isEmpty()) {
			selectedOrderIndex = mediaList.indexOf(path);
			Log
					.i(
							BelmotPlayer.TAG + "---PlayerEngineImpl---",
							"Line 123 next():Path="
									+ path
									+ "***********selectedOrderIndex="
									+ selectedOrderIndex
									+ "***************************************playbackOrder="
									+ playbackOrder.toArray());
			// selected begins from zero.
			selectedOrderIndex++;
			selectedOrderIndex %= mediaList.size();
			this.path = getPathByPlaybackOrderIndex(selectedOrderIndex);
			Log
					.i(
							BelmotPlayer.TAG + "---PlayerEngineImpl---",
							"Line 123 next():next Path="
									+ path
									+ "***********next selectedOrderIndex="
									+ selectedOrderIndex
									+ "***************************************playbackOrder="
									+ playbackOrder.toArray());
			mediaPlayerEngine.previousOrNext();
		}

	}

	@Override
	public void pause() {
		mediaPlayerEngine.pause();

	}

	@Override
	public void play() {
		mediaPlayerEngine.play(path);
	}

	@Override
	public void start() {
		mediaPlayerEngine.start();
	}

	@Override
	public void reset() {
		mediaPlayerEngine.reset();

	}

	@Override
	public void rewind(int time) {
		mediaPlayerEngine.seekTo(time);

	}

	@Override
	public void skipTo(int index) {

	}

	@Override
	public void stop() {
		mediaPlayerEngine.stop();
	}

	@Override
	public String getPlayingPath() {
		return this.path;
	}

	@Override
	public void setPlayingPath(String path) {
		this.path = path;

	}

	@Override
	public void setMediaPathList(List<String> pathList) {
		this.mediaList = pathList;
		calculateOrder(true);

	}

	private void calculateOrder(boolean force) {
		int beforeSelected = 0;
		if (!playbackOrder.isEmpty()) {
			beforeSelected = playbackOrder.get(selectedOrderIndex);
			playbackOrder.clear();
		}
		Log
				.i(BelmotPlayer.TAG + "---PlayerEngineImpl---",
						"Line 200 calculateOrder():beforeSelected="
								+ beforeSelected
								+ "***********selectedOrderIndex="
								+ selectedOrderIndex);
		for (int i = 0; i < getListSize(); i++) {
			playbackOrder.add(i, i);
		}

		if (null == playbackMode) {
			playbackMode = PlaybackMode.NORMAL;
		}
		switch (playbackMode) {
		case NORMAL: {
			break;
		}
		case SHUFFLE: {
			Collections.shuffle(playbackOrder);
			break;
		}
		case REPEAT: {
			selectedOrderIndex = beforeSelected;
			break;
		}
		case SHUFFLE_AND_REPEAT: {
			break;
		}
		default: {
			break;
		}
		}

	}

	private class MediaPlayerEngine extends MediaPlayer {

		private boolean isPause = false;

		public boolean isPause() {
			return isPause;
		}

		@Override
		public void reset() {
			isPause = false;
			super.reset();
		}

		public void play(String path) {
			try {
				this.setDataSource(path);
				if (!isPause) {
					super.prepare();
				}
				super.start();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

		public void previousOrNext() {
			reset();
			Log.i(BelmotPlayer.TAG, "previousOrNext:path = " + path);
			play(path);

		}
	}

	@Override
	public String getCurrentTime() {
		return getTime(mediaPlayerEngine.getCurrentPosition());
	}

	@Override
	public String getDurationTime() {
		return getTime(mediaPlayerEngine.getDuration());
	}

	@Override
	public PlaybackMode getPlayMode() {
		return playbackMode;
	}

	private String getTime(int timeMs) {
		int totalSeconds = timeMs / 1000;// 获取文件有多少秒
		StringBuilder mFormatBuilder = new StringBuilder();
		Formatter mFormatter = new Formatter(mFormatBuilder, Locale
				.getDefault());
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;
		mFormatBuilder.setLength(0);

		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
					.toString();// 格式化字符串
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	@Override
	public int getDuration() {
		return mediaPlayerEngine.getDuration();
	}

	@Override
	public int getCurrentPosition() {
		return mediaPlayerEngine.getCurrentPosition();
	}
}
