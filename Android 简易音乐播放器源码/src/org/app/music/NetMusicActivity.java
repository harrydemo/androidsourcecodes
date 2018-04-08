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
 * ���������ⲿ�֣��ҿ��˺ܶ����ӣ����ֺܶ�������վ�����ṩAPI������ʲô���ѣ��ٶȸ����
 * ���Ǿ��ò���ôʵ�ã����Բ���mars���Ǹ����ӡ��Լ��Tomcat��Ȼ���������ʾ��д���˱����Ǽ�Internet��SD��Ȩ��
 */
public class NetMusicActivity extends Activity {
	ListView listview;
	private static final int UPDATE = Menu.FIRST;
	private List<Mp3Info> mp3infos = null;
	// �̶�������ص����ֵ�·����SD��Ŀ¼��
		private static final String SD_PATH = "/sdcard/music/";
		// ��Ÿ���������
		private Map<String, DownLoad> downloaders = new HashMap<String, DownLoad>();
		// �������������Ӧ�Ľ�����
		private Map<String, ProgressBar> ProgressBars = new HashMap<String, ProgressBar>();
		/**
		 * ��handler���½�����
		 */
		private Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					String url = (String) msg.obj;
					int length = msg.arg1;
					ProgressBar bar = ProgressBars.get(url);
					if (bar != null) {
						// ���ý���������ȡ��length���ȸ���
						bar.incrementProgressBy(length);
						if (bar.getProgress() == bar.getMax()) {
							Toast.makeText(NetMusicActivity.this, "������ɣ�", 0).show();
							// ������ɺ��������������map�е��������
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
		menu.add(0, UPDATE, 0, "�����б�");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == UPDATE) {
			UpdateMusic();// �����µ����������
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * �������������б�
	 */
	private void UpdateMusic() {
		String xml = UpdateListView("http://192.168.2.2:8080/mp3/netmp3list.xml");// ����xml
		mp3infos = parse(xml);
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		// ��mp3info����Ϣ�õ���
		for (Iterator<Mp3Info> iterator = mp3infos.iterator(); iterator
				.hasNext();) {
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			// ��Map����װ������
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("mp3name", mp3Info.getMp3Name());
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.download_info, new String[] { "mp3name"}, new int[] {R.id.tv_resouce_name});
		listview.setAdapter(adapter);
	}

	

	

	/**
	 * �����б�
	 */
	private String UpdateListView(String urlStr) {
		String result = HttpDownload.downloadStringFromInternetFile(urlStr);

		return result;

	}

	/**
	 * ����Զ�̷�������XML
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
	 * ��Ӧ��ʼ���ذ�ť�ĵ���¼� 
	 */
	public void startDownload(View v) {
		// �õ�textView������
		LinearLayout layout = (LinearLayout) v.getParent();
		String musicName = ((TextView) layout
				.findViewById(R.id.tv_resouce_name)).getText().toString();
		String urlstr = Constant.URL.BASE_URL + musicName;
		String localfile = SD_PATH + musicName;
		// ���������߳���Ϊ4����������Ϊ�˷������̶���
		int threadcount = 4;
		// ��ʼ��һ��downloader������
		DownLoad downloader = downloaders.get(urlstr);
		if (downloader == null) {
			downloader = new DownLoad(urlstr, localfile, threadcount, this,
					mHandler);
			downloaders.put(urlstr, downloader);
		}
		if (downloader.isDownLoad())
			return;
		// �õ�������Ϣ��ĸ�����ɼ���
		LoadInfo loadInfo = downloader.getDownLoadInfos();
		// ��ʾ������
		showProgress(loadInfo, urlstr, v);
		// ���÷�����ʼ����
		downloader.download();
	}

	/**
	 * ��ʾ������
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
	 * ��Ӧ��ͣ���ذ�ť�ĵ���¼�
	 */
	public void pauseDownload(View v) {
		LinearLayout layout = (LinearLayout) v.getParent();
		String musicName = ((TextView) layout
				.findViewById(R.id.tv_resouce_name)).getText().toString();
		String urlstr = Constant.URL.BASE_URL + musicName;
		downloaders.get(urlstr).pause();
	}
}
