package cn.sharp.android.ncr.thread;

import android.os.Handler;
import android.util.Log;

public class StopWorkThread extends Thread {
	private final static String TAG="StopWorkThread";
	private Thread thread;
	private Handler handler;
	private int msgId;

	public StopWorkThread(Thread thread, Handler handler, int msgId) {
		this.thread = thread;
		this.handler = handler;
		this.msgId = msgId;
	}

	@Override
	public void run() {
		if (thread != null) {
			thread.interrupt();
			try {
				thread.join();
			} catch (InterruptedException e) {
				// ignore
			}
			if (handler != null) {
				handler.sendEmptyMessage(msgId);
				Log.d(TAG, "thread stopped by StopThread");
			}
		}
	}
}
