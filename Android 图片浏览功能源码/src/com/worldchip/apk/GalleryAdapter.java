package com.worldchip.apk;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class GalleryAdapter extends BaseAdapter
{
	  private static final String TAG = "GalleryAdapter";
	  private LayoutInflater mInflater;	  
	  private Context mContext;
	  private String[] paths;
	  private int width, height;
	  public GalleryAdapter(Context context, String[] p)
	  {
		  mContext=context;
	      mInflater = LayoutInflater.from(context);
	      paths=p;
	  }
	  
	  public GalleryAdapter(Context context, String[] p, int w, int h)
	  {
		  mContext=context;
	      mInflater = LayoutInflater.from(context);
	      paths=p;
	      width=w;
	      height=h;
	  }
	  
	  public int getCount()
	  {
	    //return paths.length;
	    return Integer.MAX_VALUE;
	  }

	  public Object getItem(int position)
	  {
	   // return paths[position];
		  return paths[position% paths.length];
	  }
	  
	  public long getItemId(int position)
	  {
	    //return position;
		  return position% paths.length;
	  }
	 
	  
	  public View getView(int position,View convertView,ViewGroup parent)
	  {
		  //System.gc();
		 // currentBitmap = null;
	     // if(currentBitmap!=null&&!currentBitmap.isRecycled()){
	    //		 currentBitmap.recycle();  
	    //		 currentBitmap = null;
	     // }
		  ImageView imageView=new ImageView(mContext);
	      String path=paths[position% paths.length];
	      Log.i(TAG, "path="+path+"; width="+width+"; height="+height);
	      
	      try
	      {
	         Bitmap image=ImageCommon.getGalleryPicture(path);
	         imageView.setImageBitmap(image);
	        // currentView.setScaleType(ScaleType.FIT_CENTER);
	         imageView.setLayoutParams(new Gallery.LayoutParams(width,height));
	      }
	      catch(OutOfMemoryError err)
	      {
	    	  err.printStackTrace();
	    	  Log.i(TAG, "OutOfMemoryError !!!!!!!!!!!!");
	    	  return null;
	      }
	      return imageView;
	  }
}

