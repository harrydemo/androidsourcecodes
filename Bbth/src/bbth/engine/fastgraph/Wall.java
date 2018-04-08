package bbth.engine.fastgraph;

import java.util.ArrayList;

import bbth.engine.net.simulation.Hash;
import bbth.engine.util.MathUtils;
import bbth.engine.util.Point;

public class Wall {

	public Point a;
	public Point b;
	public float length;
	public Point norm;
	private int hashCodeID;
	private static int nextHashCodeID;

	public Wall(Point a, Point b) {
		hashCodeID = nextHashCodeID++;
		norm = new Point();
		this.a = a;
		this.b = b;
		updateLength();
	}

	public static void resetNextHashCodeID() {
		nextHashCodeID = 0;
	}

	public Wall(float ax, float ay, float bx, float by) {
		norm = new Point();
		a = new Point(ax, ay);
		b = new Point(bx, by);
		updateLength();
	}

	public void updateLength() {
		length = MathUtils.getDist(a.x, a.y, b.x, b.y);
		float dx = b.x - a.x;
		float dy = b.y - a.y;
		norm.x = -dy / length;
		norm.y = dx / length;
	}

	public void addPoints(ArrayList<Point> points, float radius) {
		float dx = (b.x - a.x) * radius / length;
		float dy = (b.y - a.y) * radius / length;
		points.add(new Point(a.x - dx - dy, a.y - dy + dx));
		points.add(new Point(a.x - dx + dy, a.y - dy - dx));
		points.add(new Point(b.x + dx - dy, b.y + dy + dx));
		points.add(new Point(b.x + dx + dy, b.y + dy - dx));
	}

	public float getMinX() {
		return Math.min(a.x, b.x);
	}

	public float getMinY() {
		return Math.min(a.y, b.y);
	}

	public float getMaxX() {
		return Math.max(a.x, b.x);
	}

	public float getMaxY() {
		return Math.max(a.y, b.y);
	}

	public int getSimulationSyncHash() {
		int hash = 0;
		hash = Hash.mix(hash, a.x);
		hash = Hash.mix(hash, a.y);
		hash = Hash.mix(hash, b.x);
		hash = Hash.mix(hash, b.y);
		hash = Hash.mix(hash, length);
		hash = Hash.mix(hash, norm.x);
		hash = Hash.mix(hash, norm.y);
		return 0;
	}

	@Override
	public int hashCode() {
		return hashCodeID;
	}
}
