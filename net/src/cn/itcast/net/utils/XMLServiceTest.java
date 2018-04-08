package cn.itcast.net.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * 发送xml数据到web应用
 * @author Administrator
 *
 */
public class XMLServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable {
		String path = "http://192.168.1.10:8080/videoweb/video/manage.do?method=getXML";
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><persons><person id=\"23\"><name>中国</name><age>30</age></person></persons>";
		byte[] data = xml.getBytes("UTF-8");//得到了xml的实体数据
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5 *1000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(data);
		outStream.flush();
		outStream.close();
		System.out.println(conn.getResponseCode());
	}

}
