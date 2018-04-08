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
        parameters.setPreviewSize(width, height);//���óߴ�  
        parameters.setPictureFormat(PixelFormat.JPEG);
        mCamera.setParameters(parameters);  
        mCamera.startPreview();//��ʼԤ��
        Log.e("Camera","surfaceChanged");
	}
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		mCamera = Camera.open();//��������  
        try {  
            mCamera.setPreviewDisplay(holder);//����Ԥ�� 
            Log.e("Camera","surfaceCreated");
        } catch (IOException e) {  
            mCamera.release();//�ͷ�  
            mCamera = null;  
        }
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		mCamera.setPreviewCallback(null);
		mCamera.stopPreview();//ֹͣԤ��  
        mCamera = null;
        Log.e("Camera","surfaceDestroyed");
	}
	/**
	 * �Զ��Խ����ص�Camera.PreviewCallback
	 */
	public void AutoFocusAndPreviewCallback()
	{
		if(mCamera!=null)
			mCamera.autoFocus(mAutoFocusCallBack);
	}
	
	/**
     * �Զ��Խ�
     */
    private Camera.AutoFocusCallback mAutoFocusCallBack = new Camera.AutoFocusCallback() {  
    	  
        @Override  
        public void onAutoFocus(boolean success, Camera camera) {      
            if (success) {  //�Խ��ɹ����ص�Camera.PreviewCallback
            	mCamera.setOneShotPreviewCallback(previewCallback); 
            }  
        }  
    };
    

}