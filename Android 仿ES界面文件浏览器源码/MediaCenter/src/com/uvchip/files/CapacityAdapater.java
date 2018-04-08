package com.uvchip.files;

import java.util.List;
import java.util.Set;

import com.uvchip.mediacenter.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CapacityAdapater extends BaseAdapter {

	private List<CapacityItem> mDiscs;
	private LayoutInflater mInfalter;
	private ViewHolder holder;
	private Context mContext;
	
	public CapacityAdapater(List<CapacityItem> discs,Context context){
		mDiscs = discs;
		mContext = context;
		mInfalter = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		return mDiscs.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mDiscs.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInfalter.inflate(R.layout.capacity_item, null);
			holder.tv_disc_name = (TextView)convertView.findViewById(R.id.disc_name);
			holder.tv_disc_size = (TextView)convertView.findViewById(R.id.disc_size);
			holder.tv_disc_capacity = (TextView)convertView.findViewById(R.id.disc_capacity);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		String path = mDiscs.get(position).mPath;
		if(path.equals(FileManager.SD)){
		    if (FileManager.IS_SIMSDCARD){
		        holder.tv_disc_name.setText(mContext.getString(R.string.innerDisk));
		    }else {
	            holder.tv_disc_name.setText(mContext.getString(R.string.sdcard));
            }
    	}else if(path.equals(FileManager.MEMORY)){
    		holder.tv_disc_name.setText(mContext.getText(R.string.innerDisk)+"");
    	}else if(path.matches(FileManager.USB+"\\d+(-\\d+)?/")){
    	    Set<String> keys = RefreshData.usbIndex.keySet();
    	    String tmp = mContext.getText(R.string.usbDisk)+path.substring(12,path.length()-1);
    	    for (String key : keys) {
                String value = RefreshData.usbIndex.get(key);
                if (path.contains(key)) {
                    tmp = value;
                }
            }
    		holder.tv_disc_name.setText(tmp);
    	}else if (path.equals(FileManager.EXTSD)) {
    	    holder.tv_disc_name.setText(mContext.getText(R.string.ext_sd)+"");
        }
		holder.tv_disc_size.setText(mDiscs.get(position).mTotalSize);
		holder.tv_disc_capacity.setText(mDiscs.get(position).mCapacitySize);
		return convertView;
	}
	
	private final class ViewHolder{
		TextView tv_disc_name;
		TextView tv_disc_size;
		TextView tv_disc_capacity;
	}

}
