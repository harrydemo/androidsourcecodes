package org.loon.framework.javase.game.physics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.loon.framework.javase.game.action.map.Field2D;
import org.loon.framework.javase.game.action.sprite.ISprite;
import org.loon.framework.javase.game.action.sprite.SpriteImage;
import org.loon.framework.javase.game.action.sprite.SpriteRotate;
import org.loon.framework.javase.game.action.sprite.SpriteRotateSheet;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.geom.Triangle2D;
import org.loon.framework.javase.game.core.geom.Vector2D;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.utils.GraphicsUtils;

import com.badlogic.gdx.math.Vector2;
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
public class PhysicsObject implements PolygonType, ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Vector2 initLocation = new Vector2(0, 0);

	private int halfWidth, halfHeight;

	private Color color;

	private int layer;

	public float density, friction, restitution, alpha;

	public Filter filter;

	private Body body;

	private RectBox rect;

	private boolean useImage, useMake, useInster, isSensor, visible;

	private int x, y, width, height;

	private BodyDef bodyDef;

	public float angularVelocity;

	public float linearDamping;

	public float angularDamping;

	public int maxRotateCache = 90;

	public boolean allowSleep = true;

	public boolean awake = true;

	public boolean fixedRotation = false;

	public boolean bullet = false;

	public boolean active = true;

	public boolean lockRotate = false;

	public float inertiaScale = 1;

	private float rotation;

	private int polyType = Other;

	private boolean bitmapFilter;

	private Object tag;

	private BodyType type;

	private Triangle2D[] polyTriangles;

	public static final float DEG = 57.2957795f;

	public static final float RAD = 0.0174532925f;

	private boolean supportRotateSheet,
			supportSheet = LSystem.DEFAULT_ROTATE_CACHE;

	private SpriteRotateSheet sheet;

	private PhysicsListener physicsListener;

	private SpriteRotate spriteRotate;

	private static HashMap<Integer, Triangle2D[]> lazyTriangles = new HashMap<Integer, Triangle2D[]>();

	private static HashMap<Integer, SpriteRotateSheet> lazySheets = new HashMap<Integer, SpriteRotateSheet>();

	private static final AffineTransform atform = new AffineTransform();

	private int screenWidth = LSystem.screenRect.width;

	private int screenHeight = LSystem.screenRect.height;

	PhysicsObject(ISprite spr, BodyType type) {
		this.init(0, 3, spr.getBitmap().getBufferedImage(), type, spr.x(), spr.y(),
				spr.getWidth(), spr.getHeight());
	}

	PhysicsObject(ISprite spr, int polyInterval, BodyType type) {
		this.init(0, polyInterval, spr.getBitmap().getBufferedImage(), type, spr.x(), spr.y(), spr
				.getWidth(), spr.getHeight());
	}

	PhysicsObject(String fileName, BodyType type, int x, int y) {
		this(GraphicsUtils.loadImage(fileName), 3, type, x, y);
	}

	PhysicsObject(String fileName, int polyInterval, BodyType type, int x, int y) {
		this(GraphicsUtils.loadImage(fileName), polyInterval, type, x, y);
	}

	PhysicsObject(LImage img, int polyInterval, BodyType type, int x, int y) {
		this(img.getBufferedImage(), polyInterval, type, x, y);
	}

	PhysicsObject(LImage img, BodyType type, int x, int y) {
		this(img.getBufferedImage(), 3, type, x, y);
	}

	PhysicsObject(Image img, int polyInterval, BodyType type, int x, int y) {
		init(0, polyInterval, img, type, x, y, img.getWidth(null), img
				.getHeight(null));
	}

	PhysicsObject(Image img, BodyType type, int x, int y) {
		init(0, 3, img, type, x, y, img.getWidth(null), img.getHeight(null));
	}

	PhysicsObject(int polyType, int polyInterval, BodyType type, int x, int y,
			int w, int h) {
		init(polyType, polyInterval, null, type, x, y, w, h);
	}

	PhysicsObject(int polyType, BodyType type, int x, int y, int w, int h) {
		init(polyType, 3, null, type, x, y, w, h);
	}

	public PhysicsObject(String fileName, Body body) {
		this(GraphicsUtils.loadBufferedImage(fileName), body);
	}

	public PhysicsObject(BufferedImage image, Body body) {
		this.body = body;
		this.spriteRotate = new SpriteRotate(image, image.getWidth(), image
				.getHeight(), 0);
		this.x = (int) body.getPosition().x;
		this.y = (int) body.getPosition().y;
		this.type = body.getType();
		this.color = Color.blue;
		this.polyType = Circle;
		this.filter = new Filter();
		this.bodyDef = new BodyDef();
		this.bodyDef.type = type;
		this.bodyDef.position.x = x;
		this.bodyDef.position.y = y;
		this.width = spriteRotate.getWidth();
		this.height = spriteRotate.getHeight();
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
		this.useInster = true;
		this.visible = true;
	}

	private void init(int polyType, int polyInterval, Image img, BodyType type,
			int x, int y, int w, int h) {
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
		this.color = Color.blue;
		this.filter = new Filter();
		this.bodyDef = new BodyDef();
		this.bodyDef.type = type;
		this.bodyDef.position.x = x;
		this.bodyDef.position.y = y;
		if (img == null) {
			this.polyType = polyType;
			this.useImage = false;
		} else {
			SpriteImage spriteImage = new SpriteImage(img);
			spriteImage.setMakePolygonInterval(polyInterval);
			int index = GraphicsUtils.hashImage(spriteImage.getImage());
			this.polyTriangles = lazyTriangles.get(index);
			if (polyTriangles == null) {
				PhysicsPolygon ppolygon = new PhysicsPolygon(spriteImage, 0, 0);
				polyTriangles = ppolygon.getPolygon2D().getTriangles();
				lazyTriangles.put(index, polyTriangles);
			}
			this.spriteRotate = spriteImage.rotate(0);
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
					for (int i = 0; i < polyTriangles.length; i++) {

						FixtureDef fixtureDef = new FixtureDef();
						PolygonShape polyShape = new PolygonShape();

						triangle[0] = polyTriangles[i].xpoints[0];
						triangle[1] = polyTriangles[i].ypoints[0];
						triangle[2] = polyTriangles[i].xpoints[1];
						triangle[3] = polyTriangles[i].ypoints[1];
						triangle[4] = polyTriangles[i].xpoints[2];
						triangle[5] = polyTriangles[i].ypoints[2];
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
					BufferedImage image = null;
					Graphics2D g = null;
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
						if (spriteRotate == null) {
							image = GraphicsUtils.createImage(width, height,
									false);
							g = image.createGraphics();
							g.setColor(color);
							g.fillRect(0, 0, width - 1, height - 1);
							g.setColor(Color.white);
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
						if (spriteRotate == null) {
							image = GraphicsUtils.createImage(width, height,
									true);
							g = image.createGraphics();
							g.setColor(color);
							g.fillOval(0, 0, width - 1, height - 1);
							g.setColor(Color.white);
							g.drawOval(0, 0, width - 1, height - 1);
						}
						break;
					case Triangle:
						if (type == BodyType.StaticBody) {
							createStaticTriangle(fixtureDef);
						} else if (type == BodyType.DynamicBody) {
							createDynamicTriangle(fixtureDef);
						} else if (type == BodyType.KinematicBody) {
							createKinematicTriangle(fixtureDef);
						}
						if (spriteRotate == null) {
							image = GraphicsUtils.createImage(width, height,
									false);
							g = image.createGraphics();
							LImage img = LImage.createImage(width, width, true);
							LGraphics lg = img.getLGraphics();
							Triangle2D t = new Triangle2D();
							t.set(width, width);
							lg.setColor(Color.blue);
							lg.fillTriangle(t);
							lg.setColor(Color.white);
							lg.drawTriangle(t);
							lg.dispose();
							g.drawImage(img.getBufferedImage(), 0, 0, null);
							t = null;
						}
						break;
					}
					if (spriteRotate == null && image != null) {
						this.setDrawImage(image);
					}
					if (g != null) {
						g.dispose();
					}
				}
				this.body.setUserData(this);
				this.useMake = true;
			} catch (Exception e) {
				throw new RuntimeException("PhysicsObject make "
						+ e.getMessage());
			} finally {
				this.setRotateSheet(spriteRotate.finalBitmap());
			}
		}
	}

	private void setRotateSheet(final BufferedImage img) {
		if (!supportSheet) {
			return;
		}
		synchronized (lazySheets) {
			if (SpriteRotateSheet.suited(img.getWidth(), img.getHeight())) {
				final Integer index = GraphicsUtils.hashImage(img);
				sheet = lazySheets.get(index);
				if (sheet == null) {
					Runnable runnable = new Runnable() {
						public void run() {
							sheet = new SpriteRotateSheet(img, maxRotateCache,
									polyType == Circle);
							lazySheets.put(index, sheet);
							supportRotateSheet = true;
						}
					};
					Thread thread = new Thread(runnable);
					thread.start();
				} else {
					supportRotateSheet = true;
				}
			} else {
				supportRotateSheet = false;
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

	public void setAngle(float angle) {
		bodyDef.angle = angle * RAD;
		if (body != null) {
			Vector2 v = body.getPosition().clone();
			body.setTransform(v, bodyDef.angle);
			body.setLinearVelocity(initLocation);
		}
	}

	public void createUI(LGraphics g) {
		if (useMake && visible) {
			if (body == null) {
				return;
			}

			if (supportRotateSheet && sheet != null) {
				Vector2 v = body.getPosition();

				double rotation = ((body.getAngle() * DEG) % 360);

				x = (int) v.x;
				y = (int) v.y;

				if (x < 0) {
					x = 0;
				}
				if (y < 0) {
					y = 0;
				}
				if (x > screenWidth) {
					x = screenWidth - width;
				}
				if (y > screenHeight) {
					y = screenHeight - height;
				}

				if (lockRotate) {
					if (!useImage) {
						switch (polyType) {
						case Box:
						case Circle:
							x = (int) x - halfWidth;
							y = (int) y - halfHeight;
							break;
						case Triangle:
							break;
						default:
							break;
						}
					}
					if (alpha > 0 && alpha < 1.0) {
						g.setAlpha(alpha);
						g.drawImage(spriteRotate.finalBitmap(), x, y);
						g.setAlpha(1.0F);
					} else {
						g.drawImage(spriteRotate.finalBitmap(), x, y);
					}
					return;
				}

				if (alpha > 0 && alpha < 1.0) {
					g.setAlpha(alpha);
					sheet.draw(g, x, y, rotation);
					g.setAlpha(1.0F);
				} else {
					sheet.draw(g, x, y, rotation);
				}
				if (!useImage) {
					switch (polyType) {
					case Box:
					case Circle:
						x = (int) x - halfWidth;
						y = (int) y - halfHeight;
						break;
					case Triangle:
						break;
					default:
						break;
					}
				}
			} else {

				Vector2 v = body.getPosition();

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
					case Triangle:
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

				BufferedImage bitmap = spriteRotate.finalBitmap();

				width = bitmap.getWidth();
				height = bitmap.getHeight();

				if (x > screenWidth) {
					x = screenWidth - width;
				}
				if (y > screenHeight) {
					y = screenHeight - height;
				}

				if (rotation == 0 || lockRotate) {
					if (alpha > 0 && alpha < 1.0) {
						g.setAlpha(alpha);
						g.drawImage(bitmap, x, y);
						g.setAlpha(1.0F);
					} else {
						g.drawImage(bitmap, x, y);
					}
					return;
				}

				if (alpha > 0 && alpha < 1.0) {
					g.setAlpha(alpha);
					float offsetX = x;
					float offsetY = y;
					if (polyType == Other) {
						offsetX = this.x - this.halfWidth;
						offsetY = this.y - this.halfHeight;
					}
					float newWidth = offsetX + this.halfWidth;
					float newHeight = offsetY + this.halfHeight;

					atform.setToIdentity();
					atform.translate(newWidth, newHeight);
					atform.rotate(Math.toRadians(rotation));
					atform.translate(-newWidth, -newHeight);
					atform.translate(x, y);
					if (bitmapFilter) {
						g.setAntiAlias(true);
						g.drawImage(bitmap, atform);
						g.setAntiAlias(false);
					} else {
						g.drawImage(bitmap, atform);
					}
					g.setAlpha(1.0F);
				} else {
					float offsetX = x;
					float offsetY = y;
					if (polyType == Other) {
						offsetX = this.x - this.halfWidth;
						offsetY = this.y - this.halfHeight;
					}
					float newWidth = offsetX + this.halfWidth;
					float newHeight = offsetY + this.halfHeight;

					atform.setToIdentity();
					atform.translate(newWidth, newHeight);
					atform.rotate(Math.toRadians(rotation));
					atform.translate(-newWidth, -newHeight);
					atform.translate(x, y);
					if (bitmapFilter) {
						g.setAntiAlias(true);
						g.drawImage(bitmap, atform);
						g.setAntiAlias(false);
					} else {
						g.drawImage(bitmap, atform);
					}
				}
			}
		}
	}

	public void setDrawImage(Image img) {
		if (useImage) {
			throw new RuntimeException("Set is not allowed !");
		}
		if (img != null) {
			if (img instanceof BufferedImage) {
				if (spriteRotate == null) {
					spriteRotate = new SpriteRotate((BufferedImage) img, img
							.getWidth(null), img.getHeight(null), 0);
				} else {
					spriteRotate.set((BufferedImage) img, img.getWidth(null),
							img.getHeight(null), 0);
				}
			} else {
				if (spriteRotate == null) {
					spriteRotate = new SpriteRotate(GraphicsUtils
							.getBufferImage(img), img.getWidth(null), img
							.getHeight(null), 0);
				} else {
					spriteRotate.set(GraphicsUtils.getBufferImage(img), img
							.getWidth(null), img.getHeight(null), 0);
				}
			}

		}
	}

	public void setDrawImage(String fileName) {
		setDrawImage(GraphicsUtils.loadImage(fileName));
	}

	public float getDirection() {
		if (body == null) {
			return 0;
		}
		return (float) Math.toDegrees(body.getAngle());
	}

	public void setSpeed(float gx, float gy) {
		setSpeed(new Vector2(gx, gy));
	}

	public void setSpeed(Vector2 speed) {
		if (body == null) {
			return;
		}
		this.body.setLinearVelocity(speed);
	}

	public Vector2 getSpeed() {
		if (body == null) {
			return new Vector2(0, 0);
		}
		return body.getLinearVelocity();
	}

	public Vector2 getPosition() {
		if (body == null) {
			return new Vector2(0, 0);
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
		Vector2 vel = body.getLinearVelocity();
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
		setPosition(new Vector2(x, y));
	}

	public void setPosition(Vector2 position) {
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
		if (body != null) {
			return;
		}
		PhysicsScreen.destroyBody(body);
	}

	public void applyforce(Vector2 force) {
		if (body != null) {
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

	public LImage getBitmap() {
		return new LImage(spriteRotate.getBitmap(0));
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
		Triangle2D t = new Triangle2D();
		t.set(width, width);
		tPoly.set(t.getVertexs());
		fixtureDef.shape = tPoly;
		bodyDef.angle = rotation * RAD;
		createBody(bodyDef, fixtureDef);
		tPoly.dispose();
	}

	private void createStaticTriangle(FixtureDef fixtureDef) {
		PolygonShape tPoly = new PolygonShape();
		Triangle2D t = new Triangle2D();
		t.set(width, width);
		tPoly.set(t.getVertexs());
		fixtureDef.shape = tPoly;
		bodyDef.angle = rotation * RAD;
		createBody(bodyDef, fixtureDef);
		tPoly.dispose();
	}

	private void createKinematicTriangle(FixtureDef fixtureDef) {
		PolygonShape tPoly = new PolygonShape();
		Triangle2D t = new Triangle2D();
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
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
		if (rect == null) {
			rect = new RectBox(x, y, width, height);
		} else {
			rect.setBounds(x, y, width, height);
		}
		return rect;
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

	public double getX() {
		Vector2 v = getPosition();
		return v.x;
	}

	public double getY() {
		Vector2 v = getPosition();
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
		Vector2 v = getPosition();
		return (int) v.x;
	}

	public int y() {
		Vector2 v = getPosition();
		return (int) v.y;
	}

	public void translate(int x, int y) {
		Vector2 v = getPosition();
		setPosition(v.x + x, v.y + y);
	}

	public boolean isSleeping() {
		if (body == null) {
			return false;
		}
		return body.isSleepingAllowed();
	}

	public void move(double x, double y) {
		move(new Vector2D(x, y));
	}

	public void move(Vector2D vector2) {
		Vector2 v = getPosition();
		float x = v.x + vector2.x();
		float y = v.y + vector2.y();
		setPosition(new Vector2(x, y));
	}

	public void move_multiples(int direction, int multiples) {
		if (multiples <= 0) {
			multiples = 1;
		}
		Vector2D v = Field2D.getDirection(direction);
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

	public boolean isSupportRotateSheet() {
		return supportRotateSheet && supportSheet;
	}

	public void setSupportRotateSheet(boolean supporRotateSheet) {
		this.supportRotateSheet = supporRotateSheet;
		this.supportSheet = supporRotateSheet;
	}

}
