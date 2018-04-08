package com.sordid.particly;

import java.util.HashMap;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.modifier.ease.EaseSineInOut;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

public class Entities{
    // TMX objects
    public static final Object LEVEL_TYPE_WALL = "wall";
    public static final Object LEVEL_TYPE_BALL = "ball";
    public static final Object LEVEL_TYPE_GOAL = "goal";
    public static final Object LEVEL_TYPE_OBJECT = "object";
    public static final Object LEVEL_TYPE_WAYPOINT = "waypoint";
    public static final Object LEVEL_TYPE_TELEPORT = "teleport";
    public static final Object LEVEL_TYPE_LAUNCH = "launch";
    public static final Object LEVEL_TYPE_PATH = "path";
    
	private static Body body;
	private static HashMap<String,String> userData;
	private static LinkedSprite object;
	private static BodyType bodyType;
	private static String pathList;
	private static String[] paths;
	private static Path path;
	private static Vector2 vector;
	private static PhysicsConnector physicsConnector;
	
	public static void addEntity(final ParticlyActivity pParent, final Scene pScene, final int pX, final int pY,
			final int pWidth, final int pHeight, final String pType, final HashMap<String,String> pProperties, final TextureRegion pTextureRegion) {
    	if(pType.equals("")) {
    		return;
    	}
    	
    	body = null;
    	userData = new HashMap<String,String>();
    	if(pType.equals(Entities.LEVEL_TYPE_WALL)) {
    		Entities.addWall(pParent, pScene, pX, pY, pWidth, pHeight,pProperties);
    	} else if(pType.equals(Entities.LEVEL_TYPE_OBJECT)) {
    		Entities.addObject(pParent, pScene, pX, pY, pWidth, pHeight, pProperties, pTextureRegion);
    	} else if(pType.equals(Entities.LEVEL_TYPE_GOAL)) {
    		Entities.addGoal(pParent, pScene, pX, pY, pWidth, pHeight);
        } else if(pType.equals(Entities.LEVEL_TYPE_BALL)) {
        	Entities.addBall(pParent, pScene, pX, pY, pWidth, pHeight);
        } else if(pType.equals(Entities.LEVEL_TYPE_WAYPOINT)) {
        	Entities.addWaypoint(pParent, pScene, pX, pY, pWidth, pHeight);
        } else if(pType.equals(Entities.LEVEL_TYPE_TELEPORT)) {
			Entities.addTeleport(pParent, pScene, pX, pY, pWidth, pHeight, pProperties);
        } else if(pType.equals(Entities.LEVEL_TYPE_LAUNCH)) {
			Entities.addLaunch(pParent, pScene, pX, pY, pWidth, pHeight, pProperties);
        }else if(pType.equals(Entities.LEVEL_TYPE_PATH)) {
			Entities.addPath(pParent, pProperties.get("name"), pX, pY, pProperties);
        } else {
        	Slog.i("Physics", "Invalid TMX type: "+pType);
        	throw new IllegalArgumentException();
        }
    	if(body != null) {
    		userData.put("type", pType);
    		body.setUserData(userData);
    	}
    }

	// Add a wall
	private static void addWall(final ParticlyActivity pParent, final Scene pScene, final int pX, final int pY, final int pWidth, final int pHeight, final HashMap<String,String> properties) {
    	final Shape wall = new Rectangle(pX, pY, pWidth, pHeight);
    	wall.setVisible(false);
    	if(properties.containsKey("rotate")){
    		//wall.setVisible(true);
    		wall.setRotation(Float.parseFloat(properties.get("rotate")));
    	}
    	body = PhysicsFactory.createBoxBody(pParent.mPhysicsWorld, wall, BodyType.StaticBody, ParticlyActivity.STATIC_FIXTURE_DEF);
    	pScene.getFirstChild().attachChild(wall);
	}
	
	// Add an object
	private static void addObject(final ParticlyActivity pParent, final Scene pScene, final int pX, final int pY, final int pWidth, final int pHeight, final HashMap<String,String> properties, final TextureRegion pTextureRegion) {
		//textureRegion = ParticlyActivity.sTextures.get(properties.get("group"));
		object = new LinkedSprite(pX, pY-pTextureRegion.getHeight(), pTextureRegion);

		bodyType = BodyType.DynamicBody;
		boolean isConnectedBody = true;
		Vector2 firstPoint = null;
		final MouseJointDef mouseJointDef = new MouseJointDef();
		final Body mAnchorBody = pParent.mPhysicsWorld.createBody(new BodyDef());
		
		if(properties.containsKey("path")) {
			//bodyType = BodyType.KinematicBody;			
			pathList = properties.get("path");
			paths = pathList.split(",");
			path = new Path(paths.length);
			for(String pathName : paths) {
				vector = pParent.mPaths.get(pathName);
				if(firstPoint == null) {
					final Vector2 tvector = Vector2Pool.obtain(
		        		(pX+(pWidth/2))/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
		        		(pY+(pHeight/2))/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT
		        	);
					mAnchorBody.setTransform(tvector, 0);
					firstPoint = tvector;
				}
				path = path.to(vector.x, vector.y);
			}
			
			int duration = 20;
			if(properties.containsKey("duration")) {
				duration = Integer.parseInt(properties.get("duration"));
			}
			
			if(properties.containsKey("ease")) {
				if(properties.get("ease").equals("sineinout")) {
					object.registerEntityModifier(new LoopEntityModifier(new PathModifier(duration, path, null, null, EaseSineInOut.getInstance())));
				}
			} else {
				object.registerEntityModifier(new LoopEntityModifier(new PathModifier(duration, path)));
			}
			
			isConnectedBody = false;
		}

		object.registerUpdateHandler(new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(object.getX() > pParent.mCamera.getBoundsWidth()+50 || object.getX() < -100 || object.getY() > pParent.mCamera.getBoundsHeight()+50) {
					physicsConnector = pParent.mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(object);
					if(physicsConnector != null) {
						physicsConnector.getBody().setActive(false);
						object.setVisible(false);
						pParent.mPhysicsWorld.getPhysicsConnectorManager().remove(physicsConnector);
						object.unregisterUpdateHandler(this);
					}
				}
			}
			@Override public void reset() {}
		});
		body = PhysicsFactory.createBoxBody(pParent.mPhysicsWorld, object, bodyType, ParticlyActivity.OBJECT_LEVEL_FIXTURE_DEF);
		if(!isConnectedBody) {
			object.setBody(body);
			mouseJointDef.bodyA = mAnchorBody;
			mouseJointDef.bodyB = body;
			mouseJointDef.dampingRatio = 0.95f;
			mouseJointDef.frequencyHz = 60;
			mouseJointDef.maxForce = (200.0f * body.getMass());
			mouseJointDef.collideConnected = true;
			
			mouseJointDef.target.set(firstPoint);
			Vector2Pool.recycle(firstPoint);
			
			object.setJoint((MouseJoint)pParent.mPhysicsWorld.createJoint(mouseJointDef));
		}
		
    	pScene.getFirstChild().attachChild(object);
    	pParent.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(object, body, true, true));
	}
	
	// Add a goal
	private static void addGoal(final ParticlyActivity pParent, final Scene pScene, final int pX, final int pY, final int pWidth, final int pHeight) {
    	final Shape wall = new Rectangle(pX, pY, pWidth, pHeight);
    	body = PhysicsFactory.createBoxBody(pParent.mPhysicsWorld, wall, BodyType.StaticBody, ParticlyActivity.STATIC_GOAL_FIXTURE_DEF);
    	wall.setVisible(false);
    	pScene.getFirstChild().attachChild(wall);
    	Emitters.addGoal(pX+(pWidth/2)-10, pY+(pHeight/2)-10, ParticlyActivity.sTextures.get("particle"), pScene);
	}
	
	// Add the main ball
	private static void addBall(final ParticlyActivity pParent, final Scene pScene, final int pX, final int pY, final int pWidth, final int pHeight) {
		pParent.mBall = new AnimatedSprite(pX, pY, ParticlyActivity.sTiledTextures.get("ball"));
    	pParent.mBallBody = PhysicsFactory.createCircleBody(pParent.mPhysicsWorld, pParent.mBall, BodyType.DynamicBody, ParticlyActivity.OBJECT_FIXTURE_DEF);
    	pParent.mBall.registerUpdateHandler(pParent.ballUpdateHandler);
		pParent.mCamera.setChaseEntity(pParent.mBall);
		pScene.getLastChild().attachChild(pParent.mBall);
		pParent.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(pParent.mBall, pParent.mBallBody, true, true));
		pParent.mBall.animate(ParticlyActivity.ANIMATION_DEFAULT, 0, 2, true);
		pParent.mBall.setUserData(userData);
		userData.put("type", Entities.LEVEL_TYPE_BALL.toString());
		pParent.mBallBody.setUserData(userData);
		pParent.mBallBody.setAngularDamping(0.5f);
	}
	
	// Add a waypoint
	private static void addWaypoint(final ParticlyActivity pParent, final Scene pScene, final int pX, final int pY, final int pWidth, final int pHeight) {
		if(!pParent.mWayPoints.containsKey(pParent.mParticleSystems.size())) {
    		pParent.mWayPoints.put(pParent.mParticleSystems.size(), false);
    	}
    	final boolean wayPointNotReached = !pParent.mWayPoints.get(pParent.mParticleSystems.size());

    	pParent.mParticleSystems.add(Emitters.addWaypoint(pX-10, pY-10, ParticlyActivity.sTextures.get("particleWaypoint"), pScene, wayPointNotReached));
		if(wayPointNotReached) {
			final Shape obj = new Rectangle(pX, pY, pWidth+10, pHeight+10);
        	body = PhysicsFactory.createBoxBody(pParent.mPhysicsWorld, obj, BodyType.StaticBody, ParticlyActivity.STATIC_GOAL_FIXTURE_DEF);
        	userData.put("index", ""+(pParent.mParticleSystems.size()-1));
        	obj.setVisible(false);
        	pScene.getFirstChild().attachChild(obj);
    	}
	}
	
	// Add a launch
	private static void addLaunch(final ParticlyActivity pParent, final Scene pScene, final int pX, final int pY, final int pWidth, final int pHeight, final HashMap<String,String> properties) {
    	Emitters.addLaunch(pX, pY, ParticlyActivity.sTextures.get("particleLauncher"), pScene, properties.get("angle"), properties.get("force"));
		final Shape obj = new Rectangle(pX, pY, pWidth+10, pHeight+10);
       	body = PhysicsFactory.createBoxBody(pParent.mPhysicsWorld, obj, BodyType.StaticBody, ParticlyActivity.STATIC_GOAL_FIXTURE_DEF);
       	userData.put("angle", properties.get("angle"));
       	userData.put("force", properties.get("force"));
       	obj.setVisible(false);
       	pScene.getFirstChild().attachChild(obj);
	}	
	
	// Add a teleporter
	private static void addTeleport(final ParticlyActivity pParent, final Scene pScene, final int pX, final int pY, final int pWidth, final int pHeight, final HashMap<String,String> properties) {
		if(properties.containsKey("target")) {
			Emitters.addTeleport(pX-10, pY-10, ParticlyActivity.sTextures.get("particle"), pScene);
			userData.put("target", properties.get("target"));
		}
		final Shape obj = new Rectangle(pX-10, pY-10, pWidth+10, pHeight+10);
		body = PhysicsFactory.createBoxBody(pParent.mPhysicsWorld, obj, BodyType.StaticBody, ParticlyActivity.STATIC_GOAL_FIXTURE_DEF);
		pParent.mTeleports.put(properties.get("name"), new Teleporter(body));
    	obj.setVisible(false);
    	pScene.getFirstChild().attachChild(obj);   
	}
	
	// Add a path
	private static void addPath(final ParticlyActivity pParent, String name, final int pX, final int pY, final HashMap<String,String> properties) {
		pParent.mPaths.put(name, new Vector2(pX, pY));
	}	
}