package org.loon.framework.javase.game.utils;

import org.loon.framework.javase.game.core.geom.RectBox;

/**
 * Copyright 2008 - 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class NumberUtils {

	private NumberUtils() {

	}

	public static RectBox getBounds(float x, float y, float width,
			float height, float rotate, RectBox result) {
		int[] rect = getLimit(x, y, width, height, rotate);
		if (result == null) {
			result = new RectBox(rect[0], rect[1], rect[2], rect[3]);
		} else {
			result.setBounds(rect[0], rect[1], rect[2], rect[3]);
		}
		return result;
	}

	public static RectBox getBounds(float x, float y, float width,
			float height, float rotate) {
		return getBounds(x, y, width, height, rotate, null);
	}

	public static int[] getLimit(float x, float y, float width,
			float height, float rotate) {
		float rotation = MathUtils.toRadians(rotate);
		float angSin = MathUtils.sin(rotation);
		float angCos = MathUtils.cos(rotation);
		int newW = MathUtils.floor((width * MathUtils.abs(angCos))
				+ (height * MathUtils.abs(angSin)));
		int newH = MathUtils.floor((height * MathUtils.abs(angCos))
				+ (width * MathUtils.abs(angSin)));
		int centerX = (int) (x + (width / 2));
		int centerY = (int) (y + (height / 2));
		int newX = (int) (centerX - (newW / 2));
		int newY = (int) (centerY - (newH / 2));
		return new int[] { newX, newY, newW, newH };
	}

	final static private String[] zeros = { "", "0", "00", "000", "0000",
			"00000", "000000", "0000000", "00000000", "000000000", "0000000000" };

	/**
	 * 为指定数值补足位数
	 * 
	 * @param number
	 * @param numDigits
	 * @return
	 */
	public static String addZeros(long number, int numDigits) {
		return addZeros(String.valueOf(number), numDigits);
	}

	/**
	 * 为指定数值补足位数
	 * 
	 * @param number
	 * @param numDigits
	 * @return
	 */
	public static String addZeros(String number, int numDigits) {
		int length = numDigits - number.length();
		if (length != 0) {
			number = zeros[length] + number;
		}
		return number;
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isNan(String param) {
		boolean result = false;
		if (param == null || "".equals(param)) {
			return result;
		}
		param = param.replace('d', '_').replace('f', '_');
		try {
			Double test = new Double(param);
			test.intValue();
			result = true;
		} catch (NumberFormatException ex) {
			return result;
		}
		return result;
	}

	/**
	 * 检查一个数字是否为空
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(int val) {
		return (val == Integer.MIN_VALUE) ? true : 0 == val;
	}

	/**
	 * 检查一个字符串数字是否为空
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(String val) {
		return (val == null | "".equals(val) | val.equals(Integer
				.toString(Integer.MAX_VALUE)));
	}

	/**
	 * 单纯计算两个数值的百分比
	 * 
	 * @param divisor
	 * @param dividend
	 * @return
	 */
	public static float toPercent(long divisor, long dividend) {
		if (divisor == 0 || dividend == 0) {
			return 0f;
		}
		float cf = divisor * 1f;
		float pf = dividend * 1f;

		return (MathUtils.round(cf / pf * 10000f) * 1f) / 100f;
	}

	/**
	 * 获得100%进制剩余数值百分比。
	 * 
	 * @param maxValue
	 * @param minusValue
	 * @return
	 */
	public static float minusPercent(float maxValue, float minusValue) {
		return 100 - ((minusValue / maxValue) * 100);
	}

	/**
	 * 获得100%进制数值百分比。
	 * 
	 * @param maxValue
	 * @param minusValue
	 * @return
	 */
	public static float percent(float maxValue, float minValue) {
		return (minValue / maxValue) * 100;
	}
}
