package net.fenghuo.wallpaper.sakura;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class WallpaperService extends android.service.wallpaper.WallpaperService {

	private int width, height; // 屏幕宽,高

	private boolean visible; // 是否可见
	private int xPixelOffset = 0;
	private Paint paint;

	private Bitmap background; // 背景
	private Bitmap stars; // 星星
	private Bitmap moon;// 月亮
	private Bitmap mountain;// 山脉
	private Bitmap branchRight, branchLeft; // 树枝
	private Bitmap sakurasLeft, sakurasRight; // 树枝上的樱花
	private Bitmap flower1, flower2; // 花朵
	private Bitmap petal1, petal2; // 花瓣

	private static final int time_flower = 40;
	private static final int time_petal = 60;
	private int time_count_flower;
	private int time_count_petal;

	private static final Random RANDOM = new Random();

	private List<Layer> list;

	private Engine engine;
	private final Handler handler = new Handler();

	private final Runnable drawThread = new Runnable() {

		@Override
		public void run() {
			logic();
			draw();
		}
	};

	public WallpaperService() {

	}

	@Override
	public Engine onCreateEngine() {
		engine = new WallpaperEngine();
		return engine;
	}

	// ************************WallpaperEngine************************//

	public class WallpaperEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener {

		public WallpaperEngine() {
			paint = new Paint();
			paint.setStrokeWidth(4);
			paint.setTextSize(16);
			paint.setAntiAlias(true);
			paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));

			loadBitmap();
			list = new ArrayList<Layer>();
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

		}

		// 是否可见状态变更时触发
		@Override
		public void onVisibilityChanged(boolean visible) {
			WallpaperService.this.visible = visible;
			if (visible) {
				draw();
			} else {
				handler.removeCallbacks(drawThread);
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			if (width > 0 && height > 0) {
				WallpaperService.this.width = width;
				WallpaperService.this.height = height;
			}
			super.onSurfaceChanged(holder, format, width, height);
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			super.onTouchEvent(event);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
			WallpaperService.this.xPixelOffset = xPixelOffset;
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
		}
	}

	private void loadBitmap() {
		background = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/bg8.png"));
		mountain = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/mountain.png"));
		stars = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/stars.png"));
		moon = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/moon.png"));
		branchRight = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/branch8_r.png"));
		branchLeft = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/branch8_l.png"));
		sakurasLeft = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/sakura8s_l.png"));
		sakurasRight = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/sakura8s_r.png"));
		flower1 = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/sakura8_1.png"));
		flower2 = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/sakura8_1s.png"));
		petal1 = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/sakura8_2.png"));
		petal2 = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/sakura8_3.png"));

	}

	private void draw() {
		SurfaceHolder holder = engine.getSurfaceHolder();
		Canvas canvas = holder.lockCanvas(null);
		
		if (canvas != null) {
			canvas.drawBitmap(background, xPixelOffset, 0, null);
			canvas.drawBitmap(mountain, xPixelOffset + 100, 200, null);
			canvas.drawBitmap(stars, xPixelOffset, 0, null);
			canvas.drawBitmap(moon, xPixelOffset + width - moon.getWidth(), 70, null);
			canvas.drawBitmap(branchLeft, xPixelOffset, 0, null);
			canvas.drawBitmap(branchRight, xPixelOffset + background.getWidth() - branchRight.getWidth(), 0, null);
			canvas.drawBitmap(sakurasLeft, xPixelOffset, 0, null);
			canvas.drawBitmap(sakurasRight, xPixelOffset + background.getWidth() - sakurasRight.getWidth(), 0, null);

			drawDynamic(canvas);
			holder.unlockCanvasAndPost(canvas);
		}

		if (visible) {
			handler.postDelayed(drawThread, 20);
		}
	}

	private void drawDynamic(Canvas canvas) {
		for (Layer layer : list) {
			layer.draw(canvas, xPixelOffset, 0);
		}
	}

	protected void logic() {
		time_count_flower++;
		if (time_count_flower >= time_flower) {
			Flower flower;
			if (nextRandomInt(10) > 5) {
				flower = new Flower(flower1);
			} else {
				flower = new Flower(flower2);
			}

			int px = nextRandomInt(width * 2 - flower.getWidth());
			flower.setPostion(px, 0);

			list.add(flower);
			time_count_flower = 0;
		}

		time_count_petal++;
		if (time_count_petal >= time_petal) {
			Petal petal;
			if (nextRandomInt(10) > 5) {
				petal = new Petal(petal1);
			} else {
				petal = new Petal(petal2);
			}

			int px = nextRandomInt(width * 2 - petal.getWidth());
			petal.setPostion(px, 0);

			list.add(petal);
			time_count_petal = 0;
		}

		for (Iterator<Layer> it = list.iterator(); it.hasNext();) {
			if (it.next().getY() > height) {
				it.remove();
			}
		}
	}

	public static int nextRandomInt(int max) {
		int n = RANDOM.nextInt();
		return (n < 0 ? -n : n) % max;
	}

}
