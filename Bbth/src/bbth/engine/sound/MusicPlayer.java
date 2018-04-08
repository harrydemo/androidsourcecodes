package bbth.engine.sound;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * A player for single music track
 * 
 * @author jardini
 * 
 */
public class MusicPlayer {

	public static interface OnCompletionListener {
		void onCompletion(MusicPlayer mp);
	}

	private static final int IDLE = 0;
	private static final int PLAYING = 1;
	private static final int PAUSED = 2;
	private static final int STOPPED = 3;
	private static final int RELEASED = 4;

	private MediaPlayer _mediaPlayer;
	private int _state;
	private long _startTime;

	// creates a music player with no song, just for avoiding null checks
	public MusicPlayer() {
		_mediaPlayer = new MediaPlayer();
		_state = IDLE;
	}

	public MusicPlayer(Context context, int resourceId) {
		_mediaPlayer = MediaPlayer.create(context, resourceId);
		_state = IDLE;
	}

	// passes in a callback that is called when the song ends
	public void setOnCompletionListener(final OnCompletionListener listener) {
		if (_state != RELEASED) {
			_mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							listener.onCompletion(MusicPlayer.this);
						}
					});
		}
	}

	public int getCurrentPosition() {
		if (_startTime == 0) {
			return -1;
		}
		return (int) (System.currentTimeMillis() - _startTime);
	}

	public void seekToPosition(int positionMillis) {
		if (_state == PLAYING || _state == PAUSED) {
			_mediaPlayer.seekTo(positionMillis);
			_startTime = System.currentTimeMillis() - positionMillis;
		}
	}

	public void setStartDelay(int delayMillis) {
		_startTime = System.currentTimeMillis() + delayMillis;
	}

	// plays the song once
	public void play() {
		if (_state == IDLE || _state == PAUSED) {
			_mediaPlayer.start();
			_state = PLAYING;

			_startTime = System.currentTimeMillis();
		}
	}

	// loops the song infinitely
	public void loop() {
		if (_state == IDLE || _state == PAUSED) {
			_mediaPlayer.setLooping(true);
			play();
		}
	}

	public int getSongLength() {
		if (_state == PLAYING || _state == PAUSED || _state == STOPPED) {
			return _mediaPlayer.getDuration();
		}
		return 0;
	}

	// pauses the song, allowing for continuation from the current point
	public void pause() {
		if (_state == PLAYING) {
			_mediaPlayer.pause();
			_state = PAUSED;
		}
	}

	// stops the song completely
	public void stop() {
		if (_state == PLAYING || _state == PAUSED || _state == STOPPED) {
			_mediaPlayer.stop();
			_state = STOPPED;
		}
	}

	public boolean isLooping() {
		// valid in any other state
		if (_state != RELEASED) {
			return _mediaPlayer.isLooping();
		}
		return false;
	}

	public boolean isPlaying() {
		return _state == PLAYING;
	}

	public void setVolume(float volume) {
		// valid in any other state
		if (_state != RELEASED) {
			_mediaPlayer.setVolume(volume, volume);
		}
	}

	// release the resources associated with the song
	// call this when you are done with the player
	public void release() {
		if (_state != RELEASED) {
			_mediaPlayer.release();
			_state = RELEASED;
		}
	}
}
