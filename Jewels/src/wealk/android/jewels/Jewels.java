package wealk.android.jewels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceShapeModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.HorizontalAlign;
import org.anddev.andengine.util.MathUtils;

import wealk.android.jewels.constants.IConstants;
import wealk.android.jewels.entity.BackgroundCell;
import wealk.android.jewels.entity.BorderSprite;
import wealk.android.jewels.entity.JewelSprite;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Qingfeng
 * @time 2010-11-03 ~ 2010
 */
public class Jewels extends BaseGameActivity implements IOnSceneTouchListener, IConstants {

	// ===========================================================
	// Constants
	// ===========================================================
	
	/**屏幕尺寸**/
	private static final int CAMERA_WIDTH = 320; 
	private static final int CAMERA_HEIGHT = 480; 
	
	/**场景分层**/
	private static final int LAYER_BACKGROUND = 0;
	private static final int LAYER_BG_CELL = LAYER_BACKGROUND + 1;
	private static final int LAYER_JEWELS = LAYER_BG_CELL + 1;
	private static final int LAYER_SCORE = LAYER_JEWELS + 1;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private Camera mCamera;//镜头	
	protected  Scene mMainScene;//主场景	
	
	/**游戏模式**/
	private String mGameModel;
	
	/**游戏状态**/
	private boolean mGameRunning;//游戏的总开关(可处理来电、任务切换等)
	private boolean mIsSwaping;//交换状态
	private final int MOVE_UP = 1;//上移
	private final int MOVE_DOWN = 2;//下移
	private final int MOVE_LEFT = 3;//左移
	private final int MOVE_RIGHT = 4;//右移	
	private final int FALL = 5;//下落
	private final int DEAD = 6;//死局
	private final int CHECK = 0;//执行检测
	private int STATE = CHECK;//一开始就检测，没有移动命令的时候也一直检测
	
	/**游戏音效**/
	private Sound mSwapErrorSound;//交换后不消去
	private Sound mFallSound;//下落
	private Sound mRemoveSound;//消去
	private Sound mStartingSound;//开场音乐
	
	private final int SPEED = 4;//移动速度
	private int moveValue = 0;//交换移动的临时距离
		
	/**背景**/
	private int mCurBGNum;//当前使用的背景的编号
	private Texture mBackgroundTexture,mBackground2Texture;		
	protected TextureRegion mBackgroundTextureRegion;
	protected TextureRegion mBackground2TextureRegion;//游戏开始时的一个背景动画
	/**钻石**/
	private HashMap<String, JewelSprite> mHashMap;
	private Texture[] mJewelTexture;
	protected TextureRegion[] mJewelTextureRegion;
	/**钻石边框**/
	private BorderSprite mBorder;
	private Texture mBorderTexture;
	private TextureRegion mBorderTextureRegion;
	/**单元格board**/
	private Texture mBoardTexture;
	private TextureRegion mBoardTextureRegion;
	/**单元格背景**/
	private Texture mBGCellTexture;
	private TextureRegion mBGCellTextureRegion;
	/**bonus**/
	private Sprite mBonus;
	private Texture mBonusStaticBGTexture,mBonusBGTexture,mBonusTexture;
	private TextureRegion mBonusStaticBGTextureRegion,mBonusBGTextureRegion,mBonusTextureRegion;
	/**LongestChain字体**/
	private Texture mLongestChainFontTexture;
	private Font mLongestChainFont;
	/**关卡**/
	private int mChapter = 1;
	private float mChapterStep = 12;//关卡进度条每次增加到长度(每过一关递减)
	private Texture mChapterTexture;
	private Font mChapterFont;
	private ChangeableText mChapterText,mXText;
	/**最长连接**/
	private int mLongestChain = 0;
	private int mLongestChainTemp = 0;
	private ChangeableText mLongestChainText;
	/**分数**/
	private int mScore = 0;
	private Texture mScoreFontTexture;
	private Font mScoreFont;
	private ChangeableText mScoreBGText,mScoreText;
	/**发光精灵**/
	private Sprite mSpark,mSpark2;
	private Texture mSparkTexture,mSpark2Texture;
	private TextureRegion mSparkTextureRegion,mSpark2TextureRegion;
	
	private int mCurRow,mCurCol;//当前选中的行、列
	private int mLastRow,mLastCol;//上一个选中的行、列
	private ArrayList<String> mDeadArrList;//可消去的钻石地址队列
	private int mTime = 0;//每一次10秒后还不做任何操作就自动提示
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================	
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera).setNeedsSound(true));//需要播放音效
	}

	@Override
	public void onLoadResources() {
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		/*背景*/
		this.mCurBGNum = MathUtils.random(1, 4);
		String bgPath = "bground" + String.valueOf(this.mCurBGNum) + ".png";
		this.mBackgroundTexture = new Texture(512, 1024, TextureOptions.DEFAULT);	
		this.mBackground2Texture = new Texture(512, 1024, TextureOptions.DEFAULT);	
		this.mBackgroundTextureRegion = TextureRegionFactory.createFromAsset
				(this.mBackgroundTexture, this, bgPath, 0, 0);
		this.mBackground2TextureRegion = TextureRegionFactory.createFromAsset
				(this.mBackground2Texture, this, "title_bg"+String.valueOf(this.mCurBGNum)+".png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBackgroundTexture);
		this.mEngine.getTextureManager().loadTexture(this.mBackground2Texture);
		/*钻石*/
		this.mJewelTexture = new Texture[7];
		this.mJewelTextureRegion = new TextureRegion[7];
		for(int i=0; i<this.mJewelTexture.length; i++){
			this.mJewelTexture[i] = new Texture(64, 64, TextureOptions.DEFAULT);
		}
		this.mJewelTextureRegion[0] = TextureRegionFactory.createFromAsset
				(this.mJewelTexture[0], this, "jewel1.png", 0, 0);
		this.mJewelTextureRegion[1] = TextureRegionFactory.createFromAsset
				(this.mJewelTexture[1], this, "jewel2.png", 0, 0);
		this.mJewelTextureRegion[2] = TextureRegionFactory.createFromAsset
				(this.mJewelTexture[2], this, "jewel3.png", 0, 0);
		this.mJewelTextureRegion[3] = TextureRegionFactory.createFromAsset
				(this.mJewelTexture[3], this, "jewel4.png", 0, 0);
		this.mJewelTextureRegion[4] = TextureRegionFactory.createFromAsset
				(this.mJewelTexture[4], this, "jewel5.png", 0, 0);
		this.mJewelTextureRegion[5] = TextureRegionFactory.createFromAsset
				(this.mJewelTexture[5], this, "jewel6.png", 0, 0);
		this.mJewelTextureRegion[6] = TextureRegionFactory.createFromAsset
				(this.mJewelTexture[6], this, "jewel7.png", 0, 0);
		for(int i=0; i<this.mJewelTexture.length; i++){
			this.mEngine.getTextureManager().loadTexture(this.mJewelTexture[i]);
		}	
		/*钻石边框*/
		this.mBorderTexture = new Texture(64, 64, TextureOptions.DEFAULT);	
		this.mBorderTextureRegion = TextureRegionFactory.createFromAsset
				(this.mBorderTexture, this, "selection.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBorderTexture);
		/*Board*/
		this.mBoardTexture = new Texture(512, 512, TextureOptions.DEFAULT);	
		this.mBoardTextureRegion = TextureRegionFactory.createFromAsset
				(this.mBoardTexture, this, "board.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBoardTexture);
		/*单元格背景*/
		this.mBGCellTexture = new Texture(128, 128, TextureOptions.DEFAULT);	
		this.mBGCellTextureRegion = TextureRegionFactory.createFromAsset
				(this.mBGCellTexture, this, "bg_cell.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBGCellTexture);
		/*bonus*/
		this.mBonusStaticBGTexture = new Texture(64, 64, TextureOptions.DEFAULT);	
		this.mBonusStaticBGTextureRegion = TextureRegionFactory.createFromAsset
				(this.mBonusStaticBGTexture, this, "bonus.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBonusStaticBGTexture);
		this.mBonusBGTexture = new Texture(512, 32, TextureOptions.DEFAULT);	
		this.mBonusBGTextureRegion = TextureRegionFactory.createFromAsset
				(this.mBonusBGTexture, this, "bonusbar.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBonusBGTexture);
		this.mBonusTexture = new Texture(512, 32, TextureOptions.DEFAULT);	
		this.mBonusTextureRegion = TextureRegionFactory.createFromAsset
				(this.mBonusTexture, this, "bonusbar_fill.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBonusTexture);
		/*字体--longest chain*/
		this.mLongestChainFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mLongestChainFont = new Font(this.mLongestChainFontTexture, Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD), 26, true, Color.WHITE);
		this.mEngine.getTextureManager().loadTexture(this.mLongestChainFontTexture);
		this.mEngine.getFontManager().loadFont(this.mLongestChainFont);	
		/*分数板*/
		this.mScoreFontTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mScoreFont = FontFactory.createFromAsset(this.mScoreFontTexture, this, "fonts/bluehigh.ttf", 44, true, Color.WHITE);
		this.mEngine.getTextureManager().loadTexture(this.mScoreFontTexture);
		this.mEngine.getFontManager().loadFont(this.mScoreFont);
		/*关卡文字*/
		this.mChapterTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mChapterFont = FontFactory.createFromAsset(this.mChapterTexture, this, "fonts/bluehigh.ttf", 44, true, Color.GREEN);
		this.mEngine.getTextureManager().loadTexture(this.mChapterTexture);
		this.mEngine.getFontManager().loadFont(this.mChapterFont);
		/*发光精灵*/
		this.mSparkTexture = new Texture(64, 64, TextureOptions.DEFAULT);	
		this.mSparkTextureRegion = TextureRegionFactory.createFromAsset
				(this.mSparkTexture, this, "spark1.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mSparkTexture);
		this.mSpark2Texture = new Texture(64, 64, TextureOptions.DEFAULT);	
		this.mSpark2TextureRegion = TextureRegionFactory.createFromAsset
				(this.mSpark2Texture, this, "spark2.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mSpark2Texture);
	}
	
	@Override
	public Scene onLoadScene() {
		this.mMainScene = new Scene(4);	
		this.mMainScene.setBackgroundEnabled(false);
		this.mMainScene.setOnSceneTouchListener(this);
				
		//初始化
		this.init();
		
		//开场效果
		this.onStarting();
							
		//游戏准备
		this.prepareGame();		
		
		//模式调整
		this.adjustModel();
		
		//游戏循环(监听、更新)
		this.gameLoop();
				
		//自动智能提示
		this.autoTips();
	
		return this.mMainScene;
	}

	@Override
	public void onLoadComplete() {
		this.mStartingSound.play();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Jewels.this.mGameRunning = true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Jewels.this.mGameRunning = false;
	}
	
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {		
		//交换的时候不允许操作	
		if(STATE == MOVE_DOWN || STATE == MOVE_LEFT || STATE == MOVE_RIGHT || STATE == MOVE_UP || STATE == FALL){
			return false;
		}
		//是否点击有效区域	
		if(pSceneTouchEvent.getX() > 0 && pSceneTouchEvent.getX() < CAMERA_WIDTH
				&& pSceneTouchEvent.getY() > 0 && pSceneTouchEvent.getY() < CAMERA_WIDTH){ 
			
			if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){	
				mDeadArrList.clear();//试探后可以消去的队列
				mLongestChainTemp = 0;//清空临时记录的最长连接
				this.mCurRow = (int)(pSceneTouchEvent.getX()/CELL_WIDTH);
				this.mCurCol = (int)(pSceneTouchEvent.getY()/CELL_HEIGHT);
				this.mBorder.setMapPosition(this.mCurRow, this.mCurCol);
				this.mBorder.getSprite().setVisible(true);
				if(this.isNext()){//相邻
					this.mBorder.getSprite().setVisible(false);
					this.setMoveDirection();
				}else if(this.mCurRow == this.mLastRow && this.mCurCol == this.mLastCol){//两次点击是同一个
					this.mLastRow = -2;
					this.mLastCol = -2;
					this.mBorder.getSprite().setVisible(false);
				}else {//不相邻不是同一个
					this.mLastRow = this.mCurRow;
					this.mLastCol = this.mCurCol;
					this.mBorder.setMapPosition(this.mCurRow, this.mCurCol);
					this.mBorder.getSprite().setVisible(true);
				}				
			}
		}
		return false;		
	}	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Jewels.this.onGameOver();
			return true;
		} else {
			return false;
		}
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	/**
	 * 游戏初始化
	 */
	private void init(){
		this.initMode();
		this.initFields();
		this.initBG();
		this.initCellBG();
		this.initJewels();	
		this.initBorderSprite();
		this.initBonus();
		this.initLongestChain();
		this.initScore();
		this.initSound();
	}
	
	/**
	 * 初始化游戏模式
	 */
	private void initMode(){
		Intent intent = getIntent();
		this.mGameModel = intent.getStringExtra("mode");
	}
	
	/**
	 * 游戏模式调整
	 */
	private void adjustModel(){
		//时间模式得加一个定时器
		if(this.mGameModel.equals("timed")){
			this.mBonus.setWidth(CAMERA_WIDTH/2);
			this.mMainScene.registerUpdateHandler(new TimerHandler(0.5f, true, new ITimerCallback() {				
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					if(Jewels.this.mGameRunning){
						if(mBonus.getWidth() > 0.0f){
							mBonus.setWidth(mBonus.getWidth() - 2);
						}else {
			    			Message message1 = new Message();
			    			message1.what = 4;
			    			handler.sendMessage(message1);
			    			Message message = new Message();
			    			message.what = 1;
			    			handler.sendMessage(message);
						}
					}					
				}
			}));
		}
	}
	
	/**
	 * 线程之外发送消息的句柄
	 */
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				onStopGame();
				break;
			case 1:
				submitScore();
				break;
			case 2:
				showLongMessage("死局");
				break;
			case 3:
				doTips();
				break;
			case 4:
				showLongMessage("超时");
				break;
			default:
				break;
			}
		}				
	};
	
	/**
	 * 游戏准备时间
	 */
	private void prepareGame(){
		this.mMainScene.registerUpdateHandler(new TimerHandler(1.0f, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				mMainScene.unregisterUpdateHandler(pTimerHandler);
				Jewels.this.mGameRunning = true;
			}
		}));
	}
	
	/**
	 * 初始化音效
	 */
	private void initSound(){
		try {
			this.mSwapErrorSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "raw/illegal_move.ogg");
			this.mFallSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "raw/drop1.ogg");
			this.mRemoveSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "raw/remove.ogg");
			this.mStartingSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "raw/nextlevel.ogg");
			this.mSwapErrorSound.setVolume(50);
			this.mFallSound.setVolume(50);
			this.mRemoveSound.setVolume(50);
			this.mStartingSound.setVolume(50);
		} catch (final IOException e) {
			Debug.e("mGoodMusic Error", e);
		}
	}
	
	/**
	 * 游戏循环
	 */
	private void gameLoop(){
		this.mMainScene.registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				
				if(Jewels.this.mGameRunning){		
					switch (STATE) {
					case MOVE_UP:
						moveUp();
						break;
					case MOVE_DOWN:
						moveDown();
						break;
					case MOVE_LEFT:
						moveLeft();
						break;
					case MOVE_RIGHT:
						moveRight();
						break;
					case CHECK:
						checkMapDead(); //死局检测
						removeHorizontal(); //水平消去
						removeVrtical();    //垂直消去		
						changeState();
						break;
					case FALL:
						refreshScale(); //消去动画
						fillEmpty();    //填充空缺
						break;
					case DEAD:
						Jewels.this.mGameRunning = false;
						Message msg = new Message();
						msg.what = 2;
						handler.sendMessage(msg);
						Message msg1 = new Message();
						msg1.what = 1;
						handler.sendMessage(msg1);
						break;						
					default:
						break;
					}
				}				
			}			
		});
	}
	
	/**
	 * 钻石自动发光提示(可消去的)
	 */
	private void autoTips(){		
		this.mMainScene.registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() {			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				if(mGameRunning){
					if(STATE == CHECK){
						mTime ++;
						if(mTime >= 10){
							//提示
							Message msg = new Message();
							msg.what = 3;
							handler.sendMessage(msg);
							mTime = 0;
						}
					}else{
						mTime = 0;
					}
				}
			}
		}));
	}
	
	/**
	 * 初始化字段
	 */
	private void initFields(){
		this.mLastRow = -2;
		this.mLastCol = -2;
		this.mIsSwaping = false;
		mDeadArrList = new ArrayList<String>();
	}
		
	/**
	 * 初始化背景
	 */
	private void initBG(){
		final Sprite background = new Sprite(0, 0, this.mBackgroundTextureRegion);
		this.mMainScene.getLayer(LAYER_BACKGROUND).addEntity(background);
	}
	
	/**
	 * 初始化关卡进度条
	 */
	private void initBonus(){
		//静态文字背景
		final Sprite bonusBG = new Sprite(10, 425, this.mBonusStaticBGTextureRegion);
		this.mMainScene.getLayer(LAYER_BACKGROUND).addEntity(bonusBG);
		//关卡数字
		this.mChapterText = new ChangeableText(70, 416, this.mChapterFont, "1");
		this.mChapterText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mChapterText.setAlpha(1);
		this.mChapterText.setScaleY(1.5f);
		this.mMainScene.getLayer(LAYER_SCORE).addEntity(this.mChapterText);
		//关卡数字后的‘X‘
		this.mXText = new ChangeableText(86, 420, this.mChapterFont, "X");
		this.mXText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mXText.setAlpha(1);
		this.mXText.setScaleY(1.0f);
		this.mMainScene.getLayer(LAYER_SCORE).addEntity(this.mXText);
		//分数条背景
		final Sprite bonus = new Sprite(5, 460, this.mBonusBGTextureRegion);
		this.mMainScene.getLayer(LAYER_BACKGROUND).addEntity(bonus);
		this.mBonus = new Sprite(7, 462, this.mBonusTextureRegion);
		this.mBonus.setWidth(0);
		this.mMainScene.getLayer(LAYER_SCORE).addEntity(this.mBonus);
	}
	
	/**
	 * 初始化最长连接面板
	 */
	private void initLongestChain(){
		//Longest Chain
		final Text longestChain = new Text(146, 422, this.mLongestChainFont, "Longest Chain:", HorizontalAlign.LEFT);
		longestChain.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		longestChain.setAlpha(1);
		longestChain.setScale(0.55f, 1.2f);
		this.mMainScene.getLayer(LAYER_SCORE).addEntity(longestChain);
		//动态数字---最长连接
		this.mLongestChainText = new ChangeableText(293, 419, this.mChapterFont, "0");
		this.mLongestChainText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mLongestChainText.setAlpha(1);	
		this.mLongestChainText.setScale(0.8f,1.1f);
		this.mMainScene.getLayer(LAYER_SCORE).addEntity(this.mLongestChainText);
	}
	
	/**
	 * 初始化分数面板
	 */
	private void initScore(){
		this.mScoreBGText = new ChangeableText(170, 336, this.mScoreFont, "000000");
		this.mScoreBGText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mScoreBGText.setAlpha(0.4f);
		this.mScoreBGText.setScaleY(1.5f);
		this.mMainScene.getLayer(LAYER_SCORE).addEntity(this.mScoreBGText);
		
		this.mScoreText = new ChangeableText(295, 336, this.mScoreFont, "0",7);
		this.mScoreText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mScoreText.setAlpha(1);	
		this.mScoreText.setScaleY(1.5f);
		this.mMainScene.getLayer(LAYER_SCORE).addEntity(this.mScoreText);
	}
	
	/**
	 * 初始化单元格背景
	 */
	private void initCellBG(){
		//单元格大背景
		final Sprite board = new Sprite(0, 0, mBoardTextureRegion);
		board.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		board.setAlpha(0.2f);
		this.mMainScene.getLayer(LAYER_BG_CELL).addEntity(board);
		//单元格小背景
		Sprite cellBG[][] = new Sprite[CELLBG_HORIZONTAL][CELLBG_VERTICAL];
		for(int i=0; i<CELLBG_HORIZONTAL; i++){
			for(int j=0; j<CELLBG_VERTICAL; j++){
				cellBG[i][j] = new BackgroundCell(i, j, this.mBGCellTextureRegion);
				cellBG[i][j].setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				cellBG[i][j].setAlpha(0.2f);
				this.mMainScene.getLayer(LAYER_BG_CELL).addEntity(cellBG[i][j]);
			}
		}
	}
	
	/**
	 * 初始化钻石
	 */
	private void initJewels(){			
		this.mHashMap = new HashMap<String, JewelSprite>();
		for(int i = 0; i < CELLS_HORIZONTAL; i++){
			for(int j = 0; j < CELLS_VERTICAL; j++){
				String key = getKey(i, j);
				JewelSprite value = getRandomJewel(i, j);
				while(checkHorizontal(value).size() >= 3 || checkVertical(value).size() >= 3){
					value = getRandomJewel(i, j);
				}				
				mHashMap.put(key, value);
				this.mMainScene.getLayer(LAYER_JEWELS).addEntity(
						this.mHashMap.get(key).getJewel());				
			}
		}
	}
	
	/**
	 * 初始化钻石边框精灵
	 */
	private void initBorderSprite(){
		//边框精灵先隐藏起来，哪个钻石被点击了移动到哪。
		this.mBorder = new BorderSprite(-2, -2, mBorderTextureRegion);
		this.mBorder.getSprite().setVisible(false);
		this.mBorder.getSprite().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mBorder.getSprite().addShapeModifier(new LoopShapeModifier(new SequenceShapeModifier
				(new AlphaModifier(0.4f, 1, 0), new AlphaModifier(0.2f, 0, 1))));
		this.mMainScene.getLayer(LAYER_JEWELS).addEntity(this.mBorder.getSprite());	
	}	
			
	/**
	 * 开场效果(音乐、阴影效果)
	 */
	private void onStarting(){
		//阴影效果
		final Sprite background2 = new Sprite(0, 0, this.mBackground2TextureRegion);
		background2.setAlpha(0.7f);
		background2.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainScene.getLayer(LAYER_BACKGROUND).addEntity(background2);
		background2.addShapeModifier(new SequenceShapeModifier
				(new AlphaModifier(5f, 0.7f, 0f)));	
		//音效
		this.mStartingSound.play();
	}
		
	/**
	 * 执行发光提示
	 */
	private void doTips(){
		if(mSpark == null){
			mSpark = new Sprite(0, 0, mSparkTextureRegion);
			mSpark2 = new Sprite(0, 0, mSpark2TextureRegion);
			mSpark.setVisible(false);
			mSpark2.setVisible(false);
			this.mMainScene.getTopLayer().addEntity(mSpark);	
			this.mMainScene.getTopLayer().addEntity(mSpark2);	
		}
		if(mDeadArrList.size() > 0){
			String key = mDeadArrList.get(MathUtils.random(0, mDeadArrList.size()-1));
			mSpark.setPosition(Integer.parseInt(key.substring(0, 1))*CELL_WIDTH + 8,
					Integer.parseInt(key.substring(1, 2))*CELL_HEIGHT + 8);
			mSpark2.setPosition(Integer.parseInt(key.substring(0, 1))*CELL_WIDTH + 4,
					Integer.parseInt(key.substring(1, 2))*CELL_HEIGHT + 4);
			mSpark.setVisible(true);
			mSpark2.setVisible(true);
			mSpark2.addShapeModifier(new RotationModifier(1.5f, 0, 90));
			mSpark.addShapeModifier(new SequenceShapeModifier(
					new ScaleModifier(1.5f, 0.4f, 0.6f),new ScaleModifier(0.1f, 0.6f, 0f)));
			mSpark2.addShapeModifier(new SequenceShapeModifier(
					new ScaleModifier(1.5f, 0.5f, 1.1f),new ScaleModifier(0.1f, 1.1f, 0f)));
		}else{
			checkMapDead();
		}
	}
	
	/**
	 * 设置交换移动方向
	 */
	private void setMoveDirection(){
		if(this.mLastRow == this.mCurRow && this.mLastCol > this.mCurCol){
			this.STATE = this.MOVE_UP;
		}
		if(this.mLastRow == this.mCurRow && this.mLastCol < this.mCurCol){
			this.STATE = this.MOVE_DOWN;
		}
		if(this.mLastRow > this.mCurRow && this.mLastCol == this.mCurCol){
			this.STATE = this.MOVE_LEFT;
		}
		if(this.mLastRow < this.mCurRow && this.mLastCol == this.mCurCol){
			this.STATE = this.MOVE_RIGHT;
		}		
	}
	
	/**
	 * 更新缩放动画
	 */
	private void refreshScale(){
		for(int i = 0; i < CELLS_HORIZONTAL; i++)
		{
			for(int j = 0; j < CELLS_VERTICAL; j++)
			{
				if(mHashMap.get(getKey(j, i)) != null && mHashMap.get(getKey(j, i)).getState() == STATE_SCALEINT)
				{
					mHashMap.get(getKey(j, i)).doScale();
				}
			}
		}
	}
	
	/**
	 * 向上交换移动
	 */
	private void moveUp(){
		if(mIsSwaping)
		{
			if(moveValue < CELL_HEIGHT)
			{
				moveValue += SPEED;				
				final float x = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getX();
				final float curY = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getY();
				final float lastY = this.mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().getY();
				mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().setPosition(x, curY + SPEED);
				mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().setPosition(x, lastY - SPEED);
			}
			else
			{
				swapInHashMap();
				STATE = CHECK;
				moveValue = 0;
				mIsSwaping = false;
				this.mLastRow = -2;
				this.mLastCol = -2;
			}
		}
		else
		{
			if(moveValue < CELL_HEIGHT)
			{
				moveValue += SPEED;
				final float x = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getX();
				final float curY = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getY();
				final float lastY = this.mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().getY();
				mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().setPosition(x, curY + SPEED);
				mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().setPosition(x, lastY - SPEED);
			}
			else 
			{
				swapInHashMap();
				if(isSwapFall())
				{
					this.mFallSound.play();
					STATE = CHECK;
					this.mLastRow = -2;
					this.mLastCol = -2;
				}
				else
				{
					this.mSwapErrorSound.play();
					mIsSwaping = true;
				}
				moveValue = 0;
			}
		}	
	}
	
	/**
	 * 向下交换移动
	 */
	private void moveDown(){		
		if(mIsSwaping)
		{
			if(moveValue < CELL_HEIGHT)
			{
				moveValue += SPEED;				
				final float x = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getX();
				final float curY = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getY();
				final float lastY = this.mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().getY();
				mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().setPosition(x, curY - SPEED);
				mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().setPosition(x, lastY + SPEED);
			}
			else
			{
				swapInHashMap();
				STATE = CHECK;
				moveValue = 0;
				mIsSwaping = false;
				this.mLastRow = -2;
				this.mLastCol = -2;
			}
		}
		else
		{
			if(moveValue < CELL_HEIGHT)
			{
				moveValue += SPEED;
				final float x = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getX();
				final float curY = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getY();
				final float lastY = this.mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().getY();
				mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().setPosition(x, curY - SPEED);
				mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().setPosition(x, lastY + SPEED);
			}
			else 
			{
				swapInHashMap();
				if(isSwapFall())
				{
					this.mFallSound.play();
					STATE = CHECK;
					this.mLastRow = -2;
					this.mLastCol = -2;
				}
				else
				{
					this.mSwapErrorSound.play();
					mIsSwaping = true;
				}
				moveValue = 0;
			}
		}		
	}
	
	/**
	 * 向左交换移动
	 */
	private void moveLeft(){
		if(mIsSwaping)
		{
			if(moveValue < CELL_HEIGHT)
			{
				moveValue += SPEED;				
				final float curX = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getX();
				final float lastX = this.mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().getX();
				final float y = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getY();
				mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().setPosition(curX + SPEED, y);
				mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().setPosition(lastX - SPEED, y);
			}
			else
			{
				swapInHashMap();
				STATE = CHECK;
				moveValue = 0;
				mIsSwaping = false;
				this.mLastRow = -2;
				this.mLastCol = -2;
			}
		}
		else
		{
			if(moveValue < CELL_HEIGHT)
			{
				moveValue += SPEED;
				final float curX = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getX();
				final float lastX = this.mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().getX();
				final float y = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getY();
				mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().setPosition(curX + SPEED, y);
				mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().setPosition(lastX - SPEED, y);
			}
			else 
			{
				swapInHashMap();
				if(isSwapFall())
				{
					this.mFallSound.play();
					STATE = CHECK;
					this.mLastRow = -2;
					this.mLastCol = -2;
				}
				else
				{
					this.mSwapErrorSound.play();
					mIsSwaping = true;
				}
				moveValue = 0;
			}
		}	
	}
	
	/**
	 * 向右交换移动
	 */
	private void moveRight(){
		if(mIsSwaping)
		{
			if(moveValue < CELL_HEIGHT)
			{
				moveValue += SPEED;				
				final float curX = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getX();
				final float lastX = this.mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().getX();
				final float y = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getY();
				mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().setPosition(curX - SPEED, y);
				mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().setPosition(lastX + SPEED, y);
			}
			else
			{
				swapInHashMap();
				STATE = CHECK;
				moveValue = 0;
				mIsSwaping = false;
				this.mLastRow = -2;
				this.mLastCol = -2;
			}
		}
		else
		{
			if(moveValue < CELL_HEIGHT)
			{
				moveValue += SPEED;
				final float curX = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getX();
				final float lastX = this.mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().getX();
				final float y = this.mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().getY();
				mHashMap.get(getKey(mCurRow, mCurCol)).getJewel().setPosition(curX - SPEED, y);
				mHashMap.get(getKey(mLastRow, mLastCol)).getJewel().setPosition(lastX + SPEED, y);
			}
			else 
			{
				swapInHashMap();
				if(isSwapFall())
				{
					this.mFallSound.play();
					STATE = CHECK;
					this.mLastRow = -2;
					this.mLastCol = -2;
				}
				else
				{
					this.mSwapErrorSound.play();
					mIsSwaping = true;
				}
				moveValue = 0;
			}
		}
	}
	
	/**
	 * 填充空缺位置
	 */
	private void fillEmpty(){
		//所有消去状态的钻石冒泡到顶部
    	for(int i = 0; i < CELLS_HORIZONTAL; i++)
    	{
    		for(int j = 0; j < CELLS_VERTICAL; j++)
    		{
    			if(mHashMap.get(getKey(j, i)).getState() == STATE_DEAD)
    			{
    				int p = i;
    				while((p-1) >= 0  && 
    						mHashMap.get(getKey(j, p-1)).getState() != STATE_DEAD){
    					JewelSprite temp = mHashMap.get(getKey(j, p-1));
    					mHashMap.put(getKey(j, p-1),mHashMap.get(getKey(j, p)));
    					mHashMap.put(getKey(j, p),temp);
    					p--;
    				}
    			}
    		}
    	}
    	//在HashMap里补充消去的钻石
    	for(int i = 0; i < CELLS_HORIZONTAL; i++)
    	{
    		for(int j = 0; j < CELLS_VERTICAL; j++)
    		{
    			if(mHashMap.get(getKey(j, i)).getState() == STATE_DEAD)
    			{	
        			int v = 1;
        			for(v = 1; j+v < CELLS_VERTICAL 
    						&&  mHashMap.get(getKey(j, i)).getStyle() == mHashMap.get(getKey(j, i+v)).getStyle()
    						&&  mHashMap.get(getKey(j, i)).getState() == mHashMap.get(getKey(j, i+v)).getState(); 
        				v++);
					for(int z = v; z > 0; z--){
						JewelSprite newJewel = getRandomJewel(j, -z);
						mMainScene.getLayer(LAYER_JEWELS).addEntity(newJewel.getJewel());
						mHashMap.put(getKey(j, v-z), newJewel);						
					}
    			}
    		}
    	}
    	//在场景里下落
    	int count = 0;
    	for(int i = 0; i < CELLS_HORIZONTAL; i++)
    	{
    		for(int j = CELLS_VERTICAL - 1; j >= 0; j--)
    		{
    			if(mHashMap.get(getKey(i, j)).getJewel().getY() < j*CELL_HEIGHT)//还没有下落到位
				{
					mHashMap.get(getKey(i, j)).getJewel()
						.setPosition(i*CELL_WIDTH, mHashMap.get(getKey(i, j)).getJewel().getY()+ CELL_HEIGHT/2);
					count++;
				}
    		}
    	}
    	if(count == 0){
    		STATE = CHECK;
    	}
	}
	
	/**
	 * 死局检测
	 */
	private void checkMapDead(){
		int count = 0;
		for(int i = 0; i < CELLS_HORIZONTAL; i++)
		{
			for(int j = 0; j < CELLS_VERTICAL; j++)
			{
				if(mHashMap.get(getKey(j, i)).getState() == STATE_NORMAL){
					count ++;
				}
			}
		}
		if(count == 64){
			//所有遍历一遍		
			if(this.mDeadArrList.size() == 0){
				int i = 0;
				while(i < CELLS_HORIZONTAL){		
					int j = 0;
					while(j < CELLS_HORIZONTAL){
						if(checkDead(mHashMap.get(getKey(j, i))) == true){
							this.mDeadArrList.add(getKey(j, i));
						}					
						j += 1;
					}
					i += 1;
				}	
//				if(this.mDeadArrList.size() == 0){
//					STATE = DEAD;
//				}
			}
		}
	}
	
	/**
	 * 单个钻石的交换试探检测
	 */
	private boolean checkDead(final JewelSprite sprite){
		boolean flag = false;
		int row = sprite.getRow();
		int col = sprite.getCol();
		JewelSprite temp = sprite;
		//向上试探
		if((col - 1) >= 0){			
			mHashMap.put(getKey(row, col), mHashMap.get(getKey(row, col-1)));
			mHashMap.put(getKey(row, col-1), temp); 			
 			int v = 0;
			for(v = 1; col-1-v >= 0 
						&&  mHashMap.get(getKey(row,col-1)).getStyle() == mHashMap.get(getKey(row, col-1-v)).getStyle()
						&&  mHashMap.get(getKey(row, col-1)).getState() == mHashMap.get(getKey(row, col-1-v)).getState(); 
					v++);
			if(v >= 3){
				flag = true;
			}
			mHashMap.put(getKey(row, col-1), mHashMap.get(getKey(row, col)));
			mHashMap.put(getKey(row, col), temp);			
		}
		//向下试探
		if((col + 1) < CELLS_HORIZONTAL){
			mHashMap.put(getKey(row, col), mHashMap.get(getKey(row, col+1)));
			mHashMap.put(getKey(row, col+1), temp);
 			int v1 = 0;
			for(v1 = 1; col+1+v1 < CELLS_HORIZONTAL
						&&  mHashMap.get(getKey(row,col+1)).getStyle() == mHashMap.get(getKey(row, col+1+v1)).getStyle()
						&&  mHashMap.get(getKey(row, col+1)).getState() == mHashMap.get(getKey(row, col+1+v1)).getState(); 
					v1++);
			if(v1 >= 3){
				flag = true;
			}
			mHashMap.put(getKey(row, col+1), mHashMap.get(getKey(row, col)));
			mHashMap.put(getKey(row, col), temp);	
		}
		//向左试探
		if((row - 1) >= 0){
			mHashMap.put(getKey(row, col), mHashMap.get(getKey(row-1, col)));
			mHashMap.put(getKey(row-1, col), temp);
 			int v2 = 0;
			for(v2 = 1; row-1-v2 >= 0 
						&&  mHashMap.get(getKey(row-1,col)).getStyle() == mHashMap.get(getKey(row-1-v2, col)).getStyle()
						&&  mHashMap.get(getKey(row-1, col)).getState() == mHashMap.get(getKey(row-1-v2, col)).getState(); 
					v2++);
			if(v2 >= 3){
				flag = true;
			}
			mHashMap.put(getKey(row-1, col), mHashMap.get(getKey(row, col)));
			mHashMap.put(getKey(row, col), temp);	
		}
		//向右试探
		if((row + 1) < CELLS_VERTICAL){
			mHashMap.put(getKey(row, col), mHashMap.get(getKey(row+1, col)));
			mHashMap.put(getKey(row+1, col), temp);
 			int v3 = 0;
			for(v3 = 1; row+1+v3 < CELLS_VERTICAL
						&&  mHashMap.get(getKey(row+1,col)).getStyle() == mHashMap.get(getKey(row+1+v3, col)).getStyle()
						&&  mHashMap.get(getKey(row+1, col)).getState() == mHashMap.get(getKey(row+1+v3, col)).getState(); 
					v3++);
			if(v3 >= 3){
				flag = true;
			}
			mHashMap.put(getKey(row+1, col), mHashMap.get(getKey(row, col)));
			mHashMap.put(getKey(row, col), temp);	
		}
		return flag;
	}
	
	/**
	 * 水平消去
	 */
	private void removeHorizontal(){
		int k = 0;
    	for(int i = 0; i < CELLS_VERTICAL; i++)
    	{
    		for(int j = 0; j < CELLS_HORIZONTAL - 2; j++)
    		{
    			if(mHashMap.get(getKey(j, i)).getState() == STATE_NORMAL){
        			for(k = 1; j+k < CELLS_HORIZONTAL 
 				   			&&  mHashMap.get(getKey(j, i)).getStyle() == mHashMap.get(getKey(j+k, i)).getStyle()
 				   			&&  mHashMap.get(getKey(j, i)).getState() == mHashMap.get(getKey(j+k, i)).getState(); 
        				k++);
		 			if(k >= 3)
		 			{
		 				this.addBonus();
		 				this.addScore(k);
		 				removeVrtical(); //这样调T(十)字都可以消
		 				for(int n = 0; n < k; n++)
		 				{
		 					mHashMap.get(getKey(j++, i)).setState(STATE_SCALEINT);
		 				}
		 			}
    			}
    		}
    	}
	}
	
	/**
	 * 垂直消去
	 */
	private void removeVrtical(){
		int k = 0;
    	for(int i = 0; i < CELLS_HORIZONTAL; i++)
    	{
    		for(int j = 0; j < CELLS_VERTICAL - 2; j++)
    		{
    			if(mHashMap.get(getKey(i, j)).getState() == STATE_NORMAL){
        			for(k = 1; j+k < CELLS_VERTICAL 
        					&&  mHashMap.get(getKey(i, j)).getStyle() == mHashMap.get(getKey(i, j+k)).getStyle()
        					&&  mHashMap.get(getKey(i, j)).getState() == mHashMap.get(getKey(i, j+k)).getState(); 
        				k++);
		 			if(k >= 3)
		 			{
		 				this.addBonus();
		 				this.addScore(k);
		 				for(int n = 0; n < k; n++)
		 				{
		 					mHashMap.get(getKey(i, j++)).setState(STATE_SCALEINT);
		 				}
		 			}
    			}
    		}
    	}
	}
	
	/**
	 * 扫描完毕之后，根据有没有消去的来决定是否跳变状态
	 */
	private void changeState(){
		int fallCount = 0;
		for(int i = 0; i < CELLS_HORIZONTAL; i++)
		{
			for(int j = 0; j < CELLS_VERTICAL; j++)
			{
				if(mHashMap.get(getKey(j, i)).getState() == STATE_SCALEINT){
					fallCount ++;
				}
			}
		}
		if(fallCount > 0){
			STATE = FALL;
		}
	}
	
	/**
	 * 交换后是否需要消去
	 * @return ture/false(有/无消去的)
	 */
	private boolean isSwapFall(){
		int count = 0;
		//当前钻石的行检测
		if(checkHorizontal(mHashMap.get(getKey(mCurRow, mCurCol))).size() >= 3){
			count += 1;
		}
		//上一个钻石的行检测
		if(checkHorizontal(mHashMap.get(getKey(mLastRow, mLastCol))).size() >= 3){
			count += 1;
		}
		//当前钻石的列检测
		if(checkVertical(mHashMap.get(getKey(mCurRow, mCurCol))).size() >= 3){
			count += 1;
		}
		//上一个钻石的列检测
		if(checkVertical(mHashMap.get(getKey(mLastRow, mLastCol))).size() >= 3){
			count += 1;
		}
		
		if(count == 0){
			return false;
		}else {
			return true;
		}		
	}
						
	/**
	 * 检测水平方向
	 * @param jewel
	 * @return int 水平方向能消去到队列的个数
	 */
	private ArrayList<JewelSprite> checkHorizontal(final JewelSprite jewel){
		ArrayList<JewelSprite> deadArrayList  = new ArrayList<JewelSprite>();
		if(jewel != null){
			int curRow = jewel.getRow();
			final int curCol =jewel.getCol();		
			final int curStyle = jewel.getStyle();
			//向左检测
			while((curRow-1) >= 0){
				if(mHashMap.get(getKey(curRow-1, curCol)) != null){
					if(curStyle == mHashMap.get(getKey(curRow-1, curCol)).getStyle()){
						deadArrayList.add(mHashMap.get(getKey(curRow-1, curCol)));
					}else {//出现不连续的，跳过去
						curRow = 0;
					}				
				}
				curRow -= 1;
			}
			curRow = jewel.getRow();//回来原位重新开始
			deadArrayList.add(mHashMap.get(getKey(curRow, curCol)));
			//向右检测
			while((curRow+1) < CELLS_VERTICAL){
				if(mHashMap.get(getKey(curRow+1, curCol)) != null){
					if(curStyle == mHashMap.get(getKey(curRow+1, curCol)).getStyle()){
						deadArrayList.add(mHashMap.get(getKey(curRow+1, curCol)));
					}else {//出现不连续的，跳过去
						curRow = CELLS_VERTICAL;
					}
				}
				curRow += 1;
			}			
		}
		return deadArrayList;
	}
	
	/**
	 * 检测垂直方向
	 * @param jewel
	 * @return int 垂直方向能消去到队列的个数
	 */
	private ArrayList<JewelSprite>  checkVertical(final JewelSprite jewel){		
		ArrayList<JewelSprite> deadArrayList  = new ArrayList<JewelSprite>();
		if(jewel != null){
			ArrayList<JewelSprite> temp = new ArrayList<JewelSprite>();
			final int curRow = jewel.getRow();
			int curCol =jewel.getCol();			
			final int curStyle = jewel.getStyle();
			//向上检测
			while((curCol-1) >= 0){
				if(mHashMap.get(getKey(curRow, curCol-1)) != null){
					if(curStyle == mHashMap.get(getKey(curRow, curCol-1)).getStyle()){
						temp.add(mHashMap.get(getKey(curRow, curCol-1)));
					}else {//出现不连续的，跳过去，检查下侧
						curCol = 0;
					}
				}
				curCol -= 1;
			}
			if(temp.size() > 0){
				for(int p = temp.size()-1; p >= 0; p--){
					deadArrayList.add(temp.get(p));
				}
			}		
			curCol =jewel.getCol();	//回来原位重新开始
			deadArrayList.add(mHashMap.get(getKey(curRow, curCol)));
			while((curCol+1) < CELLS_HORIZONTAL){//向下检测
				if(mHashMap.get(getKey(curRow, curCol+1)) != null){
					if(curStyle == mHashMap.get(getKey(curRow, curCol+1)).getStyle()){
						deadArrayList.add(mHashMap.get(getKey(curRow, curCol+1)));
					}else {//出现不连续的，跳过去
						curCol = CELLS_HORIZONTAL;
					}
				}
				curCol += 1;
			}
		}
		return deadArrayList;
	}
			
	/**
	 * 相邻两颗钻石在HashMap里交换位置
	 */
	private void swapInHashMap(){				
		//HashMap里互换
		JewelSprite temp = mHashMap.get(getKey(mLastRow, mLastCol));
		mHashMap.remove(getKey(mLastRow, mLastCol));
		mHashMap.put(getKey(mLastRow, mLastCol), 
				mHashMap.get(getKey(mCurRow, mCurCol)));
		mHashMap.remove(getKey(mCurRow, mCurCol));
		mHashMap.put(getKey(mCurRow, mCurCol), temp);	
	}
				
	/**
	 * 随机获取一个钻石精灵
	 */
	public JewelSprite getRandomJewel(final int row,final int col){
		int style = MathUtils.random(0, 6);
		JewelSprite jewelSprite = new JewelSprite(row, col, mJewelTextureRegion[style]);
		jewelSprite.setStyle(style);
		return jewelSprite;
	}
	
	/**
	 * 是否是相邻两颗钻石
	 * @return true  相邻
	 * @return false 不相邻
	 */
	private boolean isNext(){
		if((Math.abs(this.mCurRow - this.mLastRow) == 1 && this.mCurCol == this.mLastCol)//左右相邻
				|| (Math.abs(this.mCurCol - this.mLastCol) == 1 && this.mCurRow == this.mLastRow)){//上下相邻
			return true;
		}else {
			return false;
		}		
	}

	/**
	 * 行/列转换成HashMap中到key
	 * @return  HashMap的key
	 */
	private String getKey(final int row, final int col){
		return String.valueOf(row) + String.valueOf(col);
	}	
	
	/**
	 * 增长游戏关卡进度
	 */
	private void addBonus(){
		this.mRemoveSound.play();
		if(this.mBonus.getWidth() > 299){
			this.startNewChapter();		
		}else {
			this.mBonus.setWidth(this.mBonus.getWidth() + mChapterStep);
		}
		this.updateLongestChain();
	}
	
	/**
	 * 开始新一关
	 */
	private void startNewChapter(){
		this.mChapter += 1;
		if(this.mChapter > 10){
			//通关
			this.onGameOver();
		}else{
			//新一关的开始
			if(this.mGameModel.equals("timed")){
				this.mBonus.setWidth(CAMERA_WIDTH/2);
			}else {
				this.mBonus.setWidth(0);
			}
			if(this.mGameModel.equals("infinite")){//无限模式(每过一关需要的步数增多)
				this.mChapterStep -= 0.5;
			}				
			this.mChapterText.setText(String.valueOf(this.mChapter));
			//新关开始的动画(换背景、最下面一行全部消去)
			this.changeChapter();
		}
	}
	
	/**
	 * 更新最长连接
	 */
	private void updateLongestChain(){
		this.mLongestChainTemp++;
		if(mLongestChainTemp > mLongestChain){
			mLongestChain = mLongestChainTemp;
			this.mLongestChainText.setText(String.valueOf(mLongestChain));
		}
	}
	
	/**
	 * 过关换背景
	 */
	private void changeChapter(){
		//随机消去一些
		for(int i = 0; i < CELLS_VERTICAL; i++){
			mHashMap.get(getKey(i, 7)).setState(STATE_SCALEINT);
		}
		//换背景
		this.mBackgroundTexture.clearTextureSources();
		TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, getRandomBG(), 0, 0);
	}
	
	/**
	 * 随机获取一个与原来不一样的背景
	 * @return
	 */
	private String getRandomBG(){
		String bg = "";
		int temp = MathUtils.random(1, 4);
		while(temp == mCurBGNum){
			temp = MathUtils.random(1, 4);
		}
		mCurBGNum = temp;
		bg = "bground" + String.valueOf(mCurBGNum) + ".png";
		return bg;
	}
	
	/**
	 * 增长分数
	 * @param fallCount
	 */
	private void addScore(int fallCount){
		switch (fallCount) {
		case 3:
			this.mScore += 20;
			break;
		case 4:
			this.mScore += 40;
			break;
		case 5:
			this.mScore += 60;
			break;
		case 6:
			this.mScore += 80;
			break;
		case 7:
			this.mScore += 100;
			break;
		case 8:
			this.mScore += 120;
			break;
		default:
			break;
		}
		this.adjustScorePanel();
		if(this.mScore < 9999999){
			this.mScoreText.setText(String.valueOf(this.mScore));
		}else{
			Toast.makeText(getApplicationContext(), "恭喜你，你通关了！", Toast.LENGTH_LONG).show();
			this.mScoreBGText.setText("0000000");
			this.mScoreText.setText("0");
			this.mScore = 0;
			this.mScoreText.setPosition(295, 336);
		}
	}
	
	/**
	 * 调整分数面板
	 */
	private void adjustScorePanel(){
		if(this.mScore > 9 && (int)this.mScoreText.getX() == 295){
			this.mScoreBGText.setText("00000");
			this.mScoreText.setPosition(274, 336);
		}else if(this.mScore > 99 && this.mScoreText.getX() == 274){
			this.mScoreBGText.setText("0000");
			this.mScoreText.setPosition(253, 336);
		}else if(this.mScore > 999 && this.mScoreText.getX() == 253){
			this.mScoreBGText.setText("000");
			this.mScoreText.setPosition(232, 336);
		}else if(this.mScore > 9999 && this.mScoreText.getX() == 232){
			this.mScoreBGText.setText("00");
			this.mScoreText.setPosition(211, 336);
		}else if(this.mScore > 99999 && this.mScoreText.getX() == 211){
			this.mScoreBGText.setText("0");
			this.mScoreText.setPosition(190, 336);
		}else if(this.mScore > 999999 && this.mScoreText.getX() == 190){
			this.mScoreBGText.setText("");
			this.mScoreText.setPosition(169, 336);
		}
	}
	
	/**
	 * 游戏结束提示
	 */
	private void onStopGame(){				
        Dialog dialog = new AlertDialog.Builder(this)
        .setTitle("游戏提示")
        .setMessage("你确定要退出游戏吗？")
        .setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
    			Message message = new Message();
    			message.what = 1;
    			handler.sendMessage(message);
            }
        })
        .setNeutralButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	Jewels.this.mGameRunning = true;
            }
        })
        .create();
        dialog.show();
	}
	
	/**
	 * 显示短时间消息提示
	 * @param msg
	 */
	private void showLongMessage(String msg){
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 分数保存
	 */
	private void submitScore(){		
		//获取姓名
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		final View submitTextView = layoutInflater.inflate(R.layout.submit, null);
		final EditText mNameEditText = (EditText)submitTextView.findViewById(R.id.edittext_score);
		mNameEditText.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				mNameEditText.setHint(null);//文本框灰色的提示文字消失
			}
		});		
		
        Dialog dialog = new AlertDialog.Builder(this)
        .setTitle("GameOver")
        .setMessage("游戏得分： " + String.valueOf(mScore))
        .setView(submitTextView)
        .setPositiveButton("提  交", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
    				SQLiteHelper helper = new SQLiteHelper(getApplicationContext());    
    				final String name = mNameEditText.getText().toString();
    				if(name.equals("") || name.trim().equals("")){
    					Toast.makeText(getApplicationContext(), "名字不能为空！", Toast.LENGTH_LONG).show();
    					submitScore();
    				}else if(helper.isNameExist(name)){
    					Toast.makeText(getApplicationContext(), "该名已经存在！", Toast.LENGTH_LONG).show();
    					submitScore();
    				}else {
        				final int model;
        				if(mGameModel.equals("timed")){
        					model = 1;
        				}else {
    						model = 0;
    					}
        				final String rank = helper.queryrank(String.valueOf(mScore),model);
        			    helper.insertData(name,mScore,model,Integer.parseInt(rank)+1);//插入排行榜数据库	
        			    dialog.cancel();
        			    toMenuView();
					}
            }
        })
        .setNeutralButton("取  消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	toMenuView();
            }
        })
        .create();
        dialog.show();
	}
	
	/**
	 * 游戏结束
	 */
	private void onGameOver(){
		Jewels.this.mGameRunning = false;
		if(this.mScore > 0){
			Message message = new Message();
			message.what = 0;
			handler.sendMessage(message);
		}else {
			toMenuView();			
		}		
	}
	
	/**
	 * 转到菜单界面
	 */
	private void toMenuView(){
		Jewels.this.mMainScene.clearUpdateHandlers();
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), Menu.class);
		startActivity(intent);
		Jewels.this.finish();
	}
}
