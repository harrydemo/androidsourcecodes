/*
 * Copyright (C) 2007-2011 Geometer Plus <contact@geometerplus.com>
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

import org.geometerplus.fbreader.formats.FormatPlugin;
import org.geometerplus.fbreader.formats.NativeFormatPlugin;
import org.geometerplus.fbreader.formats.PluginCollection;
import org.geometerplus.fbreader.library.Book;
import org.geometerplus.zlibrary.text.model.ZLTextModel;

public abstract class BookModel
{
	public static BookModel createModel(Book book)
	{
		FormatPlugin plugin = PluginCollection.Instance().getPlugin(book.File);
		if (plugin == null)
		{
			return null;
		}
		final BookModel model;
		final BookModel tempModel;//用于暂时放2章的内容
		if (plugin instanceof NativeFormatPlugin)
		{
			model = new NativeBookModel(book);
			//tempModel=new NativeBookModel(book);
		}
		else
		{
			model = new PlainBookModel(book);
			//tempModel=new PlainBookModel(book);
		}
		if (plugin.readModel(model))
		{
			// L.l("==============test==model:"
			// + model.getTextModel().getParagraphsNumber());
			// //
			// L.l("==============test==model content:"+model.getTextModel().getParagraph(30).iterator().getTextData());
			//
			// int kind = model.getTextModel().getParagraph(30).getKind();
			// L.l("===kind:" + kind);
			// EntryIterator iterator = model.getTextModel().getParagraph(30)
			// .iterator();
			// iterator.next();
			// char[] contentString = iterator.getTextData();
			// if (contentString != null)
			// {
			// L.l("========have content====");
			// }

			return model;
		}
		return null;
	}

	public final Book		Book;						// 电子书
	public final TOCTree	TOCTree	= new TOCTree();	// 目录

	public static final class Label
	{
		public final String	ModelId;
		public final int	ParagraphIndex;

		public Label(String modelId, int paragraphIndex)
		{
			ModelId = modelId;
			ParagraphIndex = paragraphIndex;
		}
	}

	protected BookModel(Book book)
	{
		Book = book;
	}

	public abstract ZLTextModel getTextModel();

	public abstract ZLTextModel getFootnoteModel(String id);

	public abstract Label getLabel(String id);
}
