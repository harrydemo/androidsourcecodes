package org.app.music;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.music.tools.Constant;
import org.music.tools.DownLoad;
import org.music.tools.HttpDownload;
import org.music.tools.LoadInfo;
import org.music.tools.Mp3Info;
import org.music.tools.Mp3ListContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 网络音乐这部分，我看了很多列子，发现很多音乐网站不会提供API，少数什么搜搜，百度给你的
 * 但是觉得不怎么实用，所以采用mars的那个例子。自己搭建Tomcat，然后解析并显示。写完了别忘记加Internet和SD卡权限
 */
public class NetMusicActivity extends Activity {
	ListView listview;
	private static final int UPDATE = Menu.FIRST;
	private List<Mp3Info> mp3infos = null;
	// 固定存放下载的音乐的路径：SD卡目录下
		private static final String SD_PATH = "/sdcard/music/";
		// 存放各个下载器
		private Map<String, DownLoad> downloaders = new HashMap<String, DownLoad>();
		// 存放与下载器对应的进度条
		private Map<String, ProgressBar> ProgressBars = new HashMap<String, ProgressBar>();
		/**
		 * 用handler更新进度条
		 */
		private Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					String url = (String) msg.obj;
					int length = msg.arg1;
					ProgressBar bar = ProgressBars.get(url);
					if (bar != null) {
						// 设置进度条按读取的length长度更新
						bar.incrementProgressBy(length);
						if (bar.getProgress() == bar.getMax()) {
							Toast.makeText(NetMusicActivity.this, "下载完成！", 0).show();
							// 下载完成后清除进度条并将map中的数据清空
							LinearLayout layout = (LinearLayout) bar.getParent();
							layout.removeView(bar);
							ProgressBars.remove(url);
							downloaders.get(url).delete(url);
							downloaders.get(url).reset();
							downloaders.remove(url);

						}
					}
				}
			}
		};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.net_music);
		listview = (ListView) findViewById(R.id.net_music_list);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, UPDATE, 0, "更新列表");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == UPDATE) {
			UpdateMusic();// 按更新调用这个方法
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 更新在线音乐列表
	 */
	private void UpdateMusic() {
		String xml = UpdateListView("http://192.168.2.2:8080/mp3/netmp3list.xml");// 解析xml
		mp3infos = parse(xml);
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		// 把mp3info的信息用迭代
		for (Iterator<Mp3Info> iterator = mp3infos.iterator(); iterator
				.hasNext();) {
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			// 用Map对象装载起来
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("mp3name", mp3Info.getMp3Name());
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.download_info, new String[] { "mp3name"}, new int[] {R.id.tv_resouce_name});
		listview.setAdapter(adapter);
	}

	

	

	/**
	 * 更新列表
	 */
	private String UpdateListView(String urlStr) {
		String result = HttpDownload.downloadStringFromInternetFile(urlStr);

		return result;

	}

	/**
	 * 解析远程服务器的XML
	 */
	private List<Mp3Info> parse(String xmlStr) {
		SAXParserFactory sax = SAXParserFactory.newInstance();
		List<Mp3Info> infos = new ArrayList<Mp3Info>();
		try {
			XMLReader xmlread = sax.newSAXParser().getXMLReader();

			Mp3ListContentHandler mlch = new Mp3ListContentHandler(infos);
			xmlread.setContentHandler(mlch);
			xmlread.parse(new InputSource(new StringReader(xmlStr)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;

	}

	/**
	 * 响应开始下载按钮的点击事件 
	 */
	public void startDownload(View v) {
		// 得到textView的内容
		LinearLayout layout = (LinearLayout) v.getParent();
		String musicName = ((TextView) layout
				.findViewById(R.id.tv_resouce_name)).getText().toString();
		String urlstr = Constant.URL.BASE_URL + musicName;
		String localfile = SD_PATH + musicName;
		// 设置下载线程数为4，这里是我为了方便随便固定的
		int threadcount = 4;
		// 初始化一个downloader下载器
		DownLoad downloader = downloaders.get(urlstr);
		if (downloader == null) {
			downloader = new DownLoad(urlstr, localfile, threadcount, this,
					mHandler);
			downloaders.put(urlstr, downloader);
		}
		if (downloader.isDownLoad())
			return;
		// 得到下载信息类的个数组成集合
		LoadInfo loadInfo = downloader.getDownLoadInfos();
		// 显示进度条
		showProgress(loadInfo, urlstr, v);
		// 调用方法开始下载
		downloader.download();
	}

	/**
	 * 显示进度条
	 */
	private void showProgress(LoadInfo loadInfo, String url, View v) {
		ProgressBar bar = ProgressBars.get(url);
		if (bar == null) {
			bar = new ProgressBar(this, null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setMax(loadInfo.getFileSize());
			bar.setProgress(loadInfo.getComplete());
			System.out.println(loadInfo.getFileSize()+"--"+loadInfo.getComplete());
			ProgressBars.put(url, bar);
			LinearLayout.LayoutParams params = new LayoutParams(
					LayoutParams.FILL_PARENT, 5);
			((LinearLayout) ((LinearLayout) v.getParent()).getParent())
					.addView(bar, params);
		}
	}

	/**
	 * 响应暂停下载按钮的点击事件
	 */
	public void pauseDownload(View v) {
		LinearLayout layout = (LinearLayout) v.getParent();
		String musicName = ((TextView) layout
				.findViewById(R.id.tv_resouce_name)).getText().toString();
		String urlstr = Constant.URL.BASE_URL + musicName;
		downloaders.get(urlstr).pause();
	}
}
