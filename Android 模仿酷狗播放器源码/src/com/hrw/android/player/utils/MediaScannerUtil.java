package com.hrw.android.player.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

public class MediaScannerUtil {

	private MediaScannerConnection mediaScanConn = null;

	private MusicSannerClient client = null;

	private String filePath = null;

	private String fileType = null;

	private String[] filePaths = null;

//	public void fileScan(String file) {
//		Uri data = Uri.parse("file://" + file);
//
//		Log.d("TAG", "file:" + file);
//		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
//	}
//
//	public void folderScan(String path) {
//		File file = new File(path);
//
//		if (file.isDirectory()) {
//			File[] array = file.listFiles();
//
//			for (int i = 0; i < array.length; i++) {
//				File f = array[i];
//
//				if (f.isFile()) { // FILE TYPE
//					String name = f.getName();
//
//					if (name.contains(".mp3")) {
//						fileScan(f.getAbsolutePath());
//					}
//				} else { // FOLDER TYPE
//					folderScan(f.getAbsolutePath());
//				}
//			}
//		}
//	}

	/**
	 * 然后调用MediaScanner.scanFile("/sdcard/2.mp3");
	 * */
	public MediaScannerUtil(Context context) {
		// 创建MusicSannerClient
		if (client == null) {

			client = new MusicSannerClient();
		}

		if (mediaScanConn == null) {

			mediaScanConn = new MediaScannerConnection(context, client);
		}
	}

	class MusicSannerClient implements
			MediaScannerConnection.MediaScannerConnectionClient {

		public void onMediaScannerConnected() {

			if (filePath != null) {

				mediaScanConn.scanFile(filePath, fileType);
			}

			if (filePaths != null) {

				for (String file : filePaths) {

					mediaScanConn.scanFile(file, fileType);
				}
			}

			filePath = null;

			fileType = null;

			filePaths = null;
		}

		public void onScanCompleted(String path, Uri uri) {
			// TODO Auto-generated method stub
			mediaScanConn.disconnect();
		}

	}

	/**
	 * 扫描文件标签信息
	 * 
	 * @param filePath
	 *            文件路径 eg:/sdcard/MediaPlayer/dahai.mp3
	 * @param fileType
	 *            文件类型 eg: audio/mp3 media/* application/ogg
	 * */
	public void scanFile(String filepath, String fileType) {

		this.filePath = filepath;

		this.fileType = fileType;
		// 连接之后调用MusicSannerClient的onMediaScannerConnected()方法
		mediaScanConn.connect();
	}

	/**
	 * @param filePaths
	 *            文件路径
	 * @param fileType
	 *            文件类型
	 * */
	public void scanFile(String[] filePaths, String fileType) {

		this.filePaths = filePaths;

		this.fileType = fileType;

		mediaScanConn.connect();

	}

	public String getFilePath() {

		return filePath;
	}

	public void setFilePath(String filePath) {

		this.filePath = filePath;
	}

	public String getFileType() {

		return fileType;
	}

	public void setFileType(String fileType) {

		this.fileType = fileType;
	}

}
