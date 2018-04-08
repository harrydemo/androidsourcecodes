package bbth.game.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import bbth.engine.ai.ConnectedGraph;
import bbth.engine.ai.FlockRulesCalculator;
import bbth.engine.ai.Pathfinder;
import bbth.engine.fastgraph.LineOfSightTester;
import bbth.engine.util.MathUtils;
import bbth.engine.util.Point;
import bbth.game.GridAcceleration;
import bbth.game.Team;
import bbth.game.units.Unit;

public abstract class UnitAI {

	/**
	 * The radius of the axis-aligned bounding box in which to search for nearby
	 * enemies.
	 */
	private static final float SEARCH_RADIUS = 50;

	// Cached hash set of units to avoid allocations
	private final HashSet<Unit> cachedUnitSet = new HashSet<Unit>();

	// private Point m_result;
	private float m_max_vel_change = 0.5f;
	private float m_objective_weighting = 0.05f;
	private float m_max_vel = 35.0f;

	protected Pathfinder m_pathfinder;
	protected ConnectedGraph m_map_grid;
	protected LineOfSightTester m_tester;
	protected GridAcceleration m_accel;

	public UnitAI() {
		// m_result = new Point();
	}

	public abstract void update(Unit entity, AIController c,
			FlockRulesCalculator flock);

	protected void calculateFlocking(Unit entity, AIController c,
			FlockRulesCalculator flock, Point result) {
		// flocking, we will miss you but you're just too slow T_T
		result.x = 0;
		result.y = 0;
		/*
		 * // Calculate flocking. flock.getCohesionComponent(entity, m_result);
		 * 
		 * result.x = m_result.x * .2f / c.getWidth(); result.y = m_result.y *
		 * .2f / c.getHeight();
		 * 
		 * flock.getAlignmentComponent(entity, m_result);
		 * 
		 * result.x += m_result.x; result.y += m_result.y;
		 * 
		 * flock.getSeparationComponent(entity, 20.0f, m_result);
		 * 
		 * result.x += m_result.x; result.y += m_result.y;
		 * 
		 * result.x /= 3; result.y /= 3;
		 */
		return;
	}

	public void setMaxVelChange(float m_max_vel_change) {
		if (m_max_vel_change < 0) {
			m_max_vel_change = 0;
		}
		this.m_max_vel_change = m_max_vel_change;
	}

	public float getMaxVelChange() {
		return m_max_vel_change;
	}

	public void setObjectiveWeighting(float m_objective_weighting) {
		this.m_objective_weighting = m_objective_weighting;
	}

	public float getObjectiveWeighting() {
		return m_objective_weighting;
	}

	public void setMaxVel(float m_max_vel) {
		if (m_max_vel < 0) {
			m_max_vel = 0;
		}
		this.m_max_vel = m_max_vel;
	}

	public float getMaxVel() {
		return m_max_vel;
	}

	public final void setPathfinder(Pathfinder pathfinder,
			ConnectedGraph graph, LineOfSightTester tester,
			GridAcceleration accel) {
		m_pathfinder = pathfinder;
		m_map_grid = graph;
		m_tester = tester;
		m_accel = accel;
	}

	protected final Unit getClosestEnemy(Unit entity) {
		// Get nearby units
		float x = entity.getX();
		float y = entity.getY();
		cachedUnitSet.clear();
		m_accel.getUnitsInAABB(x - SEARCH_RADIUS, y - SEARCH_RADIUS, x
				+ SEARCH_RADIUS, y + SEARCH_RADIUS, cachedUnitSet);

		// Find the closest unit ignoring units on the same team
		float bestdistSqr = SEARCH_RADIUS * SEARCH_RADIUS;
		Unit bestunit = null;
		Team oppositeTeam = entity.getTeam().getOppositeTeam();
		for (Unit unit : cachedUnitSet) {
			if (unit.getTeam() == oppositeTeam) {
				float enemydist = MathUtils.getDistSqr(entity.getX(),
						entity.getY(), unit.getX(), unit.getY());
				if (enemydist < bestdistSqr) {
					bestunit = unit;
					bestdistSqr = enemydist;
				}
			}
		}
		return bestunit;
	}

	protected final Point getClosestNode(Point s) {
		// TODO: speed this up
		float bestdist = 0;
		Point closest = null;
		HashMap<Point, ArrayList<Point>> connections = m_map_grid.getGraph();
		for (Point p : connections.keySet()) {
			float dist = MathUtils.getDistSqr(p.x, p.y, s.x, s.y);
			if ((closest == null || dist < bestdist)
					&& m_tester.isLineOfSightClear(s, p) == null) {
				closest = p;
				bestdist = dist;
			}
		}
		return closest;
	}
}
