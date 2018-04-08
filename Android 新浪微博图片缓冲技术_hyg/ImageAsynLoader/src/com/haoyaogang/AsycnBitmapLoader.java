package com.haoyaogang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class AsycnBitmapLoader {

	private static final int MSG_GET_IMAGE_SUCCESS = 1;
	
	private Handler handler;
	
	
	private HashMap<String,SoftReference<Bitmap>> imagecache = null;
	private Context context;
	private ImageCallback imageCallback;
	private ImageView imageView;
	
	
	public AsycnBitmapLoader(Context context){
		imagecache = new HashMap<String, SoftReference<Bitmap>>();
		
		this.context = context;
		
		//message handler
		handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				
				if(msg.what == MSG_GET_IMAGE_SUCCESS)
				{
					Bitmap bitmap = (Bitmap)msg.obj;
					imageCallback.imageLoad(imageView, bitmap);
				}
				
			};
		};
	}
	
	
	public Bitmap loadBitmap(ImageView imageView, final String imageUrl, ImageCallback callback)
	{
		
		imageCallback = callback;
		this.imageView = imageView;
		
		//内存缓冲
		
		if(imagecache.containsKey(imageUrl))
		{
			SoftReference<Bitmap> ref = imagecache.get(imageUrl);
			Bitmap bitmap = ref.get();
			
			if(null != bitmap)
			{
				return bitmap;
			}
		}
		//本地缓冲
		else
		{
			String bitmapName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
			
			Log.i("imageloader", "imagename: "+bitmapName);
			
			File file = context.getExternalCacheDir();
			Log.i("imageloader", "externalcachedir: "+file.getAbsolutePath());
			File[] cacheFiles = file.listFiles();
			File imagefile = null;
			
			if(cacheFiles.length > 0)
			{
				for(File f : cacheFiles)
				{
					if(bitmapName.equals(f.getName()))
					{
						imagefile = f;
						break;
					}
				}
			}
			if(null != null)
			{
				return BitmapFactory.decodeFile(file.getAbsolutePath()+bitmapName);
			}
			
		}
		//从网络获取(启动一个线程异步操作)
		
		new Thread(){
			public void run() {
				
				
				try {
					
					InputStream is = (new URL(imageUrl).openStream());
					
					Bitmap temp_bitmap = BitmapFactory.decodeStream(is);
					
					Message msg = new Message();
					msg.what = MSG_GET_IMAGE_SUCCESS;
					msg.obj = temp_bitmap;
					
					handler.sendMessage(msg);
					
					//放入内存
					imagecache.put(imageUrl, new SoftReference<Bitmap>(temp_bitmap));
					
					//放入本地缓存
					File file = context.getExternalCacheDir();
					String bitmapName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
					File bitmapfile = new File(file.getAbsoluteFile()+bitmapName);
					
					bitmapfile.createNewFile();
					
					FileOutputStream fos = new FileOutputStream(bitmapfile);
					temp_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					
					fos.close();
					
					
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			};
		}.start();
		
		
		return null;
	}
	
	public interface ImageCallback{
		public void imageLoad(ImageView imageView, Bitmap bitmap);
	}
}
