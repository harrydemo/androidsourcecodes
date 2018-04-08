package com.android.angle;

import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL10;


/**
 * thread for rendering
 * @author Ivan Pajuelo
 * 
 */
public class RenderThread extends Thread
{
   private static final Semaphore sEglSemaphore = new Semaphore(1);
	public static boolean isRendering=false;
   private boolean mSizeChanged = true;
	public boolean mDone;
	public boolean mPaused;
	public boolean mHasFocus;
	public boolean mHasSurface;
	public boolean mContextLost;
	public int mWidth;
	public int mHeight;
	public EglHelper mEglHelper;
	private AngleSurfaceView mView;
	private long lCTM;
	
	public RenderThread(AngleSurfaceView view)
	{
		super();
		mDone = false;
		mWidth = 0;
		mHeight = 0;
		mView = view;
		setName("RenderThread");
	}

	@Override
	public void run()
	{
		try
		{
			try
			{
				sEglSemaphore.acquire();
			}
			catch (InterruptedException e)
			{
				return;
			}
			guardedRun();
		}
		catch (InterruptedException e)
		{
		}
		finally
		{
			sEglSemaphore.release();
		}
	}

	private void guardedRun() throws InterruptedException
	{
		final int[] configSpec = { EGL10.EGL_DEPTH_SIZE, 0, EGL10.EGL_NONE };
		mEglHelper = new EglHelper();
		mEglHelper.start(configSpec);

		GL10 gl = null;
		boolean isSurfaceCreated = true;
		boolean isSizeChanged = true;
		boolean restartEgl = false;

		while (!mDone)
		{
			if (mDone)
			{
				mEglHelper.finish();
				return;
			}

			int w, h;
			boolean recreateSurface;
			
			restartEgl = false;
			
			synchronized (this)
			{
				if (mPaused)
				{
					mEglHelper.finish();
					restartEgl = true;
				}
				if (needToWait())
				{
					while (needToWait())
						wait();
				}
				if (mDone)
					break;

				recreateSurface = mSizeChanged;
				w = mWidth;
				h = mHeight;
				mSizeChanged = false;
			}
			if (restartEgl)
			{
				mEglHelper.start(configSpec);
				isSurfaceCreated = true;
				recreateSurface = true;
			}
			if (recreateSurface)
			{
				gl = (GL10) mEglHelper.createSurface(mView.getHolder());
				isSizeChanged = true;
				sleep(100); //Esa pedazo de chapuza para esperar a las EGL
			}
			
			if (isSurfaceCreated)
			{
				mView.surfaceCreated(gl);
				isSurfaceCreated = false;
			}
			if (isSizeChanged)
			{
				mView.sizeChanged(gl, w, h);
				isSizeChanged = false;
			}
			
			if ((AngleSurfaceView.roWidth > 0) && (AngleSurfaceView.roHeight > 0))
			{
				float secondsElapsed = 0;
				long CTM = System.currentTimeMillis();
				if (lCTM > 0)
					secondsElapsed = (CTM - lCTM) / 1000.f;
				lCTM = CTM;
				mView.step(secondsElapsed);
				mView.draw(gl);
				mEglHelper.swap();
			}
		}

		//Esto ya parece Windows (Inicio, apagar sistema XD)
		if (restartEgl)
		{
			mEglHelper.start(configSpec);
			gl = (GL10) mEglHelper.createSurface(mView.getHolder());
		}

		if (gl != null)
			mView.destroy(gl);

		mEglHelper.finish();
	}

	private boolean needToWait()
	{
		return (mPaused || (!mHasFocus) || (!mHasSurface) || mContextLost) && (!mDone);
	}

	public void surfaceCreated()
	{
		synchronized (this)
		{
			mHasSurface = true;
			mContextLost = false;
			notify();
		}
	}

	public void surfaceDestroyed()
	{
		synchronized (this)
		{
			mHasSurface = false;
			notify();
		}
	}

	public void onPause()
	{
		synchronized (this)
		{
			RenderThread.isRendering=false;
			mPaused = true;
		}
	}

	public void onResume()
	{
		synchronized (this)
		{
			RenderThread.isRendering=true;
			mPaused = false;
			notify();
		}
	}

	public void onWindowFocusChanged(boolean hasFocus)
	{
		synchronized (this)
		{
			mHasFocus = hasFocus;
			if (mHasFocus == true)
			{
				notify();
			}
		}
	}

	public void onWindowResize(int w, int h)
	{
		synchronized (this)
		{
			mWidth = w;
			mHeight = h;
			mSizeChanged = true;
		}
	}

	public void requestExitAndWait()
	{
		synchronized (this)
		{
			mDone = true;
			notify();
		}
		try
		{
			join();
		}
		catch (InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
	}
}