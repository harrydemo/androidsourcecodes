package bbth.game;

import static bbth.game.BBTHGame.HEIGHT;
import static bbth.game.BBTHGame.WIDTH;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Region;
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
import bbth.game.ai.AIController;
import bbth.game.units.Unit;
import bbth.game.units.UnitManager;
import bbth.game.units.UnitType;

public class InfiniteCombatView extends UIView implements UnitManager {
	ArrayList<Unit> units = new ArrayList<Unit>();
	
	private Paint bluePaint = new Paint();
	{
		bluePaint.setColor(Color.BLUE);
		bluePaint.setStrokeWidth(2.0f);
		bluePaint.setStrokeJoin(Join.ROUND);
		bluePaint.setStyle(Style.STROKE);
		bluePaint.setTextSize(20);
		bluePaint.setAntiAlias(true);
	}
	private Paint redPaint = new Paint();
	{
		redPaint.setColor(Color.RED);
		redPaint.setStrokeWidth(2.0f);
		redPaint.setStrokeJoin(Join.ROUND);
		redPaint.setStyle(Style.STROKE);
		redPaint.setTextSize(20);
		redPaint.setAntiAlias(true);
	}
	private Paint whitePaint = new Paint();
	{
		whitePaint.setColor(Color.WHITE);
		whitePaint.setStrokeWidth(2.0f);
		whitePaint.setStrokeJoin(Join.ROUND);
		whitePaint.setStyle(Style.STROKE);
		whitePaint.setTextSize(20);
		whitePaint.setAntiAlias(true);
	}
	private Paint particlePaint = new Paint();
	
	Random random = new Random();
	ParticleSystem particleSystem = new ParticleSystem(1000);
	
	FastGraphGenerator graphGenerator = new FastGraphGenerator(15.0f, WIDTH, HEIGHT);
	Pathfinder pathfinder = new Pathfinder(graphGenerator.graph);
	LineOfSightTester tester = new SimpleLineOfSightTester(15.0f, graphGenerator.walls);
	GridAcceleration gridAcceleration = new GridAcceleration(WIDTH, HEIGHT, WIDTH / 10);
	AIController controller = new AIController();
	{
		controller.setPathfinder(pathfinder, graphGenerator.graph, tester, gridAcceleration);
		controller.setUpdateFraction(.1f);
	}
	
	public InfiniteCombatView() {
		
		setSize(WIDTH, HEIGHT); 
		
		for (int i = 0; i < 15; i++) {
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
		controller.update();
		gridAcceleration.clearUnits();
		gridAcceleration.insertUnits(controller.getUnits());
		
		for (int i=0; i < units.size(); ++i) {
			Unit unit = units.get(i);
			unit.update(seconds);
		}
		
		while (!unitsToRemove.isEmpty()) {
			Unit toRemove = unitsToRemove.removeLast();
			units.remove(toRemove);
			spawnUnit(toRemove.getType(), toRemove.getTeam());
		}
		
		gridAcceleration.clearUnits();
		gridAcceleration.insertUnits(units);
		
		particleSystem.tick(seconds);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.save();
		canvas.clipRect(_rect, Region.Op.INTERSECT);
		for (int i = 0; i < units.size(); i++) {
			Unit ent = units.get(i);
			ent.drawChassis(canvas);
		}
		for (int i = 0; i < units.size(); i++) {
			Unit ent = units.get(i);
			ent.drawEffects(canvas);
		}
		particleSystem.draw(canvas, particlePaint);
		for (int i = 0; i < units.size(); i++) {
			Unit ent = units.get(i);
			ent.drawHealthBar(canvas, false);
		}
		
//		int size = graphGenerator.walls.size();
//		for (int i = 0; i < size; i++) {
//			Wall w = graphGenerator.walls.get(i);
//			canvas.drawLine(w.a.x, w.a.y, w.b.x, w.b.y, whitePaint);
//		}
		canvas.restore();
	}
	
	private void spawnUnit(UnitType unitType, Team team) {
		Unit unit = unitType.createUnit(this, team, team == Team.SERVER ? redPaint : bluePaint, particleSystem);
		
		switch (team) {
		case SERVER:
			unit.setPosition(random.nextFloat() * WIDTH, random.nextFloat() * HEIGHT/4);
			break;
		case CLIENT:
			unit.setPosition(random.nextFloat() * WIDTH, random.nextFloat() * HEIGHT/4 + HEIGHT*.75f);
			break;
		default:
			throw new RuntimeException("what"); //$NON-NLS-1$
		}
		
		unit.setVelocity(random.nextFloat() * .01f, random.nextFloat() * MathUtils.TWO_PI);
		units.add(unit);
		controller.addEntity(unit);
	}
	
	public void addWall(Wall w) {
		graphGenerator.walls.add(w);
		graphGenerator.compute();
		gridAcceleration.clearWalls();
		gridAcceleration.insertWalls(graphGenerator.walls);
	}
	
	@Override
	public void removeWall(Wall wall) {
		graphGenerator.walls.remove(wall);
		graphGenerator.compute();
		gridAcceleration.clearWalls();
		gridAcceleration.insertWalls(graphGenerator.walls);
	}

	@Override
	public void notifyUnitDead(Unit unit) {
		units.remove(unit);
		controller.removeEntity(unit);
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
	
}
