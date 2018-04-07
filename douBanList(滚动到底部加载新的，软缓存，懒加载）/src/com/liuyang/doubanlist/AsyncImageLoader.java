package com.liuyang.doubanlist;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {
	//存放缓存的图片对象
	private HashMap<String, SoftReference<Drawable>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}
	//判断是否有缓存的图片，有的话返回缓存，没得话开启线程下载
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		//有的情况
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			System.out.println("用的缓存粗片");
			if (drawable != null) {
				return drawable;
			}
		}
		//没的情况
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				if (imageCallback != null) {
					imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
				}
			}
		};
		new Thread() {
			@Override
			public void run() {
				System.out.println("新下载粗片");
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}
	//根据图片网络地址返回一个Drawable
	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		Drawable d = null;
		if (i != null) {
			d = Drawable.createFromStream(i, "src");
		}
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
}
