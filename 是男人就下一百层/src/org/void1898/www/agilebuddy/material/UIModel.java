package org.void1898.www.agilebuddy.material;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.void1898.www.agilebuddy.data.Food;
import org.void1898.www.agilebuddy.data.Footboard;
import org.void1898.www.agilebuddy.data.Role;
import org.void1898.www.agilebuddy.data.ScreenAttribute;

/**
 * 
 * @author void1898@gmail.com
 * 
 * @version 1.2.3
 * 
 */
public class UIModel {

	/**
	 * 游戏属性常量
	 */

	// 帧刷新间隔(单位微妙)
	public static final int GAME_ATTRIBUTE_FRAME_DELAY = 30;

	// 游戏活动对象Y方向的像素密度(将1个单位像素拆分为更小单元)
	public static final int GAME_ATTRIBUTE_PIXEL_DENSITY_Y = 10;

	// 游戏等级提升因数
	public static final int GAME_ATTRIBUTE_LEVEL_UP_FACTOR = 40;

	// 重力速度(即主角离开踏板后的y方向速度)
	public static final int GAME_ATTRIBUTE_GRAVITY_VELOCITY = 5 * GAME_ATTRIBUTE_PIXEL_DENSITY_Y;

	// 游戏状态
	public static final int GAME_STATUS_PAUSE = 0;
	public static final int GAME_STATUS_RUNNING = 1;
	public static final int GAME_STATUS_GAMEOVER = 2;

	// 游戏效果标识(用来控制不同音效和震动的标志)
	public static final int EFFECT_FLAG_NO_EFFECT = 0;
	public static final int EFFECT_FLAG_NORMAL = 1;
	public static final int EFFECT_FLAG_UNSTABLE = 2;
	public static final int EFFECT_FLAG_SPRING = 3;
	public static final int EFFECT_FLAG_SPIKED = 4;
	public static final int EFFECT_FLAG_MOVING = 5;
	public static final int EFFECT_FLAG_TOOLS = 6;

	/**
	 * 主角属性常量
	 */

	// 主角的长度和宽度
	public static final int ROLE_ATTRIBUTE_WIDTH = 32;
	public static final int ROLE_ATTRIBUTE_HEITH = 48;

	// 主角帧刷新间隔
	public static final int ROLE_ATTRIBUTE_FRAME_DELAY = 2;

	// 主角最大生命值
	public static final int ROLE_ATTRIBUTE_HP_MAX = 12;

	// 主角状态
	public static final int ROLE_STATUS_ON_FOOTBOARD = 0;
	public static final int ROLE_STATUS_ON_FOOTBOARD_LEFT = 1;
	public static final int ROLE_STATUS_ON_FOOTBOARD_RIGHT = 2;
	public static final int ROLE_STATUS_FREEFALL = 3;
	public static final int ROLE_STATUS_FREEFALL_LEFT = 4;
	public static final int ROLE_STATUS_FREEFALL_RIGHT = 5;

	// 主角帧
	public static final int ROLE_SHARP_STANDING = 0;
	public static final int ROLE_SHARP_FREEFALL_NO1 = 1;
	public static final int ROLE_SHARP_FREEFALL_NO2 = 2;
	public static final int ROLE_SHARP_FREEFALL_NO3 = 3;
	public static final int ROLE_SHARP_FREEFALL_NO4 = 4;
	public static final int ROLE_SHARP_MOVE_LEFT_NO1 = 5;
	public static final int ROLE_SHARP_MOVE_LEFT_NO2 = 6;
	public static final int ROLE_SHARP_MOVE_LEFT_NO3 = 7;
	public static final int ROLE_SHARP_MOVE_LEFT_NO4 = 8;
	public static final int ROLE_SHARP_MOVE_RIGHT_NO1 = 9;
	public static final int ROLE_SHARP_MOVE_RIGHT_NO2 = 10;
	public static final int ROLE_SHARP_MOVE_RIGHT_NO3 = 11;
	public static final int ROLE_SHARP_MOVE_RIGHT_NO4 = 12;

	/**
	 * 道具属性常量
	 */

	// 加分物品在屏幕上的滞留时间
	public static final int FOOD_ATTRIBUTE_DELAY_TIME = 450;

	// 加分物品的大小
	public static final int FOOD_ATTRIBUTE_IMAGE_SIZE = 24;

	// 踏板的长宽
	public static final int BORDER_ATTRIBUTE_IMAGE_HEITH = 20;
	public static final int BORDER_ATTRIBUTE_IMAGE_WIDTH = 100;

	// 踏板偏向速度
	public static final int BOARD_ATTRIBUTE_LEFT_VELOCITY = -4;
	public static final int BOARD_ATTRIBUTE_RIGHT_VELOCITY = 4;

	// 不稳定踏板滞留因数(可滞留时间=滞留因数*帧刷新间隔)
	public static final int BOARD_ATTRIBUTE_UNSTABLE_DELAY_FACTOR = 10;

	// 踏板类别
	public static final int FOOTBOARD_TYPE_NORMAL = 0;
	public static final int FOOTBOARD_TYPE_UNSTABLE = 1;
	public static final int FOOTBOARD_TYPE_SPRING = 2;
	public static final int FOOTBOARD_TYPE_SPIKED = 3;
	public static final int FOOTBOARD_TYPE_MOVING_LEFT = 4;
	public static final int FOOTBOARD_TYPE_MOVING_RIGHT = 5;

	// 加分物品类别
	public static final int FOOD_NONE = 0;
	public static final int FOOD_8 = 4;
	public static final int FOOD_7 = 6;
	public static final int FOOD_6 = 8;
	public static final int FOOD_5 = 10;
	public static final int FOOD_4 = 12;
	public static final int FOOD_3 = 14;
	public static final int FOOD_2 = 16;
	public static final int FOOD_1 = 20;

	/**
	 * 游戏属性
	 */

	// 游戏界面属性
	private ScreenAttribute mScreenAttribute;

	// 游戏状态
	public int mGameStatus = GAME_STATUS_RUNNING;

	// 游戏得分
	private int mScore = 0;

	// 当前难度等级
	private int mLevel = 1;

	// 生命值
	private int mHP = ROLE_ATTRIBUTE_HP_MAX;

	// 游戏等级提升计算器(等级计算器值等于等级提升因数时游戏等级提升1级,等级计算器重置为零)
	private int mLevelUpCounter = 0;

	// 随机数生成器
	private Random mRan = new Random();

	// 游戏效果标志(用于处理主角动作效果,比如:震动,音效)
	private int mEffectFlag = EFFECT_FLAG_NO_EFFECT;

	/**
	 * 游戏主角属性
	 */

	// 游戏主角
	private Role mRole;

	// 主角X方向移动速度
	private int mRoleVelocityX;

	// 主角Y方向移动速度
	private int mRoleVelocityY;
	// 附加速度(用于控制速度,在选项面板里设定)
	private int mAddVelocity;

	/**
	 * 道具属性
	 */

	// 楼梯间隔距离因数(间隔距离(px)=因数/Y方向像素因数)
	private int mFootboardSpaceFactor = 120 * GAME_ATTRIBUTE_PIXEL_DENSITY_Y;

	// 移动间隔计算器
	private int mFootboardSpaceCounter = 0;

	// 踏板移动速度
	private int mFootboartVelocity = -3 * GAME_ATTRIBUTE_PIXEL_DENSITY_Y;

	// 踏板列表
	private LinkedList<Footboard> mFootboardList;

	private Food mCurFood;

	public UIModel(ScreenAttribute screenAttribute, int addVelocity) {
		mScreenAttribute = screenAttribute;
		mAddVelocity = addVelocity;
		mRole = new Role((screenAttribute.maxX - ROLE_ATTRIBUTE_WIDTH) / 2,
				screenAttribute.maxY * 3 / 4, ROLE_ATTRIBUTE_WIDTH,
				ROLE_ATTRIBUTE_HEITH, ROLE_ATTRIBUTE_FRAME_DELAY);
		mRoleVelocityY = GAME_ATTRIBUTE_GRAVITY_VELOCITY;
		mFootboardList = new LinkedList<Footboard>();
		mFootboardList.add(new Footboard(
				(screenAttribute.maxX - BORDER_ATTRIBUTE_IMAGE_WIDTH) / 2,
				screenAttribute.maxY, BORDER_ATTRIBUTE_IMAGE_WIDTH,
				BORDER_ATTRIBUTE_IMAGE_HEITH, FOOTBOARD_TYPE_NORMAL, 1, 1));
		mCurFood = new Food(FOOD_NONE, 0, 0, 0, FOOD_ATTRIBUTE_IMAGE_SIZE);
	}

	/**
	 * 更新UI模型
	 */
	public void updateUIModel() {
		for (Footboard footboard : mFootboardList) {
			footboard.addY(mFootboartVelocity);
		}
		mRole.addX(mRoleVelocityX);
		mRole.addY(mRoleVelocityY);
		handleBorder();
		handleRoleAction();
		handleFood();
		mFootboardSpaceCounter = mFootboardSpaceCounter - mFootboartVelocity;
		if (mFootboardSpaceCounter >= mFootboardSpaceFactor) {
			mFootboardSpaceCounter -= mFootboardSpaceFactor;
			generateFootboard();
			generateFood();
			mLevelUpCounter += 1;
			if (mLevelUpCounter == GAME_ATTRIBUTE_LEVEL_UP_FACTOR) {
				mLevelUpCounter = 0;
				increaseLevel();
			}
		}
	}

	/**
	 * 随机生成踏板
	 */
	private void generateFootboard() {
		int frameAmount = 1;
		int frameDelay = 1;
		int frameType = FOOTBOARD_TYPE_NORMAL;
		switch (mRan.nextInt(20)) {
		case 0:
		case 1:
		case 2:
			frameType = FOOTBOARD_TYPE_UNSTABLE;
			break;
		case 3:
		case 4:
		case 5:
			frameType = FOOTBOARD_TYPE_SPRING;
			break;
		case 6:
		case 7:
		case 8:
			frameType = FOOTBOARD_TYPE_SPIKED;
			break;
		case 9:
		case 10:
			frameType = FOOTBOARD_TYPE_MOVING_LEFT;
			frameAmount = 2;
			frameDelay = 15;
			break;
		case 11:
		case 12:
			frameType = FOOTBOARD_TYPE_MOVING_RIGHT;
			frameAmount = 2;
			frameDelay = 15;
			break;
		default:
			frameType = FOOTBOARD_TYPE_NORMAL;
		}
		mFootboardList.add(new Footboard(mRan.nextInt(mScreenAttribute.maxX
				- BORDER_ATTRIBUTE_IMAGE_WIDTH), mScreenAttribute.maxY
				+ ROLE_ATTRIBUTE_HEITH, BORDER_ATTRIBUTE_IMAGE_WIDTH,
				BORDER_ATTRIBUTE_IMAGE_HEITH, frameType, frameAmount,
				frameDelay));
	}

	/**
	 * 随机生成加分物品
	 */
	private void generateFood() {
		if (mCurFood.mTimeCounter > 0) {
			return;
		}
		switch (mRan.nextInt(25)) {
		case 0:
			mCurFood.mFoodReward = FOOD_1;
			break;
		case 1:
			mCurFood.mFoodReward = FOOD_2;
			break;
		case 2:
		case 3:
		case 4:
			mCurFood.mFoodReward = FOOD_3;
			break;
		case 5:
		case 6:
		case 7:
			mCurFood.mFoodReward = FOOD_4;
			break;
		case 8:
		case 9:
		case 10:
			mCurFood.mFoodReward = FOOD_5;
			break;
		case 11:
		case 12:
		case 13:
			mCurFood.mFoodReward = FOOD_6;
			break;
		case 14:
		case 15:
		case 16:
		case 17:
			mCurFood.mFoodReward = FOOD_7;
			break;
		case 18:
		case 19:
		case 20:
		case 21:
			mCurFood.mFoodReward = FOOD_8;
			break;
		default:
			mCurFood.mFoodReward = FOOD_NONE;
			return;
		}
		mCurFood.mMinX = mRan
				.nextInt((mScreenAttribute.maxX - FOOD_ATTRIBUTE_IMAGE_SIZE));
		mCurFood.mMinY = mRan
				.nextInt((mScreenAttribute.maxY - FOOD_ATTRIBUTE_IMAGE_SIZE));
		mCurFood.mMaxX = mCurFood.mMinX + FOOD_ATTRIBUTE_IMAGE_SIZE;
		mCurFood.mMaxY = mCurFood.mMinY + FOOD_ATTRIBUTE_IMAGE_SIZE;
		mCurFood.mTimeCounter = FOOD_ATTRIBUTE_DELAY_TIME;
	}

	/**
	 * 处理主角移动
	 * 
	 * @param angleValue
	 */
	public void handleMoving(float angleValue) {
		if (angleValue < -5) {
			mRoleVelocityX = 10 + mAddVelocity;
		} else if (angleValue >= -5 && angleValue < -4) {
			mRoleVelocityX = 8 + mAddVelocity;
		} else if (angleValue >= -4 && angleValue < -3) {
			mRoleVelocityX = 6 + mAddVelocity;
		} else if (angleValue >= -3 && angleValue < -2) {
			mRoleVelocityX = 5 + mAddVelocity;
		} else if (angleValue >= -2 && angleValue < -1.5) {
			mRoleVelocityX = 4 + mAddVelocity;
		} else if (angleValue >= -1.5 && angleValue < 1.5) {
			mRoleVelocityX = 0;
		} else if (angleValue >= 1.5 && angleValue < 2) {
			mRoleVelocityX = -4 - mAddVelocity;
		} else if (angleValue >= 2 && angleValue < 3) {
			mRoleVelocityX = -5 - mAddVelocity;
		} else if (angleValue >= 3 && angleValue < 4) {
			mRoleVelocityX = -6 - mAddVelocity;
		} else if (angleValue >= 4 && angleValue < 5) {
			mRoleVelocityX = -8 - mAddVelocity;
		} else if (angleValue > 5) {
			mRoleVelocityX = -10 - mAddVelocity;
		}
	}

	/**
	 * 难度提升
	 */
	private void increaseLevel() {
		mLevel++;
		if (mLevel < 18 || mLevel % 20 == 0) {
			mFootboartVelocity -= 2;
			int roleStatus = mRole.getRoleStatus();
			if (roleStatus == ROLE_STATUS_ON_FOOTBOARD
					|| roleStatus == ROLE_STATUS_ON_FOOTBOARD_RIGHT
					|| roleStatus == ROLE_STATUS_ON_FOOTBOARD_LEFT) {
				mRoleVelocityY = mFootboartVelocity;
			}
		}
	}

	/**
	 * 处理边界
	 */
	private void handleBorder() {
		if (mFootboardList.size() > 0
				&& mFootboardList.getFirst().getMaxY() <= mScreenAttribute.minY) {
			mFootboardList.remove();
		}
		if (mRole.getMinY() <= mScreenAttribute.minY) {
			mHP -= 3;
			if (mHP <= 0) {
				mGameStatus = GAME_STATUS_GAMEOVER;
			} else if (mRole.getRoleStatus() == ROLE_STATUS_ON_FOOTBOARD
					|| mRole.getRoleStatus() == ROLE_STATUS_ON_FOOTBOARD_LEFT
					|| mRole.getRoleStatus() == ROLE_STATUS_ON_FOOTBOARD_RIGHT) {
				mRole.addY(BORDER_ATTRIBUTE_IMAGE_HEITH
						* GAME_ATTRIBUTE_PIXEL_DENSITY_Y);
			}
			mRoleVelocityY = GAME_ATTRIBUTE_GRAVITY_VELOCITY;
			mEffectFlag = EFFECT_FLAG_SPIKED;
			return;
		}
		if (mRole.getMinY() > mScreenAttribute.maxY) {
			mGameStatus = GAME_STATUS_GAMEOVER;
			return;
		}
		if (mRole.getMinX() < mScreenAttribute.minX) {
			mRoleVelocityX = 0;
			mRole.setX(0);
			return;
		}
		if (mRole.getMaxX() > mScreenAttribute.maxX) {
			mRoleVelocityX = 0;
			mRole.setX(mScreenAttribute.maxX - ROLE_ATTRIBUTE_WIDTH);
			return;
		}
	}

	/**
	 * 处理主角在踏板上的活动
	 */
	private void handleRoleAction() {
		Role role = mRole;
		for (Footboard footboard : mFootboardList) {
			if ((role.getMaxY() >= footboard.getMinY() && role.getMaxY() < footboard
					.getMaxY())
					&& (role.getMaxX() > footboard.getMinX() && role.getMinX() < footboard
							.getMaxX())) {
				if (role.getRoleStatus() == ROLE_STATUS_ON_FOOTBOARD
						|| role.getRoleStatus() == ROLE_STATUS_ON_FOOTBOARD_RIGHT
						|| role.getRoleStatus() == ROLE_STATUS_ON_FOOTBOARD_LEFT) {
					if (footboard.getType() == FOOTBOARD_TYPE_SPRING) {
						mRoleVelocityY = mFootboartVelocity
								- GAME_ATTRIBUTE_GRAVITY_VELOCITY;
						role.addY(-1 * GAME_ATTRIBUTE_PIXEL_DENSITY_Y);
						updateRoleStatus(ROLE_STATUS_FREEFALL);
						return;
					}
					if (footboard.getType() == FOOTBOARD_TYPE_MOVING_LEFT) {
						role.addX(BOARD_ATTRIBUTE_LEFT_VELOCITY);
					} else if (footboard.getType() == FOOTBOARD_TYPE_MOVING_RIGHT) {
						role.addX(BOARD_ATTRIBUTE_RIGHT_VELOCITY);
					} else if (footboard.getType() == FOOTBOARD_TYPE_UNSTABLE
							&& footboard.isBoardBreak()) {
						mFootboardList.remove(footboard);
					}
					updateRoleStatus(ROLE_STATUS_ON_FOOTBOARD);
				} else {
					// 主角第一次触板
					mScore += mLevel;
					mRoleVelocityY = mFootboartVelocity;
					role.setVirtualY(footboard.getVirtualY()
							- ROLE_ATTRIBUTE_HEITH
							* GAME_ATTRIBUTE_PIXEL_DENSITY_Y);
					if (footboard.getType() == FOOTBOARD_TYPE_SPIKED) {
						mHP -= 3;
					} else if (mHP < ROLE_ATTRIBUTE_HP_MAX) {
						mHP += 1;
					}
					if (mHP <= 0) {
						mGameStatus = GAME_STATUS_GAMEOVER;
					}
					updateRoleStatus(ROLE_STATUS_ON_FOOTBOARD);
					switch (footboard.getType()) {
					case FOOTBOARD_TYPE_UNSTABLE:
						mEffectFlag = EFFECT_FLAG_UNSTABLE;
						break;
					case FOOTBOARD_TYPE_SPRING:
						mEffectFlag = EFFECT_FLAG_SPRING;
						break;
					case FOOTBOARD_TYPE_SPIKED:
						mEffectFlag = EFFECT_FLAG_SPIKED;
						break;
					case FOOTBOARD_TYPE_MOVING_LEFT:
					case FOOTBOARD_TYPE_MOVING_RIGHT:
						mEffectFlag = EFFECT_FLAG_MOVING;
						break;
					default:
						mEffectFlag = EFFECT_FLAG_NORMAL;
					}
				}
				return;
			}
		}
		if (mRoleVelocityY < mFootboartVelocity) {
			mRoleVelocityY += 3;
		} else {
			mRoleVelocityY = GAME_ATTRIBUTE_GRAVITY_VELOCITY;
		}
		updateRoleStatus(ROLE_STATUS_FREEFALL);
	}

	private void handleFood() {
		Food food = mCurFood;
		food.mTimeCounter--;
		if (food.mFoodReward != FOOD_NONE && food.mTimeCounter > 0) {
			if ((mRole.getMaxX() > food.mMinX && mRole.getMinX() < food.mMaxX)
					&& ((mRole.getMaxY() >= food.mMinY && mRole.getMaxY() < food.mMaxY) || (mRole
							.getMinY() > food.mMinY && mRole.getMinY() <= food.mMaxY))) {
				mEffectFlag = EFFECT_FLAG_TOOLS;
				mScore += food.mFoodReward;
				food.mFoodReward = FOOD_NONE;
			}
		}
	}

	private void updateRoleStatus(int status) {
		if (status == ROLE_STATUS_FREEFALL) {
			if (mRoleVelocityX > 0) {
				mRole.setRoleStatus(ROLE_STATUS_FREEFALL_RIGHT);
			} else if (mRoleVelocityX < 0) {
				mRole.setRoleStatus(ROLE_STATUS_FREEFALL_LEFT);
			} else {
				mRole.setRoleStatus(ROLE_STATUS_FREEFALL);
			}
		} else {
			if (mRoleVelocityX > 0) {
				mRole.setRoleStatus(ROLE_STATUS_ON_FOOTBOARD_RIGHT);
			} else if (mRoleVelocityX < 0) {
				mRole.setRoleStatus(ROLE_STATUS_ON_FOOTBOARD_LEFT);
			} else {
				mRole.setRoleStatus(ROLE_STATUS_ON_FOOTBOARD);
			}
		}
	}

	/**
	 * 清除操作
	 */
	public void destroy() {
		mScreenAttribute = null;
		mRole = null;
		mRan = null;
		mFootboardList.clear();
		mFootboardList = null;
	}

	public Role getRoleUIObject() {
		return mRole;
	}

	public List<Footboard> getFootboardUIObjects() {
		return mFootboardList;
	}

	public Food getFood() {
		return mCurFood;
	}

	public int getEffectFlag() {
		try {
			return mEffectFlag;
		} finally {
			mEffectFlag = EFFECT_FLAG_NO_EFFECT;
		}
	}

	public String getLevel() {
		return "LV: " + mLevel;
	}

	public String getScoreStr() {
		return "SC: " + mScore;
	}

	public int getScore() {
		return mScore;
	}

	public float getHp() {
		return (float) mHP / ROLE_ATTRIBUTE_HP_MAX;
	}
}
