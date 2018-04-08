package org.app.music;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

/**
 * 为了实现底部菜单，采用Tabhost+RadioGroup方式显示。配合Intent可以实现了外加RadioGroup监听器，具体参考博客园的例子,
 */
public class LocalTabMusicListActivity extends TabActivity implements
		OnCheckedChangeListener {
	private TabHost tabhost;
	private Intent localmusic;
	private Intent favorite;
	private Intent net;
	private RadioGroup maintab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost_list);
		maintab = (RadioGroup) findViewById(R.id.tab_group);
		maintab.setOnCheckedChangeListener(this);// 设置RadioGroup监听器
		tabhost = getTabHost();
		localmusic = new Intent(this, LocalMusicListActivity.class);
		tabhost.addTab(tabhost.newTabSpec("localmusic")
				.setIndicator("localmusic").setContent(localmusic));
		favorite = new Intent(this, LocalFavoriteActivity.class);
		tabhost.addTab(tabhost.newTabSpec("favorite").setIndicator("favorite")
				.setContent(favorite));
		net = new Intent(this, NetMusicActivity.class);
		tabhost.addTab(tabhost.newTabSpec("net").setIndicator("net")
				.setContent(net));
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.local:
			tabhost.setCurrentTabByTag("localmusic");
			break;
		case R.id.favorite:
			tabhost.setCurrentTabByTag("favorite");
			break;
		case R.id.online:
			tabhost.setCurrentTabByTag("net");
		}

	}

}
