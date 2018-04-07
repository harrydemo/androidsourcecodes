package com.hrw.android.player.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.hrw.android.player.R;
import com.hrw.android.player.adapter.MusicListAdapter;
import com.hrw.android.player.component.dialog.CommonAlertDialogBuilder;
import com.hrw.android.player.dao.AudioDao;
import com.hrw.android.player.dao.impl.AudioDaoImpl;
import com.hrw.android.player.db.constants.UriConstant;
import com.hrw.android.player.domain.Audio;
import com.hrw.android.player.service.SystemService;
import com.hrw.android.player.utils.Constants;
import com.hrw.android.player.utils.DirectoryUtil;
import com.hrw.android.player.utils.Constants.PopupMenu;

public class MusicListActivity extends BaseListActivity {
	private AudioDao audioDao = new AudioDaoImpl(this);
	private ImageButton back_btn;
	private ProgressDialog progress_dialog;
	private List<Audio> musicList;
	private List<Integer> checkedItem = new ArrayList<Integer>();
	private String[] choices;
	private MusicListAdapter adapter;
	Set<Integer> popUpMenu = new HashSet<Integer>();
	private Handler musicListHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {
				progress_dialog.dismiss();
				break;
			}
			default:
				break;
			}
		}
	};

	private Runnable mRunnable = new Runnable() {
		public void run() {
			for (int i = 0; i < checkedItem.size(); i++) {
				String[] s = DirectoryUtil
						.MediaScan(choices[checkedItem.get(i)]);
				// System.out.println(s.toString());
				for (int j = 0; j < s.length; j++) {
					// System.out.println(s[j]);
				}
			}
			musicListHandler.sendEmptyMessage(0);
		}
	};

	// private Thread mThread = new Thread() {
	//
	// public void run() {
	// for (int i = 0; i < checkedItem.size(); i++) {
	// String[] s = DirectoryUtil
	// .MediaScan(choices[checkedItem.get(i)]);
	// // System.out.println(s.toString());
	// for (int j = 0; j < s.length; j++) {
	// // System.out.println(s[j]);
	// }
	// }
	// mHandler.sendEmptyMessage(0); // 告诉handler
	// }
	// };

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		AlertDialog alertDialog = (AlertDialog) dialog;
		ListView lv = alertDialog.getListView();
		for (int i = 0; i < choices.length; i++) {
			lv.setItemChecked(i, false);
		}
		super.onPrepareDialog(id, dialog);
	}

	protected void showProcessDialog() {
		progress_dialog = ProgressDialog.show(this, null, "正在扫描");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final SystemService systemService = new SystemService(this);
		Set<String> folderList = systemService.getFolderContainMedia();
		choices = folderList.toArray(new String[folderList.size()]);
		// 选项数组
		// String[] choices = { "Facebook", "Twitter" };
		// Check判断数组，与选项对应
		// boolean[] chsBool = { true, false };
		AlertDialog dialog = CommonAlertDialogBuilder.getInstance(this)
				.setIcon(R.drawable.ic_menu_scan).setTitle("请选择扫描目录")
				.setMultiChoiceItems(choices, null,
						new OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								if (isChecked) {
									checkedItem.add(which);
								} else {
									checkedItem.remove((Object) which);
								}
							}

						}).setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// System.out.println(checkedItem.toString());
								for (int i = 0; i < checkedItem.size(); i++) {
									// System.out.println(choices[checkedItem
									// .get(i)]);
									addMediaToPlaylist(systemService
											.getMediasByFolder(choices[checkedItem
													.get(i)]));
									System.out
											.println(systemService
													.getMediasByFolder(
															choices[checkedItem
																	.get(i)])
													.toString());

								}
								showProcessDialog();
								mRunnable.run();
								checkedItem.clear();

							}

						}).setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								checkedItem.clear();
							}
						}).create();
		return dialog;
	}

	private String getMediaName(String path) {
		String mediaName = path.substring(path.lastIndexOf("/") + 1, path
				.length());
		return mediaName;
	}

	// 添加音乐到播放列表
	private final void addMediaToPlaylist(Set<String> medias) {
		String pId = this.getIntent().getExtras().get(
				"com.hrw.android.player.pid").toString();
		ContentValues values = new ContentValues();
		for (String path : medias) {
			if (getCountPlaylistMediaByName(getMediaName(path)) == 0) {
				Audio audio = new Audio();
				audio.setPlaylistId(pId);
				audio.setName(getMediaName(path));
				audio.setPath(path);
				audio.setAddDate(new Date());
				audio.setUpdateDate(new Date());
				values = bulid(audio);
				audioDao.addMediaToPlaylist(values);
			}

		}
		initListAdapter();
		Toast.makeText(this, "添加音乐成功", Toast.LENGTH_LONG).show();
	}

	private final int getCountPlaylistMediaByName(String name) {
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri
				.parse("content://" + UriConstant.AUTHORITY + "/audiolist");
		String[] proj = { "id" };
		// String
		// selection=MediaStore.Audio.Media.DATA+" like '/mnt/sdcard/Recording/%'";
		String selection = "playlist_id = ? AND audio_name = ?";
		String[] selectionArgs = {
				this.getIntent().getExtras().get("com.hrw.android.player.pid")
						.toString(), name };
		Cursor cursor = resolver.query(uri, proj, selection, selectionArgs,
				null);
		return cursor.getCount();

	}

	private void initButtons() {
		final TabActivity tabActivity = (TabActivity) getParent();
		final Intent toMenuListActivity = new Intent(this,
				MenuListActivity.class);
		LinearLayout addAudioBtn = (LinearLayout) findViewById(R.id.create_audio_list_header);
		addAudioBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(1);
			}
		});
		back_btn = (ImageButton) tabActivity.findViewById(R.id.list_back);
		back_btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					TabHost.TabSpec tab_spec_menu_list = tabActivity
							.getTabHost().newTabSpec(
									Constants.TAB_SPEC_TAG.MAIN_SPEC_TAG
											.getId()).setIndicator(
									Constants.TAB_SPEC_TAG.MAIN_SPEC_TAG
											.getId());
					tab_spec_menu_list.setContent(toMenuListActivity);
					tabActivity.getTabHost().addTab(tab_spec_menu_list);
					tabActivity.getTabHost().setCurrentTabByTag(
							Constants.TAB_SPEC_TAG.MAIN_SPEC_TAG.getId());
					Intent updateUiIntent = new Intent(
							Constants.UPDATE_UI_ACTION);
					sendBroadcast(updateUiIntent);
				}
				return false;
			}
		});
	}

	public void initListAdapter() {
		musicList = audioDao.getAudioListByPlaylistId(this.getIntent()
				.getExtras().get("com.hrw.android.player.pid").toString());

		adapter = new MusicListAdapter(this, musicList, this.getIntent()
				.getExtras().get("com.hrw.android.player.pid").toString());
		setListAdapter(adapter);
		TextView count_audio = (TextView) findViewById(R.id.count_audio);
		count_audio.setText("共" + String.valueOf(musicList.size()) + "首");
		// count_audio.setText(count_audio.getText().toString().replace("{0}",
		// String.valueOf(musicList.size())));
		// this.getListView().setOnItemLongClickListener(
		// new OnItemLongClickListener() {
		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent,
		// View view, int position, long id) {
		// showItemLongClickDialog(id);
		// return false;
		// }
		// });
		// this.getListView().setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Toast.makeText(
		// getApplicationContext(),
		// adapter.getCheckedBoxPositionIds().toString()
		// + "---clicked item name:"
		// + adapter.getItem(position)
		// + "--Path:"
		// + audioDao.getMusicPathByName(adapter.getItem(
		// position).toString()),
		// Toast.LENGTH_LONG).show();
		// play(audioDao.getMusicPathByName(adapter.getItem(position)
		// .toString()));
		//
		// }
		//
		// });
	}

	/**
	 * private void play(String path) { ImageButton play_btn = (ImageButton)
	 * findViewById(R.id.play_btn); try { if (mpu == null) { mpu =
	 * MediaPlayerUtil.getInstance(onCompletionListener); } if (mpu.isPlaying()
	 * && mpu.getPlayingMediaPath().equals(path)) {
	 * play_btn.setVisibility(ImageButton.VISIBLE);
	 * findViewById(R.id.position_icon).setPadding(30, 0, 0, 0);
	 * play_btn.setImageResource(R.drawable.list_pause_indicator); mpu.pause();
	 * } else if (mpu.isPause() && mpu.getPlayingMediaPath().equals(path)) {
	 * play_btn.setVisibility(ImageButton.VISIBLE);
	 * findViewById(R.id.position_icon).setPadding(30, 0, 0, 0);
	 * play_btn.setImageResource(R.drawable.list_playing_indicator);
	 * mpu.start(); } else { if (mpu.isPlaying() || mpu.isPause()) {
	 * mpu.reset(); } play_btn.setVisibility(ImageButton.VISIBLE);
	 * findViewById(R.id.position_icon).setPadding(30, 0, 0, 0);
	 * play_btn.setImageResource(R.drawable.list_playing_indicator);
	 * mpu.play(path); }
	 * 
	 * } catch (IllegalStateException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * }
	 **/

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
					// playlistDao.removePlaylist(String.valueOf(id));
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_list);
		initPopupMenu();
		initButtons();
	}

	@Override
	protected void onResume() {
		initListAdapter();
		super.onResume();
	}

	private List<String> getMusicListByPId(String id) {
		List<String> musicList = new ArrayList<String>();
		ContentResolver cr = getContentResolver();
		Uri uri = Uri
				.parse("content://" + UriConstant.AUTHORITY + "/audiolist");
		String[] projection = { "audio_path" };
		String selection = "playlist_id = ?";
		String[] selectionArgs = { id };
		Cursor c = cr.query(uri, projection, selection, selectionArgs, null);
		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);
				musicList.add(c
						.getString(c.getColumnIndexOrThrow("audio_path")));
			}
		}
		c.close();
		return musicList;

	}

	@Override
	protected Set<Integer> getPopUpMenu() {
		if (adapter.getCheckedBoxPositionIds().size() > 0) {
			popUpMenu.add(PopupMenu.ADD_TO.getMenu());
		} else {
			popUpMenu.remove(PopupMenu.ADD_TO.getMenu());
		}
		return popUpMenu;
	}

	private void initPopupMenu() {
		popUpMenu.add(PopupMenu.ADD_TO.getMenu());
		popUpMenu.add(PopupMenu.CREATE_LIST.getMenu());
		popUpMenu.add(PopupMenu.EXIT.getMenu());
		popUpMenu.add(PopupMenu.HELP.getMenu());
		popUpMenu.add(PopupMenu.SETTING.getMenu());
	}

}
