package com.uvchip.files;

import java.io.File;

import android.os.StatFs;

import com.uvchip.utils.Helper;

public class CapacityItem {
	private File mFile;
	
	public String mTotalSize;
	
	public String mCapacitySize;
	
	public String mPath;
	
	public CapacityItem(){}
	
	public CapacityItem(File file){
		mFile = file;
		if(file!=null){
			getMessage();
		}
	}
	
	private void getMessage(){
		mPath = mFile.getAbsolutePath()+"/";
		StatFs statFs = new StatFs(mPath);	
		mTotalSize = Helper.formatFromSize(((long)statFs.getBlockCount())*statFs.getBlockSize());
		mCapacitySize = Helper.formatFromSize(((long)statFs.getAvailableBlocks())*statFs.getBlockSize());
	}
}
