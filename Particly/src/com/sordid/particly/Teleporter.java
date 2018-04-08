package com.sordid.particly;

import java.util.HashMap;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.Shape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Teleporter{
	protected final Vector2 mVector;
	protected boolean mCanTeleport = true;
	protected Scene mScene = null;
	protected ITimerCallback mTimerCallback = new ITimerCallback() {
        @Override
        public void onTimePassed(final TimerHandler pTimerHandler){
        	mCanTeleport = true;
            mScene.unregisterUpdateHandler(pTimerHandler);
        }      
    };
    protected TimerHandler mTimerHandler = new TimerHandler(2f, mTimerCallback);
	
	public Teleporter(final Body pBody) {
		mVector = new Vector2(pBody.getPosition().x, pBody.getPosition().y);
	}
	
	public Vector2 getVector() {
		return mVector;
	}
	
	public boolean canTeleport() {
		return mCanTeleport;
	}
	
	@SuppressWarnings("unchecked")
	public void teleport(final Scene pScene, String target, Shape pShape) {
		if(mCanTeleport) {
			final HashMap<String,String> userData = (HashMap<String, String>)pShape.getUserData();
			if(!userData.containsKey("teleport")) {
				userData.put("teleport", target);
				pShape.setUserData(userData);
				
				mCanTeleport = false;
				mScene = pScene;
				pScene.registerUpdateHandler(mTimerHandler);
			}
		}
	}
}