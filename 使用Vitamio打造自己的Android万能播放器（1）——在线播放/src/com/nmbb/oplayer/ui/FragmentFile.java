package com.nmbb.oplayer.ui;

import java.io.File;
import java.util.ArrayList;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import com.nmbb.oplayer.R;
import com.nmbb.oplayer.business.FileBusiness;
import com.nmbb.oplayer.database.SQLiteHelper;
import com.nmbb.oplayer.database.TableColumns.FilesColumns;
import com.nmbb.oplayer.po.PFile;
import com.nmbb.oplayer.ui.base.ArrayAdapter;
import com.nmbb.oplayer.util.FileUtils;
import com.nmbb.oplayer.util.PinyinUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class FragmentFile extends FragmentBase implements OnItemClickListener {

	private FileAdapter mAdapter;
	private TextView first_letter_overlay;
	private ImageView alphabet_scroller;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		// ~~~~~~~~~ 绑定控件
		first_letter_overlay = (TextView) v
				.findViewById(R.id.first_letter_overlay);
		alphabet_scroller = (ImageView) v.findViewById(R.id.alphabet_scroller);

		// ~~~~~~~~~ 绑定事件
		alphabet_scroller.setClickable(true);
		alphabet_scroller.setOnTouchListener(asOnTouch);
		mListView.setOnItemClickListener(this);

		// ~~~~~~~~~ 加载数据
		if (new SQLiteHelper(getActivity()).isEmpty())
			new ScanVideoTask().execute();
		else
			new DataTask().execute();

		return v;
	}

	/** 单击启动播放 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final PFile f = mAdapter.getItem(position);
		Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
		intent.putExtra("path", f.path);
		startActivity(intent);
	}

	private class DataTask extends AsyncTask<Void, Void, ArrayList<PFile>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mLoadingLayout.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}

		@Override
		protected ArrayList<PFile> doInBackground(Void... params) {
			return FileBusiness.getAllSortFiles(getActivity());
		}

		@Override
		protected void onPostExecute(ArrayList<PFile> result) {
			super.onPostExecute(result);

			mAdapter = new FileAdapter(getActivity(),
					FileBusiness.getAllSortFiles(getActivity()));
			mListView.setAdapter(mAdapter);

			mLoadingLayout.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		}
	}

	/** 扫描SD卡 */
	private class ScanVideoTask extends AsyncTask<Void, File, ArrayList<PFile>> {
		private ProgressDialog pd;
		private ArrayList<File> files = new ArrayList<File>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("正在扫描视频文件...");
			pd.show();
		}

		@Override
		protected ArrayList<PFile> doInBackground(Void... params) {
			// ~~~ 遍历文件夹
			eachAllMedias(Environment.getExternalStorageDirectory());

			// ~~~ 入库
			SQLiteHelper sqlite = new SQLiteHelper(getActivity());
			SQLiteDatabase db = sqlite.getWritableDatabase();
			try {
				db.beginTransaction();

				SQLiteStatement stat = db.compileStatement("INSERT INTO files("
						+ FilesColumns.COL_TITLE + ","
						+ FilesColumns.COL_TITLE_PINYIN + ","
						+ FilesColumns.COL_PATH + ","
						+ FilesColumns.COL_LAST_ACCESS_TIME + ","
						+ FilesColumns.COL_FILE_SIZE + ") VALUES(?,?,?,?,?)");
				for (File f : files) {
					String name = f.getName();
					int index = 1;
					stat.bindString(index++, name);// title
					stat.bindString(index++,
							PinyinUtils.chineneToSpell(name.charAt(0) + ""));// title_pinyin
					stat.bindString(index++, f.getPath());// path
					stat.bindLong(index++, System.currentTimeMillis());// last_access_time
					stat.bindLong(index++, f.length());
					stat.execute();
				}
				db.setTransactionSuccessful();
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}

			// ~~~ 查询数据
			return FileBusiness.getAllSortFiles(getActivity());
		}

		@Override
		protected void onProgressUpdate(final File... values) {
			pd.setMessage(values[0].getName());
		}

		/** 遍历所有文件夹，查找出视频文件 */
		public void eachAllMedias(File f) {
			if (f != null && f.exists() && f.isDirectory()) {
				File[] files = f.listFiles();
				if (files != null) {
					for (File file : f.listFiles()) {
						publishProgress(file);
						if (file.isDirectory()) {
							eachAllMedias(file);
						} else if (file.exists() && file.canRead()
								&& FileUtils.isVideo(file)) {
							this.files.add(file);
						}
					}
				}
			}
		}

		@Override
		protected void onPostExecute(ArrayList<PFile> result) {
			super.onPostExecute(result);
			mAdapter = new FileAdapter(getActivity(), result);
			mListView.setAdapter(mAdapter);
			pd.dismiss();
		}
	}

	private class FileAdapter extends ArrayAdapter<PFile> {

		public FileAdapter(Context ctx, ArrayList<PFile> l) {
			super(ctx, l);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final PFile f = getItem(position);
			if (convertView == null) {
				final LayoutInflater mInflater = getActivity()
						.getLayoutInflater();
				convertView = mInflater.inflate(R.layout.fragment_file_item,
						null);
			}
			((TextView) convertView.findViewById(R.id.title)).setText(f.title);
			((TextView) convertView.findViewById(R.id.file_size))
					.setText(FileUtils.showFileSize(f.file_size));

			//
			return convertView;
		}

	}

	/**
	 * A-Z
	 */
	private OnTouchListener asOnTouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:// 0
				alphabet_scroller.setPressed(true);
				first_letter_overlay.setVisibility(View.VISIBLE);
				mathScrollerPosition(event.getY());
				break;
			case MotionEvent.ACTION_UP:// 1
				alphabet_scroller.setPressed(false);
				first_letter_overlay.setVisibility(View.GONE);
				break;
			case MotionEvent.ACTION_MOVE:
				mathScrollerPosition(event.getY());
				break;
			}
			return false;
		}
	};

	/**
	 * 显示字符
	 * 
	 * @param y
	 */
	private void mathScrollerPosition(float y) {
		int height = alphabet_scroller.getHeight();
		float charHeight = height / 28.0f;
		char c = 'A';
		if (y < 0)
			y = 0;
		else if (y > height)
			y = height;

		int index = (int) (y / charHeight) - 1;
		if (index < 0)
			index = 0;
		else if (index > 25)
			index = 25;

		String key = String.valueOf((char) (c + index));
		first_letter_overlay.setText(key);

		int position = 0;
		if (index == 0)
			mListView.setSelection(0);
		else if (index == 25)
			mListView.setSelection(mAdapter.getCount() - 1);
		else {
			if (mAdapter != null && mAdapter.getAll() != null) {
				for (PFile item : mAdapter.getAll()) {
					if (item.title_pinyin.startsWith(key)) {
						mListView.setSelection(position);
						break;
					}
					position++;
				}
			}
		}
	}
}
