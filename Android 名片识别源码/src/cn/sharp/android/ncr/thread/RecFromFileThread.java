package cn.sharp.android.ncr.thread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.os.Handler;
import android.util.Log;
import cn.sharp.android.ncr.MessageId;
import cn.sharp.android.ncr.ocr.OCRManager;

public class RecFromFileThread extends RecThread {
	private final static String TAG = "RecFromFileThread";
	private File jpegFile;
	private Handler handler;

	public RecFromFileThread(Handler handler, OCRManager ocrManager,
			File jpegFile) {
		super(handler, ocrManager);
		this.jpegFile = jpegFile;
		this.handler = handler;
	}

	@Override
	public void run() {
		InputStream is = null;
		boolean error = true;
		if (jpegFile.exists() && jpegFile.isFile()) {
			try {
				is = new BufferedInputStream(new FileInputStream(jpegFile));
			} catch (FileNotFoundException e) {
				Log.e(TAG, e.getMessage());
			}
			if (is != null) {
				byte[] jpegData = new byte[(int) jpegFile.length()];
				error = false;
				try {
					is.read(jpegData);
				} catch (IOException e) {
					error = true;
				}
				if (!error) {
					super.setJpeg(jpegData);
					super.run();
				}
			}
		}
		if (error) {
			handler.sendEmptyMessage(MessageId.NAMECARD_REC_FAILURE);
		}
	}
}
