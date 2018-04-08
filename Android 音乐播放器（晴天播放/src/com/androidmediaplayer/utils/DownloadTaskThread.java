package com.androidmediaplayer.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import com.androidmediaplayer.model.Progress;

/**
 * 下载任务 (建议线程参数最多不超过3)
 * 
 */
public class DownloadTaskThread implements Runnable {
	private String fileUrlPath = "";
	private String fileSavePath = "";
	private int threadNum = 1;
	private Progress pg = null;
	private String finalFileName = "";
//	private ArrayList<Thread> threads = null;
//	private HashMap<String, ArrayList<Thread>> tasks = null;
//	private int taskIndex;

	
	
	public DownloadTaskThread(Progress pg, String fileUrlPath,
			String fileSavePath, String finalFileName, int threadNum, int taskIndex, ArrayList<Thread> threads) {
		this.fileUrlPath = fileUrlPath;
		this.fileSavePath = fileSavePath;
		if (threadNum > 1) {
			this.threadNum = threadNum;
		}
		this.pg = pg;
		this.finalFileName = finalFileName;
//		this.threads = threads;
//		this.tasks = tasks;
//		this.taskIndex = taskIndex;
	}

    @Override
	public void run() {

		URL url = null;
		int block = 0;
		File file = null;
//		RandomAccessFile randomFile = null;
		HttpURLConnection conn = null;
		FileChannel fileChannel = null;
		FileOutputStream fileOS = null;
		
		ByteBuffer byteBuffer = null;
        BufferedInputStream bis = null;
        InputStream inputStream = null;
		try {
			if (!FileUtil.isFilePathExist(fileSavePath)) {
				FileUtil.creatSDDir(fileSavePath);
				System.out.println(fileSavePath + "文件夹不存在，立即创建");
			}
			file = FileUtil.createFileInSDCard(finalFileName+".mp3", fileSavePath);
			System.out.println(finalFileName+".mp3" + "文件创建完毕");
			url = new URL(fileUrlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", fileUrlPath);
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			if (conn.getResponseCode() == 200) {
				int fileSize = conn.getContentLength();
				if (fileSize <= 0) {
					throw new RuntimeException("无法获取文件大小");
				}
				pg.setOrgfileSize(fileSize);
				if (threadNum == 1) {
					block = fileSize;
				} else {
					block = (fileSize / threadNum) + 1;
				}
//				randomFile = new RandomAccessFile(file, "rw");
//				long fs = (long) fileSize;
//				int startPosition = 0;
				
//				CountDownLatch threadSignal = new CountDownLatch(threadNum);
//				RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
//                randomFile.setLength(fs);
//              randomFile.seek(startPosition);
				
				fileOS = new FileOutputStream(file);
                fileChannel = fileOS.getChannel();
                
                
//				for (int i = 0; i < threadNum; i++) {
//					startPosition = i * block;
//					MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, startPosition, fileSize);
//					Thread t = new Thread(new DownloadThread(pg, fileChannel, url, startPosition, block, i, threadSignal));
//					threads.add(t);
//				}
				
//				for (int i = 0; i < threadNum; i++) {
//				    threads.get(i).start();
//				}
				
				inputStream = conn.getInputStream();
	            bis = new BufferedInputStream(inputStream);
	            int kb = block > 1024 ? 1024 : (block > 12 ? 12 : 1);
	            int bufferLength = 16 * kb;
	            byte[] buffer = new byte[bufferLength];
	            int readFileSize = 0;
	            int len = 0;
//	          randomFile.seek(startPosition);
	            int spare = 0;
	            byteBuffer = ByteBuffer.allocateDirect(bufferLength);
//	          int l = 0;
	            while (readFileSize < block && ((len = bis.read(buffer, 0, bufferLength)) != -1)) {
	                byteBuffer.put(buffer, 0, len);
//	              l = l + len;
//	              byteBuffer.position(l);
	                byteBuffer.flip();
	                fileChannel.write(byteBuffer);
	                fileChannel.force(true);
	                byteBuffer.clear();
	                readFileSize = readFileSize + len;
	                pg.setFileSize(len);
	                spare = block - readFileSize;
	                if(spare < bufferLength){
	                    bufferLength = spare;
	                }
	                Thread.sleep(250);
	            }
	            
				
				//等待所有线程运行完毕
//				threadSignal.await();
//				threads.clear();
//				tasks.remove(String.valueOf(taskIndex));
				System.out.println("所有线程运行完毕");
			} else {
				throw new RuntimeException("服务器响应错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		    try {
		        fileChannel.close();
                byteBuffer.clear();
                bis.close();
                inputStream.close();
                fileOS.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
	}
}
