package com.threed.jpct.games.alienrunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;

import com.threed.jpct.Logger;

public class GameConfig {

	public static boolean particles = true;

	public static boolean backDrop = true;

	public static boolean glow = true;

	public static int maxFPS = 30;

	public static int sleepValue = (int) ((1000f / (float) maxFPS) + 0.5f);

	public static boolean throttling = true;

	public static long eventSleepTime = 20;

	public static boolean showArrow = false;

	public static boolean correctCollisions = true;

	public static float defaultWidth = 480;

	public static float defaultHeight = 320;

	public static float skySpeed = 0.035f;

	public static boolean showDecorations = true;

	public static int decorationDistribution = 8;

	public static boolean mipmaps = true;

	public static boolean pauseAllowed = false;

	public static boolean staticCamera = true;

	public static boolean showFps = false;

	public static float soundMul = 1f;

	public static int versionIndex = 2;

	public static void saveConfig(Context context) {
		Logger.log("Writing config data!");
		try {
			FileOutputStream os = context.openFileOutput("config.alr", Activity.MODE_PRIVATE);

			write(os, GameConfig.particles);
			write(os, GameConfig.glow);
			write(os, GameConfig.backDrop);
			write(os, GameConfig.throttling);
			write(os, GameConfig.showArrow);
			write(os, GameConfig.showDecorations);
			write(os, GameConfig.staticCamera);
			write(os, GameConfig.showFps);

			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("Unable to write config data!", Logger.WARNING);
		}
	}

	public static void loadConfig(Context context) {
		Logger.log("Loading config data!");
		try {
			FileInputStream is = context.openFileInput("config.alr");

			GameConfig.particles = read(is);
			GameConfig.glow = read(is);
			GameConfig.backDrop = read(is);
			GameConfig.throttling = read(is);
			GameConfig.showArrow = read(is);
			GameConfig.showDecorations = read(is);
			GameConfig.staticCamera = read(is);
			GameConfig.showFps = read(is);

			is.close();
		} catch (Exception e) {
			Logger.log("Unable to load config data!", Logger.WARNING);
		}
	}

	private static boolean read(FileInputStream is) throws Exception {
		int res = is.read();
		return res == 1;
	}

	private static void write(FileOutputStream os, boolean value) throws Exception {
		if (value) {
			os.write(1);
		} else {
			os.write(0);
		}
	}

}
