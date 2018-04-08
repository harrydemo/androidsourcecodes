
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
     * ԭ������x
     */
    float o_x;

    /**
     * ԭ������y
     */
    float o_y;

    /**
     * ������Ե�handler
     */
    Handler handler;

    /**
     * handler������Ϣ�ļ��
     */
    int delayedTime = 20;

    /**
     * ��Ϣ�źţ������ı�ʶ
     */
    static final int play = 0;

    /**
     * ��Ϣ�źţ�ֹͣ�����ı�ʶ
     */
    static final int stop = 1;

    /**
     * �ϴμ�¼��ʱ��,����һ��ʱ�����߹��Ļ��ȡ������ٶ�.
     */
    double currentTime = 0;

    /**
     * ͼƬ�Ŀ��
     */
    int width;

    /**
     * ͼƬ�ĸ߶�
     */
    int height;

    /**
     * view����ʵ�����߶�:��Ϊ����ת���������view�������Σ�����ֵ��ͼƬ�ĶԽ��߳���
     */
    double maxwidth;

    /**
     * ��ת��ͼƬ
     */
    Bitmap rotatBitmap;

    public RotatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    /**
     * ��ʼ��handler���ٶȼ�����
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
        // Ĭ������һ��ͼƬ��

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
        
        o_x = o_y = (float)(maxwidth / 2);//ȷ��Բ������
    }

    /**
     * ͨ���˷�����������ת�������������360���������࣬��ֹ����ֵ�������Խ��
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
        // ����ת��λ��
        matrix.setTranslate((float)width / 2, (float)height / 2);

        // ��ʼת
        matrix.preRotate(deta_degree);
        // ת�ỹԭ
        matrix.preTranslate(-(float)width / 2, -(float)height / 2);

        // ��λ���͵�view������
        matrix.postTranslate((float)(maxwidth - width) / 2, (float)(maxwidth - height) / 2);

        canvas.drawBitmap(rotatBitmap, matrix,paint);
        
        super.onDraw(canvas);
    }

    Paint paint=new Paint();
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // ���Ŀ�߲���ͼƬ�Ŀ�ߣ������Կ��Ϊֱ�ǵľ��εĶԽ��ߵĳ���
        setMeasuredDimension((int)maxwidth, (int)maxwidth);

    }

    /**
     * ��ָ�����ĳ�ʼx������
     */
    float down_x;

    /**
     * ��ָ�����ĳ�ʼy������
     */
    float down_y;

    /**
     * �ƶ�ʱ��x������
     */
    float target_x;

    /**
     * �ƶ�ʱ��y������
     */
    float target_y;

    /**
     * ����ʱ��x������
     */
    float up_x;

    /**
     * ����ʱ��y������
     */
    float up_y;

    /**
     * ��ǰ�Ļ���(�Ը� view ������ΪԲ��)
     */
    float current_degree;

    /**
     * ����ʱ�Ļ���(�Ը� view ������ΪԲ��)
     */
    float up_degree;

    /**
     * ��ǰԲ����ת�Ļ���(�Ը� view ������ΪԲ��)
     */
    float deta_degree;

    /**
     * ���һ�����ƻ�����ʱ��
     */
    double lastMoveTime = 0;

    /**
     * ��С���ٶȣ�����ָ�����ǣ�
     */
    public static final float a_min = 0.001f;

    /**
     * ���ٶ�����
     */
    public static final float a_add = 0.001f;

    /**
     * ���ٶ�
     */
    float a = a_min;

    /**
     * �����ٶȣ�����ָ��סʱ��
     */
    public static final float a_max = a_min * 5;

    /**
     * ��ת�ٶ�(��/����)
     */
    double speed = 0;

    /**
     * �ٶȼ�����
     */
    VRecord vRecord;

    /**
     * �Ƿ�Ϊ˳ʱ����ת
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

                // �����Ļ�������
                float dete = degree - current_degree;
                // ���С��-90��˵�� �������ˣ���Ҫ���⴦��350->17,
                if (dete < -270) {
                    dete = dete + 360;

                    // �������90��˵�� �������ˣ���Ҫ���⴦��-350->-17,
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
                // ����ʱ���ٶ�
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
     * ������(src_x,src_y)Ϊ����Բ�㣬����ֱ����ϵ�����(target_x,target_y)������x��ļн�
     * ��Ҫ�����÷����к�����֪ʶ����н�
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
     * һ���쳣�������ж��Ƿ���rotatbitmap
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
     * �ٶȼ����� ԭ���ǽ������ ����������ʱ����¼������Ȼ��<br>
     * ͨ������������ʱ�����ƽ��ֵ��Ϊ���ļ�ʱ���ƻ������ٶ�
     * 
     * @author sun.shine
     */
    static class VRecord {

        /**
         * �����е���Ч����
         */
        int addCount;

        /**
         * �����װ�����ݿռ�
         */
        public static final int length = 15;

        /**
         * ��ά���飬1.���满������.2.����������������ʱ���
         */
        double[][] record = new double[length][2];

        /**
         * Ϊ��ά����װ������<br>
         * ע��ͨ���˷������и��ص㣬�ܰ�����length�����ݼ�¼������length����Ļᶪʧ
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
         * ����ٶ�
         */
        public static final double max_speed = 8;

        /**
         * ͨ����������װ�ص����ݷ�������ʱ�ٶ�<br>
         * ԭ���ǣ������������ʱ�䳤�Ⱥ�������������Ȼ�����ÿ�������߹��Ļ���<br>
         * ��Ȼ���ܳ���{@link VRecord#max_speed}
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
         * ����
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
