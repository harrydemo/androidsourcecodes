package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Segment collider
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSegmentCollider
{
	protected AnglePhysicObject mObject;
	protected AngleVector mA;
	protected AngleVector mB;
	protected AngleVector mDir;
	protected AngleVector mDiff;
	protected AngleVector mClosest;
	protected float mNormal;
	private float mLength;

	public AngleSegmentCollider(float x1, float y1, float x2, float y2)
	{
		mA = new AngleVector(x1, y1);
		mB = new AngleVector(x2, y2);
		mDir = new AngleVector();
		calculate();
	}

	public void draw(GL10 gl)
	{
		FloatBuffer vertices;
		vertices = ByteBuffer.allocateDirect(2 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

		gl.glDisable(GL11.GL_TEXTURE_2D);
		gl.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glColor4f(1f, 0f, 0f, 1f);
		gl.glTranslatef(mObject.mPosition.mX, mObject.mPosition.mY, 0.0f);
		vertices.clear();
		int count = 0;
		vertices.put(count++, mA.mX);
		vertices.put(count++, mA.mY);
		vertices.put(count++, mB.mX);
		vertices.put(count++, mB.mY);
		gl.glVertexPointer(2, GL11.GL_FLOAT, 0, vertices);
		gl.glDrawArrays(GL11.GL_LINES, 0, 2);
		gl.glPopMatrix();

		gl.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL11.GL_TEXTURE_2D);
	}

	private void calculate()
	{
		mDir.set(mB);
		mDir.sub(mA);
		float dX = mB.mX - mA.mX;
		float dY = mB.mY - mA.mY;
		mLength = (float) Math.sqrt(dX * dX + dY * dY);
		if (dX > 0)
			mNormal = (float) Math.acos(dY / mLength);
		else
			mNormal = (float) (Math.PI * 2 - Math.acos(dY / mLength));
		mNormal -= Math.PI / 2;
	}

	public float closestDist(AngleCircleCollider other)
	{
		return Math
				.abs((((mDir.mX) * ((other.mObject.mPosition.mY + other.mCenter.mY) - mObject.mPosition.mY + mA.mY)) - ((mDir.mY) * ((other.mObject.mPosition.mX + other.mCenter.mX)
						- mObject.mPosition.mX + mA.mX)))
						/ mLength);
	}
}
