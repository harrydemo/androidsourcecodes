package sser.ksoap.sserEx09;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class sserEx09 extends Activity {
	    public ProgressDialog myDialog = null;         
	    private TextView ResultViewtext = null;
	    private View view;
	    private String weatherToday;   
	    
	    private String weatherNow;
	    private String weatherWillBe;
	    ImageView imageview02;
	    ImageView imageview03
	    ;
	    EditText etProvince = null;   
 
           
	    private static final String NAMESPACE = "http://WebXml.com.cn/";
	    private static String URL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";
	    private static final String METHOD_NAME = "getWeatherbyCityName";
	    private static String SOAP_ACTION = "http://WebXml.com.cn/getWeatherbyCityName";

	    /** Called when the activity is first created. */  
	    @Override  
	    public void onCreate(Bundle savedInstanceState) {   
	        super.onCreate(savedInstanceState);   
	        setContentView(R.layout.main);   
	        
	        etProvince = (EditText) this.findViewById(R.id.edit_province);  
	        Button btn = (Button) this.findViewById(R.id.btn_webservice);   
	        ResultViewtext = (TextView)findViewById(R.id.ResultView); 
	        view = findViewById(R.id.main);

	        imageview02 = (ImageView)findViewById(R.id.ImageView02);
	        imageview03 = (ImageView)findViewById(R.id.ImageView03);
		     

	        
	        btn.setOnClickListener(new Button.OnClickListener() {
	            public void onClick(View v) {
	            	new Thread(){
	            		public void run(){
	            			getWeather(etProvince.getText().toString());
	            			//showWeather();	            			
                        view.post(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//ResultViewtext.setText(getWeatherToday());
								showWeather();
							}
                       });
	            		}
	            	}.start();
	        
	            }
	        });
	    }

	    private void showWeather(){
	         ResultViewtext.setText(getWeatherToday());
	         setIcon(weatherNow,imageview02);
	         setIcon(weatherWillBe,imageview03);
	}

	    private void setIcon(String weather, ImageView imageview) {
			// TODO Auto-generated method stub
			if(weather.equalsIgnoreCase("nothing.gif"))
				imageview.setBackgroundResource(R.drawable.a_nothing);
			if(weather.equalsIgnoreCase("0.gif"))
				imageview.setBackgroundResource(R.drawable.a_0);
			if(weather.equalsIgnoreCase("1.gif"))
				imageview.setBackgroundResource(R.drawable.a_1);
			if(weather.equalsIgnoreCase("2.gif"))
				imageview.setBackgroundResource(R.drawable.a_2);
			if(weather.equalsIgnoreCase("3.gif"))
				imageview.setBackgroundResource(R.drawable.a_3);		
			if(weather.equalsIgnoreCase("4.gif"))
				imageview.setBackgroundResource(R.drawable.a_4);
			if(weather.equalsIgnoreCase("5.gif"))
				imageview.setBackgroundResource(R.drawable.a_5);
			if(weather.equalsIgnoreCase("6.gif"))
				imageview.setBackgroundResource(R.drawable.a_6);
			if(weather.equalsIgnoreCase("7.gif"))
				imageview.setBackgroundResource(R.drawable.a_7);
			if(weather.equalsIgnoreCase("8.gif"))
				imageview.setBackgroundResource(R.drawable.a_8);
			if(weather.equalsIgnoreCase("9.gif"))
				imageview.setBackgroundResource(R.drawable.a_9);
			if(weather.equalsIgnoreCase("10.gif"))
				imageview.setBackgroundResource(R.drawable.a_10);			
			if(weather.equalsIgnoreCase("11.gif"))
				imageview.setBackgroundResource(R.drawable.a_11);
			if(weather.equalsIgnoreCase("12.gif"))
				imageview.setBackgroundResource(R.drawable.a_12);
			if(weather.equalsIgnoreCase("13.gif"))
				imageview.setBackgroundResource(R.drawable.a_13);
			if(weather.equalsIgnoreCase("14.gif"))
				imageview.setBackgroundResource(R.drawable.a_14);
			if(weather.equalsIgnoreCase("15.gif"))
				imageview.setBackgroundResource(R.drawable.a_15);
			if(weather.equalsIgnoreCase("16.gif"))
				imageview.setBackgroundResource(R.drawable.a_16);
			if(weather.equalsIgnoreCase("17.gif"))
				imageview.setBackgroundResource(R.drawable.a_17);
			if(weather.equalsIgnoreCase("18.gif"))
				imageview.setBackgroundResource(R.drawable.a_18);
			if(weather.equalsIgnoreCase("19.gif"))
				imageview.setBackgroundResource(R.drawable.a_19);
			if(weather.equalsIgnoreCase("20.gif"))
				imageview.setBackgroundResource(R.drawable.a_20);
			if(weather.equalsIgnoreCase("21.gif"))
				imageview.setBackgroundResource(R.drawable.a_21);
			if(weather.equalsIgnoreCase("22.gif"))
				imageview.setBackgroundResource(R.drawable.a_22);
			if(weather.equalsIgnoreCase("23.gif"))
				imageview.setBackgroundResource(R.drawable.a_23);
			if(weather.equalsIgnoreCase("24.gif"))
				imageview.setBackgroundResource(R.drawable.a_24);
			if(weather.equalsIgnoreCase("25.gif"))
				imageview.setBackgroundResource(R.drawable.a_25);
			if(weather.equalsIgnoreCase("26.gif"))
				imageview.setBackgroundResource(R.drawable.a_26);
			if(weather.equalsIgnoreCase("27.gif"))
				imageview.setBackgroundResource(R.drawable.a_27);
			if(weather.equalsIgnoreCase("28.gif"))
				imageview.setBackgroundResource(R.drawable.a_28);
			if(weather.equalsIgnoreCase("29.gif"))
				imageview.setBackgroundResource(R.drawable.a_29);
			if(weather.equalsIgnoreCase("30.gif"))
				imageview.setBackgroundResource(R.drawable.a_30);			
			if(weather.equalsIgnoreCase("31.gif"))
				imageview.setBackgroundResource(R.drawable.a_31);
		}

		public void getWeather(String cityName) {
			try {
	      
				SoapObject msg = new SoapObject(NAMESPACE,METHOD_NAME);
				msg.addProperty("theCityName", cityName);
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);				
				envelope.bodyOut = msg;
				envelope.dotNet = true;
				
				AndroidHttpTransport sendRequest = new AndroidHttpTransport(URL);
				envelope.setOutputSoapObject(msg);
				sendRequest.call(SOAP_ACTION, envelope);
				
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject detail = (SoapObject) result.getProperty("getWeatherbyCityNameResult");
				
				parseWeather(detail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    
		private void parseWeather(SoapObject detail) {		
			//String date = detail.getProperty(6).toString();
			//weatherToday = "今天：" + date.split(" ")[0];
			//weatherToday = weatherToday + "\n天气：" + date.split(" ")[1];

			weatherToday =  "今天: " + detail.getProperty(4)+"\n";
			weatherToday += "天气：" + detail.getProperty(6).toString().split(" ")[1]+"\n";
			weatherToday += "气温：" + detail.getProperty(5)+"\n";
			weatherToday += "风向：" + detail.getProperty(7)+"\n";
			
			weatherNow = detail.getProperty(8).toString();
			weatherWillBe = detail.getProperty(9).toString();
			//getImageView(detail);
			}
		
		public String getWeatherToday() {
			return weatherToday;
	    }
	}

