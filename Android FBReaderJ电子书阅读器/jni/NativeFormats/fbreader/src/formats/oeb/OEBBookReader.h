/*
 * Copyright (C) 2004-2012 Geometer Plus <contact@geometerplus.com>
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

#ifndef __OEBBOOKREADER_H__
#define __OEBBOOKREADER_H__

#include <map>
#include <vector>
#include <string>

#include <ZLXMLReader.h>

#include "../../bookmodel/BookReader.h"

class XHTMLReader;

class OEBBookReader : public ZLXMLReader {

public:
	OEBBookReader(BookModel &model);
	bool readBook(const ZLFile &file);

private:
	void startElementHandler(const char *tag, const char **attributes);
	void endElementHandler(const char *tag);
	bool processNamespaces() const;
	bool isOPFTag(const std::string &expected, const std::string &tag) const;
	const std::vector<std::string> &externalDTDs() const;

	void generateTOC(const XHTMLReader &xhtmlReader);

private:
	enum ReaderState {
		READ_NONE,
		READ_MANIFEST,
		READ_SPINE,
		READ_GUIDE,
		READ_TOUR
	};

	BookReader myModelReader;
	ReaderState myState;

	std::string myFilePrefix;
	std::map<std::string,std::string> myIdToHref;
	std::vector<std::string> myHtmlFileNames;
	std::string myNCXTOCFileName;
	std::string myCoverFileName;
	std::vector<std::pair<std::string,std::string> > myTourTOC;
	std::vector<std::pair<std::string,std::string> > myGuideTOC;
};

#endif /* __OEBBOOKREADER_H__ */
