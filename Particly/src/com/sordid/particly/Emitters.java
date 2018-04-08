package com.sordid.particly;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.particle.Particle;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;
import org.anddev.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.anddev.andengine.entity.particle.emitter.PointParticleEmitter;
import org.anddev.andengine.entity.particle.initializer.AccelerationInitializer;
import org.anddev.andengine.entity.particle.initializer.ColorInitializer;
import org.anddev.andengine.entity.particle.initializer.IParticleInitializer;
import org.anddev.andengine.entity.particle.initializer.RotationInitializer;
import org.anddev.andengine.entity.particle.initializer.VelocityInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Emitters {
	private static ExpireModifier sLongExpires = new ExpireModifier(3.5f);
	private static ExpireModifier sShortExpires = new ExpireModifier(2.5f);
	
	// Adds the particle emitter for the explode on ball push
	/*public static void addExplode(final float pX, final float pY, final TextureRegion pTexture, final Scene pScene) {
		final CircleOutlineParticleEmitter particleEmitter  = new CircleOutlineParticleEmitter(pX, pY, 0.5f);
        final ParticleSystem particleSystem = new ParticleSystem(particleEmitter, 10, 10, 10, pTexture);
        particleSystem.addParticleInitializer(new IParticleInitializer() {
            public void onInitializeParticle(Particle pParticle) {
                pParticle.getPhysicsHandler().setVelocityX((pParticle.getX()-particleEmitter.getCenterX())*35);
                pParticle.getPhysicsHandler().setVelocityY((pParticle.getY()-particleEmitter.getCenterY())*35);
            }
        });
        
        final IEntity parent = pScene.getLastChild();
        particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
        particleSystem.addParticleInitializer(new AccelerationInitializer(0, 15));
        particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
        particleSystem.addParticleInitializer(new ColorInitializer(1.0f, 0.0f, 0.0f));
        particleSystem.addParticleModifier(new ScaleModifier(0.5f, 3.0f, 0, 1.5f));
        particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0f, 0f, 1.5f));
        particleSystem.addParticleModifier(new ColorModifier(1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.5f));
        parent.attachChild(particleSystem);
        
        pScene.registerUpdateHandler(new TimerHandler(1.5f, new ITimerCallback() {
	            @Override
	            public void onTimePassed(final TimerHandler pTimerHandler){
                    particleSystem.setParticlesSpawnEnabled(false);
                    pScene.unregisterUpdateHandler(pTimerHandler);
	            }
	                   
	    }));
        pScene.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
	            @Override
	            public void onTimePassed(final TimerHandler pTimerHandler){
	            	particleSystem.detachChildren();
	            	pScene.detachChild(particleSystem);
                    pScene.unregisterUpdateHandler(pTimerHandler);
	            }              
	    }));
	}*/
	
	// Add the particle system for the final goal
	public static void addGoal(final float pX, final float pY, final TextureRegion pTexture, final Scene pScene) {
		final CircleOutlineParticleEmitter particleEmitter  = new CircleOutlineParticleEmitter(pX, pY, 0.5f);
        final ParticleSystem particleSystem = new ParticleSystem(particleEmitter, 10, 30, 100, pTexture);
        particleSystem.addParticleInitializer(new IParticleInitializer() {
            public void onInitializeParticle(Particle pParticle) {
                pParticle.getPhysicsHandler().setVelocityX((pParticle.getX()-particleEmitter.getCenterX())*20);
                pParticle.getPhysicsHandler().setVelocityY((pParticle.getY()-particleEmitter.getCenterY())*20);
            }
        });		
		particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
	
		particleSystem.addParticleInitializer(new AccelerationInitializer(1, 1));
		particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
		particleSystem.addParticleInitializer(new ColorInitializer(1.0f, 1.0f, 0.0f));

		particleSystem.addParticleModifier(Emitters.sLongExpires);
		particleSystem.addParticleModifier(new ColorModifier(0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f, 0.0f, 3.5f));
		particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.0f, 3.5f));	
		
		pScene.getLastChild().attachChild(particleSystem);
	}
	
	// Add the particle system for a waypoint on the map
	public static ParticleSystem addWaypoint(final float pX, final float pY, final TextureRegion pTexture, final Scene pScene, final boolean pVisible) {
		final CircleOutlineParticleEmitter particleEmitter  = new CircleOutlineParticleEmitter(pX, pY, 0.7f);
        final ParticleSystem particleSystem = new ParticleSystem(particleEmitter, 5, 5, 20, pTexture);
        particleSystem.addParticleInitializer(new IParticleInitializer() {
            public void onInitializeParticle(Particle pParticle) {
                pParticle.getPhysicsHandler().setVelocityX((pParticle.getX()-particleEmitter.getCenterX())*12);
                pParticle.getPhysicsHandler().setVelocityY((pParticle.getY()-particleEmitter.getCenterY())*12);
            }
        });		
		particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
	
		particleSystem.addParticleInitializer(new AccelerationInitializer(2, 2));
		particleSystem.addParticleInitializer(new RotationInitializer(-20.0f, 20.0f));
		particleSystem.addParticleInitializer(new ColorInitializer(1.0f, 1.0f, 0.0f));

		particleSystem.addParticleModifier(new ScaleModifier(0.5f, 1.0f, 0, 3.5f));
		particleSystem.addParticleModifier(Emitters.sLongExpires);
		particleSystem.addParticleModifier(new ColorModifier(1.0f, 1.0f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f, 3.5f));
		particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.0f, 3.5f));
		particleSystem.addParticleModifier(new AlphaModifier(0.0f, 1.0f, 0.0f, 1.0f));
		particleSystem.setVisible(pVisible);
	
		if(pVisible) {
			pScene.getLastChild().attachChild(particleSystem);
		}
		return particleSystem;
	}
	
	// Add the particle system for a teleport
	public static ParticleSystem addTeleport(final float pX, final float pY, final TextureRegion pTexture, final Scene pScene) {
		final CircleParticleEmitter particleEmitter  = new CircleParticleEmitter(pX, pY, 5f);
        final ParticleSystem particleSystem = new ParticleSystem(particleEmitter, 10, 20, 50, pTexture);
        particleSystem.addParticleInitializer(new IParticleInitializer() {
            public void onInitializeParticle(Particle pParticle) {
                pParticle.getPhysicsHandler().setVelocityX((particleEmitter.getCenterX()-pParticle.getX())*5);
                pParticle.getPhysicsHandler().setVelocityY((particleEmitter.getCenterY()-pParticle.getY())*5);
            }
        });		
		particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
	
		particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
		particleSystem.addParticleInitializer(new ColorInitializer(0.0f, 1.0f, 0.0f));

		particleSystem.addParticleModifier(new ScaleModifier(1.0f, 0.5f, 0, 2.5f));
		particleSystem.addParticleModifier(Emitters.sShortExpires);
		particleSystem.addParticleModifier(new ColorModifier(0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.5f, 0.0f, 2.5f));
		particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.0f, 2.5f));
	
		pScene.getLastChild().attachChild(particleSystem);
		return particleSystem;
	}
	
	// Add the particle system for a launcher
	public static ParticleSystem addLaunch(final float pX, final float pY, final TextureRegion pTexture, final Scene pScene, final String pAngle, final String pForce) {
		final double dAngle = Double.parseDouble(pAngle);
		final float angle = (float)Math.toRadians(dAngle);
		final double force = ((Double.parseDouble(pForce)*10)/200)+5;
		final float y = (float)(Math.sin(angle)*force*-1);
		final float x = (float)(Math.cos(angle)*force);
		
		final PointParticleEmitter particleEmitter  = new PointParticleEmitter(pX, pY);
        final ParticleSystem particleSystem = new ParticleSystem(particleEmitter, 5, 10, 50, pTexture);       
        particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
        particleSystem.addParticleInitializer(new VelocityInitializer(x-5, x+5, y-5, y+5));
        particleSystem.addParticleInitializer(new AccelerationInitializer(x, y));
        particleSystem.addParticleInitializer(new RotationInitializer((float)dAngle*-1+10, (float)dAngle*-1-10));
        particleSystem.addParticleInitializer(new ColorInitializer(1.0f, 0.0f, 0.0f));
        particleSystem.addParticleModifier(Emitters.sShortExpires);
        particleSystem.addParticleModifier(new ScaleModifier(0.5f, 1.0f, 0, 2.5f));
        particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0f, 0f, 2.5f));
        particleSystem.addParticleModifier(new ColorModifier(1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 2.5f));
	
		pScene.getLastChild().attachChild(particleSystem);
		return particleSystem;
	}	
}