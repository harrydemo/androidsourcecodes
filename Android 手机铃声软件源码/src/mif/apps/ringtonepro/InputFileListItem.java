package mif.apps.ringtonepro;

import android.graphics.drawable.Drawable;


public class InputFileListItem implements Comparable<InputFileListItem>{
	//文件名
	private String mName;
	//路径
	private String mDir;
	//图标
	private Drawable mIcon;
	//是否是文件夹
	private boolean isFolder;
	
	public InputFileListItem(String name,String dir,Drawable icon,boolean folder){
		mName = name;
		mIcon = icon;
		isFolder = folder;
		mDir = dir;
	}
	
	//取得文件名
	public String getName(){
		return mName;
	}
	//取得文件路径
	public String getDir(){
		return mDir;
	}
	//取得图标
	public Drawable getIcon(){
		return mIcon;
	}
	//是否问文件夹
	public boolean isFolder(){
		return isFolder;
	}
	//比较文件名是否相同
	public int compareTo(InputFileListItem another) {
		// TODO Auto-generated method stub
		if (this.mName != null)
			return this.mName.compareTo(another.getName());
		else
			throw new IllegalArgumentException();
	}
	
	
	
	
}