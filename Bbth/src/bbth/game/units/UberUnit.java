package bbth.game.units;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import bbth.engine.particles.ParticleSystem;
import bbth.engine.util.MathUtils;
import bbth.game.Team;

public class UberUnit extends Unit {
	private static final float MAX_POWER_LEVEL = 9000f;

	private static final float CHARGE_RATE = 4000f;
	private static final float DISCHARGE_RATE = CHARGE_RATE * .5f / 7f;
	private static final float DAMAGE_RATE = 140f;

	@Override
	public int getStartingHealth() {
		return 5 * AttackingUnit.MAX_DAMAGE;
	}

	public UberUnit(UnitManager unitManager, Team team, Paint p,
			ParticleSystem particleSystem) {
		super(unitManager, team, p, particleSystem);
	}

	boolean charging = true;
	boolean firing;
	Unit fireTarget;
	float powerLevel;

	@Override
	public void update(float seconds) {
		super.update(seconds);

		if (isDead())
			return;

		if (firing) {
			if (!getStateName().equals("attacking") || fireTarget.isDead() || powerLevel < 0) { //$NON-NLS-1$
				powerLevel = Math.max(0f, powerLevel);
				charging = true;
				firing = false;
				fireTarget = null;
			} else {
				powerLevel -= DISCHARGE_RATE;
				float damage = DAMAGE_RATE * seconds;
				for (Unit unit : unitManager.getUnitsIntersectingLine(getX(),
						getY(), fireTarget.getX(), fireTarget.getY())) {
					if (team.isEnemy(unit.getTeam())) {
						unit.takeDamage(damage, this);
					}
				}
			}
		} else {
			if (powerLevel > MAX_POWER_LEVEL) { // that is, if its power level
												// is over 9000
				powerLevel = MAX_POWER_LEVEL;
				charging = false;
			} else {
				charging = true;
			}

			if (charging) {
				powerLevel += CHARGE_RATE * seconds;
			}

			if (!charging && target != null && !target.isDead()
					&& getStateName().equals("attacking")) { //$NON-NLS-1$
				firing = true;
				fireTarget = target;
			}
		}
	}

	private float[] outline = { 0f, -15f, 10f, 10f, 10f, 10f, -10f, 10f, -10f,
			10f, 0f, -15f, };
	private static final float POWER_CIRCLE_RADIUS = 5f;

	@Override
	public void drawChassis(Canvas canvas) {
		canvas.save();

		canvas.translate(getX(), getY());

		canvas.rotate(MathUtils.toDegrees(getHeading()) + 90);
		canvas.drawLines(outline, paint);
		canvas.drawCircle(0f, 0f, 5f, paint);

		canvas.restore();

		if (!firing) {
			float radius = POWER_CIRCLE_RADIUS * powerLevel / MAX_POWER_LEVEL;
			if (radius > 0) {
				tempPaint.set(paint);

				paint.setStyle(Style.FILL);
				paint.setColor(Color.GRAY);
				canvas.drawCircle(getX(), getY(), radius, paint);

				paint.set(tempPaint);
			}
		}
	}

	@Override
	public void drawEffects(Canvas canvas) {
		if (firing) {
			float radius = POWER_CIRCLE_RADIUS * powerLevel / MAX_POWER_LEVEL;
			if (radius > 0f) {
				tempPaint.set(paint);

				paint.setStyle(Style.FILL);
				paint.setColor(Color.GRAY);

				paint.setStrokeWidth(radius);
				canvas.drawLine(getX(), getY(), fireTarget.getX(),
						fireTarget.getY(), paint);

				canvas.drawCircle(fireTarget.getX(), fireTarget.getY(),
						radius * .7f, paint);

				paint.setColor(Color.WHITE);
				canvas.drawCircle(getX(), getY(), radius, paint);
				canvas.drawCircle(fireTarget.getX(), fireTarget.getY(),
						radius * .45f, paint);

				paint.setStrokeWidth(radius * .5f);
				canvas.drawLine(getX(), getY(), fireTarget.getX(),
						fireTarget.getY(), paint);

				paint.set(tempPaint);
			}
		}
	}

	@Override
	public UnitType getType() {
		return UnitType.UBER;
	}

	@Override
	public float getRadius() {
		return 10f;
	}
}
