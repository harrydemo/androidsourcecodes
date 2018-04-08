package com.android.tools;

/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

/*
 * 加载图片的线程
 * */
public class AsyncImgLoader{
	//回调接口
	public interface ImageCallback{

		public abstract void imageLoaded(Bitmap bitmap, String s);
	}

	//图片缓存
	private Map imageCache;

	//构造函数
	public AsyncImgLoader(){
		//创建图片缓存
		imageCache = new HashMap();
	}

	//加载图片
	public Bitmap loadDrawable(final String imageUrl, final ImageCallback callback){
		Bitmap bitmap = null;
		if (!imageCache.containsKey(imageUrl)) {
			//创建handler对象
			final LoadHandler handler = new LoadHandler(callback,imageUrl);
			//创建下载图片的线程
			(new LoadImageThread(imageUrl,handler)).start();
			
			bitmap = null;
			
		} else {
			SoftReference softreference = (SoftReference)imageCache.get(imageUrl);
			if (softreference.get() == null) {
				//创建handler对象
				final LoadHandler handler = new LoadHandler(callback,imageUrl);
				//创建下载图片的线程
				(new LoadImageThread(imageUrl,handler)).start();
				
				bitmap = null;
			} else {
				bitmap = (Bitmap)softreference.get();
			}
		}
		return bitmap;
	}

	//从URL下载图片
	protected Bitmap loadImageFromUrl(String s){
		Bitmap bitmap = null;
		
		try {
			Drawable drawable = Drawable.createFromStream(
					(new URL(s)).openStream(), "src");
			if (drawable == null) {
				return bitmap;
			}
			bitmap = ((BitmapDrawable) drawable).getBitmap();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return bitmap;
		
	}

	//hander类
	private class LoadHandler extends Handler{

		final AsyncImgLoader this$0;
		private final ImageCallback callback;
		private final String  imageUrl;

		//消息出来函数
		public void handleMessage(Message message){
			Bitmap bitmap = (Bitmap)message.obj;
			//加载图片
			callback.imageLoaded(bitmap, imageUrl);
		}

		LoadHandler(ImageCallback callback, String imageUrl){
			super();
			this$0 = AsyncImgLoader.this;
			this.callback = callback;
			this.imageUrl = imageUrl;
		}
	}
	
	
	//加载图片线程
	private class LoadImageThread extends Thread{

		final AsyncImgLoader this$0;
		private final Handler handler;
		private final String  imageUrl;

		//线程下载图片
		public void run(){
			AsyncImgLoader asyncimgloader = AsyncImgLoader.this;
			Bitmap bitmap = asyncimgloader.loadImageFromUrl(imageUrl);
			
			if (bitmap != null){
				SoftReference softreference = new SoftReference(bitmap);
				Object obj = imageCache.put(imageUrl, softreference);
				Message message = handler.obtainMessage(0, bitmap);
				
				boolean flag = handler.sendMessage(message);
			}
		}

		//构造函数
		LoadImageThread(String imageUrl, Handler mHandler){
			super();
			this$0 = AsyncImgLoader.this;
			this.imageUrl = imageUrl;
			handler = mHandler;
		}
	}

}
