package com.sordid.particly;

import java.util.HashMap;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.AnimatedSprite.IAnimationListener;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;

public class WorldContact implements ContactListener {
	protected final ParticlyActivity mParent;
	protected boolean mCanTeleport = true;
	private Vector2 mScratchVector1;
	private Vector2 mScratchVector2;
	protected IAnimationListener mListener = new IAnimationListener(){
		@Override
		public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
			mParent.mBall.animate(ParticlyActivity.ANIMATION_DEFAULT, true);;
		}
	};
	
	public WorldContact(final ParticlyActivity pParent) {
		mParent = pParent;
	}
	
	// Check the two objects and see if they are touching
	public boolean isTouching(final Contact pContact, final String pActualA, final String pExpectedA, final String pActualB, final String pExpectedB) {
		return ((pActualB.equals(pExpectedA) && pActualA.equals(pExpectedB))
				|| (pActualA.equals(pExpectedA) && pActualB.equals(pExpectedB)))
				&& pContact.isTouching();
	}
	
	// Tries to find userData from either of the objects that has it
	public String findUserData(final HashMap<String, String> pA, final HashMap<String, String> pB, final String pKey) {
		if(pA.containsKey(pKey)) {
			return pA.get(pKey);
		} else if(pB.containsKey(pKey)) {
			return pB.get(pKey);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void beginContact(Contact contact) {
		final HashMap<String,String> a = (HashMap<String,String>) contact.getFixtureA().getBody().getUserData();
		final HashMap<String,String> b = (HashMap<String,String>) contact.getFixtureB().getBody().getUserData();
			
		if(isTouching(contact, a.get("type"), "goal", b.get("type"), "ball")) {
			mParent.roundWon();
			mParent.showMenu(true);
		} else if(isTouching(contact, a.get("type"), "waypoint", b.get("type"), "ball")) {
			final String index = findUserData(a, b, "index");
			if(index != null) {
				mParent.removeWaypoint(Integer.parseInt(index));
			}
		} else if(mCanTeleport && isTouching(contact, a.get("type"), "teleport", b.get("type"), "ball")) {
			final String target = findUserData(a, b, "target");
			if(target != null) {
				final Teleporter teleporter = mParent.mTeleports.get(target);
				if(teleporter != null) {
					teleporter.teleport(mParent.mScene, target, mParent.mBall);
				}
			}
		} else if(isTouching(contact, a.get("type"), "launch", b.get("type"), "ball")) {
			final String angle = findUserData(a, b, "angle");
			final String force = findUserData(a, b, "force");
			if(angle != null && force != null) {
				final double dForce = Double.parseDouble(force);
				final double dAngle = Math.toRadians(Double.parseDouble(angle));
				final float y = (float)(Math.sin(dAngle)*dForce);
				final float x = (float)(Math.cos(dAngle)*dForce);
				mParent.explode(mParent.mBall.getX()+x, mParent.mBall.getY()-y, false);
			}			
		} else if(isTouching(contact, a.get("type"), "wall", b.get("type"), "ball") || isTouching(contact, a.get("type"), "object", b.get("type"), "ball")) {
			mScratchVector1 = contact.GetWorldManifold().getPoints()[0];
			mScratchVector2 = contact.getFixtureA().getBody().getLinearVelocityFromWorldPoint(mScratchVector1);
			mScratchVector2.sub(contact.getFixtureB().getBody().getLinearVelocityFromWorldPoint(mScratchVector1));
	        float force=mScratchVector2.len();
	        
	        if(mParent.mSound) {
				if(force > 10) {
					int index = mParent.mRandom.nextInt(ParticlyActivity.sHitVoiceSounds.length-1);
					ParticlyActivity.sHitVoiceSounds[index].setVolume((((((force > 30 ? 30 : force)-10)*80)+20)/20)/100);
					ParticlyActivity.sHitVoiceSounds[index].play();
				}
	
				int index = mParent.mRandom.nextInt(ParticlyActivity.sHitSounds.length-1);
				ParticlyActivity.sHitSounds[index].setVolume(((((force > 10 ? 10 : force)*80)+20)/10)/100);
				ParticlyActivity.sHitSounds[index].play();		
	        }
			
			mParent.mBall.animate(ParticlyActivity.ANIMATION_CONTACT, false, mListener);
		}
	}

	@Override
	public void endContact(Contact contact) {}
}