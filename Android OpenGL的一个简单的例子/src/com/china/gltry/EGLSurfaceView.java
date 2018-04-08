package com.china.gltry;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class EGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	LocalRender render;
	EGLThread mEGLThread;
	SurfaceHolder mHolder;
	public EGLSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
	}
	
	
	
	public void setRender(LocalRender render){
		this.render = render;
		mEGLThread = new EGLThread(render, mHolder);
		mEGLThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Toast.makeText(getContext(), "surface changed", Toast.LENGTH_SHORT).show();
		mEGLThread.onWindowResize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Toast.makeText(getContext(), "surface created", Toast.LENGTH_SHORT).show();
		mEGLThread.surfaceCreated();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Toast.makeText(getContext(), "surface destroyed", Toast.LENGTH_SHORT).show();
	}

}
