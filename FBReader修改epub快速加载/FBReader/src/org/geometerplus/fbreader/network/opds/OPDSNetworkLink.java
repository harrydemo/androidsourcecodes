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

package org.geometerplus.fbreader.network.opds;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.geometerplus.fbreader.network.AbstractNetworkLink;
import org.geometerplus.fbreader.network.NetworkCatalogItem;
import org.geometerplus.fbreader.network.NetworkOperationData;
import org.geometerplus.fbreader.network.authentication.NetworkAuthenticationManager;
import org.geometerplus.fbreader.network.urlInfo.UrlInfo;
import org.geometerplus.fbreader.network.urlInfo.UrlInfoCollection;
import org.geometerplus.fbreader.network.urlInfo.UrlInfoWithDate;
import org.geometerplus.zlibrary.core.network.ZLNetworkException;
import org.geometerplus.zlibrary.core.network.ZLNetworkRequest;
import org.geometerplus.zlibrary.core.util.MimeType;

public abstract class OPDSNetworkLink extends AbstractNetworkLink {
	private TreeMap<RelationAlias,String> myRelationAliases;

	private final LinkedList<URLRewritingRule> myUrlRewritingRules = new LinkedList<URLRewritingRule>();
	private final Map<String,String> myExtraData = new HashMap<String,String>();
	private NetworkAuthenticationManager myAuthenticationManager;

	OPDSNetworkLink(int id, String siteName, String title, String summary, String language,
			UrlInfoCollection<UrlInfoWithDate> infos) {
		super(id, siteName, title, summary, language, infos);
	}

	final void setRelationAliases(Map<RelationAlias, String> relationAliases) {
		if (relationAliases != null && relationAliases.size() > 0) {
			myRelationAliases = new TreeMap<RelationAlias, String>(relationAliases);
		} else {
			myRelationAliases = null;
		}
	}

	final void setUrlRewritingRules(List<URLRewritingRule> rules) {
		myUrlRewritingRules.clear();
		myUrlRewritingRules.addAll(rules);
	}

	final void setExtraData(Map<String,String> extraData) {
		myExtraData.clear();
		myExtraData.putAll(extraData);
	}

	final void setAuthenticationManager(NetworkAuthenticationManager mgr) {
		myAuthenticationManager = mgr;
	}

	ZLNetworkRequest createNetworkData(final OPDSCatalogItem catalog, String url, final OPDSCatalogItem.State result) {
		if (url == null) {
			return null;
		}
		url = rewriteUrl(url, false);
		return new ZLNetworkRequest(url) {
			@Override
			public void handleStream(InputStream inputStream, int length) throws IOException, ZLNetworkException {
				if (result.Listener.confirmInterrupt()) {
					return;
				}

				new OPDSXMLReader(
					new OPDSFeedHandler(catalog, getURL(), result), false
				).read(inputStream);

				if (result.Listener.confirmInterrupt()) {
					if (result.LastLoadedId != null) {
						// reset state to load current page from the beginning 
						result.LastLoadedId = null;
					} else {
						result.Listener.commitItems(OPDSNetworkLink.this);
					}
				} else {
					result.Listener.commitItems(OPDSNetworkLink.this);
				}
			}
		};
	}

	@Override
	public OPDSCatalogItem.State createOperationData(NetworkOperationData.OnNewItemListener listener) {
		return new OPDSCatalogItem.State(this, listener);
	}

	public ZLNetworkRequest simpleSearchRequest(String pattern, NetworkOperationData data) {
		final String url = getUrl(UrlInfo.Type.Search);
		if (url == null) {
			return null;
		}
		try {
			pattern = URLEncoder.encode(pattern, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return createNetworkData(null, url.replace("%s", pattern), (OPDSCatalogItem.State)data);
	}

	public ZLNetworkRequest resume(NetworkOperationData data) {
		return createNetworkData(null, data.ResumeURI, (OPDSCatalogItem.State) data);
	}

	public NetworkCatalogItem libraryItem() {
		final UrlInfoCollection<UrlInfo> urlMap = new UrlInfoCollection<UrlInfo>();
		urlMap.addInfo(getUrlInfo(UrlInfo.Type.Catalog));
		urlMap.addInfo(getUrlInfo(UrlInfo.Type.Image));
		urlMap.addInfo(getUrlInfo(UrlInfo.Type.Thumbnail));
		return new OPDSCatalogItem(this, getTitle(), getSummary(), urlMap, myExtraData);
	}

	public NetworkAuthenticationManager authenticationManager() {
		return myAuthenticationManager;
	}

	public String rewriteUrl(String url, boolean isUrlExternal) {
		final int apply = isUrlExternal
			? URLRewritingRule.APPLY_EXTERNAL : URLRewritingRule.APPLY_INTERNAL;
		for (URLRewritingRule rule: myUrlRewritingRules) {
			if ((rule.whereToApply() & apply) != 0) {
				url = rule.apply(url);
			}
		}
		return url;
	}

	// rel and type must be either null or interned String objects.
	String relation(String rel, MimeType type) {
		if (myRelationAliases == null) {
			return rel;
		}
		RelationAlias alias = new RelationAlias(rel, type.Name);
		String mapped = myRelationAliases.get(alias);
		if (mapped != null) {
			return mapped;
		}
		if (type != null) {
			alias = new RelationAlias(rel, null);
			mapped = myRelationAliases.get(alias);
			if (mapped != null) {
				return mapped;
			}
		}
		return rel;
	}

	@Override
	public String toString() {
		return "OPDSNetworkLink: {super=" + super.toString()
			+ "; authManager=" + (myAuthenticationManager != null ? myAuthenticationManager.getClass().getName() : null)
			+ "; relationAliases=" + myRelationAliases
			+ "; rewritingRules=" + myUrlRewritingRules
			+ "}";
	}
}
