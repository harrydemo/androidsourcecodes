package cn.sharp.android.ncr.thread;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.sharp.android.ncr.MessageId;
import cn.sharp.android.ncr.ocr.OCRItems;
import cn.sharp.android.ncr.ocr.OCRManager;

public class RecThread extends Thread {
	private final static String TAG = "RecThread";
	private byte[] jpeg;
	private Handler handler;
	private AtomicBoolean stopped;
	private OCRManager ocrManager;
	private AtomicBoolean running;

	/**
	 * Constructor for RecThread class, with no jpeg data passed in, you should
	 * invoke setJpeg() before the thread running
	 * 
	 * @param handler
	 * @param ocrManager
	 */
	public RecThread(Handler handler, OCRManager ocrManager) {
		this.handler = handler;
		stopped = new AtomicBoolean();
		this.ocrManager = ocrManager;
		running = new AtomicBoolean();
	}

	public RecThread(byte[] jpeg, Handler handler, OCRManager ocrManager) {
		this.jpeg = jpeg;
		this.handler = handler;
		stopped = new AtomicBoolean();
		this.ocrManager = ocrManager;
		running = new AtomicBoolean();
	}

	@Override
	public void run() {
		running.set(true);
		stopped.set(false);
		Message msg = new Message();
		long start = System.currentTimeMillis();
		OCRItems ocrItems = ocrManager.rec(jpeg);
		long elapsed = System.currentTimeMillis() - start;
		Date elapsedTime = new Date(elapsed);
		Log.v(TAG, "time cost for rec namecard(s):" + elapsedTime.getSeconds());
		if (stopped.get()) {
			Log.e(TAG, "thead stopped after rec");
			ocrItems = null;
			return;
		}
		if (ocrItems == null) {
			msg.what = MessageId.NAMECARD_REC_FAILURE;
		} else {
			msg.what = MessageId.NAMECARD_REC_SUCCESS;
			msg.obj = ocrItems;
		}
		if (handler != null) {
			handler.sendMessage(msg);
			Log.d(TAG, "decode image data success message sent");
		} else {
			Log
					.i(TAG,
							"handler==null in decode image data progress, msg not sent");
		}
		running.set(false);
	}

	public void setJpeg(byte[] jpeg) {
		if (!running.get()) {
			this.jpeg = jpeg;
		}
	}

	public void forceStop() {
		stopped.set(true);
	}
}
