package com.ljp.laucher.myview;

import android.content.Context;
import android.graphics.Camera;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;


public class GalleryFlow extends Gallery {    
    private Camera mCamera = new Camera();
    private int mMaxRotationAngle = 60;
    private int mMaxZoom = -300;  
    private int mCoveflowCenter; 
    public GalleryFlow(Context context) {    
        super(context);    
        this.setStaticTransformationsEnabled(true);    
    }    
    public GalleryFlow(Context context, AttributeSet attrs) {    
        super(context, attrs);    
        this.setStaticTransformationsEnabled(true);    
    }    
    public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {    
        super(context, attrs, defStyle);    
        this.setStaticTransformationsEnabled(true);    
    }    
    public int getMaxRotationAngle() {    
        return mMaxRotationAngle;    
    }    
    public void setMaxRotationAngle(int maxRotationAngle) {    
        mMaxRotationAngle = maxRotationAngle;    
    }    
    public int getMaxZoom() {    
        return mMaxZoom;    
    }    
    public void setMaxZoom(int maxZoom) {    
        mMaxZoom = maxZoom;    
    }    
    private int getCenterOfCoverflow() {    
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2   
                        + getPaddingLeft();    
    }    
    private static int getCenterOfView(View view) {    
        return view.getLeft() + view.getWidth() / 2;    
    }    
        
    protected boolean getChildStaticTransformation(View child, Transformation t) {      
        final int childCenter = getCenterOfView(child);    
        final int childWidth = child.getWidth();    
        int rotationAngle = 0;    
        t.clear();    
        t.setTransformationType(Transformation.TYPE_MATRIX);    
        if (childCenter == mCoveflowCenter) {    
            transformImageBitmap((ImageView) child, t, 0);    
        } else {    
            rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);    
            if (Math.abs(rotationAngle) > mMaxRotationAngle) {    
                rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;    
            }    
            transformImageBitmap((ImageView) child, t, rotationAngle);    
        }    
        return true;    
    }    
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {    
        mCoveflowCenter = getCenterOfCoverflow();    
        super.onSizeChanged(w, h, oldw, oldh);    
    }    
    private void transformImageBitmap(ImageView child, Transformation t,    
                    int rotationAngle) {    
        mCamera.save();    
        final android.graphics.Matrix imageMatrix = t.getMatrix();    
        final int imageHeight = child.getLayoutParams().height;    
        final int imageWidth = child.getLayoutParams().width;    
            
        final int rotation = Math.abs(rotationAngle);    
            
        mCamera.translate(0.0f, 0.0f, 100.0f);    
        // As the angle of the view gets less, zoom in    
        if (rotation < mMaxRotationAngle) {    
            float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));    
            mCamera.translate(0.0f, 0.0f, zoomAmount);    
        }    
        mCamera.rotateY(rotationAngle);    
        mCamera.getMatrix(imageMatrix);    
        imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));    
        imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));    
        mCamera.restore();    
    }    
}


