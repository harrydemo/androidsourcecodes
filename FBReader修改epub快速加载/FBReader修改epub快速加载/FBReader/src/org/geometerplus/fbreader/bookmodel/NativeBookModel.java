/*
 * Copyright (C) 2011 Geometer Plus <contact@geometerplus.com>
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

package org.geometerplus.fbreader.bookmodel;

import java.util.ArrayList;
import java.util.HashMap;

import org.geometerplus.fbreader.library.Book;
import org.geometerplus.zlibrary.core.image.ZLImageMap;
import org.geometerplus.zlibrary.text.model.CachedInputCharStorage;
import org.geometerplus.zlibrary.text.model.CharStorage;
import org.geometerplus.zlibrary.text.model.ZLCachedImageMap;
import org.geometerplus.zlibrary.text.model.ZLTextModel;
import org.geometerplus.zlibrary.text.model.ZLTextNativeModel;
import org.geometerplus.zlibrary.text.model.ZLTextParagraph;

public class NativeBookModel extends BookModel {

	private ZLImageMap myImageMap;
	//脚注
	private final HashMap<String,ZLTextModel> myFootnotes = new HashMap<String,ZLTextModel>();

	private ZLTextModel myBookTextModel;

	//超链接
	private CharStorage myInternalHyperlinks;

	NativeBookModel(Book book) {
		super(book);
	}

	public void initBookModel(String[] imageIds, int[] imageIndices, int[] imageOffsets,
			String imageDirectoryName, String imageFileExtension, int imageBlocksNumber) {
		myImageMap = new ZLCachedImageMap(imageIds, imageIndices, imageOffsets, imageDirectoryName, imageFileExtension, imageBlocksNumber);
	}

	public void initInternalHyperlinks(String directoryName, String fileExtension, int blocksNumber) {
		myInternalHyperlinks = new CachedInputCharStorage(directoryName, fileExtension, blocksNumber);
	}

	public void initTOC(ZLTextModel contentsModel, int[] childrenNumbers, int[] referenceNumbers) {
		StringBuilder buffer = new StringBuilder();

		ArrayList<Integer> positions = new ArrayList<Integer>();
		TOCTree tree = TOCTree;

		final int size = contentsModel.getParagraphsNumber();
		for (int pos = 0; pos < size; ++pos) {
			positions.add(pos);
			ZLTextParagraph par = contentsModel.getParagraph(pos);

			buffer.delete(0, buffer.length());
			ZLTextParagraph.EntryIterator it = par.iterator();
			while (it.hasNext()) {
				it.next();
				if (it.getType() == ZLTextParagraph.Entry.TEXT) {
					buffer.append(it.getTextData(), it.getTextOffset(), it.getTextLength());
				}
			}

			tree = new TOCTree(tree);
			tree.setText(buffer.toString());
			tree.setReference(myBookTextModel, referenceNumbers[pos]);

			while (positions.size() > 0 && tree != TOCTree) {
				final int lastIndex = positions.size() - 1;
				final int treePos = positions.get(lastIndex);
				if (tree.subTrees().size() < childrenNumbers[treePos]) {
					break;
				}
				tree = tree.Parent;
				positions.remove(lastIndex);
			}
		}

		if (tree != TOCTree || positions.size() > 0) {
			throw new RuntimeException("Invalid state after TOC building:\n"
				+ "tree.Level = " + tree.Level + "\n"
				+ "positions.size() = " + positions.size());
		}
	}


	public ZLTextModel createTextModel(String id, String language,
			int paragraphsNumber, int[] entryIndices, int[] entryOffsets,
			int[] paragraphLenghts, int[] textSizes, byte[] paragraphKinds,
			String directoryName, String fileExtension, int blocksNumber) {
		if (myImageMap == null) {
			throw new RuntimeException("NativeBookModel hasn't been initialized with initBookModel method");
		}
		return new ZLTextNativeModel(id, language, paragraphsNumber, entryIndices, entryOffsets, paragraphLenghts, textSizes, paragraphKinds, directoryName, fileExtension, blocksNumber, myImageMap);
	}


	public void setBookTextModel(ZLTextModel model) {
		myBookTextModel = model;
	}

	public void setFootnoteModel(ZLTextModel model) {
		myFootnotes.put(model.getId(), model);
	}


	@Override
	public ZLTextModel getTextModel() {
		return myBookTextModel;
	}

	@Override
	public ZLTextModel getFootnoteModel(String id) {
		return myFootnotes.get(id);
	}

	@Override
	public Label getLabel(String id) {
		final int len = id.length();
		final int size = myInternalHyperlinks.size();

		for (int i = 0; i < size; ++i) {
			final char[] block = myInternalHyperlinks.block(i);
			for (int offset = 0; offset < block.length; ) {
				final int labelLength = (int)block[offset++];
				if (labelLength == 0) {
					break;
				}
				final int idLength = (int)block[offset + labelLength];
				if ((labelLength != len) || !id.equals(new String(block, offset, labelLength))) {
					offset += labelLength + idLength + 3;
					continue;
				}
				offset += labelLength + 1;
				final String modelId = (idLength > 0) ? new String(block, offset, idLength) : null;
				offset += idLength;
				final int paragraphNumber = (int)block[offset++] + ((int)block[offset++] << 16);
				return new Label(modelId, paragraphNumber);
			}
		}
		return null;
	}
}
