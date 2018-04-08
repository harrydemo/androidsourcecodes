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
 * 实现秒针功能：Handler + Runnable + 消息队列 实现，
 * 该方式时钟运行稍快（半秒）
 */
@RemoteView
public class AnalogClock extends View {
    private Time mCalendar;

    /**
     * 表盘Bitmap对象
     */
    private Bitmap mDial;
   
    /**
     * 表盘，时针，分针，秒针BitmapDrawable对象
     */
    private BitmapDrawable mDialDrawable;
    private BitmapDrawable mHourHandDrawable;
    private BitmapDrawable mMinuteHandDrawable;
    private BitmapDrawable mSecondHandDrawable;
    
    /**
     * 表盘有效像素宽高
     */
    private int mDialWidth;
    private int mDialHeight;
    
    /**
     * 附着状态
     */
    private boolean mAttached = false;
    
    /**
     * 时，分，秒
     */
    private float mHours;
    private float mMinutes;
    private float mSeconds;
    
    /**
     * 时区
     */
    private String time_zone;
    
    public String getTime_zone() {
		return time_zone;
	}

	public void setTime_zone(String timeZone) {
		time_zone = timeZone;
	}
	
	/**
     * 标志时间、时区、时钟布局大小等是否有改变
     */
    private boolean mChanged;
    
    /**
     * 线程队列管理,消息传递和处理机制
     */
    private Handler loopHandler = new Handler();
    
    /**
     * 标志页面刷新线程尚未执行
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
	 * 构造方法
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
         * 初始化日历对象
         */
        mCalendar = new Time();
        time_zone = mCalendar.timezone;
        
        Resources r = this.getContext().getResources();
		InputStream is =null;
		
		/**
		 * 初始化表盘，时针，分针， 秒针
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
         * 获取表盘有效像素宽高
         */
        mDialWidth = mDialDrawable.getIntrinsicWidth();
        mDialHeight = mDialDrawable.getIntrinsicHeight();
    }

    /**
     *  该方法在view附着于window时候调用，这时，界面将要绘制，也就是说该方法必定于onDraw(Canvas)之前调用
     *  然而，该方法会在onDraw(Canvas)之前的任意时间调用，不一定在onMeasure(int, int)前后
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            
            /**
             * 注册一个消息过滤器，获取时间重置或者时区改变的action
             */
            IntentFilter filter = new IntentFilter();
            
//            filter.addAction(Intent.ACTION_TIME_TICK);
            
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            /**
             * 注册广播接收器，通过loopHandler接收信息
             */
            getContext().registerReceiver(mIntentReceiver, filter, null, loopHandler);
        }

 
        mCalendar = new Time();
        /**
         * 确保将时间调到当前时间
         */
        onTimeChanged();
    }

    /**
     * 当view脱离window时候调用，此时界面不再需要绘制
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
     * 1、onMeasure方法在控件的父元素正要放置它的子控件时调用。它会问一个问题，“你想要用多大地方啊？”，然后传入两个
     * 参数 ——widthMeasureSpec 和heightMeasureSpec。它们指明控件可获得的空间以及关于这个空间描述的元数据。
     * 
     * 2、默认的onMeasure提供的大小是１００*１００所以你想设置自己ｖｉｅｗ的大小，需要重写onMeasure和onDraw方法
     * 
     * 3、如何重写onMeasure。注意，调用的本地空方法是来计算高度和宽度的。
     * 它们会译解 widthHeightSpec和heightMeasureSpec值，并计算出合适的高度和宽度值。
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
     * 当视图大小改变
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
     * 处理时间更新事件
     */
    private void onTimeChanged() {
    	/**
    	 * 更新时间到当前
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
     * 接收日期重置，时区变化时的事件
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
