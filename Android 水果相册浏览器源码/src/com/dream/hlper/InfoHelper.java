package com.dream.hlper;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;


public class InfoHelper 
{
    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";
    
	/**
	 * ʹ�õ�ǰʱ���ƴ��һ��Ψһ���ļ���
	 * @param format
	 * @return
	 */
    public static String getFileName() 
    {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
    	String fileName = format.format( new Timestamp( System.currentTimeMillis()) );
    	return fileName;
    }
    
    /**
     * ��ȡ�����ʹ�õ�Ŀ¼
     * @return
     */
    public static String getCamerPath()
    {
    	return Environment.getExternalStorageDirectory() + File.separator +  "FounderNews" + File.separator;
    }
    
	/**
	 * �жϵ�ǰUrl�Ƿ��׼��content://��ʽ��������ǣ��򷵻ؾ���·��
	 * @param uri
	 * @return
	 */
	public static String getAbsolutePathFromNoStandardUri( Uri mUri )
	{	
		String filePath = null;
		
		String mUriString = mUri.toString();
		mUriString = Uri.decode(mUriString);
		
		String pre1 = "file://" + SDCARD + File.separator;
		String pre2 = "file://" + SDCARD_MNT + File.separator;
		
		if( mUriString.startsWith(pre1) )
		{    
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring( pre1.length() );
		}
		else if( mUriString.startsWith(pre2) )
		{
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring( pre2.length() );
		}
		return filePath;
	}
	
	 /**
     * ��������Ƿ����
     * @param context
     * @return
     */
    public static boolean checkNetWork( Context context )
    {
    	boolean newWorkOK = false;  
        ConnectivityManager connectManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if( connectManager.getActiveNetworkInfo() != null )
    	{
    		newWorkOK = true;
    	}
        return newWorkOK;
    }
	
	/**
	 * ����·����ȡ��Ӧ��Ļ��С��Bitmap
	 * @param context
	 * @param filePath
	 * @param maxWidth
	 * @return
	 */
	public static Bitmap getScaleBitmap( Context context, String filePath )
	{
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 4;
		
		return BitmapFactory.decodeFile(filePath, opts);
	}
	

}