/**
 * 
 */
package com.himi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.LayoutInflater.Factory;
import android.view.SurfaceHolder.Callback;

/**
 * @author Himi
 * 
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable {

	private SurfaceHolder sh;
	private Thread th;
	private Paint paint;
	private Canvas canvas;
	private Resources res;
	private Bitmap bp;
	private Bitmap bp_backGround;
	private int ScreenH, ScreenW;
	private boolean flag;
	public static String str_zh = "Himi";
	public static String str_pass = "Android进阶群:109367315";
	private int bp_x, bp_y;
	private boolean flag_zh = true;

	public MySurfaceView(Context context) {
		super(context);
		res = this.getResources();
		bp = BitmapFactory.decodeResource(res, R.drawable.register);
		bp_backGround = BitmapFactory.decodeResource(res, R.drawable.duola);
		th = new Thread(this);
		sh = this.getHolder();
		sh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		this.setKeepScreenOn(true);
		this.setFocusable(true);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		ScreenW = this.getWidth();
		ScreenH = this.getHeight();
		bp_x = ScreenW / 2 - bp.getWidth() / 2;
		bp_y = ScreenH / 2 - bp.getHeight() / 2;
		th.start();
	}

	public void draw() {
		canvas = sh.lockCanvas();
		paint.setColor(Color.RED);
		if (canvas != null) {// 当点击home键 或者返回按键的时候canvas是得不到的，这里要注意
			canvas.drawColor(Color.WHITE);
			canvas.drawBitmap(bp_backGround,
					-(bp_backGround.getWidth() - ScreenW), -(bp_backGround
							.getHeight() - ScreenH), paint);
			canvas.drawBitmap(bp, bp_x, bp_y, paint);
			canvas.drawText(str_zh, bp_x + 20, bp_y + 43 + 15, paint);
			String temp_pass = "";
			int temp_passLength = 0;
			temp_passLength = str_pass.toCharArray().length;
			if (temp_passLength > 15) {
				temp_passLength = 15;
			}
			for (int i = 0; i < temp_passLength; i++) {
				temp_pass += "*";
			}
			canvas.drawText(temp_pass, bp_x + 21, bp_y + 79 + 15, paint);
			paint.setColor(Color.YELLOW);
			if (flag_zh) {
				canvas.drawCircle(bp_x + 23, bp_y + 113, 4, paint);
			}
			sh.unlockCanvasAndPost(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float pointx = event.getX();
		float pointy = event.getY();
		if (pointx > bp_x + 14 && pointx < bp_x + 14 + 117) {
			if (pointy > bp_y + 43 && pointy < bp_y + 43 + 15) {
				// 帐号
				Intent i = new Intent();// 得到一个意图的实例
				i.putExtra("count", 1);// 写出数据
				i.putExtra("himi", str_zh);
				i.setClass(MainActivity.instance, Register.class);// 设置当前activity以及将要操作的类
				MainActivity.instance.startActivity(i);// 用当前activity来启动另外一个activity
			}
		}
		if (pointx > bp_x + 14 && pointx < bp_x + 14 + 117) {
			if (pointy > bp_y + 79 && pointy < bp_y + 79 + 15) {
				// 密码
				Intent i = new Intent();
				i.putExtra("count", 2);
				i.putExtra("himi", str_pass);
				i.setClass(MainActivity.instance, Register.class);
				MainActivity.instance.startActivity(i);
			}
		}
		if (pointx > bp_x + 42 && pointx < bp_x + 42 + 60) {
			if (pointy > bp_y + 123 && pointy < bp_y + 123 + 24) {
				// 登录信息
				Intent i = new Intent();
				i.putExtra("count", 3);
				i.putExtra("himi_zh", str_zh);
				i.putExtra("himi_pass", str_pass);
				i.setClass(MainActivity.instance, Register.class);
				MainActivity.instance.startActivity(i);
			}
		}
		if (pointx > bp_x + 15 && pointx < bp_x + 15 + 15) {
			if (pointy > bp_y + 104 && pointy < bp_y + 104 + 15) {
				// 记住帐号
				flag_zh = !flag_zh;
			}
		}
		if (pointx > bp_x + 120 && pointx < bp_x + 120 + 22) {
			if (pointy > bp_y + 5 && pointy < bp_y + 5 + 22) {
				// 退出
				try {
					MainActivity.instance.finish();
					android.os.Process.killProcess(android.os.Process.myPid());
					MainActivity.instance = null;
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return super.onTouchEvent(event);
	}

	public void run() {
		while (!flag) {
			draw();
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
				Log.v("Run", "Error");
			}
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
