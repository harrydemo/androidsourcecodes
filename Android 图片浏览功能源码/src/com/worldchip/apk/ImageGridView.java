package com.worldchip.apk;

import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageGridView extends Activity{

	 private static final String TAG = "ImageGridView";
	 LinkedList<ImageInfo> bitImages=null;
	 String[] albums=null;
	 int flag=2;
	 @Override  
	 public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	    setContentView(R.layout.grid_view);  
	    GridView gView = (GridView) this.findViewById(R.id.gridview);  
	    bitImages=new  LinkedList<ImageInfo>();   
	    Intent intent = getIntent();  
        final String path = intent.getStringExtra("path"); 
        String name = intent.getStringExtra("name");  
        flag=intent.getIntExtra("flag",1);
        albums=intent.getStringArrayExtra("data");
        Log.i("ImageGridView_onCreate", "flag="+flag+";path="+path+"; name="+name);
        
        setTitle((String) getText(R.string.album)+":"+name+"     "
        		+(String) getText(+R.string.path)+":"+path);

        gView.setAdapter(new GridViewAdapter(this,flag, name, getNames(flag, albums)));
        
	    gView.setOnItemClickListener(new OnItemClickListener(){  
	           public void onItemClick(AdapterView<?> arg0, View arg1, int position,long id) 
	           {  
	                 //Log.i("GridView.setOnItemClickListener", "position="+position);
	                 //String path=images.get(position).path;
	        		 //Log.i("ImageListView_onListItemClick", "the path="+path);

	        		 ArrayList<String> pathArray=new ArrayList<String>();
	        		 for(int i=0; i<albums.length; i++)
	        		 {
	        			 if(flag==2)
	        			 {
	        				 pathArray.add(albums[i]);
	        			 }
	        			 else
	        			 {
	        			     String absolutePath=albums[i].split("&")[1];
	        			     Log.i(TAG, "absolutePath="+absolutePath);
	        			     pathArray.add(absolutePath);
	        			 }
	        		 }
	        		 
	        		 Intent intent = new Intent();  
	        		 intent.setClass(ImageGridView.this, ImageGalleryView.class);  
	        		 intent.putExtra("path", path);
	        		 intent.putExtra("id", position);
	        		 intent.putExtra("data", (String[])pathArray.toArray(new String[pathArray.size()]));
	        		 Log.i("ImageGridView_setOnItemClickListener", "position="+position+"; path="+path);
	        		 ImageGridView.this.startActivity(intent);  
	           }    
	     });  
	 }

	private int[] getRowIds(String[] albums)
	{
		int[] ids=new int[albums.length];
		for(int i=0; i<albums.length; i++)
		{
			
			String id=albums[i].split("&")[0];
			ids[i]=Integer.valueOf(id);
		}
		return ids;
	}
	
	private String[] getNames(int flag, String[] albums)
	{
		if(flag==0)
		{
			Log.i(TAG, "----code comes to here----");
		   String[] paths=new String[albums.length];
		   String path=null;
		   String name=null;
		   for(int i=0; i<albums.length; i++)
		   {
			 path=albums[i].split("&")[1];
			 name=path.substring(path.lastIndexOf("/")+1);
			 Log.i(TAG, "path="+path+"; name="+name);
			 paths[i]=name;
		   }
		   return paths;
		}
		else if(flag==1)
		{
			String[] ids=new String[albums.length];
			for(int i=0; i<albums.length; i++)
			{
				String id=albums[i].split("&")[0];
				ids[i]=id;
			}
			return ids;
		}
		else 
			return albums;
	}
}
