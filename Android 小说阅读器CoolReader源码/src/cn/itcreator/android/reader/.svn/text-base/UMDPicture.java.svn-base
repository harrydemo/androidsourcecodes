/**
 * <This class for view the picture.>
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
import java.util.List;

import cn.itcreator.android.reader.paramter.Constant;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery.LayoutParams;

public class UMDPicture extends Activity {

	private static final int BACK = Menu.FIRST;
	private ImageSwitcher switcher;
	private List<String> thumbIds = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.picturebrowser);
		switcher = (ImageSwitcher) findViewById(R.id.switcher);
		Log.d("create ", "mSwitcher is :" + switcher);
		switcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		switcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));
		addImageToList(Constant.FILE_PATH);
		Gallery g = (Gallery) findViewById(R.id.gallery);
		switcher.setBackgroundDrawable(Drawable
				.createFromPath(Constant.FILE_PATH));

		g.setAdapter(new ImageAdapter(this));

		g.setOnItemSelectedListener(x);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, BACK, 1, getString(R.string.back)).setIcon(
				R.drawable.uponelevel);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case BACK:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Select a picture event
	 */
	private OnItemSelectedListener x = new AdapterView.OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String path = thumbIds.get(arg2);
			Log.d("onItemSelected", path);
			switcher.setBackgroundDrawable(Drawable.createFromPath(path));
			System.gc();
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}

	};

	/**
	 * add the file path to list
	 * 
	 * @param filePath
	 *            the file path or a directory
	 */
	private void addImageToList(String filePath) {
		String tag = "addImageToList";
		File f = new File(filePath);
		if (f.isFile()) {
			f = f.getParentFile();
		}
		Log.d(tag, "start get the list files");
		File[] fs = f.listFiles();
		int length = fs.length;
		String[] imageEnds = getResources().getStringArray(R.array.imageEnds);
		for (int i = 0; i < length; i++) {
			String path = fs[i].getAbsolutePath();
			Log.d("file path is :", path);
			if (checkEnds(path, imageEnds)) {
				thumbIds.add(path);
			}
		}
		f = null;
		fs = null;
		System.gc();
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

	public class ImageAdapter extends BaseAdapter {
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return thumbIds.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			i.setImageDrawable(Drawable.createFromPath(thumbIds.get(position)));
			i.setAdjustViewBounds(true);
			i.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			return i;
		}

		private Context mContext;

	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

		}
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

		}
		if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
			// ´ò¿ª¼üÅÌ
		}
		if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
			// ¹Ø±Õ¼üÅÌ
		}
	}
}
