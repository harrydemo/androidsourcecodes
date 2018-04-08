package org.loon.game.test.physics;

import org.loon.framework.android.game.core.geom.Vector2f;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.input.LTouch;
import org.loon.framework.android.game.core.timer.LTimerContext;
import org.loon.framework.android.game.physics.PhysicsObject;
import org.loon.framework.android.game.physics.PhysicsScreen;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;


public class Test1  extends PhysicsScreen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Test1() {
		// x轴重力为0,y轴重力为0.2,重力自然衰弱（不循环）
		super(0, 0.2f, false);
	}

	/**
	 * 创建物理线桥
	 * 
	 * @param imageName
	 */
	private void createBridge(String fileName) {
		FixtureDef fixDef = new FixtureDef();
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(6f, 2f);
		fixDef.density = 6f;
		fixDef.friction = 0.2f;
		fixDef.shape = poly;
		// 构建关节以联动桥板
		RevoluteJointDef rjd = new RevoluteJointDef();
		int numPlanks = 22;
		Body prevBody = null;
		Body firstBody = null;
		for (int i = 0; i < numPlanks; i++) {
			BodyDef bd = new BodyDef();
			if (i == 0 || i == numPlanks - 1) {
				bd.type = BodyType.StaticBody;
			}
			bd.position.set(3f + (15f * i), 320);
			Body body = world.createBody(bd);
			body.createFixture(fixDef);

			// 绑定指定路径下图片与Body
			PhysicsObject o = bindTo(fileName, body);
			// 不允许图片旋转
			o.lockRotate = true;
			o.make();

			if (prevBody == null) {
				firstBody = body;
			} else {
				Vector2f anchor = new Vector2f(2.4f + (15f * i), 320);
				rjd.initialize(prevBody, body, anchor);
				world.createJoint(rjd);
			}
			prevBody = body;
		}
		Vector2f anchor = new Vector2f(2.4f + (15f * numPlanks), 320);
		// 初始化关节
		rjd.initialize(prevBody, firstBody, anchor);
		world.createJoint(rjd);
	}

	public void onLoaded() {
		setPhysicsFPS(40);
		// 初始化完毕后创建物理桥
		createBridge("assets/bridge.png");
		// 导入一张图片为物理精灵（LGame可自动获得精灵图片的物理边界）
		PhysicsObject o = bindTo("assets/a4.png", BodyType.DynamicBody, 50, 0);
		o.density = 2.0f;
		o.friction = 0.2f;
		o.make();
	}

	public void paint(GLEx g) {

	}

	public void update(LTimerContext t) {

	}

	public void onDown(LTouch e) {
		// 获得指定位置,且标记为"Ball"的物理精灵
		PhysicsObject ball = find(getTouchX(), getTouchY(), "Ball");
		if (ball != null) {
			// 存在则删除
			removeObject(ball);
		} else {
			// 添加一个圆形的物理精灵,到触摸屏点击区域，大小为64x64
			ball = bindTo(Circle, BodyType.DynamicBody, getTouchX(),
					getTouchY(), 64, 64);
			// 标记为Ball（便于查找）
			ball.setTag("Ball");
			ball.setDrawImage("assets/ball.png");
			// 设定图片(请注意，当为固定形状注入图片时，图片大小必须为设定大小，
			// 否则会发生图片位置与物理位置不匹配的情况)
			ball.setBitmapFilter(true);
			ball.density = 2.0f;
			ball.friction = 0.2f;
			ball.make();

		}
	}

	public void onMove(LTouch e) {

	}

	public void onUp(LTouch e) {

	}

	
}
