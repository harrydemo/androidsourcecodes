package com.xmobileapp.android.weatherforecast;

import java.io.StringBufferInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.xmobileapp.android.weatherforecast.meta.Constant;
import com.xmobileapp.android.weatherforecast.transport.WebServiceCaller;

public class AndroidWeatherForecast extends ListActivity {
	
	private String cityName;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btn = (Button) this.findViewById(R.id.searchBtn);
        final EditText et = (EditText) this.findViewById(R.id.cityText);
        
        btn.setOnClickListener(new OnClickListener(){

        	public void onClick(View v) {
				
				cityName = et.getText().toString();
				callWeatherResultActivity(cityName);
			}
        	
        });
        
        List<String> cityList = getAllCitiesNames();
        setListAdapter(new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1, cityList));
    }
    
    public List<String> getAllCitiesNames(){
    	
    	final List<String> cityList = new LinkedList<String>();
    	Map map = new HashMap();
    	map.put("CountryName", "China");
    	
        try {
        	
            String xmlStr = WebServiceCaller.doCallWebService(Constant.SOAP_ACTION, Constant.METHOD_NAME, map);
            RootElement root = new RootElement("NewDataSet");
            Element entry = root.getChild("Table");
            entry.getChild("City").setEndTextElementListener(
              new EndTextElementListener() {
                public void end(String body) {
                  System.out.println("City Name: " + body);
                  cityList.add(body);
                }
            });

            // create the factory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // create a parser
            SAXParser parser = factory.newSAXParser();

            XMLReader xmlreader = parser.getXMLReader();
            xmlreader.setContentHandler(root.getContentHandler());
            
            // get our data via the xml string
            InputSource is = new InputSource(new StringBufferInputStream(xmlStr));

            xmlreader.parse(is);
            
            
//            ((TextView)findViewById(R.id.testText)).setText(result.toString());
        } catch(Exception e) {
        	e.printStackTrace();
//        	((TextView)findViewById(R.id.testText)).setText("ERROR:" + e.getClass().getName() + ": " + e.getMessage());
//        	((TextView)findViewById(R.id.lblStatus)).setText(E.getMessage());
        	
        	return null;
        }
        
        return cityList;
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		cityName = (String) l.getItemAtPosition(position);
		callWeatherResultActivity(cityName);
        
		super.onListItemClick(l, v, position, id);
	}
    
	private void callWeatherResultActivity(String cityName){
		
		Log.d("cityName", cityName);
        
        Intent i = new Intent(getApplicationContext(), WeatherResult.class);
//        i.putExtra("xml-result", xmlStr);
        i.putExtra("CityName", cityName);
//        i.putExtra("CountryName", "China");
		startActivity(i);
	}
    
}