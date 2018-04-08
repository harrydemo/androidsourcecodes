package com.jameschen.movie;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.jameschen.movie.service.MediaPlaybackService;

public class MyFloatView implements OnCompletionListener, OnErrorListener,
		OnInfoListener, OnPreparedListener, OnSeekCompleteListener,
		OnVideoSizeChangedListener, SurfaceHolder.Callback,
		android.view.View.OnClickListener {
	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;
	ViewGroup mlayoutView;
	Context context;
	Display currentDisplay;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	Button mButton;
	MediaPlayer mediaPlayer;// 使用的是MediaPlayer来播放视频
	int videoWidth = 0; // 视频的宽度，初始化，后边会对其进行赋值
	int videoHeight = 0; // 同上
	boolean readyToPlayer = false;
	public final static String LOGCAT = "CUSTOM_VIDEO_PLAYER";

	public static final String ACTION_DESTROY_MOVIE = "com.jameschen.send.movie.Destroy";

	public MyFloatView(ViewGroup layoutView) {
		// TODO Auto-generated constructor stub
		mlayoutView = layoutView;
		context = mlayoutView.getContext();
		mlayoutView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
				// TODO Auto-generated method stub
				onTouchEvent(paramMotionEvent);
				return false;
			}
		});

		initWindow();
	}

	public View getLayoutView() {
		return mlayoutView;
	}

	public void onResume() {
	}

	public void initWindow() {

		// 获取WindowManager
		wm = (WindowManager) context.getApplicationContext().getSystemService(
				"window");
		// 设置LayoutParams(全局变量）相关参数
		// wmParams = ((MyApplication)getApplication()).getMywmParams();
		wmParams = new WindowManager.LayoutParams();
		/**
		 * 以下都是WindowManager.LayoutParams的相关属性 具体用途可参考SDK文档
		 */
		wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT
				| LayoutParams.TYPE_SYSTEM_OVERLAY; // 设置window type
		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.TRANSPARENT;
		// 设置Window flag

		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE
				| LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */
		wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
		// 以屏幕左上角为原点，设置x、y初始值
		currentDisplay = wm.getDefaultDisplay();
		WIDTH = currentDisplay.getWidth();
		HEIGHT = currentDisplay.getHeight();
		wmParams.x = (WIDTH - VIEW_WIDTH) / 2;
		wmParams.y = 0;
		// 设置悬浮窗口长宽数据
		wmParams.width = VIEW_WIDTH;
		wmParams.height = VIEW_HEIGHT;
	}

	static int VIEW_WIDTH = 458, VIEW_HEIGHT = 549;

	public void bindViewListener() {
		initialUI();
	}

	private void initialUI() {

		// setContentView(R.layout.main);

		// 关于SurfaceView和Surfaceolder可以查看文档
		surfaceView = (SurfaceView) mlayoutView.findViewById(R.id.myView);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnSeekCompleteListener(this);
		mediaPlayer.setOnVideoSizeChangedListener(this);
		String filePath = "/mnt/sdcard/test.mp4";// 本地地址和网络地址都可以
		try {
			mediaPlayer.setDataSource(filePath);
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			Log.v(LOGCAT, e.getMessage());
			onExit();
		} catch (IllegalStateException e) {
			Log.v(LOGCAT, e.getMessage());
			onExit();
		} catch (IOException e) {
			Log.v(LOGCAT, e.getMessage());
			onExit();
		}

		IntentFilter filter = new IntentFilter(ACTION_DESTROY_MOVIE);
		context.registerReceiver(sReceiver, filter);

	}

	BroadcastReceiver sReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			onExit();
		}
	};

	public void onExit() {

		try {
			wm.removeView(mlayoutView);
			Intent mIntent = new Intent("removeUI");
			mIntent.setClass(context, MediaPlaybackService.class);
			context.startService(mIntent);
			context.unregisterReceiver(sReceiver);
			mediaPlayer.pause();
			mediaPlayer.stop();
			mediaPlayer.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.v(LOGCAT, "surfaceChanged Called");

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.v(LOGCAT, "suc calles");
		mediaPlayer.setDisplay(holder);// 若无次句，将只有声音而无图像
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			onExit();
		} catch (IOException e) {
			onExit();
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v(LOGCAT, "surfaceDestroyed Called");

	}

	@Override
	public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
		Log.v(LOGCAT, "onVideoSizeChanged Called");

	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		Log.v(LOGCAT, "onSeekComplete Called");

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.v(LOGCAT, "onPrepared Called");
		videoWidth = mp.getVideoWidth();
		videoHeight = mp.getVideoHeight();
		/** 这一步为videod的高宽赋值，将其值控制在可控的范围之内，在VideoView的源码中也有相关的代码，有兴趣可以一看 */
		if (videoWidth > currentDisplay.getWidth()
				|| videoHeight > currentDisplay.getHeight()) {
			float heightRatio = (float) videoHeight
					/ (float) currentDisplay.getHeight();
			float widthRatio = (float) videoWidth
					/ (float) currentDisplay.getWidth();
			if (heightRatio > 1 || widthRatio > 1) {
				if (heightRatio > widthRatio) {
					videoHeight = (int) Math.ceil((float) videoHeight
							/ (float) heightRatio);
					videoWidth = (int) Math.ceil((float) videoWidth
							/ (float) heightRatio);
				} else {
					videoHeight = (int) Math.ceil((float) videoHeight
							/ (float) widthRatio);
					videoWidth = (int) Math.ceil((float) videoWidth
							/ (float) widthRatio);
				}
			}
		}

		// 设置悬浮窗口长宽数据
		wmParams.width = VIEW_WIDTH = videoWidth;
		wmParams.height = VIEW_HEIGHT = videoHeight + 36;

		mButton = new Button(context);
		mButton.setText("close");
		FrameLayout.LayoutParams sParams = new FrameLayout.LayoutParams(
				android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
				android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
		sParams.setMargins(0, videoHeight, 0, 0);
		mButton.setLayoutParams(sParams);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onExit();
			}
		});
		mlayoutView.addView(mButton);

		surfaceView.setLayoutParams(new FrameLayout.LayoutParams(videoWidth,
				videoHeight));
		mp.start();

	}

	@Override
	public boolean onInfo(MediaPlayer arg0, int whatInfo, int extra) {
		if (whatInfo == MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING) {
			Log.v(LOGCAT, "Media Info, Media Info Bad Interleaving " + extra);
		} else if (whatInfo == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
			Log.v(LOGCAT, "Media Info, Media Info Not Seekable " + extra);
		} else if (whatInfo == MediaPlayer.MEDIA_INFO_UNKNOWN) {
			Log.v(LOGCAT, "Media Info, Media Info Unknown " + extra);
		} else if (whatInfo == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
			Log.v(LOGCAT, "MediaInfo, Media Info Video Track Lagging " + extra);
		} else if (whatInfo == MediaPlayer.MEDIA_INFO_METADATA_UPDATE) {
			Log.v(LOGCAT, "MediaInfo, Media Info Metadata Update " + extra);
		}

		return false;
	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		Log.v(LOGCAT, "onError Called");
		if (arg1 == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
			Log.v(LOGCAT, "Media Error, Server Died " + arg2);
		} else if (arg1 == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
			Log.v(LOGCAT, "Media Error, Error Unknown " + arg2);
		}

		return false;
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		Log.v(LOGCAT, "onCompletion Called");
		onExit();

	}

	public void showLayoutView() {
		wm.addView(mlayoutView, wmParams);

	}

	public static int WIDTH = 1024, HEIGHT = 552;
	public static final int left = -VIEW_WIDTH / 2, top = 0, right = WIDTH
			- VIEW_WIDTH / 2, bottom = HEIGHT - VIEW_HEIGHT / 2;
	private WindowManager wm = null;
	private WindowManager.LayoutParams wmParams = null;
	private float mLastTouchY = 0;
	int rangeOut_H = -1, rangeOut_V = -1;

	public boolean onTouchEvent(MotionEvent event) {
		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY(); // 25是系统状态栏的高度
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 获取相对View的坐标，即以此View左上角为原点
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mLastTouchY > 110) {
				mTouchStartX = event.getX();
				mTouchStartY = event.getY();
				mLastTouchY = 0;
			}
			int positionX = (int) (x - mTouchStartX);
			int positionY = (int) (y - mTouchStartY);

			if (positionX < left || positionX > right) {// check borad just
														// reset
				rangeOut_H = positionX < left ? left : right;
			}
			if (positionY < top || positionY > bottom) {// check borad just
				// reset
				rangeOut_V = positionY < top ? top : bottom;
			}

			updateViewPosition();
			break;

		case MotionEvent.ACTION_UP:

			if (rangeOut_H != -1) {
				x = rangeOut_H + mTouchStartX;
			}
			if (rangeOut_V != -1) {
				y = rangeOut_V + mTouchStartY;
			}

			updateViewPosition();
			mTouchStartX = mTouchStartY = 0;
			rangeOut_H = rangeOut_V = -1;
			break;
		default:
			mTouchStartX = mTouchStartY = 0;
			rangeOut_H = rangeOut_V = -1;
			break;
		}
		return true;
	}

	private void updateViewPosition() {
		// 更新浮动窗口位置参数
		wmParams.x = (int) (x - mTouchStartX);
		wmParams.y = (int) (y - mTouchStartY);
		wm.updateViewLayout(mlayoutView, wmParams);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}