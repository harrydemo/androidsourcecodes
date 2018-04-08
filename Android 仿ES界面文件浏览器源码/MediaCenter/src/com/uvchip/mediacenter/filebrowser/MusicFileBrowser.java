package com.uvchip.mediacenter.filebrowser;

import java.io.File;
import com.uvchip.files.FileManager.FileFilter;
import com.uvchip.files.FileManager.FilesFor;
import com.uvchip.files.FileManager.ViewMode;
import com.uvchip.files.FileItemForOperation;
import com.uvchip.mediacenter.R;
import android.content.Context;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MusicFileBrowser extends Browser{
	final boolean DEBUG = false;
	static{
		TAG = MusicFileBrowser.class.getCanonicalName();
		
	}
	private ListView mListView;
	private boolean onResume = false;
	
	
	public MusicFileBrowser(Context context) {
		super(context);
		initView();
		mViewMode = ViewMode.LISTVIEW;
	}
	public void onResume(){
		if (!onResume) {
			QueryData(new File("/mnt/"), true, FileFilter.MUSIC);
			onResume = true;
		}
	}

	private void initView() {
		mView = mInflater.inflate(R.layout.music_browser, null);
		mListView = (ListView)mView.findViewById(R.id.lvMusicList);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void QueryData(File preFile,boolean clear,FileFilter filter) {
		super.QueryData(preFile,clear,filter);
		mListView.setAdapter(mItemsAdapter);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		FileItemForOperation fileItem = mData.getFileItems().get(position);
		clickFileItem(fileItem);
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
	
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	@Override
	public void whichOperation(FilesFor filesFor, int size) {
		
	}

	@Override
	public void queryFinished() {
		
	}

	@Override
	public void queryMatched() {
		refreshData();
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
	}
	@Override
	public void onContextMenuClosed(Menu menu) {
		
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return false;
	}
	@Override
	public boolean onBackPressed() {
		return false;
	}
}
