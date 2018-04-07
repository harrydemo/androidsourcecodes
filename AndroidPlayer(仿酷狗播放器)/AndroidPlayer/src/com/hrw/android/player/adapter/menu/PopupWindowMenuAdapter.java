package com.hrw.android.player.adapter.menu;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrw.android.player.R;

public class PopupWindowMenuAdapter extends BaseAdapter {
	List<MenuItem> menuList;
	private Context context;
	private Menu menu;

	public PopupWindowMenuAdapter(Context context, List<MenuItem> menuList) {
		this.context = context;
		this.menuList = menuList;
	}

	@Override
	public int getCount() {
		return menuList.size();
	}

	@Override
	public Object getItem(int position) {
		return menuList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return menuList.get(position).getOrder();
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
					R.layout.pop_menu_item, null);

			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.menu_item_icon);
			holder.textView = (TextView) convertView
					.findViewById(R.id.menu_item_text);
			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imageView
				.setBackgroundDrawable(menuList.get(position).getIcon());
		holder.textView.setText(menuList.get(position).getTitle());
		// Bind the data efficiently with the holder.
		return convertView;
	}

	static class ViewHolder {
		private ImageView imageView;
		private TextView textView;
	}
}
