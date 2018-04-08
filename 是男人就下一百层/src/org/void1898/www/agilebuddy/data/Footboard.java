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

	// 左上角X坐标
	private int mX;

	// 左上角Y坐标(Y坐标像素值=mY/Y方向像素密度)
	private int mVirtualY;

	// 宽度
	private int mWidth;

	// 高度
	private int mHeith;

	// 类型(常规踏板,左滑板,右滑板,带刺踏板,弹簧板,不稳定踏板)
	private int mType;

	// 总帧数
	private int mFrameAmount;

	// 帧延迟
	private int mFrameDelay;

	// 帧延迟时间计算器
	private int mFrameCounter;

	// 不稳定踏板可滞留时间
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
