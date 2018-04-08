package com.nil.util;

import org.geometerplus.fbreader.library.Book;

public class EpubUtil {
	static Book CURRENT_BOOK;
	static int PARAGRAPH_NUMBER;

	public static void setEpubUtil(Book book, int number) {
		CURRENT_BOOK = book;
		PARAGRAPH_NUMBER = number;
	}

	public static void setEpubUtil_CURRENT_BOOK(Book book) {
		CURRENT_BOOK = book;
	}

	public static void setEpubUtil_PARAGRAPH_NUMBER(int number) {
		if(number<0)
		{
			PARAGRAPH_NUMBER = 0;
		}else
		{
			PARAGRAPH_NUMBER = number;
		}
	}

	public static Book getEpubUtil_CURRENT_BOOK() {
		return CURRENT_BOOK;
	}

	public static int getEpubUtil_PARAGRAPH_NUMBER() {
		return PARAGRAPH_NUMBER;
	}

}
