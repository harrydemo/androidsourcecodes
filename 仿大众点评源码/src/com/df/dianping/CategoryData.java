package com.df.dianping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryData {
	public static  List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("title", "��ʳ");
		map.put("img", R.drawable.food);

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "��������");
		map.put("img", R.drawable.happy);

		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "�������");
		map.put("img", R.drawable.life);

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "����");
		map.put("img", R.drawable.shopping);

		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "�Ƶ�");
		map.put("img", R.drawable.hotel);

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "����");
		map.put("img", R.drawable.beauty);

		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "�˶�����");
		map.put("img", R.drawable.sports);

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "���");
		map.put("img", R.drawable.wedding);

		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "����");
		map.put("img", R.drawable.baby);

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "����");
		map.put("img", R.drawable.car);

		list.add(map);
		return list;
	}
}
