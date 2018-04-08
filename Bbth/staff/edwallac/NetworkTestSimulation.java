package edwallac;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import bbth.engine.net.simulation.LockStepProtocol;
import bbth.engine.net.simulation.Simulation;
import bbth.engine.particles.ParticleSystem;
import bbth.game.BBTHGame;

public class NetworkTestSimulation extends Simulation {

	private ParticleSystem particles = new ParticleSystem(1000);
	private int timestep;
	private Random random = new Random(0);

	public NetworkTestSimulation(LockStepProtocol protocol, boolean isServer) {
		super(6, 0.1f, 2, protocol, isServer);
	}

	public int getTimestep() {
		return timestep;
	}

	@Override
	protected final void simulateTapDown(float x, float y, boolean isServer, boolean isHold, boolean isOnBeat) {
		particles.createParticle().position(x, y).radius(10).color(Color.RED).shrink(0.5f);
	}

	@Override
	protected final void simulateTapMove(float x, float y, boolean isServer) {
		particles.createParticle().position(x, y).radius(5).color(Color.RED).shrink(0.5f);
	}

	@Override
	protected final void simulateTapUp(float x, float y, boolean isServer) {
	}

	@Override
	protected void simulateCustomEvent(float x, float y, int code, boolean isServer) {
	}

	public void draw(Canvas canvas, Paint paint) {
		particles.draw(canvas, paint);
	}

	@Override
	protected void update(float seconds) {
		if ((timestep & 31) == 0) {
			float x = random.nextFloat() * BBTHGame.WIDTH;
			float y = random.nextFloat() * BBTHGame.HEIGHT;
			particles.createParticle().position(x, y).radius(10).color(Color.DKGRAY).shrink(0.5f);
		}

		particles.tick(seconds);
		timestep++;
	}

	@Override
	protected int getSimulationSyncHash() {
		return 0;
	}
}
