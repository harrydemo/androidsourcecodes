package com.pixtas.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import android.app.Application;
import android.os.Environment;
import android.os.StatFs;
import android.webkit.URLUtil;

public class FileDownHelper {

	public static String getSaveAudioName(Application app) {

		// SDCARD first;
		return getLocalDirectory(app) + "/" + "yoga.mp3";
	}
	
	public static String getSaveImgName(Application app,String index) {

		// SDCARD first;
		return getLocalDirectory(app) + "/" + "chapter" + index + ".jpg";
	}

	public static String getInternalDirectory(Application app) {
		return app.getFilesDir().getAbsolutePath();
	}

	public static String getLocalDirectory(Application app) {

		// SDCARD first;
		String sdcard = getExternalDirectory(app);
		if (sdcard != null)
			return sdcard;

		return getInternalDirectory(app);
	}

	private static String getExternalDirectory(Application app) {
		// boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;

		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			//mExternalStorageAvailable = true;
			mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			//mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		}

		if (!mExternalStorageWriteable)
			return null;

		String ret = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Android/data/" + app.getPackageName();

		File f = new File(ret);

		if (f.isFile()) {
			f.delete();
		}

		if (!f.exists()) {
			f.mkdirs();
		}

		return ret;
	}
	
	/**
     * 获取SdCard路径
     */
    public static String getExternalStoragePath() {

              // 获取SdCard状态

              String state = android.os.Environment.getExternalStorageState();

              // 判断SdCard是否存在并且是可用的

              if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
                       if (android.os.Environment.getExternalStorageDirectory().canWrite()) {
                                return android.os.Environment.getExternalStorageDirectory().getPath();
                       }
              }
              return null;
    }
    
    /**
     * 获取存储卡的剩余容量，单位为字节
     * @param filePath
     * @return availableSpare
     */

    public static long getAvailableStore(String filePath) {

              // 取得sdcard文件路径

              StatFs statFs = new StatFs(filePath);

              // 获取block的SIZE

              long blocSize = statFs.getBlockSize();

              // 获取BLOCK数量

// long totalBlocks = statFs.getBlockCount();


              // 可使用的Block的数量

              long availaBlock = statFs.getAvailableBlocks();
              
// long total = totalBlocks * blocSize;


              long availableSpare = availaBlock * blocSize;
              
              return availableSpare;

    }
	
	public static String getFile(String strPath, String strSaveTo) {
		String ret = "";
		try {
			ret = getDataSource(strPath, strSaveTo);
			// ret = rawDownload(strPath, strSaveTo);
		} catch (Exception e) {
			ret = "Cannot access this file.";
		}
		return ret;
	}


	private static String getDataSource(String strURL, String strSaveTo)
			throws Exception {
		String ret = "";
		if (!URLUtil.isNetworkUrl(strURL)) {
			ret = "URL error";
		} else {
			/* 鍙栧緱URL */
			URL myURL = new URL(strURL);
			/* 鍒涘缓杩炴帴 */
			URLConnection conn = myURL.openConnection();
			conn.connect();
			conn.setConnectTimeout(10 * 60 * 1000);

			InputStream is = conn.getInputStream();
			if (is == null) {
				throw new RuntimeException("stream is null");
			}
	
			String fileEx = "";
			fileEx = strURL.substring(strURL.lastIndexOf(".") + 1,
					strURL.length()).toLowerCase();
			
			if (!fileEx.toLowerCase().equals("mp3") && !fileEx.toLowerCase().equals("jpg") && !fileEx.toLowerCase().equals("png")) {
				
				return fileEx + "file type error";
			}
			delFile(strSaveTo);
			File myFile = new File(strSaveTo);
			FileOutputStream fos = new FileOutputStream(myFile);
			byte buf[] = new byte[4096];
			do {
				int numread = is.read(buf);
				if (numread <= 0) {
					break;
				}
				fos.write(buf, 0, numread);
			} while (true);

			try {
				is.close();
			} catch (Exception ex) {
				//Log.e(Config.LOGNAME, "error: " + ex.getMessage(), ex);
			}
			// openFile(fileSaveTo);
		}
		return ret;
	}


	public static void delFile(String strFileName) {
		if (strFileName.equals("")) {
			return;
		}

		File myFile = new File(strFileName);
		if (myFile.exists()) {
			myFile.delete();
		}
	}
	
	public static int getFileLength(String strSaveTo){
		long len = 0;
		File myFile = new File(strSaveTo);
		if(myFile.exists() && myFile.isFile()){
			len = myFile.length();
		}
		return (int)len;
	}
	public static int getDirLength(String strSaveTo){
		long len = 0;
		File myFile = new File(strSaveTo);
		if(myFile.exists() && myFile.isDirectory()){
			File[] files = myFile.listFiles();
			for (File file : files) {
				len += file.length();
			}
		}
		return (int)len;
	}
}
