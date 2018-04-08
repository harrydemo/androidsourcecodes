package com.xmobileapp.android.weatherforecast;

import java.io.StringBufferInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.xmobileapp.android.weatherforecast.meta.CityWeather;
import com.xmobileapp.android.weatherforecast.meta.Constant;
import com.xmobileapp.android.weatherforecast.transport.WebServiceCaller;

public class WeatherResult extends Activity {

	private CityWeather cityWeather;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		
		TextView tv = (TextView) this.findViewById(R.id.resultText);
		
		cityWeather = new CityWeather();
		final StringBuffer sb = new StringBuffer();
		
		Intent queryIntent = getIntent();
		String cityName = queryIntent.getStringExtra("CityName");
//		String countryName = queryIntent.getStringExtra("CountryName");
		
		Map map = new HashMap();
    	map.put("CityName", cityName);
    	map.put("CountryName", "China");
    	String xmlStr = WebServiceCaller.doCallWebService(Constant.SOAP_ACTION2, Constant.METHOD_NAME2, map);
//    	Log.d("xmlStr", xmlStr);
    	
    	RootElement root = new RootElement("CurrentWeather");
        Element entry = root.getChild("Location");
        entry.setEndTextElementListener(
          new EndTextElementListener() {
            public void end(String body) {
              System.out.println("prop: " + body);
              cityWeather.setLocation(body);
              sb.append("位置："+body+"\n");
            }
        });
        Element entry2 = root.getChild("Time");
        entry2.setEndTextElementListener(
          new EndTextElementListener() {
            public void end(String body) {
              System.out.println("prop: " + body);
              cityWeather.setTime(body);
              sb.append("时间："+body+"\n");
            }
        });
        Element entry3 = root.getChild("Wind");
        entry3.setEndTextElementListener(
          new EndTextElementListener() {
            public void end(String body) {
              System.out.println("prop: " + body);
              cityWeather.setWind(body);
              sb.append("风向："+body+"\n");
            }
        });
        Element entry4 = root.getChild("Visibility");
        entry4.setEndTextElementListener(
          new EndTextElementListener() {
            public void end(String body) {
              System.out.println("prop: " + body);
              cityWeather.setVisibility(body);
              sb.append("能见度："+body+"\n");
            }
        });
        Element entry5 = root.getChild("Temperature");
        entry5.setEndTextElementListener(
          new EndTextElementListener() {
            public void end(String body) {
              System.out.println("prop: " + body);
              cityWeather.setTemperature(body);
              sb.append("气温："+body+"\n");
            }
        });
        Element entry6 = root.getChild("DewPoint");
        entry6.setEndTextElementListener(
          new EndTextElementListener() {
            public void end(String body) {
              System.out.println("prop: " + body);
              cityWeather.setDewPoint(body);
//              sb.append("位置："+body+"\n");
            }
        });
        Element entry7 = root.getChild("RelativeHumidity");
        entry7.setEndTextElementListener(
          new EndTextElementListener() {
            public void end(String body) {
              System.out.println("prop: " + body);
              cityWeather.setRelativeHumidity(body);
              sb.append("相对湿度："+body+"\n");
            }
        });
        Element entry8 = root.getChild("Pressure");
        entry8.setEndTextElementListener(
          new EndTextElementListener() {
            public void end(String body) {
              System.out.println("prop: " + body);
              cityWeather.setPressure(body);
              sb.append("气压："+body+"\n");
            }
        });

        try {
        	
            // create the factory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // create a parser
            SAXParser parser = factory.newSAXParser();

            XMLReader xmlreader = parser.getXMLReader();
            xmlreader.setContentHandler(root.getContentHandler());
            
            // get our data via the xml string
            InputSource is = new InputSource(new StringBufferInputStream(xmlStr));

            xmlreader.parse(is);

        } catch(Exception e) {
        	e.printStackTrace();

        }
        
        tv.setText(sb);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		if (choiceMode == true) {
//			MenuInflater inflater = getMenuInflater();
//			inflater.inflate(R.menu.gal_menu, menu);
//			menu.findItem(R.id.GAL_menu_OK_Item).setEnabled(false);
//		}
//		return true;
//	}
//	
//	@Override
//	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		// TODO Auto-generated method stub
//		return super.onMenuItemSelected(featureId, item);
//	}

	
}
