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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import org.geometerplus.fbreader.network.NetworkBookItem;
import org.geometerplus.fbreader.network.NetworkItem;
import org.geometerplus.fbreader.network.NetworkTree;
import org.geometerplus.fbreader.tree.FBTree;


public class NetworkAuthorTree extends NetworkTree {

	public final NetworkBookItem.AuthorData Author;

	private int myBooksNumber;
	private HashMap<String, Integer> mySeriesMap;

	NetworkAuthorTree(NetworkTree parent, NetworkBookItem.AuthorData author) {
		super(parent);
		Author = author;
	}

	@Override
	public String getName() {
		return Author.DisplayName;
	}

	@Override
	protected String getSortKey() {
		return Author.SortKey;
	}

	private int getSeriesIndex(String seriesName) {
		if (mySeriesMap == null) {
			return -1;
		}
		Integer value = mySeriesMap.get(seriesName);
		if (value == null) {
			return -1;
		}
		return value.intValue();
	}

	private void setSeriesIndex(String seriesName, int index) {
		if (mySeriesMap == null) {
			mySeriesMap = new HashMap<String, Integer>();
		}
		mySeriesMap.put(seriesName, Integer.valueOf(index));
	}

	public void updateSubTrees(LinkedList<NetworkBookItem> books) {
		if (myBooksNumber >= books.size()) {
			return;
		}
		invalidateChildren(); // call to update secondString

		ListIterator<NetworkBookItem> booksIterator = books.listIterator(myBooksNumber);
		while (booksIterator.hasNext()) {
			NetworkBookItem book = booksIterator.next();

			if (book.SeriesTitle != null) {
				final int seriesPosition = getSeriesIndex(book.SeriesTitle);
				if (seriesPosition == -1) {
					final int insertAt = subTrees().size();
					setSeriesIndex(book.SeriesTitle, insertAt);
					new NetworkBookTree(this, book, false);
				} else {
					FBTree treeAtSeriesPosition = subTrees().get(seriesPosition);
					if (treeAtSeriesPosition instanceof NetworkBookTree) {
						final NetworkBookTree bookTree = (NetworkBookTree) treeAtSeriesPosition;
						bookTree.removeSelf();
						final NetworkSeriesTree seriesTree = new NetworkSeriesTree(this, book.SeriesTitle, seriesPosition, false);
						new NetworkBookTree(seriesTree, bookTree.Book, false);
						treeAtSeriesPosition = seriesTree;
					}

					if (!(treeAtSeriesPosition instanceof NetworkSeriesTree)) {
						throw new RuntimeException("That's impossible!!!");
					}
					final NetworkSeriesTree seriesTree = (NetworkSeriesTree) treeAtSeriesPosition;
					ListIterator<FBTree> nodesIterator = seriesTree.subTrees().listIterator();
					int insertAt = 0;
					while (nodesIterator.hasNext()) {
						FBTree tree = nodesIterator.next();
						if (!(tree instanceof NetworkBookTree)) {
							throw new RuntimeException("That's impossible!!!");
						}
						NetworkBookTree bookTree = (NetworkBookTree) tree;
						if (bookTree.Book.IndexInSeries > book.IndexInSeries) {
							break;
						}
						++insertAt;
					}
					seriesTree.invalidateChildren(); // call to update secondString
					new NetworkBookTree(seriesTree, book, insertAt, false);
				}
			} else {
				new NetworkBookTree(this, book, false);
			}
		}

		myBooksNumber = books.size();
	}

	@Override
	public NetworkItem getHoldedItem() {
		return null;
	}

	@Override
	protected String getStringId() {
		return "@Author:" + Author.DisplayName + ":" + Author.SortKey;
	}
}
