package com.sordid.particly;

import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;

public class MaxStepPhysicsWorld extends PhysicsWorld {
	public static final int STEPSPERSECOND_DEFAULT = 60;
	private final float mStepLength;
	public MaxStepPhysicsWorld(final int pStepsPerSecond, final Vector2 pGravity, final boolean pAllowSleep) {
		super(pGravity, pAllowSleep);
		this.mStepLength = 1.0f / pStepsPerSecond;
	}

	public MaxStepPhysicsWorld(final int pStepsPerSecond, final Vector2 pGravity, final boolean pAllowSleep, final int pVelocityIterations, final int pPositionIterations) {
		super(pGravity, pAllowSleep, pVelocityIterations, pPositionIterations);
		this.mStepLength = 1.0f / pStepsPerSecond;
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mRunnableHandler.onUpdate(pSecondsElapsed);
		
		float stepLength = pSecondsElapsed;
		if(pSecondsElapsed>= this.mStepLength){
			stepLength = this.mStepLength;
		}
		this.mWorld.step(stepLength, this.mVelocityIterations, this.mPositionIterations);
		
		this.mPhysicsConnectorManager.onUpdate(pSecondsElapsed);
	}

}
