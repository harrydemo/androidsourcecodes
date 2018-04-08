package com.worldchip.apk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
public class ImageListView extends ListActivity
{
	  LinkedList<ImageInfo> bitmaps=null;
	  Cursor cursor=null;
	  private static final String TAG = "ImageListView";
      private DBAdapter dbAdapter = null;
      int flag=1;  //sd
      String path;
      private BroadcastReceiver mReceiver = null;
      
	  @Override
	  protected void onCreate(Bundle icicle)
	  {
	    super.onCreate(icicle);
	    
	    setTitle(R.string.album_scan);
	    setContentView(R.layout.list_view);
 
	    Intent intent = getIntent();  
        path = intent.getStringExtra("path");  
        Log.i("ImageListView_onCreate", "path="+path);
        
        bitmaps=new LinkedList<ImageInfo>();
        
        if(path.equals("/usbhost/"))
        {
        	 Log.i("TAG", "obviousrly, you clicked item is usbhost");
   		    //scan all image
   		     getAllUsbHostImages("/usbhost/");
   		     return;
        }
        
        try
        {
           dbAdapter = new DBAdapter(this);
           dbAdapter.open();
           getThumbnailsPhotosInfo(path);
           if(dbAdapter!=null)
             dbAdapter.close();
	       setListAdapter(new ListViewAdapter(this,bitmaps));
        }
        catch(Exception err)
        {
        	if(dbAdapter!=null)
                dbAdapter.close();
        	err.printStackTrace();
        	Log.i(TAG, "get Thumbnails has err!");
        	Toast.makeText(this, R.string.no_sdcard, Toast.LENGTH_LONG).show();
		    return;
        }
	  }
	  
	  @Override
	  protected void onDestroy() {
	     super.onDestroy();
	     if (mReceiver != null) {
	            unregisterReceiver(mReceiver);
	            mReceiver = null;
	      }
	     if(dbAdapter!=null)
	        dbAdapter.close();
	  }


	  @Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			if(!path.equals("/sdcard/"))
			{
				return;  //nothing
			}	
			// install an intent filter to receive SD card related events.
	        IntentFilter intentFilter =
	                new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
	        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
	        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
	        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
	        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
	        intentFilter.addDataScheme("file");

	        mReceiver = new BroadcastReceiver() {
	            @Override
	            public void onReceive(Context context, Intent intent) {

	            	try
		            {
		               getThumbnailsPhotosInfo("/sdcard");
		
		    	       setListAdapter(new ListViewAdapter(ImageListView.this,bitmaps));
		            }
		            catch(Exception err)
		            {
		            	err.printStackTrace();
		            	Log.i(TAG, "get Thumbnails has err!");
		            	Toast.makeText(ImageListView.this, R.string.no_sdcard, Toast.LENGTH_LONG).show();
		    		    return;
		            }
		                
		            }
	        };
	        registerReceiver(mReceiver, intentFilter);
		}
	  
	  @Override
	  protected void onListItemClick(ListView l,View v,
	                                 int position,long id)
	  {
		 String name=bitmaps.get(position).displayName;
		 String path=bitmaps.get(position).path;
		 Log.i("ImageListView_onListItemClick", "the name="+name+"; path="+path);

		 Intent intent = new Intent();  
		 intent.setClass(ImageListView.this, ImageGridView.class);  
		 intent.putExtra("name",name); 
		 intent.putExtra("path",path); 
		 intent.putExtra("flag",flag); 
		 
		 List list=bitmaps.get(position).tag;
		 intent.putExtra("data", (String[])list.toArray(new String[list.size()]));
		 ImageListView.this.startActivity(intent);  
	  }
	   
      private void getThumbnailsPhotosInfo(String path)
      {
    	  Log.i(TAG, "getThumbnailsPhotosInfo, the path="+path);
    	  if(path.equals("/flash/"))
    	  {
    		  Log.i("TAG", "obviousrly, you clicked item is internal");
    		  flag=0;
    		  cursor=dbAdapter.getAllImages();
    		  if(cursor==null)
    		  {
    			  cursor.close();
    			  Toast.makeText(this, R.string.no_picture, Toast.LENGTH_LONG).show();
        		  return;
    		  }
    	  }
    	  else  //SD
    	  {
    		  Log.i("TAG", "obviousrly, you clicked item is sdcard");
    		  try
    		  {
    		     cursor= getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
    		     if(cursor==null)
        	     {
        		     Toast.makeText(this, R.string.no_sdcard, Toast.LENGTH_LONG).show();
        		     return;
        	      }
    		  }
    		  catch(Exception err)
    		  {
    			  if(cursor!=null)
    			     cursor.close();
     		      Toast.makeText(this,  R.string.no_sdcard, Toast.LENGTH_LONG).show();
     		      return;
    		  }
    	  }
    	  
	      ImageInfo info=null;
	      
	      HashMap<String, LinkedList<String>> albums=ImageCommon.getAlbumsInfo(flag, cursor);
	      cursor.close();
	      
	      for(Iterator<?> it = albums.entrySet().iterator(); it.hasNext(); )
	      {
	    	   Map.Entry e = (Map.Entry)it.next();
	    	   Log.i(TAG, "key: " + e.getKey());
	    	   Log.i(TAG, "value: " + e.getValue());
	    	   LinkedList<String> album=(LinkedList<String>)e.getValue();
	    	   
	    	   if(album!=null&&album.size()>0)
	    	   {
	    		   info=new ImageInfo();
	    		   info.displayName=(String)e.getKey();
		    	   info.picturecount=String.valueOf(album.size());
		    	   
		    	   //提取第0个记录中的信息。
	    		   String id=album.get(0).split("&")[0];
	    		   String albumpath=album.get(0).split("&")[1];
	    		   
	    		   String name=albumpath.substring(albumpath.lastIndexOf("/")+1);
	    		   albumpath=albumpath.substring(0, albumpath.lastIndexOf("/"));
	    		   
	    		   if(flag==0)  //internal
	    		   {
	    			    try
	    		        {
	    			        //info.icon=dbAdapter.getImageById(Integer.valueOf(id));
	    			    	Log.i(TAG, "attampt to readBitmaps, the name="+info.displayName+";name="+name);
	    			    	Bitmap image=ImageCommon.readBitmaps(info.displayName+"_"+name);
	    			    	if(image==null)
	    			    	{
	    			    		Resources res=getResources();
	    			    		image=BitmapFactory.decodeResource(res, R.drawable.icon);
	    			    	}
	    			    	info.icon=image;
	    		        }
	    			    catch(Exception err)
	    			    {
	    			    	err.printStackTrace();
	    			    	Log.i(TAG, "get image by id had a unkonown error!");
	    			    }
	    		   }
	    		   else  //sdcard
	    		   {
	    		        info.icon=Thumbnails.getThumbnail(getContentResolver(),Integer.valueOf(id),Thumbnails.MICRO_KIND,new BitmapFactory.Options());
	    		   }
	    		   info.path=albumpath;
	    		   
	    		   List list = new ArrayList();
    		       for(String str: album)
    		       {
    			      list.add(str);
    			      Log.i(TAG, "str="+ str);
    		       }
    		       info.tag=list;
	    		   bitmaps.add(info);
	    	   }
	      }
	      cursor.close();        
        }
      
        //get all usbhost images
	    private void getAllUsbHostImages(String path) {
		// TODO Auto-generated method stub
	    	flag=2;
	    	extens=new LinkedList<String>();
	    	getExtens();
	    	hashMapAlbum=new HashMap<String, Bitmap>();
	    	hashMapPictureCount=new HashMap<String, Integer>();
	    	hashMapPicturePaths=new HashMap<String, List<String>>();
	    	File file=new File(path);
	        getAllUsbHostImageFile(file);
	    	
	    	if(hashMapAlbum.size()<=0)
	    	{
	    		Toast.makeText(this, R.string.no_picture, Toast.LENGTH_LONG).show();
	    		return;
	    	}
	    	ImageInfo info=null;
            String temp=null;
		    for(Iterator<?> it = hashMapAlbum.entrySet().iterator(); it.hasNext(); )
		    {
		    	  Map.Entry e = (Map.Entry)it.next();
		    	  Log.i(TAG, "key: " + e.getKey());
		    	  String album=(String)e.getKey();
		    	  Bitmap bitmap=(Bitmap)e.getValue();
		    	  
		    	  info=new ImageInfo();
		    	  info.displayName=album;
		    	  info.icon=bitmap;
		    	  info.picturecount=hashMapPictureCount.get(album).toString();
		    	  
		    	  try
		    	  {
		    	      info.tag=hashMapPicturePaths.get(album);
		    	  }
		    	  catch(Exception err)
		    	  {
		    		  Log.i(TAG, "---err---");
		    		  err.printStackTrace();
		    	  }
		    	  
		    	  temp="/"+album+"/";
		    	  if(temp.equals(path))
		    	  {
		    		  info.path=path;
		    	  }
		    	  else
		    	  {
		    	     info.path=path+album;
		    	  }
		    	  
		    	  bitmaps.add(info);
		    }
		    
	    	setListAdapter(new ListViewAdapter(this,bitmaps));
	    }
	    
	    private LinkedList<String> extens=null;
	    private HashMap<String, Bitmap> hashMapAlbum=null;
	    private HashMap<String, Integer> hashMapPictureCount=null;
	    private HashMap<String, List<String>> hashMapPicturePaths=null;
	    public void getExtens()
		{
			extens.add(".JPEG");
			extens.add(".JPG");
			extens.add(".PNG");
			extens.add(".GIF");
			extens.add(".BMP");
		}
	    
	    public void getAllUsbHostImageFile(File file){
			
		  	file.listFiles(new FileFilter(){
					public boolean accept(File file) {
						String name = file.getName();
						int i = name.lastIndexOf('.');
						if(i != -1){
							name = name.substring(i).toUpperCase();
							if(extens.contains(name)){
								Log.i(TAG+"_accept-file", "for ready to savePicture! name="+file.getName()
										 +"; the path="+file.getAbsolutePath());
								
								savePicture(file);
								return true;
							}
						}else if(file.isDirectory()){
							getAllUsbHostImageFile(file);
						}
						return false;
					}

					private void savePicture(File file) {
						// TODO Auto-generated method stub
						String name=file.getName();
						String album=file.getParent();
						String path=file.getAbsolutePath();
						Log.i(TAG+"_savePicture", "the name="+name+"; the parent="+album+"; the path="+path);

	                    album=album.substring(album.lastIndexOf("/")+1);
						Log.i(TAG, "after sub, the album="+album);

						if(hashMapPictureCount.containsKey(album))
						{
							 int count=hashMapPictureCount.get(album)+1;
							 hashMapPictureCount.remove(album);
							 hashMapPictureCount.put(album, count);
							 
							 try
							 {
							      List l=hashMapPicturePaths.remove(album);
							      l.add(file.getAbsolutePath());
							      hashMapPicturePaths.put(album, l);
							 }
							 catch(Exception err)
							 {
								 Log.i(TAG, "--err--");
								 err.printStackTrace();
							 }
						}
						else
						{
							//Thumbnail Picture
							Bitmap bitmap=ImageCommon.getFitSizePicture(file);
							if(bitmap==null)
							{
							    Resources res=getResources();
							    bitmap=BitmapFactory.decodeResource(res, R.drawable.icon);
							}
							hashMapAlbum.put(album, bitmap);
							hashMapPictureCount.put(album, 1);
							
							 try
							 {
								 List list=new ArrayList();
							     list.add(file.getAbsolutePath());
							     hashMapPicturePaths.put(album, list);
							 }
							 catch(Exception err)
							 {
								 Log.i(TAG, "-err-");
								 err.printStackTrace();
							 }
						}
					}
		  	});
		  }

		public LinkedList<ImageInfo> loadAllList()

	    {
	    	LinkedList<ImageInfo> photolist = new LinkedList<ImageInfo>();
	        Cursor c = dbAdapter.getAllImages();

	        if (c.moveToFirst())

	        {

	        do{

	        	  ImageInfo image = new ImageInfo();                
	    		  image.id = c.getInt(c.getColumnIndex(DBAdapter.KEY_ROWID));                
	    		  image.path = c.getString(c.getColumnIndex(DBAdapter.KEY_PATH));  
	    		  ByteArrayInputStream stream = new ByteArrayInputStream( 
	    		            c.getBlob(c.getColumnIndex(DBAdapter.KEY_IMAGE))); 
                  image.icon=BitmapFactory.decodeStream(stream);
                  photolist.add(image);
	            Log.i(TAG+"guofq", "the path="+c.getString(c.getColumnIndex(DBAdapter.KEY_PATH)));

	        } while (c.moveToNext());

	        }
	        
	        return photolist;

	    }

	  
	}
