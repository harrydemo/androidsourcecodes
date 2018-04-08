package org.void1898.www.agilebuddy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.void1898.www.agilebuddy.data.Food;
import org.void1898.www.agilebuddy.data.Footboard;
import org.void1898.www.agilebuddy.data.Role;
import org.void1898.www.agilebuddy.data.ScreenAttribute;
import org.void1898.www.agilebuddy.material.UIModel;
import org.void1898.www.agilebuddy.util.ConstantInfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * view of AgileBuddyActivity
 * 
 * @author void1898@gamil.com
 * 
 */
public class AgileBuddyView extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String HANDLE_MESSAGE_GAME_SCORE = "1";

	private AgileThread mUIThread;

	private Context mContext;

	private Handler mHandler;

	private ScreenAttribute mScreenAttribute;

	private int mActionPower;

	private boolean mVibratorFlag;
	private Vibrator mVibrator;

	private boolean mSoundsFlag;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;

	private Bitmap mBackgroundImage;

	private Bitmap mRoleStandImage;
	private Bitmap mRoleDeadmanImage;
	private Bitmap mRoleFreefallImage1;
	private Bitmap mRoleFreefallImage2;
	private Bitmap mRoleFreefallImage3;
	private Bitmap mRoleFreefallImage4;
	private Bitmap mRoleMovingLeftImage1;
	private Bitmap mRoleMovingLeftImage2;
	private Bitmap mRoleMovingLeftImage3;
	private Bitmap mRoleMovingLeftImage4;
	private Bitmap mRoleMovingRightImage1;
	private Bitmap mRoleMovingRightImage2;
	private Bitmap mRoleMovingRightImage3;
	private Bitmap mRoleMovingRightImage4;

	private Bitmap mFootboardNormalImage;
	private Bitmap mFootboardUnstableImage1;
	private Bitmap mFootboardUnstableImage2;
	private Bitmap mFootboardSpringImage;
	private Bitmap mFootboardSpikedImage;
	private Bitmap mFootboardMovingLeftImage1;
	private Bitmap mFootboardMovingLeftImage2;
	private Bitmap mFootboardMovingRightImage1;
	private Bitmap mFootboardMovingRightImage2;

	private Bitmap mFoodImage1;
	private Bitmap mFoodImage2;
	private Bitmap mFoodImage3;
	private Bitmap mFoodImage4;
	private Bitmap mFoodImage5;
	private Bitmap mFoodImage6;
	private Bitmap mFoodImage7;
	private Bitmap mFoodImage8;

	private Drawable mTopBarImage;
	private Drawable mHpBarTotalImage;
	private Drawable mHpBarRemainImage;

	private Paint mGameMsgRightPaint;
	private Paint mGameMsgLeftPaint;

	public AgileBuddyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message m) {
				// 更新本地记录文件
				int curScore = m.getData().getInt(HANDLE_MESSAGE_GAME_SCORE);
				boolean recordRefreshed = updateLocalRecord(curScore);

				LayoutInflater factory = LayoutInflater.from(mContext);
				View dialogView = factory.inflate(R.layout.score_post_panel,
						null);
				dialogView.setFocusableInTouchMode(true);
				dialogView.requestFocus();

				final EditText usernameEditText = (EditText) dialogView
						.findViewById(R.id.namefield);
				usernameEditText.setText(mContext.getSharedPreferences(
						ConstantInfo.PREFERENCE_RANKING_INFO, 0).getString(
						ConstantInfo.PREFERENCE_KEY_RANKING_NAME, ""));
				final AlertDialog dialog = new AlertDialog.Builder(mContext)
						.setView(dialogView).create();
				if (recordRefreshed) {
					dialog.setIcon(R.drawable.tip_new_record);
					dialog.setTitle(R.string.gameover_dialog_text_newrecord);
				} else {
					if (curScore < 100) {
						dialog.setIcon(R.drawable.tip_pool_guy);
						dialog.setTitle(R.string.gameover_dialog_text_poolguy);
					} else if (curScore < 500) {
						dialog.setIcon(R.drawable.tip_not_bad);
						dialog.setTitle(R.string.gameover_dialog_text_notbad);
					} else {
						dialog.setIcon(R.drawable.tip_awesome);
						dialog.setTitle(R.string.gameover_dialog_text_awesome);
					}
				}
				dialog.show();
				dialogView.findViewById(R.id.retry).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								restartGame();
							}
						});

				dialogView.findViewById(R.id.post_scores).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								String userName = null;
								if (usernameEditText.getText() != null) {
									userName = usernameEditText.getText()
											.toString().replace("\n", " ")
											.trim();
								}
								if (userName.length() > 0
										&& userName.length() < 20) {
									SharedPreferences settings = mContext
											.getSharedPreferences(
													ConstantInfo.PREFERENCE_RANKING_INFO,
													0);
									settings
											.edit()
											.putString(
													ConstantInfo.PREFERENCE_KEY_RANKING_NAME,
													userName)
											.putBoolean(
													ConstantInfo.PREFERENCE_KEY_RANKING_FLAG,
													!settings
															.getBoolean(
																	ConstantInfo.PREFERENCE_KEY_RANKING_FLAG,
																	false))
											.commit();
								} else {
									showToast(R.string.options_toast_username_too_long);
								}
							}
						});
				dialogView.findViewById(R.id.goback).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								((AgileBuddyActivity) mContext).finish();
							}
						});
			}
		};
		// 初始化资源
		initRes();
		mUIThread = new AgileThread(holder, context, mHandler);
		setFocusable(true);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mScreenAttribute = new ScreenAttribute(0, 20, width, height);
		mUIThread.initUIModel(mScreenAttribute);
		mUIThread.setRunning(true);
		mUIThread.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		mUIThread.setRunning(false);
		while (retry) {
			try {
				mUIThread.join();
				retry = false;
			} catch (InterruptedException e) {
				Log.d("", "Surface destroy failure:", e);
			}
		}
	}

	public void handleMoving(float angleValue) {
		if (mUIThread != null) {
			mUIThread.handleMoving(angleValue);
		}
	}

	public void restartGame() {
		mUIThread = new AgileThread(this.getHolder(), this.getContext(),
				mHandler);
		mUIThread.initUIModel(mScreenAttribute);
		mUIThread.setRunning(true);
		mUIThread.start();
	}

	public boolean updateLocalRecord(int score) {
		SharedPreferences rankingSettings = mContext.getSharedPreferences(
				ConstantInfo.PREFERENCE_RANKING_INFO, 0);
		if (rankingSettings
				.getInt(ConstantInfo.PREFERENCE_KEY_RANKING_SCORE, 0) < score) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			rankingSettings.edit().putInt(
					ConstantInfo.PREFERENCE_KEY_RANKING_SCORE, score)
					.putString(ConstantInfo.PREFERENCE_KEY_RANKING_DATE,
							formatter.format(new Date())).commit();
			return true;
		}
		return false;
	}

	private void showToast(int strId) {
		Toast toast = Toast.makeText(mContext, strId, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 220);
		toast.show();
	}

	/**
	 * 初始化资源
	 */
	private void initRes() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		mSoundsFlag = preferences.getBoolean(
				ConstantInfo.PREFERENCE_KEY_SOUNDS, true);
		mVibratorFlag = preferences.getBoolean(
				ConstantInfo.PREFERENCE_KEY_VIBRATE, true);
		mActionPower = preferences
				.getInt(ConstantInfo.PREFERENCE_KEY_POWER, 40);

		// 初始化活动音效
		soundPool = new SoundPool(10, AudioManager.STREAM_RING, 5);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(UIModel.EFFECT_FLAG_NORMAL, soundPool.load(
				getContext(), R.raw.normal, 1));
		soundPoolMap.put(UIModel.EFFECT_FLAG_UNSTABLE, soundPool.load(
				getContext(), R.raw.unstable, 1));
		soundPoolMap.put(UIModel.EFFECT_FLAG_SPRING, soundPool.load(
				getContext(), R.raw.spring, 1));
		soundPoolMap.put(UIModel.EFFECT_FLAG_SPIKED, soundPool.load(
				getContext(), R.raw.spiked, 1));
		soundPoolMap.put(UIModel.EFFECT_FLAG_MOVING, soundPool.load(
				getContext(), R.raw.moving, 1));
		soundPoolMap.put(UIModel.EFFECT_FLAG_TOOLS, soundPool.load(
				getContext(), R.raw.tools, 1));

		mGameMsgLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mGameMsgLeftPaint.setColor(Color.YELLOW);
		mGameMsgLeftPaint.setStyle(Style.FILL);
		mGameMsgLeftPaint.setTextSize(15f);
		mGameMsgLeftPaint.setTextAlign(Paint.Align.LEFT);
		mGameMsgLeftPaint.setTypeface(Typeface.DEFAULT_BOLD);

		mGameMsgRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mGameMsgRightPaint.setColor(Color.YELLOW);
		mGameMsgRightPaint.setStyle(Style.FILL);
		mGameMsgRightPaint.setTextSize(15f);
		mGameMsgRightPaint.setTextAlign(Paint.Align.RIGHT);
		mGameMsgRightPaint.setTypeface(Typeface.DEFAULT_BOLD);

		Resources res = mContext.getResources();

		mTopBarImage = res.getDrawable(R.drawable.top_bar);
		mHpBarTotalImage = res.getDrawable(R.drawable.hp_bar_total);
		mHpBarRemainImage = res.getDrawable(R.drawable.hp_bar_remain);

		mRoleDeadmanImage = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_deadman),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleStandImage = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_standing),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleFreefallImage1 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_freefall1),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleFreefallImage2 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_freefall2),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleFreefallImage3 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_freefall3),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleFreefallImage4 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_freefall4),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleMovingLeftImage1 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_moving_left1),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleMovingLeftImage2 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_moving_left2),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleMovingLeftImage3 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_moving_left3),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleMovingLeftImage4 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_moving_left4),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleMovingRightImage1 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_moving_right1),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleMovingRightImage2 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_moving_right2),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleMovingRightImage3 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_moving_right3),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);
		mRoleMovingRightImage4 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.role_moving_right4),
				UIModel.ROLE_ATTRIBUTE_WIDTH, UIModel.ROLE_ATTRIBUTE_HEITH,
				true);

		mFootboardNormalImage = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.footboard_normal),
				UIModel.BORDER_ATTRIBUTE_IMAGE_WIDTH,
				UIModel.BORDER_ATTRIBUTE_IMAGE_HEITH, true);
		mFootboardUnstableImage1 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.footboard_unstable1),
				UIModel.BORDER_ATTRIBUTE_IMAGE_WIDTH,
				UIModel.BORDER_ATTRIBUTE_IMAGE_HEITH, true);
		mFootboardUnstableImage2 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.footboard_unstable2),
				UIModel.BORDER_ATTRIBUTE_IMAGE_WIDTH,
				UIModel.BORDER_ATTRIBUTE_IMAGE_HEITH, true);
		mFootboardSpringImage = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.footboard_spring),
				UIModel.BORDER_ATTRIBUTE_IMAGE_WIDTH,
				UIModel.BORDER_ATTRIBUTE_IMAGE_HEITH, true);
		mFootboardSpikedImage = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.footboard_spiked),
				UIModel.BORDER_ATTRIBUTE_IMAGE_WIDTH,
				UIModel.BORDER_ATTRIBUTE_IMAGE_HEITH, true);
		mFootboardMovingLeftImage1 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.footboard_moving_left1),
				UIModel.BORDER_ATTRIBUTE_IMAGE_WIDTH,
				UIModel.BORDER_ATTRIBUTE_IMAGE_HEITH, true);
		mFootboardMovingLeftImage2 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.footboard_moving_left2),
				UIModel.BORDER_ATTRIBUTE_IMAGE_WIDTH,
				UIModel.BORDER_ATTRIBUTE_IMAGE_HEITH, true);
		mFootboardMovingRightImage1 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.footboard_moving_right1),
				UIModel.BORDER_ATTRIBUTE_IMAGE_WIDTH,
				UIModel.BORDER_ATTRIBUTE_IMAGE_HEITH, true);
		mFootboardMovingRightImage2 = Bitmap.createScaledBitmap(BitmapFactory
				.decodeResource(res, R.drawable.footboard_moving_right2),
				UIModel.BORDER_ATTRIBUTE_IMAGE_WIDTH,
				UIModel.BORDER_ATTRIBUTE_IMAGE_HEITH, true);

		mFoodImage1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				res, R.drawable.food_1), UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE,
				UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE, true);
		mFoodImage2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				res, R.drawable.food_2), UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE,
				UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE, true);
		mFoodImage3 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				res, R.drawable.food_3), UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE,
				UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE, true);
		mFoodImage4 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				res, R.drawable.food_4), UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE,
				UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE, true);
		mFoodImage5 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				res, R.drawable.food_5), UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE,
				UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE, true);
		mFoodImage6 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				res, R.drawable.food_6), UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE,
				UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE, true);
		mFoodImage7 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				res, R.drawable.food_7), UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE,
				UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE, true);
		mFoodImage8 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				res, R.drawable.food_8), UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE,
				UIModel.FOOD_ATTRIBUTE_IMAGE_SIZE, true);
		mBackgroundImage = BitmapFactory
				.decodeResource(res, R.drawable.bg_game);
	}

	// thread for updating UI
	class AgileThread extends Thread {

		private SurfaceHolder mSurfaceHolder;
		private Context mContext;
		private Handler mHandler;

		// 运行标志
		private boolean mRun = true;
		// UI模型
		private UIModel mUIModel;
		// 时间记录器
		private long mTimeLogger;

		public AgileThread(SurfaceHolder surfaceHolder, Context context,
				Handler handler) {
			this.mSurfaceHolder = surfaceHolder;
			this.mContext = context;
			this.mHandler = handler;
		}

		@Override
		public void run() {
			while (mRun) {
				Canvas c = null;
				try {
					mTimeLogger = System.currentTimeMillis();
					try {
						mUIModel.updateUIModel();
						c = mSurfaceHolder.lockCanvas(null);
						synchronized (mSurfaceHolder) {
							doDraw(c);
						}
						handleEffect(mUIModel.getEffectFlag());
					} catch (Exception e) {
						Log.d("", "Error at 'run' method", e);
					} finally {
						if (c != null) {
							mSurfaceHolder.unlockCanvasAndPost(c);
						}
					}
					mTimeLogger = System.currentTimeMillis() - mTimeLogger;
					if (mTimeLogger < UIModel.GAME_ATTRIBUTE_FRAME_DELAY) {
						Thread.sleep(UIModel.GAME_ATTRIBUTE_FRAME_DELAY
								- mTimeLogger);
					}
					if (mUIModel.mGameStatus == UIModel.GAME_STATUS_GAMEOVER) {
						Message message = new Message();
						Bundle bundle = new Bundle();
						bundle.putInt(AgileBuddyView.HANDLE_MESSAGE_GAME_SCORE,
								mUIModel.getScore());
						message.setData(bundle);
						mHandler.sendMessage(message);
						mRun = false;
					}
				} catch (Exception ex) {
					Log.d("", "Error at 'run' method", ex);
				}
			}
		}

		private void doDraw(Canvas canvas) {
			Bitmap tempBitmap = null;
			canvas.drawBitmap(mBackgroundImage, 0, 0, null);
			mTopBarImage.setBounds(0, 0,
					AgileBuddyView.this.mScreenAttribute.maxX,
					AgileBuddyView.this.mScreenAttribute.minY);
			mTopBarImage.draw(canvas);

			List<Footboard> footboards = mUIModel.getFootboardUIObjects();
			for (Footboard footboard : footboards) {
				switch (footboard.getType()) {
				case UIModel.FOOTBOARD_TYPE_UNSTABLE:
					if (!footboard.isMarked()) {
						tempBitmap = mFootboardUnstableImage1;
					} else {
						tempBitmap = mFootboardUnstableImage2;
					}
					break;
				case UIModel.FOOTBOARD_TYPE_SPRING:
					tempBitmap = mFootboardSpringImage;
					break;
				case UIModel.FOOTBOARD_TYPE_SPIKED:
					tempBitmap = mFootboardSpikedImage;
					break;
				case UIModel.FOOTBOARD_TYPE_MOVING_LEFT:
					if (footboard.nextFrame() == 0) {
						tempBitmap = mFootboardMovingLeftImage1;
					} else {
						tempBitmap = mFootboardMovingLeftImage2;
					}
					break;
				case UIModel.FOOTBOARD_TYPE_MOVING_RIGHT:
					if (footboard.nextFrame() == 0) {
						tempBitmap = mFootboardMovingRightImage1;
					} else {
						tempBitmap = mFootboardMovingRightImage2;
					}
					break;
				default:
					tempBitmap = mFootboardNormalImage;
				}
				canvas.drawBitmap(tempBitmap, footboard.getMinX(), footboard
						.getMinY(), null);
			}

			Role role = mUIModel.getRoleUIObject();
			if (mUIModel.mGameStatus == UIModel.GAME_STATUS_GAMEOVER) {
				canvas.drawBitmap(mRoleDeadmanImage, role.getMinX(), role
						.getMinY(), null);
			} else {
				switch (role.getRoleSharp()) {
				case UIModel.ROLE_SHARP_FREEFALL_NO1:
					tempBitmap = mRoleFreefallImage1;
					break;
				case UIModel.ROLE_SHARP_FREEFALL_NO2:
					tempBitmap = mRoleFreefallImage2;
					break;
				case UIModel.ROLE_SHARP_FREEFALL_NO3:
					tempBitmap = mRoleFreefallImage3;
					break;
				case UIModel.ROLE_SHARP_FREEFALL_NO4:
					tempBitmap = mRoleFreefallImage4;
					break;
				case UIModel.ROLE_SHARP_MOVE_LEFT_NO1:
					tempBitmap = mRoleMovingLeftImage1;
					break;
				case UIModel.ROLE_SHARP_MOVE_LEFT_NO2:
					tempBitmap = mRoleMovingLeftImage2;
					break;
				case UIModel.ROLE_SHARP_MOVE_LEFT_NO3:
					tempBitmap = mRoleMovingLeftImage3;
					break;
				case UIModel.ROLE_SHARP_MOVE_LEFT_NO4:
					tempBitmap = mRoleMovingLeftImage4;
					break;
				case UIModel.ROLE_SHARP_MOVE_RIGHT_NO1:
					tempBitmap = mRoleMovingRightImage1;
					break;
				case UIModel.ROLE_SHARP_MOVE_RIGHT_NO2:
					tempBitmap = mRoleMovingRightImage2;
					break;
				case UIModel.ROLE_SHARP_MOVE_RIGHT_NO3:
					tempBitmap = mRoleMovingRightImage3;
					break;
				case UIModel.ROLE_SHARP_MOVE_RIGHT_NO4:
					tempBitmap = mRoleMovingRightImage4;
					break;
				default:
					tempBitmap = mRoleStandImage;
				}
				canvas.drawBitmap(tempBitmap, role.getMinX(), role.getMinY(),
						null);
			}

			Food food = mUIModel.getFood();
			if (food.mFoodReward != UIModel.FOOD_NONE && food.mTimeCounter > 0) {
				switch (food.mFoodReward) {
				case UIModel.FOOD_1:
					tempBitmap = mFoodImage1;
					break;
				case UIModel.FOOD_2:
					tempBitmap = mFoodImage2;
					break;
				case UIModel.FOOD_3:
					tempBitmap = mFoodImage3;
					break;
				case UIModel.FOOD_4:
					tempBitmap = mFoodImage4;
					break;
				case UIModel.FOOD_5:
					tempBitmap = mFoodImage5;
					break;
				case UIModel.FOOD_6:
					tempBitmap = mFoodImage6;
					break;
				case UIModel.FOOD_7:
					tempBitmap = mFoodImage7;
					break;
				case UIModel.FOOD_8:
					tempBitmap = mFoodImage8;
					break;
				}
				canvas.drawBitmap(tempBitmap, food.mMinX, food.mMinY, null);
			}

			FontMetrics fmsr = mGameMsgLeftPaint.getFontMetrics();
			canvas.drawText(mUIModel.getScoreStr(), (float) 5,
					(float) AgileBuddyView.this.mScreenAttribute.maxY - 20
							- (fmsr.ascent + fmsr.descent), mGameMsgLeftPaint);

			mHpBarTotalImage.setBounds(
					(AgileBuddyView.this.mScreenAttribute.maxX / 3),
					AgileBuddyView.this.mScreenAttribute.maxY - 20,
					(AgileBuddyView.this.mScreenAttribute.maxX * 2 / 3),
					AgileBuddyView.this.mScreenAttribute.maxY - 15);
			mHpBarTotalImage.draw(canvas);

			mHpBarRemainImage
					.setBounds(
							(AgileBuddyView.this.mScreenAttribute.maxX / 3),
							AgileBuddyView.this.mScreenAttribute.maxY - 20,
							(int) (AgileBuddyView.this.mScreenAttribute.maxX / 3 + AgileBuddyView.this.mScreenAttribute.maxX
									* mUIModel.getHp() / 3),
							AgileBuddyView.this.mScreenAttribute.maxY - 15);
			mHpBarRemainImage.draw(canvas);

			fmsr = mGameMsgRightPaint.getFontMetrics();
			canvas.drawText(mUIModel.getLevel(),
					(float) (AgileBuddyView.this.mScreenAttribute.maxX - 5),
					(float) AgileBuddyView.this.mScreenAttribute.maxY - 20
							- (fmsr.ascent + fmsr.descent), mGameMsgRightPaint);
		}

		public void initUIModel(ScreenAttribute screenAttribut) {
			mBackgroundImage = Bitmap.createScaledBitmap(mBackgroundImage,
					screenAttribut.maxX, screenAttribut.maxY, true);
			if (mUIModel != null) {
				mRun = false;
				mUIModel.destroy();
			}
			int addVelocity = 0;
			if (mActionPower < 10) {
				addVelocity = -2;
			} else if (mActionPower < 25) {
				addVelocity = -1;
			} else if (mActionPower < 50) {
				addVelocity = 0;
			} else if (mActionPower < 60) {
				addVelocity = 1;
			} else if (mActionPower < 70) {
				addVelocity = 2;
			} else if (mActionPower < 80) {
				addVelocity = 3;
			} else if (mActionPower < 90) {
				addVelocity = 4;
			} else {
				addVelocity = 5;
			}
			mUIModel = new UIModel(screenAttribut, addVelocity);
		}

		public void handleMoving(float angleValue) {
			if (mUIModel != null) {
				mUIModel.handleMoving(angleValue);
			}
		}

		private void handleEffect(int effectFlag) {
			if (effectFlag == UIModel.EFFECT_FLAG_NO_EFFECT)
				return;
			// 处理音效
			if (mSoundsFlag) {
				playSoundEffect(effectFlag);
			}
			// 处理震动
			if (mVibratorFlag) {
				if (mVibrator == null) {
					mVibrator = (Vibrator) mContext
							.getSystemService(Context.VIBRATOR_SERVICE);
				}
				mVibrator.vibrate(25);
			}
		}

		/**
		 * 播放音效
		 * 
		 * @param soundId
		 */
		private void playSoundEffect(int soundId) {
			try {
				AudioManager mgr = (AudioManager) getContext()
						.getSystemService(Context.AUDIO_SERVICE);
				float streamVolumeCurrent = mgr
						.getStreamVolume(AudioManager.STREAM_RING);
				float streamVolumeMax = mgr
						.getStreamMaxVolume(AudioManager.STREAM_RING);
				float volume = streamVolumeCurrent / streamVolumeMax;
				soundPool.play(soundPoolMap.get(soundId), volume, volume, 1, 0,
						1f);
			} catch (Exception e) {
				Log.d("PlaySounds", e.toString());
			}
		}

		public void setRunning(boolean run) {
			mRun = run;
		}
	}// Thread

}
