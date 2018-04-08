/**
 * <A activity for display content of txt files.>
 *   *  Copyright (C) <2009>  <Wang XinFeng,ACC http://androidos.cc/dev>
 *
 *   This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package cn.itcreator.android.reader;

import java.util.Date;
import java.util.List;

import cn.itcreator.android.reader.domain.BookMark;
import cn.itcreator.android.reader.io.ReadFileRandom;
import cn.itcreator.android.reader.paramter.CR;
import cn.itcreator.android.reader.paramter.Constant;
import cn.itcreator.android.reader.util.BytesEncodingDetect;
import cn.itcreator.android.reader.util.CRDBHelper;
import cn.itcreator.android.reader.util.CopyOfTxtReader;
import cn.itcreator.android.reader.util.DateUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * A activity for display content of txt files.
 * </p>
 * 
 * @author Wang XinFeng
 * @version 1.0
 * 
 */
public class TxtActivity extends Activity {

	/** �������ö��鱳����������� */
	private final int REQUEST_CODE_SET_BAGKGROUD = 10;

	/** ��������������ص�������� */
	private final int REQUEST_CODE_SET_FONT = 11;

	/** dialog id */
	private final int SAVEBOOKMARKSUCCESS = 11;
	private final int SAVEBOOKMARKFAIL = 12;
	private CopyOfTxtReader mTxtReader;
	private ReadFileRandom mReaderBytes;
	private ScrollView mScrollView;
	private TextView mTextView;
	private LinearLayout mLinearLayout;
	private int mScreenWidth, mScreenHeigth;
	private static final int CHANGEFONT = Menu.FIRST;
	private static final int CHANGEBG = Menu.FIRST + 3;
	private static final int SAVEBOOKMARK = Menu.FIRST + 4;
	// �����˵�
	private static final int CIRC_SCREEN = Menu.FIRST + 9;
	private static final int BACK = Menu.FIRST + 6;
	private static final int EXIT = Menu.FIRST + 7;
	private static final int ABOUT = Menu.FIRST + 8;

	// �鿴��ǩ
	private static final int VIEWBOOKMARK = Menu.FIRST + 20;
	/** save points when finger press */
	private int mRawX = 0, mRawY = 0;
	/** save points when the finger release */
	private int mCurX = 0, mCurY = 0;

	/** �ļ��ı����ʽ��Ĭ��ΪGB2312 */
	private String encoding = "GB3212";

	/** ���ݿ�������� */
	private CRDBHelper mHelper = null;

	/** ������� */
	private boolean operateResult = true;

	/**
	 * ��������
	 */
	private Toast mToast = null;

	/** ��һ���ٷֱ� */
	private int mLastPercent = 0;

	/** ��ǩ���� */
	private List<BookMark> mBookMarkList = null;

	/** Ҫ��������ǩ���� */
	private BookMark mBookMark = null;
	/** ��ѡ�����ǩ�ڼ����е�λ�� */
	private int bmlocation = 0;
	private final Handler mHandler = new Handler();

	private String _mFilePath = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ��������н�����
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		Intent intent = getIntent();
		if (intent == null) {
			finish();
			return;
		}
		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			finish();
			return;
		}
		_mFilePath = bundle.getString(Constant.FILE_PATH_KEY);
		if (_mFilePath == null || _mFilePath.equals("")) {
			finish();
			return;
		}

		
		setFullScreen();

		String tag = "onCreate";
		Log.d(tag, "initialize the new Activity");
		setContentView(R.layout.reader);
		/** the phone component initialization */
		mScrollView = (ScrollView) findViewById(R.id.scrollView);
		mTextView = (TextView) findViewById(R.id.textContent);
		mLinearLayout = (LinearLayout) findViewById(R.id.textLayout);

		// ��ʾ�������ڽ���
		loadData();
		// ȡ����ʾ
	}

	private void setFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * �����鼮������
	 */
	private void loadData() {
		/** the screen heigth and width */
		mScreenHeigth = this.getWindowManager().getDefaultDisplay().getHeight();
		mScreenWidth = this.getWindowManager().getDefaultDisplay().getWidth();

		/** text size color and background */
		mTextView.setTextColor(Color.BLACK);
		mTextView.setTextSize(Constant.FONT18);

		mHelper = new CRDBHelper(this);
		// ���ñ���
		if ("".equals(Constant.IMAGE_PATH)) {
			mScrollView.setBackgroundResource(R.drawable.defautbg);
		} else {
			mScrollView.setBackgroundDrawable(Drawable
					.createFromPath(Constant.IMAGE_PATH));

		}
		mReaderBytes = new ReadFileRandom(_mFilePath);
		byte[] encodings = new byte[400];
		mReaderBytes.readBytes(encodings);
		mReaderBytes.close();
		/** �����Ǽ���ļ��ı��� */
		BytesEncodingDetect be = new BytesEncodingDetect();
		this.encoding = BytesEncodingDetect.nicename[be
				.detectEncoding(encodings)];
		/** ����ļ��ı������ */

		/** load the attribute for font */
		TextPaint tp = mTextView.getPaint();
		CR.fontHeight = mTextView.getLineHeight();

		/** Ascii char width */
		CR.upperAsciiWidth = (int) tp.measureText(Constant.UPPERASCII);
		CR.lowerAsciiWidth = (int) tp.measureText(Constant.LOWERASCII);
		/** Chinese char width */
		CR.ChineseFontWidth = (int) tp.measureText(Constant.CHINESE
				.toCharArray(), 0, 1);

		Log.d("onCreateDialog CR.FontHeight:", "" + CR.fontHeight);
		Log.d("onCreateDialog CR.AsciiWidth:", "" + CR.upperAsciiWidth);
		Log.d("onCreateDialog CR.FontWidth:", "" + CR.ChineseFontWidth);
		mTxtReader = new CopyOfTxtReader(mTextView, this, _mFilePath,
				mScreenWidth, mScreenHeigth, encoding);

		this.setTitle(_mFilePath + "-" + getString(R.string.app_name));
		mScrollView.setOnKeyListener(mUpOrDown);
		mScrollView.setOnTouchListener(mTouchListener);
		showToast();
	}

	private OnKeyListener mUpOrDown = new View.OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (0 == mTxtReader.getFileLength()) {
				return false;
			}
			/** scroll to down */
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				mScrollView.scrollTo(0, CR.fontHeight);
				if (null != mTxtReader)
					mTxtReader.displayNextToScreen(1);
				showToast();
				// Toast.makeText(CopyOfReaderCanvas.this,
				// mTxtReader.getPercent()+Constant.PERCENTCHAR,
				// Toast.LENGTH_SHORT).show();
				mHandler.post(mScrollToBottom);
				return true;
			}

			/** scroll to up */
			if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				mScrollView.scrollTo(0, 0);
				if (null != mTxtReader)
					mTxtReader.displayPreToScreen(1);
				showToast();
				// Toast.makeText(CopyOfReaderCanvas.this,
				// mTxtReader.getPercent()+Constant.PERCENTCHAR,
				// Toast.LENGTH_SHORT).show();
				return true;
			}

			/** page up */
			if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				if (null != mTxtReader)
					mTxtReader.displayPreToScreen(15);
				showToast();
				// Toast.makeText(CopyOfReaderCanvas.this,
				// mTxtReader.getPercent()+Constant.PERCENTCHAR,
				// Toast.LENGTH_SHORT).show();
				return true;
			}

			/** page down */
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (null != mTxtReader)
					mTxtReader.displayNextToScreen(15);
				showToast();
				// Toast.makeText(CopyOfReaderCanvas.this,
				// mTxtReader.getPercent()+Constant.PERCENTCHAR,
				// Toast.LENGTH_SHORT).show();
				return true;
			}
			return false;
		}
	};

	private Runnable mScrollToBottom = new Runnable() {

		public void run() {
			int off = mLinearLayout.getMeasuredHeight()
					- mScrollView.getHeight();
			if (off > 0) {
				mScrollView.scrollTo(0, off);
			}
		}
	};

	private OnTouchListener mTouchListener = new View.OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {
			if (0 == mTxtReader.getFileLength()) {
				return false;
			}
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				mRawX = (int) event.getX();
				mRawY = (int) event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				mCurX = (int) event.getX();
				mCurY = (int) event.getY();
				/** get the distance when move */
				int upDownDistancey = mCurY - mRawY;
				if (upDownDistancey < 0) {
					mTxtReader.displayNextToScreen(Math.abs(upDownDistancey
							/ (CR.fontHeight)));
					showToast();
					// Toast.makeText(CopyOfReaderCanvas.this,
					// mTxtReader.getPercent()+Constant.PERCENTCHAR,
					// Toast.LENGTH_SHORT).show();
					mHandler.post(mScrollToBottom);
				}
				if (upDownDistancey > 0) {
					mTxtReader
							.displayPreToScreen((upDownDistancey / (CR.fontHeight)));
					showToast();
					// Toast.makeText(CopyOfReaderCanvas.this,
					// mTxtReader.getPercent()+Constant.PERCENTCHAR,
					// Toast.LENGTH_SHORT).show();
					mScrollView.scrollTo(0, 0);
				}

				break;
			case MotionEvent.ACTION_UP:

				// page down and up
				int leftRightDistancey = mCurX - mRawX;
				if (leftRightDistancey < -50) {// �����ҷ�ҳ��ĳٶ�Щ�������ƶ�������50px�Ž��з�ҳ
					mTxtReader.displayPreToScreen(10);
					showToast();
					// Toast.makeText(CopyOfReaderCanvas.this,
					// mTxtReader.getPercent()+Constant.PERCENTCHAR,
					// Toast.LENGTH_SHORT).show();
					mScrollView.scrollTo(0, 0);
				}
				if (leftRightDistancey > 50) {
					mTxtReader.displayNextToScreen(10);
					showToast();
					// Toast.makeText(CopyOfReaderCanvas.this,
					// mTxtReader.getPercent()+Constant.PERCENTCHAR,
					// Toast.LENGTH_SHORT).show();
					mHandler.post(mScrollToBottom);
				}
			}
			return true;
		}
	};

	protected void onStop() {
		super.onStop();
		String tag = "onStop";
		Log.d(tag, "stop the activity...");
		if (mHelper != null) {
			mHelper.close();
		}
		if (mTxtReader != null) {
			mTxtReader.close();
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		String tag = "onDestroy";
		Log.d(tag, "destroy the activity...");
		if (mHelper != null) {
			mHelper.close();
			mHelper = null;
		}
		if (mTxtReader != null) {
			mTxtReader.close();
			mTxtReader = null;
		}
	}

	protected void onPause() {
		super.onPause();
		String tag = "onPause";
		Log.d(tag, "pause the activity...");
		if (mHelper != null) {
			mHelper.close();
		}
		if (mTxtReader != null) {
			mTxtReader.close();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(1, CHANGEFONT, 0, R.string.changefont).setShortcut('3', 'a')
				.setIcon(R.drawable.setfont);
		menu.add(1, CHANGEBG, 1, R.string.changebg).setShortcut('3', 'c')
				.setIcon(R.drawable.setbackgroud);
		menu.add(2, SAVEBOOKMARK, 2, R.string.savebookmark).setShortcut('3',
				'd').setIcon(R.drawable.addbookmark);
		menu.add(2, VIEWBOOKMARK, 3, R.string.viewbookmark).setShortcut('3',
				'q').setIcon(R.drawable.viewbookmark);
		// ��ת��Ļ�˵�
		menu.add(2, CIRC_SCREEN, 3, R.string.circumgyrate)
				.setShortcut('3', 'c').setIcon(R.drawable.circscreen);

		menu.add(3, BACK, 5, R.string.back).setShortcut('3', 'x').setIcon(
				R.drawable.uponelevel);
		menu.add(3, EXIT, 6, R.string.exit).setShortcut('3', 'e').setIcon(
				R.drawable.close);
		menu.add(3, ABOUT, 7, R.string.about).setShortcut('3', 'o').setIcon(
				android.R.drawable.star_big_on);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case CHANGEFONT:// change text size
			// �����������
			Intent ifs = new Intent(getApplicationContext(),
					FontSetActivity.class);
			startActivityForResult(ifs, REQUEST_CODE_SET_FONT);
			return true;
		case CHANGEBG:// change background image
			Intent ix = new Intent(getApplicationContext(), ImageBrowser.class);
			// startActivity(ix);
			startActivityForResult(ix, REQUEST_CODE_SET_BAGKGROUD);
			return true;

		case CIRC_SCREEN:// �����������˵�

			circumgyrateScreen();
			return true;

		case SAVEBOOKMARK:// save book mark
			saveBookMarkDialog();
			return true;
		case VIEWBOOKMARK:// �����ǩ
			bookMarkView();
			return true;

		case EXIT:// exit system
			this.finish();
			return true;
		case BACK:// back to browser file
			setProgressBarIndeterminateVisibility(false);
			Intent i = new Intent();
			i.setClass(getApplicationContext(), FileBrowser.class);
			startActivity(i);
			setProgressBarIndeterminateVisibility(true);
			this.finish();

			return true;
		case ABOUT:// about this software
			showDialog(ABOUT);
			return true;
		default:
			return true;
		}
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SAVEBOOKMARKSUCCESS:// save book mark successful
			return saveBookMarkSuccess();
		case SAVEBOOKMARKFAIL:// save book mark fail
			return saveBookMarkFail();

		case ABOUT:// about this software
			return about();
		default:
			return null;
		}
	}

	/**
	 * ������ǩ�ļ�view
	 * 
	 * @return ����ɹ�����true�����򷵻�false
	 */
	private void saveBookMarkDialog() {
		final Dialog d = new Dialog(TxtActivity.this);
		d.setTitle(R.string.inputbmname);
		d.setContentView(R.layout.bookmark_dialog);
		final EditText et = (EditText) d.findViewById(R.id.bmet);
		et.setText(mTxtReader.getCurrentLineString());
		final int offset = mTxtReader.getCurrentLineOffset();
		final Button sure = (Button) d.findViewById(R.id.bmsure);
		final Button cancel = (Button) d.findViewById(R.id.bmcancel);
		// ȷ������
		sure.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String bmn = et.getText().toString();
				if (bmn.length() < 1) {
					d.dismiss();
					d.show();
				} else {
					if (bmn.length() > 10) {
						bmn.substring(0, 10);
					}
					BookMark bm = new BookMark();
					bm.setBookId(Constant.BOOK_ID_IN_DATABASE);
					bm.setMarkName(bmn);
					bm.setCurrentOffset(offset);
					bm.setSaveTime(DateUtil.dateToString(new Date()));
					operateResult = mHelper.addBookMark(bm);
					mHelper.close();
					mHelper = new CRDBHelper(getApplicationContext());
					if (operateResult) {
						showDialog(SAVEBOOKMARKSUCCESS);
					} else {
						showDialog(SAVEBOOKMARKFAIL);
					}
					d.dismiss();
				}
			}
		});

		// ȡ������
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				d.dismiss();
			}
		});
		d.show();
		System.gc();
	}

	/**
	 * �����ǩ��view
	 */
	private void bookMarkView() {

		final Dialog d = new Dialog(this);

		d.setContentView(R.layout.bookmarklist);
		final Button deletebtn = (Button) d.findViewById(R.id.deletebm);
		final Button gobtn = (Button) d.findViewById(R.id.skipbm);
		final Button cancelbtn = (Button) d.findViewById(R.id.cancelbm);
		d.setTitle(getString(R.string.bookmarklist));
		final ListView listv = (ListView) d.findViewById(R.id.bookmarklistview);
		mBookMarkList = mHelper.queryAllBookMark(Constant.BOOK_ID_IN_DATABASE);
		final ListAdapter listAdapter = new ArrayAdapter<BookMark>(this,
				android.R.layout.simple_spinner_item, mBookMarkList);
		listv.setAdapter(listAdapter);
		listv.setSelection(0);
		// ѡ��һ����Ԫ
		listv
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long location) {
						bmlocation = (int) location;
						mBookMark = mBookMarkList.get(bmlocation);

					}

					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

		// ɾ����ť�¼�
		deletebtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {// ɾ����ǩ����
				String tag = "delete book mark ";
				if (mBookMark != null) {
					Log.d(tag, "start delete book mark");
					boolean b = mHelper.deleteBookMark(mBookMark
							.getBookMarkId());
					if (b && mBookMarkList.size() > 0) {
						mBookMarkList.remove(bmlocation);
						listv.setAdapter(listAdapter);
						mBookMark = null;
						System.gc();
					}
				}
			}
		});
		// ��ת��ť�¼�
		gobtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (mBookMark != null) {// ��ʼ��ת
					mTxtReader.readBufferByOffset(mBookMark.getCurrentOffset());
					d.dismiss();
				}
			}
		});

		// ȡ����ť�¼�
		cancelbtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				d.dismiss();
			}
		});
		d.show();

	}

	/**
	 * ��ʾ��������
	 */
	private void showToast() {
		int x = mTxtReader.getPercent();
		if (x > mLastPercent) {
			mLastPercent = x;
			mToast = Toast.makeText(TxtActivity.this, mLastPercent
					+ Constant.PERCENTCHAR, Toast.LENGTH_SHORT);
			mToast.setGravity(0, 0, 0);
			mToast.show();
			System.gc();
		}

	}

	/**
	 * ������ǩ�ɹ��Ի�����ʾ
	 * 
	 * @return �ɹ���ʾ�Ի���
	 */
	private Dialog saveBookMarkSuccess() {
		return new AlertDialog.Builder(this).setPositiveButton(
				getString(R.string.sure),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				}).setTitle(getString(R.string.saveresult)).setIcon(
				R.drawable.success).setMessage(getString(R.string.savesuccess))
				.create();
	}

	/**
	 * ������ǩʧ�ܶԻ�����ʾ
	 * 
	 * @return ʧ����ʾ�Ի���
	 */
	private Dialog saveBookMarkFail() {
		return new AlertDialog.Builder(this).setPositiveButton(
				getString(R.string.sure),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				}).setTitle(getString(R.string.saveresult)).setIcon(
				R.drawable.fail).setMessage(getString(R.string.savefail))
				.create();
	}

	/**
	 * ��������
	 * 
	 * @return
	 */
	private Dialog about() {
		return new AlertDialog.Builder(this).setPositiveButton(
				getApplicationContext().getString(R.string.sure),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				}).setTitle(
				getApplicationContext().getString(R.string.aboutcoolreader))
				.setMessage(
						getApplicationContext()
								.getString(R.string.ourintroduce)).create();
	}

	/**
	 * ��ת��Ļ
	 */
	private void circumgyrateScreen() {
		if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			// ����Ǻ����Ļ�������Ϊ��ͨģʽ
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	/**
	 * ���������øı�ʱ�򴥷��ķ���
	 */

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		String tag = "onConfigurationChanged";
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			loadDataWhenCircScreen();
			Log.d(tag, "configuration chanaged , land screen");
		}
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			loadDataWhenCircScreen();
			Log.d(tag, "configuration chanaged , common screen");
		}
		if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
			// �򿪼���
			loadDataWhenCircScreen();
		}
		if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
			// �رռ���
			loadDataWhenCircScreen();
		}

	}

	/**
	 * ����ת��Ļ�����¼������ݵ��õķ���
	 */
	private void loadDataWhenCircScreen() {
		String tag = "loadDataWhenCircScreen";

		// ���¼�����Ļ
		mScreenHeigth = this.getWindowManager().getDefaultDisplay().getHeight();
		mScreenWidth = this.getWindowManager().getDefaultDisplay().getWidth();

		Log.d(tag, "mScreenHeigth : " + mScreenHeigth);
		Log.d(tag, "mScreenWidth : " + mScreenWidth);
		// ��¼��ȡ��λ��
		int offset = mTxtReader.getCurrentLineOffset();
		// �ر�
		mTxtReader.close();

		// �����µ���
		mTxtReader = new CopyOfTxtReader(mTextView, this, _mFilePath,
				mScreenWidth, mScreenHeigth, encoding);
		Log.d(tag, "create new stream for read file");

		// ��ȡ��

		Log.d(tag, "the offset when read file is :" + offset);
		mTxtReader.readBufferByOffset(offset);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String tag = "onActivityResult";
		Log.d(tag, "go into the activity result...");
		if (requestCode == REQUEST_CODE_SET_BAGKGROUD
				&& resultCode == RESULT_OK) {// ���յ���ȷ�����ñ�����request
												// code
			mScrollView.setBackgroundDrawable(Drawable
					.createFromPath(Constant.IMAGE_PATH));
		}

		if (requestCode == REQUEST_CODE_SET_FONT && resultCode == RESULT_OK) {// �����������
			TextPaint tp = mTextView.getPaint();
			if ((int) (tp.getTextSize()) != CR.textSize) {// �����ǰ����������õ����岻��ȣ���������
				tp.setTextSize(CR.textSize);
				CR.fontHeight = mTextView.getLineHeight();
				/** Ascii char width */
				CR.upperAsciiWidth = (int) tp.measureText(Constant.UPPERASCII);
				/** Chinese char width */
				CR.ChineseFontWidth = (int) tp.measureText(Constant.CHINESE
						.toCharArray(), 0, 1);
				mTxtReader
						.readBufferByOffset(mTxtReader.getCurrentLineOffset());
			}

			// ������ɫ
			if (resultCode == RESULT_OK) {
				if (Constant.RED.equals(CR.textColor)) {
					mTextView.setTextColor(Color.RED);
				}
				if (Constant.GRAY.equals(CR.textColor)) {
					mTextView.setTextColor(Color.GRAY);
				}
				if (Constant.YELLOW.equals(CR.textColor)) {
					mTextView.setTextColor(Color.YELLOW);
				}
				if (Constant.GREEN.equals(CR.textColor)) {
					mTextView.setTextColor(Color.GREEN);
				}
				if (Constant.BLUE.equals(CR.textColor)) {
					mTextView.setTextColor(Color.BLUE);
				}
				if (Constant.BLACK.equals(CR.textColor)) {
					mTextView.setTextColor(Color.BLACK);
				}
				if (Constant.WHITE.equals(CR.textColor)) {
					mTextView.setTextColor(Color.WHITE);
				}
			}

		}
	}
}
