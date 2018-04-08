package com.hrw.android.player.activity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.hrw.android.player.R;
import com.hrw.android.player.adapter.PlaylistAdapter;
import com.hrw.android.player.component.dialog.CreatePlaylistDialog;
import com.hrw.android.player.dao.PlaylistDao;
import com.hrw.android.player.dao.impl.PlaylistDaoImpl;
import com.hrw.android.player.db.constants.UriConstant;
import com.hrw.android.player.domain.Playlist;
import com.hrw.android.player.utils.Constants;
import com.hrw.android.player.utils.Constants.PopupMenu;

public class PlaylistActivity extends BaseListActivity {

	private PlaylistDao playlistDao = new PlaylistDaoImpl(this);
	private ImageButton back_btn;
	private TabActivity tabActivity;
	private LinearLayout createNewPlaylistBtn;
	Set<Integer> popUpMenu = new HashSet<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.playlist_list);
		tabActivity = (TabActivity) getParent();
		initPopupMenu();
		initBackButton();
		initCreateNewPlaylistButton();
		initListAdapter();
		super.onCreate(savedInstanceState);
	}

	private void initCreateNewPlaylistButton() {
		createNewPlaylistBtn = (LinearLayout) findViewById(R.id.create_playlist_header);
		createNewPlaylistBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog dialog = CreatePlaylistDialog
						.getCreatePlaylistDialog(PlaylistActivity.this)
						.create();
				dialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						initListAdapter();
					}
				});
				dialog.show();
			}
		});
	}

	private void initListAdapter() {
		List<Playlist> playlist = getAllPlaylist();
		setListAdapter(new PlaylistAdapter(this, playlist));
		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						showItemLongClickDialog(id);
						return false;
					}
				});
	}

	private void showItemLongClickDialog(final long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final CharSequence[] items = { "重命名", "删除" };
		// TODO setMessage is something different with kugou's
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				// TODO 0,1 to constant
				case 0:
					break;
				case 1:
					playlistDao.removePlaylist(String.valueOf(id));
					initListAdapter();
					break;
				default:
					break;
				}

			}
		}).setTitle("id:" + id);
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void initBackButton() {
		final Intent toMenuListActivity = new Intent(this,
				MenuListActivity.class);
		back_btn = (ImageButton) tabActivity.findViewById(R.id.list_back);
		back_btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					TabHost.TabSpec tab_spec_menu_list = tabActivity
							.getTabHost().newTabSpec("menu_list").setIndicator(
									"menu_list");
					tab_spec_menu_list.setContent(toMenuListActivity);
					tabActivity.getTabHost().addTab(tab_spec_menu_list);
					tabActivity.getTabHost().setCurrentTabByTag("menu_list");
					Intent updateUiIntent = new Intent(
							Constants.UPDATE_UI_ACTION);
					sendBroadcast(updateUiIntent);
				}
				return false;
			}
		});

	}

	protected List<Playlist> getAllPlaylist() {
		List<Playlist> allPlaylist = playlistDao.getAllPlaylist();
		for (Playlist playlst : allPlaylist) {
			playlst.setCountAudio(countAudio(playlst.getId().toString()));
		}
		return allPlaylist;
	}

	private Integer countAudio(String pId) {
		ContentResolver cr = getContentResolver();
		Uri uri = Uri
				.parse("content://" + UriConstant.AUTHORITY + "/audiolist");
		String[] projection = { "id" };
		String selection = "playlist_id = ?";
		String[] selectionArgs = { pId };
		Cursor c = cr.query(uri, projection, selection, selectionArgs, null);
		c.close();
		return c.getCount();

	}

	@Override
	protected void onResume() {
		super.onResume();
		back_btn.setVisibility(ImageButton.VISIBLE);
		TextView top_title_tv = (TextView) tabActivity
				.findViewById(R.id.top_title);
		top_title_tv.setText(R.string.menu_playlist);
		// back_btn.setVisibility(ImageButton.VISIBLE);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent toMusicListActivity = new Intent(this, MusicListActivity.class);
		toMusicListActivity.putExtra("com.hrw.android.player.pid", id);
		TabHost.TabSpec tab_spec_menu_list = tabActivity.getTabHost()
				.newTabSpec("music_list" + id).setIndicator("music_list" + id);
		tab_spec_menu_list.setContent(toMusicListActivity);
		tabActivity.getTabHost().addTab(tab_spec_menu_list);
		tabActivity.getTabHost().setCurrentTabByTag("music_list" + id);

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
