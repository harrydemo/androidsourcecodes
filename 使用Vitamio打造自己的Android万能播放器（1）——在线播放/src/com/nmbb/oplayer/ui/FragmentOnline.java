package com.nmbb.oplayer.ui;

import java.util.ArrayList;

import com.nmbb.oplayer.R;
import com.nmbb.oplayer.ui.base.ArrayAdapter;
import com.nmbb.oplayer.util.FileUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentOnline extends FragmentBase implements OnItemClickListener {

	/** 缓存视频列表 */
	private static ArrayList<String[]> mOnlineList = new ArrayList<String[]>();
	/** 缓存视频LOGO列表 */
	private static ArrayList<Integer> mOnlineLogoList = new ArrayList<Integer>();
	private WebView mWebView;
	private ListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_online, container,
				false);
		mListView = (ListView) mView.findViewById(android.R.id.list);
		mWebView = (WebView) mView.findViewById(R.id.webview);
		mListView.setOnItemClickListener(this);
		initWebView();
		mListView.setAdapter(new DataAdapter(getActivity()));
		return mView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final String[] f = mOnlineList.get(position);
		mWebView.clearView();
		mWebView.loadUrl(f[1]);
		mWebView.clearHistory();
		mListView.setVisibility(View.GONE);
		mWebView.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onBackPressed() {
		if (mListView == null || mListView.getVisibility() == View.VISIBLE)
			return super.onBackPressed();
		else {
			mListView.setVisibility(View.VISIBLE);
			mWebView.setVisibility(View.GONE);
			return true;
		}
	}

	/** 初始化WebView */
	private void initWebView() {
		mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginsEnabled(true);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
			};

			/** 页面跳转 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (FileUtils.isVideoOrAudio(url)) {
					Intent intent = new Intent(getActivity(),
							VideoPlayerActivity.class);
					intent.putExtra("path", url);
					startActivity(intent);
					return true;
				}
				return false;
			};
		});

		mWebView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView != null
						&& mWebView.canGoBack()) {
					mWebView.goBack();
					return true;
				}
				return false;
			}
		});
	}

	private class DataAdapter extends ArrayAdapter<String[]> {

		public DataAdapter(Context ctx) {
			super(ctx, mOnlineList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final String[] f = getItem(position);
			if (convertView == null) {
				final LayoutInflater mInflater = getActivity()
						.getLayoutInflater();
				convertView = mInflater.inflate(R.layout.fragment_online_item,
						null);
			}

			((ImageView) convertView.findViewById(R.id.thumbnail))
					.setImageResource(mOnlineLogoList.get(position));
			((TextView) convertView.findViewById(R.id.title)).setText(f[0]);

			return convertView;
		}

	}

	static {
		// 120 60
		mOnlineList.add(new String[] { "优酷视频", "http://3g.youku.com" });
		mOnlineLogoList.add(R.drawable.logo_youku);
		// 104 43
		mOnlineList.add(new String[] { "搜狐视频", "http://m.tv.sohu.com" });
		mOnlineLogoList.add(R.drawable.logo_sohu);
		//
		mOnlineList.add(new String[] { "乐视TV", "http://m.letv.com" });
		mOnlineLogoList.add(R.drawable.logo_letv);
		// 174 48
		mOnlineList.add(new String[] { "爱奇异", "http://3g.iqiyi.com/" });
		mOnlineLogoList.add(R.drawable.logo_iqiyi);
		mOnlineList.add(new String[] { "PPTV", "http://m.pptv.com/" });
		mOnlineLogoList.add(R.drawable.logo_pptv);
		// 181 60
		mOnlineList.add(new String[] { "腾讯视频", "http://3g.v.qq.com/" });
		mOnlineLogoList.add(R.drawable.logo_qq);
		mOnlineList.add(new String[] { "56.com", "http://m.56.com/" });
		mOnlineLogoList.add(R.drawable.logo_56);
		mOnlineList.add(new String[] { "新浪视频", "http://video.sina.cn/" });
		mOnlineLogoList.add(R.drawable.logo_sina);
		mOnlineList.add(new String[] { "土豆视频", "http://m.tudou.com" });
		mOnlineLogoList.add(R.drawable.logo_tudou);
	}

}

/*
 * private boolean loadVideo(final String url) { if (StringUtils.isEmpty(url))
 * return false;
 * 
 * mCurrentUrl = url;
 * 
 * new AsyncTask<Void, Void, OnlineVideo>() {
 * 
 * @Override protected OnlineVideo doInBackground(Void... params) {
 * Log.d("Youku", url); if (url.startsWith("http://m.youku.com")) { return
 * VideoHelper.getYoukuVideo(url); } return null; }
 * 
 * @Override protected void onPostExecute(OnlineVideo result) {
 * super.onPostExecute(result); if (result != null) { Intent intent = new
 * Intent(getActivity(), VideoPlayerActivity.class); intent.putExtra("path",
 * result.url); intent.putExtra("title", result.title); startActivity(intent); }
 * else { mWebView.loadUrl(url); } } }.execute(); return true; }
 */
