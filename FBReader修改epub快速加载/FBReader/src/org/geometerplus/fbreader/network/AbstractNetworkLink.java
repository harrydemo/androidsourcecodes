/*
 * Copyright (C) 2010-2011 Geometer Plus <contact@geometerplus.com>
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

package org.geometerplus.fbreader.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.geometerplus.fbreader.network.urlInfo.UrlInfo;
import org.geometerplus.fbreader.network.urlInfo.UrlInfoCollection;
import org.geometerplus.fbreader.network.urlInfo.UrlInfoWithDate;
import org.geometerplus.zlibrary.core.language.ZLLanguageUtil;
import org.geometerplus.zlibrary.core.options.ZLStringListOption;

public abstract class AbstractNetworkLink implements INetworkLink, Basket {
	private int myId;

	protected String mySiteName;
	protected String myTitle;
	protected String mySummary;
	protected final String myLanguage;
	protected final UrlInfoCollection<UrlInfoWithDate> myInfos;

	private ZLStringListOption myBooksInBasketOption;

	/**
	 * Creates new NetworkLink instance.
	 *
	 * @param siteName   name of the corresponding website. Must be not <code>null</code>.
	 * @param title      title of the corresponding library item. Must be not <code>null</code>.
	 * @param summary    description of the corresponding library item. Can be <code>null</code>.
	 * @param language   language of the catalog. If <code>null</code> we assume this catalog is multilanguage.
	 * @param infos      collection of URL infos; must always contain one URL with <code>UrlInfo.Type.Catalog</code> identifier
	 */
	public AbstractNetworkLink(int id, String siteName, String title, String summary, String language, UrlInfoCollection<UrlInfoWithDate> infos) {
		myId = id;
		mySiteName = siteName;
		myTitle = title;
		mySummary = summary;
		myLanguage = language != null ? language : "multi";
		myInfos = new UrlInfoCollection<UrlInfoWithDate>(infos);
	}

	public int getId() {
		return myId;
	}

	public void setId(int id) {
		myId = id;
	}

	public final String getSiteName() {
		return mySiteName;
	}

	public final String getTitle() {
		return myTitle;
	}

	public final String getSummary() {
		return mySummary;
	}

	public final String getLanguage() {
		return myLanguage;
	}

	public final UrlInfoCollection<UrlInfoWithDate> urlInfoMap() {
		return new UrlInfoCollection<UrlInfoWithDate>(myInfos);
	}

	public final String getUrl(UrlInfo.Type type) {
		return getUrlInfo(type).Url;
	}

	public final UrlInfoWithDate getUrlInfo(UrlInfo.Type type) {
		final UrlInfoWithDate info = myInfos.getInfo(type);
		return info != null ? info : UrlInfoWithDate.NULL;
	}

	public final Set<UrlInfo.Type> getUrlKeys() {
		final HashSet<UrlInfo.Type> set = new HashSet<UrlInfo.Type>();
		for (UrlInfo info : myInfos.getAllInfos()) {
			set.add(info.InfoType);
		}
		return set;
	}

	public final void setSupportsBasket() {
		if (myBooksInBasketOption == null) {
			myBooksInBasketOption = new ZLStringListOption(mySiteName, "Basket", null);
		}
	}

	public final Basket basket() {
		return myBooksInBasketOption != null ? this : null;
	}

	// method from Basket interface
	public final void add(NetworkBookItem book) {
		if (book.Id != null && !"".equals(book.Id)) {
			List<String> ids = myBooksInBasketOption.getValue();
			if (!ids.contains(book.Id)) {
				ids = new ArrayList<String>(ids);
				ids.add(book.Id);
				myBooksInBasketOption.setValue(ids);
			}
		}
	}

	// method from Basket interface
	public final void remove(NetworkBookItem book) {
		if (book.Id != null && !"".equals(book.Id)) {
			List<String> ids = myBooksInBasketOption.getValue();
			if (ids.contains(book.Id)) {
				ids = new ArrayList<String>(ids);
				ids.remove(book.Id);
				myBooksInBasketOption.setValue(ids);
			}
		}
	}

	// method from Basket interface
	public final void clear() {
		myBooksInBasketOption.setValue(null);
	}

	// method from Basket interface
	public final boolean contains(NetworkBookItem book) {
		return myBooksInBasketOption.getValue().contains(book.Id);
	}

	// method from Basket interface
	public final List<String> bookIds() {
		return myBooksInBasketOption.getValue();
	}

	public NetworkOperationData createOperationData(NetworkOperationData.OnNewItemListener listener) {
		return new NetworkOperationData(this, listener);
	}

	@Override
	public String toString() {
		String icon = getUrl(UrlInfo.Type.Catalog);
		if (icon != null) {
			if (icon.length() > 64) {
				icon = icon.substring(0, 61) + "...";
			}
			icon = icon.replaceAll("\n", "");
		}
		return "AbstractNetworkLink: {"
			+ "siteName=" + mySiteName
			+ "; title=" + myTitle
			+ "; summary=" + mySummary
			+ "; icon=" + icon
			+ "; infos=" + myInfos
			+ "}";
	}

	private String getTitleForComparison() {
		String title = getTitle();
		for (int index = 0; index < title.length(); ++index) {
			final char ch = title.charAt(index);
			if (ch < 128 && Character.isLetter(ch)) {
				return title.substring(index);
			}
		}
		return title;
	}

	private static int getLanguageOrder(String language) {
		if (language == ZLLanguageUtil.MULTI_LANGUAGE_CODE) {
			return 1;
		}
		if (language.equals(Locale.getDefault().getLanguage())) {
			return 0;
		}
		return 2;
	}

	public int compareTo(INetworkLink link) {
		final int diff = getLanguageOrder(getLanguage()) - getLanguageOrder(link.getLanguage());
		if (diff != 0) {
			return diff;
		}
		return getTitleForComparison().compareToIgnoreCase(((AbstractNetworkLink)link).getTitleForComparison());
	}
}
