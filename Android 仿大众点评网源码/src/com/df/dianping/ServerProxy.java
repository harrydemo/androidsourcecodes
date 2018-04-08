package com.df.dianping;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Parcelable.Creator;

public class ServerProxy
{
	private int i = 0;
	private List<Map<String, Object>> list;
	private Object obj;
	
	public void sendRequest(final ServerListener listener)
	{	
		final int num = i;
		if(i < 5)
		{
			Thread thread = new Thread() 
			{
				public void run() 
				{
					try
					{
						this.sleep(1000);
					}
					catch(Exception e)
					{
						
					}
					
					list = createData(num);
					if(i == 4)
					{
						listener.serverDataArrived(list, true);
					}
					else
					{
						listener.serverDataArrived(list, false);
					}
					
				}
			};
			thread.start();
			i++;
		}
		
		
	}
	
	private List<Map<String, Object>> createData(int i)
	{
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		
		if(i == 0)
		{
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("name", "Mo-Mo牧场");
			map.put("price", "人均：¥147");
			map.put("addr", "淮海路 日式自助");
			map.put("distance", "5.8km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "长宁区");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "初花");
			map.put("price", "人均：¥285");
			map.put("addr", "虹桥 日本料理");
			map.put("distance", "890m");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "长宁区");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "江边城外巫山烧全鱼（金陵东路店）");
			map.put("price", "人均：¥60");
			map.put("addr", "人民广场 川菜");
			map.put("distance", "8.1km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", true);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "长宁区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "绿茶餐厅");
			map.put("price", "人均：¥60");
			map.put("addr", "鲁迅公园 杭帮菜");
			map.put("distance", "10km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "闸北区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "上享餐厅");
			map.put("price", "人均：¥69");
			map.put("addr", "长寿路 茶餐厅");
			map.put("distance", "5.0km");
			map.put("tuan", true);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "闸北区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "王鼎日本料理铁板烧");
			map.put("price", "人均：¥240");
			map.put("addr", "人民广场 日本料理");
			map.put("distance", "7.4km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "浦东新区");
			list.add(map);
		}
		
		if(i == 1)
		{
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("name", "缇客");
			map.put("price", "人均：¥136");
			map.put("addr", "西式甜点");
			map.put("distance", "2.0km");
			map.put("tuan", true);
			map.put("promo", false);
			map.put("card", true);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "徐汇区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "酒吞");
			map.put("price", "人均：¥324");
			map.put("addr", "龙柏地区 日本料理");
			map.put("distance", "4.0km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "青浦区");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "夏朵西餐厅");
			map.put("price", "人均：¥76");
			map.put("addr", "徐家汇 西式简餐");
			map.put("distance", "4.4km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "松江区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "外婆家");
			map.put("price", "人均：¥56");
			map.put("addr", "火车站 杭帮菜");
			map.put("distance", "8.0km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "宝山区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "江边城外巫山烧全鱼（南京西路店）");
			map.put("price", "人均：¥63");
			map.put("addr", "南京西路 川菜");
			map.put("distance", "6.5km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", true);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "松江区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "百合福海鲜膳食总汇（日月光店）");
			map.put("price", "人均：¥246");
			map.put("addr", "打浦桥 自助餐");
			map.put("distance", "6.6km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "宝山区");
			list.add(map);
		}
		
		if(i == 2)
		{
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("name", "金钱豹（延安西路店）");
			map.put("price", "人均：¥224");
			map.put("addr", "动物园/虹桥机场 自助餐");
			map.put("distance", "2.9km");
			map.put("tuan", true);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "徐汇区");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "布歌东京（虹口龙之梦店）");
			map.put("price", "人均：¥27");
			map.put("addr", "鲁迅公园 西式甜点");
			map.put("distance", "6.8km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "虹口区");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "BLACK MAGIC CHOCOLATE");
			map.put("price", "人均：¥48");
			map.put("addr", "打浦桥 西式简餐");
			map.put("distance", "6.8km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "青浦区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "70后饭吧");
			map.put("price", "人均：¥62");
			map.put("addr", "长寿路 本帮江浙菜");
			map.put("distance", "5.2km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "杨浦区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "喜多屋国际海鲜黄品");
			map.put("price", "人均：¥227");
			map.put("addr", "陆家嘴 自助餐");
			map.put("distance", "10km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "徐汇区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "耶里夏丽新疆菜（政通店））");
			map.put("price", "人均：¥69");
			map.put("addr", "五角场/大学区 新疆/清真");
			map.put("distance", "6.6km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "徐汇区");
			list.add(map);
		}
		
		if(i == 3)
		{
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("name", "窝");
			map.put("price", "人均：¥67");
			map.put("addr", "静安寺 咖啡厅");
			map.put("distance", "5.4km");
			map.put("tuan", true);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "长宁区");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "泰妃阁（虹口龙之梦店）");
			map.put("price", "人均：¥71");
			map.put("addr", "鲁迅公园 东南亚菜");
			map.put("distance", "10km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "青浦区");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "三宝粥铺");
			map.put("price", "人均：¥29");
			map.put("addr", "人民广场 快餐简餐");
			map.put("distance", "8.1km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "杨浦区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "小山日本料理");
			map.put("price", "人均：¥183");
			map.put("addr", "新天地 日本料理");
			map.put("distance", "7.4km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "浦东新区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "莫尔顿牛排坊");
			map.put("price", "人均：¥602");
			map.put("addr", "陆家嘴 牛排");
			map.put("distance", "10km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 05);
			map.put("area", "浦东新区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "小小花园");
			map.put("price", "人均：¥69");
			map.put("addr", "徐家汇 咖啡");
			map.put("distance", "3.7km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "浦东新区");
			list.add(map);
		}
		
		if(i == 4)
		{
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("name", "阿久");
			map.put("price", "人均：¥103");
			map.put("addr", "中山公园 日本料理");
			map.put("distance", "2.4km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "徐汇区");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "椰香天堂");
			map.put("price", "人均：¥179");
			map.put("addr", "静安寺 泰过菜");
			map.put("distance", "5.1km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "徐汇区");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "萤七人间");
			map.put("price", "人均：¥137");
			map.put("addr", "静安寺 创意菜");
			map.put("distance", "7.1km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "徐汇区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "仙炙轩（汾阳路店）");
			map.put("price", "人均：¥457");
			map.put("addr", "音乐学院 日本烧烤");
			map.put("distance", "5.1km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "虹口区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "庄源");
			map.put("price", "人均：¥224");
			map.put("addr", "新天地 西班牙菜");
			map.put("distance", "7.4km");
			map.put("tuan", true);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "虹口区");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "柚子（陆家嘴店）");
			map.put("price", "人均：¥206");
			map.put("addr", "陆家嘴 日本料理");
			map.put("distance", "10km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "虹口区");
			list.add(map);
		}	
		return list;
	}
}