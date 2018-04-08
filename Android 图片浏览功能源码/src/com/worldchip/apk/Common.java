package com.worldchip.apk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

public class Common {
 
	public static String getFileSize(long fileS)
	{
		DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
	}
	
	public static long getFileSize(File f) {//转换文件大小
		
		if(f==null) return 0;
		long size = 0;
        File[] files = f.listFiles();
        if(files==null ||files.length==0)
        	return f.length();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                size = size + getFileSize(file);
            } 
            else
            {
                size = size + file.length();
            }
        }
        
        return size;
    }
	
	public static String getFileDateTime(File file)
	{
		long  time=file.lastModified();
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.getTime().toLocaleString();
	}
}
