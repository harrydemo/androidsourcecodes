package com.stickycoding.rokon;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

import com.stickycoding.rokon.device.Graphics;
import com.stickycoding.rokon.device.OS;

/**
 * RokonRenderer.java
 * The GLSurfaceView.Renderer class for OpenGL
 * 
 * @author Richard
 */

public class RokonRenderer implements GLSurfaceView.Renderer {
	
	private RokonActivity rokonActivity;
	
	protected RokonRenderer(RokonActivity rokonActivity) {
		this.rokonActivity = rokonActivity;
	}
	
	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
	 */
	public void onDrawFrame(GL10 gl) {
		Time.update();
		if(!RokonActivity.engineLoaded) {
			RokonActivity.engineLoaded = true;
			System.gc();
			rokonActivity.onLoadComplete();
			return;
		}
		GLHelper.setGL(gl);
		FPSCounter.onFrame();
		if(RokonActivity.currentScene != null) {
			RokonActivity.currentScene.onDraw(gl);	
		}
	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		Debug.print("onSurfaceChanged()");
		//Debug.print("Surface Size Changed: " + w + " " + h);
		gl.glViewport(0, 0, w, h);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		Debug.print("gluOrtho2D : " + RokonActivity.gameWidth + "x" + RokonActivity.gameHeight);
        GLU.gluOrtho2D(gl, 0, RokonActivity.gameWidth, RokonActivity.gameHeight, 0);
	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Debug.print("onSurfaceCreated()");
		
		GLHelper.reset();
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDisable(GL10.GL_DITHER);
		gl.glDisable(GL10.GL_LIGHTING);

        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        String extensions = gl.glGetString(GL10.GL_EXTENSIONS);
        String version = gl.glGetString(GL10.GL_VERSION);
        Graphics.setSupportsVBO(!version.contains("1.0") || extensions.contains("vertex_buffer_object"));
        

		if(DrawPriority.drawPriority == DrawPriority.PRIORITY_VBO) {
			if(!Graphics.isSupportsVBO()) {
				//Debug.warning("Device does not support VBO's, defaulting back to normal");
				DrawPriority.drawPriority = DrawPriority.PRIORITY_NORMAL;
			}
		}

        OS.hackBrokenDevices();
        
        TextureManager.reloadTextures(gl);

       // Debug.print("Graphics Support - " + version + " - " + (Graphics.isSupportsVBO() ? "vbos" : ""));
        
        //GLU.gluOrtho2D(gl, 0, RokonActivity.gameWidth, RokonActivity.gameHeight, 0);
	}

	@Override
	public void onSurfaceLost() {
		Debug.print("onSurfaceLost");
		GLHelper.reset();
		TextureManager.removeTextures();
		VBOManager.removeVBOs();
		if(RokonActivity.currentScene != null) {
			RokonActivity.currentScene.useNewClearColor = true;
		}
	}

}
