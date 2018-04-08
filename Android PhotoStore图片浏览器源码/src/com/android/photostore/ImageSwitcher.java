/*****************Copyright (C), 2010-2015, FORYOU Tech. Co., Ltd.********************/
package com.android.photostore;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.photostore.zoom.ImageZoomView;
import com.android.photostore.zoom.SimpleZoomListener;
import com.android.photostore.zoom.ZoomState;
import com.android.photostore.zoom.SimpleZoomListener.ControlType;

/**
 * @Filename: ImageSwitcher.java
 * @Author: wanghb
 * @Email: wanghb@foryouge.com.cn
 * @CreateDate: 2011-7-15
 * @Description: description of the new class
 * @Others: comments
 * @ModifyHistory:
 */
public class ImageSwitcher extends Activity {

	private int mIndex;

	private int mItemwidth;
	private int mItemHerght;

	private ArrayList<String> pathes;

	private ProgressBar mProgressBar;

	/** Image zoom view */
	private ImageZoomView mZoomView;

	/** Zoom state */
	private ZoomState mZoomState;

	/** On touch listener for zoom view */
	private SimpleZoomListener mZoomListener;

	private Bitmap zoomBitmap;

	private ImageView mMovedItem;
	private boolean isMoved;

	private FlingGallery mFlingGallery;


	public int getmIndex() {
		return mIndex;
	}


	public void updateState(int visibility) {
		mProgressBar.setVisibility(visibility);
		mFlingGallery.setCanTouch(View.GONE == visibility);
	}


	private boolean isViewIntent() {
		String action = getIntent().getAction();
		return Intent.ACTION_VIEW.equals(action);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mItemwidth = dm.widthPixels;
		mItemHerght = dm.heightPixels;
		// mInflater = LayoutInflater.from(this);
		if (!isViewIntent()) {
			pathes = intent.getStringArrayListExtra("pathes");
			mIndex = intent.getIntExtra("index", 0);
		} else {
			pathes = new ArrayList<String>();
			pathes.add(intent.getData().getPath());
			mIndex = 0;
		}

		setContentView(R.layout.myhorizontalview);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_circular);
		mMovedItem = (ImageView) findViewById(R.id.removed);
		mFlingGallery = (FlingGallery) findViewById(R.id.horizontalview);
		mZoomView = (ImageZoomView) findViewById(R.id.zoomview);

		mZoomState = new ZoomState();
		mZoomListener = new SimpleZoomListener();
		mZoomListener.setmGestureDetector(new GestureDetector(this, new MyGestureListener()));

		mZoomListener.setZoomState(mZoomState);
		mZoomListener.setControlType(ControlType.ZOOM);
		mZoomView.setZoomState(mZoomState);
		mZoomView.setOnTouchListener(mZoomListener);

		// hsv = (MyHorizontalScrollView)
		mFlingGallery.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,
				pathes) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return new GalleryViewItem(getApplicationContext(), position);
			}
		}, mIndex);
	}


	public void goneTempImage() {
	}


	private Bitmap getDrawable(int index, int zoom) {
		if (index >= 0 && index < pathes.size()) {
			String path = pathes.get(index);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int mWidth = options.outWidth;
			int mHeight = options.outHeight;
			int s = 1;
			while ((mWidth / s > mItemwidth * 2 * zoom) || (mHeight / s > mItemHerght * 2 * zoom)) {
				s *= 2;
			}

			options = new BitmapFactory.Options();
			options.inPreferredConfig = Config.ARGB_8888;
			options.inSampleSize = s;
			Bitmap bm = BitmapFactory.decodeFile(path, options);

			if (bm != null) {
				int h = bm.getHeight();
				int w = bm.getWidth();

				float ft = (float) ((float) w / (float) h);
				float fs = (float) ((float) mItemwidth / (float) mItemHerght);

				int neww = ft >= fs ? mItemwidth * zoom : (int) (mItemHerght * zoom * ft);
				int newh = ft >= fs ? (int) (mItemwidth * zoom / ft) : mItemHerght * zoom;

				float scaleWidth = ((float) neww) / w;
				float scaleHeight = ((float) newh) / h;

				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				bm = Bitmap.createBitmap(bm, 0, 0, w, h, matrix, true);
				return bm;
			}
		}
		return null;
	}


	private void resetZoomState() {
		int currentIndex = mFlingGallery.getCurrentPosition();
		if (zoomBitmap != null) {
			zoomBitmap.recycle();
		}
		zoomBitmap = getDrawable(currentIndex,3);
		mZoomView.setImage(zoomBitmap);

		mZoomListener.setControlType(ControlType.ZOOM);
		mZoomState.setPanX(0.5f);
		mZoomState.setPanY(0.5f);
		mZoomState.setZoom(3f);
		mZoomState.notifyObservers();
	}


	/**
	 * 查看模式
	 * 
	 * @Description:
	 * @Author: wanghb
	 * @Email: wanghb@foryouge.com.cn
	 * @Others:
	 */
	public void goToZoomPage() {
		resetZoomState();
		mFlingGallery.setVisibility(View.GONE);
		isMoved = false;
		mZoomView.setVisibility(View.VISIBLE);
		mMovedItem.setBackgroundColor(0x0000);
		mMovedItem.setVisibility(View.VISIBLE);
	}


	public void goToSwicherPage() {
		mMovedItem.setVisibility(View.GONE);
		mFlingGallery.setVisibility(View.VISIBLE);
		mZoomView.setVisibility(View.GONE);
	}

	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			goToSwicherPage();
			return true;
		}
	}


	public void movedClick(View v) {
		isMoved = !isMoved;
		if (isMoved) {
			mZoomListener.setControlType(ControlType.PAN);
			mMovedItem.setBackgroundColor(R.drawable.pressed_application_background);
		} else {
			mZoomListener.setControlType(ControlType.ZOOM);
			mMovedItem.setBackgroundColor(0x0000);
		}
	}


	@Override
	protected void onDestroy() {
		if (zoomBitmap != null) {
			zoomBitmap.recycle();
		}
		super.onDestroy();
	}

	private class GalleryViewItem extends LinearLayout {

		public GalleryViewItem(Context context, int position) {
			super(context);

			this.setOrientation(LinearLayout.VERTICAL);

			this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));

			ImageView iv = new ImageView(context);
			iv.setImageBitmap(getDrawable(position, 1));

			this.addView(iv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));

		}
	}
}
