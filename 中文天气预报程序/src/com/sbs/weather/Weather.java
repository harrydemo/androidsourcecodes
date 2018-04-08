package com.sbs.weather;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Weather extends Activity {
	
	public EditText ETplace;
	public TextView TvPlace;
	public Button query;
	public TextView placeName;
	public ImageView imView; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ETplace = (EditText)findViewById(R.id.place);
        query = (Button)findViewById(R.id.query);
        imView = (ImageView)findViewById(R.id.myImageView);
        placeName = (TextView)findViewById(R.id.placeName);
        
		query.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				try{
						    TvPlace = (TextView)findViewById(R.id.tvPlace);
						    
							String place = CntoEn.getFullSpell(ETplace.getText().toString());
							placeName.setText(place);
							String weather = "";
							String url = "http://www.google.com/ig/api?&weather="+place;
							DefaultHttpClient client = new DefaultHttpClient();
							HttpUriRequest req = new HttpGet(url);
							HttpResponse resp = client.execute(req);
							
							//String strResult = EntityUtils.toString(resp.getEntity());
							//Log.i("weather->", strResult);
							//一华氏度等于9/5摄氏度数值+32 

							HttpEntity ent = resp.getEntity();
							InputStream stream = ent.getContent();
							
							DocumentBuilder b = DocumentBuilderFactory.newInstance()
									.newDocumentBuilder();
							Document d = b.parse(new InputSource(stream));	
							NodeList n = d.getElementsByTagName("forecast_conditions");
							
							// 获得图片url 当天的。
							String imgUrl = "http://www.google.com";
							imgUrl += n.item(0).getChildNodes().item(3).getAttributes().item(0).getNodeValue();
							imView.setImageBitmap(Utils.returnBitMap(imgUrl));  

							// 今后4天预报
							for (int i = 0; i < n.getLength(); i++) {
								weather += Utils.week(n.item(i).getChildNodes().item(0)
										.getAttributes().item(0).getNodeValue());
								weather += ", ";
								weather += (Integer
										.parseInt(n.item(i).getChildNodes().item(1)
												.getAttributes().item(0).getNodeValue()) - 32) * 5 / 9;
								weather += " ~ ";
								weather += (Integer
										.parseInt(n.item(i).getChildNodes().item(2)
												.getAttributes().item(0).getNodeValue()) - 32) * 5 / 9;
								weather += ", ";						
								weather += Utils.weather(n.item(i).getChildNodes().item(4)
										.getAttributes().item(0).getNodeValue());
								weather += "\n";
							}
							Log.i("parseed weather->", weather);
							TvPlace.setText(weather);
														
						} catch (Exception e) {
							e.printStackTrace();
						}
				
			}});	
	}
}