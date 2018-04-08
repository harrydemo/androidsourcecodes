package com.df.dianping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryData {
	public static  List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("title", "美食");
		map.put("img", R.drawable.food);

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "休闲娱乐");
		map.put("img", R.drawable.happy);

		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "生活服务");
		map.put("img", R.drawable.life);

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "购物");
		map.put("img", R.drawable.shopping);

		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "酒店");
		map.put("img", R.drawable.hotel);

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "丽人");
		map.put("img", R.drawable.beauty);

		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "运动健身");
		map.put("img", R.drawable.sports);

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "结婚");
		map.put("img", R.drawable.wedding);

		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "亲子");
		map.put("img", R.drawable.baby);

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "爱车");
		map.put("img", R.drawable.car);

		list.add(map);
		return list;
	}
}
