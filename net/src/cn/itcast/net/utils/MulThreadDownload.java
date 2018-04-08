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
		int length = conn.getContentLength();//��ȡ�����ļ��ĳ���
		File file = new File(getFilename(path));
		RandomAccessFile localFile = new RandomAccessFile(file, "rwd");//����һ���������ļ����ݳ�����ͬ�ı����ļ�
		localFile.setLength(length);
		localFile.close();
		//����ÿһ���̸߳������ص����ݳ���
		int block = length % threadsize == 0 ? length / threadsize : length / threadsize + 1;
		for(int i = 0 ; i < threadsize ; i++){
			new DownloadThread(i, block, url, file).start();
		}
	}
	
	private final class DownloadThread extends Thread{
		private int threadid;
		private int block;//ÿһ���̸߳������ص����ݳ���
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
				int startposition = threadid * block;//���̴߳��ļ���ʲôλ�ÿ�ʼ��������
				int endposition = (threadid + 1) * block - 1;//���߳����ص��ļ���ʲôλ�ý���
				RandomAccessFile accessFile = new RandomAccessFile(localFile, "rwd");
				accessFile.seek(startposition);//ָ���ӱ����ļ���ʲôλ��д������
				HttpURLConnection conn = (HttpURLConnection)downloadUrl.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5 * 1000);
				//ָ�����ļ���ʲôλ�ö�ȡ���ݣ���ȡ��ʲôλ�ý���
				conn.setRequestProperty("Range", "bytes="+ startposition+ "-"+ endposition);
				InputStream inStream = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inStream.read(buffer)) != -1){
					accessFile.write(buffer, 0, len);
				}
				inStream.close();
				accessFile.close();
				System.out.println("�߳�"+ threadid+ " �������");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}

}
