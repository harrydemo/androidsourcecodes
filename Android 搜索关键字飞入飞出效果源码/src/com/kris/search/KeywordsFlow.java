package com.kris.search;

import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

public class KeywordsFlow extends FrameLayout implements OnGlobalLayoutListener{
	public static final int IDX_X = 0;  
    public static final int IDX_Y = 1;  
    public static final int IDX_TXT_LENGTH = 2;  
    public static final int IDX_DIS_Y = 3;  
    /** �������ڵĶ����� */  
    public static final int ANIMATION_IN = 1;  
    /** ��������Ķ����� */  
    public static final int ANIMATION_OUT = 2;  
    /** λ�ƶ������ͣ�����Χ�ƶ�������㡣 */  
    public static final int OUTSIDE_TO_LOCATION = 1;  
    /** λ�ƶ������ͣ���������ƶ�����Χ�� */  
    public static final int LOCATION_TO_OUTSIDE = 2;  
    /** λ�ƶ������ͣ������ĵ��ƶ�������㡣 */  
    public static final int CENTER_TO_LOCATION = 3;  
    /** λ�ƶ������ͣ���������ƶ������ĵ㡣 */  
    public static final int LOCATION_TO_CENTER = 4;  
    public static final long ANIM_DURATION = 800l;  
    public static final int MAX = 10;  
    public static final int TEXT_SIZE_MAX = 25;  
    public static final int TEXT_SIZE_MIN = 15;  
    private OnClickListener itemClickListener;  
    private static Interpolator interpolator;  
    private static AlphaAnimation animAlpha2Opaque;  
    private static AlphaAnimation animAlpha2Transparent;  
    private static ScaleAnimation animScaleLarge2Normal, animScaleNormal2Large, animScaleZero2Normal,  
            animScaleNormal2Zero;  
    /** �洢��ʾ�Ĺؼ��֡� */  
    private Vector<String> vecKeywords;  
    private int width, height;  
    /** 
     * go2Show()�б���ֵΪtrue����ʶ������Ա�����俪ʼ������ʾ��<br/> 
     * ����ʶ�������Ƿ�ֹ�����keywrodsδ��ɵĹ����л�ȡ��width��height����ǰ����������<br/> 
     * ��show()�������䱻��ֵΪfalse��<br/> 
     * �����ܹ�������ʾ����һ��Ҫ������width �� height��Ϊ0��<br/> 
     */  
    private boolean enableShow;  
    private Random random;  
    /** 
     * @see ANIMATION_IN 
     * @see ANIMATION_OUT 
     * @see OUTSIDE_TO_LOCATION 
     * @see LOCATION_TO_OUTSIDE 
     * @see LOCATION_TO_CENTER 
     * @see CENTER_TO_LOCATION 
     * */  
    private int txtAnimInType, txtAnimOutType;  
    /** ���һ������������ʾ��ʱ�䡣 */  
    private long lastStartAnimationTime;  
    /** ��������ʱ�䡣 */  
    private long animDuration;  
  
    public KeywordsFlow(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init();  
    }  
  
    public KeywordsFlow(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init();  
    }  
  
    public KeywordsFlow(Context context) {  
        super(context);  
        init();  
    }  
  
    private void init() {  
        lastStartAnimationTime = 0l;  
        animDuration = ANIM_DURATION;  
        random = new Random();  
        vecKeywords = new Vector<String>(MAX);  
        getViewTreeObserver().addOnGlobalLayoutListener(this);  
        interpolator = AnimationUtils.loadInterpolator(getContext(), android.R.anim.decelerate_interpolator);  
        animAlpha2Opaque = new AlphaAnimation(0.0f, 1.0f);  
        animAlpha2Transparent = new AlphaAnimation(1.0f, 0.0f);  
        animScaleLarge2Normal = new ScaleAnimation(2, 1, 2, 1);  
        animScaleNormal2Large = new ScaleAnimation(1, 2, 1, 2);  
        animScaleZero2Normal = new ScaleAnimation(0, 1, 0, 1);  
        animScaleNormal2Zero = new ScaleAnimation(1, 0, 1, 0);  
    }  
  
    public long getDuration() {  
        return animDuration;  
    }  
  
    public void setDuration(long duration) {  
        animDuration = duration;  
    }  
  
    public boolean feedKeyword(String keyword) {  
        boolean result = false;  
        if (vecKeywords.size() < MAX) {  
            result = vecKeywords.add(keyword);  
        }  
        return result;  
    }  
  
    /** 
     * ��ʼ������ʾ��<br/> 
     * ֮ǰ�Ѿ����ڵ�TextView������ʾ�˳�������<br/> 
     *  
     * @return ������ʾ��������true����֮Ϊfalse������falseԭ�����£�<br/> 
     *         1.ʱ���ϲ�������lastStartAnimationTime����Լ��<br/> 
     *         2.δ��ȡ��width��height��ֵ��<br/> 
     */  
    public boolean go2Show(int animType) {  
        if (System.currentTimeMillis() - lastStartAnimationTime > animDuration) {  
            enableShow = true;  
            if (animType == ANIMATION_IN) {
                txtAnimInType = OUTSIDE_TO_LOCATION;  
                txtAnimOutType = LOCATION_TO_CENTER;  
            } else if (animType == ANIMATION_OUT) {  
                txtAnimInType = CENTER_TO_LOCATION;  
                txtAnimOutType = LOCATION_TO_OUTSIDE;  
            }  
            disapper();  
            boolean result = show();  
            return result;  
        }  
        return false;  
    }  
  
    private void disapper() {  
        int size = getChildCount();  
        for (int i = size - 1; i >= 0; i--) {  
            final TextView txt = (TextView) getChildAt(i);  
            if (txt.getVisibility() == View.GONE) {  
                removeView(txt);  
                continue;  
            }  
            FrameLayout.LayoutParams layParams = (LayoutParams) txt.getLayoutParams();  
            // Log.d("ANDROID_LAB", txt.getText() + " leftM=" +  
            // layParams.leftMargin + " topM=" + layParams.topMargin  
            // + " width=" + txt.getWidth());  
            int[] xy = new int[] { layParams.leftMargin, layParams.topMargin, txt.getWidth() };  
            AnimationSet animSet = getAnimationSet(xy, (width >> 1), (height >> 1), txtAnimOutType);  
            txt.startAnimation(animSet);  
            animSet.setAnimationListener(new AnimationListener() {  
                public void onAnimationStart(Animation animation) {  
                }  
  
                public void onAnimationRepeat(Animation animation) {  
                }  
  
                public void onAnimationEnd(Animation animation) {  
                    txt.setOnClickListener(null);  
                    txt.setClickable(false);  
                    txt.setVisibility(View.GONE);  
                }  
            });  
        }  
    }  
  
    private boolean show() {  
        if (width > 0 && height > 0 && vecKeywords != null && vecKeywords.size() > 0 && enableShow) {  
            enableShow = false;  
            lastStartAnimationTime = System.currentTimeMillis(); 
            //�ҵ����ĵ�
            int xCenter = width >> 1, yCenter = height >> 1;  
            //�ؼ��ֵĸ�����
            int size = vecKeywords.size();  
            int xItem = width / size, yItem = height / size;
             Log.d("ANDROID_LAB", "--------------------------width=" + width +  
             " height=" + height + "  xItem=" + xItem  
             + " yItem=" + yItem + "---------------------------");  
            LinkedList<Integer> listX = new LinkedList<Integer>(), listY = new LinkedList<Integer>();  
            for (int i = 0; i < size; i++) {  
                // ׼�������ѡ�����ֱ��Ӧx/y��λ��  
                listX.add(i * xItem);
                listY.add(i * yItem + (yItem >> 2));
                Log.e("Search", "ListX:"+(i * xItem)+"#listY:"+(i * yItem + (yItem >> 2)));
            }  
            // TextView[] txtArr = new TextView[size];  
            LinkedList<TextView> listTxtTop = new LinkedList<TextView>();  
            LinkedList<TextView> listTxtBottom = new LinkedList<TextView>();  
            for (int i = 0; i < size; i++) {  
                String keyword = vecKeywords.get(i);  
                // �����ɫ  
                int ranColor = 0xff000000 | random.nextInt(0x0077ffff);  
                // ���λ�ã���ֵ  
                int xy[] = randomXY(random, listX, listY, xItem);  
                // ��������С  
                int txtSize = TEXT_SIZE_MIN + random.nextInt(TEXT_SIZE_MAX - TEXT_SIZE_MIN + 1);  
                // ʵ����TextView  
                final TextView txt = new TextView(getContext());  
                txt.setOnClickListener(itemClickListener);  
                txt.setText(keyword);  
                txt.setTextColor(ranColor);  
                txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);  
                txt.setShadowLayer(1, 1, 1, 0xdd696969);  
                txt.setGravity(Gravity.CENTER);  
                
//                txt.setBackgroundColor(Color.RED);
                // ��ȡ�ı�����  
                Paint paint = txt.getPaint();  
                int strWidth = (int) Math.ceil(paint.measureText(keyword));  
                xy[IDX_TXT_LENGTH] = strWidth;  
                // ��һ������:����x����  
                if (xy[IDX_X] + strWidth > width - (xItem >> 1)) {  
                    int baseX = width - strWidth;  
                    // �����ı��ұ�Եһ���ĸ���  
                    xy[IDX_X] = baseX - xItem + random.nextInt(xItem >> 1);  
                } else if (xy[IDX_X] == 0) {  
                    // �����ı����Եһ���ĸ���  
                    xy[IDX_X] = Math.max(random.nextInt(xItem), xItem / 3);  
                }  
                xy[IDX_DIS_Y] = Math.abs(xy[IDX_Y] - yCenter);  
                txt.setTag(xy);  
                if (xy[IDX_Y] > yCenter) {  
                    listTxtBottom.add(txt);  
                } else {  
                    listTxtTop.add(txt);  
                }  
            }  
            attach2Screen(listTxtTop, xCenter, yCenter, yItem);  
            attach2Screen(listTxtBottom, xCenter, yCenter, yItem);  
            return true;  
        }  
        return false;  
    }  
  
    /** ����TextView��Y���꽫������ӵ������ϡ� */  
    private void attach2Screen(LinkedList<TextView> listTxt, int xCenter, int yCenter, int yItem) {  
        int size = listTxt.size();  
        sortXYList(listTxt, size);  
        for (int i = 0; i < size; i++) {  
            TextView txt = listTxt.get(i);  
            int[] iXY = (int[]) txt.getTag();  
            // Log.d("ANDROID_LAB", "fix[  " + txt.getText() + "  ] x:" +  
            // iXY[IDX_X] + " y:" + iXY[IDX_Y] + " r2="  
            // + iXY[IDX_DIS_Y]);  
            // �ڶ�������:����y����  
            int yDistance = iXY[IDX_Y] - yCenter;  
            // ����������ĵ�ģ���ֵ�������yItem<br/>  
            // ���ڿ���һ·�½������ĵ�ģ����ֵҲ����Ӧ�����Ĵ�С<br/>  
            int yMove = Math.abs(yDistance);  
            inner: for (int k = i - 1; k >= 0; k--) {  
                int[] kXY = (int[]) listTxt.get(k).getTag();  
                int startX = kXY[IDX_X];  
                int endX = startX + kXY[IDX_TXT_LENGTH];  
                // y�������ĵ�Ϊ�ָ��ߣ���ͬһ��  
                if (yDistance * (kXY[IDX_Y] - yCenter) > 0) {  
                    // Log.d("ANDROID_LAB", "compare:" +  
                    // listTxt.get(k).getText());  
                    if (isXMixed(startX, endX, iXY[IDX_X], iXY[IDX_X] + iXY[IDX_TXT_LENGTH])) {  
                        int tmpMove = Math.abs(iXY[IDX_Y] - kXY[IDX_Y]);  
                        if (tmpMove > yItem) {  
                            yMove = tmpMove;  
                        } else if (yMove > 0) {  
                            // ȡ��Ĭ��ֵ��  
                            yMove = 0;  
                        }  
                        // Log.d("ANDROID_LAB", "break");  
                        break inner;  
                    }  
                }  
            }  
            // Log.d("ANDROID_LAB", txt.getText() + " yMove=" + yMove);  
            if (yMove > yItem) {  
                int maxMove = yMove - yItem;  
                int randomMove = random.nextInt(maxMove);  
                int realMove = Math.max(randomMove, maxMove >> 1) * yDistance / Math.abs(yDistance);  
                iXY[IDX_Y] = iXY[IDX_Y] - realMove;  
                iXY[IDX_DIS_Y] = Math.abs(iXY[IDX_Y] - yCenter);  
                // �Ѿ�������ǰi����Ҫ�ٴ�����  
                sortXYList(listTxt, i + 1);  
            }  
            FrameLayout.LayoutParams layParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,  
                    FrameLayout.LayoutParams.WRAP_CONTENT);  
            layParams.gravity = Gravity.LEFT | Gravity.TOP;  
            layParams.leftMargin = iXY[IDX_X];  
            layParams.topMargin = iXY[IDX_Y];  
            addView(txt, layParams);  
            // ����  
            AnimationSet animSet = getAnimationSet(iXY, xCenter, yCenter, txtAnimInType);  
            txt.startAnimation(animSet);  
        }  
    }  
  
    public AnimationSet getAnimationSet(int[] xy, int xCenter, int yCenter, int type) {  
        AnimationSet animSet = new AnimationSet(true);  
        animSet.setInterpolator(interpolator);  
        if (type == OUTSIDE_TO_LOCATION) {  
            animSet.addAnimation(animAlpha2Opaque);  
            animSet.addAnimation(animScaleLarge2Normal);  
            TranslateAnimation translate = new TranslateAnimation(  
                    (xy[IDX_X] + (xy[IDX_TXT_LENGTH] >> 1) - xCenter) << 1, 0, (xy[IDX_Y] - yCenter) << 1, 0);  
            animSet.addAnimation(translate);  
        } else if (type == LOCATION_TO_OUTSIDE) {  
            animSet.addAnimation(animAlpha2Transparent);  
            animSet.addAnimation(animScaleNormal2Large);  
            TranslateAnimation translate = new TranslateAnimation(0,  
                    (xy[IDX_X] + (xy[IDX_TXT_LENGTH] >> 1) - xCenter) << 1, 0, (xy[IDX_Y] - yCenter) << 1);  
            animSet.addAnimation(translate);  
        } else if (type == LOCATION_TO_CENTER) {  
            animSet.addAnimation(animAlpha2Transparent);  
            animSet.addAnimation(animScaleNormal2Zero);  
            TranslateAnimation translate = new TranslateAnimation(0, (-xy[IDX_X] + xCenter), 0, (-xy[IDX_Y] + yCenter));  
            animSet.addAnimation(translate);  
        } else if (type == CENTER_TO_LOCATION) {  
            animSet.addAnimation(animAlpha2Opaque);  
            animSet.addAnimation(animScaleZero2Normal);  
            TranslateAnimation translate = new TranslateAnimation((-xy[IDX_X] + xCenter), 0, (-xy[IDX_Y] + yCenter), 0);  
            animSet.addAnimation(translate);  
        }  
        animSet.setDuration(animDuration);  
        return animSet;  
    }  
  
    /** 
     * ���������ĵ�ľ����ɽ���Զ����ð������ 
     *  
     * @param endIdx 
     *            ��ʼλ�á� 
     * @param txtArr 
     *            ����������顣 
     *  
     */  
    private void sortXYList(LinkedList<TextView> listTxt, int endIdx) {  
        for (int i = 0; i < endIdx; i++) {  
            for (int k = i + 1; k < endIdx; k++) {  
                if (((int[]) listTxt.get(k).getTag())[IDX_DIS_Y] < ((int[]) listTxt.get(i).getTag())[IDX_DIS_Y]) {  
                    TextView iTmp = listTxt.get(i);  
                    TextView kTmp = listTxt.get(k);  
                    listTxt.set(i, kTmp);  
                    listTxt.set(k, iTmp);  
                }  
            }  
        }  
    }  
  
    /** A�߶���B�߶��������ֱ����X��ӳ�����Ƿ��н����� */  
    private boolean isXMixed(int startA, int endA, int startB, int endB) {  
        boolean result = false;  
        if (startB >= startA && startB <= endA) {  
            result = true;  
        } else if (endB >= startA && endB <= endA) {  
            result = true;  
        } else if (startA >= startB && startA <= endB) {  
            result = true;  
        } else if (endA >= startB && endA <= endB) {  
            result = true;  
        }  
        return result;  
    }  
  
    private int[] randomXY(Random ran, LinkedList<Integer> listX, LinkedList<Integer> listY, int xItem) {  
        int[] arr = new int[4];  
        arr[IDX_X] = listX.remove(ran.nextInt(listX.size()));  
        arr[IDX_Y] = listY.remove(ran.nextInt(listY.size()));  
        return arr;  
    }  
  
    public void onGlobalLayout() {  
        int tmpW = getWidth();  
        int tmpH = getHeight();  
        if (width != tmpW || height != tmpH) {  
            width = tmpW;  
            height = tmpH;  
            show();  
        }  
    }  
  
    public Vector<String> getKeywords() {  
        return vecKeywords;  
    }  
  
    public void rubKeywords() {  
        vecKeywords.clear();  
    }  
  
    /** ֱ��������е�TextView�������֮ǰ������ʾ������ */  
    public void rubAllViews() {  
        removeAllViews();  
    }  
  
    public void setOnItemClickListener(OnClickListener listener) {  
        itemClickListener = listener;  
    }  
  
    // public void onDraw(Canvas canvas) {  
    // super.onDraw(canvas);  
    // Paint p = new Paint();  
    // p.setColor(Color.BLACK);  
    // canvas.drawCircle((width >> 1) - 2, (height >> 1) - 2, 4, p);  
    // p.setColor(Color.RED);  
    // }  
}
