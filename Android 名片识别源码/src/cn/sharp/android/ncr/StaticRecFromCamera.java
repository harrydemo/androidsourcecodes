package cn.sharp.android.ncr;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.sharp.android.ncr.image.ImageHelper;
import cn.sharp.android.ncr.ocr.OCRItems;
import cn.sharp.android.ncr.ocr.OCRManager;
import cn.sharp.android.ncr.thread.RecThread;
import cn.sharp.android.ncr.thread.StopWorkThread;
import cn.sharp.android.ncr.view.ScannerView;
/**
 * 拍照识别
 * @author shao chuanchao
 *
 */
public class StaticRecFromCamera extends Activity implements Callback,
		OnClickListener {
	private final static String TAG = "StaticRecFromCamera";
	private final static int DIALOG_REC_FAILURE = 0; // 失败
	private final static int DIALOG_REC_SUCCESS = 1; // 成功
	private final static int DIALOG_REC_PROGRESS = 2;
	private final static int DIALOG_INTERRUPT_DECODE_IMAGE_OR_REC = 5;
	private final static int DIALOG_STOP_REC_PROGRESS = 7;
	private final static int ACTIVITY_REC_NAMECARD = 0;

	public final static int REC_PROGRESS_STYLE_NORMAL = 0; //正常
	public final static int REC_PROGRESS_STYLE_SCANNER = 1;//扫描

	private final static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd_HH_mm_ss"); //日期格式
	
	private Camera mCamera;
	private SurfaceView surfaceView;
	private ImageButton btnSnap;

	private ShutterCallback shutterCallback;
	private RawPictureCallback rawCallback;
	private JpegPictureCallback jpegCallback;
	private AutoFocusCallback autoFocusCallback;
	private PreviewCallback previewCallback;
	private ScannerView scannerView;
	private OCRManager ocrManager;
	
	private AtomicBoolean isCapturePic;
	private AtomicBoolean isLive;
	private ScannerAnimation recAnimation;
	private boolean isRec;
	private boolean isInterruptDialogShown;
	private boolean isRecProgressDialogShown;

	private boolean isStoppingRec;
	private StopWorkThread stopRecThread;
	private SharedPreferences preference;
	private boolean autoFocus, saveRecImage;
	private int recStyle;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.e(TAG, "get message, id:" + msg.what);
			switch (msg.what) {
			case MessageId.NAMECARD_REC_SUCCESS:
				isRec = false;
				closeCamera();
				if (recStyle == REC_PROGRESS_STYLE_NORMAL) {
					tryDismissInterruptDialog();
					tryDismissRecProgressDialog();
				}
				if (recThread != null) {
					recThread = null;
				} else {
					Log.e(TAG,
							"recSuccess msg received, but recThread is null now");
				}
				OCRItems ocrItems = (OCRItems) msg.obj;
				Intent intent = new Intent(StaticRecFromCamera.this,
						DisplayRecResult.class);
				intent.putExtra(OCRManager.OCR_ITEMS, ocrItems);
				Log.d(TAG, "start result show activity");
				StaticRecFromCamera.this.startActivityForResult(intent,
						ACTIVITY_REC_NAMECARD);
				if (saveRecImage && jpegData != null) {
					Thread saveImageThread = new SaveJpegThread(jpegData);
					saveImageThread.start();
					Log.d(TAG, "image saving thread started");
				}
				break;
			case MessageId.NAMECARD_REC_FAILURE:
				isRec = false;
				if (recStyle == REC_PROGRESS_STYLE_NORMAL) {
					tryDismissInterruptDialog();
					tryDismissRecProgressDialog();
				} else if (recStyle == REC_PROGRESS_STYLE_SCANNER) {
					scannerView.clearAnimation();
				}
				if (recThread != null) {
					recThread = null;
					Log.e(TAG, "rec failure");
					showDialog(DIALOG_REC_FAILURE);
				} else {
					Log.i(TAG,
							"recFailure msg received, but recThread is null now");
				}
				break;
			case MessageId.REC_THREAD_STOPPED:
				Log.d(TAG, "rec thread stop msg received");
				stopRecThread = null;
				removeDialog(DIALOG_STOP_REC_PROGRESS);
				if (isLive.get()) {
					rePreview();
					Log.d(TAG,
							"screen is still alive, camera start  preview after previous rec interrupt");
					btnSnap.setEnabled(true);
				}
				isRec = false;
				isStoppingRec = false;
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		//保持界面常亮
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.rec_camera_preview);
		surfaceView = (SurfaceView) findViewById(R.id.camera_preview);
		scannerView = (ScannerView) findViewById(R.id.rec_progress_scannerview);
		btnSnap = (ImageButton) findViewById(R.id.btn_snap);
		btnSnap.setOnClickListener(this);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		shutterCallback = new ShutterCallback();
		jpegCallback = new JpegPictureCallback();
		autoFocusCallback = new AutoFocusCallback();
		previewCallback = new PreviewCallback();

		isCapturePic = new AtomicBoolean();
		isLive = new AtomicBoolean();

		isPreviewing = false;
		isRec = false;
		isStoppingRec = false;

		ocrManager = new OCRManager(handler);

		preference = PreferenceManager.getDefaultSharedPreferences(this);

		autoFocus = saveRecImage = false;
		scannerView.setVisibility(View.GONE); //不显示
		recAnimation = new ScannerAnimation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isLive.set(true);
		if (!isStoppingRec && !isRec) {
			isCapturePic.set(false);
			startPreview();
			isRecProgressDialogShown = isInterruptDialogShown = false;
			/**
			 * default value for autoFocus is true, keep identical to the value
			 * in layout/pref_settings.xml
			 */
			autoFocus = preference.getBoolean(PrefSettings.PREF_AUTO_FOCUS,
					true);
			/**
			 * default value for saveRecImage is true, keep identical to the
			 * value in layout/pref_settings.xml
			 */
			saveRecImage = preference.getBoolean(
					PrefSettings.PREF_SAVE_REC_IMAGE, true);
		}
		String recStyleStr = preference.getString(
				PrefSettings.PREF_REC_PROGRESS_STYLE, "0");
		recStyle = 0;
		try {
			recStyle = Integer.parseInt(recStyleStr);
		} catch (NumberFormatException e) {

		}

		if (saveRecImage
				&& !Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, R.string.sdcard_no_write_perm,
					Toast.LENGTH_SHORT).show();
			Log.e(TAG, "image will not be outputed to sdcard");
		}
		
		Log.e(TAG, "onResume结束");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "on pause");
		isLive.set(false);
		if (isPreviewing)
			stopPreview();
		closeCamera();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy()");
		super.onDestroy();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_REC_FAILURE:
			return new AlertDialog.Builder(this)
					.setTitle(R.string.error)
					.setMessage(R.string.rec_failure)
					.setPositiveButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (isLive.get()) {
										rePreview();
									}
								}
							}).create();
		case DIALOG_REC_PROGRESS:
			ProgressDialog recProgressDialog = new ProgressDialog(
					StaticRecFromCamera.this);
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
								Log.d(TAG, "back key pressed while recognizing");
								tryDismissRecProgressDialog();
								showDialog(DIALOG_INTERRUPT_DECODE_IMAGE_OR_REC);
								isInterruptDialogShown = true;
								return true;
							}
							return false;
						}
					});
			isRecProgressDialogShown = true;
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
									/**
									 * remove the procedure of decoding bitmap
									 * from jpeg
									 * dismissDecodeImageProgressDialog();
									 */
									if (recStyle == REC_PROGRESS_STYLE_NORMAL)
										tryDismissRecProgressDialog();
									if (isRec) {
										if (recThread != null) {
											showDialog(DIALOG_STOP_REC_PROGRESS);
											recThread.forceStop();
											stopRecThread = new StopWorkThread(
													recThread,
													handler,
													MessageId.REC_THREAD_STOPPED);
											stopRecThread.start();
											recThread = null;
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
										switch (recStyle) {
										case REC_PROGRESS_STYLE_NORMAL:
											showDialog(DIALOG_REC_PROGRESS);
											isRecProgressDialogShown = true;
											break;
										case REC_PROGRESS_STYLE_SCANNER:
											scannerView
													.startAnimation(recAnimation);
											break;
										}
									}
								}
							}).setCancelable(false).create();
		case DIALOG_STOP_REC_PROGRESS:
			return ProgressDialog.show(StaticRecFromCamera.this, "",
					getResources().getString(R.string.stopping_rec));
		default:
			return super.onCreateDialog(id);
		}
	}

	private void tryDismissRecProgressDialog() {
		if (isRecProgressDialogShown) {
			removeDialog(DIALOG_REC_PROGRESS);
			isRecProgressDialogShown = false;
		}
	}

	private void tryDismissInterruptDialog() {
		if (isInterruptDialogShown) {
			removeDialog(DIALOG_INTERRUPT_DECODE_IMAGE_OR_REC);
			isInterruptDialogShown = false;
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it
		// where
		// to draw.
		Log.v(TAG, "surface created");
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's
		// very
		// important to release it when the activity is paused.
		closeCamera();
	}

	private int previewHeight = 0, previewWidth = 0;

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Log.e(TAG, "surface changed");
		if (holder.isCreating()) {
			Log.e(TAG, "is createing surface holder");
			if (!ensureCameraDevice())
				return;
			if (mCamera != null) {
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setPictureSize(1600, 1200);
				parameters.setPreviewSize(w, h);
				previewWidth = w;
				previewHeight = h;
				parameters.setPictureFormat(PixelFormat.JPEG);
				parameters.setPreviewFormat(PixelFormat.YCbCr_420_SP);
				mCamera.setPreviewCallback(previewCallback);
				mCamera.setParameters(parameters);
			}
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				closeCamera();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVITY_REC_NAMECARD:
			Log.e(TAG, "rec-result-display activity finished, result code:"
					+ requestCode);
			finish();
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isRec && recStyle == REC_PROGRESS_STYLE_SCANNER) {
				scannerView.clearAnimation();
				showDialog(DIALOG_INTERRUPT_DECODE_IMAGE_OR_REC);
				isInterruptDialogShown = true;
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	//点击   事件
	public void onClick(View v) {
		if (v == btnSnap) {
			// capture image from camera
			Log.e(TAG, "take picture button clicked");
			if (mCamera != null) {
				Log.e(TAG, "start auto focus");
				isCapturePic.set(true);
				isPreviewing = false;
				v.setEnabled(false);
				v.setVisibility(View.INVISIBLE);
				mCamera.autoFocus(autoFocusCallback);
			} else {
				Log.e(TAG, "camera==null");
			}
		}
	}

	private class ShutterCallback implements Camera.ShutterCallback {

		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			
		}

	
	}

	private class RawPictureCallback implements Camera.PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if (data == null) {
				Log.i(TAG, "======raw picture callback,data==null");
			} else {
				Log.i(TAG, "======raw picture callback, data length:"
						+ data.length);
			}

		}
	}
//拍照
	private class JpegPictureCallback implements Camera.PictureCallback {

		public void onPictureTaken(byte[] data, Camera camera) {
			if (!isLive.get()) {
				Log.i(TAG, "JpegCallback, but activity has paused");
				return;
			}
			Log.d(TAG, "onPictureTaken callback");
			isCapturePic.set(false);
			if (data == null) {
				Log.e(TAG, "do not get any data from camera, data==null");
			} else {
				Log.v(TAG, "data length:" + data.length);
				isPreviewing = false;
				switch (recStyle) {
				case REC_PROGRESS_STYLE_NORMAL:
					showDialog(DIALOG_REC_PROGRESS);
					isRecProgressDialogShown = true;
					break;
				case REC_PROGRESS_STYLE_SCANNER:
					scannerView.setVisibility(View.VISIBLE);
					Bitmap previewImage = ImageHelper.fromYUV420P(previewData,
							previewWidth, previewHeight);
					if (previewImage != null) {
						scannerView.setBitmap(previewImage);
						recAnimation.setBitmap(previewImage);
						scannerView.invalidate();
						scannerView.startAnimation(recAnimation);
						Log.d(TAG, "animation started");
					}
					break;
				}

				recThread = new RecThread(data, handler, ocrManager);
				recThread.start();
				isRec = true;
				if (saveRecImage) {
					jpegData = data;
				}
			}
		}
	}

	private byte[] jpegData;
//自动聚焦
	private class AutoFocusCallback implements Camera.AutoFocusCallback {

		public void onAutoFocus(boolean success, Camera camera) {
			if (isCapturePic.get() && isLive.get()) {
				if (success) {
					isCapturePic.set(false);
					Log.v(TAG, "camera focus successfully");
					mCamera.takePicture(shutterCallback, rawCallback,
							jpegCallback);
				} else {
					mCamera.autoFocus(this);
					Log.i(TAG, "camera cannot focus, retry");
				}
			}
		}
	}

	private RecThread recThread;

	/**
	 * 存储图片
	 * @author shao chuanchao
	 *
	 */
	private class SaveJpegThread extends Thread {
		private byte[] jpeg;

		public SaveJpegThread(byte[] jpeg) {
			this.jpeg = jpeg;
		}

		@Override
		public void run() {
			if (jpeg != null) {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					File baseDirFile = new File(Constants.ROOT_DIR);
					if (!(baseDirFile.exists() && baseDirFile.isDirectory())) {
						Log.e(TAG, "base dir on sdcard not exists, creat it");
						if (baseDirFile.mkdirs()) {
							Log.e(TAG, "successfully create base dir");
						} else {
							Log.e(TAG, "cannot create base dir on sdcard");
							return;
						}
					}
					Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0,
							jpeg.length);
					if (bitmap == null) {
						Log.e(TAG, "cannot decode bitmap");
						return;
					}
					String name = Constants.ROOT_DIR
							+ dateFormat.format(Calendar.getInstance()
									.getTime()) + ".jpg";
					File file = new File(name);
					boolean error = false;
					try {
						file.createNewFile();
					} catch (IOException e1) {
						error = true;
					}
					if (!error) {
						OutputStream os;
						try {

							os = new BufferedOutputStream(new FileOutputStream(
									file));
							bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);

							Log.d(TAG, "bitmap compress successfully");
						} catch (FileNotFoundException e) {
						}
					} else {
						Log.e(TAG, "error when creating image file, filename:"
								+ name);
					}
				} else {
					Log.e(TAG, "sdcard not ready");
				}
			}
		}

	}

	private boolean isPreviewing;

	private boolean ensureCameraDevice() {
		if (mCamera == null) {
			mCamera = Camera.open();
		}
		return mCamera != null;
	}

	private void startPreview() {
		if (!ensureCameraDevice()) {
			Log.e(TAG, "cannot open camera device");
			return;
		}
		if (!isPreviewing) {
			mCamera.startPreview();
			isPreviewing = true;
			Log.e(TAG, "camera started");
		} else {
			Log.e(TAG, "camera has started previewing");
		}
	}

	private void rePreview() {
		if (mCamera != null) {
			if (!isPreviewing) {
				if (recStyle == REC_PROGRESS_STYLE_SCANNER) {
					scannerView.setVisibility(View.GONE);
				}
				mCamera.startPreview();
				isPreviewing = true;
				btnSnap.setVisibility(View.VISIBLE);
			} else {
				Log.i(TAG, "camera has started previewing");
			}
		} else {
			Log.e(TAG, "cannot repreview, mCamera==null");
		}
	}

	private void stopPreview() {
		if (mCamera != null) {
			mCamera.stopPreview();
			isPreviewing = false;
			Log.e(TAG, "camera stopped");
		}
	}

	private void closeCamera() {
		if (mCamera != null) {
			mCamera.release();
			isPreviewing = false;
			mCamera = null;
			Log.e(TAG, "camera released");
		}
	}

	private class PreviewCallback implements Camera.PreviewCallback {

		public void onPreviewFrame(byte[] data, Camera camera) {
			Log.v(TAG, "preview callback");
			if (mCamera != null && isPreviewing) {
				previewData = data;
				if (autoFocus)
					mCamera.autoFocus(autoFocusCallback);
			}
		}
	}

	private byte[] previewData;

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Log.i(TAG, "on low memory");
	}

}
