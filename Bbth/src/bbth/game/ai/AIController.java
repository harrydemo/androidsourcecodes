package bbth.game.ai;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import android.util.FloatMath;
import bbth.engine.ai.ConnectedGraph;
import bbth.engine.ai.FlockRulesCalculator;
import bbth.engine.ai.Pathfinder;
import bbth.engine.fastgraph.LineOfSightTester;
import bbth.engine.fastgraph.Wall;
import bbth.engine.util.MathUtils;
import bbth.engine.util.Point;
import bbth.game.BBTHGame;
import bbth.game.GridAcceleration;
import bbth.game.Team;
import bbth.game.units.Unit;
import bbth.game.units.UnitType;

public class AIController {
	ArrayList<Unit> m_units;
	EnumMap<Team, ArrayList<Unit>> m_entities;
	EnumMap<Team, Integer> m_last_updated;
	EnumMap<Team, FlockRulesCalculator> m_flocks;

	DefensiveAI m_defensive;
	OffensiveAI m_offensive;
	UberAI m_uber;

	Team[] m_teams;

	private float m_fraction_to_update = 1;
	private LineOfSightTester m_tester;

	Point m_center_stick;
	Point m_wall;
	Point m_wall_to_player;
	Point m_vec_result;

	public AIController() {
		m_defensive = new DefensiveAI();
		m_offensive = new OffensiveAI();
		m_uber = new UberAI();
		m_center_stick = new Point();
		m_wall = new Point();
		m_wall_to_player = new Point();
		m_vec_result = new Point();

		m_units = new ArrayList<Unit>();
		m_flocks = new EnumMap<Team, FlockRulesCalculator>(Team.class);
		m_last_updated = new EnumMap<Team, Integer>(Team.class);
		m_entities = new EnumMap<Team, ArrayList<Unit>>(Team.class);
		m_teams = Team.values();

		for (Team t : m_teams) {
			m_flocks.put(t, new FlockRulesCalculator());
			m_last_updated.put(t, 0);
			m_entities.put(t, new ArrayList<Unit>());
		}
	}

	public void setPathfinder(Pathfinder pathfinder, ConnectedGraph graph,
			LineOfSightTester tester, GridAcceleration accel) {
		m_tester = tester;
		m_defensive.setPathfinder(pathfinder, graph, tester, accel);
		m_offensive.setPathfinder(pathfinder, graph, tester, accel);
		m_uber.setPathfinder(pathfinder, graph, tester, accel);
	}

	public void addEntity(Unit u) {
		m_units.add(u);
		m_entities.get(u.getTeam()).add(u);
		m_flocks.get(u.getTeam()).addObject(u);
	}

	public void removeEntity(Unit u) {
		m_units.remove(u);
		m_entities.get(u.getTeam()).remove(u);
		m_flocks.get(u.getTeam()).removeObject(u);
	}

	public ArrayList<Unit> getEnemies(Unit u) {
		return m_entities.get(u.getTeam().getOppositeTeam());
	}

	public List<Unit> getUnits() {
		return m_units;
	}

	public void update() {
		for (Team t : m_teams) {
			update(m_entities.get(t), m_flocks.get(t), t);
		}
	}

	private void update(ArrayList<Unit> entities, FlockRulesCalculator flock,
			Team team) {
		int size = entities.size();
		int num_to_update = (int) ((size * m_fraction_to_update) + 1);

		if (size == 0) {
			return;
		}

		int last_updated = m_last_updated.get(team);

		if (last_updated > size - 1) {
			last_updated = 0;
		}

		int i = last_updated;
		while (num_to_update > 0) {
			Unit entity = entities.get(i);

			if (entity.getType() == UnitType.DEFENDING) {
				m_defensive.update(entity, this, flock);
			} else if (entity.getType() == UnitType.ATTACKING) {
				m_offensive.update(entity, this, flock);
			} else if (entity.getType() == UnitType.UBER) {
				// m_offensive.update(entity, this, flock);
				m_uber.update(entity, this, flock);
			}

			num_to_update--;

			if (i >= size - 1) {
				i = 0;
			} else {
				i++;
			}
		}

		if (i > size - 1) {
			m_last_updated.put(team, 0);
		} else {
			m_last_updated.put(team, i);
		}

		if (m_tester != null) {
			// Avoid letting the entities run into walls, if possible.
			for (int c = 0; c < size; c++) {
				Unit entity = entities.get(c);

				float start_x = entity.getX();
				float start_y = entity.getY();
				// Check if we are going to run into a wall:
				float heading = entity.getHeading();
				float startheading = heading;
				boolean clear = false;
				int tries = 0;
				while (!clear) {
					if (tries > 100) {
						heading = startheading + MathUtils.PI;
						break;
					}

					float s_x = start_x + 3.0f * FloatMath.cos(heading);
					float s_y = start_y + 3.0f * FloatMath.sin(heading);

					float stickoffsetx = 6.0f * FloatMath.cos(heading
							- MathUtils.PI / 2.0f);
					float stickoffsety = 6.0f * FloatMath.sin(heading
							- MathUtils.PI / 2.0f);

					float leftx1 = s_x + stickoffsetx;
					float lefty1 = s_y + stickoffsety;
					float leftx2 = leftx1 + 12.0f
							* FloatMath.cos(heading + MathUtils.PI / 6.0f);
					float lefty2 = lefty1 + 12.0f
							* FloatMath.sin(heading + MathUtils.PI / 6.0f);

					float rightx1 = s_x - stickoffsetx;
					float righty1 = s_y - stickoffsety;
					float rightx2 = rightx1 + 12.0f
							* FloatMath.cos(heading - MathUtils.PI / 6.0f);
					float righty2 = righty1 + 12.0f
							* FloatMath.sin(heading - MathUtils.PI / 6.0f);

					Wall result = m_tester.isLineOfSightClear(rightx1, righty1,
							rightx2, righty2);
					Wall result2 = m_tester.isLineOfSightClear(leftx1, lefty1,
							leftx2, lefty2);

					if (result == null && result2 == null) {
						clear = true;
					} else {
						if (result != null) {
							getTurnVector(entity, result, heading);
						} else if (result2 != null) {
							getTurnVector(entity, result2, heading);
						}

						// If we have no offset vector, any direction will do.
						if (m_vec_result.x == 0 && m_vec_result.y == 0) {
							m_vec_result.x = 0.01f;
							m_vec_result.y = 0.01f;
						}

						float otherangle = MathUtils.getAngle(0, 0,
								m_vec_result.x, m_vec_result.y);
						if (MathUtils.normalizeAngle(otherangle, startheading)
								- startheading > 0) {
							heading += .08f;
						} else {
							heading -= .08f;
						}

						tries++;
					}
				}

				entity.setVelocity(entity.getSpeed(), heading);
			}
		}
	}

	private Point getTurnVector(Unit entity, Wall result, float heading) {
		float start_x = entity.getX();
		float start_y = entity.getY();

		m_center_stick.x = 15.0f * FloatMath.cos(heading);
		m_center_stick.y = 15.0f * FloatMath.sin(heading);

		m_wall.x = result.b.x - result.a.x;
		m_wall.y = result.b.y - result.a.y;
		m_wall_to_player.x = start_x - result.a.x;
		m_wall_to_player.y = start_y - result.a.y;

		// Project the center stick onto the wall.
		MathUtils.getProjection(m_wall, m_center_stick, m_vec_result);

		// Normalize the result.
		/*
		 * float len = FloatMath.sqrt(m_vec_result.x * m_vec_result.x +
		 * m_vec_result.y * m_vec_result.y);
		 * 
		 * if (len == 0) { m_vec_result.x = 0; m_vec_result.y = 0; return
		 * m_vec_result; }
		 * 
		 * // Find the projection of the vector from one of the endpoints on the
		 * normal of the wall. float dist = MathUtils.dot(result.norm,
		 * m_wall_to_player);
		 * 
		 * // Get the right length for the resulting vector. float sticklen =
		 * FloatMath.sqrt(15.0f * 15.0f - dist * dist); m_vec_result.x *=
		 * sticklen / len; m_vec_result.y *= sticklen / len;
		 * 
		 * // Add the result vector to the scaled normal. m_vec_result.x +=
		 * result.norm.x * dist * -1.0f; m_vec_result.y += result.norm.y * dist
		 * * -1.0f;
		 */

		return m_vec_result;
	}

	public float getWidth() {
		return BBTHGame.WIDTH;
	}

	public float getHeight() {
		return BBTHGame.HEIGHT;
	}

	public void setUpdateFraction(float fraction) {
		if (fraction < 0) {
			fraction = 0;
		}
		m_fraction_to_update = fraction;
	}
}
