package com.hrw.android.player.component.dialog;

import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hrw.android.player.R;
import com.hrw.android.player.builder.ContentValuesBuilder;
import com.hrw.android.player.dao.AudioDao;
import com.hrw.android.player.dao.PlaylistDao;
import com.hrw.android.player.dao.impl.AudioDaoImpl;
import com.hrw.android.player.dao.impl.PlaylistDaoImpl;
import com.hrw.android.player.domain.Audio;
import com.hrw.android.player.domain.Playlist;

public class PlaylistDialog {
	AlertDialog.Builder builder;
	Context context;
	private PlaylistDao playlistDao;
	private List<Playlist> playlist;
	private AudioDao audioDao;
	private String[] paths;

	private void initAudioDao() {
		audioDao = new AudioDaoImpl(this.context);
	}

	public PlaylistDialog(Context context, String[] paths) {
		this.context = context;
		builder = CommonAlertDialogBuilder.getInstance(context);
		this.paths = paths;
		playlistDao = new PlaylistDaoImpl(this.context);
		playlist = playlistDao.getAllPlaylist();
		setAdapter(playlist);
		initAudioDao();
	}

	public List<Playlist> getPlaylist() {
		return playlist;
	}

	private String getMediaName(String path) {
		String mediaName = path.substring(path.lastIndexOf("/") + 1, path
				.length());
		return mediaName;
	}

	private void setAdapter(List<Playlist> items) {
		builder.setAdapter(new PlaylistDialogAdapter(context, items),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						playlist.get(which);
						try {
							for (String path : paths) {
								Audio audio = new Audio();
								audio.setPlaylistId(playlist.get(which).getId()
										.toString());
								audio.setName(getMediaName(path));
								audio.setPath(path);
								audio.setAddDate(new Date());
								audio.setUpdateDate(new Date());
								ContentValues values = ContentValuesBuilder
										.getInstance().bulid(audio);
								audioDao.addMediaToPlaylist(values);
							}

						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
		builder.setTitle("添加到播放列表");
	}

	public void show() {
		AlertDialog alert = builder.create();
		alert.show();
	}

	public class PlaylistDialogAdapter extends BaseAdapter {
		private List<Playlist> playlist;

		public PlaylistDialogAdapter(Context context, List<Playlist> playlist) {
			this.playlist = playlist;
		}

		@Override
		public int getCount() {
			return playlist.size();
		}

		@Override
		public Object getItem(int position) {
			return playlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return playlist.get(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			TextView text;

			if (convertView == null) {
				view = LayoutInflater.from(context).inflate(
						R.layout.alert_dialog_item, parent, false);
			} else {
				view = convertView;
			}
			text = (TextView) view;
			text.setText(((Playlist) getItem(position)).getName());
			return view;
		}
	}
}
