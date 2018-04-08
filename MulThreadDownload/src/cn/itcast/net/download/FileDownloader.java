package cn.itcast.net.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cn.itcast.service.FileService;

import android.content.Context;
import android.util.Log;
/**
 * �ļ�������
 * FileDownloader loader = new FileDownloader(context, "http://browse.babasport.com/ejb3/ActivePort.exe",
				new File("D:\\androidsoft\\test"), 2);
		loader.getFileSize();//�õ��ļ��ܴ�С
		try {
			loader.download(new DownloadProgressListener(){
				public void onDownloadSize(int size) {
					print("�Ѿ����أ�"+ size);
				}			
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
 */
public class FileDownloader {
	private static final String TAG = "FileDownloader";
	private Context context;
	private FileService fileService;	
	/* �������ļ����� */
	private int downloadSize = 0;
	/* ԭʼ�ļ����� */
	private int fileSize = 0;
	/* �߳��� */
	private DownloadThread[] threads;
	/* ���ر����ļ� */
	private File saveFile;
	/* ������߳����صĳ���*/
	private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();
	/* ÿ���߳����صĳ��� */
	private int block;
	/* ����·��  */
	private String downloadUrl;
	/**
	 * ��ȡ�߳���
	 */
	public int getThreadSize() {
		return threads.length;
	}
	/**
	 * ��ȡ�ļ���С
	 * @return
	 */
	public int getFileSize() {
		return fileSize;
	}
	/**
	 * �ۼ������ش�С
	 * @param size
	 */
	protected synchronized void append(int size) {
		downloadSize += size;
	}
	/**
	 * ����ָ���߳�������ص�λ��
	 * @param threadId �߳�id
	 * @param pos ������ص�λ��
	 */
	protected synchronized void update(int threadId, int pos) {
		this.data.put(threadId, pos);
		this.fileService.update(this.downloadUrl, this.data);
	}
	/**
	 * �����ļ�������
	 * @param downloadUrl ����·��
	 * @param fileSaveDir �ļ�����Ŀ¼
	 * @param threadNum �����߳���
	 */
	public FileDownloader(Context context, String downloadUrl, File fileSaveDir, int threadNum) {
		try {
			this.context = context;
			this.downloadUrl = downloadUrl;
			fileService = new FileService(this.context);
			URL url = new URL(this.downloadUrl);
			if(!fileSaveDir.exists()) fileSaveDir.mkdirs();
			this.threads = new DownloadThread[threadNum];					
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5*1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", downloadUrl); 
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			printResponseHeader(conn);
			if (conn.getResponseCode()==200) {
				this.fileSize = conn.getContentLength();//������Ӧ��ȡ�ļ���С
				if (this.fileSize <= 0) throw new RuntimeException("Unkown file size ");
						
				String filename = getFileName(conn);//��ȡ�ļ�����
				this.saveFile = new File(fileSaveDir, filename);//���������ļ�
				Map<Integer, Integer> logdata = fileService.getData(downloadUrl);//��ȡ���ؼ�¼
				if(logdata.size()>0){//����������ؼ�¼
					for(Map.Entry<Integer, Integer> entry : logdata.entrySet())
						data.put(entry.getKey(), entry.getValue());//�Ѹ����߳��Ѿ����ص����ݳ��ȷ���data��
				}
				if(this.data.size()==this.threads.length){//������������߳��Ѿ����ص����ݳ���
					for (int i = 0; i < this.threads.length; i++) {
						this.downloadSize += this.data.get(i+1);
					}
					print("�Ѿ����صĳ���"+ this.downloadSize);
				}
				//����ÿ���߳����ص����ݳ���
				this.block = (this.fileSize % this.threads.length)==0? this.fileSize / this.threads.length : this.fileSize / this.threads.length + 1;
			}else{
				throw new RuntimeException("server no response ");
			}
		} catch (Exception e) {
			print(e.toString());
			throw new RuntimeException("don't connection this url");
		}
	}
	/**
	 * ��ȡ�ļ���
	 */
	private String getFileName(HttpURLConnection conn) {
		String filename = this.downloadUrl.substring(this.downloadUrl.lastIndexOf('/') + 1);
		if(filename==null || "".equals(filename.trim())){//�����ȡ�����ļ�����
			for (int i = 0;; i++) {
				String mine = conn.getHeaderField(i);
				if (mine == null) break;
				if("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())){
					Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
					if(m.find()) return m.group(1);
				}
			}
			filename = UUID.randomUUID()+ ".tmp";//Ĭ��ȡһ���ļ���
		}
		return filename;
	}
	
	/**
	 *  ��ʼ�����ļ�
	 * @param listener �������������ı仯,�������Ҫ�˽�ʵʱ���ص�����,��������Ϊnull
	 * @return �������ļ���С
	 * @throws Exception
	 */
	public int download(DownloadProgressListener listener) throws Exception{
		try {
			RandomAccessFile randOut = new RandomAccessFile(this.saveFile, "rw");
			if(this.fileSize>0) randOut.setLength(this.fileSize);
			randOut.close();
			URL url = new URL(this.downloadUrl);
			if(this.data.size() != this.threads.length){
				this.data.clear();
				for (int i = 0; i < this.threads.length; i++) {
					this.data.put(i+1, 0);//��ʼ��ÿ���߳��Ѿ����ص����ݳ���Ϊ0
				}
			}
			for (int i = 0; i < this.threads.length; i++) {//�����߳̽�������
				int downLength = this.data.get(i+1);
				if(downLength < this.block && this.downloadSize<this.fileSize){//�ж��߳��Ƿ��Ѿ��������,�����������	
					this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i+1), i+1);
					this.threads[i].setPriority(7);
					this.threads[i].start();
				}else{
					this.threads[i] = null;
				}
			}
			this.fileService.save(this.downloadUrl, this.data);
			boolean notFinish = true;//����δ���
			while (notFinish) {// ѭ���ж������߳��Ƿ��������
				Thread.sleep(900);
				notFinish = false;//�ٶ�ȫ���߳��������
				for (int i = 0; i < this.threads.length; i++){
					if (this.threads[i] != null && !this.threads[i].isFinish()) {//��������߳�δ�������
						notFinish = true;//���ñ�־Ϊ����û�����
						if(this.threads[i].getDownLength() == -1){//�������ʧ��,����������
							this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i+1), i+1);
							this.threads[i].setPriority(7);
							this.threads[i].start();
						}
					}
				}				
				if(listener!=null) listener.onDownloadSize(this.downloadSize);//֪ͨĿǰ�Ѿ�������ɵ����ݳ���
			}
			fileService.delete(this.downloadUrl);
		} catch (Exception e) {
			print(e.toString());
			throw new Exception("file download fail");
		}
		return this.downloadSize;
	}
	/**
	 * ��ȡHttp��Ӧͷ�ֶ�
	 * @param http
	 * @return
	 */
	public static Map<String, String> getHttpResponseHeader(HttpURLConnection http) {
		Map<String, String> header = new LinkedHashMap<String, String>();
		for (int i = 0;; i++) {
			String mine = http.getHeaderField(i);
			if (mine == null) break;
			header.put(http.getHeaderFieldKey(i), mine);
		}
		return header;
	}
	/**
	 * ��ӡHttpͷ�ֶ�
	 * @param http
	 */
	public static void printResponseHeader(HttpURLConnection http){
		Map<String, String> header = getHttpResponseHeader(http);
		for(Map.Entry<String, String> entry : header.entrySet()){
			String key = entry.getKey()!=null ? entry.getKey()+ ":" : "";
			print(key+ entry.getValue());
		}
	}

	private static void print(String msg){
		Log.i(TAG, msg);
	}
}
