package cn.sharp.android.ncr.ocr;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.PopupWindow;
import cn.sharp.android.ncr.MessageId;
import cn.sharp.android.ncr.image.PgmImage;

public class OCRManager {

	private static final String TAG = "OCRManager";
	public static final String OCR_ITEMS = "ocritems";
	private Handler handler;

	/**
	 * Java method used to invoke underlying C-programmed OCR engine
	 * 
	 * @param items
	 *            the items that the OCR engine can recognize, the C native code
	 *            will fill this argument, notice that you should always pass a
	 *            NativeOCRItems instance to this parameter
	 * @param pgmContent
	 *            A raster of Height rows, in order from top to bottom. Each row
	 *            consists of Width gray values, in order from left to right.
	 *            Each gray value is a number from 0 through 255, with 0 being
	 *            black and 255 being white. Each gray value is represented in
	 *            pure binary by either 1 byte.
	 * @param width
	 *            the width of the PGM image
	 * @param height
	 *            the height of the PGM image
	 * @return if the returned array is not null, the returned integer array
	 *         consist of item numbers for each item(be aware of the fact that
	 *         an individual item may have more than 1 values. Take the name
	 *         item as an example, there may be two values, Michael Jordan and
	 *         Jordan). if the returned value is null, some error must have
	 *         occurred, or the pgm cannot be recognized
	 */
	public native int[] native_ncr(NativeOCRItems items, byte[] pgmContent,
			int width, int height);

	public native int[] native_ncr_from_jpeg(NativeOCRItems items,
			byte[] jpegData);

	public OCRManager() {
	}

	public OCRManager(Handler handler) {
		this.handler = handler;
	}

	public synchronized void startRecNamecard(PgmImage pgmImage, Handler handler) {
		PgmOCRThread thread = new PgmOCRThread(pgmImage, handler);
		thread.start();
		Log.e(TAG, "ocr thread started");
	}

	public OCRItems rec(PgmImage pgmImage) {
		return doRec(pgmImage);
	}

	//¶ÁÈ¡pgmÎÄ¼þ
	private OCRItems doRec(PgmImage pgmImage) {
		NativeOCRItems nativeOCRItems = new NativeOCRItems();
		int[] fieldLength = native_ncr(nativeOCRItems, pgmImage.getContent(),
				pgmImage.getWidth(), pgmImage.getHeight());
		if (fieldLength == null) {
			Log.e(TAG, "rec failure");
			return null;
		} else {
			Log.e(TAG, "rec success");
			OCRItems ocrItems = new OCRItems(nativeOCRItems, fieldLength);
			return ocrItems;
		}
	}

	public OCRItems rec(byte[] jpeg) {
		if (jpeg == null) {
			return null;
		}
		return doRec(jpeg);
	}

	private OCRItems doRec(byte[] jpeg) {
		NativeOCRItems nativeOcrItems = new NativeOCRItems();
		int[] fieldLength = native_ncr_from_jpeg(nativeOcrItems, jpeg);
		if (fieldLength == null) {
			Log.e(TAG, "rec failure");
			return null;
		} else {
			Log.d(TAG, "rec success");
			OCRItems ocrItems = new OCRItems(nativeOcrItems, fieldLength);
			return ocrItems;
		}
	}

	private class PgmOCRThread extends Thread {
		private PgmImage pgmImage;
		private Handler handler;

		public PgmOCRThread(PgmImage pgmImage, Handler handler) {
			this.pgmImage = pgmImage;
			this.handler = handler;
		}

		@Override
		public void run() {
			OCRItems ocrItems = doRec(pgmImage);
			if (ocrItems == null) {
				Log.e(TAG, "rec failure");
				if (handler != null) {
					Message msg = new Message();
					msg.what = MessageId.NAMECARD_REC_FAILURE;
					handler.sendMessage(msg);
				}
			} else {
				Log.d(TAG, "rec success");
				if (handler != null) {
					Message msg = new Message();
					msg.what = MessageId.NAMECARD_REC_SUCCESS;
					msg.obj = ocrItems;
					handler.sendMessage(msg);
				}
			}
		}
	};

	static {
		System.loadLibrary("namecardrec");
	}
}
