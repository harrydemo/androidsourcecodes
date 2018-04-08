package com.chuguangming.KickFlybug.View;

import com.chuguangming.GameModeActivity;
import com.chuguangming.KickFly;
import com.chuguangming.R;

import com.chuguangming.KickFlybug.Config.GameMainConfig;
import com.chuguangming.KickFlybug.Until.ActivityUtil;
import com.chuguangming.KickFlybug.Until.AudioUtil;
import com.chuguangming.KickFlybug.Until.GameObjData;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

public class GameMainView extends View
{
	// 上下文
	private Context myContext;
	// 定义开始与设置按钮
	private Rect StartRect = new Rect(275, 265, 543, 301);
	private Rect SetRect = new Rect(274, 330, 549, 374);
	private Rect AboutRect = new Rect(272, 412, 559, 467);
    //定义小分别率的按钮区域
	private Rect StartRectS=new Rect(148,160,312,195);
	private Rect SetRectS=new Rect(146,219,308,255);
	public GameMainView(Context context)
	{
		super(context);
		myContext = context;
		// 开始背景声音线程线程
		Thread MusicThread = new Thread(new MusicHandler());
		MusicThread.start();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		// 1为输出调试信息
		int debug = 2;
		switch (debug)
		{
		case 1:
			Paint myPaint = new Paint();
			String familyName = "宋体";
			Typeface font = Typeface.create(familyName, Typeface.BOLD);
			myPaint.setAntiAlias(true);
			myPaint.setColor(Color.RED);
			myPaint.setTypeface(font);
			myPaint.setTextSize(22);
			canvas.drawText(ActivityUtil.SCREEN_WIDTH + "*"
					+ ActivityUtil.SCREEN_HEIGHT, 100, 100, myPaint);
			break;
		case 2:
			// 绘制背景
			canvas.drawBitmap(ActivityUtil.returnPic("bg3", myContext), null,
					GameMainConfig.returnDescBackgroundRect(), new Paint());
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			int ax = (int) event.getX();
			int ay = (int) event.getY();

			// ActivityUtil.ShowXYMessage(getContext(), ax, ay);
			// 判断是否是低分屏的机器如G3
			if (ActivityUtil.SCREEN_WIDTH == 480)
			{
				//ActivityUtil.ShowXYMessage(getContext(), ax, ay);
				// 点击到开始按钮
				if (StartRectS.contains(ax, ay))
				{

					// 开始按钮声音线程线程
					Thread ButtonMusicThread = new Thread(
							new ButtonMusicHandler());
					ButtonMusicThread.start();
					// ActivityUtil.PlaySoundPool(myContext, R.raw.music3);
					// 停顿二秒再进入系统
					ActivityUtil.SleepTime(1000);
					// ActivityUtil.ShowXYMessage(getContext(), ax, ay);

					// 转向到继续游戏
					Intent intent = new Intent();
					intent.setClass(getContext(), KickFly.class);
					// 设定游戏的模式为打死一百虫子就结束
					GameObjData.CURRENT_GAME_MODE = GameObjData.MODE_100C;
					// 转向登陆后的页面
					getContext().startActivity(intent);
				}
				// 点击到设置按钮
				if (SetRectS.contains(ax, ay))
				{
					// 开始按钮声音线程线程
					Thread ButtonMusicThread = new Thread(
							new ButtonMusicHandler());
					ButtonMusicThread.start();
					ActivityUtil.SleepTime(1000);
					// 转向到继续游戏
					Intent intent = new Intent();
					intent.setClass(getContext(), GameModeActivity.class);
					// 转向登陆后的页面
					getContext().startActivity(intent);
					// ActivityUtil.ShowXYMessage(getContext(), ax, ay);
				}
			} else
			{
				// 点击到开始按钮
				if (StartRect.contains(ax, ay))
				{

					// 开始按钮声音线程线程
					Thread ButtonMusicThread = new Thread(
							new ButtonMusicHandler());
					ButtonMusicThread.start();
					// ActivityUtil.PlaySoundPool(myContext, R.raw.music3);
					// 停顿二秒再进入系统
					ActivityUtil.SleepTime(1000);
					// ActivityUtil.ShowXYMessage(getContext(), ax, ay);

					// 转向到继续游戏
					Intent intent = new Intent();
					intent.setClass(getContext(), KickFly.class);
					// 设定游戏的模式为打死一百虫子就结束
					GameObjData.CURRENT_GAME_MODE = GameObjData.MODE_100C;
					// 转向登陆后的页面
					getContext().startActivity(intent);
				}
				// 点击到设置按钮
				if (SetRect.contains(ax, ay))
				{
					// 开始按钮声音线程线程
					Thread ButtonMusicThread = new Thread(
							new ButtonMusicHandler());
					ButtonMusicThread.start();
					ActivityUtil.SleepTime(1000);
					// 转向到继续游戏
					Intent intent = new Intent();
					intent.setClass(getContext(), GameModeActivity.class);
					// 转向登陆后的页面
					getContext().startActivity(intent);
					// ActivityUtil.ShowXYMessage(getContext(), ax, ay);
				}
				// 点击到关于按钮
				if (AboutRect.contains(ax, ay))
				{

				}
			}
			postInvalidate();// 刷新屏幕
		}
		return super.onTouchEvent(event);
	}

	class MusicHandler implements Runnable
	{
		public void run()
		{
			try
			{
				Thread.sleep(4000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			// 播放声音
			AudioUtil.PlayMusicLoop(myContext, R.raw.backsound);
		}
	}

	class ButtonMusicHandler implements Runnable
	{
		public void run()
		{
			// 播放声音
			AudioUtil.PlayMusicLoop(myContext, R.raw.musicbutton);
		}
	}
}
