package com.threed.jpct.games.alienrunner;

import com.threed.jpct.*;

/**
 * A simple manager for particles. I wouldn't call it a "system" for now...
 */
public class ParticleManager {

	private Particle[] parts = null;
	private World world = null;
	private int cnt = 30;
	private SimpleVector work = new SimpleVector();
	
	private int active=0;

	/**
	 * Creates a new particle manager with a default number of particles (100).
	 * 
	 * @param level
	 *            World the world
	 */
	public ParticleManager(World level) {
		this.world = level;
		init();
	}

	/**
	 * Creates a new particle manager for a number of particles.
	 * 
	 * @param level
	 *            World the world
	 * @param max
	 *            int the number of particles
	 */
	public ParticleManager(World level, int max) {
		cnt = max;
		this.world = level;
		init();
	}

	/**
	 * Moves all particles manmaged by this manager.
	 * 
	 * @param ticks
	 *            long the number of ticks passed since the last call
	 */
	public void move(long ticks) {
		int act=0;
		int actStore=active;
		for (int i = 0; i < cnt && act<active; i++) {
			Particle pp = parts[i];
			if (pp != null && pp.getVisibility()) {
				act++;
				pp.move(ticks);
				if (!pp.getVisibility()) {
					actStore--;
				}
			}
		}
		active=actStore;
	}

	public void reset() {
		for (int i = 0; i < cnt; i++) {
			parts[i].setVisibility(false);
		}
	}
	
	public int getCount() {
		return cnt;
	}

	public Particle[] getParticles() {
		return parts;
	}

	public void addEmitter(SimpleVector pos, SimpleVector baseDir, int count, String texture) {
		float len=baseDir.length();
		float lenHalf=len/2f;
		long time=Ticker.getTime();
		for (int i = 0; i < count; i++) {
			work.set(baseDir);
			work.x += -lenHalf + Randomizer.random()*len;
			work.z += -lenHalf + Randomizer.random()*len;
			work.y += -lenHalf + Randomizer.random()*len;
			addParticle(pos, work, 0f, 6, texture, time);
		}
	}
	
	public void addEmitter(SimpleVector pos, SimpleVector baseDir, int count, String texture, int secs, int size, int transparency, int speed, boolean add) {
		float len=baseDir.length();
		float lenHalf=len/2f;
		long time=Ticker.getTime();
		for (int i = 0; i < count; i++) {
			work.set(baseDir);
			work.x += -lenHalf + Randomizer.random()*len;
			work.z += -lenHalf + Randomizer.random()*len;
			work.y += -lenHalf + Randomizer.random()*len;
			addParticle(pos, work, 99999999999f, secs, texture, size, add,transparency, speed, RGBColor.WHITE, time);
		}
	}

	/**
	 * Adds a new particle to the manager. If the maximum number of active,
	 * visible particles have been reached, nothing happens. This method is for
	 * adding a particle that it limited in y-direction but not in time.
	 * 
	 * @param pos
	 *            SimpleVector the position
	 * @param vel
	 *            SimpleVector the velocity
	 * @param limit
	 *            float the y-limit
	 * @param texture
	 *            String the texture's name
	 */
	public void addParticle(SimpleVector pos, SimpleVector vel, float limit, int speed, String texture, long curTime) {
		addParticle(pos, vel, limit, -1, texture, 6f, true,10, speed,RGBColor.WHITE, curTime);
	}

	/**
	 * Adds a new particle to the manager. If the maximum number of active,
	 * visible particles have been reached, nothing happens. This method is for
	 * adding a particle that it in time but not in y-direction.
	 * 
	 * @param pos
	 *            SimpleVector the position
	 * @param vel
	 *            SimpleVector the velocity
	 * @param time
	 *            long the life time
	 * @param texture
	 *            String the texture's name
	 */
	public void addParticle(SimpleVector pos, SimpleVector vel, long time, int speed, String texture, long curTime) {
		addParticle(pos, vel, 9999999999999999f, time, texture, 6f, true, 10,  speed,RGBColor.WHITE, curTime);
	}

	/**
	 * Adds a new particle to the manager. If the maximum number of active,
	 * visible particles have been reached, nothing happens. The particle will
	 * be limited in time and y-direction.
	 * 
	 * @param pos
	 *            SimpleVector the position
	 * @param vel
	 *            SimpleVector the velocity
	 * @param limit
	 *            float the y-limit
	 * @param time
	 *            long the time-limit
	 * @param texture
	 *            String the texture's name
	 */
	public void addParticle(SimpleVector pos, SimpleVector vel, float limit, long time, String texture, float size, boolean add, int transparency, int speed,RGBColor col, long curTime) {
		
		if (!GameConfig.particles) {
			return;
		}
		
		Particle p = getParticle();
		if (p != null) {
			active++;
			p.setOrigin(pos);
			p.setVelocity(vel);
			p.setLifeTime(time);
			p.setYLimit(limit);
			p.setTexture(texture);
			p.setColor(col);
			p.setSpeed(speed);
			p.setTransparency(transparency);
			if (add) {
				p.setTransparencyMode(Object3D.TRANSPARENCY_MODE_ADD);
			} else {
				p.setTransparencyMode(Object3D.TRANSPARENCY_MODE_DEFAULT);
			}
			if (size != 1) {
				p.setScale(size);
			}
			p.reset(curTime);
		}
	}

	/**
	 * Initializes the manager
	 */
	private void init() {
		parts = new Particle[cnt];
		for (int i = 0; i < cnt; i++) {
			Particle p = new Particle();
			parts[i] = p;
			p.setVisibility(false);
			world.addObject(p);
		}
	}

	/**
	 * Gets a particle from the manager's pool. If the pool if full of
	 * particles, null will be returned.
	 * 
	 * @return Particle a fresh particle or null
	 */
	private Particle getParticle() {
		int np = -1;
		for (int i = 0; i < cnt; i++) {
			Particle pp = parts[i];
			if (pp != null && !pp.getVisibility()) {
				pp.setVisibility(Object3D.OBJ_VISIBLE);
				if (pp.getScale() != 0.8f) {
					pp.setScale(0.8f);
				}
				return pp;
			}
			if (pp == null && np == -1) {
				np = i;
			}
		}

		Particle p = null;

		int s = 0;
		if (np != -1) {
			s = np;
		}

		for (int i = s; i < cnt; i++) {
			if (parts[i] == null) {
				p = new Particle();
				world.addObject(p);
				parts[i] = p;
				break;
			}
		}
		return p;
	}
}
