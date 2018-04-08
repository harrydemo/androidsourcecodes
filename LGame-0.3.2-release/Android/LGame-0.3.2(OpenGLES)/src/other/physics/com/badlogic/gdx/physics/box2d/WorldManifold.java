package com.badlogic.gdx.physics.box2d;

import org.loon.framework.android.game.core.geom.Vector2f;

/**
 * This is used to compute the current state of a contact manifold.
 */
public class WorldManifold 
{				
	protected final Vector2f normal = new Vector2f();
	protected final Vector2f[] points = { new Vector2f(), new Vector2f() }; 
	protected int numContactPoints;
	
	protected WorldManifold( )
	{		
	}
	
	/**
	 * Returns the normal of this manifold
	 */
	public Vector2f getNormal( )
	{
		return normal;
	}	
	
	/**
	 * Returns the contact points of this manifold. Use getNumberOfContactPoints
	 * to determine how many contact points there are (0,1 or 2)
	 */
	public Vector2f[] getPoints( )
	{
		return points;
	}	
	
	/**
	 * @return the number of contact points
	 */
	public int getNumberOfContactPoints( )
	{
		return numContactPoints;
	}	
}
