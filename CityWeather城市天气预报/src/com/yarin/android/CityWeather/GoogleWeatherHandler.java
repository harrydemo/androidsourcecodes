package com.yarin.android.CityWeather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

/**
<xml_api_reply version="1">
-<weather module_id="0" tab_id="0" mobile_row="0" mobile_zipped="1" row="0" section="0">
-<forecast_information>
 <city data="Chengdu, Sichuan"/>
 <postal_code data="chengdu"/>
 <latitude_e6 data=""/>
 <longitude_e6 data=""/>
 <forecast_date data="2009-08-18"/>
 <current_date_time data="2009-08-19 00:00:00 +0000"/>
 <unit_system data="SI"/>
 </forecast_information>
-<current_conditions>
 <condition data="����"/>
 <temp_f data="88"/>
 <temp_c data="31"/>
 <humidity data="ʪ�ȣ� 58%"/>
 <icon data="/ig/images/weather/cn_cloudy.gif"/>
 <wind_condition data="���� �������٣�4 ��/��"/>
 </current_conditions>
-<forecast_conditions>
 <day_of_week data="�ܶ�"/>
 <low data="24"/>
 <high data="33"/>
 <icon data="/ig/images/weather/chance_of_rain.gif"/>
 <condition data="��������"/>
 </forecast_conditions>
-<forecast_conditions>
 <day_of_week data="����"/>
 <low data="21"/>
 <high data="31"/>
 <icon data="/ig/images/weather/cn_heavyrain.gif"/>
 <condition data="��"/>
 </forecast_conditions>
-<forecast_conditions>
 <day_of_week data="����"/>
 <low data="19"/>
 <high data="29"/>
 <icon data="/ig/images/weather/mostly_sunny.gif"/>
 <condition data="������"/>
 </forecast_conditions>
-<forecast_conditions>
 <day_of_week data="����"/>
 <low data="21"/>
 <high data="31"/>
 <icon data="/ig/images/weather/chance_of_rain.gif"/>
 <condition data="��������"/>
 </forecast_conditions>
 </weather>
 </xml_api_reply>
 */
public class GoogleWeatherHandler extends DefaultHandler
{
	//������Ϣ
	private WeatherSet		myWeatherSet			= null;

	//ʵʱ������Ϣ
	private boolean			is_Current_Conditions	= false;
	//Ԥ��������Ϣ
	private boolean			is_Forecast_Conditions	= false;

	private final String	CURRENT_CONDITIONS		= "current_conditions";
	private final String	FORECAST_CONDITIONS		= "forecast_conditions";


	public GoogleWeatherHandler()
	{

	}


	//����������Ϣ����
	public WeatherSet getMyWeatherSet()
	{
		return myWeatherSet;
	}


	@Override
	public void endDocument() throws SAXException
	{
		// TODO Auto-generated method stub
		super.endDocument();
	}


	@Override
	public void endElement(String uri, String localName, String name) throws SAXException
	{

		if (localName.equals(CURRENT_CONDITIONS))
		{
			this.is_Current_Conditions = false;
		}
		else if (localName.equals(FORECAST_CONDITIONS))
		{
			this.is_Forecast_Conditions = false;
		}
	}


	@Override
	public void startDocument() throws SAXException
	{
		this.myWeatherSet = new WeatherSet();
	}


	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
	{
		if (localName.equals(CURRENT_CONDITIONS))
		{//ʵʱ����
			Log.i("localName+CURRENT", localName);
			this.myWeatherSet.setMyCurrentCondition(new WeatherCurrentCondition());
			Log.i("localName+CURRENT+1", localName);
			this.is_Current_Conditions = true;
		}
		else if (localName.equals(FORECAST_CONDITIONS))
		{//Ԥ������
			this.myWeatherSet.getMyForecastConditions().add(new WeatherForecastCondition());
			this.is_Forecast_Conditions = true;
		}
		else
		{
			//�ֱ𽫵õ�����Ϣ���õ�ָ���Ķ�����
			if (localName.equals(CURRENT_CONDITIONS))
			{
				Log.i("localName+CURRENT", localName);
			}
			String dataAttribute = attributes.getValue("data");
			
			if (localName.equals("icon"))
			{
				if (this.is_Current_Conditions)
				{
					this.myWeatherSet.getMyCurrentCondition().setIcon(dataAttribute);
				}
				else if (this.is_Forecast_Conditions)
				{
					this.myWeatherSet.getLastForecastCondition().setIcon(dataAttribute);
				}
			}
			else if (localName.equals("condition"))
			{
				if (this.is_Current_Conditions)
				{
					this.myWeatherSet.getMyCurrentCondition().setCondition(dataAttribute);
				}
				else if (this.is_Forecast_Conditions)
				{
					this.myWeatherSet.getLastForecastCondition().setCondition(dataAttribute);
				}
			}
			else if (localName.equals("temp_c"))
			{
				this.myWeatherSet.getMyCurrentCondition().setTemp_celcius(dataAttribute);
			}
			else if (localName.equals("temp_f"))
			{
				this.myWeatherSet.getMyCurrentCondition().setTemp_fahrenheit(dataAttribute);
			}
			else if (localName.equals("humidity"))
			{
				this.myWeatherSet.getMyCurrentCondition().setHumidity(dataAttribute);
			}
			else if (localName.equals("wind_condition"))
			{
				this.myWeatherSet.getMyCurrentCondition().setWind_condition(dataAttribute);
			}// Tags is forecast_conditions
			else if (localName.equals("day_of_week"))
			{
				this.myWeatherSet.getLastForecastCondition().setDay_of_week(dataAttribute);
			}
			else if (localName.equals("low"))
			{
				this.myWeatherSet.getLastForecastCondition().setLow(dataAttribute);
			}
			else if (localName.equals("high"))
			{
				this.myWeatherSet.getLastForecastCondition().setHigh(dataAttribute);
			}
		}
	}


	@Override
	public void characters(char ch[], int start, int length)
	{
		/*
		 * Would be called on the following structure:
		 * <element>characters</element>
		 */
	}
}
