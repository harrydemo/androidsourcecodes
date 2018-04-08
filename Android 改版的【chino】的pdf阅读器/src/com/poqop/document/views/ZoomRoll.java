package com.poqop.document.views;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

import com.poqop.R;

import com.poqop.document.models.ZoomModel;

public class ZoomRoll extends View
{
    private final Bitmap left;
    private final Bitmap right;
    private final Bitmap center;
    private final Bitmap serifs;
    private final Bitmap title;
    /**
     * VelocityTracker �����������ٴ����ٶȵ���
     * 
     */
    private VelocityTracker velocityTracker;  
    private Scroller scroller;
    private float lastX;
    private static final int MAX_VALUE = 1000;
    private final ZoomModel zoomModel;
    private static final float MULTIPLIER = 400.0f;

    private static final float MULTOP=10.0f;
    
    public ZoomRoll(Context context, ZoomModel zoomModel)
    {
        super(context);
        this.zoomModel = zoomModel;
        left = BitmapFactory.decodeResource(context.getResources(), R.drawable.left);
        right = BitmapFactory.decodeResource(context.getResources(), R.drawable.right);
        center = BitmapFactory.decodeResource(context.getResources(), R.drawable.center);
        serifs = BitmapFactory.decodeResource(context.getResources(), R.drawable.serifs);
    
        title = BitmapFactory.decodeResource(context.getResources(), R.drawable.zoomin);
       
        scroller = new Scroller(context);
        
        //������View�Ĳ���
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    
    
    /**
     * ��������
     * setMeasuredDimension(int width,int heith,)
     * ����������Ⱦ���.Ӧ�������ڴ��ݽ�����width��height ����֮��.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(left.getHeight(), right.getHeight()));
    }

    /*
     * ��Ҫʱ�ɸ��ؼ����������֪ͨ��һ���ӽڵ���Ҫ��������mScrollX��mScrollY��ֵ��
     */
    @Override
    public void computeScroll()
    {
        if (scroller.computeScrollOffset())
        {
            setCurrentValue(scroller.getCurrX());
            invalidate();
        }
        else
        {
            zoomModel.commit();
        }
    }

    public float getCurrentValue()
    {
        return (zoomModel.getZoom() - 1.2f) * MULTIPLIER;
    }

    public void setCurrentValue(float currentValue)
    {
        if (currentValue < 0.0) currentValue = 0.0f;
        if (currentValue > MAX_VALUE) currentValue = MAX_VALUE;
        final float zoom = 1.0f + currentValue / MULTIPLIER;
        zoomModel.setZoom(zoom);
    }
}
