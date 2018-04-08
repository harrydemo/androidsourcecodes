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
 * ��ȡ����ͼƬҵ��bean,���л��湦��
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
	 * ��ȡ�����е�Сͼ�����û�л����ֱ�Ӵ������ϻ�ȡ
	 * @param id λ��
	 * @return
	 */
	public Uri getSmallBitmap(int id){
		if(smallCache.containsKey(id)){
			return smallCache.get(id);//���ػ����ͼƬ
		}else{
			ImageBean bean = images.get(id);
			if(bean!=null){
				try {
					byte[] data = getImage(bean.getIcon());
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					File file = new File(context.getCacheDir(), UUID.randomUUID().toString()+ getExt(bean.getIcon()));//�����ļ�
					bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
					smallCache.put(id, Uri.fromFile(file));//���뻺��
					return Uri.fromFile(file);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
			}
		}
		return null;
	}
	/**
	 * ��ȡ�����еĴ�ͼ�����û�л����ֱ�Ӵ������ϻ�ȡ
	 * @param id λ��
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
					File file = new File(context.getCacheDir(), UUID.randomUUID().toString()+ getExt(bean.getSrc()));//�����ļ�
					bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
					bigCache.put(id, Uri.fromFile(file));//���뻺��
					return Uri.fromFile(file);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
			}
		}
		return null;
	}
	/**
	 * ��ȡ�ļ���׺
	 */
	private static String getExt(String path) {
		return path.substring(path.lastIndexOf('.'));
	}
	/**
	 * ��ȡͼƬ
	 * @param path ͼƬ·��
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
			return readInputStream(inStream);//ͼƬ�Ķ���������
		}else{
			throw new Exception("request image fail");
		}		
	}
	
	/**
	 * ����������ȡ����
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
	 * ��ȡ����ͼƬ��Ϣ
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
	 * ����xml����
	 * @param inStream xml����
	 * @return
	 * @throws Exception
	 */
	private static List<ImageBean> getImages(InputStream inStream) throws Exception{
		ImageBean data = null;
		List<ImageBean> datas = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();//������һ���¼�
		while(eventType != XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT://��ʼ�ĵ��¼�
				datas = new ArrayList<ImageBean>();
				break;
	
			case XmlPullParser.START_TAG://��ʼԪ���¼�
				String nodeName = parser.getName();//ȡ�ý�������ǰָ���Ԫ�ص�����
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
				
			case XmlPullParser.END_TAG://����Ԫ���¼�
				if("image".equals(parser.getName())){
					datas.add(data);
					data = null;
				}
				break;
			}
			eventType = parser.next();//������һ��Ԫ�أ����Ҵ�����Ӧ���¼�
		}
		inStream.close();
		return datas;
	}
}
