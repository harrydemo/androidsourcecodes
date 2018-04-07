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

package org.geometerplus.fbreader.network.urlInfo;

import java.util.Date;

import org.geometerplus.zlibrary.core.util.ZLMiscUtil;

public final class UrlInfoWithDate extends UrlInfo {
	private static final long serialVersionUID = -896768978957787222L;

	public static final UrlInfoWithDate NULL = new UrlInfoWithDate(null, null);

	public final Date Updated;

	public UrlInfoWithDate(Type type, String url, Date updated) {
		super(type, url);
		Updated = updated;
	}

	public UrlInfoWithDate(Type type, String url) {
		this(type, url, new Date());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof UrlInfoWithDate)) {
			return false;
		}

		final UrlInfoWithDate info = (UrlInfoWithDate)o;
		return
			InfoType == info.InfoType &&
			ZLMiscUtil.equals(Url, info.Url) &&
			ZLMiscUtil.equals(Updated, info.Updated);
	}

	@Override
	public int hashCode() {
		return InfoType.hashCode() + ZLMiscUtil.hashCode(Url) + ZLMiscUtil.hashCode(Updated);
	}
}
