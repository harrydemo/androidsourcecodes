package com.android.angle;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.microedition.khronos.opengles.GL10;

/**
 * Base class of Angle engine
 * @author Ivan Pajuelo
 *
 */
public class AngleObject
{
	AtomicBoolean updating;
	public Object mParent;
	protected AngleObject[]mChilds;
	protected AngleObject[]mNewChilds;
	protected int mChildsCount;
	protected int mNewChildsCount;
	private int mMaxObjects;
	public boolean mDie;
	public int mTag;
	
	
	public AngleObject ()
	{
		mMaxObjects=AngleSurfaceView.sMaxObjects;
		doInit();
	}

	/**
	 * Create AngleObject specifying the maximum direct descendants
	 * @param maxObjects
	 */
	public AngleObject (int maxObjects)
	{
		mMaxObjects=maxObjects;
		doInit();
	}

	private void doInit()
	{
		mDie=false;
		mChilds=new AngleObject[mMaxObjects];
		mNewChilds=new AngleObject[mMaxObjects];
		mChildsCount=0;
		mNewChildsCount=0;
		updating=new AtomicBoolean();
	}

	/**
	 * Called when this object dies
	 */
	protected void onDie()
	{
	}

	/**
	 * Used by the engine to put the new objects in the childs list. Do not use after engine is running.
	 */
	public void commit()
	{
		if (mNewChildsCount>0)
		{
			updating.set(true);
			for (int t=0;t<mNewChildsCount;t++)
			{
				mNewChilds[t].mParent=this;
				mChilds[mChildsCount++]=mNewChilds[t];
			}
			mNewChildsCount=0;
			updating.set(false);
		}
	}
	/**
	 * Called every frame 
	 * @param secondsElapsed Seconds elapsed since last frame
	 */
	public void step(float secondsElapsed)
	{
		commit();
		for (int t=0;t<mChildsCount;t++)
		{
			if (mChilds[t].mDie)
			{
				mChilds[t].onDie();
				mChilds[t].mDie=false;
				mChilds[t].mParent=null;
				mChildsCount--;
				for (int d = t; d < mChildsCount; d++)
					mChilds[d] = mChilds[d + 1];
				mChilds[mChildsCount] = null;
				t--;
			}
			else
				mChilds[t].step(secondsElapsed);
		}
	}
	
	/**
	 * 
	 * @param object Object to add
	 */
	public AngleObject addObject(AngleObject object)
	{
		object.mDie=false;
		while (updating.get());
		for (int t=0;t<mChildsCount;t++)
		{
			if (mChilds[t]==object)
				return object;
		}
		for (int t=0;t<mNewChildsCount;t++)
		{
			if (mNewChilds[t]==object)
				return object;
		}
		if (mChildsCount+mNewChildsCount<mMaxObjects)
		{
			mNewChilds[mNewChildsCount++]=object;
		}
		return object;
	}

	/**
	 * 
	 * @param object Object to remove
	 */
	public void removeObject(AngleObject object)
	{
		if (object!=null)
			object.mDie=true;
	}

	/**
	 * 
	 * @param idx Index of the object to remove
	 */
	public void removeObject(int idx)
	{
		if (idx<mChildsCount)
			mChilds[idx].mDie=true;
	}

	/**
	 * 
	 * @return Number of childs
	 */
	public int count()
	{
		return mChildsCount;
	}

	/**
	 * Draws itself and all the childs
	 * @param gl
	 */
	public void draw(GL10 gl)
	{
		for (int t=0;t<mChildsCount;t++)
			mChilds[t].draw(gl);
	}

	/**
	 * Called before object is deleted
	 */
	public void delete()
	{
		for (int t=0;t<mChildsCount;t++)
			mChilds[t].delete();
	}


	/**
	 * Called when hardware buffers must be released
	 * @param gl
	 */
	public void releaseHardwareBuffers(GL10 gl)
	{
		for (int t=0;t<mChildsCount;t++)
			mChilds[t].releaseHardwareBuffers(gl);
	}

	/**
	 * Called when hardware buffers are invalid
	 * @param gl
	 */
	public void invalidateHardwareBuffers(GL10 gl)
	{
		for (int t=0;t<mChildsCount;t++)
			mChilds[t].invalidateHardwareBuffers(gl);
	}
	
	/**
	 * 
	 * @return Main AngleSurfaceView
	 */
	public AngleSurfaceView getSurfaceView()
	{
		if (mParent instanceof AngleSurfaceView)
			return (AngleSurfaceView) mParent;
		else
			return ((AngleObject) mParent).getSurfaceView();
	}

	/**
	 * invalidate the textures of this branch
	 * @param gl
	 */
	public void invalidateTexture(GL10 gl)
	{
		for (int t=0;t<mChildsCount;t++)
			mChilds[t].invalidateTexture(gl);
	}

	/**
	 * 
	 * @return Object parent
	 */
	public AngleObject getParent ()
	{
		if (mParent instanceof AngleObject)
			return (AngleObject) mParent;
		return null;
	}

	/**
	 * 
	 * @param idx
	 * @return child at index idx
	 */
	public AngleObject childAt(int idx)
	{
		if ((idx>=0)&&(idx<mChildsCount))
			return mChilds[idx];
		return null;
	}

	/**
	 * remove all children
	 */
	public void removeAll()
	{
		for (int t=0;t<mChildsCount;t++)
			mChilds[t].mDie=true;
	}
}
