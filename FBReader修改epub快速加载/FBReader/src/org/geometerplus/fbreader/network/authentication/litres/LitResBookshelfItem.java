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

package org.geometerplus.fbreader.network.authentication.litres;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.geometerplus.fbreader.network.INetworkLink;
import org.geometerplus.fbreader.network.NetworkBookItem;
import org.geometerplus.fbreader.network.NetworkBookItemComparator;
import org.geometerplus.fbreader.network.NetworkCatalogItem;
import org.geometerplus.fbreader.network.NetworkException;
import org.geometerplus.fbreader.network.NetworkItem;
import org.geometerplus.fbreader.network.NetworkLibrary;
import org.geometerplus.fbreader.network.NetworkOperationData;
import org.geometerplus.fbreader.network.NetworkURLCatalogItem;
import org.geometerplus.fbreader.network.urlInfo.UrlInfoCollection;
import org.geometerplus.zlibrary.core.network.ZLNetworkException;
import org.geometerplus.zlibrary.core.resources.ZLResource;

abstract class SortedCatalogItem extends NetworkCatalogItem {
	private final List<NetworkItem> myChildren = new LinkedList<NetworkItem>();

	private SortedCatalogItem(NetworkCatalogItem parent, ZLResource resource, List<NetworkItem> children, int flags) {
		super(parent.Link, resource.getValue(), resource.getResource("summary").getValue(), null, Accessibility.ALWAYS, flags);
		for (NetworkItem child : children) {
			if (accepts(child)) {
				myChildren.add(child);
			}
		}
		final Comparator<NetworkItem> comparator = getComparator();
		if (comparator != null) {
			Collections.sort(myChildren, comparator);
		}
	}

	public boolean isEmpty() {
		return myChildren.isEmpty();
	}

	protected abstract Comparator<NetworkItem> getComparator();
	protected boolean accepts(NetworkItem item) {
		return item instanceof NetworkBookItem;
	}

	public SortedCatalogItem(NetworkCatalogItem parent, String resourceKey, List<NetworkItem> children, int flags) {
		this(parent, NetworkLibrary.resource().getResource(resourceKey), children, flags);
	}

	@Override
	public void onDisplayItem() {
	}

	@Override
	public void loadChildren(NetworkOperationData.OnNewItemListener listener) throws ZLNetworkException {
		for (NetworkItem child : myChildren) {
			listener.onNewItem(Link, child);
		}
		listener.commitItems(Link);
	}
}

class ByAuthorCatalogItem extends SortedCatalogItem {
	ByAuthorCatalogItem(NetworkCatalogItem parent, List<NetworkItem> children) {
		super(parent, "byAuthor", children, FLAG_GROUP_BY_AUTHOR);
	}

	@Override
	protected Comparator<NetworkItem> getComparator() {
		return new NetworkBookItemComparator();
	}

	@Override
	public String getStringId() {
		return "@ByAuthor";
	}
}

class ByTitleCatalogItem extends SortedCatalogItem {
	ByTitleCatalogItem(NetworkCatalogItem parent, List<NetworkItem> children) {
		super(parent, "byTitle", children, FLAG_SHOW_AUTHOR);
	}

	@Override
	protected Comparator<NetworkItem> getComparator() {
		return new Comparator<NetworkItem>() {
			public int compare(NetworkItem item0, NetworkItem item1) {
				return item0.Title.toString().compareTo(item1.Title.toString());
			}
		};
	}

	@Override
	public String getStringId() {
		return "@ByTitle";
	}
}

class ByDateCatalogItem extends SortedCatalogItem {
	ByDateCatalogItem(NetworkCatalogItem parent, List<NetworkItem> children) {
		super(parent, "byDate", children, FLAG_SHOW_AUTHOR);
	}

	@Override
	protected Comparator<NetworkItem> getComparator() {
		return null;
	}

	@Override
	public String getStringId() {
		return "@ByDate";
	}
}

class BySeriesCatalogItem extends SortedCatalogItem {
	BySeriesCatalogItem(NetworkCatalogItem parent, List<NetworkItem> children) {
		super(parent, "bySeries", children, FLAG_SHOW_AUTHOR | FLAG_GROUP_BY_SERIES);
	}

	@Override
	protected Comparator<NetworkItem> getComparator() {
		return new Comparator<NetworkItem>() {
			public int compare(NetworkItem item0, NetworkItem item1) {
				final NetworkBookItem book0 = (NetworkBookItem)item0;
				final NetworkBookItem book1 = (NetworkBookItem)item1;
				final int diff = book0.SeriesTitle.compareTo(book1.SeriesTitle);
				if (diff != 0) {
					return diff;
				}
				final float fdiff = book0.IndexInSeries - book1.IndexInSeries;
				if (fdiff != 0) {
					return fdiff > 0 ? 1 : -1;
				}
				return book0.Title.toString().compareTo(book1.Title.toString());
			}
		};
	}

	@Override
	protected boolean accepts(NetworkItem item) {
		return
			item instanceof NetworkBookItem &&
			((NetworkBookItem)item).SeriesTitle != null;
	}

	@Override
	public String getStringId() {
		return "@BySeries";
	}
}

public class LitResBookshelfItem extends NetworkURLCatalogItem {
	private boolean myForceReload;

	public LitResBookshelfItem(INetworkLink link, CharSequence title, CharSequence summary, UrlInfoCollection<?> urls) {
		super(link, title, summary, urls, Accessibility.SIGNED_IN, FLAGS_DEFAULT);
	}

	@Override
	public void onDisplayItem() {
		myForceReload = false;
	}

	@Override
	public void loadChildren(NetworkOperationData.OnNewItemListener listener) throws ZLNetworkException {
		final LitResAuthenticationManager mgr =
			(LitResAuthenticationManager)Link.authenticationManager();

		// TODO: Maybe it's better to call isAuthorised(true) directly 
		// and let exception fly through???
		if (!mgr.mayBeAuthorised(true)) {
			throw new ZLNetworkException(NetworkException.ERROR_AUTHENTICATION_FAILED);
		}
		try {
			if (myForceReload) {
				mgr.reloadPurchasedBooks();
			}
		} finally {
			myForceReload = true;
			// TODO: implement asynchronous loading
			ArrayList<NetworkItem> children =
				new ArrayList<NetworkItem>(mgr.purchasedBooks());
			if (children.size() <= 5) {
				Collections.sort(children, new NetworkBookItemComparator());
				for (NetworkItem item : children) {
					listener.onNewItem(Link, item);
				}
			} else {
				listener.onNewItem(Link, new ByDateCatalogItem(this, children));
				listener.onNewItem(Link, new ByAuthorCatalogItem(this, children));
				listener.onNewItem(Link, new ByTitleCatalogItem(this, children));
				final BySeriesCatalogItem bySeries = new BySeriesCatalogItem(this, children);
				if (!bySeries.isEmpty()) {
					listener.onNewItem(Link, bySeries);
				}
			}
			listener.commitItems(Link);
		}
	}
}
