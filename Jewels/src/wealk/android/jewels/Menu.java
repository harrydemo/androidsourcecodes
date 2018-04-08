package wealk.android.jewels;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceShapeModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.KeyEvent;

/**
 * 游戏开始菜单
 * @author Qingfeng
 * @time 2010-10-16 
 */
public class Menu extends BaseGameActivity implements IOnAreaTouchListener {
	
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 534;
	
	protected static final int LAYER_BGROUND = 0;
	protected static final int LAYER_MENU = LAYER_BGROUND + 1;

	// ===========================================================
	// Fields
	// ===========================================================
	protected Camera mCamera;

	protected Scene mMainScene;//主场景
	protected Scene mModelScene;//选择模式场景
	protected Scene mNormalRankScene;//普通模式排行榜场景
	protected Scene mTimedRankScene;//时间模式排行榜场景
	
	//排行榜列表
	private SQLiteHelper mSQLiteHelper = new SQLiteHelper(this);	

	//背景
	private Texture mBackGroundTexture;
	protected TextureRegion mBackGroundTextureRegion;
	//动态背景
	private Texture mDynamicBgroundTexture;
	private TextureRegion mDynamicBgroundTextureRegion;
	//选项
	private Texture mMenuModelTexture,mMenuRankingTexture,mMenuQuitTexture;
	protected TextureRegion mMenuNewGameTextureRegion,mMenuRankingTextureRegion,mMenuQuitTextureRegion;	
	//字体
	private Texture mFontTexture,mGreenFontTexture;
	private Font mFont,mGreenFont;
	//选项菜单
	private Sprite mNewGame,mRanking,mQuit;
	private Text mNewGameText,mRankingText,mQuitText;
	//模式菜单
	private Sprite mNormalMode,mTimedMode,mInfiniteMode;
	private Text mNormalModeText,mTimedModeText,mInfiniteModeText;
	//回退菜单
	private Sprite mBack;
	private Texture mBackTexture,mBackIMGTexture;
	private TextureRegion mBackTextureRegion,mBackIMGTextureRegion;
	//选择模式排行榜
	private Sprite mSelectModel,mSelect2Model,mModelBack;
	private Texture mSelectModelTexture,mModelBackTexture;
	private TextureRegion mSelectModelTextureRegion,mModelBackTextureRegion;
	//排行榜面板
	private Texture mRankingPanelTexture;
	private TextureRegion mRankingPanelTextureRegion;
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================	

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}
	
	@Override
	public void onLoadResources() {	
		//背景
		this.mBackGroundTexture = new Texture(512, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackGroundTexture, this, "gfx/hdpi_title_bg1.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBackGroundTexture);		
		//动态背景
		this.mDynamicBgroundTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mDynamicBgroundTextureRegion = TextureRegionFactory.createFromAsset(this.mDynamicBgroundTexture, this, "gfx/logo.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mDynamicBgroundTexture);	
		//新游戏
		this.mMenuModelTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuNewGameTextureRegion = TextureRegionFactory.createFromAsset(this.mMenuModelTexture, this, "gfx/menubutton1.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mMenuModelTexture);
		//排行榜		
		this.mMenuRankingTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuRankingTextureRegion = TextureRegionFactory.createFromAsset(this.mMenuRankingTexture, this, "gfx/menubutton1.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mMenuRankingTexture);
		//退出
		this.mMenuQuitTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);		
		this.mMenuQuitTextureRegion = TextureRegionFactory.createFromAsset(this.mMenuQuitTexture, this, "gfx/menubutton1.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mMenuQuitTexture);
		//字体
		this.mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = FontFactory.createFromAsset(this.mFontTexture, this, "fonts/bluehigh.ttf", 38, true, Color.WHITE);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);
		this.mGreenFontTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mGreenFont = FontFactory.createFromAsset(this.mGreenFontTexture, this, "fonts/bluehigh.ttf", 26, true, Color.WHITE);
		this.mEngine.getTextureManager().loadTexture(this.mGreenFontTexture);
		this.mEngine.getFontManager().loadFont(this.mGreenFont);
		//回退按钮
		this.mBackTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackTextureRegion =  TextureRegionFactory.createFromAsset(this.mBackTexture, this, "gfx/menubutton1.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBackTexture);
		this.mBackIMGTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackIMGTextureRegion =  TextureRegionFactory.createFromAsset(this.mBackIMGTexture, this, "gfx/backarrow.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBackIMGTexture);
		//选择查看不同模式排行榜		
		this.mSelectModelTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mSelectModelTextureRegion = TextureRegionFactory.createFromAsset(this.mSelectModelTexture, this, "gfx/menubutton1.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mSelectModelTexture);
		this.mModelBackTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mModelBackTextureRegion = TextureRegionFactory.createFromAsset(this.mSelectModelTexture, this, "gfx/menubutton1.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mModelBackTexture);
		//排行榜面板
		this.mRankingPanelTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mRankingPanelTextureRegion = TextureRegionFactory.createFromAsset(this.mRankingPanelTexture, this, "gfx/ranking_bg.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mRankingPanelTexture);
	}

	@Override
	public Scene onLoadScene() {
		//场景
		this.mMainScene = new Scene(2);
		this.mModelScene = new Scene(2);
		this.mNormalRankScene = new Scene(2);
		this.mTimedRankScene = new Scene(2);
		
		this.mModelScene.setBackgroundEnabled(true);	
		this.mNormalRankScene.setBackgroundEnabled(true);	
		this.mTimedRankScene.setBackgroundEnabled(true);	
		//初始化
		this.init();
		
		this.mMainScene.setOnAreaTouchListener(this);
		this.mModelScene.setOnAreaTouchListener(this);
		this.mNormalRankScene.setOnAreaTouchListener(this);
		this.mTimedRankScene.setOnAreaTouchListener(this);
			
		return this.mMainScene;
	}

	@Override
	public void onLoadComplete() {}	
		
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), Jewels.class);
		if(pTouchArea.equals(mNewGame)){
			this.toSelectModel();
		}else if(pTouchArea.equals(mRanking)){
			this.toNormalRanking();
			this.initNormalRanking();
		}else if(pTouchArea.equals(mQuit)){
			this.mMainScene.clearChildScene();
			Menu.this.finish();
		}else if(pTouchArea.equals(mNormalMode)){			
			this.startGame("normal");
		}else if(pTouchArea.equals(mTimedMode)){
			this.startGame("timed");
		}else if(pTouchArea.equals(mInfiniteMode)){
			this.startGame("infinite");
		}else if(pTouchArea.equals(mBack)){
			this.mModelScene.back();
		}else if(pTouchArea.equals(mModelBack)){
			this.mMainScene.clearChildScene();
		}else if(pTouchArea.equals(mSelectModel)){//进入时间模式排行榜
			this.toTimedRanking();
		}else if(pTouchArea.equals(mSelect2Model)){//进入普通模式排行榜
			this.toNormalRanking();
		}
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if(this.mMainScene.hasChildScene()) {
//				this.mModelScene.back();
				this.mMainScene.clearChildScene();
			} else {
				this.finish();
			}
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
		this.initBG();
		this.initDynamic();
		this.initMenu();
		this.initModel();
		this.initNormalRank();
		this.initTimedRank();
		this.initAreaTouch();
	}
	
	/**
	 * 初始化区域触摸事件
	 */
	private void initAreaTouch(){
		this.mMainScene.registerTouchArea(this.mNewGame);
		this.mMainScene.registerTouchArea(this.mRanking);
		this.mMainScene.registerTouchArea(this.mQuit);
		this.mModelScene.registerTouchArea(this.mNormalMode);
		this.mModelScene.registerTouchArea(this.mTimedMode);
		this.mModelScene.registerTouchArea(this.mInfiniteMode);
		this.mModelScene.registerTouchArea(this.mBack);
		this.mNormalRankScene.registerTouchArea(this.mSelectModel);	
		this.mNormalRankScene.registerTouchArea(this.mModelBack);
		this.mTimedRankScene.registerTouchArea(this.mSelect2Model);	
		this.mTimedRankScene.registerTouchArea(this.mModelBack);
	}
	
	/**
	 * 初始化背景
	 */
	private void initBG(){
		final Sprite bg = new Sprite(0, 0, this.mBackGroundTextureRegion);
		this.mMainScene.getLayer(LAYER_BGROUND).addEntity(bg);
		this.mModelScene.getLayer(LAYER_BGROUND).addEntity(bg);
		this.mNormalRankScene.getLayer(LAYER_BGROUND).addEntity(bg);
		this.mTimedRankScene.getLayer(LAYER_BGROUND).addEntity(bg);
	}
	
	/**
	 * 初始化动态背景
	 */
	private void initDynamic(){
		final Sprite dynamicBG = new Sprite(0, 5, this.mDynamicBgroundTextureRegion);
		dynamicBG.addShapeModifier(new LoopShapeModifier(new SequenceShapeModifier
				(new RotationModifier(1.5f, -3, 4),new RotationModifier(1.5f, 4, -3))));
		dynamicBG.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		dynamicBG.setAlpha(0.8f);
		this.mMainScene.getLayer(LAYER_BGROUND).addEntity(dynamicBG);	
		this.mModelScene.getLayer(LAYER_BGROUND).addEntity(dynamicBG);	
		this.mNormalRankScene.getLayer(LAYER_BGROUND).addEntity(dynamicBG);	
		this.mTimedRankScene.getLayer(LAYER_BGROUND).addEntity(dynamicBG);	
	}
	
	/**
	 * 初始化菜单选项
	 */
	private void initMenu(){
		final int centerX = (CAMERA_WIDTH - this.mMenuNewGameTextureRegion.getWidth()) / 2;
		//新游戏菜单
		this.mNewGame = new Sprite(centerX, 130, this.mMenuNewGameTextureRegion);//背景
		this.mMainScene.getLayer(LAYER_BGROUND).addEntity(this.mNewGame);
		this.mNewGameText = new Text(centerX +47, 136, this.mFont, "New Game");//文字
		this.mMainScene.getLayer(LAYER_MENU).addEntity(mNewGameText);		
		//排行榜菜单
		this.mRanking = new Sprite(centerX, 200, this.mMenuRankingTextureRegion);//背景
		this.mMainScene.getLayer(LAYER_BGROUND).addEntity(this.mRanking);
		this.mRankingText = new Text(centerX +70, 206, this.mFont, "Scores");//文字
		this.mMainScene.getLayer(LAYER_MENU).addEntity(mRankingText);		
		//退出菜单
		this.mQuit = new Sprite(centerX, 270, this.mMenuQuitTextureRegion);//背景
		this.mMainScene.getLayer(LAYER_BGROUND).addEntity(this.mQuit);
		this.mQuitText = new Text(centerX +82, 276, this.mFont, "Quit");//文字
		this.mMainScene.getLayer(LAYER_MENU).addEntity(mQuitText);
	}
	
	/**
	 * 初始化模式选项
	 */
	private void initModel(){
		final int centerX = (CAMERA_WIDTH - this.mMenuNewGameTextureRegion.getWidth()) / 2;
		//普通模式
		this.mNormalMode = new Sprite(centerX, 130, this.mMenuNewGameTextureRegion);//背景
		this.mModelScene.getLayer(LAYER_BGROUND).addEntity(this.mNormalMode);
		this.mNormalModeText = new Text(centerX +27, 137, this.mFont, "NORMAL mode");//文字
		this.mNormalModeText.setScale(0.8f, 0.9f);
		this.mModelScene.getLayer(LAYER_MENU).addEntity(mNormalModeText);		
		//时间模式
		this.mTimedMode = new Sprite(centerX, 200, this.mMenuRankingTextureRegion);//背景
		this.mModelScene.getLayer(LAYER_BGROUND).addEntity(this.mTimedMode);
		this.mTimedModeText = new Text(centerX +40, 207, this.mFont, "TIMED mode");//文字
		this.mTimedModeText.setScale(0.8f, 0.9f);
		this.mModelScene.getLayer(LAYER_MENU).addEntity(mTimedModeText);		
		//无限模式
		this.mInfiniteMode = new Sprite(centerX, 270, this.mMenuQuitTextureRegion);//背景
		this.mModelScene.getLayer(LAYER_BGROUND).addEntity(this.mInfiniteMode);
		this.mInfiniteModeText = new Text(centerX +28, 277, this.mFont, "INFINITE mode");//文字
		this.mInfiniteModeText.setScale(0.8f, 0.9f);
		this.mModelScene.getLayer(LAYER_MENU).addEntity(mInfiniteModeText);
		//回退按钮
		this.mBack = new Sprite(centerX, 340, this.mBackTextureRegion);//背景
		this.mModelScene.getLayer(LAYER_BGROUND).addEntity(this.mBack);
		final Text backText = new Text(centerX +110, 345, this.mFont, "Back");//文字
		backText.setScale(0.8f, 0.9f);
		this.mModelScene.getLayer(LAYER_MENU).addEntity(backText);	
		final Sprite backIMG = new Sprite(centerX+60, 348, this.mBackIMGTextureRegion);
		this.mModelScene.getLayer(LAYER_MENU).addEntity(backIMG);	
	}
	
	/**
	 * 初始化普通模式排行榜
	 */
	private void initNormalRank(){		
		final int centerX = (CAMERA_WIDTH - this.mMenuNewGameTextureRegion.getWidth()) / 2;
		//查看不同模式按钮
		this.mSelectModel = new Sprite(centerX, 20, this.mSelectModelTextureRegion);//背景
		this.mNormalRankScene.getLayer(LAYER_BGROUND).addEntity(this.mSelectModel);
		final Text selectModelText = new Text(centerX-7, 25, this.mFont, "TIMED mode Scores");//文字
		selectModelText.setScale(0.7f, 0.8f);
		this.mNormalRankScene.getLayer(LAYER_MENU).addEntity(selectModelText);
		//回退按钮
		this.mModelBack = new Sprite(centerX, 70, this.mModelBackTextureRegion);//背景
		this.mNormalRankScene.getLayer(LAYER_BGROUND).addEntity(this.mModelBack);
		final Text backText = new Text(centerX +110, 76, this.mFont, "Back");//文字
		backText.setScale(0.8f, 0.9f);
		this.mNormalRankScene.getLayer(LAYER_MENU).addEntity(backText);	
		final Sprite backIMG = new Sprite(centerX+60, 78, this.mBackIMGTextureRegion);
		this.mNormalRankScene.getLayer(LAYER_MENU).addEntity(backIMG);			
	}
	
	/**
	 * 初始化时间模式排行榜
	 */
	private void initTimedRank(){
		final int centerX = (CAMERA_WIDTH - this.mMenuNewGameTextureRegion.getWidth()) / 2;
		//查看不同模式按钮
		this.mSelect2Model = new Sprite(centerX, 20, this.mSelectModelTextureRegion);//背景
		this.mTimedRankScene.getLayer(LAYER_BGROUND).addEntity(this.mSelect2Model);
		final Text selectModelText = new Text(centerX-18, 25, this.mFont, "NORMAL mode Scores");//文字
		selectModelText.setScale(0.7f, 0.8f);
		this.mTimedRankScene.getLayer(LAYER_MENU).addEntity(selectModelText);
		//回退按钮
		this.mTimedRankScene.getLayer(LAYER_BGROUND).addEntity(this.mModelBack);
		final Text backText = new Text(centerX +110, 76, this.mFont, "Back");//文字
		backText.setScale(0.8f, 0.9f);
		this.mTimedRankScene.getLayer(LAYER_MENU).addEntity(backText);	
		final Sprite backIMG = new Sprite(centerX+60, 78, this.mBackIMGTextureRegion);
		this.mTimedRankScene.getLayer(LAYER_MENU).addEntity(backIMG);	
	}
		
	/**
	 * 初始化普通模式排行榜分数
	 */
	private void initNormalRanking(){
		Text[][] mRankingTextArr = new Text[10][3];
		final Sprite rankingPanel = new Sprite(10, 140, this.mRankingPanelTextureRegion);
		rankingPanel.setAlpha(0.74f);
		rankingPanel.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mNormalRankScene.getLayer(LAYER_BGROUND).addEntity(rankingPanel);	
		//排行
		int distance = 188;
        Cursor cursor = mSQLiteHelper.getListViewCursorByModel(0);
		if(cursor != null){
	        int i = 0;
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
	        	mRankingTextArr[i][0] = new Text(40, distance, this.mGreenFont, String.valueOf(cursor.getInt(cursor.getColumnIndex("_rank"))));
	        	mRankingTextArr[i][1] = new Text(90, distance, this.mGreenFont, cursor.getString(cursor.getColumnIndex("_name")));
	        	mRankingTextArr[i][1].setScale(1, 0.7f);
	        	mRankingTextArr[i][1].setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	        	mRankingTextArr[i][2] = new Text(220, distance, this.mGreenFont, String.valueOf(cursor.getInt(cursor.getColumnIndex("_score"))));
	        	for(int j = 0; j < 3; j++){
	        		this.mNormalRankScene.getLayer(LAYER_MENU).addEntity(mRankingTextArr[i][j]);
	        	}
	        	distance += 25;
	        	if(i < 10){
	        		i++;
	        	}else {
					return;
				}
			}
		}       
	}
	
	/**
	 * 初始化时间模式排行榜分数
	 */
	private void initTimedRanking(){
		Text[][] mRankingTextArr = new Text[10][3];
		final Sprite rankingPanel = new Sprite(10, 140, this.mRankingPanelTextureRegion);
		rankingPanel.setAlpha(0.74f);
		rankingPanel.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mTimedRankScene.getLayer(LAYER_BGROUND).addEntity(rankingPanel);	
		//排行
		int distance = 188;
        Cursor cursor = mSQLiteHelper.getListViewCursorByModel(1);
		if(cursor != null){
	        int i = 0;
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
	        	mRankingTextArr[i][0] = new Text(40, distance, this.mGreenFont, String.valueOf(cursor.getInt(cursor.getColumnIndex("_rank"))));
	        	mRankingTextArr[i][1] = new Text(90, distance, this.mGreenFont, cursor.getString(cursor.getColumnIndex("_name")));
	        	mRankingTextArr[i][1].setScale(1, 0.7f);
	        	mRankingTextArr[i][1].setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	        	mRankingTextArr[i][2] = new Text(220, distance, this.mGreenFont, String.valueOf(cursor.getInt(cursor.getColumnIndex("_score"))));
	        	for(int j = 0; j < 3; j++){
	        		this.mTimedRankScene.getLayer(LAYER_MENU).addEntity(mRankingTextArr[i][j]);
	        	}
	        	distance += 25;
	        	if(i < 10){
	        		i++;
	        	}else {
					return;
				}	        	
			}
		}       
	}
	
	/**
	 * 进入模式选择页面
	 */
	private void toSelectModel(){
		this.mMainScene.setChildScene(this.mModelScene, false, true, true);
	}
	
	/**
	 * 开始游戏
	 */
	private void startGame(final String model){
		Intent intent = new Intent();
		intent.putExtra("mode", model);
		intent.setClass(this.getApplicationContext(), Jewels.class);
		startActivity(intent);
		Menu.this.finish();
	}
	
	/**
	 * 进入普通排行榜
	 */
	private void toNormalRanking(){
		this.mMainScene.setChildScene(this.mNormalRankScene, false, true, true);
		initNormalRanking();
	}
	
	/**
	 * 进入时间模式排行榜
	 */
	private void toTimedRanking(){
		this.mMainScene.setChildScene(this.mTimedRankScene, false, true, true);
		initTimedRanking();
	}	
}
