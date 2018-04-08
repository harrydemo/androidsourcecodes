package com.sordid.particly;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;

public class LinkedSprite extends Sprite{
	private Body mBody;
	private MouseJoint mJoint = null;
	
	public LinkedSprite(float pX, float pY, TextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
	}
	
	@Override
    public void setPosition(final float pX, final float pY) {
        this .mX = pX;
        this .mY = pY;
        if(mJoint != null) {
        	final Vector2 vector = Vector2Pool.obtain(
        		(mX+(mWidth/2))/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
        		(mY+(mHeight/2))/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT
        	);
        	
        	this.mJoint.setTarget(vector);
        	//Vector2Pool.recycle(vector);
			mBody.setTransform(vector, 0.0f);
        	Vector2Pool.recycle(vector);
        	
        	final Vector2 vector2 = Vector2Pool.obtain(0,0);
			mBody.setLinearVelocity(vector2);
			Vector2Pool.recycle(vector2);
			
			mBody.setAngularVelocity(0);
        }
    }
	
	public void setBody(Body pBody) {
		mBody = pBody;
	}
	
	public void setJoint(MouseJoint pJoint) {
		mJoint = pJoint;
	}
}