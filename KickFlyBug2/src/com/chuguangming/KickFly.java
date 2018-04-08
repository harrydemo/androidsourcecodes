package com.chuguangming;

import com.chuguangming.KickFlybug.Until.ActivityUtil;
import com.chuguangming.KickFlybug.Until.BitmapManager;
import com.chuguangming.KickFlybug.Until.GameObjData;
import com.chuguangming.KickFlybug.View.GameView;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class KickFly extends Activity
{
	//定义游戏的View
	GameView gv;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//全屏并且没有title
		ActivityUtil.fullLandScapeScreen(this);
		ActivityUtil.initScreenData(this);
		//载入游戏资源文件，活虫子与死虫子的图片
		Resources res = getResources();
		BitmapManager.initFlyBitmap(res);
		//生成GameView的实例
		if (gv == null)
		{
			gv = new GameView(this);
		} else
		{
			Log.i(ActivityUtil.infoMessage, "GameView已存在");
		}
		setContentView(gv);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.i(ActivityUtil.infoMessage, "Destroy GameView");
		GameObjData.clear();
		System.gc();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.i(ActivityUtil.infoMessage, "Pause GameView");
		gv.gameLoop = false;
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Log.i(ActivityUtil.infoMessage, "Start GameView");
		gv.initGameObjData();
	}
}