package cn.sharp.android.ncr;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import cn.sharp.android.ncr.ocr.OCRItems;
import cn.sharp.android.ncr.ocr.OCRManager;
import cn.sharp.android.ncr.thread.RecFromFileThread;
import cn.sharp.android.ncr.thread.StopWorkThread;
import cn.sharp.android.ncr.view.OriginalImageView;
import cn.sharp.android.ncr.view.OriginalImageView.OnZoomInToMaxListener;
import cn.sharp.android.ncr.view.OriginalImageView.OnZoomOutToMinListener;

/**
 * 从sdcard 读取图片后识别
 * 
 * @author shao chuanchao
 * 
 */
public class NamecardImageGallery extends Activity implements
		OnItemClickListener, OnZoomInToMaxListener, OnZoomOutToMinListener,
		OnTouchListener {
	private final static String TAG = "NamecardImageGallery";
	private final static int MENU_ITEM_REC_SELECTED_NAMECARD = 0;
	// A view that shows items in a center-locked, horizontally scrolling list
	private Gallery gallery;
	//缩放控件
	private ZoomControls zoomController;
	private OriginalImageView originalImageView;
	private boolean sdcardCanRead;
	private String[] imageNames;
	private GestureDetector gesture;

	private final static int DIALOG_REC_FAILURE = 0;
	private final static int DIALOG_REC_SUCCESS = 1;
	private final static int DIALOG_REC_PROGRESS = 2;
	private final static int DIALOG_INTERRUPT_DECODE_IMAGE_OR_REC = 3;
	private final static int DIALOG_STOP_REC_PROGRESS = 4;

	private OCRManager ocrManager;
	private OCRItems ocrItems;
	private boolean isRec;
	private boolean isRecProgressDialogShown;
	private boolean isInterruptDialogShown;
	private boolean isStoppingRec;
	private RecFromFileThread recFromFileThread;
	private StopWorkThread stopWorkThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//获取sdcard当前状态
		String sdcardState = Environment.getExternalStorageState();
		//取得sdcard下得jpg文件
		
		if (sdcardState.equals(Environment.MEDIA_MOUNTED)
				|| sdcardState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			sdcardCanRead = true;
			/**
			 * begin to get file list in /sdcard/namecard folder
			 */
			File baseFolder = new File(Constants.ROOT_DIR);
			if (baseFolder.exists()) {
				imageNames = baseFolder.list(new JpgFileFilter());
				for(int i=0;i<imageNames.length;i++){
					Log.e("imageName", imageNames[i]);
				}
			}
		} else {
			sdcardCanRead = false;
		}

		setContentView(R.layout.card_img_gallery);
		gallery = (Gallery) findViewById(R.id.img_gallery);
		gallery.setAdapter(new ThumbImageAdapter(this));
		gallery.setUnselectedAlpha(70);
		gallery.setOnItemClickListener(this);
		zoomController = (ZoomControls) findViewById(R.id.img_zoom_control);
		//放大监听
		zoomController.setOnZoomInClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				zoomController.setIsZoomOutEnabled(true);
				originalImageView.zoom(1.2f);
				originalImageView.invalidate();
			}
		});
		//缩小监听
		zoomController.setOnZoomOutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				zoomController.setIsZoomInEnabled(true);
				originalImageView.zoom(0.8f);
				originalImageView.invalidate();
			}
		});
		originalImageView = (OriginalImageView) findViewById(R.id.original_image);
		originalImageView.setOnZoomInToMaxListener(this);
		originalImageView.setOnZoomOutToMinListener(this);
		originalImageView.setOnTouchListener(this);
		registerForContextMenu(gallery);
		gesture = new GestureDetector(this, new MyGestureListener());
		ocrManager = new OCRManager(handler);
		isStoppingRec = isRecProgressDialogShown = isInterruptDialogShown = false;
		if (imageNames != null) {
			gallery.setSelection(0);
			showBigImage(0);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		//对sdcard  状态进行判断
		//是否读取到图片  做判断
		if (sdcardCanRead) {
			/** no image file, show a toast dialog */
			if (imageNames == null) {
				zoomController.setVisibility(View.INVISIBLE);
				Toast.makeText(this, R.string.no_image_in_sdcard,
						Toast.LENGTH_LONG).show();
			}
		} else {
			zoomController.setVisibility(View.INVISIBLE);
			Toast.makeText(this, R.string.sdcard_not_ready, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_REC_FAILURE:
			return new AlertDialog.Builder(this).setTitle(R.string.error)
					.setMessage(R.string.rec_failure)
					.setPositiveButton(R.string.cancel, null).create();
		case DIALOG_REC_PROGRESS:
			ProgressDialog recProgressDialog = new ProgressDialog(
					NamecardImageGallery.this);
			recProgressDialog.setTitle(R.string.waiting);
			recProgressDialog.setMessage(getResources().getString(
					R.string.rec_in_progress));
			recProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			recProgressDialog.setCancelable(false);
			recProgressDialog
					.setOnKeyListener(new DialogInterface.OnKeyListener() {

						@Override
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								Log.e(TAG, "back key pressed while recognizing");
								tryDismissRecProgressDialog();
								showDialog(DIALOG_INTERRUPT_DECODE_IMAGE_OR_REC);
								isInterruptDialogShown = true;
								return true;
							}
							return false;
						}
					});
			return recProgressDialog;
		case DIALOG_INTERRUPT_DECODE_IMAGE_OR_REC:
			return new AlertDialog.Builder(this)
					.setTitle(R.string.warning)
					.setMessage(R.string.interrupt_rec_confirm)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									tryDismissRecProgressDialog();
									if (isRec) {
										if (recFromFileThread != null) {
											showDialog(DIALOG_STOP_REC_PROGRESS);
											recFromFileThread.forceStop();
											stopWorkThread = new StopWorkThread(
													recFromFileThread,
													handler,
													MessageId.REC_THREAD_STOPPED);
											stopWorkThread.start();
											recFromFileThread = null;
											isStoppingRec = true;
											Log.i(TAG,
													"rec process stopped by user");
										} else {
											Log.i(TAG, "recThread==null");
										}
									}
									isInterruptDialogShown = false;
									Log.d(TAG, "snap button enabled again");
								}
							})
					.setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									isInterruptDialogShown = false;
									if (isRec) {
										showDialog(DIALOG_REC_PROGRESS);
										isRecProgressDialogShown = true;
									}
								}
							}).setCancelable(false).create();
		case DIALOG_STOP_REC_PROGRESS:
			return ProgressDialog.show(NamecardImageGallery.this, "",
					getResources().getString(R.string.stopping_rec));
		default:
			return super.onCreateDialog(id);
		}
	}

	/**
	 * 去掉进度条
	 */
	private void tryDismissRecProgressDialog() {
		if (isRecProgressDialogShown) {
			removeDialog(DIALOG_REC_PROGRESS);
			isRecProgressDialogShown = false;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (imageNames != null) {
			MenuItem item1 = menu.add(0, MENU_ITEM_REC_SELECTED_NAMECARD,
					MENU_ITEM_REC_SELECTED_NAMECARD,
					R.string.rec_selected_namecard);
			item1.setIcon(R.drawable.rec_selected_card);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ITEM_REC_SELECTED_NAMECARD:
			/** start to rec */
			recSelectedCard(gallery.getSelectedItemPosition());
			break;
		}
		return true;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (imageNames != null) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			/** start to view large image, not implemented yet */
			recSelectedCard(info.position);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 
	 * @param position
	 *            the selected item index of gallery
	 */
	private void recSelectedCard(int position) {
		if (position < imageNames.length) {
			showDialog(DIALOG_REC_PROGRESS);
			isRecProgressDialogShown = true;
			isRec = true;
			File jpegFile = new File(Constants.ROOT_DIR + imageNames[position]);
			Log.e("路径", Constants.ROOT_DIR + imageNames[position]);
			recFromFileThread = new RecFromFileThread(handler, ocrManager,
					jpegFile);
			recFromFileThread.start();
			Log.e(TAG, "start to rec " + jpegFile.getAbsolutePath());
		} else {
			Log.i(TAG, "invalid gallery selection index:" + position);
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "get message, id:" + msg.what);

			switch (msg.what) {
			case MessageId.NAMECARD_REC_SUCCESS:
				isRec = false;
				tryDismissRecProgressDialog();
				ocrItems = (OCRItems) msg.obj;
				Log.e("result", ocrItems.other+"11");
				Log.e("result", ocrItems.other + "@@@@" + ocrItems.adrInfo
						+ "@@@" + ocrItems.familyName + "@@@"
						+ ocrItems.familyKana);
				Intent intent = new Intent(NamecardImageGallery.this,
						DisplayRecResult.class);
				intent.putExtra(OCRManager.OCR_ITEMS, ocrItems);
				startActivity(intent);
				break;
			case MessageId.NAMECARD_REC_FAILURE:
				isRec = false;
				tryDismissRecProgressDialog();
				showDialog(DIALOG_REC_FAILURE);
				break;
			case MessageId.REC_THREAD_STOPPED:
				Log.d(TAG, "rec thread stop msg received");
				stopWorkThread = null;
				removeDialog(DIALOG_STOP_REC_PROGRESS);
				isRec = false;
				isStoppingRec = false;
				break;
			}
		}

	};

	/**
	 * 画廊的适配器
	 * 
	 * @author shao chuanchao
	 * 
	 */
	private class ThumbImageAdapter extends BaseAdapter {
		private int galleryItemBackground;
		private Context context;
		private BitmapFactory.Options bitmapOpts;

		public ThumbImageAdapter(Context c) {
			context = c;
			bitmapOpts = new BitmapFactory.Options();
			bitmapOpts.inSampleSize = 16;
			TypedArray a = obtainStyledAttributes(R.styleable.NamecardImageGalleryStyle);
			galleryItemBackground = a
					.getResourceId(
							R.styleable.NamecardImageGalleryStyle_android_galleryItemBackground,
							0);
			a.recycle();
		}

		@Override
		public int getCount() {
			if (imageNames == null)
				return 0;
			return imageNames.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(context);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new Gallery.LayoutParams(136, 88));
				imageView.setBackgroundResource(galleryItemBackground);
			} else {
				imageView = (ImageView) convertView.getTag();
			}
			Bitmap bitmap = BitmapFactory.decodeFile(Constants.ROOT_DIR
					+ imageNames[position], bitmapOpts);
			imageView.setImageBitmap(bitmap);

			return imageView;
		}
	}

	/**
	 * 文件过滤器 根据文件名 筛选出.jpg格式的文件
	 * 
	 * @author shao chuanchao
	 * 
	 */
	private class JpgFileFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String filename) {
			return filename.endsWith(".jpg");
		}

	}

	/**
	 * 多点触摸
	 * 
	 * @author shao chuanchao
	 * 
	 */
	private class MyGestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float velocityX, float velocityY) {
			Log.d(TAG, "move dx:" + velocityX + ", move dy:" + velocityY);
			originalImageView.move(-velocityX, -velocityY);
			originalImageView.invalidate();
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (originalImageView.getDrawable() != null) {
				if (zoomController.getVisibility() == View.INVISIBLE)
					zoomController.setVisibility(View.VISIBLE);
				else
					zoomController.setVisibility(View.INVISIBLE);
			} else {
				zoomController.setVisibility(View.INVISIBLE);
			}
			return true;
		}
	}

	/**
	 * 
	 * @param index
	 *            the image name index in imageNames array
	 */
	private void showBigImage(int index) {
		if (index >= 0 && index < imageNames.length) {
			lastItemIndex = index;
			bigBitmap = BitmapFactory.decodeFile(Constants.ROOT_DIR
					+ imageNames[index]);
			zoomController.setIsZoomInEnabled(true);
			zoomController.setIsZoomOutEnabled(true);
			originalImageView.resetImage();
			originalImageView.setImageBitmap(bigBitmap);
		}
	}

	private int lastItemIndex = -1;
	private Bitmap bigBitmap;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (lastItemIndex != arg2) {
			if (bigBitmap != null) {
				bigBitmap.recycle();
			}
			showBigImage(arg2);
		}
	}

	@Override
	public void OnZoomInToMax() {
		zoomController.setIsZoomInEnabled(false);
	}

	@Override
	public void OnZoomOutToMin() {
		zoomController.setIsZoomOutEnabled(false);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v == originalImageView) {
			Log.d(TAG, "iamge view onTouch()");
			gesture.onTouchEvent(event);
			return true;
		}
		return false;
	}

}
