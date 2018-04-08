package org.music.tools;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
/*
 * 定义一个下载器
 *
 */
public class DownLoad {
	private String urlstr;// 下载的地址
	private String localfile;// 保存路径
	private int threadcount;// 线程数
	private Handler mHandler;// 消息处理器
	private DBHelperTools dao;// 工具类
	private int fileSize;// 所要下载的文件的大小
	private List<DownloadMusicInfo> infos;// 存放下载信息类的集合
	private static final int INIT = 1;// 定义三种下载的状态：初始化状态，正在下载状态，暂停状态
	private static final int DOWNLOADING = 2;
	private static final int PAUSE = 3;
	private int state = INIT;

	public DownLoad(String urlstr, String localfile, int threadcount,
			Context context, Handler mHandler) {
		this.urlstr = urlstr;
		this.localfile = localfile;
		this.threadcount = threadcount;
		this.mHandler = mHandler;
		dao = new DBHelperTools(context);
	}

	// 判断是否在下载
	public boolean isDownLoad() {
		return state == DOWNLOADING;
	}

	public LoadInfo getDownLoadInfos() {
		if (isfirst(urlstr)) {
			init();
			int rang = fileSize / threadcount;
			infos = new ArrayList<DownloadMusicInfo>();
			for (int i = 0; i < threadcount - 1; i++) {
				DownloadMusicInfo info = new DownloadMusicInfo(i, i * rang,
						(i + 1) * rang - 1, 0, urlstr);
				infos.add(info);

			}
			DownloadMusicInfo info = new DownloadMusicInfo(threadcount - 1,
					(threadcount - 1) * rang, fileSize - 1, 0, urlstr);
			infos.add(info);
			// 保存infos中的数据到数据库
			dao.saveInfos(infos);
			// 创建一个LoadInfo对象记载下载器的具体信息
			LoadInfo loadInfo = new LoadInfo(fileSize, 0, urlstr);
			return loadInfo;
		} else {
			// 得到数据库中已有的urlstr的下载器的具体信息
			infos = dao.getInfos(urlstr);
			Log.v("TAG", "not isFirst size=" + infos.size());
			int size = 0;
			int compeleteSize = 0;
			for (DownloadMusicInfo info : infos) {
				compeleteSize += info.getCompeleteSize();
				size += info.getEndPos() - info.getStartPos() + 1;
			}
			return new LoadInfo(size, compeleteSize, urlstr);
		}

	}

	private void init() {
		try {
			URL url = new URL(urlstr);
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			fileSize = connection.getContentLength();
			File file = new File(localfile);
			if (!file.exists()) {
				file.createNewFile();
			}
			// 本地访问文件
			RandomAccessFile accessfile = new RandomAccessFile(file, "rwd");
			accessfile.setLength(fileSize);
			accessfile.close();
			connection.disconnect();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private boolean isfirst(String urlstr) {
		return dao.isHasInfors(urlstr);

	}

	public void download() {
		if (infos != null) {
			if (state == DOWNLOADING)
				return;
			state = DOWNLOADING;
			for (DownloadMusicInfo info : infos) {
				new MyThread(info.getThreadId(), info.getStartPos(),
						info.getEndPos(), info.getCompeleteSize(),
						info.getUrl()).start();
			}

		}
	}

	public class MyThread extends Thread {
		private int threadId;
		private int startPos;
		private int endPos;
		private int compeleteSize;
		private String urlstr;

		public MyThread(int threadId, int startPos, int endPos,
				int compeleteSize, String urlstr) {
			this.threadId = threadId;
			this.startPos = startPos;
			this.endPos = endPos;
			this.compeleteSize = compeleteSize;
			this.urlstr = urlstr;
		}

		@Override
		public void run() {
			HttpURLConnection connection = null;
			RandomAccessFile randomAccessFile = null;
			InputStream is = null;
			try {
				URL url = new URL(urlstr);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				// 设置范围，格式为Range：bytes x-y;
				connection.setRequestProperty("Range", "bytes="
						+ (startPos + compeleteSize) + "-" + endPos);

				randomAccessFile = new RandomAccessFile(localfile, "rwd");
				randomAccessFile.seek(startPos + compeleteSize);
				// 将要下载的文件写到保存在保存路径下的文件中
				is = connection.getInputStream();
				byte[] buffer = new byte[4096];
				int length = -1;
				while ((length = is.read(buffer)) != -1) {
					randomAccessFile.write(buffer, 0, length);
					compeleteSize += length;
					// 更新数据库中的下载信息
					dao.updataInfos(threadId, compeleteSize, urlstr);
					// 用消息将下载信息传给进度条，对进度条进行更新
					Message message = Message.obtain();
					message.what = 1;
					message.obj = urlstr;
					message.arg1 = length;
					mHandler.sendMessage(message);
					if (state == PAUSE) {

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
					randomAccessFile.close();
					connection.disconnect();
					dao.closeDb();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	// 删除数据库中urlstr对应的下载器信息
	public void delete(String urlstr) {
		dao.delete(urlstr);
	}

	// 设置暂停
	public void pause() {
		state = PAUSE;
	}

	// 重置下载状态
	public void reset() {
		state = INIT;
	}
}
