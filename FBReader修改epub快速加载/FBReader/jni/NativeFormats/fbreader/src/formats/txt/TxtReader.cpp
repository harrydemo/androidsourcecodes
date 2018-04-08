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

#include <cctype>

#include <ZLInputStream.h>
#include <AndroidLog.h>


#include "TxtReader.h"

TxtReader::TxtReader(const std::string &encoding) : EncodedTextReader(encoding) {
}

TxtReader::~TxtReader() {
}


//=============talbe==
void TxtReader::readDocument(ZLInputStream &stream) {
	AndroidLog log;
	log.w("============readDocument==","======begin==");
	//log.lStartTime("readDocument");
	if (!stream.open()) {
		return;
	}

	//log.lStartTime("startDocument");
	startDocumentHandler();
	//log.lEndTime("startDocument");

	const size_t BUFSIZE = 2048;
	char *buffer = new char[BUFSIZE];
	std::string str;
	size_t length;
	//log.lEndTime("readDocument");

	do {

		//log.lStartTime("do---00--------------------------");
		length = stream.read(buffer, BUFSIZE);
		
		char *start = buffer;
		const char *end = buffer + length;
		//log.lStartTime("do---11111111");
		for (char *ptr = start; ptr != end; ++ptr) {
			if (*ptr == '\n' || *ptr == '\r') {
				bool skipNewLine = false;
				if (*ptr == '\r' && (ptr + 1) != end && *(ptr + 1) == '\n') {
					skipNewLine = true;
					*ptr = '\n';
				}
				//log.lStartTime("do---222");
				if (start != ptr) {
					str.erase();
					myConverter->convert(str, start, ptr + 1);
					characterDataHandler(str);
				}
				//log.lEndTime("do---222");
				if (skipNewLine) {
					++ptr;
				}
				start = ptr + 1;
				newLineHandler();
			} else if (isspace((unsigned char) *ptr)) {
				if (*ptr != '\t') {
					*ptr = ' ';
				}
			} else {
			}
		}
		//log.lEndTime("do---11111111");
		//log.lStartTime("do---333");
		if (start != end) {
			str.erase();
			myConverter->convert(str, start, end);
			characterDataHandler(str);
		}
		//log.lEndTime("do---333");
		//log.lEndTime("do-----------------------------");
		log.w("====================","====888888888===");
	} while (length == BUFSIZE);
	delete[] buffer;
    
	
	endDocumentHandler();

	stream.close();
}
