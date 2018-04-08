package cn.com.karl.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import cn.com.karl.domain.Dict;
import cn.com.karl.domain.Sent;

public class PullXmlHelper {

	public static Dict pullParseXml(InputStream in) {
		Dict dict = null;
		List<Sent> sents = new ArrayList<Sent>();
		Sent sent = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			// 获取XmlPullParser实例
			XmlPullParser pullParser = factory.newPullParser();

			pullParser.setInput(in, "UTF-8");

			// 开始
			int eventType = pullParser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = pullParser.getName();
				switch (eventType) {
				// 文档开始
				case XmlPullParser.START_DOCUMENT:
					
					break;
				// 开始节点
				case XmlPullParser.START_TAG:
					if ("dict".equals(nodeName)) {
						dict = new Dict();
					} else if ("key".equals(nodeName)) {
						dict.setKey(pullParser.nextText());
					} else if ("ps".equals(nodeName)) {
						dict.setPs(pullParser.nextText());
					} else if ("pron".equals(nodeName)) {
						dict.setPron(pullParser.nextText());
					} else if ("pos".equals(nodeName)) {
						dict.setPos(pullParser.nextText());
					} else if ("acceptation".equals(nodeName)) {
						dict.setAcceptation(pullParser.nextText());
					} else if ("sent".equals(nodeName)) {
						sent = new Sent();
					} else if ("orig".equals(nodeName)) {
						sent.setOrig(pullParser.nextText());
					} else if ("pron".equals(nodeName)) {
						sent.setPronUrl(pullParser.nextText());
					} else if ("trans".equals(nodeName)) {
						sent.setTrans(pullParser.nextText());
					}
					break;
				// 结束节点
				case XmlPullParser.END_TAG:
					if ("sent".equals(nodeName)) {
						sents.add(sent);
						sent = null;
					}else if("dict".equals(nodeName)){
						dict.setSents(sents);
						dict=null;
					}
					break;
				default:
					break;
				}
				// 手动的触发下一个事件
				eventType = pullParser.next();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dict;

	}
}
