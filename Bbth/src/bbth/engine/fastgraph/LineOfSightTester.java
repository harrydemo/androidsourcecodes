package bbth.engine.fastgraph;

import bbth.engine.util.Point;

public abstract class LineOfSightTester {

	/**
	 * Does the line segment not intersect any wall line segments?
	 */
	public abstract Wall isLineOfSightClear(Point start, Point end);

	public abstract Wall isLineOfSightClear(float startx, float starty,
			float endx, float endy);

}
