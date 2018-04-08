package com.chuguangming.KickFlybug.Until;
import java.util.HashMap;//引入HashMap类

import com.chuguangming.R;

import android.app.Activity;//引入Activity类
import android.content.Context;//引入Context类
import android.media.AudioManager;//引入AudioManager类
import android.media.MediaPlayer;//引入MediaPlayer类
import android.media.SoundPool;//引入SoundPool类
import android.os.Bundle;//引入Bundle类
import android.view.View;//引入View类
import android.view.View.OnClickListener;//引入OnClickListener类
import android.widget.Button;//引入Button类
import android.widget.TextView;//引入TextView类
public class AudioUtil
{
	MediaPlayer mMediaPlayer; 

	HashMap<Integer, Integer> soundPoolMap; 
	Context myContext;
	
	//播放指定声音
	public static void PlayMusic(Context context,int musicid)
	{
		MediaPlayer mediaplay;
		mediaplay=MediaPlayer.create(context, musicid);
		mediaplay.start();
	}
	public static void PlayMusicLoop(Context context,int musicid)
	{
		MediaPlayer mediaplay;
		mediaplay=MediaPlayer.create(context, musicid);
		mediaplay.setLooping(true);
		mediaplay.start();
	}
	//使用SoundPool来播放短促的声音
	public static void PlaySoundPool(Context context,int musicid)
	{
	    AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);  
		SoundPool soundPool=new SoundPool(4, AudioManager.STREAM_MUSIC, 100);//声音
	    soundPool.play(soundPool.load(context,  R.raw.musicbutton, 1),  
	    		mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC),  
	    		mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 1, 0, 1f);//播放声音
	} 
}
