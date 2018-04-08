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

package org.geometerplus.fbreader.network.authentication;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geometerplus.fbreader.network.INetworkLink;
import org.geometerplus.fbreader.network.NetworkBookItem;
import org.geometerplus.fbreader.network.NetworkException;
import org.geometerplus.fbreader.network.authentication.litres.LitResAuthenticationManager;
import org.geometerplus.fbreader.network.opds.OPDSNetworkLink;
import org.geometerplus.fbreader.network.urlInfo.BookUrlInfo;
import org.geometerplus.zlibrary.core.network.ZLNetworkException;
import org.geometerplus.zlibrary.core.options.ZLStringOption;

public abstract class NetworkAuthenticationManager {
	private static final HashMap<String, NetworkAuthenticationManager> ourManagers = new HashMap<String, NetworkAuthenticationManager>();

	public static NetworkAuthenticationManager createManager(INetworkLink link, String sslCertificate, Class<? extends NetworkAuthenticationManager> managerClass) {
		NetworkAuthenticationManager mgr = ourManagers.get(link.getSiteName());
		if (mgr == null) {
			if (managerClass == LitResAuthenticationManager.class) {
				mgr = new LitResAuthenticationManager((OPDSNetworkLink)link);
			}
			if (mgr != null) {
				ourManagers.put(link.getSiteName(), mgr);
			}
		}
		return mgr;
	}


	public final INetworkLink Link;
	public final ZLStringOption UserNameOption;
	public final String SSLCertificate;

	protected NetworkAuthenticationManager(INetworkLink link, String sslCertificate) {
		Link = link;
		UserNameOption = new ZLStringOption(link.getSiteName(), "userName", "");
		SSLCertificate = sslCertificate;
	}

	/*
	 * Common manager methods
	 */
	public abstract boolean isAuthorised(boolean useNetwork /* = true */) throws ZLNetworkException;
	public abstract void authorise(String password) throws ZLNetworkException;
	public abstract void logOut();
	public abstract BookUrlInfo downloadReference(NetworkBookItem book);

	public final boolean mayBeAuthorised(boolean useNetwork) {
		try {
			return isAuthorised(useNetwork);
		} catch (ZLNetworkException e) {
		}
		return true;
	}

	/*
	 * Account specific methods (can be called only if authorised!!!)
	 */
	public abstract String currentUserName();

	public boolean needsInitialization() {
		return false;
	}

	public void initialize() throws ZLNetworkException {
		throw new ZLNetworkException(NetworkException.ERROR_UNSUPPORTED_OPERATION);
	}

	// returns true if link must be purchased before downloading
	public boolean needPurchase(NetworkBookItem book) {
		return true;
	}

	public void purchaseBook(NetworkBookItem book) throws ZLNetworkException {
		throw new ZLNetworkException(NetworkException.ERROR_UNSUPPORTED_OPERATION);
	}

	public List<NetworkBookItem> purchasedBooks() {
		return Collections.emptyList();
	}

	public String currentAccount() {
		return null;
	}

	//public abstract ZLNetworkSSLCertificate certificate();

	/*
	 * topup account
	 */

	public String topupLink() {
		return null;
	}
	public Map<String,String> getTopupData() {
		return Collections.emptyMap();
	}

	/*
	 * Password Recovery
	 */
	public boolean passwordRecoverySupported() {
		return false;
	}

	public void recoverPassword(String email) throws ZLNetworkException {
		throw new ZLNetworkException(NetworkException.ERROR_UNSUPPORTED_OPERATION);
	}
}
