/*****************Copyright (C), 2010-2015, FORYOU Tech. Co., Ltd.********************/
package com.android.photostore;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.photostore.Constant;
import com.android.photostore.GridImageView;
import com.android.photostore.Constant.ImageFolderInfo;

/**
 * @Filename: Gallery.java
 * @Author: wanghb
 * @Email: wanghb@foryouge.com.cn
 * @CreateDate: 2011-7-18
 * @Description: description of the new class
 * @Others: comments
 * @ModifyHistory:
 */
public class Gallery extends Activity {
	public static Activity mActivity;
	private LinearLayout data;
	private LayoutInflater mInflater;

	private final static int UPDATELIST = 0;

	private class ScanThread extends Thread {
		@Override
		public void run() {
			final String mCardPath = Environment.getExternalStorageDirectory().getPath();
			getFiles(mCardPath);
		}
	}

	// uiœﬂ≥Ã
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case UPDATELIST:
					ImageFolderInfo holder = (ImageFolderInfo) msg.obj;
					View convertView = mInflater.inflate(R.layout.list_item, null);
					((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(holder.image);
					File file = new File(holder.path);
					((TextView) convertView.findViewById(R.id.name)).setText(file.getName());
					((TextView) convertView.findViewById(R.id.path)).setText(holder.path);
					((TextView) convertView.findViewById(R.id.picturecount)).setText(holder.pisNum + "");
					convertView.setTag(holder);
					convertView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ImageFolderInfo info = (ImageFolderInfo) v.getTag();
							Intent intent = new Intent(Gallery.this, GridImageView.class);
							intent.putStringArrayListExtra("data", info.filePathes);
							startActivity(intent);
						}
					});
					data.addView(convertView);
					break;

				default:
					break;
			}
//			removeMessages(msg.what);
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		setContentView(R.layout.list_view);
		data = (LinearLayout) findViewById(R.id.data);
		mInflater = LayoutInflater.from(this);
		// mAdapter = new ListViewAdapter(this);
		// setListAdapter(mAdapter);
		new ScanThread().start();
	}


	private void getFiles(String path) {
		File f = new File(path);
		File[] files = f.listFiles();
		ImageFolderInfo ifi = new ImageFolderInfo();
		ifi.path = path;
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				final File ff = files[i];
				if (ff.isDirectory()) {
					getFiles(ff.getPath());
				} else {
					String fName = ff.getName();
					if (fName.indexOf(".") > -1) {
						String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toUpperCase();
						if (Constant.getExtens().contains(end)) {
							ifi.filePathes.add(ff.getPath());
						}
					}
				}
			}
		}
		if (!ifi.filePathes.isEmpty()) {
			ifi.pisNum = ifi.filePathes.size();
			String imagePath = ifi.filePathes.get(0);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 7;
			Bitmap bm = BitmapFactory.decodeFile(imagePath, options);
			ifi.image = new BitmapDrawable(bm);
			Message msg = new Message();
			msg.what = UPDATELIST;
			msg.obj = ifi;
			mHandler.sendMessage(msg);
		}
	}

}