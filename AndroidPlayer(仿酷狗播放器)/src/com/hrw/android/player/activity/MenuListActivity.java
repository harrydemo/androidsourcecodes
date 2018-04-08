package com.hrw.android.player.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;

import com.hrw.android.player.R;
import com.hrw.android.player.adapter.MenuListAdapter;
import com.hrw.android.player.component.menu.CommonPopupWindowMenu;
import com.hrw.android.player.utils.Constants;
import com.hrw.android.player.utils.Constants.PopupMenu;

public class MenuListActivity extends BaseListActivity {
	private List<String> menu_list;
	private Intent[] arrayIntent = new Intent[4];
	CommonPopupWindowMenu popWindow;
	LinearLayout pop_menu_layout;
	ImageButton tab_menu;
	View tabButtonSelectd;
	Set<Integer> popUpMenu = new HashSet<Integer>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.common_listview);
		initPopupMenu();
		initIntent();
		initListAdapter();
		super.onCreate(savedInstanceState);
	}

	private void initIntent() {
		Intent toLocalMusicListActivity = new Intent(this,
				LocalMusicListActivity.class);
		Intent toPlaylistActivity = new Intent(this, PlaylistActivity.class);
		arrayIntent[0] = toLocalMusicListActivity;
		arrayIntent[1] = toPlaylistActivity;
	}

	private void initListAdapter() {
		menu_list = new ArrayList<String>();
		menu_list.add("L");
		menu_list.add("P");
		setListAdapter(new MenuListAdapter(this, menu_list));
	}

	private void toNextActivity(int paramInt, String tag, String label) {
		Intent nextActivity = this.arrayIntent[paramInt];
		TabActivity tabActivity = (TabActivity) getParent();
		TabHost.TabSpec tab_spec_music_list = tabActivity.getTabHost()
				.newTabSpec(tag).setIndicator(label);
		tab_spec_music_list.setContent(nextActivity);
		tabActivity.getTabHost().addTab(tab_spec_music_list);
		tabActivity.getTabHost().setCurrentTabByTag(tag);
	}

	@Override
	protected void onListItemClick(ListView paramListView, View paramView,
			int paramInt, long paramLong) {
		super.onListItemClick(paramListView, paramView, paramInt, paramLong);
		switch (paramInt) {
		case 0: {
			toNextActivity(paramInt, Constants.TAB_SPEC_TAG.AUDIO_LIST_SPEC_TAG
					.getId(), Constants.TAB_SPEC_TAG.AUDIO_LIST_SPEC_TAG
					.getId());
			break;
		}
		case 1: {
			toNextActivity(paramInt, Constants.TAB_SPEC_TAG.PLAYLIST_SPEC_TAG
					.getId(), Constants.TAB_SPEC_TAG.PLAYLIST_SPEC_TAG.getId());
			break;
		}
		default: {
			break;
		}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected Set<Integer> getPopUpMenu() {
		return popUpMenu;
	}
	private void initPopupMenu() {
		popUpMenu.add(PopupMenu.CREATE_LIST.getMenu());
		popUpMenu.add(PopupMenu.EXIT.getMenu());
		popUpMenu.add(PopupMenu.HELP.getMenu());
		popUpMenu.add(PopupMenu.SETTING.getMenu());
	}

}
