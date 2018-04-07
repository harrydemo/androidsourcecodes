/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// This file was lifted from the APIDemos sample.  See: 
// http://developer.android.com/guide/samples/ApiDemos/src/com/example/android/apis/graphics/index.html
package com.android.angle;

import java.nio.CharBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Main view for OpenGL ES graphics
 * @author Ivan Pajuelo
 *
 */
public class AngleSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	private RenderThread mGLThread;
	private SurfaceHolder mHolder;
	private AngleTextureEngine mTextureEngine;
	
	static final int sMaxObjects = 30;
	AtomicBoolean updating;
	private int mMaxObjects;
	protected AngleObject[]mChilds;
	protected AngleObject[]mNewChilds;
	protected int mChildsCount;
	protected int mNewChildsCount;
	private AngleActivity mActivity; //For future use
	
	public static int roWidth;
	public static int roHeight;
	public static boolean mTexturesLost;
	public static Context mContext; //To static context access 

	public static final char[] sIndexValues = new char[] { 0, 1, 2, 1, 2, 3 };
	public static int roIndexBufferIndex = -1;
	public static final boolean sUseHWBuffers=true;
	
	public static void invalidateHardwareBuffers (GL10 gl)
	{
		int[] hwBuffers=new int[1];
		((GL11)gl).glGenBuffers(1, hwBuffers, 0);

		// Allocate and fill the index buffer.
		roIndexBufferIndex = hwBuffers[0];
		((GL11)gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, roIndexBufferIndex);
		((GL11)gl).glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, 6 * 2, CharBuffer.wrap(sIndexValues), GL11.GL_STATIC_DRAW);
		((GL11)gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public static void releaseHardwareBuffers (GL10 gl)
	{
		int[] hwBuffers = new int[1];
		hwBuffers[0]=roIndexBufferIndex;
		((GL11) gl).glDeleteBuffers(1, hwBuffers, 0);
		roIndexBufferIndex=-1;
	}

	public AngleSurfaceView(Context context)
	{
		super(context);
		init(context);
	}

	public AngleSurfaceView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	private void init(Context context)
	{
		if (context instanceof AngleActivity)
			mActivity=(AngleActivity)context;
		mMaxObjects=sMaxObjects;
		updating=new AtomicBoolean();
		mChilds=new AngleObject[mMaxObjects];
		mNewChilds=new AngleObject[mMaxObjects];
		mChildsCount=0;
		mNewChildsCount=0;
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
		
		mTextureEngine=new AngleTextureEngine();

		mContext = getContext();
	}

	public SurfaceHolder getSurfaceHolder()
	{
		return mHolder;
	}

	public void start()
	{
		mGLThread = new RenderThread(this);
		mGLThread.start();
	}

	public void surfaceCreated(SurfaceHolder holder)
	{
      Log.d("VIEW","surfaceCreated HOLDER");
		mGLThread.surfaceCreated();
	}

	public void surfaceDestroyed(SurfaceHolder holder)
	{
      Log.d("VIEW","surfaceDestroyed HOLDER");
		mGLThread.surfaceDestroyed();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
	{
      Log.d("VIEW","surfaceChanged HOLDER");
		mGLThread.onWindowResize(w, h);
	}

	public void onPause()
	{
		mGLThread.onPause();
	}

	public void onResume()
	{
		mGLThread.onResume();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		mGLThread.onWindowFocusChanged(hasFocus);
	   Log.d("VIEW","onWindowFocusChanged");
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		roWidth=0;
		roHeight=0;
		mGLThread.requestExitAndWait();
	   Log.d("VIEW","onDetachedFromWindow");
	}
	
	public void sizeChanged(GL10 gl, int width, int height)
	{
		roWidth=width;
		roHeight=height;
		gl.glViewport(0, 0, roWidth, roHeight);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, roWidth, roHeight, 0.0f, 0.0f, 1.0f);

		gl.glShadeModel(GL10.GL_FLAT);
		gl.glDisable(GL10.GL_DITHER);
		gl.glDisable(GL10.GL_MULTISAMPLE);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

      gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(1, 1, 1, 1);
		gl.glClearColor(0, 0, 0, 1);
		Log.d("VIEW","sizeChanged");
	}

	public void surfaceCreated(GL10 gl)
	{
		mTextureEngine.loadTextures(gl);
		
		step(0);
		
		if (sUseHWBuffers)
			invalidateHardwareBuffers(gl);

		for (int t=0;t<mChildsCount;t++)
		{
			mChilds[t].invalidateTexture(gl);
   		if (sUseHWBuffers)
   			mChilds[t].invalidateHardwareBuffers(gl);
		}
		
      Log.d("VIEW","surfaceCreated");
      
	}

	public void destroy(GL10 gl)
	{
		mTextureEngine.destroy(gl);
		
		if (sUseHWBuffers)
		{
			releaseHardwareBuffers(gl);
			for (int t=0;t<mChildsCount;t++)
   			mChilds[t].releaseHardwareBuffers(gl);
      }
	}
	
	public synchronized void draw(GL10 gl)
	{
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		for (int t=0;t<mChildsCount;t++)
			mChilds[t].draw(gl);
	}

	public AngleTextureEngine getTextureEngine()
	{
		return mTextureEngine;
	}

	public void setAwake(boolean awake)
	{
		setKeepScreenOn(awake);
	}

	//---------------------------------------------
	//-- Problemas de no tener herencia múltiple --
	//---------------------------------------------
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
	 * Called before object is deleted
	 */
	public void delete()
	{
		for (int t=0;t<mChildsCount;t++)
			mChilds[t].delete();
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
	//---------------------------------------------
	//-- ####################################### --
	//---------------------------------------------


}
