package com.badlogic.gdx.physics.box2d;

import org.loon.framework.android.game.core.geom.Vector2f;

/**
 * A circle shape.
 * @author mzechner
 *
 */
public class CircleShape extends Shape 
{
	public CircleShape( )
	{
		addr = newCircleShape( );
	}
	
	private native long newCircleShape( );
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type getType() 
	{			
		return Type.Circle;
	}
	
	/**
	 * Returns the position of the shape
	 */
	private final float[] tmp = new float[2];
	private final Vector2f position = new Vector2f( );
	public Vector2f getPosition( )
	{
		jniGetPosition( addr, tmp );
		position.x = tmp[0]; position.y = tmp[1];
		return position;
	}
	
	private native void jniGetPosition( long addr, float[] position );
	
	/**
	 * Sets the position of the shape
	 */
	public void setPosition( Vector2f position )
	{
		jniSetPosition( addr, position.x, position.y );
	}
	
	private native void jniSetPosition( long addr, float positionX, float positionY );
}
