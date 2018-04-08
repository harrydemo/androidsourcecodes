package com.zyx.adscantexst.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.zyx.adscantexst.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/**
 * 
 * @author zyx
 * @since 2012-10-16 下午4:51:05
 * @version 1.00
 */

public class GalleryMoveTest extends Activity {
	private TextView tv_hintTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallerytest);
		tv_hintTextView = (TextView) findViewById(R.id.tv_hint);
		new AsyncTask<String, Integer, ArrayList<HashMap<String, Drawable>>>() {

			@Override
			protected ArrayList<HashMap<String, Drawable>> doInBackground(
					String... params) {
				ArrayList<HashMap<String, Drawable>> appList = null;
				appList = getAppList();
				return appList;
			}

			@Override
			protected void onPostExecute(
					final ArrayList<HashMap<String, Drawable>> result) {
				super.onPostExecute(result);
				if (result != null) {
					final Gallery gallery = (Gallery) findViewById(R.id.gallery1);
					gallery.setPadding(10, 10, 10, 10);
					gallery.setAdapter(new ImageAdapter(GalleryMoveTest.this,
							result));
					final ProgressBar pBar = (ProgressBar) findViewById(R.id.pb_gallery);
					pBar.setMax(result.size());
					final Handler handler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							int what = msg.what;
							pBar.setProgress(what + 1);
							gallery.setSelection(what);
							tv_hintTextView.setText((what + 1) + "/"
									+ result.size());
							Log.i("max", "max:" + result.size() + "prog "
									+ (what + 1));
						}

					};
					new Thread(new Runnable() {

						private int progress = 0;

						@Override
						public void run() {
							while (progress < result.size()) {

								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								handler.sendEmptyMessage(progress++);
							}

						}
					}).start();
				}
			}
		}.execute();
	}

	private ArrayList<HashMap<String, Drawable>> getAppList() {
		PackageManager pManager = getPackageManager();
		List<ApplicationInfo> applications = pManager
				.getInstalledApplications(0);
		ArrayList<HashMap<String, Drawable>> list = null;
		if (applications != null && applications.size() > 0) {
			list = new ArrayList<HashMap<String, Drawable>>();
			HashMap<String, Drawable> map = null;
			for (ApplicationInfo applicationInfo : applications) {
				map = new HashMap<String, Drawable>();
				String name = applicationInfo.loadLabel(pManager).toString();
				Drawable icon = applicationInfo.loadIcon(pManager);
				map.put(name, icon);
				list.add(map);
			}
		}
		return list;
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<HashMap<String, Drawable>> mList;

		public ImageAdapter(Context context,
				ArrayList<HashMap<String, Drawable>> list) {

			this.mContext = context;
			this.mList = list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HashMap<String, Drawable> hashMap = mList.get(position);
			Collection<Drawable> values = hashMap.values();
			ImageView imageView = null;
			for (Drawable drawable : values) {
				imageView = new ImageView(mContext);
				imageView.setScaleType(ScaleType.FIT_XY);
				Gallery.LayoutParams galleryParams = new Gallery.LayoutParams(
						100, 100);
				imageView.setLayoutParams(galleryParams);
				imageView.setImageDrawable(drawable);
			}
			return imageView;
		}
	}

}
