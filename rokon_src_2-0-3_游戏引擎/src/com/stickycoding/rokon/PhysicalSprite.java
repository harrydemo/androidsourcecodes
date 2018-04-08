package com.stickycoding.rokon;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * PhysicalObject.java
 * An object which can undergo collisions through Box2D
 * 
 * @author Richard
 *
 */
public class PhysicalSprite extends Sprite implements Updateable {
	
	/**
	 * The Body associated with this Sprite, for Box2D
	 */
	public Body body;
	
	/**
	 * The BodyDef associated with this Sprite, for Box2D
	 */
	public BodyDef bodyDef;
	
	protected boolean usePhysics;

	public PhysicalSprite(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	/**
	 * Creates a Body for Box2D collisions
	 * 
	 * @param bodyDef valid BodyDef object
	 * @param fixture valid FixtureDef object
	 */
	public void createBody(BodyDef bodyDef, FixtureDef fixture) {
		body = Physics.world.createBody(bodyDef);
		body.createFixture(fixture);
		this.bodyDef = bodyDef;
		usePhysics = true;
	}
	
	/**
	 * Creates a Body for Box2D collisions, given by a Shape
	 * 
	 * @param bodyDef valid BodyDef object
	 * @param shape valid Shape object
	 */
	public void createBody(BodyDef bodyDef, Shape shape) {
		body = Physics.world.createBody(bodyDef);
		body.createFixture(shape, 1f);
		this.bodyDef = bodyDef;
		usePhysics = true;
	}
	
	/**
	 * Creates and applies a circular Body for this PhysicalSprite
	 * @return
	 */
	public CircleShape createCircleShape() {
		CircleShape circle = new CircleShape();
		circle.setRadius(width / 2f);
		return circle;
	}
	
	/**
	 * Creates and applies a rectangular Body for this PhysicalSprite
	 * @return
	 */
	public PolygonShape createBoxShape() {
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width / 2f, height / 2f);
		return poly;
	}
	
	/**
	 * Creates and applies a dynamic Body from a FixtureDef
	 * 
	 * @param fixture valid FixtureDef object
	 */
	public void createDynamicBody(FixtureDef fixture) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.x = getX() + width / 2f;
		bodyDef.position.y = getY() + height / 2f;
		bodyDef.angle = rotation * MathHelper.DEG_TO_RAD;
		createBody(bodyDef, fixture);
	}
	
	/**
	 * Creates and applies a dynamic Body from a Shape
	 * 
	 * @param shape valid Shape object
	 */
	public void createDynamicBody(Shape shape) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.x = getX() + width / 2f;
		bodyDef.position.y = getY() + height / 2f;
		bodyDef.angle = rotation * MathHelper.DEG_TO_RAD;
		createBody(bodyDef, shape);
	}
	
	/**
	 * Creates and applies a static Body from a FixtureDef
	 * 
	 * @param fixture valid FixtureDef object
	 */
	public void createStaticBody(FixtureDef fixture) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.x = getX() + width / 2f;
		bodyDef.position.y = getY() + height / 2f;
		bodyDef.angle = rotation * MathHelper.DEG_TO_RAD;
		createBody(bodyDef, fixture);
	}
	
	/**
	 * Creates and applies a static Body from a Shape
	 * 
	 * @param shape valid Shape object
	 */
	public void createStaticBody(Shape shape) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.x = getX() + width / 2f;
		bodyDef.position.y = getY() + height / 2f;
		bodyDef.angle = rotation * MathHelper.DEG_TO_RAD;
		createBody(bodyDef, shape);
	}
	
	/**
	 * Creates and applies a kinematic Body from a FixtureDef
	 * 
	 * @param fixture valid FixtureDef object
	 */
	public void createKinematicBody(FixtureDef fixture) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.x = getX() + width / 2f;
		bodyDef.position.y = getY() + height / 2f;
		bodyDef.angle = rotation * MathHelper.DEG_TO_RAD;
		createBody(bodyDef, fixture);
	}
	
	/**
	 * Creates and applies a kinematic Body from a Shape
	 * 
	 * @param shape valid Shape object
	 */
	public void createKinematicBody(Shape shape) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.x = getX() + width / 2f;
		bodyDef.position.y = getY() + height / 2f;
		bodyDef.angle = rotation * MathHelper.DEG_TO_RAD;
		createBody(bodyDef, shape);
	}
	
	/**
	 * Creates and applies a circular dynamic Body from a FixtureDef
	 * 
	 * @param fixtureDef valid FixtureDef object
	 */
	public void createDynamicCircle(FixtureDef fixtureDef) {
		CircleShape circle = new CircleShape();
		circle.setRadius(width / 2f);
		fixtureDef.shape = circle;
		createDynamicBody(fixtureDef);
		circle.dispose();
	}
	
	/**
	 * Creates and applies a dynamic circular Body
	 */
	public void createDynamicCircle() {
		CircleShape circle = new CircleShape();
		circle.setRadius(width / 2f);
		createDynamicBody(circle);
		circle.dispose();
	}
	
	/**
	 * Creates and applies a static circular Body from a FixtureDef
	 * 
	 * @param fixtureDef valid FixtureDef object
	 */
	public void createStaticCircle(FixtureDef fixtureDef) {
		CircleShape circle = new CircleShape();
		circle.setRadius(width / 2f);
		fixtureDef.shape = circle;
		createStaticBody(fixtureDef);
		circle.dispose();
	}
	
	/**
	 * Creates and applies a static circular Body
	 */
	public void createStaticCircle() {
		CircleShape circle = new CircleShape();
		circle.setRadius(width / 2f);
		createStaticBody(circle);
		circle.dispose();
	}
	
	/**
	 * Creates and applies a kinematic circular Body from a FixtureDef
	 * 
	 * @param fixtureDef valid FixtureDef object
	 */
	public void createKinematicCircle(FixtureDef fixtureDef) {
		CircleShape circle = new CircleShape();
		circle.setRadius(width / 2f);
		fixtureDef.shape = circle;
		createKinematicBody(fixtureDef);
		circle.dispose();
	}
	
	/**
	 * Creates and applies a kinematic circular Body
	 */
	public void createKinematicCircle() {
		CircleShape circle = new CircleShape();
		circle.setRadius(width / 2f);
		createKinematicBody(circle);
		circle.dispose();
	}
	
	/**
	 * Creates and applies a dynamic rectangular Body from a FixtureDef
	 * 
	 * @param fixtureDef valid FixtureDef object
	 */
	public void createDynamicBox(FixtureDef fixtureDef) {
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(width / 2f, height / 2f);
		fixtureDef.shape = boxPoly;
		createDynamicBody(fixtureDef);
		boxPoly.dispose();
	}
	
	/**
	 * Creates and applies a dynamic rectangular Body
	 */
	public void createDynamicBox() {
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(width / 2f, height / 2f);
		createDynamicBody(boxPoly);
		boxPoly.dispose();
	}
	
	/**
	 * Creates and applies a static rectangular Body from a FixtureDef
	 * 
	 * @param fixtureDef valid FixtureDef object
	 */
	public void createStaticBox(FixtureDef fixtureDef) {
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(width / 2f, height / 2f);
		fixtureDef.shape = boxPoly;
		createStaticBody(fixtureDef);
		boxPoly.dispose();
	}
	
	/**
	 * Creates and applies a static rectangular Body
	 */
	public void createStaticBox() {
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(width / 2, height / 2f);
		createStaticBody(boxPoly);	
		boxPoly.dispose();	
	}
	
	/**
	 * Creates and applies a kinematic rectangular Body from a FixtureDef
	 * 
	 * @param fixtureDef valid FixtureDef object
	 */
	public void createKinematicBox(FixtureDef fixtureDef) {
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(width / 2, height / 2f);
		fixtureDef.shape = boxPoly;
		createKinematicBody(fixtureDef);
		boxPoly.dispose();
	}
	
	/**
	 * Creates and applies a kinematic rectangular Body
	 */
	public void createKinematicBox() {
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(width / 2, height / 2f);
		createKinematicBody(boxPoly);
		boxPoly.dispose();
	}
	
	/**
	 * Returns the Body associated with this PhysicalObject
	 * 
	 * @return NULL if none set
	 */
	public Body getBody() {
		return body;
	}
	
	/**
	 * Sets the Body for this PhysicalObject
	 * Automatically flags usePhysics as TRUE
	 * Stops any kinematics from Sprite
	 * 
	 * @param body valid Body object
	 */
	public void setBody(Body body) {
		this.body = body;
		usePhysics = true;
		stop();
	}
	
	/**
	 * Flags the PhysicalObject to use the Box2D physics engine
	 * If the Body object is not set, this will raise an exception on next redraw
	 * Stops any kinematics from Sprite
	 */
	public void usePhysics() {
		usePhysics = true;
		stop();
	}
	
	/**
	 * Flags the PhysicalObject to ignore the physics engine for now
	 */
	public void noPhysics() {
		usePhysics = false;
	}
	
	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Sprite#onUpdate()
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(usePhysics) {
			setX(body.getPosition().x - width / 2);
			setY(body.getPosition().y - height / 2);
			rotation = body.getAngle() * MathHelper.RAD_TO_DEG;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Sprite#onRemove()
	 */
	@Override
	public void onRemove() {
		super.onRemove();
		if(body != null) {
			Physics.world.destroyBody(body);
			body = null;
		}
	}

}
