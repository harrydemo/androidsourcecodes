/*
 * Copyright (C) 2010-2012 Geometer Plus <contact@geometerplus.com>
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

import java.util.LinkedList;

import org.geometerplus.zlibrary.core.network.ZLNetworkManager;
import org.geometerplus.zlibrary.core.network.ZLNetworkRequest;
import org.geometerplus.zlibrary.core.network.ZLNetworkException;

import org.geometerplus.fbreader.network.tree.NetworkItemsLoader;

public class AllCatalogsSearchItem extends SearchItem {
	public AllCatalogsSearchItem() {
		super(
			null,
			NetworkLibrary.resource().getResource("search").getResource("summaryAllCatalogs").getValue()
		);
	}

	@Override
	public void runSearch(NetworkItemsLoader loader, String pattern) throws ZLNetworkException {
		final LinkedList<ZLNetworkRequest> requestList = new LinkedList<ZLNetworkRequest>();
		final LinkedList<NetworkOperationData> dataList = new LinkedList<NetworkOperationData>();

		for (INetworkLink link : NetworkLibrary.Instance().activeLinks()) {
			final NetworkOperationData data = link.createOperationData(loader);
			final ZLNetworkRequest request = link.simpleSearchRequest(pattern, data);
			if (request != null) {
				dataList.add(data);
				requestList.add(request);
			}
		}

		while (!requestList.isEmpty()) {
			ZLNetworkManager.Instance().perform(requestList);

			requestList.clear();

			if (loader.confirmInterruption()) {
				return;
			}
			for (NetworkOperationData data : dataList) {
				ZLNetworkRequest request = data.resume();
				if (request != null) {
					requestList.add(request);
				}
			}
		}
	}
}
