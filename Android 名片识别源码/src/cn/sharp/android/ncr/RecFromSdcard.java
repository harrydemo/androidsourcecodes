package cn.sharp.android.ncr;

import java.io.File;
import java.io.FilenameFilter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import cn.sharp.android.ncr.image.PgmImage;
import cn.sharp.android.ncr.ocr.OCRItems;
import cn.sharp.android.ncr.ocr.OCRManager;

public class RecFromSdcard extends ListActivity implements OnItemClickListener {
	private final static String TAG = "RecFromSdcard";
	private final static int DIALOG_REC_FAILURE = 0;
	private final static int DIALOG_REC_SUCCESS = 1;
	private final static int DIALOG_REC_PROGRESS = 2;

	private String[] pgmFiles;
	private OCRManager ocrManager;
	private OCRItems ocrItems;
	private ProgressDialog recProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		File file = new File(Constants.ROOT_DIR);
		FilenameFilter filter = new PgmFileFilter();
		pgmFiles = file.list(filter);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, pgmFiles));
		getListView().setOnItemClickListener(this);
		ocrManager = new OCRManager(handler);
	}

	/**
	 * 文件过滤器 返回的是pgm文件
	 * 
	 * @author shao chuanchao
	 * 
	 */
	private class PgmFileFilter implements FilenameFilter {

		public boolean accept(File dir, String filename) {
			if (filename.length() < 4)
				return false;
			if (filename.substring(filename.length() - 3).toLowerCase()
					.equals("pgm")) {
				return true;
			}
			return false;
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "get message, id:" + msg.what);
			dismissDialog(DIALOG_REC_PROGRESS);
			switch (msg.what) {
			case MessageId.NAMECARD_REC_SUCCESS:
				ocrItems = (OCRItems) msg.obj;
				Intent intent = new Intent(RecFromSdcard.this,
						DisplayRecResult.class);
				intent.putExtra(OCRManager.OCR_ITEMS, ocrItems);
				startActivity(intent);
				break;
			case MessageId.NAMECARD_REC_FAILURE:
				showDialog(DIALOG_REC_FAILURE);
				break;
			}
		}

	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_REC_FAILURE:
			return new AlertDialog.Builder(this).setTitle(R.string.error)
					.setMessage(R.string.rec_failure)
					.setPositiveButton(R.string.cancel, null).create();
			// case DIALOG_REC_SUCCESS:
			// AlertDialog.Builder recSuccessDialogBuilder = new
			// AlertDialog.Builder(
			// this).setTitle(R.string.success).setPositiveButton(
			// R.string.ok, null);
			// return recSuccessDialogBuilder.create();
		case DIALOG_REC_PROGRESS:
			recProgressDialog = new ProgressDialog(RecFromSdcard.this);
			recProgressDialog.setTitle(R.string.waiting);
			recProgressDialog.setMessage(getResources().getString(
					R.string.rec_in_progress));
			recProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return recProgressDialog;
		default:
			return super.onCreateDialog(id);
		}
	}

	/**
	 * listview的监听
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.d(TAG, "item clicked");
		if (pgmFiles != null) {
			Log.e(TAG, "click item " + pgmFiles[arg2]);
			File file = new File(Constants.ROOT_DIR + pgmFiles[arg2]);
			if (file.exists() && file.isFile()) {
				PgmImage pgmImage = PgmImage.fromFile(file);
				if (pgmImage == null) {
					Log.e(TAG, "cannot decode pgm image from file "
							+ pgmFiles[arg2]);
				} else {
					ocrManager.startRecNamecard(pgmImage, handler);
					showDialog(DIALOG_REC_PROGRESS);
					Log.d(TAG, "start to rec, file:" + file.getAbsolutePath());
				}
			} else {
				Log.e(TAG, "file " + pgmFiles[arg2] + " does not exist");
			}
		}
	}
}
