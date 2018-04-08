package com.android.photostore;

import java.io.File;

import android.app.ListActivity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.photostore.Constant.ImageFolderInfo;

public class ListFolder extends ListActivity implements UIinterface {

	// private ListViewAdapter mAdapter;
	private LinearLayout data;
	private LayoutInflater mInflater;
	private Thread mThread = new ScanThread();
	private boolean exit;
	
	private class ScanThread extends Thread {
		@Override
		public void run() {
			final String mCardPath = Environment.getExternalStorageDirectory().getPath();
			getFiles(mCardPath, ListFolder.this);
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
					data.addView(convertView);
					// mAdapter.notifyDataSetChanged();
					break;

				default:
					break;
			}
			removeMessages(msg.what);
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		data = (LinearLayout) findViewById(R.id.data);
		mInflater = LayoutInflater.from(this);
		// mAdapter = new ListViewAdapter(this);
		// setListAdapter(mAdapter);
		mThread.start();
	}


	@Override
	public void updateUI() {
		Message msg = new Message();
		msg.what = UPDATELIST;
		mHandler.sendMessage(msg);
	}


	private void getFiles(String path, final UIinterface ui) {
		if(exit){
			return;
		}
		File f = new File(path);
		File[] files = f.listFiles();
		ImageFolderInfo ifi = new ImageFolderInfo();
		ifi.path = path;
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				final File ff = files[i];
				if (ff.isDirectory()) {
					getFiles(ff.getPath(), ui);
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
			options.inSampleSize = 8;
			Bitmap bm = BitmapFactory.decodeFile(imagePath, options);
			ifi.image = new BitmapDrawable(bm);
			Message msg = new Message();
			msg.what = UPDATELIST;
			msg.obj = ifi;
			mHandler.sendMessage(msg);
		}
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		ImageFolderInfo info = (ImageFolderInfo) v.getTag();
		Intent intent = new Intent(this, GridImageView.class);
		intent.putStringArrayListExtra("data", info.filePathes);
		startActivity(intent);
	}


	@Override
	protected void onDestroy() {
		exit = true;
		super.onDestroy();
	}

}