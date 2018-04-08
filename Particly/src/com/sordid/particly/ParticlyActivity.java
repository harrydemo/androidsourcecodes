package com.sordid.particly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXObject;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXObjectGroup;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.AnimatedSpriteMenuItem;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sordid.particly.ParallaxBackground2d.ParallaxBackground2dEntity;

public class ParticlyActivity extends BaseGameActivity implements IOnSceneTouchListener,IOnMenuItemClickListener {
	public static final boolean DEBUG = false;
	public static final int CAMERA_WIDTH = 720;
	public static final int CAMERA_HEIGHT = 480;
	private static final int DIALOG_INTRO_1 = 0;
	private static final int DIALOG_INTRO_2 = 1;
    private static final float SCALE_FACTOR = 0.1f;
    private static final int MENU_WIN = 0;
    private static final int MENU_NEXT = 1;    
    private static final int MENU_RESET = 2;
    private static final int MENU_SELECT = 3;
    private static final int MENU_SOUND = 4;
    
    public static final int STATE_STOPPED = 1;
    public static final int STATE_RUNNING = 2;
    public static final int STATE_OVER = 4;
    public static final int STATE_MENU = 8;
    
    // Item list
    private static final short WALL = 1;
    private static final short OBJECT = 2;
    private static final short MASK = WALL+OBJECT;
    
    // Fixtures
    public static final FixtureDef STATIC_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.0f, 0.5f, false, WALL, MASK, (short)0);
    public static final FixtureDef STATIC_GOAL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0f, 10f, true, WALL, MASK, (short)0);
    public static final FixtureDef OBJECT_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.7f, 0.4f, false, OBJECT, MASK, (short)0);
    public static final FixtureDef OBJECT_LEVEL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0.2f, 0.1f, 0.4f, false, OBJECT, MASK, (short)0);
    
    // Variables for performance
    private Line mLine;
    
    // General variables
    private float mLastVelocity = 0.0f;
    private ContactListener mContactListener;
    
    public static HashMap<String,TextureRegion> sTextures;
    public static HashMap<String,TiledTextureRegion> sTiledTextures;
    public static ArrayList<Texture> sTextureHolders;
    public static Sound[] sHitVoiceSounds;
    public static Sound[] sHitSounds;
    public static Sound[] sWinSounds;
    public static Sound[] sLoseSounds;    
    public ArrayList<ParticleSystem> mParticleSystems;
    public HashMap<String,Teleporter> mTeleports;
    private CustomMenuScene mMenuScene;
    private Texture mTexture;
    private TextureRegion mParallaxLayerBack;
	private TextureRegion mParallaxLayerMid;
	private TextureRegion mParallaxLayerFront;  
    public BoundCamera mCamera;
    public AnimatedSprite mBall;
    public Random mRandom = new Random();
    private Sprite mArrow;
    public Body mBallBody;
    private ParallaxBackground2d mBackground;
    private TMXTiledMap mTMXTiledMap;
    public Scene mScene;
    private int mLevel = 0;
    private int mLevels = 0;
    private int mChapter = 0;
    public boolean mSound = true;
    public HashMap<String,Vector2> mPaths;
    
    public MaxStepPhysicsWorld mPhysicsWorld;
   
    private int mState = STATE_STOPPED;
    private boolean mDragReady = false;
    private boolean mCanSeeMap = true;
    private float mLastScrollX = 0;
    private float mLastScrollY = 0;
    private int mAttempts = 0;
    private long mTime = 0;
    public final HashMap<Integer,Boolean>mWayPoints = new HashMap<Integer,Boolean>();
    private Vector2 mGravityVector = new Vector2(0, SensorManager.GRAVITY_EARTH);
    private boolean mPaused = false;
    
    public static final long[] ANIMATION_DEFAULT = new long[]{3000,150,0};
    public static final long[] ANIMATION_CONTACT = new long[]{0,0,1000};
    
    public static Stats sStats;
    public Handler data = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			new StatsTask().execute(ParticlyActivity.sStats, getApplicationContext());
		}
	};
	public IUpdateHandler ballUpdateHandler = new IUpdateHandler(){
		@SuppressWarnings("unchecked")
		@Override
		public void onUpdate(float pSecondsElapsed) {
			mCamera.onUpdate(1.0f);
			mBackground.setParallaxValue(mCamera.getCenterX(), mCamera.getCenterY());
			final Body connected = mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(mBall);
			if(connected != null) {
				final Vector2 v = connected.getLinearVelocity();
				if(mLastVelocity <= 0.5f && Math.abs(v.y) <= 0.5f && v.x != 0.0f) {
					connected.setLinearDamping(2.0f);
				} else {
					connected.setLinearDamping(0.0f);
				}
				mLastVelocity = Math.abs(v.y);
				if(mLine != null) {
					mLine.setPosition(mBall.getX()+(mBall.getWidth()/2), mBall.getY()+(mBall.getHeight()/2), mLine.getX2(), mLine.getY2());
				}
				
				if(mBall.getX() > mCamera.getBoundsWidth()+50 || mBall.getX() < -100 || mBall.getY() > mCamera.getBoundsHeight()+50) {
					gameOver();
				} else if(mBall.getY() < -100) {
					mArrow.setVisible(true);
					mArrow.setPosition(mBall.getX(), -100);
				} else {
					mArrow.setVisible(false);
				}
			}
			final HashMap<String,String> userData = (HashMap<String, String>) mBall.getUserData();
			if(userData.containsKey("teleport")) {
				final Teleporter teleporter = mTeleports.get(userData.get("teleport"));
				Body body = mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(mBall);
				body.setTransform(teleporter.getVector(), 0.0f);
				userData.remove("teleport");
				mBall.setUserData(userData);
			}
		}
		@Override public void reset() {}
	};
    
    // ===========================================================
    // Core Methods
    // ===========================================================    
    
    @Override
    protected void onCreate(final Bundle pSavedInstanceState) {
    	super.onCreate(pSavedInstanceState);

    	Bundle extras = getIntent().getExtras();
		mLevel = pSavedInstanceState != null ? pSavedInstanceState.getInt("level"):0;	
		if(mLevel == 0) {
			mLevel = extras != null ? extras.getInt("level") : 1;
		}
		mChapter = pSavedInstanceState != null ? pSavedInstanceState.getInt("chapter"):0;	
		if(mChapter == 0) {
			mChapter = extras != null ? extras.getInt("chapter") : 1;
		}
		mLevels = pSavedInstanceState != null ? pSavedInstanceState.getInt("levels"):0;	
		if(mLevels == 0) {
			mLevels = extras != null ? extras.getInt("levels") : 0;
		}
		reset();
    }
    
    @Override
	public void onPauseGame() {
    	super.onPauseGame();
    	mPaused = true;
    }   
    
    @Override
	public void onResumeGame() { 	
    	if(mPaused) {
    		this.finish();
    		Intent nintent = new Intent(getBaseContext(), LoadingScreen.class);
            startActivity(nintent);
    	}  
    	super.onResumeGame();    	
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	Bundle extras = intent.getExtras();
		mLevel = extras != null ? extras.getInt("level") : 1;
		mChapter = extras != null ? extras.getInt("chapter") : 1;
		mLevels = extras != null ? extras.getInt("levels") : 0;
    	reset();
    }    
        
    @Override
    public Engine onLoadEngine() {
        this.mCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera).setNeedsSound(true);
        engineOptions.getTouchOptions().setRunOnUpdateThread(true);
        return new Engine(engineOptions);
    }

    @Override
    public void onLoadResources() {
    	TextureRegionFactory.setAssetBasePath("gfx/");

    	if(sanityCheck())
    	{
	    	for(Texture t: ParticlyActivity.sTextureHolders) {
	    		this.mEngine.getTextureManager().loadTexture(t);
	    	}
	
	        // Background
			this.mTexture = new Texture(1024, 2048, TextureOptions.DEFAULT);
			this.mParallaxLayerFront = TextureRegionFactory.createFromAsset(this.mTexture, this, "Chapter"+mChapter+"/front.png", 0, 0);
			this.mParallaxLayerBack = TextureRegionFactory.createFromAsset(this.mTexture, this, "Chapter"+mChapter+"/back.png", 0, 480);
			this.mParallaxLayerMid = TextureRegionFactory.createFromAsset(this.mTexture, this, "Chapter"+mChapter+"/mid.png", 0, 960);
			this.mEngine.getTextureManager().loadTexture(mTexture);
    	}
    }

    @Override
    public Scene onLoadScene() {
    	if(!sanityCheck()) {
    		return null;
    	}
    	else
    	{
    		mPaths = new HashMap<String,Vector2>();
	    	mParticleSystems = new ArrayList<ParticleSystem>();
	    	mTeleports = new HashMap<String,Teleporter>();
	    	mDragReady = false;
	    	mState = STATE_RUNNING;
	        this.createMenuScene();
	
	        this.mPhysicsWorld = new MaxStepPhysicsWorld(16,new Vector2(0, SensorManager.GRAVITY_EARTH), false);
	        mScene = new Scene(3);
	    	
			mBackground = new ParallaxBackground2d(0, 0, 0);
			mBackground.addParallaxEntity(new ParallaxBackground2dEntity(-0.1f, -0.0f, new Sprite(0, 0, this.mParallaxLayerBack),0.0f));
			mBackground.addParallaxEntity(new ParallaxBackground2dEntity(-0.5f, -0.1f, new Sprite(0, 30, this.mParallaxLayerMid),0.0f));
			mBackground.addParallaxEntity(new ParallaxBackground2dEntity(-1.0f, -1.0f, new Sprite(0, 240, this.mParallaxLayerFront),0.0f));
			mScene.setBackground(mBackground);
			
			// Load the TMX level
			try {
				final TMXLoader tmxLoader = new TMXLoader(this, this.mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA);
				this.mTMXTiledMap = tmxLoader.loadFromAsset(this, "tmx/Chapter"+mChapter+"/Level"+mLevel+".tmx");
			} catch (final TMXLoadException tmxle) {
				Slog.i("TMX File Error", tmxle.toString());
			}		
			
			// Get the tile layers and add it to game
			float width = 0, height = 0;
			for(TMXLayer tmxLayer : this.mTMXTiledMap.getTMXLayers()) {
				if(width == 0 && height == 0) {
					width = tmxLayer.getWidth();
					height = tmxLayer.getHeight();
				}
				mScene.getChild(1).attachChild(tmxLayer);
			}
			
			sStats.getLevel(mLevel,mWayPoints);
			
			// Add all TMX objects to map
			final ArrayList<TMXObjectGroup> groups = mTMXTiledMap.getTMXObjectGroups();
			ArrayList<TMXObject> objects;
			TextureRegion textureRegion = null;
			for(final TMXObjectGroup group: groups) {
				objects = group.getTMXObjects();
				for(final TMXObject object : objects) {
					String type = "";
					if(group.getTMXObjectGroupProperties().size() > 0) {
						type = group.getTMXObjectGroupProperties().get(0).getValue();
					}
					
					HashMap<String,String> properties = new HashMap<String,String>();
					int size = object.getTMXObjectProperties().size();
					for(int i=0;i<size;i++) {
						properties.put(object.getTMXObjectProperties().get(i).getName(), object.getTMXObjectProperties().get(i).getValue());
					}
					
					if(properties.containsKey("type")) {
						type = properties.get("type");
					}
					
					if(object.getGID() != 0) {
						textureRegion = mTMXTiledMap.getTextureRegionFromGlobalTileID(object.getGID());
					}
					
					Entities.addEntity(
							this,
							mScene,
							object.getX(),
							object.getY(),
							object.getWidth(),
							object.getHeight(),
							type,
							properties,
							textureRegion
					);	
				}
			}	
			
			mArrow = new Sprite(0, 0, ParticlyActivity.sTextures.get("arrow"));
			mArrow.setVisible(false);
			mScene.getLastChild().attachChild(mArrow);
	        	    
		    // Keep camera associated to TMX Layer
	        this.mCamera.setBounds(0, width, -100, height);
	        this.mCamera.setBoundsEnabled(true);
	        	    
	        // See if the golf ball hits any key objects
	        mContactListener = new WorldContact(ParticlyActivity.this);
	        this.mPhysicsWorld.setContactListener(mContactListener);
	        
	        mScene.setOnSceneTouchListener(this);
	    	mScene.registerUpdateHandler(this.mPhysicsWorld);
	    	
	    	mTime = System.currentTimeMillis();
	    	
	    	System.gc();
	    	
	        return mScene;
    	}
    }

    @Override
    public void onLoadComplete() {
    	SharedPreferences settings = getSharedPreferences(Stats.PREFS_NAME, 0);
		boolean viewed = settings.getBoolean("viewedIntro",false);
		if(!viewed){
			showDialog(DIALOG_INTRO_1);
		}
		System.gc();
    }
    
    protected Dialog onCreateDialog(int id) {
    	SharedPreferences settings = getSharedPreferences(Stats.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("viewedIntro", true);
		editor.commit();
		
        Dialog dialog;
        AlertDialog.Builder builder;
        switch(id) {
        case DIALOG_INTRO_1:
        	builder = new AlertDialog.Builder(this);
        	builder.setMessage("Welcome to Particly!\n\nThe goal of the game is to get the little dude to the blue exit. Along the way you may see collectible money spawns, green teleports, wood beams to knock down and launchers. Use these to your advantage and see if you can get him to the exit in 1 move!")
        	       .setCancelable(false)
        	       .setPositiveButton("Skip", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   dialog.cancel();
        	           }
        	       })
        	       .setNegativeButton("Next", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	                dialog.cancel();
        	                showDialog(DIALOG_INTRO_2);	
        	           }
        	       });
        	dialog = builder.create();
            break;
        case DIALOG_INTRO_2:
        	builder = new AlertDialog.Builder(this);
        	builder.setMessage("To move him, press down on his body and drag your finger in the direction you want him to move. The longer you drag, the faster he will go, so try to adjust it so you get a nice, clean movement. You can move him again at any point in time by doing the same thing - that is, if you can catch him!")
        	       .setCancelable(false)
        	       .setNegativeButton("Play!", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	                dialog.cancel();
        	           }
        	       });
        	dialog = builder.create();
            break;
        default:
            dialog = null;
        }
        return dialog;
    }    
    
    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        if(this.mPhysicsWorld != null
        		&& (mState & STATE_RUNNING) == STATE_RUNNING
        		&& (mState & STATE_MENU) != STATE_MENU
        ) {
        	if(mDragReady) {
	        	if(pSceneTouchEvent.isActionMove()) {
        			mLine.setPosition(mLine.getInitialX(), mLine.getInitialY(), pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
	            } else {
	            	this.mCamera.setChaseEntity(mBall);
	                this.explode(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), true);
	                mPhysicsWorld.setGravity(mGravityVector);
	                mCanSeeMap = false;
                	pScene.getLastChild().detachChild(mLine);
	                mDragReady = false;
	            }
	        	return true;
        	} else {
        		final float x = pSceneTouchEvent.getX();
        		final float y = pSceneTouchEvent.getY();
        		if(pSceneTouchEvent.isActionDown()
        			&& x >= mBall.getX()-30 && x <= mBall.getX()+mBall.getWidth()+30
       				&& y >= mBall.getY()-30 && y <= mBall.getY()+mBall.getHeight()+30) {
        			mDragReady = true;
	        		mLine.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
        			pScene.getLastChild().attachChild(mLine);
        			return false;
        		} else if(mCanSeeMap) {
        			this.mCamera.setChaseEntity(null);
	        		if(pSceneTouchEvent.isActionDown()) {
	        			mLastScrollX = x;
		        		mLastScrollY = y;
	        		} else if(pSceneTouchEvent.isActionMove()) {
	        			mCamera.offsetCenter((mLastScrollX-x)*0.7f, (mLastScrollY-y)*0.7f);
	        			mLastScrollX -= (mLastScrollX-x)*0.5f;
		        		mLastScrollY -= (mLastScrollY-y)*0.5f;
	        		}
        		}
        	}
        }
        return false;
    }
    
    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
        if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN && (mState & STATE_RUNNING) == STATE_RUNNING) {
       		showMenu(false);
			return true;
        } else {
        	return super.onKeyDown(pKeyCode, pEvent);
        }
    }
    
    @Override
    public void onBackPressed() {
    	Intent intent = new Intent(getBaseContext(), LevelSelect.class);
    	intent.putExtra("chapter",mChapter);
		intent.putExtra("levels",mLevels);
		startActivity(intent);
		this.finish();
    }
    
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
        switch(pMenuItem.getID()) {
	        case MENU_RESET:			
				// Remove the menu and reset the game
				mScene.clearChildScene();
				reset();
	            return true;
	        case MENU_NEXT:
	        	if(((IMenuItem)this.mMenuScene.mItems.get("menuNext")).isVisible()){
					// Remove the menu and reset the game
		        	int currentLevel = mLevel+1;
		        	if(currentLevel > mLevels) {
		        		Intent intent = new Intent(getBaseContext(), LevelSelect.class);
		        		intent.putExtra("chapter",mChapter);
	   					intent.putExtra("levels",mLevels);
	   					startActivity(intent);
		        	} else {
						mLevel = currentLevel;
						reset();
		        	}
		        	return true;
	        	}
	        	else return false;
	        case MENU_SELECT:			
	        	Intent intent = new Intent(getBaseContext(), LevelSelect.class);
	        	intent.putExtra("chapter",mChapter);
				intent.putExtra("levels",mLevels);
				startActivity(intent);
	            return true;
	        case MENU_SOUND:			
	        	SharedPreferences settings = getSharedPreferences(Stats.PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				int index = ((AnimatedSpriteMenuItem)this.mMenuScene.mItems.get("menuSound")).getCurrentTileIndex();
				editor.putBoolean("sound", index != 0);
				editor.commit();
				((AnimatedSpriteMenuItem)this.mMenuScene.mItems.get("menuSound")).setCurrentTileIndex(index == 0 ? 1 : 0);
                mSound = index == 0 ? true : false;
	            return false;
	        default:
	        	return false;
        }
	}       

    // ===========================================================
    // Other Methods
    // ===========================================================

    // Move the ball
    public void explode(final float pX, final float pY, final boolean pUpdate) {
    	final Vector2 vector = Vector2Pool.obtain(
    		(pX-mBall.getX()-(mBall.getWidth()/2))*SCALE_FACTOR,
    		(pY-mBall.getY()-(mBall.getHeight()/2))*SCALE_FACTOR
    	);
	    final Body b = this.mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(mBall);
		b.setLinearVelocity(vector);
		Vector2Pool.recycle(vector);
		
		if(pUpdate) {
			mAttempts++;
		}
    }
    
    // Resets the level
    private void reset() {
    	mPaused = false;
    	mAttempts = 0;
    	mTime = 0;
    	mWayPoints.clear();
        mLastVelocity = 0.0f;
        mCanSeeMap = true;
        mDragReady = false;
        mLine = new Line(1,1,1,1);
		mLine.setAlpha(0.7f);
		mLine.setLineWidth(5.0f);
		mPaths = new HashMap<String,Vector2>();
        
		if(mPhysicsWorld != null) {
	        mEngine.unregisterUpdateHandler(mPhysicsWorld);
	        mEngine.getScene().unregisterUpdateHandler(mPhysicsWorld);
	        
	        for(int i = 0; i < mScene.getChildCount(); ++i){
	        	mScene.getChild(i).detachChildren();
	        	mScene.getChild(i).clearEntityModifiers();
	        	mScene.getChild(i).clearUpdateHandlers();
	        }
	        mScene.reset();
	        mScene.detachChildren();
	        mBall.clearUpdateHandlers();
	        mScene.clearUpdateHandlers();
	        mScene.clearTouchAreas();       
	       
	        // Destroy physics world
	        mPhysicsWorld.clearPhysicsConnectors();
	        mPhysicsWorld.dispose();
	        mPhysicsWorld = null;
	
	        onLoadScene();
	       
	        mEngine.setScene(null);
	        mEngine.setScene(mScene);
		}
    }    
    
    // Create the menu
    protected void createMenuScene() {
    	this.mMenuScene = new CustomMenuScene(this.mCamera);
        
        final AnimatedSpriteMenuItem winMenuItem = new AnimatedSpriteMenuItem(MENU_WIN, ParticlyActivity.sTiledTextures.get("menuWin"));
        winMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        winMenuItem.setCurrentTileIndex(0); 
        
        final SpriteMenuItem nextMenuItem = new SpriteMenuItem(MENU_NEXT, ParticlyActivity.sTextures.get("menuNext"));
        nextMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        final SpriteMenuItem resetMenuItem = new SpriteMenuItem(MENU_RESET, ParticlyActivity.sTextures.get("menuReset"));
        resetMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        final SpriteMenuItem selectMenuItem = new SpriteMenuItem(MENU_SELECT, ParticlyActivity.sTextures.get("menuSelect"));
        selectMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        final AnimatedSpriteMenuItem soundMenuItem = new AnimatedSpriteMenuItem(MENU_SOUND, ParticlyActivity.sTiledTextures.get("menuSound"));
		soundMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);            
        SharedPreferences settings = getSharedPreferences(Stats.PREFS_NAME, 0);
		mSound = settings.getBoolean("sound",true);
		soundMenuItem.setCurrentTileIndex(mSound?0:1);  
        
        //Texture mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
       // Font mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.BLACK);
        this.mMenuScene
        	.add("menuWin", winMenuItem)
        	.br()
        	/*.container()
        	/	.container()
        			.add(new Text(30, 30, mFont, "Total time:"))
        			.br()
        			.add(new Text(30, 90, mFont, "Attempts:"))
        			.br()
        			.add(new Text(30, 120, mFont, "Points:"))
        			.br()
        		.end()
        		.container()*/
        			.add("star1", new Sprite(200, 30, ParticlyActivity.sTextures.get("largeStarGlow")))
        			.add("star2", new Sprite(270, 30, ParticlyActivity.sTextures.get("largeStarGlow")))
        			.add("star3", new Sprite(340, 30, ParticlyActivity.sTextures.get("largeStarGlow")))
        	//	.end()
        	//.end()
        	.br()
        	.add("menuNext", nextMenuItem)
        	.add("menuReset", resetMenuItem)
        	.br()
        	.add("menuSound", soundMenuItem)        	
        	.add("menuSelect", selectMenuItem)
        	.br();
        
        mMenuScene.setMenuAnimator(new CustomMenuAnimator(5.0f));

        this.mMenuScene.buildAnimations();
        this.mMenuScene.setBackgroundEnabled(false);
        this.mMenuScene.setOnMenuItemClickListener(this);
    }    

    // Round over
    private void gameOver() {
    	if(mSound) {
	    	int index = mRandom.nextInt(ParticlyActivity.sLoseSounds.length-1);
	    	ParticlyActivity.sLoseSounds[index].play();
    	}
    	mState = STATE_OVER;
    	showMenu(false);
    }
    
    // Round was won
    public void roundWon() {
    	mTime = System.currentTimeMillis()-mTime;
    	mState = STATE_OVER;
    	
    	if(mSound) {
    		int index = mRandom.nextInt(ParticlyActivity.sWinSounds.length-1);
    		ParticlyActivity.sWinSounds[index].play();
    	}
    	
    	// Remove the menu and reset the game
    	Integer currentLevel = mLevel;
    	ParticlyActivity.sStats.saveLevel(currentLevel, mAttempts, mTime, mWayPoints);
    	data.sendEmptyMessage(1);
    	final boolean newMax = ParticlyActivity.sStats.setMaxLevel(++currentLevel);
    	
    	// Dump the current max level in the appropriate bucket
    	if(newMax) {
			SharedPreferences settings = getSharedPreferences(Stats.PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("currentLevel"+sStats.mFileExtra, currentLevel);
			editor.commit();
    	}
    	
    	mScene.clearUpdateHandlers();
    }
    
    // Show the menu
    public void showMenu(boolean showNext) {
    	if(mScene.hasChildScene()) {
    		if((mState & STATE_RUNNING) == STATE_RUNNING){
				// Remove the menu and reset it.
				this.mMenuScene.back();
				mState &= ~STATE_MENU;
    		}
		} else {
			if(mAttempts > 1 || !showNext) {
				this.mMenuScene.mItems.get("star3").setVisible(false);
			} else {
				this.mMenuScene.mItems.get("star3").setVisible(true);
			}
			if(mAttempts <= 2 && showNext) {
				this.mMenuScene.mItems.get("star2").setVisible(true);
			} else {
				this.mMenuScene.mItems.get("star2").setVisible(false);
			}
			if(!showNext) {
				this.mMenuScene.mItems.get("star1").setVisible(false);
			} else {
				this.mMenuScene.mItems.get("star1").setVisible(true);
			}
			// Attach the menu.
			((AnimatedSpriteMenuItem)this.mMenuScene.mItems.get("menuWin")).setCurrentTileIndex(showNext ? 0 : 1);
			if(sStats.getMaxLevel() > mLevel) {
				showNext = true;
			}			
			((IMenuItem)this.mMenuScene.mItems.get("menuNext")).setVisible(showNext);
			this.mMenuScene.buildAnimations();
			mScene.setChildScene(this.mMenuScene, false, true, true);
			mState |= STATE_MENU;
		}
    }
    
    // Remove a waypoint particle emitter
    public void removeWaypoint(int index) {
    	mWayPoints.put(index, true);
    	final ParticleSystem ps = mParticleSystems.get(index);
    	ps.setParticlesSpawnEnabled(false);
    	ps.detachChildren();
    	mScene.detachChild(ps);
    }
    
    // Check to see if we are in an invalid state
    public boolean sanityCheck()
    { 
    	if(sStats == null || ParticlyActivity.sTextures == null || ParticlyActivity.sTiledTextures == null || ParticlyActivity.sTextureHolders == null || ParticlyActivity.sHitVoiceSounds == null || ParticlyActivity.sHitSounds == null || ParticlyActivity.sWinSounds == null || ParticlyActivity.sLoseSounds == null) {
    		this.finish();
    		Intent nintent = new Intent(getBaseContext(), LoadingScreen.class);
            startActivity(nintent);
            return false;
    	}
    	return true;
    }
}
