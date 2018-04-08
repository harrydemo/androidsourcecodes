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

import java.util.List;
import java.util.Map;

import org.geometerplus.fbreader.network.opds.OPDSCustomNetworkLink;
import org.geometerplus.fbreader.network.opds.OPDSPredefinedNetworkLink;
import org.geometerplus.fbreader.network.urlInfo.UrlInfo;
import org.geometerplus.fbreader.network.urlInfo.UrlInfoCollection;
import org.geometerplus.fbreader.network.urlInfo.UrlInfoWithDate;

public abstract class NetworkDatabase {
	private static NetworkDatabase ourInstance;

	public static NetworkDatabase Instance() {
		return ourInstance;
	}

	protected NetworkDatabase() {
		ourInstance = this;
	}

	protected abstract void executeAsATransaction(Runnable actions);

	protected INetworkLink createLink(int id, String predefinedId, String siteName, String title, String summary, String language, UrlInfoCollection<UrlInfoWithDate> infos) {
		if (siteName == null || title == null || infos.getInfo(UrlInfo.Type.Catalog) == null) {
			return null;
		}
		return
			predefinedId != null
			? new OPDSPredefinedNetworkLink(id, predefinedId, siteName, title, summary, language, infos)
			: new OPDSCustomNetworkLink(id, siteName, title, summary, language, infos);
	}

	protected abstract List<INetworkLink> listLinks();
	protected abstract void saveLink(INetworkLink link);
	protected abstract void deleteLink(INetworkLink link);

	protected abstract Map<String,String> getLinkExtras(INetworkLink link);
	protected abstract void setLinkExtras(INetworkLink link, Map<String,String> extras);
}
