package com.uvchip.mediacenter.filebrowser;

import java.io.File;
import com.uvchip.files.FileManager.FileFilter;
import com.uvchip.files.FileManager.FilesFor;
import com.uvchip.files.FileManager.ViewMode;
import com.uvchip.files.FileItemForOperation;
import com.uvchip.mediacenter.MainActivity;
import com.uvchip.mediacenter.R;
import android.content.Context;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

public class ImageFileBrowser extends Browser{
	final boolean DEBUG = false;
	static{
		TAG = ImageFileBrowser.class.getCanonicalName();
		
	}
	private GridView mGridView;
	private ListView mListView;
	private boolean onResume = false;
	private final int MENU_FIRST 		   = Menu.FIRST + 400;
	private final int MENU_SWITCH_MODE     = MENU_FIRST;
	public ImageFileBrowser(Context context) {
		super(context);
		initView();
		mViewMode = ViewMode.GRIDVIEW;
	}
	public void onResume(){
		if (!onResume) {
			QueryData(new File("/mnt/"), true, FileFilter.PICTURE);
			onResume = true;
		}
	}

	private void initView() {
		mView = mInflater.inflate(R.layout.image_browser, null);
		mListView = (ListView)mView.findViewById(R.id.lvImageList);
		mListView.setOnItemClickListener(this);
		mListView.setVisibility(View.GONE);
		mGridView = (GridView)mView.findViewById(R.id.gvImageList);
		mGridView.setOnItemClickListener(this);
		mGridView.setNumColumns(MainActivity.mScreenWidth / 160);
	}

	@Override
	public void QueryData(File preFile,boolean clear,FileFilter filter) {
		super.QueryData(preFile,clear,filter);
		toggleViewMode();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		FileItemForOperation fileItem = mData.getFileItems().get(position);
		clickFileItem(fileItem);
	}
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.add(1, MENU_SWITCH_MODE, Menu.NONE, 
				mViewMode == ViewMode.LISTVIEW ? R.string.menu_mode_grid : R.string.menu_mode_list)
				.setIcon(mViewMode == ViewMode.LISTVIEW ? R.drawable.toolbar_mode_icon : R.drawable.toolbar_mode_list);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case MENU_SWITCH_MODE:
        	toggleMode();
        	toggleViewMode();
        	return true;
        default:
            break;
		}
		return false;
	}

    private void toggleViewMode(){       
        switch (mViewMode) {
            case LISTVIEW:
            	mItemsAdapter.setViewMode(ViewMode.LISTVIEW);
                mListView.setVisibility(View.VISIBLE);
                mListView.setAdapter(mItemsAdapter);
                mGridView.setVisibility(View.GONE);
                mGridView.setAdapter(null);
                break;
            case GRIDVIEW:
            	mItemsAdapter.setViewMode(ViewMode.GRIDVIEW);
                mGridView.setVisibility(View.VISIBLE);
                mGridView.setAdapter(mItemsAdapter);
                mListView.setVisibility(View.GONE);
                mListView.setAdapter(null);
                break;
            default:
                break;
        }
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
