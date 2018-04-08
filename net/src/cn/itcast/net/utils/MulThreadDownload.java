package cn.itcast.net.utils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class MulThreadDownload {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path = "http://net.itcast.cn/QQWubiSetup.exe";
		try {
			new MulThreadDownload().download(path, 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getFilename(String path){
		return path.substring(path.lastIndexOf("/")+1);
	}
	
	public void download(String path, int threadsize) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		int length = conn.getContentLength();//获取网络文件的长度
		File file = new File(getFilename(path));
		RandomAccessFile localFile = new RandomAccessFile(file, "rwd");//生成一个与网络文件数据长度相同的本地文件
		localFile.setLength(length);
		localFile.close();
		//计算每一条线程负责下载的数据长度
		int block = length % threadsize == 0 ? length / threadsize : length / threadsize + 1;
		for(int i = 0 ; i < threadsize ; i++){
			new DownloadThread(i, block, url, file).start();
		}
	}
	
	private final class DownloadThread extends Thread{
		private int threadid;
		private int block;//每一条线程负责下载的数据长度
		private URL downloadUrl;
		private File localFile;
		
		public DownloadThread(int threadid, int block, URL downloadUrl, File localFile) {
			this.threadid = threadid;
			this.block = block;
			this.downloadUrl = downloadUrl;
			this.localFile = localFile;
		}

		@Override
		public void run() {
			try {
				int startposition = threadid * block;//该线程从文件的什么位置开始下载数据
				int endposition = (threadid + 1) * block - 1;//该线程下载到文件的什么位置结束
				RandomAccessFile accessFile = new RandomAccessFile(localFile, "rwd");
				accessFile.seek(startposition);//指定从本地文件的什么位置写入数据
				HttpURLConnection conn = (HttpURLConnection)downloadUrl.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5 * 1000);
				//指定从文件的什么位置读取数据，读取到什么位置结束
				conn.setRequestProperty("Range", "bytes="+ startposition+ "-"+ endposition);
				InputStream inStream = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inStream.read(buffer)) != -1){
					accessFile.write(buffer, 0, len);
				}
				inStream.close();
				accessFile.close();
				System.out.println("线程"+ threadid+ " 下载完成");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}

}
