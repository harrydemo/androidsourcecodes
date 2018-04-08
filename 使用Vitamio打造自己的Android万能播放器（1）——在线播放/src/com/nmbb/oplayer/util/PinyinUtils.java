package com.nmbb.oplayer.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public final class PinyinUtils {

	private static HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();

	static {
		spellFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		//spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
	}

	public static String chineneToSpell(String chineseStr) throws BadHanyuPinyinOutputFormatCombination {
		StringBuffer result = new StringBuffer();
		for (char c : chineseStr.toCharArray()) {
			if (c > 128) {
				result.append(PinyinHelper.toHanyuPinyinStringArray(c, spellFormat)[0]);
			} else
				result.append(c);
		}
		return result.toString();
	}
}
