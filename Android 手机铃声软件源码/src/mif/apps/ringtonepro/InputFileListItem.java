package mif.apps.ringtonepro;

import android.graphics.drawable.Drawable;


public class InputFileListItem implements Comparable<InputFileListItem>{
	//�ļ���
	private String mName;
	//·��
	private String mDir;
	//ͼ��
	private Drawable mIcon;
	//�Ƿ����ļ���
	private boolean isFolder;
	
	public InputFileListItem(String name,String dir,Drawable icon,boolean folder){
		mName = name;
		mIcon = icon;
		isFolder = folder;
		mDir = dir;
	}
	
	//ȡ���ļ���
	public String getName(){
		return mName;
	}
	//ȡ���ļ�·��
	public String getDir(){
		return mDir;
	}
	//ȡ��ͼ��
	public Drawable getIcon(){
		return mIcon;
	}
	//�Ƿ����ļ���
	public boolean isFolder(){
		return isFolder;
	}
	//�Ƚ��ļ����Ƿ���ͬ
	public int compareTo(InputFileListItem another) {
		// TODO Auto-generated method stub
		if (this.mName != null)
			return this.mName.compareTo(another.getName());
		else
			throw new IllegalArgumentException();
	}
	
	
	
	
}