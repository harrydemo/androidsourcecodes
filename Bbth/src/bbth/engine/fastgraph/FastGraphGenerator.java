package bbth.engine.fastgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.processing.wiki.triangulate.Edge;
import org.processing.wiki.triangulate.Triangle;
import org.processing.wiki.triangulate.Triangulate;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import bbth.engine.ai.ConnectedGraph;
import bbth.engine.util.Point;

/**
 * Generates a minimal graph around a collection of walls. Given an enemy
 * radius, it places four graph nodes at the corners of the bounding box of each
 * wall (each wall is 2*radius across).
 * 
 * This generates a graph that is fast to query. The generation itself may not
 * be that fast.
 */
public class FastGraphGenerator {

	public final ConnectedGraph graph = new ConnectedGraph();
	public final ArrayList<Wall> walls = new ArrayList<Wall>();
	public float radius;
	private static final Paint paint = new Paint();

	public FastGraphGenerator(float largestEntityRadius, float width,
			float height) {
		radius = largestEntityRadius;
		walls.add(new Wall(0, 0, 0, height));
		walls.add(new Wall(width, 0, width, height));
		walls.add(new Wall(0, 0, width, 0));
		walls.add(new Wall(0, height, width, height));
	}

	public void compute() {
		// Triangulate the points into triangles
		ArrayList<Point> points = new ArrayList<Point>();
		for (int i = 0, n = walls.size(); i < n; i++) {
			walls.get(i).addPoints(points, radius);
		}
		ArrayList<Triangle> triangles = Triangulate.triangulate(points);

		// Transform the triangles into edges
		HashSet<Edge> edges = new HashSet<Edge>();
		for (int i = 0, n = triangles.size(); i < n; i++) {
			Triangle tri = triangles.get(i);
			edges.add(new Edge(tri.p1, tri.p2));
			edges.add(new Edge(tri.p2, tri.p3));
			edges.add(new Edge(tri.p3, tri.p1));
		}

		// Remove edges that cross walls (order matters here for allocations)
		Iterator<Edge> iter = edges.iterator();
		while (iter.hasNext()) {
			Edge edge = iter.next();

			float edgeX = edge.p2.x - edge.p1.x;
			float edgeY = edge.p2.y - edge.p1.y;

			// Loop over walls
			for (int i = 0, wallCount = walls.size(); i < wallCount; i++) {
				Wall wall = walls.get(i);
				float wallX = wall.b.x - wall.a.x;
				float wallY = wall.b.y - wall.a.y;

				// For each edge, intersect the line segment with the other
				// infinite line
				float divide = wallX * edgeY - wallY * edgeX;
				float t_edge_on_wall = ((edge.p1.x - wall.a.x) * wallY - (edge.p1.y - wall.a.y)
						* wallX)
						/ divide;
				float t_wall_on_edge = ((wall.a.x - edge.p1.x) * edgeY - (wall.a.y - edge.p1.y)
						* edgeX)
						/ -divide;

				// If they overlap (expanding the wall out to include the entity
				// radius), remove the edge
				float padding = radius * 0.95f / wall.length;
				if (t_edge_on_wall >= 0 && t_edge_on_wall <= 1
						&& t_wall_on_edge >= 0 - padding
						&& t_wall_on_edge <= 1 + padding) {
					iter.remove();
					break;
				}
			}
		}

		// Generate the graph
		graph.m_connections.clear();
		iter = edges.iterator();
		while (iter.hasNext()) {
			Edge edge = iter.next();
			graph.addConnection(edge.p1, edge.p2);
			graph.addConnection(edge.p2, edge.p1);
		}
	}

	public void draw(Canvas canvas) {
		paint.setAntiAlias(true);
		paint.setColor(Color.GREEN);
		for (Point point : graph.m_connections.keySet()) {
			ArrayList<Point> neighbors = graph.m_connections.get(point);
			for (Point neighbor : neighbors) {
				canvas.drawLine(point.x, point.y, neighbor.x, neighbor.y, paint);
			}
		}
	}
}
