
package com.sun.shine.myrotation.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

public class RotatView extends View {

    /**
     * 原心坐标x
     */
    float o_x;

    /**
     * 原心坐标y
     */
    float o_y;

    /**
     * 处理惯性的handler
     */
    Handler handler;

    /**
     * handler处理消息的间隔
     */
    int delayedTime = 20;

    /**
     * 消息信号，滚动的标识
     */
    static final int play = 0;

    /**
     * 消息信号，停止滚动的标识
     */
    static final int stop = 1;

    /**
     * 上次记录的时间,计算一定时间所走过的弧度、计算速度.
     */
    double currentTime = 0;

    /**
     * 图片的宽度
     */
    int width;

    /**
     * 图片的高度
     */
    int height;

    /**
     * view的真实宽度与高度:因为是旋转，所以这个view是正方形，它的值是图片的对角线长度
     */
    double maxwidth;

    /**
     * 旋转的图片
     */
    Bitmap rotatBitmap;

    public RotatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    /**
     * 初始化handler与速度计算器
     */
    private void init() {
        vRecord = new VRecord();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                double detaTime = System.currentTimeMillis() - currentTime;
                switch (msg.what) {

                    case play: {
                        if (isClockWise) {
                            speed = speed - a * detaTime;
                            if (speed <= 0) {
                                return;
                            } else {
                                handler.sendEmptyMessageDelayed(play, delayedTime);
                            }
                        } else {
                            speed = speed + a * detaTime;
                            if (speed >= 0) {
                                return;
                            } else {
                                handler.sendEmptyMessageDelayed(play, delayedTime);
                            }
                        }

                        addDegree((float)(speed * detaTime + (a * detaTime * detaTime) / 2));

                        // if (a < a_max) {
                        // a = (float)(a + a_add*detaTime);
                        // System.out.println("a:"+a);
                        // }
                        currentTime = System.currentTimeMillis();
                        invalidate();

                        break;
                    }
                    case stop: {
                        speed = 0;
                        handler.removeMessages(play);
                    }
                }

                super.handleMessage(msg);
            }
        };
        // 默认是有一张图片的

        initSize();
    }

    public void setRotatBitmap(Bitmap bitmap) {
        rotatBitmap = bitmap;
        initSize();
        postInvalidate();
    }

    public void setRotatDrawableResource(int id) {

        BitmapDrawable drawable = (BitmapDrawable)getContext().getResources().getDrawable(id);

        setRotatDrawable(drawable);
    }

    public void setRotatDrawable(BitmapDrawable drawable) {
        rotatBitmap = drawable.getBitmap();
        initSize();
        postInvalidate();
    }

    private void initSize() {
        if (rotatBitmap == null) {

            // throw new NoBitMapError("Error,No bitmap in RotatView!");
            return;
        }
        width = rotatBitmap.getWidth();
        height = rotatBitmap.getHeight();

        maxwidth = Math.sqrt(width * width + height * height);
        
        o_x = o_y = (float)(maxwidth / 2);//确定圆心坐标
    }

    /**
     * 通过此方法来控制旋转度数，如果超过360，让它求余，防止，该值过大造成越界
     * 
     * @param added
     */
    private void addDegree(float added) {
        deta_degree += added;
        if (deta_degree > 360 || deta_degree < -360) {
            deta_degree = deta_degree % 360;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        Matrix matrix = new Matrix();
        // 设置转轴位置
        matrix.setTranslate((float)width / 2, (float)height / 2);

        // 开始转
        matrix.preRotate(deta_degree);
        // 转轴还原
        matrix.preTranslate(-(float)width / 2, -(float)height / 2);

        // 将位置送到view的中心
        matrix.postTranslate((float)(maxwidth - width) / 2, (float)(maxwidth - height) / 2);

        canvas.drawBitmap(rotatBitmap, matrix,paint);
        
        super.onDraw(canvas);
    }

    Paint paint=new Paint();
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 它的宽高不是图片的宽高，而是以宽高为直角的矩形的对角线的长度
        setMeasuredDimension((int)maxwidth, (int)maxwidth);

    }

    /**
     * 手指触屏的初始x的坐标
     */
    float down_x;

    /**
     * 手指触屏的初始y的坐标
     */
    float down_y;

    /**
     * 移动时的x的坐标
     */
    float target_x;

    /**
     * 移动时的y的坐标
     */
    float target_y;

    /**
     * 放手时的x的坐标
     */
    float up_x;

    /**
     * 放手时的y的坐标
     */
    float up_y;

    /**
     * 当前的弧度(以该 view 的中心为圆点)
     */
    float current_degree;

    /**
     * 放手时的弧度(以该 view 的中心为圆点)
     */
    float up_degree;

    /**
     * 当前圆盘所转的弧度(以该 view 的中心为圆点)
     */
    float deta_degree;

    /**
     * 最后一次手势滑过的时间
     */
    double lastMoveTime = 0;

    /**
     * 最小加速度（当手指放手是）
     */
    public static final float a_min = 0.001f;

    /**
     * 加速度增量
     */
    public static final float a_add = 0.001f;

    /**
     * 加速度
     */
    float a = a_min;

    /**
     * 最大加速度（当手指按住时）
     */
    public static final float a_max = a_min * 5;

    /**
     * 旋转速度(度/毫秒)
     */
    double speed = 0;

    /**
     * 速度计算器
     */
    VRecord vRecord;

    /**
     * 是否为顺时针旋转
     */
    boolean isClockWise;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (rotatBitmap == null) {

            throw new NoBitMapError("Error,No bitmap in RotatView!");
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                down_x = event.getX();
                down_y = event.getY();
                current_degree = detaDegree(o_x, o_y, down_x, down_y);
                vRecord.reset();
                // handler.sendEmptyMessage(stop);
                a = a_max;

                break;

            }
            case MotionEvent.ACTION_MOVE: {
                down_x = target_x = event.getX();
                down_y = target_y = event.getY();
                float degree = detaDegree(o_x, o_y, target_x, target_y);

                // 滑过的弧度增量
                float dete = degree - current_degree;
                // 如果小于-90度说明 它跨周了，需要特殊处理350->17,
                if (dete < -270) {
                    dete = dete + 360;

                    // 如果大于90度说明 它跨周了，需要特殊处理-350->-17,
                } else if (dete > 270) {
                    dete = dete - 360;
                }
                lastMoveTime = System.currentTimeMillis();
                vRecord.add(dete, lastMoveTime);
                addDegree(dete);
                current_degree = degree;
                postInvalidate();
                
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                a = a_min;

                double lastupTime = System.currentTimeMillis();
                double detaTime = lastupTime - lastMoveTime;
                up_x = event.getX();
                up_y = event.getY();
                up_degree = detaDegree(o_x, o_y, up_x, up_y);
                // 放手时的速度
                speed = speed + vRecord.getSpeed();
                if (speed > 0) {
                    speed = Math.min(VRecord.max_speed, speed);
                } else {
                    speed = Math.max(-VRecord.max_speed, speed);
                }
//                System.out.println("speed:" + speed);
                if (speed > 0) {
                    isClockWise = true;
                    // v = 1;
                } else {
                    isClockWise = false;
                    // v = -1;
                }
                currentTime = System.currentTimeMillis();
                handler.sendEmptyMessage(0);
                break;
            }
        }
        return true;
    }

    /**
     * 计算以(src_x,src_y)为坐标圆点，建立直角体系，求出(target_x,target_y)坐标与x轴的夹角
     * 主要是利用反正切函数的知识求出夹角
     * 
     * @param src_x
     * @param src_y
     * @param target_x
     * @param target_y
     * @return
     */
    float detaDegree(float src_x, float src_y, float target_x, float target_y) {

        float detaX = target_x - src_x;
        float detaY = target_y - src_y;
        double d;
        if (detaX != 0) {
            float tan = Math.abs(detaY / detaX);

            if (detaX > 0) {

                if (detaY >= 0) {
                    d = Math.atan(tan);

                } else {
                    d = 2 * Math.PI - Math.atan(tan);
                }

            } else {
                if (detaY >= 0) {

                    d = Math.PI - Math.atan(tan);
                } else {
                    d = Math.PI + Math.atan(tan);
                }
            }

        } else {
            if (detaY > 0) {
                d = Math.PI / 2;
            } else {

                d = -Math.PI / 2;
            }
        }

        return (float)((d * 180) / Math.PI);
    }

    /**
     * 一个异常，用来判断是否有rotatbitmap
     * 
     * @author sun.shine
     */
    static class NoBitMapError extends RuntimeException {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public NoBitMapError(String detailMessage) {
            super(detailMessage);
        }

    }

    /**
     * 速度计算器 原来是将最近的 弧度增量和时间点记录下来，然后<br>
     * 通过增量除以总时间求出平均值做为它的即时手势滑过的速度
     * 
     * @author sun.shine
     */
    static class VRecord {

        /**
         * 数组中的有效数字
         */
        int addCount;

        /**
         * 最大能装的数据空间
         */
        public static final int length = 15;

        /**
         * 二维数组，1.保存弧度增量.2.保存产生这个增量的时间点
         */
        double[][] record = new double[length][2];

        /**
         * 为二维数组装载数据<br>
         * 注：通过此方法，有个特点，能把最后的length组数据记录下来，length以外的会丢失
         * 
         * @param detadegree
         * @param time
         */
        public void add(double detadegree, double time) {

            for (int i = length - 1; i > 0; i--) {
                record[i][0] = record[i - 1][0];
                record[i][1] = record[i - 1][1];
            }
            record[0][0] = detadegree;
            record[0][1] = time;
            addCount++;

        }

        /**
         * 最大速度
         */
        public static final double max_speed = 8;

        /**
         * 通过数组里所装载的数据分析出即时速度<br>
         * 原理是：计算数组里的时间长度和增量的总数，然后求出每毫秒所走过的弧度<br>
         * 当然不能超过{@link VRecord#max_speed}
         * 
         * @return
         */
        public double getSpeed() {

            if (addCount == 0) {
                return 0;
            }
            int maxIndex = Math.min(addCount, length) - 1;

            if ((record[0][1] - record[maxIndex][1]) == 0) {
                return 0;
            }

            double detaTime = record[0][1] - record[maxIndex][1];
            double sumdegree = 0;
            for (int i = 0; i < length - 1; i++) {

                sumdegree += record[i][0];
                // System.out.println(record[i][0]);
            }

            // System.out.println("----------");
            // System.out.println(sumdegree);
            // System.out.println(detaTime);
            double result = sumdegree / detaTime;
            if (result > 0) {
                return Math.min(result, max_speed);
            } else {
                return Math.max(result, -max_speed);
            }
            // System.out.println("v=" + result);

        }

        /**
         * 重置
         */
        public void reset() {
            addCount = 0;
            for (int i = length - 1; i > 0; i--) {
                record[i][0] = 0;
                record[i][1] = 0;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(rotatBitmap!=null){
        rotatBitmap.recycle();
        rotatBitmap=null;
        }
    }
}
