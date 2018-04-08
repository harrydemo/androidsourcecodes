/**
 * 
 */
package com.ty.weather;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.util.TimeFormatException;

import com.ty.weather.ForecastProvider.WeatherWidgets;
import com.ty.weather.util.ForecastEntity;
import com.ty.weather.util.WidgetEntity;

/**
 * @author 088926
 * 
 */
public class WebServiceHelper {
	private static final String TAG = "WebServiceHelper";
	private static final String WEBSERVICE_URL = "http://www.google.com/ig/api?weather=%s&hl=zh-cn";

	private static final String FORECAST_INFORMATION = "forecast_information";
	private static final String CURRENT_CONDITIONS = "current_conditions";
	private static final String FORECAST_CONDITIONS = "forecast_conditions";
	private static final String PROBLEM_CAUSE = "problem_cause";

	private static final String CITY = "city";
	private static final String FORECAST_DATE = "forecast_date";
	private static final String CONDITION = "condition";
	private static final String TEMP_F = "temp_f";
	private static final String TEMP_C = "temp_c";
	private static final String HUMIDITY = "humidity";
	private static final String ICON = "icon";
	private static final String WIND_CONDITION = "wind_condition";

	private static final String DAY_OF_WEEK = "day_of_week";
	private static final String LOW = "low";
	private static final String HIGH = "high";

	public static void updateForecasts(Context context, Uri widgetUri)
			throws ForecastParseException {
		Log.d(TAG, "update the nowest forecast!");

		Uri forecastUri = Uri.withAppendedPath(widgetUri,
				WeatherWidgets.FORECAST_END);
		ContentResolver resolver = context.getContentResolver();

		Cursor cursor = null;
		String postalCode = null;

		try {
			cursor = resolver.query(widgetUri,
					new String[] { WidgetEntity.POSTALCODE }, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				postalCode = cursor.getString(0);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		// 获取天气信息
		WidgetEntity widgetEntity = queryWebservice(postalCode);

		if (widgetEntity == null) {
			throw new ForecastParseException(
					"No forecasts found from webservice query");
		}

		resolver.delete(forecastUri, null, null);
		ContentValues values = new ContentValues();
		for (ForecastEntity forecast : widgetEntity.getDetails()) {
			values.clear();
			values.put(ForecastEntity.DAYOFWEEK, forecast.getDayOfWeek());
			values.put(ForecastEntity.HIGHT, forecast.getHight());
			values.put(ForecastEntity.LOW, forecast.getLow());
			values.put(ForecastEntity.ICON, forecast.getIcon());
			values.put(ForecastEntity.CONDITION, forecast.getCondition());

			Log.d(TAG, "insert detail forecast infomations:"
					+ forecast.getDayOfWeek());
			resolver.insert(forecastUri, values);
		}

		values.clear();
		values.put(WidgetEntity.CITY, widgetEntity.getCity());
		values.put(WidgetEntity.CONDITION, widgetEntity.getCondition());
		values.put(WidgetEntity.FORECASTDATE, widgetEntity.getForecastDate());
		values.put(WidgetEntity.HUMIDITY, widgetEntity.getHumidity());
		values.put(WidgetEntity.ICON, widgetEntity.getIcon());
		values.put(WidgetEntity.TEMPC, widgetEntity.getTempC());
		values.put(WidgetEntity.TEMPF, widgetEntity.getTempF());
		values.put(WidgetEntity.WINDCONDITION, widgetEntity.getWindCondition());
		values.put(WidgetEntity.LAST_UPDATE_TIME, System.currentTimeMillis());

		Log.d(TAG, "update the weather infomation");
		resolver.update(widgetUri, values, null, null);

	}

	public static WidgetEntity queryWebservice(String postalCode)
			throws ForecastParseException {
		if (postalCode == null) {
			throw new ForecastParseException("can not covert to entity");
		}
		Log.d(TAG, "uri:" + String.format(WEBSERVICE_URL, postalCode));

		Reader responseReader;
		WidgetEntity widgetEntity = null;

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(String.format(WEBSERVICE_URL, postalCode));

		try {
			Log.d(TAG, "get google's weather infomation");
			HttpResponse response = client.execute(request);

			StatusLine status = response.getStatusLine();
			Log.d(TAG, "Request returned status " + status);

			HttpEntity entity = response.getEntity();
			responseReader = new InputStreamReader(entity.getContent(), "GB2312");

		} catch (IOException e) {
			throw new ForecastParseException("Problem calling forecast API", e);
		}

		// If response found, send through to parser
		if (responseReader != null) {
			widgetEntity = parseResponse(responseReader);
		}

		return widgetEntity;

	}

	/**
	 * 转换为实体
	 * 
	 * @param responseReader
	 * @return
	 */
	private static WidgetEntity parseResponse(Reader responseReader)
			throws ForecastParseException {
		// TODO Auto-generated method stub
		Log.d(TAG, "conver xml to widgetEntity");
		WidgetEntity widgetEntity = new WidgetEntity();

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xpp = factory.newPullParser();

			String tagName = null;

			xpp.setInput(responseReader);
			int eventType = xpp.getEventType();
			Log.d(TAG, "eventType:" + eventType);

			while (eventType != XmlPullParser.END_DOCUMENT) {
				//Log.d(TAG, "eventType:" + eventType);
				if (eventType == XmlPullParser.START_TAG) {
					tagName = xpp.getName();
					Log.d(TAG, "tag Name:" + tagName);

					if (PROBLEM_CAUSE.equals(tagName)) {
						throw new ForecastParseException(
								"the city is non correct!");
					} else if (FORECAST_INFORMATION.equals(tagName)) {
						dealWithInfomation(tagName, widgetEntity, xpp);
					} else if (CURRENT_CONDITIONS.equals(tagName)) {
						dealWithCurrentConditions(tagName, widgetEntity, xpp);
					} else if (FORECAST_CONDITIONS.equals(tagName)) {
						dealWithForecastConditions(tagName, widgetEntity, xpp);
					}
				}

				eventType = xpp.next();
			}

		} catch (IOException e) {
			throw new ForecastParseException("Problem parsing XML forecast", e);
		} catch (XmlPullParserException e) {
			throw new ForecastParseException("Problem parsing XML forecast", e);
		} catch (TimeFormatException e) {
			throw new ForecastParseException("Problem parsing XML forecast", e);
		}
		Log.d(TAG, "covert xml to entity success");
		return widgetEntity;
	}

	private static void dealWithForecastConditions(String name,
			WidgetEntity widgetEntity, XmlPullParser xpp) throws IOException,
			XmlPullParserException {
		// TODO Auto-generated method stub
		Log.d(TAG, "dealWithForecastConditions");
		ForecastEntity forecast = new ForecastEntity();
		xpp.next();

		int eventType = xpp.getEventType();
		String tagName = xpp.getName();
		while ((!name.equals(tagName) || eventType != XmlPullParser.END_TAG)
				&& eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (tagName.equals(CONDITION)) {
					forecast.setCondition(xpp.getAttributeValue(null, "data"));
				} else if (tagName.equals(DAY_OF_WEEK)) {
					forecast.setDayOfWeek(xpp.getAttributeValue(null, "data"));
				} else if (tagName.equals(HIGH)) {
					forecast.setHight(Integer.parseInt(xpp.getAttributeValue(
							null, "data")));
				} else if (tagName.equals(LOW)) {
					forecast.setLow(Integer.parseInt(xpp.getAttributeValue(
							null, "data")));
				} else if (tagName.equals(ICON)) {
					forecast.setIcon(xpp.getAttributeValue(null, "data"));
				}
			}
			xpp.next();

			eventType = xpp.getEventType();
			tagName = xpp.getName();
		}
		widgetEntity.getDetails().add(forecast);
	}

	private static void dealWithCurrentConditions(String name,
			WidgetEntity widgetEntity, XmlPullParser xpp) throws IOException,
			XmlPullParserException {
		// TODO Auto-generated method stub
		Log.d(TAG, "dealWithCurrentConditions");
		xpp.next();

		int eventType = xpp.getEventType();
		String tagName = xpp.getName();
		while ((!name.equals(tagName) || eventType != XmlPullParser.END_TAG)
				&& eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (tagName.equals(CONDITION)) {
					widgetEntity.setCondition(xpp.getAttributeValue(null,
							"data"));
					Log.d(TAG, "condition" + widgetEntity.getCondition());
				} else if (tagName.equals(TEMP_F)) {
					widgetEntity.setTempF(Integer.parseInt(xpp
							.getAttributeValue(null, "data")));
				} else if (tagName.equals(TEMP_C)) {
					widgetEntity.setTempC(Integer.parseInt(xpp
							.getAttributeValue(null, "data")));
				} else if (tagName.equals(HUMIDITY)) {
					widgetEntity.setHumidity(xpp
							.getAttributeValue(null, "data"));
				} else if (tagName.equals(ICON)) {
					widgetEntity.setIcon(xpp.getAttributeValue(null, "data"));
				} else if (tagName.equals(WIND_CONDITION)) {
					widgetEntity.setWindCondition(xpp.getAttributeValue(null,
							"data"));
				}
			}
			xpp.next();

			eventType = xpp.getEventType();
			tagName = xpp.getName();
		}
	}

	private static void dealWithInfomation(String name,
			WidgetEntity widgetEntity, XmlPullParser xpp) throws IOException,
			XmlPullParserException {
		// TODO Auto-generated method stub
		Log.d(TAG, "dealWithInfomation");
		xpp.next();

		int eventType = xpp.getEventType();
		String tagName = xpp.getName();
		while ((!name.equals(tagName) || eventType != XmlPullParser.END_TAG)
				&& eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (tagName.equals(CITY)) {
					widgetEntity.setCity(xpp.getAttributeValue(null, "data"));
				} else if (tagName.equals(FORECAST_DATE)) {
					widgetEntity.setForecastDate(convertStr2Date(xpp
							.getAttributeValue(null, "data")).getTime());
				}
			}
			xpp.next();

			eventType = xpp.getEventType();
			tagName = xpp.getName();
		}
	}
	
	public static Date convertStr2Date(String str) {
		Date d = null;
		try{
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			d = f.parse(str);
		}catch(ParseException e){
			Log.d(TAG, "date format exception");
		}
		return d;
	}
	
	/**
	 * 获取天气预报实体类
	 * @param context
	 * @param uri
	 * @return
	 */
	public static WidgetEntity getWidgetEntity(Context context, Uri uri, boolean needDetails) {
		// TODO Auto-generated method stub
		Log.d(TAG, "get forcast infomation from database");
		WidgetEntity widgetEn = null;
		ContentResolver res = context.getContentResolver();

		Cursor cursor = null;

		try {
			cursor = res.query(uri, WidgetEntity.widgetProjection, null, null,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				widgetEn = new WidgetEntity();
				widgetEn.setUpdateMilis(cursor.getInt(0));
				widgetEn.setCity(cursor.getString(1));
				widgetEn.setPostalCode(cursor.getString(2));
				widgetEn.setForecastDate(cursor.getLong(3));
				widgetEn.setCondition(cursor.getString(4));
				widgetEn.setTempF(cursor.getInt(5));
				widgetEn.setTempC(cursor.getInt(6));
				widgetEn.setHumidity(cursor.getString(7));
				widgetEn.setIcon(cursor.getString(8));
				widgetEn.setWindCondition(cursor.getString(9));
				widgetEn.setLastUpdateTime(cursor.getLong(10));
				widgetEn.setIsConfigured(cursor.getInt(11));
			} else {
				return null;
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		if (needDetails){
			try {
				Uri forecastUri = Uri.withAppendedPath(uri,
						WeatherWidgets.FORECAST_END);
				cursor = res.query(forecastUri, ForecastEntity.forecastProjection,
						null, null, null);
				ForecastEntity forecast = null;
				while (cursor != null && cursor.moveToNext()) {
					forecast = new ForecastEntity();
					forecast.setDayOfWeek(cursor.getString(1));
					forecast.setLow(cursor.getInt(2));
					forecast.setHight(cursor.getInt(3));
					forecast.setIcon(cursor.getString(4));
					forecast.setCondition(cursor.getString(5));
					forecast.setWidgetId(cursor.getInt(6));
	
					widgetEn.getDetails().add(forecast);
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}

		return widgetEn;
	}

	public static final class ForecastParseException extends Exception {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = -891526452631557227L;

		public ForecastParseException(String detailMessage) {
			super(detailMessage);
		}

		public ForecastParseException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}
	}
}
