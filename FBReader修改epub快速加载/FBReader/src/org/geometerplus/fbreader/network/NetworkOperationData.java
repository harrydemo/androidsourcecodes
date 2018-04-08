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

import org.geometerplus.zlibrary.core.network.ZLNetworkRequest;


public class NetworkOperationData {

	public interface OnNewItemListener {
		void onNewItem(INetworkLink link, NetworkItem item);

		void commitItems(INetworkLink link);

		/**
		 * @return <code>true</code> to confirm interrupt reading; 
		 *         <code>false</code> to continue reading.
		 *         Once <code>true</code> has been returned,
		 *         all next calls MUST return <code>true</code>.
		 */
		boolean confirmInterrupt();
	}

	public final INetworkLink Link;
	public OnNewItemListener Listener;
	public String ResumeURI;

	public NetworkOperationData(INetworkLink link, OnNewItemListener listener) {
		Link = link;
		Listener = listener;
	}

	protected void clear() {
		ResumeURI = null;
	}

	public final ZLNetworkRequest resume() {
		final ZLNetworkRequest request = Link.resume(this);
		clear();
		return request;
	}
}
