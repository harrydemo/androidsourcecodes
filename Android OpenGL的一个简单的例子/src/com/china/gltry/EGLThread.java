package com.china.gltry;

import java.util.concurrent.Semaphore;

import javax.microedition.khronos.opengles.GL10;

import android.view.SurfaceHolder;


public class EGLThread extends Thread{
	LocalRender render;
	EGLHelper mEglHelper;
	SurfaceHolder mHolder;
	int mHeight = 0;
	int mWidth = 0;
	boolean bWait = true;
	private static final Semaphore sEglSemaphore = new Semaphore(1);
	public EGLThread(LocalRender render, SurfaceHolder holder){
		this.render = render;
		mHolder = holder;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		mEglHelper = new EGLHelper();
        /*
         * Specify a configuration for our opengl session
         * and grab the first configuration that matches is
         */
        int[] configSpec = render.getConfigSpec();
        mEglHelper.start(configSpec);
        
        try {
			sEglSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
    
        GL10 gl = null;
        boolean bFirstTime = true;
        while (bWait){
	        try {
	        	
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        gl = (GL10) mEglHelper.createSurface(mHolder);
        render.surfaceCreated(gl);
        render.sizeChanged(gl, mWidth, mHeight);
        render.drawFrame(gl);
       
        mEglHelper.swap();
        sEglSemaphore.release();
	}
	
	
	void onWindowResize(int w, int h){
		mHeight = h;
		mWidth = w;
		bWait = false;
	//	notify();
	}
	void surfaceCreated(){
		
	}
	
}
