package com.threed.jpct.games.alienrunner;

import com.threed.jpct.Logger;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

	public static int COLLECTED = 0;
	public static int SPRING = 1;
	public static int CLICK = 2;
	public static int BUMP = 3;
	public static int DONE = 4;
	public static int START = 5;
	public static int TURN = 6;
	public static int WATER = 7;
	public static int PERK = 8;
	public static int BOMB = 9;
	public static int JINGLE = 10;
	public static int ZOOM = 11;
	public static int HISS = 12;
	public int[] ids = new int[13];
	public long[] times = new long[13];
	public boolean loaded = false;

	private SoundPool soundPool;

	private static SoundManager instance = null;
	private static Resources res = null;
	private static Context context = null;

	public synchronized static SoundManager getInstance() {
		if (instance == null) {
			instance = new SoundManager();
		}
		return instance;
	}

	public static void inject(Resources res, Context context) {
		SoundManager.res = res;
		SoundManager.context = context;
		SoundManager.getInstance().init();
	}

	private SoundManager() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	}

	private void init() {
		if (!loaded) {
			Logger.log("Loading sounds...");
			ids[COLLECTED] = soundPool.load(context, R.raw.coin, 1);
			ids[SPRING] = soundPool.load(context, R.raw.spring, 1);
			ids[CLICK] = soundPool.load(context, R.raw.click, 1);
			ids[BUMP] = soundPool.load(context, R.raw.aahhh, 1);
			ids[DONE] = soundPool.load(context, R.raw.woohoo, 1);
			ids[START] = soundPool.load(context, R.raw.gunshot, 1);
			ids[TURN] = soundPool.load(context, R.raw.page, 1);
			ids[WATER] = soundPool.load(context, R.raw.splash, 1);
			ids[PERK] = soundPool.load(context, R.raw.doorbell, 1);
			ids[BOMB] = soundPool.load(context, R.raw.bomb, 1);
			ids[JINGLE] = soundPool.load(context, R.raw.jingle, 1);
			ids[ZOOM] = soundPool.load(context, R.raw.zoom, 1);
			ids[HISS] = soundPool.load(context, R.raw.hiss, 1);
			loaded = true;
		}
	}

	public synchronized void play(int id) {
		if (res == null || GameConfig.soundMul == 0) {
			return;
		}
		soundPool.play(ids[id], 0.75f * GameConfig.soundMul, 0.75f * GameConfig.soundMul, 0, 0, 1);
		times[id] = Ticker.getTime();
	}

	public synchronized void play(int id, long delay) {
		if (Ticker.getTime() - times[id] >= delay) {
			play(id);
		}
	}
}
