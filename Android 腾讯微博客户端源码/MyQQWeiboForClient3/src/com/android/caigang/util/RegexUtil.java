package com.android.caigang.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	public static List<Map<String,Object>> getStartAndEndIndex(String sourceStr,Pattern pattern){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Matcher matcher = pattern.matcher(sourceStr);
		boolean isFind = matcher.find();
		while (isFind) {
			Map<String,Object> map = new HashMap<String, Object>();
			String faceName = matcher.group().substring(1,matcher.group().length());//±Ì«È√˚≥∆
			map.put("startIndex",matcher.start());
			map.put("endIndex",matcher.end());
			map.put("faceName",faceName);
			list.add(map);
			isFind = matcher.find((Integer)map.get("endIndex")-1);
		}
		return list;
	}
}
