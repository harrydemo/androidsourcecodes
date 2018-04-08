package com.worldchip.apk;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter
{
	  private LayoutInflater mInflater;
	  private Bitmap internal;
	  private Bitmap sdcard;
	  private Bitmap usbhost;
	  private Bitmap update;
	  
	  private List<String> items;
	  private List<String> paths;

	  public ListAdapter(Context context,List<String> it,List<String> pa)
	  {
	    mInflater = LayoutInflater.from(context);
	    items = it;
	    paths = pa;
	    internal = BitmapFactory.decodeResource(context.getResources(),
	            R.drawable.start);
	    sdcard = BitmapFactory.decodeResource(context.getResources(),
	            R.drawable.delete);
	    usbhost = BitmapFactory.decodeResource(context.getResources(),
	            R.drawable.details);
	    update = BitmapFactory.decodeResource(context.getResources(),
	            R.drawable.serach1);
	  }
	  
	 
	  public int getCount()
	  {
	    return items.size();
	  }

	  
	  public Object getItem(int position)
	  {
	    return items.get(position);
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
	    	Log.i("---", "here");
	      convertView = mInflater.inflate(R.layout.main_row_bk, null);
	      holder = new ViewHolder();
	      holder.text = (TextView) convertView.findViewById(R.id.text2);
	      holder.icon = (ImageView) convertView.findViewById(R.id.icon2);
	      
	      convertView.setTag(holder);
	    }
	    else
	    {
	        holder = (ViewHolder) convertView.getTag();
	    }

	    String type=items.get(position).toString();
	    if(type.equals("internal"))
	    {
	        holder.text.setText(R.string.internal);
	        holder.icon.setImageBitmap(internal);
	    }
	    else if(type.equals("sdcard"))
	    {
	    	 holder.text.setText("SD¿¨´æ´¢");
	         holder.icon.setImageBitmap(sdcard);
	    }
	    else if(type.equals("usbhost"))
	    {
	    	holder.text.setText("USBHOST");
	         holder.icon.setImageBitmap(usbhost);
	    }
	    else
	    {
	    	holder.text.setText(R.string.rescan);
	         holder.icon.setImageBitmap(update);
	    }
	    return convertView;
	  }
	  
	  /* class ViewHolder */
	  private class ViewHolder
	  {
	    TextView text;
	    ImageView icon;
	  }
	}
