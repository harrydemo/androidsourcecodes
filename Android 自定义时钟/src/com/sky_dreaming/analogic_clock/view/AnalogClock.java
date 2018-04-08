package com.sky_dreaming.analogic_clock.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.res.Resources;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews.RemoteView;

import java.io.InputStream;
import java.util.TimeZone;

import com.sky_dreaming.analogic_clock.R;

/**
 * This widget display an analogic clock with three hands for hours ,minutes and
 * seconds.
 * ʵ�����빦�ܣ�Handler + Runnable + ��Ϣ���� ʵ�֣�
 * �÷�ʽʱ�������Կ죨���룩
 */
@RemoteView
public class AnalogClock extends View {
    private Time mCalendar;

    /**
     * ����Bitmap����
     */
    private Bitmap mDial;
   
    /**
     * ���̣�ʱ�룬���룬����BitmapDrawable����
     */
    private BitmapDrawable mDialDrawable;
    private BitmapDrawable mHourHandDrawable;
    private BitmapDrawable mMinuteHandDrawable;
    private BitmapDrawable mSecondHandDrawable;
    
    /**
     * ������Ч���ؿ��
     */
    private int mDialWidth;
    private int mDialHeight;
    
    /**
     * ����״̬
     */
    private boolean mAttached = false;
    
    /**
     * ʱ���֣���
     */
    private float mHours;
    private float mMinutes;
    private float mSeconds;
    
    /**
     * ʱ��
     */
    private String time_zone;
    
    public String getTime_zone() {
		return time_zone;
	}

	public void setTime_zone(String timeZone) {
		time_zone = timeZone;
	}
	
	/**
     * ��־ʱ�䡢ʱ����ʱ�Ӳ��ִ�С���Ƿ��иı�
     */
    private boolean mChanged;
    
    /**
     * �̶߳��й���,��Ϣ���ݺʹ������
     */
    private Handler loopHandler = new Handler();
    
    /**
     * ��־ҳ��ˢ���߳���δִ��
     */
    private boolean isRun = false;
    
    private void run()
    {

    	loopHandler.post(tickRunnable);
    }
    private Runnable tickRunnable = new Runnable() {   
        public void run() {

        	postInvalidate();

            loopHandler.postDelayed(tickRunnable, 1000);   
        }   
    };   
	/**
	 * ���췽��
	 */
    public AnalogClock(Context context) {
        this(context, null);
    }

    public AnalogClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClock(Context context, AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        
        /**
         * ��ʼ����������
         */
        mCalendar = new Time();
        time_zone = mCalendar.timezone;
        
        Resources r = this.getContext().getResources();
		InputStream is =null;
		
		/**
		 * ��ʼ�����̣�ʱ�룬���룬 ����
		 */
		is = r.openRawResource(R.drawable.clock);
		mDialDrawable = new BitmapDrawable(is);
		mDial = mDialDrawable.getBitmap();
		
		is = r.openRawResource(R.drawable.hands);
		mHourHandDrawable = new BitmapDrawable(is);
		
		is = r.openRawResource(R.drawable.hands);
		mMinuteHandDrawable = new BitmapDrawable(is);
		
		is = r.openRawResource(R.drawable.hands);
		mSecondHandDrawable = new BitmapDrawable(is);
		
        /**
         * ��ȡ������Ч���ؿ��
         */
        mDialWidth = mDialDrawable.getIntrinsicWidth();
        mDialHeight = mDialDrawable.getIntrinsicHeight();
    }

    /**
     *  �÷�����view������windowʱ����ã���ʱ�����潫Ҫ���ƣ�Ҳ����˵�÷����ض���onDraw(Canvas)֮ǰ����
     *  Ȼ�����÷�������onDraw(Canvas)֮ǰ������ʱ����ã���һ����onMeasure(int, int)ǰ��
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            
            /**
             * ע��һ����Ϣ����������ȡʱ�����û���ʱ���ı��action
             */
            IntentFilter filter = new IntentFilter();
            
//            filter.addAction(Intent.ACTION_TIME_TICK);
            
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            /**
             * ע��㲥��������ͨ��loopHandler������Ϣ
             */
            getContext().registerReceiver(mIntentReceiver, filter, null, loopHandler);
        }

 
        mCalendar = new Time();
        /**
         * ȷ����ʱ�������ǰʱ��
         */
        onTimeChanged();
    }

    /**
     * ��view����windowʱ����ã���ʱ���治����Ҫ����
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    /**
     * 1��onMeasure�����ڿؼ��ĸ�Ԫ����Ҫ���������ӿؼ�ʱ���á�������һ�����⣬������Ҫ�ö��ط���������Ȼ��������
     * ���� ����widthMeasureSpec ��heightMeasureSpec������ָ���ؼ��ɻ�õĿռ��Լ���������ռ�������Ԫ���ݡ�
     * 
     * 2��Ĭ�ϵ�onMeasure�ṩ�Ĵ�С�ǣ�����*�������������������Լ��������Ĵ�С����Ҫ��дonMeasure��onDraw����
     * 
     * 3�������дonMeasure��ע�⣬���õı��ؿշ�����������߶ȺͿ�ȵġ�
     * ���ǻ���� widthHeightSpec��heightMeasureSpecֵ������������ʵĸ߶ȺͿ��ֵ��
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float )heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);
 
        setMeasuredDimension(resolveSize((int) (mDialWidth * scale), widthMeasureSpec),
                resolveSize((int) (mDialHeight * scale), heightMeasureSpec));
    }

    /**
     * ����ͼ��С�ı�
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isRun)
        {
        	run();
        	isRun = true;
        	return;
        }
        onTimeChanged();
        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        int availableWidth = mDial.getWidth();
        int availableHeight = mDial.getHeight();

        int x = availableWidth / 2;
        int y = availableHeight / 2; 

        final Drawable dial = mDialDrawable;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
        
        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                                   (float) availableHeight / (float) h);

            canvas.save();

            canvas.scale(scale, scale, x, y);
        }

        if (changed) {

            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);


        canvas.save();

        canvas.rotate(mHours / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHandDrawable;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - (h * 2 / 3), x + (w / 2), y + (h / 3));
        }
        hourHand.draw(canvas);

        canvas.restore();

        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);

        final Drawable minuteHand = mMinuteHandDrawable;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h  * 4 / 5), x + (w / 2), y + (h / 5));
        }
        minuteHand.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(mSeconds / 60.0f * 360.0f, x, y);

        final Drawable scendHand = mSecondHandDrawable;
        if (changed) {
            w = scendHand.getIntrinsicWidth();
            h = scendHand.getIntrinsicHeight();
            scendHand.setBounds(x - (w / 2), y - h, x + (w / 2), y);
        }
        scendHand.draw(canvas);
        canvas.restore();

        if (scaled) {
            canvas.restore();
        }
    }

    /**
     * ����ʱ������¼�
     */
    private void onTimeChanged() {
    	/**
    	 * ����ʱ�䵽��ǰ
    	 */
        mCalendar.setToNow();

        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;
        
        mSeconds = second;
        mMinutes = minute + second / 60.0f;
        mHours = hour + mMinutes / 60.0f;
        
        mChanged = true;
    }

    /**
     * �����������ã�ʱ���仯ʱ���¼�
     */
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	String tz = "";
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
                time_zone = mCalendar.timezone;
            }
            Log.i("********************",tz);
            onTimeChanged();

            invalidate();
        }
    };
}
