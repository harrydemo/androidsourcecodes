package uk.co.jasonfry.android.apps.SwipeViewDemo;

import uk.co.jasonfry.android.apps.SwipeViewDemo.R;
import uk.co.jasonfry.android.tools.ui.PageControl;
import uk.co.jasonfry.android.tools.ui.SwipeView;
import uk.co.jasonfry.android.tools.ui.SwipeView.OnPageChangedListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SwipeViewDemo2 extends Activity 
{
	SwipeView mSwipeView;
	 
	int[] images;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo2);
        
        PageControl mPageControl = (PageControl) findViewById(R.id.page_control);
        mSwipeView = (SwipeView) findViewById(R.id.swipe_view);

        loadImages();
        
        for(int i=0; i<7;i++)
        {
        	mSwipeView.addView(new FrameLayout(this));
        }
        
        ImageView i0 = new ImageView(this);
        ImageView i1 = new ImageView(this);
        i0.setImageResource(images[0]);
        i1.setImageResource(images[1]);
        
        ((FrameLayout) mSwipeView.getChildContainer().getChildAt(0)).addView(i0);
        ((FrameLayout) mSwipeView.getChildContainer().getChildAt(1)).addView(i1);
        
        SwipeImageLoader mSwipeImageLoader = new SwipeImageLoader();
        
        mSwipeView.setOnPageChangedListener(mSwipeImageLoader);
        
        mSwipeView.setPageControl(mPageControl);
    }
    
    private class SwipeImageLoader implements OnPageChangedListener
    {

		public void onPageChanged(int oldPage, int newPage) 
		{
			if(newPage>oldPage)//going forwards
			{
				if(newPage != (mSwipeView.getPageCount()-1))//if at the end, don't load one page after the end
				{
					ImageView v = new ImageView(SwipeViewDemo2.this);
					v.setImageResource(images[newPage+1]);
					((FrameLayout) mSwipeView.getChildContainer().getChildAt(newPage+1)).addView(v);
				}
				if(oldPage!=0)//if at the beginning, don't destroy one before the beginning
				{
					((FrameLayout) mSwipeView.getChildContainer().getChildAt(oldPage-1)).removeAllViews();
				}
				
			}
			else //going backwards
			{
				if(newPage!=0)//if at the beginning, don't load one before the beginning
				{
					ImageView v = new ImageView(SwipeViewDemo2.this);
					v.setImageResource(images[newPage-1]);
					((FrameLayout) mSwipeView.getChildContainer().getChildAt(newPage-1)).addView(v);
				}
				if(oldPage != (mSwipeView.getPageCount()-1))//if at the end, don't destroy one page after the end
				{
					((FrameLayout) mSwipeView.getChildContainer().getChildAt(oldPage+1)).removeAllViews();
				}
			}
			
		}
    	
    }
    
    private void loadImages()
    {
    	//Not the most elegant way to do this, but it does enough for demo purposes...
    	
    	//The images are not actually being loaded into memory, but the resources 
    	//ids are being put in a format that can be dealt with easily
    	
    	images = new int[25];
    	images[0] = R.drawable.image001;
    	images[1] = R.drawable.image002;
    	images[2] = R.drawable.image003;
    	images[3] = R.drawable.image004;
    	images[4] = R.drawable.image005;
    	images[5] = R.drawable.image006;
    	images[6] = R.drawable.image007;
    	
    }
}