package edwallac;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import bbth.engine.core.GameScreen;
import bbth.engine.fastgraph.FastGraphGenerator;
import bbth.engine.fastgraph.Wall;
import bbth.engine.util.Point;
import bbth.game.BBTHGame;

public class TriangulationTestScreen extends GameScreen {

	private FastGraphGenerator gen = new FastGraphGenerator(10, BBTHGame.WIDTH, BBTHGame.HEIGHT);
	private Paint paint = new Paint();

	public TriangulationTestScreen() {
		paint.setAntiAlias(true);
		gen.walls.add(new Wall(40, 40, 60, 70));
		gen.walls.add(new Wall(100, 150, 150, 100));
		gen.walls.add(new Wall(200, 140, 210, 50));
		gen.walls.add(new Wall(170, 100, 250, 110));
		gen.compute();
	}

	@Override
	public void onDraw(Canvas canvas) {
		paint.setColor(Color.RED);
		for (Wall wall : gen.walls) {
			canvas.drawLine(wall.a.x, wall.a.y, wall.b.x, wall.b.y, paint);
		}

		paint.setColor(Color.GREEN);
		for (Point point : gen.graph.m_connections.keySet()) {
			ArrayList<Point> neighbors = gen.graph.m_connections.get(point);
			for (Point neighbor : neighbors) {
				canvas.drawLine(point.x, point.y, neighbor.x, neighbor.y, paint);
			}
		}
	}
}
