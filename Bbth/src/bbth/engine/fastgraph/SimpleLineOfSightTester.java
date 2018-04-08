package bbth.engine.fastgraph;

import java.util.ArrayList;

import bbth.engine.util.Point;

public class SimpleLineOfSightTester extends LineOfSightTester {

	private ArrayList<Wall> m_walls;
	public float radius;

	public SimpleLineOfSightTester(float f, ArrayList<Wall> walls) {
		radius = f;
		m_walls = walls;
	}

	@Override
	public Wall isLineOfSightClear(Point start, Point end) {
		return isLineOfSightClear(start.x, start.y, end.x, end.y);
	}

	@Override
	public Wall isLineOfSightClear(float startx, float starty, float endx,
			float endy) {
		float lineX = endx - startx;
		float lineY = endy - starty;

		// Loop over walls
		for (int i = 0, wallCount = m_walls.size(); i < wallCount; i++) {
			Wall wall = m_walls.get(i);
			float wallX = wall.b.x - wall.a.x;
			float wallY = wall.b.y - wall.a.y;

			// Intersect each line segment with the other infinite line
			float divide = wallX * lineY - wallY * lineX;
			float t_line_on_wall = ((startx - wall.a.x) * wallY - (starty - wall.a.y)
					* wallX)
					/ divide;
			float t_wall_on_line = ((wall.a.x - startx) * lineY - (wall.a.y - starty)
					* lineX)
					/ -divide;

			// Line of sight isn't clear if they overlap
			float padding = radius * 0.95f / wall.length;
			if (t_line_on_wall >= 0 && t_line_on_wall <= 1
					&& t_wall_on_line >= 0 - padding
					&& t_wall_on_line <= 1 + padding) {
				return wall;
			}
		}

		return null;
	}
}
