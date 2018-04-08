package com.hrw.android.player.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrw.android.player.R;

public class MenuListAdapter extends BaseAdapter {
	private Context context;
	private List<String> menu_list;

	public MenuListAdapter(Context paramContext, List<String> menu_list) {
		this.context = paramContext;
		this.menu_list = menu_list;
	}

	@Override
	public int getCount() {

		return menu_list.size();
	}

	@Override
	public Object getItem(int position) {
		return menu_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
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
					R.layout.common_list_item2, null);

			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.common_text);
			holder.left_icon = (ImageView) convertView
					.findViewById(R.id.common_icon);
			holder.right_icon = (ImageView) convertView
					.findViewById(R.id.common_button);
			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}
		if ("L".equals(getItem(position).toString())) {
			holder.text.setText(context.getString(R.string.menu_local_music));
			holder.left_icon.setBackgroundResource(R.drawable.ic_media);
		} else if ("P".equals(getItem(position).toString())) {
			holder.text.setText(context.getString(R.string.menu_playlist));
			holder.left_icon.setBackgroundResource(R.drawable.ic_playlist);
		}

		// Bind the data efficiently with the holder.
		return convertView;
	}

	static class ViewHolder {
		private TextView text;
		private ImageView left_icon;
		private ImageView right_icon;
	}
}
