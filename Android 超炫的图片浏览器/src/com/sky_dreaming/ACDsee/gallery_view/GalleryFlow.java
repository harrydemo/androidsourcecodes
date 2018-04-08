package com.sky_dreaming.ACDsee.gallery_view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;
/**
 * ****************************************************************
 * 文件名称	: GalleryFlow.java
 * 创建时间	: 2010-11-2 下午05:27:46
 * 文件描述	: 自定义Gallery视图，实现图片旋转效果
 *****************************************************************
 */
public class GalleryFlow extends Gallery {

    /**
     * 用于ImageView的矩阵变换
     */
    private Camera mCamera = new Camera();

    private int mMaxRotationAngle = 60;

    /**
     * The maximum zoom on the centre Child
     */
    private int mMaxZoom = -120;
    
    /**
     * Coverflow的中心
     */
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

    /**
     * get set 方法
     * @return
     */
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

	/**
	 * 获取Coverflow的中心位置
	 * @return
	 */
    private int getCenterOfCoverflow() {
            return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
                            + getPaddingLeft();
    }

    /**
     * 获取子视图的中心位置
     * @param view
     * @return
     */
    private static int getCenterOfView(View view) {
            return view.getLeft() + view.getWidth() / 2;
    }

    protected boolean getChildStaticTransformation(View child, Transformation t) {
    	

    	final int childCenter = getCenterOfView(child);

    	final int childWidth = child.getWidth();
    	int rotationAngle = 0;
    	/**
    	 * 重置transformation
    	 */
    	t.clear();
    	t.setTransformationType(Transformation.TYPE_MATRIX);

    	if (childCenter == mCoveflowCenter) {
    		transformImageBitmap((ImageView) child, t, 0);
    	} else {
    		/**
    		 * 计算旋转角度
    		 */
    		rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
    		if (Math.abs(rotationAngle) > mMaxRotationAngle) {
    			rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle: mMaxRotationAngle;
    		}
    		transformImageBitmap((ImageView) child, t, rotationAngle);
    	}
    	
    	return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mCoveflowCenter = getCenterOfCoverflow();
            super.onSizeChanged(w, h, oldw, oldh);
    }

    /**

     * 将子视图旋转相应角度 ？？？？？？？？？？？？？
     * 
     * @param imageView
     *            ImageView the ImageView whose bitmap we want to rotate
     * @param t
     *            transformation
     * @param rotationAngle
     *            the Angle by which to rotate the Bitmap
     */
    private void transformImageBitmap(ImageView child, Transformation t,
                    int rotationAngle) {
    	/**
    	 * 保存相机当前状态
    	 */
            mCamera.save();
            /**
             * 获取变换矩阵
             */
            final Matrix imageMatrix = t.getMatrix();
            
            final int imageHeight = child.getLayoutParams().height;
            final int imageWidth = child.getLayoutParams().width;
            final int rotation = Math.abs(rotationAngle);

            /**
             * 在Z轴上正向移动camera的视角，实际效果为放大图片。
             * 如果在Y轴上移动，则图片上下移动；X轴上对应图片左右移动。
             */
            mCamera.translate(0.0f, 0.0f, 100.0f);

            // As the angle of the view gets less, zoom in
            if (rotation < mMaxRotationAngle) {
                    float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
                    mCamera.translate(0.0f, 0.0f, zoomAmount);
            }

            /**
             * 在Y轴上旋转，对应图片竖向向里翻转。如果在X轴上旋转，则对应图片横向向里翻转。
             */
            mCamera.rotateY(rotationAngle);
            mCamera.getMatrix(imageMatrix);
            imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
            imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
            
            /**
             * 恢复相机原状态
             */
            mCamera.restore();
    }
}
