package com.androidmediaplayer.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;

import com.androidmediaplayer.model.Progress;

/**
 * 下载线程
 * 
 */
public class DownloadThread implements Runnable {

	private URL url = null;
	private int startPosition = 0;
	private int block = 0;
	private int threadId = 0;
	private FileChannel fileChannel = null;
	private CountDownLatch threadsSignal = null;
	private Progress pg = null;
//	private long fs = 0l;

	public DownloadThread(Progress pg, FileChannel fileChannel, URL url, int startPosition, int block,
			int threadId, CountDownLatch threadsSignal) {
		this.fileChannel = fileChannel;
		this.url = url;
		this.startPosition = startPosition;
		this.block = block;
		this.threadId = threadId;
		this.threadsSignal = threadsSignal;
		this.pg = pg;
//		this.fs = fs;
	}

	@Override
	public void run() {
		System.out.println(threadId +" 线程启动");
		System.out.println("startPosition: "+startPosition);
		HttpURLConnection conn = null;
		ByteBuffer byteBuffer = null;
		BufferedInputStream bis = null;
		InputStream inputStream = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", url.toString());
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Range", "bytes=" + this.startPosition + "-");
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			inputStream = conn.getInputStream();
			bis = new BufferedInputStream(inputStream);
			int kb = block > 1024 ? 1024 : (block > 12 ? 12 : 1);
			int bufferLength = 2 * kb;
			byte[] buffer = new byte[bufferLength];
			int readFileSize = 0;
			int len = 0;
//			randomFile.seek(startPosition);
			int spare = 0;
			byteBuffer = ByteBuffer.allocateDirect(bufferLength);
//			int l = 0;
			while (readFileSize < block && ((len = bis.read(buffer, 0, bufferLength)) != -1)) {
			    byteBuffer.put(buffer, 0, len);
//			    l = l + len;
//			    byteBuffer.position(l);
			    byteBuffer.flip();
			    fileChannel.write(byteBuffer);
			    byteBuffer.clear();
				readFileSize = readFileSize + len;
				pg.setFileSize(len);
				spare = block - readFileSize;
				if(spare < bufferLength){
					bufferLength = spare;
				}
			}
			
//			byteBuffer.flip();
//			mappedByteBuffer.put(byteBuffer);
			System.out.println(threadId + "完成");
			threadsSignal.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    try {
		        fileChannel.close();
                byteBuffer.clear();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
//		    byteBuffer.clear();
            fileChannel = null;
		    byteBuffer = null;
		    try {
                bis.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
		    conn.disconnect();
//		    byteBuffer = null;
//			try {
//				if(null != randomFile){
//					randomFile.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}

	}

}
