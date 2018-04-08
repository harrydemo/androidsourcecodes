package org.void1898.www.agilebuddy.data;

import org.void1898.www.agilebuddy.material.UIModel;

/**
 * 
 * @author void1898@gmail.com
 * 
 * @version 1.2.3
 * 
 */
public class Footboard {

	// ���Ͻ�X����
	private int mX;

	// ���Ͻ�Y����(Y��������ֵ=mY/Y���������ܶ�)
	private int mVirtualY;

	// ���
	private int mWidth;

	// �߶�
	private int mHeith;

	// ����(����̤��,�󻬰�,�һ���,����̤��,���ɰ�,���ȶ�̤��)
	private int mType;

	// ��֡��
	private int mFrameAmount;

	// ֡�ӳ�
	private int mFrameDelay;

	// ֡�ӳ�ʱ�������
	private int mFrameCounter;

	// ���ȶ�̤�������ʱ��
	private int mUnstableBoardDelay;

	public int getVirtualY() {
		return mVirtualY;
	}

	public int getMinX() {
		return mX;
	}

	public int getMaxX() {
		return mX + mWidth;
	}

	public int getMinY() {
		return mVirtualY / UIModel.GAME_ATTRIBUTE_PIXEL_DENSITY_Y;
	}

	public int getMaxY() {
		return mVirtualY / UIModel.GAME_ATTRIBUTE_PIXEL_DENSITY_Y + mHeith;
	}

	public int getType() {
		return mType;
	}

	public void addY(int virtualPixel) {
		mVirtualY += virtualPixel;
	}

	public boolean isBoardBreak() {
		return --mUnstableBoardDelay == 0;
	}

	public boolean isMarked() {
		return mUnstableBoardDelay != UIModel.BOARD_ATTRIBUTE_UNSTABLE_DELAY_FACTOR;
	}

	public int nextFrame() {
		try {
			return mFrameCounter / mFrameDelay;
		} finally {
			mFrameCounter++;
			if (mFrameCounter == mFrameAmount * mFrameDelay) {
				mFrameCounter = 0;
			}
		}
	}

	public Footboard(int x, int y, int width, int heith, int type,
			int frameAmount, int frameDelay) {
		mX = x;
		mVirtualY = y * UIModel.GAME_ATTRIBUTE_PIXEL_DENSITY_Y;
		mWidth = width;
		mHeith = heith;
		mType = type;
		mFrameAmount = frameAmount;
		mFrameDelay = frameDelay;
		mUnstableBoardDelay = UIModel.BOARD_ATTRIBUTE_UNSTABLE_DELAY_FACTOR;
		mFrameCounter = 0;
	}
}
