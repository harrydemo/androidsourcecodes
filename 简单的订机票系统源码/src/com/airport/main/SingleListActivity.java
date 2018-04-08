package com.airport.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.airport.bean.SingleBean;
import com.airport.xml.SingleXML;

public class SingleListActivity extends Activity {
	private ListView lv;
	String leaveCity,arriveCity,leavedate;
	
	public List<Map<String, Object>> parse(String path) {
		//定义一个单程的数组
		List<Map<String, Object>> singles = new ArrayList<Map<String, Object>>();
		//单程的实体
		List<SingleBean> singlebean = null;
		//Sax解析工厂
		SAXParserFactory factroy = SAXParserFactory.newInstance();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		leaveCity = bundle.getString("leaveCity");//获取出发的数据
		arriveCity = bundle.getString("arriveCity");//获取目的城市数据
		leavedate=bundle.getString("leavedate");//单程的日期
		try {

			SAXParser parser = factroy.newSAXParser();
			XMLReader xmlreader = parser.getXMLReader();
			SingleXML handler = new SingleXML();//解析单程XML
			xmlreader.setContentHandler(handler);
			xmlreader.parse(new InputSource(this.getAssets().open(path)));//解析assets下解析xml
			singlebean = handler.getSingles();//获取单程的是列
			//for加强循环遍历实体类
			for (SingleBean sp : singlebean) {
				//根据用户的查询的关键字来查询。所以要截取字符窜。
				if (leaveCity.equals(sp.getLeaveCity().substring(0, 2)
						.toString())
						&& arriveCity.equals(sp.getArriveCity().substring(0, 2)
								.toString())) {
					//然后Map的容器装入进去。
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("listview1", sp.getValue());//获取Value
					map.put("listview2", sp.getKey());//获取关键字
					map.put("listview3", sp.getLeaveCity());//获取出发城市
					map.put("listview4", sp.getArriveCity());//获取到达城市
					map.put("listview5", sp.getLeavetime());//获取离开时间
					map.put("listview6", sp.getEndtime());//获取目的时间
					map.put("listview7", sp.getPrice());//获取价格
					map.put("listview8", sp.getDisaccount());//获取折扣
					singles.add(map);//添加进map对象里。
				} else {
					continue;
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return singles;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_city_item);
		TextView title=(TextView)findViewById(R.id.airinfo);
		lv = (ListView) findViewById(R.id.simple_listView);
		List<Map<String, Object>> singles = parse("single.xml");//解析single.xml
		title.setText(leavedate+"/"+ leaveCity + "--"+arriveCity+"\t"+"共"+singles.size()+"个航班");
		for (Map<String, Object> mp : singles) {
			System.out.println(mp.get("Value"));

		}
		String[] from = { "listview1", "listview2", "listview3", "listview4",
				"listview5", "listview6", "listview7", "listview8" };
		int[] to = { R.id.listview1, R.id.listview2, R.id.listview3,
				R.id.listview4, R.id.listview5, R.id.listview6, R.id.listview7,
				R.id.listview8 };
		//用SimpleAdapter把解析结果装入进去
		SimpleAdapter adapter = new SimpleAdapter(this, singles,
				R.layout.single_city, from, to);
		lv.setAdapter(adapter);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			Intent intent=new Intent(SingleListActivity.this,CheckAirportTabActivity.class);
			startActivity(intent);
		}
		return true;
	}

}
