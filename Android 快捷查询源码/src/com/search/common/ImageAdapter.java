package com.search.common;

import java.util.List;
import java.util.Map;

import com.search.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 自定义一个可以显示含有图片的列表信息的适配器
 * @author Administrator
 *
 */
public class ImageAdapter extends BaseAdapter {
	
	private List<Map<String, Object>> datas;
	
	private Context context;
	
	public ImageAdapter(Context context, List<Map<String, Object>> datas){
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {

		return this.datas.size();
	}

	@Override
	public Object getItem(int position) {

		return this.datas.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
			holder.itemLeftImage = (ImageView)convertView.findViewById(R.id.item_left);
			holder.itemText = (TextView)convertView.findViewById(R.id.item_text);
			holder.itemRightImage = (ImageView)convertView.findViewById(R.id.item_right);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Map<String, Object> item = this.datas.get(position);
		
		holder.itemLeftImage.setImageResource(Integer.parseInt(item.get("left").toString()));
		holder.itemText.setText(item.get("text").toString());
		holder.itemRightImage.setImageResource(Integer.parseInt(item.get("right").toString()));
		
		return convertView;
	}
	
	static class ViewHolder{
		ImageView itemLeftImage;
		TextView itemText;
		ImageView itemRightImage;
	}

}
