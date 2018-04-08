package cn.itcreator.android.reader.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import cn.itcreator.android.reader.FileBrowser;
import cn.itcreator.android.reader.FontSetActivity;
import cn.itcreator.android.reader.ImageBrowser;
import cn.itcreator.android.reader.R;
import cn.itcreator.android.reader.domain.BookMark;
import cn.itcreator.android.reader.io.ReadFileRandom;
import cn.itcreator.android.reader.paramter.CR;
import cn.itcreator.android.reader.paramter.Constant;
import cn.itcreator.android.reader.util.BytesEncodingDetect;
import cn.itcreator.android.reader.util.CRDBHelper;
import cn.itcreator.android.reader.util.DateUtil;

/**
 * 
 * Copyright mawei.81@gmail.com Blog: http://www.maweis.com
 * 
 * @author peter CustomViewActivity is a main activity for a reader which will
 *         be used to enjoy mobile read life
 * 
 * 
 */
public class CustomViewActivity extends Activity {

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	/** 用于设置读书背景的请求代码 */
	private final int REQUEST_CODE_SET_BAGKGROUD = 10;

	/** 用于设置字体相关的请求代码 */
	private final int REQUEST_CODE_SET_FONT = 11;

	/** dialog id */
	private final int SAVEBOOKMARKSUCCESS = 11;
	private final int SAVEBOOKMARKFAIL = 12;
	private CustomCopyOfTextReader customCopyOfTextReader;
	private ReadFileRandom mReaderBytes;

	private CustomTextView myCustomView;

	private int mScreenWidth, mScreenHeigth;
	private static final int CHANGEFONT = Menu.FIRST;
	private static final int CHANGEBG = Menu.FIRST + 3;
	private static final int SAVEBOOKMARK = Menu.FIRST + 4;
	// 旋屏菜单
	private static final int CIRC_SCREEN = Menu.FIRST + 9;
	private static final int BACK = Menu.FIRST + 6;
	private static final int EXIT = Menu.FIRST + 7;
	private static final int ABOUT = Menu.FIRST + 8;

	// 查看书签
	private static final int VIEWBOOKMARK = Menu.FIRST + 20;
	/** save points when finger press */
	private int mRawX = 0, mRawY = 0;
	/** save points when the finger release */
	private int mCurX = 0, mCurY = 0;

	/** 文件的编码格式，默认为GB2312 */
	private String encoding = "GB3212";

	/** 数据库操作对象 */
	private CRDBHelper mHelper = null;

	/** 操作结果 */
	private boolean operateResult = true;

	/**
	 * 浮动比例
	 */
	private Toast mToast = null;

	/** 上一个百分比 */
	private int mLastPercent = 0;

	/** 书签集合 */
	private List<BookMark> mBookMarkList = null;

	/** 要操作的书签对象 */
	private BookMark mBookMark = null;
	/** 被选择的书签在集合中的位置 */
	private int bmlocation = 0;
	private final Handler mHandler = new Handler();

	private String _mFilePath = null;

	private Animation slideLeftIn;
	private Animation slideLeftOut;

	private Animation slideRightIn;
	private Animation slideRightOut;

	private ViewFlipper viewFlipper;

	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置标题栏带有进度条
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
		setContentView(R.layout.custom_reader);
		/** the phone component initialization */

		viewFlipper = (ViewFlipper) findViewById(R.id.flipper);

		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils
				.loadAnimation(this, R.anim.slide_left_out);

		slideRightIn = AnimationUtils
				.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this,
				R.anim.slide_right_out);

		myCustomView = (CustomTextView) findViewById(R.id.textContent);

		if ("".equals(Constant.IMAGE_PATH)) {
			myCustomView.setBackgroundResource(R.drawable.defautbg);
		} else {
			myCustomView.setBackgroundDrawable(Drawable
					.createFromPath(Constant.IMAGE_PATH));
		}

		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);

		slideRightIn = AnimationUtils
				.loadAnimation(this, R.anim.slide_right_in);

		// 显示操作正在进行
		loadData();
		// 取消显示

		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};

	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				float touchLength = Math.abs(e1.getY() - e2.getY());

				Log.i("touch-length", "Length: " + touchLength);
				if (touchLength > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

					customCopyOfTextReader.displayScreenbyLineWithAnimation(15,
							null);
					viewFlipper.setInAnimation(slideLeftIn);
					viewFlipper.setOutAnimation(slideLeftOut);
					viewFlipper.showNext();

					// viewFlipper.setInAnimation(slideLeftIn);
					// viewFlipper.setOutAnimation(slideLeftOut);
					// viewFlipper.showNext();
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					customCopyOfTextReader.displayScreenbyLineWithAnimation(
							-15, null);
					viewFlipper.setInAnimation(slideRightIn);
					viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showPrevious();

					// viewFlipper.setInAnimation(slideRightIn);
					// viewFlipper.setOutAnimation(slideRightOut);
					// viewFlipper.showPrevious();
				}
			} catch (Exception e) {
				// nothing
				Log.e("detector", "excetpion:" + e.getMessage());
			}
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}

	private void setFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 载入书籍的数据
	 */
	private void loadData() {
		/** the screen heigth and width */
		mScreenHeigth = this.getWindowManager().getDefaultDisplay().getHeight();
		mScreenWidth = this.getWindowManager().getDefaultDisplay().getWidth();

		/** text size color and background */
		// myCustomView.setTextColor(Color.BLACK);
		myCustomView.setTextSize(Constant.FONT18);

		mHelper = new CRDBHelper(this);
		// 设置背景

		mReaderBytes = new ReadFileRandom(_mFilePath);
		byte[] encodings = new byte[400];
		mReaderBytes.readBytes(encodings);
		mReaderBytes.close();
		/** 以下是检测文件的编码 */
		BytesEncodingDetect be = new BytesEncodingDetect();
		this.encoding = BytesEncodingDetect.nicename[be
				.detectEncoding(encodings)];
		/** 检测文件的编码结束 */

		/** load the attribute for font */
		Paint tp = myCustomView.getPaint();
		CR.fontHeight = myCustomView.getLineHeight();

		/** Ascii char width */
		CR.upperAsciiWidth = (int) tp.measureText(Constant.UPPERASCII);
		CR.lowerAsciiWidth = (int) tp.measureText(Constant.LOWERASCII);
		/** Chinese char width */
		CR.ChineseFontWidth = (int) tp.measureText(Constant.CHINESE
				.toCharArray(), 0, 1);

		Log.d("onCreateDialog CR.FontHeight:", "" + CR.fontHeight);
		Log.d("onCreateDialog CR.AsciiWidth:", "" + CR.upperAsciiWidth);
		Log.d("onCreateDialog CR.FontWidth:", "" + CR.ChineseFontWidth);
		customCopyOfTextReader = new CustomCopyOfTextReader(myCustomView, this,
				_mFilePath, mScreenWidth, mScreenHeigth, encoding);

		this.setTitle(_mFilePath + "-" + getString(R.string.app_name));

		// myCustomView.setOnKeyListener(mUpOrDown);
		// myCustomView.setOnTouchListener(mTouchListener);
		showToast();
	}

	// private OnKeyListener mUpOrDown = new View.OnKeyListener() {
	//
	// public boolean onKey(View v, int keyCode, KeyEvent event) {
	// if (0 == customCopyOfTextReader.getFileLength()) {
	// return false;
	// }
	// /** scroll to down */
	// if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
	// myCustomView.scrollTo(0, CR.fontHeight);
	// if (null != customCopyOfTextReader)
	// customCopyOfTextReader.displayNextToScreen(1);
	// showToast();
	// // Toast.makeText(CopyOfReaderCanvas.this,
	// // mTxtReader.getPercent()+Constant.PERCENTCHAR,
	// // Toast.LENGTH_SHORT).show();
	// mHandler.post(mScrollToBottom);
	// return true;
	// }
	//
	// /** scroll to up */
	// if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
	// myCustomView.scrollTo(0, 0);
	// if (null != customCopyOfTextReader)
	// customCopyOfTextReader.displayPreToScreen(1);
	// showToast();
	// // Toast.makeText(CopyOfReaderCanvas.this,
	// // mTxtReader.getPercent()+Constant.PERCENTCHAR,
	// // Toast.LENGTH_SHORT).show();
	// return true;
	// }
	//
	// /** page up */
	// if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
	// if (null != customCopyOfTextReader)
	// customCopyOfTextReader.displayPreToScreen(15);
	// showToast();
	// // Toast.makeText(CopyOfReaderCanvas.this,
	// // mTxtReader.getPercent()+Constant.PERCENTCHAR,
	// // Toast.LENGTH_SHORT).show();
	// return true;
	// }
	//
	// /** page down */
	// if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
	// if (null != customCopyOfTextReader)
	// customCopyOfTextReader.displayNextToScreen(15);
	// showToast();
	// // Toast.makeText(CopyOfReaderCanvas.this,
	// // mTxtReader.getPercent()+Constant.PERCENTCHAR,
	// // Toast.LENGTH_SHORT).show();
	// return true;
	// }
	// return false;
	// }
	// };

	// private OnTouchListener mTouchListener = new View.OnTouchListener() {
	//
	// public boolean onTouch(View v, MotionEvent event) {
	// if (0 == customCopyOfTextReader.getFileLength()) {
	// return false;
	// }
	// int action = event.getAction();
	// switch (action) {
	// case MotionEvent.ACTION_DOWN:
	// mRawX = (int) event.getX();
	// mRawY = (int) event.getY();
	// break;
	// case MotionEvent.ACTION_MOVE:
	// mCurX = (int) event.getX();
	// mCurY = (int) event.getY();
	// /** get the distance when move */
	// int upDownDistancey = mCurY - mRawY;
	// if (upDownDistancey < 0) {
	// customCopyOfTextReader.displayNextToScreen(Math
	// .abs(upDownDistancey / (CR.fontHeight)));
	// showToast();
	// // Toast.makeText(CopyOfReaderCanvas.this,
	// // mTxtReader.getPercent()+Constant.PERCENTCHAR,
	// // Toast.LENGTH_SHORT).show();
	// mHandler.post(mScrollToBottom);
	// }
	// if (upDownDistancey > 0) {
	// customCopyOfTextReader
	// .displayPreToScreen((upDownDistancey / (CR.fontHeight)));
	// showToast();
	// // Toast.makeText(CopyOfReaderCanvas.this,
	// // mTxtReader.getPercent()+Constant.PERCENTCHAR,
	// // Toast.LENGTH_SHORT).show();
	// myCustomView.scrollTo(0, 0);
	// }
	//
	// break;
	// case MotionEvent.ACTION_UP:
	//
	// // page down and up
	// int leftRightDistancey = mCurX - mRawX;
	// if (leftRightDistancey < -50) {// 让左右翻页变的迟钝些，左右移动距离在50px才进行翻页
	// customCopyOfTextReader.displayPreToScreen(10);
	// showToast();
	// // Toast.makeText(CopyOfReaderCanvas.this,
	// // mTxtReader.getPercent()+Constant.PERCENTCHAR,
	// // Toast.LENGTH_SHORT).show();
	// myCustomView.scrollTo(0, 0);
	// }
	// if (leftRightDistancey > 50) {
	// customCopyOfTextReader.displayNextToScreen(10);
	// showToast();
	// // Toast.makeText(CopyOfReaderCanvas.this,
	// // mTxtReader.getPercent()+Constant.PERCENTCHAR,
	// // Toast.LENGTH_SHORT).show();
	// mHandler.post(mScrollToBottom);
	// }
	// }
	// return true;
	// }
	// };

	protected void onStop() {
		super.onStop();
		String tag = "onStop";
		Log.d(tag, "stop the activity...");
		if (mHelper != null) {
			mHelper.close();
		}
		if (customCopyOfTextReader != null) {
			customCopyOfTextReader.close();
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
		if (customCopyOfTextReader != null) {
			customCopyOfTextReader.close();
			customCopyOfTextReader = null;
		}
	}

	protected void onPause() {
		super.onPause();
		String tag = "onPause";
		Log.d(tag, "pause the activity...");
		if (mHelper != null) {
			mHelper.close();
		}
		if (customCopyOfTextReader != null) {
			customCopyOfTextReader.close();
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
		// 旋转屏幕菜单
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
			// 设置字体相关
			Intent ifs = new Intent(getApplicationContext(),
					FontSetActivity.class);
			startActivityForResult(ifs, REQUEST_CODE_SET_FONT);
			return true;
		case CHANGEBG:// change background image
			Intent ix = new Intent(getApplicationContext(), ImageBrowser.class);
			// startActivity(ix);
			startActivityForResult(ix, REQUEST_CODE_SET_BAGKGROUD);
			return true;

		case CIRC_SCREEN:// 触发了旋屏菜单

			circumgyrateScreen();
			return true;

		case SAVEBOOKMARK:// save book mark
			saveBookMarkDialog();
			return true;
		case VIEWBOOKMARK:// 浏览书签
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
	 * 保存书签文件view
	 * 
	 * @return 保存成功返回true，否则返回false
	 */
	private void saveBookMarkDialog() {
		final Dialog d = new Dialog(CustomViewActivity.this);
		d.setTitle(R.string.inputbmname);
		d.setContentView(R.layout.bookmark_dialog);
		final EditText et = (EditText) d.findViewById(R.id.bmet);
		et.setText(customCopyOfTextReader.getCurrentLineString());
		final int offset = customCopyOfTextReader.getCurrentLineOffset();
		final Button sure = (Button) d.findViewById(R.id.bmsure);
		final Button cancel = (Button) d.findViewById(R.id.bmcancel);
		// 确定动作
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

		// 取消动作
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				d.dismiss();
			}
		});
		d.show();
		System.gc();
	}

	/**
	 * 浏览书签的view
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
		// 选择一个单元
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

		// 删除按钮事件
		deletebtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {// 删除书签调用
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
		// 跳转按钮事件
		gobtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (mBookMark != null) {// 开始跳转
					customCopyOfTextReader.readBufferByOffset(mBookMark
							.getCurrentOffset());
					d.dismiss();
				}
			}
		});

		// 取消按钮事件
		cancelbtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				d.dismiss();
			}
		});
		d.show();

	}

	/**
	 * 显示浮动比例
	 */
	private void showToast() {
		int x = customCopyOfTextReader.getPercent();
		if (x > mLastPercent) {
			mLastPercent = x;
			mToast = Toast.makeText(CustomViewActivity.this, mLastPercent
					+ Constant.PERCENTCHAR, Toast.LENGTH_SHORT);
			mToast.setGravity(0, 0, 0);
			mToast.show();
			System.gc();
		}
	}

	/**
	 * 保存书签成功对话框提示
	 * 
	 * @return 成功提示对话框
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
	 * 保存书签失败对话框提示
	 * 
	 * @return 失败提示对话框
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
	 * 关于我们
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
	 * 旋转屏幕
	 */
	private void circumgyrateScreen() {
		if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			// 如果是横屏的话，设置为普通模式
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	/**
	 * 当配置配置改变时候触发的方法
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
			// 打开键盘
			loadDataWhenCircScreen();
		}
		if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
			// 关闭键盘
			loadDataWhenCircScreen();
		}

	}

	/**
	 * 当旋转屏幕后重新加载数据调用的方法
	 */
	private void loadDataWhenCircScreen() {
		String tag = "loadDataWhenCircScreen";

		// 重新计算屏幕
		mScreenHeigth = this.getWindowManager().getDefaultDisplay().getHeight();
		mScreenWidth = this.getWindowManager().getDefaultDisplay().getWidth();

		Log.d(tag, "mScreenHeigth : " + mScreenHeigth);
		Log.d(tag, "mScreenWidth : " + mScreenWidth);
		// 记录读取的位置
		int offset = customCopyOfTextReader.getCurrentLineOffset();
		// 关闭
		customCopyOfTextReader.close();

		// 生成新的流
		customCopyOfTextReader = new CustomCopyOfTextReader(myCustomView, this,
				_mFilePath, mScreenWidth, mScreenHeigth, encoding);
		Log.d(tag, "create new stream for read file");

		// 读取流

		Log.d(tag, "the offset when read file is :" + offset);
		customCopyOfTextReader.readBufferByOffset(offset);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String tag = "onActivityResult";
		Log.d(tag, "go into the activity result...");
		if (requestCode == REQUEST_CODE_SET_BAGKGROUD
				&& resultCode == RESULT_OK) {// 接收到正确的设置背景的request
			// code
			myCustomView.setBackgroundDrawable(Drawable
					.createFromPath(Constant.IMAGE_PATH));
		}

		if (requestCode == REQUEST_CODE_SET_FONT && resultCode == RESULT_OK) {// 设置字体相关
			Paint tp = myCustomView.getPaint();
			if ((int) (tp.getTextSize()) != CR.textSize) {// 如果当前的字体和设置的字体不相等，重新设置
				tp.setTextSize(CR.textSize);
				CR.fontHeight = myCustomView.getLineHeight();
				/** Ascii char width */
				CR.upperAsciiWidth = (int) tp.measureText(Constant.UPPERASCII);
				/** Chinese char width */
				CR.ChineseFontWidth = (int) tp.measureText(Constant.CHINESE
						.toCharArray(), 0, 1);
				customCopyOfTextReader
						.readBufferByOffset(customCopyOfTextReader
								.getCurrentLineOffset());
			}

			// 设置颜色
			if (resultCode == RESULT_OK) {
				if (Constant.RED.equals(CR.textColor)) {
					myCustomView.setTextColor(Color.RED);
				}
				if (Constant.GRAY.equals(CR.textColor)) {
					myCustomView.setTextColor(Color.GRAY);
				}
				if (Constant.YELLOW.equals(CR.textColor)) {
					myCustomView.setTextColor(Color.YELLOW);
				}
				if (Constant.GREEN.equals(CR.textColor)) {
					myCustomView.setTextColor(Color.GREEN);
				}
				if (Constant.BLUE.equals(CR.textColor)) {
					myCustomView.setTextColor(Color.BLUE);
				}
				if (Constant.BLACK.equals(CR.textColor)) {
					myCustomView.setTextColor(Color.BLACK);
				}
				if (Constant.WHITE.equals(CR.textColor)) {
					myCustomView.setTextColor(Color.WHITE);
				}
			}

		}
	}
}