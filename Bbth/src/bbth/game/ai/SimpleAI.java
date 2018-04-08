package bbth.game.ai;

import android.util.FloatMath;
import bbth.engine.ai.FlockRulesCalculator;
import bbth.engine.util.MathUtils;
import bbth.game.units.Unit;

public class SimpleAI extends UnitAI {

	public SimpleAI() {
		super();
	}

	@Override
	public void update(Unit entity, AIController c, FlockRulesCalculator flock) {
		float xcomp = 0;
		float ycomp = 0;

		Unit enemy = getClosestEnemy(entity);

		if (enemy != null) {
			float angle = MathUtils.getAngle(entity.getX(), entity.getY(),
					enemy.getX(), enemy.getY());
			xcomp += 50.0f * FloatMath.cos(angle);
			ycomp += 50.0f * FloatMath.sin(angle);
			entity.setVelocityComponents(xcomp, ycomp);
		}
	}
}
