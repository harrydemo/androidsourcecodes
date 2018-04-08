package com.stickycoding.rokon.audio;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.stickycoding.rokon.Debug;
import com.stickycoding.rokon.Rokon;

/**
 * RokonMusic.java
 * A class optimized for longer audio files - using MediaPlayer
 * Shouldn't be used for sound effects, use RokonAudio for that.
 * 
 * If wanting to use callbacks (for things like onCompletion
 * use the standard MediaPlayer listeners. See MediaPlayer for
 * information - use RokonMusic.getMediaPlayer() to get the object.
 * 
 * @author Richard
 */

public class RokonMusic {
	
	protected static MediaPlayer mediaPlayer;
	protected static boolean mustResume = false;
	
	private static void prepareMediaPlayer() {
		if(mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
	}
	
	/**
	 * Plays a file from assets, no looping
	 * 
	 * @param file valid path to a file in assets
	 */
	public static void play(String file) {
		play(file, false);
	}

	/**
	 * Plays a file from assets
	 * 
	 * @param file valid path to a file in assets
	 * @param loop TRUE to loop the file, FALSE to play once
	 */
	public static void play(String file, boolean loop) {
		prepareMediaPlayer();
		mediaPlayer.setLooping(loop);
		AssetFileDescriptor afd = null;
		try {
			afd = Rokon.getActivity().getAssets().openFd(file);
		} catch (Exception e) { 
			e.printStackTrace();
			Debug.error("Tried creating RokonMusic with missing asset ... " + file);
			Debug.forceExit();
			return;
		}
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			afd.close();
		} catch (IOException e) {
			e.printStackTrace();
			Debug.error("Error setting data source in RokonMusic.play, IO exception");
			Debug.forceExit();
			return;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			Debug.error("Error setting data source in RokonMusic.play, fail ...");
			return;
		}
		try {
			mediaPlayer.prepare();	
		} catch (Exception e) { 
			e.printStackTrace();
			Debug.error("Error preparing MediaPlayer");
			Debug.forceExit();
			return;
		}
		mediaPlayer.start();
	}
	
	/**
	 * Plays the current music file, if paused or stopped
	 */
	public static void play() {
		if(mediaPlayer == null) return;
		mediaPlayer.start();
	}
	
	/**
	 * Stops the current music file from playing
	 */
	public static void stop() {
		if(mediaPlayer == null) return;
		mediaPlayer.stop();
	}
	
	/**
	 * Pauses the current music file, resume with start()
	 */
	public static void pause() {
		if(mediaPlayer == null) return;
		mediaPlayer.pause();
	}
	
	/**
	 * Call when RokonActivity is paused
	 */
	public static void onPause() {
		if(mediaPlayer == null) return;
		if(mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			mustResume = true;
		}
	}
	
	/**
	 * Call when RokonActivity is resumed
	 */
	public static void onResume() {
		if(mediaPlayer == null) return;
		if(mustResume) {
			mediaPlayer.start();
			mustResume = false;
		}
	}
	
	/**
	 * Fetches the associated MediaPlayer object, so you can make your own changes or hook your own Listeners
	 * 
	 * @return MediaPlayer object, NULL if none used
	 */
	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
}
