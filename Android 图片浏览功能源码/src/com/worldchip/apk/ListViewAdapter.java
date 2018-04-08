package com.worldchip.apk;

import java.io.File;
import java.util.LinkedList;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter
{
	  private LayoutInflater mInflater;	  
	  private LinkedList<ImageInfo> imageInfo;
	  private Context mContext;
	  public ListViewAdapter(Context context, LinkedList<ImageInfo> imageInfos)
	  {
	    mInflater = LayoutInflater.from(context);
	    imageInfo = imageInfos;
	    mContext=context;
	  }
	  

	  public int getCount()
	  {
	    return imageInfo.size();
	  }

	
	  public Object getItem(int position)
	  {
	    return imageInfo.get(position);
	  }
	  
	
	  public long getItemId(int position)
	  {
	    return position;
	  }
	  
	
	  public View getView(int position,View convertView,ViewGroup parent)
	  {
	    ViewHolder holder;
	    
	    if(convertView == null)
	    {
	      convertView = mInflater.inflate(R.layout.list_row, null);
	     // convertView.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
	      holder = new ViewHolder();
	      
	      holder.icon = (ImageView) convertView.findViewById(R.id.icon);
	      
	      holder.name = (TextView) convertView.findViewById(R.id.name);
	      holder.path = (TextView) convertView.findViewById(R.id.path);
	   //   holder.date = (TextView) convertView.findViewById(R.id.date);
	      holder.picturecount = (TextView) convertView.findViewById(R.id.picturecount);
	      
	      convertView.setTag(holder);
	    }
	    else
	    {
	      holder = (ViewHolder) convertView.getTag();
	    }

	    if(imageInfo==null)
	    {
	    	Log.i("ListViewAdapter", "imageInfo is null!");
	    	return convertView;
	    }
	    if(imageInfo.get(position)==null)
	    {
	    	Log.i("ListViewAdapter", "imageInfo.get(position) is null!");
	    	return convertView;
	    }
	    File f=new File(imageInfo.get(position).path);
    	String fName=f.getName();
    	holder.icon.setImageBitmap(imageInfo.get(position).icon);
    	
    	holder.name.setText(fName);
    	holder.path.setText(imageInfo.get(position).path);
    	holder.picturecount.setText(imageInfo.get(position).picturecount+" "
    			+(String)mContext.getText(R.string.picture_count));
	    return convertView;
	  }
	  
	  /* class ViewHolder */
	  private class ViewHolder
	  {
	    TextView name;
	    TextView path;
	  //  TextView date;
	    TextView picturecount;
	    ImageView icon;
	  }
}
