package com.sly.android.huangcun.entrey;

public class BookMark {

	@Override
	public String toString() {
		return bookmarkID + "," + bookmarkName + "," + bookPage + ","
				+ bookName;
	}

	private int bookmarkID;
	private String bookmarkName;
	private int bookPage;
	private int bookName;


	public int getBookName() {
		return bookName;
	}

	public void setBookName(int bookName) {
		this.bookName = bookName;
	}

	public int getBookmarkID() {
		return bookmarkID;
	}

	public void setBookmarkID(int bookmarkID) {
		this.bookmarkID = bookmarkID;
	}

	public String getBookmarkName() {
		return bookmarkName;
	}

	public void setBookmarkName(String bookmarkName) {
		this.bookmarkName = bookmarkName;
	}

	public int getBookPage() {
		return bookPage;
	}

	public void setBookPage(int bookPage) {
		this.bookPage = bookPage;
	}

}
