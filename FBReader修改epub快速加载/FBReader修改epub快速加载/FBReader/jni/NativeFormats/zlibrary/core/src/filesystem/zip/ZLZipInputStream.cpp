/*
 * Copyright (C) 2004-2011 Geometer Plus <contact@geometerplus.com>
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

#include <algorithm>

#include "ZLZip.h"
#include "ZLZipHeader.h"
#include "ZLZDecompressor.h"
#include "../ZLFSManager.h"

ZLZipInputStream::ZLZipInputStream(shared_ptr<ZLInputStream> &base, const std::string &entryName) : myBaseStream(new ZLInputStreamDecorator(base)), myEntryName(entryName), myUncompressedSize(0) {
}

ZLZipInputStream::~ZLZipInputStream() {
	close();
}

bool ZLZipInputStream::open() {
	close();

	const ZLZipEntryCache &cache = ZLZipEntryCache::cache(*myBaseStream);
	ZLZipEntryCache::Info info = cache.info(myEntryName);

	if (!myBaseStream->open()) {
		return false;
	}

	if (info.Offset == -1) {
		close();
		return false;
	}
	myBaseStream->seek(info.Offset, true);

	if (info.CompressionMethod == 0) {
		myIsDeflated = false;
	} else if (info.CompressionMethod == 8) {
		myIsDeflated = true;
	} else {
		close();
		return false;
	}
	myUncompressedSize = info.UncompressedSize;
	myAvailableSize = info.CompressedSize;
	if (myAvailableSize == 0) {
		myAvailableSize = (size_t)-1;
	}

	if (myIsDeflated) {
		myDecompressor = new ZLZDecompressor(myAvailableSize);
	}

	myOffset = 0;
	return true;
}

size_t ZLZipInputStream::read(char *buffer, size_t maxSize) {
	size_t realSize = 0;
	if (myIsDeflated) {
		realSize = myDecompressor->decompress(*myBaseStream, buffer, maxSize);
		myOffset += realSize;
	} else {
		realSize = myBaseStream->read(buffer, std::min(maxSize, myAvailableSize));
		myAvailableSize -= realSize;
		myOffset += realSize;
	}
	return realSize;
}

void ZLZipInputStream::close() {
	myDecompressor = 0;
	if (!myBaseStream.isNull()) {
		myBaseStream->close();
	}
}

void ZLZipInputStream::seek(int offset, bool absoluteOffset) {
	if (absoluteOffset) {
		offset -= this->offset();
	}
	if (offset > 0) {
		read(0, offset);
	} else if (offset < 0) {
		offset += this->offset();
		open();
		if (offset >= 0) {
			read(0, offset);
		}
	}
}

size_t ZLZipInputStream::offset() const {
	return myOffset;
}

size_t ZLZipInputStream::sizeOfOpened() {
	return myUncompressedSize;
}
