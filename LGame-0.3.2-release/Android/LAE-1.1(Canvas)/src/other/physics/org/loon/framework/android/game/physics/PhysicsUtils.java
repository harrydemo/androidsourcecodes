package org.loon.framework.android.game.physics;

import org.loon.framework.android.game.core.geom.Triangle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Copyright 2008 - 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class PhysicsUtils {

	/**
	 * 创建一个圆形形状
	 * 
	 * @param width
	 * @return
	 */
	public static CircleShape createCircleShape(int width) {
		CircleShape circle = new CircleShape();
		circle.setRadius(width / 2f);
		return circle;
	}

	/**
	 * 创建一个方形形状
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static PolygonShape createBoxShape(int width, int height) {
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width / 2f, height / 2f);
		return poly;
	}

	/**
	 * 创建一个三角形形状
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static PolygonShape createTriangleShape(int width, int height) {
		PolygonShape poly = new PolygonShape();
		Triangle triangle = new Triangle();
		triangle.set(0, 0, width, height);
		poly.set(triangle.getVertexs());
		return poly;
	}

	/**
	 * 生成指定内容的FixtureDef
	 * 
	 * @param density
	 * @param restitution
	 * @param friction
	 * @return
	 */
	public static FixtureDef createFixtureDef(float density, float restitution,
			float friction) {
		return createFixtureDef(density, restitution, friction, false);
	}

	/**
	 * 生成指定内容的FixtureDef
	 * 
	 * @param density
	 * @param restitution
	 * @param friction
	 * @param isSensor
	 * @return
	 */
	public static FixtureDef createFixtureDef(float density, float restitution,
			float friction, boolean isSensor) {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.restitution = restitution;
		fixtureDef.friction = friction;
		fixtureDef.isSensor = isSensor;
		return fixtureDef;
	}

	/**
	 * 生成指定内容的方形Body
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param bodyType
	 * @param fixtureDef
	 * @return
	 */
	public static Body createBoxBody(World world, int x, int y, int w, int h,
			BodyType bodyType, FixtureDef fixtureDef) {
		return createBoxBody(world, x, y, w, h, bodyType, fixtureDef, 1);
	}

	/**
	 * 生成指定内容的方形Body
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param bodyType
	 * @param fixtureDef
	 * @param offset
	 * @return
	 */
	public static Body createBoxBody(World world, int x, int y, int w, int h,
			BodyType bodyType, FixtureDef fixtureDef, float offset) {
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyType;

		boxBodyDef.position.x = x / (float) offset;
		boxBodyDef.position.y = y / (float) offset;

		Body boxBody = world.createBody(boxBodyDef);
		PolygonShape boxPoly = new PolygonShape();

		boxPoly.setAsBox(w, h);
		fixtureDef.shape = boxPoly;
		boxBody.createFixture(fixtureDef);
		boxPoly.dispose();

		return boxBody;
	}

	/**
	 * 生成指定内容的多边形Body
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param bodyType
	 * @param fixtureDef
	 * @return
	 */
	public static Body createPolygonBody(World world, int x, int y, int w,
			int h, Vector2[] vertices, BodyType bodyType, FixtureDef fixtureDef) {
		return createPolygonBody(world, x, y, w, h, vertices, bodyType,
				fixtureDef, 1);
	}

	/**
	 * 生成指定内容的多边形Body
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param bodyType
	 * @param fixtureDef
	 * @param offset
	 * @return
	 */
	public static Body createPolygonBody(World world, int x, int y, int w,
			int h, Vector2[] vertices, BodyType bodyType,
			FixtureDef fixtureDef, float offset) {
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyType;

		boxBodyDef.position.x = x / (float) offset;
		boxBodyDef.position.y = y / (float) offset;

		Body boxBody = world.createBody(boxBodyDef);
		PolygonShape boxPoly = new PolygonShape();

		boxPoly.set(vertices);

		fixtureDef.shape = boxPoly;
		boxBody.createFixture(fixtureDef);
		boxPoly.dispose();

		return boxBody;
	}

	/**
	 * 生成指定内容的三角形Body
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param bodyType
	 * @param fixtureDef
	 * @return
	 */
	public static Body createTriangleBody(World world, int x, int y, int w,
			int h, BodyType bodyType, FixtureDef fixtureDef) {

		float halfWidth = w / 2;
		float halfHeight = h / 2;

		float top = -halfHeight;
		float bottom = halfHeight;
		float left = -halfHeight;
		float center = 0;
		float right = halfWidth;

		Vector2[] vertices = { new Vector2(center, top),
				new Vector2(right, bottom), new Vector2(left, bottom) };

		return createPolygonBody(world, x, y, w, h, vertices, bodyType,
				fixtureDef, 1);
	}

	/**
	 * 生成指定内容的多边形Body
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param bodyType
	 * @param fixtureDef
	 * @return
	 */
	public static Body createHexagonBody(World world, int x, int y, int w,
			int h, BodyType bodyType, FixtureDef fixtureDef) {
		float halfWidth = w / 2;
		float halfHeight = h / 2;

		float top = -halfHeight;
		float bottom = halfHeight;
		float centerX = 0;

		float left = -halfWidth + 2.5f;
		float right = halfWidth - 2.5f;
		float higher = top + 8.25f;
		float lower = bottom - 8.25f;

		Vector2[] vertices = { new Vector2(centerX, top),
				new Vector2(right, higher), new Vector2(right, lower),
				new Vector2(centerX, bottom), new Vector2(left, lower),
				new Vector2(left, higher) };

		return createPolygonBody(world, x, y, w, h, vertices, bodyType,
				fixtureDef, 1);
	}
}
