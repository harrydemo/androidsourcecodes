package com.hrw.android.player.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hrw.android.player.R;
import com.hrw.android.player.domain.Playlist;

public class PlaylistAdapter extends BaseAdapter {
	private Context context;
	private List<Playlist> play_list;

	public PlaylistAdapter(Context paramContext, List<Playlist> play_list) {
		this.context = paramContext;
		this.play_list = play_list;
	}

	@Override
	public int getCount() {

		return play_list.size();
	}

	@Override
	public Object getItem(int position) {
		return play_list.get(position).getName();
	}

	@Override
	public long getItemId(int position) {
		return play_list.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// A ViewHolder keeps references to children views to avoid unneccessary
		// calls
		// to findViewById() on each row.
		ViewHolder holder;

		// When convertView is not null, we can reuse it directly, there is no
		// need
		// to reinflate it. We only inflate a new View when the convertView
		// supplied
		// by ListView is null.
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.playlist_list_item, null);

			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			holder.text1 = (TextView) convertView.findViewById(R.id.line1);
			holder.text2 = (TextView) convertView.findViewById(R.id.line2);
			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}

		holder.text1.setText(getItem(position).toString());
		holder.text2.setText(play_list.get(position).getCountAudio() + "首歌曲");
		// Bind the data efficiently with the holder.
		return convertView;
	}

	static class ViewHolder {
		private TextView text1;
		private TextView text2;
	}
}
