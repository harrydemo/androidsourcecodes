/**
 * <Date Util.>
 *  Copyright (C) <2009>  <Wang XinFeng,ACC http://androidos.cc/dev>
 *
 *   This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package cn.itcreator.android.reader.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Date Util
 * 
 * @author Wang XinFeng
 * @version  1.0
 * 
 */
public class DateUtil {
	
	public static String dateToString(Date d){
		Calendar calendar = Calendar.getInstance();
	//	calendar.setTime(d);
		StringBuilder sb= new StringBuilder();
		sb.append(calendar.get(Calendar.YEAR));
		sb.append("-");
		sb.append(calendar.get(Calendar.MONTH)+1);
		sb.append("-");
		sb.append(calendar.get(Calendar.DATE));
		sb.append(" ");
		sb.append(calendar.get(Calendar.HOUR_OF_DAY));
		sb.append(":");
		sb.append(calendar.get(Calendar.MINUTE));
		sb.append(":");
		sb.append(calendar.get(Calendar.SECOND));
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(dateToString(new Date(System.currentTimeMillis())));
	}
	
}
