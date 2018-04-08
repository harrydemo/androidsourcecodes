package com.szy.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;


public class JSONutil {

	/**
	 * 获取"数组形式"的JSON数据，
	 * 数据形式：[{"id":1,"name":"张三","age":22},{"id":2,"name":"李四","age":23}]
	 * @param path
	 *            网页路径
	 * @return 返回List
	 * @throws Exception
	 */
	public static List<Map<String, String>> getJSONArray(String path)
			throws Exception {

		String json = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		URL url = new URL(path);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(5 * 1000);// 单位是毫秒，设置超时时间为5秒
		// HttpURLConnection是通过HTTP协议请求path路径的，所以需要设置请求方式,可以不设置，因为默认为GET
		con.setRequestMethod("GET");
		// 判断请求码是否是200码，否则失败
		if (con.getResponseCode() == 200) {
			InputStream input = con.getInputStream();// 获取输入流
			byte[] data = readStream(input);// 把输入流转换为字符数组
			json = new String(data); // 把字符数组转换成字符串

			// 数据直接为一个数组形式，所以可以直接 用android提供的框架JSONArray读取JSON数据，转换成Array
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				// 每条记录又由几个Object对象组成
				JSONObject item = jsonArray.getJSONObject(i);
				int id = item.getInt("id"); // 获取对象对应的值
				String name = item.getString("name");
				int age = item.getInt("age");
				map = new HashMap<String, String>();
				map.put("id", id + "");
				map.put("name", name);
				map.put("age", age + "");
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 获取"对象形式"的JSON数据，
	 * 数据形式：{"total":2,"success":true,"arrayData":[{"id":1,"name"
	 * :"张三","age":23},{"id":2,"name":"李四","age":25}]}
	 * 
	 * @param path
	 *            网页路径
	 * @return 返回List
	 * @throws Exception
	 */
	public static List<Map<String, String>> getJSONObject(String path)
			throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		URL url = new URL(path);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(5 * 1000);// 单位是毫秒，设置超时时间为5秒
		// HttpURLConnection是通过HTTP协议请求path路径的，所以需要设置请求方式,可以不设置，因为默认为GET
		con.setRequestMethod("GET");
		if (con.getResponseCode() == 200) {
			InputStream input = con.getInputStream();
			byte[] bb = readStream(input);
			String json = new String(bb);
			//System.out.println(json);
			JSONObject jsonObject = new JSONObject(json);
			// 里面有一个数组数据，可以用getJSONArray获取数组
			JSONArray jsonArray = jsonObject.getJSONArray("arrayData");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject item = jsonArray.getJSONObject(i); // 得到每个对象
				int id = item.getInt("id"); // 获取对象对应的值
				String name = item.getString("name");
				int age = item.getInt("age");
				map = new HashMap<String, String>(); // 存放到Map里面
				map.put("id", id + "");
				map.put("name", name);
				map.put("age", age + "");
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 获取类型复杂的JSON数据 数据形式
	 * {"name":"张三","age":23,"content":{"questionsTotal":2,"questions":
	 * [{"question": "what's your name?", "answer": "张三"},{"question":
	 * "what's your age?", "answer": "23"}]}}
	 * @param path 网页路径
	 * @return 返回List
	 * @throws Exception
	 */
	public static List<Map<String, String>> getJSON(String path)
			throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		URL url = new URL(path);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(5 * 1000);// 单位是毫秒，设置超时时间为5秒
		// HttpURLConnection是通过HTTP协议请求path路径的，所以需要设置请求方式,可以不设置，因为默认为GET
		con.setRequestMethod("GET");
		if (con.getResponseCode() == 200) {
			InputStream input = con.getInputStream();
			byte[] bb = readStream(input);
			String json = new String(bb);
			
			JSONObject jsonObject = new JSONObject(json);
			String name = jsonObject.getString("name");
			int age = jsonObject.getInt("age");
			Log.i("abc", "name:" + name + " | age:" + age); // 测试数据
			// 获取对象中的对象
			JSONObject contentObject = jsonObject.getJSONObject("content");
			// 获取对象中的一个值
			//String questionsTotal = contentObject.getString("questionsTotal");

			// 获取对象中的数组
			JSONArray contentArray = contentObject.getJSONArray("questions");
			for (int i = 0; i < contentArray.length(); i++) {
				JSONObject item = contentArray.getJSONObject(i); // 得到每个对象
				String question = item.getString("question"); // 获取对象对应的问题
				String answer = item.getString("answer"); //获取对象对应的回答

				map = new HashMap<String, String>(); // 存放到Map里面
				map.put("question", question);
				map.put("answer", answer);
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 把输入流转换成字符数组
	 * 
	 * @param inputStream
	 *            输入流
	 * @return 字符数组
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream input) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = input.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		input.close();

		return bos.toByteArray();
	}
}
