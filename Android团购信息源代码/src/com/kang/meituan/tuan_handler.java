package com.kang.meituan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class tuan_handler
{
	public final static int MEITUAN = 0;
	public final static int LASHOU = 1;
	public final static int FTUAN = 2;
	public final static int NUOMI = 3;

	/**
	 * 通过pull解析器取得团购网站最新团购信息
	 * 
	 * @param input
	 *            团购网站输入流
	 * @param website
	 *            团购网站的名称
	 * @return 团购数据列表
	 * @throws Exception
	 */
	public static List<meituan> getListMeituan(InputStream input, int web)
			throws Exception
	{
		String data = null, website = null, city_name = null, deal_id = null, deal_title = null, deal_url = null, deal_img = null, deal_desc = null, sales_num = null, value = null, price = null, rebate = null, start_time = null, end_time = null, shop_name = null, shop_tel = null, shop_addr = null, shop_area = null;
		boolean isLashou = false;

		List<meituan> meituans = null;
		meituan mei = null;

		switch (web)
		{
		case MEITUAN:
			data = "data";
			website = "website";
			deal_id = "deal_id";
			city_name = "city_name";
			deal_title = "deal_title";
			deal_url = "deal_url";
			deal_img = "deal_img";
			deal_desc = "deal_desc";
			sales_num = "sales_num";
			value = "value";
			price = "price";
			rebate = "rebate";
			start_time = "start_time";
			end_time = "end_time";
			shop_name = "shop_name";
			shop_tel = "shop_tel";
			shop_addr = "shop_addr";
			shop_area = "shop_area";

			break;

		case LASHOU:
		case FTUAN:
		case NUOMI:

			data = "url";
			deal_url = "loc";
			website = "website";
			deal_id = "deal_id";
			city_name = "city";
			deal_title = "title";
			deal_img = "image";
			deal_desc = "deal_desc";
			sales_num = "bought";
			value = "value";
			price = "price";
			rebate = "rebate";
			start_time = "startTime";
			end_time = "endTime";
			shop_name = "name";
			shop_tel = "tel";
			shop_addr = "addr";
			shop_area = "shop_area";

			isLashou = true;

			break;

		}

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(input, "UTF-8");
		int eventType = parser.getEventType(); // 产生第一个事件

		while (eventType != XmlPullParser.END_DOCUMENT)
		{ // 只要不是文档结束事件
			switch (eventType)
			{
			case XmlPullParser.START_DOCUMENT:
				meituans = new ArrayList<meituan>();
				break;
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if (data.equals(name))
				{
					mei = new meituan();
				}
				if (null != mei)
				{
					if (website.equals(name))
					{
						mei.setWebsite(parser.nextText());
					}
					if (city_name.equals(name))
					{
						mei.setCity_name(parser.nextText());
					}
					if (deal_id.equals(name))
					{
						mei.setDeal_id(parser.nextText());
					}
					if (deal_title.equals(name))
					{
						mei.setDeal_title(parser.nextText());
					}
					if (deal_url.equals(name))
					{
						mei.setUrl(parser.nextText());
					}
					if (deal_img.equals(name))
					{
						mei.setDeal_img(parser.nextText());
					}
					if (deal_desc.equals(name))
					{
						mei.setDeal_desc(parser.nextText());
					}
					if (value.equals(name))
					{
						mei.setValue(parser.nextText());
					}
					if (price.equals(name))
					{
						mei.setPrice(parser.nextText());
					}
					if (rebate.equals(name))
					{
						mei.setRebate(parser.nextText());
					}
					if (sales_num.equals(name))
					{
						mei.setSales_num(parser.nextText());
					}
					if (start_time.equals(name))
					{
						mei.setStart_time(new Long(parser.nextText()));
					}
					if (end_time.equals(name))
					{
						mei.setEnd_time(new Long(parser.nextText()));
					}
					if (shop_name.equals(name))
					{
						mei.setShop_name(parser.nextText());
					}
					if (shop_tel.equals(name))
					{
						mei.setShop_tel(parser.nextText());
					}
					if (shop_addr.equals(name))
					{
						mei.setShop_addr(parser.nextText());
					}
					if (shop_area.equals(name))
					{
						mei.setShop_area(parser.nextText());
					}

				}

				break;

			case XmlPullParser.END_TAG:

				if (data.equals(parser.getName()))
				{
					if (isLashou)
					{
						String str = mei.getDeal_title();
						mei.setDeal_desc(str);
						mei.setDeal_title(str.substring(0, 30) + "...");

					}

					meituans.add(mei);
					mei = null;

				}

				break;

			}

			eventType = parser.next();

		}
		input.close();

		return meituans;

	}

}
