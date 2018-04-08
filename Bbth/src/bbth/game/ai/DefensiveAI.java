package bbth.game.ai;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.FloatMath;
import bbth.engine.ai.FlockRulesCalculator;
import bbth.engine.ai.fsm.FiniteState;
import bbth.engine.ai.fsm.FiniteStateMachine;
import bbth.engine.ai.fsm.SimpleGreaterTransition;
import bbth.engine.ai.fsm.SimpleLessTransition;
import bbth.engine.util.MathUtils;
import bbth.engine.util.Point;
import bbth.game.BBTHSimulation;
import bbth.game.Team;
import bbth.game.units.Unit;

public class DefensiveAI extends UnitAI {

	private Point m_flock_dir;

	Point start_point;
	Point end_point;

	private HashMap<String, Float> m_fsm_conditions;

	public DefensiveAI() {
		super();
		m_flock_dir = new Point();
		start_point = new Point();
		end_point = new Point();
		m_fsm_conditions = new HashMap<String, Float>();
	}

	@Override
	public void update(Unit entity, AIController c, FlockRulesCalculator flock) {
		FiniteState state = entity.getState();
		// Check if FSM has been initialized.
		if (state == null) {
			initialize_fsm(entity);
			state = entity.getState();
		}

		Unit enemy = getClosestEnemy(entity);
		entity.setTarget(enemy);

		String statename = state.getName();
		if (statename == "moving") { //$NON-NLS-1$
			do_movement(entity, c, flock);
		} else if (statename == "attacking") { //$NON-NLS-1$
			do_attack(entity, c, flock);
		} else {
			System.err.println("Error: entity in unknown state: " + statename); //$NON-NLS-1$
		}

		check_state_transition(entity, c, entity.getFSM());
	}

	private void check_state_transition(Unit entity, AIController c,
			FiniteStateMachine fsm) {
		Unit target = entity.getTarget();
		m_fsm_conditions.clear();
		float dist = Float.MAX_VALUE;
		if (target != null) {
			dist = MathUtils.getDistSqr(entity.getX(), entity.getY(),
					target.getX(), target.getY());
		}
		m_fsm_conditions.put("targetdist", dist); //$NON-NLS-1$
		fsm.update(m_fsm_conditions);
	}

	private void do_attack(Unit entity, AIController c,
			FlockRulesCalculator flock) {
		Unit target = entity.getTarget();
		if (target == null) {
			entity.setVelocity(0.0001f, 0.0f);
			return;
		}
		float angle = MathUtils.getAngle(entity.getX(), entity.getY(),
				target.getX(), target.getY());
		entity.setVelocity(0.0001f, angle);
	}

	private void do_movement(Unit entity, AIController c,
			FlockRulesCalculator flock) {
		float xcomp = 0;
		float ycomp = 0;
		float start_x = entity.getX();
		float start_y = entity.getY();

		// Calculate flocking.
		calculateFlocking(entity, c, flock, m_flock_dir);

		xcomp = m_flock_dir.x;
		ycomp = m_flock_dir.y;

		// Calculate somewhere to go if it's a leader.
		// if (!flock.hasLeader(entity)) {
		Unit enemy = entity.getTarget();

		float goal_x = entity.getX();
		float goal_y = (entity.getTeam() == Team.SERVER ? BBTHSimulation.GAME_HEIGHT / 4
				: BBTHSimulation.GAME_HEIGHT - BBTHSimulation.GAME_HEIGHT / 4);

		if (enemy != null) {
			start_point.set(start_x, start_y);
			end_point.set(goal_x, goal_y);

			if (m_tester != null
					&& m_tester.isLineOfSightClear(start_point, end_point) != null) {
				Point start = getClosestNode(start_point);
				Point end = getClosestNode(end_point);

				ArrayList<Point> path = null;

				if (start != null && end != null) {
					m_pathfinder.clearPath();
					m_pathfinder.findPath(start, end);
				}

				path = m_pathfinder.getPath();

				path.add(end_point);

				if (path.size() > 1) {
					Point goal_point = path.get(0);
					if (path.size() > 1
							&& m_tester.isLineOfSightClear(start_point,
									path.get(1)) == null) {
						goal_point = path.get(1);
					}

					// System.out.println("Next point: " + goal_point.x +
					// ", " + goal_point.y + " = " +
					// m_map_grid.getXPos((int)goal_point.x) + ", " +
					// m_map_grid.getYPos((int)goal_point.y));
					goal_x = goal_point.x;
					goal_y = goal_point.y;
				}

				// System.out.println("Team: " + entity.getTeam() +
				// " Start: " + entity.getX() + ", " + entity.getY() + " = "
				// + start.x + ", " + start.y + " End: " + end.x + ", " +
				// end.y);
			}
		}

		float angle = MathUtils.getAngle(entity.getX(), entity.getY(), goal_x,
				goal_y);
		float objectiveweighting = getObjectiveWeighting();
		xcomp += objectiveweighting * FloatMath.cos(angle);
		ycomp += objectiveweighting * FloatMath.sin(angle);
		// }

		float wanteddir = MathUtils.getAngle(0, 0, xcomp, ycomp);

		float wantedchange = MathUtils.normalizeAngle(wanteddir,
				entity.getHeading())
				- entity.getHeading();

		float actualchange = wantedchange;
		float maxvelchange = getMaxVelChange();

		if (actualchange > maxvelchange) {
			actualchange = maxvelchange;
		}

		if (actualchange < -1.0f * maxvelchange) {
			actualchange = -1.0f * maxvelchange;
		}

		float heading = entity.getHeading() + actualchange;

		entity.setVelocity(getMaxVel(), heading);
	}

	private void initialize_fsm(Unit entity) {
		FiniteState moving = new FiniteState("moving"); //$NON-NLS-1$
		FiniteState attacking = new FiniteState("attacking"); //$NON-NLS-1$

		SimpleLessTransition movingtrans = new SimpleLessTransition(moving,
				attacking);
		movingtrans.setInputName("targetdist"); //$NON-NLS-1$
		movingtrans.setVal(900);

		SimpleGreaterTransition attackingtrans = new SimpleGreaterTransition(
				attacking, moving);
		attackingtrans.setInputName("targetdist"); //$NON-NLS-1$
		attackingtrans.setVal(900);

		moving.addTransition(movingtrans);
		attacking.addTransition(attackingtrans);

		FiniteStateMachine fsm = entity.getFSM();
		fsm.addState("moving", moving); //$NON-NLS-1$
		fsm.addState("attacking", attacking); //$NON-NLS-1$

	}
}
