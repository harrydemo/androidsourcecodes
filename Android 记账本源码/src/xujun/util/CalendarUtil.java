package xujun.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarUtil {
	/**按照SimpleDateFormat("yyyy-MM-dd")格式转化Date
	 * @param dateString yyyy-MM-dd格式的日期表达字符串
	 * @return Date
	 */
	public static Date getDate(String dateString)
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 返回yyyy-MM-dd格式的日期表达式
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getDataString(int year,int month,int day)
	{
		return String.format("%04d-%02d-%02d", year,month,day);
	}
}
