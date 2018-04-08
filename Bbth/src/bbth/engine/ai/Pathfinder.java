package bbth.engine.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import bbth.engine.util.Point;

public class Pathfinder {
	ConnectedGraph m_graph;
	HashMap<Point, AStarEntry> m_entries;
	HashSet<Point> m_closed_set;
	HashMap<Point, Point> m_came_from;
	PriorityQueue<AStarEntry> m_open_set;
	private ArrayList<Point> m_found_path;

	public Pathfinder(ConnectedGraph cg) {
		m_graph = cg;

		m_entries = new HashMap<Point, AStarEntry>();
		for (Point p : cg.getGraph().keySet()) {
			m_entries.put(p, new AStarEntry(p));
		}

		m_closed_set = new HashSet<Point>();
		m_came_from = new HashMap<Point, Point>();
		m_open_set = new PriorityQueue<AStarEntry>();
		m_found_path = new ArrayList<Point>();
	}

	public boolean findPath(Point start, Point goal) {
		return findPath(start, goal, null, 1.0f);
	}

	public boolean findPath(Point start, Point goal, Heuristic se) {
		return findPath(start, goal, se, 1.0f);
	}

	public boolean findPath(Point start, Point goal, Heuristic se,
			float tolerance) {
		m_closed_set.clear();
		m_came_from.clear();
		m_open_set.clear();
		m_found_path.clear();

		// Reset all the scores.
		for (Point p : m_graph.getGraph().keySet()) {
			if (!m_entries.containsKey(p)) {
				m_entries.put(p, new AStarEntry(p));
			} else {
				AStarEntry e = m_entries.get(p);
				e.m_f_score = Integer.MAX_VALUE;
				e.m_g_score = Integer.MAX_VALUE;
				e.m_h_score = Integer.MAX_VALUE;
			}
		}

		if (!m_graph.contains(start)) {
			return false;
		}

		// Initialize open set.
		AStarEntry startentry = m_entries.get(start);
		startentry.m_g_score = 0;
		startentry.m_h_score = estimateHScore(se, startentry, goal);
		startentry.m_f_score = m_entries.get(start).m_h_score;

		m_open_set.offer(startentry);

		float tolerancesqr = tolerance * tolerance;

		while (m_open_set.size() != 0) {
			AStarEntry current = m_open_set.poll();

			if (getDistSqr(current, goal) < tolerancesqr) {
				// We're done.
				reconstructPath(current.m_point);
				return true;
			}

			m_closed_set.add(current.m_point);

			// Find neighbors.
			ArrayList<Point> neighbors = m_graph.getNeighbors(current.m_point);
			if (neighbors == null) {
				continue;
			}

			int size = neighbors.size();
			for (int i = 0; i < size; i++) {
				Point neighbor = neighbors.get(i);
				if (m_closed_set.contains(neighbor)) {
					continue;
				}

				int tentative_g_score = current.m_g_score
						+ (int) getDistSqr(current, neighbor);

				AStarEntry nentry = m_entries.get(neighbor);

				// Check if this is a better score for this neighbor. If so,
				// update it.
				if (nentry.m_g_score > tentative_g_score) {
					nentry.m_g_score = tentative_g_score;
					nentry.m_h_score = estimateHScore(se, nentry, goal);
					nentry.m_f_score = nentry.m_g_score + nentry.m_h_score;

					m_open_set.remove(nentry);
					m_open_set.offer(nentry);
					m_came_from.put(neighbor, current.m_point);
				}
			}
		}

		return false;
	}

	private void reconstructPath(Point current) {
		if (m_came_from.containsKey(current)) {
			reconstructPath(m_came_from.get(current));
		}
		m_found_path.add(current);
	}

	public ArrayList<Point> getPath() {
		return m_found_path;
	}

	private int estimateHScore(Heuristic se, AStarEntry start, Point goal) {
		if (se == null) {
			return (int) getDistSqr(start, goal);
		} else {
			return se.estimateHScore(start.m_point, goal);
		}
	}

	private float getDistSqr(AStarEntry current, Point goal) {
		float dx = current.m_point.x - goal.x;
		float dy = current.m_point.y - goal.y;
		return dx * dx + dy * dy;
	}

	public void clearPath() {
		m_found_path.clear();
	}
}
