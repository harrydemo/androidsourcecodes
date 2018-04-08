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
import cn.itcreator.android.reader.io.ReadFileRandom;
import cn.itcreator.android.reader.paramter.CR;
import cn.itcreator.android.reader.paramter.Constant;
import cn.itcreator.android.reader.views.CustomTextView;
import cn.itcreator.android.reader.views.CustomViewActivity;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

/**
 * This class for read txt file
 * 
 * @author Wang XinFeng
 * @version 1.0
 * 
 */
public class CopyOfTxtReader {
	/** Used to display data to the screen components */
	private TextView mTextView = null;
	/** the context of app */
	private Context mContext = null;
	/** 一个文档中存放的数据大小，也就是一次行读取多大的数据 */
	private int mDataLengthOfOneDoc = 100 * 1024;// 100k

	/** To read the files */
	private String mFileName = null;

	/** The wide-screen and high */
	private int mScreenWidth, mScreenHeigth;

	/**
	 * Visual wide and high, for rotating the screen, for the time being does
	 * not support
	 */
	private int mViewWidth, mViewHeigth;

	/** A screen can store data on the number of line */
	private int mLinesOfOneScreen;

	/** 随机读取文件 */
	private ReadFileRandom mReadFileRandom;

	/** To read the length of the document */
	private long mFileLength;

	/** At present, the starting line */
	private int mCurrentLine = 0;

	/** The first line in the current view of migration */
	private int mCurrentOffset = 0;

	/** Save for the line and offset a collection of objects */
	private List<TxtLine> mMyLines = new ArrayList<TxtLine>();

	/** The documents show that the data used to buffer */
	private byte[] mDisplayBuffer;

	/** For the preservation of the current screen data */
	private byte[] mScreenData = new byte[0];

	/** is the document is the last page ? */
	private boolean mEndOfDoc = false;

	/** is the document is the front page ? */
	private boolean mBeforeOfDoc = true;

	/** To preserve a collection of bookmarks */
	private List<BookMark> bookMarkList = new ArrayList<BookMark>();

	/** Read for the collection and preservation of the bookmark-type object */
	private ObjectUtil objectUtil = null;

	// private XMLUtil xmlutil = null;

	/** Save bookmark file path and name of */
	private String mMarkFileName = null;

	/** The file encoding */
	private String encoding = "GB2312";

	/** Percentage */
	private int mPercent = 0;
	/** 从显示缓存中拷贝数据需要的起始索引 */
	private int mStartOffset = 0;
	/** 从显示缓存中拷贝数据需要的结束索引 */
	private int mEndOffset = 0;
	/*** 屏幕数据在显示缓存数组中的起始索引 */
	private int mDataStartLocation = 0;
	/*** 屏幕数据在显示缓存数组中的结束索引 */
	private int mDataEndLocation = 0;

	/**
	 * Construction methods
	 * 
	 * @param fileName
	 * @param textView
	 * @param screenWidth
	 * @param screenHeigth
	 */
	public CopyOfTxtReader(TextView textView, Context c, String fileName,
			int screenWidth, int screenHeigth, String encoding) {
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

		/*
		 * try { this.mReadFile = new ReadFile(this.mFileName); } catch
		 * (FileNotFoundException e) { mTextView.setText(Constant.FILENOTFOUND);
		 * }
		 */
		this.mReadFileRandom = new ReadFileRandom(this.mFileName);

		/** Bookmark file name */
		this.mMarkFileName = this.mFileName + ".m";

		objectUtil = new ObjectUtil(this.mMarkFileName);
		// xmlutil = new XMLUtil(this.mMarkFileName);

		/** 取得文件的长度 */
		this.mFileLength = mReadFileRandom.getFileLength();

		if (this.mFileLength == 0) {
			mTextView.setText(Constant.NODATAINFILE);
			return;
		}

		/** Initialization screen shows a number of rows of data */
		this.mLinesOfOneScreen = mViewHeigth / (CR.fontHeight + CR.lineSpace);

		readNextBuffer();// Read a buffer
		analysisDisplayBuffer();// Analysis of buffer
		displayNextToScreen(0);
	}

	/**
	 * Read the next buffer
	 */
	public void readNextBuffer() {
		// 存放行信息的集合
		/*
		 * int myLineSize = mMyLines.size(); if(myLineSize !=0){
		 * //如果为零的话，那就是第一次读取，如果不为零，生成新的对象，然后进行跳过再读取 //打开新的输入流
		 * mReadFileRandom.openNewStream(); TxtLine t =
		 * mMyLines.get(mCurrentLine); //需要跳过的长度 int skipLength = t.offset -
		 * t.lineLength; mReadFileRandom.fastSkip(skipLength); }
		 */
		byte[] b = new byte[mDataLengthOfOneDoc];
		// long currentLocation = mReadFileRandom.getCurrentLocation();

		// 设置当前显示数据第一个字符在文件中的偏移量
		mCurrentOffset = (int) mStartOffset;
		// 跳过字节数
		mReadFileRandom.openNewStream();
		mReadFileRandom.fastSkip(mStartOffset);

		// 读取的数据已经包含了屏幕数据
		int actualLength = mReadFileRandom.readBytes(b);// 读取数据

		if (mStartOffset == 0) {// 处在第一个文档上
			mBeforeOfDoc = true;
		} else {
			mBeforeOfDoc = false;
		}
		if (actualLength < mDataLengthOfOneDoc) {// 读取到最后了
			mEndOfDoc = true;
		} else {
			mEndOfDoc = false;
		}

		if (actualLength == -1 && mScreenData.length == 0) {// 文件中没有数据，屏幕中也没有数据
			mTextView.setText("文件中没有数据");
			return;
		}

		if (mEndOfDoc) {// 如果读到了文件尾，不需要分析换行了直接跳出即可
			mDisplayBuffer = new byte[actualLength];
			System.arraycopy(b, 0, mDisplayBuffer, 0, actualLength);
			b = null;
			System.gc();
			return;
		}

		// 从下往上查找第一次出现换行的位置，并且分为上部有用数据和下部废弃数据，舍弃下部废弃数据
		int readDataLength = actualLength;
		int nlocation = 0;// 换行出现的位置
		while (readDataLength > 0) {
			if ((b[readDataLength - 1] & 0xff) == 10) {
				nlocation = readDataLength;
				break;
			}
			readDataLength--;
		}

		if (nlocation == 0) {// 100K 的数据都没有换行，不能读取
			System.exit(1);
		}

		/** 生成新的显示缓存 */
		int mDisplayBufferLength = nlocation;
		mDisplayBuffer = new byte[mDisplayBufferLength];

		/** 复制读取数据数据 */
		System.arraycopy(b, 0, mDisplayBuffer, 0, mDisplayBufferLength);
		b = null;
		System.gc();
	}

	/** Read the previous buffer */
	public void readPreBuffer() {

		// 读取当前屏幕数据第一行的偏移量
		// 屏幕最后一行的最后字符在文件中的偏移量
		int x = mCurrentLine + this.mLinesOfOneScreen;
		int offsetOfLastLineInScreen = 0;
		int sizeLines = mMyLines.size();
		if (x > sizeLines) {// 如果越界了
			offsetOfLastLineInScreen = mMyLines.get(sizeLines - 1).offset;
		} else {
			offsetOfLastLineInScreen = mMyLines.get(x).offset;
		}

		// 当前行的偏移量是否在我们要读取的数据范围内，如果是的话，就从文件头读取
		if (offsetOfLastLineInScreen <= mDataLengthOfOneDoc) {
			mBeforeOfDoc = true;
			if (offsetOfLastLineInScreen == mFileLength) {
				mEndOfDoc = true;
			}
			byte[] b = new byte[offsetOfLastLineInScreen];
			// 打开输入流
			mReadFileRandom.openNewStream();
			int readDataLength = mReadFileRandom.readBytes(b);

			mDisplayBuffer = new byte[readDataLength];
			System.arraycopy(b, 0, mDisplayBuffer, 0, readDataLength);
			// 设置当前的mDisplayBuffer的第一个字符的偏移量
			mCurrentOffset = 0;
			b = null;
			System.gc();
			return;
		}
		// 从上往下分析出第一个出现换行的位置，如果读取到最顶端，那么就不用进行分析了，并设置mBeforeOfDoc为true
		// 如果上面的数据大于我们规定的读取长度，那么就从中间我们需要的长度，并且进行查找换行操作
		// 需要跳过的长度
		int skipLength = offsetOfLastLineInScreen - mDataLengthOfOneDoc;
		mReadFileRandom.openNewStream();
		// 定位到此处
		mReadFileRandom.locate(skipLength);
		// 设置当前的mDisplayBuffer的第一个字符的偏移量
		mCurrentOffset = skipLength;
		byte[] b = new byte[mDataLengthOfOneDoc];
		int readLength = mReadFileRandom.readBytes(b);
		// 肯定不处于第一个的文档
		mBeforeOfDoc = false;
		// 是否处于最后一个文档
		if (readLength < mDataLengthOfOneDoc) {
			mEndOfDoc = true;
		}

		// 截断数据，分为上部废弃数据和下部有用数据，舍弃上面废弃的数据
		int nlocation = 0;// 换行出现的位置
		while (nlocation < readLength) {
			if ((b[nlocation - 1] & 0xff) == 10) {
				break;
			}
			nlocation++;
		}
		if (nlocation == readLength) {// 100k 的数据没有 找到一个换行，直接退出
			System.exit(1);
		}

		// 建立显示缓存数据
		mDisplayBuffer = new byte[readLength];
		System.arraycopy(b, 0, mDisplayBuffer, 0, readLength);
		b = null;
		System.gc();

	}

	/**
	 * 
	 * 使用文件的偏移量来加载数据
	 * 
	 * @param offset
	 *            数据在文件中的偏移量
	 */
	public void readBufferByOffset(int offset) {
		String tag = "readBufferByOffset";

		Log.d(tag, "read the data by offset");
		Log.d(tag, "offset is :" + offset);
		mStartOffset = offset;
		mMyLines.clear();
		TxtLine t = new TxtLine(offset, 0, 0);

		mCurrentLine = 0;
		mMyLines.add(t);
		readNextBuffer();
		analysisDisplayBuffer();
		displayNextToScreen(0);
	}

	/**
	 * display data to up
	 * 
	 * @param n
	 */
	public void displayPreToScreen(int n) {
		String tag = "displayPreToScreen";
		// 取得mCurrentLine信息，是否为0，如果为0，说处在显示缓存中的最顶端
		int tempCurrentLine = mCurrentLine;// 临时保存当前行
		int futureLine = tempCurrentLine - n;// 未来首行的位置
		Log.d(tag, "futureLine : " + futureLine);
		if (futureLine < 0) {// 越界了
			futureLine = 0;
		}

		Log.d(tag, "mCurrentLine:" + mCurrentLine);
		Log.d(tag, "futureLine:" + futureLine);
		Log.d(tag, "mBeforeOfDoc:" + mBeforeOfDoc);
		Log.d(tag, "mEndOfDoc:" + mEndOfDoc);
		Log.d(tag, "mCurrentLine:" + mCurrentLine);

		// 是否需要加载新的缓存，根据文档是否处于第一个文档判断
		if (futureLine == 0 && !mBeforeOfDoc) {// 需要加载新缓存
			// Log.d(tag, "futureLine ==0 && !mBeforeOfDoc");
			readPreBuffer();
			analysisDisplayBuffer();
			Log.d(tag, "futureLine ==0 && !mBeforeOfDoc");
			int lastLine = mLinesOfOneScreen - 1;// 屏幕数据最后一行的索引
			if (lastLine > mMyLines.size()) {// 数据连一个屏幕都不够
				Log.d(tag, "lastLine  : " + lastLine);
				Log.d(tag, "mMyLines.size():" + mMyLines.size());
				mStartOffset = 0;
				mDataStartLocation = mMyLines.get(0).beforeLineLength;
				mEndOffset = mMyLines.get(mMyLines.size() - 1).offset;
				mDataEndLocation = mMyLines.get(mMyLines.size() - 1).beforeLineLength;
			} else {
				mCurrentLine = mMyLines.size() - mLinesOfOneScreen;
				if (mCurrentLine < 0) {
					Log.d(tag, "set the mCurrentLine is 0 ....");
					mCurrentLine = 0;
				}
				mStartOffset = mMyLines.get(mCurrentLine).offset;
				mDataStartLocation = mMyLines.get(mCurrentLine).beforeLineLength;
				mEndOffset = mMyLines.get(mMyLines.size() - 1).offset;
				mDataEndLocation = mMyLines.get(mMyLines.size() - 1).beforeLineLength;
			}

			Log.d(tag, "mCurrentLine : " + mCurrentLine);
			Log.d(tag, "mDataStartLocation ： " + mDataStartLocation);
			Log.d(tag, "mDataEndLocation : " + mDataEndLocation);
			/*
			 * System.out.println("读取了新的缓冲并显示");
			 * System.out.println("mCurrentLine: "+mCurrentLine);
			 * System.out.println("lastLine: "+lastLine);
			 * System.out.println("mStartOffset ： " + mStartOffset);
			 * System.out.println("mDataStartLocation ： " + mDataStartLocation);
			 * System.out.println("mDataEndLocation : " + mDataEndLocation);
			 */
			setData(mStartOffset, mDataEndLocation);

			return;

		}

		if (futureLine == 0 && mBeforeOfDoc) {
			// 文档已经到第一个了，并且行号也在第一个
			mCurrentLine = 0;
			mStartOffset = 0;
			mDataStartLocation = 0;
			int lastLine = mLinesOfOneScreen - 1;// 屏幕数据最后一行的索引
			if (lastLine > mMyLines.size()) {// 数据连一个屏幕都不够
				mEndOffset = mMyLines.get(mMyLines.size() - 1).offset;
				mDataEndLocation = mMyLines.get(mMyLines.size() - 1).beforeLineLength;
			} else {
				mEndOffset = mMyLines.get(lastLine).offset;
				mDataEndLocation = mMyLines.get(lastLine).beforeLineLength;
			}

			Log.d(tag, "futureLine ==0 && mBeforeOfDoc");

			Log.d(tag, "mDataStartLocation : " + mDataStartLocation);
			Log.d(tag, "mDataEndLocation : " + mDataEndLocation);
			// System.out.println("文档已经到第一个了，并且行号也在第一个");
			setData(mDataStartLocation, mDataEndLocation);
			return;
		}

		if (futureLine > 0) {// 说明向上还能滚屏或者是翻页
			int lastLine = futureLine + mLinesOfOneScreen;// 最后一行
			if (lastLine >= mMyLines.size()) {// 防止取集合数据的时候越界
				lastLine = mMyLines.size() - 1;
			}
			mCurrentLine = futureLine;
			mStartOffset = mMyLines.get(futureLine).offset;
			mDataStartLocation = mMyLines.get(futureLine).beforeLineLength;
			mEndOffset = mMyLines.get(lastLine).offset;
			mDataEndLocation = mMyLines.get(lastLine).beforeLineLength;
			Log.d(tag, "futureLine > 0");

			Log.d(tag, "mDataStartLocation ： " + mDataStartLocation);
			Log.d(tag, "mDataEndLocation : " + mDataEndLocation);
			// System.out.println("说明向上还能滚屏或者是翻页");
			setData(mDataStartLocation, mDataEndLocation);
		}

		// 如果不需要加载新的缓存，那么就从数据缓存中拷贝数据的屏幕缓存中

	}

	/**
	 * display data to down
	 * 
	 * @param n
	 *            If n is 0, the data line is 0 to mLlinesOfOneScreen, if the n
	 *            is 1, the data line is 1 to mLlinesOfOneScreen+1, as this
	 */
	public void displayNextToScreen(int n) {
		String tag = "displayNextToScreen";
		// 判断 mCurrentLine处在什么位置，向下显示会不会超过mMyLines集合总数
		int tempCurrentLine = mCurrentLine;// 临时保存当前行
		int lastLineIndex = mMyLines.size() - 1;// 最后行对象在集合中的索引
		int futureLine = tempCurrentLine + n;// 未来起始行

		// 处理mCurrentLine越出mMyLines集合总数的情况
		// 1、如果mCurrentLine越出mMyLines集合总数，并且文档处在最后一页，那么设置mCurrentLine不变
		// 2、如果mCurrentLine越出mMyLines集合总数,并且文档不处在最后一页，读取下个缓存
		if (futureLine + mLinesOfOneScreen > lastLineIndex) {
			if (!mEndOfDoc) {// 不在文档的最后
				// 需要加载文档
				Log.d(tag, "read new buffer when skip...");
				readNextBuffer();
				analysisDisplayBuffer();
				mCurrentLine = n;// 从第N行开始显示
				lastLineIndex = mMyLines.size() - 1;
				// 屏幕数据起始偏移量
				mStartOffset = mMyLines.get(mCurrentLine - 1).offset;
				// 屏幕数据在显示缓存中的索引
				mDataStartLocation = mMyLines.get(mCurrentLine - 1).beforeLineLength;
				if (lastLineIndex + 1 < mLinesOfOneScreen) {
					//
					mEndOffset = mMyLines.get(lastLineIndex).offset;
					mDataEndLocation = mMyLines.get(lastLineIndex).beforeLineLength;
				} else {
					int i = mCurrentLine + mLinesOfOneScreen;
					if (i >= mMyLines.size()) {
						i = mMyLines.size() - 1;
					}
					mEndOffset = mMyLines.get(i).offset;
					mDataEndLocation = mMyLines.get(i).beforeLineLength;
				}
				Log
						.d(tag,
								"futureLine+mLinesOfOneScreen > lastLineIndex !mEndOfDoc ");
				Log.d(tag, "mDataStartLocation is :" + mDataStartLocation);
				Log.d(tag, "mDataEndLocation is :" + mDataEndLocation);

				setData(mDataStartLocation, mDataEndLocation);
				return;
			}
			if (mEndOfDoc) {// 处在最后一个文档
				if (lastLineIndex <= mLinesOfOneScreen) {
					if (mCurrentLine == 0) {
						mStartOffset = mMyLines.get(mCurrentLine).offset;
					} else {
						mStartOffset = mMyLines.get(mCurrentLine).offset;
						mDataStartLocation = mMyLines.get(mCurrentLine).beforeLineLength;
					}
					mEndOffset = mMyLines.get(lastLineIndex).offset;
					mDataEndLocation = mMyLines.get(lastLineIndex).beforeLineLength;
					Log.d(tag, "lastLineIndex<=mLinesOfOneScreen mEndOfDoc ");
					Log.d(tag, "mDataStartLocation is :" + mDataStartLocation);
					Log.d(tag, "mDataEndLocation is :" + mDataEndLocation);

					setData(mDataStartLocation, mDataEndLocation);
					return;
				} else {
					mStartOffset = mMyLines.get(mCurrentLine).offset;
					mDataStartLocation = mMyLines.get(mCurrentLine).beforeLineLength;

					mEndOffset = mMyLines.get(lastLineIndex).offset;
					mDataEndLocation = mMyLines.get(lastLineIndex).beforeLineLength;

					mCurrentLine = lastLineIndex - mLinesOfOneScreen;
					Log.d(tag,
							"  !(lastLineIndex<=mLinesOfOneScreen) mEndOfDoc ");
					Log.d(tag, "mDataStartLocation is :" + mDataStartLocation);
					Log.d(tag, "mDataEndLocation is :" + mDataEndLocation);

					setData(mDataStartLocation, mDataEndLocation);
					return;
				}
			}
		}

		if (futureLine + mLinesOfOneScreen <= lastLineIndex) {
			mCurrentLine = futureLine;
			if (mCurrentLine == 0) {
				mStartOffset = mMyLines.get(mCurrentLine).offset;
				mDataStartLocation = mMyLines.get(mCurrentLine).beforeLineLength;
			} else {
				mStartOffset = mMyLines.get(mCurrentLine - 1).offset;
				mDataStartLocation = mMyLines.get(mCurrentLine - 1).beforeLineLength;
			}
			mEndOffset = mMyLines.get(futureLine + mLinesOfOneScreen).offset;
			mDataEndLocation = mMyLines.get(futureLine + mLinesOfOneScreen).beforeLineLength;

			Log.d(tag, "futureLine+mLinesOfOneScreen <= lastLineIndex ");
			Log.d(tag, "mDataStartLocation is :" + mDataStartLocation);
			Log.d(tag, "mDataEndLocation is :" + mDataEndLocation);

			setData(mDataStartLocation, mDataEndLocation);
			return;
		}

		// 从mMyLines集合中取得起始行对象和结尾行对象
		// 通过起始行对象和结尾行对象中的offset取得数据在mDisplayBuffer的位置

		// 拷贝数据到mScreenData数组中

	}

	/**
	 * 设置TextView中的内容
	 * 
	 * @param start
	 *            起始offset
	 * @param end
	 *            结束 offset
	 */
	private void setData(int start, int end) {
		String tag = "setData";
		Log.d(tag, "start index is :" + start);
		Log.d(tag, "end index is :" + end);
		mScreenData = new byte[end - start];
		mCurrentOffset = mStartOffset;
		mPercent = (int) (((double) end / (double) mFileLength) * 100);

		System.arraycopy(mDisplayBuffer, start, mScreenData, 0,
				mScreenData.length);
		try {
			Log.d("setData:", new String(mScreenData, this.encoding));
			mTextView.setText(new String(mScreenData, this.encoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Analysis display buffer
	 */
	public void analysisDisplayBuffer() {
		if (null == mDisplayBuffer) {
			return;
		}
		mMyLines.clear();
		int length = 0;
		int offset = 0;
		int width = 0;
		int beforeLineLength = 0;// The length of before current line
		for (offset = 0; offset < mDisplayBuffer.length;) {
			int b = mDisplayBuffer[offset] & 0xff;
			if (b == 13) {// Use the blank instead of \r
				mDisplayBuffer[offset] = ' ';
			}
			if (b == 10) {// \n
				length++;
				offset++;
				beforeLineLength++;
				/** Scroll down */
				mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
						beforeLineLength));
				length = 0;
				continue;
			}
			if (b > 0x7f) {// Chinese
				if (width + CR.ChineseFontWidth > mViewWidth) {// If the line
					// length
					// more than view width
					mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
							beforeLineLength));
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
				int aw = CR.upperAsciiWidth;

				if (!(b >= 65 && b <= 90)) {// 不是大写字母
					aw = CR.lowerAsciiWidth;
				}
				if (width + aw > mViewWidth) {

					mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
							beforeLineLength));
					length = 0;
					width = 0;
					continue;
				} else {
					offset += 1;
					length += 1;
					beforeLineLength += 1;
					width += aw;
				}
			}
		}
		// Add the last line
		mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
				beforeLineLength));
		mCurrentLine = 0;
		System.gc();
	}

	public List<TxtLine> getList() {
		return mMyLines;
	}

	/**
	 * 判断是不是到了文档的最后一页
	 * 
	 * @return
	 */
	public boolean isEnd() {
		return mEndOfDoc;
	}

	public void close() {
		mReadFileRandom.close();
	}

	public int getMLinesOfOneScreen() {
		return this.mLinesOfOneScreen;
	}

	public long getFileLength() {
		return mFileLength;
	}

	/** Percent */
	public int getPercent() {
		return mPercent;
	}

	/**
	 * 返回当前屏幕第一行在文件的偏移量
	 * 
	 * @return
	 */
	public int getCurrentLineOffset() {
		return mStartOffset;
	}

	/**
	 * 取得当前屏幕第一行的数据
	 * 
	 * @return
	 */
	public String getCurrentLineString() {
		int length = mScreenData.length;
		String s = Constant.BOOKMARK;
		if (length < 10) {
			try {
				s = new String(mScreenData, this.encoding);
			} catch (UnsupportedEncodingException e) {
				return s;
			}
		} else {
			byte[] b = new byte[10];
			System.arraycopy(mScreenData, 0, b, 0, b.length);
			try {
				s = new String(b, this.encoding);
			} catch (UnsupportedEncodingException e) {
				return s;
			}
		}
		System.gc();
		return s;
	}
}
