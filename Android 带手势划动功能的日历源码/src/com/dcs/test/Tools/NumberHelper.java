package com.dcs.test.Tools;

public class NumberHelper {

	public static String LeftPad_Tow_Zero(int str) {
		java.text.DecimalFormat format = new java.text.DecimalFormat("00");
		return format.format(str);

	}

}
