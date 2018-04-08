package cn.itcast.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;

import cn.itcast.domain.ImageBean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.util.Log;
import android.util.Xml;
/**
 * 获取网络图片业务bean,具有缓存功能
 */
public class ImageServiceBean {
	private static final String TAG = "ImageServiceBean";
	private Context context;
	private List<ImageBean> images;
	private Map<Integer, Uri> smallCache = new HashMap<Integer, Uri>();
	private Map<Integer, Uri> bigCache = new HashMap<Integer, Uri>();
	
	public ImageServiceBean(Context context) {
		this.context = context;
		try {
			this.images = getLastImages();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}
	
	public int getLength(){
		return images.size();
	}
	/**
	 * 获取缓存中的小图，如果没有缓存会直接从网络上获取
	 * @param id 位置
	 * @return
	 */
	public Uri getSmallBitmap(int id){
		if(smallCache.containsKey(id)){
			return smallCache.get(id);//返回缓存的图片
		}else{
			ImageBean bean = images.get(id);
			if(bean!=null){
				try {
					byte[] data = getImage(bean.getIcon());
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					File file = new File(context.getCacheDir(), UUID.randomUUID().toString()+ getExt(bean.getIcon()));//保存文件
					bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
					smallCache.put(id, Uri.fromFile(file));//放入缓存
					return Uri.fromFile(file);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
			}
		}
		return null;
	}
	/**
	 * 获取缓存中的大图，如果没有缓存会直接从网络上获取
	 * @param id 位置
	 * @return
	 */
	public Uri getBigBitmap(int id){
		if(bigCache.containsKey(id)){
			return bigCache.get(id);
		}else{
			ImageBean bean = images.get(id);
			if(bean!=null){
				try {
					byte[] data = getImage(bean.getSrc());
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					File file = new File(context.getCacheDir(), UUID.randomUUID().toString()+ getExt(bean.getSrc()));//保存文件
					bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
					bigCache.put(id, Uri.fromFile(file));//放入缓存
					return Uri.fromFile(file);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
			}
		}
		return null;
	}
	/**
	 * 获取文件后缀
	 */
	private static String getExt(String path) {
		return path.substring(path.lastIndexOf('.'));
	}
	/**
	 * 获取图片
	 * @param path 图片路径
	 * @return
	 * @throws Exception
	 */
	private static byte[] getImage(String path) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode()==200){
			InputStream inStream = conn.getInputStream();
			return readInputStream(inStream);//图片的二进制数据
		}else{
			throw new Exception("request image fail");
		}		
	}
	
	/**
	 * 从输入流读取数据
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	private static byte[] readInputStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len = inStream.read(buffer)) !=-1 ){
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
	/**
	 * 获取网络图片信息
	 * @return
	 * @throws Exception
	 */
	private static List<ImageBean> getLastImages() throws Exception{
		String path = "http://3g.ifeng.com/t/image.xml";
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		return getImages(inStream);
	}
	/**
	 * 解析xml内容
	 * @param inStream xml内容
	 * @return
	 * @throws Exception
	 */
	private static List<ImageBean> getImages(InputStream inStream) throws Exception{
		ImageBean data = null;
		List<ImageBean> datas = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();//产生第一个事件
		while(eventType != XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT://开始文档事件
				datas = new ArrayList<ImageBean>();
				break;
	
			case XmlPullParser.START_TAG://开始元素事件
				String nodeName = parser.getName();//取得解析器当前指向的元素的名称
				if("image".equals(nodeName)){
					data = new ImageBean();
				}
				if(data!=null){
					if("icon".equals(nodeName)){
						data.setIcon(parser.nextText());
					}
					if("src".equals(nodeName)){
						data.setSrc(parser.nextText());
					}
				}				
				break;
				
			case XmlPullParser.END_TAG://结束元素事件
				if("image".equals(parser.getName())){
					datas.add(data);
					data = null;
				}
				break;
			}
			eventType = parser.next();//进入下一个元素，并且触发相应的事件
		}
		inStream.close();
		return datas;
	}
}
