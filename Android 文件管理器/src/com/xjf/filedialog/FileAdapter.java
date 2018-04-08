package com.xjf.filedialog;

public interface FileAdapter {
	
	/**
	 * 返回listview中文件item的layout, 文件文件图标和文件名在xml里的id
	 * */
	public int getLayoutId();
	
	public int getIconId();
	
	public int getFileNameTextId();
	
	
}
