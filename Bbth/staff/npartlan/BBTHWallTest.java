package npartlan;

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
import bbth.engine.fastgraph.LineOfSightTester;
import bbth.engine.fastgraph.SimpleLineOfSightTester;
import bbth.engine.fastgraph.Wall;
import bbth.engine.util.Bag;
import bbth.engine.util.MathUtils;
import bbth.engine.util.Point;
import bbth.game.BBTHGame;
import bbth.game.Team;
import bbth.game.ai.AIController;
import bbth.game.units.DefendingUnit;
import bbth.game.units.Unit;
import bbth.game.units.UnitManager;

public class BBTHWallTest extends GameScreen implements UnitManager {
	
	Unit m_entity;
	
	private Paint m_paint_0;
	private Paint m_paint_1;
	private Paint m_paint_2;
	private Paint m_paint_3;
	private Random m_rand;
	private BBTHGame m_parent;
	private Pathfinder m_pathfinder;
	private FastGraphGenerator m_graph_gen;
	private LineOfSightTester m_tester;
	
	Point m_center_stick;
	Point m_wall;
	Point m_wall_to_player;
	Point m_vec_result;
	
	float wall_start_x;
	float wall_start_y;
		
	//******** SETUP FOR AI *******//
	private AIController m_controller;
	//******** SETUP FOR AI *******//
	
	long m_last_time = 0;

	private int m_timestep = 0;

	private long m_time_since_step;
	
	public BBTHWallTest(BBTHGame bbthGame) {
		m_parent = bbthGame;
    	m_rand = new Random();
    	
    	m_center_stick = new Point();
		m_wall = new Point();
		m_wall_to_player = new Point();
		m_vec_result = new Point();
						
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
		m_controller.setPathfinder(m_pathfinder, m_graph_gen.graph, m_tester, null);

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
            	
		m_entity = new DefendingUnit(this, Team.SERVER, m_paint_1, null); // this may crash
		m_entity.setTeam(Team.SERVER);
		m_entity.setPosition(0, 100);
		m_entity.setPosition(m_rand.nextFloat() * m_parent.getWidth()/4, m_rand.nextFloat() * m_parent.getHeight());
		m_entity.setVelocity(10f, m_rand.nextFloat() * MathUtils.TWO_PI);
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
		m_controller.update();
		//******** SETUP FOR AI *******//
		
		Unit ent = m_entity;
		
		Unit entity = m_entity;
		
		float start_x = entity.getX();
		float start_y = entity.getY();
		// Check if we are going to run into a wall:
		float heading = entity.getHeading();
		float startheading = heading;
		boolean clear = false;
		int tries = 0;
		while (!clear) {
			if (tries > 100) {
				heading = startheading + MathUtils.PI;
				break;
			}
			
			float s_x = start_x + 3.0f * FloatMath.cos(heading);
			float s_y = start_y + 3.0f * FloatMath.sin(heading);
			
			float stickoffsetx = 6.0f * FloatMath.cos(heading - MathUtils.PI/2.0f);
			float stickoffsety = 6.0f * FloatMath.sin(heading - MathUtils.PI/2.0f);
			
			float leftx1 = s_x + stickoffsetx;
			float lefty1 = s_y + stickoffsety;
			float leftx2 = leftx1 + 12.0f * FloatMath.cos(heading + MathUtils.PI/6.0f);
			float lefty2 = lefty1 + 12.0f * FloatMath.sin(heading + MathUtils.PI/6.0f);
			
			float rightx1 = s_x - stickoffsetx;
			float righty1 = s_y - stickoffsety;
			float rightx2 = rightx1 + 12.0f * FloatMath.cos(heading - MathUtils.PI/6.0f);
			float righty2 = righty1 + 12.0f * FloatMath.sin(heading - MathUtils.PI/6.0f);
			
			Wall result = m_tester.isLineOfSightClear(rightx1, righty1, rightx2, righty2);
			Wall result2 = m_tester.isLineOfSightClear(leftx1, lefty1, leftx2, lefty2);
			
			if (result == null && result2 == null) {
				clear = true;
			} else {
				if (result != null) {
					getTurnVector(entity, result, heading);
				} else if (result2 != null) {
					getTurnVector(entity, result2, heading);
				}
				
				//canvas.drawLine(start_x, start_y, start_x + m_vec_result.x, start_y + m_vec_result.y, m_paint_3);
										
				if (m_vec_result.x == 0 && m_vec_result.y == 0) {
					clear = true;
				} else {
					float otherangle = MathUtils.getAngle(0, 0, m_vec_result.x, m_vec_result.y);
					if (MathUtils.normalizeAngle(otherangle, startheading) - startheading > 0) {
						heading += .08f;
					} else {
						heading -= .08f;
					}
					//canvas.drawLine(start_x, start_y, start_x + 15 * FloatMath.cos(heading), start_y + 15 * FloatMath.sin(heading), m_paint_1);
				}
				
				tries++;
			}
		}
		
		entity.setVelocity(entity.getSpeed(), heading);
		
		//******** PHYSICS AFTER AI *******//
		ent.setPosition(ent.getX() + ent.getSpeed() * FloatMath.cos(ent.getHeading()) * timediff/1000.0f, ent.getY() + ent.getSpeed() * FloatMath.sin(ent.getHeading()) * timediff/1000.0f);
		//******** PHYSICS AFTER AI *******//
		
		ent.drawChassis(canvas);
		
		int size = m_graph_gen.walls.size();
		for (int i = 0; i < size; i++) {
			Wall w = m_graph_gen.walls.get(i);
			canvas.drawLine(w.a.x, w.a.y, w.b.x, w.b.y, m_paint_2);
		}
		
		m_last_time = curr_time;
	}
	
	private Point getTurnVector(Unit entity, Wall result, float heading) {
		float start_x = entity.getX();
		float start_y = entity.getY();
		
		m_center_stick.x = 15.0f * FloatMath.cos(heading);
		m_center_stick.y = 15.0f * FloatMath.sin(heading);
		
		m_wall.x = result.b.x - result.a.x;
		m_wall.y = result.b.y - result.a.y;
		m_wall_to_player.x = start_x - result.a.x;
		m_wall_to_player.y = start_y - result.a.y;
		
		// Project the center stick onto the wall.
		MathUtils.getProjection(m_wall, m_center_stick, m_vec_result);
		
		// Normalize the result.
		float len = FloatMath.sqrt(m_vec_result.x * m_vec_result.x + m_vec_result.y * m_vec_result.y);
		
		if (len == 0) {
			m_vec_result.x = 0;
			m_vec_result.y = 0;
			return m_vec_result;
		}
		
		// Find the projection of the vector from one of the endpoints on the normal of the wall.
		float dist = MathUtils.dot(result.norm, m_wall_to_player);
		
		// Get the right length for the resulting vector.
		float sticklen = FloatMath.sqrt(15.0f * 15.0f - dist * dist);
		m_vec_result.x *= sticklen / len;
		m_vec_result.y *= sticklen / len;
		
		// Add the result vector to the scaled normal.
		m_vec_result.x += result.norm.x * dist * -1.0f;
		m_vec_result.y += result.norm.y * dist * -1.0f;
		
		return m_vec_result;
	}
	
	public void addWall(Wall w) {
		m_graph_gen.walls.add(w);
		m_graph_gen.compute();
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
		// DO NOTHING
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
