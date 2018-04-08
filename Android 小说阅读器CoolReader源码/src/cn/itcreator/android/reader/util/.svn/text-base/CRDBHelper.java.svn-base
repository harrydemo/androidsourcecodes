/**
 * < CoolReader Database operator.>
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

import java.util.ArrayList;
import java.util.List;

import cn.itcreator.android.reader.domain.Book;
import cn.itcreator.android.reader.domain.BookMark;
import cn.itcreator.android.reader.paramter.Constant;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * CoolReader Database operator
 * 
 * @author Wang XinFeng
 * @version  1.0
 * 
 */
public class CRDBHelper extends SQLiteOpenHelper {
	private SQLiteDatabase sql = null;
	private boolean isopen = false;

	public CRDBHelper(Context c) {
		super(c, Constant.DB_NAME, null, Constant.DB_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String tag = "onCreate";
		Log.d(tag, "start create table");
		db.execSQL(Constant.CREATE_TABLE_BOOK);
		db.execSQL(Constant.CREATE_TABLE_BOOK_MARK);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/** save the book info to database */
	public int saveBook(Book b) {
		String tag = "saveBook";
		Log.d(tag, "query the book form database");
		Log.d(tag, "query the book path:" + b.getBookPath());
		if (!isopen) {
			isopen = true;
			sql = getWritableDatabase();
		}
		String[] col = new String[] { Constant.BOOK_ID, Constant.BOOK_PATH };
		Cursor cur = sql.query(Constant.BOOK, col, Constant.BOOK_PATH + "=\""
				+ b.getBookPath() + "\"", null, null, null, null);
		int c = cur.getCount();
		if (c == 0) {
			ContentValues values = new ContentValues();
			values.put(Constant.BOOK_PATH, b.getBookPath());
			cur.close();
			return (int) sql.insert(Constant.BOOK, null, values);
		} else {
			cur.moveToLast();
			int i = cur.getInt(0);
			cur.close();
			return i;
		}

	}

	/**
	 * 保存书签到数据库中
	 * 
	 * @param bm
	 *            要保存的书签
	 * @return 此书签在数据库中的ID
	 */
	public boolean addBookMark(BookMark bm) {
		String tag = "addBookMark";
		Log.d(tag, "insert the book mark into database");
		if (!isopen) {
			isopen = true;
			sql = getWritableDatabase();
			Log.d(tag, "open the database...");
		}
		Log.d(tag, "constructor the new content values...");
		ContentValues values = new ContentValues();
		values.put(Constant.BOOK_ID, bm.getBookId());
		values.put(Constant.BOOK_MARK_NAME, bm.getMarkName());

		values.put(Constant.BOOK_MARK_OFFSET, bm.getCurrentOffset());
		values.put(Constant.Book_MARK_SAVETIME, bm.getSaveTime());
		Log.d(tag, "insert ...");
		long x = sql.insert(Constant.BOOK_MARK_TABLE_NAME, null, values);
		if (x > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除书签
	 * 
	 * @param bmId
	 *            书签的ID
	 * @return 删除是否成功
	 */
	public boolean deleteBookMark(int bmId) {
		String tag = "deleteBookMark";
		if (!isopen) {
			isopen = true;
			sql = getWritableDatabase();
			Log.d(tag, "open the database...");
		}
		
		String s = "delete from " + Constant.BOOK_MARK_TABLE_NAME + " where "
				+ Constant.BOOK_MARK_ID + " =" + bmId;
		
		Log.d(tag, s);
		sql.execSQL(s);
		return true;
	}

	/**
	 * 查询某个书籍的所有书签
	 * 
	 * @param bookId
	 * @return
	 */
	public List<BookMark> queryAllBookMark(int bookId) {
		String tag = "queryAllBookMark";
		if (!isopen) {
			isopen = true;
			sql = getWritableDatabase();
			Log.d(tag, "open the database...");
		}
		String[] columns = new String[] { Constant.BOOK_MARK_ID,
				Constant.BOOK_MARK_NAME, Constant.BOOK_MARK_OFFSET ,Constant.Book_MARK_SAVETIME};
		Log.d(tag, "query the book mark from database...");
		Cursor cur = sql.query(Constant.BOOK_MARK_TABLE_NAME, columns,
				Constant.BOOK_ID + "=\"" + bookId + "\"", null, null, null,
				Constant.BOOK_MARK_ID+" desc");
		Log.d(tag, "wrapper the book mark to the list...");
		List<BookMark> list = new ArrayList<BookMark>();
		while (cur.moveToNext()) {
			BookMark b = new BookMark();
			b.setBookMarkId(cur.getInt(0));
			b.setMarkName(cur.getString(1));
			b.setCurrentOffset(cur.getInt(2));
			b.setSaveTime(cur.getString(3));
			list.add(b);
		}

		Log.d(tag, "prepare return the book mark list");
		Log.d(tag, "book mark list size = "+list.size());
		cur.close();
		System.gc();
		return list;
	}

	/**
	 * 清除此书籍在数据库中的数据
	 * 
	 * @param bookName
	 * @return
	 */
	public boolean deleteBook(String bookName) {
		String tag = "deleteBook";
		if (!isopen) {
			isopen = true;
			sql = getWritableDatabase();
		}
		String[] col = new String[] { Constant.BOOK_ID, Constant.BOOK_PATH };
		Cursor cur = sql.query(Constant.BOOK, col, Constant.BOOK_PATH + "=\""
				+ bookName + "\"", null, null, null, null);

		Log.d(tag, "query the book info from the database....");

		int bookid = 0;
		while (cur.moveToNext()) {
			bookid = cur.getInt(0);
		}

		Log.d(tag, "delete all book mark of this book...");
		boolean result = true;
		result = clearAllBookMarkForBook(bookid);// 清除书签
		if (result) {
			//清除书籍
			String s = "delete from " + Constant.BOOK_TABLE_NAME + " where "
					+ Constant.BOOK_ID + "=" + bookid;
			Log.d(tag, s);
			sql.execSQL(s);
		}
		cur.close();
		return result;
	}

	/**
	 * 清除某本书的全部书签
	 * 
	 * @param bookId
	 * @return
	 */
	public boolean clearAllBookMarkForBook(int bookId) {
		String tag = "clearAllBookMarkForBook";
		if (!isopen) {
			isopen = true;
			sql = getWritableDatabase();
			Log.d(tag, "open the database...");
		}

		Log.d(tag, "delete all book mark...");
		String s = "delete from " + Constant.BOOK_MARK_TABLE_NAME + " where "
				+ Constant.BOOK_ID + " =" + bookId;
		Log.d(tag, s);
		sql.execSQL(s);

		return true;
	}

	/** close the database */
	public void close() {
		String tag = "close";
		if (isopen) {
			sql.close();
			isopen = false;
		}
	}

}
