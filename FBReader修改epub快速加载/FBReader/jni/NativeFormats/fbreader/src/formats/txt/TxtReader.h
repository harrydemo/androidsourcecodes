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

#ifndef __TXTREADER_H__
#define __TXTREADER_H__

#include <string>

#include <ZLEncodingConverter.h>

#include "../EncodedTextReader.h"

class ZLInputStream;

class TxtReader : public EncodedTextReader {

public:
	void readDocument(ZLInputStream &stream);

protected:
	TxtReader(const std::string &encoding);
	virtual ~TxtReader();

protected:
	virtual void startDocumentHandler() = 0;
	virtual void endDocumentHandler() = 0;

	virtual bool characterDataHandler(std::string &str) = 0;
	virtual bool newLineHandler() = 0;

	virtual bool characterDataHandlerTest(std::string &str)=0;
	
};

#endif /* __TXTREADER_H__ */
