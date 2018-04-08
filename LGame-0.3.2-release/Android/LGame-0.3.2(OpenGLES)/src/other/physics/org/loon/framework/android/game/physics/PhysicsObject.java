package org.loon.framework.android.game.physics;

import java.util.HashMap;

import org.loon.framework.android.game.action.map.Field2D;
import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.geom.Triangle;
import org.loon.framework.android.game.core.geom.Triangle2f;
import org.loon.framework.android.game.core.geom.Vector2f;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.GLLoader;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.graphics.opengl.LTextures;
import org.loon.framework.android.game.core.graphics.opengl.LTexture.Format;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
public class PhysicsObject extends LObject implements PolygonType, ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Vector2f initLocation = new Vector2f(0, 0);

	private int halfWidth, halfHeight;

	private GLColor color;

	private int layer;

	public float density, friction, restitution, alpha;

	public Filter filter;

	protected Body body;

	protected boolean useImage, useMake, useInster, isSensor, visible;

	private int x, y, width, height;

	private BodyDef bodyDef;

	public float angularVelocity;

	public float linearDamping;

	public float angularDamping;

	public boolean allowSleep = true;

	public boolean awake = true;

	public boolean fixedRotation = false;

	public boolean bullet = false;

	public boolean active = true;

	public boolean lockRotate = false;

	public float inertiaScale = 1;

	public int maxRotateCache = 90;

	private float rotation;

	private int polyType = Other;

	private boolean bitmapFilter = false;

	private Object tag;

	private BodyType type;

	private Triangle polyTriangles;

	public static final float DEG = 57.2957795f;

	public static final float RAD = 0.0174532925f;

	private PhysicsListener physicsListener;

	private int screenWidth = (int) LSystem.screenRect.getWidth();

	private int screenHeight = (int) LSystem.screenRect.getHeight();

	private final static HashMap<Integer, Triangle> lazyTriangles = new HashMap<Integer, Triangle>();

	private LTexture texture;

	PhysicsObject(String fileName, BodyType type, int x, int y) {
		this(LTextures.loadTexture(fileName).get(), type, x, y);
	}

	PhysicsObject(LTexture img, BodyType type, int x, int y) {
		init(0, img, type, x, y, img.getWidth(), img.getHeight());
	}

	PhysicsObject(int polyType, BodyType type, int x, int y, int w, int h) {
		init(polyType, null, type, x, y, w, h);
	}

	PhysicsObject(String fileName, Body body) {
		this(LTextures.loadTexture(fileName).get(), body);
	}

	PhysicsObject(LTexture img, Body body) {
		this.body = body;
		this.texture = img;
		this.x = (int) body.getPosition().x;
		this.y = (int) body.getPosition().y;
		this.type = body.getType();
		this.color = GLColor.blue;
		this.polyType = Circle;
		this.filter = new Filter();
		this.bodyDef = new BodyDef();
		this.bodyDef.type = type;
		this.bodyDef.position.x = x;
		this.bodyDef.position.y = y;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
		this.useInster = true;
		this.visible = true;
	}

	private void init(int polyType, LTexture img, BodyType type, int x, int y,
			int w, int h) {
		this.visible = true;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		if (width < 0) {
			width = 1;
		}
		if (height < 0) {
			height = 1;
		}
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
		this.type = type;
		this.color = GLColor.blue;
		this.filter = new Filter();
		this.bodyDef = new BodyDef();
		this.bodyDef.type = type;
		this.bodyDef.position.x = x;
		this.bodyDef.position.y = y;
		this.texture = img;
		if (texture == null) {
			this.polyType = polyType;
			this.useImage = false;
		} else {
			int index = img.hashCode();
			this.polyTriangles = lazyTriangles.get(index);
			if (polyTriangles == null) {
				PhysicsPolygon ppolygon = new PhysicsPolygon(img);
				polyTriangles = ppolygon.getPolygon().getTriangles();
				lazyTriangles.put(index, polyTriangles);
			}
			this.polyType = Other;
			this.useImage = true;
		}

	}

	/**
	 * 设定Body类型（动态、静态、无质量）
	 * 
	 * @param type
	 */
	public void setType(BodyType type) {
		if (body != null) {
			body.setType(type);
		}
	}

	/**
	 * 传递对象运行间隔时间
	 * 
	 * @param elapsedTime
	 */
	public void update(long elapsedTime) {

	}

	/**
	 * 以设定好的参数创建Body
	 * 
	 */
	public void make() {
		synchronized (this) {
			try {
				bodyDef.angularVelocity = angularVelocity;
				bodyDef.linearDamping = linearDamping;
				bodyDef.angularDamping = angularDamping;
				bodyDef.allowSleep = allowSleep;
				bodyDef.allowSleep = awake;
				bodyDef.fixedRotation = fixedRotation;
				bodyDef.bullet = bullet;
				bodyDef.active = active;
				bodyDef.inertiaScale = inertiaScale;
				if (useInster) {
					this.body.setUserData(this);
					this.useMake = true;
					return;
				}
				if (body != null) {
					PhysicsScreen.world.destroyBody(body);
					body = null;
				}
				if (useImage) {
					body = PhysicsScreen.world.createBody(bodyDef);
					float[] triangle = new float[6];

					for (int i = 0; i < polyTriangles.getTriangleCount(); i++) {
						FixtureDef fixtureDef = new FixtureDef();
						PolygonShape polyShape = new PolygonShape();
						float[] pos = polyTriangles.getTrianglePoint(i, 0);
						triangle[0] = pos[0];
						triangle[1] = pos[1];
						pos = polyTriangles.getTrianglePoint(i, 1);
						triangle[2] = pos[0];
						triangle[3] = pos[1];
						pos = polyTriangles.getTrianglePoint(i, 2);
						triangle[4] = pos[0];
						triangle[5] = pos[1];
						polyShape.set(triangle);
						fixtureDef.density = this.density;
						fixtureDef.friction = this.friction;
						fixtureDef.isSensor = this.isSensor;
						fixtureDef.restitution = this.restitution;
						fixtureDef.filter.categoryBits = this.filter.categoryBits;
						fixtureDef.filter.groupIndex = this.filter.groupIndex;
						fixtureDef.filter.maskBits = this.filter.maskBits;
						fixtureDef.friction = this.friction;
						fixtureDef.shape = polyShape;

						body.createFixture(fixtureDef);
						polyShape.dispose();
					}
				} else {
					LImage image = null;
					LGraphics g = null;
					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.density = this.density;
					fixtureDef.friction = this.friction;
					fixtureDef.isSensor = this.isSensor;
					fixtureDef.restitution = this.restitution;
					fixtureDef.filter.categoryBits = this.filter.categoryBits;
					fixtureDef.filter.groupIndex = this.filter.groupIndex;
					fixtureDef.filter.maskBits = this.filter.maskBits;
					fixtureDef.friction = this.friction;
					switch (polyType) {
					case Box:
						if (type == BodyType.StaticBody) {
							createStaticBox(fixtureDef);
						} else if (type == BodyType.DynamicBody) {
							createDynamicBox(fixtureDef);
						} else if (type == BodyType.KinematicBody) {
							createKinematicBox(fixtureDef);
						}
						if (texture == null) {
							image = LImage.createImage(width, height, false);
							g = image.getLGraphics();
							g.setColor(color.getAWTColor());
							g.fillRect(0, 0, width - 1, height - 1);
							g.setColor(LColor.white);
							g.drawRect(0, 0, width - 1, height - 1);
						}
						break;
					case Circle:
						if (type == BodyType.StaticBody) {
							createStaticCircle(fixtureDef);
						} else if (type == BodyType.DynamicBody) {
							createDynamicCircle(fixtureDef);
						} else if (type == BodyType.KinematicBody) {
							createKinematicCircle(fixtureDef);
						}
						if (texture == null) {
							image = LImage.createImage(width, height, true);
							g = image.getLGraphics();
							g.setColor(color.getAWTColor());
							g.fillOval(0, 0, width - 1, height - 1);
							g.setColor(LColor.white);
							g.drawOval(0, 0, width - 1, height - 1);
						}
						break;
					case Triangle2D:
						if (type == BodyType.StaticBody) {
							createStaticTriangle(fixtureDef);
						} else if (type == BodyType.DynamicBody) {
							createDynamicTriangle(fixtureDef);
						} else if (type == BodyType.KinematicBody) {
							createKinematicTriangle(fixtureDef);
						}
						if (texture == null) {
							image = LImage.createImage(width, height, false);
							g = image.getLGraphics();
							Triangle2f t = new Triangle2f();
							t.set(width, width);
							g.setColor(LColor.blue);
							g.fillTriangle(t);
							g.setColor(LColor.white);
							g.drawTriangle(t);

							t = null;
						}
						break;
					}
					if (texture == null && image != null) {
						this.setDrawImage(image);
					}
					if (g != null) {
						g.dispose();
					}
				}
				this.body.setUserData(this);
				this.useMake = true;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("PhysicsObject make "
						+ e.getMessage());
			}
		}
	}

	public void setUserData(Object object) {
		if (body != null) {
			this.body.setUserData(object);
		}
	}

	public Object getUserData() {
		if (body != null) {
			return this.body.getUserData();
		}
		return null;
	}

	public float getDirection() {
		if (body == null) {
			return 0;
		}
		return (float) Math.toDegrees(body.getAngle());
	}

	public void setAngle(float angle) {
		bodyDef.angle = angle * RAD;
		if (body != null) {
			Vector2f v = (Vector2f) body.getPosition().clone();
			body.setTransform(v, bodyDef.angle);
			body.setLinearVelocity(initLocation);
		}
	}

	public void createUI(final GLEx g) {

		if (texture == null) {
			return;
		}

		if (useMake && visible) {

			if (body == null) {
				return;
			}

			while (rotation < 0) {
				rotation += 360;
			}
			while (rotation > 360) {
				rotation -= 360;
			}

			Vector2f v = body.getPosition();

			int rotation = (int) ((body.getAngle() * DEG) % 360);

			x = (int) v.x;
			y = (int) v.y;

			if (!useImage) {
				switch (polyType) {
				case Box:
				case Circle:
					x = (int) x - halfWidth;
					y = (int) y - halfHeight;
					break;
				case Triangle2D:
					break;
				default:
					break;
				}
			}

			if (x < 0) {
				x = 0;
			}
			if (y < 0) {
				y = 0;
			}

			width = texture.getWidth();
			height = texture.getHeight();

			if (x > screenWidth) {
				x = screenWidth - width;
			}
			if (y > screenHeight) {
				y = screenHeight - height;
			}

			if (polyType == Other) {
				float rotate = (float) Math.toRadians(rotation);
				float sinA = (float) Math.sin(rotate);
				float cosA = (float) Math.cos(rotate);
				x = (int) (x - (halfWidth - (halfWidth * cosA - halfHeight
						* sinA)));
				y = (int) (y - (halfHeight - (halfHeight * cosA + halfWidth
						* sinA)));
			}

			if (rotation == 0 || lockRotate) {
				if (alpha > 0 && alpha < 1.0) {
					g.setAlpha(alpha);
					g.drawTexture(texture, x, y, rotation);
					g.setAlpha(1.0F);
				} else {
					g.drawTexture(texture, x, y, rotation);
				}
				return;
			}
			if (alpha > 0 && alpha < 1.0) {
				g.setAlpha(alpha);
				g.drawTexture(texture, x, y, rotation);
				g.setAlpha(1.0F);
			} else {
				g.drawTexture(texture, x, y, rotation);
			}

		}
	}

	public void setDrawImage(LImage img) {
		if (useImage) {
			throw new RuntimeException("Set is not allowed !");
		}
		if (img != null) {
			if (texture != null) {
				texture.destroy();
				texture = null;
			}
			texture = new LTexture(GLLoader.getTextureData(img), Format.LINEAR);
		}
	}

	public void setDrawImage(String fileName) {
		setDrawImage(LImage.createImage(fileName));
	}

	public void setSpeed(float gx, float gy) {
		setSpeed(new Vector2f(gx, gy));
	}

	public void setSpeed(Vector2f speed) {
		if (body == null) {
			return;
		}
		this.body.setLinearVelocity(speed);
	}

	public Vector2f getSpeed() {
		if (body == null) {
			return new Vector2f(0, 0);
		}
		return body.getLinearVelocity();
	}

	public Vector2f getPosition() {
		if (body == null) {
			return new Vector2f(0, 0);
		}
		return body.getPosition();
	}

	public void setDamping(float damping) {
		this.bodyDef.linearDamping = damping;
	}

	public void setRotation(float rotation) {
		if (body == null) {
			return;
		}
		body.setTransform(body.getPosition(), body.getAngle());
	}

	public void setVelocity(float xVelocity, float yVelocity) {
		if (body == null) {
			return;
		}
		Vector2f vel = body.getLinearVelocity();
		vel.x = xVelocity;
		vel.y = yVelocity;
		body.setLinearVelocity(vel);
	}

	public void setAngularVelocity(float vel) {
		if (body == null) {
			return;
		}
		body.setAngularVelocity(vel);
	}

	public void setPosition(float x, float y) {
		setPosition(new Vector2f(x, y));
	}

	public void setPosition(Vector2f position) {
		if (body == null) {
			return;
		}
		body.setTransform(position, 0);
		body.setLinearVelocity(initLocation);
	}

	public Body getBody() {
		if (body == null) {
			return null;
		}
		return body;
	}

	public void reset() {
		setPosition(initLocation);
	}

	public void destroy() {
		if (body == null) {
			return;
		}
		PhysicsScreen.destroyBody(body);
	}

	public void applyforce(Vector2f force) {
		if (body == null) {
			return;
		}
		body.applyForce(force, initLocation);
	}

	public void dispose() {
		if (body != null) {
			PhysicsScreen.world.destroyBody(body);
			body = null;
		}
		useMake = false;
	}

	public LTexture getBitmap() {
		return texture;
	}

	/**
	 * 创建一个指定样式的Body
	 * 
	 * @param bodyDef
	 * @param fixture
	 */
	private void createBody(BodyDef bodyDef, FixtureDef fixture) {
		body = PhysicsScreen.world.createBody(bodyDef);
		body.createFixture(fixture);
		this.bodyDef = bodyDef;
	}

	private void createDynamicBody(FixtureDef fixture) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.x = x + halfWidth;
		bodyDef.position.y = y + halfHeight;
		bodyDef.angle = rotation * RAD;
		bodyDef.angularVelocity = angularVelocity;
		bodyDef.linearDamping = linearDamping;
		bodyDef.angularDamping = angularDamping;
		bodyDef.allowSleep = allowSleep;
		bodyDef.allowSleep = awake;
		bodyDef.fixedRotation = fixedRotation;
		bodyDef.bullet = bullet;
		bodyDef.active = active;
		bodyDef.inertiaScale = inertiaScale;
		createBody(bodyDef, fixture);
	}

	private void createStaticBody(FixtureDef fixture) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.x = x + halfWidth;
		bodyDef.position.y = y + halfHeight;
		bodyDef.angle = rotation * RAD;
		bodyDef.angularVelocity = angularVelocity;
		bodyDef.linearDamping = linearDamping;
		bodyDef.angularDamping = angularDamping;
		bodyDef.allowSleep = allowSleep;
		bodyDef.allowSleep = awake;
		bodyDef.fixedRotation = fixedRotation;
		bodyDef.bullet = bullet;
		bodyDef.active = active;
		bodyDef.inertiaScale = inertiaScale;
		createBody(bodyDef, fixture);
	}

	private void createKinematicBody(FixtureDef fixture) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.x = x + halfWidth;
		bodyDef.position.y = y + halfHeight;
		bodyDef.angle = rotation * RAD;
		bodyDef.angularVelocity = angularVelocity;
		bodyDef.linearDamping = linearDamping;
		bodyDef.angularDamping = angularDamping;
		bodyDef.allowSleep = allowSleep;
		bodyDef.allowSleep = awake;
		bodyDef.fixedRotation = fixedRotation;
		bodyDef.bullet = bullet;
		bodyDef.active = active;
		bodyDef.inertiaScale = inertiaScale;
		createBody(bodyDef, fixture);
	}

	private void createDynamicCircle(FixtureDef fixtureDef) {
		CircleShape circle = new CircleShape();
		circle.setRadius(halfWidth);
		fixtureDef.shape = circle;
		createDynamicBody(fixtureDef);
		circle.dispose();
	}

	private void createStaticCircle(FixtureDef fixtureDef) {
		CircleShape circle = new CircleShape();
		circle.setRadius(halfWidth);
		fixtureDef.shape = circle;
		createStaticBody(fixtureDef);
		circle.dispose();
	}

	private void createKinematicCircle(FixtureDef fixtureDef) {
		CircleShape circle = new CircleShape();
		circle.setRadius(halfWidth);
		fixtureDef.shape = circle;
		createKinematicBody(fixtureDef);
		circle.dispose();
	}

	private void createDynamicBox(FixtureDef fixtureDef) {
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(halfWidth, halfHeight);
		fixtureDef.shape = boxPoly;
		createDynamicBody(fixtureDef);
		boxPoly.dispose();
	}

	private void createStaticBox(FixtureDef fixtureDef) {
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(halfWidth, halfHeight);
		fixtureDef.shape = boxPoly;
		createStaticBody(fixtureDef);
		boxPoly.dispose();
	}

	private void createKinematicBox(FixtureDef fixtureDef) {
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(halfWidth, halfHeight);
		fixtureDef.shape = boxPoly;
		createKinematicBody(fixtureDef);
		boxPoly.dispose();
	}

	private void createDynamicTriangle(FixtureDef fixtureDef) {
		PolygonShape tPoly = new PolygonShape();
		Triangle2f t = new Triangle2f();
		t.set(width, width);
		tPoly.set(t.getVertexs());
		fixtureDef.shape = tPoly;
		bodyDef.angle = rotation * RAD;
		createBody(bodyDef, fixtureDef);
		tPoly.dispose();
	}

	private void createStaticTriangle(FixtureDef fixtureDef) {
		PolygonShape tPoly = new PolygonShape();
		Triangle2f t = new Triangle2f();
		t.set(width, width);
		tPoly.set(t.getVertexs());
		fixtureDef.shape = tPoly;
		bodyDef.angle = rotation * RAD;
		createBody(bodyDef, fixtureDef);
		tPoly.dispose();
	}

	private void createKinematicTriangle(FixtureDef fixtureDef) {
		PolygonShape tPoly = new PolygonShape();
		Triangle2f t = new Triangle2f();
		t.set(width, width);
		tPoly.set(t.getVertexs());
		fixtureDef.shape = tPoly;
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.angle = rotation * RAD;
		createBody(bodyDef, fixtureDef);
		tPoly.dispose();
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}

	public float getRestitution() {
		return restitution;
	}

	public void setRestitution(float restitution) {
		this.restitution = restitution;
	}

	public GLColor getColor() {
		return color;
	}

	public void setColor(GLColor color) {
		this.color = color;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public void onCollision() {
		if (physicsListener != null) {
			physicsListener.onCollision();
		}
	}

	public boolean getIsSensor() {
		return isSensor;
	}

	public void setIsSensor(boolean isSensor) {
		this.isSensor = isSensor;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public RectBox getCollisionBox() {
		return getRect(x, y, width, height);
	}

	public int getHeight() {
		return height;
	}

	public int getLayer() {
		return layer;
	}

	public int getWidth() {
		return width;
	}

	public float getX() {
		Vector2f v = getPosition();
		return v.x;
	}

	public float getY() {
		Vector2f v = getPosition();
		return v.y;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int x() {
		Vector2f v = getPosition();
		return (int) v.x;
	}

	public int y() {
		Vector2f v = getPosition();
		return (int) v.y;
	}

	public void move(float x, float y) {
		move(new Vector2f(x, y));
	}

	public void move(Vector2f vector2) {
		Vector2f v = getPosition();
		float x = v.x + vector2.x();
		float y = v.y + vector2.y();
		setPosition(new Vector2f(x, y));
	}

	public void move_multiples(int direction, int multiples) {
		if (multiples <= 0) {
			multiples = 1;
		}
		Vector2f v = Field2D.getDirection(direction);
		move(v.x() * multiples, v.y() * multiples);
	}

	public void move_45D_up() {
		move_45D_up(1);
	}

	public void move_45D_up(int multiples) {
		move_multiples(Field2D.UP, multiples);
	}

	public void move_45D_left() {
		move_45D_left(1);
	}

	public void move_45D_left(int multiples) {
		move_multiples(Field2D.LEFT, multiples);
	}

	public void move_45D_right() {
		move_45D_right(1);
	}

	public void move_45D_right(int multiples) {
		move_multiples(Field2D.RIGHT, multiples);
	}

	public void move_45D_down() {
		move_45D_down(1);
	}

	public void move_45D_down(int multiples) {
		move_multiples(Field2D.DOWN, multiples);
	}

	public void move_up() {
		move_up(1);
	}

	public void move_up(int multiples) {
		move_multiples(Field2D.TUP, multiples);
	}

	public void move_left() {
		move_left(1);
	}

	public void move_left(int multiples) {
		move_multiples(Field2D.TLEFT, multiples);
	}

	public void move_right() {
		move_right(1);
	}

	public void move_right(int multiples) {
		move_multiples(Field2D.TRIGHT, multiples);
	}

	public void move_down() {
		move_down(1);
	}

	public void move_down(int multiples) {
		move_multiples(Field2D.TDOWN, multiples);
	}

	public PhysicsListener getPhysicsListener() {
		return physicsListener;
	}

	public void setPhysicsListener(PhysicsListener physicsListener) {
		this.physicsListener = physicsListener;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public boolean isBitmapFilter() {
		return bitmapFilter;
	}

	public void setBitmapFilter(boolean bitmapFilter) {
		this.bitmapFilter = bitmapFilter;
	}

	public int getPolyType() {
		return polyType;
	}

	public void setPolyType(int polyType) {
		this.polyType = polyType;
	}

}
