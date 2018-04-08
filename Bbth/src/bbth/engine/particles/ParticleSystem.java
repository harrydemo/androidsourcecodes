package bbth.engine.particles;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * A particle system that handles storing, updating, and drawing particles
 * Particles are cached to prevent object allocations/deallocations during
 * gameplay
 * 
 * @author jardini
 */
public class ParticleSystem {

	private Particle[] _particles;
	private int _count;

	public ParticleSystem(int numParticles) {
		_particles = new Particle[numParticles];
		for (int i = 0; i < numParticles; ++i) {
			_particles[i] = new Particle(0.1f);
		}
		_count = 0;
	}

	// threshold is the smallest a particle can get before being removed
	public ParticleSystem(int numParticles, float threshold) {
		_particles = new Particle[numParticles];
		for (int i = 0; i < numParticles; ++i) {
			_particles[i] = new Particle(threshold);
		}
		_count = 0;
	}

	public Particle createParticle() {
		if (_count < _particles.length) {
			return _particles[_count++];
		} else {
			return _particles[_particles.length - 1];
		}
	}

	public void tick(float seconds) {
		for (int i = 0; i < _count; ++i) {
			boolean isAlive = _particles[i].tick(seconds);
			if (!isAlive) {
				// swap the current particle with the last active particle
				Particle temp = _particles[i];
				_particles[i] = _particles[_count - 1];
				_particles[_count - 1] = temp;

				// forget about the dead particle that we just moved to the end
				// of the active particle list
				--_count;

				// don't skip the particle that we just swapped in
				--i;
			}
		}
	}

	public void draw(Canvas canvas, Paint paint) {
		for (int i = 0; i < _count; ++i) {
			_particles[i].draw(canvas, paint);
		}
	}

	public void reset() {
		_count = 0;
	}

	public void gravity(float f) {
		for (int i = 0; i < _count; ++i) {
			_particles[i]._yVel += f;
		}
	}

	public void updateAngles() {
		for (int i = 0; i < _count; ++i) {
			Particle p = _particles[i];
			p._angle = (float) Math.atan2(p._yVel, p._xVel);
		}
	}
}
