package cn.itcreator.android.reader.views;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import cn.itcreator.android.reader.domain.BookMark;
import cn.itcreator.android.reader.domain.TxtLine;
import cn.itcreator.android.reader.io.ReadFileRandom;
import cn.itcreator.android.reader.paramter.CR;
import cn.itcreator.android.reader.paramter.Constant;
import cn.itcreator.android.reader.util.ObjectUtil;

public class CustomCopyOfTextReader {
	/** Used to display data to the screen components */
	private CustomTextView customViewInTextReader = null;
	/** the context of application */
	private Context mContext = null;

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

	/** ����ȡ�ļ� */
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
	/** ����ʾ�����п��������Ҫ����ʼ���� */
	private int mStartOffset = 0;
	/** ����ʾ�����п��������Ҫ�Ľ������� */
	private int mEndOffset = 0;
	/*** ��Ļ�������ʾ���������е���ʼ���� */
	private int mDataStartLocation = 0;
	/*** ��Ļ�������ʾ���������еĽ������� */
	private int mDataEndLocation = 0;

	/**
	 * Construction methods for CustomCopyOfTextReader
	 * 
	 * @param fileName
	 * @param myCustomView
	 * @param screenWidth
	 * @param screenHeigth
	 */
	public CustomCopyOfTextReader(CustomTextView myCustomView,
			CustomViewActivity c, String fileName, int screenWidth,
			int screenHeigth, String encoding2) {
		// TODO Auto-generated constructor stub

		this.mFileName = fileName;
		this.customViewInTextReader = myCustomView;
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

		/** ȡ���ļ��ĳ��� */
		this.mFileLength = mReadFileRandom.getFileLength();

		if (this.mFileLength == 0) {
			customViewInTextReader.setText(Constant.NODATAINFILE);
			return;
		}

		/** Initialization screen shows a number of rows of data */
		this.mLinesOfOneScreen = mViewHeigth / (CR.fontHeight + CR.lineSpace);

		readNextBuffer();// Read a buffer
		analysisDisplayBuffer();// Analysis of buffer
		displayNextToScreen(0, null);
	}

	/**
	 * Read the next buffer
	 */
	public void readNextBuffer() {
		// �������Ϣ�ļ���
		/*
		 * int myLineSize = mMyLines.size(); if(myLineSize !=0){
		 * //���Ϊ��Ļ����Ǿ��ǵ�һ�ζ�ȡ�����Ϊ�㣬����µĶ���Ȼ���������ٶ�ȡ //���µ�������
		 * mReadFileRandom.openNewStream(); TxtLine t =
		 * mMyLines.get(mCurrentLine); //��Ҫ���ĳ��� int skipLength = t.offset -
		 * t.lineLength; mReadFileRandom.fastSkip(skipLength); }
		 */
		byte[] b = new byte[mDataLengthOfOneDoc];
		// long currentLocation = mReadFileRandom.getCurrentLocation();

		// ���õ�ǰ��ʾ��ݵ�һ���ַ����ļ��е�ƫ����
		mCurrentOffset = (int) mStartOffset;
		// ����ֽ���
		mReadFileRandom.openNewStream();
		mReadFileRandom.fastSkip(mStartOffset);

		// ��ȡ������Ѿ�������Ļ���
		int actualLength = mReadFileRandom.readBytes(b);// ��ȡ���

		if (mStartOffset == 0) {// ���ڵ�һ���ĵ���
			mBeforeOfDoc = true;
		} else {
			mBeforeOfDoc = false;
		}
		if (actualLength < mDataLengthOfOneDoc) {// ��ȡ�������
			mEndOfDoc = true;
		} else {
			mEndOfDoc = false;
		}

		if (actualLength == -1 && mScreenData.length == 0) {// �ļ���û����ݣ���Ļ��Ҳû�����
			customViewInTextReader.setText("�ļ���û�����");
			return;
		}

		if (mEndOfDoc) {// ���������ļ�β������Ҫ����������ֱ���������
			mDisplayBuffer = new byte[actualLength];
			System.arraycopy(b, 0, mDisplayBuffer, 0, actualLength);
			b = null;
			System.gc();
			return;
		}

		// �������ϲ��ҵ�һ�γ��ֻ��е�λ�ã����ҷ�Ϊ�ϲ�������ݺ��²�������ݣ������²��������
		int readDataLength = actualLength;
		int nlocation = 0;// ���г��ֵ�λ��
		while (readDataLength > 0) {
			if ((b[readDataLength - 1] & 0xff) == 10) {
				nlocation = readDataLength;
				break;
			}
			readDataLength--;
		}

		if (nlocation == 0) {// 100K ����ݶ�û�л��У����ܶ�ȡ
			System.exit(1);
		}

		/** ����µ���ʾ���� */
		int mDisplayBufferLength = nlocation;
		mDisplayBuffer = new byte[mDisplayBufferLength];

		/** ���ƶ�ȡ������ */
		System.arraycopy(b, 0, mDisplayBuffer, 0, mDisplayBufferLength);
		b = null;
		System.gc();
	}

	/** Read the previous buffer */
	public void readPreBuffer() {

		// ��ȡ��ǰ��Ļ��ݵ�һ�е�ƫ����
		// ��Ļ���һ�е�����ַ����ļ��е�ƫ����
		int x = mCurrentLine + this.mLinesOfOneScreen;
		int offsetOfLastLineInScreen = 0;
		int sizeLines = mMyLines.size();
		if (x > sizeLines) {// ���Խ����
			offsetOfLastLineInScreen = mMyLines.get(sizeLines - 1).offset;
		} else {
			offsetOfLastLineInScreen = mMyLines.get(x).offset;
		}

		// ��ǰ�е�ƫ�����Ƿ�������Ҫ��ȡ����ݷ�Χ�ڣ�����ǵĻ����ʹ��ļ�ͷ��ȡ
		if (offsetOfLastLineInScreen <= mDataLengthOfOneDoc) {
			mBeforeOfDoc = true;
			if (offsetOfLastLineInScreen == mFileLength) {
				mEndOfDoc = true;
			}
			byte[] b = new byte[offsetOfLastLineInScreen];
			// ��������
			mReadFileRandom.openNewStream();
			int readDataLength = mReadFileRandom.readBytes(b);

			mDisplayBuffer = new byte[readDataLength];
			System.arraycopy(b, 0, mDisplayBuffer, 0, readDataLength);
			// ���õ�ǰ��mDisplayBuffer�ĵ�һ���ַ��ƫ����
			mCurrentOffset = 0;
			b = null;
			System.gc();
			return;
		}
		// �������·�������һ�����ֻ��е�λ�ã�����ȡ����ˣ���ô�Ͳ��ý��з����ˣ�������mBeforeOfDocΪtrue
		// ����������ݴ������ǹ涨�Ķ�ȡ���ȣ���ô�ʹ��м�������Ҫ�ĳ��ȣ����ҽ��в��һ��в���
		// ��Ҫ���ĳ���
		int skipLength = offsetOfLastLineInScreen - mDataLengthOfOneDoc;
		mReadFileRandom.openNewStream();
		// ��λ���˴�
		mReadFileRandom.locate(skipLength);
		// ���õ�ǰ��mDisplayBuffer�ĵ�һ���ַ��ƫ����
		mCurrentOffset = skipLength;
		byte[] b = new byte[mDataLengthOfOneDoc];
		int readLength = mReadFileRandom.readBytes(b);
		// �϶������ڵ�һ�����ĵ�
		mBeforeOfDoc = false;
		// �Ƿ������һ���ĵ�
		if (readLength < mDataLengthOfOneDoc) {
			mEndOfDoc = true;
		}

		// �ض���ݣ���Ϊ�ϲ�������ݺ��²�������ݣ����������������
		int nlocation = 0;// ���г��ֵ�λ��
		while (nlocation < readLength) {
			if ((b[nlocation - 1] & 0xff) == 10) {
				break;
			}
			nlocation++;
		}
		if (nlocation == readLength) {// 100k �����û�� �ҵ�һ�����У�ֱ���˳�
			System.exit(1);
		}

		// ������ʾ�������
		mDisplayBuffer = new byte[readLength];
		System.arraycopy(b, 0, mDisplayBuffer, 0, readLength);
		b = null;
		System.gc();

	}

	/**
	 * 
	 * ʹ���ļ���ƫ�������������
	 * 
	 * @param offset
	 *            ������ļ��е�ƫ����
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
		displayNextToScreen(0, null);
	}

	/**
	 * display data to up
	 * 
	 * @param n
	 */
	private void displayPreToScreen(int n, Animation animation) {
		String tag = "displayPreToScreen";
		// ȡ��mCurrentLine��Ϣ���Ƿ�Ϊ0�����Ϊ0��˵������ʾ�����е����
		int tempCurrentLine = mCurrentLine;// ��ʱ���浱ǰ��
		int futureLine = tempCurrentLine - n;// δ�����е�λ��
		Log.d(tag, "futureLine : " + futureLine);
		if (futureLine < 0) {// Խ����
			futureLine = 0;
		}

		Log.d(tag, "mCurrentLine:" + mCurrentLine);
		Log.d(tag, "futureLine:" + futureLine);
		Log.d(tag, "mBeforeOfDoc:" + mBeforeOfDoc);
		Log.d(tag, "mEndOfDoc:" + mEndOfDoc);
		Log.d(tag, "mCurrentLine:" + mCurrentLine);

		// �Ƿ���Ҫ�����µĻ��棬����ĵ��Ƿ��ڵ�һ���ĵ��ж�
		if (futureLine == 0 && !mBeforeOfDoc) {// ��Ҫ�����»���
			// Log.d(tag, "futureLine ==0 && !mBeforeOfDoc");
			readPreBuffer();
			analysisDisplayBuffer();
			Log.d(tag, "futureLine ==0 && !mBeforeOfDoc");
			int lastLine = mLinesOfOneScreen - 1;// ��Ļ������һ�е�����
			if (lastLine > mMyLines.size()) {// �����һ����Ļ������
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
			Log.d(tag, "mDataStartLocation �� " + mDataStartLocation);
			Log.d(tag, "mDataEndLocation : " + mDataEndLocation);
			/*
			 * System.out.println("��ȡ���µĻ��岢��ʾ");
			 * System.out.println("mCurrentLine: "+mCurrentLine);
			 * System.out.println("lastLine: "+lastLine);
			 * System.out.println("mStartOffset �� " + mStartOffset);
			 * System.out.println("mDataStartLocation �� " +
			 * mDataStartLocation); System.out.println("mDataEndLocation : " +
			 * mDataEndLocation);
			 */
			setData(mStartOffset, mDataEndLocation, animation);

			return;

		}

		if (futureLine == 0 && mBeforeOfDoc) {
			// �ĵ��Ѿ�����һ���ˣ������к�Ҳ�ڵ�һ��
			mCurrentLine = 0;
			mStartOffset = 0;
			mDataStartLocation = 0;
			int lastLine = mLinesOfOneScreen - 1;// ��Ļ������һ�е�����
			if (lastLine > mMyLines.size()) {// �����һ����Ļ������
				mEndOffset = mMyLines.get(mMyLines.size() - 1).offset;
				mDataEndLocation = mMyLines.get(mMyLines.size() - 1).beforeLineLength;
			} else {
				mEndOffset = mMyLines.get(lastLine).offset;
				mDataEndLocation = mMyLines.get(lastLine).beforeLineLength;
			}

			Log.d(tag, "futureLine ==0 && mBeforeOfDoc");

			Log.d(tag, "mDataStartLocation : " + mDataStartLocation);
			Log.d(tag, "mDataEndLocation : " + mDataEndLocation);
			// System.out.println("�ĵ��Ѿ�����һ���ˣ������к�Ҳ�ڵ�һ��");
			setData(mDataStartLocation, mDataEndLocation, animation);
			return;
		}

		if (futureLine > 0) {// ˵�����ϻ��ܹ��������Ƿ�ҳ
			int lastLine = futureLine + mLinesOfOneScreen;// ���һ��
			if (lastLine >= mMyLines.size()) {// ��ֹȡ������ݵ�ʱ��Խ��
				lastLine = mMyLines.size() - 1;
			}
			mCurrentLine = futureLine;
			mStartOffset = mMyLines.get(futureLine).offset;
			mDataStartLocation = mMyLines.get(futureLine).beforeLineLength;
			mEndOffset = mMyLines.get(lastLine).offset;
			mDataEndLocation = mMyLines.get(lastLine).beforeLineLength;
			Log.d(tag, "futureLine > 0");

			Log.d(tag, "mDataStartLocation �� " + mDataStartLocation);
			Log.d(tag, "mDataEndLocation : " + mDataEndLocation);
			// System.out.println("˵�����ϻ��ܹ��������Ƿ�ҳ");
			setData(mDataStartLocation, mDataEndLocation, animation);
		}

		// �����Ҫ�����µĻ��棬��ô�ʹ���ݻ����п�����ݵ���Ļ������

	}

	public void displayScreenbyLineWithAnimation(int lineNumber,
			Animation animation) {
		if (lineNumber > 0) {
			this.displayNextToScreen(lineNumber, animation);
		} else {
			this.displayPreToScreen(Math.abs(lineNumber), animation);
		}

	}

	/**
	 * display data to down
	 * 
	 * @param n
	 *            If n is 0, the data line is 0 to mLlinesOfOneScreen, if the n
	 *            is 1, the data line is 1 to mLlinesOfOneScreen+1, as this
	 */
	private void displayNextToScreen(int n, Animation animation) {
		String tag = "displayNextToScreen";
		// �ж� mCurrentLine����ʲôλ�ã�������ʾ�᲻�ᳬ��mMyLines��������
		int tempCurrentLine = mCurrentLine;// ��ʱ���浱ǰ��
		int lastLineIndex = mMyLines.size() - 1;// ����ж����ڼ����е�����
		int futureLine = tempCurrentLine + n;// δ����ʼ��

		// ����mCurrentLineԽ��mMyLines������������
		// 1�����mCurrentLineԽ��mMyLines�����������ĵ��������һҳ����ô����mCurrentLine����
		// 2�����mCurrentLineԽ��mMyLines��������,�����ĵ����������һҳ����ȡ�¸�����
		if (futureLine + mLinesOfOneScreen > lastLineIndex) {
			if (!mEndOfDoc) {// �����ĵ������
				// ��Ҫ�����ĵ�
				Log.d(tag, "read new buffer when skip...");
				readNextBuffer();
				analysisDisplayBuffer();
				mCurrentLine = n;// �ӵ�N�п�ʼ��ʾ
				lastLineIndex = mMyLines.size() - 1;
				// ��Ļ�����ʼƫ����
				mStartOffset = mMyLines.get(mCurrentLine - 1).offset;
				// ��Ļ�������ʾ�����е�����
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

				setData(mDataStartLocation, mDataEndLocation, animation);
				return;
			}
			if (mEndOfDoc) {// �������һ���ĵ�
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

					setData(mDataStartLocation, mDataEndLocation, animation);
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

					setData(mDataStartLocation, mDataEndLocation, animation);
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

			setData(mDataStartLocation, mDataEndLocation, animation);
			return;
		}

		// ��mMyLines������ȡ����ʼ�ж���ͽ�β�ж���
		// ͨ����ʼ�ж���ͽ�β�ж����е�offsetȡ�������mDisplayBuffer��λ��

		// ������ݵ�mScreenData������

	}

	/**
	 * ����TextView�е�����
	 * 
	 * @param start
	 *            ��ʼoffset
	 * @param end
	 *            ���� offset
	 */
	private void setData(int start, int end, Animation animation) {
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

			customViewInTextReader.setText(new String(mScreenData,
					this.encoding));
			if (animation != null) {
				customViewInTextReader.startAnimation(animation);
			}
			customViewInTextReader.invalidate();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

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
			customViewInTextReader.setText(new String(mScreenData,
					this.encoding));
			customViewInTextReader.invalidate();
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

				if (!(b >= 65 && b <= 90)) {// ���Ǵ�д��ĸ
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
	 * �ж��ǲ��ǵ����ĵ������һҳ
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
	 * ���ص�ǰ��Ļ��һ�����ļ���ƫ����
	 * 
	 * @return
	 */
	public int getCurrentLineOffset() {
		return mStartOffset;
	}

	/**
	 * ȡ�õ�ǰ��Ļ��һ�е����
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
