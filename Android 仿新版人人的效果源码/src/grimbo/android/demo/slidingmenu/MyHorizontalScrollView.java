package grimbo.android.demo.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;


//       ���������       HorizontalScrollView
public class MyHorizontalScrollView extends HorizontalScrollView {
	
    @Override
	protected void onScrollChanged(int w, int h, int oldw, int oldh) {
		super.onScrollChanged(w, h, oldw, oldh);
		System.out.println("DDDDDDDDDDDDDDDDDD="+w);
		
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyHorizontalScrollView(Context context) {
        super(context);
        init(context);
    }

    void init(Context context) {
        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
    }
    
    
 
    public void initViews(View[] children, int scrollToViewIdx, SizeCallback sizeCallback) {
    
        ViewGroup parent = (ViewGroup) getChildAt(0);
        for (int i = 0; i < children.length; i++) {
            children[i].setVisibility(View.INVISIBLE);
            parent.addView(children[i]);
        }

  
        OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener(parent, children, scrollToViewIdx, sizeCallback);
////        view�Ŀ�  �ߵļ�����   getViewTreeObserver()һ��view
//        ����һ����ͼ����ȫ�ֲ��ַ����ı������ͼ���е�ĳ����ͼ�Ŀ���״̬�����ı�ʱ����Ҫ���õĻص������Ľӿ���
        getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
      
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
       
        return false;
    }

 
    class MyOnGlobalLayoutListener implements OnGlobalLayoutListener {
        ViewGroup parent;
        View[] children;
        int scrollToViewIdx;
        int scrollToViewPos = 0;
        SizeCallback sizeCallback;

  
        public MyOnGlobalLayoutListener(ViewGroup parent, View[] children, int scrollToViewIdx, SizeCallback sizeCallback) {
            this.parent = parent;
            this.children = children;
            this.scrollToViewIdx = scrollToViewIdx;
            this.sizeCallback = sizeCallback;
        }

        @Override
        public void onGlobalLayout() {
            
            final HorizontalScrollView me = MyHorizontalScrollView.this;
            me.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            ��ÿؼ��Ŀ��   ���û�е���������� ����view�Ŀ����һ���� ��һ��view��������list 
            sizeCallback.onGlobalLayout();					//ȱ����仰���button֮��list��Ҫ��ʧ 
            parent.removeViewsInLayout(0, children.length);
            System.out.println("children.length=="+children.length );
            final int w = me.getMeasuredWidth();
            System.out.println("w=="+w );
            final int h = me.getMeasuredHeight();
            System.out.println("h== "+h );
            int[] dims = new int[2];
            scrollToViewPos = 0;
            for (int i = 0; i < children.length; i++) {
                sizeCallback.getViewSize(i, w, h, dims);         	//ȱ����仰list��ʧ
                System.out.println("i="+i+"h== "+h+"w="+w+"dims[0]="+dims[0]+"dims[1]"+dims[1] );
//                i=0�Ǵ���ͼƬ����ҳ��      i=1Ϊ����list��view
                
                children[i].setVisibility(View.VISIBLE);
                parent.addView(children[i], dims[0], dims[1]);
//                if (i < scrollToViewIdx) {
//                    scrollToViewPos += dims[0];
//                }
            }

            // For some reason we need to post this action, rather than call immediately.
            // If we try immediately, it will not scroll.
//            new Handler().post(new Runnable() {
//                @Override
//                public void run() {
//                    me.scrollBy(scrollToViewPos, 0);
//                }
//            });
        }
    }

}
