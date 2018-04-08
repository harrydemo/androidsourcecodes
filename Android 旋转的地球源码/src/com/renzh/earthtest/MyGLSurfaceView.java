package com.renzh.earthtest;

import java.text.AttributedCharacterIterator.Attribute;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

	public class MyGLSurfaceView extends GLSurfaceView  
	{  
	    GLRender myRenderer;// �Զ������Ⱦ��  
	    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// �Ƕ����ű���  
	    private float mPreviousY;// �ϴεĴ���λ��Y����  
	    private float mPreviousX;// �ϴεĴ���λ��X����  

	    public void toRoatX(float rox,float degree){
	    	myRenderer.toRoatX(rox, degree);
	    }
        public void toRoatY(float roy,float degree){
        	myRenderer.toRoatY(roy, degree);
	    }
	    public MyGLSurfaceView(Context context,AttributeSet as)  
	    {  
	        super(context);  
	        myRenderer = new GLRender(context);// ������Ⱦ��  
	        this.setRenderer(myRenderer);// ������Ⱦ��  
	        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������Ⱦģʽ  
	    }  
	  
	    @Override  
	    public boolean onTouchEvent(MotionEvent e)  
	    {   // �����¼��Ļص�����  
	        float x = e.getX();// �õ�x����  
	        float y = e.getY();// �õ�y����  
	        switch (e.getAction())  
	        {  
	        case MotionEvent.ACTION_MOVE:// ���ر��ƶ�  
	            float dy = y - mPreviousY;// ���㴥�ر�Yλ��  
	            float dx = x - mPreviousX;// ���㴥�ر�Xλ��  
	            float yAngle=myRenderer.getRoatY();
	            float xAngle=myRenderer.getRoatX();
	            yAngle += dx * TOUCH_SCALE_FACTOR;// ������y����ת�Ƕ�  
	            xAngle += dy * TOUCH_SCALE_FACTOR;// ������z����ת�Ƕ�  
	            myRenderer.setRoatY(yAngle);  
	            myRenderer.setRoatX(xAngle);     
	            requestRender();// �ػ滭��  
	        }  
	        mPreviousY = y;// ��¼���ر�λ��  
	        mPreviousX = x;// ��¼���ر�λ��  
	        return true;// ����true  
	    }  
	}
