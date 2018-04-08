/*****************Copyright (C), 2010-2015, FORYOU Tech. Co., Ltd.********************/
package com.android.photostore;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.photostore.Constant.gridItemEntity;

/**
 * @Filename: GridImageView.java
 * @Author: wanghb
 * @Email: wanghb@foryouge.com.cn
 * @CreateDate: 2011-7-14
 * @Description: description of the new class
 * @Others: comments
 * @ModifyHistory:
 */
public class GridImageView extends Activity {
	private LayoutInflater mInflater;
	private int currentConlumID = -1;
	private int currentCount = 1;
	private int displayHeight;
	private LinearLayout data;

	private int itemh = 150;
	private int itemw = 150;

	private ArrayList<String> imagePathes;

	private boolean exit;
	private boolean isWait;
	
	private boolean firstRun = true;

	private Thread mThread = new Thread() {
		public void run() {
			for (int i = 0; i < imagePathes.size() && !exit; i++) {
				if(isWait){
					isWait = !isWait;
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				String path = imagePathes.get(i);
				if (new File(path).exists()) {
					
					gridItemEntity gie = new gridItemEntity();
					Bitmap bm = getDrawable(i, 2);
					if (bm != null) {
						if(isWait){
							isWait = !isWait;
							synchronized (this) {
								try {
									this.wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
						gie.image = new BitmapDrawable(bm);
						gie.path = path;
						gie.index = i;
						android.os.Message msg = new Message();
						msg = new Message();
						msg.what = 0;
						msg.obj = gie;
						mHandler.sendMessage(msg);
					}
				}
			}
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					gridItemEntity gie = (gridItemEntity) msg.obj;
					if (gie != null) {

						int num = displayHeight / itemh;
						num = num == 0 ? 1 : num;

						LinearLayout ll;
						if ((currentCount - 1) % num > 0) {
							ll = (LinearLayout) data.findViewWithTag("columnId_" + currentConlumID);
						} else {
							ll = (LinearLayout) mInflater.inflate(R.layout.item_column, null);
							currentConlumID--;
							ll.setTag("columnId_" + currentConlumID);
							for (int j = 0; j < num; j++) {
								LinearLayout child = new LinearLayout(GridImageView.this);
								child.setLayoutParams(new LayoutParams(itemw, itemh));
								child.setTag("item_" + j);
								ll.addView(child);
							}
							data.addView(ll);
						}

						int step = currentCount % num - 1;
						if (step == -1) {
							step = num - 1;
						}
						LinearLayout child = (LinearLayout) ll.findViewWithTag("item_" + step);
						// child.setBackgroundColor(R.color.bright_text_dark_focused);
						child.setBackgroundResource(R.drawable.grid_selector);
						child.setTag(gie);
						child.setOnClickListener(imageClick);
						child.setPadding(10, 10, 10, 10);
						//						
						ImageView v = new ImageView(GridImageView.this);
						// v.setLayoutParams(new LayoutParams(itemw,itemh));
						v.setImageDrawable(gie.image);
						child.addView(v);
						currentCount++;
					}
					break;

				default:
					break;
			}
//			removeMessages(msg.what);
		}
	};

	private OnClickListener imageClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			gridItemEntity gie = (gridItemEntity) view.getTag();
			Intent it = new Intent(GridImageView.this, com.android.photostore.ImageSwitcher.class);
			it.putStringArrayListExtra("pathes", imagePathes);
			it.putExtra("index", gie.index);
			startActivity(it);
			if(mThread.isAlive()){
				isWait = true;
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.horizontalview);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		displayHeight = dm.heightPixels;

		mInflater = LayoutInflater.from(this);

		Intent intent = getIntent();
		imagePathes = intent.getStringArrayListExtra("data");
		data = (LinearLayout) findViewById(R.id.layout_webnav);
		// mAdapter.getFilePathes().set(index, object);
	}
	
	@Override
	protected void onResume() {
		if(mThread.isAlive()){
			synchronized(mThread){
				mThread.notify();
			}
		}else{
			if(firstRun){
				firstRun = !firstRun;
				mThread.start();
			}
		}
		super.onResume();
	}


	@Override
	protected void onDestroy() {
		exit = true;
		super.onDestroy();
	}
	
	private Bitmap getDrawable(int index, int zoom) {
		if (index >= 0 && index < imagePathes.size()) {
			String path = imagePathes.get(index);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int mWidth = options.outWidth;
			int mHeight = options.outHeight;
			int s = 1;
			while ((mWidth / s > itemw * 2 * zoom) || (mHeight / s > itemh * 2 * zoom)) {
				s *= 2;
			}

			options = new BitmapFactory.Options();
			options.inSampleSize = s;
			options.inPreferredConfig = Config.ARGB_8888;
			Bitmap bm = BitmapFactory.decodeFile(path, options);

			if (bm != null) {
				int h = bm.getHeight();
				int w = bm.getWidth();

				float ft = (float) ((float) w / (float) h);
				float fs = (float) ((float) itemw / (float) itemh);

				int neww = ft >= fs ? itemw * zoom : (int) (itemh * zoom * ft);
				int newh = ft >= fs ? (int) (itemw * zoom / ft) : itemh * zoom;

				float scaleWidth = ((float) neww) / w;
				float scaleHeight = ((float) newh) / h;

				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				bm = Bitmap.createBitmap(bm, 0, 0, w, h, matrix, true);

				// Bitmap bm1 = Bitmap.createScaledBitmap(bm, w, h, true);
//				if (!bm.isRecycled()) {// 先判断图片是否已释放了
//					bm.recycle();
//				}
				return bm;
			}
		}
		return null;
	}

}
