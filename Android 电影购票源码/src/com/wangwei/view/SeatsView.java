package com.wangwei.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author 作者 E-mail:
 * @version 创建时间：Aug 10, 2010 4:23:23 PM 类说明
 */
public class SeatsView extends View {
    // 左边座位总数 左右相等
    private int leftSeatsNum = 25;
    // 左边一排几个
    private int leftColNum = 5;
    // 中间座位总数
    private int centerSeatsNum = 45;
    // 共几排
    private int colNum = leftSeatsNum / leftColNum;
    // 中间第一排几个
    private int firstCenterColNum = centerSeatsNum / colNum;
    // 座位大小
    public static int width = 14;
    public static int heigh = 14;
    public static int textSize = 10;
    public static float screenCenter_x;
    public static float screenCenter_y;
    // 屏幕大小
    private int windowWidth = 320;
    private int windowHeight = 480;
    // // 触摸时的坐标
    // private float t_x;
    // private float t_y;
    public List<float[]> pointList;
    public List<float[]> pointXYList;

    private Paint paint;
    public Random random = new Random();
    public int x_num = 180;
    int[] temp = new int[] { 0, 0 };

    private int[][] seats = new int[colNum][firstCenterColNum + 2 * leftColNum];

    public SeatsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SeatsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        screenCenter_x = windowWidth / 2;
        // 中心往上
        screenCenter_y = windowHeight / 2 - 200;
        Log.d("screenCenter_x", screenCenter_x + "");
        Log.d("screenCenter_y", screenCenter_y + "");

    }

    public SeatsView(Context context) {
        super(context);

    }

    public SeatsView(Context context, int viewWidth, int viewHeight) {
        super(context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPingMu(canvas);
        drawSeats(canvas);
    }

    private void drawPingMu(Canvas canvas) {
        Path path = new Path();
        path.moveTo(screenCenter_x, screenCenter_y-10);
        path.lineTo(screenCenter_x - 5 * width, screenCenter_y-10);
        path.lineTo(screenCenter_x - 5 * width, screenCenter_y-10 - heigh);
        path.lineTo(screenCenter_x + 5 * width, screenCenter_y-10 - heigh);
        path.lineTo(screenCenter_x + 5 * width, screenCenter_y-10);
        path.lineTo(screenCenter_x, screenCenter_y-10);
        paint.setColor(Color.GREEN);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawPath(path, paint);
    }

    private void drawJUXING(Canvas canvas, float x, float y, float width,
            float heigh, boolean isGreen) {
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x + width, y);
        path.lineTo(x + width, y + heigh);
        path.lineTo(x, y + heigh);
        path.lineTo(x, y);
        if (isGreen) {
            paint.setColor(Color.GREEN);
        } else {
            paint.setColor(Color.RED);
        }
        paint.setStyle(Style.FILL_AND_STROKE);
        canvas.drawPath(path, paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawPath(path, paint);

    }

    private void drawSeats(Canvas canvas) {
        boolean isOuShu = false;
        if (firstCenterColNum % 2 == 0) {
            isOuShu = true;
        }
        pointList = new ArrayList<float[]>();
        pointXYList = new ArrayList<float[]>();
        // 中间要留走路位置，要移动坐标右移
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[0].length; j++) {
                float[] point = { i, j };

                float leftTop_x = 0;
                float leftTop_y = 0;
                if (isOuShu) {
                    if (j >= 0 && j < leftColNum) {
                        leftTop_x = screenCenter_x - width
                                * (seats[0].length / 2 - j + 1);
                    } else if (j >= leftColNum
                            && j < leftColNum + firstCenterColNum) {
                        leftTop_x = screenCenter_x - width
                                * (seats[0].length / 2 - j);
                    } else if (j >= leftColNum + firstCenterColNum) {
                        leftTop_x = screenCenter_x - width
                                * (seats[0].length / 2 - j - 1) + width / 2;
                    }
                    leftTop_y = screenCenter_y + heigh * i;
                    if (leftTop_x > x_num) {
                        drawJUXING(canvas, leftTop_x, leftTop_y, width, heigh,
                                true);
                    } else {
                        drawJUXING(canvas, leftTop_x, leftTop_y, width, heigh,
                                false);
                    }

                } else {
                    if (j >= 0 && j < leftColNum) {
                        leftTop_x = screenCenter_x - width
                                * (seats[0].length / 2 - j + 1) - width / 2;
                    } else if (j >= leftColNum
                            && j < leftColNum + firstCenterColNum) {
                        leftTop_x = screenCenter_x - width
                                * (seats[0].length / 2 - j) - width / 2;
                    } else if (j >= leftColNum + firstCenterColNum) {
                        leftTop_x = screenCenter_x - width
                                * (seats[0].length / 2 - j) + width / 2;

                    }
                    leftTop_y = screenCenter_y + heigh * i;
                    if (leftTop_x > x_num) {
                        drawJUXING(canvas, leftTop_x, leftTop_y, width, heigh,
                                true);
                    } else {
                        drawJUXING(canvas, leftTop_x, leftTop_y, width, heigh,
                                false);
                    }
                    paint.setColor(Color.BLUE);
                    paint.setTextSize(textSize);
                    paint.setStrokeWidth(0);

                    if (j == leftColNum) {
                        canvas.drawText((i + 1) + "", screenCenter_x - width
                                * (seats[0].length / 2 - j + 1) - width * 20
                                / 100, leftTop_y + heigh * 90 / 100, paint);
                    } else if (j == leftColNum + firstCenterColNum) {
                        canvas
                                .drawText((i + 1) + "", screenCenter_x - width
                                        * (seats[0].length / 2 - j) - width / 2
                                        + width * 20 / 100, leftTop_y + heigh
                                        * 90 / 100, paint);
                    }
                    float[] xyPoint = { leftTop_x, leftTop_y };
                    paint.setColor(Color.rgb(0, 0, 0));
                    canvas.drawText((j + 1) + "", leftTop_x + width * 10 / 100,
                            leftTop_y + heigh * 90 / 100, paint);
                    pointList.add(point);
                    pointXYList.add(xyPoint);

                }
            }
        }
    }

    /**
     * 工具方法
     * 
     * @param x
     *            数组中的横坐标
     * @param y
     *            数组中的纵坐标
     * @return 将图标在数组中的坐标转成在屏幕上的真实坐标
     */
    public Point indextoScreen(int x, int y) {
        Point point = null;
        for (int i = 0; i < pointList.size(); i++) {
            if (x == pointList.get(i)[0] && y == pointList.get(i)[1]) {
                point = new Point();
                point.x = (int) pointXYList.get(i)[0];
                point.y = (int) pointXYList.get(i)[1];
                return point;
            }
        }
        return null;

    }

    /**
     * 工具方法
     * 
     * @param t_x
     *            屏幕中的横坐标
     * @param t_y
     *            屏幕中的纵坐标
     * @return 将图标在屏幕中的坐标转成在数组上的虚拟坐标
     */
    public Point screenToindex(float t_x, float t_y) {
        Point point = null;
        for (int i = 0; i < pointXYList.size(); i++) {
            if (t_x > pointXYList.get(i)[0]
                    && t_x < (pointXYList.get(i)[0] + width)
                    && t_y > pointXYList.get(i)[1]
                    && t_y < (pointXYList.get(i)[1] + width)) {
                point = new Point();
                point.x = (int) pointList.get(i)[0];
                point.y = (int) pointList.get(i)[1];
                return point;
            }
        }
        return null;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Log.d("x1:y1 ", event.getX() + ":" + event.getY());
        return super.onTouchEvent(event);
    }
}
