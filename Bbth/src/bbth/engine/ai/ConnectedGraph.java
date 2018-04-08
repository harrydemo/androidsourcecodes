package bbth.engine.ai;

import java.util.ArrayList;
import java.util.HashMap;

import bbth.engine.util.Point;

public class ConnectedGraph {
	public HashMap<Point, ArrayList<Point>> m_connections;

	public ConnectedGraph() {
		m_connections = new HashMap<Point, ArrayList<Point>>();
	}

	public void addConnection(Point p, Point p2) {
		if (!m_connections.containsKey(p)) {
			m_connections.put(p, new ArrayList<Point>());
		}
		if (!m_connections.containsKey(p2)) {
			m_connections.put(p2, new ArrayList<Point>());
		}
		ArrayList<Point> list = m_connections.get(p);
		list.add(p2);
	}

	public void addConnection(float x, float y, float x2, float y2) {
		Point key = getPointAtCoords(x, y);
		Point value = getPointAtCoords(x2, y2);

		if (key == null) {
			key = new Point(x, y);
			m_connections.put(key, new ArrayList<Point>());
		}

		ArrayList<Point> list = m_connections.get(key);

		if (value == null) {
			value = new Point(x2, y2);
		}
		list.add(value);

		if (!m_connections.containsKey(value)) {
			m_connections.put(value, new ArrayList<Point>());
		}
	}

	public void removeConnection(Point p, Point p2) {
		ArrayList<Point> list = m_connections.get(p);
		list.remove(p2);
	}

	public void removeConnection(float x, float y, float x2, float y2) {
		Point key = getPointAtCoords(x, y);
		Point value = getPointAtCoords(x2, y2);

		if (key == null) {
			return;
		}

		ArrayList<Point> list = m_connections.get(key);

		if (value == null) {
			return;
		}
		list.remove(value);
	}

	public HashMap<Point, ArrayList<Point>> getGraph() {
		return m_connections;
	}

	public ArrayList<Point> getNeighbors(Point start) {
		return m_connections.get(start);
	}

	public ArrayList<Point> getNeighbors(float startx, float starty) {
		Point point = getPointAtCoords(startx, starty);
		if (point == null) {
			return null;
		}
		return m_connections.get(point);
	}

	public boolean contains(Point p) {
		return m_connections.containsKey(p);
	}

	public Point getPointAtCoords(float x, float y) {
		return getPointAtCoords(x, y, 0.05f, 0.05f);
	}

	public Point getPointAtCoords(float x, float y, float xtolerance,
			float ytolerance) {
		for (Point p : m_connections.keySet()) {
			if (Math.abs(p.x - x) < xtolerance
					&& Math.abs(p.y - y) < ytolerance) {
				return p;
			}
		}

		return null;
	}

	public Point getClosestNode(float x, float y) {
		float bestdist = 0;
		Point closest = null;
		for (Point p : m_connections.keySet()) {
			float dist = (p.x - x) * (p.x - x) + (p.y - y) * (p.y - y);
			if (closest == null || dist < bestdist) {
				closest = p;
				bestdist = dist;
			}
		}
		return closest;
	}
}
