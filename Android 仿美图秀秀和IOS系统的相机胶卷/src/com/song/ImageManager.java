package com.song;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;


import com.song.model.Bucket;
import com.song.model.Images;
import com.song.model.Thumbnails;
import com.song.util.StringUtils;


/**
 * 图片的加载类
 * @author admin
 *
 */
public class ImageManager {
	
	public static List<Bucket> bucketList = new ArrayList<Bucket>();
	
	//获取所有文件夹信息
	public static List<Bucket> loadAllBucketList(Context context){
		
		List<Bucket> tempBucketList = new ArrayList<Bucket>();	
		String[] projection = new String[] {MediaStore.Images.Media._ID,MediaStore.Images.Media.DATA,MediaStore.Images.Media.SIZE,MediaStore.Images.Media.DISPLAY_NAME
				,MediaStore.Images.Media.SIZE,MediaStore.Images.Media.MIME_TYPE,MediaStore.Images.Media.TITLE,
				MediaStore.Images.Media.DATE_ADDED,MediaStore.Images.Media.DATE_MODIFIED,MediaStore.Images.Media.DESCRIPTION,
				MediaStore.Images.Media.PICASA_ID,MediaStore.Images.Media.IS_PRIVATE,
				MediaStore.Images.Media.LATITUDE,MediaStore.Images.Media.LONGITUDE,MediaStore.Images.Media.DATE_TAKEN,
				MediaStore.Images.Media.ORIENTATION,MediaStore.Images.Media.MINI_THUMB_MAGIC,MediaStore.Images.Media.BUCKET_ID,
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME}; 
		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				projection, null, null, null);
		int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
		int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
		int sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
		int displayNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
		int mineTypeColumn = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
		int titleColumn = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
		int dateAddedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
		int dateModifiedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
		int descriptionColumn = cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION);
		int picasaIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.PICASA_ID);
		int isprivateColumn = cursor.getColumnIndex(MediaStore.Images.Media.IS_PRIVATE);
		int latitudeColumn = cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
		int longitudeColumn = cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
		int datetakenColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
		int orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
		int miniColumn = cursor.getColumnIndex(MediaStore.Images.Media.MINI_THUMB_MAGIC);
		int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
		int bucketDisplayNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);		
		List<Images> imageList = new ArrayList<Images>();

		if (cursor != null) {
			while (cursor.moveToNext()) {
				Images image = new Images();				
				image.set_id(cursor.getInt(idColumn));
				image.set_data(cursor.getString(dateColumn));
				image.set_size(cursor.getInt(sizeColumn));
				image.set_display_name(cursor.getString(displayNameColumn));
				image.setMime_type(cursor.getString(mineTypeColumn));
				image.setTitle(cursor.getString(titleColumn));
				image.setDate_added(cursor.getLong(dateAddedColumn));
				image.setDate_modified(cursor.getLong(dateModifiedColumn));
				image.setDescription(cursor.getString(descriptionColumn));
				image.setPicasa_id(cursor.getString(picasaIdColumn));
				image.setIsprivate(cursor.getInt(isprivateColumn));
				image.setLatitude(cursor.getFloat(latitudeColumn));
				image.setLongitude(cursor.getFloat(longitudeColumn));
				image.setDatetaken(cursor.getLong(datetakenColumn));
				image.setOrientation(cursor.getInt(orientationColumn));
				image.setMini_thumb_magic(cursor.getInt(miniColumn));
				image.setBucket_id(cursor.getString(bucketIdColumn));
				image.setBucket_display_name(cursor.getString(bucketDisplayNameColumn));
				if(!StringUtils.isNull(image.get_data()))
				imageList.add(image);
				Log.i("loadAllBucketList", image.toString());
			}
			//4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)  
			if(Integer.parseInt(Build.VERSION.SDK) < 14)  
			 {  
			  cursor.close();  
			 }  	
		}		
		Hashtable tempThumbnailsSet = loadAllThumbnailsSet(context);	
		Hashtable tempBucketSet = new Hashtable();
		for(int i=0;i<imageList.size();i++)
		{
			Images tempImage = imageList.get(i);	
			Thumbnails tempThumbnails = (Thumbnails) tempThumbnailsSet.get(String.valueOf(tempImage.get_id()));
			if(tempThumbnails!=null)
			{
				tempImage.setThumbnails(tempThumbnails);
			}
			
			Bucket tempBucke = (Bucket)tempBucketSet.get(StringUtils.getBucketPath(tempImage.get_data(), tempImage.get_display_name()));
			if(tempBucke!=null)
			{
				tempBucke.addImages(tempImage);
			}
			else
			{
				tempBucke = new Bucket();
				tempBucke.setName(tempImage.getBucket_display_name());
				tempBucke.setPath(StringUtils.getBucketPath(tempImage.get_data(), tempImage.get_display_name()));
				tempBucke.addImages(tempImage);
//				Log.i("loadAllBucketList", StringUtils.getBucketPath(tempImage.get_data(), tempImage.get_display_name()));
//				Log.i("loadAllBucketList", tempBucke.toString());
				tempBucketSet.put(StringUtils.getBucketPath(tempImage.get_data(), tempImage.get_display_name()), tempBucke);
			}		
		}
		 Iterator iterator = tempBucketSet.entrySet().iterator();
    	 while(iterator.hasNext())
    	 {
    		 Entry entry = (Entry) iterator.next();
//    		 Object key = entry.getKey(); 
    		 Object val = entry.getValue(); 
    		 tempBucketList.add((Bucket)val);            
    	 }
    	Log.i("loadAllBucketList", tempBucketList.size()+"");
		return tempBucketList;
	}
	
	//获取所有缩略图信息
	public static Hashtable loadAllThumbnailsSet(Context context){	
		Hashtable tempThumbnailsSet = new Hashtable();
		String[] projection = new String[] {MediaStore.Images.Thumbnails._ID,MediaStore.Images.Thumbnails.DATA,MediaStore.Images.Thumbnails.IMAGE_ID,
				MediaStore.Images.Thumbnails.KIND,MediaStore.Images.Thumbnails.WIDTH,MediaStore.Images.Thumbnails.HEIGHT};
		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
				projection, null, null, null);
		int idColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
		int dateColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
		int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
		int kindColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.KIND);
		int widthColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.WIDTH);
		int heightColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.HEIGHT);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Thumbnails thumbnails = new Thumbnails();		
				thumbnails.set_id(cursor.getInt(idColumn));
				thumbnails.set_data(cursor.getString(dateColumn));
				thumbnails.setImage_id(cursor.getInt(imageIdColumn));
				thumbnails.setKind(cursor.getInt(kindColumn));
				thumbnails.setWidth(cursor.getInt(widthColumn));
				thumbnails.setHeight(cursor.getInt(heightColumn));
				tempThumbnailsSet.put(String.valueOf(cursor.getInt(imageIdColumn)),
						thumbnails);
				Log.i("loadAllThumbnailsList", thumbnails.toString());
				
			}
			//4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)  
			if(Integer.parseInt(Build.VERSION.SDK) < 14)  
			 {  
			  cursor.close();  
			 }
		}
		return tempThumbnailsSet;
	}
	
}
