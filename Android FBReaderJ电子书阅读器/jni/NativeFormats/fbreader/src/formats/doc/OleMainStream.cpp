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

#include <cstring> //for memset
#include <string>

#include <ZLLogger.h>
#include <ZLUnicodeUtil.h>

#include "OleUtil.h"
#include "OleStorage.h"

#include "DocInlineImageReader.h"

#include "OleMainStream.h"

OleMainStream::Style::Style() {
	(void)memset(this, 0, sizeof(*this));
	istd = ISTD_INVALID;
	istdNext = ISTD_INVALID;
	hasPageBreakBefore = false;
	charInfo.fontSize = 20;
}

OleMainStream::CharInfo::CharInfo():
	fontStyle(0),
	fontSize(20) {
}


OleMainStream::SectionInfo::SectionInfo() :
	charPos(0),
	newPage(true) {
}

OleMainStream::InlineImageInfo::InlineImageInfo() :
	dataPos(0) {
}

OleMainStream::FloatImageInfo::FloatImageInfo() :
	shapeID(0) {
}

OleMainStream::OleMainStream(shared_ptr<OleStorage> storage, OleEntry oleEntry, shared_ptr<ZLInputStream> stream) :
	OleStream(storage, oleEntry, stream) {
}

bool OleMainStream::open() {
	if (OleStream::open() == false) {
		return false;
	}

	static const size_t HEADER_SIZE = 768; //size of data in header of main stream
	char headerBuffer[HEADER_SIZE];
	seek(0, true);

	if (read(headerBuffer, HEADER_SIZE) != HEADER_SIZE) {
		return false;
	}

	bool result = readFIB(headerBuffer);
	if (!result) {
		return false;
	}

	//determining table stream number
	unsigned int tableNumber = (OleUtil::getU2Bytes(headerBuffer, 0xA) & 0x0200) ? 1 : 0;
	std::string tableName = tableNumber == 0 ? "0" : "1";
	tableName += "Table";
	OleEntry tableEntry;
	result = myStorage->getEntryByName(tableName, tableEntry);

	if (!result) {
		//cant't find table stream (that can be only in case if file format is below Word 7/8), so building simple table stream
		Piece piece = {myStartOfText, myEndOfText - myStartOfText, true, Piece::TEXT, 0}; //CHECK may be not all old documents have ANSI
		myPieces.push_back(piece);
		return true;
	}

	result = readPieceTable(headerBuffer, tableEntry);

	if (!result) {
		ZLLogger::Instance().println("OleMainStream", "error during reading piece table");
		return false;
	}

	OleEntry dataEntry;
	if (myStorage->getEntryByName("Data", dataEntry)) {
		myDataStream = new OleStream(myStorage, dataEntry, myBaseStream);
	}

	//result of reading following structures doesn't check, because all these
	//problems can be ignored, and document can be showed anyway, maybe with wrong formatting
	readBookmarks(headerBuffer, tableEntry);
	readStylesheet(headerBuffer, tableEntry);
	//readSectionsInfoTable(headerBuffer, tableEntry); //it isn't used now
	readParagraphStyleTable(headerBuffer, tableEntry);
	readCharInfoTable(headerBuffer, tableEntry);
	readFloatingImages(headerBuffer, tableEntry);
	return true;
}

const OleMainStream::Pieces &OleMainStream::getPieces() const {
	return myPieces;
}

const OleMainStream::CharInfoList &OleMainStream::getCharInfoList() const {
	return myCharInfoList;
}

const OleMainStream::StyleInfoList &OleMainStream::getStyleInfoList() const {
	return myStyleInfoList;
}

const OleMainStream::Bookmarks &OleMainStream::getBookmarks() const {
	return myBookmarks;
}

const OleMainStream::InlineImageInfoList &OleMainStream::getInlineImageInfoList() const {
	return myInlineImageInfoList;
}

const OleMainStream::FloatImageInfoList &OleMainStream::getFloatImageInfoList() const {
	return myFloatImageInfoList;
}

ZLFileImage::Blocks OleMainStream::getFloatImage(unsigned int shapeID) const {
	if (myFLoatImageReader.isNull()) {
		return ZLFileImage::Blocks();
	}
	return myFLoatImageReader->getBlocksForShapeID(shapeID);
}

ZLFileImage::Blocks OleMainStream::getInlineImage(unsigned int dataPos) const {
	if (myDataStream.isNull()) {
		return ZLFileImage::Blocks();
	}
	DocInlineImageReader imageReader(myDataStream);
	return imageReader.getImagePieceInfo(dataPos);
}

bool OleMainStream::readFIB(const char *headerBuffer) {
	int flags = OleUtil::getU2Bytes(headerBuffer, 0xA); //offset for flags

	if (flags & 0x0004) { //flag for complex format
		ZLLogger::Instance().println("OleMainStream", "This was fast-saved. Some information is lost");
		//lostInfo = (flags & 0xF0) >> 4);
	}

	if (flags & 0x1000) { //flag for using extending charset
		ZLLogger::Instance().println("OleMainStream", "File uses extended character set (get_word8_char)");
	} else {
		ZLLogger::Instance().println("OleMainStream", "File uses get_8bit_char character set");
	}

	if (flags & 0x100) { //flag for encrypted files
		ZLLogger::Instance().println("OleMainStream", "File is encrypted");
		// Encryption key = %08lx ; NumUtil::get4Bytes(header, 14)
		return false;
	}

	unsigned int charset = OleUtil::getU2Bytes(headerBuffer, 0x14); //offset for charset number
	if (charset && charset != 0x100) { //0x100 = default charset
		ZLLogger::Instance().println("OleMainStream", "Using not default character set %d");
	} else {
		ZLLogger::Instance().println("OleMainStream", "Using default character set");
	}

	myStartOfText = OleUtil::get4Bytes(headerBuffer, 0x18); //offset for start of text value
	myEndOfText = OleUtil::get4Bytes(headerBuffer, 0x1c); //offset for end of text value
	return true;
}

void OleMainStream::splitPieces(const Pieces &s, Pieces &dest1, Pieces &dest2, Piece::PieceType type1, Piece::PieceType type2, int boundary) {
	Pieces source = s;
	dest1.clear();
	dest2.clear();

	int sumLength = 0;
	size_t i = 0;
	for (i = 0; i < source.size(); ++i) {
		Piece piece = source.at(i);
		if (piece.length + sumLength >= boundary) {
			Piece piece2 = piece;

			piece.length = boundary - sumLength;
			piece.type = type1;

			piece2.type = type2;
			piece2.offset += piece.length * 2;
			piece2.length -= piece.length;

			if (piece.length > 0) {
				dest1.push_back(piece);
			}
			if (piece2.length > 0) {
				dest2.push_back(piece2);
			}
			++i;
			break;
		}
		sumLength += piece.length;
		piece.type = type1;
		dest1.push_back(piece);
	}
	for (; i < source.size(); ++i) {
		Piece piece = source.at(i);
		piece.type = type2;
		dest2.push_back(piece);
	}

}

std::string OleMainStream::getPiecesTableBuffer(const char *headerBuffer, OleStream &tableStream) {
	unsigned int clxOffset = OleUtil::getU4Bytes(headerBuffer, 0x01A2); //offset for CLX structure
	unsigned int clxLength = OleUtil::getU4Bytes(headerBuffer, 0x01A6); //offset for value of CLX structure length

	//1 step : loading CLX table from table stream
	char *clxBuffer = new char[clxLength];
	tableStream.seek(clxOffset, true);
	tableStream.read(clxBuffer, clxLength);
	std::string clx(clxBuffer, clxLength);
	delete[] clxBuffer;

	//2 step: searching for pieces table buffer at CLX
	//(determines it by 0x02 as start symbol)
	size_t from = 0;
	size_t i;
	std::string pieceTableBuffer;
	while ((i = clx.find_first_of(0x02, from)) != std::string::npos) {
		unsigned int pieceTableLength = OleUtil::getU4Bytes(clx.c_str(), i + 1);
		pieceTableBuffer = std::string(clx, i + 1 + 4);
		if (pieceTableBuffer.length() != pieceTableLength) {
			from = i + 1;
			continue;
		}
		break;
	}
	return pieceTableBuffer;
}


bool OleMainStream::readPieceTable(const char *headerBuffer, const OleEntry &tableEntry) {
	OleStream tableStream(myStorage, tableEntry, myBaseStream);
	std::string piecesTableBuffer = getPiecesTableBuffer(headerBuffer, tableStream);

	//getting count of Character Positions for different types of subdocuments in Main Stream
	int ccpText = OleUtil::get4Bytes(headerBuffer, 0x004C); //text
	int ccpFtn = OleUtil::get4Bytes(headerBuffer, 0x0050); //footnote subdocument
	int ccpHdd = OleUtil::get4Bytes(headerBuffer, 0x0054); //header subdocument
	int ccpMcr = OleUtil::get4Bytes(headerBuffer, 0x0058); //macro subdocument
	int ccpAtn = OleUtil::get4Bytes(headerBuffer, 0x005C); //comment subdocument
	int ccpEdn = OleUtil::get4Bytes(headerBuffer, 0x0060); //endnote subdocument
	int ccpTxbx = OleUtil::get4Bytes(headerBuffer, 0x0064); //textbox subdocument
	int ccpHdrTxbx = OleUtil::get4Bytes(headerBuffer, 0x0068); //textbox subdocument of the header
	int lastCP = ccpFtn + ccpHdd + ccpMcr + ccpAtn + ccpEdn + ccpTxbx + ccpHdrTxbx;
	if (lastCP != 0) {
		++lastCP;
	}
	lastCP += ccpText;

	//getting the CP (character positions) and CP descriptors
	std::vector<int> cp; //array of character positions for pieces
	unsigned int j = 0;
	for (j = 0; ; j += 4) {
		int curCP = OleUtil::get4Bytes(piecesTableBuffer.c_str(), j);
		cp.push_back(curCP);
		if (curCP == lastCP) {
			break;
		}
	}

	std::vector<std::string> descriptors;
	for (size_t k = 0; k < cp.size() - 1; ++k) {
		//j + 4, because it should be taken after CP in PiecesTable Buffer
		//k * 8, because it should be taken 8 byte for each descriptor
		descriptors.push_back(piecesTableBuffer.substr(j + 4 + k * 8, 8));
	}

	//filling the Pieces vector
	for (size_t i = 0; i < descriptors.size(); ++i) {
		//4byte integer with offset and ANSI flag
		int fcValue = OleUtil::get4Bytes(descriptors.at(i).c_str(), 0x2); //offset for piece structure
		Piece piece;
		piece.isANSI = (fcValue & 0x40000000) == 0x40000000; //ansi flag
		piece.offset = fcValue & 0x3FFFFFFF; //gettting offset for current piece
		piece.length = cp.at(i + 1) - cp.at(i);
		myPieces.push_back(piece);
	}

	//split pieces into different types
	Pieces piecesText, piecesFootnote, piecesOther;
	splitPieces(myPieces, piecesText, piecesFootnote, Piece::TEXT, Piece::FOOTNOTE, ccpText);
	splitPieces(piecesFootnote, piecesFootnote, piecesOther, Piece::FOOTNOTE, Piece::OTHER, ccpFtn);

	myPieces.clear();
	for (size_t i = 0; i < piecesText.size(); ++i) {
		myPieces.push_back(piecesText.at(i));
	}
	for (size_t i = 0; i < piecesFootnote.size(); ++i) {
		myPieces.push_back(piecesFootnote.at(i));
	}
	for (size_t i = 0; i < piecesOther.size(); ++i) {
		myPieces.push_back(piecesOther.at(i));
	}

	//converting length and offset depending on isANSI
	for (size_t i = 0; i < myPieces.size(); ++i) {
		Piece &piece = myPieces.at(i);
		if (!piece.isANSI) {
			piece.length *= 2;
		} else {
			piece.offset /= 2;
		}
	}

	//filling startCP field
	unsigned int curStartCP = 0;
	for (size_t i = 0; i < myPieces.size(); ++i) {
		Piece &piece = myPieces.at(i);
		piece.startCP = curStartCP;
		if (piece.isANSI) {
			curStartCP += piece.length;
		} else {
			curStartCP += piece.length / 2;
		}
	}
	return true;
}

bool OleMainStream::readBookmarks(const char *headerBuffer, const OleEntry &tableEntry) {
	//SttbfBkmk structure is a table of bookmark name strings
	unsigned int beginNamesInfo = OleUtil::getU4Bytes(headerBuffer, 0x142); // address of SttbfBkmk structure
	size_t namesInfoLength = (size_t)OleUtil::getU4Bytes(headerBuffer, 0x146); // length of SttbfBkmk structure

	if (namesInfoLength == 0) {
		return true; //there's no bookmarks
	}

	OleStream tableStream(myStorage, tableEntry, myBaseStream);
	std::string buffer;
	if (!readToBuffer(buffer, beginNamesInfo, namesInfoLength, tableStream)) {
		return false;
	}

	unsigned int recordsNumber = OleUtil::getU2Bytes(buffer.c_str(), 0x2); //count of records

	std::vector<std::string> names;
	unsigned int offset = 0x6; //initial offset
	for (unsigned int i = 0; i < recordsNumber; ++i) {
		unsigned int length = OleUtil::getU2Bytes(buffer.c_str(), offset) * 2; //legnth of string in bytes
		ZLUnicodeUtil::Ucs2String name;
		for (unsigned int j = 0; j < length; j+=2) {
			char ch1 = buffer.at(offset + 2 + j);
			char ch2 = buffer.at(offset + 2 + j + 1);
			ZLUnicodeUtil::Ucs2Char ucs2Char = (unsigned int)ch1 | ((unsigned int)ch2 << 8);
			name.push_back(ucs2Char);
		}
		std::string utf8Name;
		ZLUnicodeUtil::ucs2ToUtf8(utf8Name, name);
		names.push_back(utf8Name);
		offset += length + 2;
	}

	//plcfBkmkf structure is table recording beginning CPs of bookmarks
	unsigned int beginCharPosInfo = OleUtil::getU4Bytes(headerBuffer, 0x14A); // address of plcfBkmkf structure
	size_t charPosInfoLen = (size_t)OleUtil::getU4Bytes(headerBuffer, 0x14E); // length of plcfBkmkf structure

	if (charPosInfoLen == 0) {
		return true; //there's no bookmarks
	}

	if (!readToBuffer(buffer, beginCharPosInfo, charPosInfoLen, tableStream)) {
		return false;
	}

	static const unsigned int BKF_SIZE = 4;
	size_t size = calcCountOfPLC(charPosInfoLen, BKF_SIZE);
	std::vector<unsigned int> charPage;
	for (size_t index = 0, offset = 0; index < size; ++index, offset += 4) {
		charPage.push_back(OleUtil::getU4Bytes(buffer.c_str(), offset));
	}

	for (size_t i = 0; i < names.size(); ++i) {
		if (i >= charPage.size()) {
			break; //for the case if something in these structures goes wrong, to not to lose all bookmarks
		}
		Bookmark bookmark;
		bookmark.charPos = charPage.at(i);
		bookmark.name = names.at(i);
		myBookmarks.push_back(bookmark);
	}

	return true;
}

bool OleMainStream::readStylesheet(const char *headerBuffer, const OleEntry &tableEntry) {
	//STSH structure is a stylesheet
	unsigned int beginStshInfo = OleUtil::getU4Bytes(headerBuffer, 0xa2); // address of STSH structure
	size_t stshInfoLength = (size_t)OleUtil::getU4Bytes(headerBuffer, 0xa6); // length of STSH structure

	OleStream tableStream(myStorage, tableEntry, myBaseStream);
	char *buffer = new char[stshInfoLength];
	tableStream.seek(beginStshInfo, true);
	if (tableStream.read(buffer, stshInfoLength) != stshInfoLength) {
		return false;
	}

	size_t stdCount = (size_t)OleUtil::getU2Bytes(buffer, 2);
	size_t stdBaseInFile = (size_t)OleUtil::getU2Bytes(buffer, 4);
	myStyleSheet.resize(stdCount);

	std::vector<bool> isFilled;
	isFilled.resize(stdCount, false);

	size_t stdLen = 0;
	bool styleSheetWasChanged = false;
	do { //make it in while loop, because some base style can be after their successors
		styleSheetWasChanged = false;
		for (size_t index = 0, offset = 2 + (size_t)OleUtil::getU2Bytes(buffer, 0); index < stdCount; index++, offset += 2 + stdLen) {
			stdLen = (size_t)OleUtil::getU2Bytes(buffer, offset);
			if (isFilled.at(index)) {
				continue;
			}

			if (stdLen == 0) {
				//if record is empty, left it default
				isFilled[index] = true;
				continue;
			}

			Style styleInfo = myStyleSheet.at(index);

			unsigned int styleAndBaseType = OleUtil::getU2Bytes(buffer, offset + 4);
			unsigned int styleType = styleAndBaseType % 16;
			unsigned int baseStyle = styleAndBaseType / 16;
			if (baseStyle == STI_NIL || baseStyle == STI_USER) {
				//if based on nil or user style, left defaukt
			} else {
				int baseStyleIndex = getStyleIndex(baseStyle, isFilled, myStyleSheet);
				if (baseStyleIndex < 0) {
					//this base style is not filled yet, sp pass it at some time
					continue;
				}
				styleInfo = myStyleSheet.at(baseStyleIndex);
				styleInfo.istd = ISTD_INVALID;
			}

			// parse STD structure
			unsigned int tmp = OleUtil::getU2Bytes(buffer, offset + 6);
			unsigned int upxCount = tmp % 16;
			styleInfo.istdNext = tmp / 16;

			//adding current style
			myStyleSheet[index] = styleInfo;
			isFilled[index] = true;
			styleSheetWasChanged = true;

			size_t pos = 2 + stdBaseInFile;
			size_t nameLen = (size_t)OleUtil::getU2Bytes(buffer, offset + pos);
			nameLen = nameLen * 2 + 2; //from Unicode characters to bytes + Unicode null charachter length
			pos += 2 + nameLen;
			if (pos % 2 != 0) {
				++pos;
			}
			if (pos >= stdLen) {
				continue;
			}
			size_t upxLen = (size_t)OleUtil::getU2Bytes(buffer, offset + pos);
			if (pos + upxLen > stdLen) {
				//UPX length too large
				continue;
			}
			//for style info styleType must be equal 1
			if (styleType == 1 && upxCount >= 1) {
				if (upxLen >= 2) {
					styleInfo.istd = OleUtil::getU2Bytes(buffer, offset + pos + 2);
					getStyleInfo(0, buffer + offset + pos + 4, upxLen - 2, styleInfo);
					myStyleSheet[index] = styleInfo;
				}
				pos += 2 + upxLen;
				if (pos % 2 != 0) {
					++pos;
				}
				upxLen = (size_t)OleUtil::getU2Bytes(buffer, offset + pos);
			}
			if (upxLen == 0 || pos + upxLen > stdLen) {
				//too small/too large
				continue;
			}
			//for char info styleType can be equal 1 or 2
			if ((styleType == 1 && upxCount >= 2) || (styleType == 2 && upxCount >= 1)) {
				CharInfo charInfo;
				getCharInfo(0, ISTD_INVALID, buffer + offset + pos + 2, upxLen, charInfo);
				styleInfo.charInfo = charInfo;
				myStyleSheet[index] = styleInfo;
			}
		}
	} while (styleSheetWasChanged);
	delete[] buffer;
	return true;
}

bool OleMainStream::readCharInfoTable(const char *headerBuffer, const OleEntry &tableEntry) {
	//PlcfbteChpx structure is table with formatting for particular run of text
	unsigned int beginCharInfo = OleUtil::getU4Bytes(headerBuffer, 0xfa); // address of PlcfbteChpx structure
	size_t charInfoLength = (size_t)OleUtil::getU4Bytes(headerBuffer, 0xfe); // length of PlcfbteChpx structure
	if (charInfoLength < 4) {
		return false;
	}

	OleStream tableStream(myStorage, tableEntry, myBaseStream);
	std::string buffer;
	if (!readToBuffer(buffer, beginCharInfo, charInfoLength, tableStream)) {
		return false;
	}

	static const unsigned int CHPX_SIZE = 4;
	size_t size = calcCountOfPLC(charInfoLength, CHPX_SIZE);
	std::vector<unsigned int> charBlocks;
	for (size_t index = 0, offset = (size + 1) * 4; index < size; ++index, offset += CHPX_SIZE) {
		charBlocks.push_back(OleUtil::getU4Bytes(buffer.c_str(), offset));
	}

	char *formatPageBuffer = new char[OleStorage::BBD_BLOCK_SIZE];
	for (size_t index = 0; index < charBlocks.size(); ++index) {
		seek(charBlocks.at(index) * OleStorage::BBD_BLOCK_SIZE, true);
		if (read(formatPageBuffer, OleStorage::BBD_BLOCK_SIZE) != OleStorage::BBD_BLOCK_SIZE) {
			return false;
		}
		unsigned int crun = OleUtil::getU1Byte(formatPageBuffer, 0x1ff); //offset with crun (count of 'run of text')
		for (unsigned int index2 = 0; index2 < crun; ++index2) {
			unsigned int offset = OleUtil::getU4Bytes(formatPageBuffer, index2 * 4);
			unsigned int chpxOffset = 2 * OleUtil::getU1Byte(formatPageBuffer, (crun + 1) * 4 + index2);
			unsigned int len = OleUtil::getU1Byte(formatPageBuffer, chpxOffset);
			unsigned int charPos = 0;
			if (!offsetToCharPos(offset, charPos, myPieces)) {
				continue;
			}
			unsigned int istd = getIstdByCharPos(charPos, myStyleInfoList);

			CharInfo charInfo = getStyleFromStylesheet(istd, myStyleSheet).charInfo;
			if (chpxOffset != 0) {
				getCharInfo(chpxOffset, istd, formatPageBuffer + 1, len - 1, charInfo);
			}
			myCharInfoList.push_back(CharPosToCharInfo(charPos, charInfo));

			if (chpxOffset != 0) {
				InlineImageInfo pictureInfo;
				if (getInlineImageInfo(chpxOffset, formatPageBuffer + 1, len - 1, pictureInfo)) {
					myInlineImageInfoList.push_back(CharPosToInlineImageInfo(charPos, pictureInfo));
				}
			}

		}
	}
	delete[] formatPageBuffer;
	return true;
}

bool OleMainStream::readFloatingImages(const char *headerBuffer, const OleEntry &tableEntry) {
	//Plcspa structure is a table with information for FSPA (File Shape Address)
	unsigned int beginPicturesInfo = OleUtil::getU4Bytes(headerBuffer, 0x01DA); // address of Plcspa structure
	if (beginPicturesInfo == 0) {
		return true; //there's no office art objects
	}
	unsigned int picturesInfoLength = OleUtil::getU4Bytes(headerBuffer, 0x01DE); // length of Plcspa structure
	if (picturesInfoLength < 4) {
		return false;
	}

	OleStream tableStream(myStorage, tableEntry, myBaseStream);
	std::string buffer;
	if (!readToBuffer(buffer, beginPicturesInfo, picturesInfoLength, tableStream)) {
		return false;
	}


	static const unsigned int SPA_SIZE = 26;
	size_t size = calcCountOfPLC(picturesInfoLength, SPA_SIZE);

	std::vector<unsigned int> picturesBlocks;
	for (size_t index = 0, tOffset = 0; index < size; ++index, tOffset += 4) {
		picturesBlocks.push_back(OleUtil::getU4Bytes(buffer.c_str(), tOffset));
	}

	for (size_t index = 0, tOffset = (size + 1) * 4; index < size; ++index, tOffset += SPA_SIZE) {
		unsigned int spid = OleUtil::getU4Bytes(buffer.c_str(), tOffset);
		FloatImageInfo info;
		unsigned int charPos = picturesBlocks.at(index);
		info.shapeID = spid;
		myFloatImageInfoList.push_back(CharPosToFloatImageInfo(charPos, info));
	}

	//DggInfo structure is office art object table data
	unsigned int beginOfficeArtContent = OleUtil::getU4Bytes(headerBuffer, 0x22A); // address of DggInfo structure
	if (beginOfficeArtContent == 0) {
		return true; //there's no office art objects
	}
	unsigned int officeArtContentLength = OleUtil::getU4Bytes(headerBuffer, 0x022E); // length of DggInfo structure
	if (officeArtContentLength < 4) {
		return false;
	}

	shared_ptr<OleStream> newTableStream = new OleStream(myStorage, tableEntry, myBaseStream);
	shared_ptr<OleStream> newMainStream = new OleStream(myStorage, myOleEntry, myBaseStream);
	if (newTableStream->open() && newMainStream->open()) {
		myFLoatImageReader = new DocFloatImageReader(beginOfficeArtContent, officeArtContentLength, newTableStream, newMainStream);
		myFLoatImageReader->readAll();
	}
	return true;
}

bool OleMainStream::readParagraphStyleTable(const char *headerBuffer, const OleEntry &tableEntry) {
	//PlcBtePapx structure is table with formatting for all paragraphs
	unsigned int beginParagraphInfo = OleUtil::getU4Bytes(headerBuffer, 0x102); // address of PlcBtePapx structure
	size_t paragraphInfoLength = (size_t)OleUtil::getU4Bytes(headerBuffer, 0x106); // length of PlcBtePapx structure
	if (paragraphInfoLength < 4) {
		return false;
	}

	OleStream tableStream(myStorage, tableEntry, myBaseStream);
	std::string buffer;
	if (!readToBuffer(buffer, beginParagraphInfo, paragraphInfoLength, tableStream)) {
		return false;
	}

	static const unsigned int PAPX_SIZE = 4;
	size_t size = calcCountOfPLC(paragraphInfoLength, PAPX_SIZE);

	std::vector<unsigned int> paragraphBlocks;
	for (size_t index = 0, tOffset = (size + 1) * 4; index < size; ++index, tOffset += PAPX_SIZE) {
		paragraphBlocks.push_back(OleUtil::getU4Bytes(buffer.c_str(), tOffset));
	}

	char *formatPageBuffer = new char[OleStorage::BBD_BLOCK_SIZE];
	for (size_t index = 0; index < paragraphBlocks.size(); ++index) {
		seek(paragraphBlocks.at(index) * OleStorage::BBD_BLOCK_SIZE, true);
		if (read(formatPageBuffer, OleStorage::BBD_BLOCK_SIZE) != OleStorage::BBD_BLOCK_SIZE) {
			return false;
		}
		unsigned int paragraphsCount = OleUtil::getU1Byte(formatPageBuffer, 0x1ff); //offset with 'cpara' value (count of paragraphs)
		for (unsigned int index2 = 0; index2 < paragraphsCount; ++index2) {
			unsigned int offset = OleUtil::getU4Bytes(formatPageBuffer, index2 * 4);
			unsigned int papxOffset = OleUtil::getU1Byte(formatPageBuffer, (paragraphsCount + 1) * 4 + index2 * 13) * 2;
			if (papxOffset <= 0) {
				continue;
			}
			unsigned int len = OleUtil::getU1Byte(formatPageBuffer, papxOffset) * 2;
			if (len == 0) {
				++papxOffset;
				len = OleUtil::getU1Byte(formatPageBuffer, papxOffset) * 2;
			}

			unsigned int istd = OleUtil::getU2Bytes(formatPageBuffer, papxOffset + 1);
			Style styleInfo = getStyleFromStylesheet(istd, myStyleSheet);

			if (len >= 3) {
				getStyleInfo(papxOffset, formatPageBuffer + 3, len - 3, styleInfo);
			}

			unsigned int charPos = 0;
			if (!offsetToCharPos(offset, charPos, myPieces)) {
				continue;
			}
			myStyleInfoList.push_back(CharPosToStyle(charPos, styleInfo));
		}
	}
	delete[] formatPageBuffer;
	return true;
}

bool OleMainStream::readSectionsInfoTable(const char *headerBuffer, const OleEntry &tableEntry) {
	//PlcfSed structure is a section table
	unsigned int beginOfText = OleUtil::getU4Bytes(headerBuffer, 0x18); //address of text's begin in main stream
	unsigned int beginSectInfo = OleUtil::getU4Bytes(headerBuffer, 0xca); //address if PlcfSed structure

	size_t sectInfoLen = (size_t)OleUtil::getU4Bytes(headerBuffer, 0xce); //length of PlcfSed structure
	if (sectInfoLen < 4) {
		return false;
	}

	OleStream tableStream(myStorage, tableEntry, myBaseStream);
	std::string buffer;
	if (!readToBuffer(buffer, beginSectInfo, sectInfoLen, tableStream)) {
		return false;
	}

	static const unsigned int SED_SIZE = 12;
	size_t decriptorsCount = calcCountOfPLC(sectInfoLen, SED_SIZE);

	//saving the section offsets (in character positions)
	std::vector<unsigned int> charPos;
	for (size_t index = 0, tOffset = 0; index < decriptorsCount; ++index, tOffset += 4) {
		unsigned int ulTextOffset = OleUtil::getU4Bytes(buffer.c_str(), tOffset);
		charPos.push_back(beginOfText + ulTextOffset);
	}

	//saving sepx offsets
	std::vector<unsigned int> sectPage;
	for (size_t index = 0, tOffset = (decriptorsCount + 1) * 4; index < decriptorsCount; ++index, tOffset += SED_SIZE) {
		sectPage.push_back(OleUtil::getU4Bytes(buffer.c_str(), tOffset + 2));
	}

	//reading the section properties
	char tmpBuffer[2];
	for (size_t index = 0; index < sectPage.size(); ++index) {
		if (sectPage.at(index) == 0xffffffffUL) { //check for invalid record, to make default section info
			SectionInfo sectionInfo;
			sectionInfo.charPos = charPos.at(index);
			mySectionInfoList.push_back(sectionInfo);
			continue;
		}
		//getting number of bytes to read
		if (!seek(sectPage.at(index), true)) {
			continue;
		}
		if (read(tmpBuffer, 2) != 2) {
			continue;
		}
		size_t bytes = 2 + (size_t)OleUtil::getU2Bytes(tmpBuffer, 0);

		if (!seek(sectPage.at(index), true)) {
			continue;
		}
		char *formatPageBuffer = new char[bytes];
		if (read(formatPageBuffer, bytes) != bytes) {
			delete[] formatPageBuffer;
			continue;
		}
		SectionInfo sectionInfo;
		sectionInfo.charPos = charPos.at(index);
		getSectionInfo(formatPageBuffer + 2, bytes - 2, sectionInfo);
		mySectionInfoList.push_back(sectionInfo);
		delete[] formatPageBuffer;
	}
	return true;
}

void OleMainStream::getStyleInfo(unsigned int papxOffset, const char *grpprlBuffer, unsigned int bytes, Style &styleInfo) {
	int	tmp, toDelete, toAdd;
	unsigned int offset = 0;
	while (bytes >= offset + 2) {
		unsigned int curPrlLength = 0;
		switch (OleUtil::getU2Bytes(grpprlBuffer, papxOffset + offset)) {
			case 0x2403:
				styleInfo.alignment = OleUtil::getU1Byte(grpprlBuffer, papxOffset + offset + 2);
				break;
			case 0x4610:
				styleInfo.leftIndent += OleUtil::getU2Bytes(grpprlBuffer, papxOffset + offset + 2);
				if (styleInfo.leftIndent < 0) {
					styleInfo.leftIndent = 0;
				}
				break;
			case 0xc60d: // ChgTabsPapx
			case 0xc615: // ChgTabs
				tmp = OleUtil::get1Byte(grpprlBuffer, papxOffset + offset + 2);
				if (tmp < 2) {
					curPrlLength = 1;
					break;
				}
				toDelete = OleUtil::getU1Byte(grpprlBuffer, papxOffset + offset + 3);
				if (tmp < 2 + 2 * toDelete) {
					curPrlLength = 1;
					break;
				}
				toAdd = OleUtil::getU1Byte(grpprlBuffer, papxOffset + offset + 4 + 2 * toDelete);
				if (tmp < 2 + 2 * toDelete + 2 * toAdd) {
					curPrlLength = 1;
					break;
				}
				break;
			case 0x840e:
				styleInfo.rightIndent = (int)OleUtil::getU2Bytes(grpprlBuffer, papxOffset + offset + 2);
				break;
			case 0x840f:
				styleInfo.leftIndent = (int)OleUtil::getU2Bytes(grpprlBuffer, papxOffset + offset + 2);
				break;
			case 0x8411:
				styleInfo.firstLineIndent = (int)OleUtil::getU2Bytes(grpprlBuffer, papxOffset + offset + 2);
				break;
			case 0xa413:
				styleInfo.beforeIndent = OleUtil::getU2Bytes(grpprlBuffer, papxOffset + offset + 2);
				break;
			case 0xa414:
				styleInfo.afterIndent = OleUtil::getU2Bytes(grpprlBuffer, papxOffset + offset + 2);
				break;
			case 0x2407:
				styleInfo.hasPageBreakBefore = OleUtil::getU1Byte(grpprlBuffer, papxOffset + offset + 2) == 0x01;
				break;
			default:
				break;
		}
		if (curPrlLength == 0) {
			curPrlLength = getPrlLength(grpprlBuffer, papxOffset + offset);
		}
		offset += curPrlLength;
	}

}

void OleMainStream::getCharInfo(unsigned int chpxOffset, unsigned int /*istd*/, const char *grpprlBuffer, unsigned int bytes, CharInfo &charInfo) {
	unsigned int sprm = 0; //single propery modifier
	unsigned int offset = 0;
	while (bytes >= offset + 2) {
		switch (OleUtil::getU2Bytes(grpprlBuffer, chpxOffset + offset)) {
			case 0x0835: //bold
				sprm = OleUtil::getU1Byte(grpprlBuffer, chpxOffset + offset + 2);
				switch (sprm) {
					case UNSET:
						charInfo.fontStyle &= ~CharInfo::BOLD;
						break;
					case SET:
						charInfo.fontStyle |= CharInfo::BOLD;
						break;
					case UNCHANGED:
						break;
					case NEGATION:
						charInfo.fontStyle ^= CharInfo::BOLD;
						break;
					default:
						break;
				}
				break;
			case 0x0836: //italic
				sprm = OleUtil::getU1Byte(grpprlBuffer, chpxOffset + offset + 2);
				switch (sprm) {
					case UNSET:
						charInfo.fontStyle &= ~CharInfo::ITALIC;
						break;
					case SET:
						charInfo.fontStyle |= CharInfo::ITALIC;
						break;
					case UNCHANGED:
						break;
					case NEGATION:
						charInfo.fontStyle ^= CharInfo::ITALIC;
						break;
					default:
						break;
					}
				break;
			case 0x4a43: //size of font
				charInfo.fontSize = OleUtil::getU2Bytes(grpprlBuffer, chpxOffset + offset + 2);
			break;
			default:
				break;
		}
		offset += getPrlLength(grpprlBuffer, chpxOffset + offset);
	}

}

void OleMainStream::getSectionInfo(const char *grpprlBuffer, size_t bytes, SectionInfo &sectionInfo) {
	unsigned int tmp;
	size_t offset = 0;
	while (bytes >= offset + 2) {
		switch (OleUtil::getU2Bytes(grpprlBuffer, offset)) {
			case 0x3009: //new page
				tmp = OleUtil::getU1Byte(grpprlBuffer, offset + 2);
				sectionInfo.newPage = (tmp != 0 && tmp != 1);
				break;
			default:
				break;
		}
		offset += getPrlLength(grpprlBuffer, offset);
	}
}

bool OleMainStream::getInlineImageInfo(unsigned int chpxOffset, const char *grpprlBuffer, unsigned int bytes, InlineImageInfo &pictureInfo) {
	//p. 105 of [MS-DOC] documentation
	unsigned int offset = 0;
	bool isFound = false;
	while (bytes >= offset + 2) {
		switch (OleUtil::getU2Bytes(grpprlBuffer, chpxOffset + offset)) {
			case 0x080a: // ole object, p.107 [MS-DOC]
				if (OleUtil::getU1Byte(grpprlBuffer, chpxOffset + offset + 2) == 0x01) {
					return false;
				}
				break;
			case 0x0806: // is not a picture, but a binary data? (sprmCFData, p.106 [MS-DOC])
				if (OleUtil::getU4Bytes(grpprlBuffer, chpxOffset + offset + 2) == 0x01) {
					return false;
				}
				break;
//			case 0x0855: // sprmCFSpec, p.117 [MS-DOC], MUST BE applied with a value of 1 (see p.105 [MS-DOC])
//				if (OleUtil::getU1Byte(grpprlBuffer, chpxOffset + offset + 2) != 0x01) {
//					return false;
//				}
//				break;
			case 0x6a03: // location p.105 [MS-DOC]
				pictureInfo.dataPos = OleUtil::getU4Bytes(grpprlBuffer, chpxOffset + offset + 2);
				isFound = true;
				break;
			default:
				break;
		}
		offset += getPrlLength(grpprlBuffer, chpxOffset + offset);
	}
	return isFound;
}

OleMainStream::Style OleMainStream::getStyleFromStylesheet(unsigned int istd, const StyleSheet &stylesheet) {
	//TODO optimize it: StyleSheet can be map structure with istd key
	Style style;
	if (istd != ISTD_INVALID && istd != STI_NIL && istd != STI_USER) {
		for (size_t index = 0; index < stylesheet.size(); ++index) {
			if (stylesheet.at(index).istd == istd) {
				return stylesheet.at(index);
			}
		}
	}
	style.istd = istd;
	return style;
}

int OleMainStream::getStyleIndex(unsigned int istd, const std::vector<bool> &isFilled, const StyleSheet &stylesheet) {
	//TODO optimize it: StyleSheet can be map structure with istd key
	//in that case, this method will be excess
	if (istd == ISTD_INVALID) {
		return -1;
	}
	for (int index = 0; index < (int)stylesheet.size(); ++index) {
		if (isFilled.at(index) && stylesheet.at(index).istd == istd) {
			return index;
		}
	}
	return -1;
}

unsigned int OleMainStream::getIstdByCharPos(unsigned int charPos, const StyleInfoList &styleInfoList) {
	unsigned int istd = ISTD_INVALID;
	for (size_t i = 0; i < styleInfoList.size(); ++i) {
		const Style &info = styleInfoList.at(i).second;
		if (i == styleInfoList.size() - 1) { //if last
			istd = info.istd;
			break;
		}
		unsigned int curOffset = styleInfoList.at(i).first;
		unsigned int nextOffset = styleInfoList.at(i + 1).first;
		if (charPos >= curOffset && charPos < nextOffset) {
			istd = info.istd;
			break;
		}
	}
	return istd;
}

bool OleMainStream::offsetToCharPos(unsigned int offset, unsigned int &charPos, const Pieces &pieces) {
	if (pieces.empty()) {
		return false;
	}
	if ((unsigned int)pieces.front().offset > offset) {
		charPos = 0;
		return true;
	}
	if ((unsigned int)(pieces.back().offset + pieces.back().length) <= offset) {
		return false;
	}

	size_t pieceNumber = 0;
	for (size_t i = 0; i < pieces.size(); ++i) {
		if (i == pieces.size() - 1) { //if last
			pieceNumber = i;
			break;
		}
		unsigned int curOffset = pieces.at(i).offset;
		unsigned int nextOffset = pieces.at(i + 1).offset;
		if (offset >= curOffset && offset < nextOffset) {
			pieceNumber = i;
			break;
		}
	}

	const Piece &piece = pieces.at(pieceNumber);
	unsigned int diffOffset = offset - piece.offset;
	if (!piece.isANSI) {
		diffOffset /= 2;
	}
	charPos = piece.startCP + diffOffset;
	return true;
}

bool OleMainStream::readToBuffer(std::string &result, unsigned int offset, size_t length, OleStream &stream) {
	char *buffer = new char[length];
	stream.seek(offset, true);
	if (stream.read(buffer, length) != length) {
		return false;
	}
	result = std::string(buffer, length);
	delete[] buffer;
	return true;
}

unsigned int OleMainStream::calcCountOfPLC(unsigned int totalSize, unsigned int elementSize) {
	//calculates count of elements in PLC structure, formula from p.30 [MS-DOC]
	return (totalSize - 4) / (4 + elementSize);
}

unsigned int OleMainStream::getPrlLength(const char *grpprlBuffer, unsigned int byteNumber) {
	unsigned int tmp;
	unsigned int opCode = OleUtil::getU2Bytes(grpprlBuffer, byteNumber);
	switch (opCode & 0xe000) {
		case 0x0000:
		case 0x2000:
			return 3;
		case 0x4000:
		case 0x8000:
		case 0xA000:
			return 4;
		case 0xE000:
			return 5;
		case 0x6000:
			return 6;
		case 0xC000:
			//counting of info length
			tmp = OleUtil::getU1Byte(grpprlBuffer, byteNumber + 2);
			if (opCode == 0xc615 && tmp == 255) {
				unsigned int del = OleUtil::getU1Byte(grpprlBuffer, byteNumber + 3);
				unsigned int add = OleUtil::getU1Byte(grpprlBuffer, byteNumber + 4 + del * 4);
				tmp = 2 + del * 4 + add * 3;
			}
			return 3 + tmp;
		default:
			return 1;
	}
}
