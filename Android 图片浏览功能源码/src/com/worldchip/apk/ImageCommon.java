package com.worldchip.apk;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

/**
 * 
 * @author guofq
 *
 */
public class ImageCommon {

	
	
	private static final String TAG = "ImageCommon";
    private static final String PATH="/flash/.thumbnails/";


	public static HashMap<String, LinkedList<String>> getAlbumsInfo(int id, Cursor cursor)
	{
		LinkedList<ImageInfo> images=new LinkedList<ImageInfo>();
		HashMap<String, LinkedList<String>> albumsInfos=new HashMap<String, LinkedList<String>>();
		ImageInfo iamge=null;
		
		String _path="_data";
		String _album="bucket_display_name";
		if(id==0) //INTERNAL
		{
			_path="path";
			_album="albums";
		}

		if (cursor.moveToFirst())
	    {
	        do{          
	        	  int _id=cursor.getInt(cursor.getColumnIndex("_id")); 
	        	  String path=cursor.getString(cursor.getColumnIndex(_path));
	        	  String album=cursor.getString(cursor.getColumnIndex(_album));  
	        	  
	        	  if(albumsInfos.containsKey(album))
	        	  {
	        		  LinkedList<String> albums=albumsInfos.remove(album);
	        		  albums.add(_id+"&"+path);
	        		  albumsInfos.put(album, albums);
	        	  }
	        	  else
	        	  {
	        		  LinkedList<String> albums=new LinkedList<String>();
	        		  albums.add(_id+"&"+path);
	        		  albumsInfos.put(album, albums);
	        	  }
	    	}while (cursor.moveToNext());
	      }
		return albumsInfos;
	}
	
	
	  
	  public static Bitmap getFitSizePicture(File f){ 
		    Log.i(TAG, "code come to getFitSizePicture, the file name="+f.getName()+"; the path="+f.getAbsolutePath());
		    Bitmap resizeBmp = null;
		    BitmapFactory.Options opts = new BitmapFactory.Options(); 
		    if(f.length()<20480){         //0-20k
		      opts.inSampleSize = 4;
		    }else if(f.length()<51200){   //20-50k
		      opts.inSampleSize = 6;
		    }else if(f.length()<307200){  //50-300k
		      opts.inSampleSize = 8;
		    }else if(f.length()<1048576){ //800-1024k
		      opts.inSampleSize = 10;
		    }else{
		      opts.inSampleSize = 12;
		    }
		    
		    try
		    {
		    	Log.i(TAG, "file.length="+f.length());
		       resizeBmp = BitmapFactory.decodeFile(f.getPath(),opts);
		    }
		    catch(Exception err)
		    {
		    	err.printStackTrace();
		    	Log.i(TAG, "BitmapFactory.decodeFile(f.getPath(),opts) has err!!!!!!");
		    }
		    
		    return resizeBmp; 
      }

	  public static Bitmap getGalleryPicture(String path){ 

            File f=new File(path);
		    Bitmap resizeBmp = null;
		    BitmapFactory.Options opts = new BitmapFactory.Options(); 
		    if(f.length()<51200){   //20-50k
		      opts.inSampleSize = 2;
		    }else if(f.length()<307200){  //50-300k
		      opts.inSampleSize = 4;
		    }else if(f.length()<819200){  //300-800k
		      opts.inSampleSize = 6;
		    }else if(f.length()<1048576){ //800-1024k
		      opts.inSampleSize = 8;
		    }else{
		      opts.inSampleSize = 12;
		    }
		    
		    try
		    {
		       Log.i(TAG, "file.length="+f.length());
		       resizeBmp = BitmapFactory.decodeFile(f.getPath(),opts);
		    }
		    catch(Exception err)
		    {
		    	err.printStackTrace();
		    	Log.i(TAG, "BitmapFactory.decodeFile(f.getPath(),opts) has err!!!!!!");
		    }
		    
		    return resizeBmp; 
      }

	  public static Bitmap getGalleryPicture2(String path){ 

            File imageFile=new File(path);
            
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageFile.getAbsolutePath(), opts);

            opts.inSampleSize = computeSampleSize(opts, -1, 128*128);
            opts.inJustDecodeBounds = false;
 
		    Bitmap resizeBmp = null;
		   
		    try
		    {
		       Log.i(TAG, "file.length="+imageFile.length());
		       resizeBmp = BitmapFactory.decodeFile(imageFile.getPath(),opts);
		    }
		    catch(OutOfMemoryError err)
		    {
		    	err.printStackTrace();
		    	Log.i(TAG+"_guofq", "BitmapFactory.decodeFile(f.getPath(),opts) has err!!!!!!");
		    }
		    
		    return resizeBmp; 
    }

   
	 
	public static LinkedList<ImageInfo> getImageInOneAlbum(Resources res, 
			String[] pathArrary) {
		// TODO Auto-generated method stub
		LinkedList<ImageInfo> images=new LinkedList<ImageInfo>();
		ImageInfo image=null;
		for(String path : pathArrary)
		{
			image=new ImageInfo();
			image.icon=BitmapFactory.decodeFile(path);
			images.add(image);
		}
		return images;
	}
	
	public static Boolean saveBitmap(String name, Bitmap bitmap)
    {
		  Log.i("Common_saveBitmap", "PATH="+PATH+"; NAME="+name);
		  
          File file = new File(PATH, name);
          FileOutputStream out;
          try{
                out = new FileOutputStream(file);
                if(bitmap.compress(Bitmap.CompressFormat.PNG, 70, out))
                {
                    out.flush();
                    out.close();
                    return true;
                }
          }
          catch (FileNotFoundException e)
          {
        	   Log.i(TAG, "saveBitmap has err! FileNotFoundException!");
               e.printStackTrace();
          }
          catch (IOException e)
          {
        	  Log.i(TAG, "saveBitmap has err! IOException!");
               e.printStackTrace();
          }
          
          return false;
    }
	
	public static Bitmap readBitmaps(String name)
    {
		Log.i(TAG, "readBitmaps, name="+name);
        File file= new File(PATH+name);
        
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }
	
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8 ) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }

	    return roundedSize;
	}

	
	private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}
	 
}
