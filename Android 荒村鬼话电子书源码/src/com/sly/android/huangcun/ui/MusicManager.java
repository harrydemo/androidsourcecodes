package com.sly.android.huangcun.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class MusicManager extends PreferenceActivity{
	private static final String OPT_MUSIC="music";
	private static final boolean OPT_MUSIC_DEF=true;
	private static MediaPlayer mp=null;
	private static String TAG="MusicManager";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.music);
	}
	/**
	 * ��ȡ���ñ������ֵ�Ĭ����true
	 */
	public static boolean getMusic(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_MUSIC, OPT_MUSIC_DEF);
		
	}
	public static void stop(Context context){
		if(mp!=null){
			mp.stop();
			mp.release();
			mp=null;
		}
	}
	public static void play(Context context, int resource) {
		Log.i(TAG, "---------play ----"+context );

		// ֹͣ�Ѿ����ŵ�����
		stop(context);
		// ��ʼ����
		if (getMusic(context)) {
			mp = MediaPlayer.create(context, resource);
			mp.setLooping(true);
			mp.start();
		}
	}
}
