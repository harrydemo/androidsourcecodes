package npartlan;

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
import bbth.engine.core.GameScreen;
import bbth.engine.fastgraph.FastGraphGenerator;
import bbth.engine.fastgraph.SimpleLineOfSightTester;
import bbth.engine.fastgraph.Wall;
import bbth.engine.particles.ParticleSystem;
import bbth.engine.util.Bag;
import bbth.engine.util.MathUtils;
import bbth.engine.util.Point;
import bbth.game.BBTHGame;
import bbth.game.GridAcceleration;
import bbth.game.Team;
import bbth.game.ai.AIController;
import bbth.game.units.DefendingUnit;
import bbth.game.units.Unit;
import bbth.game.units.UnitManager;

public class BBTHAITest extends GameScreen implements UnitManager {
	
	ArrayList<Unit> m_entities;
	
	private Paint m_paint_0;
	private Paint m_paint_1;
	private Paint m_paint_2;
	private Paint m_paint_3;
	private Random m_rand;
	private BBTHGame m_parent;
	private Pathfinder m_pathfinder;
	private FastGraphGenerator m_graph_gen;
	private SimpleLineOfSightTester m_tester;
	
	float wall_start_x;
	float wall_start_y;
		
	//******** SETUP FOR AI *******//
	private AIController m_controller;
	//******** SETUP FOR AI *******//
	
	long m_last_time = 0;

	private int m_timestep = 0;

	private long m_time_since_step;

	private GridAcceleration m_accel;
	
	public BBTHAITest(BBTHGame bbthGame) {
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
		m_accel = new GridAcceleration(BBTHGame.WIDTH, BBTHGame.HEIGHT, BBTHGame.WIDTH / 10);
		m_accel.insertWalls(m_graph_gen.walls);
		//******** SETUP FOR AI *******//
		m_controller.setPathfinder(m_pathfinder, m_graph_gen.graph, m_tester, m_accel);

		m_last_time = System.currentTimeMillis();
		
		m_paint_0 = new Paint();
		m_paint_0.setColor(Color.BLUE);
		m_paint_0.setStrokeWidth(2.0f);
		m_paint_0.setStrokeJoin(Join.ROUND);
		m_paint_0.setStyle(Style.STROKE);
		m_paint_0.setTextSize(20);
		m_paint_0.setAntiAlias(true);
		
		m_paint_1 = new Paint();
		m_paint_1.setColor(Color.RED);
		m_paint_1.setStrokeWidth(2.0f);
		m_paint_1.setStrokeJoin(Join.ROUND);
		m_paint_1.setStyle(Style.STROKE);
		m_paint_1.setTextSize(20);
		m_paint_1.setAntiAlias(true);
		
		m_paint_2 = new Paint();
		m_paint_2.setColor(Color.WHITE);
		m_paint_2.setStrokeWidth(2.0f);
		m_paint_2.setStrokeJoin(Join.ROUND);
		m_paint_2.setStyle(Style.STROKE);
		m_paint_2.setTextSize(20);
		m_paint_2.setAntiAlias(true);
		
		m_paint_3 = new Paint();
		m_paint_3.setColor(Color.GREEN);
		m_paint_3.setStrokeWidth(1.0f);
		m_paint_3.setStrokeJoin(Join.ROUND);
		m_paint_3.setStyle(Style.STROKE);
		m_paint_3.setTextSize(20);
		m_paint_3.setAntiAlias(true);
        
    	m_entities = new ArrayList<Unit>();
    	
        randomizeEntities();
	}
	
	ParticleSystem particles;
	
	private void randomizeEntities() {
		for (int i = 0; i < 10; i++) {
			Unit e = new DefendingUnit(this, Team.SERVER, m_paint_1, particles);
			e.setTeam(Team.SERVER);
			e.setPosition(0, 100);
			e.setPosition(m_rand.nextFloat() * m_parent.getWidth()/4, m_rand.nextFloat() * m_parent.getHeight());
			e.setVelocity(m_rand.nextFloat() * .01f, m_rand.nextFloat() * MathUtils.TWO_PI);
			m_entities.add(e);
			
			//******** SETUP FOR AI *******//
			m_controller.addEntity(e);
			//******** SETUP FOR AI *******//
		}
		
		for (int i = 0; i < 10; i++) {
			Unit e = new DefendingUnit(this, Team.CLIENT, m_paint_0, particles);
			e.setTeam(Team.CLIENT);
			e.setPosition(BBTHGame.WIDTH, 100);
			e.setPosition(m_rand.nextFloat() * m_parent.getWidth()/4 + m_parent.getWidth()*.75f, m_rand.nextFloat() * m_parent.getHeight());
			e.setVelocity(m_rand.nextFloat() * .01f, m_rand.nextFloat() * MathUtils.TWO_PI);
			m_entities.add(e);
			
			//******** SETUP FOR AI *******//
			m_controller.addEntity(e);
			//******** SETUP FOR AI *******//
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		long curr_time = System.currentTimeMillis();
		long timediff = curr_time - m_last_time;
		
		m_time_since_step += timediff;
		if (m_time_since_step >= 17) {
			m_time_since_step = m_time_since_step-17;
			m_timestep++;
		}
		
		//******** SETUP FOR AI *******//
		m_accel.clearUnits();
		m_accel.insertUnits(m_controller.getUnits());
		m_controller.update();
		//******** SETUP FOR AI *******//
		
		// Draw the pathfinding connections graph
		/*for (Point point : m_graph_gen.graph.m_connections.keySet()) {
			ArrayList<Point> neighbors = m_graph_gen.graph.m_connections.get(point);
			for (Point neighbor : neighbors) {
				canvas.drawLine(point.x, point.y, neighbor.x, neighbor.y, m_paint_3);
			}
		}*/
		
		// Draw the path found
		/*Unit entity = m_entities.get(0);
		Unit enemy = m_entities.get(1);
		Point start_point = new Point();
		Point end_point = new Point();
		float start_x = entity.getX();
		float start_y = entity.getY();
		float goal_x = enemy.getX();
		float goal_y = enemy.getY();
		start_point.set(start_x, start_y);
		end_point.set(goal_x, goal_y);
		
		if (!m_tester.isLineOfSightClear(start_point, end_point) && m_pathfinder != null) {
			Point start = getClosestNode(start_point);
			canvas.drawCircle(start.x, start.y, 3.0f, m_paint_3);
			Point end = getClosestNode(end_point);
			
			ArrayList<Point> path = null;
			
			if (start != null && end != null) {
				m_pathfinder.clearPath();
				m_pathfinder.findPath(start, end);
			}
			
			path = m_pathfinder.getPath();
			
			// TODO: avoid new-ing points each time here.
			path.add(end_point);
			
			if (path.size() > 1) {
				if (m_tester.isLineOfSightClear(start_point, path.get(1))) {
					path.remove(0);
				}
				
				canvas.drawLine(start_x, start_y, path.get(0).x, path.get(0).y, m_paint_0);
				
				int size = path.size();
				for (int i = 0; i < size-1; i++) {
					Point curr = path.get(i);
					Point next = path.get(i+1);
					canvas.drawLine(curr.x, curr.y, next.x, next.y, m_paint_1);
				}
			}
			
			//System.out.println("Team: " + entity.getTeam() + " Start: " + entity.getX() + ", " + entity.getY() + " = " + start.x + ", " + start.y + " End: " + end.x + ", " + end.y);
		} else {
			canvas.drawLine(start_x, start_y, goal_x, goal_y, m_paint_1);
		}*/
		
		for (int i = 0; i < m_entities.size(); i++) {
			Unit ent = m_entities.get(i);
			
			//******** PHYSICS AFTER AI *******//
			ent.setPosition(ent.getX() + ent.getSpeed() * FloatMath.cos(ent.getHeading()) * timediff/1000.0f, ent.getY() + ent.getSpeed() * FloatMath.sin(ent.getHeading()) * timediff/1000.0f);
			//******** PHYSICS AFTER AI *******//
			
			// Draw the stick-based obstacle avoidance
			/*
			float heading = ent.getHeading();
			float start_x = ent.getX() + 3.0f * FloatMath.cos(heading);
			float start_y = ent.getY() + 3.0f * FloatMath.sin(heading);
			float stickoffsetx = 6.0f * FloatMath.cos(heading - MathUtils.PI/2.0f);
			float stickoffsety = 6.0f * FloatMath.sin(heading - MathUtils.PI/2.0f);
			
			float leftx1 = start_x + stickoffsetx;
			float lefty1 = start_y + stickoffsety;
			float leftx2 = leftx1 + 12.0f * FloatMath.cos(heading + MathUtils.PI/6.0f);
			float lefty2 = lefty1 + 12.0f * FloatMath.sin(heading + MathUtils.PI/6.0f);
			
			float rightx1 = start_x - stickoffsetx;
			float righty1 = start_y - stickoffsety;
			float rightx2 = rightx1 + 12.0f * FloatMath.cos(heading - MathUtils.PI/6.0f);
			float righty2 = righty1 + 12.0f * FloatMath.sin(heading - MathUtils.PI/6.0f);
			
			canvas.drawLine(leftx1, lefty1, leftx2, lefty2, m_paint_3);
			canvas.drawLine(rightx1, righty1, rightx2, righty2, m_paint_3);
			*/
			
			ent.drawChassis(canvas);
		}
		for (int i = 0; i < m_entities.size(); i++) {
			Unit ent = m_entities.get(i);
			ent.drawEffects(canvas);
		}
		
		int size = m_graph_gen.walls.size();
		for (int i = 0; i < size; i++) {
			Wall w = m_graph_gen.walls.get(i);
			canvas.drawLine(w.a.x, w.a.y, w.b.x, w.b.y, m_paint_2);
		}
		
		m_last_time = curr_time;
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
		m_controller.removeEntity(unit);
		m_entities.remove(unit);
	}

	@Override
	public Bag<Unit> getUnitsInCircle(float x, float y, float r) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bag<Unit> getUnitsIntersectingLine(float x, float y, float x2,
			float y2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeWall(Wall wall) {
		// TODO Auto-generated method stub
		
	}
}
