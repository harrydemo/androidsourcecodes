package com.renzh.earthtest;

import java.text.AttributedCharacterIterator.Attribute;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

	public class MyGLSurfaceView extends GLSurfaceView  
	{  
	    GLRender myRenderer;// 自定义的渲染器  
	    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// 角度缩放比例  
	    private float mPreviousY;// 上次的触控位置Y坐标  
	    private float mPreviousX;// 上次的触控位置X坐标  

	    public void toRoatX(float rox,float degree){
	    	myRenderer.toRoatX(rox, degree);
	    }
        public void toRoatY(float roy,float degree){
        	myRenderer.toRoatY(roy, degree);
	    }
	    public MyGLSurfaceView(Context context,AttributeSet as)  
	    {  
	        super(context);  
	        myRenderer = new GLRender(context);// 创建渲染器  
	        this.setRenderer(myRenderer);// 设置渲染器  
	        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// 设置渲染模式  
	    }  
	  
	    @Override  
	    public boolean onTouchEvent(MotionEvent e)  
	    {   // 触摸事件的回调方法  
	        float x = e.getX();// 得到x坐标  
	        float y = e.getY();// 得到y坐标  
	        switch (e.getAction())  
	        {  
	        case MotionEvent.ACTION_MOVE:// 触控笔移动  
	            float dy = y - mPreviousY;// 计算触控笔Y位移  
	            float dx = x - mPreviousX;// 计算触控笔X位移  
	            float yAngle=myRenderer.getRoatY();
	            float xAngle=myRenderer.getRoatX();
	            yAngle += dx * TOUCH_SCALE_FACTOR;// 设置沿y轴旋转角度  
	            xAngle += dy * TOUCH_SCALE_FACTOR;// 设置沿z轴旋转角度  
	            myRenderer.setRoatY(yAngle);  
	            myRenderer.setRoatX(xAngle);     
	            requestRender();// 重绘画面  
	        }  
	        mPreviousY = y;// 记录触控笔位置  
	        mPreviousX = x;// 记录触控笔位置  
	        return true;// 返回true  
	    }  
	}
