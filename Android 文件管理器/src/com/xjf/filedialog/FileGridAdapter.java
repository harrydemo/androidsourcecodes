package com.xjf.filedialog;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;

public class FileGridAdapter extends FileListAdapter
			implements FileAdapter{

	public FileGridAdapter(FileManager context, FileData infos, int style) {
		super(context, infos, style);
		// TODO Auto-generated constructor stub
	}
	protected void setViewExceptIcon(Viewholder holder, FileInfo fInfo) {
		TextView tv = holder.getName();
		if (tv == null) return;
		tv.setText(fInfo.name);
	}
	@Override
	public int getIconId() {
		// TODO Auto-generated method stub
		return R.id.gridicon;
	}
	@Override
	public int getFileNameTextId() {
		// TODO Auto-generated method stub
		return R.id.gridname;
	}
	@Override
	public int getLayoutId() {
		return R.layout.gridfileitem;
	}
	protected final int getStartSelfUpdateCount() { return 19;}
}
