package bbth.engine.ai;

import bbth.engine.util.Point;

public interface Heuristic {
	public int estimateHScore(Point start, Point goal);
}
