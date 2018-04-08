package com.maweis;

import java.util.ArrayList;
import java.util.List;

public class ReaderUtils {

	private int letterNumsPerLine = 20;

	private int lines = 20;

	public String[] getScreenText(String str) {
		List<String> list = new ArrayList<String>();

		str.split("\r\n");

		if (!((str.length() / letterNumsPerLine) > 20)) {
			lines = (str.length() / letterNumsPerLine);
		}

		for (int i = 0; i < lines; i++) {
			int startToken = i * letterNumsPerLine;
			int endToken = startToken + letterNumsPerLine;
			String tempStr = "";
			if (i == lines - 1) {
				tempStr = str.substring(startToken);
			} else {
				tempStr = str.substring(startToken, endToken);
			}
			list.add(tempStr);
		}
		String[] resultArray = new String[list.size()];
		list.toArray(resultArray);
		return resultArray;
	}

}
