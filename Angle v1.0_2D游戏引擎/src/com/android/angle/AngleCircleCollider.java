package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * circular collider
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleCircleCollider
{
	protected AnglePhysicObject mObject;
	protected AngleVector mCenter;
	protected float mRadius; 
	protected float mNormal;

	public AngleCircleCollider(float x, float y, float radius)
	{
		mCenter = new AngleVector(x, y);
		mRadius = radius;
	}

	public boolean test(AngleCircleCollider otherCollider)
	{
		float dX = (otherCollider.mObject.mPosition.mX + otherCollider.mCenter.mX) - (mObject.mPosition.mX + mCenter.mX);
		float dY = (otherCollider.mObject.mPosition.mY + otherCollider.mCenter.mY) - (mObject.mPosition.mY + mCenter.mY);
		float dist = (float) Math.sqrt(dX * dX + dY * dY);
		// The normal in a circle is the direction to the center of the other
		// collider
		if (dX > 0)
			otherCollider.mNormal = (float) Math.acos(dY / dist);
		else
			otherCollider.mNormal = (float) (Math.PI * 2 - Math.acos(dY / dist));

		return (dist < mRadius + otherCollider.mRadius);
	}

	public boolean test(AngleSegmentCollider otherCollider)
	{
		return otherCollider.closestDist(this) < mRadius;
	}

	protected void draw(GL10 gl)
	{
		final int segments = 20;

		gl.glDisable(GL11.GL_TEXTURE_2D);
		gl.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glColor4f(0f, 1f, 0f, 1f);
		gl.glTranslatef(mObject.mPosition.mX + mCenter.mX, mObject.mPosition.mY + mCenter.mY, 0.0f);
		FloatBuffer vertices;
		vertices = ByteBuffer.allocateDirect(segments * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

		int count = 0;
		for (float i = 0; i < Math.PI * 2; i += ((Math.PI * 2) / segments))
		{
			vertices.put(count++, (float) (Math.cos(i) * mRadius));
			vertices.put(count++, (float) (Math.sin(i) * mRadius));
		}
		gl.glVertexPointer(2, GL11.GL_FLOAT, 0, vertices);
		gl.glDrawArrays(GL11.GL_LINE_LOOP, 0, segments);
		gl.glPopMatrix();

		gl.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL11.GL_TEXTURE_2D);
	}

}
