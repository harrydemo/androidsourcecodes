package bbth.game.units;

import static bbth.game.Team.SERVER;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.FloatMath;
import bbth.engine.particles.ParticleSystem;
import bbth.engine.util.Envelope;
import bbth.engine.util.Envelope.OutOfBoundsHandler;
import bbth.engine.util.MathUtils;
import bbth.game.Team;

public class AttackingUnit extends Unit {
	public static final float DETONATION_WITHIN_DISTANCE = 15f;
	public static final float DETONATION_MAX_RADIUS = 40f;
	public static final float DETONATION_TIME = .4f;
	public static final float MIN_DAMAGE = 30f;
	public static final int MAX_DAMAGE = 90;

	private static final Envelope DAMAGE_ENVELOPE = new Envelope(MAX_DAMAGE,
			OutOfBoundsHandler.RETURN_FIRST_OR_LAST);
	static {
		DAMAGE_ENVELOPE.addLinearSegment(DETONATION_MAX_RADIUS, MIN_DAMAGE);
	}
	private static final Envelope RADIUS_ENVELOPE = new Envelope(0f,
			OutOfBoundsHandler.RETURN_FIRST_OR_LAST);
	static {
		RADIUS_ENVELOPE.addLinearSegment(DETONATION_TIME * .25f,
				DETONATION_MAX_RADIUS);
		RADIUS_ENVELOPE.addLinearSegment(DETONATION_TIME * .75f, 0f);
	}
	private static final Envelope ALPHA_ENVELOPE = new Envelope(0f,
			OutOfBoundsHandler.RETURN_FIRST_OR_LAST);
	static {
		ALPHA_ENVELOPE.addLinearSegment(DETONATION_TIME * .25f, 200f);
		ALPHA_ENVELOPE.addLinearSegment(DETONATION_TIME * .75f, 0f);
	}

	public AttackingUnit(UnitManager unitManager, Team team, Paint p,
			ParticleSystem particleSystem) {
		super(unitManager, team, p, particleSystem);
	}

	private static final float LINE_LENGTH = 6f;

	@Override
	public void drawChassis(Canvas canvas) {
		if (detonating)
			return;

		// uncomment to draw detection radius
		// tempPaint.set(paint);
		// paint.setColor(Color.WHITE);
		// canvas.drawCircle(this.getX(), this.getY(),
		// DETONATION_WITHIN_DISTANCE, paint);
		// paint.set(tempPaint);

		canvas.drawCircle(this.getX(), this.getY(), 4, paint);

		float heading = getHeading();
		canvas.drawLine(getX(), getY(),
				getX() + LINE_LENGTH * FloatMath.cos(heading), getY()
						+ LINE_LENGTH * FloatMath.sin(heading), paint);
	}

	@Override
	public void drawEffects(Canvas canvas) {
		if (detonating) {
			tempPaint.set(paint);

			float currentRadius = (float) RADIUS_ENVELOPE
					.getValueAtTime(detonationTime);

			paint.setColor(team == SERVER ? Color.rgb(231, 80, 0) : Color.rgb(
					0, 168, 231));
			// paint.setAlpha(200);
			paint.setAlpha((int) ALPHA_ENVELOPE.getValueAtTime(detonationTime));
			paint.setStyle(Style.FILL);

			canvas.drawCircle(getX(), getY(), currentRadius, paint);

			paint.set(tempPaint);
			return;
		}
	}

	boolean detonating;
	float detonationTime;

	@Override
	public boolean isDead() {
		if (detonating)
			return true;
		else
			return super.isDead();
	}

	@Override
	public void takeDamage(float damage, Unit inflictor) {
		if (!detonating)
			super.takeDamage(damage, inflictor);
	}

	@Override
	public void update(float seconds) {
		if (detonating) {
			detonationTime += seconds;
			if (detonationTime > RADIUS_ENVELOPE.getTotalLength())
				unitManager.notifyUnitDead(this);
			return;
		}

		// move only if not detonating
		super.update(seconds);

		if (isDead() || target == null || !getStateName().equals("attacking")) //$NON-NLS-1$
			return;

		float x = getX();
		float y = getY();

		if (MathUtils.getDistSqr(x, y, target.getX(), target.getY()) < DETONATION_WITHIN_DISTANCE
				* DETONATION_WITHIN_DISTANCE) {
			detonating = true;
			onDead();
			// takeDamage(getHealth());
		}
	}

	@Override
	protected void onDead() {
		super.onDead();
		for (Unit unit : unitManager.getUnitsInCircle(getX(), getY(),
				DETONATION_MAX_RADIUS)) {
			if (team.isEnemy(unit.getTeam()))
				unit.takeDamage((float) DAMAGE_ENVELOPE
						.getValueAtTime(MathUtils.getDist(getX(), getY(),
								unit.getX(), unit.getY())), this);
		}
	}

	@Override
	public UnitType getType() {
		return UnitType.ATTACKING;
	}

	@Override
	public int getStartingHealth() {
		return 50;
	}

	@Override
	public float getRadius() {
		return 4f;
	}
}
