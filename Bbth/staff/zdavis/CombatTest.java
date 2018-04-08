package zdavis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.util.FloatMath;
import bbth.engine.ai.Pathfinder;
import bbth.engine.fastgraph.FastGraphGenerator;
import bbth.engine.fastgraph.LineOfSightTester;
import bbth.engine.fastgraph.SimpleLineOfSightTester;
import bbth.engine.fastgraph.Wall;
import bbth.engine.particles.ParticleSystem;
import bbth.engine.ui.UIView;
import bbth.engine.util.Bag;
import bbth.engine.util.MathUtils;
import bbth.engine.util.Point;
import bbth.game.BBTHGame;
import bbth.game.GridAcceleration;
import bbth.game.Team;
import bbth.game.ai.AIController;
import bbth.game.units.Unit;
import bbth.game.units.UnitManager;
import bbth.game.units.UnitType;

public class CombatTest extends UIView implements UnitManager {
	
	Bag<Unit> units;
	
	private Paint bluePaint;
	private Paint redPaint;
	private Paint whitePaint;
	private Paint greenPaint;
	private Random m_rand;
	private BBTHGame m_parent;
	private Pathfinder m_pathfinder;
	private FastGraphGenerator m_graph_gen;
	private LineOfSightTester m_tester;
	private GridAcceleration accel = new GridAcceleration(BBTHGame.WIDTH, BBTHGame.HEIGHT, BBTHGame.WIDTH / 10);
	private ParticleSystem particleSystem = new ParticleSystem(1000);
	
	float wall_start_x;
	float wall_start_y;
		
	//******** SETUP FOR AI *******//
	private AIController m_controller;
	//******** SETUP FOR AI *******//
	
	public CombatTest(BBTHGame bbthGame) {
		m_parent = bbthGame;
    	m_rand = new Random();
						
		m_graph_gen = new FastGraphGenerator(15.0f, BBTHGame.WIDTH, BBTHGame.HEIGHT);
		m_pathfinder = new Pathfinder(m_graph_gen.graph);
		
		m_tester = new SimpleLineOfSightTester(15.0f, m_graph_gen.walls);
		
		for (int i = 0; i < 2; i++) {
			int length = m_rand.nextInt(100) + 30;
			int start_x = (int) (m_rand.nextInt((int) (BBTHGame.WIDTH * .75f)) + BBTHGame.WIDTH * .25f);
			int start_y = (int) (m_rand.nextInt((int) (BBTHGame.HEIGHT * .75f)) + BBTHGame.HEIGHT * .25f);
			float dir = m_rand.nextFloat() * MathUtils.PI;
			addWall(new Wall(start_x, start_y, start_x + length * FloatMath.cos(dir), start_y + length * FloatMath.sin(dir)));
		}
		
		//******** SETUP FOR AI *******//
		m_controller = new AIController();
		//******** SETUP FOR AI *******//
		m_controller.setPathfinder(m_pathfinder, m_graph_gen.graph, m_tester, accel);
		m_controller.setUpdateFraction(.1f);

		bluePaint = new Paint();
		bluePaint.setColor(Color.BLUE);
		bluePaint.setStrokeWidth(2.0f);
		bluePaint.setStrokeJoin(Join.ROUND);
		bluePaint.setStyle(Style.STROKE);
		bluePaint.setTextSize(20);
		bluePaint.setAntiAlias(true);
		
		redPaint = new Paint();
		redPaint.setColor(Color.RED);
		redPaint.setStrokeWidth(2.0f);
		redPaint.setStrokeJoin(Join.ROUND);
		redPaint.setStyle(Style.STROKE);
		redPaint.setTextSize(20);
		redPaint.setAntiAlias(true);
		
		whitePaint = new Paint();
		whitePaint.setColor(Color.WHITE);
		whitePaint.setStrokeWidth(2.0f);
		whitePaint.setStrokeJoin(Join.ROUND);
		whitePaint.setStyle(Style.STROKE);
		whitePaint.setTextSize(20);
		whitePaint.setAntiAlias(true);
		
		greenPaint = new Paint();
		greenPaint.setColor(Color.GREEN);
		greenPaint.setStrokeWidth(1.0f);
		greenPaint.setStrokeJoin(Join.ROUND);
		greenPaint.setStyle(Style.STROKE);
		greenPaint.setTextSize(20);
		greenPaint.setAntiAlias(true);
        
    	units = new Bag<Unit>();
        randomizeEntities();
	}
	
	private void spawnUnit(UnitType unitType, Team team) {
		Unit unit = unitType.createUnit(this, team, team == Team.SERVER ? redPaint : bluePaint, particleSystem);
		
		switch (team) {
		case SERVER:
			unit.setPosition(m_rand.nextFloat() * BBTHGame.WIDTH, m_rand.nextFloat() * BBTHGame.HEIGHT/4);
			break;
		case CLIENT:
			unit.setPosition(m_rand.nextFloat() * BBTHGame.WIDTH, m_rand.nextFloat() * BBTHGame.HEIGHT/4 + m_parent.getWidth()*.75f);
			break;
		default:
			throw new RuntimeException();
		}
		
		unit.setVelocity(m_rand.nextFloat() * .01f, m_rand.nextFloat() * MathUtils.TWO_PI);
		
		units.add(unit);
		
		//******** SETUP FOR AI *******//
		m_controller.addEntity(unit);
		//******** SETUP FOR AI *******//
	}
	
	private void randomizeEntities() {
		for (int i = 0; i < 20; i++) {
			spawnUnit(UnitType.ATTACKING, Team.SERVER);
			spawnUnit(UnitType.ATTACKING, Team.CLIENT);
		}
		
//		for (int i = 0; i < 10; i++) {
//			spawnUnit(UnitType.DEFENDING, Team.SERVER);
//			spawnUnit(UnitType.DEFENDING, Team.CLIENT);
//		}
		
		spawnUnit(UnitType.UBER, Team.SERVER);
		spawnUnit(UnitType.UBER, Team.SERVER);
		spawnUnit(UnitType.UBER, Team.CLIENT);
		spawnUnit(UnitType.UBER, Team.CLIENT);
	}
	
	Bag<Unit> unitsToRemove = new Bag<Unit>();
	@Override
	public void onUpdate(float seconds) {
		m_controller.update();
		accel.clearUnits();
		accel.insertUnits(m_controller.getUnits());
		
		for (int i=0; i < units.size(); ++i) {
			Unit unit = units.get(i);
			unit.update(seconds);
		}
		
		while (!unitsToRemove.isEmpty()) {
			Unit toRemove = unitsToRemove.removeLast();
			units.remove(toRemove);
			spawnUnit(toRemove.getType(), toRemove.getTeam());
		}
		
		particleSystem.tick(seconds);
	}

	@Override
	public void onDraw(Canvas canvas) {
		for (int i = 0; i < units.size(); i++) {
			Unit ent = units.get(i);
			ent.drawChassis(canvas);
		}
		for (int i = 0; i < units.size(); i++) {
			Unit ent = units.get(i);
			ent.drawEffects(canvas);
		}
		particleSystem.draw(canvas, greenPaint);
		for (int i = 0; i < units.size(); i++) {
			Unit ent = units.get(i);
			ent.drawHealthBar(canvas, false);
		}
		
		int size = m_graph_gen.walls.size();
		for (int i = 0; i < size; i++) {
			Wall w = m_graph_gen.walls.get(i);
			canvas.drawLine(w.a.x, w.a.y, w.b.x, w.b.y, whitePaint);
		}
	}
	
	public void addWall(Wall w) {
		m_graph_gen.walls.add(w);
		m_graph_gen.compute();
	}

	private Point getClosestNode(Point s) {
		float bestdist = 0;
		Point closest = null;
		HashMap<Point, ArrayList<Point>> connections = m_graph_gen.graph.getGraph();
		for (Point p : connections.keySet()) {
			float dist = MathUtils.getDistSqr(p.x, p.y, s.x, s.y);
			if ((closest == null || dist < bestdist) && m_tester.isLineOfSightClear(s, p) == null) {
				closest = p;
				bestdist = dist;
			}
		}
		return closest;
	}
	
	@Override
	public void onTouchDown(float x, float y) {
		wall_start_x = x;
		wall_start_y = y;
	}
	
	@Override
	public void onTouchUp(float x, float y) {
		addWall(new Wall(wall_start_x, wall_start_y, x, y));
	}

	@Override
	public void notifyUnitDead(Unit unit) {
		units.remove(unit);
		m_controller.removeEntity(unit);
		unitsToRemove.add(unit);
	}

	Bag<Unit> temp = new Bag<Unit>();
	@Override
	public Bag<Unit> getUnitsInCircle(float x, float y, float r) {
		temp.clear();
		for (Unit unit : units) {
			float xDelta = x - unit.getX();
			float yDelta = y - unit.getY();
			float rSum = r+unit.getRadius();
			if (xDelta*xDelta + yDelta*yDelta <= rSum*rSum) {
				temp.add(unit);
			}
		}
		return temp;
	}

	private static final boolean intervalsDontOverlap(float min1, float max1, float min2, float max2) {
		return (min1 < min2 ? min2 - max1 : min1 - max2) > 0;
	}
	
	@Override
	public Bag<Unit> getUnitsIntersectingLine(float x, float y, float x2, float y2) {
		temp.clear();
		
		// calculate axis vector
		float axisX = -(y2 - y);
		float axisY = x2 - x;
		// normalize axis vector
		float axisLen = FloatMath.sqrt(axisX*axisX + axisY*axisY);
		axisX /= axisLen;
		axisY /= axisLen;
		
		float lMin = axisX*x + axisY*y;
		float lMax = axisX*x2 + axisY*y2;
		if (lMax < lMin) {
			float temp = lMin;
			lMin = lMax;
			lMax = temp;
		}
		
		for (Unit unit : units) {
			// calculate projections
			float projectedCenter = axisX*unit.getX() + axisY*unit.getY();
			float radius = unit.getRadius();
			if (!intervalsDontOverlap(projectedCenter - radius, projectedCenter + radius, lMin, lMax)) {
				temp.add(unit);
			}
		}
		
		return temp;
	}

	@Override
	public void removeWall(Wall wall) {
		m_graph_gen.walls.remove(wall);
		m_graph_gen.compute();
	}
}
