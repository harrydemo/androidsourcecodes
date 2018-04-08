package org.loon.framework.android.game;

import java.io.Writer;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import org.loon.framework.android.game.LGameAndroid2DView.GLType;

import android.content.Context;
import android.opengl.GLSurfaceView.EGLConfigChooser;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 
 * Copyright 2008 - 2011
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
 * @version 0.1.1
 */
public class LGameGLOld extends SurfaceView implements SurfaceHolder.Callback {

	static final Semaphore sEglSemaphore = new Semaphore(1);

	private GLThread mGLThread;

	private EGLConfigChooser mEGLConfigChooser;

	private GLWrapper mGLWrapper;

	private int mDebugFlags;

	private int mRenderMode;

	private Renderer mRenderer;

	private int mSurfaceWidth;

	private int mSurfaceHeight;

	private boolean mHasSurface;

	public final static int RENDERMODE_WHEN_DIRTY = 0;

	public final static int RENDERMODE_CONTINUOUSLY = 1;

	public final static int DEBUG_CHECK_GL_ERROR = 1;

	public final static int DEBUG_LOG_GL_CALLS = 2;

	private SurfaceHolder holder;

	public LGameGLOld(Context context) {
		super(context);
		init();
	}

	public LGameGLOld(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		holder = GLType.updateSurfaceHolder(getHolder(), this);
		mRenderMode = RENDERMODE_CONTINUOUSLY;
	}

	public SurfaceHolder getSurfaceHolder() {
		return holder;
	}

	public void setGLWrapper(GLWrapper glWrapper) {
		mGLWrapper = glWrapper;
	}

	public void setDebugFlags(int debugFlags) {
		mDebugFlags = debugFlags;
	}

	public int getDebugFlags() {
		return mDebugFlags;
	}

	public void setRenderer(Renderer renderer) {
		if (mRenderer != null) {
			throw new IllegalStateException(
					"setRenderer has already been called for this instance.");
		}

		mRenderer = renderer;
	}

	public void setEGLConfigChooser(EGLConfigChooser configChooser) {
		if (mRenderer != null) {
			throw new IllegalStateException(
					"setRenderer has already been called for this instance.");
		}
		mEGLConfigChooser = configChooser;
	}

	public void setEGLConfigChooser(boolean needDepth) {
		setEGLConfigChooser(new SimpleEGLConfigChooser(needDepth));
	}

	public void setEGLConfigChooser(int redSize, int greenSize, int blueSize,
			int alphaSize, int depthSize, int stencilSize) {
		setEGLConfigChooser(new ComponentSizeChooser(redSize, greenSize,
				blueSize, alphaSize, depthSize, stencilSize));
	}

	public void setRenderMode(int renderMode) {
		mRenderMode = renderMode;
		if (mGLThread != null) {
			mGLThread.setRenderMode(renderMode);
		}
	}

	public int getRenderMode() {
		return mRenderMode;
	}

	public void requestRender() {
		mGLThread.requestRender();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (mGLThread != null) {
			mGLThread.surfaceCreated();
		}
		mHasSurface = true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mGLThread != null) {
			mGLThread.surfaceDestroyed();
		}
		mHasSurface = false;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (mGLThread != null) {
			mGLThread.onWindowResize(w, h);
		}
		mSurfaceWidth = w;
		mSurfaceHeight = h;
	}

	public void onPause() {
		mGLThread.onPause();
		mGLThread.requestExitAndWait();
		mGLThread = null;
	}

	public void onResume() {
		if (mEGLConfigChooser == null) {
			mEGLConfigChooser = new SimpleEGLConfigChooser(true);
		}
		mGLThread = new GLThread(mRenderer);
		mGLThread.start();
		mGLThread.setRenderMode(mRenderMode);
		if (mHasSurface) {
			mGLThread.surfaceCreated();
		}
		if (mSurfaceWidth > 0 && mSurfaceHeight > 0) {
			mGLThread.onWindowResize(mSurfaceWidth, mSurfaceHeight);
		}
		mGLThread.onResume();
	}

	public void queueEvent(Runnable r) {
		if (mGLThread != null) {
			mGLThread.queueEvent(r);
		}
	}

	public interface GLWrapper {

		GL wrap(GL gl);
	}

	private static abstract class BaseConfigChooser implements EGLConfigChooser {
		public BaseConfigChooser(int[] configSpec) {
			mConfigSpec = configSpec;
		}

		public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
			int[] num_config = new int[1];
			egl.eglChooseConfig(display, mConfigSpec, null, 0, num_config);

			int numConfigs = num_config[0];

			if (numConfigs <= 0) {
				throw new IllegalArgumentException(
						"No configs match configSpec");
			}

			EGLConfig[] configs = new EGLConfig[numConfigs];
			egl.eglChooseConfig(display, mConfigSpec, configs, numConfigs,
					num_config);
			EGLConfig config = chooseConfig(egl, display, configs);
			if (config == null) {
				throw new IllegalArgumentException("No config chosen");
			}
			return config;
		}

		abstract EGLConfig chooseConfig(EGL10 egl, EGLDisplay display,
				EGLConfig[] configs);

		protected int[] mConfigSpec;
	}

	private static class ComponentSizeChooser extends BaseConfigChooser {
		public ComponentSizeChooser(int redSize, int greenSize, int blueSize,
				int alphaSize, int depthSize, int stencilSize) {
			super(new int[] { EGL10.EGL_RED_SIZE, redSize,
					EGL10.EGL_GREEN_SIZE, greenSize, EGL10.EGL_BLUE_SIZE,
					blueSize, EGL10.EGL_ALPHA_SIZE, alphaSize,
					EGL10.EGL_DEPTH_SIZE, depthSize, EGL10.EGL_STENCIL_SIZE,
					stencilSize, EGL10.EGL_NONE });
			mValue = new int[1];
			mRedSize = redSize;
			mGreenSize = greenSize;
			mBlueSize = blueSize;
			mAlphaSize = alphaSize;
			mDepthSize = depthSize;
			mStencilSize = stencilSize;
		}

		@Override
		public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display,
				EGLConfig[] configs) {
			EGLConfig closestConfig = null;
			int closestDistance = 1000;
			for (EGLConfig config : configs) {
				int r = findConfigAttrib(egl, display, config,
						EGL10.EGL_RED_SIZE, 0);
				int g = findConfigAttrib(egl, display, config,
						EGL10.EGL_GREEN_SIZE, 0);
				int b = findConfigAttrib(egl, display, config,
						EGL10.EGL_BLUE_SIZE, 0);
				int a = findConfigAttrib(egl, display, config,
						EGL10.EGL_ALPHA_SIZE, 0);
				int d = findConfigAttrib(egl, display, config,
						EGL10.EGL_DEPTH_SIZE, 0);
				int s = findConfigAttrib(egl, display, config,
						EGL10.EGL_STENCIL_SIZE, 0);
				int distance = Math.abs(r - mRedSize)
						+ Math.abs(g - mGreenSize) + Math.abs(b - mBlueSize)
						+ Math.abs(a - mAlphaSize) + Math.abs(d - mDepthSize)
						+ Math.abs(s - mStencilSize);
				if (distance < closestDistance) {
					closestDistance = distance;
					closestConfig = config;
				}
			}
			return closestConfig;
		}

		private int findConfigAttrib(EGL10 egl, EGLDisplay display,
				EGLConfig config, int attribute, int defaultValue) {

			if (egl.eglGetConfigAttrib(display, config, attribute, mValue)) {
				return mValue[0];
			}
			return defaultValue;
		}

		private int[] mValue;
		protected int mRedSize;
		protected int mGreenSize;
		protected int mBlueSize;
		protected int mAlphaSize;
		protected int mDepthSize;
		protected int mStencilSize;
	}

	private static class SimpleEGLConfigChooser extends ComponentSizeChooser {
		public SimpleEGLConfigChooser(boolean withDepthBuffer) {
			super(4, 4, 4, 0, withDepthBuffer ? 16 : 0, 0);
			mRedSize = 5;
			mGreenSize = 6;
			mBlueSize = 5;
		}
	}

	private class EglHelper {
		public EglHelper() {

		}

		public void start() {

			mEgl = (EGL10) EGLContext.getEGL();

			mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

			int[] version = new int[2];
			mEgl.eglInitialize(mEglDisplay, version);
			mEglConfig = mEGLConfigChooser.chooseConfig(mEgl, mEglDisplay);

			mEglContext = mEgl.eglCreateContext(mEglDisplay, mEglConfig,
					EGL10.EGL_NO_CONTEXT, null);

			mEglSurface = null;
		}

		public GL createSurface(SurfaceHolder holder) {

			if (mEglSurface != null) {

				mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
						EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
				mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
			}

			mEglSurface = mEgl.eglCreateWindowSurface(mEglDisplay, mEglConfig,
					holder, null);

			mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface,
					mEglContext);

			GL gl = mEglContext.getGL();
			if (mGLWrapper != null) {
				gl = mGLWrapper.wrap(gl);
			}

			return gl;
		}

		public boolean swap() {
			mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);

			return mEgl.eglGetError() != EGL11.EGL_CONTEXT_LOST;
		}

		public void finish() {
			if (mEglSurface != null) {
				mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
						EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
				mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
				mEglSurface = null;
			}
			if (mEglContext != null) {
				mEgl.eglDestroyContext(mEglDisplay, mEglContext);
				mEglContext = null;
			}
			if (mEglDisplay != null) {
				mEgl.eglTerminate(mEglDisplay);
				mEglDisplay = null;
			}
		}

		EGL10 mEgl;
		EGLDisplay mEglDisplay;
		EGLSurface mEglSurface;
		EGLConfig mEglConfig;
		EGLContext mEglContext;
	}

	class GLThread extends Thread {
		GLThread(Renderer renderer) {
			super();
			mDone = false;
			mWidth = 0;
			mHeight = 0;
			mRequestRender = true;
			mRenderMode = RENDERMODE_CONTINUOUSLY;
			mRenderer = renderer;
			mSizeChanged = true;
			setName("GLThread");
		}

		@Override
		public void run() {

			try {
				try {
					sEglSemaphore.acquire();
				} catch (InterruptedException e) {
					return;
				}
				guardedRun();
			} catch (InterruptedException e) {

			} finally {
				sEglSemaphore.release();
			}
		}

		private void guardedRun() throws InterruptedException {
			mEglHelper = new EglHelper();
			mEglHelper.start();

			GL10 gl = null;
			boolean tellRendererSurfaceCreated = true;
			boolean tellRendererSurfaceChanged = true;

			while (!mDone) {

				int w, h;
				boolean changed;
				boolean needStart = false;
				synchronized (this) {
					Runnable r;
					while ((r = getEvent()) != null) {
						r.run();
					}
					if (mPaused) {
						mEglHelper.finish();
						needStart = true;
					}
					while (needToWait()) {
						wait();
					}
					if (mDone) {
						break;
					}
					changed = mSizeChanged;
					w = mWidth;
					h = mHeight;
					mSizeChanged = false;
					mRequestRender = false;
				}
				if (needStart) {
					mEglHelper.start();
					tellRendererSurfaceCreated = true;
					changed = true;
				}
				if (changed) {
					gl = (GL10) mEglHelper.createSurface(getHolder());
					tellRendererSurfaceChanged = true;
				}
				if (tellRendererSurfaceCreated) {
					mRenderer.onSurfaceCreated(gl, mEglHelper.mEglConfig);
					tellRendererSurfaceCreated = false;
				}
				if (tellRendererSurfaceChanged) {
					mRenderer.onSurfaceChanged(gl, w, h);
					tellRendererSurfaceChanged = false;
				}
				if ((w > 0) && (h > 0)) {

					mRenderer.onDrawFrame(gl);

					mEglHelper.swap();
				}
			}

			mEglHelper.finish();
		}

		private boolean needToWait() {
			if (mDone) {
				return false;
			}

			if (mPaused || (!mHasSurface)) {
				return true;
			}

			if ((mWidth > 0)
					&& (mHeight > 0)
					&& (mRequestRender || (mRenderMode == RENDERMODE_CONTINUOUSLY))) {
				return false;
			}

			return true;
		}

		public void setRenderMode(int renderMode) {
			if (!((RENDERMODE_WHEN_DIRTY <= renderMode) && (renderMode <= RENDERMODE_CONTINUOUSLY))) {
				throw new IllegalArgumentException("renderMode");
			}
			synchronized (this) {
				mRenderMode = renderMode;
				if (renderMode == RENDERMODE_CONTINUOUSLY) {
					notify();
				}
			}
		}

		public int getRenderMode() {
			synchronized (this) {
				return mRenderMode;
			}
		}

		public void requestRender() {
			synchronized (this) {
				mRequestRender = true;
				notify();
			}
		}

		public void surfaceCreated() {
			synchronized (this) {
				mHasSurface = true;
				notify();
			}
		}

		public void surfaceDestroyed() {
			synchronized (this) {
				mHasSurface = false;
				notify();
			}
		}

		public void onPause() {
			synchronized (this) {
				mPaused = true;
			}
		}

		public void onResume() {
			synchronized (this) {
				mPaused = false;
				notify();
			}
		}

		public void onWindowResize(int w, int h) {
			synchronized (this) {
				mWidth = w;
				mHeight = h;
				mSizeChanged = true;
				notify();
			}
		}

		public void requestExitAndWait() {

			synchronized (this) {
				mDone = true;
				notify();
			}
			try {
				join();
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

		public void queueEvent(Runnable r) {
			synchronized (this) {
				mEventQueue.add(r);
			}
		}

		private Runnable getEvent() {
			synchronized (this) {
				if (mEventQueue.size() > 0) {
					return mEventQueue.remove(0);
				}

			}
			return null;
		}

		private boolean mDone;
		private boolean mPaused;
		private boolean mHasSurface;
		private int mWidth;
		private int mHeight;
		private int mRenderMode;
		private boolean mRequestRender;
		private Renderer mRenderer;
		private ArrayList<Runnable> mEventQueue = new ArrayList<Runnable>();
		private EglHelper mEglHelper;
		private boolean mSizeChanged;
	}

	static class LogWriter extends Writer {

		@Override
		public void close() {
			flushBuilder();
		}

		@Override
		public void flush() {
			flushBuilder();
		}

		@Override
		public void write(char[] buf, int offset, int count) {
			for (int i = 0; i < count; i++) {
				char c = buf[offset + i];
				if (c == '\n') {
					flushBuilder();
				} else {
					mBuilder.append(c);
				}
			}
		}

		private void flushBuilder() {
			if (mBuilder.length() > 0) {
				Log.v("GLSurfaceView", mBuilder.toString());
				mBuilder.delete(0, mBuilder.length());
			}
		}

		private StringBuilder mBuilder = new StringBuilder();
	}

}
