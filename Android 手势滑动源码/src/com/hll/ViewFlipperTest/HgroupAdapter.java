package com.hll.ViewFlipperTest;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class HgroupAdapter extends BaseAdapter {
	 private LayoutInflater mInflater;
	 int state;
	 Context mContext;
	 String mState;
	 List<String> mList;
	 int sel = 0;
    public HgroupAdapter(Context context,List<String> list,int menuState) {
   	    this.mList=list;
   	    this.mContext=context;
   	    this.state = menuState;
        mInflater = LayoutInflater.from(context);
    }
	public int getCount() {
//   	 if(mList == null){
//   		 return 0;
//   	 }
//        return mList.size();
		return 10;
    }
    public Object getItem(int position) {
        return mList.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grouplist, null);
            holder = new ViewHolder();
            
//            if(sel>5){
//            	convertView.setBackgroundResource(R.drawable.handler_icon);
//            }else{
//            	convertView.setBackgroundResource(R.drawable.title_bg);
//            }
            
            
//            holder.group_name = (TextView) convertView.findViewById(R.id.group_name);
//            holder.menustate =  (ImageView) convertView.findViewById(R.id.menustate);
//            switch(state){
//			case 1:
//				holder.menustate.setImageResource(R.drawable.yes_state);
////				notifyDataSetChanged();
//				break;
//			case 2:
//				holder.menustate.setImageResource(R.drawable.yes_state);
////				notifyDataSetChanged();
//				break;
//			case 3:
//				holder.menustate.setImageResource(R.drawable.yes_state);
////				notifyDataSetChanged();
//				break;
//			default:
//				break;
//			}
//            holder.menustate.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					
//				}
//            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    static class ViewHolder {
   	 	TextView group_name;
   	 	TextView time;
   	 	TextView info;
    }
}
