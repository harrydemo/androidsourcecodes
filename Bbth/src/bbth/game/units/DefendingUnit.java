package bbth.game.units;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.FloatMath;
import bbth.engine.particles.ParticleSystem;
import bbth.engine.util.MathUtils;
import bbth.game.Team;

public class DefendingUnit extends Unit {
	private static final float FIRE_RATE = .5f; // twice a second
	private static final float DAMAGE_PER_SHOT = 25f;
	private static final float LASER_DISPLAY_TIME = .05f;

	public DefendingUnit(UnitManager unitManager, Team team, Paint p,
			ParticleSystem particleSystem) {
		super(unitManager, team, p, particleSystem);
	}

	boolean firing;
	float timeSinceLastShot;
	Unit fireTarget;

	@Override
	public void update(float seconds) {
		super.update(seconds);

		if (isDead())
			return;

		timeSinceLastShot += seconds;
		if (firing) {
			if (!getStateName().equals("attacking") || fireTarget.isDead()) { //$NON-NLS-1$
				firing = false;
				fireTarget = null;
			} else if (timeSinceLastShot > FIRE_RATE) {
				timeSinceLastShot = 0f;
				fireTarget.takeDamage(DAMAGE_PER_SHOT, this);
			}
		} else {
			if (target != null && !target.isDead()
					&& getStateName().equals("attacking")) { //$NON-NLS-1$
				firing = true;
				fireTarget = target;
			}
		}

	}

	private static final float CANNON_LENGTH = 8f;
	private static final float TURRET_RADIUS = 2f;
	private static final float SQUARE_HALFWIDTH = 4f;

	RectF rect = new RectF(-SQUARE_HALFWIDTH, -SQUARE_HALFWIDTH,
			SQUARE_HALFWIDTH, SQUARE_HALFWIDTH);

	@Override
	public void drawChassis(Canvas canvas) {
		float heading = getHeading();

		canvas.save();

		canvas.translate(getX(), getY());
		canvas.rotate(MathUtils.toDegrees(heading));

		// draw body
		canvas.drawRect(rect, paint);

		// draw turret
		canvas.drawCircle(0f, 0f, TURRET_RADIUS, paint);

		// draw cannon
		Unit currentTarget = firing ? fireTarget : target;

		if (currentTarget == null) {
			canvas.drawLine(TURRET_RADIUS, 0f, CANNON_LENGTH, 0f, paint);
		} else {
			float targetX = currentTarget.getX() - getX();
			float targetY = currentTarget.getY() - getY();

			// float endLength = FloatMath.sqrt(targetX*targetX +
			// targetY*targetY);
			float headingToTarget = MathUtils
					.getAngle(0f, 0f, targetX, targetY);

			float xComponent = FloatMath.cos(headingToTarget - heading);
			float yComponent = FloatMath.sin(headingToTarget - heading);

			float cannonEndX = CANNON_LENGTH * xComponent;
			float cannonEndY = CANNON_LENGTH * yComponent;

			canvas.drawLine(TURRET_RADIUS * xComponent, TURRET_RADIUS
					* yComponent, cannonEndX, cannonEndY, paint);
		}

		canvas.restore();
	}

	@Override
	public void drawEffects(Canvas canvas) {
		if (firing && timeSinceLastShot < LASER_DISPLAY_TIME) {
			float heading = getHeading();

			canvas.save();
			canvas.translate(getX(), getY());
			canvas.rotate(MathUtils.toDegrees(heading));

			tempPaint.set(paint);
			paint.setColor(Color.GRAY);

			float targetX = fireTarget.getX() - getX();
			float targetY = fireTarget.getY() - getY();

			float endLength = FloatMath.sqrt(targetX * targetX + targetY
					* targetY);
			float headingToTarget = MathUtils
					.getAngle(0f, 0f, targetX, targetY);

			float xComponent = FloatMath.cos(headingToTarget - heading);
			float yComponent = FloatMath.sin(headingToTarget - heading);

			float cannonEndX = CANNON_LENGTH * xComponent;
			float cannonEndY = CANNON_LENGTH * yComponent;

			canvas.drawLine(cannonEndX, cannonEndY, endLength * xComponent,
					endLength * yComponent, paint);

			paint.setColor(Color.WHITE);
			canvas.drawCircle(cannonEndX, cannonEndY, 1f, paint);

			paint.set(tempPaint);

			canvas.restore();
		}
	}

	@Override
	public UnitType getType() {
		return UnitType.DEFENDING;
	}

	@Override
	public int getStartingHealth() {
		return 60;
	}

	@Override
	public float getRadius() {
		return 5;
	}
}
