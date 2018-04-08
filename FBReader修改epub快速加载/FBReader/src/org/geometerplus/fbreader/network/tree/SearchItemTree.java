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

package org.geometerplus.fbreader.network.tree;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

import org.geometerplus.fbreader.network.NetworkBookItem;
import org.geometerplus.fbreader.network.NetworkItem;
import org.geometerplus.fbreader.network.NetworkLibrary;
import org.geometerplus.fbreader.network.NetworkTree;
import org.geometerplus.fbreader.network.SearchResult;
import org.geometerplus.fbreader.tree.FBTree;

public class SearchItemTree extends NetworkTree {
	private SearchResult myResult;

	public SearchItemTree(NetworkTree parent, int position) {
		super(parent, position);
	}

	@Override
	public String getName() {
		return NetworkLibrary.resource().getResource("search").getValue();
	}

	@Override
	public String getSummary() {
		return NetworkLibrary.resource().getResource("searchSummary").getValue();
	}

	@Override
	public String getTreeTitle() {
		if (myResult != null) {
			return myResult.Summary;
		}
		return getName();
	}

	public void setSearchResult(SearchResult result) {
		myResult = result;
		clear();
	}

	public SearchResult getSearchResult() {
		return myResult;
	}

	public void updateSubTrees() {
		ListIterator<FBTree> nodeIterator = subTrees().listIterator();

		final Set<NetworkBookItem.AuthorData> authorsSet = myResult.BooksMap.keySet();

		for (NetworkBookItem.AuthorData author: authorsSet) {
			if (nodeIterator != null) {
				if (nodeIterator.hasNext()) {
					FBTree currentNode = nodeIterator.next();
					if (!(currentNode instanceof NetworkAuthorTree)) {
						throw new RuntimeException("That's impossible!!!");
					}
					NetworkAuthorTree child = (NetworkAuthorTree)currentNode;
					if (!child.Author.equals(author)) {
						throw new RuntimeException("That's impossible!!!");
					}
					LinkedList<NetworkBookItem> authorBooks = myResult.BooksMap.get(author);
					child.updateSubTrees(authorBooks);
					continue;
				}
				nodeIterator = null;
			}

			LinkedList<NetworkBookItem> authorBooks = myResult.BooksMap.get(author);
			if (authorBooks.size() != 0) {
				NetworkAuthorTree child = new NetworkAuthorTree(this, author);
				child.updateSubTrees(authorBooks);
			}
		}
		if (nodeIterator != null && nodeIterator.hasNext()) {
			throw new RuntimeException("That's impossible!!!");
		}
	}

	@Override
	public NetworkItem getHoldedItem() {
		return null;
	}

	@Override
	protected String getStringId() {
		return "@Search";
	}

	@Override
	public int compareTo(FBTree tree) {
		if (!(tree instanceof SearchItemTree)) {
			return -1;
		}
		return 0;
	}
}
