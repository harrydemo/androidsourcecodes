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
		//����һ�����̵�����
		List<Map<String, Object>> singles = new ArrayList<Map<String, Object>>();
		//���̵�ʵ��
		List<SingleBean> singlebean = null;
		//Sax��������
		SAXParserFactory factroy = SAXParserFactory.newInstance();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		leaveCity = bundle.getString("leaveCity");//��ȡ����������
		arriveCity = bundle.getString("arriveCity");//��ȡĿ�ĳ�������
		leavedate=bundle.getString("leavedate");//���̵�����
		try {

			SAXParser parser = factroy.newSAXParser();
			XMLReader xmlreader = parser.getXMLReader();
			SingleXML handler = new SingleXML();//��������XML
			xmlreader.setContentHandler(handler);
			xmlreader.parse(new InputSource(this.getAssets().open(path)));//����assets�½���xml
			singlebean = handler.getSingles();//��ȡ���̵�����
			//for��ǿѭ������ʵ����
			for (SingleBean sp : singlebean) {
				//�����û��Ĳ�ѯ�Ĺؼ�������ѯ������Ҫ��ȡ�ַ��ܡ�
				if (leaveCity.equals(sp.getLeaveCity().substring(0, 2)
						.toString())
						&& arriveCity.equals(sp.getArriveCity().substring(0, 2)
								.toString())) {
					//Ȼ��Map������װ���ȥ��
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("listview1", sp.getValue());//��ȡValue
					map.put("listview2", sp.getKey());//��ȡ�ؼ���
					map.put("listview3", sp.getLeaveCity());//��ȡ��������
					map.put("listview4", sp.getArriveCity());//��ȡ�������
					map.put("listview5", sp.getLeavetime());//��ȡ�뿪ʱ��
					map.put("listview6", sp.getEndtime());//��ȡĿ��ʱ��
					map.put("listview7", sp.getPrice());//��ȡ�۸�
					map.put("listview8", sp.getDisaccount());//��ȡ�ۿ�
					singles.add(map);//��ӽ�map�����
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
		List<Map<String, Object>> singles = parse("single.xml");//����single.xml
		title.setText(leavedate+"/"+ leaveCity + "--"+arriveCity+"\t"+"��"+singles.size()+"������");
		for (Map<String, Object> mp : singles) {
			System.out.println(mp.get("Value"));

		}
		String[] from = { "listview1", "listview2", "listview3", "listview4",
				"listview5", "listview6", "listview7", "listview8" };
		int[] to = { R.id.listview1, R.id.listview2, R.id.listview3,
				R.id.listview4, R.id.listview5, R.id.listview6, R.id.listview7,
				R.id.listview8 };
		//��SimpleAdapter�ѽ������װ���ȥ
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
