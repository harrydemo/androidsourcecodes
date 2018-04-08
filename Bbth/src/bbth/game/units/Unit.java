package bbth.game.units;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.FloatMath;
import bbth.engine.ai.fsm.FiniteState;
import bbth.engine.ai.fsm.FiniteStateMachine;
import bbth.engine.entity.BasicMovable;
import bbth.engine.net.simulation.Hash;
import bbth.engine.particles.ParticleSystem;
import bbth.engine.util.MathUtils;
import bbth.game.Team;

/**
 * A BBTH unit is one of the little dudes that walk around on the map and kill
 * each other.
 */
public abstract class Unit extends BasicMovable {
	protected Team team;
	protected Paint paint;
	protected ParticleSystem particleSystem;
	private FiniteStateMachine fsm;

	protected Unit target;
	protected UnitManager unitManager;

	protected int health = getStartingHealth();
	private static int nextHashCodeID;
	private int hashCodeID;

	public Unit(UnitManager unitManager, Team team, Paint p,
			ParticleSystem particleSystem) {
		hashCodeID = nextHashCodeID++;
		this.team = team;
		this.unitManager = unitManager;
		this.particleSystem = particleSystem;
		this.paint = p;
		fsm = new FiniteStateMachine();
	}

	public static void resetNextHashCodeID() {
		nextHashCodeID = 0;
	}

	public abstract void drawChassis(Canvas canvas);

	public void drawEffects(Canvas canvas) {
	}

	public void drawHealthBar(Canvas canvas, boolean serverDraw) {
		if (isDead())
			return;

		tempPaint.set(paint);
		paint.setStyle(Style.FILL);

		float radius = getRadius();
		float border = 1f;

		float left = getX() - radius;
		float top = (serverDraw) ? getY() + (radius * 2f) : getY()
				- (radius * 2f);
		float right = left + 2f * radius;
		float bottom = top + radius / 2f;

		paint.setColor(Color.WHITE);
		canvas.drawRect(left - border, top - border, right + border, bottom
				+ border, paint);

		paint.setColor(Color.RED);
		canvas.drawRect(left, top, right, bottom, paint);

		paint.setColor(Color.GREEN);
		float greenStopX = MathUtils.scale(0f, getStartingHealth(), left,
				right, getHealth(), true);
		canvas.drawRect(left, top, greenStopX, bottom, paint);

		paint.set(tempPaint);
	}

	public abstract UnitType getType();

	public abstract int getStartingHealth();

	public abstract float getRadius();

	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team newteam) {
		team = newteam;
	}

	public void setFSM(FiniteStateMachine fsm) {
		this.fsm = fsm;
	}

	public FiniteStateMachine getFSM() {
		return fsm;
	}

	public FiniteState getState() {
		return fsm.getCurrState();
	}

	// If state name is "attacking," can attack current target.
	public String getStateName() {
		return fsm.getStateName();
	}

	public void setTarget(Unit target) {
		this.target = target;
	}

	public Unit getTarget() {
		return target;
	}

	public int getHealth() {
		return health;
	}

	public boolean isDead() {
		return health <= 0f;
	}

	public void takeDamage(float damage, Unit inflictor) {
		if (!isDead()) {
			health -= damage;

			if (inflictor != null && inflictor.getType() == UnitType.UBER)
				attackedByUber = true;

			if (isDead()) {
				onDead();
				killer = inflictor;
				unitManager.notifyUnitDead(this);
			}
		}
	}

	// stupid, oh well
	boolean attackedByUber;

	public boolean wasDamagedByUber() {
		return attackedByUber;
	}

	Unit killer;

	public Unit getKiller() {
		return killer;
	}

	protected void onDead() {
		for (int i = 0; i < 10 * getRadius(); ++i) {
			float angle = MathUtils.randInRange(0, 2 * MathUtils.PI);
			float sin = FloatMath.sin(angle);
			float cos = FloatMath.cos(angle);
			float xVel = MathUtils.randInRange(25.f, 50.f) * cos;
			float yVel = MathUtils.randInRange(25.f, 50.f) * sin;
			particleSystem
					.createParticle()
					.line()
					.velocity(xVel * getRadius() * .25f,
							yVel * getRadius() * .25f).angle(angle)
					.shrink(0.1f, 0.15f).radius(getRadius() * 1.5f)
					.width(getRadius() / 2f)
					.position(getX() + sin * 2f, getY() + cos * 2f)
					.color(team.getRandomShade());
		}
	}

	protected static Paint tempPaint = new Paint();

	public int getSimulationSyncHash() {
		int hash = 0;
		hash = Hash.mix(hash, getX());
		hash = Hash.mix(hash, getY());
		hash = Hash.mix(hash, getXVel());
		hash = Hash.mix(hash, getYVel());
		hash = Hash.mix(hash, getHealth());
		hash = Hash.mix(hash, getType().ordinal());
		return hash;
	}

	@Override
	public int hashCode() {
		return hashCodeID;
	}
}
