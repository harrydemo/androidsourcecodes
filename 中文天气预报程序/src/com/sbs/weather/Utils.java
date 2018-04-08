package com.sbs.weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils {

	public static String week(String enWeek) {
		if (enWeek.equals("Mon") || enWeek.equals("Monday"))
			return "星期一";
		else if (enWeek.equals("Tue") || enWeek.equals("Tuesday"))
			return "星期二";
		else if (enWeek.equals("Wed") || enWeek.equals("Wednesday"))
			return "星期三";
		else if (enWeek.equals("Thu") || enWeek.equals("Thursday"))
			return "星期四";
		else if (enWeek.equals("Fri") || enWeek.equals("Friday"))
			return "星期五";
		else if (enWeek.equals("Sat") || enWeek.equals("Saturday"))
			return "星期六";
		else if (enWeek.equals("Sun") || enWeek.equals("Sunday"))
			return "星期日";
		return "";

	}

	public static String weather(String enWeather) {
		if (enWeather.equals("Clear"))
			return "晴";
		else if (enWeather.equals("Partly Sunny")
				|| enWeather.equals("partly_cloudy"))
			return "多云";
		else if (enWeather.equals("Chance of Rain"))
			return "晴转雨";
		else if (enWeather.equals("storm"))
			return "暴雨";
		else if (enWeather.equals("thunderstorm"))
			return "雷阵雨";
		else if (enWeather.equals("fog"))
			return "大雾";
		else if (enWeather.equals("haze"))
			return "有雾";
		else if (enWeather.equals("rain"))
			return "雨";
		else if (enWeather.equals("heavyrain"))
			return "大雨";
		else if (enWeather.equals("lightrain"))
			return "小雨";
		else if (enWeather.equals("heavyrain"))
			return "大雨";
		else if (enWeather.equals("snow"))
			return "有雪";
		// / 还需要补充。。。。
		return "";
	}

	public static Bitmap returnBitMap(String imgUrl) {

		URL myImgUrl = null;

		Bitmap bitmap = null;

		try {

			myImgUrl = new URL(imgUrl);

		} catch (MalformedURLException e) {

			e.printStackTrace();

		}

		try {

			HttpURLConnection conn = (HttpURLConnection) myImgUrl

			.openConnection();

			conn.setDoInput(true);

			conn.connect();

			InputStream is = conn.getInputStream();

			bitmap = BitmapFactory.decodeStream(is);

			is.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return bitmap;

	}

}
