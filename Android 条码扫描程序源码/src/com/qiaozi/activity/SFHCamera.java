package com.qiaozi.activity;

import java.io.IOException;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
public class SFHCamera implements SurfaceHolder.Callback{
	private SurfaceHolder holder = null;
	private Camera mCamera;
    private int width,height;
    private Camera.PreviewCallback previewCallback;
    
	public SFHCamera(SurfaceHolder holder,int w,int h,Camera.PreviewCallback previewCallback) {
		this.holder = holder;  
		this.holder.addCallback(this);  
		this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        width=w;
        height=h;
        this.previewCallback=previewCallback;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		Camera.Parameters parameters = mCamera.getParameters();  
        parameters.setPreviewSize(width, height);//设置尺寸  
        parameters.setPictureFormat(PixelFormat.JPEG);
        mCamera.setParameters(parameters);  
        mCamera.startPreview();//开始预览
        Log.e("Camera","surfaceChanged");
	}
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		mCamera = Camera.open();//启动服务  
        try {  
            mCamera.setPreviewDisplay(holder);//设置预览 
            Log.e("Camera","surfaceCreated");
        } catch (IOException e) {  
            mCamera.release();//释放  
            mCamera = null;  
        }
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		mCamera.setPreviewCallback(null);
		mCamera.stopPreview();//停止预览  
        mCamera = null;
        Log.e("Camera","surfaceDestroyed");
	}
	/**
	 * 自动对焦并回调Camera.PreviewCallback
	 */
	public void AutoFocusAndPreviewCallback()
	{
		if(mCamera!=null)
			mCamera.autoFocus(mAutoFocusCallBack);
	}
	
	/**
     * 自动对焦
     */
    private Camera.AutoFocusCallback mAutoFocusCallBack = new Camera.AutoFocusCallback() {  
    	  
        @Override  
        public void onAutoFocus(boolean success, Camera camera) {      
            if (success) {  //对焦成功，回调Camera.PreviewCallback
            	mCamera.setOneShotPreviewCallback(previewCallback); 
            }  
        }  
    };
    

}