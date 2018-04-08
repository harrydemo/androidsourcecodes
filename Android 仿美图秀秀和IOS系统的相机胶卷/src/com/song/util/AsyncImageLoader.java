package com.song.util;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.song.Constant;



import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

//�������Ҫ������ʵ��ͼƬ���첽����
public class AsyncImageLoader {
	//ͼƬ�������
	//����ͼƬ��URL��ֵ��һ��SoftReference���󣬸ö���ָ��һ��Drawable����
	private Map<String, SoftReference<Bitmap>> imageCache = 
		new HashMap<String, SoftReference<Bitmap>>();
	
	//ʵ��ͼƬ���첽����
	public Bitmap loadDrawable(final String imageUrl,final ImageCallback callback){
		//��ѯ���棬�鿴��ǰ��Ҫ���ص�ͼƬ�Ƿ��Ѿ������ڻ��浱��
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> softReference=imageCache.get(imageUrl);
			if(softReference.get() != null){
				return softReference.get();
			}
		}
		
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				callback.imageLoaded((Bitmap) msg.obj);
			}
		};
		//�¿���һ���̣߳����߳����ڽ���ͼƬ�ļ���
		new Thread(){
			public void run() {
				Bitmap drawable=loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Bitmap>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			};
		}.start();
		return null;
	}
	
	protected Bitmap loadImageFromUrl(String imageUrl) {
		try {
			Bitmap bitmap = ImageUtil.getImageThumbnail(imageUrl, 
					Constant.width, 
					Constant.height);
			return bitmap;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//�ص��ӿ�
	public interface ImageCallback{
		public void imageLoaded(Bitmap imageDrawable);
	}
}
