/**
 * <This class for file browser.>
 *  Copyright (C) <2009>  <Wang XinFeng,ACC http://androidos.cc/dev>
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.itcreator.android.reader.domain.Book;
import cn.itcreator.android.reader.paramter.Constant;
import cn.itcreator.android.reader.util.CRDBHelper;
import cn.itcreator.android.reader.views.CustomViewActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * This class for file browser
 * 
 * @author Wang Baoxi
 * @version 1.0
 * 
 */
public class FileBrowser extends ListActivity {
	private final int REQUEST_CODE = 10;

	/** database operator */
	private CRDBHelper mHelper = null;

	/** exit system menu id */
	private static final int EXIT = Menu.FIRST;

	// 旋屏菜单
	private static final int CIRC_SCREEN = Menu.FIRST + 1;

	/** delete file dialog id */
	private static final int DELETEFILE = 2;

	/** display file mode */
	private enum DISPLAYMODE {
		ABSOLUTE, RELATIVE;
	}

	private final DISPLAYMODE mDisplayMode = DISPLAYMODE.RELATIVE;
	private List<IconText> mDirectoryList = new ArrayList<IconText>();
	private File mCurrentDirectory = new File("/sdcard/");

	/** Called when the activity is first created. */

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setTheme(android.R.style.Theme_Black);

		browseToSdCard();
		this.setSelection(0);
		this.getListView().setOnItemLongClickListener(onItemLongClickListener);
	}

	/**
	 * This function browses to the sdcard directory of the file-system.
	 */
	private void browseToSdCard() {
		browseToWhere(new File(getString(R.string.sdcard)));
	}

	/**
	 * This function browses up one level according to the field:
	 * currentDirectory
	 */
	private void upOneLevel() {
		/** forbidden visit the root directory */
		if (!this.mCurrentDirectory.getParent().equals("/"))
			this.browseToWhere(this.mCurrentDirectory.getParentFile());
	}

	private void browseToWhere(final File aDirectory) {
		if (this.mDisplayMode == DISPLAYMODE.RELATIVE)
			this.setTitle(aDirectory.getAbsolutePath() + " - "
					+ getString(R.string.app_name));
		if (aDirectory.isDirectory()) {
			this.mCurrentDirectory = aDirectory;
			fill(aDirectory.listFiles());
		} else {
			OnClickListener okButtonListener = new OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {

				}
			};
			OnClickListener cancelButtonListener = new OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
				}
			};
		}
	}

	private void fill(File[] files) {
		this.mDirectoryList.clear();

		/** Add the "." == "current directory" */
		this.mDirectoryList.add(new IconText(getString(R.string.current_dir),
				getResources().getDrawable(R.drawable.folder32)));
		this.mDirectoryList.add(new IconText(getString(R.string.ad),
				getResources().getDrawable(R.drawable.folder32)));
		/** and the ".." == 'Up one level' */
		/** forbidden visit root */
		if (!this.mCurrentDirectory.getParent().equals("/"))
			this.mDirectoryList.add(new IconText(
					getString(R.string.up_one_level), getResources()
							.getDrawable(R.drawable.uponelevel)));

		Drawable currentIcon = null;
		for (File currentFile : files) {
			if (currentFile.isDirectory()) {
				currentIcon = getResources().getDrawable(R.drawable.folder32);
			} else {
				String fileName = currentFile.getName();

				if (checkEnds(fileName, getResources().getStringArray(
						R.array.textEnds))) {
					currentIcon = getResources().getDrawable(R.drawable.text32);
				}
				if (checkEnds(fileName, getResources().getStringArray(
						R.array.imageEnds))) {
					currentIcon = getResources()
							.getDrawable(R.drawable.image32);
					// currentIcon =
					// Drawable.createFromPath(getString(R.string.sdcard)+fileName);
					Log.d("the image path is:", "/sdcard/" + fileName);
				}
				if (checkEnds(fileName, getResources().getStringArray(
						R.array.htmlEnds))) {
					currentIcon = getResources().getDrawable(
							R.drawable.webtext32);
					// currentIcon =
					// Drawable.createFromPath(getString(R.string.sdcard)+fileName);
				}
				// UMD[start]; by mingkg21
				if (checkEnds(fileName, getResources().getStringArray(
						R.array.umdEnds))) {
					currentIcon = getResources().getDrawable(R.drawable.umd32);
				}
				// UMD[end]
			}

			switch (this.mDisplayMode) {
			case ABSOLUTE:
				this.mDirectoryList.add(new IconText(currentFile.getPath(),
						currentIcon));
				break;
			case RELATIVE:

				int currentPathStringLenght = this.mCurrentDirectory
						.getAbsolutePath().length();
				this.mDirectoryList.add(new IconText(currentFile
						.getAbsolutePath().substring(currentPathStringLenght),
						currentIcon));

				break;
			}
		}
		Collections.sort(this.mDirectoryList);
		IconTextListAdapter iconTextListAdapter = new IconTextListAdapter(this);
		iconTextListAdapter.setListItems(this.mDirectoryList);
		this.setListAdapter(iconTextListAdapter);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		String tag = "onListItemClick";
		int selectionRowID = (int) id;
		String selectedFileString = this.mDirectoryList.get(selectionRowID)
				.getText();

		if (selectedFileString.equals(getString(R.string.current_dir))) {
			// Refresh
			this.browseToWhere(this.mCurrentDirectory);
		} else if (selectedFileString.equals(getString(R.string.up_one_level))) {
			this.upOneLevel();

		} else {
			File file = null;
			switch (this.mDisplayMode) {
			case RELATIVE:
				file = new File(this.mCurrentDirectory.getAbsolutePath()
						+ this.mDirectoryList.get(selectionRowID).getText());
				String filePath = file.getAbsolutePath();
				if (file.isFile()) {

					// is a image
					if (checkEnds(filePath, getResources().getStringArray(
							R.array.imageEnds))) {
						setProgressBarIndeterminateVisibility(false);
						// startup the picture browser
						Intent i = new Intent(this, PictureBrowser.class);
						i.putExtra(Constant.FILE_PATH_KEY, filePath);
						startActivity(i);
						setProgressBarIndeterminateVisibility(true);
					}
					// is a html or htm
					if (checkEnds(filePath, getResources().getStringArray(
							R.array.htmlEnds))) {
						setProgressBarIndeterminateVisibility(false);
						Intent i = new Intent(this, HtmlBrowser.class);
						i.putExtra(Constant.FILE_PATH_KEY, filePath);
						startActivity(i);
						setProgressBarIndeterminateVisibility(true);
					}
					// is a txt
					if (checkEnds(filePath, getResources().getStringArray(
							R.array.textEnds))) {
						setProgressBarIndeterminateVisibility(false);
						mHelper = new CRDBHelper(this);
						Book b = new Book();
						b.setBookPath(filePath);
						Log.d(tag, "book path is :" + b.getBookPath());
						Constant.BOOK_ID_IN_DATABASE = mHelper.saveBook(b);
						mHelper.close();
						Intent i = new Intent();
						i.putExtra(Constant.FILE_PATH_KEY, filePath);
//						i.setClass(getApplicationContext(), TxtActivity.class);

						i.setClass(getApplicationContext(), CustomViewActivity.class);
						startActivity(i);
						setProgressBarIndeterminateVisibility(true);
						// this.finish();
					}
					// UMD[start]; is a UMD; by mingkg21
					if (checkEnds(filePath, getResources().getStringArray(
							R.array.umdEnds))) {
						setProgressBarIndeterminateVisibility(false);
						Intent i = new Intent();
						i.putExtra(Constant.FILE_PATH_KEY, filePath);
						i.setClass(getApplicationContext(), UMDBrowser.class);
						startActivity(i);
						setProgressBarIndeterminateVisibility(true);
					}
					// UMD[end]
				}
				break;
			case ABSOLUTE:
				file = new File(this.mDirectoryList.get(selectionRowID)
						.getText());
				Constant.FILE_PATH = file.getAbsolutePath();
				if (file.isFile()) {

					// is a image
					if (checkEnds(Constant.FILE_PATH, getResources()
							.getStringArray(R.array.imageEnds))) {
						setProgressBarIndeterminateVisibility(false);

						// startup the picture browser
						Intent i = new Intent(this, PictureBrowser.class);
						startActivity(i);
						setProgressBarIndeterminateVisibility(true);
					}
					// is a html or htm
					if (checkEnds(Constant.FILE_PATH, getResources()
							.getStringArray(R.array.htmlEnds))) {
						setProgressBarIndeterminateVisibility(false);
						Intent i = new Intent(this, HtmlBrowser.class);
						startActivity(i);
						setProgressBarIndeterminateVisibility(true);
					}
					// is a txt
					if (checkEnds(Constant.FILE_PATH, getResources()
							.getStringArray(R.array.textEnds))) {
						setProgressBarIndeterminateVisibility(false);
						mHelper = new CRDBHelper(this);
						Book b = new Book();
						b.setBookPath(Constant.FILE_PATH);
						Log.d(tag, "book path is :" + b.getBookPath());
						Constant.BOOK_ID_IN_DATABASE = mHelper.saveBook(b);
						mHelper.close();
						Intent i = new Intent();
//						i.setClass(getApplicationContext(), TxtActivity.class);

						i.setClass(getApplicationContext(), CustomViewActivity.class);
						startActivity(i);
						setProgressBarIndeterminateVisibility(true);
						// this.finish();
					}
					// UMD[start]; is a UMD; by mingkg21
					if (checkEnds(Constant.FILE_PATH, getResources()
							.getStringArray(R.array.umdEnds))) {
						setProgressBarIndeterminateVisibility(false);
						Intent i = new Intent();
						i.setClass(getApplicationContext(), UMDBrowser.class);
						startActivity(i);
						setProgressBarIndeterminateVisibility(true);
					}
					// UMD[end]
				}

				break;
			}
			if (file != null)
				this.browseToWhere(file);
		}
	}

	/**
	 * Check the string ends
	 * 
	 * @param checkItsEnd
	 * @param fileEndings
	 * @return
	 */
	private boolean checkEnds(String checkItsEnd, String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}

	private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long id) {
			Constant.FILE_PATH = mCurrentDirectory.getAbsolutePath()
					+ mDirectoryList.get((int) id).getText();
			showDialog(DELETEFILE);
			return true;
		}
	};

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, EXIT, 1, R.string.exit).setShortcut('3', 'a').setIcon(
				R.drawable.close);
		menu.add(1, CIRC_SCREEN, 0, R.string.circumgyrate)
				.setShortcut('3', 'c').setIcon(R.drawable.circscreen);
		return super.onCreateOptionsMenu(menu);
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DELETEFILE:// 删除文件对话框
			return new AlertDialog.Builder(FileBrowser.this).setTitle(
					getString(R.string.suredelete)).setMessage(
					getString(R.string.deletefile)).setPositiveButton(
					R.string.sure, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							setProgressBarIndeterminateVisibility(false);
							File f = new File(Constant.FILE_PATH);
							f.delete();
							// 清楚此书籍在数据库中信息
							mHelper.deleteBook(Constant.FILE_PATH);
							setProgressBarIndeterminateVisibility(true);
							browseToWhere(mCurrentDirectory);

						}
					}).setNeutralButton(R.string.cancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
						}
					}).create();

		default:
			return null;
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case CIRC_SCREEN:
			circumgyrateScreen();
			return true;
		case EXIT:// exit system

			this.finish();
			return true;
		default:
			break;
		}
		return false;
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

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			this.browseToWhere(this.mCurrentDirectory);
		}
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			this.browseToWhere(this.mCurrentDirectory);
		}
		if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
			// 打开键盘
			this.browseToWhere(this.mCurrentDirectory);
		}
		if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
			// 关闭键盘
			this.browseToWhere(this.mCurrentDirectory);
		}
	}

	protected void onResume() {
		super.onResume();
		String tag = "onResume";
		Log.d(tag, "start onResume");
		setProgressBarIndeterminateVisibility(false);
	}

}