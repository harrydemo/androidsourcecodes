package cn.com.ldci.plants;

import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class SeedBank {
	private Context context;
	private Resources res;
	private Plants plant;
	/**
	 * 种植物的标志
	 */
	private boolean addflag = false;
	protected Bitmap[] seedImage;
	protected Bitmap seedbank;
	private byte currentState = -1;
	private static final byte mousedownState = 0;
	private static final byte mousemoveState = 1;
	private static final byte mouseupState = 2;
	int type = 0;
	Vector<Rect> rv = new Vector<Rect>();
	private float x;
	private float y;
	private float t;
	private float l;
	private float r;
	private float b;
	private float seekbankx = 90;
	private float seekbanky = 0;
	private boolean[] disable = new boolean[6];
	static boolean[] plantflag = new boolean[]{true,true,true,true,true,true};
	static float W;
	static float H;
	static final float startx = 152;

	Paint paint = new Paint();
	Bitmap bitmap = null;
	Paint apaint = new Paint();
	Paint apiant1 = new Paint();
	public SeedBank(Context context) {
		this.context = context;
		res = context.getResources();
		apaint.setAlpha(80);
		apiant1.setAlpha(60);
		initarray();

	}

	// 初始化disable数组
	private void initarray() {
		for (int i = 0; i < 6; i++) {
			if (GameView.suncount >= (i + 1) * 50) {
				disable[i] = true;
			} else {
				disable[i] = false;
			}
		}
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(seedbank, seekbankx, seekbanky, paint);
		canvas.drawText("" + GameView.suncount, 120, 50, paint);
		for (int i = 0; i < seedImage.length; i++) {
			if (disable[i]) {
				canvas.drawBitmap(seedImage[i], startx
						+ (seedImage[0].getWidth() + 4) * i, 4, paint);
			} else {
				canvas.drawBitmap(seedImage[i], startx
						+ (seedImage[0].getWidth() + 4) * i, 4, apaint);
			}
		}
		W = seedImage[0].getWidth();
		H = seedImage[0].getHeight();
		switch (currentState) {
		case mousedownState:
			
//			break;
		case mousemoveState:
			if (bitmap != null) {
				canvas.drawBitmap(bitmap, x - 12, y - 12, paint);
			}
			break;
		case mouseupState:
			break;
		}
//		synchronized(rv){
			for(int i = 0 ; i < rv.size() ; i++){//for(Rect rt:rv){
				Rect rt = rv.get(i);
				canvas.drawRect(rt, apiant1);
//			}
		}
		
	}

	public void touchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			currentState = mousedownState;
			x = event.getX();
			y = event.getY();
			bitmap = null;
			Log.e("app", "checkxy");
			if (checkIn(x, y)) {
				Log.e("app", "checkxynei");
				if (checkXY(x, y, startx, 0, startx + W + 4, H)) {
					Log.e("app", "1");
					if (disable[0]) {
						Log.e("app", "1if");
						if(plantflag[0]){
							l = startx;
							r = startx + W + 4;
							b = H;
							bitmap = BitmapFactory.decodeResource(res,
									R.drawable.plant1);
//							canvas.drawBitmap(bitmap, x - 12, y - 12, paint);
							addflag = true ;
							// plant =
							type = 1;
						}
					}else {
						addflag = false;// 没有可以种的植物 就不执行touch方法的鼠标放开事件逻辑
						return ;
					}
				} else if (checkXY(x, y, startx + W + 4, 0, startx + 2 *( W + 4),
						H)) {
					Log.e("app", "2");
					if (disable[1]) {
						Log.e("app", "2if");
						if(plantflag[1]){
							l = startx + W + 4;
							r = startx + 2 * (W + 4);
							b = H;
							bitmap = BitmapFactory.decodeResource(res,
									R.drawable.plant2);
//							canvas.drawBitmap(bitmap, x - 12, y - 12, paint);
							addflag = true ;
							// plant =
							type = 2;
						}
					}else {
						addflag = false;// 没有可以种的植物 就不执行touch方法的鼠标放开事件逻辑
						return ;
					}
				} else if (checkXY(x, y, startx + 2 *( W + 4), 0, startx + 3 * (W
						+ 4), H)) {
					Log.e("app", "3");
					if (disable[2]) {
						Log.e("app", "3if");
						if(plantflag[2]){
							l = startx + 2 * (W + 4);
							r = startx + 3 * (W + 4);
							b = H;
							bitmap = BitmapFactory.decodeResource(res,
									R.drawable.plant3);
							// plant =
//							canvas.drawBitmap(bitmap, x - 12, y - 12, paint);
							addflag = true ;
							type = 3;
						}
					}else {
						addflag = false;// 没有可以种的植物 就不执行touch方法的鼠标放开事件逻辑
						return ;
					}
				} else if (checkXY(x, y, startx + 3 * (W + 4), 0, startx + 4 *( W
						+ 4), H)) {
					Log.e("app", "4");
					if (disable[3]) {
						Log.e("app", "4if");
						if(plantflag[3]){
							l = startx + 3 * (W + 4);
							r = startx + 4 * (W + 4);
							b = H;
							bitmap = BitmapFactory.decodeResource(res,
									R.drawable.plant4);
							// plant =
//							canvas.drawBitmap(bitmap, x - 12, y - 12, paint);
							addflag = true ;
							type = 4;
						}
					}else {
						addflag = false;// 没有可以种的植物 就不执行touch方法的鼠标放开事件逻辑
						return ;
					}
				} else if (checkXY(x, y, startx + 4 * (W + 4), 0, startx + 5 * (W
						+ 4), H)) {
					Log.e("app", "5");
					if (disable[4]) {
						Log.e("app", "5if");
						if(plantflag[4]){
							l = startx + 4 * (W + 4);
							r = startx + 5 * (W + 4);
							b = H;
							bitmap = BitmapFactory.decodeResource(res,
									R.drawable.plant1);
							// plant =
//							canvas.drawBitmap(bitmap, x - 12, y - 12, paint);
							addflag = true ;
							type = 5;
						}
					}else {
						addflag = false;// 没有可以种的植物 就不执行touch方法的鼠标放开事件逻辑
						return ;
					}
				} else if (checkXY(x, y, startx + 5 * (W + 4), 0, startx + 6 * (W
						+ 4), H)) {
					Log.e("app", "6");
					if (disable[5]) {
						Log.e("app", "6if");
						if(plantflag[5]){
							l = startx + 5 * (W + 4);
							r = startx + 6 * (W + 4);
							b = H;
							bitmap = BitmapFactory.decodeResource(res,
									R.drawable.plant2);
							// plant =
//							canvas.drawBitmap(bitmap, x - 12, y - 12, paint);
							addflag = true ;
							type = 6;
						}
					} else {
						addflag = false;// 没有可以种的植物 就不执行touch方法的鼠标放开事件逻辑
						return ;
					}
				}
			} else {
				addflag = false;// 没有可以种的植物 就不执行touch方法的鼠标放开事件逻辑
			}
			break;
		case MotionEvent.ACTION_MOVE:
			currentState = mousemoveState;
			x = event.getX();
			y = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			currentState = mouseupState;
			Log.e("app", "addflag");
			if (addflag) {
				Log.e("app", "inside");
				if (MainActivity.SX <= x && x < MainActivity.EX
						&& MainActivity.SY <= y && MainActivity.EY > y) {// 向容器增加植物
					int i = (int) ((x - MainActivity.SX) / MainActivity.EXL);
					int j = (int) ((y - MainActivity.SY) / MainActivity.EYL);
					Log.e("app", "grid");
					if (MainActivity.grid[i][j]) {
						float px = MainActivity.EXL * i + MainActivity.SX
								+ MainActivity.XL; // 固定x坐标
						float py = MainActivity.EYL * j + MainActivity.SY
								+ MainActivity.YL; // 固定y坐标
						Log.e("app", "type");
						if (type != 0) {
							plant = new Plants(1, px, py);
							plant.plantsBitmap = GameView.Plant1;
							GameView.plants.add(plant);
							MainActivity.grid[i][j] = false;
							// 减阳光数
							GameView.suncount-=50;
							initarray();
							type = 0;
							bitmap = null;
							rv.add(new Rect((int)l,(int)t,(int)r,(int)b));
							int k = (int) ((l-startx)/(W+4));
							plantflag[k] = false ;
							//rect的位置清0
							l = 0;
							t = 0;
							r = 0;
							b = 0;
						}
					} else {
						Log.e("app", "false");
					}
				}
			}

			break;
		}
	}

	/***
	 * 
	 * @param x
	 *            :鼠标x坐标
	 * @param y
	 *            :鼠标y坐标
	 * @return 在自身范围里 return true 否则返回 false
	 */
	public boolean checkIn(float x, float y) {
		if (checkXY(x, y, startx, seekbanky, startx + 6 * (W + 4), H)) {
			return true;
		}
		return false;
	}

	/***
	 * 检测点(cx,cy)是否在方形(sx,sy,ex,ey)范围里
	 * 
	 * @param cx
	 *            :被检测点的x坐标
	 * @param cy
	 *            :被检测点的y坐标
	 * @param sx
	 *            :方形的左上角x坐标
	 * @param sy
	 *            :方形的左上角y坐标
	 * @param ex
	 *            :方形的右下角x坐标
	 * @param ey
	 *            :方形的有效较y坐标
	 * @return 在方形范围里return ture 不在方形范围里return false
	 */
	private boolean checkXY(float cx, float cy, float sx, float sy, float ex,
			float ey) {
		if (cx >= sx && cx <= ex && cy >= sy && cy <= ey) {
			return true;
		}
		return false;
	}
}
