package com.androidmediaplayer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownloaderUtils {

	/**
	 * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容 1.创建一个URL对象
	 * 2.通过URL对象，创建一个HttpURLConnection对象 3.得到InputStram 4.从InputStream当中读取数据
	 * 
	 * @param urlStr
	 * @return
	 */
	public static String downloadStringFromInternetFile(String urlStr) {
		StringBuffer sb = null;
		String line = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader buffer = null;
		try {
			inputStream = getInputStreamFromUrl(urlStr);
			inputStreamReader = new InputStreamReader(inputStream);
			// 使用IO流读取数据
			buffer = new BufferedReader(inputStreamReader);
			sb = new StringBuffer();
			while ((line = buffer.readLine()) != null) {
				sb.append(line.trim());
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (buffer != null) {
					buffer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (inputStreamReader != null) {
						inputStreamReader.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 该函数返回整形 -1：代表下载文件出错 0：代表下载文件成功 1：代表文件已经存在
	 */
	public int downLoadFile(String urlStr, String path, String finalFileName) {
		InputStream inputStream = null;
		try {
			if (FileUtil.isFileExist(finalFileName+".lrc", path)) {
				FileUtil.deleteFile(path, finalFileName+".lrc");
			}
			inputStream = getInputStreamFromUrl(urlStr);
			File resultFile = FileUtil.write2SDFromInput(path, finalFileName+".lrc",
					inputStream);
			if (resultFile == null) {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 根据URL得到输入流
	 * 
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static InputStream getInputStreamFromUrl(String urlStr)
			throws IOException {
		URL url = null;
		HttpURLConnection urlConn = null;
		url = new URL(urlStr);
		urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setConnectTimeout(6000);
		urlConn.setReadTimeout(6000);
		urlConn.setRequestMethod("GET");
		return urlConn.getInputStream();
	}

}
