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
			return "����һ";
		else if (enWeek.equals("Tue") || enWeek.equals("Tuesday"))
			return "���ڶ�";
		else if (enWeek.equals("Wed") || enWeek.equals("Wednesday"))
			return "������";
		else if (enWeek.equals("Thu") || enWeek.equals("Thursday"))
			return "������";
		else if (enWeek.equals("Fri") || enWeek.equals("Friday"))
			return "������";
		else if (enWeek.equals("Sat") || enWeek.equals("Saturday"))
			return "������";
		else if (enWeek.equals("Sun") || enWeek.equals("Sunday"))
			return "������";
		return "";

	}

	public static String weather(String enWeather) {
		if (enWeather.equals("Clear"))
			return "��";
		else if (enWeather.equals("Partly Sunny")
				|| enWeather.equals("partly_cloudy"))
			return "����";
		else if (enWeather.equals("Chance of Rain"))
			return "��ת��";
		else if (enWeather.equals("storm"))
			return "����";
		else if (enWeather.equals("thunderstorm"))
			return "������";
		else if (enWeather.equals("fog"))
			return "����";
		else if (enWeather.equals("haze"))
			return "����";
		else if (enWeather.equals("rain"))
			return "��";
		else if (enWeather.equals("heavyrain"))
			return "����";
		else if (enWeather.equals("lightrain"))
			return "С��";
		else if (enWeather.equals("heavyrain"))
			return "����";
		else if (enWeather.equals("snow"))
			return "��ѩ";
		// / ����Ҫ���䡣������
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
