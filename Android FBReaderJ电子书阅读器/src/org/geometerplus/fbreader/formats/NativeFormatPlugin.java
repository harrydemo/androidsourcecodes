/*
 * Copyright (C) 2011-2012 Geometer Plus <contact@geometerplus.com>
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

package org.geometerplus.fbreader.formats;

import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.encodings.EncodingCollection;
import org.geometerplus.zlibrary.core.encodings.JavaEncodingCollection;
import org.geometerplus.zlibrary.core.image.*;
import org.geometerplus.zlibrary.core.util.MimeType;

import org.geometerplus.fbreader.bookmodel.BookModel;
import org.geometerplus.fbreader.bookmodel.BookReadingException;
import org.geometerplus.fbreader.library.Book;
import org.geometerplus.fbreader.formats.fb2.FB2NativePlugin;
import org.geometerplus.fbreader.formats.oeb.OEBNativePlugin;

public class NativeFormatPlugin extends FormatPlugin {
	public static NativeFormatPlugin create(String fileType) {
		if ("fb2".equals(fileType)) {
			return new FB2NativePlugin();
		} else if ("ePub".equals(fileType)) {
			return new OEBNativePlugin();
		} else {
			return new NativeFormatPlugin(fileType);
		}
	}

	protected NativeFormatPlugin(String fileType) {
		super(fileType);
	}

	@Override
	synchronized public void readMetaInfo(Book book) throws BookReadingException {
		if (!readMetaInfoNative(book)) {
			throw new BookReadingException("errorReadingFile", book.File);
		}
	}

	private native boolean readMetaInfoNative(Book book);

	@Override
	public native void detectLanguageAndEncoding(Book book);

	@Override
	synchronized public void readModel(BookModel model) throws BookReadingException {
		if (!readModelNative(model)) {
			throw new BookReadingException("errorReadingFile", model.Book.File);
		}
	}

	private native boolean readModelNative(BookModel model);

	@Override
	public ZLImage readCover(final ZLFile file) {
		return new ZLImageProxy() {
			@Override
			public int sourceType() {
				return SourceType.DISK;
			}

			@Override
			public String getId() {
				return file.getPath();
			}

			@Override
			public ZLSingleImage getRealImage() {
				final ZLImage[] box = new ZLImage[1];
				readCoverInternal(file, box);
				return (ZLSingleImage)box[0];
			}
		};
	}

	protected native void readCoverInternal(ZLFile file, ZLImage[] box);

	// FIXME: temporary implementation; implement as a native code (?)
	@Override
	public String readAnnotation(ZLFile file) {
		final FormatPlugin plugin = PluginCollection.Instance().getPlugin(file, FormatPlugin.Type.JAVA);
		if (plugin != null) {
			return plugin.readAnnotation(file);
		}
		return null;
	}

	@Override
	public Type type() {
		return Type.NATIVE;
	}

	@Override
	public EncodingCollection supportedEncodings() {
		return JavaEncodingCollection.Instance();
	}
}
