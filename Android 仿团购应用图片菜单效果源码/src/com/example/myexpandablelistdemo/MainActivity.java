package com.example.myexpandablelistdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends Activity {
    Context context = this;
    GestureDetector gd;
    MyOnTouchListener listener;
    ViewFlipper child1;
    ImageView page1,page2,page0;
    RelativeLayout relativeLayout;//第一个子菜单
    final int TO_NEXT_PAGE=10000;
    final int TO_PREVIOUS_PAGE=10001;
    int currentPageIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //------------------------------设置可扩展列表----------------------------------------------------------
        ExpandableListView elist = new ExpandableListView(this);
        elist.setGroupIndicator(null);
        elist.setCacheColorHint(Color.TRANSPARENT);
        ArrayList<HashMap<String,View>> list = new ArrayList<HashMap<String,View>>();
        HashMap<String,View> menu1 = new HashMap<String, View>();
        HashMap<String,View> menu2 = new HashMap<String, View>();
        HashMap<String,View> menu3 = new HashMap<String, View>();
        
        ImageView groupView1 = new ImageView(this);
        ImageView groupView2 = new ImageView(this);
        ImageView groupView3 = new ImageView(this);
        
        relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.flipper_layout, null);
        child1 = (ViewFlipper)relativeLayout.findViewById(R.id.imgs_vf);
        page0 = (ImageView)relativeLayout.findViewById(R.id.pageIndicator0_iv);
        page1 = (ImageView)relativeLayout.findViewById(R.id.pageIndicator1_iv);
        page2 = (ImageView)relativeLayout.findViewById(R.id.pageIndicator2_iv);
        changePage(0);
        child1.setFocusable(false);
        ImageView child2 = new ImageView(this);
        ImageView child3 = new ImageView(this);
        
        int imgs[] = new int []{R.drawable.menu_child_lvyou,R.drawable.menu_child_meishi,R.drawable.menu_child_yule};
        for(int i :imgs) {
            ImageView mImageView = new ImageView(context);
            mImageView.setBackgroundResource(i);
           child1.addView(mImageView);
        }
        child1.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_flipper_right_left_in));
        child1.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_flipper_right_left_out));
        child1.setFlipInterval(2000);
        
        //绑定手势监听器
        gd = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            int from ;
            int to;
            boolean isAbleToChange = true;
            
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                    float velocityX, float velocityY)
            {
                Log.d("onFling", "---------------------");
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                    float distanceX, float distanceY)
            {
                Log.d("onScroll", "---------------------");
               
                int to = (int) e2.getX();
                // TODO Auto-generated method stub
                if(isAbleToChange&& distanceX<0 && (Math.abs(to-from)>100)) {
                    child1.setInAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_left_right_in));
                    child1.setOutAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_left_right_out));
                    child1.showPrevious();
                    changePage(TO_PREVIOUS_PAGE);
                    isAbleToChange=false;
                    Log.d("onScroll", "---------showPrevious---------");
                }else if(isAbleToChange&& distanceX>0 && (Math.abs(to-from)>100)){
                    child1.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_flipper_right_left_in));
                    child1.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_flipper_right_left_out));
                    child1.showNext();
                    changePage(TO_NEXT_PAGE);
                    isAbleToChange = false;
                    Log.d("onScroll", "---------showNext---------");
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onDown(MotionEvent e)
            {
                // TODO Auto-generated method stub
                from = (int) e.getX();
                isAbleToChange=true;
                Log.d("onDown", "---------------------");
                return super.onDown(e);
            }

            @Override
            public void onShowPress(MotionEvent e)
            {
                // TODO Auto-generated method stub
                Log.d("onShowPress", "---------------------");
                super.onShowPress(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e)
            {
                // TODO Auto-generated method stub
                Log.d("onSingleTapConfirmed", "---------------------");
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                // TODO Auto-generated method stub
                Log.d("onSingleTapUp", "---------------------");
                Toast.makeText(context, "第"+currentPageIndex+"张图片被点击", Toast.LENGTH_SHORT).show();
                return super.onSingleTapUp(e);
            }
            
        });
        child1.setLongClickable(true);
        child1.setClickable(true);
        listener = new MyOnTouchListener();
        child1.setOnTouchListener(listener);
   //     child1.startFlipping();
        
        groupView1.setImageResource(R.drawable.menu_title_lvyou);
        groupView2.setImageResource(R.drawable.menu_title_meishi);
        groupView3.setImageResource(R.drawable.menu_title_yule);
        child2.setImageResource(R.drawable.menu_child_meishi);
        child3.setImageResource(R.drawable.menu_child_yule);
        child2.setAdjustViewBounds(true);
        child2.setScaleType(ScaleType.FIT_XY);
        child3.setAdjustViewBounds(true);
        child3.setScaleType(ScaleType.FIT_XY);
        groupView1.setAdjustViewBounds(true);
        groupView1.setScaleType(ScaleType.FIT_XY);
        groupView2.setAdjustViewBounds(true);
        groupView2.setScaleType(ScaleType.FIT_XY);
        groupView3.setAdjustViewBounds(true);
        groupView3.setScaleType(ScaleType.FIT_XY);

        menu1.put("group", groupView1);
        menu2.put("group", groupView2);
        menu3.put("group", groupView3);
        menu1.put("child", relativeLayout);
        menu2.put("child", child2);
        menu3.put("child", child3);
      
       
        list.add(menu1);
        list.add(menu2);
        list.add(menu3);
        
        elist.setAdapter(new ImageExpandableListAdapter(this,list));
   //     GroupOpenListener groupOpenListener = new GroupOpenListener();
   //     elist.setOnGroupClickListener(groupOpenListener);
        //-------------------------------可扩展列表设置完-----------------------------------------------------------------
        
        setContentView(elist);
    }
    
    //菜单展开点击事件
    /*class GroupOpenListener implements ExpandableListView.OnGroupClickListener{

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                int groupPosition, long id)
        {
            // TODO Auto-generated method stub
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_top_bottom_in);
            if(groupPosition==0)
            relativeLayout.startAnimation(animation);
            return false;
        }
        
    }*/
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    class MyOnTouchListener implements OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
          
        //    mViewFlipper.stopFlipping();
            return gd.onTouchEvent(event);
        }
        
    }
    
    //改变页标
    void changePage(int pageIndex) {
        switch(pageIndex) {
            case TO_NEXT_PAGE :
                if(currentPageIndex<2) {
                    currentPageIndex++;
                }else {
                    currentPageIndex=0;
                }
                break;
            case TO_PREVIOUS_PAGE:
                if(currentPageIndex>0) {
                    currentPageIndex--;
                }else {
                    currentPageIndex=2;
                }
                break;
            case 0:
                currentPageIndex = 0;
                break;
            case 1:
                currentPageIndex = 1;
                break;
            case 2:
                currentPageIndex = 2;
                break;
        }
        
        switch(currentPageIndex) {
            case 0:
                page0.setImageResource(R.drawable.page_indicator_focused);
                page1.setImageResource(R.drawable.page_indicator_unfocused);
                page2.setImageResource(R.drawable.page_indicator_unfocused);
                break;
            case 1:
                page0.setImageResource(R.drawable.page_indicator_unfocused);
                page1.setImageResource(R.drawable.page_indicator_focused);
                page2.setImageResource(R.drawable.page_indicator_unfocused);
                break;
            case 2:
                page0.setImageResource(R.drawable.page_indicator_unfocused);
                page1.setImageResource(R.drawable.page_indicator_unfocused);
                page2.setImageResource(R.drawable.page_indicator_focused);
                break;
        }
    }
}
