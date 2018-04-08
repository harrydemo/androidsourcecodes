/*
 * Copyright (C) 2009-2011 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader.preferences;

import java.util.TreeSet;

import org.geometerplus.android.fbreader.FBReader;
import org.geometerplus.android.fbreader.library.BookInfoActivity;
import org.geometerplus.android.fbreader.library.SQLiteBooksDatabase;
import org.geometerplus.fbreader.library.Book;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.language.ZLLanguageUtil;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.text.hyphenation.ZLTextHyphenator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

class BookTitlePreference extends ZLStringPreference {
	private final Book myBook;

	BookTitlePreference(Context context, ZLResource rootResource, String resourceKey, Book book) {
		super(context, rootResource, resourceKey);
		myBook = book;
		setValue(book.getTitle());
	}

	public void onAccept() {
		myBook.setTitle(getValue());
	}
}

class LanguagePreference extends ZLStringListPreference {
	private final Book myBook;

	LanguagePreference(Context context, ZLResource rootResource, String resourceKey, Book book) {
		super(context, rootResource, resourceKey);
		myBook = book;
		final TreeSet<String> set = new TreeSet<String>(new ZLLanguageUtil.CodeComparator());
		set.addAll(ZLTextHyphenator.Instance().languageCodes());
		set.add(ZLLanguageUtil.OTHER_LANGUAGE_CODE);

		final int size = set.size();
		String[] codes = new String[size];
		String[] names = new String[size];
		int index = 0;
		for (String code : set) {
			codes[index] = code;
			names[index] = ZLLanguageUtil.languageName(code);
			++index;
		}
		setLists(codes, names);
		String language = myBook.getLanguage();
		if (language == null) {
			language = ZLLanguageUtil.OTHER_LANGUAGE_CODE;
		}
		if (!setInitialValue(language)) {
			setInitialValue(ZLLanguageUtil.OTHER_LANGUAGE_CODE);
		}
	}

	public void onAccept() {
		final String value = getValue();
		myBook.setLanguage((value.length() != 0) ? value : null);
	}
}

class EncodingPreference extends ZLStringListPreference {
	private final Activity myActivity;
	private final Book myBook;

	EncodingPreference(Activity activity, ZLResource rootResource, String resourceKey, Book book) {
		super(activity, rootResource, resourceKey);
		myActivity = activity;
		myBook = book;
		final TreeSet<String> set = new TreeSet<String>();
		String encoding = myBook.getEncoding();
		if (encoding == null) {
			encoding = "auto";
		}
		if (!"auto".equals(encoding)) {
			set.add("utf-8");
			set.add("windows-1251");
			set.add("windows-1252");
			set.add("koi8-r");
		}
		set.add(encoding);
		String[] names = new String[set.size()];
		int index = 0;
		for (String e : set) {
			names[index++] = e;
		}
		setLists(names, names);
		setInitialValue(encoding);
	}

	@Override
	protected void onDialogClosed(boolean result) {
		super.onDialogClosed(result);
		if (result) {
			final String value = getValue();
			setSummary(value);
			if (!"auto".equals(value) && !value.equals(myBook.getEncoding())) {
				myBook.setEncoding(value);
				myActivity.setResult(FBReader.RESULT_RELOAD_BOOK);
			}
		}
	}

	public void onAccept() {
	}
}

public class EditBookInfoActivity extends ZLPreferenceActivity {
	private Book myBook;

	public EditBookInfoActivity() {
		super("BookInfo");
	}

	@Override
	protected void init(Intent intent) {
		if (SQLiteBooksDatabase.Instance() == null) {
			new SQLiteBooksDatabase(this, "LIBRARY");
		}

		final String path = intent.getStringExtra(BookInfoActivity.CURRENT_BOOK_PATH_KEY);
		final ZLFile file = ZLFile.createFileByPath(path);
		myBook = Book.getByFile(file);
		setResult(FBReader.RESULT_REPAINT);

		if (myBook == null) {
			finish();
			return;
		}

		addPreference(new BookTitlePreference(this, Resource, "title", myBook));
		addPreference(new LanguagePreference(this, Resource, "language", myBook));
		addPreference(new EncodingPreference(this, Resource, "encoding", myBook));
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (myBook != null) {
			myBook.save();
		}
	}
}
