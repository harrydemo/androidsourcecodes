/**
 * <This class for read txt file.>
 *  Copyright (C) <2009>  <Wang XinFeng,ACC http://androidos.cc/dev>
 *
 *   This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package cn.itcreator.android.reader.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.itcreator.android.reader.domain.BookMark;
import cn.itcreator.android.reader.domain.TxtLine;
import cn.itcreator.android.reader.io.ReadFile;
import cn.itcreator.android.reader.paramter.CR;
import cn.itcreator.android.reader.paramter.Constant;



import android.content.Context;
import android.util.Log;
import android.widget.TextView;


/**
 * This class for read txt file
 * @author Wang XinFeng
 * @version 1.0
 *
 */
public class TxtReader {

	/** Used to display data to the screen components */
	private TextView mTextView = null;
	/**the context of app*/
	private Context mContext = null;
	/** Read from the document how much data */
	private int mReadLength = 65536;

	/** To read the files */
	private String mFileName = null;

	/** The wide-screen and high  */
	private int mScreenWidth, mScreenHeigth;

	/** Visual wide and high, for rotating the screen, for the time being does not support */
	private int mViewWidth, mViewHeigth;

	/**A screen can store data on the number of line */
	private int mLinesOfOneScreen;

	/** Read file object */
	private ReadFile mReadFile;

	/** To read the length of the document */
	private long mFileLength;

	/** At present, the starting line */
	private  int mCurrentLine = 0;

	/** The first line in the current view of migration */
	private  int mCurrentOffset = 0;

	/** Save for the line and offset a collection of objects */
	private List<TxtLine> mMyLines = new ArrayList<TxtLine>();

	/** The documents show that the data used to buffer */
	private byte[] mDisplayBuffer;

	/** Save the tail of the buffer zone data buffer */
	private byte[] mEndBuffer = new byte[0];

	/** For the preservation of the current screen data */
	private byte[] mScreenData = new byte[0];

	/** is the document is the last page ?*/
	private boolean mEndOfDoc = false;

	/** is the document is the front page ?*/
	private boolean mBeforeOfDoc = true;

	/** To preserve a collection of bookmarks */
	private List<BookMark> bookMarkList = new ArrayList<BookMark>();

	/** Read for the collection and preservation of the bookmark-type object */
	private ObjectUtil objectUtil = null;
	
//	private XMLUtil xmlutil = null;
	
	/** Save bookmark file path and name of*/
	private String mMarkFileName = null;

	
	/**Read up data to skip when the number of bytes*/
	private int mSkipLength = 0;
	
	/**Whether it is Page Up, used to control analysis of the cache when the offset*/
	private boolean isToUp = false;
	
	/**The file encoding*/
	private String encoding = "GB2312";
	
	/**Percentage*/
	private int mPercent=0;
	
	
	/**Database operator*/
	private CRDBHelper mDBHelper;
	
	/**
	 * Construction methods
	 * 
	 * @param fileName
	 * @param textView
	 * @param screenWidth
	 * @param screenHeigth
	 */
	public TxtReader(TextView textView,Context c, String fileName, int screenWidth,
			int screenHeigth,String encoding) {
		this.mFileName = fileName;
		this.mTextView = textView;
		this.mContext = c;
		this.mScreenWidth = screenWidth;
		this.mScreenHeigth = screenHeigth;
		this.encoding = encoding;
		/** Start initialization */
		init();
	}

	/**
	 * The entire system initialization
	 */
	private void init() {
		this.mViewHeigth = mScreenHeigth;
		this.mViewWidth = mScreenWidth;

		/** Initialization file to read the object */
		try {
			this.mReadFile = new ReadFile(this.mFileName);
		} catch (FileNotFoundException e) {
			mTextView.setText(Constant.FILENOTFOUND);
		}

		/** Bookmark file name */
		this.mMarkFileName = this.mFileName + ".m";

		objectUtil = new ObjectUtil(this.mMarkFileName);
		//xmlutil = new XMLUtil(this.mMarkFileName);

		/** Length of the document obtained*/
		this.mFileLength = mReadFile.fileSize();
		if(this.mFileLength ==0){
			mTextView.setText(Constant.NODATAINFILE);
			return;
		}

		/** Initialization screen shows a number of rows of data */
		this.mLinesOfOneScreen = mViewHeigth / (CR.fontHeight + CR.lineSpace);

		readBookMark();// Open the bookmark
		readNextBuffer();//Read a buffer
		analysisDisplayBuffer();//Analysis of buffer
		displayNextToScreen(0);

	}

	/**
	 * Read the next buffer
	 */
	public void readNextBuffer() {
		// Analysis of data on the screen
		if (mMyLines.size() != 0 && mCurrentLine != 0) {
			/** At present, the length of the line before the data */
			int beforeDataLength = mMyLines.get(mCurrentLine).beforeLineLength;
			/** The current display buffer length*/
			int totalDisplayBufferLength = mDisplayBuffer.length;
			/** Screen data */
			mScreenData = new byte[totalDisplayBufferLength - beforeDataLength];
			/** Copy the data from mDisplayBuffer to screen buffer*/
			System.arraycopy(mDisplayBuffer, beforeDataLength, mScreenData, 0,
					mScreenData.length);
		}

		// Analysis of data on end
		/** Ends data length*/
		int endBufferLength = 0;
		if (null != mEndBuffer) {
			/** Get the ends data length */
			endBufferLength = mEndBuffer.length;
		}

		// Read data from file
		byte[] readData = new byte[0];
		if (null != mReadFile)
			/** Start read data */
			readData = mReadFile.read(mReadLength);
		/** Read end? */
		if (readData.length != mReadLength) {
			/** set the mEndOfDoc true */
			mEndOfDoc = true;
			/**display buffer length is:current screen data length + ends data length + actual length of data */
			int displayBufferLength = mScreenData.length + endBufferLength
					+ readData.length;

			mDisplayBuffer = new byte[displayBufferLength];

			/** Add screen data  to display buffer */
			System.arraycopy(mScreenData, 0, mDisplayBuffer, 0,
					mScreenData.length);

			/** Add end data  to display buffer */
			System.arraycopy(mEndBuffer, 0, mDisplayBuffer, mScreenData.length,
					mEndBuffer.length);

			/** Will read all the data into the display buffer   */
			System.arraycopy(readData, 0, mDisplayBuffer, displayBufferLength
					- readData.length, readData.length);
			readData = null;
			System.gc();
			return;
		}

		/** Did not read the final document, it is necessary to analyze the tail of data through \n */
		mEndOfDoc = false;
		// Analysis of data from the rear
		/** end For the logo to read data from the first time after the forward \ n Position */
		int end = readData.length;
		if (end > 0) {
			while (true) {
				if (end < 1) {
					break;
				}
				int v = readData[end - 1] & 0xff;
				if (v == 10) { // \n 
					break;
				}
				end--;
			}
		}
		if (0 == end) { 
			// The event of a serious error, even in the absence of a 16K data carriage return
			System.exit(-1);
		}

		// All the data integration

		/** Add to display the data in the buffer length: the length of the data screen, the last legacy of the length of the data, analysis of the \ n location  */
		int displayBufferLength = mScreenData.length + endBufferLength + end;

		mDisplayBuffer = new byte[displayBufferLength];

	/** Add screen data to display buffer */
		System.arraycopy(mScreenData, 0, mDisplayBuffer, 0, mScreenData.length);

		/** Add ends data to display buffer*/
		System.arraycopy(mEndBuffer, 0, mDisplayBuffer, mScreenData.length,
				mEndBuffer.length);

		/** Will read all the data into the display buffer */
		System.arraycopy(readData, 0, mDisplayBuffer,
				displayBufferLength - end, end);

		/** Analysis of the data after the remaining length */
		int remainDataLength = mReadLength - end;
		/** Regenerate the tail of the data buffer */
		mEndBuffer = new byte[remainDataLength];
		/** Analysis of data to the tail to tail into the buffer */
		System.arraycopy(readData, end, mEndBuffer, 0, remainDataLength);
		readData = null;
		System.gc();
	}


	
	
	/**Read the previous buffer*/
	public void readPreBuffer(){
		byte[] b = new byte[0];

		/** Number of rows in the first place, and not on the first page of the document */
		if (mCurrentLine == 0 && !mBeforeOfDoc) {
			/** Skip to close  before the previous flow, and then skip to deal with */
			mReadFile.close();
			/** The new generation of document stream*/
			try {
				mReadFile = new ReadFile(this.mFileName);
			} catch (FileNotFoundException e) {
				mTextView.setText(Constant.FILENOTFOUND);
			}
			/** Skip to the length of the data */
			mSkipLength = mMyLines.get(mCurrentLine).offset-mReadLength;
			/** If the current line of offset and the length equal to the current line on the page in the document */
			if (mSkipLength  <= 0) {
				mSkipLength = 0;
				mBeforeOfDoc = true;
			}
			
			/** Skip the beginning of the number of */
			try {
				mReadFile.skip((int) mSkipLength);
			} catch (FileNotFoundException e) {
				mTextView.setText(Constant.FILENOTFOUND);
			}


			if (null != mReadFile){
				//In front of the data to determine whether there is a  mReadLength
				//If not, then get the actual length of the
				if(mSkipLength==0){
					Log.d("readPreBuffer2", "before data < mReadLength");
				//	b = mReadFile.read(mMyLines.get(mCurrentLine).offset-mMyLines.get(mCurrentLine).beforeLineLength);
					
					b = mReadFile.read(mMyLines.get(mCurrentLine+mLinesOfOneScreen).offset);
				}else{
					/** Began to read data */
					b = mReadFile.read(mReadLength);

				}
			}
				
			/**Deal with data from front to end, when found the \n first*/
			int firstN = 0;
			
			if (firstN > 0) {
				while (true) {
					if (firstN < 1) {
						break;
					}
					int v = b[firstN - 1] & 0xff;
					if (v == 10) { // \n 
						break;
					}
					firstN++;
				}
			}
			
			//The data contain front data and end data, we need end data
			// Delete front data, load the current screen data
			
			/**From front to end ,first \n, front data's length*/
			int frontDataLength = firstN;
			/** End data's length */
			int endDataLength = b.length - firstN;

			/**Set the mSkipLenght , read previous contains the front data and the end data*/
			/**But mSkipLength is only when read file when need skip length,when analysis buffer must add the front data,there are the real length*/
			mSkipLength = mSkipLength+firstN;
		//	mEndBuffer = new byte[frontDataLength];
			/**Copy the end's data to the end's data buffer */

	//		System.arraycopy(b, frontDataLength, mEndBuffer, 0, endDataLength);
			/**Current display buffer length is ： end's buffer length + current screen's data length*/
			mDisplayBuffer= new byte[endDataLength ];
			
			/**Copy the end's data to the display buffer*/
			System.arraycopy(b, 0, mDisplayBuffer, frontDataLength, endDataLength);
			b = null;
			frontDataLength = 0;
			System.gc();
		}
	}
	
	/**
	 * Analysis display buffer
	 */
	public void analysisDisplayBuffer() {
		if (null == mDisplayBuffer) {
			return;
		}
		if(0!=mMyLines.size()){
			mCurrentOffset = mMyLines.get(mCurrentLine).offset;
		}
		mMyLines.clear();
		int length = 0;
		int offset = 0;
		int width = 0;
		int beforeLineLength = 0;// The length of before current line
		for (offset = 0; offset < mDisplayBuffer.length;) {
			int b = mDisplayBuffer[offset] & 0xff;
			if (b == 13) {//Use the blank instead of  \r
				mDisplayBuffer[offset] = ' ';
			}
			if (b == 10) {// \n
				length++;
				offset++;
				beforeLineLength++;
				if(isToUp){
					/**If scroll up, the current offset is : read file skip length + current line length */
					mMyLines.add(new TxtLine(mSkipLength + offset, length,
							beforeLineLength));
				}else{
					/**Scroll down*/
					mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
							beforeLineLength));
				}
				length = 0;
				continue;
			}
			if (b > 0x7f) {// Chinese
				if (width + CR.ChineseFontWidth > mViewWidth) {// If the line length more than view width
					/** Add to list */
					if(isToUp){
						mMyLines.add(new TxtLine(mSkipLength + offset, length,
								beforeLineLength));
					}else{
						mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
								beforeLineLength));
					}
					length = 0;
					width = 0;
					continue;
				} else {
					offset += 2;
					length += 2;
					beforeLineLength += 2;
					width += CR.ChineseFontWidth;
				}

			} else {// Ascii
				if (width + CR.upperAsciiWidth > mViewWidth) {

				
					if(isToUp){
						mMyLines.add(new TxtLine(mSkipLength + offset, length,
								beforeLineLength));
					}else{
						mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
								beforeLineLength));
					}
					length = 0;
					width = 0;
					continue;
				} else {
					offset += 1;
					length += 1;
					beforeLineLength += 1;
					width += CR.upperAsciiWidth;
				}
			}
		}
		// Add the last line
		if(isToUp){
			mMyLines.add(new TxtLine(mSkipLength + offset, length,
					beforeLineLength));
		}else{
			mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
					beforeLineLength));
		}
		mCurrentLine = 0;
		System.gc();
	}

	/**
	 * display data to up
	 * 
	 * @param n
	 */
	public void displayPreToScreen(int n) {
		/**Set the to up page*/
		isToUp = true;
		/** Set the current line */
		mCurrentLine = mCurrentLine - n;
		if (mCurrentLine < 0) {
			Log.d("displayPreToScreen", "set the mCurrentLine is 0.........");
			mCurrentLine = 0;
		}
		/**If current line is 0, load new buffer from up*/
		if (mCurrentLine == 0 && !mBeforeOfDoc) {
			mCurrentLine = 0;
			Log.d("displayPreToScreen", "read pre data.........");
			readPreBuffer();
			analysisDisplayBuffer();
			int lastLine = mMyLines.size();
			
			/**Length before the last line */
			int lastLineLength = mMyLines.get(lastLine - 1).beforeLineLength;

			/**Deal with the file data length less than a screen data*/
			if (lastLine <= mLinesOfOneScreen) {
				mCurrentLine = 0;
			} else {
				mCurrentLine = lastLine - mLinesOfOneScreen;
			}

			int currentLineLength = mMyLines.get(mCurrentLine).beforeLineLength;

			/**Calculate the screen data length when display to screen*/
			int len = 0;
			if (lastLine <= mLinesOfOneScreen) {
				len = lastLineLength;
			} else {
				len = lastLineLength - currentLineLength;
			}

			mScreenData = new byte[len];
			System.arraycopy(mDisplayBuffer, currentLineLength, mScreenData, 0,
					len);

			try {
				mTextView.setText(new String(mScreenData, this.encoding));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.gc();
			
			/**computer the percent*/
			double start = (mMyLines.get(mCurrentLine).offset+mScreenData.length);
			double total = mFileLength;
			mPercent = ((int)((start/total)*100));	
			return;
		}

		/**If the current are not less than 0, have data before, move the line number */
		if (mCurrentLine >=0) {
			Log.d("displayPreToScreen", "display data to screen .........");
			int lastLine = 0;
			if (mMyLines.size() <= mLinesOfOneScreen) {
				lastLine = mMyLines.size();

			} else {
				lastLine = mCurrentLine + mLinesOfOneScreen;
			}

			int currentLineLength = mMyLines.get(mCurrentLine).beforeLineLength;
			int lastLineLength = mMyLines.get(lastLine - 1).beforeLineLength;

			int len = 0;
			if (mCurrentLine == 0) {
				len = lastLineLength;
				mScreenData = new byte[len];
				System.arraycopy(mDisplayBuffer, 0, mScreenData, 0, len);

			} else {
				len = lastLineLength - currentLineLength;

				mScreenData = new byte[len];
				System.arraycopy(mDisplayBuffer, currentLineLength,
						mScreenData, 0, len);

			}

			try {
				mTextView.setText(new String(mScreenData, this.encoding));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			/**computer the percent*/
			double start = (mMyLines.get(mCurrentLine).offset+mScreenData.length);
			double total = mFileLength;
			mPercent = ((int)((start/total)*100));	
			return;

		}
	}

	/**
	 * display data to down
	 * 
	 * @param n
	 *            If n is 0, the data line is 0 to mLlinesOfOneScreen, if the n is 1, the data line is 1 to mLlinesOfOneScreen+1, as this
	 */
	public void displayNextToScreen(int n) {
		/**up? false is down*/
		isToUp = false;
		/** Get current line's offset */
		if (mCurrentLine == mMyLines.size()) {
			mCurrentOffset = ((TxtLine) mMyLines.get(0)).offset;
		}

		if (mCurrentLine < mMyLines.size()) {
			mCurrentOffset = ((TxtLine) mMyLines.get(mCurrentLine)).offset;
		}
		
		
		/**is the first of document? */
		mBeforeOfDoc = (((TxtLine) mMyLines.get(0)).offset - ((TxtLine) mMyLines
				.get(0)).lineLength) == 0;
		
		/**is the last document*/
		if(mFileLength == mMyLines.get(mMyLines.size()-1).offset){
			mEndOfDoc=true;
		}else{
			mEndOfDoc=false;
		}
	
	
		int tempLines = mCurrentLine;
		/** move down */
		mCurrentLine = mCurrentLine + n;
		
		
		Log.d("displayNextToScreen", ":::::"+ (mCurrentLine + mLinesOfOneScreen >= mMyLines.size()));
		Log.d("displayNextToScreen", ":::::====="+ mEndOfDoc);
		
		
		/**If to last line, and is last document, don't move lines*/
		if (mCurrentLine + mLinesOfOneScreen >= mMyLines.size() && mEndOfDoc) {
			mCurrentLine = tempLines;
		}

		/**If the data less than the screen when display to screen ,load the buffer*/
		if (mCurrentLine + mLinesOfOneScreen >= mMyLines.size() && !mEndOfDoc) {
			readNextBuffer();
			analysisDisplayBuffer();
			mBeforeOfDoc = false;
		}
		
		if (mCurrentLine == 0 && mMyLines.size() <= mLinesOfOneScreen
				&& mBeforeOfDoc) {
			/**Get the last line data length*/
			int lastDataLength = mMyLines.get(mMyLines.size() - 1).beforeLineLength;
			mScreenData = new byte[lastDataLength];
			System.arraycopy(mDisplayBuffer, 0, mScreenData, 0, lastDataLength);
			try {
				mTextView.setText(new String(mScreenData, this.encoding));
			} catch (UnsupportedEncodingException e) {
			}
			
			/**computer the percent*/
			double start = (mMyLines.get(mCurrentLine).offset+mScreenData.length);
			double total = mFileLength;
			mPercent = ((int)((start/total)*100));				
			System.gc();
			return;
		}

		int temp = mCurrentLine;
		if (temp + mLinesOfOneScreen <= mMyLines.size()) {

			int lastLine = temp + mLinesOfOneScreen;

			mCurrentOffset = mMyLines.get(temp).offset;

			/**Get the data length before this current line*/
			int beforeDataLength = mMyLines.get(temp).beforeLineLength;

			int lastDataLength = mMyLines.get(lastLine).beforeLineLength;

			int len = 0;
			if (mCurrentLine - n == 0) {
				len = lastDataLength;
				mScreenData = new byte[len];
				System.arraycopy(mDisplayBuffer, 0, mScreenData, 0, len);

			} else {
				len = lastDataLength - beforeDataLength;
				mScreenData = new byte[len];
				System.arraycopy(mDisplayBuffer, beforeDataLength, mScreenData,
						0, len);

			}
			try {
				mTextView.setText(new String(mScreenData, this.encoding));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			/**computer the percent*/
			double start = (mMyLines.get(mCurrentLine).offset+mScreenData.length);
			double total = mFileLength;
			mPercent = ((int)((start/total)*100));	
			System.gc();
		}
	}

	/**
	 * Read book marks
	 */
	public void readBookMark() {
		mDBHelper = new CRDBHelper(mContext);
		Object o = objectUtil.fileToObject();
		//bookMarkList = xmlutil.fileToList();
		if (o != null) {
			if (o instanceof List) {
				bookMarkList = (List<BookMark>) o;
			}
		
			/**Book mark size*/
			int booksize = bookMarkList.size();
			if (booksize != 0) {
				/**Last book mark*/
				mCurrentOffset = bookMarkList.get(booksize - 1)
						.getCurrentOffset();
				/**Remove last book mark*/
				bookMarkList.remove(booksize - 1);
			}
			try {
				/**Skip last book mark*/
				mReadFile.skip(mCurrentOffset);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		o = null;
		System.gc();
		}
	}

	/**
	 * Save book mark
	 */
	public boolean saveBookMark() {
		byte[] b = new byte[0];
		int start = 0;
		if(mMyLines.size()>0 ){
			if(mCurrentLine==0){
				b = new byte[mMyLines.get(mCurrentLine).lineLength];
				mCurrentOffset = mMyLines.get(mCurrentLine).offset - b.length;
				start =  0;
			}else{
				b = new byte[mMyLines.get(mCurrentLine-1).lineLength];
				mCurrentOffset = mMyLines.get(mCurrentLine-1).offset;
				start  = mCurrentLine-2;
				if(start>0){
					start=mMyLines.get(mCurrentLine-1).beforeLineLength-mMyLines.get(mCurrentLine-2).beforeLineLength;
				}else{
					start=0;
				}
			}
			
		}else{
			mCurrentOffset=0;
			b = new byte[mMyLines.get(mCurrentLine).lineLength];
		}
		System.arraycopy(mScreenData, start, b, 0,b.length);
		String bookMarkName = Constant.BOOKMARK;
		try {
			bookMarkName = new String(b,this.encoding);
		} catch (UnsupportedEncodingException e) {
			bookMarkName = Constant.BOOKMARK;
		}
		
		bookMarkList.add(new BookMark(mCurrentOffset,bookMarkName,Constant.BOOK_ID_IN_DATABASE));
		boolean x = objectUtil.saveToFile(bookMarkList);
	//	boolean x = xmlutil.saveToFile(bookMarkList);
		b =null;
		bookMarkName= null;
		System.gc();
		return x;
	}

	
	/**Remove the book mark*/
	public boolean deleteBookMark(){
		File f = new File(mMarkFileName);
		return f.delete();
	}
	
	public List<TxtLine> getList() {
		return mMyLines;
	}
/**
 * 判断是不是到了文档的最后一页
 * @return
 */
	public boolean isEnd() {
		return mEndOfDoc;
	}

	public void close() {
		mReadFile.close();
		mDBHelper.close();
	}

	public int getMLinesOfOneScreen() {
		return this.mLinesOfOneScreen;
	}

	
	public long getFileLength(){
		return mFileLength;
	}
	
	/**Percent*/
	public int getPercent(){
		return mPercent;
	}
}
