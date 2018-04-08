package com.image.coverflow;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery {
    private Camera mCamera=new Camera();
    private int mMaxRotationAngle=60;
    private int mMaxZoom=-120;
    private int mCoverflowCenter;
    
	public GalleryFlow(Context context) {
		super(context);
        this.setStaticTransformationsEnabled(true);
	}
    public GalleryFlow(Context context,AttributeSet attrs){
    	super(context, attrs);
    	this.setStaticTransformationsEnabled(true);
    }
    public GalleryFlow(Context context,AttributeSet attrs,int defStyle){
    	super(context, attrs,defStyle);
    	this.setStaticTransformationsEnabled(true);
    }
    public int getMaxRotationAngle(){
    	return mMaxRotationAngle;
    }
    public void setMaxRotationAngle(int maxRotationAngle){
    	mMaxRotationAngle=maxRotationAngle;
    }
    public int getMaxZoom(){
    	return mMaxZoom;
    }
    public void setMaxZoom(int maxZoom){
    	mMaxZoom=maxZoom;
    }
    private int getCenterOfCoverflow(){
    	return (getWidth()-getPaddingLeft()-getPaddingRight())/2+getPaddingLeft();
    }
    private static int getCenterOfView(View view){
    	return view.getLeft()+view.getWidth()/2;
    }
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		// TODO Auto-generated method stub
		final int childCenter=getCenterOfView(child);
		final int childWidth=child.getWidth();
		int rotationAngle=0;
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);
		if(childCenter==mCoverflowCenter){
			transformImageBitmap((ImageView)child,t,0);
		}else{
			rotationAngle=(int)(((float)(mCoverflowCenter-childCenter)/childWidth)*mMaxRotationAngle);
		    if(Math.abs(rotationAngle)>mMaxRotationAngle){
		    	rotationAngle=(rotationAngle<0)?-mMaxRotationAngle:mMaxRotationAngle;
		    }
		    transformImageBitmap((ImageView)child, t, rotationAngle);
		}
		return true;
	}
    private void transformImageBitmap(ImageView child, Transformation t, int rotationAngle) {
    mCamera.save();
    final Matrix imageMatrix=t.getMatrix();
    final int imageHeight=child.getLayoutParams().height;
    final int imageWidth=child.getLayoutParams().width;
    final int rotation=Math.abs(rotationAngle);
    	mCamera.translate(0.0f, 0.0f, 100.0f);
    if(rotation<mMaxRotationAngle){
    	float zoomAmount=(float)(mMaxZoom+(rotation*1.5));
    	mCamera.translate(0.0f, 0.0f, zoomAmount);
    }
    mCamera.rotateY(rotationAngle);
    mCamera.getMatrix(imageMatrix);
    imageMatrix.preTranslate(-(imageWidth/2), -(imageHeight/2));
    imageMatrix.postTranslate((imageWidth/2), (imageHeight/2));
    	mCamera.restore();
	}
	protected void onSizeChanged(int w,int h,int oldw,int oldh) {
		mCoverflowCenter=getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}
    
    
    
    
}
